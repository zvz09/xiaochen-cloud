<template>
  <div>
    <div class="table-box">
      <ProTable
        ref="proTable"
        title="菜单列表"
        row-key="path"
        :indent="20"
        :columns="columns"
        :data="menuData"
        :pagination="false"
      >
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" @click="addMenu('0')">新增菜单</el-button>
        </template>
        <!-- 菜单图标 -->
        <template #icon="scope">
          <el-icon :size="18">
            <component :is="scope.row.meta.icon"></component>
          </el-icon>
        </template>
        <!-- 菜单操作 -->
        <template #operation="scope">
          <el-button type="primary" link :icon="Plus" @click="addMenu(scope.row.id)"> 新增</el-button>
          <el-button type="primary" link :icon="EditPen" @click="editMenu(scope.row)"> 编辑</el-button>
          <el-button type="primary" link :icon="Delete" @click="delMenu(scope.row.id)"> 删除</el-button>
        </template>
      </ProTable>
    </div>
    <div>
      <el-dialog v-model="dialogFormVisible" :title="dialogTitle">
        <el-form ref="menuFormRef" :inline="true" :model="menuForm" :rules="rules" label-position="top" label-width="140px">
          <el-form-item label="父节点ID" style="width: 30%">
            <el-cascader
              v-model="menuForm.parentId"
              style="width: 100%"
              :options="menuOption"
              :props="{ checkStrictly: true, label: 'title', value: 'id', disabled: 'disabled', emitPath: false }"
              :show-all-levels="false"
              filterable
            />
          </el-form-item>
          <el-form-item label="菜单名称" prop="title">
            <el-input v-model="menuForm.title" autocomplete="off" placeholder="路由标题 (用作 document.title || 菜单的名称)" />
          </el-form-item>
          <el-form-item label="访问路径" prop="path">
            <el-input v-model="menuForm.path" autocomplete="off" placeholder="路由菜单访问路径" />
          </el-form-item>
          <el-form-item label="路由 name" prop="name">
            <el-input
              v-model="menuForm.name"
              autocomplete="off"
              placeholder="路由 name (对应页面组件 name, 可用作 KeepAlive 缓存标识 && 按钮权限筛选)"
            />
          </el-form-item>
          <el-form-item label="重定向地址" prop="redirect">
            <el-input v-model="menuForm.redirect" autocomplete="off" placeholder="路由重定向地址" />
          </el-form-item>
          <el-form-item label="视图文件路径" prop="component">
            <el-input v-model="menuForm.component" autocomplete="off" placeholder="视图文件路径" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="menuForm.sort" autocomplete="off" placeholder="排序" />
          </el-form-item>
          <el-form-item label="图标" prop="icon">
            <SelectIcon v-model:icon-value="menuForm.icon" />
          </el-form-item>
          <el-form-item label="外链地址" prop="isLink">
            <el-input v-model="menuForm.isLink" autocomplete="off" placeholder="路由外链时填写的访问地址" />
          </el-form-item>
          <el-form-item label="是否隐藏" prop="isHide">
            <el-switch v-model="menuForm.isHide" />
          </el-form-item>
          <el-form-item label="是否固定" prop="isAffix">
            <el-switch v-model="menuForm.isAffix" />
          </el-form-item>
          <el-form-item label="是否全屏" prop="name">
            <el-switch v-model="menuForm.isFull" />
          </el-form-item>
          <el-form-item label="是否缓存" prop="isKeepAlive">
            <el-switch v-model="menuForm.isKeepAlive" />
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

<script setup lang="ts" name="menuMange">
import { reactive, ref } from "vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { CirclePlus, Delete, EditPen, Plus } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { MenuForm, MenuVO } from "@/api/system/menu/types";
import { createMenu, deleteMenu, listTree, updateMenu } from "@/api/system/menu";
import { ElMessage, ElMessageBox, FormInstance, FormRules } from "element-plus";
import SelectIcon from "@/components/SelectIcon/index.vue";

const proTable = ref();

const menuData = ref();
const menuOption = ref([
  {
    id: "0",
    title: "根菜单"
  }
]);

