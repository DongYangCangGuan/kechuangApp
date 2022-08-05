import Vue from 'vue'
import store from '@MAN/store'

Vue.config.errorHandler = function(err, vm, info) {
  // Don't ask me why I use Vue.nextTick, it just a hack.
  // detail see https://forum.vuejs.org/t/dispatch-in-vue-config-errorhandler-has-some-problem/23500

  // Vue.nextTick(() => {
  //   store.dispatch('errorLog/addErrorLog', {
  //     message:err.message,
  //     name:vm.$options.name,
  //     info:info,
  //     url: window.location.href
  //   })
  // })

}
