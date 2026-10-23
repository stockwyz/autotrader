<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent="onSubmit">
    <el-form-item label="First Name" required><el-input v-model="form.firstName" /></el-form-item>
    <el-form-item label="Last Name" required><el-input v-model="form.lastName" /></el-form-item>
    <el-form-item label="Email" required><el-input v-model="form.email" type="email" /></el-form-item>
    <el-form-item label="Phone" prop="phone">
      <el-input v-model="form.phone" placeholder="e.g. 416-555-1234 or 138-1234-5678" />
    </el-form-item>
    <el-form-item label="Preferred Time">
      <el-date-picker v-model="form.preferredTime" type="datetime" style="width:100%" />
    </el-form-item>
    <el-form-item label="Message">
      <el-input v-model="form.message" type="textarea" rows="3" />
    </el-form-item>
    <el-button type="primary" native-type="submit" :loading="loading">Book Test Drive</el-button>
  </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { submitTestDrive } from '@/api/lead'
import { phoneRules } from '@/utils/phoneValidator'

const props = defineProps({ vehicleId: { type: [Number, String], default: null } })
const loading = ref(false)
const formRef = ref(null)
const form = reactive({ firstName: '', lastName: '', email: '', phone: '', message: '', preferredTime: null })
const rules = { phone: phoneRules }

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await submitTestDrive({ ...form, vehicleId: props.vehicleId })
    ElMessage.success('Test drive request submitted!')
    formRef.value.clearValidate()
  } finally {
    loading.value = false
  }
}
</script>
