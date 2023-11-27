import { getAllByDictionaryEncode } from "@/api/system/dictionary";
import { defineStore } from "pinia";
import { reactive, Ref, toRaw } from "vue";
import { Dictionary } from "@/api/system/dictionary/types";
import { ResultData } from "@/api/interface";
import piniaPersistConfig from "@/stores/helper/persist";

export const useDictionaryStore = defineStore(
  "xiaochen-dictionary",
  () => {
    const dictionaryMap: Ref<Record<string, Dictionary.DictionaryDetailVO[]>> = reactive({});
    const dictionaryResultDataMap: Ref<Record<string, ResultData<Dictionary.DictionaryDetailVO[]>>> = reactive({});

    const setDictionaryMaps = (encode: string, dictionaryRes: ResultData<Dictionary.DictionaryDetailVO[]>) => {
      dictionaryResultDataMap[encode] = toRaw(dictionaryRes);
      dictionaryMap[encode] = toRaw(dictionaryRes.data);
    };

    const getDictionary = async (encode: string): Promise<Dictionary.DictionaryDetailVO[] | null> => {
      if (dictionaryMap[encode]?.length) {
        return dictionaryMap[encode];
      } else {
        const data = await getAllByDictionaryEncode(encode);
        setDictionaryMaps(encode, data);
        return dictionaryMap[encode];
      }
    };

    const getDictionaryResultData = async (encode: string): Promise<ResultData<Dictionary.DictionaryDetailVO[]> | null> => {
      if (dictionaryResultDataMap[encode]?.data) {
        return dictionaryResultDataMap[encode];
      } else {
        const data = await getAllByDictionaryEncode(encode);
        setDictionaryMaps(encode, data);
        return dictionaryResultDataMap[encode];
      }
    };

    return {
      dictionaryMap,
      setDictionaryMaps,
      getDictionary,
      dictionaryResultDataMap,
      getDictionaryResultData
    };
  },
  {
    persist: piniaPersistConfig("dictionary")
  }
);
