<script setup>
import { inject } from "@vue/runtime-core";
import { NButton } from "naive-ui";
import router from "../../router";

const { addToCart } = inject("cart");

const props = defineProps({
  item: {
    default: {},
  },
});
</script>

<template>
  <div class="product">
    <div @click="router.push({ name: 'singleProduct', params: { id: item.id + '' } })">
      <div class="product-img-box">
        <div
          class="product-img-holder"
          v-for="(sImg, index) in item.attachments.slice(0, 1)"
          :key="index"
        >
          <img :src="sImg.blob" alt="imgNotFound" />
        </div>
      </div>
      <div class="product-description-box">
        <h3>{{ item.name }}</h3>
        <p v-for="(spec, index) in item.specifications.slice(0, 4)" :key="index">
          {{ `${spec.keyName}: ${spec.keyValue}` }}
        </p>
      </div>
      <div class="product-price">
        <span class="price">{{ `${item.price} zł` }}</span>
      </div>
    </div>
    <n-button class="cartBtn" @click="addToCart(item)">
      <fa icon="basket-shopping" />
    </n-button>
  </div>
</template>

<style lang="scss" scoped>
.product {
  width: 30%;
  min-width: 300px;
  border-radius: 10px;
  position: relative;
  margin: 0 0.2rem;
  margin-bottom: 0.2rem;
  padding: 1rem;

  .product-price {
    padding: 1rem 0;
    display: flex;
    justify-content: space-between;
  }
  .cartBtn {
    border-radius: 9999px;
    position: absolute;
    bottom: 25px;
    right: 10px;
    width: 40px;
    height: 40px;
    display: none;
    color: rgb(18, 172, 44);
    border: 1px solid rgb(18, 172, 44);
  }

  &:hover {
    box-shadow: rgba(0, 0, 0, 0.3) 0px 4px 8px 0px, rgba(0, 0, 0, 0.1) 0px 0px 2px 1px;
    transition: box-shadow 300ms cubic-bezier(0.4, 0, 0.2, 1) 0s;
    cursor: pointer;

    .cartBtn {
      display: flex;

      &:hover {
        background-color: rgb(18, 172, 44);
        color: white;
      }
    }
  }

  & img {
    position: absolute;
    width: auto;
    max-width: 240px;
    max-height: 200px;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .product-img-box {
    position: relative;
    width: 100%;
  }

  .product-img-holder {
    display: flex;
    margin: 0px auto;
    height: 200px;
  }
}

.product::after {
  content: "";
  position: absolute;
  display: block;
  top: -2px;
  width: 90%;
  height: 1px;
  background-color: rgb(173, 173, 173);
}

.price {
  font-weight: bold;
  font-size: 1.2rem;
}

h2,
h3 {
  text-align: left;
  margin: 0.5rem 0;
}
</style>
