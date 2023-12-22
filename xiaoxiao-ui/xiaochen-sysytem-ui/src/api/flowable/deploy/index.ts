import http from "@/config/axios";
import { FLOWABLE_SERVICE_PATH } from "@/api/config/servicePort";
import { FlowableDeploy } from "@/api/flowable/deploy/types";
import { ResPage } from "@/api/interface";

/**
 * 查询流程部署列表
 * @param {object} params ProcessQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.processKey 流程标识
 * @param {string} params.processName 流程名称
 * @param {string} params.category 流程分类
 * @param {string} params.state 状态
 * @param {object} params.params 请求参数
 * @returns
 */
export const pageDeploy = (params: FlowableDeploy.PageParams) => {
  return http.post<ResPage<FlowableDeploy.VO>>(FLOWABLE_SERVICE_PATH + `/deploy/page`, params);
};

/**
 * 查询流程部署版本列表
 * @param {string} processKey
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const deployPublishList = (processKey: string, params: FlowableDeploy.PublishListParams) => {
  return http.post<ResPage<FlowableDeploy.VO>>(FLOWABLE_SERVICE_PATH + `/deploy/${processKey}/page`, params);
};

/**
 * 查询流程部署关联表单信息
 * @param {string} deployId
 * @returns
 */
export const deployFormInfo = (deployId: string) => {
  return http.get(FLOWABLE_SERVICE_PATH + `/deploy/${deployId}/form`);
};

/**
 * 读取xml文件
 * @param {string} definitionId
 * @returns
 */
export const getDeployBpmnXml = (definitionId: string) => {
  return http.get(FLOWABLE_SERVICE_PATH + `/deploy/${definitionId}/bpmnXml`);
};

/**
 * 激活或挂起流程
 * @param {string} definitionId
 * @param {string} state
 * @returns
 */
export const changeState = (definitionId: string, state: string) => {
  return http.put(FLOWABLE_SERVICE_PATH + `/deploy/${definitionId}/${state}`);
};

/**
 * 删除流程部署
 * @param {array} params string
 * @returns
 */
export const removeDeploy = (params: string[]) => {
  return http.delete(FLOWABLE_SERVICE_PATH + `/deploy`, params);
};
