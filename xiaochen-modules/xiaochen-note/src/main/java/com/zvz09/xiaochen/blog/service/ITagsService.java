package com.zvz09.xiaochen.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.blog.domain.entity.Tags;
import com.zvz09.xiaochen.common.core.page.BasePage;

import java.util.List;

/**
 * <p>
 * 博客标签表 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
public interface ITagsService extends IService<Tags> {

    IPage<Tags> listTags(BasePage basePage);

    void insertTag(Tags tags);

    Tags getTagsById(Long id);

    void updateTag(Tags tags);

    void deleteById(Long id);

    void deleteBatch(List<Long> ids);

    void top(Long id);
}
