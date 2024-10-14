<template>
  <div class="container">
    <Breadcrumb :items="['其他', '页头导航']" />
    <a-card class="general-card" :title="'查询表格'">
      <a-page-header :show-back="false" title="ArcoDesign">
        <template #subtitle>
          <a-space>
            <span>ArcoDesign Vue 2.0</span>
            <a-tag color="red" size="small">Default</a-tag>
          </a-space>
        </template>
        <template #extra>
          <a-radio-group type="button" default-value="A" @change="handleChange">
            <a-radio value="A">Page A</a-radio>
            <a-radio value="B">Page B</a-radio>
          </a-radio-group>
        </template>
      </a-page-header>
      <KeepAlive>
        <component :is="current"></component>
      </KeepAlive>
  </a-card>
  </div>
</template>

<script setup>
  import { shallowRef } from 'vue';
  import CompA from './components/comp-a.vue';
  import CompB from './components/comp-b.vue';

  const current = shallowRef(CompA);

  const handleChange = (value) => {
    console.log(value);
    if (value === 'A') {
      current.value = CompA;
    } else if (value === 'B') {
      current.value = CompB;
    }
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }
</style>