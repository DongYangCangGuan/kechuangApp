import Vue from 'vue'
import 'normalize.css/normalize.css'
import * as filters from '@COM/filters'
import App from './App'
import  VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import VueCookies from 'vue-cookies'



Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false;
// 自定义loading框样式
window.loadingOptions = {
  lock: true,
  text: '加载中……',
  background: 'rgba(0, 0, 0, 0.7)'
};
Vue.use(loadingOptions,VueQuillEditor,VueCookies);
// 自定义全局引入echart
import echarts from 'echarts'
Vue.prototype.$echarts = echarts;

const {router,store,i18n} = require(`./${process.env.PACKAGE}`).default

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})

