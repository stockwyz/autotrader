<template>
  <CrudTable
    ref="tableRef"
    title="Models"
    subtitle="Vehicle models, each linked to a Make"
    entity-name="Model"
    :api="dictApi.models"
    :columns="columns"
    :form-fields="formFields"
  />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import CrudTable from '@/components/admin/CrudTable.vue'
import { dictApi } from '@/api/admin'

const tableRef = ref(null)
const makes = ref([])

onMounted(async () => {
  const res = await dictApi.makes.list({ pageNum: 1, pageSize: 200 })
  makes.value = res.records || res || []
})

const makeOptions = computed(() =>
  makes.value.map(m => ({ value: m.id, label: m.name, icon: m.logoUrl }))
)

const columns = computed(() => [
  { prop: 'makeLogoUrl', label: 'Make', width: 160, type: 'image' },
  { prop: 'name', label: 'Name' },
  { prop: 'sortNo', label: 'Sort Order', width: 120 },
  { prop: 'status', label: 'Enabled', type: 'switch', width: 100 }
])

const formFields = computed(() => [
  { prop: 'name', label: 'Name', type: 'text', required: true },
  { prop: 'makeId', label: 'Make', type: 'select', options: makeOptions.value, filterable: true, required: true },
  { prop: 'sortNo', label: 'Sort Order', type: 'number', required: true },
  { prop: 'status', label: 'Enabled', type: 'switch' }
])
</script>
