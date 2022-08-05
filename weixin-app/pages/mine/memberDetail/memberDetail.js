// pages/mine/member_detail/member_detail.js
// 获取应用实例
const {
  post
} = require("../../../utils/api");
const {
  formatTime2
} = require('../../../utils/util')
const app = getApp()
const AUTH = require("../../../utils/auth");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id:'',

    obj:{
      obj1:{
        key1:{}
      }
    },
    memberDetailList: {
      enterpriseName:"上海信医科技有限公司"
    },

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    this.memberDetailList={}
    console.log("走到这")
    this.memberDetailList=wx.getStorageSync('memberDetailList')
    console.log("this.memberDetailList",this.memberDetailList)
    this.setData({memberDetailList:wx.getStorageSync('memberDetailList')})
    console.log("this.memberDetailList123",wx.getStorageSync('memberDetailList'))
    
      this.data.id=this.data.memberDetailList.id;
  },


// 获取数据
  // getMemberDetail: function () {
  //     console.log('id=',this.data.id);
   
  // },
  // 通过
  getAdopt(){
    let param={
      id:this.data.id,
      approvalstatus:'1',
      userId: this.data.memberDetailList.userId
    }
    post({
      className: "Approve",
      method: "approve",
      data: param
  }).then((res) => {
        if (res.data.code == 200) {
        //   wx.showToast({
        //     title: '审批通过',
        //     icon: 'success',
        //     duration: 1500,
        //     mask: true,
        //     success: (res) => {},
        //     fail: (res) => {},
        //     complete: (res) => {},
        // })   
        // wx.navigateTo({
        //     url: '../member/member'
        // })
        // wx.switchTab({
        //   url: '../member/member'
        // })
        // wx.navigateBack({
        //   delta: 2
        // })
        wx.showModal({
          title: '提示',
          content: '审批成功！',
          showCancel: false,
          success: res => {
              //要延时执行的代码
              wx.switchTab({
                  url: '../../mine/mine',
                })
              // setTimeout(() => {
                
              // }, 2000)
            }
        });
      }
    //   this. getApproveList ();
  }).catch(error => {
  })
  },
  getRefuse() {
    console.log(this.data.memberId,'拒绝ID');
    let param={
        id:this.data.id,
        approvalstatus:'2',
        userId: this.data.memberDetailList.userId
    }
    post({
      className: "Approve",
      method: "approve",
      data: param
  }).then((res) => {
    if (res.data.code == 200) {
      console.log('拒绝');
      wx.showModal({
        title: '提示',
        content: '审批成功！',
        showCancel: false,
        success: res => {
            //要延时执行的代码
            wx.switchTab({
                url: '../../mine/mine',
              })
            // setTimeout(() => {
              
            // }, 2000)
          }
      });
        
        // wx.navigateTo({
        //  url: '../member/member'
        // })
        // wx.navigateBack({
        //     delta: 2
        // })
    }
  }).catch(error => {
  }) 
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
    this.memberDetailList={}
    console.log("走到这")
    this.memberDetailList=wx.getStorageSync('memberDetailList')
    console.log("this.memberDetailList",this.memberDetailList)
    this.setData({memberDetailList:wx.getStorageSync('memberDetailList')})
    console.log("this.memberDetailList123",wx.getStorageSync('memberDetailList'))
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