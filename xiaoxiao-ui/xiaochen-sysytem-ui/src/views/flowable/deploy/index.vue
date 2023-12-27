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

      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button icon="view" link type="primary" @click="openHistoryDialog(scope.row)"> 版本管理 </el-button>
        <el-button icon="delete" link type="primary" @click="deleteFunc(scope.row)"> 删除 </el-button>
      </template>
    </ProTable>

    <HistoryDialog ref="historyDialogRef" />
  </div>
</template>
<script setup lang="ts">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { FlowableDeploy } from "@/api/flowable/deploy/types";
import { pageDeploy, removeDeploy } from "@/api/flowable/deploy";
import { Form } from "@/api/flowable/form/types";
import HistoryDialog from "@/views/flowable/deploy/HistoryDialog.vue";
import { ElMessage, ElMessageBox } from "element-plus";
// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<FlowableDeploy.VO>[]>([
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
  return pageDeploy(params);
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

const deleteFunc = async (row: FlowableDeploy.VO) => {
  ElMessageBox.confirm("确定要删除吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    const res = await removeDeploy([row.deploymentId]);
    if (res.success) {
      ElMessage({
        type: "success",
        message: "删除成功!"
      });
      proTable.value?.getTableList();
    }
  });
};

const historyDialogRef = ref<InstanceType<typeof HistoryDialog> | null>(null);
const openHistoryDialog = (row: Partial<Form.VO> = {}) => {
  console.log(row);
  const params = reactive<FlowableDeploy.DrawerProps>({
    row: { ...row }
  });
  historyDialogRef.value?.acceptParams(params);
};
</script>
