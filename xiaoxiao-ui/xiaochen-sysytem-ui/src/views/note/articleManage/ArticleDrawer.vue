<template>
  <el-drawer v-model="drawer" size="100%" :title="`${drawerProps.title}`" destroy-on-close>
    <!-- 富文本编辑器 -->
    <VueVditor v-model="contentStr" :options="options" @after="handleAfter" class="editor"></VueVditor>
    <template #footer>
      <el-button @click="drawer = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-drawer>
</template>
<script setup lang="tsx" name="ArticleDrawer">
import Vditor from "vditor";
import VueVditor from "@/components/VueVditor/index.vue";
import "vditor/dist/index.css";
import { ref } from "vue";
import { Article } from "@/api/note/article/types";

const contentStr = ref("");
let vueEditor: Vditor | null = null;

function handleAfter(editor: Vditor) {
  vueEditor = editor;
}

const drawerProps = ref<Article.DrawerProps>({
  title: "",
  row: {},
  id: ""
});

const drawer = ref(false);

// 接收父组件传过来的参数
const acceptParams = (params: Article.DrawerProps) => {
  drawerProps.value = params;
  drawer.value = true;
};

const options: IOptions = {
  // theme: 'dark',
  // preview: {
  // theme: {
  //   current: 'dark'
  // },
  // },
  // 模拟上传
  upload: {
    url: "/aaa",
    handler(files: File[]) {
      console.log(files);
      vueEditor?.insertValue("![](https://gitee.com/letwrong/Picture/raw/master/20210331155321.jpg)");
      return "上传成功";
    }
  }
};

function submit() {
  console.log("--------" + vueEditor?.getValue());
}

defineExpose({
  acceptParams
});
</script>
