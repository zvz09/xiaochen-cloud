<template>
  <div class="panel-tab__content">
    <el-table :data="elementListenersList" border size="small">
      <el-table-column label="序号" type="index" width="50px" />
      <el-table-column label="事件类型" min-width="100px" prop="event" />
      <el-table-column
        :formatter="row => listenerTypeObject[row.listenerType]"
        label="监听器类型"
        min-width="100px"
        show-overflow-tooltip
      />
      <el-table-column label="操作" width="90px">
        <template v-slot="{ row, $index }">
          <el-button link type="" @click="openListenerForm(row, $index)">编辑</el-button>
          <el-divider direction="vertical" />
          <el-button link style="color: #ff4d4f" type="" @click="removeListener(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="element-drawer__button">
      <el-button :icon="Plus" size="small" type="primary" @click="openListenerForm(null)">添加监听器</el-button>
    </div>

    <!-- 监听器 编辑/创建 部分 -->
    <el-drawer v-model="listenerFormModelVisible" :size="`${width}px`" append-to-body destroy-on-close title="执行监听器">
      <el-form ref="listenerFormRef" :model="listenerForm" label-width="96px" size="small" @submit.prevent>
        <el-form-item :rules="{ required: true, trigger: ['blur', 'change'] }" label="事件类型" prop="event">
          <el-select v-model="listenerForm.event" @change="changeEventType">
            <el-option label="start" value="start" />
            <el-option label="end" value="end" />
          </el-select>
        </el-form-item>
        <el-form-item :rules="{ required: true, trigger: ['blur', 'change'] }" label="监听器类型" prop="listenerType">
          <el-select v-model="listenerForm.listenerType">
            <el-option v-for="i in Object.keys(listenerTypeObject)" :key="i" :label="listenerTypeObject[i]" :value="i" />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="listenerForm.listenerType === 'classListener'"
          key="listener-class"
          :rules="{ required: true, trigger: ['blur', 'change'] }"
          label="Java类"
          prop="class"
        >
          <el-select v-model="listenerForm.class" clearable>
            <el-option
              v-for="item in executionListenerOptions"
              :key="item.classPath"
              :label="item.name"
              :value="item.classPath"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="listenerForm.listenerType === 'expressionListener'"
          key="listener-expression"
          :rules="{ required: true, trigger: ['blur', 'change'] }"
          label="表达式"
          prop="expression"
        >
          <el-input v-model="listenerForm.expression" clearable />
        </el-form-item>
        <el-form-item
          v-if="listenerForm.listenerType === 'delegateExpressionListener'"
          key="listener-delegate"
          :rules="{ required: true, trigger: ['blur', 'change'] }"
          label="代理表达式"
          prop="delegateExpression"
        >
          <el-input v-model="listenerForm.delegateExpression" clearable />
        </el-form-item>
        <template v-if="listenerForm.listenerType === 'scriptListener'">
          <el-form-item
            key="listener-script-format"
            :rules="{ required: true, trigger: ['blur', 'change'], message: '请填写脚本格式' }"
            label="脚本格式"
            prop="scriptFormat"
          >
            <el-input v-model="listenerForm.scriptFormat" clearable />
          </el-form-item>
          <el-form-item
            key="listener-script-type"
            :rules="{ required: true, trigger: ['blur', 'change'], message: '请选择脚本类型' }"
            label="脚本类型"
            prop="scriptType"
          >
            <el-select v-model="listenerForm.scriptType">
              <el-option label="内联脚本" value="inlineScript" />
              <el-option label="外部脚本" value="externalScript" />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="listenerForm.scriptType === 'inlineScript'"
            key="listener-script"
            :rules="{ required: true, trigger: ['blur', 'change'], message: '请填写脚本内容' }"
            label="脚本内容"
            prop="value"
          >
            <el-input v-model="listenerForm.value" clearable />
          </el-form-item>
          <el-form-item
            v-if="listenerForm.scriptType === 'externalScript'"
            key="listener-resource"
            :rules="{ required: true, trigger: ['blur', 'change'], message: '请填写资源地址' }"
            label="资源地址"
            prop="resource"
          >
            <el-input v-model="listenerForm.resource" clearable />
          </el-form-item>
        </template>
      </el-form>
      <el-divider />
      <p class="listener-filed__title">
        <span
          ><el-icon><Menu /></el-icon>注入字段：</span
        >
        <el-button size="small" type="primary" @click="openListenerFieldForm(null)">添加字段</el-button>
      </p>
      <el-table :data="fieldsListOfListener" border fit max-height="240" size="small" style="flex: none">
        <el-table-column label="序号" type="index" width="50px" />
        <el-table-column label="字段名称" min-width="100px" prop="name" />
        <el-table-column
          :formatter="row => fieldTypeObject[row.fieldType]"
          label="字段类型"
          min-width="80px"
          show-overflow-tooltip
        />
        <el-table-column
          :formatter="row => row.string || row.expression"
          label="字段值/表达式"
          min-width="100px"
          show-overflow-tooltip
        />
        <el-table-column label="操作" width="100px">
          <template v-slot="{ row, $index }">
            <el-button link type="" @click="openListenerFieldForm(row, $index)">编辑</el-button>
            <el-divider direction="vertical" />
            <el-button link style="color: #ff4d4f" type="" @click="removeListenerField(row, $index)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="element-drawer__button">
        <el-button size="small" @click="listenerFormModelVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="saveListenerConfig">保 存</el-button>
      </div>
    </el-drawer>

    <!-- 注入西段 编辑/创建 部分 -->
    <el-dialog v-model="listenerFieldFormModelVisible" append-to-body destroy-on-close title="字段配置" width="600px">
      <el-form
        ref="listenerFieldFormRef"
        :model="listenerFieldForm"
        label-width="96px"
        size="small"
        style="height: 136px"
        @submit.prevent
      >
        <el-form-item :rules="{ required: true, trigger: ['blur', 'change'] }" label="字段名称：" prop="name">
          <el-input v-model="listenerFieldForm.name" clearable />
        </el-form-item>
        <el-form-item :rules="{ required: true, trigger: ['blur', 'change'] }" label="字段类型：" prop="fieldType">
          <el-select v-model="listenerFieldForm.fieldType">
            <el-option v-for="i in Object.keys(fieldTypeObject)" :key="i" :label="fieldTypeObject[i]" :value="i" />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="listenerFieldForm.fieldType === 'string'"
          key="field-string"
          :rules="{ required: true, trigger: ['blur', 'change'] }"
          label="字段值："
          prop="string"
        >
          <el-input v-model="listenerFieldForm.string" clearable />
        </el-form-item>
        <el-form-item
          v-if="listenerFieldForm.fieldType === 'expression'"
          key="field-expression"
          :rules="{ required: true, trigger: ['blur', 'change'] }"
          label="表达式："
          prop="expression"
        >
          <el-input v-model="listenerFieldForm.expression" clearable />
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <el-button size="small" @click="listenerFieldFormModelVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="saveListenerFiled">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import { createListenerObject, updateElementExtensions } from "../../utils";
import { fieldType, initListenerForm, initListenerType, listenerType } from "./utilSelf";
import { Plus } from "@element-plus/icons-vue";
import { listExecutionListener } from "@/api/flowable/model";

