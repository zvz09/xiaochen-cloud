export namespace Task {
  // 参数接口
  export interface JobInfoParams {
    /* */
    id?: string;

    /*微服务名 */
    jobGroup?: string;

    /* */
    jobDesc?: string;

    /*作者 */
    author?: string;

    /*报警邮件 */
    alarmEmail?: string;

    /*调度类型 */
    scheduleType?: string;

    /*调度配置，值含义取决于调度类型 */
    scheduleConf?: string;

    /*调度过期策略 */
    misfireStrategy?: string;

    /*执行器路由策略 */
    executorRouteStrategy?: string;

    /*执行器任务handler */
    executorHandler?: string;

    /*执行器任务参数 */
    executorParam?: string;

    /*阻塞处理策略 */
    executorBlockStrategy?: string;

    /*任务执行超时时间，单位秒 */
    executorTimeout?: number;

    /*失败重试次数 */
    executorFailRetryCount?: number;

    /*GLUE类型 */
    glueType?: string;

    /*GLUE源代码 */
    glueSource?: string;

    /*GLUE备注 */
    glueRemark?: string;

    /*调度状态：0-停止，1-运行 */
    triggerStatus?: boolean;
  }

  export interface ListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  export interface JobInfoVo {
    id: string;
    jobGroup: string;
    jobDesc: string;
    author?: any;
    alarmEmail?: any;
    scheduleType: string;
    scheduleConf: string;
    misfireStrategy: string;
    executorRouteStrategy?: any;
    executorHandler: string;
    executorParam: string;
    executorBlockStrategy: string;
    executorTimeout: number;
    executorFailRetryCount: number;
    glueType: string;
    glueSource: string;
    glueRemark: string;
    glueUpdatetime?: any;
    triggerStatus: boolean;
    executorAddress: string[];
  }
  // 参数接口
  export interface ListJobLogPageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  export interface TaskLogVo {
    id: string;
    jobGroup: string;
    jobId: number;
    executorAddress?: any;
    executorHandler: string;
    executorParam: string;
    executorShardingParam?: any;
    executorFailRetryCount: number;
    triggerTime: string;
    triggerCode: number;
    triggerMsg: string;
    handleTime?: any;
    handleCode?: any;
    handleMsg?: any;
    alarmStatus: number;
    logTraceId: string;
  }
}
