package com.zvz09.xiaochen.note.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.note.domain.entity.Category;

import java.util.List;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
public interface ICategoryService extends IService<Category> {

    IPage<Category> selectCategoryList(BasePage basePage);

    Category getCategoryById(Long id);

    void insertCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Long id);

    void deleteBatch(List<Long> ids);

    void top(Long id);
}
