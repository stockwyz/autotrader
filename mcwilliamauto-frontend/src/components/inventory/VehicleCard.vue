<template>
  <!-- ============ List 横排模式 ============ -->
  <div v-if="mode === 'list'" class="vehicle-card list-mode">
    <div class="list-photo-col">
      <div class="photo-wrap" @click="goDetail">
        <div class="photo" :class="{ 'is-sold-mask': vehicle.isSold }" :style="{ background: gradientFor(vehicle.id) }">
          <img v-if="vehicle.mainThumbUrl" :src="vehicle.mainThumbUrl" :alt="vehicle.title" class="real-img" />
          <svg v-else viewBox="0 0 200 100" class="car-illustration">
            <path d="M20 65 L36 41 Q46 31 66 31 L118 31 Q136 31 148 46 L175 65 L175 78 L20 78 Z" fill="var(--s-faint)" opacity="0.85"/>
            <circle cx="55" cy="80" r="12" fill="var(--s-faint)"/>
            <circle cx="145" cy="80" r="12" fill="var(--s-faint)"/>
          </svg>
          <VehicleBadgeOverlay :vehicle="vehicle" />
        </div>
        <div class="dealer-strip">
          <div class="dealer-left">
            <div class="dealer-addr">{{ dealerInfo?.address || '155 McPhillips St, Winnipeg' }}</div>
            <div class="dealer-site">{{ dealerInfo?.websiteUrl || 'mcwilliamauto.ca' }}</div>
          </div>
          <div class="dealer-phone">{{ dealerInfo?.phone || '204-560-1234' }}</div>
        </div>
      </div>
      <label class="compare-check list-compare">
        <input type="checkbox" :checked="compareStore.isSelected(vehicle.id)" @change="compareStore.toggle(vehicle.id)" />
        Add to Compare
      </label>
    </div>

    <div class="list-info-col" @click="goDetail">
      <h3 class="title">{{ vehicle.year }} {{ vehicle.makeName }} {{ vehicle.modelName }}</h3>
      <div class="km">{{ formatKm(vehicle.odometerKm) }} Km</div>
      <div class="spec-grid">
        <div class="spec"><span class="k">Stock #:</span><span class="v">{{ vehicle.stockNo || '-' }}</span></div>
        <div class="spec"><span class="k">Body Style:</span><span class="v">{{ vehicle.bodyStyleName || '-' }}</span></div>
        <div class="spec"><span class="k">Engine:</span><span class="v">{{ vehicle.engineName || '-' }}</span></div>
        <div class="spec"><span class="k">Driveline:</span><span class="v">{{ vehicle.driveline || '-' }}</span></div>
        <div class="spec"><span class="k">Fuel Type:</span><span class="v">{{ vehicle.fuelTypeName || '-' }}</span></div>
        <div class="spec"><span class="k">Doors:</span><span class="v">{{ vehicle.doors ? vehicle.doors + ' doors' : '-' }}</span></div>
        <div class="spec"><span class="k">Exterior:</span><span class="v">{{ vehicle.exteriorColorName || '-' }}</span></div>
        <div class="spec"><span class="k">Interior:</span><span class="v">{{ vehicle.interiorColorName || '-' }}</span></div>
        <div class="spec"><span class="k">Transmission:</span><span class="v">{{ vehicle.transmissionName || '-' }}</span></div>
      </div>
    </div>

    <div class="list-action-col">
      <div class="price">${{ formatMoney(vehicle.price) }}</div>
      <div v-if="vehicle.biweeklyPayment" class="biweek">${{ vehicle.biweeklyPayment }} Bi-weekly for {{ vehicle.loanTermMonths }}mo</div>
      <div class="cta-group">
        <button class="cta primary block" @click="goDetail">View Details</button>
        <button class="cta block" @click.stop="goContactUs">Contact Us</button>
        <button class="cta block" @click.stop="goFinancing">Financing</button>
      </div>
    </div>
  </div>

  <!-- ============ Grid 卡片模式 ============ -->
  <div v-else class="vehicle-card grid-mode">
    <div class="photo-wrap" @click="goDetail">
      <div class="photo" :class="{ 'is-sold-mask': vehicle.isSold }" :style="{ background: gradientFor(vehicle.id) }">
        <img v-if="vehicle.mainThumbUrl" :src="vehicle.mainThumbUrl" :alt="vehicle.title" class="real-img" />
        <svg v-else viewBox="0 0 200 100" class="car-illustration">
          <path d="M20 65 L36 41 Q46 31 66 31 L118 31 Q136 31 148 46 L175 65 L175 78 L20 78 Z" fill="var(--s-faint)" opacity="0.85"/>
          <circle cx="55" cy="80" r="12" fill="var(--s-faint)"/>
          <circle cx="145" cy="80" r="12" fill="var(--s-faint)"/>
          <rect x="61" y="37" width="53" height="20" rx="4" fill="rgba(255,255,255,.7)"/>
        </svg>
        <VehicleBadgeOverlay :vehicle="vehicle" />
        <div v-if="vehicle.biweeklyPayment" class="payment-tag">
          ${{ vehicle.biweeklyPayment }} Bi-weekly for {{ vehicle.loanTermMonths }}mo O.A.C
        </div>
      </div>
      <div class="dealer-strip">
        <div class="dealer-left">
          <div class="dealer-addr">{{ dealerInfo?.address || '155 McPhillips St, Winnipeg' }}</div>
          <div class="dealer-site">{{ dealerInfo?.websiteUrl || 'mcwilliamauto.ca' }}</div>
        </div>
        <div class="dealer-phone">{{ dealerInfo?.phone || '204-560-1234' }}</div>
      </div>
    </div>

    <div class="cta-row cta-group">
      <button class="cta primary" @click="goDetail">View Details</button>
      <button class="cta" @click.stop="goContactUs">Contact Us</button>
      <button class="cta" @click.stop="goFinancing">Financing</button>
    </div>

    <label class="compare-row">
      <span class="compare-check">
        <input type="checkbox" :checked="compareStore.isSelected(vehicle.id)" @change="compareStore.toggle(vehicle.id)" />
        Add to Compare
      </span>
      <span class="compare-btn" :class="{ active: compareStore.isSelected(vehicle.id) }" @click="compareStore.toggle(vehicle.id)">
        {{ compareStore.isSelected(vehicle.id) ? 'Selected' : 'Compare' }}
      </span>
    </label>

    <div class="body" @click="goDetail">
      <h3 class="title">{{ vehicle.year }} {{ vehicle.makeName }} {{ vehicle.modelName }}</h3>
      <div class="km-price-row">
        <span class="km">{{ formatKm(vehicle.odometerKm) }} Km</span>
        <span class="price">${{ formatMoney(vehicle.price) }}</span>
      </div>
      <div class="spec-grid">
        <div class="spec"><span class="k">Stock #:</span><span class="v">{{ vehicle.stockNo || '-' }}</span></div>
        <div class="spec"><span class="k">Body Style:</span><span class="v">{{ vehicle.bodyStyleName || '-' }}</span></div>
        <div class="spec"><span class="k">Engine:</span><span class="v">{{ vehicle.engineName || '-' }}</span></div>
        <div class="spec"><span class="k">Driveline:</span><span class="v">{{ vehicle.driveline || '-' }}</span></div>
        <div class="spec"><span class="k">Fuel Type:</span><span class="v">{{ vehicle.fuelTypeName || '-' }}</span></div>
        <div class="spec"><span class="k">Doors:</span><span class="v">{{ vehicle.doors ? vehicle.doors + ' doors' : '-' }}</span></div>
        <div class="spec"><span class="k">Exterior:</span><span class="v">{{ vehicle.exteriorColorName || '-' }}</span></div>
        <div class="spec"><span class="k">Interior:</span><span class="v">{{ vehicle.interiorColorName || '-' }}</span></div>
        <div class="spec"><span class="k">Transmission:</span><span class="v">{{ vehicle.transmissionName || '-' }}</span></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useDealerStore } from '@/store/useDealerStore'
