<template>
  <CrudTable
    title="Career Postings"
    subtitle="Job openings shown on the Careers page"
    entity-name="Career Posting"
    :api="contentApi.careers"
    :columns="[
      { prop: 'title', label: 'Job Title' },
      { prop: 'department', label: 'Department', width: 140 },
      { prop: 'location', label: 'Location', width: 140 },
      { prop: 'jobType', label: 'Type', width: 110 },
      { prop: 'status', label: 'Open', type: 'switch', width: 90 }
    ]"
    :form-fields="[
      { prop: 'title', label: 'Job Title', type: 'text', required: true },
      { prop: 'department', label: 'Department', type: 'text' },
      { prop: 'location', label: 'Location', type: 'text' },
      { prop: 'jobType', label: 'Job Type (Full-time/Part-time)', type: 'text' },
      { prop: 'description', label: 'Description', type: 'textarea' },
      { prop: 'status', label: 'Currently Hiring', type: 'switch' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { contentApi } from '@/api/admin'
</script>
