package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuBtn;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysBaseMenuBtnService extends IService<SysBaseMenuBtn> {

    void deleteByMenuId(Long sysBaseMenuId);

    void deleteByMenuId(List<Long> sysBaseMenuIds);

    List<SysBaseMenuBtn> listByMenuIds(List<Long> menuIds);
}
