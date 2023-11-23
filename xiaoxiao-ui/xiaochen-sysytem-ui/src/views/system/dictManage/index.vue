<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button v-auth="'add'" type="primary" :icon="CirclePlus" @click="openDialog('新增字典')">新增字典</el-button>
      </template>
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="View" @click="openDrawer('查看', scope.row)">查看</el-button>
        <el-button v-auth="'edit'" type="primary" link :icon="EditPen" @click="openDialog('编辑字典', scope.row)">编辑</el-button>
        <el-button v-auth="'delete'" type="primary" link :icon="Delete" @click="deleteInfo(scope.row)">删除</el-button>
      </template>
    </ProTable>
    <DictionaryDrawer ref="drawerRef" />
    <el-dialog v-model="dialogFormVisible" :title="dialogTitle">
      <el-form ref="formRef" :inline="true" :model="formData" :rules="rules" label-width="140px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" autocomplete="off" placeholder="名称" />
        </el-form-item>
        <el-form-item label="英文编码" prop="encode">
          <el-input v-model="formData.encode" autocomplete="off" placeholder="英文编码" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="formData.status" size="large" active-text="启用" inactive-text="禁用" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" placeholder="描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitDialog">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="tsx" name="dictManage">
import { CirclePlus, Delete, EditPen, View } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { reactive, ref } from "vue";
import {
  listDictionary,
  createDictionary,
  updateDictionary,
  deleteDictionary,
  changeDictionaryStatus
} from "@/api/system/dictionary";
import { useHandleData } from "@/hooks/useHandleData";
import DictionaryDrawer from "@/views/system/dictManage/DictionaryDrawer.vue";
import { Dictionary } from "@/api/system/dictionary/types";
import { useAuthButtons } from "@/hooks/useAuthButtons";
import { ElMessage, FormInstance, FormRules } from "element-plus";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 页面按钮权限（按钮权限既可以使用 hooks，也可以直接使用 v-auth 指令，指令适合直接绑定在按钮上，hooks 适合根据按钮权限显示不同的内容）
const { BUTTONS } = useAuthButtons();

// 表格配置项
const columns = reactive<ColumnProps<Dictionary.DictionaryVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "name",
    label: "名称",
    search: { el: "input", tooltip: "登录名" }
  },
  {
    prop: "encode",
    label: "英文编码",
    search: { el: "input", tooltip: "英文编码" }
  },
  {
    prop: "status",
    label: "状态",
    render: scope => {
      return (
        <>
          {BUTTONS.value.status ? (
            <el-switch
              model-value={scope.row.status}
              active-text={scope.row.status ? "启用" : "禁用"}
              onClick={() => changeStatus(scope.row)}
            />
          ) : (
            <el-tag type={scope.row.status ? "success" : "danger"}>{scope.row.status ? "启用" : "禁用"}</el-tag>
          )}
        </>
      );
    }
  },
  {
    prop: "description",
    label: "描述"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 480 }
]);

const getTableList = (params: any) => {
  return listDictionary(params);
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

const drawerRef = ref<InstanceType<typeof DictionaryDrawer> | null>(null);
const openDrawer = (title: string, row: Partial<Dictionary.DictionaryVO> = {}) => {
  const params = {
    title,
    row: { ...row },
    getTableList: proTable.value?.getTableList
  };
  drawerRef.value?.acceptParams(params);
};

// 删除字典信息
const deleteInfo = async (params: Dictionary.DictionaryVO) => {
  await useHandleData(deleteDictionary, params.id, `删除【${params.name}】字典`);
  proTable.value?.getTableList();
};

// 切换字典状态
const changeStatus = async (row: Dictionary.DictionaryVO) => {
  await useHandleData(changeDictionaryStatus, row.id, `切换【${row.name}】字典状态`);
  proTable.value?.getTableList();
};

const formRef = ref<FormInstance>();
const dialogFormVisible = ref(false);
const dialogTitle = ref("");

const rules = reactive<FormRules>({
  name: [{ required: true, message: "请输入名称", trigger: "blur" }],
  encode: [{ required: true, message: "请输入英文编码", trigger: "blur" }]
});
const formData = ref<Dictionary.DictionaryVO>({
  description: "",
  encode: "",
  id: "",
  name: "",
  status: true
});

const openDialog = (title: string, row: Partial<Dictionary.DictionaryVO> = {}) => {
  dialogTitle.value = title;
  if (row) {
    formData.value = { ...row };
  }
  dialogFormVisible.value = true;
};

const submitDialog = async () => {
  formRef.value?.validate(async valid => {
    if (valid) {
      if (formData.value.id) {
        await updateDictionary(formData.value);
      } else {
        await createDictionary(formData.value);
      }
      ElMessage.success(formData.value.id ? "编辑成功" : "添加成功!");
      dialogFormVisible.value = false;
      proTable.value?.search();
    }
  });
};
</script>
