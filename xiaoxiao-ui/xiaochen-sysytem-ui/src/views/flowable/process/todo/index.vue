<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #procDefVersion="scope">
        <el-tag>v{{ scope.row.procDefVersion }}</el-tag>
      </template>
      <template #startUserName="scope">
        <label>
          {{ scope.row.startUserName }}
          <el-tag size="small" type="info">{{ scope.row.startDeptName }}</el-tag>
        </label>
      </template>
      <template #operation="scope">
        <el-button icon="Tickets" link size="small" type="primary" @click="toDetailProcess(scope.row)">办理</el-button>
      </template>
    </ProTable>
  </div>
</template>
<script setup lang="tsx" name="todoProcess">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { todoProcessTaskList } from "@/api/flowable/process";
import { Process } from "@/api/flowable/process/types";
import { useRouter } from "vue-router";

const router = useRouter();
// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Process.FlowableTaskVo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "taskId",
    label: "任务编号"
  },
  {
    prop: "procDefName",
    label: "流程名称",
    search: { el: "input", tooltip: "流程名称" }
  },
  {
    prop: "procDefVersion",
    label: "流程版本"
  },
  {
    prop: "taskName",
    label: "任务节点"
  },
  {
    prop: "startUserName",
    label: "流程发起人"
  },
  {
    prop: "claimTime",
    label: "接收时间"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return todoProcessTaskList(params);
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

function toDetailProcess(row: Process.FlowableTaskVo) {
  router.push({
    name: "detailProcess",
    params: {
      procInsId: row.procInsId,
      title: "办理-" + row.procInsId
    },
    query: {
      processed: "true",
      taskId: row.taskId
    }
  });
}
</script>
