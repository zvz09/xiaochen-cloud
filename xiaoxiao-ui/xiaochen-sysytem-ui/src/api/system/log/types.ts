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
}
