<template>
  <div class="option-list">
    <h3>View All Options</h3>
    <div v-for="(items, group) in grouped" :key="group" class="option-group">
      <h4>{{ group }}</h4>
      <ul>
        <li v-for="o in items" :key="o.id">{{ o.content }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({ options: { type: Array, default: () => [] } })

const grouped = computed(() => {
  const map = {}
  for (const o of props.options) {
    if (!map[o.optionGroup]) map[o.optionGroup] = []
    map[o.optionGroup].push(o)
  }
  return map
})
</script>

<style scoped>
.option-group { margin-bottom: 16px; }
.option-group h4 { margin-bottom: 6px; color: var(--s-text2); }
.option-group ul { columns: 2; padding-left: 18px; }
.option-group li { margin-bottom: 4px; font-size: 14px; }
</style>
