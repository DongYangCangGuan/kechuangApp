<template>
  <div>


  <div class="container">
    <div  >
      <el-row class="panel-group" :gutter="20">
        <el-col :xs="24" :sm="24" :lg="24" class="card-panel-col">
          <div class="card-panel  animated pulse">


              <el-form ref="addStaff" :model="addStaff" :rules="rules" label-width="100px">
              <el-form-item label="流程查询" prop="mobile">

                <el-input v-model="searchData.name"  :maxlength="100"  style="width:230px" clearable  placeholder="流程名称"></el-input>

                <el-button type="primary" plain @click="Search"  >搜索</el-button>


              </el-form-item>
              </el-form>
          </div>
        </el-col>

      </el-row>
    </div>
  </div>


  </div>

</template>

<script>
    export default {
        name: "index",
        data(){
          return{
            searchData:{
              name:''
            }
          }
        },
        methods: {
            Search(){}

          }


    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .container{
    padding: 8px;
  }
  .box1{
    position: relative;
    border-radius: 3px;
    background: #ffffff;
    border-top: 3px solid #3c763d;
    margin-bottom: 20px;
    width: 100%;

  }

  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }
  .panel-group {
    margin-left: 5px;
    margin-top: 1px;
    margin-right: 8px;
    .card-panel-col{
      margin-bottom: 32px;
    }
    .card-panel {
      height: 40rem;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
      border-color: rgba(0, 0, 0, .05);

      border-radius: 5px;
      &.selected {
        box-shadow: 1px 1px 5px #888888;
      }
      &:hover {
        box-shadow: 2px 2px 10px #888888;
        .card-panel-icon-wrapper {
          color: #fff;
        }
        .icon-people {
          background: #40c9c6;
        }
        .icon-message {
          background: #36a3f7;
        }
        .icon-money {
          background: #f4516c;
        }
        .icon-shoppingCard {
          background: #34bfa3
        }
      }
      .icon-people {
        color: #40c9c6;
      }
      .icon-message {
        color: #36a3f7;
      }
      .icon-money {
        color: #f4516c;
      }
      .icon-shoppingCard {
        color: #34bfa3
      }
      .card-panel-icon-wrapper {
        float: left;
        margin: 14px 0 0 14px;
        padding: 16px;
        transition: all 0.38s ease-out;
        border-radius: 6px;
      }
      .card-panel-icon {
        float: left;
        font-size: 48px;
      }
      .card-panel-description {
        float: right;
        font-weight: bold;
        margin: 26px;
        margin-left: 0px;
        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }
        .card-panel-num {
          font-size: 20px;
        }
      }
    }
  }
</style>
