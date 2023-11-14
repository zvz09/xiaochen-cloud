/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.util
 * @className com.zvz09.xiaochen.common.util.JacksonUtil
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * JacksonUtil
 *
 * @author zvz09
 * @version 1.0
 * @description jackson 工具类
 * @date 2023/8/30 17:33
 */
public class JacksonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static String writeValueAsString(Object object) {
        String subject = "";
        try {
            subject = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("系统异常");
        }
        return subject;
    }

    public static Map writeValueAsMap(Object object) {
        try {
            return OBJECT_MAPPER.readValue(writeValueAsString(object), Map.class); //json转换成map
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 转换异常");
        }
    }

    public static <T> T readValue(String jsonString, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 转换异常");
        }
    }

}
 