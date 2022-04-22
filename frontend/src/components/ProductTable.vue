<script setup>

</script>

<template>
	<div class="wrapper">
        <div class="upperWraper" v-if="!isLoading">
            <n-input class="nInput" placeholder="Wyszukaj produkt" v-model:value="productState.searchBy">
                <template #suffix>
                    <fa icon="magnifying-glass" />
                </template>
            </n-input>
            <n-button class="addingButton" @click="showModal = true">
                <fa class="icon" icon="plus"/>
                Dodaj Produkt
            </n-button>
        </div>
		<div class="mod">
			<n-modal v-model:show="showModal">
				<n-card
					style="width: 600px"
					title="Dodawanie kategorii"
					:bordered="false"
					size="huge"
					role="dialog"
					aria-modal="true"
				>

                <div class="categoryModal">
                    
                    <label for="product_name">Nazwa Produktu</label>
                    <n-input :status="validateStatus.nameStatus" type="text" v-model:value="model.name" id="product_name" placeholder="Nazwa"/>

                    <label for="producer_name">Producent</label>
                    <n-input type="text" v-model:value="model.producer" id="producer_name" placeholder="Producent"/>

                    <label>Wybierz zdjęcia</label>
                    <n-upload 
                        @change="addFilesEvent" 
                        @preview="handlePreview" 
                        list-type="image-card"
                        :default-file-list="defaultImageList"
                        :max="5">
                        Dodaj Pliki
                    </n-upload>

                    <n-modal
                        v-model:show="showModalPreview"
                        preset="card"
                        style="width: 600px"
                        >
                        <img :src="previewImageUrl" style="width: 100%">
                    </n-modal>

                    <label v-if="model.specifications.length>0">Specyfikacje</label>
                    <div v-for="(data, index) in model.specifications" :key="index" class="inputGroup">
                        <n-input v-model:value="data.keyName" placeholder="Nazwa specyfikacji"/>
                        <n-input v-model:value="data.keyValue" placeholder="Wartość"/>
                        <n-button @click="removeSpecification(index)">
                            <fa icon="x"/>
                        </n-button>
                    </div>

                    <label>Dodaj nową specyfikacje</label>
                    <n-button @click="addNewCategory">Dodaj</n-button>

                    <label for="product_description">Opis</label>
                    <n-input :status="validateStatus.descriptionStatus"  type="textarea" v-model:value="model.description" id="product_description"  placeholder="Opis"/>
                    
                    <label for="product_count">Liczba dostępnych produktów</label>
                    <n-input :status="validateStatus.productNumberStatus" type="number"  v-model:value="model.count" id="product_count" step="any"/>

                    <label for="product_price">Cena produktu</label>
                    <n-input :status="validateStatus.priceStatus" type="number"  v-model:value="model.price" id="product_price" step="any">
                        <template #suffix>
                            <fa icon="dollar-sign"/>
                        </template>   
                    </n-input>

                    <label for="product_categories">Kategorie</label>
                    <n-select :status="validateStatus.categoryStatus" :options="categories" v-model:value="model.categoryId"/>
                </div>
                <div class="addingCategory">
                    <button @click="updateProduct" v-if="isModification">Modyfikuj</button>
					<button @click="addProduct" v-else>Dodaj</button>
				</div>
				</n-card>
			</n-modal>

            <n-modal v-model:show="showDeleteModal">
				<n-card
					style="width: 600px"
					:title="`Czy Chcesz usunąć produkt o nazwie ${toDelete.name} ?`"
					:bordered="false"
					size="huge"
					role="dialog"
					aria-modal="true"
				>
                <div class="addingCategory" style="display: flex;">
					<button @click="removeProduct" class="deleteButton">Tak</button>
                    <button @click="showDeleteModal = false" class="deleteButton">Nie</button>
				</div>
				</n-card>
			</n-modal>
		</div>

        <n-spin v-if="isLoading" size="large" style="position:absolute; margin-top: 200px;top: 30%; left: 50%;"/>
        <table-sorter-group @click="handleSorterClick">
            <n-data-table
			:columns="columns"
			:data="dataTable"
            v-if="!isLoading"
			:bordered="false"
		    />
        </table-sorter-group>
        <n-pagination style="margin: 10px; margin-left: auto;" v-if="!isLoading" v-model:page="productState.page" :page-count="totalPages"/>
	</div>
</template>

<script>
import { h , ref , watch, reactive} from "vue";
import { NButton, NModal, NCard, NDataTable, NButtonGroup, NPagination, NInput, NSelect, NSpin, NUpload, NDropdown} from "naive-ui";
import axios from 'axios';
import TableSorter from './Sorter/TableSorter.vue';
import TableSorterGroup from "./Sorter/TableSorterGroup.vue";

const dataTable = ref([]);
const categories = ref([]);
const isLoading  = ref(true);
const showDeleteModal = ref(false);
const showModal = ref(false);
const isModification = ref(false);
const toDelete = ref({});
const showModalRef = ref(false);
const previewImageUrlRef = ref("");
const defaultImageList = ref([]);
const totalPages = ref(1);
const abortRequest = ref(new AbortController());

