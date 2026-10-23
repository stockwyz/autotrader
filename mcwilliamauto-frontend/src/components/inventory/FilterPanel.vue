<template>
  <div class="hfilter">
    <div class="hfilter-title"><el-icon><Search /></el-icon> Search</div>
    <div class="hfilter-grid">
      <el-select v-model="local.make" clearable placeholder="Any Make" @change="onMakeChange">
        <el-option v-for="m in makes" :key="m.id" :label="m.name" :value="m.id">
          <img v-if="m.logoUrl" :src="m.logoUrl" :alt="m.name" class="make-logo" />
          {{ m.name }}
        </el-option>
      </el-select>
      <el-select v-model="local.model" clearable placeholder="Any Model">
        <el-option v-for="m in models" :key="m.id" :label="m.name" :value="m.id" />
      </el-select>
      <el-select v-model="local.minYear" clearable placeholder="Min Year">
        <el-option v-for="y in yearOptions" :key="y" :label="y" :value="y" :disabled="local.maxYear != null && y > local.maxYear" />
      </el-select>
      <el-select v-model="local.maxYear" clearable placeholder="Max Year">
        <el-option v-for="y in yearOptions" :key="y" :label="y" :value="y" :disabled="local.minYear != null && y < local.minYear" />
      </el-select>
      <el-select v-model="local.transmissionId" clearable placeholder="Any Transmission">
        <el-option v-for="t in transmissions" :key="t.id" :label="t.name" :value="t.id" />
      </el-select>

      <el-select v-model="local.minPrice" clearable placeholder="Min Price">
        <el-option v-for="p in priceOptions" :key="p" :label="`$${p.toLocaleString()}`" :value="p" :disabled="local.maxPrice != null && p > local.maxPrice" />
      </el-select>
      <el-select v-model="local.maxPrice" clearable placeholder="Max Price">
        <el-option v-for="p in priceOptions" :key="p" :label="`$${p.toLocaleString()}`" :value="p" :disabled="local.minPrice != null && p < local.minPrice" />
      </el-select>
      <el-select v-model="local.minKm" clearable placeholder="Min Km">
        <el-option v-for="k in kmOptions" :key="k" :label="k.toLocaleString()" :value="k" :disabled="local.maxKm != null && k > local.maxKm" />
      </el-select>
      <el-select v-model="local.maxKm" clearable placeholder="Max Km">
        <el-option v-for="k in kmOptions" :key="k" :label="k.toLocaleString()" :value="k" :disabled="local.minKm != null && k < local.minKm" />
      </el-select>
      <el-select v-model="local.bodyStyleId" clearable placeholder="Any Body Style">
        <el-option v-for="b in bodyStyles" :key="b.id" :label="b.name" :value="b.id" />
      </el-select>

      <el-select v-model="local.exteriorColorId" clearable placeholder="Any Colour">
        <el-option v-for="c in colors" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <el-select v-model="local.engineId" clearable placeholder="Any Engine">
        <el-option v-for="e in engines" :key="e.id" :label="e.name" :value="e.id" />
      </el-select>
      <el-select v-model="local.fuelTypeId" clearable placeholder="Any Fuel Type">
        <el-option v-for="f in fuelTypes" :key="f.id" :label="f.name" :value="f.id" />
      </el-select>
      <el-input v-model="local.keyword" placeholder="Keyword Search" :prefix-icon="Search" />
      <div class="search-btn-group">
        <el-button type="primary" @click="emitSearch">Search</el-button>
        <el-button @click="resetSearch">Reset</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { getMakes, getModels, getBodyStyles, getTransmissions, getEngines, getFuelTypes, getColors } from '@/api/dict'

const props = defineProps({ modelValue: { type: Object, default: () => ({}) } })
const emit = defineEmits(['update:modelValue', 'search'])

const local = reactive({ ...props.modelValue })
const makes = ref([])
const models = ref([])
const bodyStyles = ref([])
const transmissions = ref([])
const engines = ref([])
const fuelTypes = ref([])
const colors = ref([])

onMounted(async () => {
  [makes.value, bodyStyles.value, transmissions.value, engines.value, fuelTypes.value, colors.value] =
    await Promise.all([getMakes(), getBodyStyles(), getTransmissions(), getEngines(), getFuelTypes(), getColors()])
  if (local.make) models.value = await getModels(local.make)
})

// 年份：从当前系统年份倒序到1990
const currentYear = new Date().getFullYear()
const yearOptions = []
for (let y = currentYear; y >= 1990; y--) yearOptions.push(y)

// 价格：$1,000起，每$1,000递增到$20,000；$21,000-$25,000继续每$1,000递增；
// 之后$30,000-$80,000改成每$5,000递增
const priceOptions = []
for (let p = 1000; p <= 20000; p += 1000) priceOptions.push(p)
for (let p = 21000; p <= 25000; p += 1000) priceOptions.push(p)
for (let p = 30000; p <= 80000; p += 5000) priceOptions.push(p)

// 公里数：10,000起，每10,000递增到100,000；之后100,000-250,000改成每25,000递增
const kmOptions = []
for (let k = 10000; k <= 100000; k += 10000) kmOptions.push(k)
for (let k = 125000; k <= 250000; k += 25000) kmOptions.push(k)

async function onMakeChange(makeId) {
  local.model = null
  models.value = makeId ? await getModels(makeId) : []
}

function emitSearch() {
  emit('update:modelValue', { ...local })
  emit('search')
}

/** 把所有筛选条件恢复成缺省值(全部清空)，并立即执行一次查询 */
function resetSearch() {
  Object.assign(local, {
    make: null, model: null, minYear: null, maxYear: null, transmissionId: null,
    minPrice: null, maxPrice: null, minKm: null, maxKm: null, bodyStyleId: null,
    exteriorColorId: null, engineId: null, fuelTypeId: null, keyword: ''
  })
  models.value = []
  emit('update:modelValue', { ...local })
  emit('search')
}

watch(() => props.modelValue, (val) => Object.assign(local, val))
</script>

<style scoped lang="scss">
.hfilter { background: #fff; border-radius: 12px; padding: 24px 28px; border: 1px solid var(--s-line); }
.hfilter-title { display: flex; align-items: center; gap: 8px; font-family: 'Archivo', sans-serif; font-size: 18px; font-weight: 600; margin-bottom: 18px; }
.hfilter-grid {
  display: grid; grid-template-columns: repeat(5, 1fr); gap: 12px;
}
.hfilter-grid :deep(.el-select),
.hfilter-grid :deep(.el-input-number),
.hfilter-grid :deep(.el-input) { width: 100%; }
.hfilter-grid :deep(.el-input-number .el-input__inner) { text-align: left; }
.search-btn-group { display: flex; gap: 8px; }
.search-btn-group .el-button { flex: 1; margin-left: 0; }
</style>
