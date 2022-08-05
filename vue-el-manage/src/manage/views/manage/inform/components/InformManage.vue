<template>
    <div>
        <div class="selectBox">
            <el-row :getter="24">
                <el-col :span="6">
                    通知标题：
                    <el-input class="userInput" placeholder="请输入通知标题" v-model="searchData.title" clearable
                              size='small'></el-input>
                </el-col>

                <el-col :span="6">
                    开始日期：
                    <el-date-picker
                        v-model="searchData.startTime"
                        align="right"
                        type="date"
                        placeholder="请选择开始日期"
                        size="small"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        clearable
                        class="userInput">
                    </el-date-picker>
                </el-col>

                <el-col :span="6">
                    截止日期：
                    <el-date-picker
                        v-model="searchData.endTime"
                        type="date"
                        placeholder="请选择截止日期"
                        size="small"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        clearable
                        class="userInput">
                    </el-date-picker>
                </el-col>

                <el-col :span="6">

                    <el-button size='small' icon="el-icon-search" type="primary" @click="handleQuery">
                        查询
                    </el-button>

                    <el-button size='small' icon="el-icon-refresh" type="primary" @click="reset">重置
                    </el-button>
                    <el-button size='small' icon="el-icon-delete" type="primary" @click="deletedetile=true">删除记录
                    </el-button>
                </el-col>

            </el-row>

        </div>

        <div class="container">
            <div class="top">
                <div class="left">基本信息</div>
                <div class="right">
                    <el-button type="success" icon="el-icon-plus" size="small" plain @click='add'>添加</el-button>
                </div>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
                    <el-table-column label="序号" type="index" width="80" align="center"></el-table-column>
                    <el-table-column label="通知标题" prop="title" width="300" align="center" show-overflow-tooltip></el-table-column>
                    <el-table-column label="发布类型" prop="taskType" width="150" align="center">
                        <template slot-scope="scope">
                            <p v-if="scope.row.taskType === 'all'">全局消息</p>
                            <p v-if="scope.row.taskType === 'user'">用户消息</p>
                            <p v-if="scope.row.taskType === '1'">GP</p>
                            <p v-if="scope.row.taskType === '2'">科创</p>
                            <p v-if="scope.row.taskType === '3'">LP</p>
                            <p v-if="scope.row.taskType === '4'">供应商</p>
                            <p v-if="scope.row.taskType === '5'">创业公司</p>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否反馈" prop="isFeedBack" align="center" width="150">
                        <template slot-scope="scope">
                            <p v-if="scope.row.isFeedBack == 0">不需要反馈</p>
                            <p v-if="scope.row.isFeedBack == 1">需要反馈</p>
                        </template>
                    </el-table-column>
                    <el-table-column label="修改人" prop="modifyusername" align="center"></el-table-column>
                    <el-table-column label="修改时间" prop="modifyTime" align="center" width="200">
<!--                        <template slot-scope="scope">-->
<!--                            <span>{{ scope.row.modifyTime }}</span>-->
<!--                        </template>-->
                    </el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" width="320">
                        <template slot-scope="scope">
                            <el-button size="mini" type="info" plain @click="issue(scope.row)"
                                       v-if="scope.row.status == 0">发布
                            </el-button>
                            <el-button size="mini" type="info" plain @click="look(scope.row)" v-else>查看
                            </el-button>
                            <el-button size="mini" type="primary" plain @click="edit(scope.row)">编辑
                            </el-button>
                            <el-button size="mini" type="danger" plain @click="del(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="paginationData.currentPage"
                    :page-sizes="[10,20,30,40,50]"
                    :page-size="paginationData.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="paginationData.total">
                </el-pagination>
            </div>
        </div>
        <el-dialog
            title="提示"
            :visible.sync="deletedetile"
            width="30%"
            :close-on-click-modal="false"
            :before-close="handleClose">
            <span v-for="(item,index) in NotesLis" :key="index">{{item.modifyusername}}删除了{{item.title}}<br/></span>
