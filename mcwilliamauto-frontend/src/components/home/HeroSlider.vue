<template>
  <div class="hero">
    <Swiper
      v-if="banners.length"
      class="hero-swiper"
      :modules="[Autoplay, EffectFade]"
      effect="fade"
      :fade-effect="{ crossFade: true }"
      :autoplay="{ delay: 6000, disableOnInteraction: false }"
      :loop="banners.length > 1"
    >
      <SwiperSlide v-for="b in banners" :key="b.id">
        <a v-if="b.linkUrl" :href="fixUrl(b.linkUrl)" target="_blank" rel="noopener noreferrer" class="hero-slide-bg hero-link" :style="{ backgroundImage: `url(${b.imageUrl})` }" :aria-label="b.title"></a>
        <div v-else class="hero-slide-bg" :style="{ backgroundImage: `url(${b.imageUrl})` }" :aria-label="b.title"></div>
      </SwiperSlide>
    </Swiper>
    <div v-else class="hero-slide-bg hero-fallback-bg"></div>

    <div class="hero-inner container">
      <div class="eyebrow">WINNIPEG · ONE-PRICE USED CAR DEALER</div>
      <h1>Get the vehicle you need,<br>with the payments you can afford.</h1>
      <p class="lede">Every vehicle is clearly priced, no haggling, no games. We just want car buying to be simple.</p>
      <div class="chips">
        <span v-if="vehicleTotal !== null" class="chip">{{ vehicleTotal }} Vehicles</span>
        <span class="chip">Full CARFAX Reports</span>
        <span class="chip">Flexible Financing</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Autoplay, EffectFade } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/effect-fade'
import { getBanners } from '@/api/content'
import { getVehicleList } from '@/api/vehicle'

const banners = ref([])
const vehicleTotal = ref(null)

// 后台填的链接如果漏了 http(s):// ，补上，避免被当成站内相对路径
function fixUrl(u) {
  const t = (u || '').trim()
  return /^(https?:)?\/\//i.test(t) || t.startsWith('/') ? t : 'https://' + t
}

onMounted(async () => {
  try {
    banners.value = await getBanners()
  } catch (e) {
    banners.value = []
  }
  // 车辆总数：取前台可见的全部车辆（含已售出），只要 total，所以 pageSize=1
  try {
    const res = await getVehicleList({ pageNum: 1, pageSize: 1 })
    vehicleTotal.value = res.total
  } catch (e) {
    vehicleTotal.value = null
  }
})
</script>

<style scoped lang="scss">
.hero {
  position: relative;
  min-height: 500px;
  display: flex;
  align-items: center;
  overflow: hidden;
  background: linear-gradient(115deg, var(--s-soft) 0%, #fff 55%, var(--s-soft2) 100%);
}
.hero-swiper { position: absolute; inset: 0; z-index: 0; }
.hero-swiper :deep(.swiper-slide) { height: 100%; }

.hero-slide-bg {
  display: block;
  width: 100%;
  height: 100%;
  min-height: 500px;
  background-size: cover;
  background-position: center;
}
.hero-fallback-bg {
  position: absolute; inset: 0; z-index: 0;
  background-image: url('https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?w=1600&q=80');
}

/* 后台维护的轮播图作为背景：上面叠一层浅色渐变，左侧偏白，保证深色文字清晰可读，右侧透出图片 */
.hero::before {
  content: '';
  position: absolute; inset: 0; z-index: 1;
  background: linear-gradient(100deg, rgba(255,255,255,.94) 0%, rgba(255,255,255,.82) 42%, rgba(255,255,255,.25) 75%, rgba(255,255,255,0) 100%);
  pointer-events: none;
}

.hero-link { cursor: pointer; }
/* 文字层只是展示，不拦截鼠标，这样点击文字区域也能穿透到下面的 banner 链接 */
.hero-inner { pointer-events: none; position: relative; z-index: 2; padding: 84px 40px 110px; max-width: 1280px; }
.eyebrow { font: 800 11px/1 'Archivo', sans-serif; letter-spacing: .22em; text-transform: uppercase; color: var(--s-primary); margin-bottom: 18px; }
.hero h1 { color: var(--s-ink); font-family: 'Archivo', sans-serif; font-size: 64px; font-weight: 900; line-height: 1; max-width: 900px; letter-spacing: -.03em; }
.lede { margin-top: 22px; max-width: 640px; color: var(--s-text2); font-size: 18px; line-height: 1.6; }
.chips { display: flex; gap: 10px; margin-top: 28px; flex-wrap: wrap; }
.chip { font-size: 17px; font-weight: 700; border: 1px solid var(--s-line2); background: #fff; color: var(--s-ink); padding: 8px 16px; border-radius: 99px; }
.chip { transition: color .15s; cursor: default; pointer-events: auto; }
.chip:hover { color: var(--s-primary); }
</style>
