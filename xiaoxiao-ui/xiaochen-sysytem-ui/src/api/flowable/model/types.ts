export namespace Model {
  // 参数接口
  export interface SaveParams {
    /* */
    modelId: string;
    /* */
    modelName: string;
    /* */
    modelKey: string;
    /* */
    category: string;
    /* */
    description?: string;
    /* */
    formType?: number;
    /* */
    formId?: number;
    /* */
    bpmnXml?: string;
    /* */
    newVersion?: boolean;
  }

  // 参数接口
  export interface PageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*模型名称 */
    modelName?: string;

    /*模型Key */
    modelKey?: string;

    /*流程分类 */
    category?: string;
  }

  // 参数接口
  export interface DTO {
    /* */
    modelId?: string;

    /* */
    modelName: string;

    /* */
    modelKey: string;

    /* */
    category: string;

    /* */
    description?: string;

    /* */
    formType?: number;

    /* */
    formId?: number;

    /* */
    bpmnXml?: string;

    /* */
    newVersion?: boolean;
  }

  export interface VO {
    modelId: string;
    modelName: string;
    modelKey: string;
    category: string;
    version: number;
    formType: number;
    formId: number;
    description: string;
    createTime: string;
    bpmnXml: string;
    content: string;
  }

  export interface ListenerVo {
    classPath: string;
    name: string;
    typesOfSupport: string[];
  }

  // 参数接口
  export interface HistoryListParams {
    /*页码 */
    pageNum?: number;
    /*每页大小 */
    pageSize?: number;
    /*关键字 */
    keyword?: string;
  }
}
