<template>
  <CrudTable
    title="Engines"
    entity-name="Engine"
    :api="dictApi.engines"
    :columns="[{ prop: 'name', label: 'Name' }]"
    :form-fields="[{ prop: 'name', label: 'Name', type: 'text', required: true }]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { dictApi } from '@/api/admin'
</script>
