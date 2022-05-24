<script setup>
import { NCarousel, NSkeleton } from "naive-ui";
import { inject, onMounted } from "@vue/runtime-core";
import { ref } from "@vue/reactivity";
import axios from "axios";

const isProductLoading = ref(false);
const products = ref([]);
const promotionProducts = ref([]);

const fetchData = async () => {
  const data = await axios({
    method: "get",
    url: "/api/v1/product?limit=8",
  });

  const { list } = data.data;
  products.value = list;
  promotionProducts.value = list.slice(-4).reverse();
};

onMounted(() => {
  isProductLoading.value = true;
  fetchData().finally(() => {
    isProductLoading.value = false;
  });
});
</script>

<template>
  <div>
    <div class="main-content-box">
      <div class="offerts-box">
        <n-carousel autoplay>
          <img class="carousel-img" src="../../assets/karuzela1.png" />
          <img class="carousel-img" src="../../assets/karuzela2.png" />
          <img class="carousel-img" src="../../assets/karuzela3.png" />
        </n-carousel>
      </div>
      <div class="products-header">
        <h2>Polecamy</h2>
      </div>
      <div class="products">
        <div v-if="isProductLoading" class="productsContainer">
          <n-skeleton
            text
            v-for="index in 8"
            :key="index"
            style="border-radius: 5px"
            :height="120"
          />
        </div>
        <div v-else class="productsContainer">
          <div
            class="grid-products-elements"
            v-for="(product, index) in products"
            :key="index"
            @click="
              $router.push({ name: 'singleProduct', params: { id: product.id + '' } })
            "
          >
            <img class="productImg" :src="product.attachments[0].blob" alt="" />
            <p>{{ product.name }}</p>
            <p style="font-weight: bold; font-size: 15px">{{ product.price }}zł</p>
          </div>
        </div>
        <div class="promotions-header">
          <h2>Promocje</h2>
        </div>
        <div class="promotions-box">
          <div v-if="isProductLoading" style="width: 100%" class="promotions-box">
            <n-skeleton text v-for="index in 4" :key="index" style="height: 120px" />
          </div>
          <div
            class="promotion"
            v-for="(prom, index) in promotionProducts"
            :key="index"
            @click="$router.push({ name: 'singleProduct', params: { id: prom.id + '' } })"
          >
            <img class="productImg" :src="prom.attachments[0].blob" alt="" />
            <h3>Wielka promocja!!</h3>
            <p>Giga zniżka</p>
            <p style="word-wrap: break-word; font-size: 14px;">{{ prom.name }}</p>
            <del style="color: rgba(128, 128, 128, 1)">{{ prom.price * 1.2 }}zł</del>
            <p style="font-weight: bold">{{ prom.price }}zł</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.offerts-box {
  border: 2px solid gold;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
}

.offerts-box img {
  display: block;
}

.carousel-img {
  width: 100%;
  height: 240px;
  object-fit: cover;
}

.main-content-box {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.offerts-box {
  border: 2px solid gold;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
}

.offerts-box img {
  display: block;
}

.products-header,
.promotions-header,
.promotions-box,
.products {
  width: 80%;
  margin: 0 auto;
  .productsContainer {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    grid-template-rows: 1fr 1fr;
    gap: 5px;
  }
}

.grid-products-elements {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  flex-wrap: wrap;
  cursor: pointer;
  border: 2px solid rgba(128, 128, 128, 0.6);
  border-radius: 10px;

  &:hover {
    border: 2px solid rgb(117, 117, 117);
  }
}

.grid-products-elements img {
  width: 200px;
}

.promotions-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 80%;
  gap: 5px;
  font-size: 15px;
  flex: 1;
}

.promotion {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  cursor: pointer;
  border: 2px solid rgba(128, 128, 128, 0.6);
  border-radius: 10px;
  padding: 0px;
  width: 25%;

  &:hover {
    border: 2px solid rgb(117, 117, 117);
  }

  p {
    width: 80%;
  }
}

.productImg {
  width: 200px;
  height: 200px;
}

h2 {
  padding: 10px 0;
}
</style>
