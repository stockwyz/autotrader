<template>
  <div class="admin-page">
    <div class="page-header">
      <h2>{{ isEdit ? 'Edit Vehicle' : 'New Vehicle' }}</h2>
      <el-button @click="$router.push('/admin/vehicles')">← Back to Inventory</el-button>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <el-card style="margin-bottom:16px">
        <h3>Basic Information</h3>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="Stock Number">
              <el-input v-model="form.stockNo" disabled :placeholder="isEdit ? '' : 'Auto-generated on save'" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="VIN" prop="vin"><el-input v-model="form.vin" maxlength="30" show-word-limit /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Title" prop="title"><el-input v-model="form.title" maxlength="150" show-word-limit placeholder="e.g. 2022 Ram 1500 Limited 4x4" /></el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="Make" prop="makeId">
              <el-select v-model="form.makeId" @change="onMakeChange" style="width:100%" filterable>
                <el-option v-for="m in makes" :key="m.id" :label="m.name" :value="m.id">
                  <img v-if="m.logoUrl" :src="m.logoUrl" class="option-icon" />
                  {{ m.name }}
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Model" prop="modelId">
              <el-select v-model="form.modelId" style="width:100%" filterable>
                <el-option v-for="m in models" :key="m.id" :label="m.name" :value="m.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Year" prop="year">
              <el-select v-model="form.year" style="width:100%" filterable>
                <el-option v-for="y in yearOptions" :key="y" :label="y" :value="y" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="Body Style" prop="bodyStyleId">
              <el-select v-model="form.bodyStyleId" style="width:100%" filterable>
                <el-option v-for="b in bodyStyles" :key="b.id" :label="b.name" :value="b.id">
                  <img v-if="b.photoUrl" :src="b.photoUrl" class="option-icon" />
                  {{ b.name }}
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Transmission" prop="transmissionId">
              <el-select v-model="form.transmissionId" style="width:100%">
                <el-option v-for="t in transmissions" :key="t.id" :label="t.name" :value="t.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Engine" prop="engineId">
              <el-select v-model="form.engineId" style="width:100%">
                <el-option v-for="e in engines" :key="e.id" :label="e.name" :value="e.id" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="Engine Size"><el-input v-model="form.engineSize" maxlength="20" show-word-limit placeholder="e.g. 5.7 l" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Fuel Type" prop="fuelTypeId">
              <el-select v-model="form.fuelTypeId" style="width:100%">
                <el-option v-for="f in fuelTypes" :key="f.id" :label="f.name" :value="f.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Driveline"><el-input v-model="form.driveline" maxlength="20" show-word-limit placeholder="4X4/FWD/AWD" /></el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="Exterior Colour">
              <el-select v-model="form.exteriorColorId" style="width:100%" clearable>
                <el-option v-for="c in colors" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Interior Colour">
              <el-select v-model="form.interiorColorId" style="width:100%" clearable>
                <el-option v-for="c in colors" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" />

          <el-col :span="8">
            <el-form-item label="Doors"><el-input-number v-model="form.doors" style="width:100%" :controls="false" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Passengers"><el-input-number v-model="form.passengers" style="width:100%" :controls="false" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Odometer (Km)"><el-input-number v-model="form.odometerKm" style="width:100%" :controls="false" /></el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="City Fuel Consumption"><el-input v-model="form.cityFuelConsumption" maxlength="20" show-word-limit placeholder="13.8L/100Km" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Hwy Fuel Consumption"><el-input v-model="form.hwyFuelConsumption" maxlength="20" show-word-limit placeholder="10.7L/100Km" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Price ($)" prop="price"><el-input-number v-model="form.price" style="width:100%" :controls="false" /></el-form-item>
          </el-col>

          <el-col :span="24"><el-form-item label="Description"><el-input v-model="form.description" type="textarea" rows="3" /></el-form-item></el-col>

          <el-col :span="6"><el-form-item label="One Owner"><el-switch v-model="form.isOneOwner" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="Low KM"><el-switch v-model="form.isLowKm" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="Featured"><el-switch v-model="form.isFeatured" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="Sold"><el-switch v-model="form.isSold" /></el-form-item></el-col>
        </el-row>
      </el-card>

      <el-card style="margin-bottom:16px">
        <h3>CARFAX Report</h3>
        <div v-if="form.carfaxPdfUrl" class="carfax-preview-wrap">
          <a :href="form.carfaxPdfUrl" target="_blank" class="carfax-mini-badge">
            <span class="carfax-mini-brand">CARFAX</span>
            <span class="carfax-mini-sub">CANADA</span>
          </a>
          <button type="button" class="video-remove" title="Delete CARFAX PDF" @click="onDeleteCarfax">×</button>
        </div>
        <p class="upload-hint">Click below to choose a PDF file.</p>
        <el-upload
          action=""
          :auto-upload="false"
          :show-file-list="true"
          :limit="1"
          accept="application/pdf"
          :on-change="onCarfaxFileChange"
          :on-remove="() => (pendingCarfaxFile = null)"
        >
          <el-button>Choose PDF File</el-button>
        </el-upload>
        <p style="font-size:12px;color:#909399;margin-top:8px">Selected PDF will be uploaded when you click Save below.</p>
      </el-card>

      <el-card style="margin-bottom:16px">
        <h3>Vehicle Photos <span class="required-mark">*</span></h3>

        <p class="upload-hint">Click the <b>+</b> box below to select photo files. Drag thumbnails to reorder — the first one becomes the cover photo.</p>
        <div class="photo-picker-grid">
          <div
            v-for="(item, idx) in photoItems" :key="item.key"
            class="photo-picker-item"
            draggable="true"
            @dragstart="onPhotoDragStart(idx)"
            @dragover.prevent
            @drop="onPhotoDrop(idx)"
          >
            <img :src="item.source === 'existing' ? (item.thumbUrl || item.imageUrl) : item.previewUrl" />
            <span v-if="idx === 0" class="main-tag">Cover</span>
            <span v-if="item.source === 'new'" class="new-tag">New</span>
            <button type="button" class="photo-picker-remove" @click="onRemovePhotoItem(idx)">×</button>
          </div>
          <div class="photo-picker-add" @click="triggerPhotoInput">
            <span class="plus-icon">+</span>
          </div>
          <input type="file" ref="photoInputRef" accept="image/*" multiple style="display:none" @change="onPhotoFilesSelected" />
        </div>
        <p style="font-size:12px;color:#909399;margin-top:8px">Newly selected photos will be uploaded when you click Save below.</p>
      </el-card>

      <el-card style="margin-bottom:16px">
        <h3>Vehicle Video</h3>
        <div v-if="form.videoUrl" class="video-preview-wrap">
          <video :src="form.videoUrl" controls class="video-preview" />
          <button type="button" class="video-remove" title="Delete video" @click="onDeleteVideo">×</button>
        </div>
        <p class="upload-hint">Click below to choose a video file.</p>
        <el-upload
          action=""
          :auto-upload="false"
          :show-file-list="true"
          :limit="1"
          accept="video/*"
          :on-change="onVideoFileChange"
          :on-remove="() => (pendingVideoFile = null)"
        >
          <el-button>Choose Video File</el-button>
        </el-upload>
        <p style="font-size:12px;color:#909399;margin-top:8px">Selected video will be uploaded when you click Save below. Large files may take a while.</p>
      </el-card>

      <el-card style="margin-bottom:16px">
        <h3>Feature Categories (Mechanical/Safety/Technology/Comfort/Exterior/Offroad)</h3>
        <div v-for="(f, idx) in form.features" :key="idx" class="dynamic-row">
          <el-select v-model="f.category" placeholder="Category" style="width:180px">
            <el-option label="Mechanical" value="MECHANICAL" />
            <el-option label="Safety" value="SAFETY" />
            <el-option label="Technology" value="TECHNOLOGY" />
            <el-option label="Comfort" value="COMFORT" />
            <el-option label="Exterior" value="EXTERIOR" />
            <el-option label="Off-Road" value="OFFROAD" />
          </el-select>
          <el-input v-model="f.content" placeholder="Feature content, e.g. Adaptive Cruise Control" style="flex:1;margin:0 8px" />
          <el-button type="danger" circle :icon="Delete" @click="form.features.splice(idx, 1)" />
        </div>
        <el-button @click="form.features.push({ category: 'MECHANICAL', content: '' })">+ Add Feature</el-button>
      </el-card>

      <el-card style="margin-bottom:16px">
        <h3>Factory Options (Exterior/Interior/Mechanical/Safety/Entertainment)</h3>
        <div v-for="(o, idx) in form.options" :key="idx" class="dynamic-row">
          <el-select v-model="o.optionGroup" placeholder="Group" style="width:180px">
            <el-option label="Exterior" value="EXTERIOR" />
            <el-option label="Interior" value="INTERIOR" />
            <el-option label="Mechanical" value="MECHANICAL" />
            <el-option label="Safety" value="SAFETY" />
            <el-option label="Entertainment" value="ENTERTAINMENT" />
          </el-select>
          <el-input v-model="o.content" placeholder="Option content" style="flex:1;margin:0 8px" />
          <el-button type="danger" circle :icon="Delete" @click="form.options.splice(idx, 1)" />
        </div>
        <el-button @click="form.options.push({ optionGroup: 'EXTERIOR', content: '' })">+ Add Option</el-button>
      </el-card>

      <el-button type="primary" size="large" @click="onSave" :loading="saving">Save</el-button>
      <el-button size="large" @click="$router.push('/admin/vehicles')">← Back to Inventory</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { getMakes, getModels, getBodyStyles, getTransmissions, getEngines, getFuelTypes, getColors } from '@/api/dict'
