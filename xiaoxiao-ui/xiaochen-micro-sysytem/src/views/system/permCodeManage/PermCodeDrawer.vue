<template>
  <el-drawer v-model="drawerVisible" :destroy-on-close="true" size="450px" :title="`${drawerProps.title}权限字`">
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
          <el-option v-for="item in permCodeTypes" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="showOrder">
        <el-input v-model="drawerProps.row!.showOrder" placeholder="请填写排序" clearable></el-input>
      </el-form-item>
    </el-form>
    <div v-if="drawerProps.isView">
      <el-divider content-position="center">权限资源信息</el-divider>
      <el-tree
        ref="apiTreeRef"
        empty-text="暂未绑定权限资源"
        :data="apiData"
        :default-checked-keys="apiTreeIds"
        :props="apiDefaultProps"
        default-expand-all
        highlight-current
        node-key="id"
        show-checkbox
      />
    </div>
    <template #footer>
      <el-button @click="drawerVisible = false">取消</el-button>
      <el-button v-show="!drawerProps.isView" type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts" name="PermCodeDrawer">
import { reactive, ref } from "vue";
import { ElMessage, ElTree, FormInstance } from "element-plus";
import { PermCode } from "@/api/system/permCode/types";
import { detailPermCode } from "@/api/system/permCode";
import { API } from "@/api/system/api/types";
import { getDict } from "@/utils/dict";
import { TreeOptionProps } from "element-plus/es/components/tree/src/tree.type";
import { Dictionary } from "@/api/system/dictionary/types";

let permCodeTypes: Dictionary.DictionaryDetailVO[] | null = [];
getDict("PermCodeType").then(res => {
  permCodeTypes = res;
});

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
const apiData = ref<API.ApiVO[]>();
const apiTreeIds = ref<string[]>([]);
// 接收父组件传过来的参数
const acceptParams = async (params: DrawerProps) => {
  drawerProps.value = params;
  if (drawerProps.value.title == "查看" && drawerProps.value.row.id) {
    let data = (await detailPermCode(drawerProps.value.row.id)).data;
    apiData.value = data.apiVos;
    if (data.apiIds) {
      apiTreeIds.value = data.apiIds;
    }
  }
  if (drawerProps.value.title == "新增") {
    let parentId = params.row.id;
    drawerProps.value.row = {};
    drawerProps.value.row.parentId = parentId;
  }
  drawerVisible.value = true;
};

// 提交数据（新增/编辑）
const ruleFormRef = ref<FormInstance>();
const handleSubmit = () => {
  ruleFormRef.value!.validate(async valid => {
    console.log(drawerProps.value.row);
    if (!valid) return;
    try {
      await drawerProps.value.api!(drawerProps.value.row);
      ElMessage.success({ message: `${drawerProps.value.title}成功！` });
      drawerProps.value.getTableList!();
      drawerVisible.value = false;
    } catch (error) {
      console.log(error);
    }
  });
};

const apiTreeRef = ref<InstanceType<typeof ElTree>>();
const apiDefaultProps = ref<TreeOptionProps>({
  children: "children",
  label: "description",
  disabled: () => {
    return "true";
  }
});
defineExpose({
  acceptParams
});
</script>
