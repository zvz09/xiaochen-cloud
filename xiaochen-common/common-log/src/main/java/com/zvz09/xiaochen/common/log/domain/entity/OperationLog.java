package com.zvz09.xiaochen.common.log.domain.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 操作日志记录表
 * @author zvz09
 */
@Data
public class OperationLog implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     * 日志描述。
     */
    private String description;

    /**
     * 接口所在服务名称。
     * 通常为spring.application.name配置项的值。
     */
    private String serviceName;

    /**
     * 调用的controller全类名。
     * 之所以为独立字段，是为了便于查询和统计接口的调用频度。
     */
    private String apiClass;

    /**
     * 调用的controller中的方法。
     * 格式为：接口类名 + "." + 方法名。
     */
    private String apiMethod;

    /**
     * 每次请求的Id。
     * 对于微服务之间的调用，在同一个请求的调用链中，该值是相同的。
     */
    private String traceId;

    /**
     * 调用时长。
     */
    private Long elapse;

    /**
     * HTTP 请求方法，如GET。
     */
    private String requestMethod;

    /**
     * HTTP 请求地址。
     */
    private String requestUrl;

    /**
     * controller接口参数。
     */
    private String requestArguments;

    /**
     * controller应答结果。
     */
    private String responseResult;

    /**
     * 请求IP。
     */
    private String requestIp;

    /**
     * 请求地址
     */
    private String location;

    /**
     * 应答状态。
     */
    private Boolean success;

    /**
     * 错误信息。
     */
    private String errorMsg;

    /**
     * 操作员Id。
     */
    private Long operatorId;

    /**
     * 操作员名称。
     */
    private String operatorName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 操作日志绑定的业务对象标识
     */
    private String bizNo;

    /**
     * 操作开始时间。
     */
    private String operationTimeStart;

    /**
     * 操作结束时间。
     */
    private String operationTimeEnd;

}
