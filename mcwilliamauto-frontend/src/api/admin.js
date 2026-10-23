import request from './request'

// ---------- 字典管理 ----------
export const dictApi = {
  makes: {
    list: (params) => request.get('/admin/dict/makes', { params }),
    create: (data) => request.post('/admin/dict/makes', data),
    update: (id, data) => request.put(`/admin/dict/makes/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/makes/${id}`)
  },
  models: {
    list: (params) => request.get('/admin/dict/models', { params }),
    create: (data) => request.post('/admin/dict/models', data),
    update: (id, data) => request.put(`/admin/dict/models/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/models/${id}`)
  },
  bodyStyles: {
    list: (params) => request.get('/admin/dict/body-styles', { params }),
    create: (data) => request.post('/admin/dict/body-styles', data),
    update: (id, data) => request.put(`/admin/dict/body-styles/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/body-styles/${id}`)
  },
  transmissions: {
    list: (params) => request.get('/admin/dict/transmissions', { params }),
    create: (data) => request.post('/admin/dict/transmissions', data),
    update: (id, data) => request.put(`/admin/dict/transmissions/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/transmissions/${id}`)
  },
  engines: {
    list: (params) => request.get('/admin/dict/engines', { params }),
    create: (data) => request.post('/admin/dict/engines', data),
    update: (id, data) => request.put(`/admin/dict/engines/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/engines/${id}`)
  },
  fuelTypes: {
    list: (params) => request.get('/admin/dict/fuel-types', { params }),
    create: (data) => request.post('/admin/dict/fuel-types', data),
    update: (id, data) => request.put(`/admin/dict/fuel-types/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/fuel-types/${id}`)
  },
  colors: {
    list: (params) => request.get('/admin/dict/colors', { params }),
    create: (data) => request.post('/admin/dict/colors', data),
    update: (id, data) => request.put(`/admin/dict/colors/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/colors/${id}`)
  },
  proLabels: {
    list: (params) => request.get('/admin/dict/pro-labels', { params }),
    detail: (id) => request.get(`/admin/dict/pro-labels/${id}`),
    create: (data) => request.post('/admin/dict/pro-labels', data),
    update: (id, data) => request.put(`/admin/dict/pro-labels/${id}`, data),
    delete: (id) => request.delete(`/admin/dict/pro-labels/${id}`)
  }
}

// ---------- 内容管理 ----------
export const contentApi = {
  banners: {
    list: (params) => request.get('/admin/content/banners', { params }),
    create: (data) => request.post('/admin/content/banners', data),
    update: (id, data) => request.put(`/admin/content/banners/${id}`, data),
    delete: (id) => request.delete(`/admin/content/banners/${id}`)
  },
  testimonials: {
    list: (params) => request.get('/admin/content/testimonials', { params }),
    create: (data) => request.post('/admin/content/testimonials', data),
    update: (id, data) => request.put(`/admin/content/testimonials/${id}`, data),
    delete: (id) => request.delete(`/admin/content/testimonials/${id}`)
  },
  careers: {
    list: (params) => request.get('/admin/content/careers', { params }),
    create: (data) => request.post('/admin/content/careers', data),
    update: (id, data) => request.put(`/admin/content/careers/${id}`, data),
    delete: (id) => request.delete(`/admin/content/careers/${id}`)
  },
  pages: {
    list: (params) => request.get('/admin/content/pages', { params }),
    create: (data) => request.post('/admin/content/pages', data),
    update: (id, data) => request.put(`/admin/content/pages/${id}`, data),
    delete: (id) => request.delete(`/admin/content/pages/${id}`),
    getByCode: (code) => request.get(`/admin/content/pages/by-code/${code}`),
    saveByCode: (code, data) => request.put(`/admin/content/pages/by-code/${code}`, data)
  },
  navMenu: {
    list: () => request.get('/admin/content/nav-menu'),
    create: (data) => request.post('/admin/content/nav-menu', data),
    update: (id, data) => request.put(`/admin/content/nav-menu/${id}`, data),
    delete: (id) => request.delete(`/admin/content/nav-menu/${id}`)
  }
}

// ---------- 门店与金融设置 ----------
export const settingsApi = {
  getDealerInfo: () => request.get('/admin/dealer/info'),
  saveDealerInfo: (data) => request.put('/admin/dealer/info', data),
  saveTheme: (themeCode) => request.put('/admin/dealer/theme', { themeCode }),
  listBusinessHours: () => request.get('/admin/dealer/business-hours'),
  saveBusinessHours: (data) => request.put('/admin/dealer/business-hours', data),
  getFinanceSetting: () => request.get('/admin/finance/setting'),
  saveFinanceSetting: (data) => request.put('/admin/finance/setting', data)
}

// ---------- 系统用户 ----------
export const sysUserApi = {
  list: (params) => request.get('/admin/sys-users', { params }),
  create: (data) => request.post('/admin/sys-users', data),
  update: (id, data) => request.put(`/admin/sys-users/${id}`, data),
  delete: (id) => request.delete(`/admin/sys-users/${id}`)
}

// ---------- 留资删除 ----------
export const leadDeleteApi = {
  inquiry: (id) => request.delete(`/admin/leads/inquiry/${id}`),
  testDrive: (id) => request.delete(`/admin/leads/test-drive/${id}`),
  tradeIn: (id) => request.delete(`/admin/leads/trade-in/${id}`),
  carFinder: (id) => request.delete(`/admin/leads/car-finder/${id}`),
  loanApplication: (id) => request.delete(`/admin/leads/loan-application/${id}`),
  contact: (id) => request.delete(`/admin/leads/contact/${id}`)
}
