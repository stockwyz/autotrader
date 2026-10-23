<template>
  <div class="appt">
    <!-- 提交成功 -->
    <div v-if="done" class="container appt-wrap done-wrap">
      <div class="done-card">
        <div class="done-icon">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7.5" /></svg>
        </div>
        <div class="eyebrow-label">Request received</div>
        <h1 class="done-title">Thanks, we have your request</h1>
        <p class="done-text">We will confirm your appointment by phone or email within one business day. If you need to change anything, call us{{ dealerInfo?.phone ? ` at ${dealerInfo.phone}` : '' }}.</p>
        <div class="sum-box">
          <div v-if="done.apptNo" class="sum-row no-row"><span>Appointment No.</span><b>{{ done.apptNo }}</b></div>
          <div class="sum-row"><span>Service</span><b>{{ typeName(done.serviceType) }}</b></div>
          <div class="sum-row"><span>Date</span><b>{{ dateLabelShort(done.apptDate) }}</b></div>
          <div class="sum-row"><span>Time</span><b>{{ fmtTime(done.apptTime) }}</b></div>
          <div v-if="done.detail" class="sum-row"><span>Detail</span><b class="detail-val">{{ done.detail }}</b></div>
          <div v-if="dealerInfo?.address" class="sum-row"><span>Location</span><b>{{ dealerInfo.address }}</b></div>
        </div>
        <div class="done-actions">
          <router-link to="/" class="btn-primary">Back to home</router-link>
          <button type="button" class="btn-ghost" @click="reset">Book another</button>
        </div>
      </div>
    </div>

    <!-- 预约表单 -->
    <template v-else>
      <div class="container appt-wrap head">
        <div class="eyebrow-label">Service Department</div>
        <h1 class="page-title">Book a service appointment</h1>
        <p class="page-sub">Choose the type of service, pick an open day and time from our business hours, and tell us what you need. We will confirm by phone or email.</p>
      </div>

      <div class="container appt-wrap layout">
        <div class="main-col">
          <!-- 1 Type -->
          <section class="card">
            <div class="step-head"><span class="step-no">1</span><h2>Type</h2></div>
            <div class="type-grid">
              <button v-for="t in TYPES" :key="t.id" type="button" class="type-card" :class="{ on: form.serviceType === t.id }"
                      :aria-pressed="form.serviceType === t.id" @click="form.serviceType = t.id">
                <span class="type-icon">
                  <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path :d="t.path" /></svg>
                </span>
                <span class="type-name">{{ t.name }}</span>
                <span class="type-desc">{{ t.desc }}</span>
              </button>
            </div>
          </section>

          <!-- 2 Date & time -->
          <section class="card">
            <div class="step-head"><span class="step-no">2</span><h2>Date &amp; time</h2></div>
            <div class="dt-row">
              <div class="cal">
                <div class="cal-nav">
                  <button type="button" class="nav-btn" aria-label="Previous month" :disabled="!canPrev" @click="shiftMonth(-1)">‹</button>
                  <div class="cal-title">{{ monthTitle }}</div>
                  <button type="button" class="nav-btn" aria-label="Next month" :disabled="!canNext" @click="shiftMonth(1)">›</button>
                </div>
                <div class="cal-grid">
                  <div v-for="w in WEEKDAYS" :key="w" class="cal-wd">{{ w }}</div>
                  <template v-for="(c, i) in calCells" :key="i">
                    <div v-if="!c" class="cal-blank"></div>
                    <button v-else type="button" class="cal-day" :class="{ sel: c.date === selectedDate }"
                            :disabled="c.disabled" @click="pickDate(c.date)">{{ c.day }}</button>
                  </template>
                </div>
                <div class="legend">
                  <span><i class="lg sel"></i>Selected</span>
                  <span><i class="lg open"></i>Open</span>
                  <span><i class="lg off"></i>Closed / past</span>
                </div>
              </div>

              <div class="slots-col">
                <template v-if="selectedDate">
                  <div class="slots-date">{{ dateLabelLong(selectedDate) }}</div>
                  <div class="slots-hours">{{ hoursText }}</div>
                  <div v-if="loadingSlots" class="slots-empty">Loading available times…</div>
                  <div v-else-if="!slots.length" class="slots-empty">No times available on this day.</div>
                  <div v-else class="slot-grid">
                    <button v-for="s in slots" :key="s.time" type="button" class="slot" :class="{ sel: s.time === selectedTime }"
                            :disabled="!s.available" @click="selectedTime = s.time">{{ fmtTime(s.time) }}</button>
                  </div>
                  <div class="slots-note">Greyed-out times are already booked. Each slot is 30 minutes; the shop will confirm the time needed for your service.</div>
                </template>
                <div v-else class="slots-empty">Pick a date to see available times.</div>
              </div>
            </div>
          </section>

          <!-- 3 Detail -->
          <section class="card">
            <div class="step-head"><span class="step-no">3</span><h2>Detail</h2></div>
            <label class="lbl" for="ap-detail">Tell us what you need</label>
            <textarea id="ap-detail" v-model="form.detail" class="field area" rows="5" maxlength="2000"
                      placeholder="Describe the issue or the service you want, e.g. brake noise when stopping, oil change and tire rotation…"></textarea>
            <div class="field-grid">
              <div>
                <label class="lbl" for="ap-name">Full name</label>
                <input id="ap-name" v-model.trim="form.customerName" class="field" :class="{ bad: errors.customerName }" type="text" placeholder="Your name" />
                <div v-if="errors.customerName" class="err">{{ errors.customerName }}</div>
              </div>
              <div>
                <label class="lbl" for="ap-phone">Phone</label>
                <input id="ap-phone" v-model.trim="form.phone" class="field" :class="{ bad: errors.phone }" type="tel" placeholder="(204) 000-0000" />
                <div v-if="errors.phone" class="err">{{ errors.phone }}</div>
              </div>
              <div>
                <label class="lbl" for="ap-email">Email</label>
                <input id="ap-email" v-model.trim="form.email" class="field" :class="{ bad: errors.email }" type="email" placeholder="you@example.com" />
                <div v-if="errors.email" class="err">{{ errors.email }}</div>
              </div>
              <div>
                <label class="lbl" for="ap-veh">Vehicle (optional)</label>
                <input id="ap-veh" v-model.trim="form.vehicleInfo" class="field" type="text" maxlength="150" placeholder="Year, make, model" />
              </div>
            </div>
          </section>

          <!-- 表单底部再放一个提交按钮，填完 Detail 不用再滚回右上角 -->
          <div class="bottom-submit">
            <button type="button" class="btn-primary" :disabled="submitting" @click="onSubmit">
              {{ submitting ? 'Sending…' : 'Request appointment' }}
            </button>
          </div>
        </div>

        <aside class="side-col">
          <div class="card side-card">
            <div class="side-eyebrow">Your appointment</div>
            <div class="sum-list">
              <div class="sum-row"><span>Service</span><b>{{ typeName(form.serviceType) }}</b></div>
              <div class="sum-row"><span>Date</span><b>{{ selectedDate ? dateLabelShort(selectedDate) : '—' }}</b></div>
              <div class="sum-row"><span>Time</span><b>{{ selectedTime ? fmtTime(selectedTime) : '—' }}</b></div>
              <div v-if="dealerInfo?.address" class="sum-row"><span>Location</span><b class="addr">{{ dealerInfo.address }}</b></div>
            </div>
            <button type="button" class="btn-primary submit" :disabled="submitting" @click="onSubmit">
              {{ submitting ? 'Sending…' : 'Request appointment' }}
            </button>
            <div class="side-note">This is a request. We will confirm your time by phone or email within one business day.</div>
          </div>

          <div class="card hours-card">
            <div class="side-eyebrow">Business hours</div>
            <div class="hours-list">
              <div v-for="h in hoursRows" :key="h.day" class="hours-row" :class="{ today: h.today }">
                <span>{{ h.day }}</span><span :class="{ closed: h.closed }">{{ h.time }}</span>
              </div>
            </div>
          </div>
        </aside>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'
