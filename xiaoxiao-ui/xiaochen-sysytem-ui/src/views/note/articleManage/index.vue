<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')">新增笔记</el-button>
      </template>

      <!-- 来源 -->
      <template #originalUrl="scope">
        <el-link :href="scope.row.originalUrl" target="success">{{ scope.row.originalUrl }}</el-link>
      </template>

      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="EditPen" @click="openDrawer('编辑', scope.row)">编辑</el-button>
      </template>
    </ProTable>
    <ArticleDrawer ref="drawerRef" />
  </div>
</template>

<script setup lang="tsx" name="articleManage">
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { reactive, ref } from "vue";
import { Article } from "@/api/note/article/types";
import { selectArticleList, insertArticle, updateArticle } from "@/api/note/article";
import ArticleDrawer from "@/views/note/articleManage/ArticleDrawer.vue";
import { CirclePlus, EditPen } from "@element-plus/icons-vue";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Article.ArticleVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "title",
    label: "标题",
    width: 400,
    search: { el: "input", tooltip: "标题" }
  },
  {
    prop: "stick",
    label: "置顶"
  },
  {
    prop: "original",
    label: "原创"
  },
  {
    prop: "originalUrl",
    label: "来源"
  },
  {
    prop: "quantity",
    label: "阅读量"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return selectArticleList(params);
};

const initParam = reactive({});

const dataCallback = (data: any) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

const drawerRef = ref<InstanceType<typeof ArticleDrawer> | null>(null);

const openDrawer = (title: string, row: Partial<Article.ArticleVO> = {}) => {
  console.log(row);
  const params = reactive<Article.DrawerProps>({
    title: title,
    row: { ...row },
    api: title === "新增" ? insertArticle : title === "编辑" ? updateArticle : undefined,
    getTableList: proTable.value?.getTableList,
    id: row.id
  });
  drawerRef.value?.acceptParams(params);
};
</script>
