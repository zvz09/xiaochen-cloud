import { defineStore } from "pinia";
import { AuthState } from "@/stores/interface";
import { listTree } from "@/api/system/menu";
import { getAllBreadcrumbList, getFlatMenuList, getShowMenuList } from "@/utils";
import { listPermCodes } from "@/api/system/role";

export const useAuthStore = defineStore({
  id: "xiaochen-auth",
  state: (): AuthState => ({
    //权限字列表
    authPermCodeList: {},
    // 菜单权限列表
    authMenuList: [],
    // 当前页面的 router name，用来做按钮权限筛选
    routeName: ""
  }),
  getters: {
    // 权限字列表
    authPermCodeListGet: state => state.authPermCodeList,
    // 菜单权限列表 ==> 这里的菜单没有经过任何处理
    authMenuListGet: state => state.authMenuList,
    // 菜单权限列表 ==> 左侧菜单栏渲染，需要剔除 isHide == true
    showMenuListGet: state => getShowMenuList(state.authMenuList),
    // 菜单权限列表 ==> 扁平化之后的一维数组菜单，主要用来添加动态路由
    flatMenuListGet: state => getFlatMenuList(state.authMenuList),
    // 递归处理后的所有面包屑导航列表
    breadcrumbListGet: state => getAllBreadcrumbList(state.authMenuList)
  },
  actions: {
    // Get getAuthPermCodeList
    async getAuthPermCodeList() {
      const { data } = await listPermCodes();
      this.authPermCodeList = data;
    },
    // Get AuthMenuList
    async getAuthMenuList() {
      const { data } = await listTree();
      this.authMenuList = data;
    },
    // Set RouteName
    async setRouteName(name: string) {
      this.routeName = name;
    }
  }
});
