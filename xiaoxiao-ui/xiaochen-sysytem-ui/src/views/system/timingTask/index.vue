<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!--      &lt;!&ndash; 菜单操作 &ndash;&gt;
      <template #operation="scope"> </template>-->
    </ProTable>
  </div>
</template>

<script setup lang="tsx" name="timingTask">
import { reactive, ref } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { listJobInfoPage } from "@/api/system/task";
import { ResPage } from "@/api/interface";
import { Task } from "@/api/system/task/types";

// ProTable 实例
const proTable = ref<ProTableInstance>();

const initParam = reactive({ id: "", traceId: "" });

const dataCallback = (data: ResPage<Task.JobInfoVo>) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

const getTableList = (params: any) => {
  return listJobInfoPage(params);
};

// 表格配置项
const columns = reactive<ColumnProps<Task.JobInfoVo>[]>([
  {
    prop: "jobGroup",
    label: "微服务名"
  },
  {
    prop: "author",
    label: "作者"
  },
  {
    prop: "scheduleType",
    label: "调度类型"
  },
  {
    prop: "scheduleConf",
    label: "调度配置"
  },
  {
    prop: "misfireStrategy",
    label: "调度过期策略"
  },
  {
    prop: "executorRouteStrategy",
    label: "执行器路由策略"
  },
  {
    prop: "executorHandler",
    label: "执行器任务handler"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 330 }
]);
</script>
