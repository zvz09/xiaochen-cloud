<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <template #operation="scope">
        <el-button icon="Tickets" link size="small" type="primary" @click="toDetailProcess(scope.row)">详情</el-button>
      </template>
    </ProTable>
  </div>
</template>
<script setup lang="tsx" name="sendDuplicate">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { copyProcessList } from "@/api/flowable/process";
import { Process } from "@/api/flowable/process/types";
import { useRouter } from "vue-router";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Process.FlowableCopyVo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "copyId",
    label: "抄送编号"
  },
  {
    prop: "title",
    label: "标题"
  },
  {
    prop: "processName",
    label: "流程名称",
    width: 300,
    search: { el: "input", tooltip: "流程名称" }
  },
  {
    prop: "originatorName",
    label: "发起人"
  },
  {
    prop: "createTime",
    label: "创建时间"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return copyProcessList(params);
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

const router = useRouter();

function toDetailProcess(row: Process.FlowableCopyVo) {
  router.push({
    name: "detailProcess",
    params: {
      procInsId: row.instanceId,
      title: "查看-" + row.instanceId
    },
    query: {
      processed: "false",
      taskId: row.taskId
    }
  });
}
</script>
