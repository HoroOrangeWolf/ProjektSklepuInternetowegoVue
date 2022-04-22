
<script setup>
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { NInput, NDropdown, NButton, NIcon } from "naive-ui";
import { h, onMounted, ref } from "vue";
import HomePageLoginRegister from "../components/HomePageLoginRegister.vue";
import { inject } from "vue";
import axios from "axios";
import router from "../router";
import LogoECommerce from "../components/Logo/LogoECommerce.vue";

const menuOptions = ref([]);

const {isAuthenticated, getUserEmail, getUserRole } = inject('user');
const userModel = inject('userModel');
const { getCartCount } = inject('cart');
const userDropDown = () =>{

if(isAuthenticated()){
  
  const options = [
    {
      label: ()=>h('b', {innerHTML: `Witaj ${getUserEmail()} `}),
      key: 'hello',
      disabled: true
    },
    {
      label: 'Twoje Konto',
      key: 'profile',
      icon: ()=>h(FontAwesomeIcon, {icon: 'user'}),
    },
    {
      label: 'Zamówienia',
      key: 'orders',
      icon: ()=>h(FontAwesomeIcon, {icon: 'cart-shopping'})
    },
    {
      label: 'Opinie',
      key: 'opinions',
      icon: ()=>h(FontAwesomeIcon, {icon: 'star'})
    }
  ]

  if(getUserRole() === 'ADMIN')
  {
    options.push({
      label: 'Panel administratora',
      key: 'adminPanel',
      icon: ()=>h(FontAwesomeIcon, {icon: 'gear'})
    });
  }

  return options;

}else{
  return [
    {
      label: 'LogRegister',
      type: 'render',
      render(){
        return h(HomePageLoginRegister, {});
      }
    }
  ]
}
}

const handleUserSelect = (key) =>{
  switch(key){
    case 'profile':
    case 'orders':
      router.push({path: '/user'});
      break;
    case 'opinions':
      router.push({path: '/user/opinions'});
      break;
    case 'adminPanel':
      router.push({path: '/admin'});
      break;
  }
}

const getCategories = async () => {
  const response = await axios({
    method: 'GET',
    url: '/api/v1/category/tree'
  })

  menuOptions.value = response.data;
}

const mapToOptions = (item) =>{
  const {categoryList} = item;

  return categoryList.map(f=>{
    const {name, id} = f;
    return {
      key: id,
      label: name,
    }
  })
}

const handleSelect = (id) =>{
  router.push({name: 'products', params: {categoryId: id}});
}

const handleSelectCart = (v) =>{
  router.push({path: '/cart'});
}

const optionsCart = [
  {
    label: ()=>getCartCount() > 0 ? `W twoim koszyku znajduje się ${getCartCount()} produktów` : 'Twój koszyk jest pusty!',
    key: 'cart',
    icon: ()=>h(FontAwesomeIcon, {icon: 'cart-shopping'}),
  }
]

onMounted(async ()=>{
  await getCategories();
})
</script>

<template>
  <div class="home">
    <div class="header-box">
        <logo-e-commerce/>
        <n-input class="search-box" placeholder="Czego szukasz?" round>
          <template #suffix>
            <fa icon="magnifying-glass"></fa>
          </template>
        </n-input>
        <div class="links">
            <n-dropdown :options="userDropDown()" size="huge" @select="handleUserSelect">
              <n-button round @click="goToLogin()">
                <fa icon="user"/>
              </n-button>
            </n-dropdown>
            <n-dropdown :options="optionsCart" size="huge" @select="handleSelectCart">
              <n-button round>
                <fa icon="cart-shopping"/>
              </n-button>
            </n-dropdown>
        </div>
    </div>
     <div class="navigation-box">
          <nav>
              <n-dropdown v-for="(data, index) in menuOptions" :key="index" :options="mapToOptions(data)" @select="handleSelect">
                <n-button class="menuBtn" text>
                  {{data.name}}
                </n-button>
              </n-dropdown>
          </nav>
        </div>
        <router-view/>
        <div class="footer-box">
          <footer>
            <div class="partners-box">
              <h3>Kontakt</h3>
              <fa style="width: 20px; height: 20px" icon="phone" />
              <p class="number">+ 48 123 456 678</p>
              <p class="hours">Pon-Pt: 8:00-16:00</p>
              <p class="hours">Sob-Niedz: 8:00-16:00</p>
            </div>
            <div class="bottom-footer-text">
              <fa class="icon" icon="copyright" /> E-commerce 2022 - 2022
            </div>
          </footer>
        </div>
  </div>
</template>

<script>


export default {
  components: {
    NInput,
    NDropdown,
    NButton,
    NIcon,
    LogoECommerce
  } 
}
</script>


<style scoped lang="scss">

.home {
  width: 100%;
  height: 100vh;
    .header-box {
      width: 80%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0rem 2rem;
      margin: 0 auto;

    }

  .links{
    & > *{
      margin-left: 1rem;
    }
  }
  .search-box {
      width: 40%;
  }

  .search-input {
      width: 100%;
      margin-right: .5rem;
  }
}

.footer-box {
  padding: 2rem 0 3rem 0;

  width: 100%;
  align-self: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

footer {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 10px;
  font-size: 18px;
}

footer .partners-box {
  .number {
    display: inline-block;
    font-size: 18px;
    margin-left: 10px;
  }

  .hours {
    margin-left: 5px;
    font-size: 14px;
  }
}

footer .partners-box .icon {
  font-size: 12px;
  margin: 0 0.5rem 2rem 0.5rem;
}
.navigation-box {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgb(245, 245, 245);
    box-shadow: rgba(0, 0, 0, 0.64);

    nav {
        width: 80%;
    }

    .menuBtn{
      padding: 24px;
      border-radius: 0px;
      
      &:hover{
        background-color: white;
        border-radius: 10px 10px 0 0;
      }

    }

    .nav-list-box {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .nav-list-box ul{
        list-style-type: none;
        
    }

    .nav-list-box li {
        border: 2px solid green;
        display: inline-block;
        margin: .2rem .5rem;
        border-radius: 40px;
        padding: 0 .5rem;
    }
}

</style>