import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { Dictionary } from "@/api/system/dictionary/types";
import { ResPage } from "@/api/interface";

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
 * 根据类型获取所有字典项
 * @param {string} encode
 * @returns
 */
export const getAllByDictionaryEncode = (encode: string) => {
  return http.get<Dictionary.DictionaryDetailVO[]>(SYSTEM_SERVICE_PATH + `/dictionary_detail/listAll/${encode}`);
};
