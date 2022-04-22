<template>
    <div class="wrapper">
        <header-no-input/>
		<n-spin :show="isLoading">
			<n-form class="registerForm">
				<n-form-item label="Imie">
					<n-input :status="validateStatus.inputNameStatus" ref="nameInput" placeholder="Imie" v-model:value="model.name"/>
				</n-form-item>

				<n-form-item label="Nazwisko">
					<n-input :status="validateStatus.inputSurnameStatus" placeholder="Nazwisko" v-model:value="model.surname"/>
				</n-form-item>

				<n-form-item label="Płeć">
					<n-radio-group :status="validateStatus.inputSexStatus" v-model:value="model.gender">
						<n-space>
							<n-radio value="MALE">
								Mężczyzna
							</n-radio>
							<n-radio value="FEMALE">
								Kobieta
							</n-radio>
						</n-space>
					</n-radio-group>
				</n-form-item>

				<n-form-item label="Email">
					<n-input :status="validateStatus.inputEmailStatus" placeholder="Email" v-model:value="model.email"/>
				</n-form-item>

				<n-form-item label="Hasło">
					<n-input :status="validateStatus.inputPasswordStatus" type="password" placeholder="Password" v-model:value="model.password"/>
				</n-form-item>

				<n-form-item label="Potwierdz Hasło">
					<n-input :status="validateStatus.inputConfirmPasswordStatus" type="password" placeholder="Confirm Password" v-model:value="model.confirmPassword"/>
				</n-form-item>
				
				<n-form-item label="Data urodzin">
					<n-date-picker :status="validateStatus.inputDateStatus" style="width: 100%;" v-model:value="model.birthDay"/>
				</n-form-item>
				<div class="inline">
					<n-form-item label="Kod pocztowy">
						<n-input :status="validateStatus.inputPostalCode" placeholder="Kod pocztowy" v-model:value="model.postCode"/>
					</n-form-item>

					<n-form-item label="Numer domu">
						<n-input :status="validateStatus.inputHomeNumber"  placeholder="Numer domu" v-model:value="model.homeNumber"/>
					</n-form-item>
				</div>
				<div class="inline">
					<n-form-item label="Ulica">
						<n-input :status="validateStatus.inputStreetName" placeholder="Ulica" v-model:value="model.street"/>
					</n-form-item>

					<n-form-item label="Miasto">
						<n-input :status="validateStatus.inputCityName" placeholder="Miasto" v-model:value="model.city"/>
					</n-form-item>
				</div>
				<n-button class="button" @click="registerUser()" rouned>
					Zarejestruj się!
				</n-button>
		
			</n-form>
		</n-spin>
    </div>
</template>

<script>
import { onMounted, ref, watch } from "vue"
import { NForm, NFormItem, NInput, NButton, NDatePicker, NSpace, NRadio, NRadioGroup, NSpin} from "naive-ui"
import { createToast } from 'mosha-vue-toastify';
import 'mosha-vue-toastify/dist/style.css'
import axios from "axios";
import HeaderNoInput from "./HeaderNoInput.vue";
import { useToast } from 'vue-toastification';
import router from '../router';

const isLoading = ref(false);
const toast = useToast();

const model = ref({
    name: '',
    surname: '',
    email: '',
    password: '',
    confirmPassword: '',
    postCode: '',
    homeNumber: '',
    street: '',
    city: '',
    gender: '',
    birthDay: new Date().getTime(),
});

const validateStatus = ref({

	
	inputNameStatus: '',
	inputSurnameStatus: '',
	inputSexStatus: '',
	inputEmailStatus: '',
	inputPasswordStatus: '',
	inputConfirmPasswordStatus: '',
	inputDateStatus: '',
	inputPostalCode: '',
	inputHomeNumber: '',
	inputStreetName: '',
	inputCityName: '',


	
})

const nameInput = ref(null);

