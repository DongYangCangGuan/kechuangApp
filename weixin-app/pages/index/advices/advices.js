// pages/index/advices/advices.js
// 获取应用实例
const {
  post
} = require("../../../utils/api");
const app = getApp();
const {
  formatTime2
} = require('../../../utils/util')
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
    adviceList: [], //每一次获取的消息列表
    pageIndex: 1, //页面index
    pageSize: 10, //每页个数
    showAdvicesList: [], //最终展示的消息列表
    showBottom: false, //是否展示上拉加载完提示
    currentPageId: '', //当前页面id
    source: '', //页面来源，是从那个页面过来的
    withParameters: '', //页面携带过来的参数
    showRedNoRead: false, //默认不展示是否未读、已读
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let currentPageId = getCurrentPages();
    this.setData({
      currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id  pages/index/index
      // startTime: new Date().getTime() / 1000
    })
    this.setData({
      source: options.source
    })
  },

  getLoadingData: function (source, withParameters, reportId) {
    app.addIntoActionPoint(source, withParameters, reportId);
  },

  //访问后端，拿到当前登录用户的消息列表
  getAdvices: function (pageIndex) {
    //从缓存中拿到当前用户的信息
    //let userInfo = wx.getStorageSync('userInfo');
    this.checksession().then((res) => {
      this.setData({
        showRedNoRead: true, //用户登录展示已读、未读
        showAdvicesList: []
      }); //置空原来的消息列表
      let data = {
        page: {
          pageIndex: pageIndex,
          pageSize: this.data.pageSize
        }
      }
      post({
        className: "notes",
        method: "getNotes",
        data: data
      }).then((res) => {
        if (res.data.code == 200) {
          this.setData({
            adviceList: res.data.data
          });
          for (let item of this.data.adviceList) {
            item.modifyTime = formatTime2(new Date(item.modifyTime))
          }
          let data = [...this.data.showAdvicesList, ...this.data.adviceList]
          this.setData({
            showAdvicesList: data
          })
        }
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
              url: '../../mine/mine',
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
              url: '../../mine/mine',
            })
          }
        });
      }
    })
  },

  //点击当前消息触发
  clickAdvice: function (e) {
    this.checksession().then((res) => {
      wx.navigateTo({
        url: '../advices/adviceDetail/adviceDetail?source=' + this.data.currentPageId + '&content=' + e.currentTarget.dataset.item.content
      })
      let item = e.currentTarget.dataset.item;
      if (item.readStatus != 1) {
        if (item.taskType == 'user') {
          post({
            className: "notes",
            method: "updateStatus",
            data: {
              id: item.notesId
            }
          }).then((res) => {}).catch(err => {
            wx.showModal({
              title: '提示',
              content: '系统故障，请稍后重新！',
              showCancel: false
            });
          })
        } else {
          post({
            className: "notesDetail",
            method: "addNotes",
            data: {
              id: item.notesId
            }
          }).then((res) => {}).catch(err => {
            wx.showModal({
              title: '提示',
              content: '系统故障，请稍后重新！',
              showCancel: false
            });
          })
        }
      }
    }).catch(err => {
      let one_login_date = wx.getStorageSync('one_login_date');
      if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
        wx.showModal({
          title: '提示',
          content: '未登录，请进行登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../../mine/mine',
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
              url: '../../mine/mine',
            })
          }
        });
      }
    });
  },

  //上拉加载信息
  scrollBottm: function () {
    if (this.data.adviceList < this.data.pageSize) {
      this.setData({
        showBottom: true
      })
      return;
    }
    this.getAdvices(++this.data.pageIndex);
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

  //计算收到消息时间距离当天时间
  getTime: function () {

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
    startTime();
    this.getAdvices(this.data.pageIndex);
    this.getLoadingData(this.data.source, this.data.withParameters, null)
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    let endTime = stopTime();
    //调用添加行为埋点的方法添加行为埋点信息
    app.addLeaveActionPoint(null, endTime);
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    let endTime = stopTime();
    //调用添加行为埋点的方法添加行为埋点信息
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

  }
})