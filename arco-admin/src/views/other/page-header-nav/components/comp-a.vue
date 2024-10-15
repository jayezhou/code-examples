<template>
  <p>Current component: A</p>
  <span>count: {{ count }}</span>
  <button @click="count++">+</button>

  <a-link @click="handleClick">Link</a-link>
</template>

<script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';


  const router = useRouter();

  const count = ref(0);

  const handleClick = (value) => {
    // Trigger router change
    router.push({
      name: 'SearchTable',
    });
  };
</script>
