import { App, Plugin } from "vue";
import EditorComponent from "./index.vue";

export default {
  install(Vue: App) {
    Vue.component("VueVditor", EditorComponent);
  }
} as Plugin;
