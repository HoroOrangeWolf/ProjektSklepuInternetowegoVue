import axios from "axios";
import { ref } from "vue";
import { VueCookies as cookie } from "vue-cookies";

const isLoadedUser = ref(false);

const userModel = ref({
    email: '',
    isAuthenticated: false,
    role: 'GUEST',
});

const clearModel = () =>{
    userModel.value = {
        email: '',
        isAuthenticated: false,
        role: 'GUEST'
    };
}

const getAutheticatedUser = async () => {

    const user = await axios({
        method: 'get',
        url: '/api/v1/user/authenticatedUser',
    })

    console.log(user);

    userModel.value = user.data;
    isLoadedUser.value = userModel.value.role !== 'GUEST';
}

const setUser = (user) =>{
    userModel.value = user;
}

const isLoaded = ()=>{
    return isLoadedUser.value;
}

export {getAutheticatedUser, isLoaded, setUser, clearModel, isLoadedUser};

export default userModel;