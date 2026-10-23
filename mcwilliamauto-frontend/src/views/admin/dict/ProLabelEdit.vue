<template>
  <div class="settings-page">
    <div class="panel">
      <div class="panel-head">
        <div>
          <h2 class="panel-title">{{ isEdit ? 'Edit Label' : 'New Label' }}</h2>
          <p class="panel-subtitle">Drag the label to move it. Drag the square handles to resize, drag the round handle above it to rotate.</p>
        </div>
        <el-button @click="router.push('/admin/dict/pro-labels')">Back to List</el-button>
      </div>

      <div class="editor-layout" v-loading="loading">
        <!-- 左侧：表单字段 -->
        <div class="form-col">
          <el-form label-position="top">
            <el-form-item label="Name" required>
              <el-input v-model="form.name" placeholder="e.g. Featured, Manager's Special" />
            </el-form-item>

            <el-form-item label="Image (single file)" required>
              <div v-if="previewUrl" class="image-preview-row">
                <img :src="previewUrl" class="image-thumb" />
                <el-button size="small" @click="triggerFilePick">Replace Image</el-button>
              </div>
              <el-button v-else @click="triggerFilePick">Choose Image</el-button>
              <input ref="fileInputRef" type="file" accept="image/*" style="display:none" @change="onFileSelected" />
            </el-form-item>

            <el-form-item>
              <template #label>Width (%) <span class="hint">10–400</span></template>
              <el-input-number v-model="form.widthPercent" :min="10" :max="400" style="width:100%" />
            </el-form-item>

            <el-form-item>
              <template #label>Height (%) <span class="hint">10–400</span></template>
              <el-input-number v-model="form.heightPercent" :min="10" :max="400" style="width:100%" />
            </el-form-item>

            <el-form-item>
              <template #label>Angle (°) <span class="hint">0–360, starts at 0</span></template>
              <el-input-number v-model="form.angle" :min="0" :max="360" style="width:100%" />
            </el-form-item>

            <el-form-item>
              <template #label>Position X <span class="hint">0–100 (%), 0=left edge</span></template>
              <el-input-number v-model="form.posX" :min="0" :max="100" :precision="1" :step="0.5" style="width:100%" />
            </el-form-item>

            <el-form-item>
              <template #label>Position Y <span class="hint">0–100 (%), 0=top edge</span></template>
              <el-input-number v-model="form.posY" :min="0" :max="100" :precision="1" :step="0.5" style="width:100%" />
            </el-form-item>

            <el-form-item label="Sort Order">
              <el-input-number v-model="form.sortNo" :min="0" style="width:100%" />
            </el-form-item>

            <el-form-item label="Enabled">
              <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
            </el-form-item>

            <el-button type="primary" :loading="saving" @click="onSave" style="width:100%">Save Label</el-button>
          </el-form>
        </div>

        <!-- 右侧：预览画布 -->
        <div class="preview-col">
          <p class="preview-caption">
            Preview (16:10, matches the grid card &amp; homepage image ratio). Other enabled labels are shown faded for spacing reference.
          </p>
          <div ref="canvasRef" class="preview-canvas">
            <svg viewBox="0 0 200 100" class="car-illustration">
              <path d="M20 65 L36 41 Q46 31 66 31 L118 31 Q136 31 148 46 L175 65 L175 78 L20 78 Z" fill="#17223B" opacity="0.85"/>
              <circle cx="55" cy="80" r="12" fill="#17223B"/>
              <circle cx="145" cy="80" r="12" fill="#17223B"/>
              <rect x="61" y="37" width="53" height="20" rx="4" fill="rgba(255,255,255,.55)"/>
            </svg>

            <!-- 其它已有标签，仅作为间距参考，半透明、不可拖拽 -->
            <div v-for="lbl in otherLabels" :key="lbl.id" class="label-wrap ghost" :style="wrapStyle(lbl)" :title="lbl.name">
              <img :src="lbl.imageUrl" class="label-img" />
            </div>

            <!-- 当前正在编辑的标签 -->
            <div v-if="previewUrl" class="label-wrap current" :class="{ dragging: isMoveDragging }" :style="currentWrapStyle">
              <img ref="labelImgRef" :src="previewUrl" class="label-img" @mousedown="onMoveStart" @load="onImageLoad" />
            </div>

            <!-- 控制点独立于图片的缩放/旋转容器之外，用JS直接算出画布上的真实坐标，
                 保证不管图片缩放/旋转到什么程度，控制点永远精确贴在图片实际的边框和角上 -->
            <template v-if="previewUrl">
              <div
                v-for="h in handlePositions" :key="h.key"
                class="resize-handle"
                :style="{ left: h.xPct + '%', top: h.yPct + '%', cursor: h.cursor }"
                @mousedown.stop="onResizeStart(h, $event)"
              ></div>
              <div class="rotate-stem" :style="rotateStemStyle"></div>
              <div class="rotate-handle" :style="{ left: rotateHandlePos.xPct + '%', top: rotateHandlePos.yPct + '%' }" @mousedown.stop="onRotateStart" title="Drag to rotate"></div>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { dictApi } from '@/api/admin'
