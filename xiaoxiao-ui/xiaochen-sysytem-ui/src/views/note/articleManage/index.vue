<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">

    </ProTable>
  </div>
</template>

<script setup lang="tsx" name="articleManage">
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { reactive, ref } from "vue";
import {
  Article
} from "@/api/note/article/types";
import {
  selectArticleList
} from "@/api/note/article";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Article.ArticleVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "title",
    label: "标题",
    search: { el: "input", tooltip: "标题" }
  },
  {
    prop: "stick",
    label: "置顶"
  },
  {
    prop: "publish",
    label: "发布"
  },
  {
    prop: "original",
    label: "原创"
  },
  {
    prop: "阅读量",
    label: "quantity"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 480 }
]);

const getTableList = (params: any) => {
  return listDictionary(params);
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


</script>
