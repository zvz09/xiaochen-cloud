import http from "@/config/axios";
import { FLOWABLE_SERVICE_PATH } from "@/api/config/servicePort";
import { Model } from "@/api/flowable/model/types";
import { ResPage } from "@/api/interface";

/**
 * 保存流程模型
 * @param {object} params FlowableModelDto
 * @param {string} params.modelId
 * @param {string} params.modelName
 * @param {string} params.modelKey
 * @param {string} params.category
 * @param {string} params.description
 * @param {number} params.formType
 * @param {number} params.formId
 * @param {string} params.bpmnXml
 * @param {boolean} params.newVersion
 * @returns
 */
export const saveModel = (params: Model.SaveParams) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/model/save`, params);
};

/**
 * 删除流程模型
 * @param {array} params string
 * @returns
 */
export const removeModel = (params: string[]) => {
  return http.delete(FLOWABLE_SERVICE_PATH + `/model/remove`, params);
};

/**
 * 查询流程模型列表
 * @param {object} params FlowableModelQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.modelName 模型名称
 * @param {string} params.modelKey 模型Key
 * @param {string} params.category 流程分类
 * @returns
 */
export const pageModel = (params: Model.PageParams) => {
  return http.post<ResPage<Model.VO>>(FLOWABLE_SERVICE_PATH + `/model/page`, params);
};

/**
 * 列出自定义任务侦听器
 * @returns
 */
export const listTaskListener = () => {
  return http.get<Model.ListenerVo[]>(FLOWABLE_SERVICE_PATH + `/model/list/taskListener`);
};

/**
 * 列出自定义执行侦听器
 * @returns
 */
export const listExecutionListener = () => {
  return http.get<Model.ListenerVo[]>(FLOWABLE_SERVICE_PATH + `/model/list/executionListener`);
};

/**
 * 修改流程模型
 * @param {object} params FlowableModelDto
 * @param {string} params.modelId
 * @param {string} params.modelName
 * @param {string} params.modelKey
 * @param {string} params.category
 * @param {string} params.description
 * @param {number} params.formType
 * @param {number} params.formId
 * @param {string} params.bpmnXml
 * @param {boolean} params.newVersion
 * @returns
 */
export const editModel = (params: Model.DTO) => {
  return http.put(FLOWABLE_SERVICE_PATH + `/model/edit`, params);
};

/**
 * 新增流程模型
 * @param {object} params FlowableModelDto
 * @param {string} params.modelId
 * @param {string} params.modelName
 * @param {string} params.modelKey
 * @param {string} params.category
 * @param {string} params.description
 * @param {number} params.formType
 * @param {number} params.formId
 * @param {string} params.bpmnXml
 * @param {boolean} params.newVersion
 * @returns
 */
export const addModel = (params: Model.DTO) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/model/add`, params);
};

/**
 * 查询流程模型列表
 * @param {string} modelKey
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const historyModelList = (modelKey: string, params: Model.HistoryListParams) => {
  return http.post<ResPage<Model.VO>>(FLOWABLE_SERVICE_PATH + `/model/${modelKey}/historyList`, params);
};

/**
 * 设为最新流程模型
 * @param {string} modelId
 * @returns
 */
export const latestModel = (modelId: string) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/model/${modelId}/latest`);
};

/**
 * 获取流程BpmnXml详细信息
 * @param {string} modelId
 * @returns
 */
export const getModeBpmnXml = (modelId: string) => {
  return http.get<string>(FLOWABLE_SERVICE_PATH + `/model/${modelId}/getBpmnXml`);
};

/**
 * 部署流程模型
 * @param {string} modelId
 * @returns
 */
export const deployModel = (modelId: string) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/model/${modelId}/deploy`);
};

/**
 * 获取流程模型详细信息
 * @param {string} modelId
 * @returns
 */
export const getModelInfo = (modelId: string) => {
  return http.get<Model.VO>(FLOWABLE_SERVICE_PATH + `/model/${modelId}`);
};
