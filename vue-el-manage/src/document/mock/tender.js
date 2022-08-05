export default {
  getNoticeByOrgId: () => {
    return {
      "code": "200",
      "msg": "请求成功",
        "totalPage":"2",
      "totalItem":1,
        list:[
          {"projectId":"PJ000000001",
            "title":null,
            "content":"这是一个测试的提醒事项",
            "remindTime":"2019-05-29 17:02:28",
            "specificTime":"2019-05-22 17:02:35",
            "type":null,
            "remindState":null,
            "lookState":"1",
            "createBy":null,
            "createTime":"2019-05-02 17:03:22",
            "updateBy":null,
            "updateTime":null,
            "deleteFlag":null,
            "receiveUserId":"admin",
            "receiveDepartId":null}
            ],
    }
  },
getWinByOrgId: () => {
  return {
  "code":"200","totalPage":247,"totalItem":1,
    "list":[
    {"tenderRemark":"000000201801016","failCause":"000000201801016","tenderUnit":"bank201801016","contactPerson":"CUST201801016","tenderMoney":172100,"tenderType":"1","tenderAmount":698600,"modifyTime":"2018-01-01","expireTime":"20180101","modifyName":"CUST201801016","tenderDate":"20180101","createTime":"2018-01-01","fundBalance":977100,"projectName":"PN201801016","projectId":"P201801016","createName":"CUST201801016","isTender":"1","isDonation":"1"},
      {"tenderRemark":"0000002018010110","failCause":"0000002018010110","tenderUnit":"bank2018010110","contactPerson":"CUST2018010110","tenderMoney":542600,"tenderType":"1","tenderAmount":651100,"modifyTime":"2018-01-01","expireTime":"20180101","modifyName":"CUST2018010110","tenderDate":"20180101","createTime":"2018-01-01","fundBalance":627800,"projectName":"PN2018010110","projectId":"P2018010110","createName":"CUST2018010110","isTender":"1","isDonation":"0"},
      {"tenderRemark":"0000002018010111","failCause":"0000002018010111","tenderUnit":"bank2018010111","contactPerson":"CUST2018010111","tenderMoney":310200,"tenderType":"0","tenderAmount":292100,"modifyTime":"2018-01-01","expireTime":"20180101","modifyName":"CUST2018010111","tenderDate":"20180101","createTime":"2018-01-01","fundBalance":530100,"projectName":"PN2018010111","projectId":"P2018010111","createName":"CUST2018010111","isTender":"1","isDonation":"1"},
      {"tenderRemark":"0000002018010113","failCause":"0000002018010113","tenderUnit":"bank2018010113","contactPerson":"CUST2018010113","tenderMoney":686600,"tenderType":"1","tenderAmount":671400,"modifyTime":"2018-01-01","expireTime":"20180101","modifyName":"CUST2018010113","tenderDate":"20180101","createTime":"2018-01-01","fundBalance":297200,"projectName":"PN2018010113","projectId":"P2018010113","createName":"CUST2018010113","isTender":"1","isDonation":"0"},
      {"tenderRemark":"0000002018010114","failCause":"0000002018010114","tenderUnit":"bank2018010114","contactPerson":"CUST2018010114","tenderMoney":735200,"tenderType":"0","tenderAmount":473900,"modifyTime":"2018-01-01","expireTime":"20180101","modifyName":"CUST2018010114","tenderDate":"20180101","createTime":"2018-01-01","fundBalance":164100,"projectName":"PN2018010114","projectId":"P2018010114","createName":"CUST2018010114","isTender":"1","isDonation":"0"}]

  }
},
getAnnounceByOrgId: () =>
{
  return {
    "code": "200", "totalPage": 481,"totalItem":1,
    "list": [
      {
        "nodePath": "JD201801011",
        "type": "1",
        "title": "BT201801011",
        "templateTitle": "MBBT201801011",
        "contents": null,
        "isTemplate": "0",
        "isUse": "1",
        "isSend": "1",
        "sendTime": "2018-01-01",
        "createName": "CUST201801011",
        "createTime": "2018-01-01",
        "modifyName": "CUST201801011",
        "modifyTime": "2018-01-01",
        "isView": "1"
      },
      {
        "nodePath": "JD201801012",
        "type": "1",
        "title": "BT201801012",
        "templateTitle": "MBBT201801012",
        "contents": null,
        "isTemplate": "0",
        "isUse": "0",
        "isSend": "0",
        "sendTime": "2018-01-01",
        "createName": "CUST201801012",
        "createTime": "2018-01-01",
        "modifyName": "CUST201801012",
        "modifyTime": "2018-01-01",
        "isView": "1"
      },
      {
        "nodePath": "JD2018010111",
        "type": "1",
        "title": "BT2018010111",
        "templateTitle": "MBBT2018010111",
        "contents": null,
        "isTemplate": "0",
        "isUse": "0",
        "isSend": "0",
        "sendTime": "2018-01-01",
        "createName": "CUST2018010111",
        "createTime": "2018-01-01",
        "modifyName": "CUST2018010111",
        "modifyTime": "2018-01-01",
        "isView": "1"
      }]
   }
},
getReadyByOrgId: () => {
  return {
  "code":"200","totalItem":1,"totalPage":1,"list":
      [{"pkId":"2","processId":"001","processLv":0,"processUserId":"001","processUserName":"admin","processOrgId":null,
        "processOrgName":null,"processStates":"0","processBelong":"0","processInvalid":null,
        "processTs":"2019-06-03 16:14:21","remarks":"测试数据002","projectId":"P20180101112","projectName":"PN20180101112",
        "processType":"项目报备","workitemId":null,"instId":null,"readyId":null,"createBy":"001","createName":"admin",
        "createTs":"2019-06-03 16:16:01","createOrgId":null,"createOrgName":null,"updateBy":null,"updateName":null,
        "updateTs":null,"deleteFlag":"0","isTemporary":null,"custId":"C20180101112","custName":"N20180101112",
        "custType":"0","openTime":"20180101","page":null,"tenderAmount":"890700","projectType":"T20180101112"}]
  }
},
getAlreadyByOrgId: () => {
  return {
  "code":"200","totalItem":1,"totalPage":1,"list":
      [{"pkId":"1","processId":"001","processLv":0,"processUserId":"001","processUserName":"admin","processOrgId":null,
        "processOrgName":null,"processStates":"14","processBelong":"0","processInvalid":null,
        "processTs":"2019-06-03 16:14:21","remarks":"测试数据001","projectId":"P201801011","projectName":"PN201801011",
        "processType":"项目报备","workitemId":null,"instId":null,"readyId":null,"createBy":"001","createName":"admin",
        "createTs":"2019-06-03 16:16:01","createOrgId":null,"createOrgName":null,"updateBy":null,"updateName":null,
        "updateTs":null,"deleteFlag":"0","isTemporary":null,"custId":"C201801011","custName":"N201801011","custType":"0",
        "openTime":"20180101","page":null,"tenderAmount":"162600","projectType":"T201801011"}]
  }
},

}
