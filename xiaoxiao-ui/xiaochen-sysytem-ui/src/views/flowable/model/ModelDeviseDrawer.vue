<template>
  <el-drawer v-model="deviseDrawer" direction="ttb" size="100%">
    <template #header="">
      <div style="font-size: 25px; font-weight: bold">流程设计</div>
    </template>
    <ProcessDesigner
      :key="processDesignerkey"
      :process-id="deviseModelKey"
      :process-name="deviseModelName"
      :xml-string="deviseModelBpmnXml"
      @save="onSaveDesigner"
    />
  </el-drawer>
</template>
<script setup lang="ts" name="ModelDeviseDrawer">
import ProcessDesigner from "@/components/ProcessDesigner/index.vue";
import { Model } from "@/api/flowable/model/types";
import { ref } from "vue";
import { getModeBpmnXml, saveModel } from "@/api/flowable/model";
import { ElMessage } from "element-plus";
const deviseDrawer = ref(false);
const deviseModelId = ref("");
const deviseModelKey = ref("");
const deviseModelName = ref("");
const deviseModelBpmnXml = ref("");
const processDesignerkey = ref();

const drawerProps = ref<Model.DrawerProps>({
  title: "",
  row: {},
  modelId: ""
});
const acceptParams = async (params: Model.DrawerProps) => {
  drawerProps.value = params;
  const res = await getModeBpmnXml(params.modelId);
  if (res.code === 200) {
    deviseDrawer.value = true;
    deviseModelId.value = params.row.modelId;
    deviseModelKey.value = params.row.modelKey;
    deviseModelName.value = params.row.modelName;
    deviseModelBpmnXml.value = res.data;
    processDesignerkey.value = new Date().getTime();
  }
};

const onSaveDesigner = async (bpmnXml: string, isNewVersion: boolean) => {
  const form = {
    modelId: deviseModelId.value,
    bpmnXml: bpmnXml,
    newVersion: isNewVersion
  };

  const res = await saveModel(form);
  if (res.code === 200) {
    ElMessage.success({ message: `保存成功！` });
    deviseDrawer.value = false;
    drawerProps.value.getTableList!();
  }
};

defineExpose({
  acceptParams
});
</script>
