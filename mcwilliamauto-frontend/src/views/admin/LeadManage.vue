<template>
  <div class="admin-page">
    <h2>Leads</h2>
    <el-tabs v-model="activeType" @tab-change="fetchData">
      <el-tab-pane label="Inquiry" name="inquiry" />
      <el-tab-pane label="Test Drive" name="test-drive" />
      <el-tab-pane label="Trade-In" name="trade-in" />
      <el-tab-pane label="Car Finder" name="car-finder" />
      <el-tab-pane label="Loan Application" name="loan-application" />
      <el-tab-pane label="Contact" name="contact" />
    </el-tabs>

    <el-table :data="list" v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="firstName" label="First Name" width="120" />
      <el-table-column prop="lastName" label="Last Name" width="120" />
      <el-table-column prop="email" label="Email" width="200" />
      <el-table-column prop="phone" label="Phone" width="140" />
      <el-table-column prop="message" label="Message" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="Submitted At" width="180" />
      <el-table-column label="Status" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'info' : row.status === 1 ? 'warning' : 'success'">
            {{ statusLabel(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Actions" width="160">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button size="small" @click="onView(row)">View</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">Delete</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > pageSize"
      layout="prev, pager, next"
      :total="total" :page-size="pageSize" :current-page="pageNum"
      @current-change="p => { pageNum = p; fetchData() }"
      style="margin-top:16px; justify-content:center"
    />

    <el-dialog v-model="viewVisible" title="Lead Details" width="560px">
      <el-descriptions :column="1" border>
        <el-descriptions-item v-for="f in viewFields" :key="f.key" :label="f.label">{{ f.value }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminGetLeads } from '@/api/lead'
import { leadDeleteApi } from '@/api/admin'

const activeType = ref('inquiry')
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const viewVisible = ref(false)
const viewFields = ref([])

const LABEL_OVERRIDES = {
  id: 'ID', firstName: 'First Name', lastName: 'Last Name', email: 'Email', phone: 'Phone',
  message: 'Message', createdAt: 'Submitted At',
  tradeYear: 'Trade-In Year', tradeMake: 'Trade-In Make', tradeModel: 'Trade-In Model',
  tradeTrim: 'Trim', tradeKm: 'Kilometers', tradeBodyStyle: 'Body Style',
  tradeTransmission: 'Transmission', tradeDriveline: 'Driveline', tradeFuelType: 'Fuel Type',
  tradeColor: 'Colour', tradeCondition: 'Condition', tradeVin: 'VIN', conditionNote: 'Additional Information',
  preferredTime: 'Preferred Time', desiredMake: 'Desired Make', desiredModel: 'Desired Model',
  minYear: 'Min Year', maxYear: 'Max Year', budgetMin: 'Budget Min', budgetMax: 'Budget Max',
  desiredTrim: 'Trim', desiredKm: 'Kilometers', desiredBodyStyle: 'Body Style',
  desiredTransmission: 'Transmission', desiredDriveline: 'Driveline', desiredFuelType: 'Fuel Type',
  desiredColor: 'Colour', desiredCondition: 'Condition',
  vehicleId: 'Vehicle ID', sourcePage: 'Source Page',
  salutation: 'Salutation', gender: 'Gender', maritalStatus: 'Marital Status',
  dateOfBirth: 'Birth Date', sin: 'SIN', sinEnc: 'SIN',
  address: 'Address', city: 'City', province: 'Province', postalCode: 'Postal Code',
  addressDurationYears: 'Address Duration (Years)', addressDurationMonths: 'Address Duration (Months)',
  homeStatus: 'Home Status', monthlyHousingPayment: 'Monthly Housing Payment',
  employmentType: 'Employment Type', employerName: 'Employer', jobTitle: 'Occupation', occupation: 'Occupation',
  employmentAddress: 'Employment Address', employmentCity: 'Employment City', employmentProvince: 'Employment Province',
  employmentPostalCode: 'Employment Postal Code', employmentPhone: 'Employment Phone',
  grossIncome: 'Gross Income', monthlyIncomeEnc: 'Gross Income',
  employmentDurationYears: 'Employment Duration (Years)', employmentDurationMonths: 'Employment Duration (Months)',
  previousEmployer: 'Previous Employer', previousEmployerPhone: 'Previous Employer Phone',
  previousEmploymentDurationYears: 'Previous Employment (Years)', previousEmploymentDurationMonths: 'Previous Employment (Months)',
  previousBankruptcy: 'Previous Bankruptcy', previousRepossession: 'Previous Repossession',
  cosignerAvailable: 'Cosigner Available', creditRating: 'Credit Rating',
  desiredMake: 'Desired Make', desiredModel: 'Desired Model', desiredYear: 'Desired Year', desiredVin: 'Desired VIN',
  tradeMake: 'Trade-In Make', tradeModel: 'Trade-In Model', tradeYear: 'Trade-In Year', tradeKm: 'Trade-In Kilometers',
  downPayment: 'Down Payment'
}
/** camelCase转成 "Camel Case" 这种人类可读的兜底标签，覆盖不到的字段名都走这个 */
function humanize(key) {
  return LABEL_OVERRIDES[key] || key.replace(/([a-z])([A-Z])/g, '$1 $2').replace(/^./, (c) => c.toUpperCase())
}

const HIDDEN_FIELDS = new Set(['status'])

const deleteFnMap = {
  inquiry: leadDeleteApi.inquiry,
  'test-drive': leadDeleteApi.testDrive,
  'trade-in': leadDeleteApi.tradeIn,
  'car-finder': leadDeleteApi.carFinder,
  'loan-application': leadDeleteApi.loanApplication,
  contact: leadDeleteApi.contact
}

async function fetchData() {
  loading.value = true
  try {
    const res = await adminGetLeads(activeType.value, { pageNum: pageNum.value, pageSize: pageSize.value })
    list.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function onView(row) {
  viewFields.value = Object.entries(row)
    .filter(([key, value]) => !HIDDEN_FIELDS.has(key) && value !== null && value !== '' && value !== undefined)
    .map(([key, value]) => ({ key, label: humanize(key), value }))
  viewVisible.value = true
}

async function onDelete(row) {
  await ElMessageBox.confirm('Delete this record? This cannot be undone.', 'Confirm', { type: 'warning' })
  await deleteFnMap[activeType.value](row.id)
  ElMessage.success('Deleted')
  fetchData()
}

function statusLabel(status) {
  return { 0: 'Pending', 1: 'In Progress', 2: 'Completed' }[status] || 'Pending'
}

onMounted(fetchData)
</script>

<style scoped>
.admin-page { padding: 24px; }
</style>
