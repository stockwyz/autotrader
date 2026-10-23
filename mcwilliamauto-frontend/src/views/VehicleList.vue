<template>
  <div class="container vehicle-list-page">
    <h1 class="page-title">Used Cars, SUVs, Trucks for Sale in Winnipeg, MB</h1>

    <FilterPanel v-model="filters" @search="onSearch" />

    <div class="resultbar">
      <div class="resultbar-left">
        <div class="rescount">{{ total }} Vehicles</div>
        <div class="sortrow">
          <span class="sort-label">Sort:</span>
          <span v-for="opt in sortOptions" :key="opt.value"
                class="sort-link" :class="{ active: sortBy === opt.value }"
                @click="onSortClick(opt.value)">
            {{ opt.label }}
            <svg v-if="sortBy === opt.value" viewBox="0 0 10 6" class="sort-arrow" :class="{ desc: sortOrder === 'desc' }">
              <path d="M1 1 L5 5 L9 1" stroke="currentColor" stroke-width="1.6" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
        </div>
      </div>
      <div class="viewtoggle">
        <button class="icon-btn" :class="{ active: viewMode === 'list' }" @click="viewMode = 'list'" title="List view">
          <svg viewBox="0 0 20 20" width="18" height="18"><rect x="2" y="3" width="16" height="2.4" rx="1" fill="currentColor"/><rect x="2" y="8.8" width="16" height="2.4" rx="1" fill="currentColor"/><rect x="2" y="14.6" width="16" height="2.4" rx="1" fill="currentColor"/></svg>
        </button>
        <button class="icon-btn" :class="{ active: viewMode === 'grid' }" @click="viewMode = 'grid'" title="Grid view">
          <svg viewBox="0 0 20 20" width="18" height="18"><rect x="2" y="2" width="7" height="7" rx="1" fill="currentColor"/><rect x="11" y="2" width="7" height="7" rx="1" fill="currentColor"/><rect x="2" y="11" width="7" height="7" rx="1" fill="currentColor"/><rect x="11" y="11" width="7" height="7" rx="1" fill="currentColor"/></svg>
        </button>
      </div>
    </div>

    <div v-loading="loading">
      <div class="grid" :class="{ list: viewMode === 'list' }">
        <VehicleCard v-for="v in vehicles" :key="v.id" v-reveal :vehicle="v" :mode="viewMode" />
      </div>
    </div>

    <el-pagination
      v-if="total > pageSize"
      layout="prev, pager, next"
      :total="total"
      :page-size="pageSize"
      :current-page="pageNum"
      @current-change="onPageChange"
      style="margin-top:32px; justify-content:center"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getVehicleList } from '@/api/vehicle'
import FilterPanel from '@/components/inventory/FilterPanel.vue'
import VehicleCard from '@/components/inventory/VehicleCard.vue'

const route = useRoute()
const vehicles = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(12)
const sortBy = ref('createdAt')
const sortOrder = ref('asc')
const viewMode = ref('grid')
const loading = ref(false)

const sortOptions = [
  { label: 'Make', value: 'make' },
  { label: 'Model', value: 'model' },
  { label: 'Year', value: 'year' },
  { label: 'Price', value: 'price' },
  { label: 'Mileage', value: 'mileage' },
  { label: 'Body Style', value: 'bodyStyle' }
]

function buildFiltersFromQuery(q) {
  return {
    make: q.make ? Number(q.make) : null,
    model: q.model ? Number(q.model) : null,
    minYear: q.minYear ? Number(q.minYear) : null,
    maxYear: q.maxYear ? Number(q.maxYear) : null,
    transmissionId: null,
    minPrice: null,
    maxPrice: null,
    minKm: null,
    maxKm: null,
    bodyStyleId: q.bodyStyleId ? Number(q.bodyStyleId) : null,
    exteriorColorId: null,
    engineId: null,
    fuelTypeId: null,
    keyword: '',
    sold: q.sold ? Boolean(Number(q.sold)) : null,
    featured: q.featured ? Boolean(Number(q.featured)) : null
  }
}

