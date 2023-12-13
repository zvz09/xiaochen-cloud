package com.zvz09.xiaochen.common.core.util;

import com.zvz09.xiaochen.common.core.converter.TreeConverter;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zvz09
 */
public class TreeBuilder<T, V> {

    private final Predicate<T> isRoot;

    public TreeBuilder(Predicate<T> isRoot) {
        this.isRoot = isRoot;
    }

    public List<V> buildTree(List<T> itemList, TreeConverter<T, V> converter) {
       return this.buildTree(itemList, converter, null);
    }

    public List<V> buildTree(List<T> itemList, TreeConverter<T, V> converter, Comparator<V> comparator) {
        if (itemList == null || itemList.isEmpty()) {
            return null;
        }

        List<V> list = itemList.stream()
                .filter(isRoot)
                .map(item -> buildTreeItem(item, itemList, converter,comparator))
                .collect(Collectors.toList());

        if (comparator != null) {
            list.sort(comparator);
        }
        return list;
    }

    private V buildTreeItem(T item, List<T> allItems, TreeConverter<T, V> converter,Comparator<V> comparator) {
        V convertedItem = converter.convert(item);
        List<V> children = getChildren(item, allItems, converter,comparator);
        converter.setChildren(convertedItem, children);
        return convertedItem;
    }

    private List<V> getChildren(T parent, List<T> allItems, TreeConverter<T, V> converter,Comparator<V> comparator) {
        List<V> children = allItems.stream()
                .filter(item -> converter.isChildOf(item, parent))
                .map(item -> buildTreeItem(item, allItems, converter, comparator))
                .collect(Collectors.toList());

        // 如果comparator不为null，则对children进行排序
        if (comparator != null) {
            children.sort(comparator);
        }
        return children;
    }
}

