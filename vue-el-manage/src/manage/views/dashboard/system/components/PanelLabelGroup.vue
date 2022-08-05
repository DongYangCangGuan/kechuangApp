<template>
  <el-row :gutter="10" class="panel-group" v-if="system">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel  box animated flipInY " @click="handleType('task')">
        <div class="card-panel-icon-wrapper icon-task">
          <svg-icon icon-class="document" class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">任务数</div>
          <div class="card-panel-num-text">任务总数:
            <count-to
              :start-val="0"
              :end-val="system.task.total"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
          <div class="card-panel-num-text">已完成:
            <count-to
              :start-val="0"
              :end-val="system.task.finished"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel  box animated flipInY " @click="handleType('pending')">
        <div class="card-panel-icon-wrapper icon-pending">
          <svg-icon icon-class="home_redis" class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">待处理</div>
          <div class="card-panel-num-text">待审批:
            <count-to
              :start-val="0"
              :end-val="system.pending.approval"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
          <div class="card-panel-num-text">待检查:
            <count-to
              :start-val="0"
              :end-val="system.pending.inspection"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel  box animated flipInY " @click="handleType('remind')">
        <div class="card-panel-icon-wrapper icon-remind">
          <svg-icon icon-class="calendar" class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">提醒</div>
          <div class="card-panel-num-text">待处理:
            <count-to
              :start-val="0"
              :end-val="system.remind.disposal"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
          <div class="card-panel-num-text">待完成:
            <count-to
              :start-val="0"
              :end-val="system.remind.finished"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel  box animated flipInY " @click="handleType('activity')">
        <div class="card-panel-icon-wrapper icon-activity">
          <svg-icon icon-class="clock" class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">团队活动</div>
          <div class="card-panel-num-text">已完成任务:
            <count-to
              :start-val="0"
              :end-val="system.activity.finished"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
          <div class="card-panel-num-text">未完成任务:
            <count-to
              :start-val="0"
              :end-val="system.activity.unfinished"
              :duration="2600"
              class="card-panel-num"
            />
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
  import CountTo from 'vue-count-to'

  export default {
    components: {
      CountTo
    },
    data(){
      return {
        system: {
          task: {
            total: 12345,
            finished: 2345
          },
          pending: {
            approval: 12345,
            inspection: 2345
          },
          remind: {
            disposal: 12345,
            finished: 2345
          },
          activity: {
            finished: 12345,
            unfinished: 2345
          }
        },
      }
    },
    props:{
      type:{
        type:String,
        default:''
      }
    },
    model:{
      prop:'type',
      event:'handleType'
    },
    methods: {
      handleType(type) {
        this.$emit('handleType', type)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .panel-group {
    margin-top: 18px;
    .card-panel-col {
      margin-bottom: 10px;
    }
    .card-panel {
      height: 108px;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      background: #fff;
      box-shadow: 1px 1px 5px #888888;
      border-color: rgba(0, 0, 0, .05);
      border-radius: 5px;
      &:hover {
        box-shadow: 2px 2px 10px #888888;
        .card-panel-icon-wrapper {
          color: #fff;
        }
        .icon-task {
          background: #40c9c6;
        }
        .icon-pending {
          background: #36a3f7;
        }
        .icon-remind {
          background: #f4516c;
        }
        .icon-activity {
          background: #34bfa3
        }
      }
      .icon-task {
        color: #40c9c6;
      }
      .icon-pending {
        color: #36a3f7;
      }
      .icon-remind {
        color: #f4516c;
      }
      .icon-activity {
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
        margin-right: 50px;
        margin-top: 20px;
        .card-panel-text {
          line-height: 20px;
          font-size: 16px;
        }
        .card-panel-num-text {
          color: #808080;
          line-height: 10px;
          font-size: 14px;
          margin-top: 10px;
        }
        .card-panel-num {
          color: #808080;
          font-weight: bold;
          font-size: 16px;
        }
      }
    }
  }
</style>
