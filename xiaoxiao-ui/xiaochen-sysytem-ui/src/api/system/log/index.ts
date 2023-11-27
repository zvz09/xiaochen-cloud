import http from "@/config/axios";
import { LOG_SERVICE_PATH } from "@/api/config/servicePort";
import { Log } from "@/api/system/log/types";
import { ESResPage } from "@/api/interface";

/**
 * page
 * @param {object} params QueryBody
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.applicationName
 * @param {string} params.traceId
 * @param {string} params.host
 * @param {string} params.message
 * @param {string} params.begin
 * @param {string} params.end
 * @returns
 */
export const logPage = (params: Log.PageParams) => {
  return http.post<ESResPage<Log.LogVO>>(LOG_SERVICE_PATH + `/log/server/page`, params);
};
