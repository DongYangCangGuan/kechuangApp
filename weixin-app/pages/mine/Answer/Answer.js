// pages/mine/Answer/Answer.js
const {
  post
} = require("../../../utils/api");
const app = getApp()
const AUTH = require("../../../utils/auth");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    searchData: {
      // startTime: '', //开始时间
      // endTime: '', //结束时间

  },
  dataList:{},
  questionBelong:"",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getQuestionnaireList();
  },

  //获取数据
  getQuestionnaireList: function () {
    console.log("121121")
    let param = {
        page: {
            pageIndex: 1,
            pageSize: 10
        },
        searchData: this.data.searchData,

    }
    post({
        className: "Answer",
        method: "getQuestionnaireList",
        data: param
        
    }).then((res) => {
        if (res.data.code == 200) {
          console.log(res,'用户问卷')
            this.setData({
              dataList: res.data.data.questionnaireList,
            })
            console.log('this.data.dataList===',this.data.dataList);
        }
    }).catch(error => {
    })
  }, 
  //根据问卷获取用户信息
  getFeedbackItems(e) {
    console.log(e,'iiiiiiiiiiiiiiiiiii');
    this.setData({
      questionBelong: e.currentTarget.dataset.a.questionBelong
    })
  
    wx.navigateTo({
         url: '../Answerdetail/Answerdetail?questionBelong=' + this.data.questionBelong
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})