Component({
  properties: {},
  data: {
    x: 20, //定位X轴位置
    y: 20 //定位Y轴位置
  },
  pageLifetimes: {
    show: function () {
      var that = this;
      wx.getSystemInfo({
        success: function (res) {
          console.log('res',res)
          that.setData({
            x: res.windowWidth - 50,
            y: res.windowHeight / 2 + 50
          })
        },
        fail: function (res) {

        },
        complete: function (res) {

        },
      })
    },
    hide: function () {

    },
    resize: function () {

    },
  },
  methods: {

  }
})