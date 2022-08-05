import Vue from 'vue'
import Cookies from 'js-cookie'
import Element from 'element-ui'
import '@DOC/styles/element-variables.scss'
import i18n from '@DOC/lang'
import '@DOC/styles/index.scss' // global css
import '@DOC/utils/errorLog'
import '@DOC/icons'

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})


import store from '@DOC/store'
import router from '@DOC/router'

import { mockXHR } from '@DOC/mock'
mockXHR()

import '@DOC/permission'


export default {
  store,
  router,
  i18n
}
