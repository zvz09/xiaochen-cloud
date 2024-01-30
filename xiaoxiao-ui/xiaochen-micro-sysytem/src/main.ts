import { createApp, markRaw } from "vue";
import App from "./App.vue";
// reset style sheet
import "@/styles/reset.scss";
// CSS common style sheet
import "@/styles/common.scss";
// iconfont css
import "@/assets/iconfont/iconfont.scss";
// font css
import "@/assets/fonts/font.scss";
// element css
import "element-plus/dist/index.css";
// element dark css
import "element-plus/theme-chalk/dark/css-vars.css";
// custom element dark css
import "@/styles/element-dark.scss";
// custom element css
import "@/styles/element.scss";
// svg icons
import "virtual:svg-icons-register";
// element plus
import ElementPlus from "element-plus";
// element icons
import * as Icons from "@element-plus/icons-vue";
// custom directives
import directives from "@/directives/index";
// vue Router
import router from "@/routers";
// vue i18n
import I18n from "@/languages/index";
// pinia store
import pinia from "@/stores";
// errorHandler
import errorHandler from "@/utils/errorHandler";
// 导入组件库
import NgFormElementPlus from "ng-form-elementplus";
import "ng-form-elementplus/lib/style.css";

import WujieVue from "wujie-vue3";

declare global {
  interface Window {
    // 是否存在无界
    __POWERED_BY_WUJIE__?: boolean;
    // 子应用mount函数
    __WUJIE_MOUNT: () => void;
    // 子应用unmount函数
    __WUJIE_UNMOUNT: () => void;
    // 子应用无界实例
    __WUJIE: { mount: () => void };
  }
}

if (window.__POWERED_BY_WUJIE__) {
  let app;
  window.__WUJIE_MOUNT = () => {
    console.log("window.__WUJIE_MOUNT");
    app = createApp(App);
    const NgForm = markRaw(NgFormElementPlus);
    app.config.errorHandler = errorHandler;
    Object.keys(Icons).forEach(key => {
      app.component(key, Icons[key as keyof typeof Icons]);
    });

    app.use(ElementPlus).use(directives).use(router).use(NgForm).use(I18n).use(pinia).use(WujieVue).mount("#app");
  };
  window.__WUJIE_UNMOUNT = () => {
    console.log("window.__WUJIE_UNMOUNT");
    app.unmount();
  };
  /*
    由于vite是异步加载，而无界可能采用fiber执行机制
    所以mount的调用时机无法确认，框架调用时可能vite
    还没有加载回来，这里采用主动调用防止用没有mount
    无界mount函数内置标记，不用担心重复mount
  */
  window.__WUJIE.mount();
} else {
  let app = createApp(App);
  const NgForm = markRaw(NgFormElementPlus);
  app.config.errorHandler = errorHandler;
  Object.keys(Icons).forEach(key => {
    app.component(key, Icons[key as keyof typeof Icons]);
  });

  app.use(ElementPlus).use(directives).use(router).use(NgForm).use(I18n).use(pinia).use(WujieVue).mount("#app");
}
