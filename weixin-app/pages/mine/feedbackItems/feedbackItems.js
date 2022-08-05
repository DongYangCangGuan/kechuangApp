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
    activityEventId:'',
    signDetail:[],
    memberId:'',
    name:'',
    title:'',
    phone:'',
    sdList: [],
    customization:'',
    remark:'',
    checkY:'',
    checkT:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
    this.setData({
      activityEventId:options.activityEventId
    })
    this. getSignDetail ();
    
  },
//获取数据
getSignDetail: function () {
  console.log("this.data.activityEventId",this.data.activityEventId)
    let param = {
          activityEventId: this.data.activityEventId,
    }
    
    post({
        className: "activity",
        method: "getSignDetail",
        data: param
      }).then((res) => {
          console.log('6666666666666',res)
          if (res.data.code == 200) {
            let _that = this;
            console.log('aaaaaaaaaaaaaaaaa',res.data.data.memberId)
            _that.setData({
                memberId:res.data.data.memberId,
                name:res.data.data.name,
                phone:res.data.data.phone,
                sdList: res.data.data.sdList,
                customization:res.data.data.customization,
                remark:res.data.data.remark
            })
            console.log('this.data.memberId===',_that.data.sdList);
          }
          
          this.checkReadymade();
          this.checkCustomization();
      }).catch(error => {
      })
}, 
checkCustomization(){
  if(this.data.customization==1){
  this.setData({
    checkY:true,
  })
}else{
  this.setData({
    checkY:false,
  })
}

},

checkReadymade(){
  if(this.data.readymade==1){
    this.setData({
      checkT:true,
    })
  }else{
    this.setData({
      checkT:false,
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
    
  }
})