<template>
  <div class="wrapper">
    <header-no-input />
    <n-spin :show="isLoading">
      <n-form class="registerForm">
        <n-form-item label="Imie">
          <n-input
            :status="validateStatus.inputNameStatus"
            ref="nameInput"
            placeholder="Imie"
            v-model:value="model.name"
          />
        </n-form-item>

        <n-form-item label="Nazwisko">
          <n-input
            :status="validateStatus.inputSurnameStatus"
            placeholder="Nazwisko"
            v-model:value="model.surname"
          />
        </n-form-item>

        <n-form-item label="Płeć">
          <n-radio-group
            :status="validateStatus.inputSexStatus"
            v-model:value="model.gender"
          >
            <n-space>
              <n-radio value="MALE"> Mężczyzna </n-radio>
              <n-radio value="FEMALE"> Kobieta </n-radio>
            </n-space>
          </n-radio-group>
        </n-form-item>

        <n-form-item label="Email">
          <n-input
            :status="validateStatus.inputEmailStatus"
            placeholder="Email"
            v-model:value="model.email"
          />
        </n-form-item>

        <n-form-item label="Hasło">
          <n-input
            :status="validateStatus.inputPasswordStatus"
            type="password"
            placeholder="Password"
            v-model:value="model.password"
          />
        </n-form-item>

        <n-form-item label="Potwierdz Hasło">
          <n-input
            :status="validateStatus.inputConfirmPasswordStatus"
            type="password"
            placeholder="Confirm Password"
            v-model:value="model.confirmPassword"
          />
        </n-form-item>

        <n-form-item label="Data urodzin">
          <n-date-picker style="width: 100%" v-model:value="model.birthDay" />
        </n-form-item>
        <div class="inline">
          <n-form-item label="Kod pocztowy">
            <n-input
              :status="validateStatus.inputPostalCode"
              placeholder="Kod pocztowy"
              v-model:value="model.postCode"
            />
          </n-form-item>

          <n-form-item label="Numer domu">
            <n-input
              :status="validateStatus.inputHomeNumber"
              placeholder="Numer domu"
              v-model:value="model.homeNumber"
            />
          </n-form-item>
        </div>
        <div class="inline">
          <n-form-item label="Ulica">
            <n-input
              :status="validateStatus.inputStreetName"
              placeholder="Ulica"
              v-model:value="model.street"
            />
          </n-form-item>

          <n-form-item label="Miasto">
            <n-input
              :status="validateStatus.inputCityName"
              placeholder="Miasto"
              v-model:value="model.city"
            />
          </n-form-item>
        </div>
        <n-button class="button" @click="registerUser()" rouned>
          Zarejestruj się!
        </n-button>
      </n-form>
    </n-spin>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";
import {
  NForm,
  NFormItem,
  NInput,
  NButton,
  NDatePicker,
  NSpace,
  NRadio,
  NRadioGroup,
  NSpin,
} from "naive-ui";
import { createToast } from "mosha-vue-toastify";
import "mosha-vue-toastify/dist/style.css";
import axios from "axios";
import HeaderNoInput from "./HeaderNoInput.vue";
import { useToast } from "vue-toastification";
import router from "../router";

const isLoading = ref(false);
const toast = useToast();

const model = ref({
  name: "",
  surname: "",
  email: "",
  password: "",
  confirmPassword: "",
  postCode: "",
  homeNumber: "",
  street: "",
  city: "",
  gender: "",
  birthDay: new Date().getTime(),
});

const validateStatus = ref({
  inputNameStatus: "",
  inputSurnameStatus: "",
  inputSexStatus: "",
  inputEmailStatus: "",
  inputPasswordStatus: "",
  inputConfirmPasswordStatus: "",
  inputDateStatus: "",
  inputPostalCode: "",
  inputHomeNumber: "",
  inputStreetName: "",
  inputCityName: "",
});

const nameInput = ref(null);

const validateRegister = () => {
  const {
    name,
    surname,
    email,
    password,
    birthDay,
    confirmPassword,
    postCode,
    homeNumber,
    street,
    city,
    gender,
  } = model.value;

  if (name.length < 3) {
    validateStatus.value.inputNameStatus = "error";
    toast.error("Imie powinno mieć minimum 3 znaki.", { timeout: 2000 });
    return false;
  } else {
    validateStatus.value.inputNameStatus = "";
  }
  if (surname.length < 3) {
    validateStatus.value.inputSurnameStatus = "error";
    toast.error("Nazwisko powinno mieć minimum 3 znaki.", { timeout: 2000 });
    return false;
  } else {
    validateStatus.value.inputSurnameStatus = "";
  }
  if (email.match("[a-z0-9_.-]+@[a-z0-9_.-]+\\.\\w{2,4}")) {
    validateStatus.value.inputEmailStatus = "";
  } else {
    toast.error("Email jest niepoprawny.", { timeout: 2000 });
    validateStatus.value.inputEmailStatus = "error";
    return false;
  }

  if (password.length > 5 && password.length < 31) {
    validateStatus.value.inputPasswordStatus = "";
  } else {
    validateStatus.value.inputPasswordStatus = "error";
    toast.error("Hasło ", { timeout: 2000 });
    return false;
  }

  if (password !== confirmPassword) {
    toast.error("Hasła nie są takie same.", { timeout: 2000 });
    validateStatus.value.inputPasswordStatus = "error";
    validateStatus.value.inputConfirmPasswordStatus = "error";
    return false;
  } else {
    validateStatus.value.inputPasswordStatus = "";
    validateStatus.value.inputConfirmPasswordStatus = "";
  }

  if (!birthDay > 0) {
    toast.error("Data nie jest poprawana.", { timeout: 2000 });
  }

  if (postCode.match("[0-9]{2}-[0-9]{3}")) {
    validateStatus.value.inputPostalCode = "";
  } else {
    toast.error("Kod pocztowy nie jest poprawny.", { timeout: 2000 });
    validateStatus.value.inputPostalCode = "error";
    return false;
  }

  if (homeNumber.length > 0) {
    validateStatus.value.inputHomeNumber = "";
  } else {
    toast.error("Numer domu musi się składać z minimum 1 znaku.", {
      timeout: 2000,
    });
    validateStatus.value.inputHomeNumber = "error";
    return false;
  }

  if (street.length > 1) {
    validateStatus.value.inputStreetName = "";
  } else {
    toast.error("Nazwa ulicy musi się składać z conajmiej 2 znaków.", {
      timeout: 2000,
    });
    validateStatus.value.inputStreetName = "error";
    return false;
  }

  if (city.length > 1) {
    validateStatus.value.inputCityName = "";
  } else {
    toast.error("Nazwa miasta musi się składać z conajmniej 2 znaków.", {
      timeout: 2000,
    });
    validateStatus.value.inputCityName = "error";
    return false;
  }

  if (gender === "MALE" || gender === "FEMALE") {
    validateStatus.value.inputSexStatus = "";
  } else {
    toast.error("Wybierz płeć.", { timeout: 2000 });
    validateStatus.value.inputSexStatus = "error";
    return false;
  }
  return true;
};

