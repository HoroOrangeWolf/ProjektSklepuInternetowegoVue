<template>
  <div style="background-color: white">
    <n-form class="form">
      <n-form-item label="Wybierz sposób płatnośći">
        <n-select :options="paymentOptions" v-model:value="orderModel.paymentType" />
      </n-form-item>
      <n-form-item label="Wybierz sposób dostawy">
        <n-select :options="deliveryOptions" v-model:value="orderModel.deliveryType" />
      </n-form-item>
      <n-form-item label="Uwagi do zamówienia">
        <n-input type="textarea" placeholder="" v-model:value="orderModel.remarks" />
      </n-form-item>
      <n-form-item>
        <n-button class="btn" @click="addOrder(orderModel, items)">
          {{ paymentType === "PAYPAL" ? "Przejdz do płatności" : "Złóż zamówienie" }}
        </n-button>
      </n-form-item>
      <h3 v-if="orderModel.paymentType !== 'PAYPAL'">
        Dane do przelewu: 49 1020 2892 2276 3005 0000 0000
      </h3>
    </n-form>
  </div>
</template>

<script>
import { NForm, NFormItem, NSelect, NButton, NInput } from "naive-ui";
import { ref } from "@vue/reactivity";

const orderModel = ref({
  paymentType: "",
  deliveryType: "",
  remarks: "",
});

const paymentOptions = [
  {
    label: "Paypal",
    value: "PAYPAL",
  },
  {
    label: "Płatność przy odbiorze",
    value: "ON_SITE",
  },
  {
    label: "Przelew",
    value: "TRANSFER",
  },
];

const deliveryOptions = [
  {
    label: "Kurier",
    value: "COURIER",
  },
  {
    label: "Odbiór na miejscu",
    value: "ON_SITE",
  },
  {
    label: "InPost",
    value: "IN_POST",
  },
];

export default {
  setup() {
    return {
      paymentOptions,
      deliveryOptions,
      orderModel,
    };
  },
  components: {
    NForm,
    NFormItem,
    NSelect,
    NButton,
    NInput,
  },
  props: {
    addOrder: {
      default: (v, v2) => {},
    },
    items: {
      default: [],
    },
  },
};
</script>

<style scoped lang="scss">
@import "../../style/colors.scss";
.form {
  background-color: rgba(128, 128, 128, 0.3);
  border-radius: 11px;
  width: 70%;
  padding: 20px;
}
.btn {
  background-color: $base-color;
  margin-left: auto;
  color: white;
  min-width: 200px;
  min-height: 40px;
  border-radius: 11px;
}
</style>
