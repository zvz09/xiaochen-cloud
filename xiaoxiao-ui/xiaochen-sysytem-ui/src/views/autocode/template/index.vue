<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="addTemplate()">新增代码模板</el-button>
      </template>
      <!-- 菜单操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="EditPen" @click="editTemplate(scope.row)"> 编辑</el-button>
      </template>
    </ProTable>
    <el-drawer v-model="templateInfoVisible" :destroy-on-close="true" size="100%" :title="templateInfoTitle" @open="initEditor">
      <div style="margin: 10px">
        <el-form ref="templateFormRef" :model="templateInfoForm" :rules="formRules" label-width="120px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="模板名称" prop="name">
                <el-input v-model="templateInfoForm.name" />
              </el-form-item>
            </el-col>
            <el-col :span="2" class="text-center"> </el-col>
            <el-col :span="6">
              <el-form-item label="语言类型" prop="language">
                <el-select v-model="templateInfoForm.language" placeholder="请选择" style="width: 100%">
                  <el-option
                    v-for="item in languageOptions"
                    :key="item.value"
                    :label="`${item.label}(${item.value})`"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2" class="text-center"> </el-col>
            <el-col :span="6">
              <el-form-item label="模板引擎" prop="templateEngine">
                <el-select v-model="templateInfoForm.templateEngine" placeholder="请选择" style="width: 100%">
                  <el-option
                    v-for="item in engineOptions"
                    :key="item.value"
                    :label="`${item.label}(${item.value})`"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="模板类型" prop="templateType">
                <el-input v-model="templateInfoForm.templateType" autocomplete="off" />
              </el-form-item>
            </el-col>
            <el-col :span="2" class="text-center"> </el-col>
            <el-col :span="6">
              <el-form-item label="默认文件名" prop="defaultFileName">
                <el-input v-model="templateInfoForm.defaultFileName" autocomplete="off" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="contentEditorSpan">
              <div class="main">
                <div id="code-container" ref="contentEditorRef" style="height: 800px"></div>
              </div>
            </el-col>
            <el-col :span="verifyShowSpan">
              <code-mirror
                style="margin-top: 30px"
                basic
                :lang="verifyShowData.lang"
                v-model="verifyShowData.code"
                :theme="theme"
              />
            </el-col>
            <el-col :span="attributeTableSpan">
              <el-table :data="attributeDatas" style="width: 100%; margin-bottom: 20px" row-key="name">
                <el-table-column prop="description" label="名称" sortable />
                <el-table-column prop="name" label="表达式" sortable />
                <el-table-column prop="type" label="类型" sortable />
              </el-table>
              各引擎相关文档：
              <el-link href="http://freemarker.foofun.cn/" target="_blank" type="primary">FreeMarker</el-link>
              <el-link href="https://jfinal.com/doc/6-1" target="_blank" type="primary">Enjoy</el-link>
              <el-link href="https://www.gitbook.com/book/wizardforcel/velocity-doc" target="_blank" type="primary">
                Velocity
              </el-link>
              <el-link href="http://ibeetl.com/" target="_blank" type="primary">Beetl</el-link>
            </el-col>
          </el-row>
        </el-form>
        <div style="margin-top: 20px">
          <el-button class="float-left" type="primary" @click="onVerify">验证模板</el-button>
          <el-button class="float-left" type="primary" @click="onSubmit">提交</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts" name="autoCodeTemplate">
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { Template } from "@/api/autocode/template/types";
import {
  createAutoCodeTemplate,
  getAutoCodeTemplateDetail,
  getAutoCodeTemplateList,
  getEngineType,
  getLanguageType,
  updateAutoCodeTemplate
} from "@/api/autocode/template";
import { ResPage } from "@/api/interface";
import { CirclePlus, EditPen } from "@element-plus/icons-vue";

