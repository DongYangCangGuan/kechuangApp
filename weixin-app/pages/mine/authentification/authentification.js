// pages/mine/authentification/authentification.js
const {
  post
} = require("../../../utils/api");
const app = getApp()
const {
  startTime,
  stopTime
} = require('../../../utils/stopWatch')
const AUTH = require("../../../utils/auth");

Page({

  /**
   * 页面的初始数据
   */
  data: {
    value1: '',
    value2: '',
    jobVal: '', // 职位
    jobList: [],
    value4: '',
    value5:'',
    checked:false,
  　is_agree: false,//判断我已阅读并同意是否为选中状态
  　myVal:false,
    certificationMark: '',
    memberList:[],
    profile: true,
    error: '',
    realName: '',
    enterpriseCode: '',
    enterpriseName:'',
    email: '',
    phone: '',
    formData: {
        user: {},
        member: {}
    },
    source: '', //页面来源，从哪个页面跳转过来
    defaultType: true,
    passwordType: true,
    perfileRules: [{
      name: 'realName',rules: {required: true, message: '请输入姓名' },
    }, {
      name: 'phone', rules: [{ required: true, message: '请输入手机号'
      }, {mobile: true, message: '请输入正确手机号！'}]
    }, {
      name: 'email',rules: [{ required: true, message: '请输入邮箱'
      }, { email: true, message: '请输入正确邮箱！'}]
    }],
    memberInfo: {},
    shows: false, //控制下拉列表的显示隐藏，false隐藏、true显示
    shows2: false, //控制下拉列表的显示隐藏，false隐藏、true显示
    showJob: false,  // 控制职位是否选择
    selectDatas: ['创业公司', 'GP', 'LP', '合作伙伴', '科创'], //下拉列表的数据
    // job:['HR','CEO','普通员工'],
    selectDatas2: [], //下拉列表的数据
    idx : 0,
    indexs: 0, //选择的下拉列 表下标,
    indexss: 0, //选择的下拉列 表下标,
    indexs2: 0, //选择的下拉列 表下标,
    indexss2: 0, //选择的下拉列 表下标,
    MemberType: '', //身份类型
    MemberType2: '', //创业科创基金
    enterpriceCode: '', //公司code
    pwd: '', //认证码
    kechuang:'科创投资基金公司',
    inputVal: "", // 搜索框值
    inputVals: "", // 搜索框值
    catList: [], //搜索渲染推荐数据
    enterpriseList: [], //搜索渲染推荐数据
    casList: [], //搜索渲染推荐数据
    test:false,
    test2:false,
    list1: [],
    list2:[],
    list3: [],
    list4:[],
    bianhao:'',
    investmentFund:'',
    companyName: '',
    viewShowed: false,
    showDialog: false
  },

  bindPicker1Change(e) {
    console.log("点击事件1",e)
    // if(e.detail.value)
    // this.getMembers(e.detail.value);
    this.setData({
      value1: e.detail.value,
      test: true,
      companyName: ''
    });
    let value = this.data.value1
    if(value=='0'){ //创业公司
      this.setData({
        [`formData.member.MemberType`]: 5,
        MemberType: 5,
        showJob: true,
        value5: '',
        jobList: ['董事长','CEO','CFO','HR','CTO']
      })
    }else if(value=='1'){ // GP
        this.setData({
          [`formData.member.MemberType`]: 1,
          MemberType: 1,
          showJob: true,
          value5: '',
          jobList: ['合伙人','CFO','IR']
        })
    }else if(value=='2'){ // LP
        this.setData({
          [`formData.member.MemberType`]: 3,
          MemberType: 3,
          showJob: false
        })
    }else if(value=='3'){ // 合作伙伴/供应商
        this.setData({
          [`formData.member.MemberType`]: 4,
          MemberType: 4,
          showJob: false
        })
    }else if(value=='4'){ // 科创
        this.setData({
          [`formData.member.MemberType`]: 2,
          MemberType: 2,
          showJob: false
        })
    }
    
    // this.setData({
    //     [`formData.member.MemberType`]: parseInt(e.detail.value),
    // });
    console.log('formData.member.MemberType',this.data.formData.member.MemberType);

  },

  bindPicker4Change(e) {
    console.log("点击事件",e)
    this.setData({
      value5: e.detail.value,
    });
    let jobs=this.data.jobList
     this.setData({
        [`formData.member.job`]: jobs[e.detail.value] ,
    });
  },

  chooseJob(e) {
    //   console.log('输入的job===',e.detail.value)
    this.setData({
        [`formData.member.job`]: e.detail.value
    });
  },

  bindPicker2Change(e) {
    this.setData({
      value2: e.detail.value,
    });
    let value =this.data.value2
    var list1 = this.data.list2
        this.setData({
          inputVal: list1[value],
        })
        //  this.getBianHao();
        let list5=this.data.memberList
        console.log("log",list5)
        for (let i = 0; i <list5.length; i++){
          let inputVal = this.data.inputVal
           if(inputVal==list5[i].enterpriseName){
             this.setData({
              bianhao:list5[i].enterpriseCode,
             })
           }
         }
        
        console.log(this.data.bianhao)
        this.setData({
          [`formData.member.enterpriseCode`]: this.data.bianhao,
        })
  },

  searchInput() {},

  // 选择职位
  // bindPicker4Change(e) {
  //   console.log('e===',e);
  //   this.setData({
  //     jobVal: e.detail.value,
  //   });
  //   // let value =this.data.value3
  //   // var list1 = this.data.list2
  //   //     this.setData({
  //   //       inputVal: list1[value],
  //   //     })
  //   //     //  this.getBianHao();
  //   //     let list5=this.data.memberList
  //   //     console.log("log",list5)
  //   //     for (let i = 0; i <list5.length; i++){
  //   //       let inputVal = this.data.inputVal
  //   //        if(inputVal==list5[i].enterpriseName){
  //   //          this.setData({
  //   //           bianhao:list5[i].enterpriseCode,
  //   //          })
  //   //        }
  //   //      }
        
  //   //     console.log(this.data.bianhao)
  //   //     this.setData({
  //   //       [`formData.member.enterpriseCode`]: this.data.bianhao,
  //   //     })
  // },

  bindPicker3Change(e){
    console.log("uuuu",e)
    this.setData({
      value4: e.detail.value,
    });
    let value =this.data.value4
    var list1 = this.data.list4
        this.setData({
          inputVals: list1[value],
        })
        //  this.getBianHao();
        this.setData({
          [`formData.member.enterpriseName`]: this.data.inputVals,
        })
  },

  

  inputType(e) {
        this.getMembers(e);
  },


  // 点击公司名称出发事件
//   inputType: function (e) {
//     console.log(e,'eeeeeeeeeeeeeeeeeeee');
//     var value = e.detail.value
//     var that = this;
//     var list1 = that.data.list2
//     if (value == '') {
//       that.setData({
//         viewShowed: false,
//       });
//     } else {
//       if (e.detail.cursor) { //e.detail.cursor表示input值当前焦点所在的位置
//         var arr = [];
//         for (var i = 0; i < list1.length; i++) {
//           if (list1[i].indexOf(value) >= 0) {
//             arr.push(list1[i]);
//           }
//         }
//         if (arr == '') {
//           that.setData({
//             viewShowed: false,
//           });
//         } else {
//           that.setData({
//             viewShowed: true,
//             enterpriseList: arr
//           });
//         }
//       }
//     }
//   },
//   同步选项
  showName(e) {
    let index = e.currentTarget.dataset.index;
    console.log('e=',e);
    this.setData({
        companyName: this.data.list2[index],
        viewShowed: false,
    })
    let arr = this.data.memberList
    for (let i = 0; i < arr.length; i++){
        let val = this.data.companyName
         if(val == arr[i].enterpriseName){
           this.setData({
             [`formData.member.enterpriseCode`]: arr[i].enterpriseCode,
           })
         }
    }
  },
// getBianHao() {
//     let param = {
//     }
//     post({
//       className: "authentication",
//       method: "getMembers",
//       data: param
//     }).then((res) => {
//       if (res.data.code == 200) {
//          let itemList= res.data.data;
//          this.setData({
//           memberList:itemList,
//          })
//          console.log("memberList",this.data.memberList)
//         //  for (let i = 0; i < itemList.length; i++){
//         //   let inputVal = this.data.inputVal
//         //    if(inputVal==itemList[i].enterpriseName){
//         //      this.setData({
//         //       bianhao:itemList[i].enterpriseCode,
//         //      })
//         //    }
//         //  }
//       }

//     }).catch(error => {})
//   },

  inputTyping: function (e) {
    var that = this;
    that.setData({
      [`formData.member.enterpriseName`]: e.detail.value,
    });
    var list3 = that.data.list4
    console.log("iiiii",list3)
    let value = that.data.enterpriseName;
    // this.data.enterpriseName=value;
    if (value == '') {
      that.setData({
        viewShoweds: false,
      });
    } else {
      if (e.detail.cursor) { //e.detail.cursor表示input值当前焦点所在的位置
        let arr = [];
        for (var i = 0; i < list3.length; i++) {
          if (list3[i].indexOf(value) >= 0) {
            arr.push(list3[i]);
          }
        }
        console.log("arrarrarr",arr)
        if(arr.length > 0) {
          that.setData({
            viewShoweds: true,
            casList: arr
          });
          console.log("casListcasList",this.data.casList)
        } else {
          that.setData({
            viewShoweds: false,
          });
        }
      }
    }
  },

  // // 获取选中推荐列表中的值
  // name: function (e) {
  //   var index = e.currentTarget.dataset.index
  //   this.setData({
  //     inputVal: this.data.enterpriseList[index],
  //     viewShowed: false,
  //   })
  //   this.getBianHao();
  // },

  // names: function (e) {
  //   var index = e.currentTarget.dataset.index
  //   this.setData({
  //     inputVals: this.data.casList[index],
  //     viewShoweds: false,
  //   })
  // },
  // 点击认证码出发事件
  companyInputChange:function (e) {
    this.setData({
      pwd: e.detail.value
    })
    const {
      field
    } = e.currentTarget.dataset;
    this.setData({
      [`formData.member.${field}`]: e.detail.value,
    })
  },


  // 点击下拉列表
  // optionTaps(e) {
  //   this.setData({
  //     test: true
  //   })
  //   console.log("获取下拉对象",e)
  //   let indexx = e.currentTarget.dataset.index
  //   let Indexs = e.currentTarget.dataset.index - 1 + 2; //获取点击的下拉列表的下标
  //   console.log(Indexs,'iiiiiiiiiiiiiiiiiiiiiiiiiii');
  //   if(indexx=='0'){
  //     this.setData({
  //       test :true
  //     })
  //   }else  if(indexx=='4'){
  //     this.setData({
  //       test :false,
  //       test2:true
  //     })
  //   }else{
  //     this.setData({
  //       test :false
  //     })
  //   }
  //   this.setData({
  //     indexs:indexx,
  //     MemberType: '00' + Indexs,
  //     shows: !this.data.shows,
  //       [`formData.member.MemberType`]: parseInt(e.currentTarget.dataset.index) +1,
  //   });
  // },
   // 点击下拉列表

  //  optionTaps2(e) {
  //   console.log("获取下拉对象a",e)
  //   let indexx2 = e.currentTarget.dataset.index
  //   let Indexs2 = e.currentTarget.dataset.index - 1 + 2; //获取点击的下拉列表的下标
  //   console.log(Indexs2,'iiiiiiiiiiiiiiiiiiiiiiiiiiid');
  //   this.setData({
  //     indexs2:indexx2,
  //     MemberType2: '00' + Indexs2,
  //     shows2: !this.data.shows2,
  //     enterpriseName: e.currentTarget.dataset.itme
  //       // [`formData.member.MemberType`]: parseInt(e.currentTarget.dataset.index) +1,
  //   });
  // },
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("ttttttttt",this.data.selectDatas[0]) ;
    this.setData({
      userInfo: wx.getStorageSync('userInfo')
    });
    // console.log("this.data.userInfo",this.data.userInfo)
    this.setData({
      source: options.source,
      realName: this.data.userInfo.realName,
      enterpriseCode: this.data.userInfo.enterpriseCode,
      email: this.data.userInfo.email,
      phone: this.data.userInfo.phone,
      certificationMark: this.data.userInfo.certificationMark,
    });
    
    this.getInvest();
    // this.optionTaps();
    // this.optionTaps2();
  },
// 获取数据


  getMembers(e) {
    console.log("eeeeeeeeeeee",e);
    console.log('this.data.value1',this.data.MemberType);
    if(e.detail.value === '') {
        this.setData({
          viewShowed: false
        })
    } else {
        let param = {
            page: {
            },
          }
          post({
            className: "authentication",
            method: "getMembers",
            data: {
                memberType : this.data.MemberType,
                enterpriseName: e.detail.value
              }
          }).then((res) => {
              console.log('res===',res);
            if (res.data.code == 200) {
               let itemList= res.data.data;
               let list3 = [];
               for (let i = 0; i < itemList.length; i++){
                 list3.push(itemList[i].enterpriseName)
               }
              this.setData({
                list2:list3,
                memberList:itemList,
                viewShowed: true
              })
            }
            console.log(this.data.list2);
          }).catch(error => {})
    }
    
    // let identitytype= '';
    // if(e == 0){
    //   identitytype=5
    // }else if(e == 1){
    //   identitytype=1
    // }else if(e == 2){
    //   identitytype=3
    // }else if(e==3){
    //   identitytype=4
    // }else if(e==4){
    //   identitytype=2
    // }
    
  },

  getInvest() {
    let param = {
    }
    post({
      className: "authentication",
      method: "getInvestmentFund",
      data: param
    }).then((res) => {
      if (res.data.code == 200) {
         let itemList= res.data.data;
         let list3 = [];
         for (let i = 0; i < itemList.length; i++){
           list3.push(itemList[i].enterpriseName)
         }
        this.setData({
          list4:list3
        })
      }
      
    }).catch(error => {})
  },

  getInvestmentFund() {
    let param = {
    }
    post({
      className: "authentication",
      method: "getInvestmentFund",
      data: param
    }).then((res) => {
      if (res.data.code == 200) {
         let itemList= res.data.data;
         for (let i = 0; i < itemList.length; i++){
          let inputVals= this.data.inputVals
           if(inputVals==itemList[i].enterpriseName){
             this.setData({
              investmentFund:itemList[i].id,
             })
           }
         }
      }

    }).catch(error => {})
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
    this.getLoadingData(this.data.source, null, null);
    // this.setData({
    //   memberInfo: wx.getStorageSync('memberInfo')
    // })
    this.checksession();
   
  },

  getLoadingData(source, withParameters, reportId) {
    startTime();
    app.addIntoActionPoint(source, withParameters, reportId);
  },

  /**
   * 当表中输入的数据发生变化时触发
   */
  profileInputChange: function (e) {
    const {
      field
    } = e.currentTarget.dataset

    this.setData({
      [`formData.user.${field}`]: e.detail.value
    })
  },




  //密码type切换
  eyeStatus: function () {
    this.data.defaultType = !this.data.defaultType
    this.data.passwordType = !this.data.passwordType
    this.setData({
      defaultType: this.data.defaultType,
      passwordType: this.data.passwordType
    });
  },

  /**
   * 点击下一步触发
   */
  nextStep: function () {
    this.checksession().then((res) => {
      this.selectComponent('#profileForm').validate((valid, errors) => {
        console.log('valid', valid, errors)
        if (!valid) {
          const firstError = Object.keys(errors)
          if (firstError.length) {
            this.setData({
              error: errors[firstError[0]].message
            })
            wx.showToast({
              title: this.data.error,
              icon: 'none'
            })
          }
        } else {
          wx.showToast({
            title: '校验通过'
          })
        //   this.setData({
        //     profile: false
        //   })
        }
      });
    }).catch(err => {
      let one_login_date = wx.getStorageSync('one_login_date');
      if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
        wx.showModal({
          title: '提示',
          content: '未登录，请进行登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine',
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
              url: '../mine',
            })
          }
        });
      }
    });
  },

  //点击下一步触发，没有表单验证
