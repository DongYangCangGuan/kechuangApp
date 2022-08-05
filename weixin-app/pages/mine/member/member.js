// pages/mine/member/member.js
// 获取应用实例
const {
  post
} = require("../../../utils/api");
const {
  formatTime2
} = require('../../../utils/util')
const app = getApp()
const AUTH = require("../../../utils/auth");
Page({

  /**
   * 页面的初始数据
   */
  data: {
      itemList:[],
      time: '',
      items:'',
      yes:"",
      id:'',
      memberId: '',
      uRole:'',
      userId:'',
      memberDetailList:{},
      pagesNum:1,
      maxPages: '',//分页总数量
      tyindex:1,
      totalNum: ''
   },
 
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let currentPageId = getCurrentPages();
        this.setData({
            currentPageId: currentPageId[currentPageId.length - 1].route, //拿到当前页面id
            source: options.source,
            pagesNum: 1
        })
        this.getApproveList (this.data.pagesNum);
  },
  // 获取数据
  getApproveList: function (pageIndex) {
      console.log('进入getApproveList');
    let param = {
        page: {
            pageIndex: pageIndex,
            pageSize: 10
        },
    }
    post({
        className: "Approve",
        method: "getApproveList",
        data: param
    }).then((res) => {
        if (res.data.code == 200) {
          console.log(res,'9999999999999')
            this.setData({
                itemList: res.data.data.dataList,
                maxPages: res.data.data.page.totalPage,
                totalNum: res.data.data.page.totalNum
            })
            let checked="itemList[0].checked"
            this.setData({
              [checked]:true
            })
        }
    }).catch(error => {
    })
}, 

pagesFn(e){
    let type = e.currentTarget.dataset.type;
    let _that = this;
    if(typeof type == "string"){//上下页
      if(type == "0"){//上一页
        if((_that.data.pagesNum - 1) % 3 === 0 && _that.data.pagesNum - 1 > 0){ 
            _that.setData({
              pagesNum: _that.data.pagesNum - 1,
              tyindex: _that.data.pagesNum - 3,
            })
        }else if((_that.data.pagesNum - 2) % 3 === 0 && _that.data.pagesNum - 1 > 0){ 
            _that.setData({
              pagesNum: _that.data.pagesNum - 1,
              
            })
        }else if((_that.data.pagesNum - 3) % 3 === 0 && _that.data.pagesNum - 1 > 0){ 
          _that.setData({
            pagesNum: _that.data.pagesNum - 1,
            
          })
        }
        console.log('pagesNum=',_that.data.pagesNum)
      }else if(type == "-1") {//下一页
        if(_that.data.pagesNum + 1 <=  _that.data.maxPages){ // 在最大页数之内
          if(_that.data.pagesNum % 3 == 0){
            _that.setData({
              tyindex: _that.data.pagesNum + 1,
            })
          }
          _that.setData({
            pagesNum: _that.data.pagesNum + 1,
          })
          console.log('pagesNum=',_that.data.pagesNum)
        }
      }
    } else {
      
      if(type > _that.data.pagesNum){
        if(type <= _that.data.maxPages){
          _that.setData({
            
            pagesNum: type,
          })
          console.log('pagesNum=',_that.data.pagesNum)
        }
      }
      if(type < _that.data.pagesNum){
        if(type >=1){
          _that.setData({
            
            pagesNum:type,
          })
        }
      }
      console.log('pagesNum=',_that.data.pagesNum)
    }
    _that.getApproveList(_that.data.pagesNum);
  },
//   getwatchData(val){
//       console.log('监听数据=',val);
//   },
  radioChange(e) {
    this.data.id='';
    console.log('eeeeeeeeeeeeeeeee',e,);
    this.setData({
      id:e.currentTarget.dataset.a.id,
      uRole:e.currentTarget.dataset.a.urole,
      userId:e.currentTarget.dataset.a.userId,
      memberId:e.currentTarget.dataset.a.memberId

  })
  console.log("this.data.id1",this.data.id)
  this.data.id=e.currentTarget.dataset.a.id;
  console.log("this.data.id2",this.data.id)
    const items = this.data.itemList
    for (let i = 0, len = items.length; i < len; ++i) {
      items[i].checked = items[i].value === e.detail.value
    }

    this.setData({
      items
    })
  },
  // 审批通过
  getAdopt(){
    let param={
      uRole:this.data.uRole,
      id:this.data.id,
      approvalstatus:'1',
      userId:this.data.userId
      
    }
    post({
      className: "Approve",
      method: "approve",
      data: param
  }).then((res) => {
      if (res.data.code == 200) {
          wx.showToast({
            title: '审批通过',
            icon: 'success',
            duration: 1500,
            mask: true,
            success: (res) => {
                this.setData({
                    itemList: []
                })
                this.getApproveList (this.data.pagesNum);
            },
            fail: (res) => {},
            complete: (res) => {},
          })   
          
      }
      
  }).catch(error => {
  })
  },
  goMemberDetail() {
    console.log("assa",this.data.itemList)
    console.log("this.data.id",this.data.id)
    if(this.data.id==''){
      wx.showToast({
        title: '请选择公司进行审核！',
        duration: 1000,
        icon: 'none',
        mask: true
      }) 
    }else{
      let param = {
        id:this.data.id
    }
      post({
        className: "Approve",
        method: "getMemberDetail",
        data: param
    }).then((res) => {
      console.log(res,'详情');
        if (res.data.code == '200') {
            this.setData({
                memberDetailList:res.data.data
            })
            this.memberDetailList=res.data.data
            console.log(this.data.memberDetailList,'ttttttttttttttttttt');
            wx.setStorageSync('memberDetailList', this.memberDetailList)
            wx.navigateTo({
              url: '../memberDetail/memberDetail?id='+this.data.id
            })

        }
    }).catch(error => {
    })
      // wx.navigateTo({
      //   url: '../memberDetail/memberDetail?id='+this.data.id
      // })
    }

    // let param={
    //     uRole:this.data.uRole,
    //     id:this.data.id,
    //     approvalstatus:'2',
    //     userId:this.data.userId
    //   }
    //   post({
    //     className: "Approve",
    //     method: "approve",
    //     data: param
    // }).then((res) => {
    //     if (res.data.code == 200) {
    //         wx.showToast({
    //           title: '审批拒绝',
    //           icon: 'success',
    //           duration: 1500,
    //           mask: true,
    //           success: (res) => {
    //               this.setData({
    //                   itemList: []
    //               })
    //               this. getApproveList (this.data.pagesNum);
    //           },
    //           fail: (res) => {},
    //           complete: (res) => {},
    //         })   
            
    //     }
        
    // }).catch(error => {
    // })
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
    this.getApproveList (this.data.pagesNum);
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