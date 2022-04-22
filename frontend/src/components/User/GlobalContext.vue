

<template>
    <div>
        <slot/>
    </div>
</template>

<script>
import axios from 'axios';
import { provide, ref, readonly, onMounted } from 'vue';
import userModel, {getAutheticatedUser} from './GlobalVariables.js';
import { useToast } from "vue-toastification";

const cart = ref([]);
const currentItem = ref({});

const toast = useToast();

export default {
    setup() {
        axios.defaults.withCredentials = true;
        onMounted(async ()=>{
            await getAutheticatedUser();
        });

        provide('userModel', readonly(userModel));

        const storage = localStorage.getItem("cartItems");

        if(storage != null){
            cart.value = JSON.parse(storage);
        }

        provide('user',{
            getUserRole(){
                return userModel.value.role;
            },
            getUserEmail(){
                return userModel.value.email;
            },
            isAuthenticated(){
                return userModel.value.isAuthenticated;
            }
        })

		provide('currentItem',{
			setCurrentItem(item){
				currentItem.value = item;
			},
			getCurrentItem(){
				return currentItem.value;
			}

		})

        provide('cart', {
            addToCart(item){
                if(cart.value.find(f=>f.id===item.id)===undefined){
                    cart.value.push({...item, count: 1});
                    toast.success("Dodano produkt do koszyka ", {timeout: 2000});
                }else{
                    toast.info("Produkt już jest w koszyku ", {timeout: 2000})
                }
                localStorage.setItem("cartItems", JSON.stringify(cart.value));
            },
            clearCart(){
                cart.value = [];
                localStorage.removeItem("cartItems");
            },
            removeCartByIndex(index){
                if(cart.value.length > index){
                    cart.value = cart.value.filter(f=>f!==cart.value[index]);
                    localStorage.setItem("cartItems", JSON.stringify(cart.value));
                    toast.success("Usunięto produkt z koszyka ", {timeout: 2000});
                }
            },
            getCartItems(){
                return cart.value;
            },
            getCartCount(){
                return cart.value.length;
            }
        })
    },
}
</script>