import { useCompareStore } from '@/store/useCompareStore'
import { storeToRefs } from 'pinia'
import VehicleBadgeOverlay from '@/components/vehicle/VehicleBadgeOverlay.vue'

const props = defineProps({
  vehicle: { type: Object, required: true },
  mode: { type: String, default: 'grid' } // 'grid' | 'list'
})
const router = useRouter()

const dealerStore = useDealerStore()
const { info: dealerInfo } = storeToRefs(dealerStore)
const compareStore = useCompareStore()

onMounted(() => dealerStore.ensureLoaded())

function goDetail() {
  router.push(`/cars/${props.vehicle.id}`)
}

/** 从车辆卡片点Contact Us/Financing跳过去时，带上这台车的信息，让对面表单能预填，减少客户重复打字 */
function buildInquiryMessage() {
  const title = `${props.vehicle.year} ${props.vehicle.makeName} ${props.vehicle.modelName}`
  return `Hello, I'm interested in vehicle ${props.vehicle.stockNo} - ${title}. I would like to learn more about this vehicle. Please contact me by phone or email. Thank you.`
}

function goContactUs() {
  router.push({ path: '/contact-us', query: { message: buildInquiryMessage() } })
}

function goFinancing() {
  router.push({
    path: '/car-loan-application',
    query: {
      make: props.vehicle.makeName,
      model: props.vehicle.modelName,
      year: props.vehicle.year,
      vin: props.vehicle.vin,
      message: buildInquiryMessage()
    }
  })
}

