import {constantRoutes, asyncRoutes} from '@MAN/router'


function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = {...route}
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const permission = {
  namespaced: true,
  state: {
    routes: [],
    addRoutes: [],
    asyncRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      console.log(routes)
      state.routes = constantRoutes.concat(routes)
      console.log(constantRoutes);
      console.log(" state.routes");
      console.log( state.routes)
    },
    SET_ASYNC_ROUTES: (state, asyncRoutes) => {
      state.asyncRoutes = asyncRoutes
    }
  },
  actions: {
    generateRoutes({commit, state}, roles) {
      return new Promise(resolve => {

        const _accessedRoutes = loadMenus(state.asyncRoutes)

       // _accessedRoutes.push({path: '*', redirect: '/404', hidden: true})

        console.log("_accessedRoutes");
        console.log(_accessedRoutes)

        commit('SET_ROUTES', _accessedRoutes)
        resolve(_accessedRoutes)

      })
    }
  }

}

const loadMenus = (routes) => {

  let menus = []
  routes.forEach(route => {
    let _menus = loadMenu(route);

    menus.push(_menus);
  })


  return menus

}

const loadMenu = (route) => {

  let _route = {};
  let noCache = true;
  if (route.noCache == "1") {
    noCache = false;
  }
  // if (route.children.length != 0) {
  if (route.parentid !== "0" ) { // 非一级菜单
    _route = {
      path: route.path,
      //component:()=>import(`@MA`+componentPath),
      component(resolve) {
        // let componentPath = 'dashboard/monitor/index';
        // //   require('@/views/home/index')
        // return   import(`@MAN/views/${componentPath}`)
        var componentPath='';
        if(route.url.startsWith("@MAN/")){
          componentPath=route.url.substring(11,route.url.length);
          console.log("componentPath",componentPath)
          return Promise.resolve(require(`@MAN/views/${componentPath}`));
        }
        // else if (route.url.startsWith("@SEAL/")){
        //   componentPath=route.url.substring(12,route.url.length);
        //   return import(`@SEAL/views/${componentPath}`)
        // }
      },
      name: route.name,
      meta: {title: route.name, icon: route.icon, noCache: noCache}
    }
    if (route.children.length !== 0) {
      _route.redirect =  route.children[0].path
      _route.children = loadMenus(route.children)
    }
  }
  else {


    _route = {
      path: route.path,
      component:()=>import('@MAN/layout'),
      redirect: route.children[0].path,
      meta: {title: route.name, icon: route.icon, noCache: noCache},
      children: loadMenus(route.children)
    }

  }

  console.log("_route"+_route);
  return _route;

}

export default permission
