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

library.add(fas);



createApp(App)
    .component('fa',FontAwesomeIcon)
    .use(router)
    .use(Toast)
    .use(VueCookies)
    .mount('#app')
