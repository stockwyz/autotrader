import request from './request'

// 前台
export function getVehicleList(params) {
  return request.get('/vehicles', { params })
}

export function getVehicleDetail(id) {
  return request.get(`/vehicles/${id}`)
}

// 后台
export function adminGetVehicleList(params) {
  return request.get('/admin/vehicles', { params })
}

export function adminGetVehicleDetail(id) {
  return request.get(`/admin/vehicles/${id}`)
}

export function adminCreateVehicle(data) {
  return request.post('/admin/vehicles', data)
}

export function adminUpdateVehicle(id, data) {
  return request.put(`/admin/vehicles/${id}`, data)
}

export function adminDeleteVehicle(id) {
  return request.delete(`/admin/vehicles/${id}`)
}

export function adminUpdateVehicleStatus(id, field, value) {
  return request.patch(`/admin/vehicles/${id}/status`, { field, value })
}

export function adminUploadVehicleImages(vehicleId, formData) {
  return request.post(`/admin/upload/vehicle/${vehicleId}/images`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function adminDeleteVehicleImage(vehicleId, imageId) {
  return request.delete(`/admin/vehicles/${vehicleId}/images/${imageId}`)
}

export function adminReorderVehicleImages(vehicleId, imageIds) {
  return request.put(`/admin/vehicles/${vehicleId}/images/reorder`, imageIds)
}

export function adminDeleteVehicleVideo(vehicleId) {
  return request.delete(`/admin/vehicles/${vehicleId}/video`)
}

export function adminDeleteVehicleCarfax(vehicleId) {
  return request.delete(`/admin/vehicles/${vehicleId}/carfax`)
}