import { useDealerStore } from '@/store/useDealerStore'
import { getAppointmentSlots, submitAppointment } from '@/api/appointment'
import { isValidPhone } from '@/utils/phoneValidator'
import { isValidEmail } from '@/utils/emailValidator'

const dealerStore = useDealerStore()
const { info: dealerInfo, hours } = storeToRefs(dealerStore)

const TYPES = [
  { id: 'REPAIR', name: 'Repair', desc: 'Diagnosis and fixes: noises, warning lights, brakes, suspension and more.',
    path: 'M14.7 6.3a4 4 0 0 0-5.4 5.4L3 18l3 3 6.3-6.3a4 4 0 0 0 5.4-5.4l-2.5 2.5-2.4-.6-.6-2.4z' },
  { id: 'MAINTENANCE', name: 'Maintenance', desc: 'Oil change, tires, fluids, filters and seasonal service to keep it running.',
    path: 'M12 3l7 3v5c0 4.5-3 8-7 10-4-2-7-5.5-7-10V6z' },
  { id: 'PHR', name: 'PHR', desc: 'PHR service. Tell us what you need in the details below.',
    path: 'M9 4h6l1 2h3v14H5V6h3zM9 12h6M9 16h4' }
]
const WEEKDAYS = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa']
const DAY_NAMES = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
const MONTH_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
/** 最多可提前预约的天数，需与后端 app.appointment.max-advance-days 保持一致(默认60) */
const MAX_ADVANCE_DAYS = 60

