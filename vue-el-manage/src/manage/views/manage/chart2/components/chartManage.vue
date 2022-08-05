<template>
    <div class="pageContainer">
        <div class="left">
            <div class="left_top">
                <div>
                    <p class="title">累计访问次数</p>
                    <p class="content">
                        <span style="font-size:52px;margin-right: 10px;">14235</span> 次 </p>
                </div>
                <div>
                    <p class="title">产品浏览数</p>
                    <p class="content">
                        <span style="font-size:52px;margin-right: 10px;">14235</span> 次 </p>
                </div>
            </div>
            <div class="left_foot">
                <div id="map" style="width: 100%;height: 100%;"></div>
            </div>
        </div>
        <div class="right">
            <div>
                <div id="pie" style="width: 100%;height: 100%;"></div>
            </div>
            <div>
                <div id="line" style="width: 100%;height: 100%;"></div>
            </div>
            <div style="margin-top: 1%;">
                <div id="bar1" style="width: 100%;height: 100%;"></div>
            </div>
            <div style="margin-top: 1%;">
                <div id="bar2" style="width: 100%;height: 100%;"></div>
            </div>
        </div>
    </div>
</template>

<script>
import echarts from 'echarts'
import "../../../../../../node_modules/echarts/map/js/china"; // 引入中国地图数据

