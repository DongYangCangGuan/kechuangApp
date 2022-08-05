<template>
  <div>


      <el-row>
      <el-col :span="24" class="toolbar" style="padding-top: 10px;padding-left: 10px">
        <el-form :inline="true" :model="Searchfilter">
          <el-form-item>
            <el-input v-model="Searchfilter.question" placeholder="输入问题" :clearable="true"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="Searchfilter.type" placeholder="选择文件类型" @change="change1" :clearable="true" >
              <el-option v-for="(item,index) in items"  :key="index" :label="item.name" :value="item.code"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search"   @click="Search">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-plus"   @click="AddList(),dialogVisibleAdd = true">发布</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-download" @click="exportData()">导出</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      </el-row>

      <el-row>
      <el-tabs type="border-card" v-model="activeName">
        <el-tab-pane label="已归档" name="first">

      <!--已回复列表-->
      <el-table :data="files" border stripe highlight-current-row v-loading="listLoading"
                @selection-change="selsChange" style="width: 100%;">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column prop="code" label="问题编号" width="80" >
        </el-table-column>

        <el-table-column label="问题" width="200" >
          <template slot-scope="scope">
              <p v-html='scope.row.question'></p>
          </template>
        </el-table-column>
        <el-table-column  prop="description" label="问题描述" width="150" >
        </el-table-column>
        <el-table-column  prop="typename" label="问题类型" width="100" >
        </el-table-column>
        <el-table-column   label="答案" width="400" >
          <template slot-scope="scope">
            <p v-html='scope.row.answer'></p>
          </template>
        </el-table-column>

        <el-table-column
          label="操作">
          <template slot-scope="scope">
            <el-button scope.idtype="text" type="primary" size="small" @click="handleEdit(scope.$index, scope.row)" >修改</el-button>
            <!--<el-button  scope.idtype="text" type="primary"  size="small">详细</el-button>-->
            <el-button  type="danger"  size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-footer class="footer">
        <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>


        <el-pagination background  @current-change="handleCurrentChange" @size-change="handleSizeChange"
                       :current-page="page.pageIndex" :page-sizes="[10,20,30, 50]"
                       :page-size="page.pageSize"
                       layout="total, sizes, prev, pager, next, jumper" :total="page.totalNum" style="float:right;">
        </el-pagination>
      </el-footer>
        </el-tab-pane>



        <!--待回复列表-->
        <el-tab-pane label="待回复" name="second">
          <el-table :data="asks" border stripe highlight-current-row v-loading="listLoading"
                    @selection-change="selsChange1" style="width: 100%;">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column type="index" width="60"></el-table-column>
            <el-table-column label="问题" width="200" >
              <template slot-scope="scope">
                <p v-html='scope.row.question'></p>
              </template>
            </el-table-column>
            <el-table-column  prop="description" label="问题描述" width="200" >
            </el-table-column>
            <el-table-column  prop="typename" label="问题类型" width="100" >
            </el-table-column>
            <el-table-column  prop="createName"  label="提问人" width="150" >
            </el-table-column>
            <el-table-column  prop="departmentName" label="提问人机构" width="100" >
        </el-table-column>

            <el-table-column
              label="操作">
              <template slot-scope="scope">
                <el-button scope.idtype="text"  type="primary"     size="small" @click="handleEdit1(scope.$index, scope.row)" >回答</el-button>
                <!--<el-button  scope.idtype="text" type="primary"  size="small">详细</el-button>-->
                <el-button  type="danger"  size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!--工具条-->
          <el-footer class="footer">
            <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>


            <el-pagination background  @current-change="handleCurrentChange" @size-change="handleSizeChange"
                           :current-page="page1.pageIndex" :page-sizes="[10,20,30, 50]"
                           :page-size="page1.pageSize"
                           layout="total, sizes, prev, pager, next, jumper" :total="page1.totalNum" style="float:right;">
            </el-pagination>
          </el-footer>
        </el-tab-pane>
      </el-tabs>
      </el-row>





    <!--添加文件-->
    <el-dialog
      title="问题发布"
      :visible.sync="dialogVisibleAdd"
      width="80%">
      <span>
       <el-form ref="addfrom" :model="addfrom"  label-width="80px">
        <el-form-item label="编号"
                      prop="code"
                      :rules="[
          { required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="addfrom.code" :maxlength="50" placeholder="请输入编号.." ></el-input>
        </el-form-item >

        <el-form-item label="问题"
                      prop="question">
            <el-input  v-model="addfrom.question"
                       ref="myTextEditor"
                       :options="editorOption" @ready="onEditorReady($event)"
                       placeholder="请输入问题..">
            </el-input>
          </el-form-item>

          <el-form-item label="问题描述"
                        prop="description"
                        >
          <el-input v-model="addfrom.description"
                    placeholder="请输入问题描述.."
                    type="textarea"
                    :rows="3">

          </el-input>
<!--            <div style="color:lightgrey; margin-top: -5px;" >各个关键字之间以空格分割</div>-->
        </el-form-item>

         <el-form-item label="问题类型"
                       prop="type"
                       :rules="[
          { required: true, message: '请输入问题名称', trigger: 'blur' }]">

           <el-select v-model="addfrom.type" placeholder="请选择" @change="change" >
                <el-option v-for="(item,index) in items"  :key="index" :label="item.name" :value="item.code"></el-option>
            </el-select>
        </el-form-item>



         <el-form-item label="问题配图" prop="questionimg">

           <el-button
             type="success"
             disabled
             size="mini"
             v-if="this.addfrom.type == null || this.addfrom.type ==''"
           >
             上传图片
           </el-button>

            <el-upload
              v-else
              ref="upload"
              class="upload-demo"
              action="https://jsonplaceholder.typicode.com/posts/"
              :file-list="fileList"
              :on-exceed="handleExceed"
              :limit="6"
              :on-remove="handleRemove"
              :http-request="myUpload"
              list-type="picture"
              :onError="uploadError"
              :onSuccess="uploadSuccess"
              :beforeUpload="beforeAvatarUpload"
            >
                <el-button size="small" type="success">上传图片</el-button>
            </el-upload>

        </el-form-item>


         <el-form-item label="图片名称" prop="questionimgtitle" v-if="show">
         <el-input v-model="addfrom.questionimgtitle" :maxlength="50"  type="textarea" placeholder="请输入描述.."></el-input>
        </el-form-item>

         <el-form-item label="答案" prop="answer" >

         <!--<el-input v-model="addfrom.answer" :maxlength="50"  type="textarea" placeholder="请输入答案.."></el-input>-->

            <el-input v-model="addfrom.answer"
                      type="textarea"
                      ref="myTextEditor"
                      :options="editorOption" @ready="onEditorReady($event)"
                      :rows="4"
                      placeholder="请输入答案..">
            </el-input>

        </el-form-item>

         <el-form-item label="答案配图" prop="answerimg">
           <el-button
             type="success"
             disabled
             size="mini"
             v-if="this.addfrom.type == null || this.addfrom.type ==''"
           >
             上传图片
           </el-button>
            <el-upload
              v-else
              ref="upload"
              class="upload-demo"
              action="https://jsonplaceholder.typicode.com/posts/"
              :on-exceed="handleExceed"
              :limit="6"
              :on-remove="handleRemove1"
              :http-request="myUpload1"
              :file-list="fileList"
              list-type="picture"
              :onError="uploadError"
              :onSuccess="uploadSuccess"
              :beforeUpload="beforeAvatarUpload"
            >
                <el-button size="small" type="success">上传图片</el-button>
            </el-upload>

        </el-form-item>

         <el-form-item label="图片名称" prop="questionimgtitle" v-if="show">

         <el-input v-model="addfrom.answerimgtitle" :maxlength="50"  type="textarea" placeholder="请输入描述.."></el-input>

        </el-form-item>
      </el-form>
       </span>
      <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleAdd = false">取 消</el-button>
     <el-button type="primary"  @click="addSubmit">确 定</el-button>
    </span>
    </el-dialog>


    <!--修改文件-->
    <el-dialog
      title="修改"
      :visible.sync="dialogVisibleEdit"
      width="80%">
      <span>
       <el-form ref="editfrom" :model="editfrom"  label-width="80px">
       <el-form-item label="编号"
                     prop="code"
                     :rules="[
          { required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="editfrom.code" :maxlength="50" placeholder="请输入编号.."></el-input>
        </el-form-item >

        <el-form-item label="问题"
                      prop="question">

          <el-input   v-model="editfrom.question"
                      ref="myTextEditor"
                      type="textarea"
                      :rows="4"
                      :options="editorOption" @ready="onEditorReady($event)">
          </el-input>
        </el-form-item>
          <el-form-item label="问题描述"
                        prop="description"
                       >
          <el-input v-model="editfrom.description" :maxlength="50" placeholder="请输入问题描述.."></el-input>
<!--         <div style="color:lightgrey; margin-top: -5px;" >各个关键字之间以空格分割</div>-->
          </el-form-item>
          <el-form-item label="问题类型"
                        prop="type"
                        :rules="[
          { required: true, message: '请输入问题名称', trigger: 'blur' }]">

           <el-select v-model="editfrom.type" placeholder="请选择" @change="change" >
                <el-option v-for="(item,index) in items"  :key="index" :label="item.name" :value="item.code"></el-option>
            </el-select>
        </el-form-item>

         <el-form-item label="问题配图" prop="questionimg">

            <el-upload
              ref="upload"
              class="upload-demo"
              action="https://jsonplaceholder.typicode.com/posts/"
              :on-exceed="handleExceed"
              :limit="6"
              :on-remove="handleRemove2"
              :file-list="fileList1"
              :http-request="myUploadEdit"
              list-type="picture-card"
              :onError="uploadError"
              :onSuccess="uploadSuccess"
              :beforeUpload="beforeAvatarUpload"
              :on-preview="handlePictureCardPreview"
            >
                 <i class="el-icon-plus"></i>
            </el-upload>

           <el-dialog v-model="dialogVisible1" size="tiny">
             <img width="100%" :src="dialogImageUrl" alt="">
           </el-dialog>

        </el-form-item>
         <el-form-item label="图片名称"
                       prop="questionimgtitle" v-if="show">

         <el-input v-model="editfrom.questionimgtitle" :maxlength="50"  type="textarea" placeholder="请输入描述.."></el-input>
        </el-form-item>
         <el-form-item label="答案"
                       prop="answer">

           <el-input  v-model="editfrom.answer"
                      ref="myTextEditor"
                      type="textarea"
                      :rows="4"
                      :options="editorOption" @ready="onEditorReady($event)">
          </el-input>
        </el-form-item>
         <el-form-item label="答案配图"
                       prop="answerimg">

        <el-upload
          ref="upload"
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :file-list="fileList2"
          :http-request="myUploadEdit1"
          :on-remove="handleRemove3"
          :on-exceed="handleExceed"
          :limit="6"
          list-type="picture-card"
          :onError="uploadError"
          :onSuccess="uploadSuccess"
          :beforeUpload="beforeAvatarUpload"
          :on-preview="handlePictureCardPreview"
        >
             <i class="el-icon-plus"></i>

