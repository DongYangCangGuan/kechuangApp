<!--20200911-xues-增加"投大"菜单-->
<template>
    <div>
        <el-container>

            <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
                <el-form :inline="true" :model="Searchfilter">
                    <el-form-item>
                        <el-input v-model="Searchfilter.filetitle" placeholder="输入文件名" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="Searchfilter.filetype" placeholder="选择文件类型"  :clearable="true" @change="change1" >
                            <el-option v-for="(item,index) in items"  :key="index" :label="item.name" :value="item.code"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search"  plain @click="Search()">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-plus" plain  @click="AddList(),dialogVisibleAdd = true">发布</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-download" @click="exportData()">导出</el-button>
                    </el-form-item>
                </el-form>
            </el-col>

            <!--列表-->
            <el-table :data="files" border stripe  highlight-current-row v-loading="listLoading"  @selection-change="selsChange" style="width: 100%;">

                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="code" label="编号" width="60"></el-table-column>
                <el-table-column  prop="filetitle" label="文件名称" width="250"></el-table-column>
                <el-table-column  prop="filecname" label="类型" width="200"></el-table-column>
                <el-table-column  prop="description" label="文件描述" width="500"></el-table-column>
                <!--<el-table-column  prop="hotflag" label="热点文件" width="100" :formatter="formatSex"></el-table-column>-->
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
                width="80%">
      <span>
       <el-form ref="addfrom" :model="addfrom"  label-width="80px">
        <el-form-item label="编号" prop="code" :rules="[{ required: true, message: '请输入编号', trigger: 'blur'}]">
          <el-input v-model="addfrom.code" :maxlength="50" placeholder="请输入编号.." ></el-input>
        </el-form-item >

        <el-form-item label="文件名称" prop="filetitle" :rules="[{ required: true, message: '请输入文件名称', trigger: 'blur' }]">
          <el-input v-model="addfrom.filetitle"  placeholder="请输入名称.."></el-input>
        </el-form-item>

         <el-form-item label="文件类型" prop="fifletype">
            <el-select v-model="addfrom.filetype" placeholder="请选择" @change="change" >
                <el-option v-for="(item,index) in items"  :key="index" :label="item.name" :value="item.code"></el-option>
            </el-select>
        </el-form-item>

        <el-form-item label="文件描述" prop="description">
         <el-input v-model="addfrom.description"  type="textarea" placeholder="请输入描述.."></el-input>
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
                  action="https://jsonplaceholder.typicode.com/posts/"
                  :file-list="fileList"
                  :limit=1
                  :data="upLoadData"
                  :http-request="myUpload1"
                  :onError="uploadError"
                  :onSuccess="uploadSuccess"
                  :beforeUpload="beforeAvatarUpload"
          >
        <el-button size="small" type="primary">上传文件</el-button>
        </el-upload>
				</el-form-item>


          <el-form-item label="热点文件">
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
        <el-dialog title="修改" :visible.sync="dialogVisibleEdit" width="80%">
      <span>
  <el-form ref="editfrom" :model="editfrom"  label-width="80px">
        <el-form-item label="编号" prop="code" :rules="[{ required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="editfrom.code" :maxlength="50" placeholder="请输入编号.."></el-input>
        </el-form-item >

        <el-form-item label="文件名称" prop="filetitle" :rules="[{ required: true, message: '请输入文件名称', trigger: 'blur' }]">
          <el-input v-model="editfrom.filetitle" :maxlength="50" placeholder="请输入名称.."></el-input>
        </el-form-item>

         <el-form-item label="文件类型" prop="fifletype">
            <el-select v-model="editfrom.filetype" placeholder="请选择" @change="change" >
                <el-option v-for="(item,index) in items"  :key="index" :label="item.name" :value="item.code" ></el-option>
            </el-select>
        </el-form-item>

        <el-form-item label="文件描述" prop="description">
          <el-input v-model="editfrom.description"   type="textarea" placeholder="请输入描述.."></el-input>
        </el-form-item>

         <el-form-item label="封面" prop="filepic">
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
            <el-radio v-model="editfrom.hotflag" label="1">是</el-radio>
            <el-radio v-model="editfrom.hotflag" label="0">否</el-radio>
          </template>
        </el-form-item>
      </el-form>
       </span>
            <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleEdit = false">取 消</el-button>
     <el-button type="primary"  @click="updateSubmit">确 定</el-button>
    </span>
        </el-dialog>



        <!--添加章节-->
        <el-dialog
                title="添加章节"
                :visible.sync="chapterDialogVisible"
                width="60%">
      <span>
       <el-form ref="chapterform" :model="chapterform"  label-width="80px">
        <el-form-item label="编号"
                      prop="code"
                      :rules="[
          { required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="chapterform.code" :maxlength="50" placeholder="请输入编号.." ></el-input>
        </el-form-item >
        <el-form-item label="章节名称"
                      prop="name"
                      :rules="[
          { required: true, message: '请输入名称', trigger: 'blur' }]">
          <el-input v-model="chapterform.name" :maxlength="50" placeholder="请输入名称.."></el-input>
        </el-form-item>


           <el-form-item label="附件"
                         prop="attachment">

             <el-input v-model="chapterform.attachment" v-if="show"></el-input>
    <el-upload
            ref="upload2"
            class="upload-demo"
            action="https://jsonplaceholder.typicode.com/posts/"
            :data="upLoadData"
            :file-list="fileList3"
            :http-request="myUpload2"
            :onError="uploadError"
            :onSuccess="uploadSuccess"
            :beforeUpload="beforeAvatarUpload"
    >
               <el-button size="small" type="primary">上传文件</el-button>
        </el-upload>

				</el-form-item>


      </el-form>
       </span>
            <span slot="footer" class="dialog-footer">
     <el-button @click="chapterDialogVisible = false">取 消</el-button>
     <el-button type="primary"  @click.native="chaptersubmit()">确 定</el-button>
    </span>
        </el-dialog>



        <el-dialog
                title="阅读情况"
                :visible.sync="dialogVisibleread"
                width="50%">
      <span>

 <!--列表-->
      <el-table id="out-table" :data="reader" border stripe  highlight-current-row v-loading="listLoading"  style="width: 100%;">

        <el-table-column type="index" width="60">
        </el-table-column>
         <el-table-column  prop="filecode" label="文件" width="100" sortable>
        </el-table-column>
        <el-table-column  prop="reader" label="阅读人" width="100" sortable>
        </el-table-column>
        <el-table-column  prop="readertime" label="阅读时间" width="200" sortable>
        </el-table-column>


        <el-table-column  prop="department" label="部门" width="200" sortable>
        </el-table-column>


      </el-table>

          <!--工具条-->
      <el-footer class="footer">
       <el-button type="primary" @click="exportExcel" >导出</el-button>
        <el-pagination background  @current-change="handleCurrentChange1"
                       :current-page="readlistQuery.currentPage" :page-sizes="[10,20,30, 50]"
                       :page-size="readlistQuery.pageSize"
                       layout="total, sizes, prev, pager, next, jumper" :total="readlistQuery.total" style="float:right;">
        </el-pagination>
      </el-footer>

      </span>
        </el-dialog>

    </div>
