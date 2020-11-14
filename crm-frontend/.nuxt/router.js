import Vue from 'vue'
import Router from 'vue-router'
import { interopDefault } from './utils'
import scrollBehavior from './router.scrollBehavior.js'

const _10fc34f0 = () => interopDefault(import('../pages/panel.vue' /* webpackChunkName: "pages/panel" */))
const _4064a1c0 = () => interopDefault(import('../pages/singin.vue' /* webpackChunkName: "pages/singin" */))
const _1737f4d4 = () => interopDefault(import('../pages/singup.vue' /* webpackChunkName: "pages/singup" */))
const _1de779ca = () => interopDefault(import('../pages/activate/_id.vue' /* webpackChunkName: "pages/activate/_id" */))
const _1fbced56 = () => interopDefault(import('../pages/index.vue' /* webpackChunkName: "pages/index" */))

// TODO: remove in Nuxt 3
const emptyFn = () => {}
const originalPush = Router.prototype.push
Router.prototype.push = function push (location, onComplete = emptyFn, onAbort) {
  return originalPush.call(this, location, onComplete, onAbort)
}

Vue.use(Router)

export const routerOptions = {
  mode: 'history',
  base: decodeURI('/'),
  linkActiveClass: 'nuxt-link-active',
  linkExactActiveClass: 'nuxt-link-exact-active',
  scrollBehavior,

  routes: [{
    path: "/panel",
    component: _10fc34f0,
    name: "panel___pl"
  }, {
    path: "/singin",
    component: _4064a1c0,
    name: "singin___pl"
  }, {
    path: "/singup",
    component: _1737f4d4,
    name: "singup___pl"
  }, {
    path: "/activate/:id?",
    component: _1de779ca,
    name: "activate-id___pl"
  }, {
    path: "/",
    component: _1fbced56,
    name: "index___pl"
  }],

  fallback: false
}

export function createRouter () {
  return new Router(routerOptions)
}
