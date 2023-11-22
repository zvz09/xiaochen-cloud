package com.zvz09.xiaochen.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvz09.xiaochen.system.api.domain.bo.PermCodeApiBo;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCodeApi;

import java.util.List;

/**
 * <p>
 * 系统权限字和api接口关联表 Mapper 接口
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
public interface SysPermCodeApiMapper extends BaseMapper<SysPermCodeApi> {

    List<PermCodeApiBo> selectByPermCodeIds(List<Long> permCodeIds);

    List<PermCodeApiBo> selectByApiIds(List<Long> apiIds);
}
