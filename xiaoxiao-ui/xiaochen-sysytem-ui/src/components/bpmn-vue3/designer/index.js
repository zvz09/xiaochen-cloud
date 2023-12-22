import MyProcessDesigner from '@/components/bpmn-vue3/designer/ProcessDesigner.vue';

MyProcessDesigner.install = function (Vue) {
  Vue.component(MyProcessDesigner.name, MyProcessDesigner);
};

export default MyProcessDesigner;
