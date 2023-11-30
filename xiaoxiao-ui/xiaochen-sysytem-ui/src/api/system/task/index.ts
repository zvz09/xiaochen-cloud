import http from "@/config/axios";
import { JOB_SERVICE_PATH } from "@/api/config/servicePort";
import { Task } from "@/api/system/task/types";
import { ResPage } from "@/api/interface";

/**
 * 新增任务信息
 * @param {object} params JobInfo
 * @param {string} params.id
 * @param {string} params.jobGroup 微服务名
 * @param {string} params.jobDesc
 * @param {string} params.author 作者
 * @param {string} params.alarmEmail 报警邮件
 * @param {string} params.scheduleType 调度类型
 * @param {string} params.scheduleConf 调度配置，值含义取决于调度类型
 * @param {string} params.misfireStrategy 调度过期策略
 * @param {string} params.executorRouteStrategy 执行器路由策略
 * @param {string} params.executorHandler 执行器任务handler
 * @param {string} params.executorParam 执行器任务参数
 * @param {string} params.executorBlockStrategy 阻塞处理策略
 * @param {number} params.executorTimeout 任务执行超时时间，单位秒
 * @param {number} params.executorFailRetryCount 失败重试次数
 * @param {string} params.glueType GLUE类型
 * @param {string} params.glueSource GLUE源代码
 * @param {string} params.glueRemark GLUE备注
 * @param {object} params.glueUpdatetime GLUE更新时间
 * @param {boolean} params.triggerStatus 调度状态：0-停止，1-运行
 * @returns
 */
export const createJobInfo = (params: Task.JobInfoParams) => {
  return http.post(JOB_SERVICE_PATH + `/jobinfo/`, params);
};

/**
 * 删除任务信息
 * @param {string} id
 * @returns
 */
export const deleteJobInfo = (id: string) => {
  return http.delete(JOB_SERVICE_PATH + `/jobinfo/${id}`);
};

/**
 * 根据ID更新任务信息
 * @param {object} params JobInfo
 * @param {string} params.id
 * @param {string} params.jobGroup 微服务名
 * @param {string} params.jobDesc
 * @param {string} params.author 作者
 * @param {string} params.alarmEmail 报警邮件
 * @param {string} params.scheduleType 调度类型
 * @param {string} params.scheduleConf 调度配置，值含义取决于调度类型
 * @param {string} params.misfireStrategy 调度过期策略
 * @param {string} params.executorRouteStrategy 执行器路由策略
 * @param {string} params.executorHandler 执行器任务handler
 * @param {string} params.executorParam 执行器任务参数
 * @param {string} params.executorBlockStrategy 阻塞处理策略
 * @param {number} params.executorTimeout 任务执行超时时间，单位秒
 * @param {number} params.executorFailRetryCount 失败重试次数
 * @param {string} params.glueType GLUE类型
 * @param {string} params.glueSource GLUE源代码
 * @param {string} params.glueRemark GLUE备注
 * @param {object} params.glueUpdatetime GLUE更新时间
 * @param {boolean} params.triggerStatus 调度状态：0-停止，1-运行
 * @returns
 */
export const updateJobInfo = (params: Task.JobInfoParams) => {
  return http.post(JOB_SERVICE_PATH + `/jobinfo/update`, params);
};

/**
 * 更新任务调度状态
 * @param {string} id
 * @returns
 */
export const changeStatus = (id: string) => {
  return http.put(JOB_SERVICE_PATH + `/jobinfo/status/${id}`);
};

/**
 * 列表查询
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const listJobInfoPage = (params: Task.ListParams) => {
  return http.post<ResPage<Task.JobInfoVo>>(JOB_SERVICE_PATH + `/jobinfo/page`, params);
};

/**
 * 获取所有已注册服务
 * @returns
 */
export const getAllServices = () => {
  return http.post(JOB_SERVICE_PATH + `/jobinfo/listServices`);
};
