<template>
  <div class="crud-page">
    <div class="crud-header">
      <div>
        <h2 class="crud-title">{{ title }}</h2>
        <p v-if="subtitle" class="crud-subtitle">{{ subtitle }}</p>
      </div>
      <el-button type="primary" @click="openCreate">+ New {{ entityName }}</el-button>
    </div>

    <div v-if="$slots.filters" class="crud-filters">
      <slot name="filters" :refresh="refresh" />
    </div>

    <el-table :data="list" v-loading="loading" class="crud-table">
      <el-table-column v-for="col in columns" :key="col.prop" :prop="col.prop" :label="col.label" :width="col.width">
        <template #default="{ row }">
          <img v-if="col.type === 'image'" :src="row[col.prop]" class="crud-thumb" @error="e => e.target.style.visibility='hidden'" />
          <img
            v-else-if="col.type === 'icon'"
            :src="col.resolveIcon(row[col.prop])"
            class="crud-icon-img"
            @error="e => e.target.src = col.resolveIcon(col.defaultCode)"
          />
          <el-switch
            v-else-if="col.type === 'switch'"
            :model-value="row[col.prop] === 1 || row[col.prop] === true"
            :loading="switchLoadingId === row.id"
            @change="v => onQuickToggle(row, col.prop, v)"
          />
          <span v-else>{{ col.formatter ? col.formatter(row[col.prop], row) : row[col.prop] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" width="180" fixed="right">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" @click="openEdit(row)">Edit</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">Delete</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="paged && total > pageSize"
      layout="prev, pager, next"
      :total="total" :page-size="pageSize" :current-page="pageNum"
      @current-change="p => { pageNum = p; refresh() }"
      class="crud-pagination"
    />

    <el-dialog v-model="dialogVisible" :title="form.id ? `Edit ${entityName}` : `New ${entityName}`" :width="dialogWidth">
      <el-form :model="form" label-position="top">
        <el-form-item v-for="f in formFields" :key="f.prop" :label="f.required ? f.label + ' *' : f.label">
          <el-input v-if="f.type === 'text' || !f.type" v-model="form[f.prop]" />
          <el-input v-else-if="f.type === 'textarea'" v-model="form[f.prop]" type="textarea" :rows="4" />
          <RichTextEditor v-else-if="f.type === 'richtext'" v-model="form[f.prop]" :upload-url="f.uploadUrl" />
          <el-input-number v-else-if="f.type === 'number'" v-model="form[f.prop]" style="width:100%" :controls="false" />
          <el-switch v-else-if="f.type === 'switch'" v-model="form[f.prop]" :active-value="1" :inactive-value="0" />
          <el-select v-else-if="f.type === 'select'" v-model="form[f.prop]" style="width:100%" :filterable="!!f.filterable">
            <el-option v-for="opt in f.options" :key="opt.value" :label="opt.label" :value="opt.value">
              <img v-if="opt.icon" :src="opt.icon" class="option-icon" />
              {{ opt.label }}
            </el-option>
          </el-select>
          <div v-else-if="f.type === 'icon-picker'" class="icon-picker">
            <div
              v-for="opt in f.iconOptions"
              :key="opt.value"
              class="icon-picker-item"
              :class="{ active: form[f.prop] === opt.value }"
              :title="opt.label"
              @click="form[f.prop] = opt.value"
            >
              <img :src="f.resolveIcon(opt.value)" class="icon-picker-img" />
            </div>
          </div>
          <div v-else-if="f.type === 'image'" class="image-upload-field">
            <div v-if="!imagePreview[f.prop]" class="image-dropzone" @click="triggerFileInput(f.prop)">
              <span class="plus-icon">+</span>
            </div>
            <div v-else class="image-preview-wrap">
              <img :src="imagePreview[f.prop]" class="image-preview" @click="triggerFileInput(f.prop)" />
              <button type="button" class="image-remove-btn" @click="onRemoveImage(f.prop)">×</button>
            </div>
            <input
              type="file"
              :ref="el => { if (el) fileInputRefs[f.prop] = el }"
              accept="image/*"
              style="display:none"
              @change="e => onFileSelected(e, f)"
            />
            <p class="image-hint">Click to {{ imagePreview[f.prop] ? 'replace' : 'upload' }} an icon</p>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api/request'
import RichTextEditor from './RichTextEditor.vue'

const router = useRouter()

const props = defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, default: '' },
  entityName: { type: String, default: 'Item' },
  api: { type: Object, required: true }, // { list, create, update, delete }
  columns: { type: Array, required: true }, // [{prop,label,width,type,formatter}]
  formFields: { type: Array, required: true }, // [{prop,label,type,options,uploadUrl}]
  paged: { type: Boolean, default: true },
  extraParams: { type: Object, default: () => ({}) },
  dialogWidth: { type: String, default: '560px' },
  /** 可选：给某一行判断是否应该跳转到专属的整页编辑器而不是弹窗，返回路由路径就跳转，返回空/false就还是走弹窗 */
  editRouteResolver: { type: Function, default: null },
  /** 可选：设了这个"+ New X"按钮就直接跳转到这个路由，不走弹窗 */
  newRoute: { type: String, default: null }
})

