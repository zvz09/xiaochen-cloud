export namespace Template {
  // 参数接口
  export interface CreateOrUpdateAutoCodeTemplateParams {
    /* */
    id: string;
    /*模板名称 */
    name: string;

    /*语言类型 */
    language: string;

    /*模板引擎类型 Thymeleaf ,FreeMaker */
    templateEngine: string;

    /*模板类型 */
    templateType: string;

    /*默认文件名 */
    defaultFileName: string;

    /*模板内容 */
    content: string;
  }

  // 参数接口
  export interface GetAutoCodeTemplateListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*模板名称 */
    name?: string;

    /*语言类型 */
    language?: string;

    /*模板引擎类型 Thymeleaf ,FreeMaker */
    templateEngine?: string;
  }

  export interface TemplateDetail {
    id: string;
    createdAt: string;
    updatedAt: string;
    deleted: boolean;
    name: string;
    language: string;
    templateEngine: string;
    templateType: string;
    defaultFileName: string;
    content: string;
  }
}
