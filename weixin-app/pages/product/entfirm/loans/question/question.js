// pages/product/entfirm/loans/question/question.js
const {
    post
} = require("../../../../../utils/api");
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        numberBackground: app.globalData.myBackground7,
        subList:{},
        questionnaire:[],
        userInfo: {},
        loanReason:[
            {name:"A、提高公司资金流动性",checked: false},
            {name:"B、公司有贸易融资背景实需（如收、付银票、信用证等）",checked: false},
            {name:"C、与自身经营发展相关的产业并购",checked: false}
        ],
        companyStage: [
            {name:"A、初创型科技企业",checked: false},
            {name:"B、科创板/创业板拟上市企业",checked: false}
        ],
        finTarget: [
            {name:"A、主营业务收入一亿元以上",checked: false},
            {name:"B、主营业务收入一亿元以下",checked: false},
            {name:"C、暂未实现盈利",checked: false},
            {name:"D、已实现微利",checked: false},
            {name:"E、已有稳定的利润和经营性现金流",checked: false}
        ],
        expectLine: [
            {name:"A、300万元人民币以内",checked: false},
            {name:"B、300万元--1000万元",checked: false},
            {name:"C、1000万元--3000万元",checked: false},
            {name:"D、30000万元--1亿元",checked: false},
            {name:"E、1亿元以上",checked: false}
        ],
        description: [
            {name:"A、具备政府相关部门资质认证（如高新技术/专精特新/小巨人等）",checked: false},
            {name:"B、具备专利权、软著权等知识产权",checked: false},
            {name:"C、经营范围中含有“研发”/“技术开发”等字样，且经营范围与上海市先导产业相关，包括集成电路/生物医药/人工智能等",checked: false},
            {name:"D、工信部认定的小型、微型企业",checked: false},
            {name:"E、其他创新企业（包括技术创新、模式创新、产品服务创新等）",checked: false}
        ]
    },

    getquestionnaire:function(){
      let DataCode="";
      const eventChannel = this.getOpenerEventChannel()
        eventChannel.once('isloan', Data => {
          console.log("贷款code",Data);  
         
        
        post({
            className: "product",
            method: "getQuestionnaire" ,
            data:{type:Data}
            
        }).then(res => {
            
            this.setData({
                questionnaire: res.data.data
            })
            console.log("---------qw----",this.data.questionnaire)
        })
      })
    },
    laternext:function(e){
        console.log("eeeeeeeeeeeeee",e)
    },
    gosubmitDetail(e) {
        this.setData({
            userInfo: wx.getStorageSync('userInfo')
        });
        console.log("user:",this.data.userInfo)
        console.log('form发生了submit事件，携带数据为：', e);
        
        let questionnaires={
            answer:[],//选项
            questionId:[]//问题Id
        };
        let keylist=[];
        let arrstrlist=[];
        for(let t in e.detail.value){
            //key:属性名
            keylist.push(t);
            arrstrlist.push(e.detail.value[t]) 
          }
          questionnaires.questionId=keylist.reverse();
          questionnaires.answer=arrstrlist.reverse();
         console.log("questionnaires",questionnaires)

         let dataList = []
         let answerList = questionnaires.answer;
         let idList = questionnaires.questionId;
         for(let i=0;i<answerList.length;i++) {
             if(answerList[i].length < 2) {
                dataList.push({
                    questionId: idList[i],
                    answer: answerList[i],
                    userId: this.data.userInfo.userId
                })
             } else {
                let tempobj = {
                    questionId:'',
                    answer: '',
                    userId:''
                }
                 for(let j=0;j<answerList[i].length;j++) {
                     tempobj.questionId = idList[i];
                     let arr = answerList[i];
                     tempobj.answer = ','+arr[j]+tempobj.answer;
                     tempobj.userId= this.data.userInfo.userId
                 }
                 tempobj.answer = tempobj.answer.substr(1);
                 dataList.push(tempobj);
             }
         }
         console.log('dataList=',dataList);
         let flag = true;
         for(let i=0;i<dataList.length;i++){
           if(dataList[i].answer.length==0){
              wx.showToast({
                title: '选填项不能为空该功能未上线！',
                icon: 'none',
                duration: 1500
              })
              flag = false;
              break;
           }
         }
         if(flag){
            post({
              className: "product",
              method: "answersQuestionnaire",
              data: dataList
            }).then(res => {
              wx.navigateTo({
                  url:'./submitdetail/submitdetail',
                  success: (res => {
                      res.eventChannel.emit('investigationcode', 1);
                    //   dataList = []
                  })
              })
            })
         }
       
        
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

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

        this.checkSeesion()
        this.getquestionnaire();
        
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