const registerUser = () => {
    const {name, surname, email, password, birthDay, confirmPassword, postCode, homeNumber, street, city, gender} = model.value;
    const user = {name, surname, email, password, birthDay, postCode, homeNumber, street, city, gender};

	let isCorrectValidate = true;
	if(name.length < 3){
		validateStatus.value.inputNameStatus = 'error';
		isCorrectValidate = false;
	}
	else{
		validateStatus.value.inputNameStatus = '';
	}
	if(surname.length < 3){
		validateStatus.value.inputSurnameStatus = 'error';
		isCorrectValidate = false;
	}
	else{
		validateStatus.value.inputSurnameStatus = '';
	}
	if((model.value.email.match("[a-z0-9_.-]+@[a-z0-9_.-]+\\.\\w{2,4}")) || (model.value.email.length == 0)){

			
			validateStatus.value.inputEmailStatus = '';
	}
	else{
		validateStatus.value.inputEmailStatus = 'error';
		isCorrectValidate = false;
	}

	if(model.value.password !== model.value.confirmPassword){
		validateStatus.value.inputPasswordStatus = 'error';
		validateStatus.value.inputConfirmPasswordStatus = 'error';
		isCorrectValidate = false;
	}
	else{
		
		validateStatus.value.inputPasswordStatus = '';
		validateStatus.value.inputConfirmPasswordStatus = '';
	}

	if((model.value.postCode.match("[0-9]{2}-[0-9]{3}")) || (model.value.postCode.length == 0)){
		validateStatus.value.inputPostalCode = '';
	}
	else{
		validateStatus.value.inputPostalCode = 'error';
	}
	if((model.value.homeNumber < 1 )){
		validateStatus.value.inputHomeNumber = 'error';
	}
	else{
		validateStatus.value.inputHomeNumber = '';
	}
	let streetRegex = /^[a-zA-Z]+$/;
	if((streetRegex.test(model.value.street)) && (model.value.street.length > 2)){
		validateStatus.value.inputStreetName = '';
	}
	else{
		validateStatus.value.inputStreetName = 'error';
	}
	if((streetRegex.test(model.value.city)) && (model.value.city.length > 2)){
		validateStatus.value.inputCityName = '';
	}
	else{
		validateStatus.value.inputCityName = 'error';
	}
	if(!model.value.gender){
		console.log('blad');
		validateStatus.value.inputSexStatus = 'error';
	}
	

	if(isCorrectValidate == false){
		toast.error("Popraw formularz", {timeout: 2000});
	}else{
		isLoading.value = true;
		axios({
			method: 'post',
			url: '/api/v1/registration',
			data: user,
    	})
		.then(()=>{
			toast.success("Rejestracja przebiegła pomyślnie", {timeout: 2000});
			router.push({path: '/login'});
		})
		.catch(exc=>{
			toast.error("Wystąpił błąd podczas ")
		}).finally(()=>{
			isLoading.value = false;
		})
	}
}

watch(model.value ,()=> {


		if((model.value.name.length < 3) && (model.value.name.length > 0)){

			validateStatus.value.inputNameStatus = 'warning';
		}
		else{
			validateStatus.value.inputNameStatus = '';
		}
		
		if((model.value.surname.length <3) && (model.value.surname.length > 0)){
			validateStatus.value.inputSurnameStatus = 'warning';
		}
		else
		{
			validateStatus.value.inputSurnameStatus = '';
		}
		
		if((model.value.password.length <6 ) || (model.value.password.length>30)){
			validateStatus.value.inputPasswordStatus = 'warning';
		}
		else{
			validateStatus.value.inputPasswordStatus = '';
		}

	})

	onMounted( ()=> {

		console.log('jd');
		validateStatus.value.inputNameStatus = '';
		validateStatus.value.inputSurnameStatus = '';
		validateStatus.value.inputEmailStatus = '';
		validateStatus.value.inputPasswordStatus = '';
		validateStatus.value.inputConfirmPasswordStatus = '';
		validateStatus.value.inputPostalCode = '';
		validateStatus.value.inputHomeNumber = '';
		validateStatus.value.inputStreetName = '';

	})




export default {
    setup() {
        return {
            model,
            registerUser,
			validateStatus,
			nameInput,
			isLoading
        }
    },
    components: {
        NForm,
        NFormItem,
        NInput,
        NButton,
        NDatePicker,
        NSpace,
        NRadio,
        NRadioGroup,
        HeaderNoInput,
		NSpin
    }
}
</script>

<style scoped lang="scss">
@import '../style/colors.scss';
.wrapper{
    width: 100%;
    height: 100%;

    .registerForm{
        margin: auto;
        max-width: 400px;
        padding: 10px;
        border: 2px solid rgba(128, 128, 128, 0.3);
        border-radius: 7px;
        margin-top: 3vh;
    }

    .button{
        width: 100%;
        padding: 20px;
        background-color: $base-color;
        color: white;
        font-size: 1rem;
    }

    .inline{
        display: flex;
        justify-content: space-between;

        & > *{
            width: 48%;
        }
    }
}
</style>