// pages/product/entfirm/loans/nextpage/nextpage.js
const {
    post
} = require("../../../../../utils/api");
const app = getApp();

Page({

    /**
     * 页面的初始数据
     */
    data: {
        currentIndex: 1,
        // myBackground: "../../../../../images/product/bg_linear.png"
        myBackground5: app.globalData.myBackground5,
        listData: [],
        Latest:[],
        HotList:[],
        loancode:'',
        userInfo:{}
    },

    getCurrentIndex(e) {
        console.log(e.currentTarget.id);
        this.setData({
            currentIndex: e.currentTarget.id //当前选中的分类index，用在样式上
        });
        if(this.data.currentIndex==2){
            this.getLatest()
        }else if(this.data.currentIndex==3){
            this.getHot();
        }else if(this.data.currentIndex==1){
            this.getAnswersMsg();
        }
    },
    changea(e){ 
        let a='';
        // const eventChannel = this.getOpenerEventChannel()
        // eventChannel.on('investigationcode', Data => {
          if(this.data.loancode=='211'){
            a="流动资金贷款";
        }else if(this.data.loancode=='212'){
            a="股+债";
        }else if(this.data.loancode=='213'){
            a="上市或并购贷款";
        }else if(this.data.loancode=='214'){
            a="供应链金融贷款";
        }else if(this.data.loancode=='11'){
          a="贷款";
      }
        console.log("牛牛牛",this.data.loancode)
        
        wx.setNavigationBarTitle({
           title:  a
         })
     // })
       
        
    },
   
    goDetail(e) {
        console.log("产品详情",e)
        wx.navigateTo({
            url: '../detail/detail',
            success: (res => {
                res.eventChannel.emit('recommendationId', e.currentTarget.dataset.productlist.id);
            })
        })
    },
    //获取最热产品
    getHot(d){
        console.log("产品最热：",this.data.loancode)
        post({
            className: "product",
            method: "getHot",
            data: {type :this.data.loancode}
        }).then(res => {
            
            this.setData({
                Latest: res.data.data
            })
            console.log("--------this.data.HotList-----",this.data.Latest);
           
        })
    },
    //
    getListData(d) {
        console.log('进入getListData');
        post({
            url:"getListData"
        }).then(res => {
            console.log('res11111111',res);
            this.setData({
                listData: res.data.listData
            })
            console.log(this.data.listData)
        })
    },
    //获取产品推荐
    getAnswersMsg(){
        console.log("产品推荐1：",this.data.loancode)
        post({
            className: "product",
            method: "getRecommend",
            data: {userId :this.data.userInfo.userId,type:this.data.loancode}
        }).then(res => {
            this.setData({
                Latest: res.data.data
            })
            console.log("--------this.data.getAnswersMsg-----",this.data.Latest);
           
        })
    },
    //根据产品类型获取产品/最新/
    getLatest :function(d){
        console.log("产品最新类型",this.data.loancode)
        post({
            className: "product",
            method: "getLatest",
            data: {type :this.data.loancode}
        }).then(res => {
            
            this.setData({
                Latest: res.data.data
            })
            console.log("--------this.data.Latest-----",this.data.Latest);
           
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.log('进入onload');
        console.log('options=',options);
        this.checkSeesion();
        this.setData({
            loancode: options.code
        })
        console.log('this.data.loancode',this.data.loancode);
        // this.getAnswersMsg(this.data.loancode);
        this.getAnswersMsg();
        this.changea(this.data.loancode);
        
    },
    // onLoad(){
    //   this.checkSeesion();
    //   console.log("11111111111q")
    //   const eventChannel = this.getOpenerEventChannel()
    //   eventChannel.on('investigationcode', Data => {
    //     console.log("investigationcode",Data)
        
    //     this.data.loancode=Data;
    //     this.getAnswersMsg(this.data.loancode);
    //     console.log("this.data.loancode",this.data.loancode)
        
    //       if(this.data.loancode==2){
    //           this.getLatest(this.data.loancode)
    //       }else if(this.data.loancode==3){
    //           this.getHot(this.data.loancode);
    //       }else if(this.data.loancode==1){
    //           this.getAnswersMsg(this.data.loancode);
    //       }
    //   //   this.getAnswersMsg();
      
        
        
    // })
    // this.changea(this.data.loancode);
        
    // },
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
          const eventChannel = this.getOpenerEventChannel()
          eventChannel.on('investigationcode', Data => {
          this.data.loancode=Data;
          this.getLatest();
         })
        }
        
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