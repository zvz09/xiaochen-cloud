<template>
  <div class="table-box">
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane label="任务办理" name="approval">
        <el-divider content-position="center">流程表单</el-divider>
        <div v-if="open">
          <el-card v-for="(formInfo, index) in processFormList" :key="index" class="box-card">
            <template #header>
              <div class="card-header">
                <span>{{ formInfo.formName }}</span>
              </div>
            </template>
            <ng-form-build :key="`form-${index}`" :form-template="formInfo.formTemplate" :models="formInfo.models" disabled />
          </el-card>
        </div>

        <div v-if="processTaskFormOpen && processed === 'true'">
          <el-divider content-position="center">当前节点表单</el-divider>
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>{{ processTaskFormInfo.formName }}</span>
              </div>
            </template>
            <ng-form-build
              :key="`processTask-${route.query.taskId}`"
              ref="processTaskForm"
              :form-template="processTaskFormInfo.formTemplate"
              :models="processTaskFormInfo.models"
            />
          </el-card>
        </div>
        <el-card v-if="processed === 'true'" class="box-card">
          <template #header>
            <div class="card-header">
              <span>审批流程</span>
            </div>
          </template>
          <el-row>
            <el-col :offset="2" :span="20">
              <el-form ref="refTaskForm" :model="taskForm" :rules="rules" label-width="120px">
                <el-form-item label="审批意见" prop="comment">
                  <el-input v-model="taskForm.comment" :rows="5" placeholder="请输入 审批意见" type="textarea" />
                </el-form-item>
                <el-form-item label="抄送人" prop="copyUserIds">
                  <el-select v-model="taskForm.copyUserIds" filterable multiple>
                    <el-option v-for="user in users" :key="user.id" :label="user.nickName" :value="user.id" />
                  </el-select>
                </el-form-item>
                <el-form-item label="指定审批人" prop="nextUserIds">
                  <el-select v-model="taskForm.nextUserIds" filterable multiple>
                    <el-option v-for="user in users" :key="user.id" :label="user.nickName" :value="user.id" />
                  </el-select>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
          <el-row :gutter="10" justify="center" type="flex">
            <el-col :span="1.5">
              <el-button icon="CircleCheck" type="success" @click="handleComplete">通过</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button icon="Avatar" type="primary" @click="handleDelegate">委派</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button icon="RefreshRight" type="success" @click="handleTransfer">转办</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button icon="RefreshLeft" type="warning" @click="handleReturn">退回</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button icon="Close" type="danger" @click="handleReject">拒绝</el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="流转记录" name="record">
        <el-timeline style="width: 70%">
          <el-timeline-item
            v-for="(item, index) in historyProcNodeList"
            :key="index"
            :icon="setIcon(item.endTime)"
            :color="setColor(item.endTime)"
          >
            <p style="font-weight: 700">{{ item.activityName }}</p>
            <el-card v-if="item.activityType === 'startEvent'" class="box-card" shadow="hover">
              {{ item.assigneeName }} 在 {{ item.createTime }} 发起流程
            </el-card>
            <el-card v-if="item.activityType === 'userTask'" class="box-card" shadow="hover">
              <el-descriptions :column="5" :label-style="{ 'font-weight': 'bold' }">
                <el-descriptions-item label="实际办理">{{ item.assigneeName || "-" }}</el-descriptions-item>
                <el-descriptions-item label="候选办理">{{ item.candidate || "-" }}</el-descriptions-item>
                <el-descriptions-item label="接收时间">{{ item.createTime || "-" }}</el-descriptions-item>
                <el-descriptions-item label="办结时间">{{ item.endTime || "-" }}</el-descriptions-item>
                <el-descriptions-item label="耗时">{{ item.duration || "-" }}</el-descriptions-item>
              </el-descriptions>
              <div v-if="item.commentList && item.commentList.length > 0">
                <div v-for="(comment, i) in item.commentList" :key="i">
                  <el-divider content-position="left">
                    <el-tag :type="approveTypeTag(comment.type)" size="small">{{ commentType(comment.type) }}</el-tag>
                    <el-tag type="info" effect="plain" size="small">{{ comment.time }}</el-tag>
                  </el-divider>
                  <span>{{ comment.fullMessage }}</span>
                </div>
              </div>
            </el-card>
            <el-card v-if="item.activityType === 'endEvent'" class="box-card" shadow="hover">
              {{ item.createTime }} 结束流程
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-tab-pane>
      <el-tab-pane label="流程跟踪" name="track">
        <el-card v-if="open" class="box-card">
          <ProcessViewer
            :key="`processViewer-${new Date().getTime()}`"
            :all-comment-list="historyProcNodeList"
            :bpmn-xml="previewBpmnXml"
            :finished-info="finishedInfo"
          />
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogForm.visible" :title="dialogForm.title">
      <el-form>
        <el-form-item v-show="dialogForm.type === 'delegate'" label="被委派人:">
          <el-select v-model="taskForm.userId">
            <el-option v-for="user in users" :key="user.id" :label="user.nickName" :value="user.id" />
          </el-select>
        </el-form-item>
        <el-form-item v-show="dialogForm.type === 'transfer'" label="转办:">
          <el-select v-model="taskForm.userId">
            <el-option v-for="user in users" :key="user.id" :label="user.nickName" :value="user.id" />
          </el-select>
        </el-form-item>
        <el-form-item v-show="dialogForm.type === 'return'" label="退回节点:" prop="targetKey">
          <el-radio-group v-model="taskForm.targetKey">
            <el-radio-button v-for="item in returnTaskList" :key="item.id" :label="item.id">{{ item.name }} </el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogForm.visible = false">取消</el-button>
          <el-button type="primary" @click="dialogFormSubmit">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="tsx" name="processDetail">
