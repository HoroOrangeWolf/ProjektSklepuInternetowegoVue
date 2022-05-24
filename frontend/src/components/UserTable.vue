<template>
  <div class="wrapper">
    <n-input
      class="nInput"
      placeholder="Wyszukaj użytkownika po emailu"
      v-model:value="searchInput"
    >
      <template #suffix>
        <fa icon="magnifying-glass" />
      </template>
    </n-input>
    <n-spin :show="isLoading">
      <table-sorter-group @click="handleSorterClick">
        <n-data-table
          :columns="columns"
          :data="data"
          :pagination="false"
          :bordered="false"
        />
      </table-sorter-group>
    </n-spin>
    <n-pagination
      style="margin: 10px; margin-left: auto"
      v-if="!isLoading"
      v-model:page="userState.page"
      :page-count="totalPages"
    />
  </div>
</template>

<script setup>
import { h, ref, watch, reactive, onMounted } from "vue";
import {
  NButton,
  NModal,
  NCard,
  NDataTable,
  NBadge,
  NInput,
  NSpin,
  NPagination,
} from "naive-ui";
import axios from "axios";
import { useToast } from "vue-toastification";
import TableSorter from "./Sorter/TableSorter.vue";
import TableSorterGroup from "./Sorter/TableSorterGroup.vue";

const data = ref([]);
const searchInput = ref("");

const isLoading = ref(false);
const aborter = ref(new AbortController());

const totalPages = ref(1);

const userState = reactive({
  searchBy: "",
  sort: "ASC",
  sortBy: "id",
  page: 1,
});

onMounted(() => {
  getAllUsers();
});

const toast = useToast();

const getAllUsers = () => {
  aborter.value.abort();
  aborter.value = new AbortController();
  isLoading.value = true;

  axios({
    method: "get",
    url:
      "/api/v1/user/admin?searchBy=" +
      searchInput.value +
      "&limit=10&page=" +
      (userState.page - 1) +
      "&order=" +
      userState.sort +
      "&sortBy=" +
      userState.sortBy,
    signal: aborter.value.signal,
  })
    .then((response) => {
      const { list, totalCount } = response.data;
      data.value = list;
      totalPages.value = Math.ceil(totalCount / 10);
      isLoading.value = false;
    })
    .catch((exc) => {
      if (exc.message !== "canceled") {
        toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
        isLoading.value = false;
      }
    });
};

const blockUser = (userId, isBlock) => {
  isLoading.value = true;
  axios({
    method: "post",
    url: "/api/v1/user/" + userId + "/admin?block=" + isBlock,
  })
    .then(() => {
      toast.success("Zmieniono status konta użytkownika", { timeout: 2000 });
      getAllUsers();
    })
    .catch((exc) => {
      toast.error("Error, " + exc.response?.data.message, { timeout: 2000 });
      isLoading.value = false;
    });
};

const handleSorterClick = (data) => {
  userState.sort = data.isASC ? "ASC" : "DESC";
  userState.sortBy = data.name;
};

const columns = [
  {
    title: h(TableSorter, { name: "id" }, () => "Id"),
    key: "id",
  },
  {
    title: h(TableSorter, { name: "name" }, () => "Imie"),
    key: "name",
  },
  {
    title: h(TableSorter, { name: "surname" }, () => "Nazwisko"),
    key: "surname",
  },
  {
    title: h(TableSorter, { name: "email" }, () => "Email"),
    key: "email",
  },
  {
    title: h(TableSorter, { name: "birthDay" }, () => "Data urodzin"),
    key: "birthDay",
  },
  {
    title: h(TableSorter, { name: "role" }, () => "Rola"),
    key: "role",
    render: (keyValue) => {
      switch (keyValue.role) {
        case "ADMIN":
          return h(
            NBadge,
            {
              style:
                "border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: rgba(225, 85, 84, 0.9);",
            },
            () => "Admin"
          );
        case "USER":
          return h(
            NBadge,
            {
              style:
                "border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: blue;",
            },
            () => "User"
          );
      }
      return "";
    },
  },
  {
    title: h(TableSorter, { name: "isAccountLocked" }, () => "Status konta"),
    key: "lock",
    render: (keyValue) => {
      switch (keyValue.isBlocked) {
        case true:
          return h(
            NButton,
            {
              style:
                "border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: red;",
              onClick: () => blockUser(keyValue.id, false),
            },
            () => "Zablokowany"
          );
        case false:
          return h(
            NButton,
            {
              style:
                "border: 1px solid rgba(128, 128, 128, 0.4); padding: 3px; border-radius: 9999px; color: white; background-color: green;",
              onClick: () => blockUser(keyValue.id, true),
            },
            () => "Niezablokowany"
          );
      }
      return "";
    },
  },
];

watch(searchInput, () => {
  getAllUsers();
});

watch(
  () => {
    return { ...userState };
  },
  (newValue, oldValue) => {
    const { sortBy, searchBy, sort, page } = newValue;

    if (
      sortBy !== oldValue.sortBy ||
      searchBy !== oldValue.searchBy ||
      sort !== oldValue.sort
    ) {
      userState.page = 1;
    }
    isLoading.value = true;

    getAllUsers();
  }
);
</script>

<style scoped lang="scss">
.wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;

  .nInput {
    max-width: 250px;
    margin: 15px;
    align-self: flex-end;
  }
}
</style>
