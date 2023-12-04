package com.zvz09.xiaochen.blog.strategy.impl;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.blog.strategy.ReptileDataParserStrategy;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 掘金
 * @author lizili-YF0033
 */
@Component
public class JueJinParser implements ReptileDataParserStrategy {


    @Override
    public ArticleDTO parseData(Document document) {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle(document.title());
        Element articleElement = document.getElementById("article-root");
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(articleElement);
        dto.setContent(markdown);
        Elements tagElements = document.getElementsByAttributeValue("itemprop", "keywords");
        if(tagElements!=null){
            List<String> tags = new ArrayList<>();
            tagElements.forEach(item -> {
                if(StringUtils.isNotBlank(item.attr("content"))){
                    tags.addAll(List.of(item.attr("content").split(",")));
                }
            });
            dto.setTags(tags);
        }
        return dto;
    }
}
