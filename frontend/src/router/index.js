import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import FrontPage from '../views/FrontPage.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'FrontPage',
    component: FrontPage
  },
  {
    path: '/izlozba',
    name: 'Izlozba',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/Izlozba.vue')
  },
  {
    path: '/kupovina',
    name: 'Kupovina',
    component: () => import('../views/Kupovina.vue')
  },
  {
    path: '/moj_profil',
    name: 'Umjetnik',
    component: () => import('../views/Umjetnik.vue')
  },
  {
    path: '/djela',
    name: 'Djela',
    component: () => import('../views/Djela.vue')
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // },
  
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
