<template>
  <div class="container page">
    <h1>Trade-In Appraisal</h1>
    <p>Get an estimate for your current vehicle.</p>
    <div class="layout">
      <el-card class="form-col">
        <TradeInForm />
      </el-card>
      <DealerContactSidebar />
    </div>
  </div>
</template>

<script setup>
import TradeInForm from '@/components/forms/TradeInForm.vue'
import DealerContactSidebar from '@/components/layout/DealerContactSidebar.vue'
</script>

<style scoped>
.page { padding: 24px 16px; }
.layout { display: grid; grid-template-columns: 2fr 1fr; gap: 24px; align-items: start; }
.form-col { min-width: 0; }

@media (max-width: 900px) {
  .layout { grid-template-columns: 1fr; }
}
</style>
