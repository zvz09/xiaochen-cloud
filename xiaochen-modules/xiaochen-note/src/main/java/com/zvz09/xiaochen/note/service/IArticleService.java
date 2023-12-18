package com.zvz09.xiaochen.note.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.domain.entity.Article;
import com.zvz09.xiaochen.note.domain.vo.ArticleVO;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
public interface IArticleService extends IService<Article> {

    IPage<ArticleVO> selectArticleList(BasePage basePage);

    Article selectArticleById(Long id);

    void insertArticle(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    void deleteBatchArticle(List<Long> ids);

    void topArticle(Long id);

    void reptile(String url);


}
