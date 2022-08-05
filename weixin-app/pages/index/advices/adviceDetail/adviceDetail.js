// pages/index/advices/adviceDetail/adviceDetail.js
const app = getApp();
const { startTime, stopTime } = require('../../../../utils/stopWatch')
Page({

  /**
   * 页面的初始数据
   */
  data: {
      source: '',       //页面来源，是从哪个页面跳转过来的
      content: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      source: options.source,
      content: options.content
    })
  },

  getLoadingData: function(source,withParameters,reportId){
    app.addIntoActionPoint(source,withParameters,reportId);
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
    this.getLoadingData(this.data.source, this.data.withParameters, null);
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    let endTime = stopTime();
    //调用添加行为埋点的方法添加行为埋点信息
    app.addLeaveActionPoint(null,endTime);
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    let endTime = stopTime();
    //调用添加行为埋点的方法添加行为埋点信息
    app.addLeaveActionPoint(null,endTime);
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