import request from '@/api/request'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)
const loading = ref(false)
const saving = ref(false)

const form = reactive({
  name: '', imageUrl: '', widthPercent: 100, heightPercent: 100, angle: 0, posX: 50, posY: 50, sortNo: 0, status: 1
})

const fileInputRef = ref(null)
const pendingFile = ref(null)
const previewUrl = ref('')
const otherLabels = ref([])
const naturalRatio = ref(1) // naturalHeight / naturalWidth
const labelImgRef = ref(null)

onMounted(async () => {
  loading.value = true
  try {
    const allLabels = await dictApi.proLabels.list({ pageSize: 500 }).then(r => r.records || r.list || [])
    let others
    if (isEdit.value) {
      const detail = await dictApi.proLabels.detail(route.params.id)
      Object.assign(form, detail)
      previewUrl.value = detail.imageUrl || ''
      others = allLabels.filter(l => l.id !== Number(route.params.id) && l.status === 1)
    } else {
      others = allLabels.filter(l => l.status === 1)
    }
    await Promise.all(others.map(async (lbl) => {
      lbl.__ratio = await loadImageRatio(lbl.imageUrl)
    }))
    otherLabels.value = others
  } finally {
    loading.value = false
  }
  window.addEventListener('resize', refreshCanvasSize)
  await nextTick()
  refreshCanvasSize()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', refreshCanvasSize)
  window.removeEventListener('mousemove', onMoveMove)
  window.removeEventListener('mouseup', onMoveEnd)
  window.removeEventListener('mousemove', onResizeMove)
  window.removeEventListener('mouseup', onResizeEnd)
  window.removeEventListener('mousemove', onRotateMove)
  window.removeEventListener('mouseup', onRotateEnd)
})

function loadImageRatio(url) {
  return new Promise((resolve) => {
    if (!url) return resolve(1)
    const img = new Image()
    img.onload = () => resolve(img.naturalWidth ? img.naturalHeight / img.naturalWidth : 1)
    img.onerror = () => resolve(1)
    img.src = url
  })
}

function triggerFilePick() {
  fileInputRef.value?.click()
}

function onFileSelected(e) {
  const file = e.target.files[0]
  e.target.value = ''
  if (!file) return
  pendingFile.value = file
  previewUrl.value = URL.createObjectURL(file)
}

function onImageLoad(e) {
  const { naturalWidth, naturalHeight } = e.target
  if (naturalWidth && naturalHeight) naturalRatio.value = naturalHeight / naturalWidth
}

const BASE_WIDTH_PCT = 26

function wrapStyle(lbl) {
  const ratio = lbl.__ratio || 1
  return {
    left: `${lbl.posX}%`,
    top: `${lbl.posY}%`,
    width: `${BASE_WIDTH_PCT}%`,
    aspectRatio: `1 / ${ratio}`,
    transform: `translate(-50%, -50%) rotate(${lbl.angle || 0}deg) scale(${(lbl.widthPercent || 100) / 100}, ${(lbl.heightPercent || 100) / 100})`
  }
}

