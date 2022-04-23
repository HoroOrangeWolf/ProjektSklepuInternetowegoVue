<template>
    <div class="wrapper">

        <div class="upperTableBlock">
            <n-input placeholder="Search" v-model:value="searchInput">
                <template #suffix>
                    <fa icon="magnifying-glass" />
                </template>
            </n-input>
        </div>

        <n-spin :show="isLoading">
            <table-sorter-group @click="handleSorterClick">
                <n-data-table
                :columns="columns"
                :data="dataTable"
                :pagination="pagination"
                :bordered="false"
                />
            </table-sorter-group>
            <n-pagination
            :page-count="totalPages"
            v-model:page="orderState.page"
             />
        </n-spin>
    </div>
</template>


<script>
import { h , ref, watch, reactive} from "vue";
import { NButton, NDataTable, NDropdown, NInput, NSelect, NSpin, NPagination } from "naive-ui";
import axios from 'axios';
import { useToast } from 'vue-toastification';
import TableSorter from "./Sorter/TableSorter.vue";
import TableSorterGroup from "./Sorter/TableSorterGroup.vue";

const dataTable = ref([]);
const showModal = ref(false);
const isLoading = ref(false);
const searchInput = ref('');
const aborter = ref(new AbortController());
const toast = useToast();
const totalPages = ref(1);

const orderState = reactive({
    searchBy: '',
    sort: 'ASC',
    sortBy: 'id',
    page: 1
});

const handleSorterClick = (data)=>{
    orderState.sort = data.isASC ? 'ASC' : 'DESC';
    orderState.sortBy = data.name;
}

const searchItems = () =>{
    
    aborter.value.abort();
    aborter.value = new AbortController();
    isLoading.value = true;

    axios({
        method: 'get',
        url: '/api/v1/order/admin?searchBy='+searchInput.value+"&limit=10&page="+(orderState.page - 1) + "&sortBy=" + orderState.sortBy + "&order="+orderState.sort,
        signal: aborter.value.signal
    })
    .then((response)=>{
        const { list, totalCount } = response.data;
        dataTable.value = list;
        totalPages.value = Math.ceil(totalCount/10);
    })
    .catch((exc)=>{
        if(exc.message!=='canceled'){
            toast.error("Wystąpił błąd podczas wyszukiwania", {timeout: 2000});
            console.log(exc);
        }
    }).finally(()=>{
        isLoading.value = false;
    })
}

const changePaymentStatus = (v, row) => {
    isLoading.value = true;

    axios({
        method: "put",
        url: "/api/v1/order/" + row.id + "/admin?whichStatus=PAYMENT&status=" +  v,
    })
    .then(()=>{
        searchItems();
    })
    .then(()=>{
        toast.success("Zmieniono pomyślnie status płatności", {timeout: 2000});
    }).catch((exc)=>{
        if(exc.message !== "canceled"){
            toast.error("Wystąpił błąd podczas zmiany statusu");
            console.log(exc);
        }
        isLoading.value = false;
    });
}

const changeShipmentStatus = (v, row) =>{

    isLoading.value = true;

    axios({
        method: "put",
        url: "/api/v1/order/" + row.id + "/admin?whichStatus=SHIPMENT&status=" +  v,
    })
    .then(()=>{
        searchItems();
    })
    .then(()=>{
        toast.success("Zmieniono pomyślnie status dostawy", {timeout: 2000});
    }).catch((exc)=>{
        if(exc.message !== "canceled"){
            toast.error("Wystąpił błąd podczas zmiany statusu");
            console.log(exc);
        }
        isLoading.value = false;
    });
}

watch(searchInput, ()=>{
    searchItems();
})

const columns = 
   [
    {
        title: h(TableSorter, {name: "id"}, ()=>"Id"),
        key: "id",
    },
    {
        title: h(TableSorter, {name: "user.email"}, ()=>"Email użytkownika"),
        key: "email"
    },
    {
        title: "Uwagi",
        key: "remarks"
    },
    {
        title: h(TableSorter, {name: "paymentType"}, ()=>"Rodzaj płatności"),
        key: "paymentType",
        render(row){
            switch(row.paymentType){
                case "PAYPAL":
                    return "PayPal";
                case "TRANSFER":
                    return "Przelew";
                case "ON_SITE":
                    return "Płatność na miejscu";
                default:
                    return "Brak informacji";
            }
        }
    },
    {
        title: h(TableSorter, {name: "paymentStatus"}, ()=>"Status płatności"),
        key: "paymentStatus",
        render(row){
            if(row.paymentType === "TRANSFER"){

                let mapped = "";
                const status = row.paymentStatus;

                const ar = [
                    {key: "PROCESSED", label: "Przetworzono"},
                    {key: "SUCCESS", label: "Zapłacono"},
                    {key: "DENIED", label: "Odmowa"},
                    {key: "CANCELED", label: "Anulowana"},
                    {key: "WAITING_FOR_PAYMENT", label: "Oczekiwanie na płatność"}
                ];

                switch(status){
                    case "PROCESSED":
                        mapped = "Przetworzono";
                        break;
                    case "SUCCESS":
                        mapped = "Zapłacono";
                        break;
                    case "DENIED":
                        mapped = "Odmowa";
                        break;
                    case "CANCELED":
                        mapped = "Anulowana";
                    case "WAITING_FOR_PAYMENT":
                        mapped = "Oczekiwanie na płatność";
                }

                const buff = ar.filter(f=>f.key!==status);

                return h(NDropdown, {options: buff, onSelect: (v)=>changePaymentStatus(v, row)}, ()=>h(NButton, {}, ()=>mapped));
            }else{
                if(row.paymentStatus === "SUCCESS")
                {
                    return "Zapłacono";
                }else{
                    return "Niezapłacono";
                }
            }
        }
    },
    {
        title: h(TableSorter, {name: "shipmentStatus"}, ()=>"Status dostawy"),
        key: "shipmentStatus",
        render(row){
            
            let mapped = "";
            const status = row.shipmentStatus;
            const ar = [
                {key: "DELIVERED", title: "Dostarczono"},
                {key: "IN_PREPARATION", title: "W przygotowaniu"},
                {key: "DELIVERING", title: "W trasie"}
            ];

            switch(status){
                case "DELIVERED":
                    mapped = "Dostarczono";
                    break;
                case "IN_PREPARATION":
                    mapped = "W przygotowaniu";
                    break;
                case "DELIVERING":
                    mapped = "W trasie";
            }

            const buff = ar.filter(f=>f.key!==status);

            return h(NDropdown, {options: buff, onSelect: (v) => changeShipmentStatus(v, row)}, ()=>h(NButton, {}, ()=>mapped));
        }
    },
    {
        title: h(TableSorter, {name: "totalPrice"}, ()=>"Cena całkowita"),
        key: "totalPrice",
        render(row){
            return `${row.totalPrice} zł`
        }
    } 
  ];

watch(()=>{return {...orderState}},(newValue, oldValue)=>{

    const {sortBy, searchBy, sort, page} = newValue;
    
    if(sortBy !== oldValue.sortBy || searchBy !== oldValue.searchBy || sort !== oldValue.sort)
    {
        orderState.page=1;
    }
    searchItems();
});

export default {
  setup() {
    searchItems();
    return {
      isLoading,
      columns,
      dataTable,
      pagination: false,
      showModal,
      searchInput,
      handleSorterClick,
      totalPages,
      orderState
    };
  },

  components: {NDataTable, NButton, NInput, NSelect, NSpin, NPagination, TableSorter, TableSorterGroup}
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