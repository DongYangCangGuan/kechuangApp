// pages/product/entfirm/entfirm.js
// index.js
// 获取应用实例
const {
    post
} = require("../../../utils/api");
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        myBackground: "../../../images/product/bg2.png",
        noReadNumber1:[]
    },
    // onLoad() {
    //     this.getLoadingData();
    // },

    goSearch(e) {
        console.log("#33333",e.currentTarget.dataset.theme2.code)
        var id = e.currentTarget.dataset.id;//使用event.currentTarget.dataset.XX获取内容
        console.log("66666",id);
        if(e.currentTarget.dataset.theme2.code === "21") {
            wx.navigateTo({
            url: './loans/loans?code=' + e.currentTarget.dataset.theme2.code
            //   success: (res => {
            //       res.eventChannel.emit('itemName', itemName);
            //       res.eventChannel.emit('code', e.currentTarget.dataset.theme2.code);
            //   })
            })
        }else {
          wx.navigateTo({
            url: './loans/nextpage/nextpage?code=' + e.currentTarget.dataset.theme2.code
            // success: (res => {
            //     res.eventChannel.emit('itemName', itemName);
            //     res.eventChannel.emit('investigationcode', e.currentTarget.dataset.theme2.code);
            // })
          })
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getLoadingData();
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
      //请求后端，在页面加载时拿到轮播图数据以及添加行为埋点数据
      getLoadingData: function () {
        console.log("999991")
        post({
            className: "dictionary",
            method: "selectByParent",
            data: {parentId:'2',
                   kind:'producttype'
                }
        }).then(res => {
            
            this.setData({
                noReadNumber1: res.data.data
            })
            console.log("-------------",this.data.noReadNumber1)
        })
    },

      //返回首页
    gohomepage:function(){
        console.log("aaad")
        wx.switchTab({
        url: '../../index/index'
        })
        
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