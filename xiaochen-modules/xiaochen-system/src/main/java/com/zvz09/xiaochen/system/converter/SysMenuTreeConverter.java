package com.zvz09.xiaochen.system.converter;

import com.zvz09.xiaochen.common.core.converter.TreeConverter;
import com.zvz09.xiaochen.system.api.domain.entity.SysMenu;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;

import java.util.Comparator;
import java.util.List;

/**
 * @author zvz09
 */
public class SysMenuTreeConverter implements TreeConverter<SysMenu, SysMenuVo> {

    private final Comparator<SysMenuVo> comparator = Comparator.comparing(SysMenuVo::getSort);
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
        children.sort(comparator);
        parent.setChildren(children);
    }
}

