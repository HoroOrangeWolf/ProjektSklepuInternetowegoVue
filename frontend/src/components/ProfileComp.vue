<script setup>
const props = defineProps({
  name: {
    default: "Nazwa",
  },
  role: {
    default: "Brak roli",
  },
});
</script>

<template>
  <div class="wrapper">
    <div class="wrapper-items">
      <div class="info">
        <p class="name">Zalogowany jako: {{ props.name }}</p>
        <p class="job">Rola: {{ props.role }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style scoped lang="scss">
.wrapper {
  background-color: #fff;
  max-height: 100%;

  .wrapper-items {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 1rem;
    height: 100%;
    float: right;
  }

  .info {
    .name {
      margin-bottom: 0.5rem;
      font-weight: bold;
    }
  }
}
</style>
