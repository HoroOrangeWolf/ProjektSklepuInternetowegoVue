<template>
  <div class="wrapper">
    <h2>Opinie({{ commentList.length }})</h2>
    <form v-if="isLogin" class="wrapper__form" @sumbit.prevent>
      <n-rate v-model:value="stars" v-if="showButtons" class="wrapper__form__stars" />

      <textarea
        ref="opinionText"
        @click="changeButtons()"
        class="wrapper__form__opinion"
        placeholder="Dodaj opinie"
      ></textarea>
      <button @click="addOpinion($event)" v-if="showButtons" class="wrapper__form__add">
        DODAJ
      </button>
    </form>
    <div class="wrapper__comments">
      <SingleComment
        :comment="comment"
        v-for="(comment, index) in commentList"
        :key="index"
      />
    </div>
  </div>
</template>

<script>
import SingleComment from "./SingleComment.vue";
import axios from "axios";
import { NRate } from "naive-ui";
import { onMounted, ref } from "@vue/runtime-core";
import { isLoaded } from "../User/GlobalVariables";
export default {
  components: { SingleComment, NRate },
  props: ["item"],
  name: "CommentComp",
  setup(props) {
    const commentList = ref([]);
    const showButtons = ref(false);
    const opinionText = ref(null);
    const isLogin = ref(false);
    const stars = ref(0);
    const getComments = async () => {
      const data = await axios({
        method: "get",
        url: `/api/v1/opinion?opinionsFor=PRODUCT&id=${props.item.id}&limit=9000`,
      });

      //console.log(data.data.list);
      commentList.value = data.data.list;
      isLogin.value = isLoaded();
      //console.log(isLogin.value);
      console.log(props.item);
    };

    const changeButtons = () => {
      showButtons.value = true;
    };

    const addOpinion = (event) => {
      event.preventDefault();

      let text = opinionText.value.value;
      if (text.length < 5 || stars.value < 1) {
        opinionText.value.value = "";
        stars.value = 0;
        return;
      }

      axios({
        method: "post",
        url: "/api/v1/opinion/user",
        data: { text: text, stars: stars.value, productId: props.item.id },
      }).then((res) => {
        opinionText.value.value = "";
        stars.value = 0;
        showButtons.value = false;
        getComments();
      });
    };

    onMounted(async () => {
      await getComments();
    });

    return {
      getComments,
      commentList,
      showButtons,
      changeButtons,
      addOpinion,
      opinionText,
      stars,
      isLogin,
    };
  },
};
</script>

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
