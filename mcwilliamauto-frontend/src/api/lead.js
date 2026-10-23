import request from './request'

export const submitInquiry = (data) => request.post('/leads/inquiry', data)
export const submitTestDrive = (data) => request.post('/leads/test-drive', data)
export const submitTradeIn = (data) => request.post('/leads/trade-in', data)
export const submitCarFinder = (data) => request.post('/leads/car-finder', data)
export const submitContact = (data) => request.post('/leads/contact', data)

// 后台
export const adminGetLeads = (type, params) => request.get(`/admin/leads/${type}`, { params })