const currentWrapStyle = computed(() => ({
  left: `${form.posX}%`,
  top: `${form.posY}%`,
  width: `${BASE_WIDTH_PCT}%`,
  aspectRatio: `1 / ${naturalRatio.value}`,
  transform: `translate(-50%, -50%) rotate(${form.angle}deg) scale(${form.widthPercent / 100}, ${form.heightPercent / 100})`
}))

// ---------- 画布尺寸(像素)，控制点坐标换算要用 ----------
const canvasRef = ref(null)
const canvasSize = reactive({ width: 0, height: 0 })
function refreshCanvasSize() {
  if (!canvasRef.value) return
  const rect = canvasRef.value.getBoundingClientRect()
  canvasSize.width = rect.width
  canvasSize.height = rect.height
}

const STEM_LENGTH_PX = 22 // 旋转手柄连接线长度，固定像素，不受标签缩放影响

/** 核心：根据当前中心点(posX%,posY%)、宽高(基准宽度26% × naturalRatio × widthPercent/heightPercent)、
 *  旋转角度，用三角函数算出某个"本地(未旋转)坐标偏移"点，旋转后落在画布上的百分比坐标。
 *  fx/fy: -1/0/1 表示在未旋转状态下相对中心的左右/上下方向；extraPx: 额外再往外(仅Y方向，给旋转手柄的连接线长度)加的固定像素 */
function computeHandlePoint(fx, fy, extraPx = 0) {
  if (!canvasSize.width) return { xPct: 50, yPct: 50 }
  const baseWidthPx = canvasSize.width * (BASE_WIDTH_PCT / 100)
  const baseHeightPx = baseWidthPx * naturalRatio.value
  const halfWidthPx = (baseWidthPx * (form.widthPercent / 100)) / 2
  const halfHeightPx = (baseHeightPx * (form.heightPercent / 100)) / 2

  const localX = fx * halfWidthPx
  const localY = fy * halfHeightPx - extraPx

  const rad = (form.angle * Math.PI) / 180
  const rotatedX = localX * Math.cos(rad) - localY * Math.sin(rad)
  const rotatedY = localX * Math.sin(rad) + localY * Math.cos(rad)

  const centerXpx = (form.posX / 100) * canvasSize.width
  const centerYpx = (form.posY / 100) * canvasSize.height

  return {
    xPct: ((centerXpx + rotatedX) / canvasSize.width) * 100,
    yPct: ((centerYpx + rotatedY) / canvasSize.height) * 100
  }
}

const HANDLE_DEFS = [
  { key: 'nw', fx: -1, fy: -1, signX: -1, signY: -1, cursor: 'nwse-resize' },
  { key: 'n', fx: 0, fy: -1, signX: 0, signY: -1, cursor: 'ns-resize' },
  { key: 'ne', fx: 1, fy: -1, signX: 1, signY: -1, cursor: 'nesw-resize' },
  { key: 'w', fx: -1, fy: 0, signX: -1, signY: 0, cursor: 'ew-resize' },
  { key: 'e', fx: 1, fy: 0, signX: 1, signY: 0, cursor: 'ew-resize' },
  { key: 'sw', fx: -1, fy: 1, signX: -1, signY: 1, cursor: 'nesw-resize' },
  { key: 's', fx: 0, fy: 1, signX: 0, signY: 1, cursor: 'ns-resize' },
  { key: 'se', fx: 1, fy: 1, signX: 1, signY: 1, cursor: 'nwse-resize' }
]

const handlePositions = computed(() =>
  HANDLE_DEFS.map((h) => ({ ...h, ...computeHandlePoint(h.fx, h.fy) }))
)

const rotateHandlePos = computed(() => computeHandlePoint(0, -1, STEM_LENGTH_PX))

