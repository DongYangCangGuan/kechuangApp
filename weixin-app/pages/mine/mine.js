const {
    post
} = require("../../utils/api");
const app = getApp()
const {
    startTime,
    stopTime
} = require('../../utils/stopWatch')
const {
    formatTime2
} = require('../../utils/util')
const AUTH = require("../../utils/auth");

Page({
    data: {
        reportes: [], // 区域发展-报告列表
        userInfo: {},
        type: 'login', //埋点类型。登录/退出
        currentPageId: '', //当前页面id
        memberInfo: {},
        myBackground: app.globalData.myBackground,
        memberHeader: '/images/mine/memberHeader.png',
        attestation: '/images/mine/aboutUs@2x.png',
        customization: '/images/mine/customization@2x.png',
        subscription: '/images/mine/subscription@2x.png',
        collect: '/images/mine/collect@2x.png',
        footprint: '/images/mine/footprint@2x.png',
        aboutUs: '/images/mine/feedback@2x.png',
        feedback: '/images/mine/feedback@2x.png',
        yirenzhengtu:'/images/mine/yirenzheng.png',
        clientCount: 0, //客户总数
        onlineCount: 0, //当前在线人数
        reportType: ['freeLimitTime', 'exclusive'],
        freeLimitTime: 0, //免费报告
        exclusive: 0, //收费报告
        readCount: 0, //已读报告个数
        latestAddCount: 0, //最近添加报告个数（近一个月）
        currentTime: '', //当前时间
        payingCustomers: 0, //付费客户数量
        urole:'',
        authentification: '0', //身份认证
        userID:'',//使用者身份
        num:'',//未读消息数
        showNum:true,// 我的消息角标显示隐藏
        itemNum:'',//审批数
        showItemNum:true,// 我的审批角标显示隐藏
        fullNum:'',//消息数
        isShenpi: false
    },

    // 生命周期函数--监听页面加载
    onLoad: function (options) {
        let currentPageId = getCurrentPages();
        this.setData({
            currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id  pages/index/index
        });
        
        this.getUsera();
    },
  
    // 我的消息角标显示隐藏
    getShowM() {
        if(this.data.num == 0 || this.data.num == ''){
            this.setData({
                showNum:false
            })
        } else{
            this.setData({
                showNum:true
            })
        }
    },
    // 我的审批角标显示隐藏
    getShowS: function() {
        if(this.data.itemNum == 0 || this.data.itemNum == ''){
            this.setData({
                showItemNum:false
            })
        }else{
            this.setData({
                showItemNum:true
            })
        }
    },
    // 获取消息数
  getNotes: function(){
    let param = {
      page: {
          pageIndex: 1,
          pageSize: 10
      },
  }
  post({
      className: "notes",
      method: "getNotes",
      data: param
  }).then((res) => {
      if (res.data.code == 200) {
          let dataList = res.data.data.dataList;
          let itemsLists = [];
          for (let i = 0; i < dataList.length; i++ ){
            if(dataList[i].readStatus=="未读"){
                itemsLists.push(dataList[i].readStatus)
            }
          }
          if(itemsLists.length > 1) {
            this.setData({
                num: 'N+'
            });
          } else {
            this.setData({
                num: itemsLists.length
            });
          }
    //   this.setData({
    //       num: itemsLists.length
    //   });
      wx.setStorageSync('num', this.data.num);
      this.getShowM();
      this.getNum();
      app.jiaoBiao()
    }else{
        this.setData({
            showNum:false,
            num: '0'
        })
        wx.setStorageSync('num', this.data.num)
    }
    
       
  }).catch(error => {
    this.setData({
        showNum:false
    })
  })
}, 
//获取审批数
getApproveList: function () {
    let param = {
        page: {
            pageIndex: 1,
            pageSize: 10
        },
    }
    post({
        className: "Approve",
        method: "getApproveList",
        data: param
    }).then((res) => {
        if (res.data.code && res.data.code == 200) {
            if(res.data.data.page.totalNum > 1) {
                this.setData({
                    itemNum: 'N+',
                });
            } else  {
                this.setData({
                    itemNum: res.data.data.page.totalNum,
                });
            }
            
            wx.setStorageSync('itemNum', this.data.itemNum)
            this.getShowS();
            this.getNum();
            app.jiaoBiao()
        }else{
            this.setData({
                showItemNum:false,
                itemNum: '0'
            })
            wx.setStorageSync('itemNum', this.data.itemNum)
        }
        
    }).catch(error => {
        this.setData({
            showItemNum:false
        })
    })
}, 

//  判断用户否认证
getUsera() {
    let userInfo = wx.getStorageSync('userInfo');
    console.log(userInfo,'000000000000000000000000000000000');
    if (userInfo.urole == '') {
        this.setData({
            authentification: '0'
        })
    }else if (userInfo.urole == 0) {
        this.setData({
            authentification: '1'
        })
    } else {
        this.setData({
            authentification: '2'
        })
    }
    // if(userInfo.member.enterpriseCode != '' && userInfo.urole != 0 && userInfo != '') {
    //     this.setData({
    //         authentification: '2'
    //     })
    // } 
     if(userInfo.urole == 1 ){
        this.setData({
            userID:'GP',
            isShenpi: true
        })
    }else if(userInfo.urole == 2 ){
        this.setData({
            userID:'科创',
            isShenpi: true
        })
    }else if(userInfo.urole == 3 ){
        this.setData({
            userID:'Lp',
            isShenpi: false
        })
    }else if(userInfo.urole == 4 ){
        this.setData({
            userID:'合作伙伴',
            isShenpi: false
        })
    }else if(userInfo.urole == 5 ){
        this.setData({
            userID:'创业公司',
            isShenpi: false
        })
    }

},

    // 生命周期函数--监听页面初次渲染完成
    onReady: function () {},

    // 生命周期函数--监听页面显示
    onShow: function () {
        this.setData({
            currentTime: formatTime2(new Date()).replace(/-/g, "/")
        })

        startTime();
        //调用进入页面的行为埋点方法
        app.addIntoActionPoint(null, null, null);
        this.getTime();
        this.setData({
            userInfo: wx.getStorageSync('userInfo')
        })
        if(this.data.userInfo.urole == 0){
            this.getReadCount(); //获取已读报告个数
            this.getClientCount(); //客户总数
            this.getOnlineCount(); //当前在线人数
            this.getFreeAndExclusiveReport(); //免费报告和收费报告个数
            this.getLatestAddCount(); //获取最近添加报告个数（近一个月）
            // this.getPayingCustomers(); //获取付费客户人数
        }
        this.getNotes();
        this.getApproveList();

        this.getUsera();
        
        // setInterval(function(){
        //    app.jiaoBiao()
        // },100);
    },

    getNum(){
        // let num = wx.getStorageSync('num') ? wx.getStorageSync('num') : 0;
        // let itemNum = wx.getStorageSync('itemNum') ? wx.getStorageSync('itemNum') : 0
        let num = wx.getStorageSync('num');
        if(num == '0' || num == '') {
            num = 0
        } else {
            num = 1
        }
        
        let itemNum = wx.getStorageSync('itemNum');
        if(itemNum == '0' || itemNum == '') {
            itemNum = 0
        } else {
            itemNum = 1
        }

        // console.log('num=',num);
        // console.log('itemnum=',itemNum);
        
        this.setData({
            fullNum: Number(num) + Number(itemNum)
        })
        wx.setStorageSync('fullNum', this.data.fullNum)
        console.log(1212121212121);
    },
    // 获取当前小程序信息，添加埋点数据
    userLogin() {
        let param = {
            equipment: app.globalData.model,
            version: app.globalData.version,
            address: wx.getStorageSync("address"),
            location: wx.getStorageSync("location"),
            openId: wx.getStorageSync("userInfo").openId,
            type: this.data.type
        }
        post({
            className: "statusPoint",
            method: "addStatusPoint",
            data: param
        }).then((res) => {
            if (res.data.code == 200) {
                
                console.log("埋点数据添加成功");
            } else {
                console.log("参数错误");
            }
        })
    },


    selectLogin(e) {
        wx.showModal({
            title: '登录后可通过会员认证绑定权益',
            showCancel: false,
            confirmText: '确定',
            cancelText: '企业登入',
            success: res => {
                if (res.confirm) {
                    this.handlGetUserInfo();
                }
            }
        });
    },

    //点击微信登录按钮
    handlGetUserInfo(e) {
        //获取用户信息
        wx.getUserProfile({
            desc: 'scope.userInfo',
            success: (res) => {
                console.log('res==',res);
                wx.setStorageSync('loginSubject', "applet");
                wx.setStorageSync('beforeUserInfo', res.userInfo);
                AUTH.appletLogin(this); //用户登录
            },
            fail: (e) => {
                console.log("用户授权失败", e);
            },
            
        })
        this.getUsera();
    },

    // 生命周期函数--监听页面隐藏
    onHide: function () {
        let endTime = stopTime();
        //调用离开页面的行为埋点方法
        app.addLeaveActionPoint(null, endTime);
    },

    // 生命周期函数--监听页面卸载
    onUnload: function () {},

    // 页面相关事件处理函数--监听用户下拉动作
    onPullDownRefresh: function () {
        wx.showNavigationBarLoading();
        setTimeout(() => {
            // 再此调取接口，如果接口回调速度太快，为了展示loading效果，可以使用setTimeout
        }, 1000)
        wx.stopPullDownRefresh()
    },

    // 页面上拉触底事件的处理函数
    onReachBottom: function () {},

    // 用户点击右上角分享
    onShareAppMessage: function () {},

    // 点击 tab 时触发
    onTabItemTap: function () {
        this.checksession().then((res) => {}).catch(err => {});
    },

    // 跳转到用户信息页面
    getMessages() {
        wx.navigateTo({
            url: './message/message?source=' + this.data.currentPageId
        })
    },

    goAuthentification() {
        wx.navigateTo({
            url: './authentification/authentification'
        })
    },

    /**
     * 认证
     */
    getSubscribe() {
        this.checksession().then((res) => {
            wx.navigateTo({
                url: '../../pages/mine/subscribe/subscribe?source=' + this.data.currentPageId
            });
        }).catch((err) => {
            return;
        });
    },

    /**
     * 我的收藏
     */
    getCollect() {
            wx.navigateTo({
                url: '../../pages/mine/collect/collect?source=' + this.data.currentPageId
            });
    },

    /**
     * 足迹
     */
    getHistory() {
            wx.navigateTo({
                url: '../../pages/mine/history/history?source=' + this.data.currentPageId
            });
    },

    getUs() {
        wx.navigateTo({
            url: './questionnaire/questionnaire?source=' + this.data.currentPageId
        })
    },

    //点击我的活动
    getFeedback() {
                wx.navigateTo({
                    url: '../../pages/mine/feedback/feedback?source=' + this.data.currentPageId
                })
        },
    // 我的消息
    getMessage(){
        wx.navigateTo({
            url: '../../pages/mine/myMessage/myMessage?source=' + this.data.currentPageId
        })
    },
    // 我的审批
    getMember(){
        wx.navigateTo({
            url: '../../pages/mine/member/member?source=' + this.data.currentPageId
        })
    },
    //用户问卷
    getAnswer(){
      wx.navigateTo({
        url: '../../pages/mine/Answer/Answer?source=' + this.data.currentPageId
      })
    },
    // 会员认证
    getApprove() {
            // let userInfo = wx.getStorageSync('userInfo');
            // if (userInfo != null && userInfo.member != null) {
            //     if (userInfo.id != null && userInfo.id != '') {
            //         wx.showModal({
            //             title: '提示',
            //             content: '当前已存在会员信息，不能认证!',
            //             showCancel: false
            //         });
            //     } else {
            //         wx.showModal({
            //             title: '提示',
            //             content: '请切换至用户登录进行用户认证!',
            //             showCancel: false
            //         });
            //     }
            // } else {
                wx.navigateTo({
                    url: '../../pages/mine/authentification/authentification?source=' + this.data.currentPageId
                })
            // }
        },

    //点击私人订制跳转到私人订制
    getCustomize() {
        this.checksession().then((res) => {
            let userInfo = wx.getStorageSync('userInfo');
            if (userInfo != null && userInfo.member != null && userInfo.id == null) {
                wx.showModal({
                    title: '提示',
                    content: '请切换至用户登录进行私人订制!',
                    showCancel: false
                })
            } else {
                wx.navigateTo({
                    url: '../../pages/mine/customize/customize?source=' + this.data.currentPageId
                });
            }
        }).catch((err) => {
            return;
        });
    },

    getTime() {
        let myDate = new Date();
        let year = myDate.getFullYear();
        let month = myDate.getMonth() + 1;
        this.setData({
            time: year + '-' + month
        })
    },

    //点击企业注销
    memberLogout: function () {
        wx.removeStorageSync('userInfo');
        this.setData({
            userInfo: wx.getStorageSync('userInfo')
        })
    },

    //获取客户总数
    getClientCount: function () {
        post({
            className: 'user',
            method: 'clientCount',
            data: {}
        }).then(res => {
            if (res.data.code == 200) {
                this.setData({
                    clientCount: res.data.data
                })
            }
        })
    },

    //获取当前在线用户数量
    getOnlineCount: function () {
        post({
            className: 'user',
            method: 'getOnlineCount',
            data: {}
        }).then(res => {
            if (res.data.code == 200) {
                this.setData({
                    onlineCount: res.data.data
                })
            }
        })
    },

    //获取免费报告和收费报告
    getFreeAndExclusiveReport: function () {
        this.data.reportType.forEach(item => {
            post({
                className: 'report',
                method: 'getFreeAndExclusiveReport',
                data: {
                    label: item
                }
            }).then(res => {
                if (res.data.code == 200) {
                    if (item == 'freeLimitTime') {
                        this.setData({
                            freeLimitTime: res.data.data
                        })
                    }
                    if (item == 'exclusive') {
                        this.setData({
                            exclusive: res.data.data
                        })
                    }
                }
            })
        })
    },

    //获取已读报告个数
    getReadCount: function () {
        post({
            className: 'report',
            method: 'getReadCount',
            data: {}
        }).then(res => {
            this.setData({
                readCount: res.data.data
            })
        })
    },

    //获取最近添加报告个数（近一个月）
    getLatestAddCount: function () {
        post({
            className: 'report',
            method: 'getLatestAddCount',
            data: {}
        }).then(res => {
            if (res.data.code == 200) {
                this.setData({
                    latestAddCount: res.data.data
                })
            }
        })
    },

    //获取付费客户人数
    getPayingCustomers: function () {
        post({
            className: 'member',
            method: 'getPayingCustomers',
            data: {}
        }).then(res => {
            if (res.data.code == 200) {
                this.setData({
                    payingCustomers: res.data.data
                })
            }
        })
    },

    checksession() {
        return new Promise((resolve, reject) => {
            AUTH.checkHasLogined().then(isLogined => {
                if (isLogined) {
                    this.setData({
                        userInfo: wx.getStorageSync('userInfo')
                    })
                    resolve("checkSession--成功");
                } else {
                    this.setData({
                        userInfo: wx.getStorageSync('userInfo')
                    });
                    let one_login_date = wx.getStorageSync('one_login_date');
                    if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
                        wx.showModal({
                            title: '提示',
                            content: '未登录，请进行登录',
                            showCancel: false
                        });
                    } else {
                        wx.showModal({
                            title: '提示',
                            content: '未登录，请先登录',
                            showCancel: false
                        });
                    }
                    reject("checkSession--失败");
                }
            });
        })
    },
})