import http from "@/config/axios";
import { BLOG_SERVICE_PATH } from "@/api/config/servicePort";
import { Tags } from "@/api/note/tags/types";
import { ResPage } from "@/api/interface";

/**
 * 标签列表
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const pageTags = (params: Tags.ListParams) => {
  return http.post<ResPage<Tags.TagsDTO>>(BLOG_SERVICE_PATH + `/tags/page`, params);
};

/**
 * 置顶标签
 * @param {string} id
 * @returns
 */
export const topTag = (id: string) => {
  return http.get(BLOG_SERVICE_PATH + `/tags/${id}/top`);
};

/**
 * 删除标签
 * @param {string} id
 * @returns
 */
export const deleteTagById = (id: string) => {
  return http.delete(BLOG_SERVICE_PATH + `/tags/${id}`);
};

/**
 * 标签详情
 * @param {string} id
 * @returns
 */
export const getTagsById = (id: string) => {
  return http.get<Tags.TagsDTO>(BLOG_SERVICE_PATH + `/tags/${id}`);
};

/**
 * 批量删除标签
 * @param {array} params integer
 * @returns
 */
export const deleteTagBatch = (params: string[]) => {
  return http.delete(BLOG_SERVICE_PATH + `/tags`, params);
};

/**
 * 修改标签
 * @param {object} params 博客标签表
 * @param {number} params.id
 * @param {object} params.createdAt
 * @param {object} params.updatedAt
 * @param {boolean} params.deleted
 * @param {string} params.name 标签名称
 * @param {number} params.clickVolume
 * @param {number} params.sort 排序
 * @returns
 */
export const updateTag = (params: Tags.TagsDTO) => {
  return http.put(BLOG_SERVICE_PATH + `/tags`, params);
};
/**
 * 新增标签
 * @param {object} params 博客标签表
 * @param {number} params.id
 * @param {object} params.createdAt
 * @param {object} params.updatedAt
 * @param {boolean} params.deleted
 * @param {string} params.name 标签名称
 * @param {number} params.clickVolume
 * @param {number} params.sort 排序
 * @returns
 */
export const insertTag = (params: Tags.TagsDTO) => {
  return http.post(BLOG_SERVICE_PATH + `/tags`, params);
};
