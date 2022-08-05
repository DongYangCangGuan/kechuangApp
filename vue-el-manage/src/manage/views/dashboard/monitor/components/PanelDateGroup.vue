<template>
  <div class="panel-group">
    <el-radio-group v-model="dateType.type" class="el-radio-group">
      <el-radio-button v-for="(label,index) in types" :label="index" :key="label">{{label}}
      </el-radio-button>
    </el-radio-group>
    <el-date-picker
      class="date_picker"
      readonly
      size="small"
      v-model="dateType.dates"
      type="daterange"
      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
    />
  </div>
</template>

<script>
  import moment from 'moment'
  moment.locale('zh-cn')

  export default {
    name:'PanelDateGroup',
    data() {
      return {
        types: ['本日', '本周', '本月'],
      }
    },
    props: {
      dateType: {
        type: Object,
        default:()=>{return {}}
      }
    },
    model: {
      prop: 'dateType',
      event: 'changDateType'
    },
    watch: {
      'dateType.type'(value) {
        if (value!==undefined) {
          const dateType = this.dateType
          if (value == 0) {
            dateType.dates = [new Date(), new Date()]
          }
          else if (value == 1) {
            dateType.dates = [moment().week(moment().week()).startOf('week'), moment().week(moment().week()).endOf('week')]
          } else if (value == 2) {
            dateType.dates = [moment().month(moment().month()).startOf('month'), moment().month(moment().month()).endOf('month')]
          } else {
            dateType.dates = null
          }
          this.$emit('changDateType', dateType)
          this.$emit('changeType')
        }
      },
      'dateType.dates'(value) {
        const dateType = this.dateType
        dateType.dates = value
        this.$emit('changDateType', dateType)
      }
    }
  }
</script>