const rotateStemStyle = computed(() => {
  const top = computeHandlePoint(0, -1)
  const tip = rotateHandlePos.value
  const topPx = { x: (top.xPct / 100) * canvasSize.width, y: (top.yPct / 100) * canvasSize.height }
  const tipPx = { x: (tip.xPct / 100) * canvasSize.width, y: (tip.yPct / 100) * canvasSize.height }
  const dx = tipPx.x - topPx.x
  const dy = tipPx.y - topPx.y
  const len = Math.sqrt(dx * dx + dy * dy)
  const angleDeg = (Math.atan2(dy, dx) * 180) / Math.PI
  return {
    left: `${top.xPct}%`,
    top: `${top.yPct}%`,
    width: `${len}px`,
    transform: `rotate(${angleDeg}deg)`
  }
})

// ---------- 拖拽移动位置 ----------
const isMoveDragging = ref(false)
function onMoveStart(e) {
  e.preventDefault()
  isMoveDragging.value = true
  window.addEventListener('mousemove', onMoveMove)
  window.addEventListener('mouseup', onMoveEnd)
}
function onMoveMove(e) {
  if (!canvasRef.value) return
  const rect = canvasRef.value.getBoundingClientRect()
  const pctX = ((e.clientX - rect.left) / rect.width) * 100
  const pctY = ((e.clientY - rect.top) / rect.height) * 100
  form.posX = Math.max(0, Math.min(100, Math.round(pctX * 10) / 10))
  form.posY = Math.max(0, Math.min(100, Math.round(pctY * 10) / 10))
}
function onMoveEnd() {
  isMoveDragging.value = false
  window.removeEventListener('mousemove', onMoveMove)
  window.removeEventListener('mouseup', onMoveEnd)
}

// ---------- 拖拽控制点调整宽/高 ----------
let resizeHandle = null
let resizeStartX = 0
let resizeStartY = 0
let resizeStartWidth = 100
let resizeStartHeight = 100
let resizeBaseWidthPx = 0
let resizeBaseHeightPx = 0

function onResizeStart(handle, e) {
  e.preventDefault()
  if (!canvasRef.value) return
  resizeHandle = handle
  resizeStartX = e.clientX
  resizeStartY = e.clientY
  resizeStartWidth = form.widthPercent
  resizeStartHeight = form.heightPercent
  resizeBaseWidthPx = canvasSize.width * (BASE_WIDTH_PCT / 100)
  resizeBaseHeightPx = resizeBaseWidthPx * naturalRatio.value
  window.addEventListener('mousemove', onResizeMove)
  window.addEventListener('mouseup', onResizeEnd)
}
function onResizeMove(e) {
  if (!resizeHandle) return
  const dx = e.clientX - resizeStartX
  const dy = e.clientY - resizeStartY
  const rad = (-form.angle * Math.PI) / 180
  const localDx = dx * Math.cos(rad) - dy * Math.sin(rad)
  const localDy = dx * Math.sin(rad) + dy * Math.cos(rad)

  if (resizeHandle.signX !== 0) {
    const deltaPct = resizeHandle.signX * ((localDx * 2) / resizeBaseWidthPx) * 100
    form.widthPercent = Math.max(10, Math.min(400, Math.round(resizeStartWidth + deltaPct)))
  }
  if (resizeHandle.signY !== 0) {
    const deltaPct = resizeHandle.signY * ((localDy * 2) / resizeBaseHeightPx) * 100
    form.heightPercent = Math.max(10, Math.min(400, Math.round(resizeStartHeight + deltaPct)))
  }
}
function onResizeEnd() {
  resizeHandle = null
  window.removeEventListener('mousemove', onResizeMove)
  window.removeEventListener('mouseup', onResizeEnd)
}

