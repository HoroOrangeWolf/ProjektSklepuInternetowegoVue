<template>
  <div class="wrapper">
    <h2>Zmień informacje o użytkowniku</h2>
    <n-spin :show="isLoading">
      <n-space vertical class="wrapper__form">
        <label for="name">Imie:</label>
        <n-input
          type="text"
          placeholder="Wprowadz imie"
          :maxlength="255"
          :minlength="3"
          v-model:value="nameValue"
          id="name"
        />
        <label for="surname">Nazwisko:</label>
        <n-input
          type="text"
          placeholder="Potwierdz nazwisko"
          :maxlength="255"
          :minlength="3"
          v-model:value="surnameValue"
          id="surname"
        >
        </n-input>
        <label for="date">Data:</label>
        <n-date-picker v-model:value="dateValue" type="date" clearable id="date" />
      </n-space>
      <n-button @click="changeUserInfo" class="wrapper__button" type="primary">
        Zmień dane
      </n-button>
    </n-spin>
  </div>
</template>

<script setup>
import { NSpace, NInput, NButton, NDatePicker, NSpin } from "naive-ui";
import axios from "axios";
import { ref } from "vue";
import { useToast } from "vue-toastification";
import { getUserData } from "../GlobalContext/GlobalVariables";
const userData = ref(getUserData());
const { name, surname, birthDay } = userData.value;
const nameValue = ref(name);
const surnameValue = ref(surname);
const dateValue = ref(new Date(birthDay));
const isLoading = ref(false);

const toast = useToast();
const changeUserInfo = () => {
  if (nameValue.value.length < 3 || nameValue.value.length > 255) {
    toast.error("Błędnie wprowadzone dane!", { timeout: 2000 });
    nameValue.value = name;
    surnameValue.value = surname;
    dateValue.value = new Date(birthDay);
    return;
  }
  if (surnameValue.value.length < 3 || surnameValue.value.length > 255) {
    toast.error("Błędnie wprowadzone dane!", { timeout: 2000 });
    nameValue.value = name;
    surnameValue.value = surname;
    dateValue.value = new Date(birthDay);
    return;
  }
  if (!dateValue.value) {
    toast.error("Błędnie wprowadzone dane!", { timeout: 2000 });
    nameValue.value = name;
    surnameValue.value = surname;
    dateValue.value = new Date(birthDay);
    return;
  }
  isLoading.value = true;
  axios({
    method: "put",
    url: "/api/v1/user",
    data: {
      name: nameValue.value,
      surname: surnameValue.value,
      birthDay: dateValue.value,
    },
  })
    .then((response) => {
      toast.success("Zmieniono dane!", { timeout: 2000 });
      nameValue.value = nameValue.value;
      surnameValue.value = surnameValue.value;
      dateValue.value = dateValue.value;
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
