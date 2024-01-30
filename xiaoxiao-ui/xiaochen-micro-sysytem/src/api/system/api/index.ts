import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { API } from "@/api/system/api/types";

/**
 * api 树
 * @returns
 */
export const listAPITree = () => {
  return http.get<API.ApiVO[]>(SYSTEM_SERVICE_PATH + `/api/listTree`);
};
