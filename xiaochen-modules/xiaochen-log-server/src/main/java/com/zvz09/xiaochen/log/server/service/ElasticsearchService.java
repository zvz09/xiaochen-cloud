package com.zvz09.xiaochen.log.server.service;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.zvz09.xiaochen.log.server.domain.dto.EsPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchService<T> {

    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    @Getter
    public final Index index = new Index();

    @Getter
    public final Document document = new Document();

    public class Index {
        /**
         * 创建索引（同步）
         *
         * @param indexName
         * @return true：成功，false：失败
         */
        @SneakyThrows
        public Boolean create(String indexName) {
            // 索引名称转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            ElasticsearchIndicesClient indexClient = elasticsearchClient.indices();

            boolean flag = indexClient.exists(req -> req.index(iName)).value();

            boolean result = true;
            if (flag) {
                log.info("索引【" + iName + "】已存在！");
            } else {
                result = indexClient.create(req -> req.index(iName)).acknowledged();
                if (result) {
                    log.info("索引【" + iName + "】创建成功！");
                } else {
                    log.info("索引【" + iName + "】创建失败！");
                }
            }
            return result;
        }

        /**
         * 查询索引（同步）
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Map<String, IndexState> query(String indexName) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);
            // 获取【索引客户端对象】
            ElasticsearchIndicesClient indexClient = elasticsearchClient.indices();

            // 查询结果；得到【查询索引响应对象】
            GetIndexRequest getIndexRequest = new GetIndexRequest.Builder().index(iName).build();

            GetIndexResponse getIndexResponse = indexClient.get(getIndexRequest);

            return getIndexResponse.result();
        }

        /**
         * 查询索引是否存在
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Boolean isExist(String indexName) {
            // 索引名称转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            ElasticsearchIndicesClient indexClient = elasticsearchClient.indices();

            return indexClient.exists(req -> req.index(iName)).value();
        }

        /**
         * 查询全部索引
         *
         * @return 索引名称 Set 集合
         */
        @SneakyThrows
        public Set<String> all() {
            GetIndexResponse getIndexResponse = elasticsearchClient.indices().get(req -> req.index("*"));
            return getIndexResponse.result().keySet();
        }

        /**
         * 删除索引
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Boolean delete(String indexName) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(req -> req.index(iName));

            return deleteIndexResponse.acknowledged();
        }
    }

    public class Document{
        /**
         * 创建/更新文档（异步）
         * 存在：
         *
         * @param indexName  索引名称
         * @param docId      文档ID
         * @param esDocument 文档内容
         * @return 不存在：created、存在：updated
         */
        @SneakyThrows
        public String createOrUpdate(String indexName, String docId, T esDocument) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // 可创建/可更新
            IndexRequest<T> indexRequest = new IndexRequest.Builder<T>()
                    .index(iName)
                    .id(docId)
                    .document(esDocument)
                    .build();

            return elasticsearchClient.index(indexRequest).result().jsonValue();
        }
        /**
         * 检测文档是否存在
         *
         * @param indexName
         * @param docId
         * @return
         */
        @SneakyThrows
        public Boolean isExist(String indexName, String docId) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            ExistsRequest existsRequest = new ExistsRequest.Builder()
                    .index(iName)
                    .id(docId)
                    .build();

            return elasticsearchClient.exists(existsRequest).value();
        }

        /**
         * 根据关键字查文档
         * ---------------------------
         * 只要标题和内容中有一个匹配即可
         * ---------------------------
         *
         * @param searchRequest 搜索条件
         * @param clazz
         * @param mapper
         * @return List 集合
         */
        @SneakyThrows
        public <V> List<V> query(SearchRequest searchRequest, Class<T> clazz, Function<T, V> mapper) {
            SearchResponse<T> response = elasticsearchClient.search(searchRequest, clazz);

            List<Hit<T>> hits = response.hits().hits();

            List<V> docs = hits.stream().map(hit -> mapper.apply(hit.source())).collect(Collectors.toList());

            return docs;
        }

        /**
         * 【分页查找】根据关键字查文档
         * ---------------------------
         * 只要标题和内容中有一个匹配即可
         * 支持高亮
         * ---------------------------
         *
         * @param searchRequest
         * @param clazz
         * @param mapper
         * @return EsPage 对象
         */
        @SneakyThrows
        public <V> EsPage<V> page(SearchRequest searchRequest,Class<T> clazz,Function<T, V> mapper) {


            int p = Objects.isNull(searchRequest.size()) ? 10 : searchRequest.size();
            int c = Objects.isNull(searchRequest.from()) ? 1 : searchRequest.from()/p +1;
            // 构建EsPage
            EsPage esPage = new EsPage();
            esPage.setCurrent(c);
            esPage.setSize(p);

            SearchResponse<T> response = elasticsearchClient.search(searchRequest, clazz);

            // 构建EsPage
            esPage.setTotal(response.hits().total().value());

            // 查询结果
            List<Hit<T>> hits = response.hits().hits();

            List<V> docs = hits.stream().map(hit -> mapper.apply(hit.source())).collect(Collectors.toList());

            // VO对象集合注入page对象
            esPage.setRecords(docs);

            // 返回page
            return esPage;
        }

        /**
         * 批量删除文档
         *
         * @param indexName   索引名称
         * @param docIds 文档ID集合
         * @return 成功删除数量
         */
        @SneakyThrows
        public Integer del(String indexName, List<String> docIds) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // 批量操作对象集合
            List<BulkOperation> bs = new ArrayList<>();

            // 构建【批量操作对象】，并装入list集合中
            docIds.stream().forEach(docId -> {
                // 删除操作对象
                DeleteOperation delOpe = new DeleteOperation.Builder().id(docId).build();

                // 构建【批量操作对象】
                BulkOperation opt = new BulkOperation.Builder().delete(delOpe).build();
                // 装入list集合
                bs.add(opt);
            });

            // 构建【批理请求对象】
            BulkRequest bulkRequest = new BulkRequest.Builder()
                    // 索引
                    .index(iName)
                    // 批量操作对象集合
                    .operations(bs)
                    .build();

            // 批量操作
            BulkResponse bulkResponse = elasticsearchAsyncClient.bulk(bulkRequest).get();
            int i = bulkResponse.items().size();
            log.info("成功处理 {} 份文档！", i);
            return i;
        }
    }

}
