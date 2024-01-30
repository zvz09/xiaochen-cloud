import { defineStore } from "pinia";
import { getMicroRouter } from "@/utils";
import { MicroInfo, microTree } from "@/api/modules/micro";

export const useAuthStore = defineStore({
  id: "xiaochen-auth",
  state: (): { microList: MicroInfo[] } => ({
    microList: []
  }),
  getters: {
    microListGet: state => state.microList,
    microRouterListGet: state => getMicroRouter(state.microList)
  },
  actions: {
    async getMicroList() {
      const { data } = await microTree();
      this.microList = data;
    }
  }
});
