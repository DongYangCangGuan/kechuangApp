<template >
  <div>
    <el-container>
      <el-header class="header">
        <el-row class="headerBtn" >

          <el-select v-model="depId" clearable  placeholder="机构名称" size="mini">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>

            <el-date-picker v-model="startTime"
                            align="right"
                            type="date"
                            placeholder="选择开始时间"
                            size="mini"
                            value-format="yyyy-MM-dd"
                            clearable  >
            </el-date-picker>

          <el-date-picker
            class="datepic"
            v-model="endTime"
            type="date"
            placeholder="选择结束时间"
            size="mini"
            value-format="yyyy-MM-dd"
            clearable >
          </el-date-picker>

          <el-button type="primary"  size="mini" @click="selectAll">检索</el-button>
          <el-button type="primary" v-if="generateStatemtns"  size="mini"  @click="dialogVisible = true">生成对账单</el-button>
          <el-button type="primary"  size="mini" @click="exportData">导出</el-button>
        </el-row>

      </el-header>
      <el-main>
        <el-row class="aaa">
          <template >
            <el-radio-group v-model="radio" @change="selectChange">
              <el-radio :label="1">显示所有</el-radio>
              <el-radio :label="2">未生成</el-radio>
              <el-radio :label="3">已生成</el-radio>
              <el-radio :label="4">未发送</el-radio>
              <el-radio :label="5">已发送</el-radio>
              <el-radio :label="6">已确认</el-radio>
            </el-radio-group>
          </template>
        </el-row>
        <el-table v-if="showOrder" ref="multipleTable" :row-key="getRowKeys" :summary-method="getSummaries" show-summary :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" reserve-selection :selectable="seletInit" width="55" label=""></el-table-column>
          <el-table-column prop="createTime" label="入库时间"></el-table-column>
          <el-table-column prop="name" label="机构名称"></el-table-column>
          <el-table-column prop="code" label="入库单号"></el-table-column>
          <el-table-column prop="orderId" label="订单编号"></el-table-column>
          <el-table-column prop="money" label="总计(元)"></el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
              <span>{{scope.row.reportStatus=='0'?'未生成对账单':'已生成对账单'}}</span>
            </template>
          </el-table-column>
        </el-table>

        <el-table v-if="showReport" ref="multipleTable" :data="tableReportData" :summary-method="getSummaries" show-summary style="width: 100%">
          <el-table-column type="selection" width="55" label=""></el-table-column>
          <el-table-column prop="reportTime" label="对账单时间"></el-table-column>
          <el-table-column prop="departName" label="机构名称"></el-table-column>
          <el-table-column prop="supplierName" label="供应商名称"></el-table-column>
          <el-table-column prop="code" label="对账单编号"></el-table-column>
          <el-table-column prop="reallyMoney" label="总计(元)"></el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
              <span>{{scope.row.supplierStatus=='0'?'未发送':(scope.row.supplierStatus=='1'?'已发送':'已确认')}}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" v-if="sendOrCanel">
            <template slot-scope="scope">
              <el-button size="mini" @click="SendReport(scope.row.code)">发送</el-button>
              <el-button size="mini" type="danger" @click="CancelReport(scope.row.code)">取消</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer class="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = "listQuery.pageIndex"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="listQuery.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="listQuery.total">
        </el-pagination>
      </el-footer>
    </el-container>

    <el-dialog title="请选择生成对账单月份:"
               @close="close_dia"
      :visible.sync="dialogVisible"
      width="30%">
      <div class="block">
        <span class="demonstration">月</span>
        <el-date-picker
          v-model="month"
          type="month"
          value-format="yyyy-MM"
          placeholder="选择月">
        </el-date-picker>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="GenerateStatementSOrder">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>
