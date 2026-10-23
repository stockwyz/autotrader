<template>
  <el-card class="side-col">
    <h3>Contact Information</h3>
    <p v-if="dealerInfo?.phone" class="side-line"><el-icon><Phone /></el-icon> Phone: {{ dealerInfo.phone }}</p>

    <template v-if="dealerInfo?.address">
      <p class="side-label"><el-icon><Location /></el-icon> Address:</p>
      <p class="side-line address">{{ dealerInfo.address }}</p>
    </template>

    <template v-if="formattedHours.length">
      <p class="side-label"><el-icon><Clock /></el-icon> Business Hours:</p>
      <div class="hours-grid">
        <template v-for="h in formattedHours" :key="h.day">
          <span>{{ h.day }}</span><span>{{ h.time }}</span>
        </template>
      </div>
    </template>

    <iframe v-if="mapSrc" :src="mapSrc" class="side-map" style="border:0" loading="lazy"></iframe>
  </el-card>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { Phone, Location, Clock } from '@element-plus/icons-vue'
import { useDealerStore } from '@/store/useDealerStore'

const dealerStore = useDealerStore()
const { info: dealerInfo, hours } = storeToRefs(dealerStore)
onMounted(() => dealerStore.ensureLoaded())

const DAY_NAMES = ['', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']

function formatTime(t) {
  if (!t) return null
  const [hStr, mStr] = t.split(':')
  let h = parseInt(hStr, 10)
  const period = h >= 12 ? 'PM' : 'AM'
  h = h % 12 || 12
  return `${h}:${mStr} ${period}`
}

const formattedHours = computed(() => {
  const sorted = [...(hours.value || [])].sort((a, b) => a.dayOfWeek - b.dayOfWeek)
  return sorted.map(h => ({
    day: DAY_NAMES[h.dayOfWeek] || h.dayOfWeek,
    time: h.openTime && h.closeTime ? `${formatTime(h.openTime)} - ${formatTime(h.closeTime)}` : 'Closed'
  }))
})

/** 有经纬度就精确定位，没有就退化成按地址文本搜索——跟ContactUs.vue同一套逻辑 */
const mapSrc = computed(() => {
  const info = dealerInfo.value
  if (!info) return null
  let q = null
  if (info.latitude != null && info.longitude != null) q = `${info.latitude},${info.longitude}`
  else if (info.address) q = info.address
  if (!q) return null
  return `https://maps.google.com/maps?q=${encodeURIComponent(q)}&z=15&output=embed`
})
</script>

<style scoped>
.side-col { min-width: 0; }
.side-col h3 { margin: 0 0 16px; }
.side-label { display: flex; align-items: center; gap: 6px; font-weight: 600; margin: 16px 0 4px; }
.side-line { display: flex; align-items: center; gap: 6px; margin: 0 0 4px; color: var(--s-text2); }
.side-line.address { display: block; padding-left: 22px; }
.hours-grid { display: grid; grid-template-columns: auto 1fr; gap: 4px 16px; font-size: 14px; color: var(--s-text2); }
.side-map { width: 100%; height: 220px; border-radius: 8px; margin-top: 20px; }
</style>
