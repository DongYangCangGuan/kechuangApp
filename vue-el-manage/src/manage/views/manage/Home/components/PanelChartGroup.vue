<template>
    <div class="panel-group">
        <el-container class="menu_container">
            <el-header>
                <el-form
                    :inline="true"
                    :model="searchForm"
                    class="demo-form-inline"
                >
                    <el-row>
                        <div class="float-left"
                             v-show="searchForm.type == '0' && searchForm.dateType == '1' && (chartData.length > 1) && chartData[0].year">
                            <p style="margin: 0px;font-weight: bold;font-size: 12px;">在线人数:</p>
                        </div>
                        <div class="float-left"
                             v-show="searchForm.type == '1' && searchForm.dateType == '1' && (chartData.length > 1) && chartData[0].year">
                            <p style="margin: 0px;font-weight: bold;font-size: 12px;">在线人次:</p>
                        </div>
                        <div class="float-left"
                             v-show="searchForm.dateType == '1' && (chartData.length > 1) && chartData[0].year">
                            <p style="margin: 0px;color: #1A94E6;font-weight: bold;font-size: 12px;"
                               v-for="(item,index) in chartData">
                                {{ item.year }}:{{ item.count }}
                            </p>
                        </div>
                        <div class="float-right">
                            <el-form-item label="查询类型">
                                <el-select
                                    v-model="searchForm.type"
                                    placeholder="请选择"
                                    class="select-width"
                                >
                                    <el-option label="人数" value="0"></el-option>
                                    <el-option label="人次" value="1"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="日期类型">
                                <el-select
                                    v-model="searchForm.dateType"
                                    placeholder="请选择"
                                    class="select-width"
                                >
                                    <el-option label="日" value="0"></el-option>
                                    <el-option label="年" value="1"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item v-if="this.searchForm.dateType === '1'">
                                <el-select
                                    v-model="searchForm.year"
                                    multiple
                                    placeholder="请选择"
                                >
                                    <el-option
                                        v-for="item in yearOptions"
                                        :key="item.endTime"
                                        :label="item.endTime"
                                        :value="item.endTime"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item v-else>
                                <el-date-picker
                                    v-model="searchForm.date"
                                    type="daterange"
                                    align="right"
                                    unlink-panels
                                    :clearable="false"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    :picker-options="pickerOptions">
                                </el-date-picker>
                            </el-form-item>

                            <el-form-item>
                                <el-button
                                    type="primary"
                                    @click="onSubmit"
                                    icon="el-icon-search"
                                    :loading="loading"
                                >搜索
                                </el-button
                                >
                            </el-form-item>
                            <el-form-item>
                                <el-button
                                    type="primary"
                                    @click="sync"
                                    icon="el-icon-refresh-left"
                                    :loading="syncloading"
                                >同步
                                </el-button
                                >
                            </el-form-item>
                        </div>
                    </el-row>

                </el-form>
            </el-header>
        </el-container>
    </div>
</template>

<script>
export default {
    name: "panel-chart-group",
    props: {
        chartData: {
            type: Array,
            default: () => [],
            required: true
        },
    },
    data() {
        return {
            syncloading: false,
            loading: false,
            yearOptions: [],//年份数组信息
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                },
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            searchForm: {
                type: "0",
                dateType: "0",
                date: [new Date(), new Date()],
                year: [],
                startTime: '',
                endTime: ''
            }
        };
    },
    mounted() {
        this.getYears();
    },
    methods: {
        onSubmit() {
            this.loading = true;
            if (this.searchForm.dateType == '1' && (this.searchForm.year == null || !this.searchForm.year.length > 0)) {
                this.alertMessage('请选择年份', 'warning');
                this.loading = false;
                return;
            }
            this.$emit("onSubmit", {...this.searchForm});
        },
        closeLoading() {
            this.loading = false;
        },
        /*formatDateToStr(date) {
            if (date == undefined) {
                return "";
            }
            let time = new Date(date);
            let y = time.getFullYear();
            let m = time.getMonth() + 1;
            let d = time.getDate();
            return y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
        },*/
        //========获取下拉框 年份信息=========
        getYears() {
            let params = {
                className: "HomePage",
                method: "getAllYear",
                param: {}
            };
            this.$nextTick(async () => {
                let res = await this.$store.dispatch("http/post", params);
                this.yearOptions = res;
            });
        },
        sync() {
            this.syncloading = true;
            let params = {
                className: "HomePage",
                method: "syncConductData",
                param: ''
            };
            this.$nextTick(async () => {
                let res = await this.$store.dispatch("http/post", params);
                if (res) {
                    this.alertMessage("同步成功", "success");
                    this.syncloading = false;
                } else {
                    this.alertMessage("同步失败", "error");
                    this.syncloading = false;
                }
            });
        },
        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
            });
        },
    }
};
</script>

<style lang="scss" scoped>
.float-right {
    text-align: right;
    padding: 10px 10px 0;
    display: inline-block;
    float: right;
}

.float-left {
    text-align: left;
    padding: 4px 4px 0;
    display: inline-block;
    float: left;
}

.select-width {
    width: 100px;
}
</style>
