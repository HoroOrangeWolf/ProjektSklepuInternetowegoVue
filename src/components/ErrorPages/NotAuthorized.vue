<script setup>
import { faExclamationTriangle } from "@fortawesome/free-solid-svg-icons";
</script>

<template>
  <div class="con">
    <div>
      <fa :icon="faExclamationTriangle" color="red" /> Nie posiadasz uprawnień do tej
      podstrony
    </div>
  </div>
</template>

<style scoped lang="scss">
.con {
  width: 100%;
  height: 50%;
  display: flex;
  justify-content: center;
  div {
    align-self: center;
    height: 50px;
    text-align: center;
    font-size: 2rem;
    font-weight: bold;
  }
}
</style>