import { adminGetVehicleDetail, adminCreateVehicle, adminUpdateVehicle, adminUploadVehicleImages, adminDeleteVehicleImage, adminReorderVehicleImages, adminDeleteVehicleVideo, adminDeleteVehicleCarfax } from '@/api/vehicle'
import request from '@/api/request'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)
const formRef = ref(null)

// 生成年份选项：从当前系统年份倒序到1970
const currentYear = new Date().getFullYear()
const yearOptions = []
for (let y = currentYear; y >= 1970; y--) yearOptions.push(y)

// rules 只用来在对应字段标签前显示红色必填星号（视觉提示），
// 真正的"点Save时按顺序逐个校验、弹窗提示"由下面 requiredFieldOrder + validateSequential 驱动，
// 不用 el-form 自带的 validate()（那个是一次性把所有不通过的字段都标红，不是逐个来）
const rules = {
  vin: [{ required: true, message: 'VIN is required', trigger: 'blur' }],
  title: [{ required: true, message: 'Title is required', trigger: 'blur' }],
  makeId: [{ required: true, message: 'Make is required', trigger: 'change' }],
  modelId: [{ required: true, message: 'Model is required', trigger: 'change' }],
  year: [{ required: true, message: 'Year is required', trigger: 'change' }],
  bodyStyleId: [{ required: true, message: 'Body Style is required', trigger: 'change' }],
  transmissionId: [{ required: true, message: 'Transmission is required', trigger: 'change' }],
  engineId: [{ required: true, message: 'Engine is required', trigger: 'change' }],
  fuelTypeId: [{ required: true, message: 'Fuel Type is required', trigger: 'change' }],
  price: [{ required: true, message: 'Price is required', trigger: 'change' }]
}

