<template>
<router-link to="/" class="logo-box">
    <h1>E-commerce</h1>
    <img class="logo" src="../../assets/anjunabeats.png" alt="">
</router-link>
</template>

<style scoped lang="scss">
.logo-box {
      display: flex;
      width: 300px;
      justify-content: space-between;
      align-items: center;
      padding: 5px;
      text-decoration: none;

      h1 {
        color: black;
      }

    .logo{
        width: 100px;
    }
  }
</style>