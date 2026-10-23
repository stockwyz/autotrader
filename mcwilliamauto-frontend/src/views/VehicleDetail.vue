<template>
  <div class="container detail-page" v-loading="loading" v-if="vehicle">
    <div class="nav-links">
      <router-link to="/used-cars">&larr; Back to Inventory</router-link>
      <span>
        <a v-if="vehicle.prevVehicleId" @click="goTo(vehicle.prevVehicleId)">Previous</a>
        <a v-if="vehicle.nextVehicleId" @click="goTo(vehicle.nextVehicleId)">Next</a>
      </span>
    </div>

    <div class="title-row">
      <h1>{{ vehicle.year }} {{ vehicle.makeName }} {{ vehicle.modelName }}</h1>
      <a v-if="vehicle.carfaxPdfUrl" :href="vehicle.carfaxPdfUrl" target="_blank" rel="noopener" class="carfax-badge">
        <span class="carfax-badge-brand">CARFAX</span>
        <span class="carfax-badge-sub">CANADA · View Report</span>
      </a>
    </div>
    <div class="price-row">
      <span class="price">${{ Number(vehicle.price).toLocaleString() }}</span>
      <span class="biweekly">${{ vehicle.biweeklyPayment }} Bi-weekly for {{ vehicle.loanTermMonths }}mo</span>
    </div>

    <el-row :gutter="32" style="margin-top:20px">
      <el-col :span="16">
        <ImageGallery :images="vehicle.images" :vehicle="vehicle" />
        <video v-if="vehicle.videoUrl" :src="vehicle.videoUrl" controls class="vehicle-video" />
        <SpecTable :v="vehicle" />
        <div v-if="vehicle.description" class="description-block">
          <h3>Description</h3>
          <p class="description-text">{{ vehicle.description }}</p>
        </div>
        <OptionsAccordion :options="vehicle.options" style="margin-top:24px" />
        <FeatureCategoryList :features="vehicle.features" style="margin-top:24px" />
      </el-col>
      <el-col :span="8">
        <el-card>
          <h3>Request More Information</h3>
          <InquiryForm :vehicle-id="vehicle.id" />
        </el-card>
        <el-card style="margin-top:16px">
          <h3>Test Drive This Vehicle</h3>
          <TestDriveForm :vehicle-id="vehicle.id" />
        </el-card>
        <el-card style="margin-top:16px">
          <el-button type="warning" style="width:100%" @click="goFinancing">Apply For Financing</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getVehicleDetail } from '@/api/vehicle'
import ImageGallery from '@/components/vehicle/ImageGallery.vue'
import SpecTable from '@/components/vehicle/SpecTable.vue'
import FeatureCategoryList from '@/components/vehicle/FeatureCategoryList.vue'
import OptionsAccordion from '@/components/vehicle/OptionsAccordion.vue'
import InquiryForm from '@/components/forms/InquiryForm.vue'
import TestDriveForm from '@/components/forms/TestDriveForm.vue'

const route = useRoute()
const router = useRouter()
const vehicle = ref(null)
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    vehicle.value = await getVehicleDetail(route.params.id)
  } finally {
    loading.value = false
  }
}

function goTo(id) {
  router.push(`/cars/${id}`)
}

onMounted(fetchData)

/** 从车辆详情页点"Apply For Financing"跳过去时，带上这台车的信息，让贷款申请表单能预填 */
function goFinancing() {
  const v = vehicle.value
  if (!v) return
  const title = `${v.year} ${v.makeName} ${v.modelName}`
  router.push({
    path: '/car-loan-application',
    query: {
      make: v.makeName,
      model: v.modelName,
      year: v.year,
      vin: v.vin,
      message: `Hello, I'm interested in vehicle ${v.stockNo} - ${title}. I would like to learn more about this vehicle. Please contact me by phone or email. Thank you.`
    }
  })
}
watch(() => route.params.id, fetchData)
</script>

<style scoped lang="scss">
.vehicle-video { width: 100%; border-radius: 12px; margin-top: 16px; background: #000; max-height: 480px; }
.description-block { margin-top: 24px; }
.description-block h3 { margin-bottom: 8px; }

.title-row { display: flex; align-items: flex-start; justify-content: space-between; gap: 16px; flex-wrap: wrap; }
.title-row h1 { margin: 0; }
.carfax-badge {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  min-width: 140px; padding: 8px 18px; border-radius: 8px;
  background: #0A2E6E; color: #fff; text-decoration: none; text-align: center;
  box-shadow: 0 1px 3px rgba(0,0,0,.15); transition: transform .15s, box-shadow .15s;
}
.carfax-badge:hover { transform: translateY(-1px); box-shadow: 0 4px 10px rgba(0,0,0,.2); }
.carfax-badge-brand { font-family: Arial, sans-serif; font-weight: 800; font-style: italic; font-size: 20px; letter-spacing: 0.5px; }
.carfax-badge-sub { font-size: 10px; letter-spacing: 0.5px; opacity: .85; margin-top: 2px; }
.description-text { white-space: pre-line; line-height: 1.6; color: var(--s-text2); }
.detail-page { padding: 24px 16px; }
.nav-links { display: flex; justify-content: space-between; margin-bottom: 12px; }
.nav-links a { cursor: pointer; margin-left: 12px; color: var(--s-primary); }
.price-row { display: flex; align-items: baseline; gap: 16px; }
.price { font-size: 28px; font-weight: 800; color: var(--s-primary); }
.biweekly { color: var(--s-muted); }
</style>
