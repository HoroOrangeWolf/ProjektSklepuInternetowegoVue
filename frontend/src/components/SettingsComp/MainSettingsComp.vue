<template>
  <div class="wrapper">
    <h1>Zmień ustawienia</h1>
    <div class="wrapper__menu">
      <n-menu
        v-model:value="activeKey"
        mode="horizontal"
        :options="isSocialAcc ? menuOptionsWithoutPassword : menuOptions"
      />
    </div>
    <router-view userData="userData" />
  </div>
</template>

<script setup>
import { defineComponent, h, ref, onMounted } from "vue";
import { NIcon, NMenu } from "naive-ui";
import {
  BookOutline as BookIcon,
  PersonOutline as PersonIcon,
  WineOutline as WineIcon,
} from "@vicons/ionicons5";
import router from "../../router";
import axios from "axios";
import { getIsSocialAccount } from "../GlobalContext/GlobalVariables";
const activeKey = ref(null);
const userData = ref([]);
const isSocialAcc = ref(false);
isSocialAcc.value = getIsSocialAccount();

function renderIcon(icon) {
  return () => h(NIcon, null, { default: () => h(icon) });
}

const menuOptions = [
  {
    label: () => [
      h(
        "button",
        {
          onClick() {
            router.push({ path: "/user/settings" });
          },
          style: {
            margin: "0 10px",
            padding: "7px 15px",
            borderRadius: "5px",
            outline: "0",
            border: "0",
            color: "#fff",
            fontWeight: "bold",
            cursor: "pointer",
            backgroundColor: "#18a058",
          },
        },
        "Zmień adres"
      ),
      h(
        "button",
        {
          onClick() {
            router.push({ path: "/user/settings/changePassword" });
          },
          style: {
            margin: "0 10px",
            padding: "7px 15px",
            borderRadius: "5px",
            outline: "0",
            border: "0",
            color: "#fff",
            fontWeight: "bold",
            cursor: "pointer",
            backgroundColor: "#18a058",
          },
        },
        "Zmień hasło"
      ),
      h(
        "button",
        {
          onClick() {
            router.push({ path: "/user/settings/changeUserAccount" });
          },

          style: {
            margin: "0 10px",
            padding: "7px 15px",
            borderRadius: "5px",
            outline: "0",
            border: "0",
            color: "#fff",
            fontWeight: "bold",
            cursor: "pointer",
            backgroundColor: "#18a058",
          },
        },
        "Zmień informacje"
      ),
    ],
  },
];
const menuOptionsWithoutPassword = [
  {
    label: () => [
      h(
        "button",
        {
          onClick() {
            router.push({ path: "/user/settings" });
          },
          style: {
            margin: "0 10px",
            padding: "7px 15px",
            borderRadius: "5px",
            outline: "0",
            border: "0",
            color: "#fff",
            fontWeight: "bold",
            cursor: "pointer",
            backgroundColor: "#18a058",
          },
        },
        "Zmień adres"
      ),
      h(
        "button",
        {
          onClick() {
            router.push({ path: "/user/settings/changeUserAccount" });
          },

          style: {
            margin: "0 10px",
            padding: "7px 15px",
            borderRadius: "5px",
            outline: "0",
            border: "0",
            color: "#fff",
            fontWeight: "bold",
            cursor: "pointer",
            backgroundColor: "#18a058",
          },
        },
        "Zmień informacje"
      ),
    ],
  },
];
</script>

<style scoped lang="scss">
.wrapper {
  display: block;

  &__menu {
    width: 50%;
    margin: 0 auto;
  }
}
</style>
