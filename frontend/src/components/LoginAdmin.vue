<template>
  <div class="container">
    <header-no-input />
    <form class="login" name="form" :action="`${BACKEND_URL}/login`" method="POST">
      <label for="login">Login</label>
      <input name="username" type="text" class="input" placeholder="Login" />
      <label for="password">Password</label>
      <input
        name="password"
        type="password"
        class="input"
        show-password-on="mousedown"
        placeholder="Password"
      />
      <n-button class="button" secondary circle attr-type="submit">
        <p>Login</p>
      </n-button>
      <n-button
        class="button"
        secondary
        circle
        @click="router.push({ path: '/register' })"
      >
        <p>Register</p>
      </n-button>
      <a :href="`${BACKEND_URL}/oauth2/authorization/google`" class="linkGoogle">
        <n-button class="button googleButton">
          <img src="../assets/google-brands.svg" width="20" height="20" alt="" />
          <p>Zaloguj się poprzez google!</p>
        </n-button>
      </a>
    </form>
  </div>
</template>

<script setup>
import { NInput, NButton } from "naive-ui";
import HeaderNoInput from "./HeaderNoInput.vue";
import { useToast } from "vue-toastification";
import router from "../router";
const toast = useToast();
const BACKEND_URL = process.env.VUE_APP_BACKEND_URL;
const url = window.location.href;
if (url.includes("error")) {
  toast.error("Nieprawidłowe dane logowania", { timeout: 2000 });
}
</script>

<style scoped lang="scss">
@import "../style/colors.scss";
.container {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;

  .login {
    display: flex;
    flex-direction: column;
    width: 30%;
    margin: 0 auto;
    margin-top: 20vh;
    min-width: 500px;
    max-width: 600px;
    align-items: center;

    .button {
      width: 50%;
      background-color: $base-color;
      margin-top: 1rem;
      padding: 20px;
      transition: all 90ms;
      p {
        font-weight: bold;
        font-size: 1.2rem;
      }
    }

    .button:hover {
      background-color: rgba($base-color, 0.87);
      padding: 22px;
      margin-top: 14px;
    }
    .input {
      width: 50%;
      min-height: 34px;
      border-radius: 15px;
      padding: 5px;
      border: 1px solid gray;

      &:focus {
        outline: none;
        border: 2px solid rgba($base-color, 0.87);
      }
    }
    .linkGoogle {
      width: 100%;
    }
    .googleButton {
      background-color: white;
      color: rgba(0, 0, 0, 0.7);
      width: 50%;

      p {
        font-size: 11px;
      }

      &:hover {
        background-color: white;
        border: 1px solid rgba(128, 128, 128, 0.1);
      }
    }
  }

  label {
    font-weight: bold;
    font-size: 1.5rem;
    margin: 0 auto;
  }
}
</style>
