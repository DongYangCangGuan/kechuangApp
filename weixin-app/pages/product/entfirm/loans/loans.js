// pages/product/entfirm/loans/loans.js
const {
    post
} = require("../../../../utils/api");
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        myBackground4: app.globalData.myBackground4,
        investigation:[],
        isloan:''
    },
    
    goQuestion() {
        wx.navigateTo({
            url: './question/question',
            success: (res => {
                res.eventChannel.emit('isloan', this.data.isloan);
            })
        })
    },
    goNextpage(e) {
        console.log("ssssssssssssss",e)
        wx.navigateTo({
            url: './nextpage/nextpage?code=' + e.currentTarget.dataset.investigation.code
            // success: (res => {
            //     res.eventChannel.emit('investigationcode', e.currentTarget.dataset.investigation.code);
            // })
        })
    },
    onLoad: function (options){
        this.setData({
            isloan: options.code
        })
        console.log("this.data.isloan",this.data.isloan);
        this.investigationlist(options.code);
    },
    /**
     * 生命周期函数--监听页面加载
     */
    // onLoad: function (options) {
    //     const eventChannel = this.getOpenerEventChannel()
    //     eventChannel.on('parentJump', Data => {
    //       console.log("code",Data)
     
    //   })
    // },

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
    investigationlist: function(d){
        console.log("32333")
        post({
            className: "dictionary",
            method: "selectByParent",
            data: {parentId:d}
        }).then(res => {
            
            this.setData({
                investigation: res.data.data
            })
            console.log("-------------",this.data.investigation)
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