// 严格按表单上从上到下、从左到右出现的顺序排列，onSave时按这个顺序逐个检查，
// 遇到第一个空的就用弹窗提示并中止，不继续往下检查
const requiredFieldOrder = [
  { prop: 'vin', label: 'VIN' },
  { prop: 'title', label: 'Title' },
  { prop: 'makeId', label: 'Make' },
  { prop: 'modelId', label: 'Model' },
  { prop: 'year', label: 'Year' },
  { prop: 'bodyStyleId', label: 'Body Style' },
  { prop: 'transmissionId', label: 'Transmission' },
  { prop: 'engineId', label: 'Engine' },
  { prop: 'fuelTypeId', label: 'Fuel Type' },
  { prop: 'price', label: 'Price' }
]

function validateSequential() {
  for (const f of requiredFieldOrder) {
    const v = form[f.prop]
    if (v === null || v === undefined || v === '') {
      ElMessage.error(`${f.label} is required`)
      return false
    }
  }
  return true
}
const saving = ref(false)
const photoItems = ref([]) // 统一的图片列表：已上传的(existing)和刚选还没传的(new)混在同一个数组里，拖拽排序对两种都生效
const pendingVideoFile = ref(null)
const pendingCarfaxFile = ref(null)

const makes = ref([])
const models = ref([])
const bodyStyles = ref([])
const transmissions = ref([])
const engines = ref([])
const fuelTypes = ref([])
const colors = ref([])

