package com.zvz09.xiaochen.blog.strategy;

import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import org.jsoup.nodes.Document;

/**
 * @author lizili-YF0033
 */
public interface ReptileDataParserStrategy {

    ArticleDTO parseData(Document document);
}
