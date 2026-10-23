<template>
  <div class="container featured-vehicles">
    <div class="eyebrow-label">Best-priced this week</div>
    <h2 class="section-title">Featured Vehicles</h2>
    <p class="section-subtitle">Our best-priced vehicles this week, first come first served.</p>
    <div class="grid">
      <VehicleCard v-for="v in vehicles" :key="v.id" v-reveal :vehicle="v" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getVehicleList } from '@/api/vehicle'
import VehicleCard from '@/components/inventory/VehicleCard.vue'

const vehicles = ref([])
onMounted(async () => {
  const res = await getVehicleList({ featured: true, pageNum: 1, pageSize: 6 })
  vehicles.value = res.list
})
</script>

<style scoped>
/* 跟搜索结果页(VehicleList)保持一致的容器宽度和左右边距，卡片大小才会完全一样 */
.featured-vehicles.container { max-width: 1680px; padding: 0 24px; }
.grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
@media (max-width: 992px) { .grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .grid { grid-template-columns: 1fr; } }
</style>
