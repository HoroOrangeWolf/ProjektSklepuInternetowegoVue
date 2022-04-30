<script setup>
import ProductBox from "../Product/ProductBox.vue";
import {
  NInput,
  NCheckbox,
  NSpace,
  NCard,
  NPagination,
  NButton,
  NSpin,
  NCheckboxGroup,
  NSkeleton,
} from "naive-ui";

import axios from "axios";
import { reactive, ref } from "@vue/reactivity";
import { inject, onMounted, watch } from "@vue/runtime-core";

const props = defineProps({
  categoryId: {
    default: -1,
  },
});

const allProducts = ref([]);

const allProducers = ref([]);
const categorySetup = ref(props.categoryId);

const price = ref({
  priceFrom: "0",
  priceTo: "2147483647",
});

const pager = ref(1);
const pageCount = ref(0);
const isLoading = ref(true);

const getAllProducts = async () => {
  isLoading.value = true;
  const url = `/api/v1/product?page=${pager.value - 1}&limit=9&categoryId=${
    categorySetup.value
  }`;

  const data = await axios({
    method: "get",
    url: url,
  });

  console.log(url, data);

  const { list, totalCount } = data.data;

  pageCount.value = Math.ceil(totalCount / 9);
  allProducts.value = list;
  isLoading.value = false;
};

const getAllProducers = async () => {
  const url = `/api/v1/product/producer?parentCategoryId=${categorySetup.value}`;
  const response = await axios({
    method: "get",
    url: url,
  });

  allProducers.value = response.data["list"];
};

watch(pager, (value, preValue) => {
  getAllProducts();
});

onMounted(async () => {
  await getAllProducts();
  getAllProducers();
});
</script>
<template>
  <div>
    <div class="path-box">
      <div class="path-list">
        <ul>
          <li>
            <div class="path-element">
              <a href="">e-commerce</a>
              <span class="next-path-tag">
                <fa class="icon" icon="arrow-right" />
              </span>
            </div>
          </li>
          <li>
            <div class="path-element">
              <a href="">Laptopy i komputery</a>
              <span class="next-path-tag">
                <fa class="icon" icon="arrow-right" />
              </span>
            </div>
          </li>
          <li>
            <div class="path-element">
              <a href="">Laptopy/Notebooki</a>
              <span class="next-path-tag">
                <fa class="icon" icon="arrow-right" />
              </span>
            </div>
          </li>
          <li>
            <div class="path-element">
              <a href="">Laptopy 2 w 1</a>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <div class="listing-header-box">
      <div class="listing-header">
        <h2>Laptopy 2 w 1</h2>
      </div>
    </div>

    <div class="products-content-wrapper">
      <div v-if="!isLoading" class="filter-content-wrapper">
        <div class="filter-box">
          <div class="filter-header-wrapper"></div>
          <div class="filter-headers">
            <h3>Producent</h3>
          </div>
          <div class="producents-checkboxes-box">
            <n-space item-style="display: flex;" vertical>
              <n-checkbox
                v-for="(data, index) in allProducers"
                :key="index"
                :value="data"
                :label="data"
              />
            </n-space>
          </div>
          <div class="filter-headers">
            <h3>Cena</h3>
          </div>
          <div class="price-range-box">
            <n-input
              class="search-box"
              placeholder="Od"
              @update:value="(v) => (price.priceFrom = v)"
              round
            >
            </n-input
            >zł
            <fa class="icon" icon="minus" />
            <n-input
              class="search-box"
              placeholder="Do"
              @update:value="(v) => (price.priceTo = v)"
              round
            >
            </n-input
            >zł
          </div>
          <n-button class="filter-button"> Filtruj </n-button>
        </div>
      </div>

      <n-spin v-if="isLoading" class="spinner" size="large" />

      <div v-else class="products-box">
        <div class="products-header">
          <h2>Produkty</h2>
          <n-pagination :page-count="pageCount" v-model:page="pager" />
        </div>

        <product-box
          v-for="(data, index) in allProducts"
          :item="data"
          v-bind:key="index"
        />
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.products-content-wrapper {
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.spinner {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  transform: scale(2.5);
}

.products-box {
  display: flex;
  width: 60%;
  padding: 1rem;
  flex-wrap: wrap;
  text-align: left;
}

.products-header {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

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

.filter-box {
  width: 100%;
  display: flex;
  flex-direction: column;
  padding: 1rem;
  border: 1px solid rgb(199, 199, 199);
  border-radius: 10px;
}

h2,
h3 {
  text-align: left;
  margin: 0.5rem 0;
}

.path-box {
  display: flex;
  justify-content: center;
  align-items: center;
}

.path-list {
  width: 80%;
  text-align: left;
}

.path-list ul {
  list-style-type: none;
  margin: 0 1rem;
}

.path-list li {
  display: inline-block;
}

.next-path-tag {
  margin: 0 0.2rem;
}

.listing-header-box {
  display: flex;
  justify-content: center;
  align-items: center;
}

.listing-header {
  width: 80%;
  padding: 0 1rem;
}

.filter-header-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.filter-button {
  border-radius: 20px;
  width: 100px;
  margin: 11px 0px 0px auto;
}
.producents-checkboxes-box {
  width: 100%;
}

.filter-headers {
  width: 100%;
  text-align: left;
}

.filter-content-wrapper {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 20%;
}

.price-range-box {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
}

.price-range-box .icon {
  margin: 0 0.5rem;
  opacity: 80%;
}
</style>
