<template>
  <div class="sidebar">
    <div class="logo">
      <div class="mark">{{ dealerInitial }}</div>
      <span>{{ dealerInfo?.name || 'McWilliam Auto' }} <small>Admin</small></span>
    </div>
    <el-scrollbar class="menu-scroll">
      <el-menu router :default-active="$route.path" :default-openeds="['inventory','leads','content','dict','settings']"
               background-color="#17223B" text-color="#B9C1D6" active-text-color="#fff">
        <el-menu-item index="/admin/vehicles">
          <span>Inventory</span>
        </el-menu-item>
        <el-menu-item index="/admin/leads">
          <span>Leads</span>
        </el-menu-item>
        <el-menu-item index="/admin/appointments">
          <span>Appointments</span>
        </el-menu-item>

        <el-sub-menu index="dict">
          <template #title><span>Vehicle Data</span></template>
          <el-menu-item index="/admin/dict/makes">Makes</el-menu-item>
          <el-menu-item index="/admin/dict/models">Models</el-menu-item>
          <el-menu-item index="/admin/dict/body-styles">Body Styles</el-menu-item>
          <el-menu-item index="/admin/dict/transmissions">Transmissions</el-menu-item>
          <el-menu-item index="/admin/dict/engines">Engines</el-menu-item>
          <el-menu-item index="/admin/dict/fuel-types">Fuel Types</el-menu-item>
          <el-menu-item index="/admin/dict/colors">Colours</el-menu-item>
          <el-menu-item index="/admin/dict/pro-labels">Pro-Label</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="content">
          <template #title><span>Content</span></template>
          <el-menu-item index="/admin/content/banners">Homepage Banners</el-menu-item>
          <el-menu-item index="/admin/content/about-us">About Us</el-menu-item>
          <el-menu-item index="/admin/content/testimonials">Testimonials</el-menu-item>
          <el-menu-item index="/admin/content/careers">Careers</el-menu-item>
          <el-menu-item index="/admin/content/pages">Static Pages</el-menu-item>
          <el-menu-item index="/admin/content/nav-menu">Nav Menu</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="settings">
          <template #title><span>Settings</span></template>
          <el-menu-item index="/admin/settings/dealer">Dealer Info & Hours</el-menu-item>
          <el-menu-item index="/admin/settings/finance">Finance Defaults</el-menu-item>
          <el-menu-item index="/admin/settings/users">Admin Users</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/admin/website-style">
          <span>Website Style</span>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
    <el-button @click="onLogout" class="logout-btn" text>Log Out</el-button>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/store/useUserStore'
import { useDealerStore } from '@/store/useDealerStore'

const router = useRouter()
const userStore = useUserStore()
const dealerStore = useDealerStore()
const { info: dealerInfo } = storeToRefs(dealerStore)
onMounted(() => dealerStore.ensureLoaded())

const dealerInitial = computed(() => (dealerInfo.value?.name || 'McWilliam Auto').trim().charAt(0).toUpperCase())

function onLogout() {
  userStore.logout()
  router.push('/admin/login')
}
</script>

<style scoped lang="scss">
.sidebar {
  width: 232px; position: fixed; left: 0; top: 0; bottom: 0;
  background: #17223B; color: #fff;
  display: flex; flex-direction: column;
}
.logo { display: flex; align-items: center; gap: 10px; padding: 22px 18px; border-bottom: 1px solid rgba(255,255,255,.1); }
.logo .mark { width: 32px; height: 32px; border-radius: 9px; background: #E8973A; display: flex; align-items: center; justify-content: center; color: #17223B; font-family: 'Fraunces'; font-weight: 700; font-size: 14px; flex-shrink: 0; }
.logo span { font-family: 'Fraunces'; font-weight: 600; font-size: 15px; line-height: 1.3; }
.logo small { display: block; font-family: 'Manrope'; font-size: 11px; color: #9BA5C4; font-weight: 600; letter-spacing: .06em; }
.menu-scroll { flex: 1; }
.logout-btn { margin: 16px; color: #B9C1D6 !important; }
:deep(.el-menu) { border-right: none; }
</style>