</el-upload>
           <el-dialog v-model="dialogVisible1" size="tiny">
             <img width="100%" :src="dialogImageUrl" alt="">
           </el-dialog>

        </el-form-item>
         <el-form-item label="图片名称"
                       prop="questionimgtitle" v-if="show">

         <el-input v-model="editfrom.answerimgtitle" :maxlength="50"  type="textarea" placeholder="请输入描述.."></el-input>
        </el-form-item>

      </el-form>
       </span>
      <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleEdit = false">取 消</el-button>
     <el-button type="primary"  @click="updateSubmit">确 定</el-button>
    </span>
    </el-dialog>



    <!--回答问题-->
    <el-dialog
      title="回答"
      :visible.sync="dialogVisibleAnswer"
      width="80%">
      <span>
       <el-form ref="answerfrom" :model="answerfrom"  label-width="80px">

         <el-form-item label="编号"
                       prop="code"
                       :rules="[
          { required: true, message: '请输入编号', trigger: 'blur' }]"
                      >
          <el-input v-model="answerfrom.code" :maxlength="50" placeholder="请输入编号.."  ></el-input>
        </el-form-item >

        <el-form-item label="问题"
                      prop="question">

          <el-input  v-model="answerfrom.question"
                         ref="myTextEditor"
                         type="textarea"
                         :rows="4"
                         :options="editorOption" @ready="onEditorReady($event)" >
          </el-input>
        </el-form-item>
          <el-form-item label="问题描述"
                        prop="description"
                       >
          <el-input v-model="answerfrom.description" :maxlength="50" placeholder="请输入问题问题描述" ></el-input>