export default {
    name: 'chart-Manage',
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
        this.drawPie();
        this.drawLine();
        this.drawGPBar();
        this.drawConfirmBar();
        this.drawMap();
    },
    methods: {
        drawPie() {
            let myChart = echarts.init(document.getElementById('pie'));
            myChart.setOption({
                color: ['#65CEFE','#FFCB5B','#FF6666'],
                title: {
                    text: '数据统计',
                    left: '8%',
                    top: '8%',
                    // fontSize: 30
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    top: '5%',
                    right: '5%',
                    orient: 'vertical',
                    textStyle:{
                        color:'#A8A8A8'
                    }
                },
                series: [
                    {
                        type: 'pie',
                        radius: ['30%', '58%'],
                        center: ['50%','63%'],
                        avoidLabelOverlap: false,
                        label: {
                            show: false,
                            position: 'center'
                        },
                        // emphasis: {
                        //     label: {
                        //         show: true,
                        //         fontSize: '40',
                        //         fontWeight: 'bold'
                        //     }
                        // },
                        labelLine: {
                            show: false
                        },
                        data: [
                            { value: 1048, name: '当月转发量' },
                            { value: 735, name: '当月阅读量' },
                            { value: 580, name: '其他' }
                        ]
                    }
                ]
            })
        },
        drawLine() {
            let myChart = echarts.init(document.getElementById('line'));
            myChart.setOption({
                title: {
                    text: '产品发布',
                    subtext: '近一年发布数量',
                    left: '5%',
                    top: '5%'
                },
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
                        top: '40%',
                        left: '15%',
                        bottom: '15%'
                    }
                ],
                color:['#968DCF'],
                legend: {
                    show: true,
                    top: '25%',
                    right: '5%',
                    textStyle:{
                        color:'#A8A8A8'
                    }
                },
                xAxis: {
                    type: 'category',
                    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月','8月', '9月', '10月', '11月', '12月'],
                    axisLine: {
                        lineStyle: {
                            color: '#A8A8A8',
                            width:'1'
                        }
                    },
                    axisTick:{
                        show:false
                    },
                },
                yAxis: {
                    name: '数量（次）',
                    type: 'value',
                    axisLine: {
                        lineStyle: {
                            color: '#A8A8A8',
                            width:'1'
                        }
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
                        data: [820, 932, 901, 934, 1290, 1330, 900,820, 932, 901, 934, 666],
                        type: 'line',
                        symbol: 'none',
                        smooth: true,
                        areaStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                    { offset: 0, color: "#968DCF" },
                                    { offset: 1, color: "#ffffff" }
                                ])
                            }
                        },
                        itemStyle : {
                            normal : {
                                lineStyle:{
                                    color:'#968DCF'
                                }
                            }
                        }
                    }
                ]
            })
        },
        drawGPBar() {
            let myChart = echarts.init(document.getElementById('bar1'));
            myChart.setOption({
                color: ['#65CEFE','#FFCB5B'],
                title: {
                    text: 'GP',
                    left: '5%',
                    top: '5%'
                },
                legend: {
                    top: '5%',
                    right: '5%',
                    orient: 'vertical',
                    textStyle:{
                        color:'#A8A8A8'
                    }
                },
                tooltip: {
                    show: true
                },
                grid: {
                    left: '10%',
                    right: '10%',
                    top: '30%',
                    bottom: '10%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: "{value} 万",
                        },
                        // splitNumber: 5,
                        // boundaryGap: 0,
                        splitLine: {
                            lineStyle:{
                                type:'dashed'
                            }
                        },
                        axisLine: {
                            lineStyle: {
                                color: '#A8A8A8',
                                width:'1'
                            }
                        },
                        axisTick:{
                            show:false
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'category',
                        show: false
                    }
                ],
                series: [
                    {
                        name: '贷款',
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                barBorderRadius:[0, 10, 10, 0]
                            }
                        },
                        barWidth : 40,
                        data: [200]
                    },
                    {
                        name: '募资',
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                barBorderRadius:[0, 10, 10, 0]
                            }
                        },
                        barWidth : 40,
                        data: [320]
                    }
                ]
            })
        },
        drawConfirmBar() {
            let myChart = echarts.init(document.getElementById('bar2'));
            myChart.setOption({
                color: ['#65CEFE','#FFCB5B','#FF6666'],
                title: {
                    text: '创业公司',
                    left: '5%',
                    top: '5%'
                },
                legend: {
                    top: '5%',
                    right: '5%',
                    orient: 'vertical',
                    textStyle:{
                        color:'#A8A8A8'
                    }
                },
                tooltip: {
                    show: true
                },
                grid: {
                    left: '10%',
                    right: '10%',
                    top: '30%',
                    bottom: '10%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: "{value} 万",
                        },
                        // splitNumber: 5,
                        // boundaryGap: 0,
                        splitLine: {
                            lineStyle:{
                                type:'dashed'
                            }
                        },
                        axisLine: {
                            lineStyle: {
                                color: '#A8A8A8',
                                width:'1'
                            }
                        },
                        axisTick:{
                            show:false
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'category',
                        show: false
                    }
                ],
                series: [
                    {
                        name: '贷款',
                        type: 'bar',
                        emphasis: {
                            focus: 'series'
                        },
                        itemStyle: {
                            normal: {
                                barBorderRadius:[0, 10, 10, 0]
                            }
                        },
                        barWidth : 40,
                        data: [200]
                    },
                    {
                        name: '租赁',
                        type: 'bar',
                        emphasis: {
                            focus: 'series'
                        },
                        itemStyle: {
                            normal: {
                                barBorderRadius:[0, 10, 10, 0]
                            }
                        },
                        barWidth : 40,
                        data: [320]
                    },
                    {
                        name: '保险',
                        type: 'bar',
                        emphasis: {
                            focus: 'series'
                        },
                        itemStyle: {
                            normal: {
                                barBorderRadius:[0, 10, 10, 0]
                            }
                        },
                        barWidth : 40,
                        data: [500]
                    }
                ]
            })
        },
        // randomValue() {
        //     return Math.round(Math.random()*1000);
        // },
        drawMap() {
            let myChart = echarts.init(document.getElementById('map'));
            let dataList=[
                {name: '北京市', value: 100},
                {name: '天津', value: 200},
                {name: '上海', value: 300},
                {name: '重庆', value: 400},
                {name: '安徽', value: 400},
                {name: '新疆', value: 500},

            ]
            myChart.setOption({
                title: {
                    text: '用户分布图',
                    subtext: '中国用户分布地点热力图',
                    left: '3%',
                    top: '5%',
                    // fontSize: 30
                },
                tooltip: {
                    formatter:function(params,ticket, callback){
                        return params.seriesName+'<br />'+params.name+'：'+params.value
                    }//数据格式化
                },
                visualMap: {
                    // min: 0,
                    // max: 500,
                    inRange: {
                        color: ['#DBECF6', '#5891B2']//取值范围的颜色
                    },
                    show:false//图注
                },
                geo: {
                    map: 'china',
                    roam: false,//不开启缩放和平移
                    layoutCenter: ['50%', '55%'],//距离左右，上下距离的百分比
                    layoutSize:'105%',
                    // zoom:0.8,//视角缩放比例
                    label: {
                        normal: {
                            show: true,
                            fontSize:'10',
                            color: 'rgba(0,0,0,0.7)'
                        }
                    },
                    //去掉南海诸岛
                    regions: [
                        {
                            name: "南海诸岛",
                            itemStyle: {
                                // 隐藏地图
                                normal: {
                                    opacity: 0, // 为 0 时不绘制该图形
                                }
                            },
                            label: {
                                show: false // 隐藏文字
                            }
                        }
                    ],
                    itemStyle: {
                        normal:{
                            borderColor: '#ffffff'
                        },
                        emphasis:{
                            areaColor: '#F3B329',//鼠标选择区域颜色
                            shadowOffsetX: 0,
                            shadowOffsetY: 0,
                            shadowBlur: 20,
                            borderWidth: 0,
                            shadowColor: '#ffffff'
                        }
                    }
                },
                series : [
                    {
                        name: '信息量',
                        type: 'map',
                        geoIndex: 0,
                        data: dataList
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
.left {
    width: 48%;
    height: 85vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .left_top {
        width: 100%;
        height: 30%;
        display: flex;
        justify-content: space-between;
        div {
            width: 49%;
            height: 100%;
            background-color: #fff;
            .title {
                height: 25%;
                display: flex;
                align-items: end;
                padding-left: 20px;
            }
            .content {
                height: 70%;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            p {
                font-size: 20px;
            }
        }

    }
    .left_foot {
        width: 100%;
        height: 68%;
        background-color: #fff;

    }
}
.right {
    width: 48%;
    height: 85vh;
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    div {
        width: 49%;
        height: 49%;
        background-color: #fff;
    }
}



</style>
