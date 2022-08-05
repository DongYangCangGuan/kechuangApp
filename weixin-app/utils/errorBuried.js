function init(_data) {
  data = _data
  // 重写page函数，增加阿里云监控
  let oldPage = Page
  Page = function(obj) {
    // 对Page的传参obj对象的所有函数进行异常捕捉
    for (let key in obj) {
      let oldFunction = obj[key]
      if (typeof oldFunction === 'function') {
        obj[key] = function() {
          try {
            // 日志埋点
            if (['onShow', 'onHide'].includes(key)) {
              log.info('执行了' + this.__route__ + '的' + key + '方法')
              key === 'onShow' ? enterPageLog() : leavePageLog()
            }
            return oldFunction.call(this, ...arguments)
          } catch (e) {
            const errInfo = '发生了错误！页面：' + this.__route__ + '，方法：' + key + '，错误信息：' + e.message
            console.error(errInfo)
            // 上传信息到服务端
            wx.request({
              url: host + '/addWebLog',
              method: 'POST',
              data: {
                errInfo: errInfo
              }
            })
            throw e
          }
        }
      }
    }
    return oldPage(Monitor.hookPage(obj))
  }
}