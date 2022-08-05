
const app = getApp();
const {
    post
} = require("../../utils/api");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        userInfo: {},
        userisnull:"",
        productis:false,
        myBackground:"../../images/product/bg2.png",
        noReadNumber1:[]
    },

    goAuthentification() {
        wx.navigateTo({
            url: '../mine/authentification/authentification'
        })
    },
    AuthenticationPage(){
      this.setData({
        productis:true
      })
    },

    goDetail(e) {
        let navItem = e.currentTarget.dataset.theme;
        console.log('111',navItem);
        if(navItem === "entfirm") {
            wx.navigateTo({
                url: './entfirm/entfirm',
                success: (res => {
                    res.eventChannel.emit('navitem', navItem);
                })
            })
        } else if(navItem === "gp") {
            wx.navigateTo({
                url: './gp/gp',
                success: (res => {
                    res.eventChannel.emit('navitem', navItem);
                })
            })
        }

    },

    goSearch(e) {
        console.log("GP:",e)
        wx.navigateTo({
            url: './entfirm/loans/nextpage/nextpage?code=' + e.currentTarget.dataset.theme.code
        })
    },
    //创业公司
    goSearch2(e) {
        console.log('e===',e)
        console.log("#33333",e.currentTarget.dataset.theme2.code)
        let code = e.currentTarget.dataset.theme2.code
        let id = e.currentTarget.dataset.theme2.id;//使用event.currentTarget.dataset.XX获取内容
        console.log("66666",id);
        let itemName = e.currentTarget.dataset.theme;
        if(code == '21') {
            wx.navigateTo({
                url: './entfirm/loans/loans?code=' + e.currentTarget.dataset.theme2.code
            })
        } else {
            wx.navigateTo({
                url: './entfirm/loans/nextpage/nextpage?code=' + e.currentTarget.dataset.theme2.code
                // url: './entfirm/loans/loans',
                // success: (res => {
                //     res.eventChannel.emit('itemName', itemName);
                //     res.eventChannel.emit('code', e.currentTarget.dataset.theme2.code);
                // })
            })
        }
    },

    checkSeesion() {
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
            if(this.data.userInfo.urole == '1' || this.data.userInfo.urole == '4' ) {
                console.log("bagearea")
                // wx.navigateTo({
                //   url: './gp/gp'
                // })
                this.getLoadingData();//GP
                this.setData({
                    userisnull : 2
                })
            } else if(this.data.userInfo.urole == '5') {
                // wx.navigateTo({
                //   url: './entfirm/entfirm'
                // })
                this.getLoadingData2();//创业公司
                this.setData({
                    userisnull : 3
                })
            }
            else if(this.data.userInfo.urole == '0') {
                // wx.navigateTo({
                //   url: './entfirm/entfirm'
                // })
                // this.getLoadingData();//GP
                // this.setData({
                //   userisnull : 2
                // })
                this.setData({
                    userisnull : 1
                })
            }else if(this.data.userInfo.urole == '3') {
                // wx.navigateTo({
                //   url: './gp/gp'
                // })
                this.getLoadingData();//创业公司
                this.setData({
                    userisnull : 1
                })
            }else if(this.data.userInfo.urole == '2') {
                // wx.navigateTo({
                //   url: './gp/gp'
                // })
                this.getLoadingData();//GP
                this.setData({
                    userisnull : 2
                })
            }else{
                // wx.navigateTo({
                //   url: './authenticationinformation/authenticationinformation'
                // })
                this.setData({
                    userisnull : 1
                })
            }
        }

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      this.setData({
        productis:false
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
      this.setData({
        productis:false
      })
        this.checkSeesion();


    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

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
    //请求后端，在页面加载时拿到轮播图数据以及添加行为埋点数据
    getLoadingData2: function () {
        console.log("999991")
        post({
            className: "dictionary",
            method: "selectByParent",
            data: {parentId:'2',
                kind:'producttype'
            }
        }).then(res => {

            this.setData({
                noReadNumber1: res.data.data
            })
            console.log("-------------------",this.data.noReadNumber1)
        })
    },
    //切换到创业
    goEntfirm() {
        wx.navigateTo({
            //url: '../entfirm/entfirm'
            url: '../../pages/product/entfirm/entfirm'
        })
    },
    //返回首页
    gohomepage:function(){
        console.log("aaad")
        // wx.switchTab({
        //     url: '../index/index'
        // })
        wx.navigateTo({
            url: '../../pages/mine/authentification/authentification'
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