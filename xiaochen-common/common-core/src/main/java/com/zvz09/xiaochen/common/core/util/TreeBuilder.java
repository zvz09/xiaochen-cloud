package com.zvz09.xiaochen.common.core.util;

import com.zvz09.xiaochen.common.core.converter.TreeConverter;

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
        if (itemList == null || itemList.isEmpty()) {
            return null;
        }

        List<V> treeItems = itemList.stream()
                .filter(isRoot)
                .map(item -> buildTreeItem(item, itemList, converter))
                .collect(Collectors.toList());

        return treeItems;
    }

    private V buildTreeItem(T item, List<T> allItems, TreeConverter<T, V> converter) {
        V convertedItem = converter.convert(item);
        List<V> children = getChildren(item, allItems, converter);
        converter.setChildren(convertedItem, children);
        return convertedItem;
    }

    private List<V> getChildren(T parent, List<T> allItems, TreeConverter<T, V> converter) {
        return allItems.stream()
                .filter(item -> converter.isChildOf(item, parent))
                .map(item -> buildTreeItem(item, allItems, converter))
                .collect(Collectors.toList());
    }
}

