<template>
  <div class="menu-demo">
    <a-menu mode="horizontal" :default-selected-keys="['1']">
      <a-sub-menu key="0">
        <template #title>Navigation 1</template>
        <a-menu-item key="0_0">Menu 1</a-menu-item>
        <a-menu-item key="0_1">Menu 2</a-menu-item>
        <a-menu-item key="0_2">Menu 3</a-menu-item>
        <a-menu-item key="0_3">Menu 4</a-menu-item>
      </a-sub-menu>
      <a-menu-item key="1">Home</a-menu-item>
      <a-menu-item key="2">Solution</a-menu-item>
      <a-menu-item key="3">Cloud Service</a-menu-item>
    </a-menu>
  </div>    

  <p>Current component: A</p>
  <span>count: {{ count }}</span>
  <button @click="count++">+</button>

</template>

<script setup>
  import { ref } from 'vue';

  const count = ref(0);

</script>
