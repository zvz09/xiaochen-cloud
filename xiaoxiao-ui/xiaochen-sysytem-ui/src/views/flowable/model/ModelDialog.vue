<template>
  <el-dialog v-model="dialogVisible" :before-close="closeDialog" :title="`${drawerProps.title}模型`">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="模型标识" prop="modelName">
        <el-input v-model="form.modelKey" autocomplete="off" disabled/>
      </el-form-item>
      <el-form-item label="模型名称" prop="modelName">
        <el-input v-model="form.modelName" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="流程分类" prop="category">
        <DictionaryDetailSelect :value="form.category" v-model:label="form.category" dictionaryType="process_category"
                                placeholder="请选择流程分类"/>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description"
                  :rows="2" autocomplete="off" type="textarea"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="enterDialog">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="tsx" name="ModelDialog">
import { ref } from "vue";
import { Model } from "@/api/flowable/model/types";
import DictionaryDetailSelect from "@/components/DictionaryDetailSelect/index.vue";
import {ElMessage} from "element-plus";

const formRef = ref(null)
const dialogVisible = ref(false)

const rules = ref({
  modelName: [{required: true, message: '请输入模型名称', trigger: 'blur'}],
  category: [{required: true, message: '请选择流程分类', trigger: 'blur'}],
})
const drawerProps = ref<Model.DrawerProps>({
  title: "",
  row: {},
  modelId: ""
});
const form = ref<Model.DTO>({
  modelName: '',
  modelKey: '',
  category: '',
  description: '',
})
// 接收父组件传过来的参数
const acceptParams = (params: Model.DrawerProps) => {
  drawerProps.value = params;
  form.value = {...params.row}
  dialogVisible.value = true;
};

const initForm = () => {
  formRef.value.resetFields()
  form.value = {
    positionCode: '',
    positionName: '',
    positionSort: '',
    status: '',
    remark: '',
  }
}

const closeDialog = () => {
  initForm()
  dialogVisible.value = false
}

function enterDialog() {
  formRef.value!.validate(async valid => {
    if (!valid) return;
    try {
      await drawerProps.value.api!(drawerProps.value.row);
      ElMessage.success({ message: `${drawerProps.value.title}模型成功！` });
      drawerProps.value.getTableList!();
      dialogVisible.value = false;
    } catch (error) {
      console.log(error);
    }
  });
}

defineExpose({
  acceptParams
});
</script>
