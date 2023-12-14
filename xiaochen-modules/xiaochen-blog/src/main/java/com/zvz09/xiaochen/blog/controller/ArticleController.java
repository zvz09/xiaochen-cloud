package com.zvz09.xiaochen.blog.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.blog.domain.entity.Article;
import com.zvz09.xiaochen.blog.service.IArticleService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 博客文章表 前端控制器
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Tag(name = "后台文章管理")
public class ArticleController {

    private final IArticleService articleService;

    @PostMapping(value = "/page")
    @Operation(summary = "文章列表")
    public IPage<Article> selectArticleList(@RequestBody BasePage basePage) {
        return articleService.selectArticleList(basePage);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "文章详情")
    @BizNo(spEl = "{#id}")
    public Article selectArticleById(@PathVariable(value = "id") Long id) {
        return articleService.selectArticleById(id);
    }

    @PostMapping(value = "")
    @Operation(summary = "保存文章")
    @BizNo(spEl = "{#articleDTO.title}")
    public void insertArticle(@RequestBody ArticleDTO articleDTO) {
        articleService.insertArticle(articleDTO);
    }

    @PutMapping("")
    @Operation(summary = "修改文章")
    @BizNo(spEl = "{#articleDTO.title}")
    public void updateArticle(@RequestBody ArticleDTO articleDTO) {
        articleService.updateArticle(articleDTO);
    }


    @DeleteMapping(value = "")
    @Operation(summary = "批量删除文章")
    @BizNo(spEl = "{#ids}")
    public void deleteBatchArticle(@RequestBody List<Long> ids) {
        articleService.deleteBatchArticle(ids);
    }

    @PutMapping(value = "/{id}/top")
    @Operation(summary = "置顶文章")
    @BizNo(spEl = "{#id}")
    public void topArticle(@PathVariable(value = "id") Long id) {
        articleService.topArticle(id);
    }

    @PutMapping(value = "/{id}/pubOrShelf")
    @Operation(summary = "发布或下架文章")
    @BizNo(spEl = "{#id}")
    public void psArticle(@PathVariable(value = "id") Long id) {
        articleService.psArticle(id);
    }

    @PostMapping(value = "/baiduSeo")
    @Operation(summary = "批量文章SEO(TODO)")
    @BizNo(spEl = "{#ids}")
    public void seoBatch(@RequestBody List<Long> ids) {
        articleService.seoBatch(ids);
    }

    @GetMapping(value = "/reptile")
    @Operation(summary = "文章爬虫")
    @BizNo(spEl = "{#url}")
    public void reptile(String url) {
        articleService.reptile(url);
    }

    @GetMapping(value = "/randomImg")
    @Operation(summary = "随机获取一张图片(TODO)")
    public String randomImg() {
        return articleService.randomImg();
    }

}

