import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/useUserStore'

const routes = [
  { path: '/', name: 'Home', component: () => import('@/views/Home.vue') },
  { path: '/used-cars', name: 'VehicleList', component: () => import('@/views/VehicleList.vue') },
  { path: '/cars/:id', name: 'VehicleDetail', component: () => import('@/views/VehicleDetail.vue') },
  { path: '/carfinder', name: 'CarFinder', component: () => import('@/views/CarFinder.vue') },
  { path: '/trade-in', name: 'TradeIn', component: () => import('@/views/TradeIn.vue') },
  { path: '/finance', name: 'Finance', component: () => import('@/views/Finance.vue') },
  { path: '/car-loan-calculator', name: 'LoanCalculator', component: () => import('@/views/LoanCalculator.vue') },
  { path: '/car-loan-application', name: 'LoanApplication', component: () => import('@/views/LoanApplication.vue') },
  { path: '/contact-us', name: 'ContactUs', component: () => import('@/views/ContactUs.vue') },
  { path: '/about-us', name: 'AboutUs', component: () => import('@/views/AboutUs.vue') },
  { path: '/appointment', name: 'Appointment', component: () => import('@/views/Appointment.vue') },
  { path: '/appointment/response/:token', name: 'AppointmentResponse', component: () => import('@/views/AppointmentResponse.vue') },
  { path: '/careers', name: 'Careers', component: () => import('@/views/Careers.vue') },

  // 后台管理
  { path: '/admin/login', name: 'AdminLogin', component: () => import('@/views/admin/Login.vue') },
  {
    path: '/admin/vehicles',
    name: 'AdminVehicleList',
    component: () => import('@/views/admin/VehicleManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/vehicles/edit/:id?',
    name: 'AdminVehicleEdit',
    component: () => import('@/views/admin/VehicleEdit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/leads',
    name: 'AdminLeads',
    component: () => import('@/views/admin/LeadManage.vue'),
    meta: { requiresAuth: true }
  },
  { path: '/admin/appointments', name: 'AdminAppointments', component: () => import('@/views/admin/AppointmentManage.vue'), meta: { requiresAuth: true } },
  // 字典管理
  { path: '/admin/dict/makes', component: () => import('@/views/admin/dict/DictMakes.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/models', component: () => import('@/views/admin/dict/DictModels.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/body-styles', component: () => import('@/views/admin/dict/DictBodyStyles.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/transmissions', component: () => import('@/views/admin/dict/DictTransmissions.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/engines', component: () => import('@/views/admin/dict/DictEngines.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/fuel-types', component: () => import('@/views/admin/dict/DictFuelTypes.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/colors', component: () => import('@/views/admin/dict/DictColors.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/pro-labels', component: () => import('@/views/admin/dict/ProLabels.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/pro-labels/new', component: () => import('@/views/admin/dict/ProLabelEdit.vue'), meta: { requiresAuth: true } },
  { path: '/admin/dict/pro-labels/edit/:id', component: () => import('@/views/admin/dict/ProLabelEdit.vue'), meta: { requiresAuth: true } },
  // 内容管理
  { path: '/admin/content/testimonials', component: () => import('@/views/admin/content/Testimonials.vue'), meta: { requiresAuth: true } },
  { path: '/admin/content/careers', component: () => import('@/views/admin/content/Careers.vue'), meta: { requiresAuth: true } },
  { path: '/admin/content/banners', component: () => import('@/views/admin/content/Banners.vue'), meta: { requiresAuth: true } },
  { path: '/admin/content/about-us', component: () => import('@/views/admin/content/AboutUsEditor.vue'), meta: { requiresAuth: true } },
  { path: '/admin/content/pages', component: () => import('@/views/admin/content/Pages.vue'), meta: { requiresAuth: true } },
  { path: '/admin/content/nav-menu', component: () => import('@/views/admin/content/NavMenu.vue'), meta: { requiresAuth: true } },
  // 前台网站风格
  { path: '/admin/website-style', component: () => import('@/views/admin/settings/WebsiteStyle.vue'), meta: { requiresAuth: true } },
  // 设置
  { path: '/admin/settings/dealer', component: () => import('@/views/admin/settings/DealerSettings.vue'), meta: { requiresAuth: true } },
  { path: '/admin/settings/finance', component: () => import('@/views/admin/settings/FinanceSettings.vue'), meta: { requiresAuth: true } },
  { path: '/admin/settings/users', component: () => import('@/views/admin/settings/SysUsers.vue'), meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const userStore = useUserStore()
    if (!userStore.token) {
      next('/admin/login')
      return
    }
  }
  next()
})

export default router
