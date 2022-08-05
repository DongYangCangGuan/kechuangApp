// pages/product/entfirm/loans/detail/detail.js
const {
    post
} = require("../../../../../utils/api");
// = require('../../utils/stopWatch')
const {
  startTime,
  stopTime
} = require('../../../../../utils/stopWatch')
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        Namne:'当前是details页面',
        myBackground: app.globalData.myBackground6,
        titleBackground: app.globalData.myBackground11,
        latestDetail:{},
        job: [],
        jobList: [],
        id: '',
        isClick: false,
        jobStorage: [],
        jobId: '',
        startTime: '', //页面进来时间
        endTime: '', //页面离开时间
        // titleBackground: "../../../../../images/product/bg_title.png"
        showDialog: false,
        userInfo:{},
        productlist:[],
        productId:"",
        personpic: 'data:image/png;base64,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',
        activityId:"",
        Contactobj:[],
    },

    showpop() {
        this.setData({
            showDialog: true
        })
        this.getContactUs();
    },
    yulan(){
      wx.showLoading({
        title: 'pdf预览准备中',
      })
      console.log("this.data.latestDetail[0].casePath",this.data.latestDetail[0].casePath)
       //这一步是下载PDF
      wx.downloadFile({
        url: this.data.latestDetail[0].casePath,
        success(res) {
          console.log(res)
          const filePath = res.tempFilePath
          setTimeout(function () {
            //这一步是打开PDF
            wx.openDocument({
              fileType: "pdf",
              filePath: filePath,
              showMenu: true,
              success: function (res) {
                console.log(res)
                wx.hideLoading({
                  success: function () {
                    console.log('关闭');
                  }
                })
               
              }
            })
          }, 300)
        },
        fail: function (e) {
          console.log(e, '网络错误');
        }
      })
    },
    onL:function(){
      var that=this
      wx.showModal({
   
        title: '提示',
   
        content: '请致电'+ that.data.Contactobj.linkPhone,
   
        success: function (res) {
   
          if (res.confirm) {//这里是点击了确定以后
   
            console.log('用户点击确定')
            that.addconsultingInfo();
            that.freeTell();
   
   
          } else {//这里是点击了取消以后
   
            console.log('用户点击取消')
   
          }
   
        }
   
      })
   
    },
    
    freeTell:function(){
      wx.makePhoneCall({
        phoneNumber: this.data.Contactobj.linkPhone,
      })
    },
    //添加咨询信息
    addconsultingInfo:function(){
      console.log("添加咨询信息")
      post({
        className: "product",
        method: "addconsultingInfo",
        data: {userId : this.data.userInfo.userId,accountmanager:this.data.Contactobj.linkMan,telephone:this.data.Contactobj.linkPhone,email:this.data.Contactobj.linkEmail}
    }).then(res => {
        console.log("添加咨询信息",res.data.data)
      //  this.setData({
      //   Contactobj : res.data.data
      //  })
        
    })
    },
    //获取联系方式
    getContactUs(){
      console.log("获取联系放肆ID",this.data.productId)
      post({
        className: "product",
        method: "getContactUs",
        data: {id : this.data.productId}
    }).then(res => {
        console.log("获取联系方式",res.data.data)
       this.setData({
        Contactobj : res.data.data
       })
       
    })
    },
    close() {
      this.setData({
        showDialog: false
    })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    // onLoad: function (options) {
       
    // },
    onLoad(){
        this.checkSeesion();
        const eventChannel = this.getOpenerEventChannel()
        eventChannel.once('recommendationId', Data => {
          this.data.activityId=Data;
          this.setData({
            productId:Data,
            Namne:"Data123"
          })
         console.log("detailproductId",this.data.productId)
         this.getrecommendatail(Data);
         console.log(wx.getStorageSync('jobData'));
         let savejob = wx.getStorageSync('jobData')//获得缓存
         let index = savejob.length-1;
         console.log(savejob[index].id);
        //  let jobid = savejob[index].id
        //  let temp= jobList[jobid] //将获得缓存后匹配的数据放入新的数组
        //  let job= [];
        //  job.push(temp);
        //  this.setData({id:index,job: job,})
      })
     
    },
    //收藏
    haveSave(e) {
        if (this.data.latestDetail[0].isCollect!= '1') {
            let jobData = this.data.jobStorage;
            jobData.push({
            jobid: jobData.length,
            id: this.data.job.id
            })
            wx.setStorageSync('jobData', jobData);//设置缓存
            //  wx.showToast({
            //  title: '已收藏',
            //  });
            post({
                className: "home",
                method: "addCollect",
                data: {userId : this.data.userInfo.userId,productId:this.data.productId}
            }).then(res => {
            console.log("已收藏")
            this.data.latestDetail[0].isCollect=1;
            
            })
        } else {
            // wx.showToast({
            // title: '已取消收藏',
            // });
            post({
                className: "home",
                method: "delCollect",
                data: {userId : this.data.userInfo.userId,productId:this.data.productId}
              }).then(res => {
              console.log("取消收藏",res.data.data)
              this.data.latestDetail[0].isCollect=0;
              })
        }
        this.setData({
         isClick: !this.data.isClick
        })
        },
    getrecommendatail:function(d){
      startTime();
      //调用进入页面的行为埋点方法
      console.log("调用进入页面的行为埋点方法a",this.data.productId)
      app.addIntoActionPoint(this.data.productId, "22221", "33333");
        post({
            className: "product",
            method: "getLatest",
            data: {id : d,userId : this.data.userInfo.userId}
        }).then(res => {
            console.log("uuuuuuuuuuuuuu",res.data.data)
            this.setData({
                latestDetail: res.data.data
            })
            console.log("this.data.latestDetail",this.data.latestDetail);
            if(this.data.latestDetail[0].isCollect=='1'){
                console.log("111已经收藏")
                this.setData({
                    isClick: this.data.isClick
                   })
            }else{
                console.log("222没有收藏")
                this.setData({
                    isClick: !this.data.isClick
                   })
            }
           
        })
    },

    onHide() {
      let endTime = stopTime();
      //调用离开页面的行为埋点方法
      app.addLeaveActionPoint(null, endTime);
    },

    checkSeesion() {
        this.setData({
          userInfo: wx.getStorageSync('userInfo')
        })
        console.log('userInfo', this.data.userInfo);
        if (this.data.userInfo === '') {
          let one_login_date = wx.getStorageSync('one_login_date');
          console.log('one_login_date', one_login_date);
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
          if (this.data.userInfo.urole === 1) {
            wx.navigateTo({
              url: './gp/gp'
            })
          } else {
            wx.navigateTo({
              url: './entfirm/entfirm'
            })
          }
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