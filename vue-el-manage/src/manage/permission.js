import router from '@MAN/router'
import store from '@MAN/store'
import {Message} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {getToken,getSecret} from '@COM/utils/auth' // get token from cookie
import {hideLoading} from "./utils/loading";

NProgress.configure({showSpinner: false}) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // determine whether the user has logged in
  const hasToken = getToken()
  if (hasToken) {
    if (to.path === '/login') {
      next({path: '/'})
      NProgress.done()
    }
    else {
      const hasRoles = store.state.user.roles && store.state.user.roles.length > 0
      if (hasRoles) {
        next()
      }
      else {
        try {

          const {data,roles} = await store.dispatch('user/getInfo',store.state.user.roleslist[0].id)
          console.log(data);
          store.commit('permission/SET_ASYNC_ROUTES',data)

          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
          console.log("roles")
          console.log(roles);
          this.loading=true;
          router.addRoutes(accessRoutes)

          next({...to, replace: true})
        }
        catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)

          NProgress.done()
          this.loading=false;
        }
      }
    }
  }
  else {
    const hasSecretKey = getSecret()
    // if (!hasSecretKey) {
    //   //获取getSecretKey
    //   const encrypted = await store.dispatch('secret/getKey')
    //   await store.dispatch('secret/getAESKey', encrypted)
    // }
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    }
    else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
