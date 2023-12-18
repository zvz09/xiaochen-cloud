package com.zvz09.xiaochen.blog.strategy;

import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import org.jsoup.nodes.Document;

/**
 * @author zvz09
 */
public interface ReptileDataParserStrategy {

    String getBaseUrl();
    default boolean  isMatch(String url){
        return url.startsWith(getBaseUrl());
    }
    ArticleDTO parseData(Document document);
}
