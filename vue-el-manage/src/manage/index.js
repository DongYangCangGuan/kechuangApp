import Vue from 'vue'
import Cookies from 'js-cookie'
import Element from 'element-ui'
import '@MAN/styles/element-variables.scss'
import '@MAN/styles/index.scss' // global css
import 'animate.css'
import '@MAN/icons'
import '@MAN/utils/errorLog'
Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
})

import VueLazyLoad from 'vue-lazyload'
Vue.use(VueLazyLoad,{
  error:require('@MAN/assets/lazyload/error.jpg'),
  loading:require('@MAN/assets/lazyload/loading.svg'),
  attempt:1,
  throttleWait: 200
})

import store from '@MAN/store'
import router from '@MAN/router'
import '@MAN/permission'


// import '@SEAL/store'

export default {
  store,
  router
}