function formatMoney(v) { return v ? Number(v).toLocaleString() : '0' }
function formatKm(v) { return v ? Number(v).toLocaleString() : '0' }

const PH_GRADIENT = 'linear-gradient(135deg, var(--s-ph1), var(--s-ph2))'
function gradientFor(id) {
  return PH_GRADIENT
}
</script>

<style scoped lang="scss">
.vehicle-card {
  background: #fff; border-radius: 8px; overflow: hidden;
  border: 1px solid var(--s-line);
  transition: .2s;
}
.vehicle-card:hover { box-shadow: 0 14px 30px rgba(var(--s-primary-rgb), .14); border-color: var(--s-line2); }

.photo-wrap { cursor: pointer; }
.photo { position: relative; aspect-ratio: 16/10; display: flex; align-items: center; justify-content: center; overflow: hidden; }
.photo.is-sold-mask .real-img,
.photo.is-sold-mask .car-illustration { filter: grayscale(90%) brightness(0.82); }
.real-img { width: 100%; height: 100%; object-fit: cover; }
.car-illustration { width: 64%; height: 64%; }

.payment-tag {
  position: absolute; bottom: 12px; right: 12px;
  background: rgba(255,255,255,.94); color: var(--s-ink); box-shadow: 0 1px 4px rgba(31,41,55,.15);
  font-size: 11px; font-weight: 700; padding: 6px 12px; border-radius: 6px;
}

