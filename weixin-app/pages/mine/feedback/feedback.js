// const {
//   post
// } = require("../../../utils/api");
// const app = getApp()
// const AUTH = require("../../../utils/auth");

// Page({

//   /**
//    * 页面的初始数据
//    */
//   data: {
//     opinion: '',
//   },

//   submit(e) {
//     this.checksession().then((res) => {
//       let opinion = this.data.opinion
//       if (opinion == '') {
//         wx.showToast({
//           title: '请填写内容',
//         })
//         return false
//       } else {
//         let param = {
//           opinion: this.data.opinion
//         }
//         post({
//             className: "feedback",
//             method: "addFeedback",
//             data: param
//           })
//           .then(res => {
//             if (res.data.code == 200) {
//               wx.showToast({
//                 title: '提交成功',
//               });
//               setTimeout(() => {
//                 wx.navigateBack({
//                   delta: 2,
//                 })
//               }, 1000);
//             }
//           }).catch(err => {
//             console.log("error", err)
//           })
//       }
//     }).catch(err => {
//       let one_login_date = wx.getStorageSync('one_login_date');
//       if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
//         wx.showModal({
//           title: '提示',
//           content: '未登录，请进行登录',
//           showCancel: false,
//           success: res => {
//             wx.switchTab({
//               url: '../mine',
//             })
//           }
//         });
//       } else {
//         wx.showModal({
//           title: '提示',
//           content: '未登录，请先登录',
//           showCancel: false,
//           success: res => {
//             wx.switchTab({
//               url: '../mine',
//             })
//           }
//         });
//       }
//     })
//   },

//   //验证登录是否过期
//   checksession() {
//     return new Promise((resolve, reject) => {
//       AUTH.checkHasLogined().then(isLogined => {
//         if (isLogined) {
//           resolve("checkSession--成功");
//         } else {
//           reject("checkSession--失败");
//         }
//       });
//     })
//   },
// })
const {
  post
} = require("../../../utils/api");
const app = getApp()
const AUTH = require("../../../utils/auth");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    itemList:[],
    activityEventId:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this. getSignList ();
  },
//获取数据
  getSignList: function () {
    let param = {
        page: {
            pageIndex: 1,
            pageSize: 10
        },

    }
    
    post({
        className: "activity",
        method: "getSignList",
        data: param
        
    }).then((res) => {
        if (res.data.code == 200) {
          console.log(res,'9999999999999')
            this.setData({
              dataList: res.data.data.dataList,
            })
            console.log('this.data.dataList===',this.data.dataList);
        }
    }).catch(error => {
    })
}, 
getFeedbackItems(e) {
  console.log(e,'iiiiiiiiiiiiiiiiiii');
  this.setData({
    activityEventId: e.currentTarget.dataset.a.id
  })

  wx.navigateTo({
       url: '../feedbackItems/feedbackItems?activityEventId=' + this.data.activityEventId
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