import { getAllByDictionaryEncode } from "@/api/system/dictionary";
import { defineStore } from "pinia";
import { ref } from "vue";
import { Dictionary } from "@/api/system/dictionary/types";
import { ResultData } from "@/api/interface";

export const useDictionaryStore = defineStore("dictionary", () => {
  const dictionaryMap = ref({});
  // for ProTable
  const dictionaryResultDataMap = ref({});

  const setDictionaryMaps = (encode: string, dictionaryRes: ResultData<Dictionary.DictionaryDetailVO[]>) => {
    console.log("setDictionaryMaps", dictionaryRes);
    dictionaryResultDataMap.value[encode] = dictionaryRes;
    dictionaryMap.value[encode] = dictionaryRes.data;
    dictionaryResultDataMap.value["xxxxxxxx"] = dictionaryRes;
    console.log("xxxxx-setDictionaryMaps", dictionaryResultDataMap.value["xxxxxxxx"]);
  };

  const getDictionary = async (encode: string): Promise<Dictionary.DictionaryDetailVO[]> => {
    if (dictionaryMap.value[encode] && dictionaryMap.value[encode].length) {
      return dictionaryMap.value[encode];
    } else {
      const data = await getAllByDictionaryEncode(encode);
      setDictionaryMaps(encode, data);
      return dictionaryMap.value[encode];
    }
  };

  const getDictionaryResultData = async (encode: string): Promise<ResultData<Dictionary.DictionaryDetailVO[]>> => {
    if (dictionaryResultDataMap.value[encode] && dictionaryResultDataMap.value[encode].data) {
      return dictionaryResultDataMap.value[encode];
    } else {
      const data = await getAllByDictionaryEncode(encode);
      console.log("xxxxxxx", data);
      setDictionaryMaps(encode, data);
      console.log("sssssssss", dictionaryResultDataMap.value[encode]);
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
