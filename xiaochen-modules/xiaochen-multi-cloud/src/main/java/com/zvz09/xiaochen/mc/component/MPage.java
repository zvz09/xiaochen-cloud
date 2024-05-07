package com.zvz09.xiaochen.mc.component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MPage<T> extends Page<T> {
    private String nextToken;

    public MPage(Integer pageNumber, Integer pageSize) {
        super(pageNumber,pageSize);
    }
}
