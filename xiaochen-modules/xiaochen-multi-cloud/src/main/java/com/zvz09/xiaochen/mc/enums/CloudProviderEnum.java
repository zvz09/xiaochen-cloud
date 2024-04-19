package com.zvz09.xiaochen.mc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 云厂商枚举
 */
@Getter
@AllArgsConstructor
public enum CloudProviderEnum {

    ALI_YUN("aliyun", "阿里云"),
    TENCENT_CLOUD("tencentcloud", "腾讯云"),
    VOLCENGINE("volcengine", "火山云");

    private final String value;
    private final String name;

    /**
     * 查询所有枚举
     */
    public static CloudProviderEnum[] getAllEnums() {
        return values();
    }

    /**
     * 根据value查询枚举
     * @param value
     * @return
     */
    public static CloudProviderEnum getByValue(String value) {
        for (CloudProviderEnum myEnum : values()) {
            if (myEnum.getValue().equals(value)) {
                return myEnum;
            }
        }
        return null; // 如果没有匹配的枚举常量
    }

    /**
     * 根据name查询枚举
     * @param name
     * @return
     */
    public static CloudProviderEnum getByName(String name) {
        for (CloudProviderEnum myEnum : values()) {
            if (myEnum.getName().equals(name)) {
                return myEnum;
            }
        }
        return null; // 如果没有匹配的枚举常量
    }

}
