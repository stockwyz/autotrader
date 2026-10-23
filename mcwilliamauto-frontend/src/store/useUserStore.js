import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    username: localStorage.getItem('admin_username') || '',
    realName: localStorage.getItem('admin_realname') || ''
  }),
  actions: {
    setLogin({ token, username, realName }) {
      this.token = token
      this.username = username
      this.realName = realName
      localStorage.setItem('admin_token', token)
      localStorage.setItem('admin_username', username)
      localStorage.setItem('admin_realname', realName || '')
    },
    logout() {
      this.token = ''
      this.username = ''
      this.realName = ''
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_username')
      localStorage.removeItem('admin_realname')
    }
  }
})
