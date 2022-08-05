const app = getApp();
const {
  post
} = require("./api");
const {
  Encrypt
} = require("./encrypt");

async function checkSession() {
  return new Promise((resolve, reject) => {
    wx.checkSession({
      success() {
        return resolve(true)
      },
      fail() {
        return resolve(false)
      }
    })
  })
}

async function checkToken() {
  return new Promise((resolve, reject) => {
    post({
      className: "user",
      method: "getUser",
      data: {}
    }).then(res => {
      if (res.data.code == 200) {
        wx.setStorageSync('userInfo', res.data.data);
        resolve(true);
      } else {
        resolve(false);
      }
    }).catch(err => {
      resolve(false);
    })
  });
}

// 检测登录状态，返回 true / false
async function checkHasLogined() {
  const token = wx.getStorageSync('token')
  const userInfo = wx.getStorageSync('userInfo')
  if (!token || !userInfo || JSON.stringify(userInfo) === "{}") {
    wx.removeStorageSync('token')
    wx.removeStorageSync('userInfo')
    return false
  }

  const loggined = await checkSession();
  if (!loggined) {
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    return false
  }

  const checkTokenRes = await checkToken(token);
  if (!checkTokenRes) {
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    return false
  }
  return true
}

async function wxaCode() {
  return new Promise((resolve, reject) => {
    wx.login({
      success(res) {
        return resolve(res.code)
      },
      fail() {
        wx.showToast({
          title: '获取code失败',
          icon: 'none'
        })
        return resolve('获取code失败')
      }
    })
  })
}

async function appletLogin(pages) {
  const _that = this
  let code = await _that.wxaCode();
  if (code) {
    let data = {
      code: code,
      appId: app.globalData.appId,
      secret: app.globalData.secret,
      userInfo: JSON.stringify(wx.getStorageSync('beforeUserInfo'))
    }
    console.log("000000",app.globalData.loginUrl),
    wx.request({
      url: app.globalData.loginUrl,
      
      data: {
        // param: Encrypt(data)
         param : data
      },
      header: {
        "Content-Type": "application/json"
      },
      method: 'POST',
      success: function (res) {
        if (res.data.code != 200) {
          //登录错误
          wx.showModal({
            title: '无法登录',
            showCancel: false
          });
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
        } else {
          //登录成功
          console.log("登录成功1",res.data.data);
          wx.setStorageSync('token', res.data.data.token);
          wx.setStorageSync('userInfo', res.data.data);
          let one_login_date = wx.getStorageSync('one_login_date');
          if (one_login_date === undefined || one_login_date == null || one_login_date == '') {
            wx.setStorageSync('one_login_date', (new Date()).valueOf()); //没有存过登录，给第一次登录时间赋值当前时间
          }
          if (res.data.data.isFirst) {
            setTimeout(function () {
              wx.showModal({
                title: '提示',
                content: '登录成功。是否绑定权益',
                success: res => {
                  if (res.confirm) {
                    wx.navigateTo({
                      url: '../mine/authentification/authentification',
                    })
                  }
                }
              })
            }, 1000)

          }
          app.userLogin();
          if (pages) {
            pages.onShow();
          }
        }
      }
    });
  }
}

async function accountLogin(pages) {
  console.log(wx.getStorageSync('beforeAccount'), 'beforeAccount------->');
  let _that = this;
  let code = await _that.wxaCode();
  if (code) {
    wx.request({
      url: app.globalData.accountLoginUrl,
      data: {
        param: Encrypt(wx.getStorageSync('beforeAccount'))
      },
      header: {
        "Content-Type": "application/json"
      },
      method: 'POST',
      success: function (res) {
        if (res.data.code === 200) {
          wx.setStorageSync('token', res.data.data.token);
          wx.setStorageSync('userInfo', res.data.data);
          let one_login_date = wx.getStorageSync('one_login_date');
          if (one_login_date === undefined || one_login_date == null || one_login_date == '') {
            wx.setStorageSync('one_login_date', (new Date()).valueOf()); //没有存过登录，给第一次登录时间赋值当前时间
          }
          wx.switchTab({
            url: '/pages/mine/mine',
          });
        } else {
          wx.showModal({
            title: '提示',
            content: '用户名或密码错误,请重新输入',
            showCancel: false,
          });
        }
      }
    });
  }
}

//退出登录
function loginOut() {
  wx.removeStorageSync('token')
  wx.removeStorageSync('userInfo')
}

async function checkAndAuthorize(scope) {
  return new Promise((resolve, reject) => {
    wx.getSetting({
      success(res) {
        if (!res.authSetting[scope]) {
          wx.authorize({
            scope: scope,
            success() {
              resolve() // 无返回参数
            },
            fail(e) {
              console.error(e)
              wx.showModal({
                title: '无权操作',
                content: '需要获得您的授权',
                showCancel: false,
                confirmText: '立即授权',
                confirmColor: '#e64340',
                success(res) {
                  wx.openSetting();
                },
                fail(e) {
                  console.error(e)
                  reject(e)
                },
              })
            }
          })
        } else {
          resolve() // 无返回参数
        }
      },
      fail(e) {
        reject(e)
      }
    })
  })
}

module.exports = {
  checkSession,
  checkToken,
  checkHasLogined,
  wxaCode,
  appletLogin,
  accountLogin,
  loginOut,
  checkAndAuthorize
}