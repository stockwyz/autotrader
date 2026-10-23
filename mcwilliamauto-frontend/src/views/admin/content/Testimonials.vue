<template>
  <CrudTable
    title="Customer Testimonials"
    subtitle="Reviews shown on the homepage and Reviews page"
    entity-name="Testimonial"
    :api="contentApi.testimonials"
    :columns="[
      { prop: 'customerName', label: 'Customer' },
      { prop: 'rating', label: 'Rating', width: 90 },
      { prop: 'content', label: 'Review' },
      { prop: 'source', label: 'Source', width: 110 },
      { prop: 'status', label: 'Published', type: 'switch', width: 100 }
    ]"
    :form-fields="[
      { prop: 'customerName', label: 'Customer Name', type: 'text', required: true },
      { prop: 'rating', label: 'Rating (1-5)', type: 'number' },
      { prop: 'content', label: 'Review Content', type: 'textarea', required: true },
      { prop: 'source', label: 'Source (e.g. Google)', type: 'text' },
      { prop: 'sortNo', label: 'Sort Order', type: 'number' },
      { prop: 'status', label: 'Published', type: 'switch' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { contentApi } from '@/api/admin'
</script>
