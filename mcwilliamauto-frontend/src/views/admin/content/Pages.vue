<template>
  <CrudTable
    title="Static Pages"
    subtitle="Rich-text content for About Us, Finance, Services, Detailing, Privacy, etc."
    entity-name="Page"
    :api="contentApi.pages"
    :paged="true"
    dialog-width="920px"
    :edit-route-resolver="row => row.code === 'about-us' ? '/admin/content/about-us' : null"
    :columns="[
      { prop: 'code', label: 'Page Code', width: 160 },
      { prop: 'title', label: 'Title' },
      { prop: 'seoTitle', label: 'SEO Title' }
    ]"
    :form-fields="[
      { prop: 'code', label: 'Page Code (e.g. about-us, finance, services)', type: 'text', required: true },
      { prop: 'title', label: 'Title', type: 'text', required: true },
      { prop: 'content', label: 'Content', type: 'richtext', uploadUrl: '/admin/upload/page-content-image' },
      { prop: 'seoTitle', label: 'SEO Title', type: 'text' },
      { prop: 'seoKeywords', label: 'SEO Keywords', type: 'text' },
      { prop: 'seoDescription', label: 'SEO Description', type: 'textarea' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { contentApi } from '@/api/admin'
</script>
