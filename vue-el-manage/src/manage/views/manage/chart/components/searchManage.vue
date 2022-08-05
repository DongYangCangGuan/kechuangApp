<template>
    <div class="pageContainer">
        <div class="left">
            <div class="left_top">
                <div>
                    <p class="p1">7日&nbsp;|&nbsp;累计活跃用户数</p>
                    <p class="p2">2021-10-01至2021-10-07过去七天</p>
                    <p class="p3">合计</p>
                    <p class="p4"><span style="font-size: 38px;">14235</span> 人 </p>
                </div>
                <div>
                    <p class="p1">当月&nbsp;|&nbsp;累计活跃用户数</p>
                    <p class="p2">2021-10-01至2021-10-30过去30天</p>
                    <p class="p3">合计</p>
                    <p class="p4"><span style="font-size: 38px;">14235</span> 人 </p>
                </div>
            </div>
            <div class="left_foot">
                <div class="chartbox">
                    <div class="dataform">
                        <div style="font-size: 20px;">当月&nbsp;|&nbsp;累计活跃用户数</div>
                        <div style="color: #9B9B9B;">2021-10-01至2021-10-30过去30天</div>
                        <div class="form_footer">
                            <div class="footer_left">
                                <div style="font-size: 20px;">10-30（一）</div>
                                <div class="rate_form">
                                    <div>  <span style="font-size: 36px;">242</span> 人 </div>
                                    <div style="margin-left: 10%;">
                                        <div>
                                            环比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;6.42%</span>
                                        </div>
                                        <div style="margin-top: 5px;">
                                            同比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;3.42%</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="footer_right">
                                <p>合计 <span>&nbsp;&nbsp;133</span> 人 </p>
                                <p>均值 <span>&nbsp;&nbsp;213</span> 人 </p>
                            </div>
                        </div>
                    </div>
                    <div class="chartform">
                        <div id="line" style="width: 100%;height: 100%;"></div>
                    </div>
                </div>
                <div class="chartbox">
                    <div class="dataform">
                        <div style="font-size: 20px;">当月&nbsp;|&nbsp;累计活跃用户数</div>
                        <div style="color: #9B9B9B;">2021-10-01至2021-10-30过去30天</div>
                        <div class="form_footer">
                            <div class="footer_left">
                                <div style="font-size: 20px;">10-30（一）</div>
                                <div class="rate_form">
                                    <div>  <span style="font-size: 36px;">242</span> 人 </div>
                                    <div style="margin-left: 10%;">
                                        <div>
                                            环比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;6.42%</span>
                                        </div>
                                        <div style="margin-top: 5px;">
                                            同比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;3.42%</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="footer_right">
                                <p>合计 <span>&nbsp;&nbsp;133</span> 人 </p>
                                <p>均值 <span>&nbsp;&nbsp;213</span> 人 </p>
                            </div>
                        </div>
                    </div>
                    <div class="chartform">
                        <div id="line2" style="width: 100%;height: 100%;"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="right">
            <div class="left_top">
                <div>
                    <p class="p1">7日&nbsp;|&nbsp;累计新增用户数</p>
                    <p class="p2">2021-10-01至2021-10-07过去七天</p>
                    <p class="p3">合计</p>
                    <p class="p4"><span style="font-size: 38px;">14235</span> 人 </p>
                </div>
                <div>
                    <p class="p1">当月&nbsp;|&nbsp;累计新增用户数</p>
                    <p class="p2">2021-10-01至2021-10-07过去七天</p>
                    <p class="p3">合计</p>
                    <p class="p4"><span style="font-size: 38px;">14235</span> 人 </p>
                </div>
            </div>
            <div class="left_foot">
                <div class="chartbox">
                    <div class="dataform">
                        <div style="font-size: 20px;">当月&nbsp;|&nbsp;累计活跃用户数</div>
                        <div style="color: #9B9B9B;">2021-10-01至2021-10-30过去30天</div>
                        <div class="form_footer">
                            <div class="footer_left">
                                <div style="font-size: 20px;">10-30（一）</div>
                                <div class="rate_form">
                                    <div>  <span style="font-size: 36px;">242</span> 人 </div>
                                    <div style="margin-left: 10%;">
                                        <div>
                                            环比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;6.42%</span>
                                        </div>
                                        <div style="margin-top: 5px;">
                                            同比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;3.42%</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="footer_right">
                                <p>合计 <span>&nbsp;&nbsp;133</span> 人 </p>
                                <p>均值 <span>&nbsp;&nbsp;213</span> 人 </p>
                            </div>
                        </div>
                    </div>
                    <div class="chartform">
                        <div id="bar" style="width: 100%;height: 100%;"></div>
                    </div>
                </div>
                <div class="chartbox">
                    <div class="dataform">
                        <div style="font-size: 20px;">当月&nbsp;|&nbsp;累计活跃用户数</div>
                        <div style="color: #9B9B9B;">2021-10-01至2021-10-30过去30天</div>
                        <div class="form_footer">
                            <div class="footer_left">
                                <div style="font-size: 20px;">10-30（一）</div>
                                <div class="rate_form">
                                    <div>  <span style="font-size: 36px;">242</span> 人 </div>
                                    <div style="margin-left: 10%;">
                                        <div>
                                            环比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;6.42%</span>
                                        </div>
                                        <div style="margin-top: 5px;">
                                            同比
                                            <i class="el-icon-caret-bottom" style="color: red;"></i>
                                            <i class="el-icon-caret-top" style="color: yellow;"></i>
                                            <span>&nbsp;&nbsp;3.42%</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="footer_right">
                                <p>合计 <span>&nbsp;&nbsp;133</span> 人 </p>
                                <p>均值 <span>&nbsp;&nbsp;213</span> 人 </p>
                            </div>
                        </div>
                    </div>
                    <div class="chartform">
                        <div id="bar2" style="width: 100%;height: 100%;"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
