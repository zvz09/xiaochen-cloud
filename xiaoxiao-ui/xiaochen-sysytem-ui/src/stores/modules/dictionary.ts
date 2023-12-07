// src/store/modules/dictionary.ts

import { defineStore } from "pinia";
import { Ref, toRaw } from "vue";
import { Dictionary } from "@/api/system/dictionary/types";
import { ResultData } from "@/api/interface";
import { getAllByDictionaryEncode } from "@/api/system/dictionary";

export const useDictionaryStore = defineStore("xiaochen-dictionary", {
  state: () => ({
    dictionaryMap: {} as Ref<Record<string, Dictionary.DictionaryDetailVO[]>>,
    dictionaryResultDataMap: {} as Ref<Record<string, ResultData<Dictionary.DictionaryDetailVO[]>>>
  }),

  actions: {
    setDictionaryMaps(encode: string, dictionaryRes: ResultData<Dictionary.DictionaryDetailVO[]>): void {
      this.dictionaryResultDataMap[encode] = toRaw(dictionaryRes);
      this.dictionaryMap[encode] = toRaw(dictionaryRes.data);
    },
    async getDictionary(encode: string): Promise<Dictionary.DictionaryDetailVO[] | null> {
      if (this.dictionaryMap[encode]?.length) {
        return this.dictionaryMap[encode];
      } else {
        const data = await getAllByDictionaryEncode(encode);
        this.setDictionaryMaps(encode, data);
        return this.dictionaryMap[encode];
      }
    },
    async getDictionaryResultData(encode: string): Promise<ResultData<Dictionary.DictionaryDetailVO[]> | null> {
      if (this.dictionaryResultDataMap[encode]?.data) {
        return this.dictionaryResultDataMap[encode];
      } else {
        const data = await getAllByDictionaryEncode(encode);
        this.setDictionaryMaps(encode, data);
        return this.dictionaryResultDataMap[encode];
      }
    }
  }
});
