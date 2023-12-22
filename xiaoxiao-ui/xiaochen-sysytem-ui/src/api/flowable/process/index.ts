import http from "@/config/axios";
import { FLOWABLE_SERVICE_PATH } from "@/api/config/servicePort";
import { Process } from "@/api/flowable/process/types";
import { ResPage } from "@/api/interface";
import { Form } from "@/api/flowable/form/types";

/**
 * 获取待办列表
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
export const todoProcessTaskList = (params: Process.TodoProcessListParams) => {
  return http.post<ResPage<Process.FlowableTaskVo>>(FLOWABLE_SERVICE_PATH + `/process/todo/page`, params);
};

/**
 * 根据流程定义id启动流程实例
 * @param {object} params StartProcessDto
 * @param {string} params.processDefId
 * @param {object} params.variables
 * @returns
 */
export const startProcess = (params: Process.StartParams) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/process/start`, params);
};

/**
 * 获取待签列表
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
export const receiptedProcessTaskList = (params: Process.ProcessTaskPageParams) => {
  return http.post<ResPage<Process.FlowableTaskVo>>(FLOWABLE_SERVICE_PATH + `/process/receipted/page`, params);
};

/**
 * 查询可发起流程列表
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
export const startProcessList = (params: Process.StartProcessListParams) => {
  return http.post<ResPage<Process.FlowableDeployVo>>(FLOWABLE_SERVICE_PATH + `/process/page`, params);
};

/**
 * 我发起的流程
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
export const ownProcessTaskList = (params: Process.ProcessTaskPageParams) => {
  return http.post<ResPage<Process.FlowableTaskVo>>(FLOWABLE_SERVICE_PATH + `/process/own/page`, params);
};

/**
 * 删除流程实例
 * @param {array} params string
 * @returns
 */
export const deleteProcess = (params: string[]) => {
  return http.delete(FLOWABLE_SERVICE_PATH + `/process/instance`, params);
};

/**
 * 获取已办列表
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
export const finishedProcessTaskList = (params: Process.ProcessTaskPageParams) => {
  return http.post<ResPage<Process.FlowableTaskVo>>(FLOWABLE_SERVICE_PATH + `/process/finished/page`, params);
};

/**
 * 获取抄送列表
 * @param {object} params FlowableCopyQuery
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.processName
 * @param {number} params.userId
 * @param {string} params.originatorName
 * @returns
 */
export const copyProcessList = (params: Process.CopyProcessListParams) => {
  return http.post<ResPage<Process.FlowableCopyVo>>(FLOWABLE_SERVICE_PATH + `/process/copy/page`, params);
};

/**
 * 查询流程详情信息
 * @param {string} procInsId
 * @param {string} taskId
 * @returns
 */
export const detailProcess = (procInsId: string, taskId: string) => {
  return http.get<Process.FlowableDetailVo>(FLOWABLE_SERVICE_PATH + `/process/${procInsId}/${taskId}/detail`);
};

/**
 * 读取xml文件
 * @param {string} processDefId
 * @returns
 */
export const getProcessBpmnXml = (processDefId: string) => {
  return http.get<string>(FLOWABLE_SERVICE_PATH + `/process/${processDefId}/bpmnXml`);
};

/**
 * 查询流程部署关联表单信息
 * @param {string} definitionId
 * @param {string} deployId
 * @param {string} procInsId
 * @returns
 */
export const getProcessForm = (definitionId: string, deployId: string, procInsId: string) => {
  return http.get<Form.VO>(FLOWABLE_SERVICE_PATH + `/process/${definitionId}/${deployId}/form?procInsId=${procInsId}`);
};