import echarts from 'echarts'
import "../../../../../../node_modules/echarts/map/js/china"; // 引入中国地图数据

export default {
    name: 'search-Manage',
    data() {
        return {
            opinionData: [
                {name: 'a',value: 10},
                {name: 'b',value: 20}
            ],
            opinion: ['a','b']
        }
    },
    mounted() {
        this.drawLine();
        this.drawLine2();
        this.drawBar();
        this.drawBar2();
    },
    methods: {
        drawLine() {
            let myChart = echarts.init(document.getElementById('line'));
            myChart.setOption({
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        lineStyle: {
                            type: 'dashed'
                        }
                    }
                },
                grid: [
                    {
                        top: '5%',
                        left: '10%',
                        right: '8%',
                        bottom: '25%'
                    }
                ],
                xAxis: {
                    type: 'category',
                    data: ['10-01', '10-02', '10-03', '10-04', '10-05', '10-06', '10-07','10-08', '10-09', '10-10',
                        '10-11', '10-12', '10-13', '10-14', '10-15','10-16', '10-17', '10-18', '10-19', '10-20', '10-21',
                        '10-22','10-23', '10-24', '10-25', '10-26', '10-27', '10-28', '10-29', '10-30'],
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                },
                yAxis: {
                    type: 'value',
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                    splitLine: {
                        lineStyle:{
                            type:'dashed'
                        }
                    }
                },
                series: [
                    {
                        name: '次数',
                        data: [820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888,
                            820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888],
                        type: 'line',
                        symbol:'circle',
                        symbolSize:6,
                        itemStyle : {
                            normal : {
                                color: '#4A90F6',
                                lineStyle:{
                                    color:'#4d98d4'
                                }
                            }
                        }
                    }
                ]
            })
        },
        drawLine2() {
            let myChart = echarts.init(document.getElementById('line2'));
            myChart.setOption({
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        lineStyle: {
                            type: 'dashed'
                        }
                    }
                },
                grid: [
                    {
                        top: '5%',
                        left: '10%',
                        right: '8%',
                        bottom: '25%'
                    }
                ],
                xAxis: {
                    type: 'category',
                    data: ['10-01', '10-02', '10-03', '10-04', '10-05', '10-06', '10-07','10-08', '10-09', '10-10',
                        '10-11', '10-12', '10-13', '10-14', '10-15','10-16', '10-17', '10-18', '10-19', '10-20', '10-21',
                        '10-22','10-23', '10-24', '10-25', '10-26', '10-27', '10-28', '10-29', '10-30'],
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                },
                yAxis: {
                    type: 'value',
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                    splitLine: {
                        lineStyle:{
                            type:'dashed'
                        }
                    }
                },
                series: [
                    {
                        name: '次数',
                        data: [820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888,
                            820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888],
                        type: 'line',
                        symbol:'circle',
                        symbolSize:8,
                        itemStyle : {
                            normal : {
                                color: '#4A90F6',
                                lineStyle:{
                                    color:'#4d98d4'
                                }
                            }
                        }
                    }
                ]
            })
        },
        drawBar() {
            let myChart = echarts.init(document.getElementById('bar'));
            myChart.setOption({
                color: ['#4794FE'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        lineStyle: {
                            type: 'dashed'
                        }
                    }
                },
                grid: [
                    {
                        top: '5%',
                        left: '10%',
                        right: '8%',
                        bottom: '25%'
                    }
                ],
                xAxis: {
                    type: 'category',
                    data: ['10-01', '10-02', '10-03', '10-04', '10-05', '10-06', '10-07','10-08', '10-09', '10-10',
                        '10-11', '10-12', '10-13', '10-14', '10-15','10-16', '10-17', '10-18', '10-19', '10-20', '10-21',
                        '10-22','10-23', '10-24', '10-25', '10-26', '10-27', '10-28', '10-29', '10-30'],
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                },
                yAxis: {
                    type: 'value',
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                    splitLine: {
                        lineStyle:{
                            type:'dashed'
                        }
                    }
                },
                series: [
                    {
                        data: [820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888,
                            820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888],
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                barBorderRadius:[5, 5, 0, 0]
                            }
                        },
                        barWidth : 10,
                    }
                ]
            })
        },
        drawBar2() {
            let myChart = echarts.init(document.getElementById('bar2'));
            myChart.setOption({
                color: ['#4794FE'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        lineStyle: {
                            type: 'dashed'
                        }
                    }
                },
                grid: [
                    {
                        top: '5%',
                        left: '10%',
                        right: '8%',
                        bottom: '25%'
                    }
                ],
                xAxis: {
                    type: 'category',
                    data: ['10-01', '10-02', '10-03', '10-04', '10-05', '10-06', '10-07','10-08', '10-09', '10-10',
                        '10-11', '10-12', '10-13', '10-14', '10-15','10-16', '10-17', '10-18', '10-19', '10-20', '10-21',
                        '10-22','10-23', '10-24', '10-25', '10-26', '10-27', '10-28', '10-29', '10-30'],
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                },
                yAxis: {
                    type: 'value',
                    axisLine: {
                        show: false
                    },
                    axisTick:{
                        show:false
                    },
                    splitLine: {
                        lineStyle:{
                            type:'dashed'
                        }
                    }
                },
                series: [
                    {
                        data: [820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888,
                            820, 932, 901, 934, 1290, 1330, 600, 800, 764, 658, 1111, 1200, 987, 777, 888],
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                barBorderRadius:[5, 5, 0, 0]
                            }
                        },
                        barWidth : 10,
                    }
                ]
            })
        },
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$dark_gray: #889aa4;

