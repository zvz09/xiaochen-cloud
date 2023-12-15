package com.zvz09.xiaochen.blog.controller;


import com.zvz09.xiaochen.blog.domain.dto.ReptileClassDto;
import com.zvz09.xiaochen.blog.domain.entity.ReptileClass;
import com.zvz09.xiaochen.blog.service.IReptileClassService;
import com.zvz09.xiaochen.common.web.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class ReptileClassController extends BaseController<IReptileClassService, ReptileClass,ReptileClassDto> {

    public ReptileClassController(IReptileClassService reptileClassService) {
        super.setBaseService(reptileClassService);
    }

}

