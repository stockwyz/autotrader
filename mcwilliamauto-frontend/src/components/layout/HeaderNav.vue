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

      <el-menu mode="horizontal" :ellipsis="false" class="nav-menu" router :default-active="$route.path">
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
    </div>
  </header>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useDealerStore } from '@/store/useDealerStore'

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
</style>
