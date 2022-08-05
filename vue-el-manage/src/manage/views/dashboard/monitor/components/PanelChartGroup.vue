<template>
  <div class="panel-group">
    <el-container class="menu_container">
      <el-header class="menu_header">
        <span>{{types[type]}}{{dataTypes[dataType]}}</span>
      </el-header>
      <el-main>
        <line-chart :chartData="chartData"/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  export default {
    name:'PanelChartGroup',
    components:{
      LineChart:()=>import('./LineChart').then(m=>m.default)
    },
    props:{
      chartData:{
        type:Array,
        default:()=>[],
        require:true
      },
      type:{
        type:Number,
        default:0
      },
      dataType:{
        type:Number,
        default:0
      }
    },
    data() {
      return {
        types: ['本日', '本周', '本月'],
        dataTypes:['系统登录量','系统交互量','系统出错量','redis监控'],
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
        /*background-color: rgb(240, 242, 245);*/
        font-size: 16px;
        padding: 20px 10px 0;
        font-weight: bolder;
        color: #1e6abc;
      }
    }
  }
</style>
