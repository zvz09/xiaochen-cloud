<template>
  <div class="table-box">
    <el-card class="box-card">
      <template #header>
        <span>发起流程</span>
      </template>
      <el-col :offset="3" :span="18">
        <div v-if="formVisible" class="form-conf">
          <ng-form-build ref="form" :form-template="formTemplate" :models="models" />
        </div>
      </el-col>
      <el-col :offset="10" :span="18">
        <el-button type="primary" @click="onSubmit">提交</el-button>
        <el-button icon="refresh" @click="onReset">重置</el-button>
      </el-col>
    </el-card>
  </div>
</template>
<script setup lang="tsx" name="startProcess">
import { useRoute } from "vue-router";
import { ref } from "vue";
import { getProcessForm, startProcess } from "@/api/flowable/process";
import { ElMessage } from "element-plus";
import { closeThisPage } from "@/utils/closeThisPage";

const route = useRoute();

const form = ref(null);
const formVisible = ref(false);

const formTemplate = ref();
const models = ref();

function initData() {
  getProcessForm({
    definitionId: route.params.definitionId,
    deployId: route.params.deployId
  }).then(res => {
    if (res.data) {
      formTemplate.value = JSON.parse(res.data.formTemplate);
      if (res.data.models) {
        models.value = JSON.parse(res.data.models);
      } else {
        models.value = {};
      }
      formVisible.value = true;
    }
  });
}

function onReset() {
  form.value.reset();
}

function onSubmit() {
  form.value.validate().then(function (status) {
    if (status) {
      startProcess({
        processDefId: route.params.definitionId,
        variables: form.value.getData()
      }).then(res => {
        if (res.code === 200) {
          ElMessage({
            type: "success",
            message: "提交成功!"
          });
          closeThisPage();
        }
      });
    }
  });
}

initData();
</script>