const setMenuOptions = (menuData: MenuVO[], optionsData: any, disabled: boolean) => {
  menuData &&
    menuData.forEach(item => {
      if (item.children && item.children.length) {
        const option = {
          title: item.meta.title,
          id: item.id,
          disabled: disabled || item.id === menuForm.id,
          children: []
        };
        setMenuOptions(item.children, option.children, disabled || item.id === menuForm.id);
        optionsData.push(option);
      } else {
        const option = {
          title: item.meta.title,
          id: item.id,
          disabled: disabled || item.id === menuForm.id
        };
        optionsData.push(option);
      }
    });
};
const setOptions = () => {
  menuOption.value = [
    {
      id: "0",
      title: "根目录"
    }
  ];
  setMenuOptions(menuData.value, menuOption.value, false);
};

async function getMenus() {
  const { data } = await listTree();
  menuData.value = data;
  setOptions();
}

// 表格配置项
const columns: ColumnProps[] = [
  { prop: "meta.title", label: "菜单名称", align: "left" },
  { prop: "meta.icon", label: "菜单图标" },
  { prop: "name", label: "菜单名称" },
  { prop: "path", label: "菜单路径", width: 300 },
  { prop: "component", label: "组件路径", width: 300 },
  { prop: "operation", label: "操作", width: 300, fixed: "right" }
];

const delMenu = (id: string) => {
  ElMessageBox.confirm("此操作将永久删除所有角色下该菜单, 是否继续?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(async () => {
      await deleteMenu(id);
      ElMessage({
        type: "success",
        message: "删除成功!"
      });
      await getMenus();
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "已取消删除"
      });
    });
};

getMenus();

//弹窗部分
const menuFormRef = ref<FormInstance>();
const dialogFormVisible = ref(false);
const dialogTitle = ref("新增菜单");

const rules = reactive<FormRules>({
  path: [{ required: true, message: "请输入菜单name", trigger: "blur" }],
  component: [{ required: true, message: "请输入文件路径", trigger: "blur" }],
  "meta.title": [{ required: true, message: "请输入菜单展示名称", trigger: "blur" }]
});

const menuForm = reactive<MenuForm>({
  id: undefined,
  parentId: undefined,
  path: "",
  name: "",
  component: "",
  redirect: "",
  sort: 1,
  icon: "",
  title: "",
  activeMenu: "",
  isLink: "",
  isHide: false,
  isFull: false,
  isAffix: false,
  isKeepAlive: true
});

/** 重置表单 */
function resetForm() {
  menuFormRef.value?.resetFields();
  menuFormRef.value?.clearValidate();
  menuForm.id = undefined;
  menuForm.parentId = undefined;
  menuForm.path = "";
  menuForm.name = "";
  menuForm.component = "";
  menuForm.redirect = "";
  menuForm.icon = "";
  menuForm.title = "";
  menuForm.activeMenu = "";
  menuForm.isLink = "";
  menuForm.isHide = false;
  menuForm.isFull = false;
  menuForm.isAffix = false;
  menuForm.isKeepAlive = true;
}

const addMenu = (parentId: string) => {
  dialogTitle.value = parentId == "0" ? "新增根菜单" : "新增子菜单";
  menuForm.parentId = parentId;
  dialogFormVisible.value = true;
};
// 修改菜单方法
const editMenu = async (menuOptions: MenuVO) => {
  dialogTitle.value = "编辑菜单";
  Object.assign(menuForm, menuOptions);
  Object.assign(menuForm, menuOptions.meta);
  dialogFormVisible.value = true;
};

//关闭弹窗
const closeDialog = () => {
  dialogFormVisible.value = false;
  resetForm();
};
//弹窗提交
const enterDialog = async () => {
  menuFormRef.value?.validate(async valid => {
    if (valid) {
      if (menuForm.id) {
        await updateMenu(menuForm);
      } else {
        await createMenu(menuForm);
      }
      ElMessage.success(menuForm.id ? "编辑成功" : "添加成功!");
      dialogFormVisible.value = false;
      await getMenus();
    }
  });
};
</script>
