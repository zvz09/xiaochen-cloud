<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button v-auth="'add'" type="primary" :icon="CirclePlus" @click="openDrawer('新增')">新增角色</el-button>
      </template>

      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="View" @click="openDrawer('查看', scope.row)">查看</el-button>
        <el-button v-auth="'edit'" type="primary" link :icon="EditPen" @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button v-auth="'bind'" type="primary" link :icon="EditPen" @click="openDrawer('绑定权限字', scope.row)">
          绑定权限字
        </el-button>
        <el-button v-auth="'delete'" type="primary" link :icon="Delete" @click="deleteAccount(scope.row)">删除</el-button>
      </template>
    </ProTable>
    <RoleDrawer ref="drawerRef" />
  </div>
</template>

<script setup lang="tsx" name="roleManage">
import { reactive, ref } from "vue";
import { Role } from "@/api/system/role/types";
import { useHandleData } from "@/hooks/useHandleData";
import ProTable from "@/components/ProTable/index.vue";
import RoleDrawer from "@/views/system/roleManage/RoleDrawer.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { CirclePlus, Delete, EditPen, View } from "@element-plus/icons-vue";
import { createRole, deleteRole, getRoleList, updateRole } from "@/api/system/role";

// ProTable 实例
const proTable = ref<ProTableInstance>();

const initParam = reactive({});

const dataCallback = (data: any) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

const getTableList = (params: any) => {
  return getRoleList(params);
};

// 表格配置项
const columns = reactive<ColumnProps<Role.RoleVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "roleCode",
    label: "角色编码",
    search: { el: "input", tooltip: "角色编码" }
  },
  {
    prop: "roleName",
    label: "角色名称",
    search: { el: "input", tooltip: "角色编码" }
  },
  { prop: "operation", label: "操作", fixed: "right", width: 330 }
]);

// 删除角色信息
const deleteAccount = async (params: Role.RoleVO) => {
  await useHandleData(deleteRole, params.id, `删除【${params.roleName}】角色`);
  proTable.value?.getTableList();
};

// 打开 drawer(新增、查看、编辑)
const drawerRef = ref<InstanceType<typeof RoleDrawer> | null>(null);
const openDrawer = (title: string, row: Partial<Role.RoleVO> = {}) => {
  const params = {
    title,
    row: { ...row },
    api: title === "新增" ? createRole : title === "编辑" ? updateRole : undefined,
    getTableList: proTable.value?.getTableList,
    id: row.id
  };
  drawerRef.value?.acceptParams(params);
};
</script>
