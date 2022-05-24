<script setup>
import { inject, onBeforeMount, onMounted, ref } from "vue";
import { NRate, NSpace, NCarousel, NSkeleton, NIcon, useLoadingBar } from "naive-ui";
import { ArrowBack, ArrowForward } from "@vicons/ionicons5";
import CommentComp from "../Comment/CommentComp.vue";
import axios from "axios";
import { useToast } from "vue-toastification";

const props = defineProps({
  id: {
    required: true,
  },
});
const { addToCart } = inject("cart");

const value = ref(1);
const isAvailable = ref(true);
const item = ref({});
const isLoading = ref(true);
const toast = useToast();
const loadingBar = useLoadingBar();

const getItem = async () => {
  loadingBar.start();
  isLoading.value = true;
  try {
    const response = await axios({
      method: "get",
      url: "/api/v1/product/" + props.id,
    });
    loadingBar.finish();
    item.value = response.data;
  } catch (exc) {
    toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    loadingBar.error();
  } finally {
    isLoading.value = false;
  }
};

onMounted(async () => {
  await getItem();
});
</script>

<template>
  <div class="wrapper">
    <div
      class="main-content"
      v-if="isLoading"
      style="display: flex; flex-direction: column"
    >
      <n-skeleton
        style="margin-top: 20px; height: 40px; width: 100%"
        v-for="index in 5"
        :key="index"
        round
      />
    </div>
    <div class="main-content" v-else>
      <div class="imagePart">
        <n-carousel show-arrow autoplay>
          <img
            v-for="(img, index) in item.attachments"
            :key="index"
            class="carousel-img"
            :src="img.blob"
          />
          <template #arrow="{ prev, next }">
            <div class="custom-arrow">
              <button type="button" class="custom-arrow--left" @click="prev">
                <n-icon color="grey"><arrow-back style="transform: scale(2)" /></n-icon>
              </button>
              <button type="button" class="custom-arrow--right" @click="next">
                <n-icon color="grey">
                  <arrow-forward style="transform: scale(2)" />
                </n-icon>
              </button>
            </div>
          </template>
          <template #dots="{ total, currentIndex, to }">
            <ul class="custom-dots">
              <li
                v-for="index of total"
                :key="index"
                :class="{ ['is-active']: currentIndex === index - 1 }"
                @click="to(index - 1)"
                style="background-color: grey"
              />
            </ul>
          </template>
        </n-carousel>
      </div>
      <div class="infoPart">
        <div class="mainInfo">
          <h2>{{ item.name }}</h2>

          <div class="reviewPart">
            <n-rate readonly v-model:value="item.avgOpinion" />
          </div>
          <p class="from">od:{{ item.producer }}</p>
        </div>
        <div class="detailsPart">
          <div
            v-for="(info, index) in item.specifications"
            :key="index"
            class="detailsPart__info"
          >
            <span>{{ info.keyName }}:&nbsp;</span>
            <p class="partinfo">{{ info.keyValue }}</p>
          </div>

          <div class="detailsPart__info scrolling">
            <span class="scrollButton"><fa class="icon" icon="angles-down" /></span>
            <p>Przewiń do pełnej specyfikacji</p>
          </div>
        </div>

        <div class="userSelectPart">
          <p class="userSelectPart__price">{{ item.price }} zł</p>
          <div class="userSelectPart__buttons">
            <button class="userSelectPart__buttons__add" @click="addToCart(item)">
              <fa class="icon" icon="cart-shopping" />Dodaj do koszyka
            </button>
          </div>
          <p class="userSelectPart__info">Możesz kupić maks. 1 szt. na osobę</p>
          <hr />
          <div class="userSelectPart__available">
            <fa v-if="isAvailable" class="icon" icon="check" />
            <fa v-else class="icon notAvailable" icon="xmark" />
            <div class="userSelectPart__available__details">
              <p class="userSelectPart__available__details__info">Dostępny</p>
              <p class="userSelectPart__available__details__moreinfo">
                Dowiedz się więcej
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="specs">
      <h3>Opis przedmiotu</h3>
      <p class="specs__description">{{ item.description }}</p>
      <div class="specs__specifications">
        <h3>Specyfikacja</h3>
        <div
          v-for="(spec, index) in item.specifications"
          :key="index"
          class="specs__specifications__item"
        >
          <span class="specs__specifications__item__name">
            {{ spec.keyName }}
          </span>
          <span class="specs__specifications__item__value">
            {{ spec.keyValue }}
          </span>
        </div>
      </div>
    </div>
    <div class="comments">
      <comment-comp :itemId="id" />
    </div>
  </div>
</template>

<style lang="scss" scoped>
* {
  box-sizing: border-box;
}

