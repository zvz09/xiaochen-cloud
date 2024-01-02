/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

-- 创建第一个数据库
CREATE DATABASE IF NOT EXISTS nacos;

-- 创建第一个数据库用户
CREATE USER 'nacos'@'%' IDENTIFIED BY '!QAZ2wsx#EDC';
GRANT ALL PRIVILEGES ON nacos.* TO 'nacos'@'%';

-- 切换到第一个数据库
USE nacos;

/******************************************/
/*   表名称 = config_info                  */
/******************************************/
drop table if exists `config_info`;
CREATE TABLE `config_info`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`            varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`           varchar(128)          DEFAULT NULL COMMENT 'group_id',
    `content`            longtext     NOT NULL COMMENT 'content',
    `md5`                varchar(32)           DEFAULT NULL COMMENT 'md5',
    `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`           text COMMENT 'source user',
    `src_ip`             varchar(50)           DEFAULT NULL COMMENT 'source ip',
    `app_name`           varchar(128)          DEFAULT NULL COMMENT 'app_name',
    `tenant_id`          varchar(128)          DEFAULT '' COMMENT '租户字段',
    `c_desc`             varchar(256)          DEFAULT NULL COMMENT 'configuration description',
    `c_use`              varchar(64)           DEFAULT NULL COMMENT 'configuration usage',
    `effect`             varchar(64)           DEFAULT NULL COMMENT '配置生效的描述',
    `type`               varchar(64)           DEFAULT NULL COMMENT '配置的类型',
    `c_schema`           text COMMENT '配置的模式',
    `encrypted_data_key` text         NOT NULL COMMENT '密钥',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='config_info';

/******************************************/
/*   表名称 = config_info_aggr             */
/******************************************/
drop table if exists `config_info_aggr`;
CREATE TABLE `config_info_aggr`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) NOT NULL COMMENT 'group_id',
    `datum_id`     varchar(255) NOT NULL COMMENT 'datum_id',
    `content`      longtext     NOT NULL COMMENT '内容',
    `gmt_modified` datetime     NOT NULL COMMENT '修改时间',
    `app_name`     varchar(128) DEFAULT NULL COMMENT 'app_name',
    `tenant_id`    varchar(128) DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`, `group_id`, `tenant_id`, `datum_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='增加租户字段';


/******************************************/
/*   表名称 = config_info_beta             */
/******************************************/
drop table if exists `config_info_beta`;
CREATE TABLE `config_info_beta`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`            varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`           varchar(128) NOT NULL COMMENT 'group_id',
    `app_name`           varchar(128)          DEFAULT NULL COMMENT 'app_name',
    `content`            longtext     NOT NULL COMMENT 'content',
    `beta_ips`           varchar(1024)         DEFAULT NULL COMMENT 'betaIps',
    `md5`                varchar(32)           DEFAULT NULL COMMENT 'md5',
    `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`           text COMMENT 'source user',
    `src_ip`             varchar(50)           DEFAULT NULL COMMENT 'source ip',
    `tenant_id`          varchar(128)          DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text         NOT NULL COMMENT '密钥',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='config_info_beta';

/******************************************/
/*   表名称 = config_info_tag              */
/******************************************/
drop table if exists `config_info_tag`;
CREATE TABLE `config_info_tag`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) NOT NULL COMMENT 'group_id',
    `tenant_id`    varchar(128)          DEFAULT '' COMMENT 'tenant_id',
    `tag_id`       varchar(128) NOT NULL COMMENT 'tag_id',
    `app_name`     varchar(128)          DEFAULT NULL COMMENT 'app_name',
    `content`      longtext     NOT NULL COMMENT 'content',
    `md5`          varchar(32)           DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text COMMENT 'source user',
    `src_ip`       varchar(50)           DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`, `group_id`, `tenant_id`, `tag_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='config_info_tag';