//   nextStep: function(){
//     this.setData({
//       profile: false
//     })
//   },

  //点击跳过触发
  jump: function () {
    this.checksession().then((res) => {
      this.setData({
        profile: false
      })
    }).catch(err => {
      let one_login_date = wx.getStorageSync('one_login_date');
      if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
        wx.showModal({
          title: '提示',
          content: '未登录，请进行登录',
          showCancel: false,
          success: res => {
            wx.switchTab({
              url: '../mine',
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
              url: '../mine',
            })
          }
        });
      }
    });
  },

  /**
   * 点击提交申请触发，先进行表单验证操作
   */
  commitApply: function () {
    console.log(this.data.checked)
    console.log("this.data",this.data)
    console.log("this.data.formData",this.data.formData)
    if(this.data.myVal==true){
      if(this.data.MemberType=='2'){
        this.setData({
          [`formData.member.enterpriseCode`]: 'kc0001',
          companyName: '0',
        })
      }
      console.log("this.data.formData.member.enterpriseCode",this.data.formData.member.enterpriseCode)
      this.checksession().then((res) => {
          this.selectComponent('#profileForm').validate((valid, errors) => {
            console.log('valid', valid, errors)
            if (!valid) {
              const firstError = Object.keys(errors)
              if (firstError.length) {
                this.setData({
                  error: errors[firstError[0]].message
                })
                wx.showToast({
                  title: this.data.error,
                  icon: 'none'
                })
              }
            } else {
              if(this.data.companyName == '' || this.data.formData.member.enterpriseCode == undefined){
                  wx.showToast({
                    title: '请输入正确的公司名称',
                    duration: 1000,
                    icon: 'none',
                    mask: true
                  }) 
              }else if(this.data.formData.member.MemberType == 5 || this.data.formData.member.MemberType==1){
                console.log("1232441234124123")
                if(this.data.formData.member.job==undefined || this.data.formData.member.job==''){
                    wx.showToast({
                      title: '请选择职位',
                      duration: 1000,
                      icon: 'none',
                      mask: true
                    }) 
                }else  {
                  wx.showToast({
                    title: '校验通过'
                  })
                  this.authentication();
                }
              } else  {
                    wx.showToast({
                      title: '校验通过'
                    })
                    this.authentication();
                  }
              }
          });
        }).catch(err => {
          let one_login_date = wx.getStorageSync('one_login_date');
          if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
            wx.showModal({
              title: '提示',
              content: '未登录，请进行登录',
              showCancel: false,
              success: res => {
                wx.switchTab({
                  url: '../mine',
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
                  url: '../mine',
                })
              }
            });
          }
      });
  }
  },
  checkboxChange: function (e) {
    　//监听checkbox点击事件
      console.log(e,"点击了外层")

　　if(e.detail.value.length == 0){  // 当用户点击了取消了checkbox，e.detail.value值就为undefined就为0
        console.log("12",e.detail.value.length)
        this.setData({ //并把is_agree设置为空就不会选中
          is_agree:false,　　
          myVal:false,
        })
      }else{   //当用户点击了checkbox确定了，值为1
        console.log("21",e.detail.value.length)
        this.setData({
          is_agree:true,
          myVal:true,
        })
        // this.confirmOrder()
      }
  },

  /**
   * 向后台请求用户认证
   */
  authentication: function () {
    console.log("this.data.formData",this.data.formData)
    let data = this.data.formData;
    post({
      className: "authentication",
      method: "checkMember",
      data: data
    }).then(res => {
      console.log('res===',res);
      if (res.data.code == 200) {
        // wx.showToast({
        //   title: '认证成功',
        //   icon: 'success',
        //   duration: 3000,
        //   success: res => {
        //     setTimeout(() => {
        //       wx.switchTab({
        //         url: '../../mine/mine',
        //       })
        //     }, 3000)
        //   }
        // })
        post({
          className: "user",
          method: "getUser",
          data
        }).then(res => {
          if (res.data.code == 200) {
            console.log('8888888888',res);
            wx.setStorageSync('userInfo', res.data.data);
            wx.showModal({
                title: '提示',
                content: '您的认证信息已收到，审核通过后将通知您！',
                showCancel: false,
                success: res => {
                    //要延时执行的代码
                    wx.switchTab({
                        url: '../../mine/mine',
                      })
                    // setTimeout(() => {
                      
                    // }, 2000)
                  }
              });
            // wx.showToast({
            //   title: '您的认证信息已收到，审核通过后将通知您！',
            //   icon: 'success',
            //   duration: 3000,
            //   success: res => {
            //     setTimeout(() => {
            //       //要延时执行的代码
            //       wx.switchTab({
            //         url: '../../mine/mine',
            //       })
            //     }, 3000)
            //   }
            // })
          }
        }).catch(err => {
          console.log(err);
        })
      } else {
        wx.showToast({
          title: res.data.data.msg,
          icon: 'error',
          duration: 3000
        })
      }
    }).catch(err => {
      wx.showToast({
        title: '用户名密码错误',
        icon: 'error',
        duration: 3000
      })
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    let endTime = stopTime();
    //调用离开页面的行为埋点方法
    app.addLeaveActionPoint(null, endTime);
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    let endTime = stopTime();
    //调用离开页面的行为埋点方法
    app.addLeaveActionPoint(null, endTime);
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

  checksession() {
    return new Promise((resolve, reject) => {
      AUTH.checkHasLogined().then(isLogined => {
        if (isLogined) {
          this.setData({
            userInfo: wx.getStorageSync('userInfo')
          })
          resolve("checkSession--成功");
        } else {
            this.setData({
                userInfo: wx.getStorageSync('userInfo')
            });
            let one_login_date = wx.getStorageSync('one_login_date');
            if (one_login_date !== undefined && one_login_date != null && one_login_date != '') {
                wx.showModal({
                    title: '提示',
                    content: '未登录，请进行登录',
                    showCancel: false,
                    success(res){
                        if(res.confirm == true){
                            wx.switchTab({
                                url: '../mine'
                            })
                        }
                    }
                });
            } else {
                wx.showModal({
                    title: '提示',
                    content: '未登录，请先登录',
                    showCancel: false,
                    success(res){
                        console.log('res==',res);
                        if(res.confirm == true){
                            wx.switchTab({
                                url: '../mine'
                            })
                        }
                    }
                });
            }
            reject("checkSession--失败");
        }
      });
    })
  },
})