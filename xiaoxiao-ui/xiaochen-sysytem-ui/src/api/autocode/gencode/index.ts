import http from "@/config/axios";
import { AUTO_CODE_PATH } from "@/api/config/servicePort";
import { GenCode } from "@/api/autocode/gencode/types";
import { ResPage } from "@/api/interface";

/**
 * 下载代码
 * @param {object} params GenConfig
 * @param {string} params.basePackageName
 * @param {string} params.description
 * @param {string} params.author
 * @param {string} params.datetime
 * @param {string} params.tableName
 * @param {string} params.className
 * @param {string} params.businessName
 * @param {string} params.classShortName
 * @param {array} params.columns
 * @param {array} params.templateIds
 * @param {string} params.capBusinessName
 * @param {string} params.capClassName
 * @param {string} params.capClassShortName
 * @returns
 */
export const downloadCode = (params: GenCode.DownloadCodeParams) => {
  return http.download(AUTO_CODE_PATH + `/gencode/downloadCode`, params);
};

/**
 * 解析建表SQL
 * @param {object} params 建表sql
 * @param {string} params.sql
 * @param {string} params.dbType
 * @returns
 */
export const parseCreateSql = (params: GenCode.ParseCreateSqlParams) => {
  return http.post<GenCode.ParseCreateSqlRes>(AUTO_CODE_PATH + `/gencode/parseSql`, params);
};

/**
 * 预览代码
 * @param {object} params GenConfig
 * @param {string} params.basePackageName
 * @param {string} params.description
 * @param {string} params.author
 * @param {string} params.datetime
 * @param {string} params.tableName
 * @param {string} params.className
 * @param {string} params.businessName
 * @param {string} params.classShortName
 * @param {array} params.columns
 * @param {array} params.templateIds
 * @param {string} params.capClassName
 * @param {string} params.capBusinessName
 * @param {string} params.capClassShortName
 * @returns
 */
export const previewCode = (params: GenCode.ParseCreateSqlRes) => {
  return http.post<GenCode.PreviewCodeRes[]>(AUTO_CODE_PATH + `/gencode/previewCode`, params);
};

/**
 * 验证代码模板
 * @param {object} params 自动生成代码模板
 * @param {number} params.id
 * @param {string} params.name 模板名称
 * @param {string} params.language 语言类型
 * @param {string} params.templateEngine 模板引擎类型 Thymeleaf ,FreeMaker
 * @param {string} params.templateType 模板类型
 * @param {string} params.defaultFileName 默认文件名
 * @param {string} params.content 模板内容
 * @returns
 */
export const verifyAutoCodeTemplate = (params: GenCode.VerifyAutoCodeTemplateParams) => {
  return http.post<string>(AUTO_CODE_PATH + `/gencode/verify`, params);
};

/**
 * 分页查询历史记录
 * @param {object} params BasePage
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @returns
 */
export const getGenCodeHistoryList = (params: GenCode.GetGenCodeHistoryListParams) => {
  return http.post<ResPage<GenCode.HistoryListRes>>(AUTO_CODE_PATH + `/gencode/page`, params);
};
