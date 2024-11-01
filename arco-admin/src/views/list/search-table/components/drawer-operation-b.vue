<template>
  <div class="drawer-operation-container">
    <a-drawer :width="340" :visible="visible" unmount-on-close @ok="handleOk()" @cancel="handleCancel()" >
      <template #title>
        B
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
  emits: ['hideDrawerOperationB'],
  setup(props, { emit }) {
    const handleOk = () => {
    };

    const handleCancel = () => {
      emit('hideDrawerOperationB');
    };

    return {
      handleOk,
      handleCancel
    }
  },
};
</script>