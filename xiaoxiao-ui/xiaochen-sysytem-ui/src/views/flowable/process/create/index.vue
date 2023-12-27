<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #version="scope">
        <el-text type="primary">v{{ scope.row.version }}</el-text>
      </template>
      <template #suspended="scope">
        <el-tag v-if="!scope.row.suspended" type="success">激活</el-tag>
        <el-tag v-if="scope.row.suspended" type="warning">挂起</el-tag>
      </template>
    </ProTable>
  </div>
</template>
<script setup lang="tsx" name="createProcess">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { startProcessList } from "@/api/flowable/process";
import { Process } from "@/api/flowable/process/types";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Process.FlowableDeployVo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "processKey",
    label: "流程标识",
    search: { el: "input", tooltip: "流程标识" }
  },
  {
    prop: "processName",
    label: "流程名称"
  },
  {
    prop: "category",
    label: "流程分类"
  },
  {
    prop: "version",
    label: "流程版本"
  },
  {
    prop: "suspended",
    label: "状态"
  },
  {
    prop: "deploymentTime",
    label: "部署时间"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return startProcessList(params);
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
