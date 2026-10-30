<template>
  <header class="header">
    <div class="top-bar">
      <div class="container top-bar-inner">
        <span>{{ dealerInfo?.address || '155 McPhillips St, Winnipeg, MB, R3E 2J8' }}</span>
        <span class="right">
          <a :href="`tel:${dealerInfo?.phone || '204-560-1234'}`">Call {{ dealerInfo?.phone || '204-560-1234' }}</a>
          <router-link to="/contact-us" class="text-us-link">Text Us Now</router-link>
        </span>
      </div>
    </div>

    <div class="container nav-inner">
      <router-link to="/" class="logo">
        <div class="mark">{{ dealerInitial }}</div>
        <span class="name">{{ dealerInfo?.name || 'McWilliam Auto' }}</span>
      </router-link>

      <el-menu class="desktop-menu nav-menu" mode="horizontal" :ellipsis="false" router :default-active="$route.path">
        <el-menu-item index="/">Home</el-menu-item>
        <el-sub-menu index="inventory">
          <template #title>Inventory</template>
          <el-menu-item index="/used-cars">Browse Used Inventory</el-menu-item>
          <el-menu-item @click="$router.push({ path: '/used-cars', query: { sold: 1 } })">Sold Vehicles</el-menu-item>
          <el-menu-item @click="$router.push({ path: '/used-cars', query: { featured: 1 } })">Used Vehicle Specials</el-menu-item>
          <el-menu-item index="/carfinder">Car Finder</el-menu-item>
          <el-menu-item index="/trade-in">Trade-In Appraisal</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="financing">
          <template #title>Financing</template>
          <el-menu-item index="/finance">Finance Department</el-menu-item>
          <el-menu-item index="/car-loan-calculator">Car Loan Calculator</el-menu-item>
          <el-menu-item index="/car-loan-application">Apply For Credit</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="dealership">
          <template #title>Dealership</template>
          <el-menu-item index="/about-us">About Us</el-menu-item>
          <el-menu-item index="/contact-us">Contact Us</el-menu-item>
          <el-menu-item index="/careers">Careers</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/appointment">Appointment</el-menu-item>
      </el-menu>
      <button class="mobile-menu-toggle" type="button" :aria-expanded="menuOpen" aria-controls="mobile-navigation" @click="menuOpen = !menuOpen">
        <span aria-hidden="true">{{ menuOpen ? '✕' : '☰' }}</span> {{ menuOpen ? 'Close' : 'Menu' }}
      </button>
    </div>
    <nav v-if="menuOpen" id="mobile-navigation" class="mobile-navigation" aria-label="Main navigation" @keydown.esc="closeMenu">
      <router-link to="/" @click="closeMenu">Home</router-link>
      <details>
        <summary>Inventory</summary>
        <router-link to="/used-cars" @click="closeMenu">Browse Used Inventory</router-link>
        <router-link :to="{ path: '/used-cars', query: { sold: 1 } }" @click="closeMenu">Sold Vehicles</router-link>
        <router-link :to="{ path: '/used-cars', query: { featured: 1 } }" @click="closeMenu">Used Vehicle Specials</router-link>
        <router-link to="/carfinder" @click="closeMenu">Car Finder</router-link>
        <router-link to="/trade-in" @click="closeMenu">Trade-In Appraisal</router-link>
      </details>
      <details>
        <summary>Financing</summary>
        <router-link to="/finance" @click="closeMenu">Finance Department</router-link>
        <router-link to="/car-loan-calculator" @click="closeMenu">Car Loan Calculator</router-link>
        <router-link to="/car-loan-application" @click="closeMenu">Apply For Credit</router-link>
      </details>
      <details>
        <summary>Dealership</summary>
        <router-link to="/about-us" @click="closeMenu">About Us</router-link>
        <router-link to="/contact-us" @click="closeMenu">Contact Us</router-link>
        <router-link to="/careers" @click="closeMenu">Careers</router-link>
      </details>
      <router-link to="/appointment" @click="closeMenu">Appointment</router-link>
    </nav>
  </header>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useMediaQuery } from '@/composables/useMediaQuery'
import { storeToRefs } from 'pinia'
import { useDealerStore } from '@/store/useDealerStore'

const route = useRoute()
const menuOpen = ref(false)
const compact = useMediaQuery('(max-width: 1100px)')
const closeMenu = () => { menuOpen.value = false }
watch(() => route.fullPath, closeMenu)
watch(compact, closeMenu)
const dealerStore = useDealerStore()
const { info: dealerInfo } = storeToRefs(dealerStore)
onMounted(() => dealerStore.ensureLoaded())

