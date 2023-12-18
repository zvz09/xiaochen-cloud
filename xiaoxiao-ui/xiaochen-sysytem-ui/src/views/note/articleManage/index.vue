<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')">新增笔记</el-button>
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
import { selectArticleList } from "@/api/note/article";
import ArticleDrawer from "@/views/note/articleManage/ArticleDrawer.vue";
import { CirclePlus } from "@element-plus/icons-vue";
import { User } from "@/api/system/user/types";

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

const openDrawer = (title: string, row: Partial<User.UserVO> = {}) => {
  console.log(row);
  const params = {};
  drawerRef.value?.acceptParams(params);
};
</script>
