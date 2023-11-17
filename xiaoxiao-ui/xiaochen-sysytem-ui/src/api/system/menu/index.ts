import http from "@/config/axios";
import type { MenuVO } from "./types";
import { MenuForm } from "./types";

// 获取菜单列表
export const listTree = () => {
  return http.get<MenuVO[]>(`/system/menu/listTree`, {}, { loading: false });
};

/**
 * 新增菜单
 * @returns
 */
export const createMenu = (params: MenuForm) => {
  return http.post(`/system/menu`, params);
};

/**
 * 根据ID更新菜单
 * @returns
 */
export const updateMenu = (params: MenuForm) => {
  return http.put(`/system/menu`, params);
};

/**
 * 删除菜单
 * @param {string} id
 * @returns
 */
export const deleteMenu = (id: string) => {
  return http.delete(`/menu/${id}`);
};
