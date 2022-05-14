<script setup>
import { h, onMounted, ref, watch } from "@vue/runtime-core";
import axios from "axios";
import TimeAgo from "javascript-time-ago";
const timeAgo = new TimeAgo("pl");
const page = ref(0);
const maxPage = ref(1);
const orders = ref([]);
const isLoading = ref(false);
const toast = useToast();

const loadData = async () => {
  isLoading.value = true;
  const response = await axios({
    method: "get",
    url: "/api/v1/order?limit=999999",
  });

  const { totalCount, list } = response.data;
  maxPage.value = Math.ceil(totalCount / 20);
  orders.value = list;
};

const cancelOrder = (id) => {
  isLoading.value = true;
  axios({
    method: "put",
    url: "api/v1/order/" + id + "/cancel/user",
  })
    .then(() => {
      toast.success("Zamówienie zostało anulowane.", { timeout: 2000 });
      return loadData();
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};

onMounted(() => {
  loadData().finally(() => {
    isLoading.value = false;
    console.log(orders.value);
  });
});

watch(page, () => {
  loadData().finally(() => {
    isLoading.value = false;
  });
});

const mapPaymentType = (type) => {
  switch (type) {
    case "PAYPAL":
      return "PayPal";
    case "TRANSFER":
      return "Przelew";
    case "ON_SITE":
      return "Płatność przy odbiorze";
    default:
      return "";
  }
};

const mapDeliveryType = (type) => {
  switch (type) {
    case "ON_SITE":
      return "Odbiór Osobisty";
    case "IN_POST":
      return "InPost";
    case "COURIER":
      return "Kurier";
    default:
      return "";
  }
};

const mapPaymentStatus = (type) => {
  switch (type) {
    case "PROCESSED":
      return '<div>Status płatności:&nbsp; </div><div style="color: green">Przetworzono</div>';
    case "SUCCESS":
      return '<div>Status płatności:&nbsp;</div><div style="color: green">Zapłacono</div>';
    case "DENIED":
      return '<div>Status płatności:&nbsp;</div><div style="color: red">Płatność odrzucona</div>';
    case "CANCELED":
      return '<div>Status płatności:&nbsp;</div><div style="color: red">Płatność anulowana</div>';
    case "WAITING_FOR_PAYMENT":
      return '<div>Status płatności:&nbsp;</div><div style="color: red;">Oczekiwanie na płatność</div>';
    default:
      return "<div>Status płatności:&nbsp;</div>";
  }
};

const mapDeliveryStatus = (type) => {
  switch (type) {
    case "DELIVERED":
      return '<div>Status dostawy:&nbsp; </div><div style="color: green">Dostarczono</div>';
    case "IN_PREPARATION":
      return '<div>Status dostawy:&nbsp;</div><div style="color: orange">W przygotowaniu</div>';
    case "DELIVERING":
      return '<div>Status dostawy:&nbsp;</div><div style="color: red">W trasie</div>';
    default:
      return "<div>Status dostawy:&nbsp;</div>";
  }
};
</script>

<template>
  <div>
    <h1>Twoje zamówienia</h1>
    <n-list v-if="isLoading" class="listItem">
      <n-list-item v-for="i in 6" :key="i">
        <n-skeleton class="item" style="height: 135px" />
      </n-list-item>
    </n-list>
    <n-list class="listItem" v-else>
      <n-list-item v-for="(data, index) in orders" :key="index">
        <div class="item">
          <div class="left">
            <div>
              <b>Nr zamówienia: {{ data.id }}</b>
            </div>
            <div>
              <b>Rodzaj płatności: {{ mapPaymentType(data.paymentType) }}</b>
            </div>
            <div>
              <b>Rodzaj dostawy: {{ mapDeliveryType(data.deliveryType) }}</b>
            </div>
            <div v-html="mapPaymentStatus(data.paymentStatus)" />
            <div v-html="mapDeliveryStatus(data.shipmentStatus)" />
          </div>
          <div class="right">
            <div class="bottom">
              <div>
                Kwota dostawy: {{ data.totalPrice }} zł
                <div v-html="timeAgo.format(new Date(data.creationTime))" />
              </div>

              <a
                :href="data.payLink"
                v-if="
                  data.paymentStatus === 'WAITING_FOR_PAYMENT' &&
                  data.paymentType === 'PAYPAL'
                "
              >
                <n-button class="btn">
                  <template #default> Zapłać </template>
                  <template #icon>
                    <img
                      src="../../assets/paypal-svg.svg"
                      width="30"
                      height="20"
                      alt=""
                    />
                  </template>
                </n-button>
              </a>
              <n-button
                v-if="
                  data.paymentType === 'PAYPAL' &&
                  data.paymentStatus === 'WAITING_FOR_PAYMENT'
                "
                @click="cancelOrder(data.id)"
              >
                Anuluj zamówienie
              </n-button>
            </div>
          </div>
        </div>
      </n-list-item>
    </n-list>
  </div>
</template>

<script>
import { NList, NListItem, NButton, NSkeleton } from "naive-ui";
import { useToast } from "vue-toastification";

export default {
  components: {
    NList,
    NListItem,
    NButton,
    NSkeleton,
  },
};
</script>

<style lang="scss" scoped>
@import "../../style/colors.scss";

.listItem {
  width: 100%;
  .item {
    display: flex;
    flex-direction: row;
    .left {
      width: 40%;
      text-align: left;
      border-right: 1px solid rgba(128, 128, 128, 0.6);

      & > * {
        margin: 4px 0;
        display: flex;
        font-weight: bold;
      }
    }
    .right {
      width: 60%;
      position: relative;
      .bottom {
        position: absolute;
        width: 100%;
        bottom: 0;
        display: flex;
        justify-content: space-between;
        a {
          text-decoration: none;
        }

        &:first-child {
          font-weight: bold;
          margin: 0 10px;
          text-align: left;
        }
        .btn {
          background-color: $base-color;
          min-width: 200px;
          min-height: 40px;
          border-radius: 11px;
          font-size: 1rem;
          color: white;

          &:hover {
            background-color: rgba($base-color, 0.8);
          }
        }
      }
    }
  }
}
</style>
>
