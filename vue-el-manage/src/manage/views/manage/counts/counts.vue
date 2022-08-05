<template>
  <div>
      <el-row>
      <el-col :span="24" class="toolbar" style="padding-top: 10px;padding-left: 10px">
        <el-form :inline="true">
          <el-form-item>
            <el-select v-model="departId" clearable placeholder="机构名称" @change="search">
            <el-option v-for="item in departmentOptions"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id">
            </el-option>
          </el-select>
          <el-button type="primary" @click="Export">{{name}}导出</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      </el-row>
      <el-col :span="24" class="toolbar" style="padding-bottom:0px;">
      <el-tabs type="border-card" v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="平台访问统计" name="first">
      <!--平台访问统计-->
       <el-table :data="Platform" border stripe highlight-current-row v-loading="listLoading" style="width: 100%;">
        <el-table-column  prop="username" label="访问人" width="300" ></el-table-column>
        <el-table-column  prop="departmentname" label="部门名称" width="300" ></el-table-column>
        <el-table-column  prop="moduleid" label="访问类型" width="300" ></el-table-column>
        <el-table-column  prop="counts" label="最近访问数" width="200" ></el-table-column>
        <el-table-column  prop="maxTime" label="最近访问时间"></el-table-column>
      </el-table>
      </el-tab-pane>
      <!--交易锦囊-->
      <el-tab-pane label="交易锦囊" name="second">
        <el-table :data="Question" border stripe highlight-current-row v-loading="listLoading" style="width: 100%;">
          <el-table-column  prop="username" label="姓名" width="300" ></el-table-column>
          <el-table-column  prop="departmentname" label="部门名称" width="300" ></el-table-column>
          <el-table-column  prop="filename" label="文件名称" width="300" ></el-table-column>
          <el-table-column  prop="counts" label="交易锦囊(访问数)" width="200"></el-table-column>
          <el-table-column  prop="maxTime" label="交易锦囊(最近访问时间)"></el-table-column>
        </el-table>
      </el-tab-pane>
        <!--智能答疑-->
      <el-tab-pane label="智能答疑" name="third">
        <el-table :data="Trading" border stripe highlight-current-row v-loading="listLoading" style="width: 100%;">
          <el-table-column  prop="username" label="姓名" width="300" ></el-table-column>
          <el-table-column  prop="departmentname" label="部门名称" width="300" ></el-table-column>
          <el-table-column  prop="filename" label="文件名称" width="300" ></el-table-column>
          <el-table-column  prop="counts" label="智能答疑(访问数)" width="200"></el-table-column>
          <el-table-column  prop="maxTime" label="智能答疑(最近访问时间)"></el-table-column>
        </el-table>
      </el-tab-pane>
        <!--工具条-->
      <el-footer class="footer"> 
        <el-pagination background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = currentPage
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
          :total='this.listQuery.totalNum' style="float:right;">
        </el-pagination>
      </el-footer>
      </el-tabs>
    </el-col> 
  </div>
