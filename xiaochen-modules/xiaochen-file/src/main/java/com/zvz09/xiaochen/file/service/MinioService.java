package com.zvz09.xiaochen.file.service;

import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.file.config.MinIoProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;
    private final MinIoProperties minIoProperties;

    /**
     * 查看bucket是否存在
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows
    public Boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    @SneakyThrows
    public void makeBucket(String bucketName) {
        minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(bucketName)
                .build());
    }
    /**
     * 删除存储bucket
     * @return Boolean
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build());
    }
    /**
     * 获取全部bucket
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }


    /**
     * 查看文件对象
     * @return 存储bucket内文件对象信息
     */
    @SneakyThrows
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(minIoProperties.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        for (Result<Item> result : results) {
            items.add(result.get());
        }
        return items;
    }

    /**
     * 删除
     * @param fileName
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public void remove(String fileName) {
        minioClient.removeObject(RemoveObjectArgs.builder().
                bucket(minIoProperties.getBucketName())
                .object(fileName)
                .build());
    }

    /**
     * 文件上传
     *
     * @param fileName:
     *            文件名
     * @param filePath:
     *            文件路径
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public void upload(String fileName, String filePath) {
        minioClient.uploadObject(UploadObjectArgs.builder()
                        .bucket(minIoProperties.getBucketName())
                        .object(fileName)
                        .filename(filePath)
                        .build());
    }

    /**
     * 文件上传
     *
     * @param fileName:
     *            文件名
     * @param stream:
     *            文件流
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */
    @SneakyThrows(Exception.class)
    public  String upload(String fileName, InputStream stream) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minIoProperties.getBucketName())		// 指定 Bucket
                .object(fileName)			// 指定文件的路径
                .stream(stream, stream.available(), -1) // 文件的 Inputstream 流
                .build());
        return getFileUrl(fileName);
    }

    /**
     * 文件上传
     *
     * @param file:
     *            文件
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */
    @SneakyThrows(Exception.class)
    public  String upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String[] split = fileName.split("\\.");
        if (split.length > 1) {
            fileName = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
        } else {
            fileName = fileName + System.currentTimeMillis();
        }
        InputStream in = null;
        try {
            in = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minIoProperties.getBucketName())
                    .object(fileName)
                    .stream(in, in.available(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (Exception e) {
            log.error("文件上传失败",e);
            throw new BusinessException("文件上传失败");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("文件上传失败",e);
                    throw new BusinessException("文件上传失败");
                }
            }
        }
        return getFileUrl(fileName);
    }

    /**
     * 下载文件
     *
     * @param fileName:
     *            文件名
     * @param response:
     * @return: void
     * @date : 2020/8/17 0:34
     */
    @SneakyThrows(Exception.class)
    public void download(String fileName, HttpServletResponse response) {
        // 获取对象的元数据
        final StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(minIoProperties.getBucketName()).object(fileName).build());
        response.setContentType(stat.contentType());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(minIoProperties.getBucketName())
                .object(fileName)
                .build());
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param fileName:
     *            文件名
     * @return: java.lang.String
     * @date : 2020/8/16 22:07
     */
    @SneakyThrows(Exception.class)
    public String getFileUrl(String fileName) {
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                .bucket(minIoProperties.getBucketName()).object(fileName).method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(build);
    }

}
