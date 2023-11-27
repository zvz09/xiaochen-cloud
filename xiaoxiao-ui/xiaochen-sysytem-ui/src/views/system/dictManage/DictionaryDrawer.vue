<template>
  <el-drawer v-model="drawerVisible" :destroy-on-close="true" size="60%" :title="`${drawerProps.title}`">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button v-auth="'addItem'" type="primary" :icon="CirclePlus" @click="openDialog('新增字典项')">新增字典项</el-button>
      </template>
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button v-auth="'editItem'" type="primary" link :icon="EditPen" @click="openDialog('编辑字典项', scope.row)">
          编辑
        </el-button>
        <el-button v-auth="'deleteItem'" type="primary" link :icon="Delete" @click="deleteInfo(scope.row)">删除</el-button>
      </template>

      <template #tagType="scope">
        <p v-if="!scope.row.tagType">--</p>
        <el-tag :type="scope.row.tagType" v-else>
          {{ scope.row.tagType }}
        </el-tag>
      </template>
    </ProTable>
    <el-dialog v-model="dialogFormVisible" :title="dialogTitle">
      <el-form ref="formRef" :inline="true" :model="formData" :rules="rules" label-width="140px">
        <el-form-item label="显示值" prop="label">
          <el-input v-model="formData.label" autocomplete="off" placeholder="显示值" />
        </el-form-item>
        <el-form-item label="枚举值" prop="value">
          <el-input v-model="formData.value" autocomplete="off" placeholder="枚举值" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="formData.status" size="large" active-text="启用" inactive-text="禁用" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="sort">
          <el-input v-model="formData.sort" type="number" placeholder="显示顺序" />
        </el-form-item>
        <el-form-item label="tag类型" prop="tagType">
          <el-select v-model="formData.tagType" placeholder="Select">
            <el-option v-for="item in tagTypes" :key="item.value" :label="item.label" :value="item.value">
              <el-tag :type="item.value">{{ item.label }}</el-tag>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitDialog">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </el-drawer>
</template>

<script setup lang="tsx" name="DictionaryDrawer">
import { reactive, ref } from "vue";
import { ElMessage, FormInstance, FormRules } from "element-plus";
import { Dictionary } from "@/api/system/dictionary/types";
import { CirclePlus, Delete, EditPen } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { useAuthButtons } from "@/hooks/useAuthButtons";
import { useHandleData } from "@/hooks/useHandleData";
import {
  changeDictionaryDetailStatus,
  createDictionaryDetail,
  deleteDictionaryDetail,
  getDictionaryDetailList,
  updateDictionaryDetail
} from "@/api/system/dictionary";

// ProTable 实例
const proTable = ref<ProTableInstance>();
interface DrawerProps {
  title: string;
  row: Partial<Dictionary.DictionaryVO>;
  getTableList?: () => void;
}

const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  title: "",
  row: {}
});

// 页面按钮权限（按钮权限既可以使用 hooks，也可以直接使用 v-auth 指令，指令适合直接绑定在按钮上，hooks 适合根据按钮权限显示不同的内容）
const { BUTTONS } = useAuthButtons();

// 表格配置项
const columns = reactive<ColumnProps<Dictionary.DictionaryDetailVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "label",
    label: "显示值",
    search: { el: "input", tooltip: "显示值" }
  },
  {
    prop: "value",
    label: "枚举值",
    search: { el: "input", tooltip: "枚举值" }
  },
  {
    prop: "status",
    label: "状态",
    render: scope => {
      return (
        <>
          {BUTTONS.value.detailStatus ? (
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
    prop: "sort",
    label: "显示顺序"
  },
  {
    prop: "tagType",
    label: "tag类型"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 480 }
]);

const getTableList = (params: any) => {
  return getDictionaryDetailList(params);
};

const initParam = ref({ sysDictionaryId: "" });

const dataCallback = (data: any) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

// 接收父组件传过来的参数
const acceptParams = (params: DrawerProps) => {
  drawerProps.value = params;
  initParam.value.sysDictionaryId = drawerProps.value.row.id;
  drawerVisible.value = true;
};

// 删除字典项信息
const deleteInfo = async (params: Dictionary.DictionaryDetailVO) => {
  await useHandleData(deleteDictionaryDetail, params.id, `删除【${params.name}】字典`);
  proTable.value?.getTableList();
};

// 切换字典项状态
const changeStatus = async (row: Dictionary.DictionaryDetailVO) => {
  await useHandleData(changeDictionaryDetailStatus, row.id, `切换【${row.name}】字典状态`);
  proTable.value?.getTableList();
};

const formRef = ref<FormInstance>();
const dialogFormVisible = ref(false);
const dialogTitle = ref("新增菜单");

const tagTypes = reactive([
  {
    value: "",
    label: "默认"
  },
  {
    value: "success",
    label: "success"
  },
  {
    value: "info",
    label: "info"
  },
  {
    value: "warning",
    label: "warning"
  },
  {
    value: "danger",
    label: "danger"
  }
]);

const rules = reactive<FormRules>({
  label: [{ required: true, message: "请输入显示值", trigger: "blur" }],
  value: [{ required: true, message: "请输入枚举值", trigger: "blur" }]
});
const formData = ref<Dictionary.DictionaryDetailVO>({
  id: "",
  label: "",
  sort: 0,
  status: true,
  sysDictionaryId: drawerProps.value.row.id,
  tagType: "",
  value: ""
});

const openDialog = (title: string, row: Partial<Dictionary.DictionaryDetailVO> = {}) => {
  dialogTitle.value = title;
  if (row) {
    formData.value = { ...row };
  }
  formData.value.sysDictionaryId = drawerProps.value.row.id;
  dialogFormVisible.value = true;
};
const submitDialog = async () => {
  formRef.value?.validate(async valid => {
    if (valid) {
      if (formData.value.id) {
        await updateDictionaryDetail(formData.value);
      } else {
        await createDictionaryDetail(formData.value);
      }
      ElMessage.success(formData.value.id ? "编辑成功" : "添加成功!");
      dialogFormVisible.value = false;
      proTable.value?.search();
    }
  });
};
defineExpose({
  acceptParams
});
</script>
