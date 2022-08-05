<template>
  <div>
    <el-container>
      <div class="header">
          <el-upload
            class="upload-demo"
            ref="upload"
            action="https://jsonplaceholder.typicode.com/posts/"
            :file-list="fileLists"
            :limit=1
            :auto-upload="true"
            :http-request="myUpload"
            :before-upload="beforeUpload">
            <el-button slot="trigger" type="primary" size="mini">选择文件</el-button>
            <el-button style="margin-left: 10px;" size="mini" type="success" @click="submitUpload" :loading="bodyLoading">导入</el-button>
              <div slot="tip" class="el-upload__tip">只能上传.xls/.xlsx/.doc/.docx/.pdf文件，且不超过2m</div>
            </el-upload>
      </div>

      <el-main>
        <el-table
          v-loading="tableLoading"
          :data="listData"
          style="width: 100%">
          <el-table-column
            prop="createTime"
            label="上传时间"
            min-width="20%">
          </el-table-column>
          <el-table-column
            prop="filefirstname"
            label="文件名"
            min-width="20%">
          </el-table-column>
          <el-table-column
            prop="filelastname"
            label="文件类型"
            min-width="20%">
          </el-table-column>
          <el-table-column
            label="下载"
            min-width="20%">
            <template slot-scope="scope">
              <el-button type="primary" icon="el-icon-download" size="mini" @click.native.prevent="downloadMat(scope.$index)"></el-button>
<!--              <a :href="get_modified_auth_users(scope.$index)" :download="getDownloadName(scope.$index)">下载名单</a>-->
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer class="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = currentPage
          :page-sizes="[10, 20, 30, 40]"
          :page-size="params.param.page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total=totalNum>
        </el-pagination>
      </el-footer>
    </el-container>
  </div>
</template>
<script>
  import axios from 'axios'
  import qs from 'qs';

  export default {
    data(){
      return{
        bodyLoading:false,
        tableLoading:false,
        fileLastNameList:[],
        serviceFilePath:'',
        name: "helping",
        input:'',
        currentPage: 0,
        value1:'',
        Feed:'',
        Read:'',
        listData: [],
        totalNum:0,
        infoData:{},
        isFeedback:true,
        start:'',
        end:'',
        fileLists: [],
        fileName:'',
        params:{
            className: 'Help',
            method: 'selectHelpList',
            param:{
                searchData:{
                    type:"Help"
                },
                page:{
                pageIndex:1,
                pageSize:10,
                totalPage:0,
        },
            }
        },
      }
    },
    created(){
      this.getList();
    },
    methods: {
        //上传前校验
        beforeUpload(file) {
            console.log(file);
            this.fileName = file.name;
            let aaa = this.fileName.substr(this.fileName.lastIndexOf('.') + 1).toLowerCase();
            console.log(aaa);
            if(aaa != ""&&aaa !="xlsx"&&aaa != "pdf"&&aaa!= "doc"&&aaa!="docx"){
                this.$message({
                    message: '上传文件格式错误',
                    type: 'warning'
                });
                return false
            }else{
            const isLt1M = file.size / 1024 / 1024 < 2;
            console.log(isLt1M)
            if (isLt1M) {
                return true
            }else {
                this.$message({
                    message: '上传文件大小不能超过2M',
                    type: 'warning'
                });
                return false
            }}
        },
        //读取文件
        myUpload(content) {
            this.$nextTick(async () => {
                let fd = new FormData();
                fd.append('file', content.file);
                let params = {param: fd, url: '/api/file/fileupload'};
                let response = await this.$store.dispatch('http/fileUpload', params);
                if (response) {
                    console.log("serviceFilePath:", response);
                    this.serviceFilePath = response;
                    this.$message({
                        message: '读取成功'
                    });
                } else {
                    this.$message.error('读取失败');
                }
            })
        },
        //上传文件
        submitUpload() {
            if (this.serviceFilePath == '' || this.serviceFilePath == null) {
                this.$message({message: '请选择上传文件！', type: 'warning'});
                return false;
            }
            this.bodyLoading = true;
            this.$nextTick(async () => {
                let param = {
                    FileName: this.fileName,
                    FileUrl: this.serviceFilePath,
                };
                let params = {
                    className: 'Help',
                    method: 'helpUpload',
                    param: param
                };
                let res = await this.$store.dispatch("http/post", params);
                console.log(res);
                if (res == '200') {
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                    this.fileLists = [];
                    this.getList();
                    this.serviceFilePath = '';
                    this.bodyLoading = false;
                } else {
                    this.$message.error(res.code + ':' + res.msg);
                }
            })
        },
        //获取列表
        getList() {
            this.tableLoading = true;
            this.$nextTick(async () => {
                let res = await this.$store.dispatch('http/post', this.params);
                console.log(res);
                this.listData = res.dataList;
                console.log(this.fileLastNameList)
                this.totalNum = Number(res.page.totalNum);
                this.tableLoading = false;
            })
        },
        //切换每页个数
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.params.param.page.pageSize = val;
            this.getList();
        },
        //切换页数
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.params.param.page.pageIndex = val;
            this.getList();
        },
        //下载文件
        downloadMat(row){
            let thisData = qs.stringify({
                fileName : this.listData[row].fileName,
                fileUrl : this.listData[row].fileUrl
            });
                axios({
                    method: "POST",
                    url: "/api/file/download",
                    data: thisData,
                    responseType: 'blob',
                })
                    .then(res => {
                    const blob = new Blob([res.data]);//处理文档流
                    const fileName = this.listData[row].fileName;
                    const elink = document.createElement('a');
                    elink.style.display = 'none';
                    elink.href = window.URL.createObjectURL(blob);
                    elink.setAttribute("download",fileName);
                    document.body.appendChild(elink);
                    elink.click();
                    URL.revokeObjectURL(elink.href); // 释放URL 对象
                    document.body.removeChild(elink);
                })
                    .catch(error => {
                    // 请求失败
                    this.$message({showClose: true, message: '下载异常，请稍后再试', type: 'error'});
                });
        },
    }
  }
</script>
<style scope>

  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
  }
  .header{
    padding: 13px;
  }

</style>
