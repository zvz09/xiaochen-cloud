package com.zvz09.xiaochen.common.core.converter;

import java.util.List;

/**
 * @author lizili-YF0033
 */
public interface TreeConverter<T, V> {
    V convert(T item);

    boolean isChildOf(T item, T parent);

    void setChildren(V parent, List<V> children);
}

