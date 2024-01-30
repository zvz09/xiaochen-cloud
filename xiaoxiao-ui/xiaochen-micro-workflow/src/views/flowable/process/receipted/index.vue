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
        <el-button icon="Tickets" link size="small" type="primary" @click="receiptedFunc(scope.row)">签收 </el-button>
      </template>
    </ProTable>
  </div>
</template>
<script setup lang="tsx" name="receiptedProcess">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { receiptedProcessTaskList } from "@/api/flowable/process";
import { receiptedTask } from "@/api/flowable/task";
import { Process } from "@/api/flowable/process/types";
import { ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { closeThisPage } from "@/utils/closeThisPage";

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
    prop: "createTime",
    label: "提交时间"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return receiptedProcessTaskList(params);
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

function receiptedFunc(row: Process.FlowableTaskVo) {
  receiptedTask({ taskId: row.taskId }).then(response => {
    if (response.code == 200) {
      ElMessageBox.confirm("签收成功,是否跳转到代办任务?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "success"
      }).then(async () => {
        closeThisPage(route);
        await router.push({
          name: "todoProcess"
        });
      });
    }
    proTable.value?.getTableList();
  });
}
</script>
