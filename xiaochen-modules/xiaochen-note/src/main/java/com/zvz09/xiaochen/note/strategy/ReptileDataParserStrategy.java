package com.zvz09.xiaochen.note.strategy;

import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
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
