<template>
  <div class="wrapper">
    <n-spin :show="isLoading">
      <h2>Zmiana hasła</h2>
      <n-space vertical class="wrapper__form">
        <label for="password">Hasło:</label>
        <n-input
          type="password"
          show-password-on="mousedown"
          placeholder="Wprowadz haslo"
          :maxlength="30"
          :minlength="6"
          v-model:value="passwordValue"
          id="password"
        />
        <label for="confirmPassword">Potwierdz haslo:</label>
        <n-input
          type="password"
          show-password-on="click"
          placeholder="Potwierdz haslo"
          :maxlength="30"
          :minlength="6"
          v-model:value="confirmPasswordValue"
          id="confirmPassword"
        >
          <template #password-visible-icon>
            <n-icon :size="16" :component="GlassesOutline" />
          </template>
          <template #password-invisible-icon>
            <n-icon :size="16" :component="Glasses" />
          </template>
        </n-input>
      </n-space>
      <n-button @click="changePassword" class="wrapper__button" type="primary">
        Zmień hasło
      </n-button>
    </n-spin>
  </div>
</template>

<script setup>
import { GlassesOutline, Glasses } from "@vicons/ionicons5";
import { NSpace, NInput, NButton, NSpin } from "naive-ui";
import axios from "axios";
import { ref } from "vue";
import { useToast } from "vue-toastification";
import { getUserData } from "../GlobalContext/GlobalVariables";
const userData = ref(getUserData());
const passwordValue = ref("");
const confirmPasswordValue = ref("");
const isLoading = ref(false);

const toast = useToast();
const changePassword = () => {
  if (passwordValue.value.length < 6 || passwordValue.value.length > 30) {
    toast.error("Błędnie wprowadzone hasła!", { timeout: 2000 });
    passwordValue.value = "";
    confirmPasswordValue.value = "";
    return;
  }
  if (confirmPasswordValue.value.length < 6 || confirmPasswordValue.value.length > 30) {
    toast.error("Błędnie wprowadzone hasła!", { timeout: 2000 });
    passwordValue.value = "";
    confirmPasswordValue.value = "";
    return;
  }

  if (passwordValue.value !== confirmPasswordValue.value) {
    toast.error("Hasla sie nie zgadzaja!", { timeout: 2000 });
    passwordValue.value = "";
    confirmPasswordValue.value = "";
    return;
  }
  isLoading.value = true;
  axios({
    method: "put",
    url: "/api/v1/user/password",
    data: {
      newPassword: passwordValue.value,
    },
  })
    .then((response) => {
      toast.success("Zmieniono hasło!", { timeout: 2000 });
      passwordValue.value = "";
      confirmPasswordValue.value = "";
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};
</script>

<style scoped lang="scss">
.wrapper {
  margin-top: 20px;
  &__form {
    width: 50%;
    margin: 10px auto 0 auto;
  }

  &__button {
    margin-top: 10px;
    padding: 5px 20px;
  }
}
</style>
