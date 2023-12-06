package com.zvz09.xiaochen.blog.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.blog.domain.entity.Tags;
import com.zvz09.xiaochen.blog.service.ITagsService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.log.annotation.BizNo;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@RestController
@RequestMapping("/tags")
@Tag(name = "标签管理")
@RequiredArgsConstructor
public class TagsController {

    private final ITagsService tagsService;

    @PostMapping("/page")
    @Operation(summary = "标签列表")
    public IPage<Tags> list(@RequestBody BasePage basePage) {
        return tagsService.listTags(basePage);
    }

    @PostMapping
    @Operation(summary = "新增标签")
    public void insert(@RequestBody Tags tags) {
        tagsService.insertTag(tags);
    }

    @GetMapping("/{id}")
    @Operation(summary = "标签详情")
    @BizNo(spEl = "{#id}")
    public Tags getTagsById(@PathVariable(value = "id") Long id) {
        return tagsService.getTagsById(id);
    }

    @PutMapping
    @Operation(summary = "修改标签")
    @BizNo(spEl = "{#tags.name}")
    public void update(@RequestBody Tags tags) {
        tagsService.updateTag(tags);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    @BizNo(spEl = "{#id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        tagsService.deleteById(id);
    }

    @DeleteMapping()
    @Operation(summary = "批量删除标签")
    @BizNo(spEl = "{#ids}")
    public void deleteBatch(@RequestBody List<Long> ids) {
        tagsService.deleteBatch(ids);
    }

    @RequestMapping(value = "/{id}/top", method = RequestMethod.GET)
    @Operation(summary = "置顶标签")
    @BizNo(spEl = "{#id}")
    public void top(@PathVariable(value = "id") Long id) {
        tagsService.top(id);
    }
}

