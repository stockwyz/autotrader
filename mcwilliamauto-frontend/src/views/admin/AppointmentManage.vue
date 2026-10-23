<template>
  <div class="admin-page">
    <h2>Appointments</h2>
    <div class="toolbar">
      <el-select v-model="statusFilter" placeholder="All statuses" clearable style="width:180px" @change="onFilter">
        <el-option v-for="s in STATUS_OPTIONS" :key="s.value" :label="s.label" :value="s.value" />
      </el-select>
      <el-date-picker v-model="dateFilter" type="date" value-format="YYYY-MM-DD" placeholder="Filter by date" clearable
                      style="width:200px" @change="onFilter" />
    </div>

    <el-table :data="list" v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="apptNo" label="Appt No." width="170" />
      <el-table-column label="Date" width="120">
        <template #default="{ row }">{{ row.apptDate }}</template>
      </el-table-column>
      <el-table-column label="Time" width="110">
        <template #default="{ row }">{{ fmtTime(row.apptTime) }}</template>
      </el-table-column>
      <el-table-column label="Type" width="130">
        <template #default="{ row }">{{ TYPE_LABELS[row.serviceType] || row.serviceType }}</template>
      </el-table-column>
      <el-table-column prop="customerName" label="Customer" width="160" />
      <el-table-column prop="phone" label="Phone" width="140" />
      <el-table-column prop="detail" label="Detail" min-width="220" :show-overflow-tooltip="detailTooltip" />
      <el-table-column label="Status" width="160">
        <template #default="{ row }">
          <el-select :model-value="row.status" size="small" @change="(v) => onStatus(row, v)">
            <el-option v-for="s in STATUS_OPTIONS" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
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

    <el-dialog v-model="viewVisible" title="Appointment Details" width="560px">
      <el-descriptions v-if="current" :column="1" border>
        <el-descriptions-item label="Appointment No.">{{ current.apptNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="Type">{{ TYPE_LABELS[current.serviceType] || current.serviceType }}</el-descriptions-item>
        <el-descriptions-item label="Date">{{ current.apptDate }}</el-descriptions-item>
        <el-descriptions-item label="Time">{{ fmtTime(current.apptTime) }}</el-descriptions-item>
        <el-descriptions-item label="Customer">{{ current.customerName }}</el-descriptions-item>
        <el-descriptions-item label="Phone">{{ current.phone }}</el-descriptions-item>
        <el-descriptions-item label="Email">{{ current.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="Vehicle">{{ current.vehicleInfo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="Detail"><span style="white-space:pre-wrap">{{ current.detail || '-' }}</span></el-descriptions-item>
        <el-descriptions-item label="Status">{{ statusLabel(current.status) }}</el-descriptions-item>
        <el-descriptions-item label="Customer reply">
          {{ current.customerResponse === 1 ? 'Confirmed by customer' : current.customerResponse === 2 ? 'Cancelled by customer' : 'No reply yet' }}
          <span v-if="current.respondedAt"> ({{ current.respondedAt }})</span>
        </el-descriptions-item>
        <el-descriptions-item label="Submitted At">{{ current.createdAt }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminGetAppointments, adminUpdateAppointmentStatus, adminDeleteAppointment } from '@/api/appointment'

const STATUS_OPTIONS = [
  { value: 0, label: 'New' },
  { value: 1, label: 'Confirmed' },
  { value: 2, label: 'Completed' },
  { value: 3, label: 'Cancelled' }
]
const TYPE_LABELS = { REPAIR: 'Repair', MAINTENANCE: 'Maintenance', PHR: 'PHR' }

const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const statusFilter = ref(null)
const dateFilter = ref(null)
const viewVisible = ref(false)
const current = ref(null)

/** Detail 列只显示一行，超出用 … 表示；鼠标停在上面时用浮层显示完整内容 */
const detailTooltip = { placement: 'top', popperClass: 'appt-detail-tip', showAfter: 150 }

function fmtTime(t) {
  if (!t) return ''
  const [hStr, mStr] = t.split(':')
  let h = parseInt(hStr, 10)
  const ap = h >= 12 ? 'PM' : 'AM'
  h = h % 12 || 12
  return `${h}:${mStr} ${ap}`
}
const statusLabel = (s) => STATUS_OPTIONS.find((o) => o.value === s)?.label || 'New'

async function fetchData() {
  loading.value = true
  try {
    const res = await adminGetAppointments({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      status: statusFilter.value ?? undefined,
      date: dateFilter.value || undefined
    })
    list.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}
function onFilter() {
  pageNum.value = 1
  fetchData()
}
/** 管理员人工二次确认：选了新状态后先弹 Yes / No，点 Yes 才真正修改，并自动给客户发邮件 */
async function onStatus(row, value) {
  const label = statusLabel(value)
  const mails = value !== 0
  const emailNote = !mails
    ? ''
    : (row.email
        ? ` An email will be sent to ${row.email}.`
        : ' This customer left no email, so no email will be sent.')
  try {
    await ElMessageBox.confirm(
      `Change the status of ${row.customerName}'s appointment (${row.apptDate} ${fmtTime(row.apptTime)}) to "${label}"?${emailNote}`,
      'Confirm status change',
      { confirmButtonText: 'Yes', cancelButtonText: 'No', type: value === 3 ? 'warning' : 'info' }
    )
  } catch (e) {
    return // 点了 No：什么都不改，下拉框保持原状态
  }
  await adminUpdateAppointmentStatus(row.id, value)
  ElMessage.success(value === 3 ? 'Cancelled, the time slot is open again' : 'Status updated')
  fetchData()
}
function onView(row) {
  current.value = row
  viewVisible.value = true
}
async function onDelete(row) {
  await ElMessageBox.confirm('Delete this appointment? This cannot be undone.', 'Confirm', { type: 'warning' })
  await adminDeleteAppointment(row.id)
  ElMessage.success('Deleted')
  fetchData()
}

onMounted(fetchData)
</script>

<style>
/* 浮层是挂到 body 上的，所以这里不能 scoped。保留换行、限制宽度，内容多时可以完整阅读 */
.appt-detail-tip { max-width: 460px; max-height: 50vh; overflow-y: auto; white-space: pre-wrap; word-break: break-word; line-height: 1.55; }
</style>
<style scoped>
.admin-page { padding: 24px; }
.toolbar { display: flex; gap: 12px; margin-bottom: 16px; }
</style>
