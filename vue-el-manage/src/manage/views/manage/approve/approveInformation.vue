<template>
  <div>
    <el-container>
      <el-header class="header">
        <el-row class="headerBtn">

          <el-select v-model="processName" @clear="clearProcessName" @change="processNode" clearable placeholder="审批流程">
            <el-option
              v-for="item in approveOptions"
              :key="item.value"
              :label="item"
              :value="item"
           >
            </el-option>
          </el-select>
          <el-select v-model="nodeName" clearable placeholder="节点名称">
            <el-option v-for="item in nodeOptions"
                       :key="item.value"
                       :label="item"
                       :value="item">
            </el-option>
          </el-select>
          <el-date-picker
            v-model="startTime"
            value-format="yyyy-MM-dd"
            type="date"
            clearable
            placeholder="选择开始时间">
          </el-date-picker>
            <span class="black">至</span>
            <el-date-picker
              v-model="endTime"
              value-format="yyyy-MM-dd"
              type="date"
              clearable
              placeholder="选择结束时间">
            </el-date-picker>


          <span class="black">订单:</span>
          <el-input v-model='keyWord' :maxlength="100" style="width:200px" clearable></el-input>
          <el-button type="primary" class="btnCss" @click="searchInfo">搜索</el-button>
        </el-row>
      </el-header>
      <el-main>
        <el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column
            label=" "
            type="index">
          </el-table-column>
          <el-table-column
            prop="billNo"
            label="单号">
          </el-table-column>
          <el-table-column
            prop="processName"
            label="流程名称">
          </el-table-column>

          <el-table-column
            prop="nodeName"
            label="当前节点">
          </el-table-column>
          <el-table-column
            prop="result1"
            label="审批情况">
          </el-table-column>
          <el-table-column
            prop="result2"
            label="二审情况">
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer class="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = '1'
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
          :total='this.listQuery.totalNum'>
        </el-pagination>
      </el-footer>
    </el-container>

  </div>
</template>

<script>
  //import $http from '@H'

  export default {
    name: "approveInformation",
    data() {
      return {
        currentPage:'1',
        tableData:[],
        startTime:'',
        endTime:'',
        processName:'',
        nodeName:'',
        keyWord:'',
        approveOptions:[],
        nodeOptions:[],
        listQuery:{
          pageIndex:1,
          pageSize:10,
          totalNum:0,
        },
      }
    },
    mounted() {
      this.getInfo()
    },
    methods: {
      //进入页面加载数据
      getInfo(){
        //获取流程名称下拉框
        this.$nextTick(async ()=> {
          let para={
          };
          let params = {
            className: 'approveNode',
            method: 'loadProcessNames',
            param: JSON.stringify(para) };
          let res = await this.$store.dispatch('http/post', params);
          console.log(res)
          this.approveOptions = res;
        });
        //加载页面数据
        this.$nextTick(async ()=> {
          let para={
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData:{
              processName:"",
              nodeName:"",
              startTime:"",
              endTime:"",
              keyWord:""
            }
          };
          let params = {
            className:'approve',
            method:'searchProcess',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.tableData=res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })
      },
      //选择流程后加载节点名称下拉框
      processNode(){
        //加载节点名称下拉框
        this.$nextTick(async ()=> {
          let para={
            processName:this.processName
          };
          let params = {
            className:'approveNode',
            method:'loadProcessNodeNames',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.nodeOptions = res;
        });
      },
      //搜索数据
      searchInfo(){
        this.$nextTick(async () => {
          let para = {
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData: {
              processName:this.processName,
              nodeName:this.nodeName,
              startTime:this.startTime,
              endTime:this.endTime,
              keyWord:this.keyWord,
            }
          }
          let params = {
            className: 'approve',
            method: 'searchProcess',
            param: JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post', params);
          console.log(res);
          this.tableData = res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })

      },

      clearProcessName(){
        this.nodeName='';
      },
      handleSizeChange(val){
        this.listQuery.pageSize= val;
        this.searchInfo();
      },
      handleCurrentChange(val){
        this.listQuery.pageIndex=val;
        this.searchInfo()
      },
    }

  }
</script>

<style scoped>
  .header {
    background-color: rgb(240, 244, 245);
    text-align: left;
  }

  /*.headerBtn {*/
    /*margin-top: 13px;*/
  /*}*/

  .btnCss {
    margin-left: 10px;
  }

  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
  }
  .black{
    color: black;
  }
  .aaa {
    float: right;
  }
</style>
