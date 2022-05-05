<template>
  <n-loading-bar-provider>
    <global-context>
      <router-view />
    </global-context>
  </n-loading-bar-provider>
</template>

<script>
import { NLoadingBarProvider } from "naive-ui";
import GlobalContext from "./components/GlobalContext/GlobalContext.vue";

export default {
  setup() {},
  components: {
    GlobalContext,
    NLoadingBarProvider,
  },
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