const form = reactive({
  stockNo: '', vin: '', title: '', slug: '',
  makeId: null, modelId: null, year: new Date().getFullYear(), bodyStyleId: null,
  transmissionId: null, engineId: null, engineSize: '', fuelTypeId: null,
  driveline: '', exteriorColorId: null, interiorColorId: null, doors: 4, passengers: 5,
  odometerKm: 0, cityFuelConsumption: '', hwyFuelConsumption: '',
  price: 0, loanTermMonths: 72, description: '',
  isOneOwner: false, isLowKm: false, isFeatured: false, isSold: false, status: 1,
  dealerPermitNo: '',
  videoUrl: '', carfaxPdfUrl: '',
  features: [], options: []
})

onMounted(async () => {
  [makes.value, bodyStyles.value, transmissions.value, engines.value, fuelTypes.value, colors.value] =
    await Promise.all([getMakes(), getBodyStyles(), getTransmissions(), getEngines(), getFuelTypes(), getColors()])

  if (isEdit.value) {
    const data = await adminGetVehicleDetail(route.params.id)
    Object.assign(form, data)
    if (data.makeId) models.value = await getModels(data.makeId)
    form.features = (data.features || []).map(f => ({ category: f.category, content: f.content }))
    form.options = (data.options || []).map(o => ({ optionGroup: o.optionGroup, content: o.content }))
    photoItems.value = (data.images || []).map(img => ({
      key: `existing-${img.id}`, source: 'existing', id: img.id, imageUrl: img.imageUrl, thumbUrl: img.thumbUrl
    }))
  }
})

async function onMakeChange(makeId) {
  form.modelId = null
  models.value = makeId ? await getModels(makeId) : []
}

const photoInputRef = ref(null)
let photoUidCounter = 0

function triggerPhotoInput() {
  photoInputRef.value?.click()
}

function onPhotoFilesSelected(e) {
  const files = Array.from(e.target.files || [])
  files.forEach(file => {
    photoItems.value.push({
      key: `new-${++photoUidCounter}`,
      source: 'new',
      raw: file,
      previewUrl: URL.createObjectURL(file)
    })
  })
  e.target.value = '' // 允许重复选择同一批文件也能再次触发change
}

let photoDragIndex = null
function onPhotoDragStart(idx) {
  photoDragIndex = idx
}
function onPhotoDrop(idx) {
  if (photoDragIndex === null || photoDragIndex === idx) return
  const arr = photoItems.value
  const [moved] = arr.splice(photoDragIndex, 1)
  arr.splice(idx, 0, moved)
  photoDragIndex = null
}

function onVideoFileChange(file) {
  pendingVideoFile.value = file.raw
}

function onCarfaxFileChange(file) {
  const isPdf = file.raw.type === 'application/pdf' || /\.pdf$/i.test(file.name)
  if (!isPdf) {
    ElMessage.error('Only PDF files are allowed')
    return
  }
  pendingCarfaxFile.value = file.raw
}

