const plugin = requirePlugin('WechatSI');
// infoReport.js
const http = require("../../../utils/api");
// index.js
// 获取应用实例
const {
  post
} = require("../../../utils/api");
const app = getApp();
const {
  startTime,
  stopTime
} = require('../../../utils/stopWatch')
const {
  formatTime3
} = require('../../../utils/util')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTime: "00:00", //正在播放当前时间的时间
    alltime: "00:00", //总时长
    backgroundAudioManager: null, //当前InnerAudioContext对象
    audioFile: "", //微信同声传译生成的音频文件
    numberBackground: app.globalData.myBackground7,
    process: 0, //slider的值
    voiceBroadcastInfo: {}, //语音播报对象信息
    fromDays: 0, //距离天数
    showpdf: false, //展示pdf文件
    consume: '进入页面', //页面消费数据，进入页面、离开页面、点击、播放开始/结束，订，踩，评论等
    source: '', //页面来源，从哪个页面进入到这个页面
    withParameters: '', //携带参数
    pdfUrl: '',
    switch: false, //语音播放开关
    companyName: '', //项目公司
    userName: '', //联系人
    userphone: '', //联系方式
    productAdvantages: '', //产品或技术优势
    userInfo: {},
    testa:"",
    activityId: '0', //活动ID
    score: '1',
    score2: '1',
    path: '',
    filename: '',
    tempFilePatha: {
      appendixName:"",
      appendix:""
    },
    Activityintroductionlist:[],
    sdList: [],
    obj:{

    },
    obj2:{

    },
    myBackground: app.globalData.myBackground13
  },
  checkSeesion() {
    this.setData({
      userInfo: wx.getStorageSync('userInfo')
    })
    console.log('userInfo', this.data.userInfo);
    if (this.data.userInfo === '') {
      let one_login_date = wx.getStorageSync('one_login_date');
      console.log('one_login_date', one_login_date);
      if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
        wx.showModal({
          title: '提示',
          content: '未登录，请进行登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine/mine',
            })
          }
        });
      } else {
        wx.showModal({
          title: '提示',
          content: '未登录，请先登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine/mine',
            })
          }
        });
      }
    } else {
      console.log('验证urole');
      if (this.data.userInfo.urole === 1) {
        wx.navigateTo({
          url: './gp/gp'
        })
      } else {
        wx.navigateTo({
          url: './entfirm/entfirm'
        })
      }
    }

  },

  gettest: function () {

    let _that = this;
    console.log("111")
    wx.chooseMessageFile({
      count: 1, //能选择文件的数量
      type: 'file', //能选择文件的类型,我这里只允许上传文件.还有视频,图片,或者都可以
      success(res) {
        console.log("res.tempFiles", res.tempFiles)
        console.log("res.tempFiles.path", res.tempFiles[0].path)
        _that.setData({
            tempFilePatha: res.tempFiles[0]
          }),
          //图片转base64
          wx.chooseImage({
            success: res => {
            wx.getFileSystemManager().readFile({
                filePath: res.tempFilePaths[0], //选择图片返回的相对路径
                encoding: 'base64', //编码格式
                success: res => { //成功的回调
                  console.log('data:image/png;base64,' + res.data)
                }
              })
      
          //以下两行注释的是同步方法，不过我不太喜欢用。
                //let base64 = wx.getFileSystemManager().readFileSync(res.tempFilePaths[0], 'base64') 
              //console.log(base64)
            }
          })
      
      
          _that.data.tempFilePatha.appendixName= _that.data.tempFilePatha.name
          _that.data.tempFilePatha.appendix= _that.data.tempFilePatha.path
          console.log("this.data.tempFilePathas", _that.data.tempFilePatha)
          wx.showToast({
            title: '上传成功',
            icon: 'success',
            duration: 1500
          })
        // var size = res.tempFiles[0].size;

        // var filename = res.tempFiles[0].filename;

        // var newfilename = filename + ""; 



        //  if (size > 4194304||newfilename.indexOf(".pdf")==-1){ //我还限制了文件的大小和具体文件类型

        //    wx.showToast({

        //    title: '文件大小不能超过4MB,格式必须为pdf！',

        //    icon: "none",

        //    duration: 2000,

        //    mask: true

        //    })

        //   }else{

        //    that.setData({

        //    path: res.tempFiles[0].path, //将文件的路径保存在页面的变量上,方便 wx.uploadFile调用

        //    filename: filename    //渲染到wxml方便用户知道自己选择了什么文件

        //    })

        //   }

      }

    })
  },
  //项目公司
  getInputValue(e) {
    //console.log(e.detail)// {value: "ff", cursor: 2} 
    // console.log(wx.getStorageInfoSync('userInfo'))
    this.data.companyName = e.detail.value
    console.log(this.data.companyName);

  },
  //联系人
  getInputValue2(e) {
    //console.log(e.detail)// {value: "ff", cursor: 2} 
    // console.log(wx.getStorageInfoSync('userInfo'))
    this.data.userName = e.detail.value

  },
  //联系方式
  getInputValue3(e) {
    //console.log(e.detail)// {value: "ff", cursor: 2} 
    // console.log(wx.getStorageInfoSync('userInfo'))
    this.data.userphone = e.detail.value
    console.log(this.data.companyName);

  },
  //产品或技术优势代码
  getInputValue4(e) {
    //console.log(e.detail)// {value: "ff", cursor: 2} 
    // console.log(wx.getStorageInfoSync('userInfo'))
    // this.data.productAdvantages = e.detail.value
    // console.log(this.data.companyName);
    console.log('getInputValue4',e.detail.value);
  },
  //循环获取input框中的值
  getFillInInf:function(e){
    console.log("e.detail.value",e);
    // let item = `obj2.${e.target.id}`
    // this.setData({
    //   [item]: e.detail.value
    // })
    // console.log("qqqqqqqqqqqqqa", this.data.obj2);
    // let arr2 = []
    // let that=this;
    // for(let is in that.data.obj2){
    //   let obj2 = that.data.obj2
    //   arr2.push({
    //     templateId: is,
    //     answer: obj2[is]
    //   })
    // }
    // that.data.obj2=arr2
    // console.log("this.data.obj2",that.data.obj2)
  },
  gotoIndex() {
    console.log("999991", this.data.obj)
    console.log("9999912", this.data.obj2)
    console.log("yyyyyyyyyyy",this.data.tempFilePatha)
    console.log("this.data.userphone",this.data.Activityintroductionlist)
    let re = /^1\d{10}$/;

    if(this.data.userphone.length<1||this.data.companyName.length<1||this.data.userName.length<1){
      // wx.showToast({
      //   title: '带*项不能为空！',
      //   icon: 'none',
      //   duration: 1500
      // })
      wx.showModal({
        title: "提示",
        content: "带*项不能为空",
        showCancel: false,
        confirmColor: "#db3c40"
      });
      
    }
    // else if(!(/^1(3|4|5|7|8)\d{9}$/.test(this.data.userphone))){
    //   wx.showModal({
    //     title: "提示",
    //     content: "请输入正确的联系方式",
    //     showCancel: false,
    //     confirmColor: "#db3c40"
    //   });
    // }
    else if(this.data.userInfo==null||this.data.userInfo==''){
      // wx.showToast({
      //   title: '用户登录失效请重新登录后在填写',
      //   icon: 'none',
      //   duration: 1500
      // })
      wx.showModal({
        title: "提示",
        content: "用户登录失效请重新登录",
        showCancel: false,
        confirmColor: "#db3c40"
      });
      
    }else{
      console.log("活动用户Id",this.data)
      post({
        className: "home",
        method: "signUp",
        data: {
          "memberId": this.data.companyName,
          "name": this.data.userName,
          "activityEventId": this.data.activityId,
          "phone": this.data.userphone,
          "remark": this.data.productAdvantages,
          "sdList": this.data.sdList,
          // "customization": this.data.score2,
          "userId": this.data.userInfo.userId,
          "SignAppendixList":this.data.tempFilePatha
        }
      }).then(res => {
        console.log("999991", this.companyName)
        // this.setData({
        //     noReadNumber1: res.data.data
        // })
        console.log("-------------",res);
      })
      wx.switchTab({
        url: '../index'
        // success: (res => {
        //     res.eventChannel.emit('navitem', navItem);
        // })
      })
    }
  
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
   
    const eventChannel2 = this.getOpenerEventChannel()
    eventChannel2.on('Activityintroductionlist', Data => {
      console.log("Activityintroductionlist",Data)
      this.setData({
        Activityintroductionlist:Data,
        activityId: Data.id
      })
    })
    // this.setData({
    //   activityId: wx.getStorageSync('cookieKey')
    // })
    // this.data.= wx.getStorageSync('cookieKey')
    console.log("666666666666", this.data.activityId)
    // console.log("qqq",wx.getStorageSync("cookieKey"))

    for (let item of this.data.Activityintroductionlist.acList) {
      let itema = `obj.${item.id}`
      this.setData({
        [itema]:1
      })
      };
      console.log("this.data.obj",this.data.obj);

      // for (let item of this.data.Activityintroductionlist.acList) {
      //   let itema = `obj2.${item.id}`
      //   this.setData({
      //     [itema]:1
      //   })
      //   };
      //   console.log("this.data.obj2",this.data.obj2);

    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('item', res => {
      this.setData({
        voiceBroadcastInfo: res.data,
        source: options.source,
        reportId: res.data.id
      })
      const backgroundAudioManager = wx.getBackgroundAudioManager()
      this.setData({
        backgroundAudioManager: backgroundAudioManager
      });
      this.getDay(this.data.voiceBroadcastInfo.modifyTime);
      this.playBackgroundAUdio();
    })

    // this.getDuration();   
    // this.playVoice();
  },


  getLoadingData: function (source, withParameters, reportId) {
    startTime(); //进页面时间
    //调用添加行为埋点的方法添加行为埋点信息
    app.addIntoActionPoint(source, withParameters, reportId);
  },
  //获取单选框value
  laternext: function (e) {
    this.setData({
      score: e.detail.value
    })
    console.log("qqqqqqqqqqqqq", this.data.score);
  },
  laternext2: function (e) {
    console.log("qqqqqqqqqqqqq1",e)
    console.log(this.data.Activityintroductionlist.acList);
    let tempObj = {
      seq: e.currentTarget.dataset.model,
      answer: e.detail.value
    };
    let index = e.currentTarget.dataset.index
    let item = `sdList[`+index+`]` ; // 按索引赋值
    this.setData({
      [item]: tempObj	//item外层的括号记得加上，不然代码没作用
    });
    console.log('this.data.sdlist===',this.data.sdList);
    
    // let item = `obj.${e.target.id}`
    // this.setData({
    //   [item]: e.detail.value
    // })
    // console.log("qqqqqqqqqqqqq", this.data.obj);
    // let arr = []
    // for(let i in this.data.obj){
    //   let obj = this.data.obj
    //   arr.push({
    //     templateId: i,
    //     answer: obj[i]
    //   })
    // }
    // this.data.obj=arr
    // console.log("this.data.obj===",this.data.obj)
  },
  //将文件转换为base64
  toBase64: function (file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    })
  },

  //计算收到消息时间距离当天时间
  getDay: function (dataTime) {
    let data = dataTime.replace(/-/g, "/");
    //指定日期和时间
    let endTime = new Date(dataTime);
    //当前系统时间
    let nowTime = new Date();
    let t = nowTime.getTime() - endTime.getTime();
    let d = Math.floor(t / 1000 / 60 / 60 / 24);
    this.setData({
      fromDays: d
    })
  },

  gosubmitDetail:function(e){
    console.log("aaaa",e)
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
    // setTimeout(() => {
    //   this.getDuration();
    // }, 1000);
   
   this.checkSeesion()
    this.getLoadingData(this.data.source, this.data.withParameters, this.data.reportId)
  },


  //点击分享触发，打开web-view组件
  handleShare: function () {
    this.setData({
      pdfUrl: app.globalData.openPDF + this.data.voiceBroadcastInfo.url
    });
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    wx.downloadFile({ //下载
      url: this.data.pdfUrl, //服务器上的pdf地址
      success: function (res) {
        const filePath = res.tempFilePath
        wx.openDocument({ //打开
          filePath: filePath,
          success: function (res) {
            wx.hideLoading();
          }
        })
      }
    });
    // this.setData({
    //   showpdf: true
    // })
  },

  //读取文件内容
  // getFileContent: function () {
  //   PDFJS.GlobalWorkerOptions.workerSrc = '../../../utils/pdf.worker.js';
  //   let pdfurl = this.data.voiceBroadcastInfo.url;
  //   let document = PDFJS.getDocument({data:pdfurl})
  //   document.then(pdf => {
  //     let pagesPromises = [];
  //     for (let i = 0; i < pdf.pdfInfo.numPages; i++) {
  //       (function (pageNumber) {
  //         pagesPromises.push(getPageText(pageNumber, PDFJS));                                                                                                                                        
  //       })(i + 1);
  //     }
  //     Promise.all(pagesPromises).then(function (pagesText) {
  //       // Display text of all the pages in the console
  //     });
  //   }).catch(err => {
  //   })
  // },

  /**
   * 检索通过PDF .js获得的PDF文档中的规格页的文本
   * 
   * @param {Integer} pageNum 页面编号
   * @param {PDFDocument} PDFDocumentInstance 获取的PDF文件 
   **/
  getPageText(pageNum, PDFDocumentInstance) {
    // 返回一个Promise，一旦页面的文本被检索，这个Promise就被解决了
    return new Promise((resolve, reject) => {
      PDFDocumentInstance.getPage(pageNum).promise.then(pdfPage => {
        // 使用getTextContent方法
        pdfPage.getTextContent().then(textContent => {
          let textItems = textContent.items;
          let finalString = "";
          // 将项目的字符串连接到最后一个字符串
          for (let i = 0; i < textItems.length; i++) {
            let item = textItems[i];
            finalString += item.str + " ";
          }
          // 解决承诺与文本检索页面
          resolve(finalString);
        });
      });
    });
  },

  //初始化一个音频播放器，然后获取音频的总长度，然后将格式转化为00:00的格式显示
  getDuration: function () {
    let innerAudioContext = this.data.innerAudioContext;
    innerAudioContext.src = app.globalData.openPDF + this.data.voiceBroadcastInfo.voice;
    innerAudioContext.onCanplay(() => {
      innerAudioContext.play();
      this.playAudio();
    })
    innerAudioContext.onError((err) => {
      console.log(err, 'err-不可以播放') //报错
    })
  },

  //播放音频
  playAudio: function () {
    let backgroundAudioManager = this.data.backgroundAudioManager;
    backgroundAudioManager.onTimeUpdate(() => {
      let process = backgroundAudioManager.currentTime / backgroundAudioManager.duration * 100;
      //正常播放，更改进度信息，更改播放时间信息
      this.setData({
        process: process,
        currentTime: this.formatTime(backgroundAudioManager.currentTime),
        alltime: this.formatTime(backgroundAudioManager.duration)
      });
    })
  },

  //当slider被拖动时触发
  sliderChange: function (e) {
    let value = e.detail.value
    this.setData({
      process: value
    })
    let duration = this.data.backgroundAudioManager.duration
    let currentTime = parseInt(value * duration / 100)
    this.setData({
      currentTime: this.formatTime(currentTime)
    })
    this.data.backgroundAudioManager.pause();
    this.data.backgroundAudioManager.seek(currentTime);
    this.data.backgroundAudioManager.play();
  },

  //背景播放
  playBackgroundAUdio: function () {
    const backgroundAudioManager = wx.getBackgroundAudioManager()
    backgroundAudioManager.playbackRate = 0.9
    backgroundAudioManager.title = this.data.voiceBroadcastInfo.title
    this.setData({
      switch: true
    })
    backgroundAudioManager.src = app.globalData.openPDF + this.data.voiceBroadcastInfo.voice
    this.playAudio();
  },

  //点击停止
  stop: function () {
    this.setData({
      switch: false
    })
    this.data.backgroundAudioManager.pause();
  },

  //点击播放
  play: function () {
    this.setData({
      switch: true
    })
    this.data.backgroundAudioManager.play()
  },

  // //使用微信同声传译的方式将内容转化为音频输出
  // playVoice: function () {
  //   plugin.textToSpeech({
  //     lang: "zh_CN",
  //     tts: true,
  //     content: "产品经济是相对于自然经济和商品经济说的一种经济形式，也是马克思设想的在商品经济消亡以后的未来社会的交换方式。这种交换与商品交换的最大区别是，人与人之间的关系不再通过以货币为媒介的等价交换来表现，而是通过直接的产品交换来体现。",
  //     success: res => {
  //       this.setData({
  //         audioFile: res.filename
  //       })
  //       this.getDuration(res.filename);
  //     },
  //     fail: res => { }
  //   })
  // },


  // 秒数转分钟
  formatTime(time) {
    // 分
    let min = parseInt(time / 60) + "";
    if (min.length === 1) min = "0" + min;

    // 秒
    let sec = parseInt(time % 60) + "";
    if (sec.length === 1) sec = "0" + sec;

    return `${min}:${sec}`;
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    let endTime = stopTime();
    app.addLeaveActionPoint(this.data.reportId, endTime);
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    let endTime = stopTime();
    app.addLeaveActionPoint(this.data.reportId, endTime);
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