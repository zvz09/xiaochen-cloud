import http from "@/config/axios";
import { FLOWABLE_SERVICE_PATH } from "@/api/config/servicePort";
import { Form } from "@/api/flowable/form/types";
import { ResPage } from "@/api/interface";

/**
 * 查询流程表单列表
 * @param {object} params SysFormQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const pageForm = (params: Form.PageParams) => {
  return http.post<ResPage<Form.VO>>(FLOWABLE_SERVICE_PATH + `/form/page`, params);
};

/**
 * 查询所有流程表单
 * @param {object} params SysForm
 * @param {number} params.id
 * @param {object} params.createdAt
 * @param {object} params.updatedAt
 * @param {boolean} params.deleted
 * @param {string} params.formName
 * @param {string} params.formContent
 * @param {string} params.thumbnail
 * @returns
 */
export const listForm = (params: Form.ListParams) => {
  return http.post<Form.VO[]>(FLOWABLE_SERVICE_PATH + `/form/list`, params);
};

/**
 * 获取流程表单详细信息
 * @param {string} formId
 * @returns
 */
export const getFormInfo = (formId: number) => {
  return http.get<Form.VO>(FLOWABLE_SERVICE_PATH + `/form/${formId}/info`);
};

/**
 * 删除流程表单
 * @param {string} formId
 * @returns
 */
export const removeForm = (formId: number) => {
  return http.delete(FLOWABLE_SERVICE_PATH + `/form/${formId}`);
};

/**
 * 删除流程表单
 * @returns
 * @param params
 */
export const batchrRemoveForm = (params: string[]) => {
  return http.delete(FLOWABLE_SERVICE_PATH + `/form`, params);
};

/**
 * 修改流程表单
 * @param {object} params SysForm
 * @param {number} params.id
 * @param {object} params.createdAt
 * @param {object} params.updatedAt
 * @param {boolean} params.deleted
 * @param {string} params.formName
 * @param {string} params.formContent
 * @param {string} params.thumbnail
 * @returns
 */
export const editForm = (params: Form.DTO) => {
  return http.put(FLOWABLE_SERVICE_PATH + `/form`, params);
};

/**
 * 新增流程表单
 * @param {object} params SysForm
 * @param {number} params.id
 * @param {object} params.createdAt
 * @param {object} params.updatedAt
 * @param {boolean} params.deleted
 * @param {string} params.formName
 * @param {string} params.formContent
 * @param {string} params.thumbnail
 * @returns
 */
export const addForm = (params: Form.DTO) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/form`, params);
};
