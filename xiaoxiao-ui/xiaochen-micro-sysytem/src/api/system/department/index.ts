import http from "@/config/axios";
import { SYSTEM_SERVICE_PATH } from "@/api/config/servicePort";
import { Department } from "@/api/system/department/types";

/**
 * 列表查询
 * @returns
 */
export const getDepartmentTree = () => {
  return http.get<Department.VO[]>(SYSTEM_SERVICE_PATH + `/dept/tree`);
};

/**
 * 删除部门
 * @param {string} id
 * @returns
 */
export const deleteDepartment = (id: string) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/dept/${id}`);
};

/**
 * 删除选中部门
 * @param {array} params integer
 * @returns
 */
export const deleteDepartmentByIds = (params: string[]) => {
  return http.delete(SYSTEM_SERVICE_PATH + `/dept`, params);
};

/**
 * 根据ID更新部门
 * @param {object} params DepartmentDto
 * @param {number} params.id
 * @param {number} params.parentId 父部门id
 * @param {string} params.deptName 部门名称
 * @param {number} params.orderNum 显示顺序
 * @param {string} params.leader 负责人
 * @param {string} params.phone 联系电话
 * @param {string} params.email 邮箱
 * @param {boolean} params.status 部门状态0正常1停用
 * @returns
 */
export const updateDepartment = (params: Department.DTO) => {
  return http.put(SYSTEM_SERVICE_PATH + `/dept`, params);
};

/**
 * 新增部门
 * @param {object} params DepartmentDto
 * @param {number} params.id
 * @param {number} params.parentId 父部门id
 * @param {string} params.deptName 部门名称
 * @param {number} params.orderNum 显示顺序
 * @param {string} params.leader 负责人
 * @param {string} params.phone 联系电话
 * @param {string} params.email 邮箱
 * @param {boolean} params.status 部门状态0正常1停用
 * @returns
 */
export const createDepartment = (params: Department.DTO) => {
  return http.post(SYSTEM_SERVICE_PATH + `/dept`, params);
};
