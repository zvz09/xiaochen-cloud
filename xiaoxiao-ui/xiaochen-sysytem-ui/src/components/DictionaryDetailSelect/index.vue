<template>
  <div class="LabelSelectCpm">
    <el-select
      v-model="valueStr"
      :loading="loading"
      :multiple="false"
      :placeholder="placeholder"
      :remote-method="remoteMethod"
      filterable
      remote
      reserve-keyword
      @change="selectChange"
      @visible-change="visibleChange"
    >
      <div class="LabelSelectCpmBox">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </div>
    </el-select>
  </div>
</template>

<script lang="ts">
export default {
  name: "DictionaryDetailSelect"
};
</script>

<script lang="ts" setup>
import { ref, reactive } from "vue";
import { getByDictionaryEncode } from "@/api/system/dictionary";
// const emit = defineEmits([])
const props = defineProps({
  modelValue: {
    type: String,
    default: ""
  },
  value: {
    type: String,
    default: ""
  },
  label: {
    type: String,
    default: ""
  },
  placeholder: String,
  dictionaryType: String,
  apiFun: Function
});

const emits = defineEmits(["update:modelValue", "update:value", "update:label"]);
// select 绑定的 v-model
const valueStr = ref();
const searchKeyword = ref("");
//dom 事件节点
const dom = ref();
const loading = ref(false);
// select-options 数据源
const options = ref([]);
// 分页控制
const page = reactive({
  page: 1,
  size: 10,
  pages: 0
});

valueStr.value = props.value;

/**
 *  加载接口数据
 * @return Promise
 */
const loadData = async () => {
  return getByDictionaryEncode(props.dictionaryType, {
    page: page.page,
    pageSize: page.size,
    keyword: searchKeyword.value
  });
};

/**
 * @param {string} query 输入的搜索关键词
 * @param {function} fn 需要在接口数据返回后执行的回调
 */
const remoteMethod = (query: string, fn?: Function) => {
  loading.value = true;
  if (query) {
    searchKeyword.value = query;
  }
  loadData().then((res: any) => {
    options.value = options.value.concat(res.data.records);
    if (page.pages != res.pages) {
      page.pages = res.pages;
    }
    loading.value = false;
    fn && fn();
  });
  loading.value = false;
};

const selectChange = (val: any) => {
  const selectVal = options.value.filter((e: any) => e.value == val);
  emits("update:modelValue", val);
  emits("update:value", val);
  emits("update:label", selectVal[0].label);
};

/* 滚动监听函数 */
const scrollAddEventFn = e => {
  const self = e.target as any;
  if (self.scrollHeight - self.scrollTop - self.clientHeight < 1) {
    if (page.page + 1 < page.pages) {
      page.page++;
      remoteMethod(searchKeyword.value);
    }
  }
};
const visibleChange = (isShow: any) => {
  options.value = []; //清空数组
  if (isShow) {
    // 下拉框显示时，渲染数据，初始化滚动监听
    remoteMethod(searchKeyword.value, () => {
      /* 在数据渲染完之后的回调 */
      /* 初始化滚动监听 （由于 dom 渲染未完成，所以需要开启一个 timeout 在 1s 后实现监听） */
      const parentDom = document.querySelectorAll(
        ".el-select-dropdown__wrap.el-scrollbar__wrap.el-scrollbar__wrap--hidden-default"
      ) as any;
      setTimeout(() => {
        parentDom.forEach((e: any, idx: number) => {
          if (
            e.querySelector(".LabelSelectCpmBox") &&
            e.querySelector(".LabelSelectCpmBox").children &&
            e.querySelector(".LabelSelectCpmBox").children.length > 0
          ) {
            dom.value = parentDom[idx];
            dom.value.addEventListener("scroll", scrollAddEventFn, false);
          }
        });
      }, 1000);
    });
  } else {
    // 移除滚动监听
    dom.value?.removeEventListener("scroll", scrollAddEventFn, false);
    options.value = [];
  }
};
</script>
