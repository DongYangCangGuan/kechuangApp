<template>
    <div>
        <div class="selectBox">
            <el-row  class="searchTwo">
                <el-col :span="6">
                    发布日期：
                    <el-date-picker
                        v-model="searchData.startTime"
                        align="right"
                        type="date"
                        placeholder="请选择日期"
                        size="small"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        clearable
                        class="userInput">
                    </el-date-picker>
                </el-col>
                <el-col :span="6" class="searchDataBtn">
                        <el-button size='small'
                                   icon="el-icon-search"
                                   type="primary"
                                   @click="handleQuery">
                            查询
                        </el-button>
                    <el-button size='small'
                               icon="el-icon-refresh"
                               type="primary"
                               @click="reset">
                        重置
                    </el-button>
                </el-col>
            </el-row>
        </div>
        <div class="container">
            <div class="top">
                <div class="left">新闻基本信息</div>
                <div class="right">
                    <el-button type="success"
                               icon="el-icon-plus"
                               size="small"
                               plain
                               @click="add">创建新闻
                    </el-button>
                </div>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
                    <el-table-column label="缩略图" prop="newsPic" align="center" min-width="200">
                        <template slot-scope="scope">
                            <img :src="scope.row.newsPic" width="80" height="80">
                        </template>
                    </el-table-column>
                    <el-table-column label="新闻标题" prop="title" align="center" min-width="350" show-overflow-tooltip></el-table-column>
                    <el-table-column label="发布时间" prop="newsTime" align="center" min-width="200"></el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" width="300">
                        <template slot-scope="scope">
                            <el-button size="mini" type="info" plain @click="show(scope.row)">查看</el-button>
                            <el-button size="mini" type="primary" plain @click="edit(scope.row)">编辑</el-button>
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


    </div>
</template>

<script>

export default {
    name: "newsManage",
    props: {
        onAdd: {
            type: Function,
            default: null
        },
        //子页面调用父页面
        onDetail: {
            type: Function,
            default: null
        },
        onEdit: {
            type: Function,
            default: null
        }
    },
    components: {
    },
    data() {
        return {
            searchData: {},
            firstTableData: [], //用来存储表格的相关信息
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
        // this.getPageMessage(); //获取页面相关信息
        this.getFirstTableData(); // 页面创建时获取后台数据
    },

    // 方法
    methods: {

        //带条件查询
        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        //获取页面相关信息
        getPageMessage() {
            // this.getComboOptions(); //获取套餐的下拉框信息
            // this.getAccountManagerOptions(); //获取客户经理的下拉框信息
            // this.getFirstTableData(); // 页面创建时获取后台数据
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


        // 获取会员列表
        getFirstTableData() {
            this.$nextTick(async () => {
                this.searchData.endTime=this.searchData.startTime
                console.log("this.searchData",this.searchData)
                let params = {
                    className: 'newsMange',
                    method: 'getNewsList',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: this.searchData
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res3333',res);
                this.firstTableData = res.dataList;
                this.firstTableData.forEach((e, i) => {
                    e.newsTime = this.getLocalTime(e.newsTime);
                });
                this.paginationData.total = res.page.totalNum;
            });
        },

        // 添加
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        //查询
        show(row) {
            if (this.onDetail) {
                this.onDetail(row);
            }
        },

        //编辑
        edit(row) {
            if (this.onEdit) {
                this.onEdit(row);
            }
        },

        //重置
        reset() {
            this.searchData = {
                startTime: '',
                endTime: '',
            }
            this.getFirstTableData();
        },

        // 删除
        del(val) {
            this.$confirm('此操作将永久删除该新闻, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'newsMange',
                        method: 'deleteNews',
                        param: {
                            id: val.id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    if (res) {
                        this.$message({message: '删除成功', type: 'success'});
                    } else {
                        this.$message({message: '删除失败', type: 'error'});
                    }
                    // 重新加载页面数据
                    this.getFirstTableData();
                });
            }).catch(() => {
                this.$message({message: '已取消删除', type: 'info'});
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
    padding-left: 10px;
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
    //height: 100%;
    width: 100%;
    margin-bottom: 20px;
}

.selectBox {
    margin: 15px 22px;
}
.userInput {
    width: 65%;
}
</style>

