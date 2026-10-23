<template>
  <div class="gallery">
    <div class="main-image-wrap">
      <el-image :src="mainImage" fit="cover" class="main-image" :class="{ 'is-sold-mask': vehicle?.isSold }" :preview-src-list="allUrls" :initial-index="activeIndex" :preview-teleported="true" />
      <VehicleBadgeOverlay v-if="vehicle" :vehicle="vehicle" />
      <button v-if="images.length > 1" class="nav-arrow left" @click.stop="prevImage">‹</button>
      <button v-if="images.length > 1" class="nav-arrow right" @click.stop="nextImage">›</button>
    </div>
    <div class="thumbs">
      <img v-for="(img, idx) in images" :key="img.id" :src="img.thumbUrl"
           :class="{ active: idx === activeIndex }" @click="activeIndex = idx" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import VehicleBadgeOverlay from './VehicleBadgeOverlay.vue'

const props = defineProps({ images: { type: Array, default: () => [] }, vehicle: { type: Object, default: null } })
const activeIndex = ref(0)
const placeholder = 'https://via.placeholder.com/800x600?text=No+Image'

const mainImage = computed(() => {
  if (!props.images.length) return placeholder
  return props.images[activeIndex.value]?.imageUrl || placeholder
})
const allUrls = computed(() => props.images.map(i => i.imageUrl))

function prevImage() {
  if (!props.images.length) return
  activeIndex.value = (activeIndex.value - 1 + props.images.length) % props.images.length
}
function nextImage() {
  if (!props.images.length) return
  activeIndex.value = (activeIndex.value + 1) % props.images.length
}
</script>

<style scoped>
.main-image-wrap { position: relative; }
.main-image { width: 100%; height: 420px; border-radius: 8px; background: var(--s-ph1); display: block; }
.main-image.is-sold-mask { filter: grayscale(90%) brightness(0.82); }
.nav-arrow {
  position: absolute; top: 50%; transform: translateY(-50%);
  width: 44px; height: 44px; border-radius: 50%; border: none;
  background: rgba(255,255,255,.94); color: var(--s-ink); font-size: 26px; line-height: 1;
  cursor: pointer; display: flex; align-items: center; justify-content: center;
  transition: background .15s;
}
.nav-arrow:hover { background: #fff; }
.nav-arrow.left { left: 14px; }
.nav-arrow.right { right: 14px; }
.thumbs { display: flex; gap: 8px; margin-top: 10px; overflow-x: auto; }
.thumbs img { width: 80px; height: 60px; object-fit: cover; border-radius: 4px; cursor: pointer; opacity: .6; }
.thumbs img.active { opacity: 1; outline: 2px solid var(--s-primary); }
</style>
