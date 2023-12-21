<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')">新增解析类</el-button>
      </template>

      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" :disabled="scope.row.status" link :icon="EditPen" @click="openDrawer('编辑', scope.row)">
          编辑
        </el-button>
      </template>
    </ProTable>
    <ParseClassDrawer ref="drawerRef" />
  </div>
</template>

<script setup lang="tsx" name="reptileParseClass">
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { reactive, ref } from "vue";
import { ReptileParseClass } from "@/api/note/reptileParseClass/types";
import { pageParseClass, operateParseClass, insertParseClass, updateParseClass } from "@/api/note/reptileParseClass";
import { useAuthButtons } from "@/hooks/useAuthButtons";
import { useHandleData } from "@/hooks/useHandleData";
import ParseClassDrawer from "@/views/note/reptileParseClass/ParseClassDrawer.vue";
import { Article } from "@/api/note/article/types";
import { CirclePlus, EditPen } from "@element-plus/icons-vue";

// ProTable 实例
const proTable = ref<ProTableInstance>();

// 页面按钮权限（按钮权限既可以使用 hooks，也可以直接使用 v-auth 指令，指令适合直接绑定在按钮上，hooks 适合根据按钮权限显示不同的内容）
const { BUTTONS } = useAuthButtons();

// 表格配置项
const columns = reactive<ColumnProps<ReptileParseClass.ReptileParseClassVO>[]>([
  { type: "selection", fixed: "left", width: 70 },
  {
    prop: "className",
    label: "类名",
    width: 400,
    search: { el: "input", tooltip: "类名" }
  },
  {
    prop: "siteUrl",
    label: "网站",
    width: 400,
    search: { el: "input", tooltip: "网站" }
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
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const getTableList = (params: any) => {
  return pageParseClass(params);
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

const changeStatus = async (row: ReptileParseClass.ReptileParseClassVO) => {
  await useHandleData(
    operateParseClass,
    { id: row.id, operate: row.status ? "disable" : "enable" },
    `切换【${row.className}】状态`
  );
  proTable.value?.getTableList();
};

const drawerRef = ref<InstanceType<typeof ParseClassDrawer> | null>(null);

const openDrawer = (title: string, row: Partial<Article.ArticleVO> = {}) => {
  const params = reactive<Article.DrawerProps>({
    title: title,
    row: { ...row },
    api: title === "新增" ? insertParseClass : title === "编辑" ? updateParseClass : undefined,
    getTableList: proTable.value?.getTableList,
    id: row.id
  });
  drawerRef.value?.acceptParams(params);
};
</script>
