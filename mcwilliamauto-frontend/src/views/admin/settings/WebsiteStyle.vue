<template>
  <div class="panel">
    <h2 class="panel-title">Website Style</h2>
    <p class="panel-subtitle">Choose the look of the public website. The admin backend keeps its current style. Changes apply to visitors after you save.</p>

    <div class="theme-grid" v-loading="loading">
      <div v-for="t in themes" :key="t.code" class="theme-card" :class="{ active: selected === t.code }" @click="selected = t.code">
        <!-- 迷你首页示意图：用该风格自己的颜色画出顶部条/导航/大图/搜索条/车辆卡片 -->
        <div class="mini" :style="{ background: t.bg }">
          <div class="mini-announce" :style="{ background: t.soft }"></div>
          <div class="mini-nav" :style="{ borderBottomColor: t.line }">
            <span class="mini-logo" :style="{ background: t.primary }"></span>
            <span class="mini-link" v-for="n in 4" :key="n" :style="{ background: n === 1 ? t.primary : t.line2 }"></span>
          </div>
          <div class="mini-hero" :style="{ background: `linear-gradient(110deg, ${t.soft}, #fff 55%, ${t.soft2})` }">
            <div class="mini-h" :style="{ background: t.ink }"></div>
            <div class="mini-h short" :style="{ background: t.ink }"></div>
            <div class="mini-btn" :style="{ background: t.primary }"></div>
          </div>
          <div class="mini-cards">
            <div v-for="n in 3" :key="n" class="mini-card" :style="{ borderColor: t.line }">
              <div class="mini-photo" :style="{ background: t.ph }"><span class="mini-tag" :style="{ background: t.accent }"></span></div>
              <div class="mini-line" :style="{ background: t.ink }"></div>
              <div class="mini-line short" :style="{ background: t.line2 }"></div>
            </div>
          </div>
        </div>
        <div class="theme-info">
          <el-radio :model-value="selected" :value="t.code" @click.stop="selected = t.code">
            <b>{{ t.code }} · {{ t.name }}</b>
          </el-radio>
          <div class="swatches">
            <span v-for="c in [t.primary, t.soft2, t.bg, t.accent, t.ink]" :key="c" class="sw" :style="{ background: c }"></span>
          </div>
          <p class="theme-desc">{{ t.desc }}</p>
        </div>
      </div>
    </div>

    <el-button type="primary" :loading="saving" :disabled="!selected || selected === saved" @click="save">Save Style</el-button>
    <span v-if="saved" class="current">Current: {{ saved }}</span>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { settingsApi } from '@/api/admin'
import { useDealerStore } from '@/store/useDealerStore'

const themes = [
  { code: 'A', name: 'Sky Blue', desc: 'Sky blue + cool grey-white, amber accents. Clean and professional.',
    primary: '#2D6CB0', soft: '#EAF1F9', soft2: '#DCE8F6', bg: '#F3F5F8', line: '#DDE2EA', line2: '#CDD4DF', ph: '#E8EBF0', ink: '#1F2937', accent: '#C9822B' },
  { code: 'B', name: 'Forest Green', desc: 'Forest green + warm off-white. Calm and trustworthy.',
    primary: '#2F6B4F', soft: '#E8F0EA', soft2: '#D6E6DB', bg: '#F6F5F0', line: '#DEDDD2', line2: '#CFCEC1', ph: '#E9E8E0', ink: '#1F2A24', accent: '#B7791F' },
  { code: 'C', name: 'Teal', desc: 'Teal + cool white. Modern and fresh.',
    primary: '#1F7A8C', soft: '#E4F2F5', soft2: '#D0E8EE', bg: '#F4F7F8', line: '#D9E3E6', line2: '#C7D5DA', ph: '#E4EAEC', ink: '#1D2B30', accent: '#C27A2C' },
  { code: 'D', name: 'Burgundy', desc: 'Burgundy + ivory. Classic and refined.',
    primary: '#9A3B3B', soft: '#F6ECE9', soft2: '#EEDAD4', bg: '#FAF7F4', line: '#E6DDD6', line2: '#D6CABF', ph: '#EDE6E0', ink: '#2B2220', accent: '#B07A2A' },
  { code: 'E', name: 'Indigo', desc: 'Indigo + soft lavender-white. Modern and trustworthy.',
    primary: '#4A57A8', soft: '#ECEEF8', soft2: '#DDE1F2', bg: '#F5F5F9', line: '#DEDFEA', line2: '#CFD1E1', ph: '#E8E9F1', ink: '#1E2036', accent: '#C98A2B' },
  { code: 'F', name: 'Antique Bronze', desc: 'Antique bronze + warm sand, deep teal accents. Warm and premium.',
    primary: '#8A6420', soft: '#F6EFE1', soft2: '#EDE0C6', bg: '#FAF8F3', line: '#E7E0D0', line2: '#D8CEB8', ph: '#EEE8DA', ink: '#2A241A', accent: '#2B6E73' },
  { code: 'G', name: 'Plum', desc: 'Plum + light lilac-white. Elegant and distinctive.',
    primary: '#6E4B8E', soft: '#F1ECF6', soft2: '#E4DAEE', bg: '#F8F6FA', line: '#E3DDE9', line2: '#D3CADD', ph: '#ECE8F0', ink: '#271F30', accent: '#C08A2E' },
  { code: 'H', name: 'Graphite', desc: 'Graphite slate + neutral grey-white. Understated and mature.',
    primary: '#475569', soft: '#ECEFF3', soft2: '#DEE3EA', bg: '#F4F5F7', line: '#DCE0E6', line2: '#CBD1D9', ph: '#E7EAEE', ink: '#1E2530', accent: '#C9822B' }
]

