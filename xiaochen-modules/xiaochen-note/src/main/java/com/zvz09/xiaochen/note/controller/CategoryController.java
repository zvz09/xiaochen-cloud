package com.zvz09.xiaochen.note.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.common.core.annotation.BizNo;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.note.domain.entity.Category;
import com.zvz09.xiaochen.note.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 博客分类表 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@RestController
@Tag(name = "分类管理")
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService blogCategoryService;

    @PostMapping("/page")
    @Operation(summary = "分类列表")
    public IPage<Category> page(@RequestBody BasePage basePage) {
        return blogCategoryService.selectCategoryList(basePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "分类详情")
    @BizNo(spEl = "{#id}")
    public Category getCategoryById(@PathVariable(value = "id") Long id) {
        return blogCategoryService.getCategoryById(id);
    }

    @PostMapping
    @Operation(summary = "新增分类")
    @BizNo(spEl = "{#category.name}")
    public void insertCategory(@RequestBody Category category) {
        blogCategoryService.insertCategory(category);
    }

    @PutMapping
    @Operation(summary = "修改分类")
    @BizNo(spEl = "{#category.name}")
    public void update(@RequestBody Category category) {
        blogCategoryService.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    @BizNo(spEl = "{#id}")
    public void deleteCategory(@PathVariable(value = "id") Long id) {
        blogCategoryService.deleteCategory(id);
    }

    @DeleteMapping
    @Operation(summary = "批量删除分类")
    @BizNo(spEl = "{#ids}")
    public void deleteBatch(@RequestBody List<Long> ids) {
        blogCategoryService.deleteBatch(ids);
    }

    @PutMapping("/{id}/top")
    @Operation(summary = "置顶分类")
    @BizNo(spEl = "{#id}")
    public void top(@PathVariable(value = "id") Long id) {
        blogCategoryService.top(id);
    }

}

