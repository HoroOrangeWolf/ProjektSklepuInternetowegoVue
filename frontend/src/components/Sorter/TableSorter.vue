<script setup>
import { ref } from '@vue/reactivity';
import { inject, onMounted, onUnmounted } from '@vue/runtime-core';

const reference = ref({});
const holder = ref({name: '', isASC: true})
const isASC = ref(true);
const isFirstClick = ref(true);
const emiter = defineEmits(['click']);

const props = defineProps({
    name: {
        required: true
    }
})

const mod = inject('moduler');

const click = () => {
    if(isFirstClick.value)
    {
        isFirstClick.value = false;
    }else{
        isASC.value = !isASC.value;
    }
    emiter('click', {isASC: isASC.value});
    if(mod){
        holder.value = {name: props.name, isASC: isASC.value}
        mod.clickModul(reference.value, holder.value);
    }
}

const resetSorter = ()=>{
    isASC.value = true;
    isFirstClick.value = true;
}


onMounted(()=>{
    if(mod){
        console.log(reference.value);
        mod.addModul(reference.value, resetSorter);
    }
});

onUnmounted(()=>{
    if(mod){
        mod.removeModul(reference.value);
    }
});

</script>


<template>
    <div class="container">
        <slot/>
        <button @click="click">
            <fa icon="grip-lines" v-if="isFirstClick"/>
            <fa icon="chevron-up" v-else-if="isASC"/>
            <fa icon="chevron-down" v-else/>
        </button>
    </div>
</template>

<script>

</script>

<style scoped lang="scss">
.container{
    width: 100%;
    height: 100%;
    display: flex;

    button{
        border: 0px;
        background-color: rgb(0, 0, 0, 0);
        border-radius: 9999px;
        color: black;
        margin-left: 9px;

        padding: 8px;

        &:hover{
            background-color: rgba(128, 128, 128, 0.15);
        }
    }

}
</style>