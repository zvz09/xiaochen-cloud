import { useRoute } from "vue-router";
import { useTabsStore } from "@/stores/modules/tabs";

const route = useRoute();
const tabStore = useTabsStore();
export function closeThisPage() {
  if (route.meta.isAffix) return;
  tabStore.removeTabs(route.fullPath);
}
