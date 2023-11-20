import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { Role, RoleVO } from "@/api/system/role/types";

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
  return http.post<Role.RoleVO>(SYSTEM_SERVICE_PATH + `/role/list`, params);
};

export const copyRole = (params: Role.CopyRoleParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/role/copy`, params);
};
