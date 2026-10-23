<template>
  <div class="search-wrap">
    <div class="search-bar">
      <div class="field">
        <label>Make</label>
        <el-select v-model="form.make" placeholder="Any Make" clearable @change="onMakeChange">
          <el-option v-for="m in makes" :key="m.id" :label="m.name" :value="m.id">
            <img v-if="m.logoUrl" :src="m.logoUrl" :alt="m.name" class="make-logo" />
            {{ m.name }}
          </el-option>
        </el-select>
      </div>
      <div class="field">
        <label>Model</label>
        <el-select v-model="form.model" placeholder="Any Model" clearable>
          <el-option v-for="m in models" :key="m.id" :label="m.name" :value="m.id" />
        </el-select>
      </div>
      <div class="field">
        <label>Min Year</label>
        <el-select v-model="form.minYear" placeholder="Any" clearable>
          <el-option v-for="y in years" :key="y" :label="y" :value="y" :disabled="form.maxYear != null && y > form.maxYear" />
        </el-select>
      </div>
      <div class="field">
        <label>Max Year</label>
        <el-select v-model="form.maxYear" placeholder="Any" clearable>
          <el-option v-for="y in years" :key="y" :label="y" :value="y" :disabled="form.minYear != null && y < form.minYear" />
        </el-select>
      </div>
      <el-button type="primary" size="large" @click="onSearch">Search Vehicles</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMakes, getModels } from '@/api/dict'

const router = useRouter()
const makes = ref([])
const models = ref([])
const currentYear = new Date().getFullYear()
const years = Array.from({ length: 25 }, (_, i) => currentYear - i)

const form = reactive({ make: null, model: null, minYear: null, maxYear: null })

onMounted(async () => {
  makes.value = await getMakes()
})

async function onMakeChange(makeId) {
  form.model = null
  models.value = makeId ? await getModels(makeId) : []
}

function onSearch() {
  router.push({
    path: '/used-cars',
    query: {
      make: form.make || undefined,
      model: form.model || undefined,
      minYear: form.minYear || undefined,
      maxYear: form.maxYear || undefined
    }
  })
}
</script>

<style scoped lang="scss">
.search-wrap { position: relative; z-index: 2; margin-top: -64px; padding: 0 40px; }
.search-bar {
  max-width: 1200px; margin: 0 auto;
  background: #fff; border-radius: 12px; border: 1px solid var(--s-line);
  box-shadow: 0 18px 40px rgba(var(--s-primary-rgb), .12);
  padding: 26px 28px;
  display: flex; align-items: flex-end; gap: 18px; flex-wrap: wrap;
}
.field { flex: 1; min-width: 190px; }
.field label { display: block; font: 800 17px 'Archivo', sans-serif; color: var(--s-muted); margin-bottom: 8px; text-transform: uppercase; letter-spacing: .1em; }
:deep(.el-select) { width: 100%; }
:deep(.el-select__wrapper) { padding: 8px 14px !important; }
:deep(.el-select__placeholder), :deep(.el-select__selected-item) { font-size: 15px !important; font-weight: 600 !important; }
:deep(.el-button) { padding: 0 28px !important; height: 46px !important; }
</style>
