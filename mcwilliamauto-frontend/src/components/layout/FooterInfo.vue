<template>
  <footer class="footer">
    <div class="container footer-grid">
      <div>
        <div class="flogo">
          <div class="mark">{{ dealerInitial }}</div><span>{{ dealerInfo?.name || 'McWilliam Auto' }}</span>
        </div>
        <p>Winnipeg's one-price used car dealership on South Landing Drive. Straightforward car buying, done right.</p>
      </div>
      <div>
        <h4>Our Hours</h4>
        <p v-for="h in formattedHours" :key="h.day">{{ h.day }}: {{ h.time }}</p>
      </div>
      <div>
        <h4>Contact Us</h4>
        <p>{{ dealerInfo?.address || '155 McPhillips St, Winnipeg, MB R3E 2J8' }}</p>
        <p>{{ dealerInfo?.phone || '204-560-1234' }}</p>
      </div>
      <div>
        <h4>Follow Us</h4>
        <div class="social-row">
          <a v-if="dealerInfo?.facebookUrl" :href="dealerInfo.facebookUrl" target="_blank" rel="noopener" class="social-icon" aria-label="Facebook">
            <svg viewBox="0 0 24 24" fill="currentColor"><path d="M22 12a10 10 0 1 0-11.5 9.9v-7H7.9V12h2.6V9.8c0-2.6 1.5-4 3.9-4 1.1 0 2.3.2 2.3.2v2.5h-1.3c-1.3 0-1.7.8-1.7 1.6V12h2.9l-.5 2.9h-2.4v7A10 10 0 0 0 22 12z"/></svg>
          </a>
          <a v-if="dealerInfo?.instagramUrl" :href="dealerInfo.instagramUrl" target="_blank" rel="noopener" class="social-icon" aria-label="Instagram">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><rect x="3" y="3" width="18" height="18" rx="5"/><circle cx="12" cy="12" r="4"/><circle cx="17.3" cy="6.7" r="1.1" fill="currentColor" stroke="none"/></svg>
          </a>
        </div>
      </div>
    </div>
    <div class="copyright">© {{ new Date().getFullYear() }} {{ dealerInfo?.name || 'McWilliam Auto' }}</div>
  </footer>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useDealerStore } from '@/store/useDealerStore'

const dealerStore = useDealerStore()
const { info: dealerInfo, hours } = storeToRefs(dealerStore)
onMounted(() => dealerStore.ensureLoaded())

const dealerInitial = computed(() => (dealerInfo.value?.name || 'McWilliam Auto').trim().charAt(0).toUpperCase())

const DAY_NAMES = ['', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']

function formatTime(t) {
  if (!t) return null
  const [hStr, mStr] = t.split(':')
  let h = parseInt(hStr, 10)
  const period = h >= 12 ? 'PM' : 'AM'
  h = h % 12 || 12
  return `${h}:${mStr} ${period}`
}

const formattedHours = computed(() => {
  const sorted = [...(hours.value || [])].sort((a, b) => a.dayOfWeek - b.dayOfWeek)
  return sorted.map(h => ({
    day: DAY_NAMES[h.dayOfWeek] || h.dayOfWeek,
    time: h.openTime && h.closeTime ? `${formatTime(h.openTime)} - ${formatTime(h.closeTime)}` : 'Closed'
  }))
})
</script>

<style scoped lang="scss">
.footer { background: #fff; color: var(--s-text2); margin-top: 80px; padding: 64px 0 0; border-top: 1px solid var(--s-line); }
.footer-grid { display: grid; grid-template-columns: 1.6fr repeat(3, 1fr); gap: 40px; padding-bottom: 36px; }
.flogo { display: flex; align-items: center; gap: 10px; margin-bottom: 16px; }
.flogo .mark { width: 36px; height: 36px; border-radius: 8px; background: var(--s-primary); display: flex; align-items: center; justify-content: center; color: #fff; font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 20px; }
.flogo span { font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 22px; text-transform: uppercase; letter-spacing: -.01em; color: var(--s-ink); }
.footer h4 { font-family: 'Archivo', sans-serif; text-transform: uppercase; font-size: 11px; letter-spacing: .2em; color: var(--s-ink); margin-bottom: 16px; font-weight: 800; }
.footer p { font-size: 14px; line-height: 1.9; color: var(--s-text2); }
.footer a { display: block; font-size: 14px; line-height: 1.9; color: var(--s-text2); }
.social-row { display: flex; gap: 12px; margin-top: 4px; }
.social-icon {
  display: flex; align-items: center; justify-content: center;
  width: 36px; height: 36px; border-radius: 50%;
  border: 1px solid var(--s-line2); background: #fff; color: var(--s-primary);
  transition: background .15s, color .15s;
}
.social-icon svg { width: 17px; height: 17px; }
.social-icon:hover { background: var(--s-primary); color: #fff; border-color: var(--s-primary); }
.copyright { border-top: 1px solid var(--s-line); text-align: center; padding: 20px; font-size: 12px; color: var(--s-faint); }
</style>
