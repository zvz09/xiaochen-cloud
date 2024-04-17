import { RouteLocationNormalizedLoaded } from "vue-router";
import { useTabsStore } from "@/stores/modules/tabs";

const tabStore = useTabsStore();

export function closeThisPage(route: RouteLocationNormalizedLoaded) {
  console.log(route.meta);
  if (route.meta.isAffix) return;
  tabStore.removeTabs(route.fullPath);
}
