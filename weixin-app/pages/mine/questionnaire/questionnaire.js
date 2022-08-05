// const http = require("../../../utils/api");
// const {post, login} = require("../../../utils/api");
// const app = getApp()

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
    // information:'',
    questionList: [],
    kindId: '',
    productRecommendationList: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // this.getAboutus();
    this.getAnswerList();
    this.getRecommend();
  },
  //获取数据
  getAnswerList() {
    let param = {
      page: {
        pageIndex: 1,
        pageSize: 10
      },
    }
    post({
      className: "Answer",
      method: "getAnswerList",
      data: param

    }).then((res) => {
      console.log(res,'666666666666');
      if (res.data.code == 200) {
        console.log(res,'099999999999');
        this.setData({
          questionList: res.data.data.dataList,
        })
      }
    }).catch(error => {})
  },

  //获取产品推荐信息
getRecommend:function(){
    let userInfo = wx.getStorageSync('userInfo');
    post({
        className: "product",
        method: "getRecommend",
        data: {userId : userInfo.userId}
    }).then(res => {
        // console.log("this.productRecommendationList:",res.data.data)
        this.setData({
            productRecommendationList: res.data.data
        })
        console.log("aaaaaaaaaaaaaa",this.data.productRecommendationList);
       
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

  },

  //去问答详情页

  getQes(e) {
      console.log('e====',e);
    const kindId = e.currentTarget.dataset.b
    this.setData({
      kindId
    })
    let param = {
      page: {
        pageIndex: 1,
        pageSize: 10
      },
    }
    post({
      className: "Answer",
      method: "getAnswerList",
      data: param

    }).then((res) => {
      if (res.data.code == 200) {
        this.setData({
          kindId: this.data.kindId
        })

        wx.navigateTo({
          url: '../questionnaireres/questionnaireres?kindId=' + this.data.kindId
        })
      }
    }).catch(error => {})
  },

  //跳转产品推荐页
  getQesTJ(e){
    wx.navigateTo({
      url: '../../product/entfirm/loans/question/submitdetail/submitdetail',
      success: (res => {
        res.eventChannel.emit('investigationcode', 1);
      })
    })
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

  },

  // getAboutus( ) {
  //   post({
  //       className: "aboutus",
  //       method: "getAboutus",
  //       data: {}
  //     })
  //     .then(res => {
  //       this.setData({
  //         information: res.data.data.information
  //       });
  //     }).catch(err => {
  //     })

  // }



})