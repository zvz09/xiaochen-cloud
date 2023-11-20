/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.handler
 * @className com.zvz09.xiaochen.common.web.handler.MybatisplusMetaObjectHandler
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MybatisplusMetaObjectHandler
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/31 16:52
 */
@Slf4j
@Component
public class MybatisplusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        this.fillStrategy(metaObject, "deleted", false);
        this.fillStrategy(metaObject, "createdAt", LocalDateTime.now());
        this.fillStrategy(metaObject, "updatedAt", LocalDateTime.now());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        this.fillStrategy(metaObject, "updatedAt", LocalDateTime.now());
    }
}
 