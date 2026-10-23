<template>
  <div class="container page">
    <h1>Apply For Credit</h1>
    <p class="lead-text"><strong>Get approved from home!</strong><br />Please fill out the secure credit application below.</p>
    <div class="layout">
      <el-card class="form-col">
        <LoanApplicationForm
          :initial-make="route.query.make || ''"
          :initial-model="route.query.model || ''"
          :initial-year="route.query.year || null"
          :initial-vin="route.query.vin || ''"
          :initial-message="route.query.message || ''"
        />
      </el-card>
      <DealerContactSidebar />
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import LoanApplicationForm from '@/components/forms/LoanApplicationForm.vue'
import DealerContactSidebar from '@/components/layout/DealerContactSidebar.vue'

const route = useRoute()
</script>

<style scoped>
.page { padding: 24px 16px; }
.lead-text { margin: 0 0 20px; line-height: 1.6; }
.layout { display: grid; grid-template-columns: 2fr 1fr; gap: 24px; align-items: start; }
.form-col { min-width: 0; }

@media (max-width: 900px) {
  .layout { grid-template-columns: 1fr; }
}
</style>
