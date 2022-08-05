
// // index.js
// // 获取应用实例
// const {
//     post,
//     fileDownload
// } = require("../../../utils/api");
// const {
//     formatTime2
// } = require('../../../utils/util')
// const app = getApp()
// const {
//     startTime,
//     stopTime
// } = require('../../../utils/stopWatch')
// const AUTH = require("../../../utils/auth");

// Page({
//     data: {
//         kindInfo: [],
//         returnResult: [],
//         labelList: [], //每一份报告图标列表
//         industryList: [], //每一份报告的行业列表
//         navInfo: [], //全部分页数据
//         pageIndex: 1, //使用swiper做分页，当前页
//         pageSize: 10, //每页个数
//         pageCount: 0, //一共有多少页
//         showBottom: false, //显示上拉到底
//         windowHeight: 0, //页面高度
//         scrollHeight: 0, //信息滚动条高度
//         userInfo: {}, //当前用户信息
//         source: '', //来源，从哪个页面跳转过来
//         currentPageId: '', //当前页面id
//         noData: '/images/report/noData.png'
//     },

//     onLoad: function (options) {
//         let currentPageId = getCurrentPages();
//         this.setData({
//             currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id  pages/index/index
//             source: options.source
//         })
//     },

//     // 生命周期函数--监听页面初次渲染完成
//     onReady: function () {
//         this.animation = wx.createAnimation()
//     },

//     onShow: function () {
//         this.setData({
//             kindInfo: []
//         })
//         this.getLoadingData(this.data.source, null, null);
//     },
//     onHide: function () {
//         let endTime = stopTime();
//         //调用离开页面的行为埋点方法
//         app.addLeaveActionPoint(null, endTime);
//     },
//     onUnload: function () {
//         let endTime = stopTime();
//         //调用离开页面的行为埋点方法
//         app.addLeaveActionPoint(null, endTime);
//     },
//     onPullDownRefresh: function () {
//         // this.setData({
//         //   kindInfo: [],
//         //   pageIndex: 1
//         // })
//     },
//     onReachBottom: function () {
//     },


//     // 打开搜索页
//     goSearch() {
//         console.log("goSearch");
//         wx.navigateTo({
//             url: '../collect/search/search',
//             success: (res => {
//                 //res.eventChannel.emit('theme', this.data.reportTheme);
//             })
//         })
//     },

//     // 页面加载数据，添加埋点数据，拿到收藏数据
//     getLoadingData: function (source, withParameters, reportId) {
//         startTime();
//         //调用进入页面的行为埋点方法
//         app.addIntoActionPoint(source, withParameters, reportId);
//         let param = {
//             page: {
//                 pageIndex: this.data.pageIndex,
//                 pageSize: this.data.pageSize
//             }
//         }
//         post({
//             className: "collect",
//             method: "getCollect",
//             data: param
//         }).then((res) => {
//             console.log(res);
//             if (res.data.code == 200) {
//                 this.setData({
//                     returnResult: res.data.data.dataList
//                 })
//                 this.data.returnResult.forEach((item) => {
//                     let date = new Date(item.report.modifyTime);
//                     item.report.modifyTime = formatTime2(date);
//                     this.getPic(item.report.pic).then(res => {
//                         item.report.pic = res;
//                         this.setData({
//                             kindInfo: this.data.kindInfo
//                         });
//                     })
//                 });

//                 let kindinfo = this.data.returnResult;
//                 let data = [...this.data.kindInfo, ...kindinfo];
//                 this.setData({
//                     kindInfo: data
//                 })
//             } else {
//             }
//         }).catch(error => {
//             console.log(error);
//         })
//     },


//     //scroll-view触底触发
//     bindscrolltolower: function () {
//         if (this.data.returnResult.length < this.data.pageSize) {
//             this.setData({
//                 showBottom: true
//             })
//             return;
//         }
//         this.setData({
//             pageIndex: this.data.pageIndex + 1
//         });
//         this.getLoadingData();
//     },

//     //点击每个推荐的资讯进入详情页面
//     viewDetails: function (e) {
//         this.checksession().then((res) => {
//             let reportId = e.currentTarget.dataset.item.id;
//             post({
//                 className: 'report',
//                 method: 'getPathAndVerifyPermission',
//                 data: {
//                     id: reportId
//                 }
//             }).then((res) => {
//                 if (res.data.data.code === 200) {
//                     wx.showLoading({
//                         title: '加载中',
//                         mask: true
//                     })
//                     this.setData({
//                         pdfUrl: app.globalData.openPDF + res.data.data.data
//                     });
//                     wx.downloadFile({ //下载
//                         url: this.data.pdfUrl, //服务器上的pdf地址
//                         success: res => {
//                             const filePath = res.tempFilePath
//                             wx.openDocument({ //打开
//                                 filePath: filePath,
//                                 success: res => {
//                                     wx.hideLoading();
//                                     app.addIntoActionPoint(this.data.currentPageId, null, reportId);
//                                     this.data.kindInfo.forEach(item => {
//                                         if (item.id == reportId) {
//                                             item.articleviews++;
//                                         }
//                                     })
//                                     this.setData({
//                                         kindInfo: this.data.kindInfo
//                                     })
//                                 }
//                             })
//                         }
//                     });
//                 } else if (res.data.data.code == '204') {
//                     wx.showModal({
//                         title: '提示',
//                         content: res.data.data.msg,
//                         showCancel: false,
//                     });
//                 } else if (res.data.data.code == '205') {
//                     wx.showModal({
//                         title: '提示',
//                         content: res.data.data.msg,
//                         showCancel: false
//                     });

