<template>
  <div class="container page">
    <h1>{{ page?.title || 'About Us' }}</h1>
    <div v-loading="loading">
      <div v-if="page?.content" class="rte-content" v-html="page.content"></div>
      <p v-else-if="!loading" class="empty-hint">
        Content for this page hasn't been published yet.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPageByCode } from '@/api/content'

const page = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    page.value = await getPageByCode('about-us')
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page { padding: 24px 16px; }
.empty-hint { color: var(--s-faint); }
</style>
