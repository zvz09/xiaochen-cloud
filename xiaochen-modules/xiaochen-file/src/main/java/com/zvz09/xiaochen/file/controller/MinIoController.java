package com.zvz09.xiaochen.file.controller;

import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.log.annotation.BizNo;
import com.zvz09.xiaochen.file.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 18237
 */
@RestController
@RequestMapping("/file/minio")
@Tag(name ="MinIO接口")
@RequiredArgsConstructor
public class MinIoController {

    private final MinioService minioService;

    @Operation(summary = "上传文件")
    @PostMapping(value = "/upload")
    public ApiResult<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String fileUrl = minioService.upload(file);
        return ApiResult.success(fileUrl);
    }

    @Operation(summary = "下载文件")
    @GetMapping(value = "/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        minioService.download(fileName, response);
    }

    @Operation(summary = "删除文件")
    @GetMapping(value = "/delete")
    @BizNo(spEl = "{#fileName}")
    public ApiResult delete(@RequestParam("fileName") String fileName) {
        minioService.remove(fileName);
        return ApiResult.success();
    }

}

