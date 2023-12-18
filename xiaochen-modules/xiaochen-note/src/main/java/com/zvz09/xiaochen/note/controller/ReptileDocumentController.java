package com.zvz09.xiaochen.note.controller;


import com.zvz09.xiaochen.common.web.controller.BaseController;
import com.zvz09.xiaochen.note.domain.dto.ReptileDocumentDTO;
import com.zvz09.xiaochen.note.domain.entity.ReptileDocument;
import com.zvz09.xiaochen.note.service.IReptileDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 爬取的原始数据 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@RestController
@RequestMapping("/reptile/document")
@Tag(name = "收藏文章管理")
public class ReptileDocumentController extends BaseController<IReptileDocumentService, ReptileDocument, ReptileDocumentDTO> {

    private  final IReptileDocumentService reptileDocumentService;
    public ReptileDocumentController(IReptileDocumentService reptileDocumentService) {
        super.setBaseService(reptileDocumentService);
        this.reptileDocumentService = reptileDocumentService;
    }

    @GetMapping("/notParsed")
    @Operation(summary = "未正确解析")
    public long  page() {
        return reptileDocumentService.countNotParsed();
    }

}

