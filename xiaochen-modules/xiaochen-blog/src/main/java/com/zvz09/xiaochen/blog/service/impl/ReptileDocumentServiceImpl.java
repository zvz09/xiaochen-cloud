package com.zvz09.xiaochen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zvz09.xiaochen.blog.domain.entity.ReptileDocument;
import com.zvz09.xiaochen.blog.mapper.ReptileDocumentMapper;
import com.zvz09.xiaochen.blog.service.IReptileDocumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 爬取的原始数据 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@Service
public class ReptileDocumentServiceImpl extends ServiceImpl<ReptileDocumentMapper, ReptileDocument> implements IReptileDocumentService {

    @Async
    @Override
    public void add(String url, Document document) {
        ReptileDocument reptileDocument = ReptileDocument
                .builder()
                .url(url)
                .content(document.toString())
                .build();
        this.save(reptileDocument);
    }

    @Override
    public ReptileDocument getByUrl(String url) {
        return this.getOne(new LambdaQueryWrapper<ReptileDocument>().eq(ReptileDocument::getUrl, url));
    }
}
