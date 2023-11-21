package com.zvz09.xiaochen.system.converter;

import com.zvz09.xiaochen.common.core.converter.TreeConverter;
import com.zvz09.xiaochen.system.api.domain.entity.SysMenu;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;

import java.util.List;

/**
 * @author lizili-YF0033
 */
public class SysMenuTreeConverter implements TreeConverter<SysMenu, SysMenuVo> {

    @Override
    public SysMenuVo convert(SysMenu item) {
        return new SysMenuVo(item);
    }

    @Override
    public boolean isChildOf(SysMenu item, SysMenu parent) {
        return item.getParentId().equals(parent.getId());
    }

    @Override
    public void setChildren(SysMenuVo parent, List<SysMenuVo> children) {
        parent.setChildren(children);
    }
}