import { java } from "@codemirror/lang-java";
import { javascript } from "@codemirror/lang-javascript";
import { xml } from "@codemirror/lang-xml";
import { vue } from "@codemirror/lang-vue";
import { oneDark } from "@codemirror/theme-one-dark";
import { autocompletion } from "@codemirror/autocomplete";
import { basicSetup, EditorView } from "codemirror";
import { Compartment, EditorState } from "@codemirror/state";
import { ElMessage, FormInstance } from "element-plus";
import { LanguageSupport } from "@codemirror/language";
import { verifyAutoCodeTemplate } from "@/api/autocode/gencode";

import CodeMirror from "vue-codemirror6";

// ProTable 实例
const proTable = ref<ProTableInstance>();

const initParam = reactive({ id: "", traceId: "" });

const languageOptions = getLanguageType;

const engineOptions = getEngineType;

const columns = reactive<ColumnProps<Template.TemplateDetail>[]>([
  {
    prop: "name",
    label: "模板名",
    search: { el: "input", tooltip: "模板名" }
  },
  {
    prop: "language",
    label: "语言类型",
    search: { el: "select", tooltip: "语言类型" },
    enum: languageOptions
  },
  {
    prop: "templateEngine",
    label: "模板引擎类型",
    search: { el: "select", tooltip: "引擎类型" },
    enum: engineOptions
  },
  {
    prop: "templateType",
    label: "模板类型"
  },
  {
    prop: "defaultFileName",
    label: "默认文件名"
  },
  {
    prop: "createdAt",
    label: "创建时间"
  },
  { prop: "operation", label: "操作", fixed: "right", width: 330 }
]);

const getTableList = (params: any) => {
  return getAutoCodeTemplateList(params);
};

const dataCallback = (data: ResPage<Template.TemplateDetail>) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

// 新增/修改
const templateFormRef = ref<FormInstance>();
const contentEditorRef = ref<Element>();
const lang = ref<LanguageSupport>(java());
const editorView = ref<EditorView>();
const templateInfoTitle = ref("新增代码模板");
const templateInfoVisible = ref(false);
const verifyShowData = ref({
  lang: lang.value,
  code: ""
});
const contentEditorSpan = ref(18);
const verifyShowSpan = ref(0);
const attributeTableSpan = ref(6);

const templateInfoForm = ref<Template.CreateOrUpdateAutoCodeTemplateParams>({
  id: "",
  name: "",
  language: "",
  templateEngine: "",
  templateType: "",
  defaultFileName: "",
  content: "//请输入模板内容"
});

const formRules = ref({
  name: [{ required: true, message: "请输入模板名称", trigger: "blur" }],
  language: [{ required: true, message: "请选择语言", trigger: "blur" }],
  templateEngine: [{ required: true, message: "请选择模板引擎", trigger: "blur" }],
  templateType: [{ required: true, message: "请选择模板类型", trigger: "blur" }],
  content: [{ required: true, message: "请输入模板内容", trigger: "blur" }]
});

const addTemplate = () => {
  templateInfoTitle.value = "新增代码模板";
  templateInfoVisible.value = true;
};

const editTemplate = async (row: Template.TemplateDetail) => {
  templateInfoTitle.value = "编辑代码模板";
  const res = await getAutoCodeTemplateDetail(row.id);
  if (res.code === 200) {
    templateInfoVisible.value = true;
    templateInfoForm.value = res.data;
  }
};

const myCompletions = (context: any) => {
  let word = context.matchBefore(/\w*/);
  if (word.from === word.to && !context.explicit) return null;
  return {
    from: word.from,
    options: [
      { label: "match", type: "keyword" },
      { label: "hello", type: "variable", info: "(World)" },
      { label: "magic", type: "text", apply: "⠁⭒*.✩.*⭒⠁", detail: "macro" }
    ]
  };
};
const languageConf = new Compartment();
const initEditor = () => {
  if (typeof editorView.value !== "undefined") {
    editorView.value.destroy();
  }
  const startState = EditorState.create({
    doc: templateInfoForm.value.content,
    extensions: [basicSetup, oneDark, languageConf.of(java()), autoLanguage, autocompletion({ override: [myCompletions] })]
  });
  editorView.value = new EditorView({
    state: startState,
    parent: contentEditorRef.value
  });
};

