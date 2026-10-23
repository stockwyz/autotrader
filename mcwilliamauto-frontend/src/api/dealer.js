import request from './request'

export const getDealerInfo = () => request.get('/dealer/info')
export const getBusinessHours = () => request.get('/dealer/business-hours')
