import http from "@/config/axios";
import { FLOWABLE_SERVICE_PATH } from "@/api/config/servicePort";
import { Task } from "@/api/flowable/task/types";

/**
 * 取消流程
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const stopProcess = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/stopProcess`, params);
};

/**
 * 撤回流程
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const revokeProcess = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/revokeProcess`, params);
};

/**
 * 获取流程变量
 * @param {string} taskId
 * @returns
 */
export const processVariables = (taskId: string) => {
  return http.get<Record<string, any>>(FLOWABLE_SERVICE_PATH + `/task/${taskId}/processVariables`);
};

/**
 * 审批流程
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const taskComplete = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/complete`, params);
};

/**
 * 拒绝任务
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const rejectTask = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/reject`, params);
};

/**
 * 退回任务
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const returnTask = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/return`, params);
};

/**
 * 获取所有可回退的节点
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const findReturnTaskList = (params: Task.FlowableTaskDto) => {
  return http.post<Task.FlowElement[]>(FLOWABLE_SERVICE_PATH + `/task/returnList`, params);
};

/**
 * 删除任务
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const deleteTask = (params: Task.FlowableTaskDto) => {
  return http.delete(FLOWABLE_SERVICE_PATH + `/task/delete`, params);
};

/**
 * 签收任务
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const receiptedTask = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/receipted`, params);
};

/**
 * 取消签收任务
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const unReceiptedTask = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/unReceipted`, params);
};

/**
 * 委派任务
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const delegateTask = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/delegate`, params);
};

/**
 * 转办任务
 * @param {object} params FlowableTaskDto
 * @returns
 */
export const transferTask = (params: Task.FlowableTaskDto) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/transfer`, params);
};

/**
 * 生成流程图
 * @param {string} processId
 * @returns
 */
export const genProcessDiagram = (processId: string) => {
  return http.post(FLOWABLE_SERVICE_PATH + `/task/${processId}/diagram`);
};
