<template>
  <div class="container body-style-nav">
    <div class="eyebrow-label">Browse</div>
    <h2 class="section-title">Search By Body Type</h2>
    <p class="section-subtitle">Find the vehicle style that fits your life</p>
    <div class="body-style-grid">
      <div v-for="b in bodyStyles" :key="b.id" class="body-style-item"
           @click="$router.push({ path: '/used-cars', query: { bodyStyleId: b.id } })">
        <div class="photo-wrap">
          <img v-if="b.photoUrl" :src="b.photoUrl" :alt="b.name" class="photo" />
          <div v-else class="photo-placeholder">No Photo</div>
        </div>
        <div class="lbl">{{ b.name }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getBodyStyles } from '@/api/dict'

const bodyStyles = ref([])
onMounted(async () => {
  bodyStyles.value = await getBodyStyles()
})
</script>

<style scoped lang="scss">
// 容器宽度/左右边距与 Featured Vehicles 保持一致。
// 车型数量由后台维护：卡片宽度固定（一行按 --cols 列计算），用 space-between 让第一张贴左边、
// 最后一张贴右边，中间空白自动平均分配；超过一行自动换行
.body-style-nav.container { max-width: 1680px; padding: 0 24px; }
.body-style-grid { --cols: 6; --gap: 24px; display: flex; flex-wrap: wrap; justify-content: space-between; gap: var(--gap); padding: 0 56px; }
@media (max-width: 1200px) { .body-style-grid { --cols: 4; padding: 0 32px; } }
@media (max-width: 900px) { .body-style-grid { --cols: 3; } }
@media (max-width: 640px) { .body-style-grid { --cols: 2; --gap: 14px; padding: 0; } }
.body-style-item {
  flex: 0 0 calc((100% - var(--gap) * (var(--cols) - 1)) / var(--cols));
  background: #fff; border-radius: 8px; cursor: pointer;
  border: 1px solid var(--s-line);
  transition: .18s;
  overflow: hidden;
}
.body-style-item:hover {
  border-color: var(--s-primary);
  transform: translateY(-3px);
  box-shadow: 0 12px 24px rgba(var(--s-primary-rgb), .12);
}
.photo-wrap {
  height: 120px;
  display: flex; align-items: center; justify-content: center;
  padding: 14px;
  border-bottom: 1px solid var(--s-line);
  background: linear-gradient(135deg, var(--s-ph1), #fff);
}
.photo { max-width: 100%; max-height: 100%; object-fit: contain; mix-blend-mode: multiply; }
.photo-placeholder { color: var(--s-faint); font-size: 12px; font-weight: 600; }
.lbl {
  font-family: 'Archivo', sans-serif; font-weight: 800; font-size: 13px; letter-spacing: .14em; text-transform: uppercase;
  text-align: center; padding: 14px 10px;
}
</style>
