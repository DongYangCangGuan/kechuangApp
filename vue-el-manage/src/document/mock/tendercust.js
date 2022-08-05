export default {
  getNoticeByOrgId: () => {
    return {
      "code": "200",
      "msg": "请求成功",
      "data": {
        "list": [{
          PK_ID: "01",
          PROJ_ID: "projid",
          CONTENT: "银行已乔迁，注意查看新地址",
          TITLE: '宁波银行上海长宁支行乔迁新址',
          LOOK_STATE: 0,
          TYPE: 0,
          RECEIVE_USER_ID: "李达康",
          RECEIVE_DEPART_ID: "总部"

        }, {
          PK_ID: "02",
          PROJ_ID: "projid",
          CONTENT: "银行已乔迁，注意查看新地址",
          TITLE: '外汇交易金管家“2019送祝福”活动获奖客户名单',
          LOOK_STATE: 0,
          TYPE: 1,
          RECEIVE_USER_ID: "李达康",
          RECEIVE_DEPART_ID: "总部"
        }, {
          PK_ID: "03",
          PROJ_ID: "projid",
          CONTENT: "银行已乔迁，注意查看新地址",
          TITLE: '宁波银行一季度不良率0.78% 资产质量保持领先优势',
          LOOK_STATE: 0,
          TYPE: 2,
          RECEIVE_USER_ID: "李达康",
          RECEIVE_DEPART_ID: "总部"
        }, {
          PK_ID: "04",
          PROJ_ID: "projid",
          CONTENT: "银行已乔迁，注意查看新地址",
          TITLE: '04宁波银行一季度不良率0.78% 资产质量保持领先优势',
          LOOK_STATE: 0,
          TYPE: 2,
          RECEIVE_USER_ID: "李达康",
          RECEIVE_DEPART_ID: "总部"
        }],
        "totalPage": "2"

      }
    }
  },
  getCustNamesArr: () => {
    return {
      "code": "200",
      "msg": "请求成功",
      "data": {
        custNames: [
          {"PK_ID": "客户号01", "CUST_NM": "客户名称1"}, {"PK_ID": "客户号02", "CUST_NM": "客户名称2"},
          {"PK_ID": "客户号03", "CUST_NM": "客户名称3"}, {"PK_ID": "客户号04", "CUST_NM": "客户名称4"},
          {"PK_ID": "客户号05", "CUST_NM": "客户名称5"}, {"PK_ID": "客户号06", "CUST_NM": "客户名称6"}],
        custTypes: [{CUST_TYPE: "0"}, {CUST_TYPE: '1'}],
        custSyses: [{CUST_SYS_TYPE: "财政类"}, {CUST_SYS_TYPE: "社保基金"}, {CUST_SYS_TYPE: "公积金"}, {CUST_SYS_TYPE: "各局办专户"},
          {CUST_SYS_TYPE: "事业单位"}, {CUST_SYS_TYPE: "政府融资平台"}, {CUST_SYS_TYPE: "国企央企"}, {CUST_SYS_TYPE: "其他"}],
        custAdresses: [
          {
            value: "zjs", label: "浙江省",
            children: [
              {
                value: "nbs", label: "宁波市",
                children: [
                  {value: "pzq", label: "鄞州区"},
                  {value: "jbq", label: "江北区"}]
              },
              {
                value: "hzs", label: "杭州市",
                children: [
                  {value: "xsq", label: "萧山区"},
                  {value: "xhq", label: "西湖区"}]
              }]
          },
          {
            value: "jss", label: "江苏省",
            children: [
              {value: "njs", label: "南京市", children: [{value: "xwq", label: "玄武区"}, {value: "bxq", label: "白下区"}]},
              {value: "wxs", label: "无锡市", children: [{value: "hsq", label: "惠山区"}, {value: "ncq", label: "南长区"}]}]
          }
        ],
        custLabels:  [{"labelNm":"财政类","labelValue":"2241"},
          {"labelNm":"事业单位","labelValue":"2245"},
          {"labelNm":"各局办专户","labelValue":"2244"},
          {"labelNm":"其他","labelValue":"2243"},
          {"labelNm":"4758","labelValue":"2246"}]


      }
    }
  },
  submitSearchContent: () => {
    return {
      "code": "200",
      "msg": "请求成功",
      "data":{total:7,list:
          [
            {"PK_ID":"客户号01","CUST_NM":"客户名称1","CUST_TYPE":"存量客户","REMARK":"备注信息","DEPART_ID":"所属机构01","CUST_SYS_TYPE":"所属系统02"},
            {"PK_ID":"客户号02","CUST_NM":"客户名称2","CUST_TYPE":"新增客户","REMARK":"备注信息","DEPART_ID":"所属机构01","CUST_SYS_TYPE":"所属系统02"},
            {"PK_ID":"客户号03","CUST_NM":"客户名称3","CUST_TYPE":"存量客户","REMARK":"备注信息","DEPART_ID":"所属机构01","CUST_SYS_TYPE":"所属系统02"},
            {"PK_ID":"客户号04","CUST_NM":"客户名称4","CUST_TYPE":"新增客户","REMARK":"备注信息","DEPART_ID":"所属机构01","CUST_SYS_TYPE":"所属系统02"},
            {"PK_ID":"客户号05","CUST_NM":"客户名称5","CUST_TYPE":"新增客户","REMARK":"备注信息","DEPART_ID":"所属机构01","CUST_SYS_TYPE":"所属系统02"},
            {"PK_ID":"客户号06","CUST_NM":"客户名称6","CUST_TYPE":"存量客户","REMARK":"备注信息","DEPART_ID":"所属机构01","CUST_SYS_TYPE":"所属系统02"},
            {"PK_ID":"客户号07","CUST_NM":"客户名称7","CUST_TYPE":"存量客户","REMARK":"备注信息","DEPART_ID":"所属机构01","CUST_SYS_TYPE":"所属系统02"}]}
    }
  },
  getPreselectedCustNamesArr: () => {
    return {
      "code": "200",
      "msg": "请求成功",
      "data": {
        custNames: [],
        custCodeTypes:[{CUST_CODE_TYPE:"0"},{CUST_CODE_TYPE:"1"},{CUST_CODE_TYPE:"2"},{CUST_CODE_TYPE:"3"},{CUST_CODE_TYPE:"4"}],
        custTypes: [{CUST_TYPE: "1"}, {CUST_TYPE: "0"}],
        custSyses: [{CUST_SYS_TYPE: "财政类"}, {CUST_SYS_TYPE: "社保基金"}, {CUST_SYS_TYPE: "公积金"}, {CUST_SYS_TYPE: "各局办专户"}, {CUST_SYS_TYPE: "事业单位"}, {CUST_SYS_TYPE: "政府融资平台"}, {CUST_SYS_TYPE: "国企央企"}, {CUST_SYS_TYPE: "其他"}],
        custOpenOrgs: [{openOrg:"鄞州支行"},{openOrg:"江北支行"},{openOrg:"萧山支行"},{openOrg:"西湖支行"},{openOrg:"玄武支行"},
          {openOrg:"白下支行"},{openOrg:"惠山支行"},{openOrg:"南长支行"}],
        custAdresses: [
          {
            value: "浙江省", label: "浙江省",
            children: [
              {
                value: "宁波市", label: "宁波市",
                children: [
                  {value: "鄞州区", label: "鄞州区"},
                  {value: "江北区", label: "江北区"}]
              },
              {
                value: "杭州市", label: "杭州市",
                children: [
                  {value: "萧山区", label: "萧山区"},
                  {value: "西湖区", label: "西湖区"}]
              }]
          },
          {
            value: "江苏省", label: "江苏省",
            children: [
              {value: "南京市", label: "南京市", children: [{value: "玄武区", label: "玄武区"}, {value: "白下区", label: "白下区"}]},
              {value: "无锡市", label: "无锡市", children: [{value: "惠山区", label: "惠山区"}, {value: "南长区", label: "南长区"}]}]
          }
        ],
        custLabels: [{"labelNm":"财政类","labelValue":"2241"},
          {"labelNm":"事业单位","labelValue":"2245"},
          {"labelNm":"各局办专户","labelValue":"2244"},
          {"labelNm":"其他","labelValue":"2243"},
          {"labelNm":"4758","labelValue":"2246"}]
      }
    }
  },
  sumbitNewCustInfoPost: () => {
    return {
      "code": "200",
      "msg": "提交成功",
      "data": {}
    }
  },
  getTablemanageInfo: () => {
    return {
      "code": "200",
      "msg": "提交成功",
      "data": [
        {"tableId":"CREDIT_CARD_ADDR_TYPE","tableName":"信用卡客户地址类型"},
        {"tableId":"CREDIT_CARD_ADDR_TYPE1","tableName":"信用卡卡状态"},
        {"tableId":"CREDIT_CARD_ADDR_TYPE2","tableName":"信用卡城市配置"},
        {"tableId":"CREDIT_CARD_ADDR_TYPE3","tableName":"信用卡省份配置"},
        {"tableId":"CREDIT_CARD_ADDR_TYPE4","tableName":"ESB短信地址"},
        {"tableId":"FMAP_ORG_CHA_PLAN_STATUS","tableName":"机构计划状态"}]
    }
  },
  getTabledetailInfo: () => {
    return {
      "code": "200",
      "msg": "提交成功",
      "data": [
        {"id":"0304","name":"住宅地址1","order":1},{"id":"0310","name":"单位地址5","order":2},
        {"id":"0305","name":"单位地址","order":3},{"id":"0303","name":"单位地址4","order":4},
        {"id":"0306","name":"住宅地址3","order":5},{"id":"0302","name":"单位地址3","order":6},
        {"id":"0307","name":"单位地址","order":7},{"id":"0301","name":"单位地址1","order":8},
        {"id":"0308","name":"住宅地址4","order":9},{"id":"0309","name":"单位地址7","order":10}]
    }
  },
  addLeftTableInfo: () => {
    return {
      "code": "200",
      "msg": "提交成功",
      "data":{}
    }
  },
  editLeftTableInfo: () => {
    return {
      "code": "200",
      "msg": "修改成功",
      "data":{}
    }
  },

  getProjectInfoArr: () => {
    return {
      "code": "200",
      "msg": "请求成功",
      "data": {//项目类型 0：新增 1：存量
        projectTypes: [{projectType: "1"}, {projectType: "0"}],
        //信息来源 0：客户提供 1：外部信息获取
        fromTypes: [{fromType: "0"}, {fromType: "1"}],
        tenderTypes: [{tenderType: "定期存款"}, {tenderType: "入围项目"}, {tenderType: "陪标项目"}],
        //初步沟通结果 0倾向我行 1倾向他行 2无倾向
        isCooperations: [{isCooperation: "0"}, {isCooperation: "1"}, {isCooperation: "2"}],
        // 是否参与标书 1是 0不是
        isParticipations: [{isParticipation: "0"}, {isParticipation: "1"}],
        // 是否需要总行协同走访   1是 0不是
        assistTypes: [{assistType: "0"}, {assistType: "1"}],
        //协同走访领导层级
        assistCodes:[{assistCode:"分行公司银行部行长室"},{assistCode:"总行公司银行部行长室"},
          {assistCode:"总公司银行部总经理室"},{assistCode:"分公司银行部总经理室"}],
        //对接关键人 0否 1是
        keyPersonTypes:[{keyPersonType:"0"},{keyPersonType:"1"}]
      }

    }
  },
}
