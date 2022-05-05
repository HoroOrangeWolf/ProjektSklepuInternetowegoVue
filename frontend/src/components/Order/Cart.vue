<template>
  <div style="width: 80%; margin: 0 auto; margin-top: 20px">
    <n-list class="list">
      <template #header>
        <p style="font-size: 1rem; font-weight: bold">Twój koszyk</p>
      </template>
      <n-list-item v-for="(data, index) in items" :key="index">
        <template #prefix>
          <n-image
            class="img"
            width="150"
            height="150"
            :src="data.attachments.length > 0 ? data.attachments[0].blob : ''"
            alt="No Img!"
          />
        </template>
        <template #default>
          <div class="inside">
            <div class="productName">Produkt: {{ data.name }}</div>
            <div class="productCount">{{ data.count }} szt.</div>
            <div class="productPrice">
              {{ `Cena szt: ${data.price} zł` }}
              <n-button type="text" class="removeBtn" @click="removeCart(index)">
                <fa icon="x" />
              </n-button>
            </div>
          </div>
        </template>
      </n-list-item>
    </n-list>
    <div class="summary">
      <n-card
        class="summaryBlock"
        :segmented="{
          content: true,
          footer: true,
        }"
      >
        <template #header>
          <div class="cardHeader">Podsumowanie twojego koszyka</div>
        </template>
        <template #default>
          <div class="cardContent">
            Calłkowity koszt twojego zamówienia to: {{ totalPrice }} zł
          </div>
        </template>
        <template #footer>
          <div class="cardFooter">
            <n-button class="button" @click="$router.push({ name: 'summary' })">
              <div style="font-weight: 700">Złóż zamówienie</div>
            </n-button>
          </div>
        </template>
      </n-card>
    </div>
  </div>
</template>

<script>
import { NList, NListItem, NImage, NCard, NButton } from "naive-ui";
import { inject, ref } from "vue";

const items = ref([]);

export default {
  setup() {
    const { getCartItems, removeCartByIndex } = inject("cart");
    items.value = getCartItems();
    return {
      items,
      removeCart: (index) => {
        removeCartByIndex(index);
        items.value = getCartItems();
      },
    };
  },
  components: {
    NList,
    NListItem,
    NImage,
    NCard,
    NButton,
  },
  computed: {
    totalPrice() {
      let total = 0.0;
      this.items.forEach((f) => {
        total += f.price * f.count;
      });
      return total;
    },
  },
};
</script>

<style scoped lang="scss">
@import "../../style/colors.scss";
.list {
  flex: 2;
  border: 1px solid rgba(128, 128, 128, 0.4);
  border-radius: 11px;
  .img {
    margin: 10px;
  }
  .inside {
    width: 100%;
    height: 100%;
    display: flex;

    .productName {
      flex: 2;
      text-align: left;
    }
    .productCount {
      flex: 1;
      color: rgb(128, 128, 128);
    }

    .productPrice {
      flex: 1;
    }

    .removeBtn {
      &:hover {
        color: red;
      }
    }
  }
}
.summary {
  flex: 1;
  margin-top: 20px;
  .summaryBlock {
    background-color: rgba(128, 128, 128, 0.3);
    border-radius: 10px;
    width: 90%;
    margin: auto;

    .cardHeader {
      text-align: left;
      font-weight: 700;
    }
    .cardContent {
      font-weight: 500;
      font-size: 0.9rem;
      text-align: left;
    }
    .cardFooter {
      display: flex;
      justify-content: right;
      .button {
        background-color: $base-color;
        min-width: 200px;
        min-height: 40px;
        border-radius: 12px;
        font-weight: 700;
        font-size: 17px;
        color: white;
      }
    }
  }
}
</style>