/******************************************/
/*   表名称 = config_tags_relation         */
/******************************************/
drop table if exists `config_tags_relation`;
CREATE TABLE `config_tags_relation`
(
    `id`        bigint(20)   NOT NULL COMMENT 'id',
    `tag_name`  varchar(128) NOT NULL COMMENT 'tag_name',
    `tag_type`  varchar(64)  DEFAULT NULL COMMENT 'tag_type',
    `data_id`   varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`  varchar(128) NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
    `nid`       bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
    PRIMARY KEY (`nid`),
    UNIQUE KEY `uk_configtagrelation_configidtag` (`id`, `tag_name`, `tag_type`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity               */
/******************************************/
drop table if exists `group_capacity`;
CREATE TABLE `group_capacity`
(
    `id`                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id`          varchar(128)        NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota`             int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage`             int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size`          int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size`     int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create`        datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info              */
/******************************************/
drop table if exists `his_config_info`;
CREATE TABLE `his_config_info`
(
    `id`                 bigint(20) unsigned NOT NULL COMMENT 'id',
    `nid`                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
    `data_id`            varchar(255)        NOT NULL COMMENT 'data_id',
    `group_id`           varchar(128)        NOT NULL COMMENT 'group_id',
    `app_name`           varchar(128)                 DEFAULT NULL COMMENT 'app_name',
    `content`            longtext            NOT NULL COMMENT 'content',
    `md5`                varchar(32)                  DEFAULT NULL COMMENT 'md5',
    `gmt_create`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`       datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`           text COMMENT 'source user',
    `src_ip`             varchar(50)                  DEFAULT NULL COMMENT 'source ip',
    `op_type`            char(10)                     DEFAULT NULL COMMENT 'operation type',
    `tenant_id`          varchar(128)                 DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text                NOT NULL COMMENT '密钥',
    PRIMARY KEY (`nid`),
    KEY `idx_gmt_create` (`gmt_create`),
    KEY `idx_gmt_modified` (`gmt_modified`),
    KEY `idx_did` (`data_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='多租户改造';


/******************************************/
/*   表名称 = tenant_capacity              */
/******************************************/
drop table if exists `tenant_capacity`;
CREATE TABLE `tenant_capacity`
(
    `id`                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`         varchar(128)        NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota`             int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage`             int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size`          int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
    `max_aggr_size`     int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create`        datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='租户容量信息表';

drop table if exists `tenant_info`;
CREATE TABLE `tenant_info`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp`            varchar(128) NOT NULL COMMENT 'kp',
    `tenant_id`     varchar(128) default '' COMMENT 'tenant_id',
    `tenant_name`   varchar(128) default '' COMMENT 'tenant_name',
    `tenant_desc`   varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32)  DEFAULT NULL COMMENT 'create_source',
    `gmt_create`    bigint(20)   NOT NULL COMMENT '创建时间',
    `gmt_modified`  bigint(20)   NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`, `tenant_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='tenant_info';

drop table if exists `users`;
CREATE TABLE `users`
(
    `username` varchar(50)  NOT NULL PRIMARY KEY COMMENT 'username',
    `password` varchar(500) NOT NULL COMMENT 'password',
    `enabled`  boolean      NOT NULL COMMENT 'enabled'
);
drop table if exists `roles`;
CREATE TABLE `roles`
(
    `username` varchar(50) NOT NULL COMMENT 'username',
    `role`     varchar(50) NOT NULL COMMENT 'role',
    UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);
drop table if exists `permissions`;
CREATE TABLE `permissions`
(
    `role`     varchar(50)  NOT NULL COMMENT 'role',
    `resource` varchar(128) NOT NULL COMMENT 'resource',
    `action`   varchar(8)   NOT NULL COMMENT 'action',
    UNIQUE INDEX `uk_role_permission` (`role`, `resource`, `action`) USING BTREE
);

INSERT INTO users (username, password, enabled)
VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role)
VALUES ('nacos', 'ROLE_ADMIN');

INSERT INTO nacos.tenant_info (id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified)
VALUES (1, '1', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', 'local-docker', 'docker', 'nacos', 1702384721797,
        1702384721797);
