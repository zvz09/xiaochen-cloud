<template>
  <el-drawer v-model="drawer" size="100%" :title="`${drawerProps.title}笔记`" destroy-on-close>
    <el-form ref="formRef" label-width="100px" label-suffix=" :" :rules="rules" :model="drawerProps.row">
      <el-row>
        <el-col :span="12">
          <el-form-item label="标题" prop="title">
            <el-input v-model="drawerProps.row.title" placeholder="请填写标题" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分类" prop="categoryName">
            <el-select
              v-model="drawerProps.row.categoryName"
              filterable
              allow-create
              default-first-option
              :reserve-keyword="false"
              placeholder="请选择分类"
            >
              <el-option v-for="item in categoryOptions" :key="item.name" :label="item.name" :value="item.name" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="标签" prop="tags">
        <el-select v-model="drawerProps.row.tags" multiple filterable allow-create default-first-option :reserve-keyword="false">
          <el-option v-for="item in tagsOptions" :key="item.name" :label="item.name" :value="item.name" />
        </el-select>
      </el-form-item>
    </el-form>
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
import { reactive, ref } from "vue";
import { Article } from "@/api/note/article/types";
import { Category } from "@/api/note/category/types";
import { Tags } from "@/api/note/tags/types";
import { selectArticleById } from "@/api/note/article";
import { pageCategory } from "@/api/note/category";
import { pageTags } from "@/api/note/tags";
import { ElMessage, FormInstance } from "element-plus";

const contentStr = ref("");
let vueEditor: Vditor | null = null;

let categoryOptions: Category.CategoryVO[] | null = [];
pageCategory({ pageNum: 1, pageSize: 999 }).then(res => {
  categoryOptions = res.data.records;
});

let tagsOptions: Tags.TagsVO[] | null = [];
pageTags({ pageNum: 1, pageSize: 999 }).then(res => {
  tagsOptions = res.data.records;
});

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
  if (params.title === "编辑") {
    selectArticleById(params.row.id as string).then(res => {
      drawer.value = true;
      contentStr.value = res.data.content;
    });
  } else {
    drawer.value = true;
  }
  console.log(vueEditor);
};

const options: IOptions = {
  mode: "sv",
  height: "88%",
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

// 提交数据（新增/编辑）
const formRef = ref<FormInstance>();
const rules = reactive({
  title: [{ required: true, message: "请填写标题" }],
  categoryId: [{ required: true, message: "请选择分类名" }]
});
function submit() {
  formRef.value!.validate(async valid => {
    if (!valid) return;
    try {
      drawerProps.value.row.content = vueEditor?.getValue();
      await drawerProps.value.api!(drawerProps.value.row);
      ElMessage.success({ message: `${drawerProps.value.title}笔记成功！` });
      drawerProps.value.getTableList!();
      vueEditor?.destroy();
      drawer.value = false;
    } catch (error) {
      console.log(error);
    }
  });
}

defineExpose({
  acceptParams
});
</script>
