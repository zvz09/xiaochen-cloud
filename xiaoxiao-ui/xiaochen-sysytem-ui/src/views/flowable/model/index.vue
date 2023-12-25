<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')">新增模型</el-button>
      </template>

      <template #version="scope">
        <el-text type="primary">v{{ scope.row.version }}</el-text>
      </template>

      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="EditPen" @click="openDrawer('编辑', scope.row)">编辑</el-button>
      </template>
    </ProTable>
    <ModelDialog ref="drawerRef" />
  </div>
</template>
<script setup lang="tsx" name="formManage">
import { CirclePlus, EditPen } from "@element-plus/icons-vue";
import ModelDialog from "@/views/flowable/model/ModelDialog.vue";
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Model } from "@/api/flowable/model/types";
import { pageModel, addModel, editModel } from "@/api/flowable/model";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Model.VO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "modelKey",
    label: "模型标识"
  },
  {
    prop: "modelName",
    label: "模型名称"
  },
  {
    prop: "category",
    label: "流程分类"
  },
  {
    prop: "version",
    label: "模型版本"
  },
  {
    prop: "description",
    label: "描述"
  },
  {
    prop: "createTime",
    label: "创建时间"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return pageModel(params);
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

const drawerRef = ref<InstanceType<typeof ModelDialog> | null>(null);
const openDrawer = (title: string, row: Partial<Model.VO> = {}) => {
  const params = reactive<Model.DrawerProps>({
    title: title,
    row: { ...row },
    api: title === "新增" ? addModel : title === "编辑" ? editModel : undefined,
    getTableList: proTable.value?.getTableList,
    modelId: row.modelId
  });
  drawerRef.value?.acceptParams(params);
};
</script>
