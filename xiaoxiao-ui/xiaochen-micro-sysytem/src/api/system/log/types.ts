export namespace Log {
  // 参数接口
  export interface PageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /* */
    applicationName?: string;

    /* */
    traceId?: string;

    /* */
    host?: string;

    /* */
    message?: string;

    /* */
    begin?: string;

    /* */
    end?: string;
  }

  export interface LogVO {
    id: string;
    dateTime: string;
    applicationName: string;
    level: string;
    traceId?: any;
    host: string;
    className: string;
    message: string;
    error?: any;
    timestamp: string;
  }

  // 参数接口
  export interface OperationLogPageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /* */
    serviceName?: string;

    /* */
    requestUrl?: string;

    /* */
    operatorId?: number;

    /* */
    operatorName?: string;

    /* */
    businessType?: string;

    /* */
    begin: string;

    /* */
    end: string;
  }

  export interface OperationLogVo {
    description: string;
    serviceName: string;
    apiClass: string;
    apiMethod: string;
    traceId: string;
    elapse: number;
    requestMethod: string;
    requestUrl: string;
    requestArguments: string;
    responseResult: string;
    requestIp: string;
    success: boolean;
    errorMsg?: any;
    operatorId: number;
    operatorName: string;
    businessType: string;
    bizNo?: any;
    operationTimeStart: string;
    operationTimeEnd: string;
  }
}
