<template>
    <div class="currentDiv" v-loading="loading.form">
        <!--第一部分-->
        <div class="firstPortion portion">
            <!--标题-->
            <div class="header">
                <span class="icon-header"></span>
                <span>数据统计</span>
            </div>

            <!--运营数据-->
            <div class="content">
                <el-row>
                    <div class="header-content">运营数据</div>
                </el-row>

                <el-row class="panel-group">
                    <!--报告总数-->
                    <el-col :xs="4" :sm="4" :lg="4" class="card-panel-col reportNum">
                        <div class="card-panel">
                            <div class="card-panel-description">
                                <div class="card-panel-text">报告总数</div>
                                <span class="card-panel-num">{{ operationalData.reportNum }}个</span>
                            </div>
                        </div>
                    </el-col>

                    <!--本月新增报告-->
                    <el-col :xs="4" :sm="4" :lg="4" class="card-panel-col monthAddReport">
                        <div class="card-panel">
                            <div class="card-panel-description">
                                <div class="card-panel-text">本月新增报告</div>
                                <span class="card-panel-num">{{ operationalData.monthAddReport }}个</span>
                            </div>
                        </div>
                    </el-col>

                    <!--会员单位-->
                    <el-col :xs="4" :sm="4" :lg="4" class="card-panel-col memberUnit">
                        <div class="card-panel">
                            <div class="card-panel-description">
                                <div class="card-panel-text">会员单位</div>
                                <span class="card-panel-num">{{ operationalData.memberUnit }}</span>
                            </div>
                        </div>
                    </el-col>

                    <!--用户数-->
                    <el-col :xs="4" :sm="4" :lg="4" class="card-panel-col userNum">
                        <div class="card-panel">
                            <div class="card-panel-description">
                                <div class="card-panel-text">用户数</div>
                                <span class="card-panel-num">{{ operationalData.userNum }}人</span>
                            </div>
                        </div>
                    </el-col>

                    <!--报告阅读次数-->
                    <el-col :xs="4" :sm="4" :lg="4" class="card-panel-col reportReadNum">
                        <div class="card-panel">
                            <div class="card-panel-description">
                                <div class="card-panel-text">报告阅读次数</div>
                                <span class="card-panel-num">{{ operationalData.reportReadNum }}次</span>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>

        <!--第二部分-->
        <div class="secondPortion portion">
            <!--标题-->
            <div class="header">
                <span class="icon-header"></span>
                <span>用户活跃度</span>
                <div class="header-right">
                    <div>
                        <el-span :class="{'checkTimeSpan': checkedTime == 1}" @click="getDateThisYear">今年</el-span>
                        <el-span :class="{'checkTimeSpan': checkedTime == 2}" @click="getDateLastYear">去年</el-span>
                        <el-span :class="{'checkTimeSpan': checkedTime == 3}" @click="getDateRecentlySevenDay">最近7日
                        </el-span>
                    </div>
                    <div>
                        <el-date-picker
                            v-model="startAndEndTime"
                            type="daterange"
                            align="right"
                            unlink-panels
                            :clearable="false"
                            range-separator="~"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :picker-options="pickerOptions"
                            size="small"
                            readonly
                        >
                        </el-date-picker>
                    </div>
                </div>
            </div>

            <!--echart-->
            <div class="content">
                <el-row>
                    <div class="header-content">数据统计表格</div>
                </el-row>
                <line-chart ref="lineChart" v-loading="lineChartLoading" :chartData="chartData"/>
            </div>
        </div>
    </div>
</template>

<script>
import LineChart from "./components/LineChart";

