<template>
  <div>
    <div>
      <el-steps :active="active" finish-status="success">
        <el-step title="SQL" />
        <el-step title="属性配置" />
        <el-step title="选择模板" />
        <el-step title="代码预览" />
      </el-steps>
    </div>
    <div class="main" v-show="active == 0">
      <el-switch v-model="isSql" active-text="SQL生成" inactive-text="历史记录" />
      <div v-show="isSql">
        <code-mirror v-model="createSql.sql" :lang="lang" :theme="theme" basic style="height: 400px" />
      </div>
      <div v-show="!isSql">
        <div class="gva-table-box">
          <el-table :data="historyTableData" highlight-current-row @current-change="historyTableHandleCurrentChange">
            <el-table-column align="left" label="id" min-width="60" prop="id" sortable="custom" />
            <el-table-column align="left" label="表名" min-width="150" prop="tableName" sortable="custom" />
            <el-table-column align="left" label="业务描述" min-width="150" prop="description" sortable="custom" />
            <el-table-column
              align="left"
              label="配置信息json"
              min-width="150"
              prop="genConfig"
              show-overflow-tooltip
              sortable="custom"
            />
          </el-table>
          <div class="gva-pagination">
            <el-pagination
              :current-page="historyTablePage"
              :page-size="historyTablePageSize"
              :page-sizes="[10, 30, 50, 100]"
              :total="historyTableTotal"
              layout="total, sizes, prev, pager, next, jumper"
              @current-change="historyHandleCurrentChange"
              @size-change="historyHandleSizeChange"
            />
          </div>
        </div>
      </div>
      <el-button style="margin-top: 12px" @click="step0To1()">下一步</el-button>
    </div>
    <div class="main" v-show="active == 1">
      <el-form ref="genConfigForm" :inline="true" :model="genConfig" :rules="rules">
        <el-form-item label="基础包名" prop="basePackageName">
          <el-input :span="8" v-model="genConfig.basePackageName" placeholder="基础包名" clearable />
        </el-form-item>
        <el-form-item label="业务描述" prop="description">
          <el-input v-model="genConfig.description" placeholder="业务描述" clearable />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="genConfig.author" placeholder="作者" clearable />
        </el-form-item>
        <el-form-item label="表名" prop="tableName">
          <el-input v-model="genConfig.tableName" placeholder="表名" clearable />
        </el-form-item>
        <el-form-item label="实体类名称" prop="className">
          <el-input v-model="genConfig.className" placeholder="实体类名称" clearable />
        </el-form-item>
        <el-form-item label="业务子包名" prop="businessName">
          <el-input v-model="genConfig.businessName" placeholder="业务子包名" clearable />
        </el-form-item>
        <el-form-item label="实体类简称" prop="classShortName">
          <el-input v-model="genConfig.classShortName" placeholder="实体类简称" clearable />
        </el-form-item>
      </el-form>
      <el-table :data="genConfig.columns" table-layout="fixed">
        <el-table-column label="字段名" prop="name">
          <template #default="scope">
            <el-input v-model="scope.row.name" />
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="dto" label="是否是DTO字段">
          <template #default="scope">
            <el-switch v-model="scope.row.dto" active-text="是" inactive-text="否" />
            <el-switch v-show="scope.row.dto" v-model="scope.row.canNull" active-text="可为空" inactive-text="不能为空" />
          </template>
        </el-table-column>
        <el-table-column prop="query" label="是否是查询字段">
          <template #default="scope">
            <el-switch v-model="scope.row.query" active-text="是" inactive-text="否" />
            <el-select v-show="scope.row.query" v-model="scope.row.queryType" class="m-2" placeholder="查询方式">
              <el-option v-for="item in queryTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="sole" label="是否是唯一字段">
          <template #default="scope">
            <el-switch v-model="scope.row.sole" active-text="是" inactive-text="否" />
          </template>
        </el-table-column>
      </el-table>
      <el-button style="margin-top: 12px" @click="step1To0()">上一步</el-button>
      <el-button style="margin-top: 12px" @click="step1To2()">下一步</el-button>
    </div>
    <div class="main" v-show="active == 2">
      <el-table
        ref="multipleTable"
        :data="templateTableData"
        style="width: 100%"
        tooltip-effect="dark"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column :reserve-selection="true" align="left" type="selection" width="55" />
        <el-table-column align="left" label="模板名称" prop="name" width="280" />
        <el-table-column align="left" label="语言类型" prop="language" width="180" />
        <el-table-column align="left" label="模板引擎类型" prop="templateEngine" width="180" />
        <el-table-column align="left" label="模板类型" prop="templateType" width="180" />
        <el-table-column align="left" label="默认文件名" prop="defaultFileName" />
      </el-table>
      <div class="gva-pagination">
        <el-pagination
          :current-page="page"
          :page-size="pageSize"
          :page-sizes="[10, 30, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
      <el-button style="margin-top: 12px" @click="step2To1()">上一步</el-button>
      <el-button style="margin-top: 12px" @click="step2To3()">预览代码</el-button>
    </div>
    <div class="main" v-show="active == 3">
      <el-tabs v-model="previewCodeTabsValue" type="card">
        <el-tab-pane v-for="(item, key) in previewCodeTabs" :key="item.name" :label="item.title" :name="item.name">
          <el-container>
            <el-aside style="height: 600px; width: 90%; margin-left: 30px; margin-top: 10px">
              <code-mirror basic :lang="item.langType" v-model="item.code" style="" :theme="theme" />
            </el-aside>
            <el-main>
              <el-tooltip effect="dark" content="复制文件名" placement="bottom">
                <el-button
                  class=".copy-btn"
                  type="primary"
                  :icon="CopyDocument"
                  style="float: right"
                  @click="copyFileName(key)"
                ></el-button>
              </el-tooltip>

              <el-tooltip effect="dark" content="复制代码" placement="bottom">
                <el-button
                  class=".copy-btn"
                  type="primary"
                  :icon="CopyDocument"
                  style="float: right"
                  @click="copyCode(key)"
                ></el-button>
              </el-tooltip>
            </el-main>
          </el-container>
        </el-tab-pane>
      </el-tabs>
      <el-button style="margin-top: 12px" @click="step3To2()">上一步</el-button>
      <el-button style="margin-top: 12px" @click="download()">下载代码</el-button>
    </div>
  </div>
