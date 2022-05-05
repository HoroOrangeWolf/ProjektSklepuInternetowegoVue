<template>
  <div class="wrapper">
    <div class="mod">
      <n-button class="addingButton" @click="initAddCategoryModal()">
        <fa class="icon" icon="plus" />
        Dodaj kategorie
      </n-button>
      <n-modal v-model:show="showModal">
        <n-card
          style="width: 600px"
          title="Dodawanie kategorii"
          :bordered="false"
          size="huge"
          role="dialog"
          aria-modal="true"
        >
          <template #header-extra> </template>

          <div class="categoryModal">
            <label>Nazwa kategorii</label>
            <input
              type="text"
              name=""
              id=""
              v-model="categoryModel.name"
              class="inputCategory"
            />

            <label>Kategoria nadrzędna</label>
            <div class="modelRow">
              <n-select
                :options="categoryOptions"
                v-model:value="categoryModel.parentCategoryId"
              />
              <n-button style="margin-left: 10px" @click="clearCategory">
                <fa icon="x" />
              </n-button>
            </div>
          </div>
          <div class="addingCategory">
            <button @click="updateCategory" v-if="isModification">Modyfikuj</button>
            <button @click="addCategory()" v-else>Dodaj</button>
          </div>
        </n-card>
      </n-modal>
      <n-modal v-model:show="showDeleteModal">
        <n-card
          style="width: 600px"
          :title="`Czy chcesz usunąć kategorie o nazwie ${toDelete.name}?`"
          :bordered="false"
          size="huge"
          role="dialog"
          aria-modal="true"
        >
          <div class="addingCategory" style="display: flex">
            <button @click="removeCategory" class="deleteButton">Tak</button>
            <button @click="showDeleteModal = false" class="deleteButton">Nie</button>
          </div>
        </n-card>
      </n-modal>
    </div>

    <n-spin :show="isLoading">
      <n-data-table
        v-if="!isLoading"
        :columns="createColums()"
        :data="categories"
        :pagination="{ pageSize: 10 }"
        :bordered="false"
      />
    </n-spin>
  </div>
</template>

<script setup>
import { h, ref, onMounted } from "vue";
import {
  NButton,
  NModal,
  NCard,
  NDataTable,
  NSpin,
  NButtonGroup,
  NSelect,
} from "naive-ui";
import TableSorter from "./Sorter/TableSorter.vue";

import axios from "axios";
import { useToast } from "vue-toastification";

const categories = ref([]);
const isLoading = ref(true);
const showModal = ref(false);
const showDeleteModal = ref(false);
const toDelete = ref({});
const isModification = ref(false);
const name = ref({ name: "" });
const categoryOptions = ref([]);

const toast = useToast();

const categoryModel = ref({
  name: "",
});

const clearModel = () => {
  categoryModel.value = {
    name: "",
  };
};

const createColums = () => [
  {
    title: "Lp",
    key: "no",
  },
  {
    title: "Nazwa",
    key: "name",
    sorter: {
      compare: (a, b) => {
        if (a.name > b.name) return 1;
        else if (a.name < b.name) return -1;
        return 0;
      },
    },
  },
  {
    title: "Akcje",
    render(row) {
      return [
        h(NButtonGroup, {}, () => [
          h(
            NButton,
            {
              onClick: () => {
                showModal.value = true;
                isModification.value = true;
                categoryModel.value = { ...row };
                categoryOptions.value = categories.value
                  .filter((f) => f.id !== row.id)
                  .map((f) => {
                    return {
                      label: f.name,
                      value: f.id,
                    };
                  });
              },

              style: "border-radius: 9999px 0 0 9999px",
              secondary: "",
            },
            () => "Modyfikuj"
          ),
          h(
            NButton,
            {
              onClick: () => {
                toDelete.value = row;
                showDeleteModal.value = true;
              },
              style: "border-radius: 0 9999px 9999px 0",
              secondary: "",
            },
            () => "Usun"
          ),
        ]),
      ];
    },
  },
];

const getCategories = async () => {
  return await axios({
    method: "get",
    url: "/api/v1/category",
  });
};
const loadCategories = async () => {
  isLoading.value = true;
  getCategories()
    .then((val) => {
      const ar = [];
      val.data.forEach((element, index) => {
        ar.push({ ...element, no: index + 1 });
      });
      categories.value = ar;
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isLoading.value = false;
      showModal.value = false;
    });
};

const clearCategory = () => {
  categoryModel.value.parentCategoryId = undefined;
};

const updateCategory = () => {
  isModification.value = true;
  isLoading.value = true;
  axios({
    method: "put",
    url: "/api/v1/category/" + categoryModel.value.id + "/admin",
    data: categoryModel.value,
  })
    .then(() => {
      toast.success("Udało się zaktualizować kategorie pomyślnie", { timeout: 2000 });
      loadCategories();
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
    })
    .finally(() => {
      isModification.value = false;
    });
};

const removeCategory = () => {
  showDeleteModal.value = false;
  isLoading.value = true;

  axios({
    method: "delete",
    url: "/api/v1/category/" + toDelete.value.id + "/admin",
  }).finally(() => {
    loadCategories().finally(() => {
      isLoading.value = false;
    });
  });
};

const initAddCategoryModal = () => {
  showModal.value = true;
  clearModel();
  categoryOptions.value = categories.value.map((f) => {
    return {
      label: f.name,
      value: f.id,
    };
  });
};

const addCategory = async () => {
  showModal.value = false;
  isLoading.value = true;
  try {
    await axios({
      method: "post",
      url: "/api/v1/category/admin",
      data: categoryModel.value,
    });
  } catch (exc) {
    toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
  }

  loadCategories();
  clearModel();
};

onMounted(() => {
  loadCategories();
});
</script>

<style scoped lang="scss">
* {
  box-sizing: border-box;
}
.wrapper {
  margin: 0 auto;
  width: 100%;
  max-width: 100%;

  .mod {
    width: 30%;
    margin-left: auto;
    display: flex;
    flex-direction: column;
  }
}

.categoryModal {
  padding: 10px 0 20px 0;
  border-top: 1px solid grey;
  border-bottom: 1px solid grey;

  label {
    display: block;
  }

  input {
    padding: 2px 0;
  }
  .modelRow {
    display: flex;
  }
}

.addingCategory {
  overflow: hidden;
  button {
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

.addingButton {
  margin: 10px;
  max-width: 300px;
  margin-left: auto;
  color: #fff;
  padding: 5px 10px;
  background-color: rgb(0, 183, 255);
  border-radius: 5px;

  .icon {
    margin-right: 10px;
  }

  &:hover {
    border: 0;
    background-color: rgb(0, 183, 255);
    color: #fff;
    outline: 0;
  }
}
</style>
