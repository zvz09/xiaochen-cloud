import router from "@/routers/index";
import { LOGIN_URL } from "@/config";
import { RouteRecordRaw } from "vue-router";
import { ElNotification } from "element-plus";
import { useUserStore } from "@/stores/modules/user";
import { useAuthStore } from "@/stores/modules/auth";
import { HomeMenu } from "@/utils";

// 引入 views 文件夹下所有 vue 文件
const modules = import.meta.glob("@/views/**/*.vue");

/**
 * @description 初始化动态路由
 */
export const initDynamicRouter = async () => {
  const userStore = useUserStore();
  const authStore = useAuthStore();

  try {
    // 1.获取微前端配置
    await authStore.getMicroList();

    // 2.判断当前用户有没有菜单权限
    if (!authStore.microRouterListGet.length) {
      ElNotification({
        title: "无权限访问",
        message: "当前账号无任何权限，请联系系统管理员！",
        type: "warning",
        duration: 3000
      });
      userStore.setToken("");
      router.replace(LOGIN_URL);
      return Promise.reject("No permission");
    }
    // 3 添加微前端路由
    authStore.microRouterListGet.forEach(item => {
      if (item.name === HomeMenu.name) {
        item.component = modules["/src/views/home/index.vue"];
      } else {
        item.component = modules["/src/views/micro/index.vue"];
      }
      router.addRoute("layout", item as unknown as RouteRecordRaw);
    });
  } catch (error) {
    // 当按钮 || 菜单请求出错时，重定向到登陆页
    userStore.setToken("");
    await router.replace(LOGIN_URL);
    return Promise.reject(error);
  }
};
