// infoReport.js
const http = require("../../../utils/api");
// index.js
// 获取应用实例
const {
    post
} = require("../../../utils/api");

// 获取应用实例
const app = getApp()

Page({
    /**
     * 页面的初始数据
     */
    data: {
        activitylist:[],
        pageIndex: 0,
        symyBackground1: app.globalData.symyBackground1,
        Activityintroduction:[],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    // onLoad: function (option) {
    //     this.getLoadingData();
    //     const eventChannel = this.getOpenerEventChannel()
    //     eventChannel.emit('acceptDataFromOpenedPage', { data: 'test' });
    //     eventChannel.emit('someEvent', { data: 'test' });
    //     // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
    //     eventChannel.on('acceptDataFromOpenerPage', function (data) {
    //         console.log(data)
    //     })
    //     this.getLoading();
    //     this.getCustom();
    //     this.setData({
    //         search: this.search.bind(this)
    //     })
    // },
    onLoad(options) {
      console.log("adfdsfas",options)
      if(Object.keys(options).length>0){
        console.log("1111111")
        this.getLoadingData(options.id);
      }else{
        const eventChannel = this.getOpenerEventChannel()
        eventChannel.on('Activityintroduction', Data => {
          console.log("Activityintroduction",Data)
          this.setData({
            Activityintroduction:Data
          })
          this.Activityintroduction=Data
        })

        // console.log("this.Activityintroduction.id",this.Activityintroduction.id)
        // this.getLoadingData();
        let currentPageId = getCurrentPages();
        this.setData({
            currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id  pages/index/index
        })

      }
       // this.getAddress();
       // this.getReport(this.data.reportKind, this.data.pageIndex);
    },

    onShareAppMessage: function (res) {
      console.log("1111")
      console.log("this.Activityintroduction.id",this.Activityintroduction.id)
      let id = this.Activityintroduction.id;
      console.log("ididid",id)
      return {
        title: "查看活动",
  
        // path: '/pages/details/details?id=' + id ,
        path: '/pages/index/infoReport/infoReport?id=' + id  ,
  
        // imageUrl: this.data.xiangqing.banner_image,  
  
        success: function (res) {
            wx.showToast({
              title: "分享成功",
  
              icon: 'success',
  
              duration: 2000
  
            })
  
         },
  
        fail: function (res) {
        },
  
      }
  
    },
    //查询活动介绍
    getLoadingData: function (id) {
       
        post({
            className: "home",
            method: "getActivity",
            data: {id:id}
        }).then(res => {
            this.setData({
              Activityintroduction: res.data.data[0]
            })
            console.log("999ssds99",this.data.Activityintroduction)
            // wx.setStorageSync("cookieKey",this.data.activitylist.id);
            // wx.setStorage("cookieKey",this.data.activitylist[0].id);
            wx.setStorage({
                key: 'cookieKey',
                data: this.data.activitylist[0].id
              })
            // console.log(wx.setStorage("cookieKey",this.data.activitylist[0].id));
            
        })
    },
    /**
     * 拉取报告信息底部时触发
     */
    downLoadingInfo: function () {
        this.setData({
            pageIndex: this.data.pageIndex + 1
        })
        this.getCustom();
    },
    goDetail(e) {
        // let navItem = e.currentTarget.dataset.theme;
        // console.log('111',navItem);
        // if(navItem === "entfirm") {
            console.log("e.currentTarget.dataset.theme2a",e.currentTarget.dataset.theme2)
          wx.navigateTo({
            url: '../voiceBroadcast/voiceBroadcast',
            success: (res => {
                // res.eventChannel.emit('navitem', navItem);
                res.eventChannel.emit('Activityintroductionlist', e.currentTarget.dataset.theme2);
            })
          })
        // } else if(navItem === "gp") {
        //   wx.navigateTo({
        //     url: './gp/gp',
        //     success: (res => {
        //         res.eventChannel.emit('navitem', navItem);
        //     })
        //   })
        // }
        
      },
    /**
     * 直播信息内数据的点击事件
     */
    groupInfoClick: function (e) {
        var dataset = e.currentTarget.dataset;
        //判断是否是上一次点击的那个列表
        //如果是 取消选中效果
        //如果不是 查询新信息
        if (this.data.groupInfo_selectIndex == dataset.index) {
            this.dataChange(undefined, undefined, -1, undefined, undefined, undefined, -1)
            this.getCustom()
        } else {
            this.dataChange(false, undefined, -1, undefined, undefined, -1, dataset.index)
            let params = {
                "groupCode": dataset.groupCode
            }
            this.queryParamsChanges(params)
            this.getCustom()
            return new Promise((resolve, reject) => {
                setTimeout(() => {
                    this.dataChange(undefined, undefined, -1, false, false, undefined, undefined)
                }, 100);
            })
        }
    },

    /**
     * 查询的取消
     */
    hideInput: function () {
        this.queryParamsChanges();
    },

    /**
     * 排行榜内数据的点击事件
     */
    rankInfoClick: function (e) {
        let dataset = e.currentTarget.dataset
        //判断是否是上一次点击的那个列表
        //如果是 取消选中效果
        //如果不是 查询新信息
        if (this.data.rankInfo_selectIndex == dataset.index) {
            this.dataChange(undefined, undefined, -1, undefined, undefined, -1, undefined)
            this.getCustom()
        } else {
            this.dataChange(undefined, false, -1, undefined, undefined, dataset.index, -1)
            let params = {
                "rankCode": dataset.rankCode
            }
            this.queryParamsChanges(params)
            this.getCustom()
            //延长100毫秒执行列表窗口隐藏
            return new Promise((resolve, reject) => {
                setTimeout(() => {
                    this.dataChange(undefined, undefined, -1, false, undefined, undefined, undefined)
                }, 100);
            })
        }
    },

    /**
     * 无效点击，不触发任何事件
     */
    bluehost: function () {

    },

    /**
     * 排行榜的点击事件
     */
    rankClick: function () {
        //判断是否具有选中效果
        let rank_sel = this.data.rank_sel ? false : true
        //判断是隐藏状态还是显示状态
        let rankInfo_box = this.data.rankInfo_box ? false : true
        //判断是否有选中
        rank_sel = this.data.rankInfo_selectIndex == -1 ? rank_sel : true
        //设置
        this.dataChange(rank_sel, undefined, -1, rankInfo_box, false, undefined, undefined)
    },

    /**
     * 直播活动的点击事件
     */
    groupClick: function () {
        //判断是否具有选中效果
        let group_sel = this.data.group_sel ? false : true
        //判断是隐藏状态还是显示状态
        let groupInfo_box = this.data.groupInfo_box ? false : true
        //判断是否有选中
        group_sel = this.data.groupInfo_selectIndex == -1 ? group_sel : true
        //设置
        this.dataChange(undefined, group_sel, -1, false, groupInfo_box, undefined, undefined)
    },

    /**
     * 分类列表的点击事件
     */
    kindInfoClick: function (e) {
        //获取页面以data-前缀修饰的属性的集合
        let dataset = e.currentTarget.dataset
        //判断json数据中是否存在指定字段
        let boo = dataset.hasOwnProperty("index")
        this.dataChange(false, false, dataset.index, false, false, -1, -1)
        let params = {
            "kindCode": dataset.kindCode
        }
        this.queryParamsChanges(params)
        //得到页面信息
        this.getCustom();
    },

    /**
     * 获取查询的结果
     */
    search: function (value) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve([{
                    text: value,
                    value: 1
                }])

                let params = {
                    searchValue: value
                }
                this.queryParamsChanges(params)
            }, 200)
        })
    },

    /**
     * 查询的点击事件
     */
    searchClick: function (e) {
        this.init();
        this.getCustom();
    },

    /**
     * 页面的全局点击事件
     */
    customClick: function () {
        this.dataChange(undefined, undefined, undefined, false, false, undefined, undefined)
    },

    /**
     * 页面的全局长按事件
     */
    customLongtap: function () {
        this.dataChange(undefined, undefined, undefined, false, false, undefined, undefined)
    },

    /**
     * 页面的全局触摸事件
     */
    customTouchmove: function () {
        this.dataChange(undefined, undefined, undefined, false, false, undefined, undefined)
    },

    /**
     * 页面加载时载入排行榜信息和直播活动信息
     */
    getLoading: function () {
        wx.setStorageSync('openId', '12312')
        wx.setStorageSync('token', '123')

        let openId = wx.getStorageSync('openId');
        let token = wx.getStorageSync('token');
        http.post({
            url: "getLoading",
            data: {
                "openId": openId,
                "token": token
            }
        }).then((res) => {
            if (res.code == 200) {
                this.setData({
                    data: res.data
                })
            }

            console.log("访问结果res", res)
        }).catch(err => {
            console.log("错误信息", err)
        })
    },

    /**
     * 根据条件查询页面信心
     */
    getCustom: function () {
        let openId = wx.getStorageSync('openId');
        let token = wx.getStorageSync('token');
        let params = {
            "pageIndex": this.data.pageIndex,
            "search": this.data.searchValue,
            "rank": this.data.rankCode,
            "group": this.data.groupCode,
            "kind": this.data.kindCode
        }

        console.log("getCustom方法的参数params", params)

        wx.login({
            success: (res) => {
                let params = {
                    "appId": app.globalData.appId,
                    "secret": app.globalData.secret,
                    "code": res.code
                }

                wx.request({
                    url: 'http://127.0.0.1:8083/applet/login',
                    data: JSON.stringify(params),
                    method: 'POST',
                    success: (res) => {
                        console.log(res);
                        this.setData({
                            "app.globalData.openId": res.data.openid
                        })

                        wx.setStorage({
                            data: res.data.openid,
                            key: 'openid',
                        })
                    }
                });
            }
        })

        http.post({
            url: "getAllInfoReport",
            data: {
                "openId": openId,
                "token": token,
                "data": params
            }
        }).then((res) => {
            if (res.code == 200) {
                var info = res.data.info
                if (this.data.datas !== undefined) {
                    info = [...this.data.datas.info, ...res.data.info]
                }

                if (this.data.pageIndex == 0) {
                    info = res.data.info
                    this.setData({
                        "topNum": 0
                    })
                }

                this.setData({
                    datas: {
                        kind: res.data.kind,
                        info: info
                    }
                })
            }
            console.log("访问结果res", res)
        }).catch(err => {
            console.log("错误信息", err)
        })
    },

    /**
     * 初始化
     * 取消所有选中
     */
    init: function () {
        this.setData({
            rank_sel: false, //排行榜是否选中
            group_sel: false, //直播活动是否选中
            kind_selectIndex: 0, //被选中的分类列表的元素的索引，默认第一个被选中
            groupInfo_box: false, //是否展示排行榜信息列表，默认不展示
            rankInfo_box: false, //是否展示直播活动信息列表，默认不展示
            groupInfo_selectIndex: -1, // 被选中的排行榜列表的元素的索引，默认没有选中
            rankInfo_selectIndex: -1 // 被选中的直播活动列表的元素的索引，默认没有选中
        })
    },

    /**
     * 选中值的变更
     */
    dataChange: function (rank_sel, group_sel, kind_selectIndex, rankInfo_box, groupInfo_box, rankInfo_selectIndex, groupInfo_selectIndex) {

        /**
         * 非空验证
         */
        if (rank_sel !== undefined) {
            this.setData({
                rank_sel: rank_sel
            })
        }
        if (group_sel !== undefined) {
            this.setData({
                group_sel: group_sel
            })
        }
        if (kind_selectIndex !== undefined) {
            this.setData({
                kind_selectIndex: kind_selectIndex
            })
        }
        if (rankInfo_box !== undefined) {
            this.setData({
                rankInfo_box: rankInfo_box
            })
        }
        if (groupInfo_box !== undefined) {
            this.setData({
                groupInfo_box: groupInfo_box
            })
        }
        if (rankInfo_selectIndex !== undefined) {
            this.setData({
                rankInfo_selectIndex: rankInfo_selectIndex
            })
        }
        if (groupInfo_selectIndex !== undefined) {
            this.setData({
                groupInfo_selectIndex: groupInfo_selectIndex
            })
        }
    },

    /**
     * 查询参数的变化
     */
    queryParamsChanges: function (params) {
        this.setData({
            pageIndex: 0,
            searchValue: undefined,
            rankCode: undefined,
            groupCode: undefined,
            kindCode: undefined
        })

        if (params.hasOwnProperty("pageIndex")) {
            this.setData({
                pageIndex: params.pageIndex
            })
        }

        if (params.hasOwnProperty("searchValue")) {
            this.setData({
                searchValue: params.searchValue
            })
        }
        if (params.hasOwnProperty("rankCode")) {
            this.setData({
                rankCode: params.rankCode
            })
        }
        if (params.hasOwnProperty("groupCode")) {
            this.setData({
                groupCode: params.groupCode
            })
        }
        if (params.hasOwnProperty("kindCode")) {
            this.setData({
                kindCode: params.kindCode
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
    onShow: function (res) {
        this.init();
        console.log("1221211",res)
         //在传递参数的页面js文件中
        const app=getApp()  
        // app.globalData.id="id"
        console.log("app.globalData.inf",app.globalData)
        //在接收参数的页面js文件中
        // const app=getApp()  
        // console.log('通过app.js全局变量传递参数',app.globalData)
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
    // onShareAppMessage: function () {

    // },
    // 点击 tab 时触发
    onTabItemTap: function () {
    },
})