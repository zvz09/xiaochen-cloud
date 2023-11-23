import { getAllByDictionaryEncode } from "@/api/system/dictionary";
import { defineStore } from "pinia";
import { reactive, toRaw, Ref } from "vue";
import { Dictionary } from "@/api/system/dictionary/types";
import { ResultData } from "@/api/interface";

// 定义字典映射的类型
type DictionaryMap = Record<string, Dictionary.DictionaryDetailVO[]>;
type DictionaryResultDataMap = Record<string, ResultData<Dictionary.DictionaryDetailVO[]>>;

export const useDictionaryStore = defineStore("dictionary", () => {
  // 使用 Ref 泛型来明确 dictionaryMap 的类型
  const dictionaryMap: Ref<DictionaryMap> = reactive({});
  // 使用 Ref 泛型来明确 dictionaryResultDataMap 的类型
  const dictionaryResultDataMap: Ref<DictionaryResultDataMap> = reactive({});

  const setDictionaryMaps = (encode: string, dictionaryRes: ResultData<Dictionary.DictionaryDetailVO[]>) => {
    dictionaryResultDataMap.value[encode] = toRaw(dictionaryRes);
    dictionaryMap.value[encode] = toRaw(dictionaryRes.data);
  };

  const getDictionary = async (encode: string): Promise<Dictionary.DictionaryDetailVO[] | null> => {
    if (dictionaryMap.value[encode]?.length) {
      return dictionaryMap.value[encode];
    } else {
      const data = await getAllByDictionaryEncode(encode);
      setDictionaryMaps(encode, data);
      return dictionaryMap.value[encode];
    }
  };

  const getDictionaryResultData = async (encode: string): Promise<ResultData<Dictionary.DictionaryDetailVO[]> | null> => {
    if (dictionaryResultDataMap.value[encode]?.data) {
      return dictionaryResultDataMap.value[encode];
    } else {
      const data = await getAllByDictionaryEncode(encode);
      setDictionaryMaps(encode, data);
      return dictionaryResultDataMap.value[encode];
    }
  };

  return {
    dictionaryMap,
    setDictionaryMaps,
    getDictionary,
    dictionaryResultDataMap,
    getDictionaryResultData
  };
});
