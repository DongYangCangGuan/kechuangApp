// pages/questionnaireres/questionnaireres.js
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
    kindId:'',
    itemList:[],
    duoXuanShow:true,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      kindId: options.kindId
  })
    wx.setNavigationBarColor({
      frontColor: '#000000',
      backgroundColor: '#CBE3F4',
    }),
        this. getAnswerDetail ();
  },
  getQesTJ(e){
    wx.navigateTo({
      url: '../../product/entfirm/loans/question/submitdetail/submitdetail',
      success: (res => {
        res.eventChannel.emit('investigationcode', 1);
      })
    })
  },
  getAnswerDetail: function () {
    console.log("this.data.kindId",this.data.kindId)
    let param = {
        page: {
          kindId: this.data.kindId
        },
    }
    post({
        className: "Answer",
        method: "getAnswerDetail",
        data: param
    }).then((res) => {
      console.log(res,'666666666')
        if (res.data.code == 200) {
          console.log(res,'9999999999999')
            this.setData({
                itemList: res.data.data,
            })
            // for(let i = 0;i<itemList.length;i++){
            //   if(itemList[i].questionType == "单选"){
            //     this.setData({
            //       duoXuanShow:false
            //     })
            //   }
            // }
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