package com.zvz09.xiaochen.common.web.util;

import java.util.UUID;

/**
 * @author zvz09
 * @date 2022/6/26
 **/
public class UUIDUtil {

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