</template>

<script setup lang="ts" name="genCode">
import { ref } from "vue";
import CodeMirror from "vue-codemirror6";
import useClipboard from "vue-clipboard3";
import { sql } from "@codemirror/lang-sql";
import { java } from "@codemirror/lang-java";
import { xml } from "@codemirror/lang-xml";
import { vue } from "@codemirror/lang-vue";
import { javascript } from "@codemirror/lang-javascript";
import { parseCreateSql, previewCode, downloadCode, getGenCodeHistoryList } from "@/api/autocode/gencode";
import { getAutoCodeTemplateList } from "@/api/autocode/template";

import { CopyDocument } from "@element-plus/icons-vue";
import { ElMessage, FormInstance } from "element-plus";
import { GenCode } from "@/api/autocode/gencode/types";
import { Template } from "@/api/autocode/template/types";

//一键复制
const { toClipboard } = useClipboard();

const isSql = ref(true);
// 初始化
const createSql = {
  sql: "",
  dbType: "mysql"
};
// sql
const lang = sql();
// 主题样式设置
const theme = {
  "&": {
    color: "white",
    backgroundColor: "#282c34"
  },
  ".cm-content": {
    caretColor: "#0e9"
  },
  "&.cm-focused .cm-cursor": {
    borderLeftColor: "#0e9"
  },
  "&.cm-focused .cm-selectionBackground, ::selection": {
    backgroundColor: "#074"
  },
  ".cm-gutters": {
    backgroundColor: "#282c34",
    color: "#9b76da",
    border: "none"
  }
};

const previewCodeTabsValue = ref("");
const previewCodeTabs = ref<GenCode.PreviewCodeRes[]>([]);
const active = ref(0);
const queryTypeOptions = [
  {
    value: "like",
    label: "模糊查询"
  },
  {
    value: "eq",
    label: "精确"
  }
];
const genConfig = ref<GenCode.ParseCreateSqlRes>({
  capBusinessName: "",
  capClassName: "",
  capClassShortName: "",
  datetime: "",
  basePackageName: "",
  description: "",
  author: "",
  tableName: "",
  className: "",
  businessName: "",
  classShortName: "",
  columns: [],
  templateIds: []
});
const rules = ref({
  basePackageName: [{ required: true, message: "请输入基础包名", trigger: "blur" }],
  description: [{ required: true, message: "请输入业务描述", trigger: "blur" }],
  author: [{ required: true, message: "请输入作者", trigger: "blur" }],
  tableName: [{ required: true, message: "请输入表名", trigger: "blur" }],
  className: [{ required: true, message: "请输入实体类名称", trigger: "blur" }],
  businessName: [{ required: true, message: "请输入业务子包名", trigger: "blur" }],
  classShortName: [{ required: true, message: "请输入实体类简称", trigger: "blur" }]
});
const genConfigForm = ref<FormInstance>();
const templateTableData = ref<Template.TemplateDetail[]>([]);
const multipleSelection = ref<Template.TemplateDetail[]>([]);
const page = ref(1);
const total = ref(0);
const pageSize = ref(10);
const handleSelectionChange = (val: Template.TemplateDetail[]) => {
  multipleSelection.value = val;
};

