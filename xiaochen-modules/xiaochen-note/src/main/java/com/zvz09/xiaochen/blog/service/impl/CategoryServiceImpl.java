package com.zvz09.xiaochen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.blog.domain.entity.Category;
import com.zvz09.xiaochen.blog.mapper.CategoryMapper;
import com.zvz09.xiaochen.blog.service.ICategoryService;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public IPage<Category> selectCategoryList(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()),
                new LambdaQueryWrapper<Category>()
                        .like(StringUtils.isNotBlank(basePage.getKeyword()), Category::getName, basePage.getKeyword()));
    }

    @Override
    public Category getCategoryById(Long id) {
        return this.getById(id);
    }

    @Override
    public void insertCategory(Category category) {
        if (this.count(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName())) > 0) {
            throw new BusinessException("该分类名称已存在");
        }
        this.save(category);
    }

    @Override
    public void updateCategory(Category category) {
        if (this.getById(category.getId()) != null) {
            this.updateById(category);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        this.removeById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            this.removeBatchByIds(ids);
        }
    }

    @Override
    public void top(Long id) {
        Category category = baseMapper.selectOne(new LambdaQueryWrapper<Category>().orderByDesc(Category::getSort).last("LIMIT 1"));
        if (!category.getId().equals(id)) {
            Category entity = Category.builder()
                    .sort(category.getSort() + 1).id(id).build();
            this.updateById(entity);
        }
    }
}