INSERT INTO nacos.tenant_info (id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified)
VALUES (2, '1', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', 'local', 'local', 'nacos', 1702517097804, 1702517097804);

INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (14, 'xiaochen-gateway', 'DEFAULT_GROUP', 'spring:
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: xiaochen-auth
          uri: lb://xiaochen-auth
          predicates:
            - Path=/auth/**
          filters:
            - StripPrefix=1
        # 系统模块
        - id: xiaochen-system
          uri: lb://xiaochen-system
          predicates:
            - Path=/system/**
          filters:
            - StripPrefix=1
        # 系统日志
        - id: xiaochen-log-server
          uri: lb://xiaochen-log-server
          predicates:
            - Path=/log/**
          filters:
            - StripPrefix=1
        # 代码生成
        - id: xiaochen-autocode
          uri: lb://xiaochen-autocode
          predicates:
            - Path=/autocode/**
          filters:
            - StripPrefix=1
        # 工作流模块
        - id: xiaochen-flowable
          uri: lb://xiaochen-flowable
          predicates:
            - Path=/flowable/**
          filters:
            - StripPrefix=1
        # 消息模块
        - id: xiaochen-message
          uri: lb://xiaochen-message
          predicates:
            - Path=/message/**
          filters:
            - StripPrefix=1
        # jobAdmin模块
        - id: xiaochen-job-admin
          uri: lb://xiaochen-job-admin
          predicates:
            - Path=/job/**
          filters:
            - StripPrefix=1

knife4j:
  gateway:
    enabled: true
    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组
    strategy: discover
    discover:
      enabled: true
      # 指定版本号(Swagger2|OpenAPI3)
      version: openapi3
      # 需要排除的微服务(eg:网关服务)
      excluded-services:
        - xiaochen-gateway
        - xiaochen-monitor
# 安全配置
security:
  ignore:
    whites:
      - /logout
      - /login
      - /register
      - /v3/api-docs/**
      - /swagger-resources/**
      - /doc.html
      - /swagger-ui.html/**
      - /swagger-ui/**
                  ', '7a0999f9006ae9d3e50993017001029d', '2023-12-12 20:55:15', '2024-01-02 16:16:50', 'nacos', '172.18.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (15, 'xiaochen-system', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: \'!QAZ2wsx#EDC\'


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.system.controller
        - com.zvz09.xiaochen.system.service.impl
# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'bffc3ffe3affd4fff1b0ed98a1e7b8e7', '2023-12-12 20:55:15', '2023-12-12 21:28:29', 'nacos', '172.27.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (16, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  data:
    redis:
      host: redis
      port: 6379
      password:
      lettuce:
        pool:
          max-active: 20  # 最大连接数，负值表示没有限制，默认8
          max-wait: -1    # 最大阻塞等待时间，负值表示没限制，默认-1
          max-idle: 8     # 最大空闲连接，默认8
          min-idle: 0     # 最小空闲连接，默认0
  rabbitmq:
    host: rabbitmq
    port: 5672
    username: admin
    password: 5tgbhu8
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

logging:
  config: classpath:logback_xiaochen.xml
  file:
    path: datacenter', 'd20a5d35728ca8bb6e57d4a98f55e87b', '2023-12-12 20:55:15', '2023-12-12 21:26:20', 'nacos', '172.27.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (17, 'xiaochen-auth', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  data:
    redis:
      host: redis
      port: 6379
      password:
  application:
    name: xiaochen-auth

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.auth.controller
# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'ffcb1a8d37703eb3dba45d2733fee269', '2023-12-12 20:55:15', '2023-12-12 21:29:08', 'nacos', '172.27.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (18, 'sentinel-xiaochen-gateway', 'DEFAULT_GROUP', '[
    {
        "resource": "xiaochen-auth",
        "count": 500,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xiaochen-system",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xiaochen-autocode",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xiaochen-flowable",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    }
]', 'c8d34ab45d764cea74afbaafab695da6', '2023-12-12 20:55:15', '2023-12-12 20:55:15', null, '172.22.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', null, null, null, 'text', null, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (19, 'xiaochen-monitor', 'DEFAULT_GROUP', '# spring
spring:
  security:
    user:
      name: xiaoxiao
      password: 123456
  boot:
    admin:
      ui:
        title: 筱筱服务状态监控
', 'ea0e34f11491f0feb902b8fd4128d412', '2023-12-12 20:55:15', '2023-12-12 21:29:23', 'nacos', '172.27.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (20, 'xiaochen-autocode', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: \'!QAZ2wsx#EDC\'
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.autocode.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '14395e5df07af34f7bc7f3b126bba787', '2023-12-12 20:55:15', '2023-12-12 21:30:06', 'nacos', '172.27.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (21, 'xiaochen-message', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: \'!QAZ2wsx#EDC\'
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.message.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'e554d0cffd76ac5b9a06a9b901dd06f5', '2023-12-12 20:55:15', '2023-12-12 21:30:46', 'nacos', '172.27.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (22, 'xiaochen-flowable', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql:3306/flowable?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: flowable
    password: \'!QAZ2wsx#EDC\'

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.flowable.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '92a013b798c6ea905aa93e25d986c865', '2023-12-12 20:55:15', '2023-12-22 09:35:40', 'nacos', '172.18.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (23, 'xiaochen-file', 'DEFAULT_GROUP', 'spring:
  # 配置文件上传大小限制
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
# ====================== ↓↓↓↓↓↓ MinIO文件服务器 ↓↓↓↓↓↓ ======================
minio:
  base-url: http://minio:9000
  endpoint: http://minio:9000
  access-key: ny4mqHlQhfHjKJK8DuWd
  secret-key: vG37rc9VCzqmsznY6xEN0ugLNwPULhkvgzKZ9JGM
  bucket-name: xiaochen


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.file.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '406edb946386ee25f89a138e2fba00ab', '2023-12-12 20:55:15', '2023-12-12 21:31:38', 'nacos', '172.27.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (24, 'xiaochen-log-server', 'DEFAULT_GROUP', 'es:
  address : 192.168.191.1:9200
  api-key: cldYMFBJd0JTOUpnd0FhOTJmV2o6Wi0yaFM1Wk5UUnFlZDliLXlNMnJxQQ==

#不记录日志请求路径集合
api:
  log:
    exclude-url:
      - /log/server/page

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.log.server.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '8b2c53872d3de277cdc5a0d6e319735f', '2023-12-12 20:55:15', '2023-12-20 17:14:05', 'nacos', '172.18.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'text', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (25, 'xiaochen-job-admin', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.191.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen-cloud
    password: 2T5kLTwTYWdsBASH


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.job.admin.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'c3a65afce0b69fca64d1cf554d14ac02', '2023-12-12 20:55:15', '2023-12-12 20:55:15', null, '172.22.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', null, null, null, 'text', null, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (26, 'xiaochen-note', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.191.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen-cloud
    password: 2T5kLTwTYWdsBASH

es:
  address : 192.168.191.1:9200
  api-key: cldYMFBJd0JTOUpnd0FhOTJmV2o6Wi0yaFM1Wk5UUnFlZDliLXlNMnJxQQ==


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.note.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '46a012305a8682d5e0ce808f5e5d769d', '2023-12-12 20:55:15', '2023-12-20 17:14:17', 'nacos', '172.18.0.1', '', '0c709398-4a48-4c72-8d8d-cfd49671ca1a', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (27, 'xiaochen-gateway', 'DEFAULT_GROUP', 'spring:
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: xiaochen-auth
          uri: lb://xiaochen-auth
          predicates:
            - Path=/auth/**
          filters:
            - StripPrefix=1
        # 系统模块
        - id: xiaochen-system
          uri: lb://xiaochen-system
          predicates:
            - Path=/system/**
          filters:
            - StripPrefix=1
        # 系统日志
        - id: xiaochen-log-server
          uri: lb://xiaochen-log-server
          predicates:
            - Path=/log/**
          filters:
            - StripPrefix=1
        # 代码生成
        - id: xiaochen-autocode
          uri: lb://xiaochen-autocode
          predicates:
            - Path=/autocode/**
          filters:
            - StripPrefix=1
        # 工作流模块
        - id: xiaochen-flowable
          uri: lb://xiaochen-flowable
          predicates:
            - Path=/flowable/**
          filters:
            - StripPrefix=1
        # 消息模块
        - id: xiaochen-message
          uri: lb://xiaochen-message
          predicates:
            - Path=/message/**
          filters:
            - StripPrefix=1
        # jobAdmin模块
        - id: xiaochen-job-admin
          uri: lb://xiaochen-job-admin
          predicates:
            - Path=/job/**
          filters:
            - StripPrefix=1
        # 笔记模块
        - id: xiaochen-note
          uri: lb://xiaochen-note
          predicates:
            - Path=/note/**
          filters:
            - StripPrefix=1
knife4j:
  gateway:
    enabled: true
    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组
    strategy: discover
    discover:
      enabled: true
      # 指定版本号(Swagger2|OpenAPI3)
      version: openapi3
      # 需要排除的微服务(eg:网关服务)
      excluded-services:
        - xiaochen-gateway
        - xiaochen-monitor
# 安全配置
security:
  ignore:
    whites:
      - /logout
      - /login
      - /register
      - /v3/api-docs/**
      - /swagger-resources/**
      - /doc.html
      - /swagger-ui.html/**
      - /swagger-ui/**
                  ', '534ebdbb5c58ecb62567cdb4d121e143', '2023-12-12 20:55:15', '2023-12-25 14:27:59', 'nacos', '192.168.65.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (28, 'xiaochen-system', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: \'!QAZ2wsx#EDC\'


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.system.controller
        - com.zvz09.xiaochen.system.service.impl
# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'bffc3ffe3affd4fff1b0ed98a1e7b8e7', '2023-12-12 20:55:15', '2023-12-12 21:28:29', 'nacos', '172.27.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (29, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      lettuce:
        pool:
          max-active: 20  # 最大连接数，负值表示没有限制，默认8
          max-wait: -1    # 最大阻塞等待时间，负值表示没限制，默认-1
          max-idle: 8     # 最大空闲连接，默认8
          min-idle: 0     # 最小空闲连接，默认0
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: admin
    password: 5tgbhu8
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

logging:
  config: classpath:logback_xiaochen.xml
  file:
    path: datacenter', 'f8c471a00241cc571e18a60b20de036d', '2023-12-12 20:55:15', '2023-12-18 13:47:56', 'nacos', '172.18.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (30, 'xiaochen-auth', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:
  application:
    name: xiaochen-auth

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.auth.controller
# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'ffcb1a8d37703eb3dba45d2733fee269', '2023-12-12 20:55:15', '2023-12-12 21:29:08', 'nacos', '172.27.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (31, 'sentinel-xiaochen-gateway', 'DEFAULT_GROUP', '[
    {
        "resource": "xiaochen-auth",
        "count": 500,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xiaochen-system",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xiaochen-autocode",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xiaochen-flowable",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    }
]', 'c8d34ab45d764cea74afbaafab695da6', '2023-12-12 20:55:15', '2023-12-18 16:39:21', 'nacos', '172.18.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', null, null, null, 'text', null, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (32, 'xiaochen-monitor', 'DEFAULT_GROUP', '# spring
spring:
  security:
    user:
      name: xiaoxiao
      password: 123456
  boot:
    admin:
      ui:
        title: 筱筱服务状态监控
', 'ea0e34f11491f0feb902b8fd4128d412', '2023-12-12 20:55:15', '2023-12-12 21:29:23', 'nacos', '172.27.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (33, 'xiaochen-autocode', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: \'!QAZ2wsx#EDC\'
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.autocode.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '14395e5df07af34f7bc7f3b126bba787', '2023-12-12 20:55:15', '2023-12-12 21:30:06', 'nacos', '172.27.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (34, 'xiaochen-message', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: \'!QAZ2wsx#EDC\'
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.message.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'e554d0cffd76ac5b9a06a9b901dd06f5', '2023-12-12 20:55:15', '2023-12-12 21:30:46', 'nacos', '172.27.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (35, 'xiaochen-flowable', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/flowable?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: flowable
    password: \'!QAZ2wsx#EDC\'

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.flowable.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'f54488d1f63cf2beec92b8fedb49e6f2', '2023-12-12 20:55:15', '2023-12-22 09:35:15', 'nacos', '172.18.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (36, 'xiaochen-file', 'DEFAULT_GROUP', 'spring:
  # 配置文件上传大小限制
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
# ====================== ↓↓↓↓↓↓ MinIO文件服务器 ↓↓↓↓↓↓ ======================
minio:
  base-url: http://minio:9000
  endpoint: http://minio:9000
  access-key: ny4mqHlQhfHjKJK8DuWd
  secret-key: vG37rc9VCzqmsznY6xEN0ugLNwPULhkvgzKZ9JGM
  bucket-name: xiaochen


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.file.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '406edb946386ee25f89a138e2fba00ab', '2023-12-12 20:55:15', '2023-12-12 21:31:38', 'nacos', '172.27.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (37, 'xiaochen-log-server', 'DEFAULT_GROUP', '
es:
  address : 192.168.191.1:9200
  api-key: cldYMFBJd0JTOUpnd0FhOTJmV2o6Wi0yaFM1Wk5UUnFlZDliLXlNMnJxQQ==

#不记录日志请求路径集合
api:
  log:
    exclude-url:
      - /log/server/page

#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.log.server.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', '578db5f00a6ec2cd45e2a063c02dfee9', '2023-12-12 20:55:15', '2023-12-12 20:55:15', null, '172.22.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', null, null, null, 'text', null, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (38, 'xiaochen-job-admin', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: !QAZ2wsx#EDC


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.job.admin.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'c3a65afce0b69fca64d1cf554d14ac02', '2023-12-12 20:55:15', '2023-12-12 20:55:15', null, '172.22.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', null, null, null, 'text', null, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (39, 'xiaochen-note', 'DEFAULT_GROUP', 'spring:
  cache:
    type: simple #指定所使用的缓存管理器
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/xiaochen-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: xiaochen
    password: \'!QAZ2wsx#EDC\'

es:
  address : 192.168.191.1:9200
  api-key: cldYMFBJd0JTOUpnd0FhOTJmV2o6Wi0yaFM1Wk5UUnFlZDliLXlNMnJxQQ==


#监控
management:
  endpoints:
    web:
      exposure:
        include: \'*\'


# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: \'default\'
      paths-to-match: \'/**\'
      packages-to-scan:
        - com.zvz09.xiaochen.note.controller

# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
', 'fe9b17fe91fa3a5e39ec365a4d1a4a65', '2023-12-12 20:55:15', '2023-12-20 11:32:58', 'nacos', '172.18.0.1', '', 'f144b10b-ab75-4f5a-8e57-a13af514cf10', '', '', '', 'yaml', '', '');



