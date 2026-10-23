<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent="onSubmit">
    <h3 class="section-title">Personal Information</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="First Name" prop="firstName"><el-input v-model="form.firstName" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Last Name" prop="lastName"><el-input v-model="form.lastName" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Email" prop="email"><el-input v-model="form.email" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="e.g. 416-555-1234 or 138-1234-5678" />
        </el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Vehicle Information</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Make" prop="desiredMake">
          <el-select v-model="form.desiredMake" filterable allow-create default-first-option clearable placeholder="Select or type a make" style="width:100%" @change="onMakeChange">
            <el-option v-for="m in makes" :key="m.id" :label="m.name" :value="m.name">
              <img v-if="m.logoUrl" :src="m.logoUrl" :alt="m.name" class="make-logo" />
              {{ m.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Model" prop="desiredModel">
          <el-select v-model="form.desiredModel" filterable allow-create default-first-option clearable placeholder="Select or type a model" style="width:100%">
            <el-option v-for="m in models" :key="m.id" :label="m.name" :value="m.name" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Min Year">
          <el-select v-model="form.minYear" clearable placeholder="Select min year" style="width:100%">
            <el-option v-for="y in yearOptions" :key="y" :label="y" :value="y" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Max Year">
          <el-select v-model="form.maxYear" clearable placeholder="Select max year" style="width:100%">
            <el-option v-for="y in yearOptions" :key="y" :label="y" :value="y" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Trim"><el-input v-model="form.desiredTrim" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Kilometers"><el-input-number v-model="form.desiredKm" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Body Style">
          <el-select v-model="form.desiredBodyStyle" filterable allow-create default-first-option clearable placeholder="Select a body style" style="width:100%">
            <el-option v-for="b in bodyStyles" :key="b.id" :label="b.name" :value="b.name" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Transmission">
          <el-select v-model="form.desiredTransmission" filterable allow-create default-first-option clearable placeholder="Select a transmission" style="width:100%">
            <el-option v-for="t in transmissions" :key="t.id" :label="t.name" :value="t.name" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Driveline">
          <el-select v-model="form.desiredDriveline" clearable placeholder="Select a driveline" style="width:100%">
            <el-option v-for="d in drivelineOptions" :key="d" :label="d" :value="d" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Fuel Type">
          <el-select v-model="form.desiredFuelType" filterable allow-create default-first-option clearable placeholder="Select a fuel type" style="width:100%">
            <el-option v-for="f in fuelTypes" :key="f.id" :label="f.name" :value="f.name" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Colour">
          <el-select v-model="form.desiredColor" filterable allow-create default-first-option clearable placeholder="Select a colour" style="width:100%">
            <el-option v-for="c in colors" :key="c.id" :label="c.name" :value="c.name" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Condition">
          <el-select v-model="form.desiredCondition" clearable placeholder="Select condition" style="width:100%">
            <el-option v-for="c in conditionOptions" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Budget Min"><el-input-number v-model="form.budgetMin" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Budget Max" prop="budgetMax"><el-input-number v-model="form.budgetMax" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="Additional Information">
          <el-input v-model="form.message" type="textarea" rows="4" placeholder="Anything else we should know about what you're looking for?" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-button type="primary" native-type="submit" :loading="loading">Submit Request</el-button>
  </el-form>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { submitCarFinder } from '@/api/lead'
import { getMakes, getModels, getBodyStyles, getTransmissions, getFuelTypes, getColors } from '@/api/dict'
import { phoneRules } from '@/utils/phoneValidator'
import { emailRules } from '@/utils/emailValidator'

const loading = ref(false)
const formRef = ref(null)
const form = reactive({
  firstName: '', lastName: '', email: '', phone: '',
  desiredMake: '', desiredModel: '', minYear: null, maxYear: null,
  desiredTrim: '', desiredKm: null, desiredBodyStyle: '', desiredTransmission: '',
  desiredDriveline: '', desiredFuelType: '', desiredColor: '', desiredCondition: '',
  budgetMin: null, budgetMax: null, message: ''
})

const rules = {
  firstName: [{ required: true, message: 'First Name is required', trigger: 'blur' }],
  lastName: [{ required: true, message: 'Last Name is required', trigger: 'blur' }],
  email: emailRules,
  phone: phoneRules,
  desiredMake: [{ required: true, message: 'Make is required', trigger: 'change' }],
  desiredModel: [{ required: true, message: 'Model is required', trigger: 'change' }],
  budgetMax: [{ required: true, message: 'Budget Max is required', trigger: 'change' }]
}

const makes = ref([])
const models = ref([])
const bodyStyles = ref([])
const transmissions = ref([])
const fuelTypes = ref([])
const colors = ref([])

// 跟Trade-In Appraisal同一套：没有专门字典管理页的字段，用固定选项
const drivelineOptions = ['FWD', 'RWD', 'AWD', '4WD']
const conditionOptions = ['Excellent', 'Good', 'Fair', 'Poor']

const currentYear = new Date().getFullYear()
const yearOptions = []
for (let y = currentYear; y >= 1970; y--) yearOptions.push(y)

onMounted(async () => {
  ;[makes.value, bodyStyles.value, transmissions.value, fuelTypes.value, colors.value] =
    await Promise.all([getMakes(), getBodyStyles(), getTransmissions(), getFuelTypes(), getColors()])
})

async function onMakeChange(makeName) {
  form.desiredModel = ''
  const matched = makes.value.find((m) => m.name === makeName)
  models.value = matched ? await getModels(matched.id) : []
}

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await submitCarFinder(form)
    ElMessage.success('Request submitted! We will find the right vehicle for you.')
    formRef.value.clearValidate()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.section-title { font-family: 'Archivo', sans-serif; font-size: 18px; font-weight: 600; margin: 4px 0 16px; }
.section-title:not(:first-child) { margin-top: 8px; }
</style>
