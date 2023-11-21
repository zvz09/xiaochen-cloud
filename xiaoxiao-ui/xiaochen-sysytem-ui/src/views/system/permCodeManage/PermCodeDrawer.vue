<template>
  <el-drawer v-model="drawerVisible" :destroy-on-close="true" size="450px" :title="`${drawerProps.title}角色`">
    <el-form
      ref="ruleFormRef"
      label-width="100px"
      label-suffix=" :"
      :rules="rules"
      :disabled="drawerProps.isView"
      :model="drawerProps.row"
      :hide-required-asterisk="drawerProps.isView"
    >
      <el-form-item label="权限字编码" prop="permCode">
        <el-input v-model="drawerProps.row!.permCode" placeholder="请填写编码" clearable></el-input>
      </el-form-item>
      <el-form-item label="显示名称" prop="showName">
        <el-input v-model="drawerProps.row!.showName" placeholder="请填写名称" clearable></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="permCodeType">
        <el-select v-model="drawerProps.row!.permCodeType" placeholder="请选择类型" clearable>
          <el-option v-for="item in getPermCodeType" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="showOrder">
        <el-input v-model="drawerProps.row!.showOrder" placeholder="请填写排序" clearable></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="drawerVisible = false">取消</el-button>
      <el-button v-show="!drawerProps.isView" type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts" name="PermCodeDrawer">
import { reactive, ref } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { PermCode } from "@/api/system/permCode/types";
import { getPermCodeType } from "@/api/system/permCode";

const rules = reactive({
  roleCode: [{ required: true, message: "请填写角色编码" }],
  roleName: [{ required: true, message: "请填写角色名称" }]
});

interface DrawerProps {
  title: string;
  isView: boolean;
  row: Partial<PermCode.PermCodeVO>;
  api?: (params: any) => Promise<any>;
  getTableList?: () => void;
}

const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  isView: false,
  title: "",
  row: {}
});

const apiData = ref([]);

// 接收父组件传过来的参数
const acceptParams = async (params: DrawerProps) => {
  apiData.value = await listAPITree().data;
  drawerProps.value = params;
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

defineExpose({
  acceptParams
});
</script>
