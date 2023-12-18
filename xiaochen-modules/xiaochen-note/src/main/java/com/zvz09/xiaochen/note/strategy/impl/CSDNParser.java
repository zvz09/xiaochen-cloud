package com.zvz09.xiaochen.note.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CSDN
 *
 * @author zvz09
 */
@Component
public class CSDNParser implements ReptileDataParserStrategy {

    @Override
    public String getBaseUrl() {
        return "https://blog.csdn.net";
    }

    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementById("article_content");
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        Elements tagElements = document.getElementsByClass("tag-link");
        if (tagElements != null) {
            List<String> tags = new ArrayList<>();
            tagElements.forEach(item -> {
                if (StringUtils.isNotBlank(item.text())) {
                    tags.add(item.text());
                }
            });
            dto.setTags(tags);
        }
        return dto;
    }
}