const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const switchLoadingId = ref(null)
const form = reactive({})

// 图片上传字段专用状态
const imagePreview = reactive({})   // prop -> 预览图URL（已有图或本地选中图的blob URL）
const pendingFiles = reactive({})   // prop -> 待上传的 File 对象（未保存前先暂存本地）
const removedImages = reactive({})  // prop -> true 表示用户点了删除，保存时要清空该字段
const fileInputRefs = {}            // prop -> 对应的 <input type=file> DOM 引用

function triggerFileInput(prop) {
  fileInputRefs[prop]?.click()
}

function onFileSelected(e, field) {
  const file = e.target.files[0]
  if (!file) return
  pendingFiles[field.prop] = file
  removedImages[field.prop] = false
  imagePreview[field.prop] = URL.createObjectURL(file)
  e.target.value = '' // 允许重复选择同一文件也能触发change
}

function onRemoveImage(prop) {
  // 这一步纯本地操作，不调用任何接口——此时后台数据库和文件都还没变，
  // 只是清空编辑框里的预览，让用户可以重新选图；真正的文件删除和数据库更新
  // 统一放到点Save的时候一起处理
  imagePreview[prop] = null
  pendingFiles[prop] = null
  removedImages[prop] = true
  form[prop] = null
}

async function onQuickToggle(row, prop, value) {
  switchLoadingId.value = row.id
  try {
    await props.api.update(row.id, { ...row, [prop]: value ? 1 : 0 })
    row[prop] = value ? 1 : 0
    ElMessage.success('Updated')
  } catch (e) {
    ElMessage.error('Update failed')
  } finally {
    switchLoadingId.value = null
  }
}

function resetForm() {
  Object.keys(form).forEach(k => delete form[k])
  form.id = null
  props.formFields.forEach(f => {
    form[f.prop] = f.type === 'switch' ? 1 : (f.type === 'number' ? null : '')
    if (f.type === 'image') {
      imagePreview[f.prop] = null
      pendingFiles[f.prop] = null
      removedImages[f.prop] = false
    }
  })
}

async function refresh() {
  loading.value = true
  try {
    const params = props.paged
      ? { pageNum: pageNum.value, pageSize: pageSize.value, ...props.extraParams }
      : { ...props.extraParams }
    const res = await props.api.list(params)
    if (Array.isArray(res)) {
      list.value = res
      total.value = res.length
    } else {
      list.value = res.records || []
      total.value = res.total || 0
    }
  } finally {
    loading.value = false
  }
}

function openCreate() {
  if (props.newRoute) {
    router.push(props.newRoute)
    return
  }
  resetForm()
  dialogVisible.value = true
}

function openEdit(row) {
  if (props.editRouteResolver) {
    const path = props.editRouteResolver(row)
    if (path) {
      router.push(path)
      return
    }
  }
  resetForm()
  Object.assign(form, row)
  props.formFields.forEach(f => {
    if (f.type === 'image' && row[f.prop]) {
      imagePreview[f.prop] = row[f.prop]
    }
  })
  dialogVisible.value = true
}