export default {
  name: "ElementListeners",
  setup() {
    return { Plus };
  },
  props: {
    id: String,
    type: String
  },
  inject: {
    prefix: "prefix",
    width: "width"
  },
  data() {
    return {
      executionListenerList: [],
      executionListenerOptions: [],
      elementListenersList: [], // 监听器列表
      listenerForm: {}, // 监听器详情表单
      listenerFormModelVisible: false, // 监听器 编辑 侧边栏显示状态
      fieldsListOfListener: [],
      listenerFieldForm: {}, // 监听器 注入字段 详情表单
      listenerFieldFormModelVisible: false, // 监听器 注入字段表单弹窗 显示状态
      editingListenerIndex: -1, // 监听器所在下标，-1 为新增
      editingListenerFieldIndex: -1, // 字段所在下标，-1 为新增
      listenerTypeObject: listenerType,
      fieldTypeObject: fieldType
    };
  },
  watch: {
    id: {
      immediate: true,
      handler(val) {
        val && val.length && this.$nextTick(() => this.resetListenersList());
      }
    }
  },
  methods: {
    resetListenersList() {
      this.bpmnElement = window.bpmnInstances.bpmnElement;
      this.otherExtensionList = [];
      this.bpmnElementListeners =
        this.bpmnElement.businessObject?.extensionElements?.values?.filter(
          ex => ex.$type === `${this.prefix}:ExecutionListener`
        ) ?? [];
      this.elementListenersList = this.bpmnElementListeners.map(listener => initListenerType(listener));
    },
    // 打开 监听器详情 侧边栏
    openListenerForm(listener, index) {
      if (listener) {
        this.listenerForm = initListenerForm(listener);
        this.editingListenerIndex = index;
      } else {
        this.listenerForm = {};
        this.editingListenerIndex = -1; // 标记为新增
      }
      if (listener && listener.fields) {
        this.fieldsListOfListener = listener.fields.map(field => ({
          ...field,
          fieldType: field.string ? "string" : "expression"
        }));
      } else {
        this.fieldsListOfListener = [];
        this.listenerForm["fields"] = [];
      }
      // 打开侧边栏并清楚验证状态
      this.listenerFormModelVisible = true;
      this.$nextTick(() => {
        if (this.$refs["listenerFormRef"]) this.$refs["listenerFormRef"].clearValidate();
      });
    },
    // 打开监听器字段编辑弹窗
    openListenerFieldForm(field, index) {
      this.listenerFieldForm = field ? JSON.parse(JSON.stringify(field)) : {};
      this.editingListenerFieldIndex = field ? index : -1;
      this.listenerFieldFormModelVisible = true;
      this.$nextTick(() => {
        if (this.$refs["listenerFieldFormRef"]) this.$refs["listenerFieldFormRef"].clearValidate();
      });
    },
    // 保存监听器注入字段
    async saveListenerFiled() {
      let validateStatus = await this.$refs["listenerFieldFormRef"].validate();
      if (!validateStatus) return; // 验证不通过直接返回
      if (this.editingListenerFieldIndex === -1) {
        this.fieldsListOfListener.push(this.listenerFieldForm);
        this.listenerForm.fields.push(this.listenerFieldForm);
      } else {
        this.fieldsListOfListener.splice(this.editingListenerFieldIndex, 1, this.listenerFieldForm);
        this.listenerForm.fields.splice(this.editingListenerFieldIndex, 1, this.listenerFieldForm);
      }
      this.listenerFieldFormModelVisible = false;
      this.$nextTick(() => (this.listenerFieldForm = {}));
    },
    // 移除监听器字段
    removeListenerField(field, index) {
      this.$confirm("确认移除该字段吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消"
      })
        .then(() => {
          this.fieldsListOfListener.splice(index, 1);
          this.listenerForm.fields.splice(index, 1);
        })
        .catch(() => console.info("操作取消"));
    },
    // 移除监听器
    removeListener(listener, index) {
      this.$confirm("确认移除该监听器吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消"
      })
        .then(() => {
          this.bpmnElementListeners.splice(index, 1);
          this.elementListenersList.splice(index, 1);
          updateElementExtensions(this.bpmnElement, this.otherExtensionList.concat(this.bpmnElementListeners));
        })
        .catch(() => console.info("操作取消"));
    },
    // 保存监听器配置
    async saveListenerConfig() {
      let validateStatus = await this.$refs["listenerFormRef"].validate();
      if (!validateStatus) return; // 验证不通过直接返回
      const listenerObject = createListenerObject(this.listenerForm, false, this.prefix);
      if (this.editingListenerIndex === -1) {
        this.bpmnElementListeners.push(listenerObject);
        this.elementListenersList.push(this.listenerForm);
      } else {
        this.bpmnElementListeners.splice(this.editingListenerIndex, 1, listenerObject);
        this.elementListenersList.splice(this.editingListenerIndex, 1, this.listenerForm);
      }
      // 保存其他配置
      this.otherExtensionList =
        this.bpmnElement.businessObject?.extensionElements?.values?.filter(
          ex => ex.$type !== `${this.prefix}:ExecutionListener`
        ) ?? [];
      updateElementExtensions(this.bpmnElement, this.otherExtensionList.concat(this.bpmnElementListeners));
      // 4. 隐藏侧边栏
      this.listenerFormModelVisible = false;
      this.listenerForm = {};
    },
    changeEventType(value) {
      console.log(value);
      this.executionListenerOptions = this.executionListenerList.filter(element => {
        return element.typesOfSupport.indexOf(value) > -1;
      });
    }
  },
  created() {
    listExecutionListener().then(res => {
      this.executionListenerList = res.data;
    });
  }
};
</script>
