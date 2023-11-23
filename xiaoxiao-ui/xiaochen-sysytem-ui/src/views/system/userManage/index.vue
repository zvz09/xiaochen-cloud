<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button v-auth="'add'" type="primary" :icon="CirclePlus" @click="openDrawer('新增')">新增用户</el-button>
      </template>
      <!-- 头像 -->
      <template #headerImg="scope">
        <el-avatar :size="30" :src="scope.row.headerImg" />
      </template>
      <template #role="scope">
        <p v-if="!scope.row.roleVos">--</p>
        <el-tag type="success" v-else v-for="roleVo in scope.row.roleVos" :key="roleVo">
          {{ roleVo.roleName }}
        </el-tag>
      </template>

      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="View" @click="openDrawer('查看', scope.row)">查看</el-button>
        <el-button v-auth="'edit'" type="primary" link :icon="EditPen" @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button v-auth="'resetPassword'" type="primary" link :icon="EditPen" @click="resetUserPassword(scope.row)">
          重置密码
        </el-button>
        <el-button v-auth="'delete'" type="primary" link :icon="Delete" @click="deleteUserInfo(scope.row)">删除</el-button>
      </template>
    </ProTable>
    <UserDrawer ref="drawerRef" />
  </div>
</template>

<script setup lang="ts" name="userManage">
import { CirclePlus, Delete, EditPen, View } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { reactive, ref } from "vue";
import { User } from "@/api/system/user/types";
import {
  createUser,
  updateUserInfo,
  getUserList,
  deleteUser,
  resetPassword,
  getEnableType,
  getGenderType
} from "@/api/system/user";
import { useHandleData } from "@/hooks/useHandleData";
import UserDrawer from "@/views/system/userManage/UserDrawer.vue";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 表格配置项
const columns = reactive<ColumnProps<User.UserVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "username",
    label: "登录名",
    search: { el: "input", tooltip: "登录名" }
  },
  {
    prop: "nickName",
    label: "昵称",
    search: { el: "input", tooltip: "昵称" }
  },
  {
    prop: "gender",
    label: "性别",
    enum: getGenderType
  },
  {
    prop: "headerImg",
    label: "头像"
  },
  {
    prop: "role",
    label: "角色"
  },
  {
    prop: "phone",
    label: "手机号",
    search: { el: "input", tooltip: "手机号" }
  },
  {
    prop: "email",
    label: "邮箱"
  },
  {
    prop: "enable",
    label: "状态",
    tag: true,
    enum: getEnableType,
    search: { el: "tree-select" }
  },
  { prop: "operation", label: "操作", fixed: "right", width: 480 }
]);

const getTableList = (params: any) => {
  return getUserList(params);
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

// 打开 drawer(新增、查看、编辑)
const drawerRef = ref<InstanceType<typeof UserDrawer> | null>(null);
const openDrawer = (title: string, row: Partial<User.UserVO> = {}) => {
  const params = {
    title,
    row: { ...row },
    api: title === "新增" ? createUser : title === "编辑" ? updateUserInfo : undefined,
    getTableList: proTable.value?.getTableList,
    id: row.id
  };
  drawerRef.value?.acceptParams(params);
};
const resetUserPassword = async (params: User.UserVO) => {
  await useHandleData(resetPassword, params.id, `重置【${params.nickName}】用户密码`);
  proTable.value?.getTableList();
};
// 删除用户信息
const deleteUserInfo = async (params: User.UserVO) => {
  await useHandleData(deleteUser, params.id, `删除【${params.nickName}】用户`);
  proTable.value?.getTableList();
};
</script>
