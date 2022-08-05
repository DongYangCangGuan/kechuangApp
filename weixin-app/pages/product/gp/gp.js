// pages/product/gp/gp.js
const {
    post
} = require("../../../utils/api");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        myBackground: "../../../images/product/bg2.png",
        noReadNumber1:[]
    },

    goEntfirm() {
        wx.navigateTo({
            url: '../entfirm/entfirm'
        })
    },
    gohomepage() {
        wx.switchTab({
          url: '../../index/index'
        })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getLoadingData();
    },
    goSearch(e) {
        console.log("GP:",e)
        let itemName = e.currentTarget.dataset.theme;
        // if(itemName.code === "11") {
            wx.navigateTo({
              url: '../entfirm/loans/loans',
              success: (res => {
                  res.eventChannel.emit('itemName', itemName);
              })
            })
        // }
    },
     //请求后端，在页面加载时拿到轮播图数据以及添加行为埋点数据
     getLoadingData: function () {
        post({
            className: "dictionary",
            method: "selectByParent",
            data: {parentId:'1',
                    kind:'producttype'}
        }).then(res => {
            console.log("------asdfasd-------", res.data.data)
            this.setData({
                noReadNumber1: res.data.data
            })
            console.log("-------------",this.data.noReadNumber1)
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