</template>
<script>
  export default{
   components: {},
      data() {
       return {
        departmentOptions:[],
        listLoading:true,
        activeName:'first',
        name:'平台访问统计',
        departId:[],
        Platform: [],
        Question:[],
        Trading:[],
        currentPage:1,
        listQuery:{
          pageIndex:1,
          pageSize:10,
          totalNum:0,
         },
       }
     },
      methods: {
        page(){
          this.listQuery={pageIndex:1,pageSize:10,totalNum:0,}
        },
        handleClick(tab, event){
          this.activeName=tab.name;
          switch(tab.name){
            case "first":
              this.listLoading = true;
              this.page();
              this.getAccessStatistics(tab.name);//全平台统计
              this.name="全平台统计"
            break;
            case "second":
              this.listLoading = true;
              this.page();
              this.getAccessStatistics(tab.name);//交易锦囊
              this.name="交易锦囊"
            break;
            case "third":
              this.listLoading = true;
              this.page();
              this.getAccessStatistics(tab.name);//智能答疑
              this.name="智能答疑"
            break;
            default:
            break;
          }
        },
        getAccessStatistics(name){
         console.log(name);
         let param={
            page:{
              pageIndex:this.listQuery.pageIndex,
              pageSize:this.listQuery.pageSize,
            },
            searchData:{
              departId:this.departId
            },
            code:name
          };
          let params = {
            className:'Access',
            method:'getAccessStatistics',
            param:param
          };
          this.$nextTick(async () => {await this.$store.dispatch('http/post', params)
          .then((res) => {
              console.info(res);
              if(res.code="second"){
               this.Question=res.dataList;
              }
              if(res.code="third"){
                this.Trading=res.dataList;
              }
              if(res.code="first"){
                this.Platform=res.dataList;
              }
              this.listLoading = false;
              this.listQuery.totalNum=res.page.totalNum;
            });
          });
        },
        handleSizeChange(val){
          this.listQuery.pageSize= val;
          this.getAccessStatistics(this.activeName);
        },
        handleCurrentChange(val){
          console.log(val);
          this.listQuery.pageIndex=val;
          this.getAccessStatistics(this.activeName)
        },
        Export(){
          switch(this.activeName){
            case "first":
              this.getExportExcel(this.activeName);//全平台统计
            break;
            case "second":
              this.getExportExcel(this.activeName);//交易锦囊
            break;
            case "third":
              this.getExportExcel(this.activeName);//智能答疑
            break;
            default:
            break;
          }
        },
        getExportExcel(activeName){
          let param={
            activeName:activeName,
            departId:this.departId
          };
          let params = {
            className:'Access',
            method:'getExportExcel',
            param:param
          };
          this.$nextTick(async () => {await this.$store.dispatch('http/post', params)
          .then((res) => {            
            switch(res.code){
            case "first":
              let tempArr = [];
              let data=res.VisitorHistory;
              for(let i in data){
               let obj ={
                username:data[i].username,
                departmentname:data[i].departmentname,
                moduleid:data[i].moduleid,
                counts:data[i].counts,
                maxTime:data[i].maxTime}
                tempArr.push(obj)
              }
              let str = '<tr><td>姓名</td><td>部门名称</td><td>访问类型</td><td>最近访问数</td><td>最近访问时间</td></tr>';
              this.dataList(tempArr,str);
            break;
            case "second":
              let tempArr1 = [];
              let data1=res.VisitorHistory;
              for(let i in data1){
               let obj ={
                username:data1[i].username,
                departmentname:data1[i].departmentname,
                filename:data1[i].filename,
                counts:data1[i].counts,
                maxTime:data1[i].maxTime}
                tempArr1.push(obj)
              }
              let str1 = '<tr><td>姓名</td><td>部门名称</td><td>文件名称</td><td>交易锦囊(访问数)</td><td>交易锦囊(最近访问时间)</td></tr>';
              this.dataList(tempArr1,str1);
            break;
            case "third":
              let tempArr2 = [];
              let data2=res.VisitorHistory;
              for(let i in data2){
               let obj ={
                username:data2[i].username,
                departmentname:data2[i].departmentname,
                filename:data2[i].filename,
                counts:data2[i].counts,
                maxTime:data2[i].maxTime}
                tempArr2.push(obj)
              }
              let str2 = '<tr><td>姓名</td><td>部门名称</td><td>文件名称</td><td>智能答疑(访问数)</td><td>智能答疑(最近访问时间)</td></tr>';
              this.dataList(tempArr2,str2);
            break;
            default:
            break;
            }
           });
         });
        },
        getdepartmentList(){
          let param={};
          let params = {
            className:'Access',
            method:'getdepartmentList',
            param:param
          };
          this.$nextTick(async () => {await this.$store.dispatch('http/post', params)
          .then((res) => {            
             this.departmentOptions=res;
           });
          });
        },
        //导出
        dataList(tempArr,str){
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
        <head><meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8"><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>
        <x:Name>${worksheet}</x:Name>
        <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>
        </x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->
        </head><body><table>${str}</table></body></html>`;
        //下载模板
        var link = document.createElement("a");
        link.download = this.name + ".xls";
        link.href = uri + this.getbase64(template);
        link.click();
       },
       getbase64(s){
        return window.btoa(unescape(encodeURIComponent(s)))
       },
       search(value){
        this.listLoading = true;
        this.page();
        this.departId=value;
        this.getAccessStatistics(this.activeName);
       },
     },
     mounted:function(){
      let self = this;
      this.$nextTick(async function () {
       self.getAccessStatistics(this.activeName);
       self.getdepartmentList();
      }.bind(this));
    }
  }
</script>

<style scoped>
.header{
  background-color: rgb(240, 244, 245);
  text-align: left;
  margin-bottom: 10px;
}
.headerbtn{
  /*margin-top: 15px;*/
  text-align: right;
  line-height: 5;
}
</style>