const validateStatus = ref( {
	nameStatus: '',
	descriptionStatus: '',
	productNumberStatus: '',
	priceStatus: '',
	categoryStatus: ''
});

const productState = reactive({
    searchBy: '',
    sort: 'ASC',
    sortBy: 'id',
    page: 1
});

const removeSpecification = (index) => {
    model.value.specifications.splice(index, 1);
}

const model = ref({
    id: -1,
    name: '',
    producer: '',
    description: '',
    count: '0',
    price: '0.0',
    specifications: [],
    attachments: []
});

const handlePreview = (file) => {
    const { blob } = file;
    previewImageUrlRef.value = blob;
    showModalRef.value = true;
}

const handleSorterClick = (data)=>{
    productState.sort = data.isASC ? 'ASC' : 'DESC';
    productState.sortBy = data.name;
}

const clearModel = () => {
    model.value = {
        id: -1,
        name: '',
        producer: '',
        description: '',
        count: '0',
        price: '0.0',
        specifications: [],
        attachments: []
    }
};

const addFilesEvent = (event) =>{

    const fileList = event.fileList;

    model.value.attachments = fileList;
}

const addNewCategory = () => {
    model.value.specifications.push({keyName: '', keyValue: ''});
}

const getCategories = async () => {
	return await axios(
		{
            method: 'get',
            url: '/api/v1/category'
		}
	);
};

const getProducts = async () => {
    return await axios(
        {
            method: 'get',
            signal: abortRequest.value.signal,
            url: '/api/v1/product?sortBy=' + productState.sortBy  + "&sort=" + productState.sort + "&page=" + (productState.page - 1) + "&limit=10&searchBy=" + productState.searchBy,
        }
    )
}

const updateProduct = async () => {
    isModification.value = true;
    isLoading.value = true;

    model.value.attachments = await imagesToBase64(model.value.attachments);
    console.log(model.value.attachments);
    model.value.files = [];

    axios({
        method: 'put',
        url: '/api/v1/product/' + model.value.id + '/admin',
        data: model.value
    })
    .then(()=>{
        loadAllData();
    })
    .finally(()=>{
        isModification.value = false;
        showModal.value = false;
        clearModel();
    })
}



watch(model.value, ()=> {
	let regex = /^[a-zA-Z]+$/;

	if(model.value.name.length <3  || !regex.test(model.value.name)){
		validateStatus.value.nameStatus = 'warning';
	}
	else{
		validateStatus.value.nameStatus = '';
	}

	if(model.value.description.length < 5){
		validateStatus.value.descriptionStatus = 'warning';
	}
	else{
		validateStatus.value.descriptionStatus = '';
	}

	if(model.value.count < 0)
	{
		validateStatus.value.productNumberStatus = 'warning';
		
	}
	else{
		validateStatus.value.productNumberStatus = '';
		
	}

	if( model.value.price < 0){
		validateStatus.value.priceStatus = 'warning';
	}
	else{
		validateStatus.value.priceStatus = '';
	}

})

function readFileAsync(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  })
}

const imagesToBase64 = async (files)=>{

    const ar = [];

    for(let i = 0; i < files.length; ++i){
        const f = files[i];
        console.log(f);
        if(f.file===undefined)
        {
            ar.push({blob: f.url ? f.url : f.blob});
            continue;
        }

        let extension = f.name.split(".");

        if(extension.length > 2){
            continue;
        }
        let data = await readFileAsync(f.file);
        ar.push({blob: data});
    }
    return ar;
}

const addProduct = async () => {
    //TODO:Dodać całą walidacje po kliknięciu też tutaj, zamienić również validacje pół liczbowych żeby wyrzucały waning/error jeżeli value <= 0 zamiast value < 0

    model.value.attachments = await imagesToBase64(model.value.attachments);
    model.value.files = [];
    
	if(model.value.count < 0 || model.value.price < 0){

		validateStatus.value.productNumberStatus = 'error';
		validateStatus.value.priceStatus = 'error';
		return;
	}
	else{
        isLoading.value = true;
        showModal.value = false;
		await axios(
            {
                method: 'post',
                url: '/api/v1/product/admin',
                data: model.value
            }
        )
    	clearModel();
    	await loadAllData();
	}
    
}

const removeProduct = () => {

    showDeleteModal.value = false;
    isLoading.value = true;

    axios(
        {
            method: 'delete',
            url: '/api/v1/product/' + toDelete.value.id + '/admin',
        }
    )
    .finally(()=>{
        loadAllData()
            .finally(()=>{
                isLoading.value = false;
            })
    })
}

const loadAllData = async () => {

    const buffCat = await getCategories();

    {
        let {data} = buffCat;

        categories.value = data.map(v=>{
            return {
                label: v.name,
                value: v.id
            }
        });
    }

    const buffProducts = await getProducts();

    let {data} = buffProducts;

    const {list, totalCount} = data;
    totalPages.value = Math.ceil(totalCount/10);
    dataTable.value = list;
    isLoading.value = false;
}

