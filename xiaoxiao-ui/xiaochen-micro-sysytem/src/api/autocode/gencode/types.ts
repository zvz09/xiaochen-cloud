import { LanguageSupport } from "@codemirror/language";

export namespace GenCode {
  export interface DownloadCodeParams {
    /* */
    basePackageName?: string;

    /* */
    description?: string;

    /* */
    author?: string;

    /* */
    datetime?: string;

    /* */
    tableName?: string;

    /* */
    className?: string;

    /* */
    businessName?: string;

    /* */
    classShortName?: string;

    /* */
    columns?: {
      /* */
      name?: string;

      /* */
      description?: string;

      /* */
      type?: string;

      /* */
      dto?: boolean;

      /* */
      canNull?: boolean;

      /* */
      query?: boolean;

      /* */
      queryType?: string;

      /* */
      sole?: boolean;

      /* */
      capName?: string;
    }[];

    /* */
    templateIds?: Record<string, unknown>[];

    /* */
    capBusinessName?: string;

    /* */
    capClassName?: string;

    /* */
    capClassShortName?: string;
  }

  export interface ParseCreateSqlParams {
    /* */
    sql: string;

    /* */
    dbType: string;
  }

  export interface SqlColumn {
    name: string;
    description: string;
    type: string;
    dto: boolean;
    canNull: boolean;
    query: boolean;
    queryType: string;
    sole: boolean;
    capName: string;
  }

  export interface ParseCreateSqlRes {
    basePackageName: string;
    description: string;
    author: string;
    datetime: string;
    tableName: string;
    className: string;
    businessName: string;
    classShortName: string;
    columns: SqlColumn[];
    templateIds: any[];
    capClassName: string;
    capBusinessName: string;
    capClassShortName: string;
  }

  export interface PreviewCodeRes {
    title: string;
    name: string;
    type: string;
    langType: LanguageSupport;
    code: string;
  }

  // 参数接口
  export interface VerifyAutoCodeTemplateParams {
    /* */
    id: string;

    /*模板名称 */
    name?: string;

    /*语言类型 */
    language?: string;

    /*模板引擎类型 Thymeleaf ,FreeMaker */
    templateEngine?: string;

    /*模板类型 */
    templateType?: string;

    /*默认文件名 */
    defaultFileName?: string;

    /*模板内容 */
    content?: string;
  }

  // 参数接口
  export interface GetGenCodeHistoryListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  export interface HistoryListRes {
    id: number;
    createdAt: string;
    updatedAt: string;
    deleted: boolean;
    tableName: string;
    description: string;
    genConfig: string;
  }
}