/** 已上传的图片(source==='existing')删除要弹确认框、物理删除服务器上的文件，立即生效；
 *  刚选还没上传的图片(source==='new')只是本地状态，直接从数组里移除就行，不用请求服务器 */
async function onRemovePhotoItem(idx) {
  const item = photoItems.value[idx]
  if (item.source === 'existing') {
    await ElMessageBox.confirm('Delete this photo? This cannot be undone.', 'Confirm', { type: 'warning' })
    await adminDeleteVehicleImage(route.params.id, item.id)
    ElMessage.success('Photo deleted')
  }
  photoItems.value.splice(idx, 1)
}

async function onDeleteVideo() {
  await ElMessageBox.confirm('Delete this video? The file will be permanently removed. This cannot be undone.', 'Confirm', { type: 'warning' })
  await adminDeleteVehicleVideo(route.params.id)
  form.videoUrl = ''
  ElMessage.success('Video deleted')
}

async function onDeleteCarfax() {
  await ElMessageBox.confirm('Delete this CARFAX PDF? The file will be permanently removed. This cannot be undone.', 'Confirm', { type: 'warning' })
  await adminDeleteVehicleCarfax(route.params.id)
  form.carfaxPdfUrl = ''
  ElMessage.success('CARFAX PDF deleted')
}

/** 图片/视频/CARFAX一律先暂存在浏览器里，等主表单点Save保存成功(新增模式下拿到新id，编辑模式下用现有id)
 *  之后统一在这里一次性上传——新增车辆和编辑车辆走的是同一套逻辑，交互体验保持一致，不再有单独的"Upload"按钮。
 *  视频/CARFAX上传成功后要把返回的URL写回form，再由调用方统一save一次，否则文件传上去了但数据库字段没更新，
 *  下次进编辑页就看不到了（图片走的是单独的关联表attachImages，不需要额外回写form）。*/
