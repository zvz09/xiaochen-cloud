<template>
  <el-drawer v-model="drawer" size="100%" :title="`${drawerProps.title}表单`" destroy-on-close @opened="drawerOpen()">
    <ng-form-design ref="formDesign" v-loading="loading" :exp="false" :imp="false">
      <template #formName>
        <el-form-item label="表单名称">
          <el-input v-model="drawerProps.row.formName" />
        </el-form-item>
      </template>
      <template #controlButton>
        <el-button type="primary" @click="enterDrawer">保存</el-button>
      </template>
    </ng-form-design>
  </el-drawer>
</template>
<script setup lang="tsx" name="FormDrawer">
import { ref } from "vue";
import { Form } from "@/api/flowable/form/types";
import { ElMessage } from "element-plus";
import html2canvas from "html2canvas";

const loading = ref(false);
const formDesign = ref(null);

const drawerProps = ref<Form.DrawerProps>({
  title: "",
  row: {},
  id: ""
});

const drawer = ref(false);

// 接收父组件传过来的参数
const acceptParams = (params: Form.DrawerProps) => {
  drawer.value = true;
  loading.value = false;
  drawerProps.value = params;
};
const drawerOpen = () => {
  console.log(formDesign);
  if (drawerProps.value.row.formContent) {
    formDesign.value.initModel(JSON.parse(drawerProps.value.row.formContent));
  }
};

const enterDrawer = async () => {
  loading.value = true;
  html2canvas(document.querySelector(".form-panel"), {
    scale: 8
  }).then(async canvas => {
    drawerProps.value.row.thumbnail = canvas.toDataURL("image/jpeg", 0.1);
    drawerProps.value.row.formContent = JSON.stringify(formDesign.value.getModel());

    await drawerProps.value.api!(drawerProps.value.row);
    ElMessage.success({ message: `${drawerProps.value.title}表单成功！` });
    drawerProps.value.getTableList!();
    loading.value = false;
    drawer.value = false;
  });
};

defineExpose({
  acceptParams
});
</script>