const filters = ref(buildFiltersFromQuery(route.query))

async function fetchData() {
  loading.value = true
  try {
    const res = await getVehicleList({
      makeId: filters.value.make,
      modelId: filters.value.model,
      minYear: filters.value.minYear,
      maxYear: filters.value.maxYear,
      transmissionId: filters.value.transmissionId,
      minPrice: filters.value.minPrice,
      maxPrice: filters.value.maxPrice,
      minKm: filters.value.minKm,
      maxKm: filters.value.maxKm,
      bodyStyleId: filters.value.bodyStyleId,
      exteriorColorId: filters.value.exteriorColorId,
      engineId: filters.value.engineId,
      fuelTypeId: filters.value.fuelTypeId,
      keyword: filters.value.keyword,
      sold: filters.value.sold,
      featured: filters.value.featured,
      sortBy: sortBy.value,
      sortOrder: sortOrder.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    vehicles.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function onSortClick(field) {
  if (sortBy.value === field) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortBy.value = field
    sortOrder.value = 'asc'
  }
  fetchData()
}

function onSearch() {
  pageNum.value = 1
  fetchData()
}

function onPageChange(p) {
  pageNum.value = p
  fetchData()
}

onMounted(fetchData)

/**
 * 侧边栏"Sold Vehicles"/"Used Vehicle Specials"这类菜单项点击时，如果已经停留在
 * /used-cars页面，Vue Router只会更新query参数、不会重新创建组件，filters这个
 * reactive对象只在setup时读了一次route.query，后续不会自动跟着变——所以之前
 * 点了没反应。这里监听route.query，只要sold/featured/make/model/bodyStyleId
 * 这些能从外部链接带进来的筛选条件变化，就重新同步filters并重新查询。
 */
watch(
  () => route.query,
  (q) => {
    filters.value = buildFiltersFromQuery(q)
    pageNum.value = 1
    fetchData()
  }
)
</script>

<style scoped lang="scss">
.page-title { font-family: 'Archivo', sans-serif; font-size: 26px; font-weight: 600; margin: 24px 0 20px; }

/* 加宽这个页面容器,缩小左右边距,让下面的车辆卡片能显示更大 */
.vehicle-list-page.container { max-width: 1680px; padding: 0 24px; }

.resultbar { display: flex; justify-content: space-between; align-items: flex-start; margin: 24px 0 20px; flex-wrap: wrap; gap: 12px; }
.resultbar-left { display: flex; flex-direction: column; gap: 8px; }
.rescount { font-family: 'Archivo', sans-serif; font-size: 18px; font-weight: 600; }
.sortrow { display: flex; align-items: center; gap: 18px; font-size: 15px; }
.sort-label { color: var(--s-muted); font-weight: 700; }
.sort-link { color: var(--s-muted); font-weight: 600; cursor: pointer; display: inline-flex; align-items: center; gap: 3px; }
.sort-link.active { color: var(--s-primary); }
.sort-link:hover { color: var(--s-primary); }
.sort-arrow { width: 10px; height: 6px; transition: transform .15s; }
.sort-arrow.desc { transform: rotate(180deg); }

.viewtoggle { display: flex; gap: 8px; }
.icon-btn {
  width: 38px; height: 38px; border-radius: 8px; border: none;
  background: var(--s-line); color: var(--s-muted); display: flex; align-items: center; justify-content: center;
  cursor: pointer; transition: .15s;
}
.icon-btn:hover { background: var(--s-line2); }
.icon-btn.active { background: var(--s-primary); color: #fff; }

.grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
.grid.list { display: flex; flex-direction: column; gap: 20px; }
@media (max-width: 992px) { .grid:not(.list) { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .grid:not(.list) { grid-template-columns: 1fr; } }
</style>
