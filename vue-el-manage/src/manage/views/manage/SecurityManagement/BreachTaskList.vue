<template>
  <div>
    <el-container>
      <el-header class="header">
      <el-row class="headerBtn" >
      <i class="el-icon-search" ></i>
        <el-cascader
          class="prop"
          :props="defaultParams"
          v-model="value"
          :options="taskKind"
          @change="handleNodeClick"
          size="mini"
          placeholder="请选择模块"
          clearable
        >
        </el-cascader>
      <!--</el-select>-->
      <el-select clearable v-model="Read" placeholder="请选择支行" size="mini" @change="ChangeDepartment">
      <el-option
      v-for="item in departmentList"
      :key="item.id"
      :label="item.name"
      :value="item.id">
      </el-option>
      </el-select>
        <el-button type="primary" size="mini" @click="getList()">搜索</el-button>
      </el-row>

      </el-header>
      <el-main>
        <el-table
          v-loading="tableLoading"
          :data="listData"
          style="width: 100%">
          <el-table-column
            prop="title"
            label="任务主题"
            min-width="25%">
          </el-table-column>
          <el-table-column
            prop="departmentName"
            label="上传部门"
            min-width="25%">
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="提交时间"
            min-width="25%">
          </el-table-column>
          <el-table-column
            label="操作"
            min-width="25%">
            <template slot-scope="scope">
              <el-button type="primary" icon="el-icon-share" size="mini" @click.native="getInfo(scope.$index)">查看</el-button>
            </template>
              <el-dialog title="任务详情"
                         :visible.sync="show.infoTableVisible"
                         :modal-append-to-body="false"
                         :append-to-body="true"
                         @close='closeDialog'
                         v-loading="dialogLoading"
              >
                <el-form ref="form" :model="infoData" label-width="80px">
                  <el-form-item label="任务主题">
                    <el-input v-model="infoData.title" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="任务内容">
                    <el-input v-model="infoData.content" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="任务类型">
                    <el-input v-model="infoData.taskTypeName" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="截止时间">
                    <el-input v-model="infoData.taskEndTime" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="反馈内容">
                      <el-input v-model="feedbackObject.content" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="图片">
                    <el-button type="primary" size="mini" @click="showpic()">点击查看</el-button>

                    <el-dialog title="图片详情"
                               :visible.sync="show.pic"
                               :modal-append-to-body="false"
                               :append-to-body="true">

                    <div v-for="item in taskimg">
                      <img style="width: 100%;height: 100%;" :src="item.fileUrl" alt="暂无上传的图片或图片加载失败">
                    </div>
                    </el-dialog>
                  </el-form-item>
                </el-form>
              </el-dialog>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer class="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = currentPage
          :page-sizes="[10, 20, 30, 40]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total=totalNum>
        </el-pagination>
      </el-footer>
    </el-container>
  </div>
