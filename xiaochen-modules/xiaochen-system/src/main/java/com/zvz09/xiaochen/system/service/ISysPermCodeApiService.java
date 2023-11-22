package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.bo.PermCodeApiBo;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCodeApi;

import java.util.List;

/**
 * <p>
 * 系统权限字和api接口关联表 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
public interface ISysPermCodeApiService extends IService<SysPermCodeApi> {

    void deleteByPermCodeId(Long id);

    List<Long> getApiIdByPermCodeId(Long permCodeId);

    List<PermCodeApiBo> getPermCodeApiInfo(List<Long> permCodeIds);
}
