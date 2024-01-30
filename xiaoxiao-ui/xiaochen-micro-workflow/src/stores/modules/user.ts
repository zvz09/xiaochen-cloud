import { defineStore } from "pinia";
import { UserState } from "@/stores/interface";
import piniaPersistConfig from "@/stores/helper/persist";

export const useUserStore = defineStore({
  id: "xiaochen-user",
  state: (): UserState => ({
    token: "",
    userInfo: {}
  }),
  getters: {},
  actions: {
    // Set Token
    setToken(token: string) {
      this.token = token;
    },
    // Set setUserInfo
    setUserInfo(userInfo: UserState["userInfo"]) {
      this.userInfo = userInfo;
    }
  },
  persist: piniaPersistConfig("xiaochen-user")
});