const form = reactive({ serviceType: 'REPAIR', detail: '', customerName: '', phone: '', email: '', vehicleInfo: '' })
const errors = reactive({ customerName: '', phone: '', email: '' })
const selectedDate = ref(null)
const selectedTime = ref(null)
const slots = ref([])
const slotsMeta = ref(null)
const loadingSlots = ref(false)
const submitting = ref(false)
const done = ref(null)

// ---- 日期工具(全部用本地日期，避免 toISOString 带来的时区偏移) ----
const pad = (n) => String(n).padStart(2, '0')
const ymd = (y, m, d) => `${y}-${pad(m + 1)}-${pad(d)}`
const parseYmd = (s) => { const [y, m, d] = s.split('-').map(Number); return new Date(y, m - 1, d) }
const today = new Date(); today.setHours(0, 0, 0, 0)
const maxDate = new Date(today); maxDate.setDate(maxDate.getDate() + MAX_ADVANCE_DAYS)

const viewYear = ref(today.getFullYear())
const viewMonth = ref(today.getMonth())

function fmtTime(t) {
  if (!t) return ''
  const [hStr, mStr] = t.split(':')
  let h = parseInt(hStr, 10)
  const ap = h >= 12 ? 'PM' : 'AM'
  h = h % 12 || 12
  return `${h}:${mStr} ${ap}`
}
const dateLabelLong = (s) => { const d = parseYmd(s); return `${DAY_NAMES[d.getDay()]}, ${MONTH_NAMES[d.getMonth()]} ${d.getDate()}` }
const dateLabelShort = (s) => { const d = parseYmd(s); return `${DAY_NAMES[d.getDay()].slice(0, 3)}, ${MONTH_NAMES[d.getMonth()].slice(0, 3)} ${d.getDate()}` }
const typeName = (id) => TYPES.find((t) => t.id === id)?.name || id

// ---- 营业时间(来自后台 Dealer Info & Hours)：dayOfWeek 1=周一...7=周日 ----
const openDow = computed(() => {
  const set = new Set()
  ;(hours.value || []).forEach((h) => { if (h.openTime && h.closeTime) set.add(h.dayOfWeek) })
  return set
})
const isOpenDay = (d) => openDow.value.has(d.getDay() === 0 ? 7 : d.getDay())

const hoursRows = computed(() => {
  const todayDow = today.getDay() === 0 ? 7 : today.getDay()
  const map = new Map((hours.value || []).map((h) => [h.dayOfWeek, h]))
  return [1, 2, 3, 4, 5, 6, 7].map((n) => {
    const h = map.get(n)
    const open = h && h.openTime && h.closeTime
    return {
      day: DAY_NAMES[n % 7],
      time: open ? `${fmtTime(h.openTime)} – ${fmtTime(h.closeTime)}` : 'Closed',
      closed: !open,
      today: n === todayDow
    }
  })
})

const hoursText = computed(() => {
  const m = slotsMeta.value
  if (!m || !m.open || !m.openTime) return 'Closed'
  return `Open ${fmtTime(m.openTime)} – ${fmtTime(m.closeTime)}`
})

