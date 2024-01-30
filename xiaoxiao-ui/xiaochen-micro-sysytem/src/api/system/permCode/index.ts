import { PermCode } from "@/api/system/permCode/types";
import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { ResPage } from "@/api/interface";

/**
 * 列表
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const listTree = (params: PermCode.ListTreeParams) => {
  return http.post<ResPage<PermCode.PermCodeVO>>(SYSTEM_SERVICE_PATH + `/perm_code/tree`, params);
};

/**
 * 删除权限字
 */
export const deletePermCode = (id: number) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/perm_code/${id}`);
};

/**
 * 根据ID更新权限字
 */
export const updatePermCode = (params: PermCode.UpdateParams) => {
  return http.put(SYSTEM_SERVICE_PATH + `/perm_code`, params);
};

/**
 * 新增权限字
 */
export const createPermCode = (params: PermCode.CreateParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/perm_code`, params);
};

/**
 * 详情
 * @param {string} id
 * @returns
 */
export const detailPermCode = (id: string) => {
  return http.get<PermCode.PermCodeVO>(SYSTEM_SERVICE_PATH + `/perm_code/${id}`);
};

/**
 * 绑定权限资源
 * @param {string} id
 * @param {array} params integer
 * @returns
 */
export const bindApis = (id: string, params: any[]) => {
  return http.put(SYSTEM_SERVICE_PATH + `/perm_code/bind/${id}`, params);
};
