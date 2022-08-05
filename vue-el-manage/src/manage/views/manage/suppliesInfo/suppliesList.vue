<template>
  <div id="mainContent" class="row_frame">
    <div class="block_background searchRow">
      <div>
        <img :src="searchImg" class="searchImg" />
      </div>
      <div>
        <el-select v-model="status" placeholder="审批状态">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div>
        <el-select v-model="category" placeholder="供货类别">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div>
        <el-select v-model="departId" placeholder="机构号">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="searchContent">
        <div>供货商名称</div>
        <div>
          <el-input v-model="supplierName" ></el-input>
        </div>
        <div>
          <el-button type="primary" round>搜索</el-button>
        </div>

      </div>
      <div>
        <el-button type="primary" round>导入</el-button>
      </div>
      <div>
        <el-button type="primary" round>新增</el-button>
      </div>
    </div>
    <div class="supplierTable">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="40">
        </el-table-column>
        <el-table-column
          label="注册时间"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="供应商类型"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="供应商名称"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          label="地址"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="供货类别"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="供货种类数"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          label="联系人"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="联系方式"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>

        <el-table-column
          label="审批人"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="审批时间"
          width="100">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="操作"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  // import searchImg from '@A/supplier/icon_search.png'
    export default {
        name: "suppliesList",
        data() {
          return {
            searchImg:searchImg,
            options:[
              {
                label:"未审批",
                value:0
              },
              {
                label:"通过",
                value:1
              },
              {
                label:"未通过",
                value:2
              }
            ],
            status:"",
            category:"",
            departId:"",
            supplierName:"",
          }
        }
    }
</script>

<style scoped>
  .block_background{
    background-color: #F8F8F8;
    border: 1px solid #F3F3F3;
  }
  .row_frame{
    height: 3rem;
    padding: 10px 15px 5px 15px;
    background: #ffffff;
  }
  .searchRow{
    height: 100%;
    width: 100%;
    display: flex;
  }
  .searchData_background{
    background-color: #E5E5E5;
    border: 1px solid #DCDCDC;
  }
  .searchImg{
    height: 15px;
    width: 15px;
    margin: 10px 15px;
  }
  .searchContent{
    display: flex;
  }
  .supplierTable{

  }
</style>