import { ref } from "vue";
import { useRoute } from "vue-router";
import { detailProcess } from "@/api/flowable/process";
import { taskComplete, delegateTask, rejectTask, returnTask, transferTask, findReturnTaskList } from "@/api/flowable/task";
import { getUserList } from "@/api/system/user";
import ProcessViewer from "@/components/ProcessViewer/index.vue";
import { ElMessage, ElMessageBox, FormInstance } from "element-plus";
import { Process } from "@/api/flowable/process/types";
import { User } from "@/api/system/user/types";
import { Task } from "@/api/flowable/task/types";
import { closeThisPage } from "@/utils/closeThisPage";

const route = useRoute();
const users = ref<User.UserVO[]>([]);
const activeName = ref("approval");
const processed = ref(route.query.processed);
const processFormList = ref<Process.ProcessFormList[]>([]);
const open = ref(false);
const historyProcNodeList = ref<Process.HistoryProcNodeList[]>([]);
const previewBpmnXml = ref();
const finishedInfo = ref<Process.FlowViewer>({
  finishedSequenceFlowSet: [],
  finishedTaskSet: [],
  unfinishedTaskSet: [],
  rejectedTaskSet: []
});
const processTaskFormOpen = ref(false);
const processTaskFormInfo = ref({
  formName: "",
  formTemplate: null,
  models: {}
});
const processTaskForm = ref();

const taskForm = ref<Task.FlowableTaskDto>({
  userId: "",
  comment: "", // 意见内容
  procInsId: "", // 流程实例编号
  taskId: "", // 流程任务编号
  copyUserIds: [], // 抄送人Id
  nextUserIds: [],
  targetKey: ""
});
const dialogForm = ref({
  visible: false,
  type: "",
  title: ""
});

const returnTaskList = ref<Task.FlowElement[]>([]); // 回退列表数据

const refTaskForm = ref<FormInstance>();
const rules = ref({
  comment: [{ required: true, message: "请输入审批意见", trigger: "blur" }]
});

function initData() {
  taskForm.value.procInsId = route.params && (route.params.procInsId as string);
  taskForm.value.taskId = route.query && (route.query.taskId as string);
  // 流程任务重获取变量表单
  getProcessDetails(taskForm.value.procInsId, taskForm.value.taskId);
  getUsers();
}

initData();

function getProcessDetails(procInsId: string, taskId: string) {
  detailProcess(procInsId, taskId).then(res => {
    console.log(res);
    if (res.code == 200) {
      res.data.processFormList.forEach(processForm => {
        processFormList.value.push({
          formName: processForm.formName,
          formTemplate: JSON.parse(processForm.formTemplate),
          models: JSON.parse(processForm.models)
        });
      });

      if (res.data.existTaskForm) {
        processTaskFormInfo.value.formName = res.data.taskFormData.formName;
        processTaskFormInfo.value.formTemplate = JSON.parse(res.data.taskFormData.formTemplate);
        console.log(processTaskFormInfo.value.formName);
      }

      historyProcNodeList.value = res.data.historyProcNodeList;
      previewBpmnXml.value = res.data.bpmnXml;
      finishedInfo.value = res.data.flowViewer;

      processTaskFormOpen.value = res.data.existTaskForm;
      open.value = true;
    }
  });
}

