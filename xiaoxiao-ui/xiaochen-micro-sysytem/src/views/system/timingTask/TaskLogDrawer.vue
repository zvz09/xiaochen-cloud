<template>
  <el-drawer v-model="drawerVisible" :destroy-on-close="true" size="80%" :title="`${drawerProps.title}调度日志`">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :data-callback="dataCallback">
      <template #logTraceId="scope">
        <el-button v-copy="scope.row.logTraceId" link> {{ scope.row.logTraceId }} </el-button>
      </template>
    </ProTable>
  </el-drawer>
</template>
<script setup lang="ts" name="TaskLogDrawer">
import { reactive, ref } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { Task } from "@/api/system/task/types";
import { ColumnProps } from "@/components/ProTable/interface";
import { listJobLogPage } from "@/api/system/task";

interface DrawerProps {
  title: string;
  taskId: string;
}
const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  title: "",
  taskId: ""
});

// 表格配置项
const columns = reactive<ColumnProps<Task.TaskLogVo>[]>([
  {
    prop: "executorAddress",
    label: "执行器地址"
  },
  {
    prop: "executorHandler",
    label: "任务handler"
  },
  {
    prop: "executorParam",
    label: "任务参数"
  },
  {
    prop: "triggerTime",
    label: "调度时间"
  },
  {
    prop: "triggerCode",
    label: "调度结果"
  },
  {
    prop: "triggerMsg",
    label: "调度日志"
  },
  {
    prop: "logTraceId",
    label: "日志流水号",
    width: "300"
  }
]);

const getTableList = (params: any) => {
  return listJobLogPage(drawerProps.value.taskId, params);
};

const dataCallback = (data: any) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

// 接收父组件传过来的参数
const acceptParams = async (params: DrawerProps) => {
  drawerProps.value = params;

  drawerVisible.value = true;
};

defineExpose({
  acceptParams
});
</script>
