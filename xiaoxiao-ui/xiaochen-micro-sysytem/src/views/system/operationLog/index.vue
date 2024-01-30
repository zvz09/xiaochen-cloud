<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
    </ProTable>
  </div>
</template>

<script setup lang="tsx" name="operationLog">
import { reactive, ref } from "vue";
import { Log } from "@/api/system/log/types";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { operationLogPage } from "@/api/system/log";
import { ResPage } from "@/api/interface";
import { getDateTimeBeforeMinutes, getFormattedDateTime } from "@/utils/date";

// ProTable 实例
const proTable = ref<ProTableInstance>();

const initParam = reactive({ id: "", traceId: "" });

const dataCallback = (data: ResPage<Log.LogVO>) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

const getTableList = (params: any) => {
  let newParams = JSON.parse(JSON.stringify(params));
  newParams.operationTimeStart && (newParams.begin = newParams.operationTimeStart[0]);
  newParams.operationTimeStart && (newParams.end = newParams.operationTimeStart[1]);
  delete newParams.operationTimeStart;
  return operationLogPage(newParams);
};

// 表格配置项
const columns = reactive<ColumnProps<Log.OperationLogVo>[]>([
  {
    prop: "operationTimeStart",
    label: "时间",
    width: 220,
    search: {
      el: "date-picker",
      span: 2,
      props: { type: "datetimerange", valueFormat: "YYYY-MM-DD HH:mm:ss" },
      defaultValue: [getFormattedDateTime(getDateTimeBeforeMinutes(15)), getFormattedDateTime(new Date())]
    }
  },
  {
    prop: "requestIp",
    label: "源地址"
  },
  {
    prop: "serviceName",
    label: "微服务名"
  },
  {
    prop: "businessType",
    label: "业务类型"
  },
  {
    prop: "description",
    label: "操作"
  },
  {
    prop: "operatorName",
    label: "操作人"
  },
  {
    prop: "bizNo",
    label: "业务标识"
  },
  {
    prop: "success",
    label: "状态"
  },
  {
    prop: "elapse",
    label: "耗时"
  },
  {
    prop: "traceId",
    label: "日志流水号"
  }
]);
</script>
