<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #traceId="scope">
        <el-button type="primary" link @click="queryByTraceId(scope.row.traceId)">
          {{ scope.row.traceId }}
        </el-button>
      </template>

      <!-- 菜单操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="View" @click="openDialog(scope.row)"> 查看</el-button>
      </template>
    </ProTable>
    <el-dialog v-model="dialogFormVisible" :title="dialogTitle">
      <el-form :model="formData" disabled label-width="140px">
        <el-form-item label="ID" prop="id">
          <el-input v-model="formData.id" />
        </el-form-item>
        <el-form-item label="时间" prop="dateTime">
          <el-input v-model="formData.dateTime" />
        </el-form-item>
        <el-form-item label="应用编码" prop="applicationName">
          <el-input v-model="formData.applicationName" />
        </el-form-item>
        <el-form-item label="主机" prop="host">
          <el-input v-model="formData.host" />
        </el-form-item>
        <el-form-item label="日志级别" prop="level">
          <el-input v-model="formData.level" />
        </el-form-item>
        <el-form-item label="流水号" prop="traceId">
          <el-input v-model="formData.traceId" />
        </el-form-item>
        <el-form-item label="类名" prop="className">
          <el-input v-model="formData.className" />
        </el-form-item>
        <el-form-item label="内容" prop="message">
          <el-input type="textarea" v-model="formData.message" />
        </el-form-item>
        <el-form-item label="异常栈" prop="error">
          <el-input type="textarea" v-model="formData.error" />
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup lang="tsx" name="systemLog">
import { reactive, ref } from "vue";
import { Log } from "@/api/system/log/types";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { logPage } from "@/api/system/log";
import { ESResPage } from "@/api/interface";
import { getDateTimeBeforeMinutes, getFormattedDateTime } from "@/utils/date";
import { getDictResultData } from "@/utils/dict";
import { View } from "@element-plus/icons-vue";

// ProTable 实例
const proTable = ref<ProTableInstance>();

const initParam = reactive({ id: "", traceId: "" });

const dataCallback = (data: ESResPage<Log.LogVO>) => {
  return {
    list: data.list,
    total: data.total,
    pageNum: data.pageNum,
    pageSize: data.pageSize
  };
};

const getTableList = (params: any) => {
  let newParams = JSON.parse(JSON.stringify(params));
  newParams.dateTime && (newParams.begin = newParams.dateTime[0]);
  newParams.dateTime && (newParams.end = newParams.dateTime[1]);
  delete newParams.dateTime;
  return logPage(newParams);
};

// 表格配置项
const columns = reactive<ColumnProps<Log.LogVO>[]>([
  {
    prop: "dateTime",
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
    prop: "applicationName",
    label: "系统编码",
    search: { el: "input" }
  },
  {
    prop: "host",
    label: "主机名",
    search: { el: "input" }
  },
  {
    prop: "level",
    label: "日志级别",
    tag: true,
    enum: () => getDictResultData("LogLevel"),
    fieldNames: { label: "label", value: "value" },
    search: { el: "select", props: { filterable: true } }
  },
  {
    prop: "traceId",
    label: "流水号",
    width: 200,
    search: { el: "input" }
  },
  {
    prop: "className",
    label: "类名"
  },
  {
    prop: "message",
    label: "内容",
    search: { el: "input", tooltip: "内容" }
  },
  {
    prop: "error",
    label: "异常栈"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 330 }
]);

const queryByTraceId = (traceId: string) => {
  initParam.traceId = traceId;
};

const dialogFormVisible = ref(false);
const dialogTitle = ref("查看日志详情");
const formData = reactive<Log.LogVO>({
  applicationName: "",
  className: "",
  dateTime: "",
  error: undefined,
  host: "",
  id: "",
  level: "",
  message: "",
  timestamp: "",
  traceId: undefined
});

const openDialog = async (logVO: Log.LogVO) => {
  Object.assign(formData, logVO);
  dialogFormVisible.value = true;
};
</script>
