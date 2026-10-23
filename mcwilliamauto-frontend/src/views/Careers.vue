<template>
  <div class="container page">
    <h1>Careers</h1>
    <p>Join our team at {{ dealerInfo?.name || 'our dealership' }}. See our current openings below.</p>

    <div class="layout">
      <div v-loading="loading" class="list-col">
        <div v-if="careers.length" class="career-list">
          <el-card v-for="job in careers" :key="job.id" class="career-card">
            <div class="career-head">
              <h3>{{ job.title }}</h3>
              <div class="career-tags">
                <el-tag v-if="job.department" size="small">{{ job.department }}</el-tag>
                <el-tag v-if="job.location" size="small" type="info">{{ job.location }}</el-tag>
                <el-tag v-if="job.jobType" size="small" type="warning">{{ job.jobType }}</el-tag>
              </div>
            </div>
            <p v-if="job.description" class="career-desc">{{ job.description }}</p>
          </el-card>
        </div>
        <p v-else-if="!loading" class="empty-hint">There are no open positions right now. Please check back soon!</p>
      </div>
      <DealerContactSidebar />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { getCareers } from '@/api/content'
import { useDealerStore } from '@/store/useDealerStore'
import DealerContactSidebar from '@/components/layout/DealerContactSidebar.vue'

const careers = ref([])
const loading = ref(true)
const dealerStore = useDealerStore()
const { info: dealerInfo } = storeToRefs(dealerStore)

onMounted(async () => {
  dealerStore.ensureLoaded()
  try {
    careers.value = await getCareers()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page { padding: 24px 16px; }
.layout { display: grid; grid-template-columns: 2fr 1fr; gap: 24px; align-items: start; margin-top: 24px; }
.list-col { min-width: 0; }
.career-list { display: flex; flex-direction: column; gap: 16px; }
.career-head { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 8px; margin-bottom: 8px; }
.career-head h3 { margin: 0; font-family: 'Archivo', sans-serif; }
.career-tags { display: flex; gap: 6px; flex-wrap: wrap; }
.career-desc { color: var(--s-text2); line-height: 1.6; margin: 8px 0; white-space: pre-line; }
.empty-hint { color: var(--s-faint); }

@media (max-width: 900px) {
  .layout { grid-template-columns: 1fr; }
}
</style>
