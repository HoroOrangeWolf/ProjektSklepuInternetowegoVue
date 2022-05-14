<template>
  <div class="wrapper">
    <nav>
      <n-list class="itemList">
        <template #header>
          <h1>Twoje konto</h1>
        </template>
        <template #default>
          <n-list-item class="linkContainer">
            <n-button class="link" type="text" @click="$router.push({ path: '/user' })">
              <template #icon>
                <fa icon="book" />
              </template>
              <h2>Twoje zamówienie</h2>
            </n-button>
          </n-list-item>
          <n-list-item class="linkContainer">
            <n-button
              class="link"
              type="text"
              @click="$router.push({ path: '/user/opinions' })"
            >
              <template #icon>
                <fa icon="star" />
              </template>
              <h2>Twoje opinie</h2>
            </n-button>
          </n-list-item>
		   <n-list-item class="linkContainer">
            <n-button
              class="link"
              type="text"
              @click="$router.push({ path: '/user/settings' })"
            >
              <template #icon>
                <fa icon="gears" />
              </template>
              <h2>Ustawienia</h2>
            </n-button>
          </n-list-item>
        </template>
      </n-list>
    </nav>
    <router-view class="container" />
  </div>
</template>

<script setup>
import { NList, NListItem, NButton } from "naive-ui";
</script>

<style scoped lang="scss">
.wrapper {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin-top: 11px;

  nav {
    width: 20%;
    padding: 0 15px;

    .itemList {
      background-color: rgb(245, 245, 245);
      border-radius: 11px;
      padding: 0;
      overflow: hidden;
      .linkContainer {
        padding: 0;
        width: 100%;
        height: 100%;
        background-color: rgb(245, 245, 245);

        &:hover {
          background-color: rgb(220, 220, 220);
        }

        .link {
          text-decoration: none;
          color: rgb(51, 54, 57);
          padding: 20px 10px;
          width: 100%;
		  
		  
		  
        }
      }
    }
  }

  .container {
    width: 60%;
  }
}
</style>
