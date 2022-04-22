import axios from "axios";
import { ref } from "vue";

const isLoadedUser = ref(false);

const userModel = ref({
    email: 'Hello',
    isAuthenticated: false,
    role: 'GUEST',
});

const getAutheticatedUser = async () => {
    const user = await axios({
        method: 'get',
        url: '/api/v1/user/authenticatedUser',
    })
    userModel.value = user.data;
    isLoadedUser.value = true;
    console.log('Test',user.data);
}

const isLoaded = ()=>{
    return isLoadedUser.value;
}

export {getAutheticatedUser, isLoaded};

export default userModel;