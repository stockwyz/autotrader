import request from './request'

// 前台
export const getAppointmentSlots = (date) => request.get('/appointments/slots', { params: { date } })
export const submitAppointment = (data) => request.post('/appointments', data)

// 邮件里的确认/取消链接打开的页面
export const getAppointmentByToken = (token) => request.get(`/appointments/response/${token}`)
export const respondAppointment = (token, action) =>
  request.post(`/appointments/response/${token}`, null, { params: { action } })

// 后台
export const adminGetAppointments = (params) => request.get('/admin/appointments', { params })
export const adminUpdateAppointmentStatus = (id, status) =>
  request.patch(`/admin/appointments/${id}/status`, null, { params: { status } })
export const adminDeleteAppointment = (id) => request.delete(`/admin/appointments/${id}`)
