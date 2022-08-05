// 使用方法： 在需要使用的页面先引用，然后在data中加上  buttonClicked: false,在点击事件中调用ButtonClicked方法，将this作参数传进去，后面写页面逻辑就行
// 在wxml中点击事件绑定的部分用   '{{ !buttonClicked ? '事件名称' : '' }}'

let ButtonClicked = function (self) {
  self.setData({
    buttonClicked: true
  })
  setTimeout(function () {
    self.setData({
      buttonClicked: false
    })
  }, 1000)
}
module.exports = {
  ButtonClicked: ButtonClicked
}