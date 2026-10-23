<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>McWilliam Auto Admin</h2>
      <el-form :model="form" label-position="top" @submit.prevent="onSubmit">
        <el-form-item label="Username">
          <el-input v-model="form.username" placeholder="Enter your username" />
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="form.password" type="password" show-password placeholder="Enter your password" />
        </el-form-item>
        <el-button type="primary" native-type="submit" :loading="loading" style="width:100%">Log In</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'
import { useUserStore } from '@/store/useUserStore'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

async function onSubmit() {
  loading.value = true
  try {
    const res = await login(form)
    userStore.setLogin(res)
    ElMessage.success('Logged in successfully')
    router.push('/admin/vehicles')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page { display: flex; align-items: center; justify-content: center; height: 80vh; }
.login-card { width: 360px; padding: 20px; }
</style>
