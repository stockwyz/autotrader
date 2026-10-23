import { defineStore } from 'pinia'

export const useVehicleFilterStore = defineStore('vehicleFilter', {
  state: () => ({
    filters: {
      make: null,
      model: null,
      minYear: null,
      maxYear: null,
      transmissionId: null,
      minPrice: null,
      maxPrice: null,
      minKm: null,
      maxKm: null,
      bodyStyleId: null,
      exteriorColorId: null,
      engineId: null,
      fuelTypeId: null,
      keyword: ''
    },
    viewMode: 'grid' // grid | list
  }),
  actions: {
    setFilters(payload) {
      this.filters = { ...this.filters, ...payload }
    },
    resetFilters() {
      this.filters = {
        make: null, model: null, minYear: null, maxYear: null,
        transmissionId: null, minPrice: null, maxPrice: null,
        minKm: null, maxKm: null, bodyStyleId: null,
        exteriorColorId: null, engineId: null, fuelTypeId: null, keyword: ''
      }
    }
  }
})
