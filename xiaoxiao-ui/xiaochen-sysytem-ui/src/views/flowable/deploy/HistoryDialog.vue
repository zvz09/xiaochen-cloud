<template>
  <el-dialog v-model="historyDialog" title="版本管理" width="70%">
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
        <el-button
          v-if="!scope.row.suspended"
          :icon="VideoPause"
          size="small"
          type="warning"
          @click="handleChangeState(scope.row, 'suspended')"
        >
          挂起
        </el-button>
        <el-button
          v-if="scope.row.suspended"
          :icon="VideoPlay"
          size="small"
          type="primary"
          @click="handleChangeState(scope.row, 'active')"
        >
          激活
        </el-button>
        <el-button :icon="DeleteFilled" size="small" type="danger" @click="deleteFunc(scope.row)"> 删除 </el-button>
      </template>
    </ProTable>
  </el-dialog>
</template>
<script setup lang="tsx" name="HistoryDialog">
import ProTable from "@/components/ProTable/index.vue";
import { FlowableDeploy } from "@/api/flowable/deploy/types";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { DeleteFilled, VideoPause, VideoPlay } from "@element-plus/icons-vue";
import { deployPublishList, changeState, removeDeploy } from "@/api/flowable/deploy";
import { ElMessage, ElMessageBox } from "element-plus";

const historyDialog = ref(false);

const drawerProps = ref<FlowableDeploy.DrawerProps>({
  row: {}
});

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
    prop: "version",
    label: "流程版本"
  },
  {
    prop: "suspended",
    label: "状态"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  if (!drawerProps.value.row.processKey) {
    return;
  }
  return deployPublishList(drawerProps.value.row.processKey, params);
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

const handleChangeState = async (row: FlowableDeploy.VO, state: string) => {
  const res = await changeState(row.definitionId, state);
  if (res.success) {
    ElMessage.success({ message: `操作成功！` });
    proTable.value?.getTableList();
  } else {
    ElMessage.error({ message: `操作失败！` });
  }
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

// 接收父组件传过来的参数
const acceptParams = (params: FlowableDeploy.DrawerProps) => {
  drawerProps.value = params;
  historyDialog.value = true;
};

defineExpose({
  acceptParams
});
</script>
