// index.js
// 获取应用实例
const {
    post,
    fileDownload
} = require("../../../utils/api");
const {
    formatTime2
} = require('../../../utils/util')
const app = getApp()
const bbuttonClicked = require('../../../utils/resolveContinuousClick');
const AUTH = require("../../../utils/auth");

Page({
    data: {
        kindInfo: [],
        returnResult: [],
        labelList: [], //每一份报告图标列表
        industryList: [], //每一份报告的行业列表
        navInfo: [], //全部分页数据
        pageIndex: 1, //使用swiper做分页，当前页
        pageSize: 10, //每页个数
        pageCount: 0, //一共有多少页
        showBottom: false, //显示上拉到底
        windowHeight: 0, //页面高度
        scrollHeight: 0, //信息滚动条高度
        currentPageId: '', //当前页面id
        dataList:[]
    },

    onLoad: function (options) {
        let currentPageId = getCurrentPages();
        this.setData({
            currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id
        })
        this.getPointList();
    },

    // 生命周期函数--监听页面初次渲染完成
    onReady: function () {
        this.animation = wx.createAnimation()
    },

    onShow: function () {
    },
    onHide: function () {
    },
    onUnload: function () {
    },
    onPullDownRefresh: function () {
    },
    onReachBottom: function () {
    },


    // 足迹
    getPointList() {
        let param = {
          page: {
            pageIndex: 1,
            pageSize: 10
          },
        }
        post({
          className: "point",
          method: "getPointList",
          data: param
    
        }).then((res) => {
            console.log(res,'66666666666666');
          if (res.data.code == 200) {
            console.log(res,'9999999999');
            this.setData({
                dataList: res.data.data.dataList,
            })
          }
        }).catch(error => {})
      },


    // getPoint: function () {
    //     let param = {
    //         page: {
    //             pageIndex: 1,
    //             pageSize: 10
    //         }
    //     }

    //     post({
    //         className: "point",
    //         method: "getPoint",
    //         data: param
    //     }).then((res) => {
    //         console.log(res,'66666666666666');
    //         if (res.data.code == 200) {
    //             console.log(res,'9999999999');
    //             this.setData({
    //                 returnResult: res.data.data.dataList
    //             })
    //             this.data.returnResult.forEach((item) => {
    //                 // let date = item.modifyTime.replace(/-/g, "/");
    //                 let date = new Date(item.modifyTime);
    //                 item.modifyTime = formatTime2(date);
    //                 this.getPic(item.pic).then((res) => {
    //                     item.pic = res;
    //                     this.setData({
    //                         kindInfo: this.data.kindInfo
    //                     })
    //                 })
    //             });
    //             let kindinfo = this.data.returnResult;
    //             let data = [...this.data.kindInfo, ...kindinfo];
    //             this.setData({
    //                 kindInfo: data
    //             })
    //         } else {
    //         }
    //     }).catch(error => {
    //     })
    // },

    //scroll-view触底触发
    bindscrolltolower: function () {
        if (this.data.returnResult.length < this.data.pageSize) {
            this.setData({
                showBottom: true
            })
            return;
        }
        this.setData({
            pageIndex: this.data.pageIndex + 1
        });
        this.getTrack();
    },


    goDetail(e) {
        console.log("产品详情",e);
        wx.navigateTo({
            url: '../../product/entfirm/loans/detail/detail',
            success: (res => {
                console.log('666666666',res);
                res.eventChannel.emit('recommendationId', e.currentTarget.dataset.src.productId);
            })
        })
    },

    //点击每个推荐的资讯进入详情页面
    viewDetails: function (e) {
        this.checksession().then((res) => {
            let reportId = e.currentTarget.dataset.item.id;
            post({
                className: 'report',
                method: 'getPathAndVerifyPermission',
                data: {
                    id: reportId
                }
            }).then((res) => {
                if (res.data.data.code === 200) {
                    wx.showLoading({
                        title: '加载中',
                        mask: true
                    })
                    this.setData({
                        pdfUrl: app.globalData.openPDF + res.data.data.data
                    });
                    wx.downloadFile({ //下载
                        url: this.data.pdfUrl, //服务器上的pdf地址
                        success: res => {
                            const filePath = res.tempFilePath
                            wx.openDocument({ //打开
                                filePath: filePath,
                                success: res => {
                                    wx.hideLoading();
                                    app.addIntoActionPoint(this.data.currentPageId, null, reportId);
                                    this.data.kindInfo.forEach(item => {
                                        if (item.id == reportId) {
                                            item.articleviews++
                                        }
                                    })
                                    this.setData({
                                        kindInfo: this.data.kindInfo
                                    })
                                },
                                fail: (err) => {
                                    console.log('打开失败', err);
                                    wx.hideLoading();
                                }
                            })
                        }
                    });
                } else if (res.data.data.code == '204') {
                    wx.showModal({
                        title: '提示',
                        content: res.data.data.msg,
                        showCancel: false,
                    });
                } else if (res.data.data.code == '205') {
                    wx.showModal({
                        title: '提示',
                        content: res.data.data.msg,
                        showCancel: false
                    });
                } else if (res.data.data.code == '203') {
                    wx.showModal({
                        title: '提示',
                        content: res.data.data.msg,
                        showCancel: false,
                        success: res => {
                            wx.switchTab({
                                url: '../mine',
                            })
                        }
                    });
                } else {
                    wx.showModal({
                        title: '提示',
                        content: res.data.data.msg || '系统故障，未知错误',
                        showCancel: false
                    });
                }
            });
        }).catch(err => {
            let one_login_date = wx.getStorageSync('one_login_date');
            if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
                wx.showModal({
                    title: '提示',
                    content: '未登录，请进行登录',
                    showCancel: false,
                    success: res => {
                        wx.switchTab({
                            url: '../mine',
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
                            url: '../mine',
                        })
                    }
                });
            }
        });
    },

    collect(e) {
        bbuttonClicked.ButtonClicked(this);
        this.checksession().then((res) => {
            const report = e.currentTarget.dataset.item;
            if (report != null && report.collectFlag > 0) { //已收藏
                let params = {
                    className: "collect",
                    method: "delCollect",
                    data: {
                        reportId: report.id
                    }
                }
                post(params).then((res) => {
                    if (res.data.code == 200) {
                        if (this.data.kindInfo != null && this.data.kindInfo.length > 0) {
                            let arr = new Array();
                            this.data.kindInfo.forEach((e, i) => {
                                if (e.id == report.id) {
                                    e.collectFlag = 0;
                                    e.favorite = Number(e.favorite) - 1;
                                }
                                arr.push(e);
                            });

                            this.setData({
                                kindInfo: arr
                            });
                        }
                    }
                }).catch(err => {
                });
            } else { //未收藏
                let params = {
                    className: "collect",
                    method: "addCollect",
                    data: {
                        reportId: report.id
                    }
                }
                post(params).then((res) => {
                    if (res.data.code == 200) {
                        if (this.data.kindInfo != null && this.data.kindInfo.length > 0) {
                            let arr = new Array();
                            this.data.kindInfo.forEach((e, i) => {
                                if (e.id == report.id) {
                                    e.collectFlag = 1;
                                    e.favorite = Number(e.favorite) + 1;
                                }
                                arr.push(e);
                            });
                            this.setData({
                                kindInfo: arr
                            });
                        }
                    }
                })
            }
        }).catch(err => {
            let one_login_date = wx.getStorageSync('one_login_date');
            if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
                wx.showModal({
                    title: '提示',
                    content: '未登录，请进行登录',
                    showCancel: false,
                    success: res => {
                        wx.switchTab({
                            url: '../mine',
                        });
                    }
                });
            } else {
                wx.showModal({
                    title: '提示',
                    content: '您当前尚未登录，无法使用收藏功能，请先去登录？',
                    showCancel: false,
                    success: res => {
                        wx.switchTab({
                            url: '../mine',
                        })
                    }
                });
            }
        });
    },

    //将路径转成base64位的图片
    getPic(pic) {
        return new Promise((resolve, reject) => {
            fileDownload({
                url: 'downloadfile',
                data: pic
            }).then((res) => {
                if (res.data.code == 200 && res.data.data != null && res.data.data != '') {
                    let path = res.data.data.replace(/[\r\n]/g, "");
                    resolve('data:image/png;base64,' + path);
                } else {
                    wx.showToast({
                        title: res.data.msg,
                        icon: 'none',
                        duration: 2000
                    })
                }
                resolve('');
            });
        });
    },

    //验证登录是否过期
    checksession() {
        return new Promise((resolve, reject) => {
            AUTH.checkHasLogined().then(isLogined => {
                if (isLogined) {
                    resolve("checkSession--成功");
                } else {
                    reject("checkSession--失败");
                }
            });
        })
    },
})