<template>
  <div class="panel-group">
    <el-container class="menu_container menu_height">
      <el-header class="menu_header">
        <span>日志信息</span>
      </el-header>
      <el-main>
        <el-form inline :model="form" size="small">
          <el-form-item label="日期范围：">
            <el-date-picker
              v-model="logDates"
              size="small"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="来源:">
            <el-select v-model="form.type" clearable placeholder="请选择来源">
              <el-option v-for="(item,index) in types" :key='item' :label="item" :value="index"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关键字:">
            <el-select v-model="form.actiontype" clearable placeholder="请选择操作">
              <el-option v-for="(item,index) in actionTypes" :key='item' :label="item" :value="index"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日志级别:">
            <el-select v-model="form.log_level" clearable placeholder="请选择日志级别">
              <el-option v-for="(item,index) in levels" :key='item.key' :label="item.value" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="tableList" :max-height="500" border ref="table">
          <el-table-column prop="userid" label="用户" min-width="5%"></el-table-column>
          <el-table-column prop="recivetime" label="接收时间" min-width="8%"></el-table-column>
          <el-table-column prop="module" label="模块" min-width="7%"></el-table-column>
          <el-table-column show-overflow-tooltip prop="function" label="方法" min-width="8%"></el-table-column>
          <el-table-column show-overflow-tooltip prop="inputparam" label="入参" min-width="13%"></el-table-column>
          <el-table-column show-overflow-tooltip prop="outputparam" label="出参" min-width="13%"></el-table-column>
          <el-table-column prop="client_ip" label="服务器IP" min-width="8%"></el-table-column>
          <el-table-column prop="typeInfo" label="信息来源" min-width="8%"></el-table-column>
          <el-table-column show-overflow-tooltip prop="errormsg" label="错误信息" min-width="8%"></el-table-column>
          <el-table-column prop="code" label="返回码" min-width="6%"></el-table-column>
          <el-table-column prop="runtime" label="运行时间" min-width="7%"></el-table-column>
          <el-table-column label="操作" align="center" min-width="10%">
            <template slot-scope="scope">
              <el-button
                size="primary"
                @click="lookLogMore(scope.row)">查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer>
        <el-pagination
          class="pagination"
          :small="device==='mobile'"
          :pageSize.sync="pageSize"
          :currentPage.sync="page"
          :page-sizes="[10, 20, 30, 40]"
          background
          layout="total,sizes,prev, pager, next,jumper"
          :total="totalCount"
          @size-change="getLogInfo"
          @current-change="getLogInfo"/>
      </el-footer>
    </el-container>
        <el-dialog title="日志详情" :visible.sync="modalFlag2" >
        <el-form ref='modalFlag2' :model="form" label-width="100px">
        <el-form-item label="用户" prop="userid">

            <el-input name="userid"    v-model="form.userid" readonly></el-input>
			
        </el-form-item>
        <el-form-item label="接收时间" prop="recivetime">
            <el-input name="recivetime"   v-model="form.recivetime" readonly></el-input>
        </el-form-item>
        <el-form-item label="模块" prop="module">

            <el-input name="module"    v-model="form.module" readonly></el-input>

        </el-form-item>
        <el-form-item label="方法" prop="function">
            
            <el-input name="function"    v-model="form.function" readonly></el-input>

        </el-form-item>
        <el-form-item  label="入参" prop="inputparam" >
             
            <el-input name="inputparam" type="textarea" autosize  v-model="form.inputparam" readonly  ></el-input>

           
        </el-form-item>
        <el-form-item label="出参" prop="outputparam" >

            <el-input name="outputparam"  type="textarea" autosize  v-model="form.outputparam" readonly ></el-input>

            
        </el-form-item>
        <el-form-item  label="服务器IP" prop="client_ip" >
           
             <el-input name="client_ip"    v-model="form.client_ip" readonly ></el-input>

            
        </el-form-item>
          <el-form-item  label="信息来源" prop="type" >

            <el-input name="type"    v-model="form.type" readonly ></el-input>


          </el-form-item>
        <el-form-item  label="错误信息" prop="errormsg" >
          
             <el-input name="errormsg"  type="textarea" autosize  v-model="form.errormsg" readonly  ></el-input>

            
        </el-form-item>
        <el-form-item  label="返回码" prop="code" >
           
             <el-input name="code"    v-model="form.code" readonly ></el-input>

        </el-form-item>
        <el-form-item label="运行时间" prop="runtime" >
           
             <el-input name="runtime"    v-model="form.runtime" readonly ></el-input>

            
        </el-form-item>
	   <el-form-item>
			<el-button type="primary" @click="close()">关闭</el-button>
		</el-form-item>

       </el-form>
	   
	  </el-dialog>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: 'PanelLogGroup',
    data() {
      return {
        logDates: [new Date(), new Date()],
		    modalFlag2:false,
        pickerOptions: {
          shortcuts: [
            {
              text: "最近一周",
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                picker.$emit("pick", [start, end]);
              }
            },
            {
              text: "最近一个月",
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit("pick", [start, end]);
              }
            },
            {
              text: "最近三个月",
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit("pick", [start, end]);
              }
            }
          ]
        },
        form: {
          type: '',//来源
          module: '',//模块
          actiontype: '',//操作
          log_level: ''//日志级别
        },
        types: ['app', 'pc'],
        actionTypes: ['登录', '调用'],
        levels: [{key:'info',value:'普通'},{key:'ERROR',value:'错误'}],
        page: 1,
        pageSize: 10,
        tableList: [],
        totalCount: 0
      }
    },
    computed: {
      ...mapState({
        device: state => state.app.device
      })
    },
    mounted() {
      this.getLogInfo()
    },
    methods: {
      search() {
        console.log('1111',this.form.type)
        this.page = 1;
        this.pageSize = 10;
        this.getLogInfo()
      },
	  close(){
	         this.modalFlag2=false;
	  },
	   lookLogMore(row){
           console.log(row);
          this.form=row;
          console.log(this.form);
          this.modalFlag2=true;
    },
      getLogInfo() {
        this.$nextTick(async () => {
		 var startYear=this.logDates[0].getFullYear();
                 var startMonth=this.logDates[0].getMonth()+1;
                 startMonth = startMonth < 10 ? '0' + startMonth : startMonth;
                 var startDay=this.logDates[0].getDate()
                 startDay = startDay < 10 ? ('0' + startDay) : startDay;
                 var startDate=startYear+'-'+startMonth+'-'+startDay+' 00:00:00';
				 var endYear=this.logDates[1].getFullYear();
                 var endMonth=this.logDates[1].getMonth()+1;
                 endMonth = endMonth < 10 ? '0' + endMonth : endMonth;
                 var endDay=this.logDates[1].getDate()
                 endDay = endDay < 10 ? ('0' + endDay) : endDay;
                 var endDate=endYear+'-'+endMonth+'-'+endDay+' 23:59:59';
			console.log(startDate);
			console.log(endDate);
          const params = {
		  searchdata:{
            actiontype: this.form.actiontype,
            startDate: startDate,
            log_level: this.form.log_level,
            module: this.form.module,
            endDate: endDate,
            type: this.form.type
			},
			page:{
            pageIndex: this.page,
            pageSize: this.pageSize
			}
          }
          this.$refs.table.bodyWrapper.scrollTop = 0
          const {resultData, count} = await this.$store.dispatch('monitor/getLogInfo', params)
          this.tableList = resultData
          this.totalCount = count
          console.log('123',resultData)

        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .panel-group {
    margin-top: 18px;
    .menu_container {
      background: #fff;
      .menu_header {
        text-align: center;
        font-size: 16px;
        padding: 20px 10px 0;
        font-weight: bolder;
        color: #1e6abc;
      }
      .pagination {
        overflow: auto;
      }
    }
  }
  .text1{
  width:100px;
  text-align:center;
  float:left;
  fon-size:14px;
  color:#606266;
  padding:0 12px 0 0;
  box-sizing:border-box;
}
</style>