</template>

<script>
    // import { getFileList,configList ,InsertRiskFile,fileupload,batchdelFile,download,delFile,updateFile,getReadList} from '../../api/api';
    // import FileSaver from 'file-saver'
    // import XLSX from 'xlsx'
    import ElUpload from "element-ui/packages/upload/src/index";
    export default {
        components: {ElUpload},
        data() {

            return {
                hotflagradio:'0',
                hotistop:'0',
                files: [],
                Searchfilter:{
                    filetitle:'',
                    filetype:''
                },

                radio:'0',
                show:false,
                fileList:[],
                fileList1:[],
                fileList2:[],
                fileList3:[],
                importFileUrl: 'E://',
                editFormVisible:false,
                listLoading: false,
                dialogVisible: false,
                dialogImageUrl:'',
                pic:'',
                dialogVisible1:false,
                dialogVisibleAdd:false,
                dialogVisibleEdit:false,
                chapterDialogVisible:false,
                dialogVisibleread:false,
                items:[],
                reader:[],
                sels: [],//列表选中列
                chapters:[],
                kind:'touda',
                addfrom:{
                    id:'',
                    code:'',
                    fifletitle:'',
                    filepic:'',
                    attachment:'',
                    description:'',
                    hotflag:'0',
                    chapterflag:'',
                    filetype:'',
                    fileformat:'',
                    filename:'',
                    importfile:'',
                    istop:'0',
                    // chapterSet:[]
                },
                editfrom:{
                    id:'',
                    code:'',
                    fifletitle:'',
                    filepic:'',
                    attachment:'',
                    description:'',
                    chapterflag:'',
                    hotflag:'',
                    filetype:'',
                    fileformat:'',
                    filename:'',
                    importfile:'',
                    istop:'',
                    // chapterSet:[]
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
                listQuery: {
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

            //导出
            exportExcel () {

                /* generate workbook object from table */
                var wb = XLSX.utils.table_to_book(document.querySelector('#out-table'))

                /* get binary string as output */
                var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
                try {
                    FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), 'sheetjs.xlsx')
                } catch (e) { if (typeof console !== 'undefined') console.log(e, wbout) }
                return wbout
            },
            //上传
            myUpload(content){
                this.$nextTick(async () =>{
                    var fd = new FormData();
                    fd.append('file', content.file);
                    let params = {param:fd,url:process.env.BASE_API + '/api/file/fileupload'};
                    let response = await this.$store.dispatch('http/fileUpload', params);
                    if(response){
                        console.log("serviceFilePath:",response);
                        this.addfrom.filepic=response;
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
                        this.editfrom.filepic=response;
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
                        this.addfrom.attachment=response;
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
                        this.editfrom.attachment=response;
                        this.$message({message: '上传成功', type: 'success'});
                    }else{
                        this.$message({message: '上传失败', type: 'failed'});
                    }
                })
            },

            myUpload2(content){
                var fd = new FormData();
                fd.append('file', content.file);
                this.chapterform.sectionflag=content.file.type;
                this.chapterform.attachmentname=content.file.name;

                fileupload(fd).then((res) => {
                    var data=res.data;
                    //NProgress.done();
                    if(data.code=="200"){
                        this.chapterform.attachment=data.data;
                        this.$message({message: '上传成功', type: 'success'});
                    }
                    else{
                        this.$message({message: '上传失败', type: 'failed'
                        });
                    }

                })
            },
            // 上传成功后的回调
            uploadSuccess (response, file, fileList) {
                console.log('上传文件', response)
            },
            //删除章节
            handlechapterDel(index,row){
                this.editfrom.chapterSet.pop(row);
            },
            chapterDel(index,row){
                this.chapters.pop(row);
            },
            // 上传错误
            uploadError (response, file, fileList) {
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
            beforeAvatarUpload (file) {
                const fileType = file.name.split('.')[file.name.split('.').length-1] === 'pdf';
                // const fileSize = file.size / 1024 / 1024 < 10;
                if(!fileType){
                    this.$message({message:'上传的文件只能是 pdf 格式！', type:'error', duration:2500});
                    // this.$refs.upload.clearFiles();
                    return false;
                }
                // if(!fileSize){
                //   this.$message({
                //     message:'上传模板大小不能超过10MB！',
                //     type:'error',
                //     duration:2500
                //   });
                //   this.$refs.upload.clearFiles();
                // }
                // return fileType && fileSize
                return fileType
            },


            selsChange: function (sels) {
                this.sels = sels;
            },
            change:function(){

                this.getfiletype();
            },
            change1:function(){
                this.getFiles();
            },
            chaptersubmit:function () {
                this.$refs.chapterform.validate((valid) => {
                    if (valid) {
                        this.AddChapterToFile();
                    }
                });
            },
            AddList(){
                for(var name in this.$data.addfrom) {
                    this.$data.addfrom[name] = ""
                }
                this.chapters=[];
                this.fileList=[];
                this.fileList1=[];
                this.fileList2=[];
            },

            ChapterList(){


                this.chapterform.code="";
                this.chapterform.name="";
                this.chapterform.attachment="";
                this.fileList3=[];
            },

            AddChapterToFile(){
                let chapter={};
                chapter.code=this.chapterform.code;
                chapter.name=this.chapterform.name;
                chapter.attachment=this.chapterform.attachment;
                chapter.sectionflag=this.chapterform.sectionflag;
                chapter.attachmentname=this.chapterform.attachmentname;
                this.chapters.push(chapter);
                this.chapterDialogVisible=false;

                this.editfrom.chapterSet.push(chapter);
                this.ChapterList();

            },
            formatSex: function (row, column) {
                return row.hotflag == 1 ? '是' : row.hotflag == 0 ? '否' : '否';
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
                this.addfrom.hotflag = this.hotflagradio;
                this.addfrom.istop = this.hotistop
                this.$refs.addfrom.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认是否发布到call浦端？', '提示', {}).then(() => {
                            this.addLoading = true;
                            //NProgress.start();
                            // this.addfrom.chapterSet=[];
                            // if(this.chapters.length>0){
                            //   this.addfrom.chapterflag='1';
                            // }
                            // else{
                            //   this.addfrom.chapterflag='0';
                            // }
                            // for(var i=0;i<this.chapters.length;i++){
                            //   this.addfrom.chapterSet.push(this.chapters[i]);
                            // }
                            //this.addfrom.chapterSet=Object.assign({}, this.chapters);
                            this.$nextTick(async () =>{
                                let params = {
                                    className:'SearchFile',
                                    method:'addRiskFile',
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
                this.$refs.editfrom.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            this.$nextTick(async () =>{
                                let params = {
                                    className:'SearchFile',
                                    method:'updateRiskFile',
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
                        });
                    }
                });
            },
            //获取用户列表
            getFiles() {
                let _this = this;
                this.$nextTick(async () =>{
                    let param = {
                        searchData:{
                            searchName: _this.Searchfilter.filetitle,
                            filetype:_this.Searchfilter.filetype,
                            kind:"touda"
                        },
                        page:{
                            pageIndex:_this.listQuery.pageIndex,
                            pageSize:_this.listQuery.pageSize
                        }
                    }
                    const params = {className:'SearchFile', method:'getallfilebypage', param:param};
                    let response = await this.$store.dispatch('http/post', params);
                    console.log(response);
                    this.files = response.dataList;
                    this.listQuery.total = response.page.totalNum;
                })
            },

            resolve1: function (data) {
                this.reader = data.data.data;
                this.readlistQuery.currentPage =data.data.currentPage;
                this.readlistQuery.totalItems =data.data.totalItems;
                this.readlistQuery.total = data.data.totalItems;
            },

            order(val) {
                if(val=="descending"){
                    this.readlistQuery.orderKind='desc'
                }else if(val=="ascending"){
                    this.readlistQuery.orderKind='asc'
                }
            },


            handleCurrentChange1(val) {
                // alert(val);
                this.readlistQuery.currentPage = val

                this.readlistQuery.currentItem = (this.readlistQuery.currentPage-1) *  this.readlistQuery.pageSize+1;

                this.getreadinfo();
            },
            //搜索
            Search1(row){

                this.getreadinfo(row);
            },
            getreadinfo(row){
                // this.readlistQuery.searchData.filecode=row.code;
                this.readlistQuery.searchData.filecode=row.id;
                this.listLoading = true;
                //NProgress.start();
                getReadList(this.readlistQuery).then((res) => {
                    this.total ="";
                    this.resolve1(res.data);

                    this.listLoading = false;
                    //NProgress.done();
                });

            },
            getfiletype(){
                let _this = this;
                this.$nextTick(async () =>{
                    let param={kind:_this.kind}
                    const params = {className:'SearchFile', method:'getFileType', param:param};
                    let response = await this.$store.dispatch('http/post', params);
                    this.items = response;
                })
            },

            //update
            handleEdit: function (index, row) {
                this.dialogVisibleEdit = true;
                this.fileList1=[];
                this.fileList2=[];
                if(row.filename!="" && row.filename!=null){this.fileList2.push({name:row.filename});}
                this.$nextTick(async () =>{
                    let params = {param:row.filepic,url:'/api/file/downloadfile'};
                    console.log(row.filepic)
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
                            className:'SearchFile',
                            method:'batchDeleteRiskFile',
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
                            className:'SearchFile',
                            method:'delRiskFile',
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
            //导出数据
            exportData(){
                let _this = this;
                this.$nextTick(async () =>{
                    let param = {
                        searchData:{
                            searchName: _this.Searchfilter.filetitle,
                            filetype:_this.Searchfilter.filetype,
                            kind:"touda"
                        },
                        page:null
                    }
                    const params = {className:'SearchFile', method:'getallfilebypage', param:param};
                    let response = await this.$store.dispatch('http/post', params);
                    this.exportdata2(response.dataList)
                })
            },
            exportdata2(data){
                let tempArr = [];
                for(var o in data){
                    var obj = {
                        code:data[o].code,
                        filetitle:data[o].filetitle,
                        filecname:data[o].filecname,
                        description:data[o].description,
                        hotflag:data[o].hotflag == '0'?"否":"是",
                    }
                    tempArr.push(obj)
                }
                console.log("开始导出")
                //列标题
                let str = '<tr><td>编号</td><td>文件名称</td><td>类型</td><td>文件描述</td><td>热点文件</td></tr>';
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
                link.download = "自贸协调.xls";
                link.href = uri + this.getbase64(template);
                link.click();
            },
            getbase64(s){
                return window.btoa(unescape(encodeURIComponent(s)))
            },
        },
        mounted:function () {
            let self = this;
            self.getFiles();
            self.getfiletype();
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
