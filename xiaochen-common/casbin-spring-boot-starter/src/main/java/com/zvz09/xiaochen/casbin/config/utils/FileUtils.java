package com.zvz09.xiaochen.casbin.config.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author lizili-YF0033
 */
public class FileUtils {
    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static String removePrefix(String str, String prefix) {
        if (str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }

    public static File getFile(String filePath) {
        File template = new File(removePrefix(filePath, "classpath:"));
        if (template.exists()) {
            return template;
        } else {
            return null;
        }
    }

    public static InputStream getFileAsInputStream(String filePath) {
        File file = getFile(filePath);
        try {
            if (file != null && file.exists()) {
                return Files.newInputStream(file.toPath());
            }
            Resource resource = new DefaultResourceLoader().getResource(filePath);
            if (resource.exists()) {
                return resource.getInputStream();
            }
            return null;
        } catch (Exception e) {
            logger.error("load file $filePath by inputStream error", e);
            return null;
        }
    }

    public static String getFileAsText(String filePath) {
        try (InputStream inputStream = getFileAsInputStream(filePath);) {
            if (inputStream != null) {
                return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
