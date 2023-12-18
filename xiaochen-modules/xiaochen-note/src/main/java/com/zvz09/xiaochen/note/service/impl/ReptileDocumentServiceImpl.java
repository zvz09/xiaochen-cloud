package com.zvz09.xiaochen.note.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.note.domain.entity.ReptileDocument;
import com.zvz09.xiaochen.note.domain.vo.ReptileDocumentVO;
import com.zvz09.xiaochen.note.mapper.ReptileDocumentMapper;
import com.zvz09.xiaochen.note.service.IReptileDocumentService;
import org.jsoup.nodes.Document;
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

    @Override
    public ReptileDocument add(String url, Document document) {
        ReptileDocument reptileDocument = ReptileDocument
                .builder()
                .url(url)
                .title(document.title())
                .content(document.toString())
                .build();
        this.save(reptileDocument);
       return reptileDocument;
    }

    @Override
    public ReptileDocument getByUrl(String url) {
        return this.getOne(new LambdaQueryWrapper<ReptileDocument>().eq(ReptileDocument::getUrl, url));
    }

    @Override
    public long countNotParsed() {
        return this.count(new LambdaQueryWrapper<ReptileDocument>().eq(ReptileDocument::getStatus,false));
    }

    @Override
    public IPage<ReptileDocumentVO> page(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize())).convert(ReptileDocumentVO::new);
    }
}
