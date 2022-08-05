Page({

  /**
   * 页面的初始数据
   */
  data: {
    showNav: false, // 默认不显示
    showBottons: 'block', //默认显示
    webViewSrc: '', //打开的html路径，放置在web-view中的路径
    fontSize: 20, //字体大小
    maxNumSize: 100, //字体的最大值
    minNumSize: 10, //字体的最小值
    recursiveValue: 4, //递归的值
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
    setTimeout(() => {
      this.setData({
        showNav: true,
        webViewSrc: 'http://127.0.0.1:8085/downloadContent',
      })
    }, 300);
  },

  // //增加字体大小
  // increaseFontSize: function () {
  //   var sizeValue = this.data.fontSize + this.data.recursiveValue;
  //   if (sizeValue < this.data.maxNumSize) {
  //     this.setData({
  //       fontSize: sizeValue,
  //       webViewSrc: 'https://actort.club/downloadContent?size=' + sizeValue,
  //     });
  //     console.log(this.data.fontSize, this.data.webViewSrc, 'increaseFontSize-------->')
  //   }
  // },

  // //减少字体大小
  // decreaseFontSize: function () {
  //   var sizeValue = this.data.fontSize - this.data.recursiveValue;
  //   if (sizeValue > this.data.minNumSize) {
  //     this.setData({
  //       fontSize: sizeValue,
  //       webViewSrc: 'https://actort.club/downloadContent?size=' + sizeValue,
  //     });
  //     console.log(this.data.fontSize, this.data.webViewSrc, 'decreaseFontSize-------->')
  //   }
  // },

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

  webviewTap: function () {
    setTimeout(() => {
      var showBottons = !this.data.showBottons;
      this.setData({
        showBottons: showBottons,
        showNav: true
      });
      console.log(this.data.showNav, this.data.showBottons);
    }, 300);
  }
})