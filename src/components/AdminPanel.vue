<script setup>
import userModel from "./User/GlobalVariables";

const { role, email } = userModel.value;
</script>

<template>
  <link
    href="https://cdn.jsdelivr.net/npm/@mdi/font@6.x/css/materialdesignicons.min.css"
    rel="stylesheet"
  />
  <div class="container">
    <nav>
      <ul>
        <li>
          <router-link to="/admin">
            <fa class="icon" icon="users" />
            Users
          </router-link>
        </li>
        <li>
          <router-link to="/admin/category">
            <fa class="icon" icon="book-bookmark" />
            Categories
          </router-link>
        </li>
        <li>
          <router-link to="/admin/product">
            <fa class="icon" icon="book-bookmark" />
            Products
          </router-link>
        </li>
        <li>
          <router-link to="/admin/orders">
            <fa class="icon" icon="book-bookmark" />
            Orders
          </router-link>
        </li>
      </ul>
    </nav>
    <div class="profile-comp">
      <logo-e-commerce />
      <profile-comp :name="email" :role="role" />
    </div>
    <div class="new-comp">
      <router-view />
    </div>
  </div>
</template>

<script>
import ProfileComp from "./ProfileComp.vue";
import LogoECommerce from "./Logo/LogoECommerce.vue";

export default {
  components: {
    ProfileComp,
    LogoECommerce,
  },
  setup() {},
};
</script>

<style scoped lang="scss">
body {
  min-width: 100vh;
  min-height: 100vh;
  margin: 0;
  padding: 0;

  .container {
    min-width: 100vw;
    min-height: 100vh;

    display: grid;
    grid-template-areas:
      "nav profile "
      "nav main ";

    grid-template-columns: 300px 1fr;
    grid-template-rows: 100px 1fr;

    nav {
      height: 100%;
      background-color: rgb(250, 250, 252);
      border-right: 1px solid rgba(128, 128, 128, 0.6);
      grid-area: nav;
      color: #fff;

      li {
        padding: 0.7rem 0;
        padding-top: 1.3rem;
        font-weight: bold;
        display: flex;
        align-items: center;
        font-size: 1.2rem;
        cursor: pointer;
        text-decoration: none;
        border-bottom: 1px solid rgba(128, 128, 128, 0.6);

        a {
          color: #000;
          text-decoration: none;

          &:focus {
            color: rgb(128, 128, 128);
          }
        }

        .icon {
          margin-left: 1rem;
          font-size: 1.5rem;
        }

        &:hover {
          background-color: #fff;
        }
      }
    }
  }

  .profile-comp {
    grid-area: profile;
    display: flex;
    justify-content: space-between;
    padding: 10px;
    border-bottom: 1px solid rgba(128, 128, 128, 0.6);
  }

  .new-comp {
    grid-area: main;
  }
}
</style>
