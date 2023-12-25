<template>
  <el-dialog v-model="historyDialog" title="历史版本" width="70%">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #version="scope">
        <el-text type="primary">v{{ scope.row.version }}</el-text>
      </template>
    </ProTable>
  </el-dialog>
</template>
<script setup lang="ts">
import { reactive, ref } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { Model } from "@/api/flowable/model/types";
import { historyModelList } from "@/api/flowable/model";

const historyDialog = ref(false);

const drawerProps = ref<Model.DrawerProps>({
  title: "",
  row: {},
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
  return historyModelList(drawerProps.value.row.modelKey, params);
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

defineExpose({
  acceptParams
});
</script>
