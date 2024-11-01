<template>
  <div class="container">
    <Breadcrumb :items="['列表页', '详情']" />
    <a-card class="general-card">
      <!-- <p>传入参数：{{this.$route}}</p> -->
      <p>记录ID：{{ this.$route.query.id }}</p>
    </a-card>
  </div>
</template>

<!-- <script setup>
  defineProps({
    recordId: String,
  });
</script> -->

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }
</style>
