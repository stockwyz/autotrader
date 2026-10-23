<template>
  <CrudTable
    title="Makes"
    subtitle="Vehicle brands shown in search filters and vehicle forms"
    entity-name="Make"
    :api="dictApi.makes"
    :columns="[
      { prop: 'logoUrl', label: 'Logo', width: 160, type: 'image' },
      { prop: 'name', label: 'Name' },
      { prop: 'sortNo', label: 'Sort Order', width: 120 },
      { prop: 'status', label: 'Enabled', type: 'switch', width: 100 }
    ]"
    :form-fields="[
      { prop: 'name', label: 'Name', type: 'text', required: true },
      { prop: 'logoUrl', label: 'Logo', type: 'image', uploadUrl: '/admin/upload/make-logo', required: true },
      { prop: 'sortNo', label: 'Sort Order', type: 'number', required: true },
      { prop: 'status', label: 'Enabled', type: 'switch' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { dictApi } from '@/api/admin'
</script>
