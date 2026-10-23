import request from './request'

export const getMakes = () => request.get('/dict/makes')
export const getModels = (makeId) => request.get('/dict/models', { params: { makeId } })
export const getBodyStyles = () => request.get('/dict/body-styles')
export const getTransmissions = () => request.get('/dict/transmissions')
export const getEngines = () => request.get('/dict/engines')
export const getFuelTypes = () => request.get('/dict/fuel-types')
export const getColors = () => request.get('/dict/colors')
