
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {}
  },

  goDetail(e) {
    let navItem = e.currentTarget.dataset.theme;
    console.log('111',navItem);
    if(navItem === "entfirm") {
      wx.navigateTo({
        url: './entfirm/entfirm',
        success: (res => {
            res.eventChannel.emit('navitem', navItem);
        })
      })
    } else if(navItem === "gp") {
      wx.navigateTo({
        url: './gp/gp',
        success: (res => {
            res.eventChannel.emit('navitem', navItem);
        })
      })
    }
    
  },

  checkSeesion() {
    this.setData({
      userInfo: wx.getStorageSync('userInfo')
    })
    console.log('userInfo',this.data.userInfo);
    if(this.data.userInfo === '') {
      let one_login_date = wx.getStorageSync('one_login_date');
      console.log('one_login_date',one_login_date);
      if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
          wx.showModal({
              title: '提示',
              content: '未登录，请进行登录',
              showCancel: false,
              success: res => {
                wx.switchTab({
                  url: '../mine/mine',
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
                  url: '../mine/mine',
                })
              }
          });
      }
    } else {
      console.log('验证urole');
      // if(this.data.userInfo.urole === 1) {
      //   wx.navigateTo({
      //     url: './gp/gp'
      //   })
      // } else if(this.data.userInfo.urole === 0) {
      //   wx.navigateTo({
      //     url: './entfirm/entfirm'
      //   })
      // }
    }
    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },
  gohomepage:function(){
    console.log("aaad")
    wx.switchTab({
      url: '../../index/index'
    })
    
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.checkSeesion();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
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