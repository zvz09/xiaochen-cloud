<template>
  <el-drawer v-model="drawerVisible" :destroy-on-close="true" size="450px" :title="`${drawerProps.title}`">
    <el-form
      ref="ruleFormRef"
      label-width="100px"
      label-suffix=" :"
      :rules="rules"
      :disabled="isView"
      :model="drawerProps.row"
      :hide-required-asterisk="isView"
    >
      <el-form-item label="用户头像" prop="headerImg">
        <UploadImg v-model:image-url="drawerProps.row!.headerImg" width="135px" height="135px" :file-size="3">
          <template #empty>
            <span>请上传头像</span>
          </template>
          <template #tip> 头像大小不能超过 3M</template>
        </UploadImg>
      </el-form-item>
      <el-form-item label="登录名" prop="username">
        <el-input v-model="updateUserInfoParams!.username" placeholder="请填写用户姓名" clearable></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="updateUserInfoParams!.nickName" placeholder="请填写昵称" clearable></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="updateUserInfoParams!.gender" placeholder="请选择性别" clearable>
          <el-option v-for="item in getGenderType" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="updateUserInfoParams!.phone" placeholder="请填写手机号" clearable></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="updateUserInfoParams!.email" placeholder="请填写邮箱" clearable></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="email">
        <el-select
          v-model="updateUserInfoParams!.roleIds"
          multiple
          collapse-tags
          collapse-tags-tooltip
          :max-collapse-tags="2"
          placeholder="请选择角色"
          style="width: 240px"
        >
          <el-option v-for="item in ruleOptions" :key="item.id" :label="item.roleName" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <div v-show="drawerProps.title === '查看'">
      <el-divider content-position="center">角色信息</el-divider>
      <template v-for="roleVo in drawerProps.row.roleVos" :key="roleVo">
        <el-tag style="margin-left: 5px" type="success"> {{ roleVo.roleName }}</el-tag>
      </template>
    </div>
    <template #footer>
      <el-button @click="drawerVisible = false">取消</el-button>
      <el-button v-show="!isView" type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts" name="UserDrawer">
import { reactive, ref } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { getGenderType } from "@/api/system/user";
import { User } from "@/api/system/user/types";
import UploadImg from "@/components/Upload/Img.vue";
import { Role } from "@/api/system/role/types";
import { getRoleList } from "@/api/system/role";

const rules = reactive({
  headerImg: [{ required: true, message: "请上传用户头像" }],
  username: [{ required: true, message: "请填写登录名" }],
  nickName: [{ required: true, message: "请填写昵称" }],
  gender: [{ required: true, message: "请选择性别" }],
  phone: [{ required: true, message: "请填写手机号" }],
  email: [{ required: true, message: "请填写邮箱" }]
});

interface DrawerProps {
  title: string;
  row: Partial<User.UserVO>;
  api?: (params: any) => Promise<any>;
  getTableList?: () => void;
}

const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  title: "",
  row: {}
});

const isView = ref(false);
const ruleOptions = ref<Role.RoleVO[]>();

const updateUserInfoParams = ref<User.UpdateUserInfoParams>();

// 接收父组件传过来的参数
const acceptParams = (params: DrawerProps) => {
  drawerProps.value = params;
  updateUserInfoParams.value = { ...drawerProps.value.row };
  if (updateUserInfoParams.value) {
    updateUserInfoParams.value.roleIds = [];
  }
  drawerProps.value.row.roleVos &&
    drawerProps.value.row.roleVos.forEach((role: Role.RoleVO) => {
      updateUserInfoParams.value?.roleIds?.push(role.id);
    });
  if (drawerProps.value.title == "查看" && drawerProps.value.row.id) {
    isView.value = true;
  } else if (drawerProps.value.title == "编辑") {
    getRoleList({ pageNum: 1, pageSize: 9999 }).then(res => {
      ruleOptions.value = res.data.records;
    });
    isView.value = false;
  }
  drawerVisible.value = true;
};

// 提交数据（新增/编辑）
const ruleFormRef = ref<FormInstance>();
const handleSubmit = () => {
  ruleFormRef.value!.validate(async valid => {
    if (!valid) return;
    try {
      await drawerProps.value.api!(updateUserInfoParams.value);
      ElMessage.success({ message: `${drawerProps.value.title}成功！` });
      drawerProps.value.getTableList!();
      drawerVisible.value = false;
    } catch (error) {
      console.log(error);
    }
  });
};

defineExpose({
  acceptParams
});
</script>