const dealerStore = useDealerStore()
const loading = ref(false)
const saving = ref(false)
const selected = ref('A')
const saved = ref('')

onMounted(async () => {
  loading.value = true
  try {
    const info = await settingsApi.getDealerInfo()
    const code = themes.some(t => t.code === info?.themeCode) ? info.themeCode : 'A'
    selected.value = code
    saved.value = code
  } finally {
    loading.value = false
  }
})

async function save() {
  saving.value = true
  try {
    await settingsApi.saveTheme(selected.value)
    saved.value = selected.value
    // 同步前台缓存，切回前台页面立刻生效；其他访客刷新页面后看到新风格
    if (dealerStore.info) dealerStore.info = { ...dealerStore.info, themeCode: selected.value }
    try { localStorage.setItem('siteTheme', selected.value) } catch (e) { /* ignore */ }
    ElMessage.success('Website style saved')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped lang="scss">
.panel { background: #fff; border-radius: 20px; padding: 28px 32px; box-shadow: 0 1px 3px rgba(23,34,59,.06); }
.panel-title { font-family: 'Fraunces', serif; font-size: 22px; font-weight: 600; margin: 0; }
.panel-subtitle { color: #7A7364; font-size: 14px; margin: 4px 0 20px; }

.theme-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 20px; margin-bottom: 24px; }
@media (max-width: 1100px) { .theme-grid { grid-template-columns: 1fr; } }
.theme-card { border: 2px solid #EAE3D4; border-radius: 16px; overflow: hidden; cursor: pointer; transition: .15s; background: #fff; }
.theme-card:hover { border-color: #C9BFA6; }
.theme-card.active { border-color: #E0684A; box-shadow: 0 6px 20px rgba(224,104,74,.18); }
.theme-info { padding: 14px 18px 16px; border-top: 1px solid #EAE3D4; }
.swatches { display: flex; gap: 8px; margin: 10px 0 8px; }
.sw { width: 26px; height: 26px; border-radius: 50%; border: 1px solid rgba(0,0,0,.1); }
.theme-desc { margin: 0; color: #7A7364; font-size: 13px; }
.current { margin-left: 14px; color: #7A7364; font-size: 13px; }

/* 迷你首页示意图 */
.mini { padding: 0 0 12px; height: 230px; overflow: hidden; }
.mini-announce { height: 12px; }
.mini-nav { display: flex; align-items: center; gap: 10px; padding: 8px 14px; background: #fff; border-bottom: 1px solid; }
.mini-logo { width: 16px; height: 16px; border-radius: 4px; margin-right: auto; }
.mini-link { width: 26px; height: 5px; border-radius: 3px; }
.mini-hero { padding: 16px 14px 18px; }
.mini-h { height: 9px; width: 55%; border-radius: 3px; margin-bottom: 6px; }
.mini-h.short { width: 38%; }
.mini-btn { width: 54px; height: 12px; border-radius: 3px; margin-top: 10px; }
.mini-cards { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; padding: 12px 14px 0; }
.mini-card { background: #fff; border: 1px solid; border-radius: 5px; padding: 6px; }
.mini-photo { height: 38px; border-radius: 3px; position: relative; margin-bottom: 6px; }
.mini-tag { position: absolute; left: 4px; top: 4px; width: 20px; height: 6px; border-radius: 2px; }
.mini-line { height: 5px; width: 80%; border-radius: 3px; margin-bottom: 4px; }
.mini-line.short { width: 50%; }
</style>
