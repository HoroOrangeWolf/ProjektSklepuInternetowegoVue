<template>
    <div class="wrapper">

        <div class="upperTableBlock">
            <n-input placeholder="Search" v-model:value="searchInput">
                <template #suffix>
                    <fa icon="magnifying-glass" />
                </template>
            </n-input>
        </div>

        <n-spin v-if="isLoading" size="large" style="position:absolute; margin-top: 200px;"/>

		<n-data-table
			:columns="columns"
			:data="dataTable"
            v-if="!isLoading"
			:pagination="pagination"
			:bordered="false"
		/>
    </div>
</template>


<script>
import { h , ref, watch} from "vue";
import { NButton, NDataTable, NButtonGroup, NCollapse, NCollapseItem, NInput, NSelect, NSpin, } from "naive-ui";
import axios from 'axios';


const dataTable = ref([]);
const showModal = ref(false);
const isLoading = ref(false);
const searchInput = ref('');

const searchItems = async () =>{
    const response = await axios({
        method: 'get',
        url: '/api/v1/order/admin?searchBy='+searchInput.value+"&limit=20&page=0"
    })

    console.log(response.data);

    const {totalCount, list} = response.data;

    dataTable.value = list;
}

watch(searchInput, ()=>{
    searchItems();
})

const columns = 
   [
    {
        title: "Lp.",
        key: "no",
        render: (row, index) => index + 1
    },
    {
        title: "Email użytkownika",
        key: "email"
    },
    {
        title: "Uwagi",
        key: "remarks"
    },
    {
        title: "Payment status",
        key: "paymentStatus"
    },
    {
        title: "Shipment status",
        key: "shipmentStatus",
    },
    {
        title: "Cena całkowita",
        key: "totalPrice"
    } 
  ];

export default {
  setup() {
    searchItems();
    return {
      isLoading,
      columns,
      dataTable,
      pagination: false,
      showModal,
      searchInput
    };
  },

  components: {NDataTable, NButton, NInput, NSelect, NSpin}
};
</script>

<style scoped lang="scss">

	.wrapper{
		margin: 0 auto;
		width: 100%;
		max-width: 100%;

		.mod{
			width: 30%;
			margin-left: auto;
		}

        .upperTableBlock{
            padding: 30px;
            display: flex;;
            &:first-child{
                width: 350px;
                margin-left: auto;
            }
        }
	}

	.categoryModal{
        padding: 10px 0 20px 0;
        border-top: 1px solid grey;
        border-bottom: 1px solid grey;

        label {
            display: block;
        }

        input {
            padding: 2px 0;
        }

        label{
            margin-top: 15px;
            font-size: 0.9rem;
        }

        label:first-child{
            margin-top: 0px;
        }
	}

    
    .addingCategory{

        overflow: hidden;
        button{

            cursor: pointer;
            float: right;
            margin: 10px;
            color: #fff;
            padding: 5px 10px;
            background-color: rgb(0, 183, 255);
            border-radius: 5px;
            border: 0;
            outline: 0;

        }
    }

    .deleteButton{
        width: 50%;
        height: 40px;
    }
    .addingButton{
        overflow: hidden;
        cursor: pointer;
        float: right;
        margin: 10px;
        color: #fff;
        padding: 5px 10px;
        background-color: rgb(0, 183, 255);
        border-radius: 5px;
        
        outline: 0;

        .icon{
            margin-right: 10px;
        }

        &:hover{
            
            border: 0;
            background-color: rgb(0, 183, 255);
            color: #fff;
            outline: 0;
        }	
	}
		
</style>