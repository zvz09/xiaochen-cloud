<template>
  <WujieVue width="100%" height="100%" :name="microName" :url="microUrl" :props="props" sync alive></WujieVue>
</template>

<script setup lang="ts" name="about">
import { useRoute } from "vue-router";

const route = useRoute();
console.log(route.query);
const microUrl = route.query.microUrl as string;
const microName = route.query.microName as string;
const props = {
  redirect: route.query.redirect as string,
  microName: route.query.microName as string
};
</script>
