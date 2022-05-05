<script setup>
import { ref } from "@vue/reactivity";
import { inject, provide } from "@vue/runtime-core";

const moduls = ref([]);

const emiter = defineEmits(["click"]);

provide("moduler", {
  addModul(modul, resetModuleFunction) {
    moduls.value = [...moduls.value, { modul, resetModuleFunction }];
  },
  clickModul(modul, name) {
    moduls.value.filter((f) => f.modul !== modul).forEach((f) => f.resetModuleFunction());
    emiter("click", name);
  },
  removeModul(modul) {
    moduls.value = moduls.value.filter((f) => f.modul != modul);
  },
});
</script>

<template>
  <div>
    <slot />
  </div>
</template>

<script>
export default {
  setup() {},
};
</script>
