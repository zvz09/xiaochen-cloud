import http from "@/config/axios";
import type { MenuVO } from "./types";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";

// 获取菜单列表
export const listTreeByMicroName = (microName: string) => {
  return http.get<MenuVO[]>(SYSTEM_SERVICE_PATH + `/menu/tree/${microName}`, {}, { loading: false });
};

// 获取菜单列表
export const listTree = () => {
  return http.get<MenuVO[]>(SYSTEM_SERVICE_PATH + `/menu/tree`, {}, { loading: false });
};
