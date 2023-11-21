<template>
  <el-drawer v-model="drawerVisible" :destroy-on-close="true" size="450px" :title="`${drawerProps.title}权限字绑定资源`">
    <el-tree
      ref="apiTreeRef"
      :data="apiData"
      :default-checked-keys="apiTreeIds"
      :props="apiDefaultProps"
      default-expand-all
      highlight-current
      node-key="id"
      show-checkbox
      :filter-node-method="filterNode"
      @check="nodeChange"
    />
    <template #footer>
      <el-button @click="drawerVisible = false">取消</el-button>
      <el-button v-show="!drawerProps.isView" type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts" name="BindApiDrawer">
import { ref } from "vue";
import { ElMessage, ElTree } from "element-plus";
import { listAPITree } from "@/api/system/api";
import { bindApis } from "@/api/system/permCode";
import { API } from "@/api/system/api/types";

interface DrawerProps {
  title: string;
  isView: boolean;
  id: string;
  apiIds: number[];
}

const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
  isView: false,
  title: "",
  id: "",
  apiIds: []
});

const apiDefaultProps = ref({
  children: "children",
  label: "description",
  disabled: () => {
    return drawerProps.value.isView;
  }
});

const apiData = ref<API.ApiVO[]>();
const apiTreeIds = ref([]);
// 接收父组件传过来的参数
const acceptParams = async (params: DrawerProps) => {
  apiData.value = (await listAPITree()).data;
  drawerProps.value = params;
  drawerProps.value.apiIds &&
    drawerProps.value.apiIds.forEach(item => {
      apiTreeIds.value.push(item);
    });
  drawerVisible.value = true;
};

const filterNode = (value: any, data: any) => {
  if (!value) return true;
  return data.description.indexOf(value) !== -1;
};
const needConfirm = ref(false);
const nodeChange = () => {
  needConfirm.value = true;
};
const apiTreeRef = ref<InstanceType<typeof ElTree>>();
const handleSubmit = async () => {
  const checkArr = apiTreeRef.value?.getCheckedNodes(true);
  let ids: any = [];
  checkArr &&
    checkArr.forEach(item => {
      ids.push(item.id);
    });
  await bindApis(drawerProps.value.id, ids);
  ElMessage.success({ message: `设置成功` });
  drawerVisible.value = false;
};

defineExpose({
  acceptParams
});
</script>
