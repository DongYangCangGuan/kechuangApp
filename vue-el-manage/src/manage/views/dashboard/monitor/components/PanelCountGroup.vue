<template>
  <el-row :gutter="40" class="panel-group">
    <el-col v-for="(type,index) in types" :key="type" :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleDataType(`${index}`)">
        <div class="card-panel-icon-wrapper" :class="[`icon-${type}`]">
          <svg-icon :icon-class="icons[index]" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{titles[index]}}
          </div>
          <count-to :start-val="0" :end-val="count&&count[`${type}`]||0" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  name:'PanelCountGroup',
  components: {
    CountTo
  },
  data(){
    return {
      titles:['系统登录量','系统交互量','系统出错量','redis监控'],
      types:['login','interactive','error','redis'],
      icons:['peoples','interactive','bug','redis']
    }
  },
  props:{
    count:{
      type:Object,
      default:()=>null
    },
    dataType:{
      type:Number,
      default:0
    }
  },
  model:{
    prop:'dataType',
    event:'handleDataType'
  },
  methods: {
    handleDataType(type) {
      this.$emit('handleDataType', parseInt(type))
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;
  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-login {
         background: #40c9c6;
      }
      .icon-interactive {
        background: #36a3f7;
      }
      .icon-error {
        background: #f4516c;
      }
      .icon-redis {
        background: #34bfa3
      }
    }
    .icon-login {
      color: #40c9c6;
    }
    .icon-interactive {
      color: #36a3f7;
    }
    .icon-error {
      color: #f4516c;
    }
    .icon-redis {
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