<!--         <div style="color:lightgrey; margin-top: -5px;" >各个关键字之间以空格分割</div>-->
        </el-form-item>
          <el-form-item label="问题类型"
                        prop="type"
                        :rules="[
          { required: true, message: '请输入问题名称', trigger: 'blur' }]">

           <el-select v-model="answerfrom.type" placeholder="请选择" @change="change(val)"  >
                <el-option v-for="(item,index) in items"  :key="index" :label="item.name" :value="item.code" ></el-option>
            </el-select>
        </el-form-item>

         <el-form-item label="问题配图"
                       prop="questionimg">

        <el-upload
          ref="upload"
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :file-list="fileList4"
          :http-request="myUploadEdit3"
          :on-exceed="handleExceed"
          :limit="6"
          :on-remove="handleRemove4"
          list-type="picture-card"
          :onError="uploadError"
          :onSuccess="uploadSuccess"
          :beforeUpload="beforeAvatarUpload"
          :on-preview="handlePictureCardPreview"
        >
       <i class="el-icon-plus"></i>
</el-upload>


        </el-form-item>
         <el-form-item label="图片名称"
                       prop="questionimgtitle" v-if="show">

         <el-input v-model="answerfrom.questionimgtitle" :maxlength="50"  type="textarea" placeholder="请输入描述.."></el-input>
        </el-form-item>
         <el-form-item label="答案"
                       prop="answer">

           <el-input  v-model="answerfrom.answer"
                          ref="myTextEditor"
                          type="textarea"
                          :rows="4"
                          :options="editorOption" @ready="onEditorReady($event)">
          </el-input>
        </el-form-item>
         <el-form-item label="答案配图"
                       prop="answerimg">

        <el-upload
          ref="upload"
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :file-list="fileList5"
          :on-exceed="handleExceed"
          :limit="6"
          :on-remove="handleRemove5"
          :http-request="myUploadEdit2"
          list-type="picture-card"
          :onError="uploadError"
          :onSuccess="uploadSuccess"
          :beforeUpload="beforeAvatarUpload"
          :on-preview="handlePictureCardPreview"
        >
             <i class="el-icon-plus"></i>

</el-upload>
           <el-dialog v-model="dialogVisible1" size="tiny">
             <img width="100%" :src="dialogImageUrl" alt="">
           </el-dialog>

        </el-form-item>
         <el-form-item label="图片名称"
                       prop="questionimgtitle" v-if="show">

         <el-input v-model="answerfrom.answerimgtitle" :maxlength="50"  type="textarea" placeholder="请输入描述.."></el-input>
        </el-form-item>

      </el-form>
       </span>
      <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleAnswer = false">取 消</el-button>
     <el-button type="primary"  @click="updateSubmit1">确 定</el-button>
    </span>
    </el-dialog>

    <el-dialog :visible.sync="dialogVisible1"  >
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>

  </div>
</template>

