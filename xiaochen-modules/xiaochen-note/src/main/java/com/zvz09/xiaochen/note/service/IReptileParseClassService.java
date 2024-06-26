package com.zvz09.xiaochen.note.service;

import com.zvz09.xiaochen.common.web.service.BaseService;
import com.zvz09.xiaochen.note.constant.OperateAction;
import com.zvz09.xiaochen.note.domain.entity.ReptileParseClass;

/**
 * <p>
 * 爬取数据类 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
public interface IReptileParseClassService extends BaseService<ReptileParseClass> {

    void operate(Long id, OperateAction operate);
}
