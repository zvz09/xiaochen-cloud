package com.zvz09.xiaochen.system.converter;

import com.zvz09.xiaochen.common.core.converter.TreeConverter;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCode;
import com.zvz09.xiaochen.system.api.domain.vo.SysPermCodeVo;

import java.util.Comparator;
import java.util.List;

/**
 * @author lizili-YF0033
 */
public class SysPermCodeTreeConverter implements TreeConverter<SysPermCode, SysPermCodeVo> {

    private final Comparator<SysPermCodeVo> comparator = Comparator.comparing(SysPermCodeVo::getShowOrder);

    @Override
    public SysPermCodeVo convert(SysPermCode item) {
        return new SysPermCodeVo(item);
    }

    @Override
    public boolean isChildOf(SysPermCode item, SysPermCode parent) {
        return item.getParentId().equals(parent.getId());
    }

    @Override
    public void setChildren(SysPermCodeVo parent, List<SysPermCodeVo> children) {
        children.sort(comparator);
        parent.setChildren(children);
    }

}

