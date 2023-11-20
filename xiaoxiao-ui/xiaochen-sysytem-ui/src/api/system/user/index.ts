import http from "@/config/axios";
import { GetUserListParams, RegisterParams, UpdateUserInfoParams } from "@/api/system/user/types";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";

export const updateUserInfo = (params: UpdateUserInfoParams) => {
  return http.put(SYSTEM_SERVICE_PATH + `/user`, params);
};

export const getUserList = (params: GetUserListParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user/list`, params);
};

export const simpleList = (params: GetUserListParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user/simpleList`, params);
};

export const deleteUser = (id: number) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/user/${id}`);
};

export const currentUser = () => {
  return http.get(SYSTEM_SERVICE_PATH + `/user/currentUser`);
};

export const register = (params: RegisterParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user/register`, params);
};

export const resetPassword = (id: number) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user/resetPassword/${id}`);
};
