const dashboard = {
    path: '',
    component: () => import('@MAN/views/manage/chart2').then(m => m.default),
    redirect: '/manage/report',
    name: 'Chart',
    hidden: true,
    meta: {
        title: '首页',
        icon: 'dashboard',
        hidden: true
    },
    // children: [
    //     {
    //         path: 'dashboard',
    //         redirect: '/system',
    //         hidden: true,
    //     },
    //     {
    //         path: 'detail',
    //         redirect: '/dashboard',
    //         hidden: true,
    //     },
    //     {
    //         path: 'intellanswer',
    //         component: () => import('@MAN/views/manage/Home/index').then(m => m.default),
    //         name: 'intellanswer',
    //         meta: {title: '系统首页', icon: 'intellanswer', noCache: true, affix: true}
    //     },
    //     {
    //         path: 'counts',
    //         component: () => import('@MAN/views/manage/counts/counts').then(m => m.default),
    //         name: 'counts',
    //         meta: {title: '访问统计', icon: 'counts', noCache: true}
    //     },
    //     {
    //         path: 'counts',
    //         component: () => import('@MAN/views/manage/policyguid/searchfile').then(m => m.default),
    //         name: 'System',
    //         meta: {title: '交易锦囊', icon: 'system', noCache: true}
    //     },
    //     {
    //         path: 'monitor',
    //         component: () => import('@MAN/views/dashboard/monitor').then(m => m.default),
    //         name: 'Monitor',
    //         meta: {title: '监控首页', icon: 'monitor', noCache: true},
    //     },
    //     {
    //         path: 'detail/:type(\\w+)/:id(\\d+)*',
    //         component: () => import('@MAN/views/dashboard/detail').then(m => m.default),
    //         name: 'Detail',
    //         meta: {title: '详情', noCache: true},
    //         hidden: true
    //     }
    // ]
}

export default dashboard
