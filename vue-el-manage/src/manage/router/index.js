import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import dashboard from './modules/dashboard'
import login from './modules/login'
import manage from './modules/manage'

export const asyncRoutes = [
  //首页
  dashboard,
  //管理
  manage,
  //其他页面
  {path: '*', redirect: '/404', hidden: true},
]

export const constantRoutes = [
  dashboard,
  //404页面
  {
    path: '/404',
    component: () => import('@COM/views/errorPage/404'),
    hidden: true
  },

  //大会议室
  // bigMeeting,

  //401页面
  {
    path: '/401',
    component: () => import('@COM/views/errorPage/401'),
    hidden: true
  },
  ...login
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
