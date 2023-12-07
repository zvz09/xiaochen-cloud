<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="View" @click="openDrawer('查看', scope.row)">查看</el-button>
        <el-button
          v-auth="'add'"
          v-show="scope.row.permCodeType === '0'"
          type="primary"
          link
          :icon="Plus"
          @click="openDrawer('新增', scope.row)"
        >
          新增
        </el-button>
        <el-button v-auth="'bind'" type="primary" link :icon="EditPen" @click="openApiDrawer('编辑', scope.row)">
          绑定权限资源
        </el-button>
        <el-button
          v-auth="'edit'"
          v-show="scope.row.permCodeType !== '0'"
          type="primary"
          link
          :icon="EditPen"
          @click="openDrawer('编辑', scope.row)"
        >
          编辑
        </el-button>
        <el-button
          v-auth="'delete'"
          v-show="scope.row.permCodeType !== '0'"
          type="primary"
          link
          :icon="Delete"
          @click="deletePermCodeInfo(scope.row)"
        >
          删除
        </el-button>
      </template>
    </ProTable>
    <PermCodeDrawer ref="drawerRef" />
    <BindApiDrawer ref="bindApiDrawerRef" />
  </div>
</template>

<script setup lang="tsx" name="permCodeManage">
import { reactive, ref } from "vue";
import { PermCode } from "@/api/system/permCode/types";
import { useHandleData } from "@/hooks/useHandleData";
import ProTable from "@/components/ProTable/index.vue";
import PermCodeDrawer from "@/views/system/permCodeManage/PermCodeDrawer.vue";
import BindApiDrawer from "@/views/system/permCodeManage/BindApiDrawer.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Delete, EditPen, Plus, View } from "@element-plus/icons-vue";
import { createPermCode, deletePermCode, detailPermCode, listTree, updatePermCode } from "@/api/system/permCode";
import { getDictResultData } from "@/utils/dict";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 如果表格需要初始化请求参数，直接定义传给 ProTable (之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({});

// dataCallback 是对于返回的表格数据做处理，如果你后台返回的数据不是 list && total && pageNum && pageSize 这些字段，可以在这里进行处理成这些字段
// 或者直接去 hooks/useTable.ts 文件中把字段改为你后端对应的就行
const dataCallback = (data: any) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

// 如果你想在请求之前对当前请求参数做一些操作，可以自定义如下函数：params 为当前所有的请求参数（包括分页），最后返回请求列表接口
// 默认不做操作就直接在 ProTable 组件上绑定	:requestApi="getUserList"
const getTableList = (params: any) => {
  return listTree(params);
};

// 表格配置项
const columns = reactive<ColumnProps<PermCode.PermCodeVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "showName",
    label: "权限名称",
    search: { el: "input", tooltip: "权限名称" }
  },
  {
    prop: "permCode",
    label: "权限编码",
    search: { el: "input", tooltip: "权限编码" }
  },
  {
    prop: "permCodeType",
    label: "权限类型",
    tag: true,
    enum: () => getDictResultData("PermCodeType"),
    fieldNames: { label: "label", value: "value" }
  },
  {
    prop: "showOrder",
    label: "排序"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 480 }
]);

// 删除权限字信息
const deletePermCodeInfo = async (params: PermCode.PermCodeVO) => {
  await useHandleData(deletePermCode, params.id, `删除【${params.showName}】权限字`);
  proTable.value?.getTableList();
};

// 打开 drawer(新增、查看、编辑)
const drawerRef = ref<InstanceType<typeof PermCodeDrawer> | null>(null);
const bindApiDrawerRef = ref<InstanceType<typeof BindApiDrawer> | null>(null);
const openDrawer = async (title: string, row: Partial<PermCode.PermCodeVO> = {}) => {
  const params = {
    title,
    isView: title === "查看",
    row: { ...row },
    api: title === "新增" ? createPermCode : title === "编辑" ? updatePermCode : undefined,
    getTableList: proTable.value?.getTableList
  };
  drawerRef.value?.acceptParams(params);
};
const openApiDrawer = async (title: string, row: Partial<PermCode.PermCodeVO> = {}) => {
  let data = (await detailPermCode(row.id as string)).data;
  const params = {
    title,
    isView: title === "查看" ? "true" : "false",
    apiIds: data.apiIds,
    id: row.id
  };
  bindApiDrawerRef.value?.acceptParams(params);
};
</script>
