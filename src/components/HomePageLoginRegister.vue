<template>
  <div class="wrapper">
    <n-button round class="upBtn" @click="goToLogin()"> Zaloguj się! </n-button>
    <n-divider class="divider"> Nie masz konta? </n-divider>
    <n-button round class="downBtn" @click="goToRegister()"> Zarejestruj się! </n-button>
  </div>
</template>

<script setup>
import { NButton, NDivider } from "naive-ui";
import router from "../router";

const goToLogin = () => {
  router.push({ path: "/login" });
};

const goToRegister = () => {
  router.push({ path: "/register" });
};
</script>

<style scoped lang="scss">
@import "../style/colors.scss";

.wrapper {
  display: flex;
  flex-direction: column;
  min-width: 250px;
  border-bottom: 1px solid rgba(128, 128, 128, 0.15);
  padding: 0 20px;

  .divider {
    margin: 20px 0;
  }

  .upBtn {
    background-color: $base-color;
    color: white;
    margin-top: 13px;
    &:hover {
      color: white;
    }
  }
  .downBtn {
    margin-bottom: 20px;
  }
}
</style>
