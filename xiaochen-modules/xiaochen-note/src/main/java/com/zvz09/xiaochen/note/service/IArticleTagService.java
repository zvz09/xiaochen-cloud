package com.zvz09.xiaochen.note.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.note.domain.entity.ArticleTag;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
public interface IArticleTagService extends IService<ArticleTag> {

    void deleteByArticleIds(List<Long> longs);
}
