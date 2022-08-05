/* 使用说明：
  startByTab()
    场景1--tab页面, 于onTabItemTap生命周期内调用, 切换至当前tab时触发埋点;
    场景2--被跳转页面, 于onShow生命周期内调用, 进入该页面时触发埋点;

  startByBack()
    场景--被跳转页面, 于onUnload生命周期内调用, 返回上一页时触发埋点;

  startByClick(当前点击事件返回的唯一id(currentTarget.id))
    场景--点击事件, 于页面bindtap返回方法内调用, 点击时触发埋点;
*/
const { post } = require("../utils/api");

//拿到设备型号
let unitType = () => {
   wx.getSystemInfo({
    success: (result) => {
      return result.model
    },
  })
}


// 新建页面埋点
let newStorage = (data) => {
  return {
    time: data && data.time || 0,// 访问事件
    click: data && data.click || [],// 点击事件
  }
}
let route = "";// 当前页面
let storage = null;// 埋点信息

// 计时器：记录当前页面停留时间
let TIME = null;
let buying = function () {
  let func = () => {
    storage.time++;
    // console.log('页面停留时间', storage.time);
  }
  TIME = setInterval(func, 1000);
}

// 获取埋点数据
let getSession = () => {
  wx.getStorage({
    key: route,
    success: (res) => {
      // 已有当前页信息
      storage = newStorage(res.data);
    },
    fail: (res) => {
      // 没有当前页信息
      storage = newStorage();
    },
    complete: () => {
      console.log("监听开始：", route, storage);
      buying();
    }
  })
}

// 停止统计 && 保存埋点数据 && 初始化页面变量
let setSession = () => {
  // 停止当前页统计
  clearInterval(TIME);

  // 保存当前页统计信息
  console.log("待存信息：");
  console.log("key:", route);
  console.log("storage:", storage);
  
  wx.setStorage({
    data: storage,
    key: route,
  })
  console.log("监听结束：", route, storage);

  // 初始化
  initData();
}

// 小程序切前台
wx.onAppShow((result) => {
  route = result.path;
  getSession(route);
})

// 小程序切后台
wx.onAppHide((result) => {
  setSession();
})

// 异常捕捉
wx.onError((error) => {
  console.error("捕获到异常错误：", error);
})

// 初始化信息
let initData = () => {
  route = "";
  storage = null;
  TIME = null;
}

module.exports = {
  // 通过tab触发埋点
  startByTab() {
    // 停止旧页面统计 && 保存旧页面信息
    setSession();

    // 获取新页面埋点信息 && 开始统计
    let arr = getCurrentPages();
    route = arr[arr.length - 1].route;
    getSession();
  },

  // 左上角返回按钮触发埋点
  startByBack(){
    // 停止旧页面统计 && 保存旧页面信息
    setSession();

    // 获取新页面埋点信息 && 开始统计
    let arr = getCurrentPages();
    route = arr[arr.length - 2].route;
    getSession();
  },

  // 通过点击事件触发埋点
  startByClick(id){
    let arr = storage.click.filter(item => item.id === id);
    if(arr.length) {
      // 自增
      arr[0].num++;

      // 点击事件次数到达阈值(点击数超过10次)，发起请求入库并清空相应字段
      for(let item of storage.click){
        if(storage.click.length>=10 || item.num>=3 ){
          setSession();
          //拿到客户端基础库版本
          let sdkVersion = wx.getSystemInfo({
            success: (result) => {
              result.SDKVersion
            }
          })
          let data = {
            equipment: unitType,        //设备信息
            version: sdkVersion,        //APP应用版本号
            address: wx.getStorageSync('address'),   //地名
            location: wx.getStorageSync('location'),    //经纬度
            consume: 0
          }
          post({ className: "point", method: "addPoint", data: {} }).then((res) => {
            if (res.data.code == 200) {
              let swiperList = res.data.data;
              this.setData({
                swiper: swiperList
              })
            } else {
              console.log("参数错误");
            }
          })
        }
      }
      
      // ...未完成

      storage.click.concat(arr);
      console.log(storage.click.length);
    }else{
      // 新增
      storage.click.push({ id: id, num: 1 });
    }
  },

  // 查询storage用量, 最大限制10MB
  getBalance(){
    wx.getStorageInfo({
      success: (option) => {
        console.log("当前内存使用量：", option.currentSize, "KB");
        console.log("剩余内存可用量：", `${option.limitSize - option.currentSize}KB(${100 - (option.currentSize / option.limitSize)}%)`)
      },
    })
  }
}