const dealerInitial = computed(() => (dealerInfo.value?.name || 'McWilliam Auto').trim().charAt(0).toUpperCase())
</script>

<style scoped lang="scss">
.header { background: #fff; position: sticky; top: 0; z-index: 100; border-bottom: 1px solid var(--s-line); }

.top-bar { background: var(--s-strip); color: var(--s-text2); font-size: 13px; font-weight: 600; }
.top-bar-inner { display: flex; justify-content: space-between; align-items: center; padding: 0 40px; height: 38px; }
.top-bar-inner .right { display: flex; align-items: center; gap: 14px; }
.top-bar-inner .right a { color: var(--s-ink); font-weight: 700; }
.top-bar-inner .right a.text-us-link { color: var(--s-primary); }
.text-us-link { border: 1px solid var(--s-line2); background: #fff; padding: 4px 12px; border-radius: 99px; font-weight: 700; }

.nav-inner { display: flex; align-items: center; justify-content: space-between; padding: 0 40px; height: 76px; }
.logo { display: flex; align-items: center; gap: 10px; }
.logo .mark { width: 38px; height: 38px; border-radius: 8px; background: var(--s-primary); display: flex; align-items: center; justify-content: center; color: #fff; font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 22px; }
.logo .name { font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 24px; letter-spacing: -.01em; text-transform: uppercase; color: var(--s-ink); }

.nav-menu { border-bottom: none; flex: 1; justify-content: flex-end; background: transparent;
  --el-menu-bg-color: transparent; --el-menu-text-color: var(--s-ink); --el-menu-hover-bg-color: transparent;
  --el-menu-hover-text-color: var(--s-primary); --el-menu-active-color: var(--s-primary); --el-menu-item-hover-fill: transparent; }
:deep(.el-menu-item), :deep(.el-sub-menu__title) { font-weight: 700 !important; font-size: 17px !important; height: 76px !important; line-height: 76px !important; padding: 0 18px !important; border-bottom: 3px solid transparent !important; background: transparent !important; }
:deep(.el-menu-item.is-active), :deep(.el-sub-menu.is-active > .el-sub-menu__title) { color: var(--s-primary) !important; border-bottom-color: var(--s-primary) !important; }
:deep(.el-menu-item:hover), :deep(.el-sub-menu__title:hover) { color: var(--s-primary) !important; background: transparent !important; }

.mobile-menu-toggle, .mobile-navigation { display: none; }
@media (max-width: 1100px) {
  .desktop-menu { display: none; }
  .nav-inner { height: auto; min-height: 76px; gap: 16px; }
  .logo { min-width: 0; }
  .logo .mark { flex-shrink: 0; }
  .logo .name { font-size: 22px; line-height: 1.15; overflow-wrap: anywhere; }
  .mobile-menu-toggle { display: inline-flex; align-items: center; justify-content: center; gap: 8px; flex-shrink: 0; min-height: 44px; padding: 8px 12px; border: 1px solid var(--s-line2); border-radius: 6px; color: var(--s-primary); background: #fff; font: inherit; font-weight: 700; font-size: 15px; cursor: pointer; }
  .mobile-menu-toggle span { font-size: 22px; }
  .mobile-navigation { display: block; border-top: 1px solid var(--s-line); padding: 8px 24px 16px; background: #fff; max-height: 65vh; max-height: 65dvh; overflow-y: auto; overscroll-behavior: contain; }
  .mobile-navigation a, .mobile-navigation summary { display: block; padding: 12px 8px; min-height: 44px; font-weight: 700; cursor: pointer; }
  .mobile-navigation summary { display: list-item; margin-left: 20px; }
  .mobile-navigation details a { padding-left: 28px; color: var(--s-text2); }
  .mobile-navigation .router-link-exact-active { color: var(--s-primary); }
}
@media (max-width: 640px) {
  .top-bar-inner { flex-direction: column; align-items: flex-start; height: auto; gap: 6px; padding: 8px 16px; font-size: 12px; line-height: 1.4; }
  .top-bar-inner .right { width: 100%; justify-content: space-between; flex-wrap: wrap; gap: 8px; }
  .top-bar-inner .right > a { white-space: nowrap; }
  .top-bar-inner .right a.text-us-link { padding: 4px 10px; }
  .nav-inner { min-height: 68px; padding: 12px 16px; }
  .logo .name { font-size: 20px; }
  .logo .mark { width: 32px; height: 36px; font-size: 20px; }
  .mobile-navigation { padding-inline: 16px; }
}
</style>
