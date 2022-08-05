const login = [
  //登录
  {
    path: '/login',
    component: () => import('@MAN/views/login').then(m=>m.default),
    hidden: true
  },
  //重定向页面
  {
    path: '/redirect',
    component: () => import('@MAN/layout').then(m => m.default),
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@COM/views/redirect').then(m => m.default)
      }
    ]
  }
]

export default login
