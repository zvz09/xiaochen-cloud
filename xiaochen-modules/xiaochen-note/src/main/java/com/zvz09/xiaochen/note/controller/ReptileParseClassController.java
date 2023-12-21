package com.zvz09.xiaochen.note.controller;


import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.web.controller.BaseController;
import com.zvz09.xiaochen.common.web.util.StringUtils;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import com.zvz09.xiaochen.note.constant.OperateAction;
import com.zvz09.xiaochen.note.domain.dto.ReptileClassDTO;
import com.zvz09.xiaochen.note.domain.entity.ReptileParseClass;
import com.zvz09.xiaochen.note.service.IReptileParseClassService;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import com.zvz09.xiaochen.note.strategy.ReptileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * 爬取数据类 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@Slf4j
@RestController
@RequestMapping("/reptile/parse/class")
@Tag(name = "爬虫数据解析类管理")
public class ReptileParseClassController extends BaseController<IReptileParseClassService, ReptileParseClass, ReptileClassDTO> {

    private final IReptileParseClassService reptileClassService;
    private final ReptileService reptileService;

    public ReptileParseClassController(IReptileParseClassService reptileClassService,ReptileService reptileService) {
        super.setBaseService(reptileClassService);
        this.reptileClassService = reptileClassService;
        this.reptileService = reptileService;
    }

    @Override
    public void insert(@RequestBody ReptileClassDTO dto){
        ReptileParseClass reptileParseClass = buildReptileParseClass(dto);
        reptileClassService.save(reptileParseClass);
    }

    public void update(@Validated(UpdateValidation.class) @RequestBody ReptileClassDTO dto) {
        ReptileParseClass dbData = reptileClassService.getById(dto.getId());
        if(dbData == null){
            return;
        }
        if(dbData.getStatus()){
            throw new BusinessException("解析类启用状态无法修改。");
        }
        ReptileParseClass reptileParseClass = buildReptileParseClass(dto);
        reptileClassService.updateById(reptileParseClass);
    }

    @NotNull
    private ReptileParseClass buildReptileParseClass(ReptileClassDTO dto) {
        try{
            ReptileParseClass reptileParseClass = dto.convertedToPo();
            reptileParseClass.setId(null);
            if(StringUtils.isEmpty(dto.getContent())){
                throw new BusinessException("爬取数据解析类内容不能为空");
            }
            ReptileService.CheckResult checkResult = reptileService.checkReptileParseClass(reptileParseClass);
            if(!checkResult.getErrMsg().isEmpty()){
                throw new BusinessException(checkResult.getErrMsgString());
            }
            Class<ReptileDataParserStrategy> myClass = reptileService.compileAndLoad(reptileParseClass);

            Method method = myClass.getMethod("getBaseUrl");
            // 获取构造函数并创建实例
            Constructor<ReptileDataParserStrategy> constructor = myClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            ReptileDataParserStrategy instance = (ReptileDataParserStrategy) constructor.newInstance();
            // 执行私有方法
            Object r = method.invoke(instance);
            reptileParseClass.setSiteUrl(r.toString());
            reptileParseClass.setClassName(checkResult.getClassName());
            return reptileParseClass;
        } catch (BusinessException ignored){
            throw ignored;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    @Operation(summary = "启动/禁用抓取解析类")
    @PutMapping("/{id}/{operate}")
    public void operate(@PathVariable(value = "id") Long id,
                        @PathVariable("operate") OperateAction operate) {
        reptileClassService.operate(id,operate);
    }

}