.dealer-strip {
  display: flex; justify-content: space-between; align-items: center;
  background: var(--s-strip); color: var(--s-ink); padding: 10px 16px; border-top: 1px solid var(--s-line);
}
.dealer-left { flex: 1; min-width: 0; margin-right: 14px; }
.dealer-addr { font-size: 13px; font-weight: 700; line-height: 1.3; }
.dealer-site { font-size: 11.5px; color: var(--s-muted); margin-top: 1px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.dealer-phone { flex-shrink: 0; font-family: 'Archivo', sans-serif; font-size: 16px; font-weight: 800; color: var(--s-primary); white-space: nowrap; }

.cta { border: none; background: #fff; color: var(--s-ink); font-family: 'Archivo', sans-serif; font-weight: 800; font-size: 11px; letter-spacing: .1em; text-transform: uppercase; cursor: pointer; }
.cta:hover { background: var(--s-soft); }
.cta.primary { background: var(--s-primary); color: #fff; }
.cta.primary:hover { background: var(--s-primary-dark); }

/* 默认View Details是橙色高亮；鼠标移到Contact Us/Financing上时，橙色高亮跟着移动到当前hover的那个tab上，
   离开整行时恢复回默认的View Details高亮 —— 纯CSS实现，不需要额外的JS状态 */
.cta-group:hover .cta.primary:not(:hover) { background: #fff; color: var(--s-ink); }
.cta-group .cta:hover { background: var(--s-primary) !important; color: #fff !important; }

.compare-check { display: flex; align-items: center; gap: 8px; font-size: 13px; color: var(--s-muted); font-weight: 600; }
.compare-check input { width: 15px; height: 15px; }

.title { font-family: 'Archivo', sans-serif; font-weight: 800; letter-spacing: -.01em; margin: 0 0 10px; }
.spec-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 9px 16px; }
.spec { display: flex; gap: 6px; font-size: 13.5px; overflow: hidden; }
.k { color: var(--s-muted); font-weight: 600; white-space: nowrap; }
.v { color: var(--s-ink); font-weight: 700; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

/* ---- Grid 卡片模式专属样式 ---- */
.grid-mode .cta-row { display: grid; grid-template-columns: repeat(3, 1fr); }
.grid-mode .cta { padding: 12px 4px; border-right: 1px solid var(--s-line); }
.grid-mode .cta:last-child { border-right: none; }
.grid-mode .compare-row {
  display: flex; justify-content: space-between; align-items: center;
  padding: 10px 16px; border-bottom: 1px solid var(--s-line); cursor: pointer;
}
.grid-mode .compare-btn { font-size: 12px; font-weight: 800; padding: 5px 12px; border-radius: 20px; background: var(--s-soft); color: var(--s-text2); }
.grid-mode .compare-btn.active { background: var(--s-primary); color: #fff; }
.grid-mode .body { padding: 18px 20px 22px; cursor: pointer; }
.grid-mode .title { font-size: 21px; }
.grid-mode .km-price-row { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 14px; }
.grid-mode .km { font-size: 14px; color: var(--s-muted); font-weight: 600; }
.grid-mode .price { font-family: 'Archivo', sans-serif; font-size: 24px; font-weight: 900; color: var(--s-ink); }
.grid-mode .spec-grid { border-top: 1px solid var(--s-line); padding-top: 14px; }

/* ---- List 横排模式专属样式 ---- */
.list-mode { display: grid; grid-template-columns: 340px 1fr 220px; }
.list-photo-col { display: flex; flex-direction: column; }
.list-photo-col .photo { aspect-ratio: 4/3; }
.list-compare { padding: 12px 16px; border-bottom: 1px solid var(--s-line); border-right: 1px solid var(--s-line); }
.list-info-col { padding: 20px 24px; cursor: pointer; border-right: 1px solid var(--s-line); }
.list-info-col .title { font-size: 19px; margin-bottom: 8px; }
.list-info-col .km { font-size: 13px; color: var(--s-muted); font-weight: 600; margin-bottom: 16px; }
.list-action-col { padding: 20px; display: flex; flex-direction: column; align-items: stretch; }
.list-action-col .cta-group { display: flex; flex-direction: column; }
.list-action-col .price { font-family: 'Archivo', sans-serif; font-size: 26px; font-weight: 900; color: var(--s-ink); text-align: right; }
.list-action-col .biweek { font-size: 11.5px; color: var(--s-muted); text-align: right; margin-bottom: 16px; }
.list-action-col .cta.block { padding: 12px; border-radius: 10px; margin-top: 8px; }

@media (max-width: 900px) {
  .list-mode { grid-template-columns: 1fr; }
  .list-info-col, .list-compare { border-right: none; }
}
</style>
