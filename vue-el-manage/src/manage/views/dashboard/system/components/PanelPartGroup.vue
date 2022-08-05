<template>
  <el-row :gutter="10" class="panel-group">
    <el-col :xs="24" :sm="24" :lg="10" class="card-panel-col">
      <div class="card-panel animated pulse ">
        <div class="card-panel-title">
          <el-badge value="+2" class="badge-text">
            <span>提醒</span>
          </el-badge>
          <el-button circle type="info" class="badge-right">醒</el-button>
        </div>
        <div class="card-panel-table">
          <el-table border
                    :data="customPlan"
                    :max-height="300"
                    :header-cell-style="{textAlign:'center'}"
                    :cell-style="{textAlign:'center'}"
                    @row-click="handleRow">
            <el-table-column :width="80" prop="name" label="序号"/>
            <el-table-column :width="100" prop="time" label="日期">
              </el-table-column>
            <el-table-column  prop="title" label="标题">
            </el-table-column>
            <p v-if="seen">
              <el-table-column  prop="id" label="id">
              </el-table-column>
            </p>
          </el-table>
        </div>

        <el-pagination class="card-panel-pagination"
                       :small="device==='mobile'"
                       :page-size="pageSize"
                       :currentPage.sync="currentPage"
                       background
                       layout="prev, pager, next"
                       :total="total"
                       @current-change="pageChange"/>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="7" class="card-panel-col">
      <div class="card-panel animated pulse">
        <div class="card-panel-chart">
          <raddar-chart class="chart-part"/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="7" class="card-panel-col">
      <div class="card-panel animated pulse">
        <div class="card-panel-chart">
          <bar-chart class="chart-part"/>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "PanelPartGroup",
    components:{
      RaddarChart:()=>import('./RaddarChart').then(m=>m.default),
      BarChart:()=>import('./BarChart').then(m=>m.default)
    },
    data() {

      return {
        seen: false,
        customPlan: [],
        total:'',
        pageSize: 2,
        currentPage: 1,
      }
    },
    computed: {
      ...mapState({
        device:state=>state.app.device
      })
    },
    props:{
      type:{
        type:String,
        default:''
      }
    },
     mounted(){
            this.getInfo();
          },
     methods: {
      pageChange() {
        //请求该页数下的数据
        let param = {
          'pageIndex':this.currentPage,
          'pageSize':this.pageSize,
        }
        this.getInfo(param),
        console.log(this.currentPage)
      },
      handleRow(row){
        this.updateStatus(row),
        this.$emit('handleRow',{...row})
      },
       getInfo(param){
              if(!param){
                param = {
                  'pageIndex':1,
                  'pageSize':2,
                }
              }
         this.$nextTick(async () => {
                const params = {
                  url:'/service-manage-crm/managecrm/api',
                  className: 'CustomerVisitSchedule',
                  method: 'getCustomerManagerPlanVisitList',
                  param: param
                };
                let response = await this.$store.dispatch('http/post', params);
                console.log('response',response);
                this.customPlan= response.dataList;
                console.log('this.customPlan',this.customPlan);
                for(let i=0;i<this.customPlan.length;i++){
                  this.customPlan[i].name;
                  this.customPlan[i].title;
                  this.customPlan[i].time;
                }
                this.total=response.totalNum;
                this.totalCount = response.totalNum;
                this.currentPages = response.totalPage;

              })
            },
       updateStatus(row){
         let param = {
           'id':row.id,
         }
         console.log(param)
         this.$nextTick(async () => {
           const params = {
             url:'/service-manage-crm/managecrm/api',
             className: 'CustomerVisitSchedule',
             method: 'UpdateCustomerMangerPlanVisit',
             param: param
           };
           let response = await this.$store.dispatch('http/post', params);
         })
       }
    }
  }
</script>

<style lang="scss" scoped>
  .panel-group {
    margin-top: 18px;
    .card-panel-col {
      margin-bottom: 32px;
    }
    .card-panel {
      height: 400px;
      cursor: pointer;
      font-size: 12px;
      overflow: hidden;
      box-shadow: 1px 1px 5px #888888;
      border-color: rgba(0, 0, 0, .05);
      position: relative;
      border-radius: 3px;
      background: #ffffff;
      border-top: 3px solid #3c763d;
      margin-bottom: 20px;
      width: 100%;
      &:hover {
        box-shadow: 2px 2px 10px #888888;
      }

      .card-panel-title {
        padding: 10px;
        font-size: 16px;
        .badge-text {
          padding: 0 10px 0 0;
        }

        .badge-right {
          float: right;
          margin-top: -5px;
        }

      }

      .card-panel-table {
        margin-top: 10px;
        padding: 0px 10px;
      }

      .card-panel-pagination {
        text-align: right;
        position: absolute;
        bottom: 10px;
        left: 0;
        right: 0;
      }

      .card-panel-chart{
        background: #fff;
        padding: 8px 16px 0;
        height: 100%;

        .chart-part{
          height: 100%!important;
        }
      }
    }
  }
</style>
