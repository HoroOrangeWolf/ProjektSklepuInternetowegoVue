<script setup>
import SingleComment from "./SingleComment.vue";
import axios from "axios";
import { NRate, NList, NListItem, NSkeleton, NInput } from "naive-ui";
import { onMounted, ref } from "@vue/runtime-core";
import { isLoaded } from "../GlobalContext/GlobalVariables";
import { useToast } from "vue-toastification";

const props = defineProps({
  itemId: {
    required: true,
  },
});

const commentList = ref([]);
const showButtons = ref(false);
const opinionText = ref("");
const isLogin = ref(false);
const isLoading = ref(false);
const stars = ref(0);
const toast = useToast();

const getComments = () => {
  isLoading.value = true;
  axios({
    method: "get",
    url: `/api/v1/opinion?opinionsFor=PRODUCT&id=${props.itemId}&limit=9000`,
  })
    .then((response) => {
      commentList.value = response.data.list;
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
  isLogin.value = isLoaded();
};

const addOpinion = (event) => {
  event.preventDefault();

  let text = opinionText.value;
  if (text.length === 0) {
    toast.error("Opinia musi zawierać jakiś tekst", { timeout: 2000 });
    return;
  }
  if (stars.value <= 0 || stars.value >= 6) {
    toast.error("Opinia musi mieć ocene", { timeout: 2000 });
    return;
  }
  isLoading.value = true;
  axios({
    method: "post",
    url: "/api/v1/opinion/user",
    data: { text: text, stars: stars.value, productId: props.itemId },
  })
    .then((res) => {
      opinionText.value = "";
      stars.value = 0;
      showButtons.value = false;
      toast.success("Dodano opinie", { timeout: 2000 });
      getComments();
    })
    .catch((exc) => {
      console.log(exc);
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
      isLoading.value = false;
    });
};

onMounted(() => {
  isLoading.value = true;
  getComments();
});

const changeButtons = () => {
  showButtons.value = true;
};
</script>

<template>
  <div class="wrapper">
    <h2>Opinie({{ commentList.length }})</h2>
    <form v-if="isLogin && !isLoading" class="wrapper__form" @sumbit.prevent>
      <n-rate v-model:value="stars" v-if="showButtons" class="wrapper__form__stars" />

      <n-input
        v-model:value="opinionText"
        @click="changeButtons()"
        class="wrapper__form__opinion"
        placeholder="Dodaj opinie"
        type="textarea"
      ></n-input>
      <button @click="addOpinion($event)" v-if="showButtons" class="wrapper__form__add">
        DODAJ
      </button>
    </form>
    <n-list v-if="isLoading">
      <n-list-item v-for="index in 9" :key="index">
        <n-skeleton :height="135" />
      </n-list-item>
    </n-list>
    <n-list v-else>
      <n-list-item v-for="(comment, index) in commentList" :key="index">
        <single-comment :comment="comment" />
      </n-list-item>
    </n-list>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  &__form {
    border: 1px solid rgb(199, 199, 199);
    width: 50%;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    padding: 0.6rem;

    &__opinion {
      width: 60%;
      min-height: 100px;
      padding: 0.5rem;
      margin: 0.7rem auto;
    }

    &__add {
      display: block;
      margin-left: auto;
      padding: 0.6rem 1.5rem;
      border-radius: 20px;
      border: 0;
      color: #fff;
      background-color: rgb(7, 170, 7);
      transition: 200ms all ease-in-out;
      cursor: pointer;

      &:hover {
        background-color: rgb(3, 94, 3);
      }
    }
  }

  &__comments {
    & > * {
      margin-top: 0.7rem;
    }
  }
}
</style>
