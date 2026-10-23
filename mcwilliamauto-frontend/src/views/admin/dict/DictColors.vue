<template>
  <CrudTable
    title="Colours"
    entity-name="Colour"
    :api="dictApi.colors"
    :columns="[
      { prop: 'name', label: 'Name' },
      { prop: 'hexCode', label: 'Hex Code', width: 140 }
    ]"
    :form-fields="[
      { prop: 'name', label: 'Name', type: 'text', required: true },
      { prop: 'hexCode', label: 'Hex Code (e.g. #3E6FA8)', type: 'text' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { dictApi } from '@/api/admin'
</script>