<script>

  export default {
    mounted(){
      this.getInfo();
      this.departInfo();
    },
    data(){
      return{
        reportStatus:-1,
        dialogVisible: false,
        month:'',
        receiptList:[],
        ReceiptNos:[],
        orderIds:[],
        radio:1,
        value:'',
        depId:'',
        startTime:'',
        endTime:'',
        gName:'',
        input:'',
        num:100,
        currentPage:1,
        value1:'',
        value2: '',
        tableData:[],
        tableReportData:[],
        options:[],
        listQuery:{
          //当前页
          pageIndex:1,
          //每页大小
          pageSize:10,
          pageCount:0,//总页数
          // 总条目数
          total: 0,
        },

        showOrder:true,
        showReport:false,
        supplierStatus:0,

        sendOrCanel:true,
        generateStatemtns:true,
      }
    },
    methods: {
      getRowKeys(row){
        return row.code;
      },
      seletInit(row,index){
          if(row.reportStatus=='0'){
            return true;
          }else {
            return false;
          }
      },
      handleSizeChange(val) {
        this.listQuery.pageSize = val;
        this.getInfo();
      },
      handleCurrentChange(val) {
        this.listQuery.pageIndex = val;
        this.getInfo()
      },

      selectAll(){
        this.radio=1;
        this.reportStatus=-1;
        this.departId='';
        this.showOrder=true;
        this.showReport=false;
        this.listQuery.pageIndex=1;
        console.log(this.startTime+"----"+this.endTime)
        this.getInfo();
      },
      selectChange(){
        this.listQuery.pageIndex=1;
        if(this.radio==2){
          this.generateStatemtns=true;
          this.showOrder=true;
          this.showReport=false;
            this.reportStatus=0;
            this.getInfo();
        }else if(this.radio==1){
          this.generateStatemtns=true;
          this.showOrder=true;
          this.showReport=false;
          this.reportStatus=-1;
          this.getInfo();
        }else if(this.radio==3){
          this.generateStatemtns=false;
          this.showOrder=true;
          this.showReport=false;
          this.reportStatus=1;
          this.getInfo();
        }else if(this.radio==4){
          this.generateStatemtns=false;
          this.sendOrCanel=true;
          this.showOrder=false;
            this.showReport=true;
            this.supplierStatus=0;
            this.getReportData();
        }else if(this.radio==5){
          this.generateStatemtns=false;
          this.sendOrCanel=false;
          this.showOrder=false;
          this.showReport=true;
          this.supplierStatus=1;
          this.getReportData();
        }else if(this.radio==6){
          this.generateStatemtns=false;
          this.showOrder=false;
          this.showReport=true;
          this.supplierStatus=2;
          this.getReportData();
        }
      },
      close_dia(){
        this.month='';
      },
      exportData(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={
              departId:_this.depId,
              endTime:_this.endTime,
              startTime:_this.startTime,
              reportStatus:_this.reportStatus
          }
          let params={
            className:'Statistical',
            method:'exportInOrderData',
            param:param,
          };
          let response = await _this.$store.dispatch('http/post', params);
          console.info(response);
         this.dataList(response);
        })
      },
      //导出
      dataList(data){
        let tempArr = [];
        for(var o in data){
          var obj = {
            createTime:data[o].createTime,
            departName:data[o].name,
            code:data[o].code,
            orderId:data[o].orderId,
            money:parseFloat(data[o].money).toFixed(2),
          }
          tempArr.push(obj)
        }
        console.log("开始导出")
        //列标题
        let str = '<tr><td>入库时间</td><td>机构名称</td><td>入库单号</td><td>订单编号</td><td>总计(元)</td></tr>';
        for(let i=0;i<tempArr.length;i++){
          str+='<tr>';
          for (let item in tempArr[i]) {
            str+=`<td>${tempArr[i][item] + '\t'}</td>`;
          }
          str +='</tr>';
        }
        //Worksheet名
        let worksheet = 'Sheet1'
        let uri = 'data:application/vnd.ms-excel;base64,';

        //下载的表格模板数据
        let template = `<html xmlns:o="urn:schemas-microsoft-com:office:office"
      xmlns:x="urn:schemas-microsoft-com:office:excel"
      xmlns="http://www.w3.org/TR/REC-html40">
      <head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>
        <x:Name>${worksheet}</x:Name>
        <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>
        </x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->
        </head><body><table>${str}</table></body></html>`;
        //下载模板
        window.location.href = uri + this.getbase64(template)
        //列标题，逗号隔开，每一个逗号就是隔开一个单元格  ，入库单号，订单编号，总计
        // let str1 = `入库时间,机构名称,入库单号,订单编号,总计\n`;
        // //增加\t为了不让表格显示科学计数法或者其他格式
        // let tempArr = [];
        // for(var o in data){
        //   var obj = {
        //     createTime:data[o].createTime,
        //     departName:data[o].name,
        //     code:data[o].code,
        //     orderId:data[o].orderId,
        //     money:parseFloat(data[o].money).toFixed(2),
        //   }
        //   tempArr.push(obj)
        // }
        // for(let i=0;i< tempArr.length;i++){
        //   for(let item in tempArr[i]){
        //     str1+=`${tempArr[i][item] + '\t'},`;
        //   }
        //   str1+='\n';
        // }
        // //encodeURIComponent解决中文乱码
        // let uri = 'data:text/csv;charset=UTF-8,\ufeff' + encodeURIComponent(str1);
        // console.log(uri)
        // //通过创建a标签实现
        // let link = document.createElement("a");
        // link.href = uri;
        // //对下载的文件命名
        // link.download =  "入库订单.csv";
        // document.body.appendChild(link);
        // link.click();
        // document.body.removeChild(link);
      },
      getbase64(s){
        return window.btoa(unescape(encodeURIComponent(s)))
      },
      getSummaries(param){
        const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = parseFloat(sums[index]).toFixed(2);
          }
        });
        return sums;
      },
      getInfo(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={
            searchData:{
              departId:_this.depId,
              endTime:_this.endTime,
              startTime:_this.startTime,
              reportStatus:_this.reportStatus
            },
            page:{
              pageIndex:_this.listQuery.pageIndex,
              pageSize:_this.listQuery.pageSize,
            }
          }
          let params={
            className:'Statistical',
            method:'selectInWareOrder',
            param:param,
          };
          let response = await _this.$store.dispatch('http/post', params);
          console.info(response);
          _this.tableData=response.dataList;
          for(let i=0;i<_this.tableData.length;i++){
           if(_this.tableData[i].money != null && _this.tableData[i].money!=''){
             _this.tableData[i].money = parseFloat(_this.tableData[i].money).toFixed(2);
           }
          }
          this.listQuery.total=response.page.totalNum
        })
      },
      getReportData(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={
            searchData:{
              departId:_this.depId,
              endTime:_this.endTime,
              startTime:_this.startTime,
              supplierStatus:_this.supplierStatus
            },
            page:{
              pageIndex:_this.listQuery.pageIndex,
              pageSize:_this.listQuery.pageSize,
            }
          }
          let params={
            className:'Statistical',
            method:'selectReport',
            param:param,
          };
          let response = await _this.$store.dispatch('http/post', params);
          console.info(response);
          _this.tableReportData=response.dataList;
          for(let i=0;i<_this.tableData.length;i++){
            if(_this.tableData[i].reallyMoney != null && _this.tableData[i].reallyMoney!=''){
              _this.tableData[i].reallyMoney = parseFloat(_this.tableData[i].reallyMoney).toFixed(2);
            }
          }
          this.listQuery.total=response.page.totalNum
        })
      },
      SendReport(code){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={
            code:code
          }
          let params={
            className:'Statistical',
            method:'sendReport',
            param:param,
          };
          let response = await _this.$store.dispatch('http/post', params);
          if (response === "200") {
            this.$message({  showClose: true,  message: '成功',  type: 'success' });
            //this.loading = false;
          }else{
            this.$message({  showClose: true,  message: 'error',  type: 'error' });
            //this.loading = false;
          }
          this.radio=4;
          this.selectChange();
        })
      },
      CancelReport(code){
        console.log(code)
        let _this = this;
        this.$nextTick(async ()=>{
          let param ={
            code:code
          };
          let params={
            className:'Statistical',
            method:'updateStorageAndReport',
            param:param
          }
          let response = await _this.$store.dispatch('http/post', params);
          this.getReportData();
          if (response === "200") {
            this.$message({  showClose: true,  message: '成功',  type: 'success' });
          }else{
            this.$message({  showClose: true,  message: 'error',  type: 'error' });
          }
        })
      },
      //生成对账单
      GenerateStatementSOrder(){
        this.ReceiptNos=[];
        this.orderIds=[];
        for (let i=0;i<this.receiptList.length;i++){
          this.ReceiptNos.push(this.receiptList[i].code)
          this.orderIds.push(this.receiptList[i].orderId)
        }
        console.log(this.month+"---"+this.ReceiptNos)
        let _this=this;
        if(this.ReceiptNos.length==0){
          this.$message({  showClose: true,  message: '请选择至少选择一个订单！',  type: 'error' });
          this.dialogVisible=false;
          return false;
        }
        if(this.month==null || this.month==''){
          this.$message({  showClose: true,  message: '请选择生成对账单月份！',  type: 'error' });
          return false;
        }
        this.$nextTick(async ()=>{
          let param={
            month:_this.month,
            receiptNos:_this.ReceiptNos,
            orderIds:_this.orderIds
          };
          let params={
            className:'Statistical',
            method:'GenerateStatementSOrder',
            param:param
          }
          let response = await _this.$store.dispatch('http/post', params);
          if (response === "200") {
            this.$message({  showClose: true,  message: '成功',  type: 'success' });
            this.month='';
            this.getInfo();
          }else{
            this.$message({  showClose: true,  message: 'error',  type: 'error' });
          }
        })
        this.receiptList=[];
        this.dialogVisible=false;
        this.$refs.multipleTable.clearSelection();
      },
      //取消对账单
      CancelStatementSOrder(){},
      //选择
      handleSelectionChange(val){
        this.receiptList=val;
        console.log(this.receiptList)
      },
      departInfo(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={};
          let params={
            className:'Statistical',
            method:'selectInstitutions',
            param:param,
          };
          let response1=await _this.$store.dispatch('http/post', params);
          console.info(response1);
          _this.options=response1;
        })
      },
    }

  }
</script>
<style scope>
  .header {
    background-color: rgb(240, 244, 245);
    text-align: left;
  }
  .btnCss {
    margin-left: 10px;
  }

  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
  }
  .black{
    color: black;
  }
  .aaa {
    float: right;
    margin-bottom: 8px;
  }
  .block{
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  /deep/.datepic{
    width: 150px;
  }
</style>
