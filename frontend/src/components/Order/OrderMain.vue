<script setup>
import { inject, ref } from '@vue/runtime-core';

const {getCartItems, clearCart } = inject('cart');

const order = ref({});
const items = getCartItems();

const addOrder = (orderBuff, items) => {
    order.value = orderBuff;
    const data = {
        ...orderBuff,
        productRequestList: items.map(f=>{
            const {count, id} = f;
            return {
                id,
                count
            }
        })
    }
    console.log(orderBuff);
    axios({
        method: 'post',
        url: '/api/v1/order',
        data: data,
    }).then(response=>{
        const {paymentLink, totalPrice} = response.data;
        console.log(paymentLink, totalPrice, response.data);
        clearCart();
        router.push({name: 'payment', params: { paymentLink: paymentLink, totalPrice: totalPrice}});
    })
}
</script>

<template>
    <div>
        <header-no-input/>
        <div class="wrapper">
            <div class="container">
                <router-view :items="items" :addOrder="addOrder"/>
            </div>
        </div>
    </div>
</template>

<script>
import HeaderNoInput from "../HeaderNoInput.vue"
import { inject, ref } from '@vue/runtime-core'
import axios from 'axios';
import router from '../../router';

export default {
    components: {
        HeaderNoInput,
    },
}
</script>

<style scoped lang="scss">
.wrapper{
    width: 100vw;
    display: flex;

    .container{
        display: flex;
        flex-direction: column;
        width: 80%;
        margin: 0 auto;
        margin-top: 50px;

        .breadcrumb{
            margin-right: auto;
        }
    }
}
</style>