<template>
  <div class="drawer-operation-container">
    <a-drawer :width="540" :visible="visible" unmount-on-close @ok="handleOk()" @cancel="handleCancel()" >
      <template #title>
        A
      </template>
      <div>
        {{ `ID: ${id}` }}
      </div>
    </a-drawer>
</div>
</template>

<script>
export default {
  props: {  
    id: {  
      type: String,  
      required: true  
    },
    visible: {  
      type: Boolean,  
      required: true  
    } 
  },
  emits: ['hideDrawerOperationA'],

  setup(props, { emit }) {
    const handleOk = () => {
    };

    const handleCancel = () => {
      emit('hideDrawerOperationA');
    };

    return {
      handleOk,
      handleCancel
    }
  },
};
</script>

<style scoped lang="less">
  // .drawer-operation-container {
  //   background-color: rgb(29 33 41 / 3%);
  // }
</style>