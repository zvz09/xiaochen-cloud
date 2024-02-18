<template>
  <WujieVue width="100%" height="100%" :name="microName" :url="microUrl" :props="props" sync alive></WujieVue>
</template>

<script setup lang="ts" name="about">
import { useRoute } from "vue-router";
import Crypto from "@/utils/crypto";

const route = useRoute();
const queryObj = JSON.parse(Crypto.decrypt(route.query.value));
const microUrl = queryObj.microUrl as string;
const microName = queryObj.microName as string;
const props = {
  redirect: queryObj.redirect as string,
  microName: queryObj.microName as string
};
</script>
