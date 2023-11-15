/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.autocode.controller
 * @className com.zvz09.xiaochen.autocode.controller.GenCodeController
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.controller;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.CreateSqlDto;
import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;
import com.zvz09.xiaochen.autocode.domain.dto.template.AutoCodeTemplateDto;
import com.zvz09.xiaochen.autocode.domain.vo.PreviewCodeVo;
import com.zvz09.xiaochen.autocode.service.IGenCodeService;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * GenCodeController
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 15:25
 */
@Slf4j
@RestController
@Tag(name = "生成代码")
@RequestMapping("/gencode")
@RequiredArgsConstructor
public class GenCodeController {

    private final IGenCodeService genCodeService;

    @Operation(summary = "验证代码模板")
    @PostMapping("/verifyAutoCodeTemplate")
    public ApiResult<String> verifyAutoCodeTemplate(@Valid @RequestBody AutoCodeTemplateDto autoCodeTemplateDto) {
        return ApiResult.success(genCodeService.verifyAutoCodeTemplate(autoCodeTemplateDto));
    }

    @Operation(summary = "解析建表SQL")
    @PostMapping("/parseCreateSql")
    public ApiResult<GenConfig> parseCreateSql(@Valid @RequestBody CreateSqlDto createSqlDto) {

        return ApiResult.success(genCodeService.parseCreateSql(createSqlDto));
    }

    @Operation(summary = "预览代码")
    @PostMapping("/previewCode")
    public ApiResult<List<PreviewCodeVo>> previewCode(@Valid @RequestBody GenConfig genConfig) {

        return ApiResult.success(genCodeService.previewCode(genConfig));
    }

    @Operation(summary = "下载代码")
    @PostMapping("/downloadCode")
    public void downloadCode(HttpServletResponse response, @Valid @RequestBody GenConfig genConfig) throws IOException {
        byte[] data = genCodeService.downloadCode(genConfig);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + genConfig.getCapClassName() + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }
}
 