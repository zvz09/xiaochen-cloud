<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')">新增表单</el-button>
      </template>

      <template #thumbnail="scope">
        <el-popover
          :width="500"
          popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
        >
          <template #reference>
            <el-image fit="fill" :src="scope.row.thumbnail" alt="" style="width: 70px; height: 50px"></el-image>
          </template>
          <template #default>
            <el-image style="width: 700px; height: 500px" :src="scope.row.thumbnail" />
          </template>
        </el-popover>
      </template>

      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="EditPen" @click="openDrawer('编辑', scope.row)">编辑</el-button>
      </template>
    </ProTable>
    <FormDrawer ref="drawerRef" />
  </div>
</template>
<script setup lang="tsx" name="formManage">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Form } from "@/api/flowable/form/types";
import { pageForm, addForm, editForm } from "@/api/flowable/form";
import { CirclePlus, EditPen } from "@element-plus/icons-vue";
import FormDrawer from "@/views/flowable/form/FormDrawer.vue";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<Form.VO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "formName",
    label: "表单名称",
    width: 300,
    search: { el: "input", tooltip: "标题" }
  },
  {
    prop: "thumbnail",
    label: "缩略图"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return pageForm(params);
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

const drawerRef = ref<InstanceType<typeof FormDrawer> | null>(null);
const openDrawer = (title: string, row: Partial<Form.VO> = {}) => {
  console.log(row);
  const params = reactive<Form.DrawerProps>({
    title: title,
    row: { ...row },
    api: title === "新增" ? addForm : title === "编辑" ? editForm : undefined,
    getTableList: proTable.value?.getTableList,
    id: row.id
  });
  drawerRef.value?.acceptParams(params);
};
</script>
