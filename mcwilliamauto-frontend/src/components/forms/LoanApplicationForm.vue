<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent="onSubmit">
    <h3 class="section-title">Personal Information</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Salutation">
          <el-select v-model="form.salutation" clearable placeholder="Salutation" style="width:100%">
            <el-option v-for="o in salutationOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Gender">
          <el-select v-model="form.gender" clearable placeholder="Gender" style="width:100%">
            <el-option v-for="o in genderOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="First Name" prop="firstName"><el-input v-model="form.firstName" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Last Name" prop="lastName"><el-input v-model="form.lastName" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="e.g. 416-555-1234 or 138-1234-5678" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Email" prop="email"><el-input v-model="form.email" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Marital Status" prop="maritalStatus">
          <el-select v-model="form.maritalStatus" placeholder="Marital Status" style="width:100%">
            <el-option v-for="o in maritalStatusOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Birth Date  Format (yyyy-mm-dd)" prop="dateOfBirth">
          <el-date-picker v-model="form.dateOfBirth" type="date" value-format="YYYY-MM-DD" placeholder="yyyy-mm-dd" style="width:100%" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="SIN"><el-input v-model="form.sin" maxlength="11" /></el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Current Address</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Address" prop="address"><el-input v-model="form.address" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="City" prop="city"><el-input v-model="form.city" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Province" prop="province">
          <el-select v-model="form.province" placeholder="Province" style="width:100%">
            <el-option v-for="p in provinceOptions" :key="p" :label="p" :value="p" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Postal Code" prop="postalCode"><el-input v-model="form.postalCode" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Duration Year" prop="addressDurationYears"><el-input-number v-model="form.addressDurationYears" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Duration Month" prop="addressDurationMonths"><el-input-number v-model="form.addressDurationMonths" style="width:100%" :controls="false" :min="0" :max="11" /></el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Home Rent/Mortgage Information</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Home Status" prop="homeStatus">
          <el-select v-model="form.homeStatus" placeholder="Home Status" style="width:100%">
            <el-option v-for="o in homeStatusOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Monthly Payment" prop="monthlyHousingPayment"><el-input-number v-model="form.monthlyHousingPayment" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Current Employment</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Type" prop="employmentType">
          <el-select v-model="form.employmentType" placeholder="Type" style="width:100%">
            <el-option v-for="o in employmentTypeOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Employer" prop="employerName"><el-input v-model="form.employerName" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Occupation" prop="occupation"><el-input v-model="form.occupation" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Employment Address" prop="employmentAddress"><el-input v-model="form.employmentAddress" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="City" prop="employmentCity"><el-input v-model="form.employmentCity" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Province" prop="employmentProvince">
          <el-select v-model="form.employmentProvince" placeholder="Province" style="width:100%">
            <el-option v-for="p in provinceOptions" :key="p" :label="p" :value="p" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Postal Code" prop="employmentPostalCode"><el-input v-model="form.employmentPostalCode" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Phone" prop="employmentPhone"><el-input v-model="form.employmentPhone" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Gross Income" prop="grossIncome"><el-input-number v-model="form.grossIncome" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Duration Year" prop="employmentDurationYears"><el-input-number v-model="form.employmentDurationYears" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Duration Month" prop="employmentDurationMonths"><el-input-number v-model="form.employmentDurationMonths" style="width:100%" :controls="false" :min="0" :max="11" /></el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Previous Employment</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Previous Employer"><el-input v-model="form.previousEmployer" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Phone"><el-input v-model="form.previousEmployerPhone" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Duration Year"><el-input-number v-model="form.previousEmploymentDurationYears" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Duration Month"><el-input-number v-model="form.previousEmploymentDurationMonths" style="width:100%" :controls="false" :min="0" :max="11" /></el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Other Information</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Previous Bankruptcy?">
          <el-select v-model="form.previousBankruptcy" clearable placeholder="Previous Bankruptcy?" style="width:100%">
            <el-option v-for="o in yesNoOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Previous Repossession?">
          <el-select v-model="form.previousRepossession" clearable placeholder="Previous Repossession?" style="width:100%">
            <el-option v-for="o in yesNoOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Is Cosigner Available?">
          <el-select v-model="form.cosignerAvailable" clearable placeholder="Is Cosigner Available?" style="width:100%">
            <el-option v-for="o in yesNoOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Please rate your credit">
          <el-select v-model="form.creditRating" clearable placeholder="Please rate your credit" style="width:100%">
            <el-option v-for="o in creditRatingOptions" :key="o" :label="o" :value="o" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Desired Vehicle</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Make"><el-input v-model="form.desiredMake" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Model"><el-input v-model="form.desiredModel" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Year"><el-input-number v-model="form.desiredYear" style="width:100%" :controls="false" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="VIN"><el-input v-model="form.desiredVin" maxlength="17" /></el-form-item>
      </el-col>
    </el-row>

    <h3 class="section-title">Trade In</h3>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-form-item label="Trade-In Make"><el-input v-model="form.tradeMake" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Trade-In Model"><el-input v-model="form.tradeModel" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Trade-In Year"><el-input-number v-model="form.tradeYear" style="width:100%" :controls="false" /></el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="Trade-In Kilometers"><el-input-number v-model="form.tradeKm" style="width:100%" :controls="false" :min="0" /></el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="Questions or Comments">
          <el-input v-model="form.message" type="textarea" rows="4" />
        </el-form-item>
      </el-col>
    </el-row>

    <p class="disclaimer"><strong>Disclaimer:</strong> By submitting this application, you authorize us to run your credit report.</p>

    <el-button type="primary" native-type="submit" :loading="loading">Get Approved</el-button>
  </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/api/request'