.wrapper {
  width: 80%;
  margin: 0 auto;
  max-width: 100vw;

  .main-content {
    display: flex;
    min-height: 70vh;
    width: 100%;

    .imagePart {
      padding: 0 20px;
      width: 45%;
      .carousel-img {
        width: 100%;
        height: 100%;
        object-fit: fill;
      }

      .custom-arrow {
        display: flex;
        position: absolute;
        bottom: 25px;
        right: 10px;
      }

      .custom-arrow button {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 28px;
        height: 28px;
        margin-right: 12px;
        color: #fff;
        background-color: rgba(255, 255, 255, 0.1);
        border-width: 0;
        border-radius: 8px;
        transition: background-color 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        cursor: pointer;
      }

      .custom-arrow button:hover {
        background-color: rgba(255, 255, 255, 0.2);
      }

      .custom-arrow button:active {
        transform: scale(0.95);
        transform-origin: center;
      }

      .custom-dots {
        display: flex;
        margin: 0;
        padding: 0;
        position: absolute;
        bottom: 20px;
        left: 20px;
      }

      .custom-dots li {
        display: inline-block;
        width: 12px;
        height: 4px;
        margin: 0 3px;
        border-radius: 4px;
        background-color: rgba(255, 255, 255, 0.4);
        transition: width 0.3s, background-color 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        cursor: pointer;
      }

      .custom-dots li.is-active {
        width: 40px;
        background: #fff;
      }
    }
    .infoPart {
      width: 55%;
      display: grid;
      grid-template-areas:
        "mainInfo mainInfo"
        "detailsPart userSelectPart";
      grid-template-columns: 50% 50%;
      grid-template-rows: 100px 1fr;

      .mainInfo {
        border-bottom: 1px solid grey;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        grid-area: mainInfo;
        padding: 10px;

		h2{
			font-size: 16px;
		}

        .reviewPart {
          display: flex;
          justify-content: space-between;

          width: 140px;

          .star {
            color: #000;

            &.clicked {
              color: rgb(255, 255, 34);
            }
          }
        }
      }

      .detailsPart {
        grid-area: detailsPart;

        &__info {
          padding: 5px 0;
          border-bottom: 1px solid grey;
          p,
          span {
            display: inline-block;
          }
          text-align: left;

          span {
            font-weight: bold;
            padding: 0 5px;
          }

          .scrollButton {
            margin-right: 5px;
          }

          &.scrolling {
            cursor: pointer;
          }
        }
      }

      .userSelectPart {
        border: 1px solid rgb(179, 179, 179);
        border-top: 0;
        grid-area: userSelectPart;

        &__price {
          padding-top: 10px;
          font-weight: bold;
          font-size: 20px;
        }

        &__buttons {
          width: 90%;
          max-width: 300px;
          margin: 10px auto;

          display: flex;
          align-items: center;
          justify-content: space-evenly;
          padding: 10px;

          .select {
            width: 30%;
            max-width: 200px;
            min-width: 50px;

            .select > * {
              width: 100%;
            }
          }

          &__add {
            padding: 10px 15px;
            background-color: rgb(7, 170, 7);
            border: 0;
            border-radius: 20px;
            color: #fff;
            cursor: pointer;
            transition: 200ms all ease;
            display: flex;
            align-items: center;
            justify-content: space-evenly;

            &:hover {
              background-color: rgb(3, 94, 3);
            }
          }
        }

        &__available {
          display: flex;
          align-items: center;
          justify-content: space-evenly;
          width: 90%;
          max-width: 300px;
          margin: 20px auto;
          height: 10%;
          min-height: 50px;

          .icon {
            height: 100%;
            color: rgb(3, 94, 3);

            &.notAvailable {
              color: red;
            }
          }

          &__details {
            &__info {
              color: rgb(3, 94, 3);
              font-weight: bold;
            }
          }
        }
      }
    }
  }

  .specs {
    h3 {
      margin: 10px 0;
    }
    &__description {
      text-align: center;
      font-size: 16px;
    }

    &__specifications {
      width: 70%;
      margin: 0 auto;
      max-width: 1000px;

      &__item {
        margin: 0 auto;
        width: 70%;
        max-width: 800px;
        padding: 5px 0;
        display: flex;
        align-items: center;
        border-bottom: 1px solid rgba(128, 128, 128, 0.6);
        font-size: 15px;

        &:nth-child(even) {
          background-color: rgb(233, 231, 231);
        }

        &__name {
          font-weight: bold;
          width: 30%;
        }

        &__value {
          padding-left: 10px;
          text-align: left;
          flex: 1;
        }
      }
    }
  }

  .comments {
    margin-top: 2rem;
    border-top: 1px solid rgb(199, 199, 199);
  }
}

@media (max-width: 1180px) {
  .wrapper {
    .main-content {
      .infoPart {
        .mainInfo {
          h2 {
            font-size: 12px;
          }
        }

        .userSelectPart {
          &__buttons {
            &__add {
              padding: 5px 10px;
              font-size: 12px;
            }
          }
        }
      }
    }
  }
}

@media (min-width: 1500px) {
  .wrapper {
    width: 80%;
    margin: 0 auto;
    .main-content {
      .imagePart {
        width: 60%;
      }

      .infoPart {
        width: 40%;
      }

      .detailsPart {
        font-size: 15px;
      }
    }
  }
}
</style>
