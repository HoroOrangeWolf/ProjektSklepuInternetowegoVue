import axios from "axios";
import { ref } from "vue";
import { VueCookies as cookie } from "vue-cookies";

const isLoadedUser = ref(false);
const isSocialAcc = ref(false);

const userModel = ref({
  email: "",
  isAuthenticated: false,
  role: "GUEST",
});

const clearModel = () => {
  userModel.value = {
    email: "",
    isAuthenticated: false,
    role: "GUEST",
  };
  isLoadedUser.value = false;
};

const getAutheticatedUser = async () => {
  const user = await axios({
    method: "get",
    url: "/api/v1/user/authenticatedUser",
  });
  userModel.value = user.data;
  isLoadedUser.value = userModel.value.role !== "GUEST";
};

const getIsSocialAccount = async () => {
  const data = await axios({
    method: "get",
    url: "/api/v1/user/authenticatedUser",
  });
  isSocialAcc.value = data.data.isSocialAccount;
  return isSocialAcc.value;
};

const getUserData = () => {
  return userModel.value;
};

const setUser = (user) => {
  userModel.value = user;
};

const isLoaded = () => {
  return isLoadedUser.value;
};

export {
  getAutheticatedUser,
  isLoaded,
  setUser,
  clearModel,
  isLoadedUser,
  getIsSocialAccount,
  getUserData,
};

export default userModel;
