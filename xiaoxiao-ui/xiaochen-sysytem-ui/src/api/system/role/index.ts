import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { Role } from "@/api/system/role/types";
import { ResPage } from "@/api/interface";

export const createRole = (params: Role.CreateRoleParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/role`, params);
};

export const deleteRole = (id: number) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/role/${id}`);
};

export const updateRole = (params: Role.UpdateRoleParams) => {
  return http.put(SYSTEM_SERVICE_PATH + `/role`, params);
};

/**
 * 角色列表
 * @returns
 */
export const getRoleList = (params: Role.GetRoleListParams) => {
  return http.post<ResPage<Role.RoleVO>>(SYSTEM_SERVICE_PATH + `/role/page`, params);
};

export const copyRole = (params: Role.CopyRoleParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/role/copy`, params);
};

/**
 * 获取当前角色所有权限字
 * @returns
 */
export const listPermCodes = () => {
  return http.get<Role.ListPermCodesRes>(SYSTEM_SERVICE_PATH + `/perm_code`);
};

/**
 * 绑定权限字
 * @param {string} id
 * @param {array} params integer
 * @returns
 */
export const bindPerm = (id: string, params: any[]) => {
  return http.post(SYSTEM_SERVICE_PATH + `/role/bind/${id}`, params);
};

/**
 * 详情
 * @param {string} id
 * @returns
 */
export const detailRole = (id: string) => {
  return http.get<Role.RoleVO>(SYSTEM_SERVICE_PATH + `/role/${id}`);
};