const columns = 

   [
    {
        title: h(TableSorter,{name: 'id'}, ()=>'Id'),
        key: "id",
    },
    {
        title: h(TableSorter, {name: 'name'},()=>"Nazwa"),
        key: "name"
    },
    {
        title: h(TableSorter, {name: 'producer'},()=>"Producent"),
        key: 'producer'
    },
    {
        title: h(TableSorter, {name: 'count'}, ()=>"Liczba sztuk"),
        key: "count"
    },
    {
        title: h(TableSorter, {name: 'price'}, ()=>"Cena"),
        key: "price",
        render(row){
            return h('p', {innerHTML: `${row.price} PLN`})
        }
    },
    {
        title: "Specyfikacje",
        key: "specifications",
        render(row){

            const options = row.specifications.map(f=>{
                return {label: `Nazwa: ${f.keyName} Specyfikacja: ${f.keyValue}`}
            });

            return h(
                NDropdown,
                {trigger: "click", options},
                h(NButton, {}, ()=>"Specyfikacje")
            ); 
        }
    },
    {
        title: 'Kategoria',
        key: "categories",
        render (row) {

            return (
                h('p',{innerHTML: row.category?.name})
            );
        }
    },
    {
      title: "Akcje",
      key: "actions",
      render: (row) => {
        return [
            h(
                NButtonGroup,
                {
                },
                ()=>[
                    h(
                        NButton,
                        {
                            onClick: ()=> {
                                showModal.value = true;
                                isModification.value = true;
                                console.log(row);
                                const {id, name,producer ,description, count, price, category, specifications, attachments} = row;
                                const ata = attachments.map((f, index)=>{
                                    return {
                                        url: f.blob,
                                        name: 'c.png',
                                        status: 'finished',
                                        name: 'Nice photo: ' + index,
                                    }
                                })
                                defaultImageList.value = ata;
                                console.log(category);
                                model.value = {
                                    id,
                                    name,
                                    producer,
                                    description,
                                    count: count.toString(),
                                    price: price.toString(),
                                    categoryId: category?.id,
                                    specifications,
                                    attachments
                                };
                            },
        
                            style: "border-radius: 9999px 0 0 9999px; border: 1px solid rgba(128, 128, 128, 0.3); background-color: rgba(0, 0, 0, 0);",
                            secondary: '',
                        },
                        ()=>'Modyfikuj'
                    ),
                    h(
                        NButton,
                        {
                            onClick: ()=> {
                                toDelete.value = row;
                                showDeleteModal.value = true;
                            },
                            style: "border-radius: 0 9999px 9999px 0; border: 1px solid rgba(128, 128, 128, 0.3); background-color: rgba(0, 0, 0, 0);",
                            secondary: '',
                        },
                        ()=>'Usuń'
                    ),
                ]
            )
		]
      }
    }
  ];


watch(()=>{return {...productState}},async (newValue, oldValue)=>{

    const {sortBy, searchBy, sort, page} = newValue;
    
    if(sortBy !== oldValue.sortBy || searchBy !== oldValue.searchBy || sort !== oldValue.sort)
    {
        productState.page=1;
    }
    abortRequest.value.abort();
    abortRequest.value = new AbortController();
    try{
        const buffProducts = await getProducts();
        
        let {data} = buffProducts;

        const {list, totalCount} = data;
        totalPages.value = Math.ceil(totalCount/10);
        dataTable.value = list;
    }catch(exc){
        
    }
});

export default {
  setup() {
    return {
      toDelete,
      showDeleteModal,
      model,
      productState,
      columns,
      removeProduct,
      updateProduct,
      dataTable,
      clearModel,
      categories,
      addProduct,
      isLoading,
      isModification,
      pagination: false,
      showModal,
      addFilesEvent,
	  validateStatus,
      handlePreview,
      defaultImageList,
      showModalPreview: showModalRef,
      previewImageUrl: previewImageUrlRef,
      addNewCategory,
      removeSpecification,
      totalPages,
      handleSorterClick
    };
  },
  methods: {
      setToDelete(fun){
          this.toDelete = fun;
      }
  },
  mounted() {
      loadAllData();
  },
  
  components: {NDataTable, NButton, NCard, NModal, NInput, NSelect, NSpin, NUpload, NPagination, TableSorter, TableSorterGroup}
};
</script>

<style scoped lang="scss">

	.wrapper{
		margin: 0 auto;
		width: 100%;
		max-width: 100%;
        display: flex;
        flex-direction: column;
        .upperWraper{
            display: flex;
            justify-content: space-between;
            padding: 15px;
            .nInput{
                max-width: 350px;
                height: auto;
                max-height: 35px;
                align-self: center;
            }
        }
		.mod{
			width: 30%;
			margin-left: auto;
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

        .inputGroup{
            display: flex;
            justify-content: space-between;

            & > * {
                margin-top: 10px;
                margin-right: 10px;
            }
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