<template>
  <div class="wrapper">
    <div class="wrapper__details">
      <div class="wrapper__details__detail">
        <div
          class="wrapper__details__detail__productName"
          @click="navigateToSingleProduct()"
        >
          <p>Nazwa produktu: {{ commentInfo.productName }}</p>
        </div>
        <div class="wrapper__details__detail__stars">
          <n-rate readonly :default-value="commentInfo.stars" />
        </div>
      </div>
      <div class="wrapper__details__content">
        <p class="wrapper__details__content__text">Opinia: {{ commentInfo.text }}</p>
        <p class="wrapper__details__content__text">{{ formatAgo }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "@vue/runtime-core";
import { NRate, NButton } from "naive-ui";
import router from "../../router";
import TimeAgo from "javascript-time-ago";

const timeAgo = new TimeAgo("pl");
const commentInfo = ref({});

const props = defineProps({
  comment: {
    default: {},
  },
});
commentInfo.value = props.comment;

const formatAgo = ref(timeAgo.format(new Date(commentInfo.value.creationTime)));

const navigateToSingleProduct = () => {
  router.push({ name: "singleProduct", params: { id: commentInfo.value.productId } });
};
</script>

<style lang="scss" scoped>
.wrapper {
  border-top: 1px solid rgb(199, 199, 199);

  display: flex;
  font-size: 15px;

  &__details {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-top: 0.6rem;

    &__detail {
      display: flex;

      padding: 0.6rem;
      gap: 1rem;
      justify-content: space-between;
      align-items: center;

      &__productName {
        font-weight: bold;

        p {
          &:hover {
            color: rgba(black, 0.6);
          }
        }
      }

      &__stars {
        span {
          margin: 0 0.2rem;
        }
      }
    }

    &__info {
      display: flex;

      padding: 0.4rem;
    }

    &__content {
      text-align: left;
      padding: 0.7rem;
    }
  }
}

.wrapper:last-child {
  border-bottom: 1px solid rgb(199, 199, 199);
}
</style>
