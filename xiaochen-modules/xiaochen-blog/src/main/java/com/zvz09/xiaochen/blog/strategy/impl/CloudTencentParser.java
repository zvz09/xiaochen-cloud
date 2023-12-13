package com.zvz09.xiaochen.blog.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.blog.strategy.ReptileDataParserStrategy;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * @author zvz09
 */
@Component
@RequiredArgsConstructor
public class CloudTencentParser implements ReptileDataParserStrategy {
    @Override
    public String getBaseUrl() {
        return "https://cloud.tencent.com";
    }

    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementsByClass("mod-content").get(0);
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        return dto;
    }
}
