package com.zvz09.xiaochen.note.service;

import com.zvz09.xiaochen.common.web.service.BaseService;
import com.zvz09.xiaochen.note.domain.entity.ReptileDocument;
import org.jsoup.nodes.Document;

/**
 * <p>
 * 爬取的原始数据 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
public interface IReptileDocumentService extends BaseService<ReptileDocument> {

    ReptileDocument add(String url, Document document);

    ReptileDocument getByUrl(String url);

    long countNotParsed();
}
