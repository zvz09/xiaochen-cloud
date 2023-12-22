<template>
  <div style="margin-top: 16px">
    <el-form-item label="受理人类型">
      <el-radio-group v-model="dataType" @change="changeDataType">
        <el-radio label="USER">指定用户</el-radio>
        <el-radio label="USERS">候选用户</el-radio>
        <el-radio label="AUTHORITY">角色</el-radio>
        <el-radio label="DEPTS">部门</el-radio>
        <el-radio label="INITIATOR">发起人</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item v-if="dataType === 'USER'" label="指定用户">
      <el-select v-model="userTaskForm.assignee" @change="updateElementTaskAssignee()">
        <el-option v-for="user in users" :key="user.id" :label="user.nickName" :value="user.id"/>
      </el-select>
    </el-form-item>
    <el-form-item v-if="dataType === 'USERS'" label="候选用户">
      <el-select v-model="userTaskForm.candidateUsers" collapse-tags multiple
                 @change="updateElementTaskCandidateUsers('candidateUsers')">
        <el-option v-for="user in users" :key="user.id" :label="user.nickName" :value="user.id"/>
      </el-select>
    </el-form-item>
    <el-form-item v-if="dataType === 'AUTHORITY'" label="角色">
      <el-tree-select
          v-model="userTaskForm.candidateGroups"
          :data="authorityTree"
          :props="{label: 'authorityName'}"
          check-strictly
          multiple
          node-key="authorityCode"
          @change="updateElementTaskCandidateGroups"
      />
    </el-form-item>
    <el-form-item v-if="dataType === 'DEPTS'" label="部门">
      <el-tree-select
          v-model="userTaskForm.candidateDepts"
          :data="deptTree"
          :props="{label: 'deptName'}"
          check-strictly
          multiple
          node-key="id"
          @change="updateElementTaskCandidateDepts"
      />
    </el-form-item>
    <el-form-item label="到期时间">
      <el-input v-model="userTaskForm.dueDate" clearable @change="updateElementTask('dueDate')"/>
    </el-form-item>
    <el-form-item label="跟踪时间">
      <el-input v-model="userTaskForm.followUpDate" clearable @change="updateElementTask('followUpDate')"/>
    </el-form-item>
    <el-form-item label="优先级">
      <el-input v-model="userTaskForm.priority" clearable @change="updateElementTask('priority')"/>
    </el-form-item>
  </div>
</template>

<script>
import {getAllUser} from '@/api/user'
import {getAuthorityTree} from '@/api/authority'
import {getDepartmentTree} from '@/api/sysDepartment'

export default {
  name: "UserTask",
  props: {
    id: String,
    type: String
  },
  data() {
    return {
      defaultTaskForm: {
        assignee: "",
        candidateUsers: [],
        candidateGroups: [],
        candidateDepts: [],
        dueDate: "",
        followUpDate: "",
        priority: ""
      },
      dataType: 'USER',
      userTaskForm: {},
      mockData: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      allUsers: [],
      users: [],
      authorityTree: [],
      deptTree: [],
      loading: false,
    };
  },
  watch: {
    id: {
      immediate: true,
      handler() {
        this.bpmnElement = window.bpmnInstances.bpmnElement;
        this.$nextTick(() => this.resetTaskForm());
      }
    }
  },
  methods: {
    resetTaskForm() {
      for (let key in this.defaultTaskForm) {
        let value;
        if (key === "candidateUsers" || key === "candidateGroups") {
          value = this.bpmnElement?.businessObject[key] ? this.bpmnElement.businessObject[key].split(",") : [];
        } else {
          value = this.bpmnElement?.businessObject[key] || this.defaultTaskForm[key];
        }
        this.userTaskForm[key] = value
      }
    },
    updateElementTaskAssignee() {
      const taskAttr = Object.create(null);
      taskAttr["assignee"] = this.userTaskForm["assignee"] || null;
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr);
    },
    updateElementTaskCandidateUsers() {
      const key = "candidateUsers";
      const taskAttr = Object.create(null);
      taskAttr[key] = this.userTaskForm[key] && this.userTaskForm[key].length ? this.userTaskForm[key].join() : null;
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr);
    },
    updateElementTaskCandidateGroups() {
      const key = "candidateGroups";
      const taskAttr = Object.create(null);
      if (this.userTaskForm[key] && this.userTaskForm[key].length) {
        let roles = []
        this.userTaskForm[key].forEach(id => {
          roles.push("ROLE_" + id)
        })
        window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {"candidateGroups": roles.join()});
      }
    },
    updateElementTaskCandidateDepts() {
      const key = "candidateDepts";
      const taskAttr = Object.create(null);
      if (this.userTaskForm[key] && this.userTaskForm[key].length) {
        let roles = []
        this.userTaskForm[key].forEach(id => {
          roles.push("DEPT_" + id)
        })
        window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {"candidateGroups": roles.join()});
      }
    },
    updateElementTask(key) {
      const taskAttr = Object.create(null);
      if (key === "candidateUsers" || key === "candidateGroups") {
        taskAttr[key] = this.userTaskForm[key] && this.userTaskForm[key].length ? this.userTaskForm[key].join() : null;
      } else {
        taskAttr[key] = this.userTaskForm[key] || null;
      }
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr);
    },
    changeDataType(val) {
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {"assignee": null})
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {"candidateUsers": null})
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {"candidateGroups": null})
      if (val === 'AUTHORITY') {
        getAuthorityTree().then(res => {
          this.authorityTree = res.data
        })
      } else if (val === 'DEPTS') {
        getDepartmentTree().then(res => {
          this.deptTree = res.data
        })
      } else if (val === 'INITIATOR') {
        window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {"assignee": "${initiator}"})
      }
    }
  },
  beforeUnmount() {
    this.bpmnElement = null;
  },
  created() {
    getAllUser().then(res => {
      this.allUsers = res.data
      this.users = res.data
    })
  }
};
</script>
