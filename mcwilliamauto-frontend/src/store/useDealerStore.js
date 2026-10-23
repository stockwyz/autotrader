import { defineStore } from 'pinia'
import { getDealerInfo, getBusinessHours } from '@/api/dealer'

/** 门店信息+营业时间只需要全站请求一次，缓存起来给Header/Footer/车辆卡片复用 */
export const useDealerStore = defineStore('dealer', {
  state: () => ({
    info: null,
    hours: [],
    loaded: false
  }),
  actions: {
    async ensureLoaded() {
      if (this.loaded) return
      try {
        const [info, hours] = await Promise.all([getDealerInfo(), getBusinessHours()])
        this.info = info
        this.hours = hours || []
      } catch (e) {
        this.info = null
        this.hours = []
      } finally {
        this.loaded = true
      }
    }
  }
})