<script>
  // import { getQuestionInfo,configList ,InsertQuestion,fileupload,batchdelQuestion,download,delQuestion,updateQuestion,getAskInfo,updateAnswer} from '../../api/api';
  import ElUpload from "element-ui/packages/upload/src/index";
  import { quillEditor } from 'vue-quill-editor'

  export default {
    components: {ElUpload,quillEditor},
    data() {
      return {
        files: [],
        asks:[],
        Searchfilter:{question:'',type:''},
        activeName:'first',
        show:false,
        fileList:[],
        fileList1:[],
        fileList2:[],
        fileList3:[],
        fileList4:[],
        fileList5:[],
        importFileUrl: 'E://',
        editFormVisible:false,
        listLoading: false,
        dialogVisible: false,
        dialogVisibleAnswer:false,

        dialogImageUrl:'',
        addLoading:false,
        dialogVisible1:false,
        dialogVisibleAdd:false,
        dialogVisibleEdit:false,
        chapterDialogVisible:false,
        items:[],
        sels: [],//列表选中列

        kind:'answerQuestion',
        editorOption: {
          modules: {
            toolbar: [
              ['bold', 'italic', 'underline', 'strike'],
              [{ 'header': 1 }, { 'header': 2 }],
              [{ 'list': 'ordered' }, { 'list': 'bullet' }],

              [{ 'indent': '-1' }, { 'indent': '+1' }],

              [{ 'size': ['small', false, 'large', 'huge'] }],

              [{ 'font': [] }],
              [{ 'color': [] }, { 'background': [] }],
              [{ 'align': [] }],
              ['clean']
            ]

          }
        },
        file:[],
        addquestionlist:[],
        addanswerlist:[],
        editquestionlist:[],
        editanswerlist:[],
        questionlist:[],
        answerlist:[],
        addfrom:{
          id:'',
          code:'',
          question:'',
          answer:'',
          type:'',
          description:'',
        },
        editfrom:{
          id:'',
          code:'',
          question:'',
          answer:'',
          description:'',
          type:''
        },
        answerfrom:{
          id:'',
          code:'',
          question:'',
          keywords:'',
          questionimg:'',
          questionimgtitle:'',
          answer:'',
          answerimg:'',
          answerimgtitle:'',
          type:''
        },

          page:{
              beginIndex: "",
              endIndex: "",
              pageIndex: 1,
              pageSize: 10,
              totalNum: 0,
              totalPage: 0
          },
          page1:{
              beginIndex: "",
              endIndex: "",
              pageIndex: 1,
              pageSize: 10,
              totalNum: 0,
              totalPage: 0
          },

        listQuery: {
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
          searchData:{question:undefined,type:''}
        },
        listQuery1: {
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
          searchData:{question:undefined,type:''}
        }

      }
    },
    methods: {
      //上传文件
      myUpload(content){
          if(this.addfrom.type == ''){
              this.$message({
                  message: '请先选择文件类型',
                  type: 'failed'
              });
              return false
          }

        var fd = new FormData();
        fd.append('file', content.file);

        this.addfrom.questionimgtitle=content.file.name;

        console.log(content.file);

          this.$nextTick(async () => {
              let params = {param: fd, url: '/api/file/fileupload'};
              await this.$store.dispatch('http/fileUpload', params)
                  .then((res) => {
                  console.log(res)
                  //NProgress.done();
                  if(res!=null){
                    this.addquestionlist.push({url:res,name:content.file.name});
                    console.log(this.addfrom.type)
                    this.file.push({
                    fileName:content.file.name,
                    fileUrl:res,
                    fileType:this.addfrom.type,
                    flag: 'Q',
                      })
                    console.log("f",this.file);
                      //  this.$refs.upload.clearFiles();
                      this.$message({
                          message: '上传成功',
                          type: 'success'
                      });
                  }else{
                      this.$message({
                          message: '上传失败',
                          type: 'failed'
                      });
                  }


              })

          });

        // fileupload(fd).


      },

      myUploadEdit(content){
        var fd = new FormData();
        fd.append('file', content.file);
        this.editfrom.questionimgtitle=content.file.name;
        console.log(content.file);

          this.$nextTick(async () => {
              let params = {param: fd, url: '/api/file/fileupload'};
              await this.$store.dispatch('http/fileUpload', params)
              // fileupload(fd)
                  .then((res) => {
                      var data=res;
                      console.log(data);
                      //NProgress.done();
                      if(data!=null){

                        this.editquestionlist.push({url:data.data,name:content.file.name});
                        this.file.push({
                          fileName:content.file.name,
                          fileUrl:data,
                          fileType:this.addfrom.type,
                          flag: 'Q',
                        })
                          console.log(this.file)
                          //  this.$refs.upload.clearFiles();
                          this.$message({
                              message: '上传成功',
                              type: 'success'
                          });
                      }
                      else{
                          this.$message({
                              message: '上传失败',
                              type: 'failed'
                          });
                      }


                  })

          })

      },


      onEditorReady(editor) {
      },
      handlePictureCardPreview(file){
        this.dialogImageUrl=file.url;

        this.dialogVisible1=true;
      },
      myUpload1(content){
        var fd = new FormData();
        fd.append('file', content.file);
        this.addfrom.answerimgtitle=content.file.name;

          this.$nextTick(async () => {
              let params = {param: fd, url: '/api/file/fileupload'};
              await this.$store.dispatch('http/fileUpload', params)

              // fileupload(fd)
                  .then((res) => {
                    console.log(res)
                      var data=res;
                      //NProgress.done();
                      if(data!=null){

                        this.addanswerlist.push({url:data.data,name:content.file.name});
                        this.file.push({
                          fileName:content.file.name,
                          fileUrl:data,
                          fileType:this.addfrom.type,
                          flag: 'A',
                        });
                        console.log(this.file)

                          //  this.$refs.upload1.clearFiles();
                          this.$message({
                              message: '上传成功',
                              type: 'success'
                          });
                      }
                      else{
                          this.$message({
                              message: '上传失败',
                              type: 'failed'
                          });
                      }


                  })


          })

      },
      handleRemove(file) {
        //this.addquestionlist.pop(file.name);
        var index= this.file.map(item => item.name).indexOf(file.name);
        this.file.splice(index,1)
          console.log(this.file)
      },
      handleRemove1(file) {

        var index= this.file.map(item => item.name).indexOf(file.name);
        this.file.splice(index,1);
          console.log(this.file)
      },
      handleRemove2(file) {
        var index= this.file.map(item => item.name).indexOf(file.name);
        this.file.splice(index,1);
          console.log(this.file)
      },
      handleRemove3(file) {
        var index= this.file.map(item => item.name).indexOf(file.name);
        this.file.splice(index,1);
          console.log(this.file)
      },
      handleRemove4(file) {
        var index= this.file.map(item => item.name).indexOf(file.name);
        this.file.splice(index,1);
          console.log(this.file)
      },
      handleRemove5(file) {
        var index= this.file.map(item => item.name).indexOf(file.name);
        this.file.splice(index,1);
          console.log(this.file)
      },


      handleExceed(file,fileList){
        if(fileList.length>=6){
          alert("最大上传数量不能超过6个");
          return -1;
        }

      },


      myUploadEdit1(content){
        var fd = new FormData();
        fd.append('file', content.file);

        this.editfrom.answerimgtitle=content.file.name;


          this.$nextTick(async () => {
              let params = {param: fd, url: '/api/file/fileupload'};
              await this.$store.dispatch('http/fileUpload', params)
              // fileupload(fd)
                  .then((res) => {
                      var data=res;
                      //NProgress.done();
                      if(data!=null){


                          this.editanswerlist.push({url:data.data,name:content.file.name});
                        this.file.push({
                          fileName:content.file.name,
                          fileUrl:data,
                          fileType:this.addfrom.type,
                          flag: 'A',
                        })

                          //  this.$refs.upload1.clearFiles();
                          this.$message({
                              message: '上传成功',
                              type: 'success'
                          });
                      }
                      else{
                          this.$message({
                              message: '上传失败',
                              type: 'failed'
                          });
                      }


                  })

          })

      },


      myUploadEdit3(content){


        var fd = new FormData();
        fd.append('file', content.file);
        this.answerfrom.questionimgtitle=content.file.name;

          this.$nextTick(async () => {
              let params = {param: fd, url: '/api/file/fileupload'};
              await this.$store.dispatch('http/fileUpload', params)

              // fileupload(fd)
                  .then((res) => {
                      var data = res;
                      //NProgress.done();
                      if (data!=null) {

                          this.questionlist.push({url: data.data, name: content.file.name});
                        this.file.push({
                          fileName:content.file.name,
                          fileUrl:data,
                          fileType:this.addfrom.type,
                          flag: 'Q',
                        })
                          //  this.$refs.upload1.clearFiles();
                          this.$message({
                              message: '上传成功',
                              type: 'success'
                          });
                      } else {
                          this.$message({
                              message: '上传失败',
                              type: 'failed'
                          });
                      }


                  })
          })
      },
      myUploadEdit2(content){
        var fd = new FormData();
        fd.append('file', content.file);
        this.answerfrom.answerimgtitle=content.file.name;


          this.$nextTick(async () => {
              let params = {param: fd, url: '/api/file/fileupload'};
              await this.$store.dispatch('http/fileUpload', params)
              // fileupload(fd)
                  .then((res) => {
                      var data = res;
                      //NProgress.done();
                      if (data!=null) {

                          this.answerlist.push({url: data.data, name: content.file.name});
                        this.file.push({
                          fileName:content.file.name,
                          fileUrl:data,
                          fileType:this.addfrom.type,
                          flag: 'A',
                        });

                          this.$message({
                              message: '上传成功',
                              type: 'success'
                          });
                      } else {
                          this.$message({
                              message: '上传失败',
                              type: 'failed'
                          });
                      }


                  })
          })
      },


      // 上传成功后的回调
      uploadSuccess (response, file, fileList) {
        console.log('上传文件', response)
      },

      handleSizeChange(val) {
        this.listQuery.pageSize = val
        this.page.pageSize = val
        this.getFiles();
      },

      // 上传错误
      uploadError (response, file, fileList) {
        console.log('上传失败，请重试！')
      },
      // 上传前对文件的大小的判断
      beforeAvatarUpload (file) {

        const extension = file.name.split('.')[1] === 'jpg'||file.name.split('.')[1] === 'JPG'||file.name.split('.')[1] === 'jpeg'||file.name.split('.')[1] === 'JPEG'
        const extension2 = file.name.split('.')[1] === 'png'||file.name.split('.')[1] === 'PNG'
        const extension3 = file.name.split('.')[1] === 'gif'
        const extension4 = file.name.split('.')[1] === 'pdf'
        const extension5 = file.name.split('.')[1].indexOf('doc')!=-1

        const isLt2M = file.size / 1024 / 1024 < 10
        if (!extension && !extension2 && !extension3 && !extension4 && !extension5) {

          this.$alert('上传模板只能是 jpg、png、gif、jpeg、pdf、word格式!', '上传提示', {
            confirmButtonText: '确定',
            callback: action => {
              this.$refs.upload.clearFiles();
              return -1 ;
            }
          });
        }
        if (!isLt2M) {

          this.$alert('上传模板大小不能超过 10MB!', '上传提示', {
            confirmButtonText: '确定',
            callback: action => {
              this.$refs.upload.clearFiles();
              return -1 ;
            }
          });
        }
        return extension || extension2 || extension3 ||extension4 || extension5&& isLt2M
      },


      selsChange: function (sels) {
        console.log("111",sels);
        this.sels = sels;
      },
        selsChange1: function (sels) {
        console.log("22222",sels);
        this.sels = sels;
      },
      change:function(val){
        // this.getfiletype();
          this.addfrom.type = val
          console.log(this.addfrom.type)
      },
      change1:function(){
        this.getFiles();
        this.getAskFiles();
      },

      AddList(){
        for(var name in this.$data.addfrom) {
          this.$data.addfrom[name] = ""

        }
        this.file = [];
        this.fileList=[];
        this.fileList1=[];
        this.fileList2=[];
        this.addquestionlist=[];
        this.addanswerlist=[];
      },

      //已回复
      resolve: function (data) {
          console.log('已回复列表',data);
          if(data.dataList.length==0){
            this.files=[]
          }else{
            this.files = data.dataList;
        }

        this.listQuery.currentPage =data.page.totalPage;
        this.listQuery.totalItems =data.page.totalNum;
        this.listQuery.total = data.page.totalNum;
        this.page.pageIndex = data.page.pageIndex;
        this.page.totalPage =data.page.totalPage;
        this.page.totalNum =data.page.totalNum;
      },
      //待回复
      resolve1: function (data) {
        console.log("待回复列表：",data.dataList);
        if(data.dataList.length==0){
          this.asks = []
        }else{
          this.asks=data.dataList;
        }

        this.listQuery1.currentPage =data.page.totalPage;
        this.listQuery1.totalItems =data.page.totalNum;
        this.listQuery1.total = data.page.totalNum;
        this.page1.pageIndex = data.page.pageIndex;
        this.page1.totalPage =data.page.totalPage;
        this.page1.totalNum =data.page.totalNum;

      },
      sort(val){
        console.log(val)
        this.order(val.order);
        al.prop=="question"

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


      handleCurrentChange(val) {
        // alert(val);


          this.listQuery.currentPage = val

          this.listQuery.currentItem = (this.listQuery.currentPage-1) *  this.listQuery.pageSize+1;

          this.page.pageIndex = val;

          this.getFiles();

      },

      handleCurrentChange1(val) {
        // alert(val);


        this.listQuery1.currentPage = val

        this.listQuery1.currentItem = (this.listQuery1.currentPage-1) *  this.listQuery1.pageSize+1;

        this.page1.pageIndex = val;

        this.getAskFiles();

      },



      //搜索
      Search(){


        this.getFiles();
        this.getAskFiles();

      },


      //新增
      addSubmit: function () {
        //
        // let strquestion=[];
        // let stranswer=[];
        //
        // for(let i=0;i<this.addquestionlist.length;i++){
        //   strquestion.push(this.addquestionlist[i].url);
        // }
        //
        // for(let i=0;i<this.addanswerlist.length;i++){
        //   stranswer.push(this.addanswerlist[i].url);
        // }
        //
        // this.addfrom.questionimg= strquestion.join("$");
        // this.addfrom.answerimg=stranswer.join("$");

        this.$refs.addfrom.validate((valid) => {
          if (valid) {
            this.$confirm('确认是否发布到call浦端？', '提示', {}).then(() => {
              this.addLoading = true;
              //NProgress.start();

                let param = {
                  addFrom:this.addfrom,
                  file:this.file,
                            }
                let params = {
                    className: 'QuestionAnswer',
                    method: 'addQuestion',
                    param: param
                };
                console.log("提交时的list",params)
                this.$nextTick(async () => {await this.$store.dispatch('http/post', params)

                    // InsertQuestion(this.addfrom)
                        .then((res) => {
                        this.addLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '发布成功',
                            type: 'success'
                        });
                        this.AddList();
                        this.dialogVisibleAdd = false;

                        this.getFiles();
                        this.getAskFiles();
                    });


                });


            });
          }
        });
      },

      //修改
      updateSubmit: function () {

        // let strquestion=[];
        // let stranswer=[];
        //
        // for(let i=0;i<this.editquestionlist.length;i++){
        //   strquestion.push(this.editquestionlist[i].url);
        // }
        //
        // for(let i=0;i<this.editanswerlist.length;i++){
        //   stranswer.push(this.editanswerlist[i].url);
        // }
        // this.editfrom.questionimg=strquestion.join("$");
        // this.editfrom.answerimg=stranswer.join("$");

        this.$refs.editfrom.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.addLoading = true;
              //NProgress.start();
                console.log(this.editfrom)

                let param ={
                  editfrom:this.editfrom,
                  file:this.file,
                  id:this.editfrom.id
                }
                let params = {
                    className: 'QuestionAnswer',
                    method: 'updateQuestion',
                    param: param
                };
                this.$nextTick(async () => {await this.$store.dispatch('http/post', params)


                    // updateQuestion(this.editfrom)
                        .then((res) => {
                        this.addLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '提交成功',
                            type: 'success'
                        });
                        this.AddList();
                        this.dialogVisibleEdit = false;

                        this.getFiles();
                    });

                });

            });
          }
        });
      },

      //回答
      updateSubmit1: function () {

        let strquestion=[];
        let stranswer=[];

        for(let i=0;i<this.questionlist.length;i++){
          strquestion.push(this.questionlist[i].url);
        }

        for(let i=0;i<this.answerlist.length;i++){
          stranswer.push(this.answerlist[i].url);
        }



        this.answerfrom.questionimg=strquestion.join("$");
        this.answerfrom.answerimg=stranswer.join("$");






        this.$refs.answerfrom.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.addLoading = true;
              //NProgress.start();
                let param = {
                    id:this.answerfrom.id,
                    editfrom:this.answerfrom,
                    file:this.file
                };

                // let param = this.answerfrom;
                let params = {
                    className: 'QuestionAnswer',
                    method: 'updateAnswer',
                    param: param
                };
                this.$nextTick(async () => {await this.$store.dispatch('http/post', params)


                    // updateAnswer(this.answerfrom)
                        .then((res) => {
                        this.addLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '提交成功',
                            type: 'success'
                        });
                        this.AddList();
                        this.dialogVisibleAnswer = false;
                        setTimeout(this.getFiles(),1000)
                        this.getAskFiles();
                    });

                });


            });
          }
        });
      },
      // //获取用户列表
      // getFiles() {
      //   this.listQuery.searchData.question=this.Searchfilter.question;
      //   this.listQuery.searchData.type=this.Searchfilter.type;
      //   this.listLoading = true;
      //   //NProgress.start();
      //   console.log(this.listQuery)
      //   getQuestionInfo(this.listQuery).then((res) => {
      //     this.total ="";
      //     this.resolve(res.data);
      //
      //     this.listLoading = false;
      //     //NProgress.done();
      //   });
      //
      //
      // },

      //获取用户列表
      getFiles() {
          this.listQuery.searchData.question=this.Searchfilter.question;
          this.listQuery.searchData.type=this.Searchfilter.type;
          this.listLoading = true;
          //NProgress.start();
          // console.log("查询条件",this.listQuery);
          let param = {
            searchData:{
              searchName:this.Searchfilter.question,
              type:this.Searchfilter.type,
              flag:'1'
                    },
            page:{
              pageIndex:this.page.pageIndex,
              pageSize:this.page.pageSize
                }
                    };
          let params = {
              className: 'QuestionAnswer',
              method: 'getQuestionInfo',
              param: param
          };
          this.$nextTick(async () => {await this.$store.dispatch('http/post', params)
          // getQuestionInfo()
              .then((res) => {
                  if(res != undefined){
                    console.log("返回参数",res)
                    this.total ="";

                    this.resolve(res);

                    this.listLoading = false;
                    //NProgress.done();
                  }else {
                      this.listLoading = false;
                      this.files = [];
                  }

              });

          });
      },

      //获取用户列表
      getAskFiles() {
        this.listQuery1.searchData.question=this.Searchfilter.question;
        this.listQuery1.searchData.type=this.Searchfilter.type;
        this.listLoading = true;


          // let param = this.listQuery
          let param = {
              searchData:{
                  searchName:this.Searchfilter.question,
                  type:this.Searchfilter.type,
                  flag:"0"
              },
              page:{
                  pageIndex:this.page.pageIndex,
                  pageSize:this.page.pageSize
              }
          };
          let params = {
              className: 'QuestionAnswer',
              method: 'getQuestionInfo',
              param: param
          };
          this.$nextTick(async () => {
              await this.$store.dispatch('http/post', params)

              // getAskInfo(this.listQuery)
                  .then((res) => {
                      if(res != undefined){
                          console.log("待回复返回参数",res)
                          this.total = "";
                          this.resolve1(res);
                          this.listLoading = false;
                          //NProgress.done();
                      }else {
                          this.listLoading = false;
                          this.asks = []
                      }



                  });
          });
      },

      getfiletype(){
        let param = this.kind
        let params = {
            className: 'QuestionAnswer',
            method: 'getDictionary',
            param: {
                kind:param
          }
        };
        this.$nextTick(async () => {await this.$store.dispatch('http/post', params)
        // configList(this.kind)
            .then((res) => {

          console.log("res：",res)
          this.items=res;
          console.log(this.items);


          //NProgress.done();
        });
        });
      },

      //update
      handleEdit: function (index, row) {
        this.dialogVisibleEdit = true;

        this.fileList2=[];

        this.fileList1=[];

        this.editanswerlist=[];
        this.editquestionlist=[];

        this.file = [];

        console.log(row)
        // let imgs=row.questionimg!=null&& row.questionimg!=""?row.questionimg.split('$'):new Array();
        let imgs = row.questionImgList;
        imgs.forEach((item)=> {

          row.type = item.fileType;
          this.file.push({
              fileName:item.name,
              fileUrl:item.fileUrl,
              fileType:item.fileType,
              flag: 'Q',
          });

            this.$nextTick(async () => {
                let params = {param:item.fileUrl,url:'/api/file/downloadfile'};
                await this.$store.dispatch('http/fileDownload', params)
                    .then((res) => {
                    console.log(res);

                    let path = "data:image/png;base64," + res;
                    this.fileList1.push({url: path,name:item});
                })

            })

          // download(item).
        })

            // let imgs1=row.answerimg!=null&& row.answerimg!=""?row.answerimg.split('$'):new Array();

            let imgs1 = row.answerImgList;
            imgs1.forEach((item)=>{
              this.file.push({
                  fileName:item.name,
                  fileUrl:item.fileUrl,
                  fileType:item.fileType,
                  flag: 'A',
              });

            this.$nextTick(async () => {
                let params = {param: item.fileUrl, url: '/api/file/downloadfile'};
                await this.$store.dispatch('http/fileDownload', params)

                // download(item)
                    .then((res) => {
                        console.log(res);

                        let path = "data:image/png;base64," + res;
                        this.fileList2.push({url: path, name: item});

                    });
            })

            });


        this.editfrom = Object.assign({}, row);



      },


      handleEdit1: function (index, row) {
        this.dialogVisibleAnswer = true;

        this.fileList4=[];

        this.fileList5=[];

        this.questionlist=[];
        this.answerlist=[];
        this.file = [];

         // let imgs=row.questionimg!=null&& row.questionimg!=""?row.questionimg.split("$"):new Array();


          let imgs = row.questionImgList;
          imgs.forEach((item)=>{
              console.log(item)
              row.type = item.fileType;
                  this.file.push({
                      fileName:item.name,
                      fileUrl:item.fileUrl,
                      fileType:item.fileType,
                      flag: 'Q',
                  });


           this.questionlist.push({url:item,name:item});


           // download(item)

                 this.$nextTick(async () => {
                     let params = {param: item.fileUrl, url: '/api/file/downloadfile'};
                     await this.$store.dispatch('http/fileDownload', params)

                         .then((res) => {
                             console.log(res);

                             let path = "data:image/png;base64," + res;

                             this.fileList4.push({url: path, name: item});

                         })
                 })
         })


        // let imgs1=row.answerimg.split("$");
        //
        // imgs1.forEach((item)=> {
        //
        //   download(item).then((res)=>{
        //     console.log(res.data);
        //
        //     let path="data:image/png;base64,"+res.data;
        //     this.fileList5.push({url:path});
        //
        //   })
        // });
        //
        // alert(JSON.stringify(fileList5));

        this.answerfrom = Object.assign({}, row);



      },


      //批量删除
      batchRemove: function () {
        var ids = this.sels.map(item => item.id).toString();
        console.log(ids)
        this.$confirm('确认删除选中记录吗？', '提示', {
          type: 'warning'
        }).then(() => {
          this.listLoading = true;
          //NProgress.start();
            let para =  ids ;
            let params = {
                className: 'QuestionAnswer',
                method: 'batchDelQuestion',
                param: {
                    files:para
                }
            };
            this.$nextTick(async () => {await this.$store.dispatch('http/post', params)
            // batchdelQuestion(para)
                .then((res) => {
                    this.listLoading = false;
                    //NProgress.done();
                    this.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    this.getFiles();
                    this.getAskFiles();
                });

            });

        }).catch(() => {

        });
      },

      //删除
      handleDel: function (index, row) {
        this.$confirm('确认删除该记录吗?', '提示', {
          type: 'warning'
        }).then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = row.id;

            let params = {
                className: 'QuestionAnswer',
                method: 'delQuest',
                param: {
                    id:para
                }
            };
            this.$nextTick(async () => {await this.$store.dispatch('http/post', params)


                // delQuestion(para)
                    .then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getFiles();
                        this.getAskFiles();
                    });

            });

        }).catch(() => {

        });
      },

      //导出
      exportData(){
        console.log(this.activeName)
        let flag;
        if(this.activeName == "first"){
          flag = 1;
        }else{
          flag = 0;
        }
        let param = {
            searchName:this.Searchfilter.question,
            type:this.Searchfilter.type,
            flag:flag
        };
        let params = {
          className: 'QuestionAnswer',
          method: 'getQuestionData',
          param: param
        };
        this.$nextTick(async () => {await this.$store.dispatch('http/post', params)
          .then((res) => {
            console.log(res)
            this.exportdata2(res)
          });
        });
      },
      getbase64(s){
        return window.btoa(unescape(encodeURIComponent(s)))
      },

      exportdata2(data){
        let tempArr = [];
        for(var o in data){
          var obj = {
            question:data[o].question,
            description:data[o].description,
            typename:data[o].typename,
            answer:data[o].answer,
          }
          tempArr.push(obj)
        }
        console.log("开始导出")
        //列标题
        let str = '<tr><td>问题</td><td>问题描述</td><td>问题类型</td><td>答案</td></tr>';
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
        link.download = "智能答疑.xls";
        link.href = uri + this.getbase64(template);
        link.click();
      },

    },




    mounted:function () {

      let self = this;
      this.getfiletype();


      this.$nextTick(async function () {

        //const data = {totlePages: 0, pageSize: 10}
        await self.getFiles();

        self.getAskFiles();

      }.bind(this));



    }
  }
</script>

<style  lang="scss" scoped>





  .ql-snow .ql-editor img {
    max-width: 200px;
  }
  .ql-toolbar{
    min-height: 50px;
  }
  .quill-editor editor-container{
    min-height: 300px;
  }




</style>
