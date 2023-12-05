<template>
  <div class="table-box">
    <ProTable ref="proTable" :columns="columns" :request-api="getTableList" :init-param="initParam" :data-callback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button v-auth="'add'" type="primary" :icon="CirclePlus" @click="addTaskInfo()">新增定时任务</el-button>
      </template>

      <template #schedule="scope"> {{ scope.row.scheduleType.toUpperCase() }}: {{ scope.row.scheduleConf }} </template>
      <template #executor="scope"> {{ scope.row.glueType.toUpperCase() }}: {{ scope.row.executorHandler }} </template>
      <!-- 菜单操作 -->
      <template #operation="scope">
        <el-dropdown :hide-on-click="false">
          <el-button type="primary" link :icon="Setting">操作</el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>
                <el-button v-auth="'run'" type="primary" link @click="runTask(scope.row)">执行一次</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="primary" link @click="openLogDrawer(scope.row)">查看日志</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="primary" link @click="showAddress(scope.row)"> 注册节点 </el-button>
              </el-dropdown-item>
              <el-dropdown-item divided>
                <el-button v-auth="'edit'" type="primary" link @click="updateTaskInfo(scope.row)">编辑</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button v-auth="'delete'" type="primary" link @click="deleteTask(scope.row)">删除</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </ProTable>
    <el-dialog v-model="addressDialogVisible" title="任务执行机器列表">
      <ul>
        <li v-for="address in addressData" :key="address">
          {{ address }}
        </li>
      </ul>
      <span v-show="addressData.length === 0">无</span>
    </el-dialog>

    <el-dialog v-model="taskInfoVisible" :title="taskInfoTitle">
      <el-form ref="taskInfoFormRef" label-width="140px" label-suffix=" :" :rules="rules" :model="taskInfoFormData">
        <el-divider content-position="left">基础配置</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="微服务名" prop="jobGroup">
              <el-input v-model="taskInfoFormData.jobGroup" placeholder="请填写微服务名" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="author">
              <el-input v-model="taskInfoFormData.author" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="任务描述" prop="jobDesc">
          <el-input type="textarea" v-model="taskInfoFormData.jobDesc" placeholder="请填写任务描述" clearable></el-input>
        </el-form-item>
        <el-divider content-position="left">调度配置</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="调度类型" prop="scheduleType">
              <el-select v-model="taskInfoFormData.scheduleType" placeholder="调度类型" clearable>
                <el-option v-for="item in scheduleTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col v-show="taskInfoFormData.scheduleType === 'Cron'" :span="12">
            <el-form-item label="Cron" prop="scheduleConf">
              <!--              <el-input v-model="taskInfoFormData.scheduleConf" clearable></el-input>-->
              <el-input v-model="taskInfoFormData.scheduleConf" placeholder="cron表达式...">
                <template #append>
                  <el-popover v-model:visible="cronState.cronPopover" width="700px">
                    <noVue3Cron
                      :cron-value="taskInfoFormData.scheduleConf"
                      @change="changeCron"
                      @close="cronState.cronPopover = false"
                      max-height="400px"
                      i18n="cn"
                    ></noVue3Cron>
                    <template #reference>
                      <el-button @click="cronState.cronPopover = !cronState.cronPopover">设置</el-button>
                    </template>
                  </el-popover>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col v-show="taskInfoFormData.scheduleType === '固定速度'" :span="12">
            <el-form-item label="固定速度" prop="scheduleConf">
              <el-input v-model="taskInfoFormData.scheduleConf" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">任务配置</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="运行模式" prop="glueType">
              <el-select v-model="taskInfoFormData.glueType" placeholder="调度类型" clearable>
                <el-option v-for="item in glueTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="JobHandler" prop="executorHandler">
              <el-input v-model="taskInfoFormData.executorHandler" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="任务参数" prop="executorParam">
          <el-input type="textarea" v-model="taskInfoFormData.executorParam" placeholder="任务参数" clearable></el-input>
        </el-form-item>
        <el-divider content-position="left">高级配置</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="路由策略" prop="executorRouteStrategy">
              <el-select v-model="taskInfoFormData.executorRouteStrategy" placeholder="调度类型" clearable>
                <el-option v-for="item in executorRouteStrategies" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调度过期策略" prop="misfireStrategy">
              <el-select v-model="taskInfoFormData.misfireStrategy" placeholder="调度类型" clearable>
                <el-option v-for="item in misfireStrategies" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeDialog">取 消</el-button>
          <el-button type="primary" @click="enterDialog">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <TaskLogDrawer ref="drawerRef" />
  </div>
</template>

<script setup lang="tsx" name="timingTask">
import { reactive, ref } from "vue";
import { noVue3Cron } from "no-vue3-cron";
import "no-vue3-cron/lib/noVue3Cron.css"; // 引入样式
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps, ProTableInstance } from "@/components/ProTable/interface";
import { listJobInfoPage, updateJobInfo, createJobInfo, changeStatus, runJobInfo, deleteJobInfo } from "@/api/system/task";
import { ResPage } from "@/api/interface";
import { Task } from "@/api/system/task/types";
import { CirclePlus, Setting } from "@element-plus/icons-vue";
import { ElMessage, FormInstance, FormRules } from "element-plus";
import { useAuthButtons } from "@/hooks/useAuthButtons";
import { useHandleData } from "@/hooks/useHandleData";
import TaskLogDrawer from "@/views/system/timingTask/TaskLogDrawer.vue";

