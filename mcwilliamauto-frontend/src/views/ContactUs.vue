<template>
  <div class="container page">
    <h1>Contact Us</h1>
    <el-row :gutter="32">
      <el-col :span="12">
        <el-card>
          <ContactForm type="CONTACT" :initial-message="route.query.message || ''" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <h3>Location</h3>
        <p>{{ dealerInfo?.address || 'Address not configured' }}</p>
        <p v-if="dealerInfo?.phone">Phone: {{ dealerInfo.phone }}</p>
        <iframe
          v-if="mapSrc"
          :src="mapSrc"
          width="100%" height="300" style="border:0" loading="lazy"></iframe>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useDealerStore } from '@/store/useDealerStore'
import ContactForm from '@/components/forms/ContactForm.vue'

const route = useRoute()
const dealerStore = useDealerStore()
const { info: dealerInfo } = storeToRefs(dealerStore)
onMounted(() => dealerStore.ensureLoaded())

/** 优先用经纬度精确定位到dealer info配置的坐标，没有经纬度时退化为按地址文本搜索 */
const mapSrc = computed(() => {
  const info = dealerInfo.value
  if (!info) return null
  let q = null
  if (info.latitude != null && info.longitude != null) {
    q = `${info.latitude},${info.longitude}`
  } else if (info.address) {
    q = info.address
  }
  if (!q) return null
  return `https://maps.google.com/maps?q=${encodeURIComponent(q)}&z=15&output=embed`
})
</script>

<style scoped>
.page { padding: 24px 16px; }
</style>