function getUsers() {
  getUserList({ pageNum: 1, pageSize: 999 }).then(res => {
    users.value = res.data.records;
  });
}

function setIcon(val: any) {
  if (val) {
    return "Check";
  } else {
    return "Timer";
  }
}

function setColor(val: any) {
  if (val) {
    return "#2bc418";
  } else {
    return "#b3bdbb";
  }
}

/** 通过任务 */
function handleComplete() {
  // 若无任务表单，则 taskFormPromise 为 true，即不需要校验
  const taskFormPromise = processTaskFormOpen.value ? processTaskForm.value!.validate() : true;
  const approvalPromise = refTaskForm.value!.validate();

  Promise.all([taskFormPromise, approvalPromise]).then(() => {
    if (processTaskFormOpen.value) {
      taskForm.value.variables = processTaskForm.value.getData();
    }
    taskComplete(taskForm.value).then(response => {
      if (response.code == 200) {
        ElMessage({
          type: "success",
          message: "办理成功!"
        });
        closeThisPage(route);
      }
    });
  });
}

/** 拒绝任务 */
function handleReject() {
  refTaskForm.value!.validate(valid => {
    if (valid) {
      ElMessageBox.confirm("拒绝审批单流程会终止，是否继续？", "提示", {
        confirmButtonText: "是",
        cancelButtonText: "否",
        type: "warning"
      }).then(async () => {
        rejectTask(taskForm.value).then(response => {
          if (response.code == 200) {
            ElMessage({
              type: "success",
              message: "办理成功!"
            });
            closeThisPage(route);
          }
        });
      });
    }
  });
}

/** 委派任务 */
function handleDelegate() {
  refTaskForm.value!.validate(valid => {
    if (valid) {
      dialogForm.value.type = "delegate";
      dialogForm.value.title = "委派任务";
      dialogForm.value.visible = true;
    }
  });
}

/** 转办任务 */
function handleTransfer() {
  refTaskForm.value!.validate(valid => {
    if (valid) {
      dialogForm.value.type = "transfer";
      dialogForm.value.title = "转办任务";
      dialogForm.value.visible = true;
    }
  });
}

/** 退回任务 */
function handleReturn() {
  refTaskForm.value!.validate(valid => {
    if (valid) {
      findReturnTaskList(taskForm.value).then(res => {
        if (res.code == 200) {
          if (res.data.length == 0) {
            ElMessage({
              message: "该任务没有可退回节点",
              type: "warning"
            });
            return;
          }
          returnTaskList.value = res.data;
          dialogForm.value.type = "return";
          dialogForm.value.title = "退回流程";
          dialogForm.value.visible = true;
        }
      });
    }
  });
}

function dialogFormSubmit() {
  if (taskForm.value.userId == "" && dialogForm.value.type != "return") {
    ElMessage({
      message: "请选择用户",
      type: "warning"
    });
    return;
  }
  if (dialogForm.value.type === "delegate") {
    delegateTask(taskForm.value).then(res => {
      if (res.code == 200) {
        ElMessage({
          type: "success",
          message: "办理成功!"
        });
        closeThisPage(route);
      }
    });
  } else if (dialogForm.value.type === "transfer") {
    transferTask(taskForm.value).then(res => {
      if (res.code == 200) {
        ElMessage({
          type: "success",
          message: "办理成功!"
        });
        closeThisPage(route);
      }
    });
  } else if (dialogForm.value.type == "return") {
    returnTask(taskForm.value).then(res => {
      if (res.code == 200) {
        ElMessage({
          type: "success",
          message: "办理成功!"
        });
        closeThisPage(route);
      }
    });
  }
}

function approveTypeTag(val: string) {
  switch (val) {
    case "1":
      return "success";
    case "2":
      return "warning";
    case "3":
      return "danger";
    case "4":
      return "";
    case "5":
      return "success";
    case "6":
      return "danger";
    case "7":
      return "info";
  }
}

function commentType(val: string) {
  switch (val) {
    case "1":
      return "通过";
    case "2":
      return "退回";
    case "3":
      return "驳回";
    case "4":
      return "委派";
    case "5":
      return "转办";
    case "6":
      return "终止";
    case "7":
      return "撤回";
  }
}
</script>
