<template>
  <div>
    <el-container>
      <!--<el-header class="header">-->
        <!--<el-row class="headerBtn" >-->
          <!--<i class="el-icon-search" ></i>-->
          <!--<el-select v-model="Feed" placeholder="反馈状态" size="mini" @change="ChangeFeed">-->
            <!--<el-option-->
              <!--v-for="item in FeedbackSubmission"-->
              <!--:key="item.value"-->
              <!--:label="item.label"-->
              <!--:value="item.value">-->
            <!--</el-option>-->
          <!--</el-select>-->
          <!--<el-select v-model="Read" placeholder="已阅状态" size="mini" @change="ChangeRead">-->
            <!--<el-option-->
              <!--v-for="item in ReadSubmission"-->
              <!--:key="item.value"-->
              <!--:label="item.label"-->
              <!--:value="item.value">-->
            <!--</el-option>-->
          <!--</el-select>-->
          <!--<span>日期</span>-->
          <!--<el-date-picker-->
            <!--v-model="value1"-->
            <!--type="daterange"-->
            <!--range-separator="至"-->
            <!--start-placeholder="开始日期"-->
            <!--end-placeholder="结束日期"-->
            <!--size="mini"-->
            <!--@change="onPick">-->
          <!--</el-date-picker>-->
        <!--</el-row>-->

      <!--</el-header>-->
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
            prop="taskStartTime"
            label="开始时间"
            min-width="25%">
          </el-table-column>
          <el-table-column
            prop="taskEndTime"
            label="截止时间"
            min-width="25%">
          </el-table-column>
          <el-table-column
            prop="status"
            label="完成情况"
            min-width="25%">
          </el-table-column>
          <el-table-column
            label="操作"
            min-width="25%">
            <template slot-scope="scope">
              <el-button type="primary" icon="el-icon-share" size="mini" @click.native="getInfo(scope.$index)">查看</el-button>
            </template>
              <el-dialog title="消息详情"
                         :visible.sync="show.infoTableVisible"
                         :modal-append-to-body="false"
                         :append-to-body="true"
                         :before-close="handleClose"
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
                  <el-form-item label="完成情况">
                    <el-tabs type="border-card" @tab-click="handleClick">
                      <el-tab-pane label="已完成">
                        <div v-show="noData"></div>
                        <el-row v-for="item in departmentDataFinished" :key="item.name">
                          <el-col :span="3">
                            <div>部门名称：</div>
                          </el-col>
                          <el-col :span="4">
                            <el-tag>{{item.name}}</el-tag>
                          </el-col>
                          <el-col :span="3">
                            <div>完成时间：</div>
                          </el-col>
                          <el-col :span="8">
                            <el-tag>{{item.modifytime}}</el-tag>
                          </el-col>
                        </el-row>
                      </el-tab-pane>
                      <el-tab-pane label="未完成">
                        <el-row v-for="item in departmentDataUnFinished" :key="item.name">
                          <el-col :span="3">
                            <div>部门名称：</div>
                          </el-col>
                          <el-col :span="4">
                            <el-tag>{{item.name}}</el-tag>
                          </el-col>
                        </el-row>
                      </el-tab-pane>
                    </el-tabs>
                  </el-form-item>
                </el-form>

                <!--<el-table :data="infoData">-->
                  <!--<el-table-column property="title" label="消息主题"></el-table-column>-->
                  <!--<el-table-column property="content" label="消息详情"></el-table-column>-->
                  <!--<el-table-column property="isFeedback" label="是否反馈"></el-table-column>-->
                <!--</el-table>-->
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
        name: "TaskList",
        input:'',
        show:{
          infoTableVisible: false,
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
        departmentDataFinished:{},
        departmentDataUnFinished:{},
        tab:{index:0},
        noData:false,
      }
    },
    created(){
      this.getList();
    },
    filters:{
      numFilter(value) {
        //截取当前数据到小数点后两位
        let realVal=parseFloat(value).toFixed(2)
        return realVal
      },
    },
    methods: {
      //获取列表
      getList(){
        this.tableLoading = true;
        this.$nextTick(async () => {
          let params = {
            className: 'Task',
            method: 'selectTask',
            param:{
              searchData:{
                type:"Task"
              },
              page:this.page
            }
          };
          let res = await this.$store.dispatch('http/post', params);
          console.log(res);
          this.listData = res.dataList;
          this.totalNum=Number(res.page.totalNum);
          this.tableLoading = false;
        })
      },
      handleClick(tab) {
        console.log(tab.index);
        if(tab.index == 0){
          //获取已完成部门
          this.$nextTick(async () => {
            let params = {
              className: "Task",
              method: "selectFinished",
              param: {
                id: this.taskId
              }
            };
           let res = await this.$store.dispatch('http/post', params);
           this.departmentDataFinished = res;
            console.log(this.departmentDataFinished)
          })
        }else
          //获取未完成部门
          {
          this.$nextTick(async () => {
            let params = {
              className: "Task",
              method: "selectUnfinished",
              param: {
                id: this.taskId
              }
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
      ChangeRead(val){
        console.log('Read:',val)
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
        this.dialogLoading = true;
        this.show.infoTableVisible = true;
        this.taskId = this.listData[index].id;
        this.infoData = {};


        this.$nextTick(async ()=>{
          let params = {
            className:"Task",
            method: "selectTaskDetails",
            param:{
              id:this.listData[index].id,
              departmentId:this.departmentId,
            }
          };
          let res = await this.$store.dispatch('http/post', params);
          this.infoData = res[0];
          console.log(this.infoData)
          this.handleClick(this.tab);
          this.dialogLoading = false;
        });
      },
      //反馈窗口弹框
      feedback(index){
        console.log(index)
      },
        handleClose(done) {
                    this.departmentDataFinished = {};
                    this.departmentDataUnFinished = {};
                    done();
        }
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

  .aaa {
    float: right;
  }

</style>