async function uploadPendingMedia(vehicleId) {
  const newPhotoIndexes = photoItems.value
    .map((item, idx) => (item.source === 'new' ? idx : -1))
    .filter(idx => idx !== -1)

  if (newPhotoIndexes.length) {
    const formData = new FormData()
    newPhotoIndexes.forEach(idx => formData.append('files', photoItems.value[idx].raw))
    const uploaded = await adminUploadVehicleImages(vehicleId, formData) // 按发送顺序返回新建的图片[{id,imageUrl,thumbUrl}]

    // 把刚上传拿到的真实id按顺序填回photoItems里对应的"new"项，让它们跟"existing"项一样带上真实id
    newPhotoIndexes.forEach((idx, i) => {
      const created = uploaded[i]
      photoItems.value[idx] = {
        key: `existing-${created.id}`, source: 'existing', id: created.id,
        imageUrl: created.imageUrl, thumbUrl: created.thumbUrl
      }
    })

    // 全部图片现在都有真实id了，按当前(可能被拖拽调整过的)顺序整体持久化排序，第一张自动成为封面图
    await adminReorderVehicleImages(vehicleId, photoItems.value.map(item => item.id))
  } else if (isEdit.value && photoItems.value.length) {
    // 就算这次没新加照片，编辑模式下也可能单纯拖拽调整了已有照片的顺序，同样要持久化
    await adminReorderVehicleImages(vehicleId, photoItems.value.map(item => item.id))
  }
  if (pendingVideoFile.value) {
    const formData = new FormData()
    formData.append('file', pendingVideoFile.value)
    const res = await request.post(`/admin/upload/vehicle/${vehicleId}/video`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    form.videoUrl = res.url
    pendingVideoFile.value = null
  }
  if (pendingCarfaxFile.value) {
    const formData = new FormData()
    formData.append('file', pendingCarfaxFile.value)
    const res = await request.post(`/admin/upload/vehicle/${vehicleId}/carfax`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    form.carfaxPdfUrl = res.url
    pendingCarfaxFile.value = null
  }
}

async function onSave() {
  if (!validateSequential()) return

  if (!photoItems.value.length) {
    ElMessage.error('At least one vehicle photo is required')
    return
  }

  saving.value = true
  try {
    if (isEdit.value) {
      // 先上传，把可能返回的新videoUrl/carfaxPdfUrl合并进form，再一次性保存，避免漏写数据库
      await uploadPendingMedia(route.params.id)
      await adminUpdateVehicle(route.params.id, form)
      ElMessage.success('Saved successfully')
      router.push('/admin/vehicles')
    } else {
      const newId = await adminCreateVehicle(form)
      await uploadPendingMedia(newId)
      if (form.videoUrl || form.carfaxPdfUrl) {
        // 新增时videoUrl/carfaxPdfUrl只有传了文件才会有值，创建时还是空的，这里补一次保存
        await adminUpdateVehicle(newId, form)
      }
      ElMessage.success('Vehicle created successfully')
      router.push('/admin/vehicles')
    }
  } finally {
    saving.value = false
  }
}
</script>

<style scoped lang="scss">
.admin-page { padding: 24px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.required-mark { color: #C1502E; font-family: 'Manrope'; font-size: 16px; }
.upload-hint { font-size: 13px; color: #17223B; font-weight: 600; margin-bottom: 10px; }

.main-tag {
  position: absolute; bottom: 4px; left: 4px; background: rgba(23,34,59,.85); color: #fff;
  font-size: 10px; font-weight: 700; padding: 2px 7px; border-radius: 10px;
}
.new-tag {
  position: absolute; bottom: 4px; right: 4px; background: rgba(224,104,74,.9); color: #fff;
  font-size: 10px; font-weight: 700; padding: 2px 7px; border-radius: 10px;
}

.video-preview-wrap { position: relative; width: 100%; max-width: 480px; margin-bottom: 12px; }
.video-preview { width: 100%; display: block; border-radius: 8px; }
.video-remove {
  position: absolute; top: -8px; right: -8px; width: 26px; height: 26px;
  border-radius: 50%; background: #C1502E; color: #fff; border: 2px solid #fff;
  font-size: 15px; line-height: 1; cursor: pointer; display: flex; align-items: center; justify-content: center;
  padding: 0;
}
.video-remove:hover { background: #8C3822; }

.carfax-preview-wrap { position: relative; display: inline-block; margin-bottom: 12px; }
.carfax-mini-badge {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  min-width: 110px; padding: 6px 14px; border-radius: 6px;
  background: #0A2E6E; color: #fff; text-decoration: none; text-align: center;
}
.carfax-mini-brand { font-family: Arial, sans-serif; font-weight: 800; font-style: italic; font-size: 15px; letter-spacing: 0.5px; }
.carfax-mini-sub { font-size: 9px; letter-spacing: 0.5px; opacity: .85; }

.photo-picker-grid { display: flex; flex-wrap: wrap; gap: 10px; }
.photo-picker-item { position: relative; width: 100px; height: 100px; cursor: grab; }
.photo-picker-item:active { cursor: grabbing; }
.photo-picker-item img { width: 100%; height: 100%; object-fit: cover; border-radius: 6px; border: 1px solid #EAE3D4; pointer-events: none; }
.photo-picker-remove {
  position: absolute; top: -8px; right: -8px; width: 22px; height: 22px;
  border-radius: 50%; background: #C1502E; color: #fff; border: 2px solid #fff;
  font-size: 13px; line-height: 1; cursor: pointer; display: flex; align-items: center; justify-content: center;
  padding: 0;
}
.photo-picker-remove:hover { background: #8C3822; }
.photo-picker-add {
  width: 100px; height: 100px; border: 2px dashed #D8D3C4; border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; background: #FBF8F2; transition: .15s;
}
.photo-picker-add:hover { border-color: #E0684A; background: #FBEFE8; }
.plus-icon { font-size: 30px; color: #B5AE9B; font-weight: 300; line-height: 1; }
.dynamic-row { display: flex; align-items: center; margin-bottom: 10px; }
.option-icon { width: 20px; height: 20px; object-fit: contain; vertical-align: -5px; margin-right: 8px; }
</style>
