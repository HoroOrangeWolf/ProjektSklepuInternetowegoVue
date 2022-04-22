<template>
	<div class="wrapper">
		<n-input class="nInput" placeholder="Wyszukaj użytkownika po emailu" v-model:value="searchInput">
            <template #suffix>
                <fa icon="magnifying-glass" />
            </template>
        </n-input>
		<n-data-table
			:columns="columns"
			:data="data"
			:pagination="pagination"
			:bordered="false"
		/>
		

	</div>
</template>

<script>
import { h , ref, watch} from "vue";
import { NButton, NModal, NCard, NDataTable, NButtonGroup, NBadge, NInput } from "naive-ui";
import axios from 'axios';

const columns = 

   [
    {
      title: "User id",
      key: "id",
    },
    {
      title: "Imie",
      key: "name"
    },
    {
        title: "Nazwisko",
        key: "surname"
    },
    {
        title: "Email",
        key: "email"
    },
    {
        title: "Data urodzin",
        key: "birthDay"
    },
    {
        title: "Role",
        key: "role",
        render: keyValue =>{
            
            switch(keyValue.role){
                case 'ADMIN':
                    return h(
                        NBadge,
                        {
                            style: 'border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: rgba(225, 85, 84, 0.9);'
                        },
                        ()=>"Admin"
                    );
                case 'USER':
                return h(
                        NBadge,
                        {
                            style: 'border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: blue;'
                        },
                        ()=>"User"
                    );

            }
            return '';
        }
    },
    {
        title: "Status konta",
        key: "lock",
        render: keyValue =>{
            switch(keyValue.isBlocked){
                case true:
                    return h(
                        NBadge,
                        {
                            style: 'border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: red;'
                        },
                        ()=>"Zablokowany"
                    )
                case false:
                    return h(
                        NBadge,
                        {
                            style: 'border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: green;'
                        },
                        ()=>"Nie zablokowany"
                    )
            }
            return '';
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
                [
                    h(
                        NButton,
                        {
                            onClick: ()=> {
                                console.log("siema");
                            },

                            style: "border-radius: 9999px 0 0 9999px; border: 1px solid rgba(128, 128, 128, 0.3); background-color: rgba(0, 0, 0, 0);",
                            secondary: '',
                        },
                        'Zamówienia'
                    ),
                    h(
                        NButton,
                        {
                            onClick: ()=> {
                                console.log("siema");
                            },
                            style: "border-radius: 0 9999px 9999px 0; border: 1px solid rgba(128, 128, 128, 0.3); background-color: rgba(0, 0, 0, 0);",
                            secondary: '',
                        },
                        'Zablokuj'
                    ),
                ]
            )
		]
      }
    }
  ];
const data = ref([]);
const searchInput = ref('');

const getAllUsers = async () => {
    const response = await axios({
        method: 'get',
        url: '/api/v1/user/admin?searchBy=' + searchInput.value
    });
    data.value = response.data;
}

watch(searchInput, ()=>{
    getAllUsers();
})


export default {
  setup() {
    getAllUsers();
    return {
      data,
      columns,
      pagination: false,
      searchInput
    };
  },

  
  components: {NDataTable,NButton,NCard,NModal, NBadge, NInput}
};
</script>

<style scoped lang="scss">

	.wrapper{
		width: 100%;
		display: flex;
        flex-direction: column;
        
        .nInput{
            max-width: 250px;
            margin: 15px;
            align-self: flex-end;
        }

    }
</style>