一 、vue-manage文件目录结构

```
-build
-config  运行时相关项目的配置信息
  |_document
    |_dev.env.js
    |_prod.env.js
    |_sit.env.js
  |_manage
    |_dev.env.js
    |_prod.env.js
    |_sit.env.js
  |_index.js
-src
  |_common  公共方法、资源、组件
    |_assets  公共图片资源 主要包含401 404页面
    |_components 公共组件 饿了么管理平台提供的公共组件
    |_directive 公共指令方法
    |_filters   公共过滤方法
    |_icons     公共svg图片资源 icon-图片名
      |_index.js 公共方法将svg图片转为icon-图片名的别名
    |_lang  公共i18n文字语言切换  按照项目需要在项目对应目录lang重写相关语言值 主要包含zh.js、es.js、en.js语言，可以自行添加其他语言
      |_index.js
    |_layout 需要对应项目目录位置重写Layout组件
    |_router  公共路由  按照项目需要在项目对应目录router重新配置路由routes 主要包含constantRoutes常用路由以及asyncRoutes动态路由
      |_index.js  提供了resetRouter重置路由方法  router对象
    |_store   公共状态管理
      |_modules   
      |_getters.js 
      |_index.js 
    |_styles 公共样式  可以按照项目结构重写varialbes.scss、index.scss 在相关项目中引用项目对应的index.scss
    |_utils   公共方法 主要按照项目结构重写request.js请求方法
    |_vendor  导出excel 、zip方法
    |_views   公共页面  主要有错误页面401、404  重定向页面
    |_permission.js  路由守卫 按照项目需要重写permission.js中的doPermission方法
    |_settings.js  其他配置信息
  |_document  饿了么管理平台
  |_manage    公司项目管理平台
    |_api  
      |_user.js
      |_... 更多
    |_assets  项目所需的资源图片
    |_icons
      |_svg  放入项目所需要的svg图片资源
    |_layout 重写Layout组件
      |_index.vue 必须
    |_mock 挡板数据 按实际需要添加
    |_router 重新定义routes 项目packageRoutes路由  项目asyncRoutes动态路由
      |_modules
        |_crm.js
        |_login.js
        |_manage.js
        |_office.js
        |_...更多
      |_index.js  必须
    |_store 
      |_modules
        |_user.js
        |_secret.js
        |_...更多
      |_getters
      |_index.js
    |_styles 重写样式文件
      |_index.scss
      |_variables.scss
    |_utils 重写request.js 请求方法
      |_request.js 必须
      |_response.js
      |_...更多
    |_views 项目页面
      |_login
      |_manage
      |_...更多
    |_index.js 入口文件所需资源 
    |_permission.js 重写 doPermission方法
  |_App.vue   入口组件
  |_main.js   入口文件
-static
  |_tinymce4.7.5  富文本编辑
-.bablelrc
_.editorconfig
-.postcssrc.js
-index.html     index模版页面
-package.json   

```





二、vue-manage-crm、vue-manage-office等第三方目录结构

```
-src
  |_api  
  |_assets  页面所需要用到的图片资源
  |_icons
      |_svg  放入项目所需要的svg图片资源
  |_components  页面所需要组件
  |_router
    |_index.js 路由配置
  |_store
    |_index.js 状态管理配置
  |_views 路由页面
```
- 页面所需要引用的公共的内容 通过别名@COM 后面跟具体路径文件
- 页面所需引用本目录中的内容 通过别名@CRM、@OFFICE 后面跟具体路径文件

三、别名配置

```
在vue-manage项目 build/webpack.base.conf.js中配置

resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      'vue$': 'vue/dist/vue.esm.js',
      '@COM': resolve('../vue-el-manage/src/common'),
      '@DOC': resolve('../vue-el-manage/src/document'),
      '@MAN': resolve('../vue-el-manage/src/manage'),
      '@CRM': resolve('../vue-el-manage-crm/src'),
      '@OFFICE': resolve('../vue-el-manage-office/src'),
      ...如果有更多系统按照此格式配置别名
    }
  },
```

