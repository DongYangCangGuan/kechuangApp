<template>
  <div class="app-container">
    <panel-date-group v-model="dateType" @changeType="getMonitorCount"/>
    <panel-count-group :count="dataCount" v-model="dataType" @handleDataType="getChartData"/>
    <panel-chart-group :chartData="chartData" :type="dateType.type" :dataType="dataType"/>
    <panel-log-group/>
  </div>
</template>

<script>
  export default {
    name: "Monitor",
    components:{
      PanelDateGroup:()=>import('./components/PanelDateGroup').then(m=>m.default),
      PanelCountGroup:()=>import('./components/PanelCountGroup').then(m=>m.default),
      PanelChartGroup:()=>import('./components/PanelChartGroup').then(m=>m.default),
      PanelLogGroup:()=>import('./components/PanelLogGroup').then(m=>m.default),
    },
    data(){
      return {
        dateType:{
          type:0,//本日 、本周 、 本月
          dates:[new Date(),new Date()]
        },
        dataCount:null,
        dataType:0,//系统登录、系统交互、系统出错、redis,
        chartData:[]

      }
    },
    mounted(){
      this.getMonitorCount()
    },
    methods:{
      getMonitorCount(){
        this.$nextTick(async () =>{
          console.log("获取数量")
          this.dataCount = await this.$store.dispatch('monitor/getMonitorCount',{type:this.dateType.type})
          console.log(this.dataCount);
          this.getChartData()

        })

      },
      getChartData(){
        this.$nextTick(async ()=>{
          this.chartData = await this.$store.dispatch('monitor/getChartData',{type:this.dateType.type,dataType:this.dataType})
        })
      }
    }
  }
</script>

<style lang="scss">

</style>
