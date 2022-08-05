<template>
  <div class="animate-container">
    <div class="animate-wrapper">
      <div v-for="item of animatesMap" class="animate-content" :key="item" @click="handleClipboard(item,$event)">
        <el-tooltip placement="top">
          <div slot="content">
            {{item}}
          </div>
          <div class="animate-item animated infinite" :class="[`${item}`]" >
            <span class="animate-text">{{item}}</span>
          </div>
        </el-tooltip>
      </div>
    </div>
  </div>
</template>

<script>
  import 'animate.css'
  import animates from './animate'
  import clipboard from '@COM/utils/clipboard'

  export default {
    name: "animated",
    data(){
      return {
        animatesMap:animates
      }
    },
    methods:{
      handleClipboard(text, event) {
        clipboard(text, event)
      }
    }
  }
</script>

<style lang="scss">
  @keyframes hue {
    0% {
      -webkit-filter: hue-rotate(0deg);
    }
    100% {
      -webkit-filter: hue-rotate(-360deg);
    }
  }
.animate-container {
  margin: 10px 20px 0;
  overflow: hidden;
  .animate-wrapper {
    margin: 0 auto;
    overflow: hidden;
    width: 100%;
    border: 1px solid #CCC;
    border-right: none;
    border-bottom: none;
  }

  .animate-content{
    width: 25%;
    float: left;
    overflow: hidden;
    border: 1px solid #CCC;
    border-top: none;
    border-left: none;
  }

  .animate-item {
    text-align: center;
    width: 100%;
    font-size: 20px;
    padding: 30px 10px;
    cursor: pointer;

    &.animated{
      animation-duration: 3s;
    }

    .animate-text{
      color: #f35626;
      background-image: -webkit-linear-gradient(92deg,#f35626,#feab3a);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      -webkit-animation: hue 30s infinite linear;
    }
  }
}
</style>
