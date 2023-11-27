// ? 系统全局字典

import { useDictionaryStore } from "@/stores/modules/dictionary";
import { Dictionary } from "@/api/system/dictionary/types";
import { ResultData } from "@/api/interface";

/**
 * @description：用户性别
 */
export const genderType = [
  { label: "男", value: 1 },
  { label: "女", value: 2 }
];

/**
 * @description：用户状态
 */
export const userStatus = [
  { label: "启用", value: 1, tagType: "success" },
  { label: "禁用", value: 0, tagType: "danger" }
];

//  获取字典方法 使用示例 getDict('gender').then(res)  或者 async函数下 const res = await getDict('gender')
export const getDict = (encode: string): Promise<Dictionary.DictionaryDetailVO[] | null> => {
  const dictionaryStore = useDictionaryStore();
  return dictionaryStore.getDictionary(encode);
};

export const getDictResultData = (encode: string): Promise<ResultData<Dictionary.DictionaryDetailVO[]> | null> => {
  const dictionaryStore = useDictionaryStore();
  return dictionaryStore.getDictionaryResultData(encode);
};