const autoLanguage = EditorState.transactionExtender.of(() => {
  switch (templateInfoForm.value.language) {
    case "Java":
      lang.value = java();
      return {
        effects: languageConf.reconfigure(java())
      };
    case "Vue":
      lang.value = vue();
      return {
        effects: languageConf.reconfigure(vue())
      };
    case "Xml":
      lang.value = xml();
      return {
        effects: languageConf.reconfigure(xml())
      };
    case "JavaScript":
      lang.value = javascript();
      return {
        effects: languageConf.reconfigure(javascript())
      };
    default:
      lang.value = java();
      return {
        effects: languageConf.reconfigure(java())
      };
  }
});
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

const onVerify = () => {
  const content = editorView.value!.state.doc.toString();
  templateFormRef.value!.validate(async (valid: boolean) => {
    if (valid) {
      templateInfoForm.value.content = content;
      const res = await verifyAutoCodeTemplate(templateInfoForm.value);
      if (res.code === 200) {
        contentEditorSpan.value = 12;
        verifyShowSpan.value = 12;
        attributeTableSpan.value = 0;
        verifyShowData.value.lang = lang.value;
        verifyShowData.value.code = res.data;
      }
    }
  });
};
const onSubmit = () => {
  const content = editorView.value!.state.doc.toString();
  templateFormRef.value!.validate(async (valid: boolean) => {
    if (valid) {
      templateInfoForm.value.content = content;
      if (templateInfoForm.value.id === undefined) {
        const res = await createAutoCodeTemplate(templateInfoForm.value);
        if (res.code === 200) {
          ElMessage({
            type: "success",
            message: "添加成功",
            showClose: true
          });
        }
      } else {
        const res = await updateAutoCodeTemplate(templateInfoForm.value);
        if (res.code === 200) {
          ElMessage({
            type: "success",
            message: "编辑成功",
            showClose: true
          });
        }
      }
      templateInfoVisible.value = false;
    }
  });
};
interface Attribute {
  name: string;
  description: string;
  type: string;
  hasChildren?: boolean;
  children?: Attribute[];
}

const attributeDatas: Attribute[] = [
  {
    description: "基础包名",
    name: "genConfig.basePackageName",
    type: "String"
  },
  {
    description: "业务描述",
    name: "genConfig.description",
    type: "String"
  },
  {
    description: "作者",
    name: "genConfig.author",
    type: "String"
  },
  {
    description: "时间",
    name: "genConfig.datetime",
    type: "String"
  },
  {
    description: "表名",
    name: "genConfig.tableName",
    type: "String"
  },
  {
    description: "实体类名称",
    name: "genConfig.className",
    type: "String"
  },
  {
    description: "业务子包名",
    name: "genConfig.businessName",
    type: "String"
  },
  {
    description: "实体类简称",
    name: "genConfig.classShortName",
    type: "String"
  },
  {
    description: "列字段属性集合",
    name: "genConfig.columns",
    type: "List",
    children: [
      {
        description: "字段名",
        name: "name",
        type: "String"
      },
      {
        description: "描述",
        name: "description",
        type: "String"
      },
      {
        description: "类型",
        name: "type",
        type: "String"
      },
      {
        description: "是否DTO字段",
        name: "dto",
        type: "Boolean"
      },
      {
        description: "是否查询字段",
        name: "name",
        type: "Boolean"
      },
      {
        description: "是否唯一",
        name: "sole",
        type: "Boolean"
      }
    ]
  }
];
</script>

<style>
.main {
  margin-top: 30px;
  margin-left: 50px;
  width: 90%;
  height: 100%;
}

/* required! */
.cm-editor {
  height: 100%;
}
.el-link {
  margin-right: 8px;
}
</style>
