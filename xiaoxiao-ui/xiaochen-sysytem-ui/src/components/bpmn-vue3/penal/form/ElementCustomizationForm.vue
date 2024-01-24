<template>
  <div class="panel-tab__content">
    <el-form label-width="80px" size="small" @submit.prevent>
      <el-form-item label="自定义表单">
        <el-select v-model="formId" value-key="formName" @change="updateForm">
          <el-option v-for="form in formList" :key="form.id" :label="form.formName" :value="form.id" />
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { Plus } from "@element-plus/icons-vue";
import { listForm } from "@/api/flowable/form";

export default {
  name: "ElementCustomizationForm",
  setup() {
    return {
      Plus
    };
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
      formId: "",
      formName: "",
      formList: []
    };
  },
  watch: {
    id: {
      immediate: true,
      handler(val) {
        val && val.length && this.$nextTick(() => this.resetFormList());
      }
    }
  },

  methods: {
    getFormList() {
      listForm({
        page: 1,
        pageSize: 99999
      }).then(res => {
        if (res.code === 200) {
          this.formList = res.data;
        }
        if (this.formId) {
          this.formList.forEach(form => {
            if (form.id === this.formId) {
              this.formName = form.formName;
            }
          });
        }
      });
    },

    resetFormList() {
      this.getFormList();
      this.bpmnELement = window.bpmnInstances.bpmnElement;
      this.formId = this.bpmnELement.businessObject.get("flowable:customizationFormKey");
    },

    updateForm() {
      window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { "flowable:customizationFormKey": this.formId });
    }
  },
  created() {
    this.getFormList();
  }
};
</script>
