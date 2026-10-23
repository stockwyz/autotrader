<template>
  <CrudTable
    title="Homepage Banners"
    subtitle="Hero slider images shown at the top of the homepage"
    entity-name="Banner"
    :api="contentApi.banners"
    :columns="[
      { prop: 'title', label: 'Title' },
      { prop: 'imageUrl', label: 'Image', type: 'image', width: 140 },
      { prop: 'linkUrl', label: 'Link URL' },
      { prop: 'sortNo', label: 'Sort Order', width: 120 },
      { prop: 'status', label: 'Enabled', type: 'switch', width: 100 }
    ]"
    :form-fields="[
      { prop: 'title', label: 'Title', type: 'text', required: true },
      { prop: 'imageUrl', label: 'Image', type: 'image', uploadUrl: '/admin/upload/banner-image', required: true },
      { prop: 'linkUrl', label: 'Link URL', type: 'text' },
      { prop: 'sortNo', label: 'Sort Order', type: 'number' },
      { prop: 'status', label: 'Enabled', type: 'switch' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { contentApi } from '@/api/admin'
</script>
