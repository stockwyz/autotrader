<template>
  <div class="container resp-wrap">
    <div class="resp-card">
      <div v-if="loading" class="state">Loading…</div>

      <div v-else-if="error" class="state">
        <div class="icon bad">!</div>
        <h1>This link is not valid</h1>
        <p>{{ error }}</p>
        <router-link to="/appointment" class="btn-primary">Book an appointment</router-link>
      </div>

      <template v-else-if="appt">
        <!-- 已操作完成 -->
        <div v-if="result" class="state">
          <div class="icon" :class="result === 'confirm' ? 'ok' : 'bad'">
            <svg v-if="result === 'confirm'" width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7.5" /></svg>
            <svg v-else width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" aria-hidden="true"><path d="M6 6l12 12M18 6L6 18" /></svg>
          </div>
          <div class="eyebrow-label">{{ result === 'confirm' ? 'Thank you' : 'Noted' }}</div>
          <h1>{{ result === 'confirm' ? 'You have confirmed your appointment' : 'You have cancelled your appointment' }}</h1>
          <p>We have let our team know. They will review your response and send you a final email once it is processed.
            <template v-if="result === 'cancel'"> If you change your mind, just call us or book a new time.</template></p>
          <router-link v-if="result === 'cancel'" to="/appointment" class="btn-primary">Book a new appointment</router-link>
          <router-link v-else to="/" class="btn-primary">Back to home</router-link>
        </div>

        <!-- 管理员已处理完，客户不能再操作 -->
        <div v-else-if="closed" class="state">
          <div class="icon bad">!</div>
          <h1>This appointment is already {{ appt.status === 2 ? 'completed' : 'cancelled' }}</h1>
          <p>No further action is needed. You are welcome to book a new appointment at any time.</p>
          <router-link to="/appointment" class="btn-primary">Book an appointment</router-link>
        </div>

        <!-- 待操作：先让客户再确认一次，避免误点 -->
        <div v-else>
          <div class="eyebrow-label">Appointment</div>
          <h1>{{ action === 'cancel' ? 'Cancel this appointment?' : 'Confirm this appointment?' }}</h1>
          <p class="lead">Hi {{ appt.customerName }}, please check the details below.</p>
          <div class="sum-box">
            <div v-if="appt.apptNo" class="sum-row"><span>Appointment No.</span><b>{{ appt.apptNo }}</b></div>
            <div class="sum-row"><span>Service</span><b>{{ typeName(appt.serviceType) }}</b></div>
            <div class="sum-row"><span>Date</span><b>{{ dateLong(appt.apptDate) }}</b></div>
            <div class="sum-row"><span>Time</span><b>{{ fmtTime(appt.apptTime) }}</b></div>
            <div v-if="appt.detail" class="sum-row"><span>Detail</span><b class="val">{{ appt.detail }}</b></div>
          </div>
          <p v-if="appt.customerResponse === 1 || appt.customerResponse === 2" class="prev">
            You previously replied: <b>{{ appt.customerResponse === 1 ? 'Confirmed' : 'Cancelled' }}</b>.
          </p>
          <div class="actions">
            <button v-if="action === 'cancel'" type="button" class="btn-danger" :disabled="submitting" @click="submit('cancel')">
              {{ submitting ? 'Sending…' : 'Yes, cancel appointment' }}
            </button>
            <button v-else type="button" class="btn-primary" :disabled="submitting" @click="submit('confirm')">
              {{ submitting ? 'Sending…' : 'Yes, confirm appointment' }}
            </button>
            <button type="button" class="btn-ghost" :disabled="submitting" @click="switchAction">
              {{ action === 'cancel' ? 'No, I will keep it' : 'I need to cancel instead' }}
            </button>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getAppointmentByToken, respondAppointment } from '@/api/appointment'

const route = useRoute()
const token = route.params.token
const action = ref(route.query.action === 'cancel' ? 'cancel' : 'confirm')

const loading = ref(true)
const error = ref('')
const appt = ref(null)
const result = ref(null)
const submitting = ref(false)