import { phoneRules } from '@/utils/phoneValidator'
import { emailRules } from '@/utils/emailValidator'

const loading = ref(false)
const formRef = ref(null)

const props = defineProps({
  initialMake: { type: String, default: '' },
  initialModel: { type: String, default: '' },
  initialYear: { type: [Number, String], default: null },
  initialVin: { type: String, default: '' },
  initialMessage: { type: String, default: '' }
})

const form = reactive({
  salutation: '', gender: '', firstName: '', lastName: '', phone: '', email: '',
  maritalStatus: '', dateOfBirth: '', sin: '',
  address: '', city: '', province: '', postalCode: '', addressDurationYears: null, addressDurationMonths: null,
  homeStatus: '', monthlyHousingPayment: null,
  employmentType: '', employerName: '', occupation: '', employmentAddress: '', employmentCity: '',
  employmentProvince: '', employmentPostalCode: '', employmentPhone: '', grossIncome: null,
  employmentDurationYears: null, employmentDurationMonths: null,
  previousEmployer: '', previousEmployerPhone: '', previousEmploymentDurationYears: null, previousEmploymentDurationMonths: null,
  previousBankruptcy: '', previousRepossession: '', cosignerAvailable: '', creditRating: '',
  desiredMake: props.initialMake, desiredModel: props.initialModel,
  desiredYear: props.initialYear ? Number(props.initialYear) : null, desiredVin: props.initialVin,
  tradeMake: '', tradeModel: '', tradeYear: null, tradeKm: null,
  message: props.initialMessage
})

const rules = {
  firstName: [{ required: true, message: 'First Name is required', trigger: 'blur' }],
  lastName: [{ required: true, message: 'Last Name is required', trigger: 'blur' }],
  phone: phoneRules,
  email: emailRules,
  maritalStatus: [{ required: true, message: 'Marital Status is required', trigger: 'change' }],
  dateOfBirth: [{ required: true, message: 'Birth Date is required', trigger: 'change' }],
  address: [{ required: true, message: 'Address is required', trigger: 'blur' }],
  city: [{ required: true, message: 'City is required', trigger: 'blur' }],
  province: [{ required: true, message: 'Province is required', trigger: 'change' }],
  postalCode: [{ required: true, message: 'Postal Code is required', trigger: 'blur' }],
  addressDurationYears: [{ required: true, message: 'Duration Year is required', trigger: 'blur' }],
  addressDurationMonths: [{ required: true, message: 'Duration Month is required', trigger: 'blur' }],
  homeStatus: [{ required: true, message: 'Home Status is required', trigger: 'change' }],
  monthlyHousingPayment: [{ required: true, message: 'Monthly Payment is required', trigger: 'blur' }],
  employmentType: [{ required: true, message: 'Employment Type is required', trigger: 'change' }],
  employerName: [{ required: true, message: 'Employer is required', trigger: 'blur' }],
  occupation: [{ required: true, message: 'Occupation is required', trigger: 'blur' }],
  employmentAddress: [{ required: true, message: 'Employment Address is required', trigger: 'blur' }],
  employmentCity: [{ required: true, message: 'City is required', trigger: 'blur' }],
  employmentProvince: [{ required: true, message: 'Province is required', trigger: 'change' }],
  employmentPostalCode: [{ required: true, message: 'Postal Code is required', trigger: 'blur' }],
  employmentPhone: [{ required: true, message: 'Phone is required', trigger: 'blur' }],
  grossIncome: [{ required: true, message: 'Gross Income is required', trigger: 'blur' }],
  employmentDurationYears: [{ required: true, message: 'Duration Year is required', trigger: 'blur' }],
  employmentDurationMonths: [{ required: true, message: 'Duration Month is required', trigger: 'blur' }]
}

const salutationOptions = ['Mr.', 'Mrs.', 'Ms.', 'Miss', 'Dr.']
const genderOptions = ['Male', 'Female', 'Other', 'Prefer not to say']
const maritalStatusOptions = ['Single', 'Married', 'Common-Law', 'Divorced', 'Widowed', 'Separated']
const provinceOptions = [
  'Alberta', 'British Columbia', 'Manitoba', 'New Brunswick', 'Newfoundland and Labrador',
  'Northwest Territories', 'Nova Scotia', 'Nunavut', 'Ontario', 'Prince Edward Island',
  'Quebec', 'Saskatchewan', 'Yukon'
]
const homeStatusOptions = ['Rent', 'Own', 'Live with Family', 'Other']
const employmentTypeOptions = ['Full-Time', 'Part-Time', 'Contract', 'Self-Employed', 'Retired', 'Unemployed', 'Student']
const yesNoOptions = ['Yes', 'No']
const creditRatingOptions = ['Excellent Credit', 'Good Credit', 'Fair Credit', 'Poor Credit', 'No Credit History']

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    // 后端 /api/leads/loan-application 接口
    await request.post('/leads/loan-application', form)
    ElMessage.success('Application submitted! Our finance team will contact you soon.')
    formRef.value.clearValidate()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.section-title { font-family: 'Archivo', sans-serif; font-size: 18px; font-weight: 600; margin: 4px 0 16px; }
.section-title:not(:first-child) { margin-top: 8px; }
.disclaimer { font-size: 13px; color: var(--s-muted); margin: 16px 0; }
</style>
