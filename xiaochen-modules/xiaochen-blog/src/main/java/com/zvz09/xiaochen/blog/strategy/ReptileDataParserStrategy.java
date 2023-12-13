package com.zvz09.xiaochen.blog.strategy;

import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import org.jsoup.nodes.Document;

/**
 * @author zvz09
 */
public interface ReptileDataParserStrategy {

    String getBaseUrl();
    default boolean  isMatch(String url){
        if (url.startsWith(getBaseUrl())) {
            return true;
        }else {
            return false;
        }
    }
    ArticleDTO parseData(Document document);
}