const handleCurrentChange = (val: number) => {
  page.value = val;
  getTemplateTableData();
};
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  getTemplateTableData();
};

const getTemplateTableData = async () => {
  const table = await getAutoCodeTemplateList({
    pageNum: page.value,
    pageSize: pageSize.value
  });
  if (table.code === 200) {
    templateTableData.value = table.data.records;
    total.value = table.data.total;
    page.value = table.data.current;
    pageSize.value = table.data.size;
  }
};
const selectHistoryRow = ref("");
const step0To1 = async () => {
  if (isSql.value) {
    if (createSql.sql === "") {
      ElMessage.error("请输入建表sql");
    } else {
      const res = await parseCreateSql(createSql);
      if (res.code === 200) {
        active.value = 1;
        genConfig.value = res.data;
      }
    }
  } else {
    if (selectHistoryRow.value === "") {
      ElMessage.error("请选择历史记录");
    } else {
      console.log(selectHistoryRow.value);
      active.value = 1;
      genConfig.value = JSON.parse(selectHistoryRow.value);
    }
  }
};
const step1To0 = () => {
  active.value = 0;
  genConfig.value = {
    capBusinessName: "",
    capClassName: "",
    capClassShortName: "",
    datetime: "",
    basePackageName: "",
    description: "",
    author: "",
    tableName: "",
    className: "",
    businessName: "",
    classShortName: "",
    columns: [],
    templateIds: []
  };
};

const step1To2 = () => {
  active.value = 2;
  genConfigForm.value!.validate(async (valid, obj) => {
    if (valid) {
    } else {
      let a = [];
      for (let key in obj) {
        a.push(obj[key][0].message);
      }
      ElMessage.error(a[0]);
    }
  });
  getTemplateTableData();
};
const step2To1 = () => {
  active.value = 1;
  multipleSelection.value = [];
};

const step2To3 = async () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.error("请选择模板");
    return false;
  }
  await genConfigForm.value!.validate(async (valid, obj) => {
    if (valid) {
      genConfig.value.templateIds = multipleSelection.value.map(item => item.id);
      const res = await previewCode(genConfig.value);
      if (res.code === 200) {
        active.value = 3;
        previewCodeTabsValue.value = res.data[0].name;
        previewCodeTabs.value = res.data;
        previewCodeTabs.value.forEach(item => {
          if (item.type == "java") {
            item.langType = java();
          } else if (item.type == "xml") {
            item.langType = xml();
          } else if (item.type == "vue") {
            item.langType = vue();
          } else {
            item.langType = javascript();
          }
        });
      }
    } else {
      let a = [];
      for (let key in obj) {
        a.push(obj[key][0].message);
      }
      ElMessage.error(a[0]);
    }
  });
};
const step3To2 = () => {
  active.value = 2;
  previewCodeTabsValue.value = "";
  previewCodeTabs.value = [];
};

const copyCode = async (index: number) => {
  try {
    await toClipboard(previewCodeTabs.value[index].code);
    ElMessage({
      message: "复制成功！",
      type: "success"
    });
  } catch (e) {
    console.error(e);
    ElMessage.error("复制失败！");
  }
};
const copyFileName = async (index: number) => {
  try {
    await toClipboard(previewCodeTabs.value[index].title);
    ElMessage({
      message: "复制成功！",
      type: "success"
    });
  } catch (e) {
    console.error(e);
    ElMessage.error("复制失败！");
  }
};
const download = async () => {
  await downloadCode(genConfig.value);
};
/* ----------------- */
const historyTablePage = ref(1);
const historyTableTotal = ref(0);
const historyTablePageSize = ref(10);
const historyTableData = ref<GenCode.HistoryListRes[]>([]);

// 分页
const historyHandleSizeChange = (val: number) => {
  pageSize.value = val;
  getHistoryTableData();
};

const historyTableHandleCurrentChange = (row: GenCode.HistoryListRes) => {
  selectHistoryRow.value = row.genConfig;
};

const historyHandleCurrentChange = (val: number) => {
  page.value = val;
  getHistoryTableData();
};
// 查询
const getHistoryTableData = async () => {
  const table = await getGenCodeHistoryList({ pageNum: historyTablePage.value, pageSize: historyTablePageSize.value });
  if (table.code === 200) {
    historyTableData.value = table.data.records;
    historyTableTotal.value = table.data.total;
    historyTablePage.value = table.data.current;
    historyTablePageSize.value = table.data.size;
  }
};

getHistoryTableData();
</script>
<style>
.main {
  margin: 30px;
  width: 95%;
  height: 100%;
}

/* required! */
.cm-editor {
  height: 100%;
}
</style>
