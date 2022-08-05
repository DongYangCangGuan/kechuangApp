module.exports =  {
  navbarBtn: { 
    height: app.globalData.headerBtnPosi.height,
    width: app.globalData.headerBtnPosi.width,
    // 胶囊top - 状态栏高度
    top: app.globalData.headerBtnPosi.top - app.globalData.systeminfo.statusBarHeight,
    // 胶囊bottom - 胶囊height - 状态栏height （现胶囊bottom 为距离导航栏底部的长度）
    bottom: app.globalData.headerBtnPosi.bottom - app.globalData.headerBtnPosi.height - app.globalData.systeminfo.statusBarHeight,
    // 屏幕宽度 - 胶囊right
    right: app.globalData.systeminfo.screenWidth - app.globalData.headerBtnPosi.right
  },
  statusBarHeight: app.globalData.systeminfo.statusBarHeight,
  navbarHeight:'0',
}