const registerUser = () => {
  if (!validateRegister()) {
    return;
  }

  const {
    name,
    surname,
    email,
    password,
    birthDay,
    confirmPassword,
    postCode,
    homeNumber,
    street,
    city,
    gender,
  } = model.value;
  const user = {
    name,
    surname,
    email,
    password,
    birthDay,
    postCode,
    homeNumber,
    street,
    city,
    gender,
  };

  isLoading.value = true;
  axios({
    method: "post",
    url: "/api/v1/registration",
    data: user,
  })
    .then(() => {
      toast.success("Rejestracja przebiegła pomyślnie", { timeout: 2000 });
      router.push({ path: "/login" });
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};

watch(
  () => ({ ...model.value }),
  () => {
    const {
      name,
      surname,
      email,
      password,
      birthDay,
      confirmPassword,
      postCode,
      homeNumber,
      street,
      city,
      gender,
    } = model.value;

    if (name.length < 3) {
      validateStatus.value.inputNameStatus = "warning";
    } else {
      validateStatus.value.inputNameStatus = "";
    }
    if (surname.length < 3) {
      validateStatus.value.inputSurnameStatus = "warning";
    } else {
      validateStatus.value.inputSurnameStatus = "";
    }

    if (email.match("[a-z0-9_.-]+@[a-z0-9_.-]+\\.\\w{2,4}")) {
      validateStatus.value.inputEmailStatus = "";
    } else {
      validateStatus.value.inputEmailStatus = "warning";
    }

    if (password !== confirmPassword) {
      validateStatus.value.inputPasswordStatus = "warning";
      validateStatus.value.inputConfirmPasswordStatus = "warning";
    } else {
      validateStatus.value.inputPasswordStatus = "";
      validateStatus.value.inputConfirmPasswordStatus = "";
    }

    if (password.length > 5 && password.length < 31) {
      validateStatus.value.inputPasswordStatus = "";
    } else {
      validateStatus.value.inputPasswordStatus = "warning";
    }

    if (postCode.match("[0-9]{2}-[0-9]{3}")) {
      validateStatus.value.inputPostalCode = "";
    } else {
      validateStatus.value.inputPostalCode = "warning";
    }

    if (homeNumber.length > 0) {
      validateStatus.value.inputHomeNumber = "";
    } else {
      validateStatus.value.inputHomeNumber = "warning";
    }

    if (street.length > 1) {
      validateStatus.value.inputStreetName = "";
    } else {
      validateStatus.value.inputStreetName = "warning";
    }

    if (city.length > 1) {
      validateStatus.value.inputCityName = "";
    } else {
      validateStatus.value.inputCityName = "warning";
      return false;
    }

    if (gender === "MALE" || gender === "FEMALE") {
      validateStatus.value.inputSexStatus = "";
    } else {
      validateStatus.value.inputSexStatus = "warning";
    }
  }
);

onMounted(() => {
  validateStatus.value.inputNameStatus = "";
  validateStatus.value.inputSurnameStatus = "";
  validateStatus.value.inputEmailStatus = "";
  validateStatus.value.inputPasswordStatus = "";
  validateStatus.value.inputConfirmPasswordStatus = "";
  validateStatus.value.inputPostalCode = "";
  validateStatus.value.inputHomeNumber = "";
  validateStatus.value.inputStreetName = "";
});
</script>

<style scoped lang="scss">
@import "../style/colors.scss";
.wrapper {
  width: 100%;
  height: 100%;

  .registerForm {
    margin: auto;
    max-width: 400px;
    padding: 10px;
    border: 2px solid rgba(128, 128, 128, 0.3);
    border-radius: 7px;
    margin-top: 3vh;
  }

  .button {
    width: 100%;
    padding: 20px;
    background-color: $base-color;
    color: white;
    font-size: 1rem;
  }

  .inline {
    display: flex;
    justify-content: space-between;

    & > * {
      width: 48%;
    }
  }
}
</style>
