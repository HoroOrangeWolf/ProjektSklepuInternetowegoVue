<script setup>
import { inject, ref } from "@vue/runtime-core";
import { useToast } from "vue-toastification";

const { getCartItems, clearCart } = inject("cart");

const order = ref({});
const items = getCartItems();
const toast = useToast();
const isLoading = ref(false);

const validate = (orderBuff, items) => {
  if (orderBuff.paymentType === "") {
    toast.error("Wybierz rodzaj płatności");
    return false;
  }
  if (orderBuff.deliveryType === "") {
    toast.error("Wybierz rodzaj dostawy");
    return false;
  }
  if (items.length === 0) {
    toast.error("W twoim koszyku nie ma produktów");
    return false;
  }
  return true;
};

const addOrder = (orderBuff, items) => {
  if (!validate(orderBuff, items)) {
    return;
  }
  order.value = orderBuff;
  isLoading.value = true;
  const data = {
    paymentType: orderBuff.paymentType,
    deliveryType: orderBuff.deliveryType,
    productRequestList: items.map((f) => {
      const { count, id } = f;
      return {
        id,
        count,
      };
    }),
  };
  axios({
    method: "post",
    url: "/api/v1/order",
    data: data,
  })
    .then((response) => {
      const { paymentLink, totalPrice } = response.data;
      clearCart();
      if (data.paymentType === "PAYPAL") {
        toast.success("Złozono zamówienie, dokonaj teraz płatności", { timeout: 2000 });
        router.push({
          name: "payment",
          params: { paymentLink: paymentLink, totalPrice: totalPrice },
        });
      } else {
        toast.success("Złozono zamówienie pomyślnie", { timeout: 2000 });
        router.push({
          path: "/",
        });
      }
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};
</script>

<template>
  <div>
    <div class="wrapper">
      <div class="container">
        <router-view :items="items" :addOrder="addOrder" />
      </div>
    </div>
  </div>
</template>

<script>
import HeaderNoInput from "../HeaderNoInput.vue";
import { inject, ref } from "@vue/runtime-core";
import axios from "axios";
import router from "../../router";
import { useToast } from "vue-toastification";

export default {
  components: {
    HeaderNoInput,
  },
};
</script>

<style scoped lang="scss">
.wrapper {
  width: auto;
  display: flex;

  .container {
    display: flex;
    flex-direction: column;
    width: 80%;
    margin: 0 auto;
    margin-top: 50px;

    .breadcrumb {
      margin-right: auto;
    }
  }
}
</style>
