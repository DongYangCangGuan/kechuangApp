<template>
  <div>
    <el-container>
      <el-header class="header">
        <el-row class="headerBtn" >
          <i class="el-icon-search" ></i>
          <el-select clearable  v-model="Feed" placeholder="反馈状态" size="mini" @change="ChangeFeed">
            <el-option
              v-for="item in FeedbackSubmission"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-select clearable  v-model="Read" placeholder="已阅状态" size="mini" @change="ChangeRead">
            <el-option
              v-for="item in ReadSubmission"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <span style="color:darkgrey;">日期</span>
          <el-date-picker
            clearable
            v-model="value1"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            size="mini"
            value-format="yyyy-MM-dd"
            @change="onPick">
          </el-date-picker>
          <el-button type="primary" size="mini" @click="search()">搜索</el-button>
        </el-row>
      </el-header>

        <el-table
          :data="listData"
          style="width: 100%">
          <el-table-column
            prop="time"
            label="发布日期"
            min-width="20%">
          </el-table-column>
          <el-table-column
            prop="title"
            label="主题"
            min-width="20%">
          </el-table-column>
          <el-table-column
            prop="readnum"
            label="已阅数量"
            min-width="20%">
            <template slot-scope="scope">
              <el-button size="mini">{{scope.row.readnum}}</el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="feedbacknum"
            label="已反馈数量"
            min-width="20%">
            <template slot-scope="scope">
              <el-button size="mini" @click.native.prevent="getFeedbackNum(scope.$index)">{{scope.row.feedbacknum}}</el-button>
            </template>
            <el-dialog title="已反馈数量"
                       :visible.sync="FeedbackNumVisible"
                       :modal-append-to-body="false"
                       :append-to-body="true"
                       v-loading="dialogLoading">
                <el-tabs type="border-card" @tab-click="handleClick">
                  <el-tab-pane label="已反馈机构">
                    <el-table
                      :data="list"
                      style="width: 100%">
                      <el-table-column
                        label="已反馈机构"
                        width="180">
                      </el-table-column>
                    </el-table>
                  </el-tab-pane>
                  <el-tab-pane label="未反馈机构">
                    <el-table
                      :data="list"
                      style="width: 100%">
                      <el-table-column
                        label="为反馈机构"
                        width="180">
                      </el-table-column>
                    </el-table>
                  </el-tab-pane>
                </el-tabs>
              </el-form>
            </el-dialog>
          </el-table-column>
          <el-table-column
            label="操作"
            min-width="20%">
            <template slot-scope="scope">
                <el-button type="primary" icon="el-icon-share" size="mini" @click.native.prevent="getInfo(scope.$index)">查看</el-button>
            </template>
                <el-dialog title="消息详情"
                           :visible.sync="infoTableVisible"
                           :modal-append-to-body="false"
                           :append-to-body="true"
                           v-loading="dialogLoading">
                  <el-form ref="form" :model="infoData" label-width="80px">
                    <el-form-item label="消息主题">
                      <el-input v-model="infoData.title" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="消息详情">
                      <el-input v-model="infoData.content" type="textarea" :rows="4" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="反馈">
                      <el-row>
                        <el-col>
                          <el-switch
                            v-model="isFeedback"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            active-text="需要反馈"
                            inactive-text="不需要反馈"
                            disabled>
                          </el-switch>
                        </el-col>
                      </el-row>
                    </el-form-item>
                  </el-form>
                </el-dialog>
          </el-table-column>
        </el-table>
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

  export default {
    data(){
      return{
        list:"",
        FeedbackNumVisible:false,
        dialogLoading:false,
        name: "MessageList",
        input:'',
        infoTableVisible: false,
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
        infoData:{},
        isFeedback:true,
        params:{
            className: 'Task',
            method: 'selectMessage',
            param:{
                searchData:{
                    type:"publish",
                    start:"",
                    end:"",
                    feed:"",
                    read:""
                            },
                page:{
                pageIndex:1,
                pageSize:10,
                totalPage:0,
                      },
                  }
        }
      }
    },
    created(){
        this.search()
    },
    filters:{
      numFilter(value) {
        //截取当前数据到小数点后两位
        let realVal=parseFloat(value).toFixed(2)
        return realVal
      },
    },
    methods: {
        handleClick(tab) {
            console.log(tab.index);
                let index01 = tab.index;
                let method ='';
                if(tab.index == 0){
                    method = "selectMessageFeedback";
                }else {
                    method = "selectMessageNotFeedback";
                }
                this.$nextTick(async ()=> {
                    this.isShow = true;
                    let params = {
                        className: "Task",
                        method: method,
                        param: {
                            searchData: {
                                taskId:this.listData[index01].id
                            },
                            page: this.params.param.page
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log(res);
                    this.list=res.dataList;
                });
        },
        //获取已反馈数量
        getFeedbackNum(index){
            console.log(index);
            this.FeedbackNumVisible = true;
            this.dialogLoading = true;
            this.$nextTick(async ()=>{
                let params = {
                    className:"Task",
                    method: "selectMessageDetail",
                    param:{
                        id:this.listData[index].id
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.infoData = res[0];
                this.dialogLoading = false;
                console.log(this.infoData.isFeedback);
                if(this.infoData.isFeedback == 0){
                    this.isFeedback = false;
                }else {
                    this.isFeedback = true;
                }
                console.log(this.infoData)
            });
        },
      //获取列表
      getList(params){
        this.$nextTick(async () => {
          let res = await this.$store.dispatch('http/post', params);
          console.log(res);
          this.listData = res.dataList;
          this.totalNum=Number(res.page.totalNum);
        })
      },
      //获取消息详情
      getInfo(index){
        this.dialogLoading = true;
        this.infoData = {};
        console.log(index);
        this.infoTableVisible = true;

        this.$nextTick(async ()=>{
          let params = {
            className:"Task",
            method: "selectMessageDetail",
            param:{
              id:this.listData[index].id
            }
          };
          let res = await this.$store.dispatch('http/post', params);
          this.infoData = res[0];
          this.dialogLoading = false;
          console.log(this.infoData.isFeedback)
          if(this.infoData.isFeedback == 0){
            this.isFeedback = false;
          }else {
            this.isFeedback = true;
          }
          console.log(this.infoData)
        });
      },
      //时间查询
      onPick(val){
          if(val){
              this.params.param.searchData.start = val[0];
              this.params.param.searchData.end = val[1];
          }else {
              this.params.param.searchData.start = "";
              this.params.param.searchData.end = "";
          }
      },
      //选择已反馈条件查询
        ChangeFeed(val){
        console.log('Read:',val)
        this.params.param.searchData.feed = val;
      },
      //选择已阅条件查询
      ChangeRead(val){
          console.log('Read:',val)
          this.params.param.searchData.read = val;
      },
      //切换每页个数
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.params.param.page.pageSize = val;
        this.search()
      },
      //切换页数
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.params.param.page.pageIndex = val;
        this.search()
      },
      //搜索
      search(){
        let params = this.params;
        console.log(params);
        this.getList(params);
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

</style>
