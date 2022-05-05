<template>
  <div style="margin-top: 20px">
    <div class="products-content-wrapper">
      <div class="filter-content-wrapper">
        <div class="filter-box">
          <div class="filter-header-wrapper"></div>
          <div class="filter-headers">
            <h3>Producent</h3>
          </div>
          <div class="producents-checkboxes-box">
            <div v-if="isProducersLoading">
              <n-skeleton v-for="i in 5" :key="i" style="margin-top: 5px" />
            </div>
            <n-checkbox-group
              :max="1"
              v-else
              @update:value="(v) => producerSelectHandler(v)"
              :default-value="query.searchBy ? [query.searchBy] : []"
            >
              <n-space item-style="display: flex;" vertical>
                <n-checkbox
                  v-for="(data, index) in allProducers"
                  :key="index"
                  :value="data"
                  :label="data"
                />
              </n-space>
            </n-checkbox-group>
          </div>
          <div class="filter-headers">
            <h3>Cena</h3>
          </div>
          <div class="price-range-box">
            <n-input
              class="search-box"
              placeholder="Od"
              :default-value="query.priceFrom"
              @update:value="
                (v) =>
                  v.length > 0 ? (query.priceFrom = v) : (query.priceFrom = undefined)
              "
              round
            >
            </n-input
            >zł
            <fa class="icon" icon="minus" />
            <n-input
              class="search-box"
              placeholder="Do"
              :default-value="query.priceTo"
              @update:value="
                (v) => (v.length > 0 ? (query.priceTo = v) : (query.priceTo = undefined))
              "
              round
            >
            </n-input
            >zł
          </div>
          <n-button
            v-if="!(isLoading && isProducersLoading)"
            class="filter-button"
            @click="onFilterClick()"
          >
            Filtruj
          </n-button>
        </div>
      </div>

      <div class="products-box" v-if="isLoading">
        <n-skeleton v-for="i in 9" :key="i" class="skeletonProduct" />
      </div>
      <div class="products-box" v-else>
        <div class="products-header">
          <h2>Produkty</h2>
          <n-select
            placeholder="Wybierz sortowanie"
            :options="optionsSelect"
            @update:value="(v) => pickSortType(v)"
            class="selector"
          />
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
<script setup>
import ProductBox from "../Product/ProductBox.vue";
import {
  NInput,
  NCheckbox,
  NSpace,
  NCard,
  NPagination,
  NButton,
  NSelect,
  NSpin,
  NCheckboxGroup,
  NSkeleton,
} from "naive-ui";

import axios from "axios";
import { reactive, ref } from "@vue/reactivity";
import { inject, onMounted, onUpdated, watch } from "@vue/runtime-core";
import router from "../../router";
import { useRoute } from "vue-router";
import { useToast } from "vue-toastification";

const props = defineProps({
  id: {
    required: true,
  },
});

const { searchBy, priceFrom, priceTo, sort } = useRoute().query;

const query = ref({ searchBy, priceFrom, priceTo, sort });

const allProducts = ref([]);
const allProducers = ref([]);
const categorySetup = ref(props.id);
const isProducersLoading = ref(false);
const pager = ref(1);
const pageCount = ref(0);
const isLoading = ref(true);
const toast = useToast();

const producerSelectHandler = (v) => {
  query.value.searchBy = v[0];
};

const optionsSelect = [
  {
    label: "Rosnąco po cenie",
    value: "ASC",
  },
  {
    label: "Malejąco po cenie",
    value: "DESC",
  },
];

const getAllProducts = async () => {
  isLoading.value = true;
  const url = `/api/v1/product`;

  const data = await axios({
    method: "get",
    url: url,
    params: {
      page: pager.value - 1,
      limit: 9,
      categoryId: categorySetup.value,
      ...query.value,
    },
  });

  const { list, totalCount } = data.data;

  pageCount.value = Math.ceil(totalCount / 9);
  allProducts.value = list;
};

const getAllProducers = async () => {
  isProducersLoading.value = true;
  const url = `/api/v1/product/producer?parentCategoryId=${categorySetup.value}`;
  const response = await axios({
    method: "get",
    url: url,
  });

  allProducers.value = response.data["list"];
};

const pickSortType = (event) => {
  query.value["sortBy"] = "price";
  query.value["sort"] = event;
  router.replace({
    params: { id: props.id },
    query: {
      ...query.value,
    },
  });
  getAllProducts()
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};

const onFilterClick = () => {
  router.replace({
    params: { id: props.id },
    query: {
      ...query.value,
    },
  });
  getAllProducts()
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};

watch(pager, (value, preValue) => {
  getAllProducts().finally(() => {
    isLoading.value = false;
  });
});

watch(props, () => {
  categorySetup.value = props.id;
  getAllProducts().finally(() => {
    isLoading.value = false;
  });
  getAllProducers().finally(() => {
    isProducersLoading.value = false;
  });
});

onMounted(() => {
  getAllProducts().finally(() => {
    isLoading.value = false;
  });
  getAllProducers().finally(() => {
    isProducersLoading.value = false;
  });
});
</script>
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

.skeletonProduct {
  width: 30%;
  min-width: 300px;
  border-radius: 10px;
  position: relative;
  margin: 0 0.2rem;
  margin-bottom: 0.2rem;
  padding: 1rem;
  min-height: 250px;
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
  align-items: center;

  .selector {
    width: 20%;
  }
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
