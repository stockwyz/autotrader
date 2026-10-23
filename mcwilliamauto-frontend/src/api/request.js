import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/useUserStore'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.request.use((config) => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.closeAll()
      ElMessage.error(res.message || 'Request failed')
      if (res.code === 401) {
        const userStore = useUserStore()
        userStore.logout()
        router.push('/admin/login')
      }
      return Promise.reject(new Error(res.message || 'Request failed'))
    }
    return res.data
  },
  (error) => {
    ElMessage.closeAll()
    const status = error.response?.status
    if (status === 401 || status === 403) {
      const userStore = useUserStore()
      const wasLoggedIn = !!userStore.token
      userStore.logout()
      if (wasLoggedIn) {
        ElMessage.error('Your session has expired. Please log in again.')
        router.push('/admin/login')
      } else {
        ElMessage.error('Please log in to continue.')
      }
      return Promise.reject(error)
    }
    ElMessage.error(error.message || 'Network error')
    return Promise.reject(error)
  }
)

export default request
