<template>
    <n-card title="Zapłać za swoje zamówienie!" :segmented="cardSegmentation">
        <template #default>
            <div class="content">
                <p>Całkowity koszt twojego zamówienia: {{totalPrice}}zł</p>
                <p>Płatność: PayPal</p>
            </div>
        </template>
        <template #footer>
            <div class="btnContainer">
                <n-button class="btn">
                    <template #default>
                        Przerwij płatność
                    </template>
                    <template #icon>
                        &nbsp;
                    </template>
                </n-button>
                <a :href="paymentLink">
                    <n-button class="btn">
                        <template #default>
                            Zapłać
                        </template>
                        <template #icon>
                            <img src="../../assets/paypal-svg.svg" width="30" height="20" alt="" />
                        </template>
                    </n-button>
                </a>
            </div>
        </template>
    </n-card>
</template>

<script>
import { NCard, NButton } from "naive-ui"

const cardSegmentation = {
    content: true,
    footer: 'soft'
}

export default {
    setup() {
        return {
            cardSegmentation
        }
    },
    components: {
        NCard,
        NButton
    },
    props: {
        paymentLink: {
            default: ''
        },
        totalPrice: {
            default: 0
        }
    }
}
</script>

<style scoped lang="scss">
@import '../../style/colors.scss';
.content{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;

    & > p {
        text-align: left;
        font-weight: 700;
    }
}
.btnContainer{
    display: flex;
    justify-content: space-between;
    .btn{
        background-color: $base-color;
        min-width: 200px;
        min-height: 40px;
        border-radius: 11px;
        font-size: 1rem;
        color: white;
    
    &:hover{
        background-color: rgba($base-color, 0.8);
    }
}
}

</style>