</template>
<script>

  export default {
    data(){
      return{
        dialogLoading:false,
        tableLoading:false,
        name: "BreachTaskList",
        input:'',
        show:{
          infoTableVisible: false,
          pic:false
        },
        currentPage: 0,
        value1:'',
        FeedbackSubmission: [{
          value: '0',
          label: '已反馈'
        }, {
          value: '1',
          label: '未反馈'
        }],
        ReadSubmission: [{
          value: '0',
          label: '已读'
        }, {
          value: '1',
          label: '未读'
        }],
        Feed:'',
        Read:'',
        listData: [],
        totalNum:0,
        page:{
          pageIndex:1,
          pageSize:10,
          totalPage:0,
        },
        infoData:{},
        feedbackObject:{
          content:''
        },
        departmentDataFinished:{},
        departmentDataUnFinished:{},
        tab:{index:0},
        noData:false,
        departmentList:[],
        taskKind:[],
        value: [],
        defaultParams: {
          children: 'typeList',
          label: 'name',
          value: 'id',
        },
        departmentId:'',
        fileType:'',
        taskimg:{},
      }
    },
    created(){
      this.getList();
      this.departmentlist();
      this.getFileTypeList();
    },
    filters:{
      numFilter(value) {
        //截取当前数据到小数点后两位
        let realVal = parseFloat(value).toFixed(2)
        return realVal
      },
    },
    methods: {
      //获取机构下拉框
      departmentlist(){
        this.$nextTick(async () => {
          //获取执行机构下拉框
          let selectDepartment = {
            className: 'Task',
            method: 'selectDepartment',
            param:{}
          };
          let res = await this.$store.dispatch('http/post', selectDepartment);
          console.log(res);
          this.departmentList = res;
        })
      },
      //获取文件类型下拉框
      getFileTypeList(){
        this.$nextTick(async () =>{
        let selectTaskType = {
          className:'Task',
          method: 'selectFileType',
          param:{kind:'fileType'}
        };
        this.taskKind = await this.$store.dispatch('http/post', selectTaskType);
        console.log("机构的下拉书",this.taskKind);})
      },
      //获取列表
      getList(){
        this.tableLoading = true;
        this.$nextTick(async () => {
          let params = {
            className: 'Task',
            method: 'selectTypeTask',
            param:{searchData:{departmentId:this.departmentId, fileType:this.fileType}, page:this.page}
          };
          let res = await this.$store.dispatch('http/post', params);
          console.log(res);
          this.listData = res.dataList;
          this.totalNum=Number(res.page.totalNum);
          this.tableLoading = false;
        })
      },
      //选中模块下拉框
      handleNodeClick(value) {
        this.fileType = value[1];
        console.log('所选模块id',this.fileType)
      },
      //选中机构下拉框
      ChangeDepartment(val){
        this.departmentId = val;
        console.log('所选机构id',this.departmentId)
      },
      handleClick(tab) {
        console.log(tab.index);
        if(tab.index == 0)
        //获取已完成部门
        {this.$nextTick(async () => {
            let params = {
              className: "Task",
              method: "selectFinished",
              param: {id: this.taskId}
            };
            let res = await this.$store.dispatch('http/post', params);
            this.departmentDataFinished = res;
            console.log(this.departmentDataFinished)
          })
        }else
        //获取未完成部门
        {this.$nextTick(async () => {
            let params = {
              className: "Task",
              method: "selectUnfinished",
              param: {id: this.taskId}
            };
            let res = await this.$store.dispatch('http/post', params);
            this.departmentDataUnFinished = res;
            console.log(this.departmentDataUnFinished)
          });
        }
      },
      //获取时间
      onPick(val){
        console.log('选中日期',val)
      },
      ChangeFeed(val){
        console.log('Feed:',val)
      },
      handleChange(value) {
        console.log(value);
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.page.pageSize = val;
        this.getList();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.page.pageIndex = val;
        this.getList();
      },
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      //获取消息详情
      getInfo(index){
        this.show.infoTableVisible = true;
        this.taskId = this.listData[index].id;
        this.dialogLoading = true;
        this.$nextTick(async () => {
            let params = {
              className: "Task",
              method: "selectTaskDetails",
              param: {id:this.listData[index].id, departmentId:this.departmentId}
            };
            let res = await this.$store.dispatch('http/post', params);
            this.infoData=res[0];
            console.log("res",this.feedbackObject.content);
            if(res[0].feedbackObject){
              this.taskimg = res[0].feedbackObject.fileUrlList;
              this.feedbackObject.content=res[0].feedbackObject.content
            }else{
              res[0].feedbackObject=[];
              this.feedbackObject.content='选择支行后才能查看反馈内容';
              res[0].feedbackObject.fileUrlList={0:''}
            }
            this.dialogLoading = false;
      })
      },
      //反馈窗口弹框
      showpic(){
        this.show.pic = true;
      },
      //弹窗关闭销毁数据
      closeDialog(){
        this.infoData = {};
        this.feedbackObject.content = '';
      },
    }

  }
</script>
<style scope>
  .header {
    background-color: rgb(240, 244, 245);
    text-align: left;
    /*display: flex;*/
  }

  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
  }
</style>
