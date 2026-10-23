<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent="onSubmit">
    <el-form-item label="First Name" prop="firstName"><el-input v-model="form.firstName" /></el-form-item>
    <el-form-item label="Last Name"><el-input v-model="form.lastName" /></el-form-item>
    <el-form-item label="Email" prop="email">
      <el-input v-model="form.email" />
    </el-form-item>
    <el-form-item label="Phone" prop="phone">
      <el-input v-model="form.phone" placeholder="e.g. 416-555-1234 or 138-1234-5678" />
    </el-form-item>
    <el-form-item label="Message" prop="message">
      <el-input v-model="form.message" type="textarea" rows="4" />
    </el-form-item>
    <el-button type="primary" native-type="submit" :loading="loading">Send</el-button>
  </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { submitContact } from '@/api/lead'
import { isValidPhone } from '@/utils/phoneValidator'
import { isValidEmail } from '@/utils/emailValidator'

const props = defineProps({ type: { type: String, default: 'CONTACT' }, initialMessage: { type: String, default: '' } })
const loading = ref(false)
const formRef = ref(null)
const form = reactive({ firstName: '', lastName: '', email: '', phone: '', message: props.initialMessage })

/** Phone 选填，一旦填写才需要校验格式；不像 Email 那样强制必填 */
function validatePhoneOptional(rule, value, callback) {
  if (!value) {
    callback()
  } else if (!isValidPhone(value)) {
    callback(new Error('Please enter a valid North American or China phone number'))
  } else {
    callback()
  }
}

const rules = {
  firstName: [{ required: true, message: 'First Name is required', trigger: 'blur' }],
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value && !isValidEmail(value)) callback(new Error('Please enter a valid email address'))
        else callback()
      }, trigger: 'blur' }
  ],
  phone: [{ validator: validatePhoneOptional, trigger: 'blur' }],
  message: [{ required: true, message: 'Message is required', trigger: 'blur' }]
}

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await submitContact({ ...form, type: props.type })
    ElMessage.success('Message sent!')
    Object.keys(form).forEach(k => (form[k] = ''))
    formRef.value.clearValidate()
  } finally {
    loading.value = false
  }
}
</script>
