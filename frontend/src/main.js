import App from './App.vue'
import router from './router'
import './style/main.scss'
import { createApp } from 'vue'
import {library} from '@fortawesome/fontawesome-svg-core'
import {fas} from '@fortawesome/free-solid-svg-icons'
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import "vue-toastification/dist/index.css";
import Toast from "vue-toastification";
import VueCookies from 'vue-cookies';
import pl from 'javascript-time-ago/locale/pl.json';
import TimeAgo from 'javascript-time-ago';
import axios from 'axios'
import { clearModel } from './components/GlobalContext/GlobalVariables'

TimeAgo.addDefaultLocale(pl)
library.add(fas);


axios.interceptors.response.use((response)=>{
    return response;
},
(error)=>{
    if(error.response.status === 403){
        clearModel();
        window.location ="/";
    }else{
        return Promise.reject(error);
    }
})


createApp(App)
    .component('fa',FontAwesomeIcon)
    .use(router)
    .use(Toast)
    .use(VueCookies)
    .mount('#app')
