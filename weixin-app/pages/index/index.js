// index.js
// 获取应用实例
const {
    post,
    fileDownload
} = require("../../utils/api");
const app = getApp();
const {
    formatTime2
} = require('../../utils/util')
const {
    startTime,
    stopTime
} = require('../../utils/stopWatch')
const bbuttonClicked = require('../../utils/resolveContinuousClick');
const AUTH = require("../../utils/auth");

Page({
    data: {
        swiper: [], //轮播图
        // myBackground1: " ../../images/report/bj.png",

        // myBackground2: " ../../images/report/banner.png",
        kindInfo: [], //头条热点、语音播报
        kindPicInfo: [],
        reportKind: 'hotHeadline', //当前所选分类index
        currentId: 1, //当前选中的id，（热点头条或者是语音播报）
        login: false,
        authFlag: false,
        currentTime: '',
        navInfo: [], //全部分页数据
        pageIndex: 1, //使用swiper做分页，当前页
        pageSize: 10, //每页个数
        pageCount: 0, //一共有多少页
        currentIndex: 0, //当前分组数组的index
        inform: [], //通知
        returnResult: [], //每次请求报告返回的数据
        showBottom: false, //显示上拉到底
        windowHeight: 0, //页面高度
        scrollHeight: 0, //信息滚动条高度
        userInfo: {}, //当前用户信息
        showConcatModal: false, //是否打开客服会话,
        selCollect: false, //是否收藏
        location: '', //经纬度
        address: '', //地名
        type: 'login', //埋点类型。登录/退出
        startTime: '', //页面进来时间
        endTime: '', //页面离开时间
        tips: '加载中', //loading动画提示文字
        show: true, //是否展示loading动画
        buttonClicked: false, //防重复点击
        refresher_triggered: false, //是否触发下拉刷新
        refresher_threshold: 10, //自定义下拉刷新阈值
        pdfUrl: '',
        useCustomer: false,
        // noReadNumber: 0, //未读消息个数
        titleHeight: 87, //标题高度
        noReadNumber1: [],
        activitylist: [],
        TOP: '',
        //animated: true  //loading动画,
        myBackground2: app.globalData.myBackground2,
        // myBackground2:noReadNumber1[0].pic,
        myBackground1: app.globalData.myBackground12,
        newsList: []
    },

    onLoad() {
      
       // let currentPageId = getCurrentPages();
        
        // this.setData({
        //     currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id  pages/index/index
        //     pageIndex:1,
        //     newsList: []
        // });
        this.getLoadingData();
        // this.getAddress();
        // var that = this;
        

        //this.getReport(this.data.reportKind, this.data.pageIndex);
        // this.getNewsList(1);
       
    },

    //请求后端，在页面加载时拿到轮播图数据以及添加行为埋点数据
    getLoadingData: function () {
        
        post({
            className: "home",
            method: "getBanner"
        }).then(res => {
            this.setData({
                noReadNumber1: res.data.data
            })
        })
        console.log("?????????????????????????????????????????????????//////",this.data.noReadNumber1)
        post({
            className: "home",
            method: "getActivity",
            data: {}
        }).then(res => {
            console.log("活动list",res.data.data)
            this.setData({
                activitylist: res.data.data
            });
        })
        
        let param = {
          page: {
              pageIndex: 1,
              pageSize: 4
          }
      }
      console.log("新闻param",param)
      post({
          className: "home",
          method: "getNews",
          data: param
      }).then(res => {
          console.log('新闻res===',res);
          let noteslist=[];
          this.setData({
              newsList: res.data.data.dataList.slice(0,4),
              showBottom: res.data.data.dataList.length < 5
          })
          console.log('this.data.newsList=',this.data.newsList)
      })
    },

    goDetail2(e) {
        console.log("eeeeeeeeee",e)
        let navItem = e.currentTarget.dataset.theme;
        if (navItem === "entfirm") {
            wx.navigateTo({
                url: './infoReport/infoReport',
                success: (res => {
                    res.eventChannel.emit('Activityintroduction', e.currentTarget.dataset.theme2);
                })
            })
        } else if (navItem === "gp") {
            wx.navigateTo({
                url: './gp/gp',
                success: (res => {
                    res.eventChannel.emit('navitem', navItem);
                })
            })
        }
    },

    // 获取新闻列表
    // getNewsList(pageIndex) {
    //     let param = {
    //             page: {
    //                 pageIndex: 1,
    //                 pageSize: 4
    //             }
    //         }
    //         console.log("新闻param",param)
    //         post({
    //             className: "home",
    //             method: "getNews",
    //             data: param
    //         }).then(res => {
    //             console.log('res===',res);
    //             let noteslist=[];
               
    //             this.setData({
    //                 newsList: this.data.newsList.concat(res.data.data.dataList.slice(0,4)),
    //                 showBottom: res.data.data.dataList.length < 5
    //             })
    //             console.log('this.data.newsList=',this.data.newsList)
    //         })
    // },
    //scroll-view触底触发
    bindscrolltolower: function () {
        if (this.data.showBottom) return;
        // this.getNewsList(++this.data.pageIndex);
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

    //点击打开客服会话
    dismissConcatModal: function () {
        this.setData({
            showConcatModal: true
        })
    },

    //获取当前用户未读消息个数
    getUserNotesNoReadNumber: function () {
        post({
            className: "report",
            method: "getUserNotesNoReadNumber",
            data: {}
        }).then(res => {
            this.setData({
                noReadNumber: res.data.data
            })
        })
    },

    //点击切换头条热点，语音播报
    selected: function (e) {
        let reportKind = e.currentTarget.dataset.title;
        if (reportKind === this.data.reportKind) return;
        this.setData({
            pageIndex: 1,
            currentId: e.currentTarget.id,
            kindInfo: [],
            showBottom: false
        })
        this.getReport(reportKind, this.data.pageIndex);
    },


    //请求后端拿到报告列表信息
    getReport(reportKind, pageIndex) {
        this.setData({
            reportKind: reportKind
        })
        let param = {
            searchdata: {
                reportKind: reportKind,
                rank: 'time',
                articleviews: 'articleviews'
            },
            page: {
                pageIndex: pageIndex,
                pageSize: this.data.pageSize
            }
        }
        // post({
        //     className: "report",
        //     method: "getReportInfo",
        //     data: param
        // }).then(res => {
        //     if (res.data.code == 200) {
        //         for (let item of res.data.data.dataList) {
        //             item.modifyTime = formatTime2(new Date(item.modifyTime));
        //             if (item.industryList != null && item.industryList != '') {
        //                 if (item.industryList.length > 0) {
        //                     let industry = [];
        //                     item.industryList.forEach(industryItem => {
        //                         industry.push(industryItem.split('(')[0])
        //                     })
        //                     item.industry = industry;
        //                 }
        //             }
        //             // this.getPic(item.pic).then(res => {
        //             //     item.pic = res;
        //             //     this.setData({
        //             //         kindInfo: this.data.kindInfo
        //             //     });
        //             // });
        //         }
        //         this.setData({

        //             showBottom: res.data.data.dataList.length < 10
        //         })
        //         if (reportKind === this.data.reportKind) {
        //             let data = [...this.data.kindInfo, ...res.data.data.dataList];
        //             this.setData({
        //                 returnResult: res.data.data.dataList,
        //                 kindInfo: data
        //             });
        //         } else {
        //             this.setData({
        //                 kindInfo: res.data.data.dataList,
        //                 reportKind: reportKind
        //             });
        //         }
        //     } else {
        //         console.log('错误')
        //     }
        // }).catch(error => {
        //     console.log(error);
        // })
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
                    reject('')
                }
            });
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
                                            item.articleviews++;
                                        }
                                    })
                                    this.setData({
                                        kindInfo: this.data.kindInfo
                                    })
                                },
                                fail: (err) => {
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
                                url: '../mine/mine',
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
        });
    },

    //通过moke请求获取头条热点，语音播报信息
    getKindInfo: function (kindIndex, pageIndex) {
        let params = {
            kindIndex: kindIndex,
            currentPage: pageIndex
        }
        post({
            url: "getKindInfo",
            data: {
                openId: "fhoe570394hfieo",
                data: params
            }
        }).then(res => {
            if (kindIndex === this.data.kindIndex) {
                let data = [...this.data.kindInfo, ...res.data.dataList];
                this.setData({
                    kindInfo: data
                })
            } else {
                this.setData({
                    kindInfo: res.data.kindInfo,
                    kindIndex: kindIndex
                })
            }
        }).catch(err => {
            console.log(err)
        })
    },


    //scroll-view下拉刷新
    bindrefresherrefresh: function () {
        this.setData({
            kindInfo: [],
            pageIndex: 1,
            showBottom: false,
            refresher_triggered: false
        })
        this.getReport(this.data.reportKind, this.data.pageIndex);
        wx.stopPullDownRefresh();
    },

    //监听用户上拉触底事件
    onReachBottom: function () {},

    //当swiper左右滑动时触发
    changeNavItem(e) {
        this.setData({
            currentIndex: e.detail.current
        })
    },

    //生命周期回调—监听页面加载
    onShow() {
        // this.checkSeesion1();
        this.titleHeight();
        this.getLoadingData();
        // this.getAddress();//获取地理位置
        this.setData({
            TOP: wx.getMenuButtonBoundingClientRect().bottom,
            showConcatModal: false,
            showBottom: false
        });
        this.checksession().then((res) => {
            this.getUserNotesNoReadNumber(); //获取未读消息个数
        }).catch(err => {
            this.setData({
                noReadNumber: 0
            })
            let loginSubject = wx.getStorageSync('loginSubject');
            if (loginSubject != null && loginSubject != "") {
                // wx.showModal({
                //     title: '提示',
                //     content: '您的登录已失效，请重新登录!',
                //     showCancel: false
                // });
                this.setData({
                    userInfo: wx.getStorageSync('userInfo')
                });
            }
        });
        startTime();
        //调用进入页面的行为埋点方法
        app.addIntoActionPoint(null, null, null);

        // if(this.data.pageIndex == 1) {
        //     this.setData({
        //         newsList: []
        //     })
        //     setTimeout(() => {
        //         this.getNewsList(1);
        //     },500)
            
        // }
        
    },

    onReady() {

    },

    // 点击 tab 时触发
    onTabItemTap: function () {
        this.setData({
            pageIndex: 1,
            showBottom: false,
            kindInfo: []
        })
        this.getReport(this.data.reportKind, this.data.pageIndex);
    },

    test:function(e){
      console.log("6666",e.currentTarget.dataset.theme)
      wx.navigateTo({
        url: '../index/weblink/weblink?newsLink=' + e.currentTarget.dataset.theme
      })
    

    },
    // getAddress() {
    //     // 获取用户所有授权信息
    //     wx.getSetting({
    //         success: (res) => {
    //             // 这里判断的是地理位置权限,如果有这个字段,状态是false,说明拒接过,这种情况是不会再次自动弹出授权窗口的
    //             if (res.authSetting.hasOwnProperty('scope.userLocation') && res.authSetting['scope.userLocation'] === false) {
    //                 wx.showModal({
    //                     title: '是否授权地理位置信息',
    //                     content: '需要获取您的地理位置信息，请确认授权',
    //                     success: (tip) => {
    //                         if (tip.confirm) {
    //                             //  打开授权页面,让用户手动打开权限
    //                             wx.openSetting({
    //                                 success: (data) => {
    //                                     if (data.authSetting["scope.userLocation"]) {
    //                                         wx.getLocation({
    //                                             type: 'gcj02',
    //                                             success: (res) => {
    //                                                 // 根据经纬度获取地名
    //                                                 wx.request({
    //                                                     url: 'https://apis.map.qq.com/ws/geocoder/v1/',
    //                                                     // data是该请求需要的数据，如文档，location和key是必须的
    //                                                     data: {
    //                                                         location: [res.latitude, res.longitude].join(','),
    //                                                         key: 'P4HBZ-LEGKU-Z2GV7-BN7AB-CIP7S-Z6BJK',
    //                                                     },
    //                                                     success: (res) => {
    //                                                         this.setData({
    //                                                             address: res.data.result.address,
    //                                                             location: res.data.result.location
    //                                                         })
    //                                                         wx.setStorageSync('address', this.data.address)
    //                                                         wx.setStorageSync('location', this.data.location)
    //                                                     },
    //                                                     fail: () => {
    //                                                         wx.showToast({
    //                                                             title: '获取Address失败',
    //                                                             icon: 'none',
    //                                                             duration: 2000
    //                                                         })
    //                                                     },
    //                                                 })
    //                                             },
    //                                             fail: () => {
    //                                                 wx.showToast({
    //                                                     title: '获取Location失败',
    //                                                     icon: 'none',
    //                                                     duration: 2000
    //                                                 })
    //                                             },
    //                                         })
    //                                     } else {
    //                                         wx.showToast({
    //                                             title: '授权失败',
    //                                             icon: 'none',
    //                                             duration: 2000
    //                                         })
    //                                     }
    //                                 },
    //                             })
    //                         } else {
    //                             wx.showToast({
    //                                 title: '取消授权',
    //                                 icon: 'none',
    //                                 duration: 2000
    //                             })
    //                         }
    //                     }
    //                 })
    //             } else {
    //                 wx.getLocation({
    //                     type: 'gcj02',
    //                     success: (res) => {
    //                         // 根据经纬度获取地名
    //                         wx.request({
    //                             url: 'https://apis.map.qq.com/ws/geocoder/v1/',
    //                             // data是该请求需要的数据，如文档，location和key是必须的
    //                             data: {
    //                                 location: [res.latitude, res.longitude].join(','),
    //                                 key: 'P4HBZ-LEGKU-Z2GV7-BN7AB-CIP7S-Z6BJK',
    //                             },
    //                             success: (res) => {
    //                                 this.setData({
    //                                     address: res.data.result.address,
    //                                     location: res.data.result.location
    //                                 })
    //                                 wx.setStorageSync('address', this.data.address)
    //                                 wx.setStorageSync('location', this.data.location)
    //                             }
    //                         })
    //                     },
    //                 })
    //             }
    //         }
    //     });
    // },


    //点击消息跳转到消息页面
    toAdvices() {
        this.checksession().then((res) => {
            wx.navigateTo({
                url: '../index/advices/advices?source=' + this.data.currentPageId,
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
                            url: '../mine/mine',
                        })
                    }
                });
            } else {
                wx.showModal({
                    title: '提示',
                    content: '您当前尚未登录，无法使用消息功能，请先去登录？',
                    showCancel: false,
                    success: res => {
                        wx.switchTab({
                            url: '../mine/mine',
                        })
                    }
                });
            }
        })
    },

    //点击收藏按钮
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
                    console.log(err)
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
                    let that = this;
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
                }).catch(err => {
                    console.log(err)
                });
            };
        }).catch(err => {
            let one_login_date = wx.getStorageSync('one_login_date');
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
                    content: '您当前尚未登录，无法使用收藏功能，请先去登录？',
                    showCancel: false,
                    success: res => {
                        wx.switchTab({
                            url: '../mine/mine',
                        })
                    }
                });
            }
        });
    },
    //判断用户是否登录
    checkSeesion1() {
      this.setData({
        userInfo: wx.getStorageSync('userInfo')
      });
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
        console.log('验证urolea',this.data.userInfo.urole);
      }
      
    },
    onHide() {
        let endTime = stopTime();
        //调用离开页面的行为埋点方法
        app.addLeaveActionPoint(null, endTime);
    },

    //点击遮罩层，遮罩层消失
    clickContatNodal: function () {
        this.setData({
            showConcatModal: false
        })
    },

    //适配iphoneX标题高度
    titleHeight: function () {
        wx.getSystemInfo({
            success: (result) => {
                console.log(result.model);
                if (result.model == 'iPhone XR' || result.model == 'iPhone X' || result.model == 'iPhone XS Max') {
                    this.setData({
                        titleHeight: 110
                    })
                }
            },
        })
    },


    //触发下拉刷新
    onPullDownRefresh: function () {
        console.log("触发下拉刷新");
        this.getLoadingData();
        this.setData({
            pageIndex: 1,
            showBottom: false,
            kindInfo: []
        })
        this.getReport(this.data.reportKind, this.data.pageIndex);
        this.getUserNotesNoReadNumber();
        wx.stopPullDownRefresh();
    }
})