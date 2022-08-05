// pages/report/search/search.js
const {
    post,
    fileDownload
} = require("../../../../utils/api");
const {
    formatTime2
} = require('../../../../utils/util')
const app = getApp()
const AUTH = require("../../../../utils/auth");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        searchValue: '', // 输入框中文字内容
        flag: true, // 默认展示最近搜索历史
        reportTheme: '',
        rank: 'time', // 默认展示按时间排序的报告
        pageSize: 10, // 每次请求获取的数据条数
        returnResult: [], //每次请求返回的数据
        reportes: [], //展示的报告数据
        pageIndex: 1,
        historySearchText: [], //历史搜索记录
        currentPageId: '', //当前页面id
        showNoData: false, //展示没有数据图片
        noData: '/images/report/noData.png', //缺省图片路径
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let currentPageId = getCurrentPages();
        this.setData({
            currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id
            source: options.source
        })
        let historySearch = wx.getStorageSync('historySearch')
        if (Boolean(historySearch)) {
            this.setData({
                historySearchText: historySearch
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

    },

    //监听用户输入
    searchInput(e) {
        this.setData({
            searchValue: e.detail.value
        })
    },

    //点击取消清空搜索框输入内容
    // cleanSearchValue() {
    //     this.setData({
    //         searchValue: ''
    //     })
    // },
    //点击清除最近搜索
    cleanStorage() {
        wx.removeStorage({
            key: 'historySearch',
            success: res => {
                this.setData({
                    historySearchText: []
                })
            }
        })
    },

    //点击搜索
    search() {
        let index = this.data.historySearchText.findIndex(item => item === this.data.searchValue);
        if (index !== -1) {
            this.data.historySearchText.unshift(this.data.historySearchText.splice(index, 1)[0])
        } else {
            this.data.historySearchText.unshift(this.data.searchValue)
        }
        wx.setStorageSync('historySearch', this.data.historySearchText)
        this.setData({
            historySearchText: this.data.historySearchText,
            pageIndex: 1
        })
        this.setData({
            flag: false,
            reportes: []
        })
        // 请求后端搜索结果数据
        this.getReportList(this.data.searchValue, this.data.pageIndex);
    },

    //点击最近搜索中的记录
    selectSearchText: function (e) {

        let searchTest = e.currentTarget.dataset.item
        this.setData({
            searchValue: searchTest,
            pageIndex: 1,
            flag: false
        })
        let index = this.data.historySearchText.findIndex(item => item === this.data.searchValue);
        if (index !== -1) {
            this.data.historySearchText.unshift(this.data.historySearchText.splice(index, 1)[0])
            wx.setStorageSync('historySearch', this.data.historySearchText)
        } else {
            this.data.historySearchText.push(this.data.searchValue)
        }
        this.getReportList(this.data.searchValue, this.data.pageIndex);
    },

    //请求后端拿到展示数据
    getReportList(searchValue, pageIndex) {
        let param = {
            searchdata: {
                name: this.data.searchValue,
            },
            page: {
                pageIndex: pageIndex,
                pageSize: this.data.pageSize
            }
        }
        post({
            className: "collect",
            method: "getCollect",
            data: param
        }).then((res) => {
            console.log(res,'66666666666666666');
            if (res.data.code == 200) {
                console.log(res,'9999999999999999');
                this.setData({
                    returnResult: res.data.data.dataList
                })
                // for (let item of this.data.returnResult) {
                //     let date = new Date(item.product.timeLimit);
                //     item.product.timeLimit = formatTime2(date);
                //     this.getPic(item.productPicture.pic).then((res) => {
                //         item.productPicture.pic = res;
                //         this.setData({
                //             reportes: this.data.reportes
                //         })
                //     })
                // }
                // if (searchValue === this.data.searchValue) {
                //     let data = [...this.data.reportes, ...this.data.returnResult];
                //     this.setData({
                //         reportes: data,
                //         showNoData: Boolean(!res.data.data.dataList.length)
                //     })
                //     console.log(this.data.reportes,'rrrrrrrrrrrrrrrrrrrrr');
                // } else {
                //     this.setData({
                //         reportes: res.data.data.dataList,
                //     })
                // }
            } else {
                console.log("参数传递失败");
            }
        })
    },

    scrollToLower() {
        if (this.data.dataLength < this.data.pageSize) {
            wx.showToast({
                title: '已经到底了',
                icon: 'error',
                duration: 1000
            });
            return
        }
        this.setData({
            pageIndex: this.data.pageIndex + 1
        });
        this.getCollect();
    },


    

    collect(e) {
        var _that = this;
        this.checksession().then((res) => {
            const report = e.currentTarget.dataset.item;
            if (report != null) {
                let params = {
                    className: "collect",
                    method: "delCollect",
                    data: {
                        reportId: report.id
                    }
                }
                post(params).then((res) => {
                    if (res.data.code == 200) {
                        if (_that.data.reportes != null && _that.data.reportes.length > 0) {
                            let arr = new Array();
                            arr = _that.data.reportes;
                            arr.forEach((e, i) => {
                                if (e.id == report.id) {
                                    arr.splice(i, 1);
                                    return;
                                }
                            });

                            _that.setData({
                                reportes: arr
                            });
                        }
                    }
                }).catch(err => {
                    console.log(err)
                });
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
                            url: '../../mine',
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
                            url: '../../mine',
                        })
                    }
                });
            }
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