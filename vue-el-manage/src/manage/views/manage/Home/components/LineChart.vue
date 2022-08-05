<template>
    <div id="echarts" :class="className" :style="{ height: height, width: width }"/>
</template>

<script>
import echarts from "echarts";
import elementResize from 'element-resize-detector' // 尺寸监听组件

require("echarts/theme/macarons"); // echarts theme

export default {
    name: "line-chart",
    props: {
        className: {
            type: String,
            default: "chart"
        },
        width: {
            type: String,
            default: "100%"
        },
        height: {
            type: String,
            default: "400px"
        },
        chartData: {
            type: Array,
            default: () => [],
            required: true
        }
    },
    data() {
        return {
            chart: null,
            series: [], //显示的数据
            xData: [], //x轴数据
            yData: [], //y轴数据
            barTextList: ['用户数', '会员单位'], //柱状图的名称集合
            yValueMax: 100, //y轴的最大值,默认100
            ySplitNumber: 5, //y轴被平均切次数
        };
    },

    watch: {
        chartData: {
            deep: true,
            handler() {
                this.setOptions();
            }
        }
    },

    mounted() {
        this.initChart();
        let mainChart = document.getElementById('echarts');
        this.chart = echarts.init(mainChart); // 图标ID初始化
        // 初始化element-resize-detector组件
        const elementResize1 = elementResize({
            strategy: 'scroll', // <- 推荐监听滚动，提升性能
            callOnAdd: true // 添加侦听器时是否应调用,默认true
        });
        elementResize1.listenTo(mainChart, function (element) {
            echarts.init(mainChart).resize() // 当元素尺寸发生改变是会触发此事件，刷新图表
        });
    },

    methods: {
        setOptions() {
            this.series = [];
            var maxYdataValue = 0;//保存y轴中的最大值
            this.barTextList.forEach((e, i) => {
                this.xData = [];
                this.yData = [];
                if (e == '会员单位') {
                    const memberPillarCountList = this.chartData.memberPillarCountList;
                    memberPillarCountList.forEach((e, i) => {
                        this.xData.push(e.xData);
                        this.yData.push(e.yData);
                    });
                } else {
                    const userPillarCountList = this.chartData.userPillarCountList;
                    userPillarCountList.forEach((e, i) => {
                        this.xData.push(e.xData);
                        this.yData.push(e.yData);
                    });
                }

                //得到最大值
                if (this.yData != null && this.yData.length > 0) {
                    let maxValue = this.yData.reduce((a, b) => {
                        return b > a ? b : a;
                    });
                    maxYdataValue = maxValue > maxYdataValue ? maxValue : maxYdataValue;
                }

                const serie = {
                    name: e,
                    smooth: true,
                    type: 'bar', //柱状图
                    data: this.yData,
                    barWidth: '10%', //柱状体的宽度
                    itemStyle: {barBorderRadius: [20, 20, 0, 0]},//设置圆角
                    animationDuration: 2000,
                    animationEasing: "quadraticOut",
                }
                this.series.push(serie);
            });

            //设置y轴的最大值
            this.yValueMax = Math.pow(10, ('' + maxYdataValue).length);

            this.chart.setOption(
                {
                    xAxis: { //x轴
                        type: 'category',
                        data: this.xData,
                        offset: 15,//文字与轴的距离
                        // boundaryGap: false,
                        axisLabel: {
                            interval: 0,
                            textStyle: {
                                color: '#000',
                                fontSize: 10
                            },
                            margin: 8
                        },
                        axisLine: {
                            show: false,
                            lineStyle: {
                                color: '#CCCCD1',
                                width: 2,
                                type: 'dotted'
                            }
                        },
                        axisTick: {
                            show: false
                        }
                    },
                    yAxis: { //y轴
                        type: 'value',
                        name: "单位（次）",//y轴上方的单位
                        nameTextStyle: {//y轴上方单位的颜色
                            color: '#000'
                        },
                        axisLabel: { //y轴的坐标文字
                            show: true,
                            textStyle: {
                                color: '#000' //文字的颜色
                            },

                        },
                        max: this.yValueMax,
                        min: 0,
                        splitNumber: this.ySplitNumber,//min-max值之间切割次数
                        offset: 5,//文字与轴的距离
                        axisLine: {
                            show: false,
                            lineStyle: {
                                color: 'rgb(2,121,253)'
                            }
                        },
                        axisTick: {
                            show: false,
                        },
                        splitLine: {  //坐标在grid区域的分割线
                            lineStyle: { //设置分割线的样式(图表横线颜色)
                                color: '#CCCCD1',
                                width: 2,
                                type: 'dotted'
                            }
                        }
                    },
                    color: ['rgb(255,148,75)', 'rgb(167,109,255)'],
                    legend: { //图例
                        data: this.barTextList,
                        bottom: 10,
                        textStyle: {
                            color: '#000',
                        },
                        itemWidth: 14,
                        itemHeight: 14,
                        itemGap: 25,
                    },
                    grid: { //图表和父盒子之间的距离
                        left: '15',
                        right: '0%',
                        bottom: '15%',
                        top: '15%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {},
                        }
                    },
                    tooltip: {
                        trigger: "axis",
                        axisPointer: {
                            type: "cross",
                            crossStyle: {
                                color: 'rgb(2,121,253)'
                            }
                        },
                        padding: [5, 10]
                    },
                    series: this.series,
                },
                true
            );

            let _this = this;
            this.chart.on('legendselectchanged', function (params) {
                let select_key = Object.keys(params.selected);
                let select_value = Object.values(params.selected);
                _this.$emit("changeCount", {
                    key: select_key,
                    value: select_value
                });
            });
        },

        initChart() {
            this.chart = echarts.init(this.$el, "macarons");
            this.setOptions();
        }
    }
};
</script>
