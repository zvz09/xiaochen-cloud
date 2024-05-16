package com.zvz09.xiaochen.common.log.aop;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zvz09.xiaochen.common.core.annotation.BizNo;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.core.util.DateUtils;
import com.zvz09.xiaochen.common.log.domain.entity.OperationLog;
import com.zvz09.xiaochen.common.log.service.LogRabbitMqService;
import com.zvz09.xiaochen.common.log.util.ContextUtil;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import static com.zvz09.xiaochen.common.core.constant.CommonConstant.TRACE_ID;

/**
 * 操作日志记录处理AOP对象。
 *
 * @author springbob
 * @date 2022-09-15
 */
@Aspect
@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspect {

    @Value("${spring.application.name}")
    private String serviceName;

    private final ApplicationContext applicationContext;
    private final LogRabbitMqService logRabbitMqService;
    private final Searcher searcher;

    /**
     * 错误信息、请求参数和应答结果字符串的最大长度。
     */
    private static final int MAX_LENGTH = 5000;

    /**
     * 需要被SpEl解析的模板前缀和后缀 { expression  }
     */
    public static final TemplateParserContext TEMPLATE_PARSER_CONTEXT = new TemplateParserContext("{", "}");


    /**
     * SpEL解析器
     */
    public static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 所有controller方法。
     */
    @Pointcut("execution(public * com.zvz09.xiaochen..controller..*(..))")
    public void operationLogPointCut() {
        // 空注释，避免sonar警告
    }

    @Around("operationLogPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 计时。
        long start = System.currentTimeMillis();
        HttpServletRequest request = ContextUtil.getHttpRequest();
        HttpServletResponse response = ContextUtil.getHttpResponse();

        String[] parameterNames = this.getParameterNames(joinPoint);
        Object[] args = joinPoint.getArgs();
        JSONObject jsonArgs = new JSONObject();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (this.isNormalArgs(arg)) {
                String parameterName = parameterNames[i];
                jsonArgs.put(parameterName, arg);
            }
        }
        String params = jsonArgs.toJSONString();

        OperationLog operationLog = this.buildSysOperationLog(joinPoint, params);;

        Object result = null;
        log.info("开始请求，url={}, reqData={}", request.getRequestURI(), params);
        try {
            // 调用原来的方法
            result = joinPoint.proceed();
            String respData = result == null ? "null" : JSON.toJSONString(result);
            Long elapse = System.currentTimeMillis() - start;
            this.operationLogPostProcess(respData, operationLog, result);
            if (elapse > 30000) {
                log.warn("耗时较长的请求完成警告, url={}，elapse={}ms reqData={} respData={}",
                        request.getRequestURI(), elapse, params, respData);
            }
            log.info("请求完成, url={}，elapse={}ms, respData={}", request.getRequestURI(), elapse, respData);
            operationLog.setSuccess(true);
        } catch (Throwable e) {
            Long elapse = System.currentTimeMillis() - start;
            log.warn("请求处理异常, url={}，elapse={}ms", request.getRequestURI(), elapse);
            operationLog.setSuccess(false);
            operationLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, MAX_LENGTH));
            throw e;
        } finally {
            operationLog.setElapse(System.currentTimeMillis() - start);
            operationLog.setOperationTimeEnd(DateUtils.convertToUtc(new Date()));
            logRabbitMqService.sendLog(operationLog);
        }
        return result;
    }

    private OperationLog buildSysOperationLog(
            ProceedingJoinPoint joinPoint,
            String params){
        HttpServletRequest request = ContextUtil.getHttpRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 参数
        Object[] args = joinPoint.getArgs();
        // 参数名称
        String[] parameterNames = signature.getParameterNames();
        /*
          SpEL解析的上下文，把 HandlerMethod 的形参都添加到上下文中，并且使用参数名称作为KEY
         */
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        for (int i = 0; i < args.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }

        BizNo bizNoAnnotation = getAnnotation(joinPoint, BizNo.class);
        Operation operationAnnotation = getAnnotation(joinPoint,Operation.class);

        OperationLog operationLog = new OperationLog();
        operationLog.setOperationTimeStart(DateUtils.convertToUtc(new Date()));
        operationLog.setTraceId(MDC.get(TRACE_ID));
        if(operationAnnotation !=null){
            operationLog.setDescription(operationAnnotation.summary());
        }else {
            operationLog.setDescription(method.getName());
        }

        operationLog.setServiceName(this.serviceName);
        operationLog.setApiClass(joinPoint.getTarget().getClass().getName());
        operationLog.setApiMethod(operationLog.getApiClass() + "." + joinPoint.getSignature().getName());
        operationLog.setRequestMethod(request.getMethod());
        operationLog.setRequestUrl(request.getRequestURI());

        if(bizNoAnnotation !=null){
            operationLog.setBizNo(StringUtils.isNotBlank(bizNoAnnotation.spEl()) ?
                    getSpELValue(evaluationContext, bizNoAnnotation.spEl(), String.class) : null);
        }
        operationLog.setBusinessType(getBusinessType(joinPoint));
        operationLog.setRequestIp(SecurityContextHolder.getRemoteIp());
        try{
            operationLog.setLocation(searcher.search(SecurityContextHolder.getRemoteIp()));
        }catch (Exception e){
            operationLog.setLocation("未知");
        }

        if (params != null) {
            if (params.length() <= MAX_LENGTH) {
                operationLog.setRequestArguments(params);
            } else {
                operationLog.setRequestArguments(StringUtils.substring(params, 0, MAX_LENGTH));
            }
        }

        operationLog.setOperatorId(SecurityContextHolder.getUserId());
        operationLog.setOperatorName(SecurityContextHolder.getUserName());
        operationLog.setRequestIp(SecurityContextHolder.getRemoteIp());
        return operationLog;
    }

    private void operationLogPostProcess(String respData, OperationLog operationLog, Object result) {
        if (respData.length() <= MAX_LENGTH) {
            operationLog.setResponseResult(respData);
        } else {
            operationLog.setResponseResult(StringUtils.substring(respData, 0, MAX_LENGTH));
        }
        // 处理大部分返回ResponseResult的接口。
        if (!(result instanceof ApiResult<?> responseResult)) {
            if (ContextUtil.hasRequestContext()) {
                operationLog.setSuccess(ContextUtil.getHttpResponse().getStatus() == HttpServletResponse.SC_OK);
            }
            return;
        }
        operationLog.setSuccess(responseResult.isSuccess());
        if (!responseResult.isSuccess()) {
            operationLog.setErrorMsg(responseResult.getMsg());
        }
    }

    private String[] getParameterNames(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getParameterNames();
    }

    private String getBusinessType(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Tag tagAnnotation = target.getClass().getAnnotation(Tag.class);
        if (tagAnnotation != null) {
           return tagAnnotation.name();
        }else {
            return target.getClass().getName();
        }
    }

    private BizNo getOperationLogAnnotation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(BizNo.class);
    }

    private <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> clazz) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(clazz);
    }

    private <T> T getSpELValue(StandardEvaluationContext evaluationContext, String expressionString, @Nullable Class<T> desiredResultType) {
        T value = null;
        try {
            value = EXPRESSION_PARSER.parseExpression(expressionString, TEMPLATE_PARSER_CONTEXT).getValue(evaluationContext, desiredResultType);
        } catch (Exception e) {
            log.error("操作日志SpEL表达式解析异常: {}", e.getMessage(), e);
        }
        return value;
    }

    private boolean isNormalArgs(Object o) {
        if (o instanceof List) {
            List<?> list = (List<?>) o;
            if (CollUtil.isNotEmpty(list)) {
                return !(list.get(0) instanceof MultipartFile);
            }
        }
        return !(o instanceof HttpServletRequest)
                && !(o instanceof HttpServletResponse)
                && !(o instanceof MultipartFile);
    }
}
