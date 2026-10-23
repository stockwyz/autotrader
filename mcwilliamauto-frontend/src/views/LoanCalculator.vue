<template>
  <div class="container page">
    <h1>Car Loan Calculator</h1>
    <el-card style="max-width:500px">
      <el-form label-position="top">
        <el-form-item label="Vehicle Price ($)">
          <el-input-number v-model="price" :min="0" :step="500" style="width:100%" />
        </el-form-item>
        <el-form-item label="Down Payment ($)">
          <el-input-number v-model="downPayment" :min="0" :step="500" style="width:100%" />
        </el-form-item>
        <el-form-item label="Annual Interest Rate (%)">
          <el-input-number v-model="rate" :min="0" :max="30" :step="0.1" style="width:100%" />
        </el-form-item>
        <el-form-item label="Loan Term (months)">
          <el-select v-model="term" style="width:100%">
            <el-option v-for="t in terms" :key="t" :label="`${t} months`" :value="t" />
          </el-select>
        </el-form-item>
      </el-form>
      <el-divider />
      <div class="result">
        <p>Estimated Monthly Payment: <strong>${{ monthly }}</strong></p>
        <p>Estimated Bi-weekly Payment: <strong>${{ biweekly }}</strong></p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const price = ref(30000)
const downPayment = ref(2000)
const rate = ref(7.99)
const term = ref(72)
const terms = [24, 36, 48, 60, 72, 84, 96]

const monthly = computed(() => {
  const principal = Math.max(price.value - downPayment.value, 0)
  const r = rate.value / 100 / 12
  const n = term.value
  if (principal <= 0) return '0.00'
  const m = r === 0 ? principal / n : (principal * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1)
  return m.toFixed(2)
})

const biweekly = computed(() => ((Number(monthly.value) * 12) / 26).toFixed(2))
</script>

<style scoped>
.page { padding: 24px 16px; }
.result p { font-size: 16px; }
</style>
