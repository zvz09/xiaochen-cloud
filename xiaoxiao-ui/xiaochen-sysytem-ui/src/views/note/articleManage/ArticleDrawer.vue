<template>
  <!-- 指定一个容器 -->
  <div v-show="drawer" id="vditor"></div>
</template>
<script setup lang="tsx" name="ArticleDrawer">
// 1.1 引入Vditor 构造函数
import Vditor from "vditor";
// 1.2 引入样式
import "vditor/dist/index.css";
import { ref } from "vue";
import { User } from "@/api/system/user/types";

// 2. 获取DOM引用
const vditor = ref();

interface DrawerProps {
  title: string;
  row: Partial<User.UserVO>;
  api?: (params: any) => Promise<any>;
  getTableList?: () => void;
}

const drawer = ref(false);

// 接收父组件传过来的参数
const acceptParams = (params: DrawerProps) => {
  console.log(params);
  drawer.value = true;
  vditor.value = new Vditor("vditor", {
    height: "100vh",
    width: "100vw"
  });
};

defineExpose({
  acceptParams
});
</script>