// ---- 日历 ----
const monthTitle = computed(() => `${MONTH_NAMES[viewMonth.value]} ${viewYear.value}`)
const canPrev = computed(() => viewYear.value > today.getFullYear() || viewMonth.value > today.getMonth())
const canNext = computed(() => {
  const first = new Date(viewYear.value, viewMonth.value + 1, 1)
  return first <= maxDate
})
function shiftMonth(delta) {
  const d = new Date(viewYear.value, viewMonth.value + delta, 1)
  viewYear.value = d.getFullYear()
  viewMonth.value = d.getMonth()
}
const calCells = computed(() => {
  const y = viewYear.value, m = viewMonth.value
  const lead = new Date(y, m, 1).getDay()
  const total = new Date(y, m + 1, 0).getDate()
  const cells = Array(lead).fill(null)
  for (let day = 1; day <= total; day++) {
    const d = new Date(y, m, day)
    cells.push({ day, date: ymd(y, m, day), disabled: d < today || d > maxDate || !isOpenDay(d) })
  }
  return cells
})

async function pickDate(date) {
  selectedDate.value = date
  selectedTime.value = null
  await loadSlots()
}
async function loadSlots() {
  if (!selectedDate.value) return
  loadingSlots.value = true
  try {
    const res = await getAppointmentSlots(selectedDate.value)
    slotsMeta.value = res
    slots.value = res.slots || []
  } catch (e) {
    slots.value = []
    slotsMeta.value = null
  } finally {
    loadingSlots.value = false
  }
}

// ---- 提交 ----
function validate() {
  errors.customerName = form.customerName ? '' : 'Full name is required'
  errors.phone = !form.phone ? 'Phone is required' : (isValidPhone(form.phone) ? '' : 'Please enter a valid phone number')
  errors.email = form.email && !isValidEmail(form.email) ? 'Please enter a valid email address' : ''
  return !errors.customerName && !errors.phone && !errors.email
}
async function onSubmit() {
  if (!selectedDate.value || !selectedTime.value) {
    ElMessage.warning('Please choose a date and time')
    return
  }
  if (!validate()) {
    ElMessage.warning('Please complete the highlighted fields')
    return
  }
  submitting.value = true
  const payload = {
    serviceType: form.serviceType,
    apptDate: selectedDate.value,
    apptTime: selectedTime.value,
    detail: form.detail || null,
    customerName: form.customerName,
    phone: form.phone,
    email: form.email || null,
    vehicleInfo: form.vehicleInfo || null
  }
  try {
    const res = await submitAppointment(payload)
    done.value = { ...payload, apptNo: res?.apptNo }
    window.scrollTo({ top: 0, behavior: 'smooth' })
  } catch (e) {
    // 错误提示由 request 拦截器统一弹出；时间段可能刚被别人订走，刷新一下可选时间
    selectedTime.value = null
    await loadSlots()
  } finally {
    submitting.value = false
  }
}
function reset() {
  done.value = null
  Object.assign(form, { serviceType: 'REPAIR', detail: '', customerName: '', phone: '', email: '', vehicleInfo: '' })
  selectedTime.value = null
  autoPickFirstOpenDay()
}

function autoPickFirstOpenDay() {
  const d = new Date(today)
  for (let i = 0; i <= MAX_ADVANCE_DAYS; i++) {
    if (isOpenDay(d)) {
      viewYear.value = d.getFullYear()
      viewMonth.value = d.getMonth()
      pickDate(ymd(d.getFullYear(), d.getMonth(), d.getDate()))
      return
    }
    d.setDate(d.getDate() + 1)
  }
}

onMounted(async () => {
  await dealerStore.ensureLoaded()
  autoPickFirstOpenDay()
})
</script>

<style scoped lang="scss">
.appt { padding-bottom: 24px; }
.appt-wrap { max-width: 1680px; padding-left: 24px; padding-right: 24px; }
.head { padding-top: 44px; }
.page-title { font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 48px; line-height: 1.05; letter-spacing: -.02em; margin: 0 0 12px; color: var(--s-ink); }
.page-sub { margin: 0; font-size: 17px; color: var(--s-text2); max-width: 720px; line-height: 1.55; }

.layout { display: flex; flex-wrap: wrap; gap: 24px; align-items: flex-start; padding-top: 28px; }
.main-col { flex: 999 1 640px; min-width: 0; display: flex; flex-direction: column; gap: 24px; }
.side-col { flex: 1 1 380px; max-width: 440px; min-width: 300px; display: flex; flex-direction: column; gap: 24px; }

