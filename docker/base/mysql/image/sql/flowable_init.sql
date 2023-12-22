CREATE DATABASE IF NOT EXISTS `flowable`;

CREATE USER 'flowable'@'%' IDENTIFIED BY '!QAZ2wsx#EDC';
GRANT ALL PRIVILEGES ON `flowable`.* TO 'flowable'@'%';

drop table if exists `sys_form`;
CREATE TABLE `sys_form` (
                            `id` bigint(20) DEFAULT NULL,
                            `form_name` varchar(50) DEFAULT NULL,
                            `form_content` longtext,
                            `thumbnail` longtext,
                            `created_at` datetime DEFAULT NULL,
                            `updated_at` datetime DEFAULT NULL,
                            `deleted` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `flowable_copy`;
CREATE TABLE `flowable_copy` (
                                 `id` bigint(20) DEFAULT NULL,
                                 `created_at` datetime DEFAULT NULL,
                                 `updated_at` datetime DEFAULT NULL,
                                 `deleted` tinyint(4) DEFAULT NULL,
                                 `title` varchar(255) DEFAULT NULL,
                                 `process_id` varchar(64) DEFAULT NULL,
                                 `process_name` varchar(255) DEFAULT NULL,
                                 `category_id` varchar(255) DEFAULT NULL,
                                 `deployment_id` varchar(64) DEFAULT NULL,
                                 `instance_id` varchar(64) DEFAULT NULL,
                                 `task_id` varchar(64) DEFAULT NULL,
                                 `user_id` bigint(20) DEFAULT NULL,
                                 `originator_id` bigint(20) DEFAULT NULL,
                                 `originator_name` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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