<template>
  <div class="sort-bar">
    <span>{{ total }} Vehicles</span>
    <div>
      Sort:
      <el-radio-group v-model="sortByLocal" size="small" @change="onChange">
        <el-radio-button label="year">Year</el-radio-button>
        <el-radio-button label="price">Price</el-radio-button>
        <el-radio-button label="mileage">Mileage</el-radio-button>
      </el-radio-group>
      <el-radio-group v-model="viewModeLocal" size="small" style="margin-left:16px" @change="onViewChange">
        <el-radio-button label="grid">Grid</el-radio-button>
        <el-radio-button label="list">List</el-radio-button>
      </el-radio-group>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({ total: Number, sortBy: String, viewMode: String })
const emit = defineEmits(['update:sortBy', 'update:viewMode'])

const sortByLocal = ref(props.sortBy)
const viewModeLocal = ref(props.viewMode)

watch(() => props.sortBy, v => sortByLocal.value = v)
watch(() => props.viewMode, v => viewModeLocal.value = v)

function onChange(v) { emit('update:sortBy', v) }
function onViewChange(v) { emit('update:viewMode', v) }
</script>

<style scoped>
.sort-bar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
</style>
