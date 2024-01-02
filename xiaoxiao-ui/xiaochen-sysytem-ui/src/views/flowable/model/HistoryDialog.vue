<template>
  <el-dialog v-model="historyDialog" title="历史版本" width="70%">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #version="scope">
        <el-text type="primary">v{{ scope.row.version }}</el-text>
      </template>

      <template #operation="scope">
        <el-tooltip content="部署" placement="top-start">
          <el-button :icon="VideoPlay" size="small" type="primary" @click="deployFunc(scope.row)" />
        </el-tooltip>
        <el-tooltip content="流程图" placement="top-start">
          <el-button :icon="View" size="small" type="primary" @click="openPreviewDialog(scope.row)" />
        </el-tooltip>
      </template>
    </ProTable>
    <PreviewModelDialog ref="previewModelDialogRef" />
  </el-dialog>
</template>
<script setup lang="ts">
import { reactive, ref } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Model } from "@/api/flowable/model/types";
import { deployModel, historyModelList } from "@/api/flowable/model";
import { VideoPlay, View } from "@element-plus/icons-vue";
import { useHandleData } from "@/hooks/useHandleData";
import PreviewModelDialog from "@/views/flowable/model/PreviewModelDialog.vue";

const historyDialog = ref(false);
const proTable = ref<ProTableInstance>();

const previewModelDialogRef = ref<InstanceType<typeof PreviewModelDialog> | null>(null);

const drawerProps = ref<Model.DrawerProps>({
  title: "",
  row: {
    modelId: ""
  },
  modelId: ""
});

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
  return historyModelList(drawerProps.value.row.modelKey as string, params);
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

const acceptParams = async (params: Model.DrawerProps) => {
  drawerProps.value = params;
  historyDialog.value = true;
};

const deployFunc = async (params: Model.VO) => {
  await useHandleData(deployModel, params.modelId, `部署【${params.modelName}】模型的【v${params.version}】版本`);
  await proTable.value!.getTableList();
};

const openPreviewDialog = (row: Partial<Model.VO> = {}) => {
  const params = reactive<Model.DrawerProps>({
    title: "查看流程图",
    row: { ...row },
    modelId: row.modelId
  });
  previewModelDialogRef.value?.acceptParams(params);
};

defineExpose({
  acceptParams
});
</script>
