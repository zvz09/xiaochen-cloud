package com.zvz09.xiaochen.system.service;

import com.zvz09.xiaochen.common.web.service.BaseService;
import com.zvz09.xiaochen.system.api.domain.entity.SysMicro;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zvz09
 * @since 2024-01-29
 */
public interface ISysMicroService extends BaseService<SysMicro> {

    List<SysMicro> listTree();
}
