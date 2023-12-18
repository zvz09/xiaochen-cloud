package com.zvz09.xiaochen.note.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.note.domain.entity.ArticleTag;
import com.zvz09.xiaochen.note.mapper.ArticleTagMapper;
import com.zvz09.xiaochen.note.service.IArticleTagService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {

    @Override
    public void deleteByArticleIds(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            return;
        }
        this.remove(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getArticleId, articleIds));
    }
}
