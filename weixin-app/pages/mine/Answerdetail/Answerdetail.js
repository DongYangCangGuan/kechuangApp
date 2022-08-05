// pages/mine/Answerdetail/Answerdetail.js
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
    questionBelong:"",
    dataList:{},
    inputShowed: false,
    inputVal: '',
    paginationData: {
      currentPage: 1,
      pageSize: 10,
      total: 0
    },
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.setData({
      questionBelong:options.questionBelong
    })
    this.getAnswerMember();
  },
  showInput() {
    this.setData({
      inputShowed: true,
    });
  },

  inputTyping(e) {
    console.log("eee",e)
    this.setData({
      inputVal: e.detail.value,
    });
    this.getAnswerMember()
  },

  hideInput() {
    this.setData({
      inputVal: '',
      inputShowed: false,
    });
  },

  //获取数据
  getAnswerMember: function () {
    console.log("this.data.activityEventId",this.data.questionBelong)
      let param = {
            // activityEventId: this.data.activityEventId,
            page: {
              pageIndex: this.data.paginationData.currentPage,
              pageSize: this.data.paginationData.pageSize,
          },
          searchdata: {
              id: this.data.questionBelong,
              realName: this.data.inputVal
          }
      }
      
      post({
          className: "Answer",
          method: "getAnswerMember",
          data: param
        }).then((res) => {
            console.log('66666666666667',res)
            if (res.data.code == 200) {
              // let _that = this;
              console.log('aaaaaaaaaaaaaaaaa',res.data.data.memberId)
              this.setData({
                dataList: res.data.data.dataList,
              })
              // _that.setData({
              //     memberId:res.data.data.memberId,
              //     name:res.data.data.name,
              //     phone:res.data.data.phone,
              //     sdList: res.data.data.sdList,
              //     customization:res.data.data.customization,
              //     remark:res.data.data.remark
              // })
              console.log('this.data.memberId===',this.data.dataList);
            }
            
            // this.checkReadymade();
            // this.checkCustomization();
        }).catch(error => {
        })
  }, 

  getFeedbackItems:function(){
    wx.navigateTo({
      url: '../AnswerdetailQuestion/AnswerdetailQuestion?questionBelong=' + this.data.questionBelong
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