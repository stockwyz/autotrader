<template>
  <div class="settings-page">
    <div class="panel">
      <h2 class="panel-title">Dealer Information</h2>
      <p class="panel-subtitle">Shown in the footer, Contact Us page, and map</p>
      <el-form :model="info" label-position="top" v-loading="loading">
        <el-form-item label="Dealer Logo">
          <div class="logo-upload">
            <div v-if="!logoPreview" class="logo-dropzone" @click="triggerLogoInput">
              <span class="plus-icon">+</span>
            </div>
            <div v-else class="logo-preview-wrap">
              <img :src="logoPreview" class="logo-preview" @click="triggerLogoInput" />
              <button type="button" class="logo-remove-btn" @click="onRemoveLogo">×</button>
            </div>
            <input type="file" ref="logoInputRef" accept="image/*" style="display:none" @change="onLogoFileSelected" />
          </div>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="Dealer Name"><el-input v-model="info.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Phone"><el-input v-model="info.phone" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Address"><el-input v-model="info.address" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Website URL"><el-input v-model="info.websiteUrl" placeholder="e.g. mcwilliamauto.ca" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Email"><el-input v-model="info.email" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Dealer Permit No."><el-input v-model="info.dealerPermitNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Facebook URL"><el-input v-model="info.facebookUrl" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Instagram URL"><el-input v-model="info.instagramUrl" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Latitude"><el-input-number v-model="info.latitude" style="width:100%" :controls="false" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="Longitude"><el-input-number v-model="info.longitude" style="width:100%" :controls="false" /></el-form-item></el-col>
        </el-row>
        <el-button type="primary" :loading="savingInfo" @click="saveInfo">Save Dealer Info</el-button>
      </el-form>
    </div>

    <div class="panel">
      <h2 class="panel-title">Business Hours</h2>
      <p class="panel-subtitle">Shown in footer and Directions page</p>
      <el-table :data="hours" v-loading="loading">
        <el-table-column label="Day" width="140">
          <template #default="{ row }">{{ dayLabel(row.dayOfWeek) }}</template>
        </el-table-column>
        <el-table-column label="Open Time">
          <template #default="{ row }"><el-time-select v-model="row.openTime" start="06:00" end="22:00" step="00:30" /></template>
        </el-table-column>
        <el-table-column label="Close Time">
          <template #default="{ row }"><el-time-select v-model="row.closeTime" start="06:00" end="23:30" step="00:30" /></template>
        </el-table-column>
        <el-table-column label="Closed" width="90">
          <template #default="{ row }">
            <el-checkbox :model-value="!row.openTime" @change="v => toggleClosed(row, v)" />
          </template>
        </el-table-column>
      </el-table>
      <el-button type="primary" style="margin-top:16px" :loading="savingHours" @click="saveHours">Save Business Hours</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { settingsApi } from '@/api/admin'
import request from '@/api/request'

const loading = ref(false)
const savingInfo = ref(false)
const savingHours = ref(false)
const info = reactive({})
const hours = ref([])

const logoInputRef = ref(null)
const logoPreview = ref(null)
const logoPendingFile = ref(null)
const logoRemoved = ref(false)

function triggerLogoInput() {
  logoInputRef.value?.click()
}

function onLogoFileSelected(e) {
  const file = e.target.files[0]
  if (!file) return
  logoPendingFile.value = file
  logoRemoved.value = false
  logoPreview.value = URL.createObjectURL(file)
  e.target.value = ''
}

function onRemoveLogo() {
  logoPreview.value = null
  logoPendingFile.value = null
  logoRemoved.value = true
}

const dayNames = ['', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
function dayLabel(d) { return dayNames[d] || d }

function toggleClosed(row, checked) {
  if (checked) { row.openTime = null; row.closeTime = null }
  else { row.openTime = '08:00'; row.closeTime = '19:00' }
}

async function load() {
  loading.value = true
  try {
    const [infoRes, hoursRes] = await Promise.all([
      settingsApi.getDealerInfo(),
      settingsApi.listBusinessHours()
    ])
    Object.assign(info, infoRes)
    if (info.logoUrl) logoPreview.value = info.logoUrl
    hours.value = hoursRes.length ? hoursRes : Array.from({ length: 7 }, (_, i) => ({ dayOfWeek: i + 1, openTime: '08:00', closeTime: '19:00' }))
  } finally {
    loading.value = false
  }
}

async function saveInfo() {
  savingInfo.value = true
  try {
    if (logoPendingFile.value) {
      const formData = new FormData()
      formData.append('file', logoPendingFile.value)
      formData.append('name', info.name || 'dealer')
      const res = await request.post('/admin/upload/dealer-logo', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      info.logoUrl = res.url
      logoPendingFile.value = null
    } else if (logoRemoved.value) {
      info.logoUrl = null
      logoRemoved.value = false
    }
    await settingsApi.saveDealerInfo(info)
    ElMessage.success('Dealer info saved')
  } finally {
    savingInfo.value = false
  }
}

async function saveHours() {
  savingHours.value = true
  try {
    await settingsApi.saveBusinessHours(hours.value)
    ElMessage.success('Business hours saved')
  } finally {
    savingHours.value = false
  }
}

onMounted(load)
</script>

<style scoped lang="scss">
.settings-page { display: flex; flex-direction: column; gap: 24px; }
.panel { background: #fff; border-radius: 20px; padding: 28px 32px; box-shadow: 0 1px 3px rgba(23,34,59,.06); }
.panel-title { font-family: 'Fraunces', serif; font-size: 22px; font-weight: 600; margin: 0; }
.panel-subtitle { color: #7A7364; font-size: 14px; margin: 4px 0 20px; }

.logo-upload { display: flex; }
.logo-dropzone {
  width: 88px; height: 88px; border: 2px dashed #D8D3C4; border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; background: #FBF8F2; transition: .15s;
}
.logo-dropzone:hover { border-color: #E0684A; background: #FBEFE8; }
.plus-icon { font-size: 32px; color: #B5AE9B; font-weight: 300; line-height: 1; }
.logo-preview-wrap { position: relative; width: 88px; height: 88px; }
.logo-preview {
  width: 100%; height: 100%; object-fit: contain; border: 1px solid #EAE3D4;
  border-radius: 14px; background: #fff; cursor: pointer;
}
.logo-remove-btn {
  position: absolute; top: -8px; right: -8px; width: 24px; height: 24px;
  border-radius: 50%; background: #C1502E; color: #fff; border: 2px solid #fff;
  font-size: 14px; line-height: 1; cursor: pointer; display: flex; align-items: center; justify-content: center;
  padding: 0;
}
.logo-remove-btn:hover { background: #8C3822; }
</style>