//                 } else if (res.data.data.code == '203') {
//                     wx.showModal({
//                         title: '提示',
//                         content: res.data.data.msg,
//                         showCancel: false,
//                         success: res => {
//                             wx.switchTab({
//                                 url: '../mine',
//                             })
//                         }
//                     });
//                 } else {
//                     wx.showModal({
//                         title: '提示',
//                         content: res.data.data.msg || '系统故障，未知错误',
//                         showCancel: false
//                     });
//                 }
//             });
//         }).catch(err => {
//             let one_login_date = wx.getStorageSync('one_login_date');
//             if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
//                 wx.showModal({
//                     title: '提示',
//                     content: '未登录，请进行登录',
//                     showCancel: false,
//                     success: res => {
//                         wx.switchTab({
//                             url: '../mine',
//                         })
//                     }
//                 });
//             } else {
//                 wx.showModal({
//                     title: '提示',
//                     content: '未登录，请先登录',
//                     showCancel: false,
//                     success: res => {
//                         wx.switchTab({
//                             url: '../mine',
//                         })
//                     }
//                 });
//             }
//         });
//     },

//     collect(e) {
//         var _that = this;
//         this.checksession().then((res) => {
//             const report = e.currentTarget.dataset.item;
//             if (report != null) {
//                 let params = {
//                     className: "collect",
//                     method: "delCollect",
//                     data: {
//                         reportId: report.id
//                     }
//                 }
//                 post(params).then((res) => {
//                     if (res.data.code == 200) {
//                         if (_that.data.kindInfo != null && _that.data.kindInfo.length > 0) {
//                             let arr = new Array();
//                             arr = _that.data.kindInfo;
//                             arr.forEach((e, i) => {
//                                 if (e.id == report.id) {
//                                     arr.splice(i, 1);
//                                     return;
//                                 }
//                             });

//                             _that.setData({
//                                 kindInfo: arr
//                             });
//                         }
//                     }
//                 }).catch(err => {
//                     console.log(err)
//                 });
//             }
//         }).catch(err => {
//             let one_login_date = wx.getStorageSync('one_login_date');
//             if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
//                 wx.showModal({
//                     title: '提示',
//                     content: '未登录，请进行登录',
//                     showCancel: false,
//                     success: res => {
//                         wx.switchTab({
//                             url: '../mine',
//                         });
//                     }
//                 });
//             } else {
//                 wx.showModal({
//                     title: '提示',
//                     content: '您当前尚未登录，无法使用收藏功能，请先去登录？',
//                     showCancel: false,
//                     success: res => {
//                         wx.switchTab({
//                             url: '../mine',
//                         })
//                     }
//                 });
//             }
//         });
//     },

//     //将路径转成base64位的图片
//     getPic(pic) {
//         return new Promise((resolve, reject) => {
//             fileDownload({
//                 url: 'downloadfile',
//                 data: pic
//             }).then((res) => {
//                 if (res.data != null && res.data.data != null && res.data.data != '' && res.data.code == '200') {
//                     let path = res.data.data.replace(/[\r\n]/g, "");
//                     resolve('data:image/png;base64,' + path);
//                 } else {
//                     wx.showToast({
//                         title: res.data.msg,
//                         icon: 'none',
//                         duration: 2000
//                     })
//                 }
//                 resolve('');
//             });
//         });
//     },

//     //验证登录是否过期
//     checksession() {
//         return new Promise((resolve, reject) => {
//             AUTH.checkHasLogined().then(isLogined => {
//                 if (isLogined) {
//                     resolve("checkSession--成功");
//                 } else {
//                     reject("checkSession--失败");
//                 }
//             });
//         })
//     },
// })


// 获取应用实例
const {
    post,
    fileDownload
} = require("../../../utils/api");
const {
    formatTime2
} = require('../../../utils/util')
const app = getApp()
const {
    startTime,
    stopTime
} = require('../../../utils/stopWatch')
const AUTH = require("../../../utils/auth");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    itemList:[],
    img:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
   
    this.getCollect()
  },


  goDetail(e) {
    console.log("产品详情",e);
    wx.navigateTo({
        url: '../../product/entfirm/loans/detail/detail',
        success: (res => {
            res.eventChannel.emit('recommendationId', e.currentTarget.dataset.src.productId);
        })
    })
},
  //获取数据
  getCollect: function () {
    let param = {
        page: {
            pageIndex: 1,
            pageSize: 10
        },
    }
    post({
        className: "collect",
        method: "getCollect",
        data: param
    }).then((res) => {
        console.log(res,'666666666666666666666666')
        if (res.data.code == 200) {
            console.log(res,'999999999999999999999999')
            this.setData({
                itemList: res.data.data.dataList,
            })
        }
        console.log('this.data.itemList==',this.data.itemList);
    }).catch(error => {
    })

   
}, 



//去搜索页
goSearch() {
            console.log("goSearch");
            wx.navigateTo({
                url: '../collect/search/search',
                success: (res => {
                    //res.eventChannel.emit('theme', this.data.reportTheme);
                })
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