.card { background: #fff; border: 1px solid var(--s-line); border-radius: 16px; padding: 32px; }
.step-head { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.step-head h2 { margin: 0; font-family: 'Archivo', sans-serif; font-weight: 800; font-size: 24px; color: var(--s-ink); }
.step-no { width: 30px; height: 30px; border-radius: 99px; background: var(--s-primary); color: #fff; font: 800 14px 'Archivo', sans-serif; display: flex; align-items: center; justify-content: center; }

/* type */
.type-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 16px; }
.type-card { text-align: left; display: flex; flex-direction: column; gap: 10px; padding: 22px; border-radius: 12px; cursor: pointer; border: 2px solid var(--s-line); background: #fff; transition: border-color .15s, background .15s; font-family: inherit; }
.type-card:hover { border-color: var(--s-primary); }
.type-card.on { border-color: var(--s-primary); background: var(--s-soft); }
.type-icon { width: 48px; height: 48px; border-radius: 10px; display: flex; align-items: center; justify-content: center; background: var(--s-soft); color: var(--s-primary); }
.type-card.on .type-icon { background: var(--s-primary); color: #fff; }
.type-name { font-family: 'Archivo', sans-serif; font-weight: 800; font-size: 20px; color: var(--s-ink); }
.type-desc { font-size: 14px; line-height: 1.45; color: var(--s-muted); font-weight: 500; }

/* date & time */
.dt-row { display: flex; flex-wrap: wrap; gap: 36px; }
.cal { flex: 0 0 360px; max-width: 100%; }
.cal-nav { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.cal-title { font-family: 'Archivo', sans-serif; font-weight: 800; font-size: 18px; color: var(--s-ink); }
.nav-btn { width: 36px; height: 36px; border-radius: 8px; border: 1px solid var(--s-line); background: #fff; color: var(--s-ink); cursor: pointer; font-size: 18px; }
.nav-btn:disabled { opacity: .35; cursor: default; }
.cal-grid { display: grid; grid-template-columns: repeat(7, minmax(0, 1fr)); gap: 6px; }
.cal-wd { text-align: center; font: 800 11px 'Archivo', sans-serif; letter-spacing: .1em; color: var(--s-muted); padding: 4px 0; text-transform: uppercase; }
.cal-day { height: 44px; border-radius: 8px; font: 700 15px 'Figtree', sans-serif; border: 1px solid var(--s-line2, var(--s-line)); background: #fff; color: var(--s-ink); cursor: pointer; transition: border-color .12s; }
.cal-day:hover:not(:disabled) { border-color: var(--s-primary); }
.cal-day.sel { background: var(--s-primary); border-color: var(--s-primary); color: #fff; }
.cal-day:disabled { border-color: transparent; background: var(--s-strip); color: var(--s-faint); cursor: default; }
.legend { display: flex; gap: 18px; margin-top: 16px; font-size: 13px; color: var(--s-muted); font-weight: 500; }
.legend span { display: flex; align-items: center; gap: 6px; }
.lg { width: 12px; height: 12px; border-radius: 4px; display: inline-block; }
.lg.sel { background: var(--s-primary); }
.lg.open { border: 1px solid var(--s-line2, var(--s-line)); background: #fff; }
.lg.off { background: var(--s-strip); }

.slots-col { flex: 1 1 360px; min-width: 0; }
.slots-date { font-family: 'Archivo', sans-serif; font-weight: 800; font-size: 18px; color: var(--s-ink); }
.slots-hours { font-size: 14px; color: var(--s-muted); font-weight: 500; margin: 4px 0 16px; }
.slots-empty { color: var(--s-muted); font-size: 15px; padding: 12px 0; }
.slot-grid { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 10px; }
.slot { height: 44px; border-radius: 8px; font: 700 14px 'Figtree', sans-serif; border: 1px solid var(--s-line2, var(--s-line)); background: #fff; color: var(--s-ink); cursor: pointer; transition: border-color .12s; }
.slot:hover:not(:disabled) { border-color: var(--s-primary); }
.slot.sel { background: var(--s-primary); border-color: var(--s-primary); color: #fff; }
.slot:disabled { border-color: transparent; background: var(--s-strip); color: var(--s-faint); text-decoration: line-through; cursor: default; }
.slots-note { margin-top: 14px; font-size: 13px; color: var(--s-muted); font-weight: 500; line-height: 1.5; }

/* detail */
.lbl { display: block; font: 800 12px 'Archivo', sans-serif; letter-spacing: .14em; text-transform: uppercase; color: var(--s-muted); margin-bottom: 8px; }
.field { width: 100%; box-sizing: border-box; height: 48px; border: 1px solid var(--s-line2, var(--s-line)); border-radius: 10px; padding: 0 14px; font: 500 16px 'Figtree', sans-serif; color: var(--s-ink); background: #fff; outline: none; }
.field:focus { border-color: var(--s-primary); box-shadow: 0 0 0 3px rgba(var(--s-primary-rgb), .15); }
.field.bad { border-color: #D14343; }
.area { height: auto; padding: 14px 16px; line-height: 1.5; resize: vertical; }
.field-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 16px; margin-top: 20px; }
.err { margin-top: 6px; font-size: 13px; color: #D14343; font-weight: 600; }

/* side */
.side-eyebrow { font: 800 12px 'Archivo', sans-serif; letter-spacing: .14em; text-transform: uppercase; color: var(--s-primary); margin-bottom: 14px; }
.side-card { padding: 28px; }
.sum-list, .sum-box { display: flex; flex-direction: column; gap: 14px; }
.sum-row { display: flex; justify-content: space-between; gap: 16px; font-size: 15px; color: var(--s-ink); }
.sum-row > span { color: var(--s-muted); font-weight: 500; }
.sum-row b { text-align: right; }
.addr { max-width: 220px; }
.btn-primary { display: inline-flex; align-items: center; justify-content: center; height: 54px; padding: 0 28px; border: 0; border-radius: 8px; background: var(--s-primary); color: #fff; font: 800 14px 'Archivo', sans-serif; letter-spacing: .1em; text-transform: uppercase; cursor: pointer; transition: background .15s; }
.btn-primary:hover { background: var(--s-primary-dark, var(--s-primary)); }
.btn-primary:disabled { opacity: .6; cursor: default; }
.btn-ghost { display: inline-flex; align-items: center; height: 54px; padding: 0 28px; border-radius: 8px; border: 1px solid var(--s-line2, var(--s-line)); background: #fff; color: var(--s-ink); font: 800 14px 'Archivo', sans-serif; letter-spacing: .1em; text-transform: uppercase; cursor: pointer; }
.submit { width: 100%; margin-top: 24px; }
.bottom-submit { display: flex; justify-content: center; padding-top: 4px; }
.bottom-submit .btn-primary { min-width: 260px; }
.side-note { margin-top: 12px; font-size: 13px; line-height: 1.5; color: var(--s-muted); font-weight: 500; }
.hours-card { background: var(--s-soft); padding: 28px; }
.hours-list { display: flex; flex-direction: column; gap: 10px; font-size: 15px; }
.hours-row { display: flex; justify-content: space-between; gap: 12px; font-weight: 600; color: var(--s-ink); }
.hours-row.today { font-weight: 800; }
.hours-row .closed { color: var(--s-muted); }

/* done */
.done-wrap { max-width: 760px; padding-top: 72px; }
.done-card { background: #fff; border: 1px solid var(--s-line); border-radius: 16px; padding: 48px 44px; text-align: center; }
.done-icon { width: 72px; height: 72px; border-radius: 99px; background: var(--s-soft); color: var(--s-primary); margin: 0 auto 24px; display: flex; align-items: center; justify-content: center; }
.done-title { font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 40px; line-height: 1.1; letter-spacing: -.02em; margin: 0 0 14px; color: var(--s-ink); }
.done-text { margin: 0 auto 32px; font-size: 17px; line-height: 1.55; color: var(--s-text2); max-width: 520px; }
.sum-box { text-align: left; background: var(--s-bg, #F2F6FA); border: 1px solid var(--s-line); border-radius: 12px; padding: 24px 28px; font-size: 16px; }
.detail-val { max-width: 340px; word-break: break-word; }
.no-row b { font-family: 'Archivo', sans-serif; font-weight: 800; letter-spacing: .06em; color: var(--s-primary); }
.done-actions { display: flex; gap: 14px; justify-content: center; margin-top: 32px; flex-wrap: wrap; }

@media (max-width: 760px) {
  .page-title { font-size: 34px; }
  .card { padding: 22px; }
  .type-grid { grid-template-columns: 1fr; }
  .cal { flex-basis: 100%; }
  .slot-grid { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .done-card { padding: 32px 22px; }
}
</style>
