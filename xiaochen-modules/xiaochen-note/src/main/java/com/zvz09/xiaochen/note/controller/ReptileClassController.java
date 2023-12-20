package com.zvz09.xiaochen.note.controller;


import com.zvz09.xiaochen.common.web.controller.BaseController;
import com.zvz09.xiaochen.note.constant.OperateAction;
import com.zvz09.xiaochen.note.domain.dto.ReptileClassDTO;
import com.zvz09.xiaochen.note.domain.entity.ReptileClass;
import com.zvz09.xiaochen.note.service.IReptileClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 爬取数据类 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@RestController
@RequestMapping("/reptile/class")
@Tag(name = "爬虫类管理")
public class ReptileClassController extends BaseController<IReptileClassService, ReptileClass, ReptileClassDTO> {

    private final IReptileClassService reptileClassService;

    public ReptileClassController(IReptileClassService reptileClassService) {
        super.setBaseService(reptileClassService);
        this.reptileClassService = reptileClassService;
    }


    @Operation(summary = "启动/禁用抓取解析类")
    @PutMapping("/{id}/{operate}")
    public void operate(@PathVariable(value = "id") Long id,
                        @PathVariable("operate") OperateAction operate) {
        reptileClassService.operate(id,operate);
    }

}

