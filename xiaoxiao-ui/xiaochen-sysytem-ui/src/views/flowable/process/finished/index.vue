<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #startUserName="scope">
        <label>
          {{ scope.row.startUserName }}
          <el-tag size="small" type="info">{{ scope.row.startDeptName }}</el-tag>
        </label>
      </template>
      <template #operation="scope">
        <el-button icon="Tickets" link size="small" type="primary" @click="handleFlowRecord(scope.row)"> 流转记录</el-button>
        <el-button icon="Tickets" link size="small" type="primary" @click="handleRevoke(scope.row)"> 撤回</el-button>
      </template>
    </ProTable>
  </div>
</template>
<script setup lang="tsx" name="finishedProcess">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { finishedProcessTaskList } from "@/api/flowable/process";
import { Process } from "@/api/flowable/process/types";
import { revokeProcess } from "@/api/flowable/task";
import { ElMessage } from "element-plus";
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
    prop: "processName",
    label: "流程名称",
    search: { el: "input", tooltip: "流程名称" }
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
    prop: "createTime",
    label: "接收时间"
  },
  {
    prop: "finishTime",
    label: "审批时间"
  },
  {
    prop: "duration",
    label: "耗时"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return finishedProcessTaskList(params);
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

function handleFlowRecord(row: Process.FlowableTaskVo) {
  router.push({
    name: "detailProcess",
    params: {
      procInsId: row.procInsId,
      title: "查看-" + row.procInsId
    },
    query: {
      processed: "false",
      taskId: row.taskId
    }
  });
}

/** 撤回任务 */
function handleRevoke(row: Process.FlowableTaskVo) {
  const params = {
    procInsId: row.procInsId,
    taskId: row.taskId
  };
  revokeProcess(params).then(() => {
    ElMessage.success({ message: `操作成功！` });
    proTable.value?.getTableList();
  });
}
</script>
