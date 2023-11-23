import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { Dictionary } from "@/api/system/dictionary/types";
import { ResPage } from "@/api/interface";

/**
 * 新增字典
 * @param {object} params SysDictionaryDto
 * @param {number} params.id
 * @param {string} params.name 字典名（中）
 * @param {string} params.type 字典名（英）
 * @param {boolean} params.status 状态
 * @param {string} params.description 描述
 * @returns
 */
export const createDictionary = (params: Dictionary.DictionaryVO) => {
  return http.post(SYSTEM_SERVICE_PATH + `/dictionary`, params);
};

/**
 * 更新字典
 * @param {object} params SysDictionaryDto
 * @param {number} params.id
 * @param {string} params.name 字典名（中）
 * @param {string} params.type 字典名（英）
 * @param {boolean} params.status 状态
 * @param {string} params.description 描述
 * @returns
 */
export const updateDictionary = (params: Dictionary.DictionaryVO) => {
  return http.put(SYSTEM_SERVICE_PATH + `/dictionary`, params);
};

/**
 * 删除字典
 * @param {string} id
 * @returns
 */
export const deleteDictionary = (id: string) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/dictionary/${id}`);
};

/**
 * 更新状态
 * @param {string} id
 * @returns
 */
export const changeDictionaryStatus = (id: string) => {
  return http.put(SYSTEM_SERVICE_PATH + `/dictionary/changeStatus/${id}`);
};

/**
 * 字典列表
 * @param {object} params SysDictionaryQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.name 字典名（中）
 * @param {string} params.type 字典名（英）
 * @param {boolean} params.status 状态
 * @returns
 */
export const listDictionary = (params: Dictionary.GetSysDictionaryListParams) => {
  return http.post<ResPage<Dictionary.DictionaryVO>>(SYSTEM_SERVICE_PATH + `/dictionary/list`, params);
};

/**
 * 创建字典项
 * @param {object} params SysDictionaryDetailDto
 * @param {number} params.id
 * @param {string} params.label 展示值
 * @param {string} params.value 字典值
 * @param {boolean} params.status 启用状态
 * @param {number} params.sort 启用状态
 * @param {string} params.tagType
 * @param {number} params.sysDictionaryId 字典id
 * @returns
 */
export const createDictionaryDetail = (params: Dictionary.DictionaryDetailVO) => {
  return http.post(SYSTEM_SERVICE_PATH + `/dictionary_detail`, params);
};

/**
 * 更新字典项
 * @param {object} params SysDictionaryDetailDto
 * @param {number} params.id
 * @param {string} params.label 展示值
 * @param {string} params.value 字典值
 * @param {boolean} params.status 启用状态
 * @param {number} params.sort 启用状态
 * @param {string} params.tagType
 * @param {number} params.sysDictionaryId 字典id
 * @returns
 */
export const updateDictionaryDetail = (params: Dictionary.DictionaryDetailVO) => {
  return http.put(SYSTEM_SERVICE_PATH + `/dictionary_detail`, params);
};

/**
 * 删除字典项
 * @param {string} id
 * @returns
 */
export const deleteDictionaryDetail = (id: string) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/dictionary_detail/${id}`);
};

/**
 * 根据类型获取字典项列表
 * @param {string} encode
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const getByDictionaryEncode = (encode: string, params: Dictionary.GetByDictionaryTypeParams) => {
  return http.post<ResPage<Dictionary.DictionaryDetailVO>>(SYSTEM_SERVICE_PATH + `/dictionary_detail/${encode}`, params);
};

/**
 * 字典项列表
 * @param {object} params SysDictionaryDetailQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {number} params.sysDictionaryId 字典Id
 * @param {string} params.label 展示值
 * @param {number} params.value 字典值
 * @param {boolean} params.status 启用状态
 * @returns
 */
export const getDictionaryDetailList = (params: Dictionary.GetSysDictionaryDetailListParams) => {
  return http.post<ResPage<Dictionary.DictionaryDetailVO>>(SYSTEM_SERVICE_PATH + `/dictionary_detail/list`, params);
};

/**
 * 根据类型获取所有字典项
 * @param {string} encode
 * @returns
 */
export const getAllByDictionaryEncode = (encode: string) => {
  return http.get<Dictionary.DictionaryDetailVO[]>(SYSTEM_SERVICE_PATH + `/dictionary_detail/listAll/${encode}`);
};

/**
 * 切换状态
 * @param {string} id
 * @returns
 */
export const changeDictionaryDetailStatus = (id: string) => {
  return http.put(SYSTEM_SERVICE_PATH + `/dictionary_detail/changeStatus/${id}`);
};
