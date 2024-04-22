package com.zvz09.xiaochen.mc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductEnum {

    ECS("ecs", "云服务器"),
    OSS("oss","对象存储");

    private final String value;
    private final String name;
}
