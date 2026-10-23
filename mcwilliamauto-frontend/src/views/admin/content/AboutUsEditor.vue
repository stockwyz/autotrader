<template>
  <div class="settings-page">
    <div class="panel">
      <h2 class="panel-title">About Us</h2>
      <p class="panel-subtitle">Shown on the public "/about-us" page</p>
      <el-form :model="form" label-position="top" v-loading="loading">
        <el-form-item label="Page Title">
          <el-input v-model="form.title" placeholder="About Us" />
        </el-form-item>
        <el-form-item label="Content">
          <RichTextEditor v-model="form.content" upload-url="/admin/upload/page-content-image" />
        </el-form-item>

        <el-collapse class="seo-collapse">
          <el-collapse-item title="SEO Settings (optional)" name="seo">
            <el-form-item label="SEO Title"><el-input v-model="form.seoTitle" /></el-form-item>
            <el-form-item label="SEO Keywords"><el-input v-model="form.seoKeywords" /></el-form-item>
            <el-form-item label="SEO Description"><el-input v-model="form.seoDescription" type="textarea" rows="3" /></el-form-item>
          </el-collapse-item>
        </el-collapse>

        <el-button type="primary" :loading="saving" @click="onSave" style="margin-top:16px">Save</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { contentApi } from '@/api/admin'
import RichTextEditor from '@/components/admin/RichTextEditor.vue'

const PAGE_CODE = 'about-us'
const loading = ref(false)
const saving = ref(false)
const form = reactive({ title: 'About Us', content: '', seoTitle: '', seoKeywords: '', seoDescription: '' })

async function load() {
  loading.value = true
  try {
    const page = await contentApi.pages.getByCode(PAGE_CODE)
    if (page) Object.assign(form, page)
  } finally {
    loading.value = false
  }
}

async function onSave() {
  saving.value = true
  try {
    await contentApi.pages.saveByCode(PAGE_CODE, form)
    ElMessage.success('About Us page saved')
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<style scoped lang="scss">
.settings-page { display: flex; flex-direction: column; gap: 24px; }
.panel { background: #fff; border-radius: 20px; padding: 28px 32px; box-shadow: 0 1px 3px rgba(23,34,59,.06); }
.panel-title { font-family: 'Fraunces', serif; font-size: 22px; font-weight: 600; margin: 0; }
.panel-subtitle { color: #7A7364; font-size: 14px; margin: 4px 0 20px; }
.seo-collapse { margin-top: 8px; }
</style>
