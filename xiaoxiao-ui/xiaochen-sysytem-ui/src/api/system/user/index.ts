import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { User } from "@/api/system/user/types";
import { ResPage } from "@/api/interface";

/**
 * 用户列表
 * @param {object} params SysUserQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.userName 用户名
 * @param {string} params.phone 手机号
 * @returns
 */
export const getUserList = (params: User.GetUserListParams) => {
  return http.post<ResPage<User.UserVO>>(SYSTEM_SERVICE_PATH + `/user/list`, params);
};

/**
 * 更新用户
 * @param {object} params UpdateUserDto
 * @param {number} params.id
 * @param {string} params.userName 用户名
 * @param {string} params.nickName 昵称
 * @param {string} params.headerImg 头型地址
 * @param {number} params.enable 是否启用
 * @param {string} params.phone 手机号
 * @param {string} params.email 邮箱地址
 * @param {array} params.roleIds
 * @returns
 */
export const updateUserInfo = (params: User.UpdateUserInfoParams) => {
  return http.put(SYSTEM_SERVICE_PATH + `/user`, params);
};

/**
 * 删除用户
 * @param {string} id
 * @returns
 */
export const deleteUser = (id: number) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/user/${id}`);
};

/**
 * 当前用户信息
 * @returns
 */
export const currentUser = () => {
  return http.get<User.UserVO>(SYSTEM_SERVICE_PATH + `/user/currentUser`);
};

/**
 * 注册用户
 * @param {object} params RegisterUserDto
 * @param {string} params.userName 用户名
 * @param {string} params.password 密码
 * @param {string} params.nickName 昵称
 * @param {string} params.headerImg 头型地址
 * @param {number} params.enable 是否启动
 * @param {string} params.phone 手机号
 * @param {string} params.email 邮箱地址
 * @returns
 */
export const register = (params: User.RegisterParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user/register`, params);
};

/**
 * 重置密码
 * @param {string} id
 * @returns
 */
export const resetPassword = (id: number) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user/resetPassword/${id}`);
};

/**
 * 用户简要信息列表
 * @param {object} params SysUserQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.userName 用户名
 * @param {string} params.phone 手机号
 * @returns
 */
export const simpleList = (params: User.SimpleListParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user/simpleList`, params);
};

/**
 * 新增用户
 * @param {object} params UpdateUserDto
 * @param {number} params.id
 * @param {string} params.userName 用户名
 * @param {string} params.nickName 昵称
 * @param {boolean} params.gender
 * @param {string} params.headerImg 头型地址
 * @param {number} params.enable 是否启用
 * @param {string} params.phone 手机号
 * @param {string} params.email 邮箱地址
 * @param {array} params.roleIds
 * @returns
 */
export const createUser = (params: User.UpdateUserInfoParams) => {
  return http.post(SYSTEM_SERVICE_PATH + `/user`, params);
};

export const getEnableType = [
  { label: "启用", value: 1, tagType: "success" },
  { label: "禁用", value: 0, tagType: "danger" }
];

export const getGenderType = [
  { label: "男", value: true },
  { label: "女", value: false }
];
