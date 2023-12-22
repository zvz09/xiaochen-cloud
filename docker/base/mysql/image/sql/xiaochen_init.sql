CREATE DATABASE IF NOT EXISTS `xiaochen-cloud`;

CREATE USER 'xiaochen'@'%' IDENTIFIED BY '!QAZ2wsx#EDC';
GRANT ALL PRIVILEGES ON `xiaochen-cloud`.* TO 'xiaochen'@'%';

USE `xiaochen-cloud`;
CREATE TABLE `casbin_rule`
(
    `id`    bigint(20) unsigned DEFAULT NULL,
    `ptype` varchar(100)        DEFAULT NULL,
    `v0`    varchar(100)        DEFAULT NULL,
    `v1`    varchar(100)        DEFAULT NULL,
    `v2`    varchar(100)        DEFAULT NULL,
    `v3`    varchar(100)        DEFAULT NULL,
    `v4`    varchar(100)        DEFAULT NULL,
    `v5`    varchar(100)        DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `event_remind`
(
    `id`             bigint(20)   DEFAULT NULL,
    `created_at`     datetime     DEFAULT NULL,
    `updated_at`     datetime     DEFAULT NULL,
    `deleted`        tinyint(4)   DEFAULT NULL,
    `action`         varchar(255) DEFAULT NULL,
    `source_id`      varchar(255) DEFAULT NULL,
    `source_type`    varchar(255) DEFAULT NULL,
    `source_content` text,
    `state`          tinyint(4)   DEFAULT NULL,
    `sender_id`      bigint(20)   DEFAULT NULL,
    `recipient_id`   bigint(20)   DEFAULT NULL,
    `remind_time`    datetime     DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exa_file`
(
    `id`          bigint(20)   DEFAULT NULL,
    `created_at`  datetime     DEFAULT NULL,
    `updated_at`  datetime     DEFAULT NULL,
    `deleted_at`  datetime     DEFAULT NULL,
    `file_name`   varchar(191) DEFAULT NULL,
    `file_md5`    varchar(191) DEFAULT NULL,
    `file_path`   varchar(191) DEFAULT NULL,
    `chunk_total` bigint(20)   DEFAULT NULL,
    `is_finish`   tinyint(4)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `job_info`
(
    `id`                        bigint(20)   NOT NULL,
    `created_at`                datetime              DEFAULT NULL,
    `updated_at`                datetime              DEFAULT NULL,
    `deleted`                   tinyint(4)            DEFAULT NULL,
    `job_group`                 varchar(255) NOT NULL COMMENT '微服务名',
    `job_desc`                  varchar(255) NOT NULL,
    `author`                    varchar(64)           DEFAULT NULL COMMENT '作者',
    `alarm_email`               varchar(255)          DEFAULT NULL COMMENT '报警邮件',
    `schedule_type`             varchar(50)  NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
    `schedule_conf`             varchar(128)          DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
    `misfire_strategy`          varchar(50)  NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
    `executor_route_strategy`   varchar(50)           DEFAULT NULL COMMENT '执行器路由策略',
    `executor_handler`          varchar(255)          DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param`            varchar(512)          DEFAULT NULL COMMENT '执行器任务参数',
    `executor_block_strategy`   varchar(50)           DEFAULT NULL COMMENT '阻塞处理策略',
    `executor_timeout`          int(11)      NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
    `executor_fail_retry_count` int(11)      NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `glue_type`                 varchar(50)  NOT NULL COMMENT 'GLUE类型',
    `glue_source`               mediumtext COMMENT 'GLUE源代码',
    `glue_remark`               varchar(128)          DEFAULT NULL COMMENT 'GLUE备注',
    `glue_updatetime`           datetime              DEFAULT NULL COMMENT 'GLUE更新时间',
    `trigger_status`            tinyint(4)   NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `job_log`
(
    `id`                        bigint(20)   NOT NULL AUTO_INCREMENT,
    `job_group`                 varchar(255) NOT NULL COMMENT '执行器主键ID',
    `job_id`                    bigint(20)   NOT NULL COMMENT '任务，主键ID',
    `executor_address`          varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
    `executor_handler`          varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param`            varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_sharding_param`   varchar(20)  DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
    `executor_fail_retry_count` int(11)      DEFAULT '0' COMMENT '失败重试次数',
    `trigger_time`              datetime     DEFAULT NULL COMMENT '调度-时间',
    `trigger_code`              int(11)      DEFAULT NULL COMMENT '调度-结果',
    `trigger_msg`               text COMMENT '调度-日志',
    `handle_time`               datetime     DEFAULT NULL COMMENT '执行-时间',
    `handle_code`               int(11)      DEFAULT NULL COMMENT '执行-状态',
    `handle_msg`                text COMMENT '执行-日志',
    `alarm_status`              tinyint(4)   DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
    `log_trace_id`              varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `I_trigger_time` (`trigger_time`),
    KEY `I_handle_code` (`handle_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `note_article`
(
    `id`           bigint(20)   NOT NULL,
    `created_at`   datetime              DEFAULT NULL,
    `updated_at`   datetime              DEFAULT NULL,
    `deleted`      tinyint(4)            DEFAULT NULL,
    `user_id`      varchar(100)          DEFAULT NULL COMMENT '用户id',
    `category_id`  bigint(20)            DEFAULT NULL COMMENT '分类id',
    `title`        varchar(150) NOT NULL DEFAULT '' COMMENT '文章标题',
    `avatar`       varchar(200)          DEFAULT NULL COMMENT '文章封面地址',
    `summary`      varchar(255) NOT NULL DEFAULT '' COMMENT '文章简介',
    `content`      mediumtext COMMENT '文章内容',
    `read_type`    int(2)                DEFAULT '0' COMMENT '阅读方式 0无需验证 1：评论阅读 2：点赞阅读 3：扫码阅读',
    `stick`        int(11)               DEFAULT '0' COMMENT '是否置顶 0否 1是',
    `original`     int(11)               DEFAULT '1' COMMENT '是否原创  0：转载 1:原创',
    `original_url` varchar(255)          DEFAULT NULL COMMENT '转载地址',
    `quantity`     bigint(20)            DEFAULT '0' COMMENT '文章阅读量',
    `recommend`    int(11)               DEFAULT '0' COMMENT '是否推荐',
    `keywords`     varchar(200)          DEFAULT NULL COMMENT '关键词',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='博客文章表';

CREATE TABLE `note_article_tag`
(
    `id`         bigint(20) NOT NULL,
    `article_id` bigint(20) NOT NULL COMMENT '文章id',
    `tag_id`     bigint(20) NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `fk_article_tag_1` (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `note_category`
(
    `id`           bigint(20)  NOT NULL,
    `created_at`   datetime             DEFAULT NULL,
    `updated_at`   datetime             DEFAULT NULL,
    `deleted`      tinyint(4)           DEFAULT NULL,
    `name`         varchar(32) NOT NULL DEFAULT '' COMMENT '分类名称',
    `click_volume` int(11)              DEFAULT '0',
    `sort`         int(11)     NOT NULL COMMENT '排序',
    `icon`         varchar(100)         DEFAULT NULL COMMENT '图标',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `category_name` (`name`) USING BTREE COMMENT '博客分类名称'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='博客分类表';

CREATE TABLE `note_reptile_parse_class`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime     DEFAULT NULL,
    `updated_at` datetime     DEFAULT NULL,
    `deleted`    tinyint(4)   DEFAULT NULL,
    `class_name` varchar(500) DEFAULT NULL COMMENT '类名',
    `content`    mediumtext COMMENT '类内容',
    `site_url`   varchar(500) DEFAULT NULL COMMENT '数据来源网站',
    `status`     tinyint(4)   DEFAULT NULL COMMENT '启用状态',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='爬取数据解析类';

CREATE TABLE `note_reptile_document`
(
    `id`         bigint(20) NOT NULL,
    `created_at` datetime      DEFAULT NULL,
    `updated_at` datetime      DEFAULT NULL,
    `deleted`    tinyint(4)    DEFAULT NULL,
    `title`      varchar(2000) DEFAULT NULL,
    `url`        varchar(5000) DEFAULT NULL COMMENT 'url',
    `content`    mediumtext COMMENT '文章内容',
    `status`     tinyint(1)    DEFAULT NULL COMMENT '状态 true 成功 fasle 解析失败',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='爬取的原始数据';

CREATE TABLE `note_tags`
(
    `id`           bigint(20)  NOT NULL,
    `created_at`   datetime             DEFAULT NULL,
    `updated_at`   datetime             DEFAULT NULL,
    `deleted`      tinyint(4)           DEFAULT NULL,
    `name`         varchar(32) NOT NULL DEFAULT '' COMMENT '标签名称',
    `click_volume` int(10)              DEFAULT '0',
    `sort`         int(11)     NOT NULL COMMENT '排序',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `tag_name` (`name`) USING BTREE COMMENT '博客标签名称'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='博客标签表';

CREATE TABLE `sys_api`
(
    `id`           bigint(20)   DEFAULT NULL,
    `created_at`   datetime     DEFAULT NULL,
    `updated_at`   datetime     DEFAULT NULL,
    `deleted`      tinyint(4)   DEFAULT NULL,
    `service_name` varchar(256) DEFAULT NULL,
    `path`         varchar(191) DEFAULT NULL,
    `description`  varchar(191) DEFAULT NULL,
    `api_group`    varchar(191) DEFAULT NULL,
    `method`       varchar(191) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_auto_code`
(
    `id`           bigint(20)   DEFAULT NULL,
    `created_at`   datetime     DEFAULT NULL,
    `updated_at`   datetime     DEFAULT NULL,
    `deleted`      tinyint(4)   DEFAULT NULL,
    `package_name` varchar(191) DEFAULT NULL,
    `label`        varchar(191) DEFAULT NULL,
    `description`  varchar(191) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_auto_code_historiy`
(
    `id`             bigint(20)   DEFAULT NULL,
    `created_at`     datetime     DEFAULT NULL,
    `updated_at`     datetime     DEFAULT NULL,
    `deleted`        tinyint(4)   DEFAULT NULL,
    `package_name`   varchar(191) DEFAULT NULL,
    `business_db`    varchar(191) DEFAULT NULL,
    `table_name`     varchar(191) DEFAULT NULL,
    `request_meta`   text,
    `auto_code_path` text,
    `injection_meta` text,
    `struct_name`    varchar(191) DEFAULT NULL,
    `struct_cn_name` varchar(191) DEFAULT NULL,
    `api_ids`        varchar(191) DEFAULT NULL,
    `flag`           bigint(20)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_auto_code_template`
(
    `id`                bigint(20)   DEFAULT NULL,
    `created_at`        datetime     DEFAULT NULL,
    `updated_at`        datetime     DEFAULT NULL,
    `deleted`           tinyint(4)   DEFAULT NULL,
    `name`              varchar(255) DEFAULT NULL,
    `language`          varchar(255) DEFAULT NULL,
    `template_engine`   varchar(255) DEFAULT NULL,
    `template_type`     varchar(255) DEFAULT NULL,
    `default_file_name` varchar(256) DEFAULT NULL,
    `content`           longtext
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_data_authority_id`
(
    `sys_authority_authority_id`     bigint(20) DEFAULT NULL,
    `data_authority_id_authority_id` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_department`
(
    `id`         bigint(20)  DEFAULT NULL,
    `parent_id`  bigint(20)  DEFAULT NULL,
    `dept_name`  varchar(30) DEFAULT NULL,
    `order_num`  int(11)     DEFAULT NULL,
    `leader`     varchar(20) DEFAULT NULL,
    `phone`      varchar(11) DEFAULT NULL,
    `email`      varchar(50) DEFAULT NULL,
    `status`     tinyint(4)  DEFAULT NULL,
    `created_at` datetime    DEFAULT NULL,
    `updated_at` datetime    DEFAULT NULL,
    `deleted`    tinyint(4)  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_dictionary`
(
    `id`          bigint(20)   DEFAULT NULL,
    `created_at`  datetime     DEFAULT NULL,
    `updated_at`  datetime     DEFAULT NULL,
    `deleted`     tinyint(4)   DEFAULT NULL,
    `name`        varchar(191) DEFAULT NULL,
    `encode`      varchar(191) DEFAULT NULL,
    `status`      tinyint(4)   DEFAULT NULL,
    `description` varchar(191) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_dictionary_detail`
(
    `id`                bigint(20)   DEFAULT NULL,
    `created_at`        datetime     DEFAULT NULL,
    `updated_at`        datetime     DEFAULT NULL,
    `deleted`           tinyint(4)   DEFAULT NULL,
    `label`             varchar(256) DEFAULT NULL,
    `value`             varchar(256) DEFAULT NULL,
    `status`            tinyint(4)   DEFAULT NULL,
    `tag_type`          varchar(20)  DEFAULT NULL COMMENT '标签类型(''success'' | ''info'' | ''warning'' | ''danger'' | '''')',
    `sort`              bigint(20)   DEFAULT NULL,
    `sys_dictionary_id` bigint(20)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_gencode_history`
(
    `id`          bigint(20)   DEFAULT NULL,
    `table_name`  varchar(300) DEFAULT NULL,
    `description` varchar(500) DEFAULT NULL,
    `gen_config`  text,
    `created_at`  datetime     DEFAULT NULL,
    `updated_at`  datetime     DEFAULT NULL,
    `deleted`     tinyint(4)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_menu`
(
    `id`          bigint(20)   DEFAULT NULL,
    `created_at`  datetime     DEFAULT NULL,
    `updated_at`  datetime     DEFAULT NULL,
    `deleted`     tinyint(4)   DEFAULT NULL,
    `parent_id`   bigint(20)   DEFAULT NULL COMMENT '父id',
    `path`        varchar(256) DEFAULT NULL COMMENT '路由菜单访问路径',
    `name`        varchar(256) DEFAULT NULL COMMENT '路由名称',
    `redirect`    varchar(256) DEFAULT NULL COMMENT '路由重定向地址',
    `component`   varchar(256) DEFAULT NULL COMMENT '视图文件路径',
    `sort`        bigint(20)   DEFAULT NULL COMMENT '排序',
    `icon`        varchar(256) DEFAULT NULL COMMENT '菜单和面包屑对应的图标',
    `title`       varchar(256) DEFAULT NULL COMMENT '路由标题',
    `link`        varchar(256) DEFAULT NULL COMMENT '路由标题',
    `hide`        tinyint(1)   DEFAULT NULL COMMENT '是否在菜单中隐藏',
    `full_screen` tinyint(1)   DEFAULT NULL COMMENT '菜单是否全屏',
    `affix`       tinyint(1)   DEFAULT NULL COMMENT '菜单是否固定在标签页中',
    `keep_alive`  tinyint(1)   DEFAULT NULL COMMENT '当前路由是否缓存'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_perm_code`
(
    `id`             bigint(20)                                       NOT NULL COMMENT '主键Id',
    `menu_id`        bigint(20) DEFAULT NULL COMMENT '菜单Id',
    `parent_id`      bigint(20) DEFAULT NULL COMMENT '上级权限字Id',
    `perm_code`      varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '权限字标识(一般为有含义的英文字符串)',
    `perm_code_type` varchar(10) COLLATE utf8mb4_bin                  NOT NULL COMMENT '类型(0: 菜单 1:操作)',
    `show_name`      varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '显示名称',
    `show_order`     int(11)                                          NOT NULL COMMENT '显示顺序(数值越小，越靠前)',
    `created_at`     datetime   DEFAULT NULL,
    `updated_at`     datetime   DEFAULT NULL,
    `deleted`        tinyint(4) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_parent_id` (`parent_id`) USING BTREE,
    KEY `idx_show_order` (`show_order`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='系统权限字';

CREATE TABLE `sys_perm_code_api`
(
    `perm_code_id` bigint(20) NOT NULL COMMENT '权限字Id',
    `api_id`       bigint(20) NOT NULL COMMENT '权限id',
    PRIMARY KEY (`perm_code_id`, `api_id`) USING BTREE,
    KEY `idx_perm_id` (`api_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='系统权限字和api接口关联表';

CREATE TABLE `sys_position`
(
    `id`            bigint(20)   DEFAULT NULL,
    `position_code` varchar(64)  DEFAULT NULL,
    `position_name` varchar(50)  DEFAULT NULL,
    `position_sort` int(11)      DEFAULT NULL,
    `status`        tinyint(4)   DEFAULT NULL,
    `remark`        varchar(500) DEFAULT NULL,
    `created_at`    datetime     DEFAULT NULL,
    `updated_at`    datetime     DEFAULT NULL,
    `deleted`       tinyint(4)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_role`
(
    `created_at` datetime            DEFAULT NULL,
    `updated_at` datetime            DEFAULT NULL,
    `deleted`    tinyint(4)          DEFAULT NULL,
    `role_code`  varchar(20)         DEFAULT NULL,
    `role_name`  varchar(191)        DEFAULT NULL,
    `id`         bigint(20) unsigned DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_role_perm_code`
(
    `role_id`      bigint(20) NOT NULL COMMENT '角色Id',
    `perm_code_id` bigint(20) NOT NULL COMMENT '权限字Id',
    PRIMARY KEY (`role_id`, `perm_code_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='角色与系统权限字对应关系表';

CREATE TABLE `sys_user`
(
    `id`           bigint(20)   DEFAULT NULL,
    `created_at`   datetime     DEFAULT NULL,
    `updated_at`   datetime     DEFAULT NULL,
    `deleted`      tinyint(4)   DEFAULT NULL,
    `uuid`         varchar(191) DEFAULT NULL,
    `username`     varchar(191) DEFAULT NULL,
    `password`     varchar(191) DEFAULT NULL,
    `nick_name`    varchar(191) DEFAULT NULL,
    `gender`       tinyint(1)   DEFAULT NULL COMMENT '0 女 1 男',
    `side_mode`    varchar(191) DEFAULT NULL,
    `header_img`   varchar(191) DEFAULT NULL,
    `base_color`   varchar(191) DEFAULT NULL,
    `active_color` varchar(191) DEFAULT NULL,
    `authority_id` bigint(20)   DEFAULT NULL,
    `phone`        varchar(191) DEFAULT NULL,
    `email`        varchar(191) DEFAULT NULL,
    `enable`       bigint(20)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_user_dept`
(
    `user_id` bigint(20) DEFAULT NULL,
    `dept_id` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_user_position`
(
    `user_id`     bigint(20) DEFAULT NULL,
    `position_id` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `sys_user_role`
(
    `sys_user_id`   bigint(20) DEFAULT NULL,
    `sys_role_code` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `user_system_notice`
(
    `id`               bigint(20) DEFAULT NULL,
    `state`            tinyint(4) DEFAULT NULL,
    `system_notice_id` bigint(20) DEFAULT NULL,
    `recipient_id`     bigint(20) DEFAULT NULL,
    `pull_time`        datetime   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `xiaochen-cloud`.sys_user (id, created_at, updated_at, deleted, uuid, username, password, nick_name, gender,
                                       side_mode, header_img, base_color, active_color, authority_id, phone, email,
                                       enable)
VALUES (1, '2023-08-30 09:47:19', '2023-10-27 13:26:31', 0, '93e7577b-b86e-45e9-ae0f-3700d0e3e1c0', 'admin',
        '$2a$10$Q0i3Yc0GEllO4uh5W5/5N.set0TMLCl.KAJR3tJbLnSO7KmeFITLC', '管理员', 1, 'dark',
        'https://qmplusimg.henrongyi.top/gva_header.jpg', '#fff', '#1890ff', 1, '17611111111', '333333333@qq.com', 1);

INSERT INTO `xiaochen-cloud`.sys_role (created_at, updated_at, deleted, role_code, role_name, id)
VALUES ('2023-08-30 09:47:18', '2023-08-30 09:47:19', 0, '888', '超级管理员', 1);

INSERT INTO `xiaochen-cloud`.sys_user_role (sys_user_id, sys_role_code)
VALUES (1, 888);

INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 0, '/home/index', 'home', '', '/home/index', 1,
        'HomeFilled', '仪表盘', '', 0, 0, 1, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (2, '2023-11-16 17:11:40', '2023-11-16 17:11:42', 0, 0, '/system', 'system', '/system/accountManage', null, 2,
        'Tools', '系统管理', null, 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (3, '2023-11-16 17:13:20', '2023-11-22 17:09:54', 0, 2, '/system/menuMange', 'menuMange', null,
        '/system/menuMange/index', 2, 'Menu', '菜单管理', null, 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1725407120861429761, '2023-11-17 14:54:49', '2023-11-17 15:55:46', 1, 0, '/dataScreen', 'dataScreen', '',
        '/dataScreen/index', null, 'Histogram', '数据大屏1111', '', 0, 1, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1726777800827920385, '2023-11-21 09:41:24', '2023-11-22 17:10:00', 0, 2, '/system/roleManage', 'roleManage', '',
        '/system/roleManage/index', 3, 'Menu', '角色管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1726844567755542529, '2023-11-21 14:06:43', '2023-11-22 17:56:02', 0, 2, '/system/permCodeManage',
        'permCodeManage', '', '/system/permCodeManage/index', 4, 'Menu', '权限字管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1727229150502072321, '2023-11-22 15:34:55', '2023-11-22 17:09:51', 0, 2, '/system/userManage', 'userManage', '',
        '/system/userManage/index', 1, 'Menu', '用户管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1727521295351959553, '2023-11-23 10:55:47', '2023-11-23 10:55:59', 0, 2, '/system/dictManage', 'dictManage', '',
        '/system/dictManage/index', 5, 'Menu', '字典管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1729053179210924034, '2023-11-27 16:22:57', '2023-11-27 16:22:57', 0, 2, '/system/systemLog', 'systemLog', '',
        '/system/systemLog/index', 7, 'Menu', '系统日志', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1729851011777597441, '2023-11-29 21:13:15', '2023-11-29 21:18:49', 0, 2, '/system/timingTask', 'timingTask', '',
        '/system/timingTask/index', 3, 'Menu', '定时任务', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1729851011777597442, '2023-12-06 21:26:01', '2023-12-06 21:26:03', 0, 2, '/system/operationLog', 'operationLog',
        null, '/system/operationLog/index', 6, 'Menu', '操作日志', null, 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1736922123682729985, '2023-12-19 09:31:19', '2023-12-19 09:31:19', 0, 0, '/note', 'note', '', '', 1, 'Notebook',
        '笔记', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component,
                                       sort, icon, title, link, hide, full_screen, affix, keep_alive)
VALUES (1736922287923286018, '2023-12-19 09:31:59', '2023-12-19 09:32:29', 0, 1736922123682729985,
        '/note/articleManage', 'articleManage', '', '/note/articleManage/index', 1, 'Menu', '笔记管理', '', 0, 0, 0, 1);


INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1, 1, 0, 'home', '0', '仪表盘', 0, '2023-11-21 11:22:16', '2023-11-21 11:22:17', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (2, 2, 0, 'system', '0', '系统管理', 1, '2023-11-20 14:44:54', '2023-11-20 14:44:56', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (3, 3, 2, 'menuManage', '0', '菜单管理', 2, '2023-11-20 14:45:01', '2023-11-20 14:45:00', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (4, 1726777800827920385, 1726777813389860866, 'add', '1', '新增', 1, '2023-11-21 10:07:26',
        '2023-11-21 10:07:27', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1726777813389860866, 1726777800827920385, 2, 'roleManage', '0', '角色管理', 3, '2023-11-21 09:41:27',
        '2023-11-21 09:41:27', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1726844583828115457, 1726844567755542529, 2, 'permCodeManage', '0', '权限字管理', 2, '2023-11-21 14:06:47',
        '2023-11-22 17:52:47', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1726963486956367873, 1726844567755542529, 1726844583828115457, 'add', '1', '新增', 1, '2023-11-21 21:59:15',
        '2023-11-21 21:59:15', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727204917185155074, 1726844567755542529, 1726844583828115457, 'bind', '1', '绑定权限资源', 2,
        '2023-11-22 13:58:37', '2023-11-22 13:58:37', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727204982586937345, 1726844567755542529, 1726844583828115457, 'edit', '1', '编辑', 3, '2023-11-22 13:58:52',
        '2023-11-22 13:58:52', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727205067416735745, 1726844567755542529, 1726844583828115457, 'delete', '1', '删除', 4, '2023-11-22 13:59:13',
        '2023-11-22 13:59:13', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727205402319327234, 1726777800827920385, 1726777813389860866, 'bind', '1', '绑定权限字', 2,
        '2023-11-22 14:00:32', '2023-11-22 14:00:32', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727205479653904385, 1726777800827920385, 1726777813389860866, 'delete', '1', '删除', 3, '2023-11-22 14:00:51',
        '2023-11-22 14:00:51', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727205578689810434, 1726777800827920385, 1726777813389860866, 'edit', '1', '编辑', 4, '2023-11-22 14:01:15',
        '2023-11-22 14:01:15', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727229151823278082, 1727229150502072321, 2, 'userManage', '0', '用户管理', 1, '2023-11-22 15:34:55',
        '2023-11-22 15:34:55', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727253387044503554, 1727229150502072321, 1727229151823278082, 'add', '1', '新增', 1, '2023-11-22 17:11:13',
        '2023-11-22 17:11:13', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727253464899174402, 1727229150502072321, 1727229151823278082, 'edit', '1', '编辑', 2, '2023-11-22 17:11:32',
        '2023-11-22 17:11:32', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727253531743797249, 1727229150502072321, 1727229151823278082, 'resetPassword', '1', '重置密码', 3,
        '2023-11-22 17:11:47', '2023-11-22 17:11:47', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727253646369931266, 1727229150502072321, 1727229151823278082, 'delete', '1', '删除', 4, '2023-11-22 17:12:15',
        '2023-11-22 17:12:15', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727521296681553921, 1727521295351959553, 2, 'dictManage', '0', '字典管理', 5, '2023-11-23 10:55:48',
        '2023-11-23 10:55:48', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727528750999953410, 1727521295351959553, 1727521296681553921, 'status', '1', '切换状态', 1,
        '2023-11-23 11:25:25', '2023-11-23 11:25:25', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727569426768756738, 1727521295351959553, 1727521296681553921, 'add', '1', '新增字典', 2, '2023-11-23 14:07:03',
        '2023-11-23 14:07:03', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727569516027740161, 1727521295351959553, 1727521296681553921, 'edit', '1', '编辑字典', 3,
        '2023-11-23 14:07:24', '2023-11-23 14:07:24', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727569631585009665, 1727521295351959553, 1727521296681553921, 'delete', '1', '删除字典', 4,
        '2023-11-23 14:07:52', '2023-11-23 14:07:52', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727569989279449089, 1727521295351959553, 1727521296681553921, 'addItem', '1', '新增字典项', 5,
        '2023-11-23 14:09:17', '2023-11-23 14:09:17', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727570066031017985, 1727521295351959553, 1727521296681553921, 'editItem', '1', '编辑字典项', 6,
        '2023-11-23 14:09:35', '2023-11-23 14:09:35', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1727570144833601537, 1727521295351959553, 1727521296681553921, 'deleteItem', '1', '删除字典项', 7,
        '2023-11-23 14:09:54', '2023-11-23 14:09:54', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1729053185926004738, 1729053179210924034, 2, 'systemLog', '0', '系统日志', 0, '2023-11-27 16:22:58',
        '2023-11-27 16:22:58', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1729851012285108225, 1729851011777597441, 0, 'timingTask', '0', '定时任务', 3, '2023-11-29 21:13:15',
        '2023-11-29 21:13:15', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1731580055766732802, 1729851011777597441, 1729851012285108225, 'add', '1', '新增', 1, '2023-12-04 15:43:51',
        '2023-12-04 15:43:51', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1731580360042516482, 1729851011777597441, 1729851012285108225, 'run', '1', '执行一次', 2, '2023-12-04 15:45:04',
        '2023-12-04 15:45:04', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1731580436622118913, 1729851011777597441, 1729851012285108225, 'edit', '1', '编辑', 3, '2023-12-04 15:45:22',
        '2023-12-04 15:45:22', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1731580493803065345, 1729851011777597441, 1729851012285108225, 'delete', '1', '删除', 4, '2023-12-04 15:45:36',
        '2023-12-04 15:45:36', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1731602641573679105, 1729851011777597441, 1729851012285108225, 'taskStatus', '1', '切换状态', 5,
        '2023-12-04 17:13:36', '2023-12-04 17:13:36', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1736922123779198977, 1736922123682729985, 0, 'note', '0', '笔记', 0, '2023-12-19 09:31:19',
        '2023-12-19 09:31:19', 0);
INSERT INTO `xiaochen-cloud`.sys_perm_code (id, menu_id, parent_id, perm_code, perm_code_type, show_name, show_order,
                                            created_at, updated_at, deleted)
VALUES (1736922287986200578, 1736922287923286018, 1736922123779198977, 'articleManage', '0', '笔记管理', 1,
        '2023-12-19 09:31:59', '2023-12-19 09:31:59', 0);

INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (1, '2023-08-30 09:47:18', '2023-09-13 13:00:14', 0, '性别', 'gender', 1, '性别字典xxxx');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (2, '2023-08-30 09:47:18', '2023-08-30 09:47:19', 1, '数据库int类型', 'int', 1, 'int类型对应的数据库类型');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (3, '2023-08-30 09:47:18', '2023-08-30 09:47:19', 0, '数据库时间日期类型', 'time.Time', 1, '数据库时间日期类型');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (4, '2023-08-30 09:47:18', '2023-08-30 09:47:19', 0, '数据库浮点型', 'float64', 1, '数据库浮点型');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (5, '2023-08-30 09:47:18', '2023-08-30 09:47:19', 0, '数据库字符串', 'string', 1, '数据库字符串');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (6, '2023-08-30 09:47:18', '2023-08-30 09:47:19', 0, '数据库bool类型', 'bool', 1, '数据库bool类型');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (7, '2023-09-13 13:00:29', '2023-09-13 13:00:29', 1, 'xxxxxx', 'xxxxxx', 1, 'xxxxxxxx');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (8, '2023-10-17 14:42:59', '2023-10-17 14:42:59', 0, '流程分类', 'process_category', 1, '流程分类');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (9, '2023-11-06 10:12:02', '2023-11-06 10:12:02', 0, '消息类型', 'NoticeSourceType', 1, '消息类型');
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (1727581702884204546, '2023-11-23 14:55:50', '2023-11-23 14:55:50', 0, '状态', 'enableType', 1, null);
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (1727582283208105986, '2023-11-23 14:58:08', '2023-11-23 14:58:08', 0, '权限字类型', 'PermCodeType', 1, null);
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (1729057735609188354, '2023-11-27 16:41:03', '2023-11-27 16:41:03', 0, '日志级别', 'LogLevel', 1, null);
INSERT INTO `xiaochen-cloud`.sys_dictionary (id, created_at, updated_at, deleted, name, encode, status, description)
VALUES (1731572342575894529, '2023-12-04 15:13:12', '2023-12-04 15:13:39', 0, '任务状态', 'TaskStatus', 1, null);

INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1, '2023-08-30 09:47:19', '2023-11-23 14:55:02', 0, '男', '1', 1, null, 1, 1);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (2, '2023-08-30 09:47:19', '2023-11-23 14:55:07', 0, '女', '0', 1, null, 2, 1);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (3, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'smallint', '1', 1, null, 1, 2);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (4, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'mediumint', '2', 1, null, 2, 2);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (5, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'int', '3', 1, null, 3, 2);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (6, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'bigint', '4', 1, null, 4, 2);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (7, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'date', '0', 1, null, 0, 3);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (8, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'time', '1', 1, null, 1, 3);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (9, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'year', '2', 1, null, 2, 3);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (10, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'datetime', '3', 1, null, 3, 3);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (11, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'timestamp', '5', 1, null, 5, 3);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (12, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'float', '0', 1, null, 0, 4);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (13, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'double', '1', 1, null, 1, 4);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (14, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'decimal', '2', 1, null, 2, 4);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (15, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'char', '0', 1, null, 0, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (16, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'varchar', '1', 1, null, 1, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (17, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'tinyblob', '2', 1, null, 2, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (18, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'tinytext', '3', 1, null, 3, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (19, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'text', '4', 1, null, 4, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (20, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'blob', '5', 1, null, 5, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (21, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'mediumblob', '6', 1, null, 6, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (22, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'mediumtext', '7', 1, null, 7, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (23, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'longblob', '8', 1, null, 8, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (24, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'longtext', '9', 1, null, 9, 5);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (25, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 'tinyint', '0', 1, null, 0, 6);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (26, '2023-09-13 17:21:11', '2023-09-13 17:21:50', 1, '未知', '4', 1, null, 3, 1);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (27, '2023-10-17 14:44:49', '2023-10-17 14:44:49', 0, '请假流程', '1', 1, null, 1, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (28, '2023-10-17 14:47:15', '2023-10-17 14:47:09', 0, '门禁权限审批流', '2', 1, null, 2, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (29, '2023-10-17 14:47:16', '2023-10-17 14:47:10', 0, '出差申请', '3', 1, null, 3, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (30, '2023-10-17 14:47:17', '2023-10-17 14:47:11', 0, '出差申请', '4', 1, null, 4, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (31, '2023-10-17 14:47:18', '2023-10-17 14:47:12', 0, '辞职流程', '5', 1, null, 5, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (32, '2023-10-17 14:47:18', '2023-10-17 14:47:12', 0, '人资审核流程', '6', 1, null, 6, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (33, '2023-10-17 14:47:19', '2023-10-17 14:47:13', 0, '需求立项', '7', 1, null, 7, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (34, '2023-10-17 14:47:19', '2023-10-17 14:47:14', 0, '货物审批流', '8', 1, null, 8, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (35, '2023-10-17 14:47:20', '2023-10-17 14:47:14', 0, '访客审批流程', '9', 1, null, 9, 8);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (36, '2023-11-06 10:12:24', '2023-11-23 14:41:09', 0, '系统消息', 'system', 1, null, 1, 9);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (37, '2023-11-06 10:12:33', '2023-11-06 10:12:33', 0, '工作流', 'flowable', 1, null, 2, 9);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1727578076585435137, '2023-11-23 14:41:25', '2023-11-23 14:41:25', 0, '测试', 'test', 1, null, 1, 9);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1727581904990937089, '2023-11-23 14:56:38', '2023-11-23 14:56:38', 0, '启用', '1', 1, 'success', 1,
        1727581702884204546);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1727581965078536193, '2023-11-23 14:56:52', '2023-11-23 14:56:52', 0, '禁用', '0', 1, 'danger', 2,
        1727581702884204546);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1727582549206671361, '2023-11-23 14:59:11', '2023-11-23 14:59:11', 0, '菜单', '0', 1, 'success', 1,
        1727582283208105986);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1727582618999889922, '2023-11-23 14:59:28', '2023-11-23 14:59:28', 0, '按钮', '1', 1, 'danger', 2,
        1727582283208105986);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1729057854576427009, '2023-11-27 16:41:32', '2023-11-27 16:42:05', 0, '追踪', 'TRACE', 1, 'info', 1,
        1729057735609188354);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1729057950504353793, '2023-11-27 16:41:54', '2023-11-27 16:44:05', 0, '调试', 'DEBUG', 1, '', 2,
        1729057735609188354);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1729058138925072386, '2023-11-27 16:42:39', '2023-12-07 15:36:55', 0, '信息', 'INFO', 1, 'success', 3,
        1729057735609188354);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1729058214619676673, '2023-11-27 16:42:57', '2023-11-27 16:43:34', 0, '警告', 'WARN', 1, 'warning', 4,
        1729057735609188354);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1729058443372822529, '2023-11-27 16:43:52', '2023-11-27 16:43:52', 0, '错误', 'ERROR', 1, 'danger', 5,
        1729057735609188354);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1731572607387471873, '2023-12-04 15:14:15', '2023-12-04 15:14:15', 0, 'RUNNING', '1', 1, 'success', 1,
        1731572342575894529);
INSERT INTO `xiaochen-cloud`.sys_dictionary_detail (id, created_at, updated_at, deleted, label, value, status, tag_type,
                                                    sort, sys_dictionary_id)
VALUES (1731572708017213442, '2023-12-04 15:14:39', '2023-12-04 15:14:39', 0, 'STOP', '0', 1, 'info', 2,
        1731572342575894529);

INSERT INTO `xiaochen-cloud`.job_info (id, created_at, updated_at, deleted, job_group, job_desc, author, alarm_email,
                                       schedule_type, schedule_conf, misfire_strategy, executor_route_strategy,
                                       executor_handler, executor_param, executor_block_strategy, executor_timeout,
                                       executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime,
                                       trigger_status)
VALUES (1729781272649256961, '2023-11-29 16:36:08', '2023-12-07 23:58:03', 0, 'xiaochen-log-server',
        'xiaochen-log-server', null, null, 'Cron', '0/10 * * * * ?', 'DO_NOTHING', null, 'cleanOverdueLog', '', '', 0,
        0, 'BEAN', '', '', null, 0);
INSERT INTO `xiaochen-cloud`.job_info (id, created_at, updated_at, deleted, job_group, job_desc, author, alarm_email,
                                       schedule_type, schedule_conf, misfire_strategy, executor_route_strategy,
                                       executor_handler, executor_param, executor_block_strategy, executor_timeout,
                                       executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime,
                                       trigger_status)
VALUES (1729781272649256962, '2023-11-30 10:57:09', '2023-12-07 23:57:51', 0, 'xiaochen-auth', 'xiaochen-auth', null,
        null, 'Cron', '0/50 * * * * ?', 'DO_NOTHING', null, 'cleanJwtBlackListJob', null, null, 0, 0, 'BEAN', null,
        null, null, 0);

INSERT INTO `xiaochen-cloud`.note_reptile_parse_class (id, created_at, updated_at, deleted, class_name, site_url,
                                                       content, status)
VALUES (1, '2023-12-13 16:56:37', '2023-12-13 16:56:38', 0, 'JueJinParser', 'https://juejin.cn', 'package com.zvz09.xiaochen.note.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 掘金
 *
 * @author zvz09
 */
@Component
public class JueJinParser implements ReptileDataParserStrategy {


    @Override
    public String getBaseUrl() {
        return "https://juejin.cn";
    }

    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementById("article-root");
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        Elements tagElements = document.getElementsByAttributeValue("itemprop", "keywords");
        if (tagElements != null) {
            List<String> tags = new ArrayList<>();
            tagElements.forEach(item -> {
                if (StringUtils.isNotBlank(item.attr("content"))) {
                    tags.addAll(List.of(item.attr("content").split(",")));
                }
            });
            dto.setTags(tags);
        }
        return dto;
    }
}
', 0);
INSERT INTO `xiaochen-cloud`.note_reptile_parse_class (id, created_at, updated_at, deleted, class_name, site_url,
                                                       content, status)
VALUES (1737774513654931457, '2023-12-21 17:58:25', '2023-12-21 17:58:25', 0, 'CloudTencentParser',
        'https://cloud.tencent.com', 'package com.zvz09.xiaochen.note.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author zvz09
 */
public class CloudTencentParser implements ReptileDataParserStrategy {
    @Override
    public String getBaseUrl() {
        return "https://cloud.tencent.com";
    }

    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementsByClass("mod-content").get(0);
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        return dto;
    }
}
', null);
INSERT INTO `xiaochen-cloud`.note_reptile_parse_class (id, created_at, updated_at, deleted, class_name, site_url,
                                                       content, status)
VALUES (1737774591962587138, '2023-12-21 17:58:44', '2023-12-21 17:58:44', 0, 'CnblogParser', 'https://www.cnblogs.com', 'package com.zvz09.xiaochen.note.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 博客园
 *
 * @author zvz09
 */
public class CnblogParser implements ReptileDataParserStrategy {
    @Override
    public String getBaseUrl() {
        return "https://www.cnblogs.com";
    }

    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementsByClass("postBody").get(0);
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        return dto;
    }
}
', null);
INSERT INTO `xiaochen-cloud`.note_reptile_parse_class (id, created_at, updated_at, deleted, class_name, site_url,
                                                       content, status)
VALUES (1737774640893337602, '2023-12-21 17:58:55', '2023-12-21 17:58:55', 0, 'CSDNParser', 'https://blog.csdn.net', 'package com.zvz09.xiaochen.note.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * CSDN
 *
 * @author zvz09
 */
public class CSDNParser implements ReptileDataParserStrategy {

    @Override
    public String getBaseUrl() {
        return "https://blog.csdn.net";
    }

    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementById("article_content");
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        Elements tagElements = document.getElementsByClass("tag-link");
        if (tagElements != null) {
            List<String> tags = new ArrayList<>();
            tagElements.forEach(item -> {
                if (StringUtils.isNotBlank(item.text())) {
                    tags.add(item.text());
                }
            });
            dto.setTags(tags);
        }
        return dto;
    }
}
', null);
