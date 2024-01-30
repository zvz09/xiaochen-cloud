<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDialog('新增')">新增模型</el-button>
      </template>

      <template #version="scope">
        <el-text type="primary">v{{ scope.row.version }}</el-text>
      </template>

      <!-- 表格操作 -->
      <template #operation="scope">
        <!--        <el-button type="primary" link :icon="EditPen" @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button type="primary" link :icon="Delete" @click="deleteModel(scope.row)">删除</el-button>-->
        <el-tooltip content="编辑" placement="top-start">
          <el-button :icon="EditPen" size="small" type="primary" @click="openDialog('编辑', scope.row)" />
        </el-tooltip>
        <el-tooltip content="设计" placement="top-start">
          <el-button :icon="Discount" size="small" type="primary" @click="openDrawer(scope.row)" />
        </el-tooltip>
        <el-tooltip content="部署" placement="top-start">
          <el-button :icon="VideoPlay" size="small" type="primary" @click="deployFunc(scope.row)" />
        </el-tooltip>
        <el-tooltip content="流程图" placement="top-start">
          <el-button :icon="View" size="small" type="primary" @click="openPreviewDialog(scope.row)" />
        </el-tooltip>
        <el-tooltip content="历史" placement="top-start">
          <el-button :icon="Tickets" size="small" type="primary" @click="openHistoryDialog(scope.row)" />
        </el-tooltip>
        <el-tooltip content="删除" placement="top-start">
          <el-button :icon="Delete" size="small" type="danger" @click="deleteModel(scope.row)" />
        </el-tooltip>
      </template>
    </ProTable>
    <ModelDialog ref="modelDialogRef" />
    <ModelDeviseDrawer ref="modelDeviseDrawerRef" />
    <PreviewModelDialog ref="previewModelDialogRef" />
    <HistoryDialog ref="historyDialogRef" />
  </div>
</template>
<script setup lang="tsx" name="formManage">
import { CirclePlus, Delete, Discount, EditPen, Tickets, VideoPlay, View } from "@element-plus/icons-vue";
import ModelDialog from "@/views/flowable/model/ModelDialog.vue";
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Model } from "@/api/flowable/model/types";
import { pageModel, addModel, editModel, removeModel, deployModel } from "@/api/flowable/model";
import { useHandleData } from "@/hooks/useHandleData";
import ModelDeviseDrawer from "@/views/flowable/model/ModelDeviseDrawer.vue";
import PreviewModelDialog from "@/views/flowable/model/PreviewModelDialog.vue";
import HistoryDialog from "@/views/flowable/model/HistoryDialog.vue";

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
  { prop: "operation", label: "操作", fixed: "right", width: 350 }
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

const modelDialogRef = ref<InstanceType<typeof ModelDialog> | null>(null);
const openDialog = (title: string, row: Partial<Model.VO> = {}) => {
  const params = reactive<Model.DrawerProps>({
    title: title,
    row: { ...row },
    api: title === "新增" ? addModel : title === "编辑" ? editModel : undefined,
    getTableList: proTable.value?.getTableList,
    modelId: row.modelId
  });
  modelDialogRef.value?.acceptParams(params);
};

const deleteModel = async (params: Model.VO) => {
  await useHandleData(removeModel, [params.modelId], `删除【${params.modelName}】模型`);
  proTable.value?.getTableList();
};

const deployFunc = async (params: Model.VO) => {
  await useHandleData(deployModel, params.modelId, `部署【${params.modelName}】模型的【v${params.version}】版本`);
  proTable.value?.getTableList();
};

const modelDeviseDrawerRef = ref<InstanceType<typeof ModelDeviseDrawer> | null>(null);
const openDrawer = (row: Partial<Model.VO> = {}) => {
  const params = reactive<Model.DrawerProps>({
    title: "流程设计",
    row: { ...row },
    getTableList: proTable.value?.getTableList,
    modelId: row.modelId
  });
  modelDeviseDrawerRef.value?.acceptParams(params);
};

const previewModelDialogRef = ref<InstanceType<typeof PreviewModelDialog> | null>(null);
const openPreviewDialog = (row: Partial<Model.VO> = {}) => {
  const params = reactive<Model.DrawerProps>({
    title: "查看流程图",
    row: { ...row },
    modelId: row.modelId
  });
  previewModelDialogRef.value?.acceptParams(params);
};

const historyDialogRef = ref<InstanceType<typeof HistoryDialog> | null>(null);
const openHistoryDialog = (row: Partial<Model.VO> = {}) => {
  const params = reactive<Model.DrawerProps>({
    title: "查看历史版本",
    row: { ...row },
    getTableList: proTable.value?.getTableList,
    modelId: row.modelId
  });
  historyDialogRef.value?.acceptParams(params);
};
</script>
