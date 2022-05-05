<template>
  <div>
    <h1>Twoje opinie</h1>
    <n-list v-if="isLoading">
      <n-list-item v-for="index in 9" :key="index">
        <n-skeleton class="item" style="height: 135px" />
      </n-list-item>
    </n-list>
    <n-list v-else>
      <n-list-item v-for="(data, index) in opinions" :key="index">
        <single-comment-visible-on-profile :comment="data" />
      </n-list-item>
    </n-list>
  </div>
</template>

<script setup>
import { ref } from "@vue/reactivity";
import { onMounted } from "@vue/runtime-core";
import axios from "axios";
import { NSkeleton, NList, NListItem } from "naive-ui";
import { useToast } from "vue-toastification";
import SingleCommentVisibleOnProfile from "./SingleCommentVisibleOnProfile.vue";

const opinions = ref([]);
const isLoading = ref(false);

const toast = useToast();

const loadOpinions = () => {
  isLoading.value = true;
  axios({
    method: "get",
    url: "/api/v1/opinion?limit=99999&opinionsFor=USER",
  })
    .then((response) => {
      opinions.value = response.data.list;
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};

onMounted(() => {
  loadOpinions();
});
</script>

<style></style>
