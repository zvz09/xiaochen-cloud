CREATE DATABASE IF NOT EXISTS `xiaochen-cloud`;

CREATE USER 'xiaochen'@'%' IDENTIFIED BY '!QAZ2wsx#EDC';
GRANT ALL PRIVILEGES ON `xiaochen-cloud`.* TO 'xiaochen'@'%';

USE `xiaochen-cloud`;
drop table if exists `blog_article`;
CREATE TABLE `blog_article`
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
    `is_stick`     int(11)               DEFAULT '0' COMMENT '是否置顶 0否 1是',
    `is_publish`   int(11)               DEFAULT '0' COMMENT '是否发布 0：下架 1：发布',
    `is_original`  int(11)               DEFAULT '1' COMMENT '是否原创  0：转载 1:原创',
    `original_url` varchar(255)          DEFAULT NULL COMMENT '转载地址',
    `quantity`     bigint(20)            DEFAULT '0' COMMENT '文章阅读量',
    `is_carousel`  int(11)               DEFAULT '0' COMMENT '是否首页轮播',
    `is_recommend` int(11)               DEFAULT '0' COMMENT '是否推荐',
    `keywords`     varchar(200)          DEFAULT NULL COMMENT '关键词',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='博客文章表';

drop table if exists `blog_article_tag`;
CREATE TABLE `blog_article_tag`
(
    `id`         bigint(20) NOT NULL,
    `article_id` bigint(20) NOT NULL COMMENT '文章id',
    `tag_id`     bigint(20) NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `fk_article_tag_1` (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;

drop table if exists `blog_category`;
CREATE TABLE `blog_category`
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

drop table if exists `blog_tags`;
CREATE TABLE `blog_tags`
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

drop table if exists `casbin_rule`;
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

drop table if exists `event_remind`;
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

drop table if exists `exa_file`;
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

drop table if exists `job_info`;
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

drop table if exists `job_log`;
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
  AUTO_INCREMENT = 586
  DEFAULT CHARSET = utf8mb4;

drop table if exists `sys_api`;
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

drop table if exists `sys_auto_code`;
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

drop table if exists `sys_auto_code_historiy`;
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

drop table if exists `sys_auto_code_template`;
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

drop table if exists `sys_data_authority_id`;
CREATE TABLE `sys_data_authority_id`
(
    `sys_authority_authority_id`     bigint(20) DEFAULT NULL,
    `data_authority_id_authority_id` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

drop table if exists `sys_department`;
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

drop table if exists `sys_deploy_form`;
CREATE TABLE `sys_deploy_form`
(
    `form_id`   bigint(20)   DEFAULT NULL,
    `deploy_id` varchar(50)  DEFAULT NULL,
    `form_key`  varchar(64)  DEFAULT NULL,
    `node_key`  varchar(64)  DEFAULT NULL,
    `form_name` varchar(64)  DEFAULT NULL,
    `node_name` varchar(255) DEFAULT NULL,
    `content`   longtext
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

drop table if exists `sys_dictionary`;
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

drop table if exists `sys_dictionary_detail`;
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

drop table if exists `sys_gencode_history`;
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

drop table if exists `sys_menu`;
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

drop table if exists `sys_operation_record`;
CREATE TABLE `sys_operation_record`
(
    `id`            bigint(20)   DEFAULT NULL,
    `created_at`    datetime     DEFAULT NULL,
    `updated_at`    datetime     DEFAULT NULL,
    `deleted`       tinyint(4)   DEFAULT NULL,
    `ip`            varchar(191) DEFAULT NULL,
    `method`        varchar(191) DEFAULT NULL,
    `path`          varchar(191) DEFAULT NULL,
    `status`        bigint(20)   DEFAULT NULL,
    `elapsed_time`  bigint(20)   DEFAULT NULL,
    `agent`         varchar(191) DEFAULT NULL,
    `error_message` varchar(191) DEFAULT NULL,
    `body`          text,
    `resp`          text,
    `username`      varchar(191) DEFAULT NULL,
    `nick_name`     varchar(191) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

drop table if exists `sys_perm_code`;
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

drop table if exists `sys_perm_code_api`;
CREATE TABLE `sys_perm_code_api`
(
    `perm_code_id` bigint(20) NOT NULL COMMENT '权限字Id',
    `api_id`       bigint(20) NOT NULL COMMENT '权限id',
    PRIMARY KEY (`perm_code_id`, `api_id`) USING BTREE,
    KEY `idx_perm_id` (`api_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='系统权限字和api接口关联表';

drop table if exists `sys_position`;
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

drop table if exists `sys_role`;
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


drop table if exists `sys_role_perm_code`;
CREATE TABLE `sys_role_perm_code`
(
    `role_id`      bigint(20) NOT NULL COMMENT '角色Id',
    `perm_code_id` bigint(20) NOT NULL COMMENT '权限字Id',
    PRIMARY KEY (`role_id`, `perm_code_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='角色与系统权限字对应关系表';


drop table if exists `sys_user`;
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


drop table if exists `sys_user_dept`;
CREATE TABLE `sys_user_dept`
(
    `user_id` bigint(20) DEFAULT NULL,
    `dept_id` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


drop table if exists `sys_user_position`;
CREATE TABLE `sys_user_position`
(
    `user_id`     bigint(20) DEFAULT NULL,
    `position_id` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


drop table if exists `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `sys_user_id`   bigint(20) DEFAULT NULL,
    `sys_role_code` bigint(20) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

drop table if exists `user_system_notice`;
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

INSERT INTO `xiaochen-cloud`.sys_role (created_at, updated_at, deleted, role_code, role_name, id) VALUES ('2023-08-30 09:47:18', '2023-08-30 09:47:19', 0, '888', '超级管理员', 1);

INSERT INTO `xiaochen-cloud`.sys_user_role (sys_user_id, sys_role_code) VALUES (1, 888);

INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1, '2023-08-30 09:47:19', '2023-08-30 09:47:19', 0, 0, '/home/index', 'home', '', '/home/index', 1, 'HomeFilled', '仪表盘', '', 0, 0, 1, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (2, '2023-11-16 17:11:40', '2023-11-16 17:11:42', 0, 0, '/system', 'system', '/system/accountManage', null, 2, 'Tools', '系统管理', null, 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (3, '2023-11-16 17:13:20', '2023-11-22 17:09:54', 0, 2, '/system/menuMange', 'menuMange', null, '/system/menuMange/index', 2, 'Menu', '菜单管理', null, 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1725407120861429761, '2023-11-17 14:54:49', '2023-11-17 15:55:46', 1, 0, '/dataScreen', 'dataScreen', '', '/dataScreen/index', null, 'Histogram', '数据大屏1111', '', 0, 1, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1726777800827920385, '2023-11-21 09:41:24', '2023-11-22 17:10:00', 0, 2, '/system/roleManage', 'roleManage', '', '/system/roleManage/index', 3, 'Menu', '角色管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1726844567755542529, '2023-11-21 14:06:43', '2023-11-22 17:56:02', 0, 2, '/system/permCodeManage', 'permCodeManage', '', '/system/permCodeManage/index', 4, 'Menu', '权限字管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1727229150502072321, '2023-11-22 15:34:55', '2023-11-22 17:09:51', 0, 2, '/system/userManage', 'userManage', '', '/system/userManage/index', 1, 'Menu', '用户管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1727521295351959553, '2023-11-23 10:55:47', '2023-11-23 10:55:59', 0, 2, '/system/dictManage', 'dictManage', '', '/system/dictManage/index', 5, 'Menu', '字典管理', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1729053179210924034, '2023-11-27 16:22:57', '2023-11-27 16:22:57', 0, 2, '/system/systemLog', 'systemLog', '', '/system/systemLog/index', 7, 'Menu', '系统日志', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1729851011777597441, '2023-11-29 21:13:15', '2023-11-29 21:18:49', 0, 2, '/system/timingTask', 'timingTask', '', '/system/timingTask/index', 3, 'Menu', '定时任务', '', 0, 0, 0, 1);
INSERT INTO `xiaochen-cloud`.sys_menu (id, created_at, updated_at, deleted, parent_id, path, name, redirect, component, sort, icon, title, link, hide, full_screen, affix, keep_alive) VALUES (1729851011777597442, '2023-12-06 21:26:01', '2023-12-06 21:26:03', 0, 2, '/system/operationLog', 'operationLog', null, '/system/operationLog/index', 6, 'Menu', '操作日志', null, 0, 0, 0, 1);
