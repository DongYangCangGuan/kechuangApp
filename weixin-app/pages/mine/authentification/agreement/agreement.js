// pages/mine/authentification/agreement/agreement.js
Page({
  data: {
    wrap: false,
  },
  onShow() {
    wx.createSelectorQuery().select('#js_btn')
      .boundingClientRect((rect) => {
        if (rect.height > 48) {
          this.setData({ wrap: true });
        }
      })
      .exec();
  },
  navigateBack(){
    wx.navigateBack({
      delta: 1
    });  
  }
})