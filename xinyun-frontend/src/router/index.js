import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  // TODO: 对接后端时添加登录页路由
  // {
  //   path: '/login',
  //   name: 'Login',
  //   component: () => import('../views/Login.vue')
  // }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // TODO: 对接后端时添加登录验证
  // const token = localStorage.getItem('token')
  // if (to.path !== '/login' && !token) {
  //   next('/login')
  // } else {
  //   next()
  // }
  next()
})

export default router
