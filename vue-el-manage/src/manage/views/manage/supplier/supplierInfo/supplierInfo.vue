<template>
  <div>
    <el-container>
      <el-header class="header">
        <el-row class="headerBtn">
          供应商名称:
          <el-input v-model='supplierName' @clear="clearSupplierName" :maxlength="100" class="width1" clearable></el-input>
          是否启用:
          <el-select v-model="isused" placeholder="是否启用">
            <el-option v-for="item in usedOptions"
                       :key="item.value"
                       :label="item.label"
                       :value="item.value">
            </el-option>
          </el-select>
          <el-button type="primary" class="btnCss" @click="search">搜索</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="addSupplierInfo" plain >新增</el-button>

          <template class="aaa">
            <el-radio-group v-model="radio" @change="btnchange">
              <el-radio :label="3">显示所有</el-radio>
              <el-radio :label="6">已启用</el-radio>
              <el-radio :label="9">已停用</el-radio>
            </el-radio-group>
          </template>

<!--          <el-button type="primary" class="btnCss aaa" @click="allSuppliers">显示所有</el-button>-->
<!--          <el-button type="primary" class="btnCss aaa" @click="allUsed">已启用</el-button>-->
<!--          <el-button type="primary" class="btnCss aaa" @click="allStopped">已停用</el-button>-->
        </el-row>
      </el-header>
      <el-main>
        <el-table :data="tableData" class="table">
          <el-table-column label=" " type="index"></el-table-column>
          <el-table-column prop="createTime" label="注册时间"></el-table-column>
          <el-table-column prop="type" label="供应商类型"></el-table-column>
          <el-table-column prop="name" label="供应商名称"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
          <el-table-column prop="totalNum" label="供货种类数"></el-table-column>
          <el-table-column prop="person" label="联系人"></el-table-column>
          <el-table-column prop="phone" label="联系方式"></el-table-column>
          <el-table-column prop="modifierName" label="审核人"></el-table-column>
          <el-table-column prop="modifyTime" label="审核时间"></el-table-column>
          <el-table-column prop="isused" label="是否启用"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span class="color1" @click="goSupplierDetail(scope.row.id)">查看</span>
              <span class="color2" v-if="scope.row.isused=='停用'" @click="changeUsed(scope.row.id)">启用</span>
              <span  class="color3" v-if="scope.row.isused=='启用'" @click="changeStopped(scope.row.id)">停用</span>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer class="footer">
        <div class="block">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page = "listQuery.pageIndex"
            :page-sizes="[10, 20, 30, 40]"
            :page-size="listQuery.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="listQuery.totalNum">
          </el-pagination>

        </div>
      </el-footer>
    </el-container>

  </div>
</template>

<script>
  //import $http from '@H'

  export default {
    name: "supplier-Info",
    // 接受父组件的传值
    props: {
      onAdd: {
        type: Function,
        default: null
      },
      onModify: {
        type: Function,
        default: null
      }
    },
    data() {
      return {
        used:true,
        stop:true,
        num:10,
        currentPage:1,
        tableData:[],
        supplierName:'',
        isused:'',
        usedOptions:[
          { value: '1',
            label: '已启用'},
          { value: '0',
            label: '已停用'},
        ],
        radio:3,
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
      clearSupplierName(){
        this.getInfo();
      },
      btnchange(){
        this.listQuery.pageIndex=1;
        console.log(this.radio);
        if(this.radio==3){
          this.getInfo();
        }else if(this.radio==6){
          this.allUsed();
        }else{
          this.allStopped();
        }
      },
      //跳转新增页面
      addSupplierInfo(){
        if (this.onAdd) {
          this.onAdd(null)
        }
      },
      //跳转详情页面
      goSupplierDetail(id){
        sessionStorage.setItem('supplierId',id)
        if (this.onModify) {
          this.onModify(null)
        }
      },
      getInfo(){
        //进入页面获取所有数据
        this.$nextTick(async ()=> {
          let para={
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData:{
              supplierName:"",
             isused:"",
            }
          };
          let params = {
            className:'supplier',
            method:'querySuppliers',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.tableData=res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })
        this.radio=3;
      },
      //已启用
      allUsed(){
        this.$nextTick(async ()=> {
          let para={
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData:{
              supplierName:"",
              isused:'1',
            }
          };
          let params = {
            className:'supplier',
            method:'querySuppliers',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.tableData=res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })
      },
      //已禁用
      allStopped(){
        this.$nextTick(async ()=> {
          let para={
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData:{
              supplierName:"",
              isused:'0',
            }
          };
          let params = {
            className:'supplier',
            method:'querySuppliers',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.tableData=res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })
      },

      //根据条件搜索相应的数据
      search(){
        this.$nextTick(async ()=> {
          let para={
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData:{
              supplierName:this.supplierName,
              isused:this.isused
            }
          };
          let params = {
            className:'supplier',
            method:'querySuppliers',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res);
          this.tableData=res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })
      },
      //分页
      handleSizeChange(val){
        this.listQuery.pageSize= val;
        this.getInfo();
      },
      handleCurrentChange(val){
        console.log(val);
        this.listQuery.pageIndex=val;
        this.getInfo()
      },
      //点击启用操作
      changeUsed(id){
        this.$nextTick(async ()=> {
            let para={
            id:id
            };
            let params = {
              className:'supplier',
              method:'enableSupplier',
              param:JSON.stringify(para)
            };
            let res = await this.$store.dispatch('http/post',params);
            console.log(res);
            if(res!==null){
              this.$message ({
                message:'启用成功',
                type:'succ'
              })
              this.getInfo();
            } else{
              this.$message ({
                message:'启用失败',
                type:'error'
              })
            }
        })
        this.getInfo();
      },
      //点击停用操作
      changeStopped(id){
        this.$nextTick(async ()=> {
          let para={
            id:id
          };
          let params = {
            className:'supplier',
            method:'unableSupplier',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res);
          if(res!==null){
            this.$message ({
              message:'停用成功',
              type:'succ'
            })
            this.getInfo();
          } else{
            this.$message ({
              message:'停用失败',
              type:'error'
            })
          }
        })


      },

    }

  }
</script>

<style scoped>
  .header {
    color: #606266;
    background-color: rgb(240, 244, 245);
    text-align: left;
  }

  .headerBtn {
    /*margin-top: 13px;*/
  }

  .btnCss {
    margin-left: 10px;
  }

  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
    /* position: relative; */
  }
  .block{
    /* position: absolute; */
    right: 0;
    left: 0;
  }
.color1{
  color: #F19434;
  margin-right: 4px;
}
.color2{
  color: #ff0000;
}
.color3{
  color: #606266;
}
.table{
  width:100%
}
.width1{
  width:200px
}
  .aaa {
    margin-top: 13px;
    float: right;
  }
  /deep/ .el-radio-group{
    margin-top: 26px;
    float: right;

  }
</style>