* {
    margin: 0;
    padding: 0;
}

.pageContainer {
    display: flex;
    justify-content: space-around;
}
.left,
.right{
    width: 49%;
    height: 120vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .left_top {
        width: 100%;
        height: 20%;
        display: flex;
        justify-content: space-between;
        div {
            padding: 2% 0 3% 4%;
            box-sizing: border-box;
            width: 49%;
            height: 100%;
            background-color: #fff;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around;
            align-items: center;
            p {
                width: 100%;
            }
            .p1 {
                font-size: 20px;
            }
            .p2 {
                color: #9B9B9B;
            }
            .p3 {
                //font-weight: 700;
                margin-top: 10px;
            }
            .p4 {
                padding-left: 22%;
            }

        }

    }
    .left_foot {
        width: 100%;
        height: 78%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .chartbox {
            width: 100%;
            height: 49%;
            background-color: #fff;
            .dataform {
                width: 100%;
                height: 58%;
                padding: 3% 3% 2% 5%;
                //background-color: pink;
                display: flex;
                flex-direction: column;
                justify-content: space-around;
                .form_footer {
                    width: 100%;
                    height: 60%;
                    padding: 1% 0;
                    box-sizing: border-box;
                    //background-color: skyblue;
                    display: flex;
                    .footer_left {
                        width: 70%;
                        height: 100%;
                        //background-color: skyblue;
                        display: flex;
                        flex-direction: column;
                        justify-content: space-around;
                        .rate_form {
                            display: flex;
                            align-items: center;
                        }
                    }
                    .footer_right {
                        width: 30%;
                        height: 100%;
                        //background-color: brown;
                        box-sizing: border-box;
                        border-left: 3px solid #E8E8E8;
                        display: flex;
                        flex-direction: column;
                        justify-content: space-around;
                        align-items: center;
                        color: #666666;
                        span {
                            font-size: 22px;
                        }
                    }
                }
            }
            .chartform {
                width: 100%;
                height: 42%;
            }
        }

    }
}



</style>
