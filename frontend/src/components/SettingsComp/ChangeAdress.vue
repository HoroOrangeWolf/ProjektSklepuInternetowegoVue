<template>
  <div class="wrapper">
    <n-spin :show="isLoading">
      <h2>Zmiana adresu</h2>
      <n-space vertical class="wrapper__form">
        <label for="postalcode">Kod pocztowy:</label>
        <n-input
          type="text"
          placeholder="Wprowadz kod pocztowy"
          :maxlength="10"
          :minlength="1"
          v-model:value="postalcodeValue"
          id="postalcode"
        />
        <label for="homenumber">Numer domu:</label>
        <n-input
          type="text"
          placeholder="Wprowadz numer domu"
          :maxlength="10"
          :minlength="1"
          v-model:value="homenumberValue"
          id="homenumber"
        >
        </n-input>
        <label for="streetname">Nazwa ulicy:</label>
        <n-input
          type="text"
          placeholder="Wprowadz nazwe ulicy"
          :maxlength="255"
          :minlength="2"
          v-model:value="streetnameValue"
          id="streetname"
        />
        <label for="cityname">Nazwa miasta:</label>
        <n-input
          type="text"
          placeholder="Wprowadz nazwe miasta"
          :maxlength="255"
          :minlength="2"
          v-model:value="citynameValue"
          id="cityname"
        />
      </n-space>
      <n-button @click="changeAdress" class="wrapper__button" type="primary">
        Zmień adres
      </n-button>
    </n-spin>
  </div>
</template>

<script setup>
import { NSpace, NInput, NButton, NSpin } from "naive-ui";
import axios from "axios";
import { ref } from "vue";
import { useToast } from "vue-toastification";
import { getUserData } from "../GlobalContext/GlobalVariables";
const userData = ref(getUserData());
const { postCode, homeNumber, street, city } = userData.value.address;

const postalcodeValue = ref(postCode);
const homenumberValue = ref(homeNumber);
const streetnameValue = ref(street);
const citynameValue = ref(city);
const isLoading = ref(false);

const toast = useToast();
let regex = new RegExp("[0-9]{2}-[0-9]{3}");

const changeAdress = () => {
  console.log(postalcodeValue.value);
  if (!regex.test(postalcodeValue.value)) {
    toast.error("Błędnie wprowadzone dane!", { timeout: 2000 });
    postalcodeValue.value = postCode;
    homenumberValue.value = homeNumber;
    streetnameValue.value = street;
    citynameValue.value = city;
    return;
  }
  if (homenumberValue.value.length < 1 || homenumberValue.value.length > 5) {
    toast.error("Błędnie wprowadzone dane!", { timeout: 2000 });
    postalcodeValue.value = postCode;
    homenumberValue.value = homeNumber;
    streetnameValue.value = street;
    citynameValue.value = city;
    return;
  }

  if (streetnameValue.value.length < 2 || streetnameValue.value.length > 255) {
    toast.error("Błędnie wprowadzone dane!", { timeout: 2000 });
    postalcodeValue.value = postCode;
    homenumberValue.value = homeNumber;
    streetnameValue.value = street;
    citynameValue.value = city;
    return;
  }
  if (citynameValue.value.length < 2 || citynameValue.value.length > 255) {
    toast.error("Błędnie wprowadzone dane!", { timeout: 2000 });
    postalcodeValue.value = postCode;
    homenumberValue.value = homeNumber;
    streetnameValue.value = street;
    citynameValue.value = city;
    return;
  }
  isLoading.value = true;
  axios({
    method: "put",
    url: "/api/v1/user/address",
    data: {
      postCode: postalcodeValue.value,
      homeNumber: homenumberValue.value,
      street: streetnameValue.value,
      city: citynameValue.value,
    },
  })
    .then((response) => {
      toast.success("Zmieniono dane!", { timeout: 2000 });
      postalcodeValue.value = postalcodeValue.value;
      homenumberValue.value = homenumberValue.value;
      streetnameValue.value = streetnameValue.value;
      citynameValue.value = citynameValue.value;
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
    });
};
</script>

<style scoped lang="scss">
.wrapper {
  margin-top: 20px;
  &__form {
    width: 50%;
    margin: 10px auto 0 auto;
  }

  &__button {
    margin-top: 10px;
    padding: 5px 20px;
  }
}
</style>
