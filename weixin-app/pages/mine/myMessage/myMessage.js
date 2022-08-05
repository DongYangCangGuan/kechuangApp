// pages/mine/getMessage/getMessage.js
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
    dataList:[],
    notesId:'',
    userId:'',
    isShow:false,
    itemsList:[],
    title: '',
    content: '',
    time: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let currentPageId = getCurrentPages();
    this.setData({
        currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id
    })
    
    
  },
  //获取数据
  getNotes: function(){
    let param = {
      page: {
          pageIndex: 1,
          pageSize: 10
      },
  }
  post({
      className: "notes",
      method: "getNotes",
      data: param
  }).then((res) => {
    console.log(res,'12321231321');
      if (res.data.code == 200) {
        console.log(res,'456654465645465');
        this.setData({
          dataList: res.data.data.dataList,
      })
      }
  }).catch(error => {
  })
}, 


updateStatus:function(e){
  console.log(e,'tttttttttttttttttttt');
  this.setData({
    notesId: e.currentTarget.dataset.a.id,
    title: e.currentTarget.dataset.a.title,
    content: e.currentTarget.dataset.a.content,
    time: e.currentTarget.dataset.a.time,
  })
  wx.navigateTo({
    url: '../myMessage/myMessageItems/myMessageItems?notesId=' + this.data.notesId + "&title=" + this.data.title + "&content=" + this.data.content + "&time=" +this.data.time
  })
  let param = {
    notesId: e.currentTarget.dataset.a.id,
}
post({
    className: "notes",
    method: "updateStatus",
    data: param
}).then((res) => {
  console.log(res,'77777777777');
    if (res.data.code == 200) {
      console.log(res,'888888888888888');
      this.setData({
        dataList: res.data.data.dataList,
    })
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
    this.getNotes();
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