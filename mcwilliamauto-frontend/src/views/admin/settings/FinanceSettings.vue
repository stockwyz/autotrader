<template>
  <div class="panel">
    <h2 class="panel-title">Loan Calculator Defaults</h2>
    <p class="panel-subtitle">Default parameters used by the public loan calculator and bi-weekly payment estimates on vehicle listings</p>
    <el-form :model="setting" label-position="top" v-loading="loading">
      <el-row :gutter="20">
        <el-col :span="12"><el-form-item label="Default Annual Interest Rate (%)"><el-input-number v-model="setting.defaultInterestRate" :step="0.01" style="width:100%" :controls="false" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="Default Term (months)"><el-input-number v-model="setting.defaultTermMonths" style="width:100%" :controls="false" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="Minimum Term (months)"><el-input-number v-model="setting.minTermMonths" style="width:100%" :controls="false" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="Maximum Term (months)"><el-input-number v-model="setting.maxTermMonths" style="width:100%" :controls="false" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="Minimum Down Payment (%)"><el-input-number v-model="setting.minDownPaymentPct" :step="0.01" style="width:100%" :controls="false" /></el-form-item></el-col>
      </el-row>
      <el-button type="primary" :loading="saving" @click="save">Save Settings</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { settingsApi } from '@/api/admin'

const loading = ref(false)
const saving = ref(false)
const setting = reactive({})

onMounted(async () => {
  loading.value = true
  try {
    Object.assign(setting, await settingsApi.getFinanceSetting())
  } finally {
    loading.value = false
  }
})

async function save() {
  saving.value = true
  try {
    await settingsApi.saveFinanceSetting(setting)
    ElMessage.success('Finance settings saved')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped lang="scss">
.panel { background: #fff; border-radius: 20px; padding: 28px 32px; box-shadow: 0 1px 3px rgba(23,34,59,.06); }
.panel-title { font-family: 'Fraunces', serif; font-size: 22px; font-weight: 600; margin: 0; }
.panel-subtitle { color: #7A7364; font-size: 14px; margin: 4px 0 20px; }
</style>