// ---------- 拖拽圆形手柄旋转 ----------
function onRotateStart(e) {
  e.preventDefault()
  window.addEventListener('mousemove', onRotateMove)
  window.addEventListener('mouseup', onRotateEnd)
}
function onRotateMove(e) {
  if (!canvasRef.value) return
  const rect = canvasRef.value.getBoundingClientRect()
  const centerX = rect.left + (form.posX / 100) * rect.width
  const centerY = rect.top + (form.posY / 100) * rect.height
  const dx = e.clientX - centerX
  const dy = e.clientY - centerY
  let deg = (Math.atan2(dx, -dy) * 180) / Math.PI
  if (deg < 0) deg += 360
  form.angle = Math.round(deg)
}
function onRotateEnd() {
  window.removeEventListener('mousemove', onRotateMove)
  window.removeEventListener('mouseup', onRotateEnd)
}

async function onSave() {
  if (!form.name) {
    ElMessage.error('Name is required')
    return
  }
  if (!pendingFile.value && !form.imageUrl) {
    ElMessage.error('Please choose an image')
    return
  }
  saving.value = true
  try {
    if (pendingFile.value) {
      const formData = new FormData()
      formData.append('file', pendingFile.value)
      const res = await request.post('/admin/upload/label-image', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      form.imageUrl = res.url
    }
    if (isEdit.value) {
      await dictApi.proLabels.update(route.params.id, form)
    } else {
      await dictApi.proLabels.create(form)
    }
    ElMessage.success('Label saved')
    router.push('/admin/dict/pro-labels')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped lang="scss">
.settings-page { display: flex; flex-direction: column; gap: 24px; }
.panel { background: #fff; border-radius: 20px; padding: 28px 32px; box-shadow: 0 1px 3px rgba(23,34,59,.06); }
.panel-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 16px; margin-bottom: 24px; }
.panel-title { font-family: 'Fraunces', serif; font-size: 22px; font-weight: 600; margin: 0; }
.panel-subtitle { color: #7A7364; font-size: 14px; margin: 4px 0 0; }

.editor-layout { display: grid; grid-template-columns: 340px 1fr; gap: 32px; align-items: start; }
.hint { font-size: 12px; color: #909399; font-weight: 400; margin-left: 6px; }

.image-preview-row { display: flex; align-items: center; gap: 12px; }
.image-thumb { width: 64px; height: 64px; object-fit: contain; border: 1px solid #EAE3D4; border-radius: 8px; background: #FBF8F2; }

.preview-caption { font-size: 13px; color: #7A7364; margin: 0 0 12px; }
.preview-canvas {
  position: relative; width: 100%; aspect-ratio: 16/10;
  background: linear-gradient(135deg, #DCD3C2, #EDE7DA);
  border-radius: 12px; overflow: visible;
  display: flex; align-items: center; justify-content: center;
  border: 1px solid #EAE3D4;
}
.car-illustration { width: 60%; height: 60%; }

.label-wrap { position: absolute; transform-origin: center center; }
.label-wrap.ghost { opacity: .4; pointer-events: none; }
.label-img { width: 100%; height: 100%; display: block; pointer-events: auto; cursor: grab; filter: drop-shadow(0 3px 8px rgba(0,0,0,.25)); }
.label-wrap.current.dragging .label-img { cursor: grabbing; opacity: .85; }

.resize-handle {
  position: absolute; width: 11px; height: 11px; margin: -5.5px 0 0 -5.5px; border-radius: 3px;
  background: #fff; border: 2px solid #E0684A; box-shadow: 0 1px 3px rgba(0,0,0,.3);
  z-index: 5;
}
.rotate-stem {
  position: absolute; height: 2px; background: #E0684A;
  transform-origin: left center; z-index: 4; pointer-events: none;
}
.rotate-handle {
  position: absolute; width: 14px; height: 14px; margin: -7px 0 0 -7px; border-radius: 50%;
  background: #fff; border: 2px solid #E0684A; box-shadow: 0 1px 3px rgba(0,0,0,.3);
  cursor: grab; z-index: 5;
}

@media (max-width: 900px) {
  .editor-layout { grid-template-columns: 1fr; }
}
</style>
