const http = require("../../utils/api");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        pageIndex: 0,
    },

    /**
     * 获取直播页面信息
     */
    getLiveDatas: function () {
        let params = {
            openId: wx.getStorageSync('openId'),
            token: wx.getStorageSync('token')
        }
        http.post({
            url: "getLiveData",
            data: params
        }).then((res) => {
            if (this.data.pageIndex == 0) {
                this.setData({
                    data: res.data
                })
            } else {
                var info = [...this.data.data.liveList, ...res.data.liveList]
                this.setData({
                    data: {
                        liveList: info
                    }
                })
                console.log("data数据：", this.data)
            }
            console.log("访问信息：", res)
        }).catch(err => {
            console.log("错误信息：", err)
        })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        //this.getLiveDatas();
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
        // wx.showModal({
        //     title: 'e视功能暂未上线，敬请期待',
        //     confirmText: '我知道了',
        //     confirmColor: '#165689',
        //     showCancel: false,
        // });
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
        this.setData({
            pageIndex: this.data.pageIndex + 1
        })
        this.getLiveDatas();
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },
    // 点击 tab 时触发
    onTabItemTap: function () {},
})