import http from "@/config/axios";
import { AUTO_CODE_PATH } from "@/api/config/servicePort";
import { Template } from "@/api/autocode/template/types";
import { ResPage } from "@/api/interface";

/**
 * 新增代码模板
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
export const createAutoCodeTemplate = (params: Template.CreateOrUpdateAutoCodeTemplateParams) => {
  return http.post(AUTO_CODE_PATH + `/autoCodeTemplate`, params);
};

/**
 * 根据ID更新代码模板
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
export const updateAutoCodeTemplate = (params: Template.CreateOrUpdateAutoCodeTemplateParams) => {
  return http.put(AUTO_CODE_PATH + `/autoCodeTemplate`, params);
};

/**
 * 删除选中代码模板
 * @param {array} params integer
 * @returns
 */
export const deleteAutoCodeTemplateByIds = (params: string[]) => {
  return http.delete(AUTO_CODE_PATH + `/autoCodeTemplate`, params);
};

/**
 * 根据ID查询代码模板详情
 * @param {string} id
 * @returns
 */
export const getAutoCodeTemplateDetail = (id: string) => {
  return http.get<Template.TemplateDetail>(AUTO_CODE_PATH + `/autoCodeTemplate/${id}`);
};

/**
 * 删除代码模板
 * @param {string} id
 * @returns
 */
export const deleteAutoCodeTemplate = (id: string) => {
  return http.delete(AUTO_CODE_PATH + `/autoCodeTemplate/${id}`);
};

/**
 * 分页查询
 * @param {object} params QueryDto
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.keyword 关键字
 * @param {string} params.name 模板名称
 * @param {string} params.language 语言类型
 * @param {string} params.templateEngine 模板引擎类型 Thymeleaf ,FreeMaker
 * @returns
 */
export const getAutoCodeTemplateList = (params: Template.GetAutoCodeTemplateListParams) => {
  return http.post<ResPage<Template.TemplateDetail>>(AUTO_CODE_PATH + `/autoCodeTemplate/page`, params);
};

export const getLanguageType = [
  {
    value: "Java",
    label: "Java"
  },
  {
    value: "JavaScript",
    label: "JavaScript"
  },
  {
    value: "Vue",
    label: "Vue"
  },
  {
    value: "Xml",
    label: "Xml"
  }
];
export const getEngineType = [
  {
    value: "FreeMarker",
    label: "FreeMarker"
  },
  {
    value: "Enjoy",
    label: "Enjoy"
  },
  {
    value: "Velocity",
    label: "Velocity"
  },
  {
    value: "Beetl",
    label: "Beetl"
  }
];
