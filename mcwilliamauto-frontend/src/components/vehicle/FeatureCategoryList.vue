<template>
  <div class="feature-list">
    <h3>Features</h3>
    <div v-for="(items, category) in grouped" :key="category" class="category-block">
      <h4>{{ categoryLabel(category) }}</h4>
      <ul>
        <li v-for="f in items" :key="f.id">{{ f.content }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({ features: { type: Array, default: () => [] } })

const grouped = computed(() => {
  const map = {}
  for (const f of props.features) {
    if (!map[f.category]) map[f.category] = []
    map[f.category].push(f)
  }
  return map
})

const labels = {
  MECHANICAL: 'Mechanical / Performance',
  SAFETY: 'Safety',
  TECHNOLOGY: 'Technology / Infotainment',
  COMFORT: 'Comfort / Interior',
  EXTERIOR: 'Exterior / Styling',
  OFFROAD: 'Off Road / Capability'
}
function categoryLabel(c) { return labels[c] || c }
</script>

<style scoped>
.category-block { margin-bottom: 16px; }
.category-block h4 { margin-bottom: 6px; color: var(--s-text2); }
.category-block ul { columns: 2; padding-left: 18px; }
.category-block li { margin-bottom: 4px; font-size: 14px; }
</style>