// ProTable 实例
const proTable = ref<ProTableInstance>();

const initParam = reactive({ id: "", traceId: "" });

const dataCallback = (data: ResPage<Task.JobInfoVo>) => {
  return {
    list: data.records,
    total: data.total,
    pageNum: data.current,
    pageSize: data.size
  };
};

const getTableList = (params: any) => {
  return listJobInfoPage(params);
};
// 页面按钮权限（按钮权限既可以使用 hooks，也可以直接使用 v-auth 指令，指令适合直接绑定在按钮上，hooks 适合根据按钮权限显示不同的内容）
const { BUTTONS } = useAuthButtons();

// 表格配置项
const columns = reactive<ColumnProps<Task.JobInfoVo>[]>([
  {
    prop: "jobGroup",
    label: "微服务名"
  },
  {
    prop: "jobDesc",
    label: "任务描述",
    width: 260
  },
  {
    prop: "schedule",
    label: "调度类型",
    width: 260
  },
  {
    prop: "executor",
    label: "运行模式",
    width: 260
  },
  {
    prop: "author",
    label: "负责人"
  },
  {
    prop: "triggerStatus",
    label: "状态",
    width: 200,
    render: scope => {
      return (
        <>
          {BUTTONS.value.taskStatus ? (
            <el-switch
              model-value={scope.row.triggerStatus}
              active-text={scope.row.triggerStatus ? "运行" : "停止"}
              onClick={() => changeTaskStatus(scope.row)}
            />
          ) : (
            <el-tag type={scope.row.triggerStatus ? "success" : "danger"}>{scope.row.triggerStatus ? "运行" : "停止"}</el-tag>
          )}
        </>
      );
    }
  },
  { prop: "operation", label: "操作", fixed: "right", width: 200 }
]);

const changeTaskStatus = async (row: Task.JobInfoVo) => {
  await useHandleData(changeStatus, row.id, `切换【${row.jobDesc}】定时任务状态`);
  proTable.value?.getTableList();
};

const deleteTask = async (row: Task.JobInfoVo) => {
  await useHandleData(deleteJobInfo, row.id, `删除【${row.jobDesc}】定时任务`);
  proTable.value?.getTableList();
};

const runTask = async (row: Task.JobInfoVo) => {
  await useHandleData(runJobInfo, row.id, `运行一次【${row.jobDesc}】定时任务`);
  proTable.value?.getTableList();
};

const addressDialogVisible = ref(false);
const addressData = ref<string[]>([]);
const showAddress = (jobInfoVo: Task.JobInfoVo) => {
  addressData.value = jobInfoVo.executorAddress;
  addressDialogVisible.value = true;
};

const taskInfoFormRef = ref<FormInstance>();
const taskInfoVisible = ref(false);
const taskInfoTitle = ref("新增定时任务");
const taskInfoFormData = ref<Task.JobInfoParams>({});

const rules = reactive<FormRules>({
  jobGroup: [{ required: true, message: "请填写微服务名", trigger: "blur" }],
  jobDesc: [{ required: true, message: "请填写任务描述", trigger: "blur" }],
  scheduleType: [{ required: true, message: "请选择调度类型", trigger: "blur" }],
  scheduleConf: [{ required: true, message: "请输入调度配置", trigger: "blur" }],
  glueType: [{ required: true, message: "请选择调度类型", trigger: "blur" }],
  executorHandler: [{ required: true, message: "请输入JobHandler", trigger: "blur" }]
});

const scheduleTypes = [
  { label: "Cron", value: "Cron" },
  { label: "固定速度", value: "固定速度" }
];
const glueTypes = [{ label: "BEAN", value: "BEAN" }];
const executorRouteStrategies = [{ label: "第一个", value: "FIRST" }];
const misfireStrategies = [{ label: "忽略", value: "DO_NOTHING" }];

const cronState = reactive({
  cronPopover: false
});
const changeCron = (val: any) => {
  if (typeof val !== "string") return false;
  taskInfoFormData.value.scheduleConf = val;
};

const addTaskInfo = () => {
  taskInfoTitle.value = "新增定时任务";
  taskInfoFormData.value = { scheduleType: "Cron" };
  taskInfoVisible.value = true;
};
const updateTaskInfo = (jobInfoVo: Task.JobInfoVo) => {
  taskInfoTitle.value = "修改定时任务";
  taskInfoFormData.value = { ...jobInfoVo };
  taskInfoVisible.value = true;
};

//关闭弹窗
const closeDialog = () => {
  taskInfoVisible.value = false;
};
//弹窗提交
const enterDialog = async () => {
  taskInfoFormRef.value?.validate(async valid => {
    if (valid) {
      if (taskInfoFormData.value.id) {
        await updateJobInfo(taskInfoFormData.value);
      } else {
        await createJobInfo(taskInfoFormData.value);
      }
      ElMessage.success(taskInfoFormData.value.id ? "编辑成功" : "添加成功!");
      taskInfoVisible.value = false;
      proTable.value?.getTableList();
    }
  });
};

// 打开 drawer(新增、查看、编辑)
const drawerRef = ref<InstanceType<typeof TaskLogDrawer> | null>(null);
const openLogDrawer = (jobInfoVo: Task.JobInfoVo) => {
  console.log("xxxxxxxxxxxx" + drawerRef.value);
  const params = {
    title: jobInfoVo.jobDesc,
    taskId: jobInfoVo.id
  };
  drawerRef.value?.acceptParams(params);
};
</script>
