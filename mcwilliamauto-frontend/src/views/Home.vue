<template>
  <div class="home">
    <HeroSlider />
    <QuickSearchBar />
    <div style="margin-top:64px">
      <BodyStyleNav />
    </div>
    <div style="margin-top:64px">
      <FeaturedVehicles />
    </div>
    <div style="margin-top:80px; margin-bottom:24px">
      <FinancingPromo />
    </div>
  </div>
</template>

<script setup>
import HeroSlider from '@/components/home/HeroSlider.vue'
import QuickSearchBar from '@/components/home/QuickSearchBar.vue'
import BodyStyleNav from '@/components/home/BodyStyleNav.vue'
import FeaturedVehicles from '@/components/home/FeaturedVehicles.vue'
import FinancingPromo from '@/components/home/FinancingPromo.vue'
</script>
