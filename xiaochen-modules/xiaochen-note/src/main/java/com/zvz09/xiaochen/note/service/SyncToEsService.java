package com.zvz09.xiaochen.note.service;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.elasticsearch.service.ElasticsearchService;
import com.zvz09.xiaochen.note.domain.entity.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lizili-YF0033
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SyncToEsService {

    private final ElasticsearchService<Article> elasticsearchService;


    @Async
    public void syncArticle(Article article){
        Article esData = Article.builder()
                .id(article.getId())
                .content(article.getContent())
                .build();
        String result =
                elasticsearchService.document.createOrUpdate(Article.class.getAnnotation(TableName.class).value(), String.valueOf(article.getId()),esData);
        log.info("同步文章到es结果:{}",result);
    }

}
