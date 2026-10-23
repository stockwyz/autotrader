<template>
  <CrudTable
    title="Body Styles"
    entity-name="Body Style"
    :api="dictApi.bodyStyles"
    :columns="columns"
    :form-fields="formFields"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { dictApi } from '@/api/admin'

const columns = [
  { prop: 'photoUrl', label: 'Photo', width: 160, type: 'image' },
  { prop: 'name', label: 'Name' },
  { prop: 'sortNo', label: 'Sort Order', width: 120 },
  { prop: 'status', label: 'Enabled', type: 'switch', width: 100 }
]

const formFields = [
  { prop: 'name', label: 'Name', type: 'text', required: true },
  { prop: 'photoUrl', label: 'Photo', type: 'image', uploadUrl: '/admin/upload/bodystyle-photo', required: true },
  { prop: 'sortNo', label: 'Sort Order', type: 'number', required: true },
  { prop: 'status', label: 'Enabled', type: 'switch' }
]
</script>
