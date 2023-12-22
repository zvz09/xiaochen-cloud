export namespace FlowableDeploy {
  // 参数接口
  export interface PageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*流程标识 */
    processKey?: string;

    /*流程名称 */
    processName?: string;

    /*流程分类 */
    category?: string;

    /*状态 */
    state?: string;

    /*请求参数 */
    params?: Record<string, string>;
  }

  // 参数接口
  export interface PublishListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  export interface VO {
    definitionId: string;
    processName: string;
    processKey: string;
    category: string;
    version: number;
    formId: string;
    formName: string;
    deploymentId: string;
    suspended: boolean;
    deploymentTime: string;
  }
}
