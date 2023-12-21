<template>
  <el-drawer v-model="drawer" size="100%" :title="`${drawerProps.title}解析类`" destroy-on-close>
    <CodeMirror basic :lang="lang" v-model="drawerProps.row.content" style="" :theme="theme" />

    <template #footer>
      <el-button @click="drawer = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-drawer>
</template>
<script setup lang="tsx" name="ParseClassDrawer">
import { ref } from "vue";
import CodeMirror from "vue-codemirror6";
import { java } from "@codemirror/lang-java";
import { ReptileParseClass } from "@/api/note/reptileParseClass/types";
import { ElMessage } from "element-plus";

const drawer = ref(false);
const lang = ref(java());
// 主题样式设置
const theme = {
  "&": {
    color: "white",
    backgroundColor: "#282c34"
  },
  ".cm-content": {
    caretColor: "#0e9"
  },
  "&.cm-focused .cm-cursor": {
    borderLeftColor: "#0e9"
  },
  "&.cm-focused .cm-selectionBackground, ::selection": {
    backgroundColor: "#074"
  },
  ".cm-gutters": {
    backgroundColor: "#282c34",
    color: "#9b76da",
    border: "none"
  }
};
const drawerProps = ref<ReptileParseClass.DrawerProps>({
  title: "",
  row: {},
  id: ""
});

// 接收父组件传过来的参数
const acceptParams = (params: ReptileParseClass.DrawerProps) => {
  drawerProps.value = params;
  if (params.title === "新增") {
    drawerProps.value.row.content = `package com.zvz09.xiaochen.note.strategy.impl;

import com.zvz09.xiaochen.note.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.note.strategy.ReptileDataParserStrategy;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;


@RequiredArgsConstructor
public class {}Parser implements ReptileDataParserStrategy {
    @Override
    public String getBaseUrl() {
        return "{}";
    }

    @Override
    public ArticleDTO parseData(Document document) {
       //业务逻辑
    }
}
`;
  }
  drawer.value = true;
};

async function submit() {
  if (drawerProps.value.row.content != undefined && drawerProps.value.row.content.length > 0) {
    await drawerProps.value.api!(drawerProps.value.row);
    ElMessage.success({ message: `${drawerProps.value.title}解析类成功！` });
    drawerProps.value.getTableList!();
    drawer.value = false;
  } else {
    ElMessage.error({ message: `请输入解析类内容` });
  }
}

defineExpose({
  acceptParams
});
</script>
