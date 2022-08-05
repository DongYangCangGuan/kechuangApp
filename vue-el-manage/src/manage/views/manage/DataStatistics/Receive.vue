<template>
  <div>
    <el-container>
      <el-header class="header">
        <el-row class="headerBtn" >

          <el-select v-model="depId" clearable placeholder="机构名称" size="mini" >
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>


          <span class="black">日期</span>
          <el-date-picker
            v-model="start"
            align="right"
            type="date"
            placeholder="选择开始时间"
            size="mini"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>

          <span>-</span>

          <el-date-picker
            class="time"
            v-model="end"
            type="date"
            placeholder="选择结束时间"
            size="mini"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
          <el-input v-model="gName" placeholder="请输入内容" size="mini"
                    :maxlength="100" style="width:150px" clearable ></el-input>

          <el-button type="primary" size="mini" @click="selectAll">检索</el-button>
          <el-button type="primary"  size="mini" @click="exportData">导出</el-button>

        </el-row>

      </el-header>
      <el-main>
        <el-table :data="tableData"  style="width: 100%">
          <el-table-column prop="createTime" label="领用日期"></el-table-column>
          <el-table-column prop="userName" label="领用人"></el-table-column>
          <el-table-column prop="code" label="领用编号" width="220"></el-table-column>
          <el-table-column prop="name" label="商品名称"></el-table-column>
          <el-table-column prop="amount" label="商品数量"></el-table-column>
          <el-table-column prop="use" label="商品用途"></el-table-column>
          <el-table-column prop="depName" label="机构名称"></el-table-column>
        </el-table>
      </el-main>
      <el-footer class="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = "listQuery.pageIndex"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="listQuery.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="listQuery.total">
        </el-pagination>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
  export default {

    mounted(){
      this.getInfo();
      this.departInfo();
    },
    data(){
      return{
        currentPage:1,
        num:100,
        value:'',
        value1:'',
        value2: '',
        options:[],
        tableData:[],
        depId:'',
        start:'',
        end:'',
        gName:'',
        listQuery:{
          //当前页
          pageIndex:1,
          //每页大小
          pageSize:10,
          pageCount:0,//总页数
          // 总条目数
          total: 0
        },
      }

    },
    methods: {
      handleChange(value) {
        console.log(value);
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.listQuery.pageSize = val;
        this.getInfo();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.listQuery.pageIndex = val;
        this.getInfo()
      },

      selectAll(){
        this.listQuery.pageIndex=1;
        this.getInfo();

      },
      exportData(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={
            departId:_this.depId,
            endDate:_this.end,
            beginDate:_this.start,
            gName:_this.gName,
          }
          let params={
            className:'Statistical',
            method:'exportReceiveData',
            param:param,
          };
          let response = await _this.$store.dispatch('http/post', params);
          console.info(response);
          this.dataList(response);
        })
      },
      //导出
      dataList(data){
        console.log("开始导出")
        //列标题，逗号隔开，每一个逗号就是隔开一个单元格  ，入库单号，订单编号，总计
        let str1 = `领用日期,领用人,领用编号,商品名称,商品数量,商品用途,机构名称\n`;
        //增加\t为了不让表格显示科学计数法或者其他格式
        let tempArr = [];
        for(var o in data){
          var obj = {
            createTime:data[o].createTime,
            userName:data[o].userName,
            code:data[o].code,
            name:data[o].name,
            amount:data[o].amount,
            use:data[o].use,
            depName:data[o].depName,
          }
          tempArr.push(obj)
        }
        for(let i=0;i< tempArr.length;i++){
          for(let item in tempArr[i]){
            str1+=`${tempArr[i][item] + '\t'},`;
          }
          str1+='\n';
        }
        //encodeURIComponent解决中文乱码
        let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(str1);
        //通过创建a标签实现
        let link = document.createElement("a");
        link.href = uri;
        //对下载的文件命名
        link.download =  "计划领用.xlsx";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      },

      getInfo(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={
            searchData:{
              departId:_this.depId,
              endTime:_this.end,
              startTime:_this.start,
              goodsName:_this.gName,
            },
            page:{
              pageIndex:_this.listQuery.pageIndex,
              pageSize:_this.listQuery.pageSize,
            }
          }

          let params={
            className:'Statistical',
            method:'selectReceive',
            param:param,

          };
          let response = await _this.$store.dispatch('http/post', params);
          console.info(response);
          _this.tableData=response.dataList;
          this.listQuery.total=response.page.totalNum
        })


      },

      departInfo(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={};
          let params={
            className:'Statistical',
            method:'selectInstitutions',
            param:param,
          };
          let response1=await _this.$store.dispatch('http/post', params);
          console.info(response1);
          _this.options=response1;
        })
      }

    }


  }
</script>
<style  scope>
  .header {
    background-color: rgb(240, 244, 245);
    text-align: left;
  }

  /*.headerBtn {*/
  /*  margin-top: 13px;*/
  /*}*/

  .btnCss {
    margin-left: 10px;
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
