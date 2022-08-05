// pages/product/entfirm/loans/question/submitdetail/submitdetail.js
const {
    post
} = require("../../../../../../utils/api");
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        myBackground1: app.globalData.myBackground8,
        myBackground2: app.globalData.myBackground9,
        queryData: [],
        userInfo:{},
        feedbackList:[],
        productRecommendationList:[]
    },

    goDetail(e) {
        console.log("获取产品详情Id",e.currentTarget.dataset.recommendation.id)
        wx.navigateTo({
            url: '../../detail/detail',
            success: (res => {
                res.eventChannel.emit('recommendationId', e.currentTarget.dataset.recommendation.id);
            })
        })
    },

    //获取产品反馈
    getAnswersMsg :function(){
        post({
            className: "product",
            method: "getAnswersMsg",
            data: {userId :this.data.userInfo.userId}
        }).then(res => {
            console.log("uuuuuuuuuuuuuu",res.data.data)
            this.setData({
                feedbackList: res.data.data
            })
            console.log("aaaaaaaaaaaaaa",this.data.feedbackList);
           
        })
    },
    //获取产品推荐信息
    getRecommend:function(){
        post({
            className: "product",
            method: "getRecommend",
            data: {userId : this.data.userInfo.userId,type:'21'}
        }).then(res => {
            console.log("this.productRecommendationList:",res.data.data)
            this.setData({
                productRecommendationList: res.data.data
            })
            console.log("aaaaaaaaaaaaaa",this.data.productRecommendationList);
           
        })
    },
    //获取登录信息
    checkSeesion() {
        console.log("rrrrr")
        this.setData({
          userInfo: wx.getStorageSync('userInfo')
        })
        console.log('userInfo',this.data.userInfo);
        if(this.data.userInfo === '') {
          let one_login_date = wx.getStorageSync('one_login_date');
          console.log('one_login_date',one_login_date);
          if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
              wx.showModal({
                  title: '提示',
                  content: '未登录，请进行登录',
                  showCancel: false,
                  success: res => {
                    wx.switchTab({
                      url: '../mine/mine',
                    })
                  }
              });
          } else {
              wx.showModal({
                  title: '提示',
                  content: '未登录，请先登录',
                  showCancel: false,
                  success: res => {
                    wx.switchTab({
                      url: '../mine/mine',
                    })
                  }
              });
          }
        } else {
          console.log('验证urole');
          this.getAnswersMsg();
          this.getRecommend();
        }
        
      },

    /**
     * 生命周期函数--监听页面加载
     */
    // onLoad: function (options) {
    //     let queryData = JSON.parse(options.data)
    //     // this.setData({
    //     //     queryData: queryData
    //     // })
    //     let dataList = []
    //     for(var i=0;i<queryData.loan_reason.length;i++) {
    //         let e = queryData.loan_reason[i]
    //         console.log(e)
    //         dataList.push(e.substring(2, e.length))
    //     }
    //     dataList.push(queryData.company_stage.substring(2, queryData.company_stage.length))
    //     dataList.push(queryData.expect_line.substring(2, queryData.expect_line.length))
    //     this.setData({
    //         queryData: dataList
    //     })
    // },
    onLoad(){
        this.checkSeesion();
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