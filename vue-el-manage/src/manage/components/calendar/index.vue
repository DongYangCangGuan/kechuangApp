<template>
  <section class="wh_container">
    <div class="wh_content_all">
      <div class="wh_top_change">
        <li @click="PreMonth(myDate,false)">
          <div class="wh_left"></div>
        </li>
        <li class="wh_content_li">{{dateTop}}</li>
        <li @click="NextMonth(myDate,false)">
          <div class="wh_right"></div>
        </li>
      </div>
      <div class="wh_content">
        <div class="wh_content_item" v-for="tag in textTop">
          <div class="wh_top_tag">{{tag}}</div>
        </div>
      </div>
      <div class="wh_content">
        <div class="wh_content_item" v-for="(item,index) in list" @click="clickDay(item,index)">
          <div
            class="wh_item_date"
            v-bind:class="[{wh_chose_day:item.chooseDay},setClass(item)]"
          >{{item.id}}
          </div>
          <div class="wh_item_point" v-show="item.isMark">
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
  import timeUtil from "@COM/utils/calendar";

  export default {
    data() {
      return {
        myDate: [],
        list: [],
        historyChose: [],
        dateTop: "",
        todayDate:""
      };
    },
    props: {
      markDate: {
        type: Array,
        default: () => []
      },
      markDateMore: {
        type: Array,
        default: () => []
      },
      textTop: {
        type: Array,
        default: () => ["周一", "周二", "周三", "周四", "周五", "周六", "周日"]
      },
      sundayStart: {
        type: Boolean,
        default: () => false
      },
      agoDayHide: {
        type: String,
        default: `0`
      },
      futureDayHide: {
        type: String,
        default: `2554387200`
      }
    },
    created() {
      this.todayDate = new Date().toLocaleDateString();
      this.intStart();
      this.myDate = new Date();
    },
    methods: {
      intStart() {
        timeUtil.sundayStart = this.sundayStart;
      },
      setClass(data) {
        let obj = {};
        obj[data.markClassName] = data.markClassName;
        return obj;
      },
      clickDay: function (item, index) {
        this.todayDate = "";
        if (item.otherMonth === "nowMonth" && !item.dayHide) {
          this.getList(this.myDate, item.date);
        }
        if (item.otherMonth !== "nowMonth") {
          item.otherMonth === "preMonth"
            ? this.PreMonth(item.date)
            : this.NextMonth(item.date);
        }
      },
      ChoseMonth: function (date, isChosedDay = true) {
        date = timeUtil.dateFormat(date);
        this.myDate = new Date(date);
        this.$emit("changeMonth", timeUtil.dateFormat(this.myDate));
        if (isChosedDay) {
          this.getList(this.myDate, date, isChosedDay);
        } else {
          this.getList(this.myDate);
        }
      },
      PreMonth: function (date, isChosedDay = true) {
        date = timeUtil.dateFormat(date);
        this.myDate = timeUtil.getOtherMonth(this.myDate, "preMonth");
        this.$emit("changeMonth", timeUtil.dateFormat(this.myDate));
        if (isChosedDay) {
          this.getList(this.myDate, date, isChosedDay);
        } else {
          this.getList(this.myDate);
        }
      },
      NextMonth: function (date, isChosedDay = true) {
        date = timeUtil.dateFormat(date);
        this.myDate = timeUtil.getOtherMonth(this.myDate, "nextMonth");
        this.$emit("changeMonth", timeUtil.dateFormat(this.myDate));
        if (isChosedDay) {
          this.getList(this.myDate, date, isChosedDay);
        } else {
          this.getList(this.myDate);
        }
      },
      forMatArgs: function () {
        let markDate = this.markDate;
        let markDateMore = this.markDateMore;
        markDate = markDate.map(k => {
          return timeUtil.dateFormat(k);
        });
        markDateMore = markDateMore.map(k => {
          k.date = timeUtil.dateFormat(k.date);
          return k;
        });
        return [markDate, markDateMore];
      },
      getList: function (date, chooseDay, isChosedDay = true) {
        const [markDate, markDateMore] = this.forMatArgs();
        this.dateTop = `${date.getFullYear()}年${date.getMonth() + 1}月`;
        let arr = timeUtil.getMonthList(this.myDate);
        for (let i = 0; i < arr.length; i++) {
          let markClassName = "";
          let k = arr[i];
          if(this.todayDate == k.date){
            k.chooseDay = true;
          }else{
            k.chooseDay = false;
          }
          const nowTime = k.date;
          const t = new Date(nowTime).getTime() / 1000;
          //看每一天的class
          for (const c of markDateMore) {
            if (c.date === nowTime) {
              markClassName = c.className || "";
            }
          }
          //标记选中某些天 设置class
          k.markClassName = markClassName;
          k.isMark = markDate.indexOf(nowTime) > -1;
          //无法选中某天
          k.dayHide = t < this.agoDayHide || t > this.futureDayHide;
          if (k.isToday) {
            this.$emit("isToday", nowTime);
          }
          let flag = !k.dayHide && k.otherMonth === "nowMonth";
          if (chooseDay && chooseDay === nowTime && flag) {
            this.$emit("choseDay", nowTime);
            this.historyChose.push(nowTime);
            k.chooseDay = true;
          } else if (
            this.historyChose[this.historyChose.length - 1] === nowTime &&
            !chooseDay &&
            flag
          ) {
            k.chooseDay = true;
          }
        }
        this.list = arr;
      }
    },
    mounted() {
      this.getList(this.myDate);
    },
    watch: {
      markDate: {
        handler(val, oldVal) {
          this.getList(this.myDate);
        },
        deep: true
      },
      markDateMore: {
        handler(val, oldVal) {
          this.getList(this.myDate);
        },
        deep: true
      },
      agoDayHide: {
        handler(val, oldVal) {
          this.getList(this.myDate);
        },
        deep: true
      },
      futureDayHide: {
        handler(val, oldVal) {
          this.getList(this.myDate);
        },
        deep: true
      },
      sundayStart: {
        handler(val, oldVal) {
          this.intStart();
          this.getList(this.myDate);
        },
        deep: true
      }
    }
  };
