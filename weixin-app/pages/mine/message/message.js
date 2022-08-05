const {
  post
} = require("../../../utils/api");
const app = getApp()
const {
  startTime,
  stopTime
} = require('../../../utils/stopWatch')
const AUTH = require("../../../utils/auth");

Page({

  /**
   * 页面的初始数据
   */
  data: {
    array: ['男', '女'],
    userInfo: {},
    nickName: '',
    sex: '',
    birthdate: '',
    phone: '',
    email: '',
    source: '', //页面来源。从哪个页面跳转过来的
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {},

  //记录行为埋点
  getLoadingData: function (source, withParameters, reportId) {
    startTime();
    //调用进入页面的行为埋点方法
    app.addIntoActionPoint(source, withParameters, reportId);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

    this.getLoadingData(this.data.source, null, null);
    this.setData({
      userInfo: wx.getStorageSync('userInfo')
    });
    this.getSex();
    this.setData({
      nickName: this.data.userInfo.nickName,
      gender: this.data.userInfo.gender,
      birthdate: this.data.userInfo.birthdate,
      email: this.data.userInfo.email,
      phone: this.data.userInfo.phone,
    });

  },

  bindDateChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      birthdate: e.detail.value
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    let endTime = stopTime();
    //调用离开页面的行为埋点方法
    app.addLeaveActionPoint(null, endTime);
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    let endTime = stopTime();
    //调用离开页面的行为埋点方法
    app.addLeaveActionPoint(null, endTime);
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  getSex() {
    if (wx.getStorageSync('userInfo').gender == 1) {
      this.setData({
        sex: '男'
      })
    } else {
      this.setData({
        sex: '女'
      })
    }
  },

  //点击提交
  submit(e) {
    this.checksession().then((res) => {
      let reg1 = /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
      if (this.data.phone != null) {
        if (!reg1.test(this.data.phone.toString())) {
          wx.showToast({
            title: '手机格式错误！',
            icon: 'error',
          });
          return
        }
      }
      let reg = /^([\w+\.])+@\w+([.]\w+)+$/;
      if (this.data.email != null) {
        if (!reg.test(this.data.email.toString())) {
          wx.showToast({
            title: '邮箱格式错误！',
            icon: 'error',
          });
          return
        }
      }

      post({
        className: "user",
        method: "upDateUser",
        data: {
          nickName: this.data.nickName,
          gender: this.data.gender,
          birthdate: this.data.birthdate,
          phone: this.data.phone,
          email: this.data.email,
        }
      }).then(res => {
        if (res.data.code == 200) {
          this.getUser();
          wx.showToast({
            title: '提交成功',
          });
          setTimeout(() => {
            wx.navigateBack({
              delta: 1,
            });
          }, 2000);
        }
      });
    }).catch(err => {
      let one_login_date = wx.getStorageSync('one_login_date');
      if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
        wx.showModal({
          title: '提示',
          content: '未登录，请进行登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine',
            })
          }
        });
      } else {
        wx.showModal({
          title: '提示',
          content: '未登录，请先登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine',
            })
          }
        });
      }
    });
  },

  getUser() {
    post({
      className: "user",
      method: "getUser",
      data: "",
    }).then((res) => {
      if (res.data.code == 200) {
        this.setData({
          userInfo: res.data.data
        })
        console.log("登录成功res.data.data",res.data.data)
        wx.setStorageSync('userInfo', res.data.data);
      }
    })
  },

  bindPickerChange: function (e) {
    console.log('picker发送选择改变，携带值为', e)
    this.setData({
      gender: e.detail.value
    })
  },

  //点击注销
  logout: function () {
    this.checksession().then((res) => {
      wx.removeStorageSync('userInfo');
      wx.removeStorageSync('token');
      wx.switchTab({
        url: '../mine'
      })
    }).catch(err => {
      let one_login_date = wx.getStorageSync('one_login_date');
      if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
        wx.showModal({
          title: '提示',
          content: '未登录，请进行登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine',
            })
          }
        });
      } else {
        wx.showModal({
          title: '提示',
          content: '未登录，请先登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine',
            })
          }
        });
      }
    });
  },

  //验证登录是否过期
  checksession() {
    return new Promise((resolve, reject) => {
      AUTH.checkHasLogined().then(isLogined => {
        if (isLogined) {
          resolve("checkSession--成功");
        } else {
          reject("checkSession--失败");
        }
      });
    })
  },

})