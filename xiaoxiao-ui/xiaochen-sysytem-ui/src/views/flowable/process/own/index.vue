<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #procDefVersion="scope">
        <el-tag>v{{ scope.row.procDefVersion }}</el-tag>
      </template>
      <template #operation="scope">
        <el-button icon="Tickets" link size="small" type="primary" @click="toDetailProcess(scope.row)">详情 </el-button>
        <el-button v-if="scope.row.finishTime" icon="Delete" link size="small" type="danger" @click="deleteFunc(scope.row)">
          删除
        </el-button>
        <el-button icon="Close" link size="small" type="warning" @click="stopFunc(scope.row)">取消 </el-button>
      </template>
    </ProTable>
  </div>
</template>
<script setup lang="tsx" name="ownProcess">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { ownProcessTaskList, deleteProcess } from "@/api/flowable/process";
import { Process } from "@/api/flowable/process/types";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { stopProcess } from "@/api/flowable/task";

const router = useRouter();
// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Process.FlowableTaskVo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "procInsId",
    label: "流程编号"
  },
  {
    prop: "procDefName",
    label: "流程名称",
    search: { el: "input", tooltip: "流程名称" }
  },
  {
    prop: "category",
    label: "流程类别"
  },
  {
    prop: "procDefVersion",
    label: "流程版本"
  },
  {
    prop: "taskName",
    label: "当前节点"
  },
  {
    prop: "createTime",
    label: "提交时间"
  },
  {
    prop: "processStatus",
    label: "流程状态"
  },
  {
    prop: "duration",
    label: "耗时"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return ownProcessTaskList(params);
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
      title: "查看-" + row.procInsId
    },
    query: {
      processed: "false",
      taskId: ""
    }
  });
}

function deleteFunc(row: Process.FlowableTaskVo) {
  ElMessageBox.confirm("确定要删除吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    const res = await deleteProcess([row.procInsId]);
    if (res.code === 200) {
      ElMessage({
        type: "success",
        message: "删除成功!"
      });
      proTable.value?.getTableList();
    }
  });
}
function stopFunc(row: Process.FlowableTaskVo) {
  ElMessageBox.confirm("确定要取消吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    const res = await stopProcess({ procInsId: row.procInsId });
    if (res.code === 200) {
      ElMessage({
        type: "success",
        message: "取消成功!"
      });
      proTable.value?.getTableList();
    }
  });
}
</script>
