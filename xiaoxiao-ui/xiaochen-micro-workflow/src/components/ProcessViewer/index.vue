<template>
  <div class="process-viewer">
    <div v-show="!isLoading" ref="bpmnViewerCanvas" class="process-canvas" style="height: 70vh"></div>

    <!-- 自定义箭头样式，用于成功状态下流程连线箭头 -->
    <defs ref="customSuccessDefs">
      <marker
        id="sequenceflow-end-white-success"
        markerHeight="10"
        markerWidth="10"
        orient="auto"
        refX="11"
        refY="10"
        viewBox="0 0 20 20"
      >
        <path
          class="success-arrow"
          d="M 1 5 L 11 10 L 1 15 Z"
          style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1"
        ></path>
      </marker>
      <marker
        id="conditional-flow-marker-white-success"
        markerHeight="10"
        markerWidth="10"
        orient="auto"
        refX="-1"
        refY="10"
        viewBox="0 0 20 20"
      >
        <path
          class="success-conditional"
          d="M 0 10 L 8 6 L 16 10 L 8 14 Z"
          style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1"
        ></path>
      </marker>
    </defs>
    <!-- 自定义箭头样式，用于失败状态下流程连线箭头 -->
    <defs ref="customFailDefs">
      <marker
        id="sequenceflow-end-white-fail"
        markerHeight="10"
        markerWidth="10"
        orient="auto"
        refX="11"
        refY="10"
        viewBox="0 0 20 20"
      >
        <path
          class="fail-arrow"
          d="M 1 5 L 11 10 L 1 15 Z"
          style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1"
        ></path>
      </marker>
      <marker
        id="conditional-flow-marker-white-fail"
        markerHeight="10"
        markerWidth="10"
        orient="auto"
        refX="-1"
        refY="10"
        viewBox="0 0 20 20"
      >
        <path
          class="fail-conditional"
          d="M 0 10 L 8 6 L 16 10 L 8 14 Z"
          style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1"
        ></path>
      </marker>
    </defs>
  </div>
</template>

<script>
export default {
  name: "ProcessViewer"
};
</script>
<script setup>
import { ref, onMounted } from "vue";
import Viewer from "bpmn-js/lib/Viewer";

const props = defineProps({
  bpmnXml: String,
  finishedInfo: Object,
  // 所有节点审批记录
  allCommentList: Object
});
const isLoading = ref(false);
const bpmnViewer = ref(null);
const bpmnViewerCanvas = ref(null);
const customSuccessDefs = ref(null);
const customFailDefs = ref(null);

async function createNewDiagram() {
  try {
    clearViewer();
    bpmnViewer.value = new Viewer({
      container: bpmnViewerCanvas.value
    });
    initModelListeners();
    if (props.bpmnXml) {
      isLoading.value = true;
      const result = await bpmnViewer.value.importXML(props.bpmnXml);
      addCustomDefs();
      const { warnings } = result;
    }
  } catch (err) {
    console.error(err.message, err.warnings);
  } finally {
    isLoading.value = false;
    setProcessStatus(props.finishedInfo);
  }
}

function initModelListeners() {
  // 注册需要的监听事件
  bpmnViewer.value.on("element.hover", function (eventObj) {
    let element = eventObj ? eventObj.element : null;
    elementHover(element);
  });
  bpmnViewer.value.on("element.out", function (eventObj) {
    let element = eventObj ? eventObj.element : null;
    elementOut(element);
  });
}

