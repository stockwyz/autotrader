<template>
  <div class="admin-page">
    <div class="toolbar">
      <h2>Vehicle Inventory</h2>
      <el-button type="primary" @click="$router.push('/admin/vehicles/edit')">+ New Vehicle</el-button>
    </div>

    <el-form :inline="true" style="margin-bottom:16px">
      <el-form-item label="Keyword">
        <el-input v-model="keyword" placeholder="Title / Stock No / VIN" clearable @keyup.enter="fetchData" />
      </el-form-item>
      <el-form-item>
        <el-button @click="fetchData">Search</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" v-loading="loading" border style="width:100%">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="Photo" width="200">
        <template #default="{ row }">
          <img
            :src="row.mainThumbUrl || defaultPhoto"
            class="thumb"
            :class="{ clickable: !!row.mainThumbUrl }"
            @click="row.mainThumbUrl && openGallery(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="Title" width="320" show-overflow-tooltip />
      <el-table-column prop="year" label="Year" width="80" />
      <el-table-column prop="makeName" label="Make" width="200" />
      <el-table-column prop="modelName" label="Model" width="200" />
      <el-table-column prop="price" label="Price" width="100">
        <template #default="{ row }">${{ Number(row.price).toLocaleString() }}</template>
      </el-table-column>
      <el-table-column label="Sold" width="80">
        <template #default="{ row }">
          <el-switch :model-value="row.isSold" @change="v => toggleStatus(row, 'sold', v)" />
        </template>
      </el-table-column>
      <el-table-column label="Featured" width="110" class-name="nowrap-header">
        <template #default="{ row }">
          <el-switch :model-value="row.isFeatured" @change="v => toggleStatus(row, 'featured', v)" />
        </template>
      </el-table-column>
      <el-table-column label="Actions" width="180">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" @click="$router.push(`/admin/vehicles/edit/${row.id}`)">Edit</el-button>
            <el-button size="small" type="danger" @click="onDelete(row.id)">Delete</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > pageSize"
      layout="prev, pager, next"
      :total="total" :page-size="pageSize" :current-page="pageNum"
      @current-change="p => { pageNum = p; fetchData() }"
      style="margin-top:16px; justify-content:center"
    />

    <!-- 全屏图片/视频画廊：点缩略图弹出，遮罩住整个页面（包括后面的Inventory列表） -->
    <Teleport to="body">
      <div v-if="galleryVisible" class="gallery-mask" @click.self="closeGallery">
        <button class="gallery-close" @click="closeGallery">×</button>
        <button class="gallery-arrow left" @click="prevItem">‹</button>
        <div class="gallery-content">
          <img v-if="currentItem.type === 'image'" :src="currentItem.url" class="gallery-media" />
          <video v-else :src="currentItem.url" controls autoplay class="gallery-media" />
          <div class="gallery-counter">{{ galleryIndex + 1 }} / {{ galleryItems.length }}</div>
        </div>
        <button class="gallery-arrow right" @click="nextItem">›</button>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminGetVehicleList, adminGetVehicleDetail, adminDeleteVehicle, adminUpdateVehicleStatus } from '@/api/vehicle'
import defaultPhoto from '@/assets/default-vehicle-photo.svg'

const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const keyword = ref('')
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const res = await adminGetVehicleList({ keyword: keyword.value, pageNum: pageNum.value, pageSize: pageSize.value })
    list.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

async function toggleStatus(row, field, value) {
  await adminUpdateVehicleStatus(row.id, field, value)
  ElMessage.success('Updated successfully')
  fetchData()
}

async function onDelete(id) {
  await ElMessageBox.confirm('Delete this vehicle? This cannot be undone.', 'Confirm', { type: 'warning' })
  await adminDeleteVehicle(id)
  ElMessage.success('Deleted successfully')
  fetchData()
}

// ---------- 全屏画廊 ----------
const galleryVisible = ref(false)
const galleryItems = ref([]) // [{ type: 'image'|'video', url }]
const galleryIndex = ref(0)
const currentItem = computed(() => galleryItems.value[galleryIndex.value] || { type: 'image', url: '' })

async function openGallery(row) {
  const detail = await adminGetVehicleDetail(row.id)
  const items = (detail.images || []).map(img => ({ type: 'image', url: img.imageUrl }))
  if (detail.videoUrl) {
    items.push({ type: 'video', url: detail.videoUrl })
  }
  if (!items.length) {
    ElMessage.info('No media to display')
    return
  }
  galleryItems.value = items
  galleryIndex.value = 0
  galleryVisible.value = true
}

function closeGallery() {
  galleryVisible.value = false
}

function prevItem() {
  galleryIndex.value = (galleryIndex.value - 1 + galleryItems.value.length) % galleryItems.value.length
}

function nextItem() {
  galleryIndex.value = (galleryIndex.value + 1) % galleryItems.value.length
}

onMounted(fetchData)
</script>

<style scoped>
.admin-page { padding: 24px; }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }

.thumb { width: 160px; height: 110px; object-fit: cover; border-radius: 6px; display: block; }
.thumb.clickable { cursor: pointer; }
.thumb.clickable:hover { opacity: .85; }
:deep(.nowrap-header .cell) { white-space: nowrap; }

.gallery-mask {
  position: fixed; inset: 0; background: rgba(0,0,0,.88);
  z-index: 3000; display: flex; align-items: center; justify-content: center;
}
.gallery-close {
  position: absolute; top: 24px; right: 32px; background: none; border: none;
  color: #fff; font-size: 36px; line-height: 1; cursor: pointer; opacity: .85;
}
.gallery-close:hover { opacity: 1; }
.gallery-arrow {
  position: absolute; top: 50%; transform: translateY(-50%);
  background: rgba(255,255,255,.12); border: none; color: #fff;
  font-size: 40px; width: 60px; height: 60px; border-radius: 50%;
  cursor: pointer; line-height: 1;
}
.gallery-arrow:hover { background: rgba(255,255,255,.25); }
.gallery-arrow.left { left: 24px; }
.gallery-arrow.right { right: 24px; }
.gallery-content { display: flex; flex-direction: column; align-items: center; max-width: 82vw; max-height: 86vh; }
.gallery-media { max-width: 82vw; max-height: 80vh; object-fit: contain; border-radius: 8px; }
.gallery-counter { color: #ccc; margin-top: 14px; font-size: 14px; }
</style>