<!--            <span v-for="(item,index) in NotesLis" :key="index">{{item.modifyusername}}删除了{{item.title}}</span>-->
            <span slot="footer" class="dialog-footer">
                <el-button @click="deletedetile = false">取 消</el-button>
                <el-button type="primary" @click="deletedetile = false">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "inform-manage",
    props: {
        //子页面调用父页面
        onAdd: {
            type: Function,
            default: null
        },
        onEdit: {
            type: Function,
            default: null
        },
        onDetail: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            // 搜索对象
            searchData: {
                title: '',
                taskType: '',
                startTime: '',
                endTime: '',
            },
            deletedetile:false,
            taskTypeOptions: [],
            NotesLis:[],
            firstTableData: [],
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            }
        };
    },

    // 钩子函数
    created() {
        this.getPageMessage();
        this.getFirstTableData(); // 页面创建时获取后台数据
        this.getDelNotesList();
    },

    // 方法
    methods: {
        //加载页面信息
        getPageMessage() {
            this.$nextTick(async () => {
                const params = {
                    className: 'Notes',
                    method: 'selectDictionaryByKindEqTaskType',
                    param: {}
                }
                this.taskTypeOptions = await this.$store.dispatch('http/post', params);
                console.log(this.taskTypeOptions, 'ops');
            });
        },
        //加载页面删除记录信息
        getDelNotesList() {
            this.$nextTick(async () => {
                const params = {
                    className: 'Notes',
                    method: 'getDelNotesList',
                    param: {}
                }
                this.NotesLis = await this.$store.dispatch('http/post', params);
                console.log('aNotesLis',this.NotesLis );
            });
        },


        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
            });
        },

        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                })
                .catch(_ => {});
        },

        //带条件查询
        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        //验证时间
        dateVerify() {
            const startTime = this.searchData.startTime;
            const endTime = this.searchData.endTime;
            if ((startTime == null || startTime == '') && (startTime == null || endTime == '')) {
                return true;
            } else if (startTime == null || startTime == '' || endTime == null || endTime == '') {
                this.$message({
                    message: '使用日期查询时，开始日期和截止日期同时不能为空',
                    type: 'warning',
                });
                return false;
            } else {
                if (startTime > endTime) {
                    this.$message({
                        message: '使用日期查询时，开始日期不能大于截止日期',
                        type: 'warning',
                    });
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        },

        getLocalTime(time) {
            let date = new Date(time);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            let Y = date.getFullYear() + '-';
            let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            let D = (date.getDate() < 10 ? '0'+ date.getDate() : date.getDate()) + ' ';
            let h = (date.getHours() < 10 ? '0'+ date.getHours() : date.getHours()) + ':';
            let m = (date.getMinutes() < 10 ? '0'+ date.getMinutes() : date.getMinutes()) + ':';
            let s = (date.getSeconds() < 10 ? '0'+ date.getSeconds() : date.getSeconds());
            return Y+M+D+h+m+s;
        },

        // 获取通知列表
        getFirstTableData() {
            //验证时间
            if (this.dateVerify()) {
                this.firstTableData = [];
                this.$nextTick(async () => {
                    let params = {
                        className: 'Notes',
                        method: 'getNotesPageVo',
                        param: {
                            page: {
                                pageIndex: this.paginationData.currentPage,
                                pageSize: this.paginationData.pageSize,
                            },
                            searchdata: this.searchData,
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    this.firstTableData = res.dataList;
                    this.firstTableData.forEach((e, i) => {
                        e.modifyTime = this.getLocalTime(e.modifyTime);
                    });
                    console.log('this.firstTableData=',this.firstTableData)
                    this.paginationData.total = res.page.totalNum;
                });
            }
        },

        // 添加消息信息
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        // 修改消息信息
        edit(val) {
            if (val.status == 1) {
                this.alertMessage("已发布消息不允许编辑", "info");
            } else {
                if (this.onEdit) {
                    console.log('进入编辑页面');
                    this.onEdit(val.id);
                }
            }
        },

        //查看消息删除记录


        //重置
        reset() {
            this.searchData = {
                title: '',
                taskType: '',
                startTime: '',
                endTime: '',
            };
            this.getFirstTableData();
        },

        //查看
        look(val) {
            if (this.onDetail) {
                this.onDetail(val.id);
            }
        },

        //发布
        issue(val) {
            this.$confirm('确定要发布该条消息吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                if (val.status == 1) {
                    this.alertMessage("已发布消息不允许编辑", "info");
                } else {
                    this.$nextTick(async () => {
                        let params = {
                            className: 'Notes',
                            method: 'issueNotes',
                            param: {
                                id: val.id
                            }
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        if (res) {
                            this.alertMessage("发布成功", "success");
                        } else {
                            this.alertMessage("发布失败", "error");
                        }
                        // 重新加载页面数据
                        this.getFirstTableData();
                    });
                }
            }).catch(() => {
                this.alertMessage('取消发布', 'info');
            });
        },

        // 删除角色
        del(val) {
            this.$confirm('此操作将永久删除该通知信息, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'Notes',
                        method: 'deleteNotes',
                        param: {
                            id: val.id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    if (res) {
                        this.alertMessage("删除成功", "success");
                    } else {
                        this.alertMessage("删除失败", "error");
                    }
                    // 重新加载页面数据
                    this.getFirstTableData();
                });
            }).catch(() => {
                this.alertMessage("已取消删除", "info");
            });
        },

        // 每页总条数发生改变
        handleSizeChange(val) {
            this.paginationData.pageSize = val;
            this.getFirstTableData();
        },

        // 页数发生改变
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.getFirstTableData();
        }
    }
};
</script>


<style rel="stylesheet/scss" lang="scss" scoped>
$dark_gray: #889aa4;

.right {
    float: right;
    margin-top: -7px;
}

.left {
    float: left;
    border-left: 4px solid #6ba7bd;
    padding-left: 1%;
}

.page {
    float: right
}

.table {
    width: 100%;
    padding: 1px 22px 15px 22px;

    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1
    }
}

.top {
    width: 100%;
    height: 20px;
    padding: 0px 22px;
    margin: 15px 0px;
}

.container {
    border: 1px solid #bbc8c1;
    height: 100%;
    width: 100%
}

.selectBox {
    margin: 0px 22px 15px 22px;
    .el-row {
        margin: 10px 0;
    }
}

.userInput {
    width: 65%;
}

.searchTwo {
    margin-top: 8px;
}

.searchDataBtn {
    display: flex;
    padding-left: 425px;

    .el-row {
        margin: 0px 8px;
    }
}
</style>

