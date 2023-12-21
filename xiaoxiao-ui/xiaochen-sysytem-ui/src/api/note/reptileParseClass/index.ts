import http from "@/config/axios";
import { NOTE_SERVICE_PATH } from "@/api/config/servicePort";
import { ReptileParseClass } from "@/api/note/reptileParseClass/types";
import { ResPage, ResultData } from "@/api/interface";

/**
 * 列表
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export function pageParseClass(params: ReptileParseClass.PageParams): Promise<ResPage<ReptileParseClass.ReptileParseClassVO>> {
  return http.post(NOTE_SERVICE_PATH + `/reptile/parse/class/page`, params);
}

/**
 * 添加
 * @param {object} params ReptileClassDTO
 * @param {number} params.id
 * @param {string} params.className 类名
 * @param {string} params.content 类内容
 * @returns
 */
export function insertParseClass(params: ReptileParseClass.ReptileParseClassDTO): Promise<ResultData> {
  return http.post(NOTE_SERVICE_PATH + `/reptile/parse/class`, params);
}

/**
 * 删除
 * @param {string} id
 * @returns
 */
export function deleteParseClass(id: string): Promise<ResultData> {
  return http.delete(NOTE_SERVICE_PATH + `/reptile/parse/class/${id}`);
}

/**
 * 批量删除
 * @param {array} params integer
 * @returns
 */
export function deleteBatchParseClass(params: string[]): Promise<ResultData> {
  return http.delete(NOTE_SERVICE_PATH + `/reptile/parse/class`, params);
}

/**
 * 修改
 * @param {object} params ReptileClassDTO
 * @param {number} params.id
 * @param {string} params.className 类名
 * @param {string} params.content 类内容
 * @returns
 */
export function updateParseClass(params: ReptileParseClass.ReptileParseClassDTO): Promise<ResultData> {
  return http.put(NOTE_SERVICE_PATH + `/reptile/parse/class`, params);
}

/**
 * 启动-禁用抓取解析类
 * @returns
 * @param params
 */
export function operateParseClass(params: { id: string; operate: string }): Promise<ResultData> {
  return http.put(NOTE_SERVICE_PATH + `/reptile/parse/class/${params.id}/${params.operate}`);
}
