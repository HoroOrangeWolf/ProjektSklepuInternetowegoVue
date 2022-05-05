<template>
  <div class="header-box">
    <router-link to="/" class="logo-box">
      <h1>E-commerce</h1>
      <img class="logo" src="../assets/anjunabeats.png" alt="" />
    </router-link>
  </div>
</template>

<style scoped lang="scss">
.header-box {
  width: 80%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0rem 2rem;
  margin: 0 auto;
  border-bottom: 1px solid rgba(128, 128, 128, 0.6);

  .logo {
    width: 100px;
  }

  .logo-box {
    display: flex;
    width: 300px;
    justify-content: space-between;
    align-items: center;
    padding: 5px;
  }
}
</style>
