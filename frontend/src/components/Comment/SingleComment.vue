<template>
  <div class="wrapper">
    <div class="wrapper__profile">
      <div class="wrapper__profile__info">
        <fa class="icon" icon="user-large" />
        <p class="wrapper__profile__info__name">{{ commentInfo.userName }}</p>
      </div>
    </div>
    <div class="wrapper__details">
      <div class="wrapper__details__detail">
        <div class="wrapper__details__detail__stars">
          <n-rate readonly :default-value="commentInfo.stars" />
        </div>
      </div>

      <div class="wrapper__details__content">
        <p class="wrapper__details__content__text">{{ commentInfo.text }}</p>
        <p>{{ formatAgo }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "@vue/runtime-core";
import { NRate, NButton } from "naive-ui";
import TimeAgo from "javascript-time-ago";

const timeAgo = new TimeAgo("pl");

const commentInfo = ref({});
const props = defineProps({
  comment: {
    default: {},
  },
  deleteCommentHandler: {
    default: undefined,
  },
});
commentInfo.value = props.comment;

const formatAgo = ref(timeAgo.format(new Date(commentInfo.value.creationTime)));
</script>

<style lang="scss" scoped>
.wrapper {
  border-top: 1px solid rgb(199, 199, 199);

  display: flex;
  font-size: 15px;

  &__profile {
    flex-basis: 20%;

    display: flex;
    align-items: center;
    justify-content: center;

    &__info {
      display: flex;
      gap: 1rem;
      align-items: center;
      justify-content: center;

      .icon {
        font-size: 2rem;
      }

      &__name {
        font-weight: bold;
      }
    }
  }

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