const closed = computed(() => appt.value && (appt.value.status === 2 || appt.value.status === 3))

const TYPES = { REPAIR: 'Repair', MAINTENANCE: 'Maintenance', PHR: 'PHR' }
const typeName = (t) => TYPES[t] || t
const DAY = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
const MON = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
function dateLong(s) {
  if (!s) return ''
  const [y, m, d] = s.split('-').map(Number)
  const dt = new Date(y, m - 1, d)
  return `${DAY[dt.getDay()]}, ${MON[m - 1]} ${d}, ${y}`
}
function fmtTime(t) {
  if (!t) return ''
  const [hStr, mStr] = t.split(':')
  let h = parseInt(hStr, 10)
  const ap = h >= 12 ? 'PM' : 'AM'
  h = h % 12 || 12
  return `${h}:${mStr} ${ap}`
}

function switchAction() {
  action.value = action.value === 'cancel' ? 'confirm' : 'cancel'
  // "No, I will keep it" 等同于什么都不做，回到首页；"我要取消"则切到取消确认
  if (action.value === 'confirm' && route.query.action === 'cancel') {
    window.location.href = '/'
  }
}

async function submit(act) {
  submitting.value = true
  try {
    await respondAppointment(token, act)
    result.value = act
  } catch (e) {
    // 错误提示由 request 拦截器弹出(如：已被管理员处理/日期已过)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  try {
    appt.value = await getAppointmentByToken(token)
  } catch (e) {
    error.value = 'This link is invalid or has expired. Please contact us if you need help.'
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.resp-wrap { max-width: 680px; padding: 64px 24px 24px; }
.resp-card { background: #fff; border: 1px solid var(--s-line); border-radius: 16px; padding: 44px 40px; text-align: center; }
.resp-card h1 { font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 32px; line-height: 1.15; letter-spacing: -.02em; margin: 0 0 14px; color: var(--s-ink); }
.resp-card p { margin: 0 auto 26px; font-size: 16px; line-height: 1.6; color: var(--s-text2); max-width: 480px; }
.lead { margin-bottom: 22px !important; }
.prev { margin-top: 18px !important; font-size: 14px !important; color: var(--s-muted) !important; }
.state { padding: 8px 0; }
.icon { width: 68px; height: 68px; border-radius: 99px; margin: 0 auto 22px; display: flex; align-items: center; justify-content: center; font: 900 30px 'Archivo', sans-serif; }
.icon.ok { background: var(--s-soft); color: var(--s-primary); }
.icon.bad { background: #FDECEC; color: #C23B3B; }
.sum-box { text-align: left; background: var(--s-bg, #F2F6FA); border: 1px solid var(--s-line); border-radius: 12px; padding: 20px 24px; display: flex; flex-direction: column; gap: 12px; }
.sum-row { display: flex; justify-content: space-between; gap: 16px; font-size: 15px; color: var(--s-ink); }
.sum-row > span { color: var(--s-muted); font-weight: 500; }
.sum-row b { text-align: right; }
.val { max-width: 300px; word-break: break-word; }
.actions { display: flex; gap: 12px; justify-content: center; flex-wrap: wrap; margin-top: 26px; }
.btn-primary, .btn-danger, .btn-ghost { display: inline-flex; align-items: center; justify-content: center; height: 52px; padding: 0 26px; border-radius: 8px; font: 800 13px 'Archivo', sans-serif; letter-spacing: .08em; text-transform: uppercase; cursor: pointer; text-decoration: none; }
.btn-primary { border: 0; background: var(--s-primary); color: #fff; }
.btn-danger { border: 0; background: #C23B3B; color: #fff; }
.btn-ghost { border: 1px solid var(--s-line2, var(--s-line)); background: #fff; color: var(--s-ink); }
button:disabled { opacity: .6; cursor: default; }
@media (max-width: 640px) { .resp-card { padding: 32px 22px; } .resp-card h1 { font-size: 26px; } }
</style>
