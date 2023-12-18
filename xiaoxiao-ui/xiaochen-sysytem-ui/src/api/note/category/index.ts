import http from "@/config/axios";
import { NOTE_SERVICE_PATH } from "@/api/config/servicePort";
import { Category } from "@/api/note/category/types";
import { ResPage } from "@/api/interface";

/**
 * 分类列表
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const pageCategory = (params: Category.PageParams) => {
  return http.post<ResPage<Category.CategoryDTO>>(NOTE_SERVICE_PATH + `/category/page`, params);
};

/**
 * 置顶分类
 * @param {string} id
 * @returns
 */
export const topCategory = (id: string) => {
  return http.put(NOTE_SERVICE_PATH + `/category/${id}/top`);
};

/**
 * 删除分类
 * @param {string} id
 * @returns
 */
export const deleteCategory = (id: string) => {
  return http.delete(NOTE_SERVICE_PATH + `/category/${id}`);
};

/**
 * 分类详情
 * @param {string} id
 * @returns
 */
export const getCategoryById = (id: string) => {
  return http.get<Category.CategoryDTO>(NOTE_SERVICE_PATH + `/category/${id}`);
};

/**
 * 批量删除分类
 * @param {array} params integer
 * @returns
 */
export const deleteCategoryBatch = (params: string[]) => {
  return http.delete(NOTE_SERVICE_PATH + `/category`, params);
};

/**
 * 修改分类
 * @param {object} params 博客分类表
 * @param {number} params.id
 * @param {object} params.createdAt
 * @param {object} params.updatedAt
 * @param {boolean} params.deleted
 * @param {string} params.name 分类名称
 * @param {number} params.clickVolume
 * @param {number} params.sort 排序
 * @param {string} params.icon 图标
 * @returns
 */
export const updateCategory = (params: Category.CategoryDTO) => {
  return http.put(NOTE_SERVICE_PATH + `/category`, params);
};

/**
 * 新增分类
 * @param {object} params 博客分类表
 * @param {number} params.id
 * @param {object} params.createdAt
 * @param {object} params.updatedAt
 * @param {boolean} params.deleted
 * @param {string} params.name 分类名称
 * @param {number} params.clickVolume
 * @param {number} params.sort 排序
 * @param {string} params.icon 图标
 * @returns
 */
export const insertCategory = (params: Category.CategoryDTO) => {
  return http.post(NOTE_SERVICE_PATH + `/category`, params);
};
