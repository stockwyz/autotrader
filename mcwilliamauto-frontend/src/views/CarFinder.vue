<template>
  <div class="container page">
    <h1>Car Finder</h1>
    <p>Can't find what you're looking for? Tell us what you need and we'll help find it.</p>
    <div class="layout">
      <el-card class="form-col">
        <CarFinderForm />
      </el-card>
      <DealerContactSidebar />
    </div>
  </div>
</template>

<script setup>
import CarFinderForm from '@/components/forms/CarFinderForm.vue'
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
