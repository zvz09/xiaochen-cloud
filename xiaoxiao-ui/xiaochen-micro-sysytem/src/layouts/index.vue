<!-- 💥 这里是一次性加载 LayoutComponents -->
<template>
  <LayoutClassic />
</template>

<script setup lang="ts" name="layout">
import LayoutClassic from "./LayoutClassic/index.vue";
</script>

<style scoped lang="scss">
.layout {
  min-width: 600px;
}
</style>
