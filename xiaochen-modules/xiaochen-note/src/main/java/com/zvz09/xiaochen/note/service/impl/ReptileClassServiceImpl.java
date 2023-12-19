package com.zvz09.xiaochen.note.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.note.domain.entity.ReptileClass;
import com.zvz09.xiaochen.note.mapper.ReptileClassMapper;
import com.zvz09.xiaochen.note.service.IReptileClassService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 爬取数据类 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@Service
public class ReptileClassServiceImpl extends ServiceImpl<ReptileClassMapper, ReptileClass> implements IReptileClassService {

    @Override
    public IPage<ReptileClass> page(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()));
    }
}