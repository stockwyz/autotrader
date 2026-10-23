import { defineStore } from 'pinia'

/** 车辆对比勾选状态，纯前端会话内存储，不做持久化 */
export const useCompareStore = defineStore('compare', {
  state: () => ({
    ids: []
  }),
  getters: {
    count: (state) => state.ids.length,
    isSelected: (state) => (id) => state.ids.includes(id)
  },
  actions: {
    toggle(id) {
      const idx = this.ids.indexOf(id)
      if (idx >= 0) {
        this.ids.splice(idx, 1)
      } else {
        this.ids.push(id)
      }
    }
  }
})
