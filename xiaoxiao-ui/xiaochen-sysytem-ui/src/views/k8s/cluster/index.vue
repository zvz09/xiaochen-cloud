<template>
  <div class="table-box">
    <el-card v-for="cluster in clusterData" :key="cluster" class="box-card">
      <template #header>
        <div class="card-header">
          <span>{{ cluster.clusterName }}</span>
          <el-button class="button" text>{{ cluster.createdAt }}</el-button>
        </div>
      </template>

      <template #footer>{{ cluster.version }}</template>
    </el-card>
  </div>
</template>
<script setup lang="ts" name="k8sManage">
import { ref } from "vue";
import { listK8sCluster } from "@/api/k8s/cluster";
import { K8sCluster } from "@/api/k8s/cluster/types";

const clusterData = ref<K8sCluster.Cluster[]>();

async function getCluster() {
  const { data } = await listK8sCluster();
  clusterData.value = data;
}

getCluster();
</script>
<style>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
}
</style>
