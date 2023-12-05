import http from "@/config/axios";
import { BLOG_SERVICE_PATH } from "@/api/config/servicePort";
import { Article } from "@/api/blog/article/types";
import { ResPage } from "@/api/interface";

/**
 * 保存文章
 * @param {object} params ArticleDTO
 * @param {string} params.id
 * @param {string} params.title
 * @param {string} params.avatar
 * @param {string} params.summary
 * @param {string} params.content
 * @param {string} params.contentMd
 * @param {string} params.keywords
 * @param {number} params.readType
 * @param {boolean} params.isStick
 * @param {boolean} params.isOriginal
 * @param {string} params.originalUrl
 * @param {string} params.categoryName
 * @param {boolean} params.isPublish
 * @param {boolean} params.isCarousel
 * @param {boolean} params.isRecommend
 * @param {array} params.tags
 * @returns
 */
export const insertArticle = (params: Article.ArticleDTO) => {
  return http.post(BLOG_SERVICE_PATH + `/article`, params);
};

/**
 * 修改文章
 * @param {object} params ArticleDTO
 * @param {string} params.id
 * @param {string} params.title
 * @param {string} params.avatar
 * @param {string} params.summary
 * @param {string} params.content
 * @param {string} params.contentMd
 * @param {string} params.keywords
 * @param {number} params.readType
 * @param {boolean} params.isStick
 * @param {boolean} params.isOriginal
 * @param {string} params.originalUrl
 * @param {string} params.categoryName
 * @param {boolean} params.isPublish
 * @param {boolean} params.isCarousel
 * @param {boolean} params.isRecommend
 * @param {array} params.tags
 * @returns
 */
export const updateArticle = (params: Article.ArticleDTO) => {
  return http.put(BLOG_SERVICE_PATH + `/article`, params);
};

/**
 * 批量删除文章
 * @param {array} params integer
 * @returns
 */
export const deleteBatchArticle = (params: string[]) => {
  return http.delete(BLOG_SERVICE_PATH + `/article`, params);
};

/**
 * 文章详情
 * @param {string} id
 * @returns
 */
export const selectArticleById = (id: string) => {
  return http.get(BLOG_SERVICE_PATH + `/article/${id}`);
};

/**
 * 发布或下架文章
 * @param {string} id
 * @returns
 */
export const pubOrShelfArticle = (id: string) => {
  return http.put(BLOG_SERVICE_PATH + `/article/${id}/pubOrShelf`);
};

/**
 * 置顶文章
 * @param {string} id
 * @returns
 */
export const topArticle = (id: string) => {
  return http.put(BLOG_SERVICE_PATH + `/article/${id}/top`);
};

/**
 * 批量文章SEO(TODO)
 * @param {array} params integer
 * @returns
 */
export const seoBatch = (params: string[]) => {
  return http.post(BLOG_SERVICE_PATH + `/article/baiduSeo`, params);
};

/**
 * 文章列表
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const selectArticleList = (params: Article.SelectArticleListParams) => {
  return http.post<ResPage<Article.ArticleVO>>(BLOG_SERVICE_PATH + `/article/page`, params);
};

/**
 * 随机获取一张图片(TODO)
 * @returns
 */
export const randomImg = () => {
  return http.get<string>(BLOG_SERVICE_PATH + `/article/randomImg`);
};

/**
 * 文章爬虫(TODO)
 * @param {string} url
 * @returns
 */
export const reptile = (url: string) => {
  return http.get<Article.ArticleDTO>(BLOG_SERVICE_PATH + `/article/reptile?url=${url}`);
};
