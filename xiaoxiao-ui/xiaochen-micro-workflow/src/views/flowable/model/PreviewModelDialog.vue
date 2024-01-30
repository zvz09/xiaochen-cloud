<template>
  <el-dialog v-model="previewDialog" title="流程图预览">
    <ProcessViewer :key="previewBpmnXmlKey" :bpmn-xml="previewBpmnXml" />
  </el-dialog>
</template>
<script setup lang="ts">
import ProcessViewer from "@/components/ProcessViewer/index.vue";
import { ref } from "vue";
import { Model } from "@/api/flowable/model/types";
import { getModeBpmnXml } from "@/api/flowable/model";

const previewDialog = ref(false);
const previewBpmnXml = ref("");
const previewBpmnXmlKey = ref();

const drawerProps = ref<Model.DrawerProps>({
  title: "",
  row: {},
  modelId: ""
});

const acceptParams = async (params: Model.DrawerProps) => {
  drawerProps.value = params;
  const res = await getModeBpmnXml(params.modelId);
  if (res.code === 200) {
    previewBpmnXmlKey.value = new Date().getTime();
    previewBpmnXml.value = res.data;
    previewDialog.value = true;
  }
};

defineExpose({
  acceptParams
});
</script>