function elementHover(element) {
  if (element === null) {
    return;
  }
  const overlays = bpmnViewer.value.get("overlays");
  let { finishedTaskSet } = processNodeInfo.value;
  if (element.type !== "bpmn:Process" && finishedTaskSet.indexOf(element.id) > -1) {
    let html = ``; // 默认值
    if (element.type === "bpmn:StartEvent") {
      props.allCommentList.forEach(comment => {
        if (comment.activityId === element.id) {
          html += `<p>发起人：${comment.assigneeName}</p>
                  <p>创建时间：${comment.createTime}</p>`;
        }
      });
    } else if (element.type === "bpmn:UserTask") {
      props.allCommentList.forEach(comment => {
        if (comment.activityId === element.id && comment.commentList[0]) {
          html += `<p>审批人：${comment.assigneeName}</p>
                  <p>审批意见：${comment.commentList && comment.commentList[0] ? comment.commentList[0].fullMessage : ""}</p>
                  <p>审批时间：${comment.endTime}</p>
                  <p>-----------------------------------------</p>`;
        }
      });
    } else if (element.type === "bpmn:EndEvent") {
      props.allCommentList.forEach(comment => {
        if (comment.activityId === element.id) {
          html = `<p>结束时间：${comment.endTime}</p>`;
        }
      });
    }
    overlays.add(element, {
      position: { left: 0, bottom: 0 },
      html: `<div class="element-overlays">${html} </div>`
    });
  }
}

function elementOut(element) {
  if (element === null) {
    return;
  }
  const overlays = bpmnViewer.value.get("overlays");
  overlays.remove({ element });
}

function addCustomDefs() {
  const canvas = bpmnViewer.value.get("canvas");
  const svg = canvas._svg;
  svg.appendChild(customSuccessDefs);
  svg.appendChild(customFailDefs);
}

// 流程图预览清空
function clearViewer() {
  if (bpmnViewerCanvas.value) {
    bpmnViewerCanvas.value.innerHTML = "";
  }
  if (bpmnViewer.value) {
    bpmnViewer.value.destroy();
  }
  bpmnViewer.value = null;
}

const processNodeInfo = ref();

// 设置流程图元素状态
function setProcessStatus(nodeInfo) {
  processNodeInfo.value = nodeInfo;
  if (isLoading.value || processNodeInfo.value == null || bpmnViewer.value == null) return;
  let { finishedTaskSet, rejectedTaskSet, unfinishedTaskSet, finishedSequenceFlowSet } = processNodeInfo.value;
  const canvas = bpmnViewer.value.get("canvas");
  const elementRegistry = bpmnViewer.value.get("elementRegistry");
  if (Array.isArray(finishedSequenceFlowSet)) {
    finishedSequenceFlowSet.forEach(item => {
      if (item != null) {
        canvas.addMarker(item, "highlight");
        let element = elementRegistry.get(item);
        const conditionExpression = element.businessObject.conditionExpression;
        if (conditionExpression) {
          canvas.addMarker(item, "condition-expression");
        }
      }
    });
  }
  if (Array.isArray(finishedTaskSet)) {
    finishedTaskSet.forEach(item => canvas.addMarker(item, "highlight"));
  }
  if (Array.isArray(unfinishedTaskSet)) {
    unfinishedTaskSet.forEach(item => canvas.addMarker(item, "primary"));
  }
  if (Array.isArray(rejectedTaskSet)) {
    rejectedTaskSet.forEach(item => {
      if (item != null) {
        let element = elementRegistry.get(item);
        if (element.type.includes("Task")) {
          canvas.addMarker(item, "danger");
        } else {
          canvas.addMarker(item, "warning");
        }
      }
    });
  }
}

onMounted(async () => {
  await createNewDiagram();
});
</script>

<style>
.flowMsgPopover {
  display: none;
}

.highlight:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: rgba(251, 233, 209, 1) !important; /* color elements as green */
}

.highlight g.djs-visual > :nth-child(1) {
  stroke: rgba(0, 190, 0, 1) !important;
}

.highlight-line g.djs-visual > :nth-child(1) {
  stroke: rgb(236, 11, 8) !important;
}

.element-overlays {
  position: relative;
  box-sizing: border-box;
  padding: 8px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 4px;
  color: #fafafa;
  width: 320px;
}
</style>
