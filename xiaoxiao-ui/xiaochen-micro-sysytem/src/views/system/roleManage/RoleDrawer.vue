<template>
  <el-drawer v-model="drawerVisible" :destroy-on-close="true" size="450px" :title="`${drawerProps.title}`">
    <el-form
      v-show="drawerProps.title === '查看' || drawerProps.title === '编辑'"
      ref="ruleFormRef"
      label-width="100px"
      label-suffix=" :"
      :rules="rules"
      :disabled="isView === 'true'"
      :model="drawerProps.row"
      :hide-required-asterisk="isView === 'true'"
    >
      <el-form-item label="角色编码" prop="roleCode">
        <el-input v-model="drawerProps.row!.roleCode" placeholder="请填写角色编码" clearable></el-input>
      </el-form-item>
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="drawerProps.row!.roleName" placeholder="请填写角色名称" clearable></el-input>
      </el-form-item>
    </el-form>
    <div v-show="drawerProps.title === '查看' || drawerProps.title === '绑定权限字'">
      <el-divider v-show="drawerProps.title !== '绑定权限字'" content-position="center">权限字信息</el-divider>
      <el-tree
        ref="permCodeTreeRef"
        empty-text="暂未绑定权限字"
        :data="permCodeData"
        :default-checked-keys="permCodeTreeIds"
        :props="treeDefaultProps"
        default-expand-all
        highlight-current
        node-key="id"
        show-checkbox
      />
    </div>
    <template #footer>
      <el-button @click="drawerVisible = false">取消</el-button>
      <el-button v-show="drawerProps.title === '编辑'" type="primary" @click="handleSubmit">确定</el-button>
      <el-button v-show="drawerProps.title === '绑定权限字'" type="primary" @click="bindHandleSubmit">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts" name="RoleDrawer">
import { reactive, ref } from "vue";
import { ElMessage, ElTree, FormInstance } from "element-plus";
import { bindPerm, detailRole } from "@/api/system/role";
import { listTree } from "@/api/system/permCode";
import { PermCode } from "@/api/system/permCode/types";
import { TreeOptionProps } from "element-plus/es/components/tree/src/tree.type";

const rules = reactive({
  roleCode: [{ required: true, message: "请填写角色编码" }],
  roleName: [{ required: true, message: "请填写角色名称" }]
});

const isView = ref("false");

const drawerVisible = ref(false);
const drawerProps = ref<PermCode.DrawerProps>({
  title: "",
  row: {},
  id: ""
});

const permCodeData = ref<PermCode.PermCodeVO[]>();
const permCodeTreeIds = ref<string[]>();

// 接收父组件传过来的参数
const acceptParams = async (params: PermCode.DrawerProps) => {
  drawerProps.value = params;
  if (drawerProps.value.title == "绑定权限字" && drawerProps.value.row.id) {
    let data = (await detailRole(drawerProps.value.row.id)).data;
    permCodeData.value = (await listTree({})).data.records;
    permCodeTreeIds.value = data.permCodeIds;
    drawerProps.value.id = drawerProps.value.row.id;
    isView.value = "false";
  } else if (drawerProps.value.title == "查看" && drawerProps.value.row.id) {
    let data = (await detailRole(drawerProps.value.row.id)).data;
    permCodeData.value = data.permCodeVos;
    permCodeTreeIds.value = data.permCodeIds;
    isView.value = "true";
  } else if (drawerProps.value.title == "编辑") {
    isView.value = "false";
  }
  drawerVisible.value = true;
};

// 提交数据（新增/编辑）
const ruleFormRef = ref<FormInstance>();
const handleSubmit = () => {
  ruleFormRef.value!.validate(async valid => {
    if (!valid) return;
    try {
      await drawerProps.value.api!(drawerProps.value.row);
      ElMessage.success({ message: `${drawerProps.value.title}用户成功！` });
      drawerProps.value.getTableList!();
      drawerVisible.value = false;
    } catch (error) {
      console.log(error);
    }
  });
};

const permCodeTreeRef = ref<InstanceType<typeof ElTree>>();
const bindHandleSubmit = async () => {
  const checkArr = permCodeTreeRef.value?.getCheckedKeys().concat(permCodeTreeRef.value?.getHalfCheckedKeys());
  let ids: any = [];
  checkArr &&
    checkArr.forEach(item => {
      ids.push(item);
    });
  await bindPerm(drawerProps.value.id as string, ids);
  ElMessage.success({ message: `设置成功` });
  drawerVisible.value = false;
};

const treeDefaultProps = ref<TreeOptionProps>({
  children: "children",
  label: "showName",
  disabled: () => {
    return isView.value;
  }
});

defineExpose({
  acceptParams
});
</script>