</script>
<style scoped>
  @media screen and (min-width: 460px) {
    .wh_item_date:hover {
      /*background: #71c7a5;*/
      cursor: pointer;
    }
  }

  * {
    margin: 0;
    padding: 0;
  }

  .wh_container {
    max-width: 100%;
    /*margin: auto;*/
  }

  li {
    list-style-type: none;
  }

  .wh_top_change {
    display: flex;
  }

  .wh_top_change li {
    cursor: pointer;
    display: flex;
    color: #1E90FF;
    font-size: 18px;
    flex: 1;
    justify-content: center;
    align-items: center;
    height: 60px;
  }

  .wh_top_change .wh_content_li {
    cursor: auto;
    flex: 2;
  }

  .wh_content_all {
    font-family: -apple-system, BlinkMacSystemFont, "PingFang SC",
    "Helvetica Neue", STHeiti, "Microsoft Yahei", Tahoma, Simsun, sans-serif;
    background-color: white;
    width: 100%;
    overflow: hidden;
    padding-bottom: 8px;
    border: 1px solid #ebebeb;
    /*box-shadow: 5px 5px 5px #ebebeb;*/
    box-shadow: 0px 0px 5px 5px #ebebeb;
  }

  .wh_content {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: center;
    padding: 0 2% 0 2%;
    width: 100%;
  }
  .wh_item_point{
    width: 6px;
    height: 6px;
    border-radius: 6px;
    background-color: #1E90FF;
  }
  .wh_content:first-child .wh_content_item_tag,
  .wh_content:first-child .wh_content_item {
    color: #ddd;
    font-size: 16px;
  }

  .wh_content_item,
  wh_content_item_tag {
    font-size: 15px;
    width: 13.4%;
    text-align: center;
    color: #fff;
    position: relative;
  }

  .wh_content_item {
    display: flex;
    display: -webkit-flex;
    flex-direction: column;
    -webkit-flex-direction: column;
    justify-content: flex-start;
    -webkit-align-content: flex-start;
    align-items: center;
    -webkit-align-items: center;
    height: 90px;
  }

  .wh_top_tag {
    width: 50px;
    height: 40px;
    line-height: 40px;
    margin: auto;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #1E90FF;
  }

  .wh_item_date {
    width: 40px;
    height: 40px;
    line-height: 40px;
    /*margin: auto;*/
    margin: 1px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #bfbfbf;
  }

  .wh_left {
    width: 12px;
    height: 12px;
    border-top: 2px solid #1E90FF;
    border-left: 2px solid #1E90FF;
    transform: rotate(-45deg);
  }

  .wh_left:active,
  .wh_right:active {
    border-color: #ddd;
  }

  .wh_right {
    width: 12px;
    height: 12px;
    border-top: 2px solid #1E90FF;
    border-right: 2px solid #1E90FF;
    transform: rotate(45deg);
  }

  .wh_content_item > .wh_isMark {
    margin: auto;
    border-radius: 100px;
    background: blue;
    z-index: 2;
  }

  .wh_content_item .wh_other_dayhide {
    color: #bfbfbf;
  }

  .wh_content_item .wh_want_dayhide {
    color: #bfbfbf;
  }

  .wh_content_item .wh_isToday {
    background: #589df8;
    border-radius: 100px;
  }

  .wh_content_item .wh_chose_day {
    background: #589df8;
    border-radius: 100px;
  }
</style>
