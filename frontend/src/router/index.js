import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import ProfileComp from '../components/ProfileComp.vue'
import CategoryManager from '../components/CategoryManager.vue'
import Login from '../views/Login.vue'
import Admin from '../views/Admin.vue'
import AdminPanel from '../components/AdminPanel.vue'
import UserTable from '../components/UserTable.vue'
import ProductTable from '../components/ProductTable.vue'
import OrderManager from '../components/OrderManager.vue'
import RegisterAccount from '../components/RegisterAccount.vue'
import OrderMain from '../components/Order/OrderMain.vue'
import Cart from '../components/Order/Cart.vue'
import OrderSummary from '../components/Order/OrderSummary.vue'
import OrderPayment from '../components/Order/OrderPayment.vue'
import HomePageContent from '../components/Home/HomePageContent.vue'
import HomePageProducts from '../components/Home/HomePageProducts.vue'
import ProductCard from '../components/Product/ProductCard.vue'
import OrderStatus from '../components/Order/OrderStatus.vue'
import UserAccount from '../components/User/UserAccount.vue'
import UserOrder from '../components/User/UserOrder.vue'
import UserOpinions from '../components/User/UserOpinions.vue'
import NotAuthorized from '../components/ErrorPages/NotAuthorized.vue'

//User model
import userModel, {isLoaded, getAutheticatedUser} from '../components/GlobalContext/GlobalVariables'


const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '',
        components: {
          default: HomePageContent
        }
      },
      {
        path: 'products/:id',
        name: 'products',
        components: {
          default: HomePageProducts
        },
        props: true,
      },
      {
        path: '/cart',
        name: 'cart',
        components: {
          default:  Cart
        }
      },
      {
        path: '/user',
        name: 'user',
        component: UserAccount,
        meta: {authorize: ['USER']},
        children: [
          {
            path: '',
            components: {
              default: UserOrder
            }
          },
          {
            path: 'opinions',
            components: {
              default: UserOpinions
            }
          },
          {
            path: 'order',
            name: 'Order',
            component: OrderMain,
            props: true,
            meta: {authorize: ['USER']},
            children: [
              {
                path: 'summary',
                name: 'summary',
                components: {
                  default: OrderSummary
                }
              },
              {
                path: 'payment',
                name: 'payment',
                components: {
                  default: OrderPayment
                },
                props: true
              },
              {
                path: 'status',
                components: {
                  default: OrderStatus
                }
              }
            ]
          },
        ]
      },
      {
        path: 'authorizationError',
        name: 'authorizationError',
        components: {
          default: NotAuthorized
        }
      },
      {
        path: 'p/:id',
        name: 'singleProduct',
        components: {
          default: ProductCard
        },
        props: true,
      }
    ]
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfileComp,
    meta: {authorize: ['USER', 'ADMIN']},
  },
  {
	  path: '/login',
	  name: 'login',
	  component: Login,
    meta: {authorize: ['GUEST']},
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterAccount,
    meta: {authorize: ['GUEST']},
  },
  {
    path: '/admin',
    name: 'admin',
    component: Admin,
    meta: {authorize: ['ADMIN']},
    children:[
      {
        path: '',
        components: {
          default: UserTable
        },
      },
      {
        path: 'category',
        components: {
          default: CategoryManager
        }
      },
      {
        path: 'product',
        components: {
          default: ProductTable
        }
      },
      {
        path: 'orders',
        components: {
          default: OrderManager
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach(async (to, from, next)=>{

  const {authorize} = to.meta;
  if(!isLoaded()){
    await getAutheticatedUser();
  }
  if(authorize){
    if(userModel.value.role === 'GUEST' && !authorize.find((f)=>f==='GUEST')){
      return next({path: '/login'});
    }
    if(userModel.value.role !== 'ADMIN' && authorize.find((f)=>f==='ADMIN')){
      return next({path: '/authorizationError'});
    }
  }
  return next();
})

export default router