function validateForm() {
  for (const f of props.formFields) {
    if (!f.required) continue
    if (f.type === 'image') {
      if (!pendingFiles[f.prop] && !imagePreview[f.prop]) {
        ElMessage.error(`${f.label} is required`)
        return false
      }
    } else {
      const v = form[f.prop]
      if (v === null || v === undefined || v === '') {
        ElMessage.error(`${f.label} is required`)
        return false
      }
    }
  }
  return true
}

async function onSave() {
  if (!validateForm()) return
  saving.value = true
  try {
    // 图片字段：有待上传文件的先上传拿到路径，标记删除的清空字段
    for (const f of props.formFields) {
      if (f.type !== 'image') continue
      if (pendingFiles[f.prop]) {
        const formData = new FormData()
        formData.append('file', pendingFiles[f.prop])
        const res = await request.post(f.uploadUrl, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })
        form[f.prop] = res.url
      } else if (removedImages[f.prop]) {
        form[f.prop] = null
      }
    }

    if (form.id) {
      await props.api.update(form.id, form)
      ElMessage.success('Updated')
    } else {
      await props.api.create(form)
      ElMessage.success('Created')
    }
    dialogVisible.value = false
    refresh()
  } finally {
    saving.value = false
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm(`Delete this ${props.entityName.toLowerCase()}? This cannot be undone.`, 'Confirm', { type: 'warning' })
  await props.api.delete(row.id)
  ElMessage.success('Deleted')
  refresh()
}

defineExpose({ refresh })
onMounted(refresh)
</script>

<style scoped lang="scss">
.crud-page { background: #fff; border-radius: 20px; padding: 28px 32px; box-shadow: 0 1px 3px rgba(23,34,59,.06); }
.crud-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 22px; }
.crud-title { font-family: 'Fraunces', serif; font-size: 24px; font-weight: 600; margin: 0; }
.crud-subtitle { color: #7A7364; font-size: 14px; margin-top: 4px; }
.crud-filters { margin-bottom: 18px; }
.crud-table { width: 100%; }
.crud-pagination { margin-top: 20px; justify-content: center; }
.crud-thumb { width: 56px; height: 40px; object-fit: contain; display: block; }

.image-upload-field { display: flex; flex-direction: column; align-items: flex-start; }
.image-dropzone {
  width: 88px; height: 88px; border: 2px dashed #D8D3C4; border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; background: #FBF8F2; transition: .15s;
}
.image-dropzone:hover { border-color: #E0684A; background: #FBEFE8; }
.plus-icon { font-size: 32px; color: #B5AE9B; font-weight: 300; line-height: 1; }
.image-preview-wrap { position: relative; width: 88px; height: 88px; }
.image-preview {
  width: 100%; height: 100%; object-fit: contain; border: 1px solid #EAE3D4;
  border-radius: 14px; background: #fff; cursor: pointer;
}
.image-remove-btn {
  position: absolute; top: -8px; right: -8px; width: 24px; height: 24px;
  border-radius: 50%; background: #C1502E; color: #fff; border: 2px solid #fff;
  font-size: 14px; line-height: 1; cursor: pointer; display: flex; align-items: center; justify-content: center;
  padding: 0;
}
.image-remove-btn:hover { background: #8C3822; }
.image-hint { font-size: 12px; color: #7A7364; margin-top: 8px; }
.option-icon { width: 20px; height: 20px; object-fit: contain; vertical-align: -5px; margin-right: 8px; }
.crud-icon-img { width: 26px; height: 26px; }
.icon-picker { display: flex; flex-wrap: wrap; gap: 10px; max-width: 420px; }
.icon-picker-item {
  width: 44px; height: 44px; border-radius: 12px; border: 1.5px solid #EAE3D4;
  display: flex; align-items: center; justify-content: center; cursor: pointer;
  transition: .15s;
}
.icon-picker-img { width: 26px; height: 26px; }
.icon-picker-item:hover { border-color: #E8973A; }
.icon-picker-item.active { border-color: #E0684A; background: #FBEFE8; }
</style>
