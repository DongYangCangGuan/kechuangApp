<template>
    <div>
      <el-container>
        <el-main>
            <div class="header_title">预算设置</div>
            <el-row class="importBtn">
              <el-col :span="6">
              <el-upload
                class="upload-demo"
                ref="upload"
                action="https://jsonplaceholder.typicode.com/posts/"
                :file-list="fileLists"
                :limit=1
                :auto-upload="true"
                :http-request="myUpload"
                :before-upload="beforeUpload">
                <el-button slot="trigger" type="primary" size="small">选择文件</el-button>
                <el-button style="margin-left: 10px;" size="small"
                           v-loading.fullscreen.lock="fullscreenLoading"
                           element-loading-text="正在拼命上传中"
                           element-loading-spinner="el-icon-loading"
                           type="success" @click="submitUpload">导入</el-button>
              </el-upload>
              </el-col>
              <el-col :span="10" :offset="8">
                <span>选择机构</span>
                <el-select style="width: 16rem;" v-model="value" clearable placeholder="请选择" @change="getDptData(value)" @clear="newgetIfo">
                  <el-option
                    v-for="item in options1"
                    :key="item.departId"
                    :label="item.departName"
                    :value="item.departId">
                  </el-option>
                </el-select>
              </el-col>
            </el-row>

        <el-table :data="options" style="width: 100%" :summary-method="getSummaries" show-summary>
          <el-table-column type="index" label="序号" width="200"></el-table-column>
          <el-table-column prop="departName" label="机构名称"></el-table-column>
          <el-table-column prop="createTime" label="日期"></el-table-column>
          <el-table-column prop="budget" label="预算金额"></el-table-column>
          <el-table-column prop="categoryName" label="预算用途"></el-table-column>
        </el-table>

        </el-main>

        <el-footer class="footer">
          <div>
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page = "listQuery.pageIndex"
              :page-sizes="[10, 20, 30, 40]"
              :page-size="listQuery.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="listQuery.total">
            </el-pagination>

          </div>
        </el-footer>
      </el-container>

    </div>
</template>

<script>
    export default {
        name: "BudgeSetting",
      data(){
          return {
            fullscreenLoading: false,
            currentPage:1,
            loading:false,
            fileLists:[],
            serviceFilePath:'',
            options:[],
            options1:[],
            value:'',
            // conbined:false,
            listQuery:{
              //当前页
              pageIndex:1,
              //每页大小
              pageSize:10,
              pageCount:0,//总页数
              // 总条目数
              total: 0
            },
            departId: '',
        }
      },
      created(){
          this.getInfo();
      },

      methods:{
        getDptData(value){
          console.log(value)
          this.departId=value;
         if(value!=null && value!=''){
           //this.conbined=true;
           this.listQuery.pageIndex=1;
           this.listQuery.pageSize=10;
           this.getInfo();
          // this.conbined=true;
         }
        },
        newgetIfo(){
          this.listQuery.pageIndex=1;
          this.listQuery.pageSize=10;
          this.getInfo();
        },
        getSummaries(param){
            const { columns, data } = param;
            const sums = [];
            console.log(sums[3]);
            columns.forEach((column, index) => {
              if (index === 0) {
                sums[index] = '合计';
                return;
              }
              if(index===1 || index===2 || index===4){
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
            let _this = this
            _this.conbined=false;
            this.$nextTick(async () =>{
              let param={
                searchData:{departId:_this.departId},
                page:{
                  pageIndex:_this.listQuery.pageIndex,
                  pageSize:_this.listQuery.pageSize,
                }
              }
              const params = {className:'BudgetSetting', method:'selectAllBudget', param:param};
              let response = await this.$store.dispatch('http/post', params);
              // console.log(response)
              _this.options=response.dataList;
              for(let i=0;i<_this.options.length;i++){
                _this.options[i].budget = parseFloat(_this.options[i].budget).toFixed(2);
              }
              this.listQuery.total=response.page.totalNum
              //获取部门下拉框
              const selectDpt = {className:'BudgetSetting',method:'selectDepart', param:param}
              this.options1 = await this.$store.dispatch('http/post', selectDpt);
            })

          },
        myUpload(content){
          this.$nextTick(async () =>{
            let fd = new FormData();
            fd.append('file',content.file);
            let params = {param:fd,url:'/api/file/fileupload'};
            let response = await this.$store.dispatch('http/fileUpload', params);
            if(response){
              console.log("serviceFilePath:",response);
              this.serviceFilePath = response;
              this.$message({
                message: '读取成功'
              });
            }else{
              this.$message.error('读取失败');
            }
          })
        },
        beforeUpload(file) {
          const isLt1M = file.size / 1024 / 1024 < 2
          if (isLt1M) {
            return true
          }
          this.$message({
            message: '文件大小不超过2M.',
            type: 'warning'
          })
          return false
        },
        submitUpload(){
          if(this.serviceFilePath==''||this.serviceFilePath==null){
            this.$message({message: '请选择上传文件！', type: 'warning'});
            return false;
          }
          this.fullscreenLoading = true;
            this.$nextTick(async () =>{
              let param={
                filePath:this.serviceFilePath
              }
              let params={
                className:'BudgetSetting',
                method:'ImportBudget',
                param: param
              }
              let res = await this.$store.dispatch("http/post", params);
              console.log(res)
              this.fullscreenLoading = false;
              if (res.code=='200') {
                this.$message({message: '导入成功', type: 'success'});
                this.fileLists = [];
                this.getInfo();
                this.serviceFilePath='';
              } else {
                this.$message.error(res.code+':'+res.msg);
              }
            })
        },
        handleSizeChange(val){
          this.listQuery.pageSize = val;
          this.getInfo();
        },
        handleCurrentChange(val){
            this.listQuery.pageIndex = val;
            this.getInfo()
        },
      }
    }
</script>

<style scoped>
  /deep/ .el-container{
    border:1px solid #bbc8c1;
    height: 100%;
     }
  /deep/ .el-row{
    width: 100%;
    height: 45px;
    border-bottom: 1px solid #EBEEF5;
  }
  /deep/ .el-row .el-col{
    line-height: 45px;
    text-align: left;
    font-size: 14px;
    padding-left: 1em;
  }
  /deep/ .el-select{
    width: 100%;
  }
  .num{
    padding-left: 6px;
  }
  .tab_h{
    background: #D3E2DA;
  }
  .header_title{
    height: 30px;
    line-height: 30px;
    padding-left: 1.2em;
  }
  .importBtn{
    padding-left: 1.2em;
    height: 80px;
  }
  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
  }
  /deep/ .el-main{
    padding: 10px;
  }
</style>