export default {
    name: "home",
    components: {
        LineChart
    },
    data() {
        return {
            firstTableData: [],
            operationalData: {//运营数据
                reportReadNum: 0,//用来存储报告阅读次数
                userNum: 0, //用来存储用户数
                memberUnit: 0,  //用来存储会员单位数
                monthAddReport: 0, //用来存储本月新增报告数
                reportNum: 0, //用来存储报告总数
            },

            startAndEndTime: '',
            checkedTime: 0, //选中的日期，默认第一个
            according: '',
            loading: {
                form: false
            },
            chartData: [],
            lineChartLoading: false,
        }
    },
    //钩子函数
    created() {
        this.getPageMessage();//获取页面信息
    },

    methods: {
        //获取页面信息
        getPageMessage() {
            this.getOperationalData();//获取运营数据
            this.getDateThisYear();//默认今年
        },

        //获取运营数据
        getOperationalData() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Home',
                    method: 'getOperationalData',
                    param: {}
                }
                this.operationalData = await this.$store.dispatch('http/post', params);
            });
        },

        //获取图表信息
        getChartData() {
            this.loading.form = true;
            this.$nextTick(async () => {
                const params = {
                    className: "Home",
                    method: 'selectUserAndMemberMonthOrDayNumByDate',
                    param: {
                        startTime: this.startAndEndTime[0],
                        endTime: this.startAndEndTime[1],
                        according: this.according
                    }
                };

                this.chartData = await this.$store.dispatch('http/post', params);
                this.loading.form = false;
            });
        },

        //今年
        getDateThisYear() {
            this.checkedTime = 1;
            this.getYearTime(0);
            this.according = 'year';
            this.getChartData();
        },

        //去年
        getDateLastYear() {
            this.checkedTime = 2;
            this.getYearTime(-1);
            this.according = 'year';
            this.getChartData();
        },

        //最近7日(本周)
        getDateRecentlySevenDay() {
            this.checkedTime = 3;
            this.getDayTime(0);
            this.according = 'week';
            this.getChartData();
        },

        //获取年的信息
        getYearTime(n) {
            let date = new Date();
            let year = date.getFullYear() + Number(n);
            let startTime = year + '-01-01';
            let endTime = year + '-12-31';
            this.startAndEndTime = [startTime, endTime];
        },

        //获取周的信息
        getDayTime(n) {
            let now = new Date();
            let nowTime = now.getTime();
            let day = now.getDay();
            let longTime = 24 * 60 * 60 * 1000;
            let m = longTime * 7 * (n || 0);
            let startTime = new Date(nowTime - (day - 1) * longTime + m);
            let endTime = new Date(nowTime + (7 - day) * longTime + m);
            this.startAndEndTime = [startTime, endTime];
        }
    },
}
</script>

<style lang="scss" scoped>
.portion {
    padding: 0px 45px 0px 35px;
}

.firstPortion {
    height: 250px;
    border: 2px solid rgba(204, 204, 204, 0.3);
    border-radius: 10px;
    box-shadow: 0px 1px 3px rgba(34, 25, 25, 0.2);
}


.header {
    padding-top: 20px;
    color: #000000;
    font-size: 16px;
    font-weight: bold;
    display: flex;
    align-items: center;
}

.icon-header {
    display: inline-block;
    width: 8px;
    height: 34px;
    border-radius: 100px;
    background-color: #FF944B;
    margin-right: 6px;
}

.content {
    padding-left: 14px;
}

.header-right {
    height: 100%;
    display: flex;
    align-items: center;
    margin-left: auto;
    font-size: smaller;
    font-weight: 300;
}

.header-right div {
    display: flex;
    margin-left: 10px;
}

.header-right div:last-child {
    width: 300px;
    height: 30px;
}

.header-right div el-span {
    width: 65px;
    padding: 6.3px 0px;
    display: flex;
    justify-content: center;
    border: 1px solid #cccccc;
}

.header-right div el-span:not(:first-child) {
    border-left: 0px;
}

.header-content {
    font-size: 13px;
    color: #9595ee;
    margin-bottom: 16px;
}

.panel-group {
    box-sizing: border-box;
}

.card-panel-col {
    border: 1px solid #f7f7f7;
    margin-right: 3.5%;
    border-radius: 6px;
}

.reportNum {
    background-color: #96A1F5;
}

.monthAddReport {
    background-color: #FFA7A7;
}

.memberUnit {
    background-color: #8AB6DE;
}

.userNum {
    background-color: #B08DD6;
}

.reportReadNum {
    background-color: #22D3A7;
    margin-right: 0px;
}

.card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #fff;
    border-color: rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
}

.card-panel-icon-wrapper {
    float: left;
    margin: 14px 0 0 14px;
    padding: 16px;
    transition: all 0.38s ease-out;
    border-radius: 6px;
}

.card-panel-description {
    font-weight: bold;
    text-align: left;
    padding-left: 26px;
}

.card-panel-text {
    line-height: 18px;
    font-size: 13px;
    margin-bottom: 12px;
}

.card-panel-num {
    font-size: 16px;
}

.card-panel-icon {
    float: left;
    font-size: 48px;
}

.checkTimeSpan {
    color: #FFFFFF;
    font-weight: bold;
    border: 1px solid rgb(2, 121, 253);
    background-color: rgb(2, 121, 253);
}

</style>
