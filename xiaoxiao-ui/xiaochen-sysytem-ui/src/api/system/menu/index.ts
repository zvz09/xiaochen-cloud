import http from "@/config/axios";
import type { MenuVO } from "./types";
import { MenuForm } from "./types";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";

// 获取菜单列表
export const listTree = () => {
  return http.get<MenuVO[]>(SYSTEM_SERVICE_PATH + `/menu/tree`, {}, { loading: false });
};

/**
 * 新增菜单
 * @returns
 */
export const createMenu = (params: MenuForm) => {
  return http.post(SYSTEM_SERVICE_PATH + `/menu`, params);
};

/**
 * 根据ID更新菜单
 * @returns
 */
export const updateMenu = (params: MenuForm) => {
  return http.put(SYSTEM_SERVICE_PATH + `/menu`, params);
};

/**
 * 删除菜单
 * @param {string} id
 * @returns
 */
export const deleteMenu = (id: string) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/menu/${id}`);
};
