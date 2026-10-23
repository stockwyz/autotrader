<template>
  <CrudTable
    title="Top Navigation Menu"
    subtitle="Controls the header menu structure on the public site"
    entity-name="Menu Item"
    :api="contentApi.navMenu"
    :paged="false"
    :columns="[
      { prop: 'name', label: 'Name' },
      { prop: 'linkUrl', label: 'Link URL' },
      { prop: 'parentId', label: 'Parent ID (0 = top-level)', width: 170 },
      { prop: 'sortNo', label: 'Sort Order', width: 120 },
      { prop: 'status', label: 'Enabled', type: 'switch', width: 100 }
    ]"
    :form-fields="[
      { prop: 'name', label: 'Name', type: 'text', required: true },
      { prop: 'linkUrl', label: 'Link URL', type: 'text', required: true },
      { prop: 'parentId', label: 'Parent ID (0 for top-level)', type: 'number' },
      { prop: 'sortNo', label: 'Sort Order', type: 'number' },
      { prop: 'status', label: 'Enabled', type: 'switch' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { contentApi } from '@/api/admin'
</script>
