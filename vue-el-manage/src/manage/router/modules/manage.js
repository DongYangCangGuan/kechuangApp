const manage = {
    path: '/manage',
    component: () => import('@MAN/layout').then(m => m.default),
    redirect: '/manage/menu',
    name: 'Menage',
    hidden: true,
    meta: {
        title: '管理',
        icon: 'example',
        hidden: true,
    },
    children: [
        {
            path: 'hot',
            component: () => import('@MAN/views/manage/hotUpdate').then(m => m.default),
            name: 'Hot',
            hidden: true,
            meta: {title: '热更新', icon: 'hot', noCache: true}
        },
        {
            path: 'menu',
            component: () => import('@MAN/views/manage/menu').then(m => m.default),
            name: 'Menu',
            hidden: false,
            meta: {title: '菜单管理', icon: 'component', noCache: true}
        },
        {
            path: 'department',
            component: () => import('@MAN/views/manage/department').then(m => m.default),
            name: 'Department',
            hidden: false,
            meta: {title: '部门管理', icon: 'tree', noCache: true}
        },
        {
            path: 'member',
            component: () => import('@MAN/views/manage/member').then(m => m.default),
            name: 'Member',
            hidden: false,
            meta: {title: '人员管理', icon: 'user', noCache: true}
        },
        {
            path: 'picture',
            component: () => import('@MAN/views/manage/picture').then(m => m.default),
            name: 'Picture',
            hidden: false,
            meta: {title: 'Banner图管理', icon: 'guide', noCache: true}
        },
        {
            path: 'role',
            component: () => import('@MAN/views/manage/role').then(m => m.default),
            name: 'Role',
            hidden: false,
            meta: {title: '角色管理', icon: 'peoples', noCache: true}
        },
        {
            path: 'word',
            component: () => import('@MAN/views/manage/keyWord').then(m => m.default),
            name: 'Word',
            hidden: false,
            meta: {title: '字典管理', icon: 'language', noCache: true}
        },
        //货品大类
        {
            path: 'Category',
            component: () => import('@MAN/views/manage/product/index1').then(m => m.default),
            name: 'Category',
            hidden: false,
            meta: {title: '货品大类', icon: 'language', noCache: true}
        },
        //产品展示
        {
            path: 'product',
            component: () => import('@MAN/views/manage/product/index').then(m => m.default),
            name: 'product',
            hidden: false,
            meta: {title: '产品展示', icon: 'language', noCache: true}
        },
        {
            path: 'approveInformation',
            component: () => import('@MAN/views/manage/approve/approveInformation').then(m => m.default),
            name: 'approveInformation',
            hidden: false,
            meta: {title: '审批信息查询', icon: 'language', noCache: true}
        },
        {
            path: 'approveProcess',
            component: () => import('@MAN/views/manage/approve/approveProcess').then(m => m.default),
            name: 'approveProcess',
            hidden: false,
            meta: {title: '审批流程配置', icon: 'language', noCache: true}
        },

        //入库统计
        {
            path: 'InBoundOrder',
            name: 'InBoundOrder',
            component: () => import('@MAN/views/manage/DataStatistics/InBoundOrder').then(m => m.default),
            hidden: false,
            meta: {title: '入库订单统计', icon: 'language', noCache: true}
        },
        //入库统计
        {
            path: 'InBound',
            name: 'InBound',
            component: () => import('@MAN/views/manage/DataStatistics/InBound').then(m => m.default),
            hidden: false,
            meta: {title: '入库商品统计', icon: 'language', noCache: true}
        },
        //出库统计
        {
            path: 'OutBound',
            name: 'OutBound',
            component: () => import('@MAN/views/manage/DataStatistics/OutBound').then(m => m.default),
            hidden: false,
            meta: {title: '出库商品统计', icon: 'language', noCache: true}
        },
        //出库统计
        {
            path: 'OutBoundOrder',
            name: 'OutBoundOrder',
            component: () => import('@MAN/views/manage/DataStatistics/OutBoundOrder').then(m => m.default),
            hidden: false,
            meta: {title: '出库订单统计', icon: 'language', noCache: true}
        },
        //领用统计
        {
            path: 'Receive',
            name: 'Receive',
            component: () => import('@MAN/views/manage/DataStatistics/Receive').then(m => m.default),
            hidden: false,
            meta: {title: '领用统计', icon: 'language', noCache: true}
        },
        //库存统计
        {
            path: 'Stock',
            name: 'Stock',
            component: () => import('@MAN/views/manage/DataStatistics/Stock').then(m => m.default),
            hidden: false,
            meta: {title: '库存统计', icon: 'language', noCache: true}
        },
        //预算设置
        {
            path: 'BudgeSetting',
            name: 'BudgeSetting',
            component: () => import('@MAN/views/manage/DataStatistics/BudgeSetting').then(m => m.default),
            hidden: false,
            meta: {title: '预算设置', icon: 'language', noCache: true}
        },
        //消息列表
        {
            path: 'MessageList',
            name: 'MessageList',
            component: () => import('@MAN/views/manage/SecurityManagement/MessageList').then(m => m.default),
            hidden: false,
            meta: {title: '消息列表', icon: 'language', noCache: true}
        },
        //任务列表
        {
            path: 'TaskList',
            name: 'TaskList',
            component: () => import('@MAN/views/manage/SecurityManagement/TaskList').then(m => m.default),
            hidden: false,
            meta: {title: '任务列表', icon: 'language', noCache: true}
        },
        //任务详情查看
        {
            path: 'BreachTaskList',
            name: 'BreachTaskList',
            component: () => import('@MAN/views/manage/SecurityManagement/BreachTaskList').then(m => m.default),
            hidden: false,
            meta: {title: '任务详情查看', icon: 'language', noCache: true}
        },
        //供应商管理
        {
            path: 'supplierInfo',
            name: 'supplierInfo',
            component: () => import('@MAN/views/manage/supplier/index').then(m => m.default),
            hidden: false,
            meta: {title: '供应商管理', icon: 'language', noCache: true}
        },
        //权重设置
        {
            path: 'commodityScoring',
            name: 'commodityScoring',
            component: () => import('@MAN/views/manage/CommodityScoring/index').then(m => m.default),
            hidden: false,
            meta: {title: '权重设置', icon: 'language', noCache: true}
        },
        //帮助
        {
            path: 'helping',
            name: 'helping',
            component: () => import('@MAN/views/manage/help/helping').then(m => m.default),
            hidden: false,
            meta: {title: '帮助', icon: 'language', noCache: true}
        },
        //报告
        {
            path: 'report',
            name: 'report',
            component: () => import('@MAN/views/manage/report').then(m => m.default),
            hidden: false,
            meta: {title: '报告管理', icon: 'language', noCache: true}
        },
        //通知管理
        {
            path: 'inform',
            name: 'inform',
            component: () => import('@MAN/views/manage/inform').then(m => m.default),
            hidden: false,
            meta: {title: '通知管理', icon: 'language', noCache: true}
        },

        //会员管理
        {
            path: 'member',
            name: 'member',
            component: () => import('@MAN/views/manage/member').then(m => m.default),
            hidden: false,
            meta: {title: '会员管理', icon: 'language', noCache: true}
        },

        //数据洞察
        {
            path: 'chart',
            name: 'chart',
            component: () => import('@MAN/views/manage/chart').then(m => m.default),
            hidden: false,
            meta: {title: '数据洞察', icon: 'chart', noCache: true}
        },

        //数据分析
        {
            path: 'chart2',
            name: 'chart2',
            component: () => import('@MAN/views/manage/chart2').then(m => m.default),
            hidden: false,
            meta: {title: '数据分析', icon: 'people', noCache: true}
        },

        //用户管理
        {
            path: 'user2',
            name: 'user2',
            component: () => import('@MAN/views/manage/user2').then(m => m.default),
            hidden: false,
            meta: {title: '用户管理2', icon: 'user', noCache: true}
        },

        //新闻管理
        {
            path: 'news',
            name: 'news',
            component: () => import('@MAN/views/manage/news').then(m => m.default),
            hidden: false,
            meta: {title: '新闻管理', icon: 'chart', noCache: true}
        },
    ]
}

export default manage
