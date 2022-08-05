<template>
  <div>
    <el-container>
      <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
        <el-form :inline="true" :model="Searchfilter">
          <el-form-item>
            <el-input v-model="Searchfilter.filetitle" placeholder="输入文件名" :clearable="true"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search"  plain @click="Search()">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-plus" plain  @click="AddList(),dialogVisibleAdd = true">发布</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <!--列表-->
      <el-table :data="files" border stripe  highlight-current-row v-loading="listLoading"  @selection-change="selsChange" style="width: 100%;">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="code" label="编号" width="60"></el-table-column>
        <el-table-column prop="name" label="视频名称" width="300"></el-table-column>
        <el-table-column prop="videoIntroduction" label="视频简介" width="600"></el-table-column>
        <el-table-column prop="isused" label="热点视频" width="100" :formatter="formatSex"></el-table-column>
        <el-table-column
          label="操作">
          <template slot-scope="scope">
            <el-button scope.idtype="text"  type="primary"    plain  size="small" @click="handleEdit(scope.$index, scope.row)" >修改</el-button>
            <!--<el-button  scope.idtype="text" type="primary"  size="small" @click="Search1(scope.row),dialogVisibleread=true">访问情况</el-button>-->
            <el-button  type="danger"  size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-footer class="footer">
        <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
        <el-pagination background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="listQuery.pageIndex"
          :page-sizes="[10,20,30, 50]"
          :page-size="listQuery.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="listQuery.total" style="float:right;">
        </el-pagination>
      </el-footer>
    </el-container>


    <!--添加文件-->
    <el-dialog
      title="添加"
      :visible.sync="dialogVisibleAdd"
      width="80%" top="4vh">
      <span>
       <el-form ref="addfrom" :model="addfrom"  label-width="80px">
        <el-form-item label="编号" prop="code" :rules="[{ required: true, message: '请输入编号', trigger: 'blur'}]">
          <el-input v-model="addfrom.code" :maxlength="50" placeholder="请输入编号.." ></el-input>
        </el-form-item >

        <el-form-item label="视频名称" prop="name" :rules="[{ required: true, message: '请输入视频名称', trigger: 'blur' }]">
          <el-input v-model="addfrom.name"  placeholder="请输入视频名称.."></el-input>
        </el-form-item>

        <el-form-item label="视频简介" prop="videoIntroduction" :rules="[{ required: true, message: '请输入视频简介', trigger: 'blur' }]">
          <el-input v-model="addfrom.videoIntroduction"  type="textarea" placeholder="请输入视频简介.."></el-input>
        </el-form-item>

        <el-form-item label="封面" prop="filepic">
        <el-upload
          ref="upload"
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :data="upLoadData"
          :file-list="fileList"
          :limit=1
          :http-request="myUpload"
          list-type="picture"
          :onError="uploadError"
          :onSuccess="uploadSuccess"
          :beforeUpload="beforeAvatarUploadPic"
        >
        <el-button size="small" type="success">上传图片</el-button>
        <div slot="tip" class="el-upload__tip">图片长宽（2：1）</div>
        </el-upload>
        </el-form-item>

        <el-form-item label="附件" prop="attachment">
          <el-upload
          ref="upload1"
          class="upload-demo"
          action=""
          :file-list="fileList"
          :limit=1
          :data="upLoadData"
          :http-request="myUpload1"
          :onError="uploadError"
          :onSuccess="uploadSuccess"
          :beforeUpload="beforeAvatarUpload">
          <el-button size="small" type="primary">上传视频</el-button>
          </el-upload>
				 </el-form-item>
         <el-form-item label="热点视频">
          <template>
            <el-radio v-model="hotflagradio" label="1">是</el-radio>
            <el-radio v-model="hotflagradio" label="0">否</el-radio>
          </template>
         </el-form-item>
      </el-form>
       </span>
      <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleAdd = false">取 消</el-button>
     <el-button type="primary"  @click="addSubmit">确 定</el-button>
    </span>
    </el-dialog>


    <!--修改文件:disabled="true"-->
    <el-dialog title="修改" :visible.sync="dialogVisibleEdit" width="80%" top="4vh">
      <span>
        <el-form ref="editfrom" :model="editfrom"  label-width="80px">
        <el-form-item label="编号" prop="code" :rules="[{required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="editfrom.code" :maxlength="50" placeholder="请输入编号.."></el-input>
        </el-form-item >

        <el-form-item label="视频名称" prop="name" :rules="[{required: true, message: '请输入文件名称', trigger: 'blur' }]">
          <el-input v-model="editfrom.name" :maxlength="50" placeholder="请输入名称.."></el-input>
        </el-form-item>

        <el-form-item label="视频简介" prop="videoIntroduction" :rules="[{required: true, message: '请输入视频简介', trigger: 'blur' }]">
          <el-input v-model="editfrom.videoIntroduction" type="textarea" placeholder="请输入描述.."></el-input>
        </el-form-item>

         <el-form-item label="封面" prop="videoCoverUrl">
          <el-upload
            ref="upload"
            class="upload-demo"
            action="https://jsonplaceholder.typicode.com/posts/"
            :data="upLoadData"
            :file-list="fileList1"
            :limit=1
            :http-request="myUploadEdit"
            list-type="picture-card">
            <i class="el-icon-plus"></i>
           </el-upload>
           <el-dialog v-model="dialogVisible1" size="tiny">
             <img width="100%" :src="dialogImageUrl" alt="">
           </el-dialog>
        </el-form-item>

        <el-form-item label="附件" prop="attachment">
        <el-upload
          ref="upload1"
          class="upload-demo"
          :file-list="fileList2"
          action="https://jsonplaceholder.typicode.com/posts/"
          :data="upLoadData"
          :limit=1
          :http-request="myUploadEdit1"
          :onError="uploadError"
          :onSuccess="uploadSuccess"
          :beforeUpload="beforeAvatarUpload"
        >
          <el-button size="small" type="primary">修改</el-button>
        </el-upload>

				</el-form-item>
          <el-form-item label="热点文件">
          <template>
            <el-radio v-model="editfrom.isused" label="1">是</el-radio>
            <el-radio v-model="editfrom.isused" label="0">否</el-radio>
          </template>
        </el-form-item>
      </el-form>
       </span>
      <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleEdit = false">取 消</el-button>
     <el-button type="primary"  @click="updateSubmit">确 定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
  import ElUpload from "element-ui/packages/upload/src/index";
    export default {
      components: {ElUpload},
      data() {

        return {
          hotflagradio:'1',
          files: [],
          Searchfilter:{
            filetitle:''
          },
          show:false,
          fileList:[],
          fileList1:[],
          fileList2:[],
          listLoading: false,
          dialogVisible: false,
          dialogImageUrl:'',
          dialogVisible1:false,
          dialogVisibleAdd:false,
          dialogVisibleEdit:false,
          items:[],
          reader:[],
          sels: [],//列表选中列
          kind:'purchaseIdea',
          addfrom:{
            id:'',
            code:'',
            name:'',
            videoCoverUrl:'',
            videoUrl:'',
            filename:'',
            fileformat:'',
            videoIntroduction:'',
            isused:''
          },
          editfrom:{
            id:'',
            code:'',
            name:'',
            videoCoverUrl:'',
            videoUrl:'',
            filename:'',
            fileformat:'',
            videoIntroduction:'',
            isused:''
          },
          chapterform:{
            id:'',
            code:'',
            name:'',
            sectionflag:'',
            attachment:'',
            attachmentname:''
          },
          readfrom:{
            id:'',
            code:'',
            filecode:'',
            reader:'',
            readertime:'',
            department:''
          },
          upLoadData: {
            cpyId: '123456',
            occurTime: '2017-08'
          },
          readlistQuery: {
            currentPage: 1,
            currentItem:1,
            pageSize: 10,
            importance: undefined,
            total:0,
            totalItems:0,
            title: undefined,
            type: undefined,
            sort: '+id',
            orderKind:'desc',
            orderName:undefined,
            searchData:{filetitle:undefined,filetype:undefined}
          },
          listQuery:{
            pageIndex:1,//当前页
            pageSize:10,//每页大小
            pageCount:0,//总页数
            total: 0,// 总条目数
            currentPage: 1,
            currentItem:1,
            importance: undefined,
            totalItems:0,
            title: undefined,
            type: undefined,
            sort: '+id',
            orderKind:'desc',
            orderName:undefined,
            searchData:{filetitle:undefined}
          }
        }
      },
      methods: {
        //上传
        myUpload(content){
          this.$nextTick(async () =>{
            var fd = new FormData();
            fd.append('file', content.file);
            let params = {param:fd,url:process.env.BASE_API + '/api/file/fileupload'};
            let response = await this.$store.dispatch('http/fileUpload', params);
            if(response){
              console.log("serviceFilePath:",response);
              this.addfrom.videoCoverUrl=response;
              // this.serviceFilePath = response;
              this.$message({message: '上传成功', type: 'success'});
            }else{
              this.$message.error('上传失败');
            }
          })
        },

        //修改上传
        myUploadEdit(content){
          var fd = new FormData();
          fd.append('file', content.file);
          this.$nextTick(async () =>{
            let params = {param:fd,url:process.env.BASE_API + '/api/file/fileupload'};
            let response = await this.$store.dispatch('http/fileUpload', params);
            console.log(response)
            if(response){
              this.editfrom.videoCoverUrl=response;
              this.$message({message: '上传成功', type: 'success'});
            }
            else{
              this.$message({message: '上传失败', type: 'failed'});
            }
          })
        },
        handlePictureCardPreview(file){
         this.dialogImageUrl=file.url;
         this.dialogVisible1=true;
        },

        myUpload1(content){
          console.log(content)
          var fd = new FormData();
          fd.append('file', content.file);
          this.addfrom.filename=content.file.name;
          this.addfrom.fileformat=content.file.type;
          this.$nextTick(async () =>{
            let params = {param:fd,url:process.env.BASE_API + '/api/file/fileupload'};
            let response = await this.$store.dispatch('http/fileUpload', params);
            if(response){
              console.log("serviceFilePath:",response);
              this.addfrom.videoUrl=response;
              this.$message({message: '上传成功', type: 'success'});
            }else{
              this.$message({message: '上传失败', type: 'failed'});
            }
          })
        },

        myUploadEdit1(content){
          var fd = new FormData();
          fd.append('file', content.file);
          this.editfrom.filename=content.file.name;
          this.editfrom.fileformat=content.file.type;
          this.$nextTick(async () =>{
            let params = {param:fd,url:process.env.BASE_API + '/api/file/fileupload'};
            let response = await this.$store.dispatch('http/fileUpload', params);
            if(response){
              console.log("serviceFilePath:",response);
              this.editfrom.videoUrl=response;
              this.$message({message: '上传成功', type: 'success'});
            }else{
              this.$message({message: '上传失败', type: 'failed'});
            }
          })
        },
        // 上传成功后的回调
        uploadSuccess (response, file, fileList) {
          console.log('上传文件', response)
        },
        // 上传错误
        uploadError(response, file, fileList) {
          console.log('上传失败，请重试！')
        },
        beforeAvatarUploadPic(file){
          const fileType = file.name.split('.')[file.name.split('.').length-1] === 'png' || file.name.split(',')[file.name.split(',').length-1] === 'jpeg' || file.name.split('.')[file.name.split('.').length-1] === 'PNG';
          // const fileSize = file.size / 1024 / 1024 < 10;
          if(!fileType){
            this.$message({message:'上传的图片只能是 png/jpeg 格式！', type:'error', duration:2500});
            // this.$refs.upload.clearFiles();
            return false;
          }
          return fileType;
        },
        // 上传前对文件的判断
        beforeAvatarUpload(file) {
          const fileType = file.name.split('.')[file.name.split('.').length-1] === 'mp4';
          // const fileSize = file.size / 1024 / 1024 < 10;
          if(!fileType){
            this.$message({message:'上传的文件只能是 mp4 格式！', type:'error', duration:2500});
            // this.$refs.upload.clearFiles();
            return false;
          }
          return fileType
        },

        selsChange:function (sels) {
          this.sels = sels;
        },
        AddList(){
          for(var name in this.$data.addfrom) {
            this.$data.addfrom[name] = ""
          }
          this.fileList=[];
          this.fileList1=[];
          this.fileList2=[];
        },

        formatSex: function (row, column) {
          return row.isused == 1 ? '是' : row.isused == 0 ? '否' : '否';
        },

        sort(val){
          this.order(val.order);
          val.prop=="filetitle"

          this.listQuery.orderName=val.prop;
          this.getFiles();
        },
        order(val) {
          if(val=="descending"){
            this.listQuery.orderKind='desc'
          }else if(val=="ascending"){
            this.listQuery.orderKind='asc'
          }
        },

        handleSizeChange(val){
          this.listQuery.pageSize = val;
          this.getFiles();
        },
        handleCurrentChange(val) {
          this.listQuery.pageIndex = val;
          this.getFiles();
        },

        //搜索
        Search(){
          this.getFiles();
        },
        //新增
        addSubmit: function () {
          this.addfrom.isused = this.hotflagradio;
          console.log(this.addfrom);
          if(this.addfrom.videoUrl==null || this.addfrom.videoUrl==''){
            this.$message({
              message:'请上传视频',
              type: 'warning'
            });
            return
          }
          this.$refs.addfrom.validate((valid) =>{
            if (valid) {
              this.$confirm('确认是否发布到call浦端？','提示', {}).then(() => {
                this.addLoading = true;
                this.$nextTick(async () =>{
                  let params = {
                    className:'VideoFile',
                    method:'addvideoFile',
                    param: this.addfrom
                  }
                  let res = await this.$store.dispatch("http/post", params);
                  console.log(res)
                  this.addLoading = false;
                  if(res==="200"){
                    this.$message({message: '发布成功', type: 'success'});
                  }else{
                    this.$message({message: '发布失败', type: 'failed'});
                  }
                  this.AddList();
                  this.dialogVisibleAdd = false;
                  this.getFiles();
                })
              });
            }
          });
        },
        //修改
        updateSubmit: function () {
          console.log(this.editfrom);
          if(this.editfrom.videoUrl==null || this.editfrom.videoUrl==''){
            this.$message({
              message:'请上传视频',
              type: 'warning'
            });
            return
          }
          this.$refs.editfrom.validate((valid)=>{
            if (valid) {
              this.$confirm('确认提交吗？', '提示', {}).then(() => {
                this.addLoading = true;
                this.$nextTick(async () =>{
                  let params={
                    className:'VideoFile',
                    method:'updatevideoFile',
                    param: this.editfrom
                  }
                  let res = await this.$store.dispatch("http/post", params);
                  console.log(res)
                  if(res==="200"){
                    this.$message({message: '提交成功', type: 'success'});
                  }else{
                    this.$message({message: '删除失败', type: 'failed'});
                  }
                  this.AddList();
                  this.dialogVisibleEdit = false;
                  this.getFiles();
                })
              }).then(() => {});
             }
          });
        },
        //获取用户列表
        getFiles(){
          let _this = this;
          this.$nextTick(async () =>{
            let param = {
              searchData:{
                searchName: _this.Searchfilter.filetitle,
              },
              page:{
                pageIndex:_this.listQuery.pageIndex,
                pageSize:_this.listQuery.pageSize
              }
            }
            const params = {className:'VideoFile', method:'getVideofilebypage', param:param};
            let response = await this.$store.dispatch('http/post', params);
            console.log(response);
            this.files = response.dataList;
            this.listQuery.total = response.page.totalNum;
           })
        },

        order(val) {
          if(val=="descending"){
            this.readlistQuery.orderKind='desc'
          }else if(val=="ascending"){
            this.readlistQuery.orderKind='asc'
          }
        },
        //update
        handleEdit: function (index, row){
          this.dialogVisibleEdit = true;
          this.fileList1=[];
          this.fileList2=[];
          if(row.name!="" && row.name!=null){this.fileList2.push({name:row.name});}
          this.$nextTick(async () =>{
            let params = {param:row.videoCoverUrl,url:'/api/file/downloadfile'};
            console.log(row.videoCoverUrl)
            let response = await this.$store.dispatch('http/fileDownload', params);
            console.log(response);
            let path;
            if(response!="" && response != null){
              path = "data:image/png;base64,"+response;
            }
            else{
              path = "";
            }
            this.fileList1.push({url:path})
          })
          this.editfrom = Object.assign({}, row);
          console.log(this.editfrom);
        },

        //批量删除
        batchRemove: function () {
          var ids = this.sels.map(item => item.id).toString();
          console.log(ids)
          this.$confirm('确认删除选中记录吗？', '提示', {
            type: 'warning'
          }).then(() => {
            this.$nextTick(async () =>{
              let param = {
                ids:ids
              }
              let params ={
                className:'VideoFile',
                method:'batchDeleteVideoFile',
                param: param
              }
              let res = await this.$store.dispatch("http/post", params);
              console.log(res)
              if(res==="200"){
                this.listLoading = false;
                this.$message({message: '删除成功', type: 'success'});
              }else{
                this.$message({message: '删除失败', type: 'failed'});
              }
              this.getFiles();
            })
          }).catch(() => {

          });
        },
        //删除
        handleDel: function (index, row) {
          this.$confirm('确认删除该记录吗?', '提示', {
            type: 'warning'
          }).then(() => {
            this.listLoading = true;
            this.$nextTick(async () =>{
              let param = {
                id:row.id
              }
              let params = {
                className:'VideoFile',
                method:'delvideoFile',
                param: param
              }
              let res = await this.$store.dispatch("http/post", params);
              console.log(res)
              if(res==="200"){
                this.listLoading = false;
                  this.$message({message: '删除成功', type: 'success'});
              }else{
                this.$message({message: '删除失败', type: 'failed'});
              }
              this.getFiles();
            })
          }).catch(() => {});
        },
      },
      mounted:function () {
        let self = this;
        self.getFiles();
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
  .toolbar{
    margin-top: 1rem;
  }
  .footer{
    padding-top: 1rem;
  }
</style>
