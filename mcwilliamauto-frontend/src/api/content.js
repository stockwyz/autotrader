import request from './request'

export const getPageByCode = (code) => request.get(`/content/pages/${code}`)
export const getCareers = () => request.get('/content/careers')
export const getCareerDetail = (id) => request.get(`/content/careers/${id}`)
export const getBanners = () => request.get('/content/banners')
