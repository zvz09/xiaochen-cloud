package com.zvz09.xiaochen.blog.service;

import com.zvz09.xiaochen.blog.domain.entity.ReptileDocument;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jsoup.nodes.Document;

/**
 * <p>
 * 爬取的原始数据 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
public interface IReptileDocumentService extends IService<ReptileDocument> {

    ReptileDocument add(String url, Document document);

    ReptileDocument getByUrl(String url);
}
