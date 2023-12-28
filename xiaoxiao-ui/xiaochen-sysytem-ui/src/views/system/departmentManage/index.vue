<template>
  <div class="card content-box">
    <div class="table-box">
      <ProTable
        ref="proTable"
        title="部门列表"
        row-key="path"
        :indent="20"
        :columns="columns"
        :data="deptData"
        :pagination="false"
      >
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" @click="addFunc('0')">新增菜单</el-button>
        </template>

        <!-- 操作 -->
        <template #operation="scope">
          <el-button type="primary" link :icon="Plus" @click="addFunc(scope.row.id)"> 新增</el-button>
          <el-button type="primary" link :icon="EditPen" @click="editFunc(scope.row)"> 编辑</el-button>
          <el-button type="primary" link :icon="Delete" @click="deleteFunc(scope.row)"> 删除</el-button>
        </template>
      </ProTable>
      <el-dialog v-model="dialogFormVisible" :before-close="closeDialog" :title="dialogTitle">
        <el-form ref="deptFormRef" :model="deptForm" :rules="rules" label-width="80px">
          <el-form-item label="上级部门" prop="parentId">
            <el-cascader
              v-model="deptForm.parentId"
              :options="deptOption"
              :placeholder="parentDeptName"
              :props="{ checkStrictly: true, label: 'title', value: 'id', disabled: 'disabled', emitPath: false }"
              :show-all-levels="false"
              filterable
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="部门名称" prop="deptName">
            <el-input v-model="deptForm.deptName" autocomplete="off" />
          </el-form-item>
          <el-form-item label="显示顺序" prop="orderNum">
            <el-input v-model="deptForm.orderNum" autocomplete="off" />
          </el-form-item>
          <el-form-item label="负责人" prop="leader">
            <el-input v-model="deptForm.leader" autocomplete="off" />
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="deptForm.phone" autocomplete="off" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="deptForm.email" autocomplete="off" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-switch
              v-model="deptForm.status"
              :active-value="true"
              :inactive-value="false"
              active-text="启用"
              inactive-text="停止"
              inline-prompt
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="closeDialog">取 消</el-button>
            <el-button type="primary" @click="enterDialog">确 定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts" name="departmentManage">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { Department } from "@/api/system/department/types";
import { getDepartmentTree, deleteDepartment, createDepartment, updateDepartment } from "@/api/system/department";
import { ElMessage, ElMessageBox, FormInstance } from "element-plus";
import { CirclePlus, Delete, EditPen, Plus } from "@element-plus/icons-vue";

const proTable = ref();

const deptData = ref();
const parentDeptName = ref("");
const deptOption = ref([
  {
    id: "0",
    deptName: "根节点"
  }
]);

const deptForm = ref<Department.DTO>({
  deptName: "",
  email: "",
  leader: "",
  phone: "",
  status: false
});

// 表格配置项
const columns = reactive<ColumnProps<Department.VO>[]>([
  {
    prop: "deptName",
    label: "部门名称"
  },
  {
    prop: "orderNum",
    label: "显示顺序"
  },
  {
    prop: "leader",
    label: "负责人"
  },
  {
    prop: "phone",
    label: "联系电话"
  },
  {
    prop: "email",
    label: "邮箱"
  },
  {
    prop: "status",
    label: "部门状态"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const setDeptOptions = (data: Department.VO[], optionsData: any, disabled: boolean) => {
  data &&
    data.forEach(item => {
      if (item.id === deptForm.value.parentId) {
        parentDeptName.value = item.deptName;
      }
      if (item.children && item.children.length) {
        const option = {
          deptName: item.deptName,
          id: item.id,
          disabled: disabled || item.id === deptForm.value.id,
          children: []
        };
        setDeptOptions(item.children, option.children, disabled || item.id === deptForm.value.id);
        optionsData.push(option);
      } else {
        const option = {
          deptName: item.deptName,
          id: item.id,
          disabled: disabled || item.id === deptForm.value.id
        };
        optionsData.push(option);
      }
    });
};

const setOptions = () => {
  deptOption.value = [
    {
      id: "0",
      deptName: "根节点"
    }
  ];
  setDeptOptions(deptData.value, deptOption.value, false);
};

async function getDepts() {
  const { data } = await getDepartmentTree();
  deptData.value = data;
  setOptions();
}

getDepts();

const deleteFunc = async (row: Department.VO) => {
  ElMessageBox.confirm("是否继续?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    const res = await deleteDepartment(row.id);
    if (res.code === 200) {
      ElMessage({
        type: "success",
        message: "删除成功!"
      });
      await getDepts();
    }
  });
};

//弹窗相关
const dialogTitle = ref("新增部门");
const dialogFormVisible = ref(false);
const deptFormRef = ref<FormInstance>();
const rules = ref({
  parentId: [{ required: true, message: "请选择上级部门", trigger: "blur" }],
  deptName: [{ required: true, message: "请输入部门名称", trigger: "blur" }],
  leader: [{ required: true, message: "请输入负责人", trigger: "blur" }],
  phone: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
  email: [{ required: true, message: "请输入邮箱", trigger: "blur" }]
});

const initForm = () => {
  deptFormRef.value!.resetFields();
  deptForm.value = {
    deptName: "",
    email: "",
    leader: "",
    phone: "",
    status: false
  };
};

const closeDialog = () => {
  initForm();
  dialogFormVisible.value = false;
};

const editFunc = async (row: Department.VO) => {
  dialogTitle.value = "编辑部门";
  Object.assign(deptForm.value, row);
  dialogFormVisible.value = true;
};

const addFunc = (parentId: string) => {
  setOptions();
  initForm();
  dialogTitle.value = parentId == "0" ? "新增部门" : "新增子部门";
  deptForm.value.parentId = parentId;
  dialogFormVisible.value = true;
};

const enterDialog = async () => {
  await deptFormRef.value?.validate(async valid => {
    if (valid) {
      if (deptForm.value.id === undefined) {
        await createDepartment(deptForm.value);
        ElMessage.success("操作成功");
      } else {
        await updateDepartment(deptForm.value);
        ElMessage.success("操作成功");
      }
      await getDepts();
      dialogFormVisible.value = false;
    }
  });
};
</script>
