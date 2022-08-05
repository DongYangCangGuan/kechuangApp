<template >
  <div>
    <el-container>
      <el-header class="header">
        <el-row class="headerBtn" >
          <el-select v-model="depId" clearable  placeholder="机构名称" size="mini">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>

          <el-input v-model="gName" placeholder="请输入内容" size="mini"
                    :maxlength="100" style="width:150px" clearable ></el-input>
          <el-button type="primary"  size="mini" @click="selectAll">检索</el-button>
          <el-button type="primary"  size="mini" @click="exportData">导出</el-button>
        </el-row>

      </el-header>
      <el-main>
        <el-table :data="tableData" style="width: 100%" :summary-method="getSummaries" show-summary>
          <el-table-column prop="depName" label="机构名称"></el-table-column>
          <el-table-column prop="wareName" label="仓库名称"></el-table-column>
          <el-table-column prop="goodsName" label="商品名称"></el-table-column>
          <el-table-column prop="reallyStock" label="数量"></el-table-column>
          <el-table-column prop="total" label="总计(元)"></el-table-column>

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
        value:'',
        depId:'',
        start:'',
        end:'',
        gName:'',
        input:'',
        num:100,
        currentPage:1,
        value1:'',
        value2: '',
        tableData:[],
        options:[],
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
    filters:{
      numFilter(value) {
        //截取当前数据到小数点后两位
        let realVal=parseFloat(value).toFixed(2)
        return realVal
      },

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
      getSummaries(param){
        const { columns, data } = param;
        const sums = [];
        console.log(sums[3]);
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          if(index===2 || index===3){
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = parseFloat(sums[index]).toFixed(2);
          }
        });
        return sums;
      },
      exportData(){
        let _this=this;
        this.$nextTick(async ()=>{
          let param={
            departId:_this.depId,
            gName:_this.gName,
          }
          let params={
            className:'Statistical',
            method:'exportStockData',
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
        let str1 = `机构名称,仓库名称,商品名称,数量,总计(元)\n`;
        //增加\t为了不让表格显示科学计数法或者其他格式
        let tempArr = [];
        for(var o in data){
          var obj = {
            depName:data[o].depName,
            wareName:data[o].wareName,
            goodsName:data[o].goodsName,
            reallyStock:data[o].reallyStock,
            total:parseFloat(data[o].total).toFixed(2),
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
        link.download =  "库存数量.xlsx";
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
              goodsName:_this.gName,
            },
            page:{
              pageIndex:_this.listQuery.pageIndex,
              pageSize:_this.listQuery.pageSize,
            }
          }
          let params={
            className:'Statistical',
            method:'selectStock',
            param:param,
          };
          let response = await _this.$store.dispatch('http/post', params);
          _this.tableData=response.dataList;
          for(let i=0;i<response.dataList.length;i++){
            if(response.dataList[i].total != null && response.dataList[i].total!=''){
              response.dataList[i].total = parseFloat(response.dataList[i].total).toFixed(2);
            }
          }
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
