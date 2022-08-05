// pages/information/information.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    informationList: [{
        title: '人才发展，是属于企业人力资源中重要的组成部分。人才不仅需要...',
        modifyTime: '2021-04-19',
        articleviews: '2.2w',
        pic: 'https://img-blog.csdnimg.cn/img_convert/1aebd966c7494c883103a80f16eb16db.png'
      },
      {
        title: '不知道你是否看过或者发过这样一种文案',
        modifyTime: '2021-04-19',
        articleviews: '2.2w',
        pic: 'https://img-blog.csdnimg.cn/img_convert/cd079b23c4a639cbf3485a9677fc1531.png'
      },
      {
        title: '马云：5G会把很多企业永远留在4G和3G时代，包括BAT',
        modifyTime: '2021-04-19',
        articleviews: '2.2w',
        pic: 'https://img-blog.csdnimg.cn/0e889c99cda34d0a90d19d0b620a6621.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5Y6G5Y-y5LiK55qE5LuK5aSp,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center'
      },
      {
        title: '“微信青蛙Pro”双面屏正式发布：前屏刷脸会员，后屏互动海报',
        modifyTime: '2021-04-19',
        articleviews: '2.2w',
        pic: 'https://img-blog.csdnimg.cn/img_convert/004da9ffca108b2f62db97c0802d5fa3.png'
      },
      {
        title: '李彦宏给AI落地发展提三点建议：充分利用开源和开放平台',
        modifyTime: '2021-04-19',
        articleviews: '2.2w',
        pic: 'https://img-blog.csdnimg.cn/img_convert/879665afc1b3ed2bea4d7e1ce31333c4.png'
      }
    ],
    showBottom: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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

  openHtml: function (e) {
    let reportId = e.currentTarget.dataset.item.id;
    wx.navigateTo({
      url: '../../pages/information/detail/detail',
    });
  }
})