package com.zvz09.xiaochen.blog.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.blog.strategy.ReptileDataParserStrategy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * 博客园
 * @author zvz09
 */
@Component
public class CnblogParser implements ReptileDataParserStrategy {
    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementsByClass("postBody").get(0);
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        return dto;
    }
}
