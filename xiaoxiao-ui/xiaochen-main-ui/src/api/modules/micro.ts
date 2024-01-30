import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
export interface MicroInfo {
  id: string;
  createdAt: string;
  updatedAt: string;
  deleted: boolean;
  path: string;
  name: string;
  redirect: string;
  url: string;
  sort: number;
  icon: string;
  title: string;
}

/**
 * 列表
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const microTree = () => {
  return http.get<MicroInfo[]>(SYSTEM_SERVICE_PATH + `/micro/tree`);
};
