<template>
  <div id="bpmnProcess">
    <my-process-palette/>
    <my-process-designer
        :key="`designer-${reloadIndex}`"
        ref="processDesigner"
        :modelValue = "props.xmlString"
        :options="{
        taskResizingEnabled: true,
        eventResizingEnabled: true,
        minimap: {
          open: true
        }
      }"
        :process-id="controlForm.processId"
        :process-name="controlForm.processName"
        keyboard
        v-bind="controlForm"
        @save="getProcessXML"
        @element-click="elementClick"
        @element-contextmenu="elementContextmenu"
        @init-finished="initModeler"
    />
    <my-properties-panel :key="`penal-${reloadIndex}`" :bpmn-modeler="modeler" :prefix="controlForm.prefix"
                         class="process-panel"/>

    <el-dialog
        v-model="dialogVisible"
        title="是否按新版本保存"
        width="30%"
    >
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="saveAsNewVersion(false)">否</el-button>
        <el-button type="primary" @click="saveAsNewVersion(true)">是</el-button>
      </span>
      </template>
    </el-dialog>
  </div>


</template>
<script>
export default {
  name: 'ProcessDesigner'
}
</script>
<script setup>
import {onMounted, ref} from 'vue'
import CustomContentPadProvider from "@/components/bpmn-vue3/designer/plugins/content-pad";
import CustomPaletteProvider from "@/components/bpmn-vue3/designer/plugins/palette";
import minimapModule from "diagram-js-minimap";
import Log from "@/components/bpmn-vue3/Log";

import 'bpmn-js/dist/assets/diagram-js.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';

const props = defineProps({
  processId: {
    default: function () {
      return `Process_${new Date().getTime()}`
    },
    type: String
  },
  processName: {
    default: function () {
      return `业务流程_${new Date().getTime()}`
    },
    type: String
  },
  xmlString: String
})

const reloadIndex = new Date().getTime()
const modeler = ref()
const controlDrawerVisible = ref(false)
const controlForm = ref({
  processId: props.processId,
  processName: props.processName,
  simulation: true,
  labelEditing: false,
  labelVisible: false,
  prefix: 'flowable',
  headerButtonSize: 'small',
  events: ['element.click', 'element.contextmenu'],
  // additionalModel: []
  additionalModel: [CustomContentPadProvider, CustomPaletteProvider, minimapModule]
})
const emit = defineEmits(['save'])
const initModeler = (m) => {
  setTimeout(() => {
    modeler.value = m;
    const canvas = m.get('canvas');
    const rootElement = canvas.getRootElement();
    Log.prettyPrimary('Process Id:', rootElement.id);
    Log.prettyPrimary('Process Name:', rootElement.businessObject.name);
  }, 10);
}
const elementClick = (element) => {
  console.log(element);
}
const elementContextmenu = (element) => {
  console.log('elementContextmenu:', element);
}

const dialogVisible = ref(false)
const processXML = ref('')
const getProcessXML = (xml) => {
  dialogVisible.value = true
  processXML.value = xml
  //emit('save', xml)
}
const saveAsNewVersion = (newVersion) => {
  dialogVisible.value = false
  emit('save', processXML.value, newVersion)
}
onMounted(async () => {
  console.log("onMounted")
})
</script>
<style lang="scss">
body {
  overflow: hidden;
  margin: 0;
  box-sizing: border-box;
}

#bpmnProcess {
  width: 100%;
  height: 900px;
  box-sizing: border-box;
  display: inline-grid;
  grid-template-columns: 100px auto max-content;
}

.demo-info-bar {
  position: fixed;
  right: 8px;
  bottom: 108px;
  z-index: 1;
}

.demo-control-bar {
  position: fixed;
  right: 8px;
  bottom: 48px;
  z-index: 1;
}

.open-model-button {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  font-size: 32px;
  background: rgba(64, 158, 255, 1);
  color: #ffffff;
  cursor: pointer;
}

.zoom-in-right-enter-active,
.zoom-in-right-leave-active {
  opacity: 1;
  transform: scaleY(1) translateY(-48px);
  transition: all 300ms cubic-bezier(0.23, 1, 0.32, 1);
  transform-origin: right center;
}

.zoom-in-right-enter,
.zoom-in-right-leave-active {
  opacity: 0;
  transform: scaleX(0) translateY(-48px);
}

.info-tip {
  position: absolute;
  width: 480px;
  top: 0;
  right: 64px;
  z-index: 10;
  box-sizing: border-box;
  padding: 0 16px;
  color: #333333;
  background: #f2f6fc;
  transform: translateY(-48px);
  border: 1px solid #ebeef5;
  border-radius: 4px;

  &::before,
  &::after {
    content: "";
    width: 0;
    height: 0;
    border-width: 8px;
    border-style: solid;
    position: absolute;
    right: -15px;
    top: 50%;
  }

  &::before {
    border-color: transparent transparent transparent #f2f6fc;
    z-index: 10;
  }

  &::after {
    right: -16px;
    border-color: transparent transparent transparent #ebeef5;
    z-index: 1;
  }
}

.control-form {
  .el-radio {
    width: 100%;
    line-height: 32px;
  }
}

.element-overlays {
  box-sizing: border-box;
  padding: 8px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 4px;
  color: #fafafa;
}

body,
body * {
  /* 滚动条 */
  &::-webkit-scrollbar-track-piece {
    background-color: #fff; /*滚动条的背景颜色*/
    -webkit-border-radius: 0; /*滚动条的圆角宽度*/
  }

  &::-webkit-scrollbar {
    width: 10px; /*滚动条的宽度*/
    height: 8px; /*滚动条的高度*/
  }

  &::-webkit-scrollbar-thumb:vertical {
    /*垂直滚动条的样式*/
    height: 50px;
    background-color: rgba(153, 153, 153, 0.5);
    -webkit-border-radius: 4px;
    outline: 2px solid #fff;
    outline-offset: -2px;
    border: 2px solid #fff;
  }

  &::-webkit-scrollbar-thumb {
    /*滚动条的hover样式*/
    background-color: rgba(159, 159, 159, 0.3);
    -webkit-border-radius: 4px;
  }

  &::-webkit-scrollbar-thumb:hover {
    /*滚动条的hover样式*/
    background-color: rgba(159, 159, 159, 0.5);
    -webkit-border-radius: 4px;
  }
}
</style>