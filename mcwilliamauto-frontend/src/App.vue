<template>
  <template v-if="isAdminRoute">
    <AdminSidebar />
    <div class="admin-content"><router-view /></div>
  </template>
  <template v-else>
    <HeaderNav />
    <router-view />
    <FooterInfo />
  </template>
</template>

<script setup>
import { computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useDealerStore } from '@/store/useDealerStore'
import HeaderNav from '@/components/layout/HeaderNav.vue'
import FooterInfo from '@/components/layout/FooterInfo.vue'
import AdminSidebar from '@/components/layout/AdminSidebar.vue'

const route = useRoute()
const isAdminRoute = computed(() => route.path.startsWith('/admin') && route.path !== '/admin/login')

// 前台网站风格：后台"Website Style"里选的 A–H 存在 dealer_info.theme_code，
// 这里把它写到 <html data-site-theme>，样式变量(style.scss)据此切换；后台页面不带这个属性，所以后台风格不变
const THEMES = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
const dealerStore = useDealerStore()
const { info: dealerInfo } = storeToRefs(dealerStore)
onMounted(() => dealerStore.ensureLoaded())

function cachedTheme() {
  try { const t = localStorage.getItem('siteTheme'); return THEMES.includes(t) ? t : null } catch (e) { return null }
}
function applyTheme() {
  const el = document.documentElement
  if (route.path.startsWith('/admin')) { delete el.dataset.siteTheme; return }
  const fromServer = dealerInfo.value?.themeCode
  const code = THEMES.includes(fromServer) ? fromServer : (cachedTheme() || 'A')
  el.dataset.siteTheme = code
  if (THEMES.includes(fromServer)) {
    try { localStorage.setItem('siteTheme', fromServer) } catch (e) { /* 无痕模式等拿不到storage时忽略 */ }
  }
}
watch([() => route.path, () => dealerInfo.value?.themeCode], applyTheme, { immediate: true })
</script>

<style>
.admin-content { margin-left: 232px; min-height: 100vh; background: #FBF8F2; padding: 32px; }
</style>
