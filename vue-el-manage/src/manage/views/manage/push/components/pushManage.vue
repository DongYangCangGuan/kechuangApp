<template>
    <div>
        <div class="selectBox">
            <el-row  class="searchTwo">
                <el-col :span="6">
                    通知标题：
                    <el-input class="userInput" placeholder="请输入通知标题" v-model="searchData.title" clearable
                              size='small'></el-input>
                </el-col>
                <el-col :span="6" class="searchDataBtn">
                        <el-button size='small'
                                   icon="el-icon-search"
                                   type="primary"
                                   @click="handleQuery">
                            查询
                        </el-button>
                </el-col>
            </el-row>
        </div>
        <div class="container">
            <div class="top">
                <div class="left">推送基本信息</div>
                <div class="right">
                    <el-button type="success"
                               icon="el-icon-plus"
                               size="small"
                               plain
                               @click="add">创建推送
                    </el-button>
                </div>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
<!--                    <el-table-column label="标题" prop="title" align="center" min-width="350" show-overflow-tooltip></el-table-column>-->
<!--                    <el-table-column label="地址" prop="url" align="center" min-width="200"></el-table-column>-->
                    <el-table-column label="标题" prop="keywords1" align="center" min-width="200"></el-table-column>
                    <el-table-column label="时间" prop="keywords2" align="center" min-width="200"></el-table-column>
                    <el-table-column label="内容" prop="keywords3" align="center" min-width="200"></el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" width="300">
                        <template slot-scope="scope">
                            <el-button size="mini" type="info" v-if="scope.row.status===0" plain @click="show(scope.row)">发布</el-button>
                            <el-button size="mini" type="info" v-if="scope.row.status===1" :disabled="true" plain @click="show(scope.row)">已发布</el-button>
                                <el-button size="mini" type="primary" v-if="scope.row.status===0" plain @click="edit(scope.row)">编辑</el-button>

                                <el-button size="mini" type="primary" v-if="scope.row.status===1" :disabled="true" plain @click="edit(scope.row)">编辑</el-button>
                                <el-button size="mini" type="danger" v-if="scope.row.status===0" plain @click="del(scope.row)">删除</el-button>
                                <el-button size="mini" type="danger" v-if="scope.row.status===1" :disabled="true" plain @click="del(scope.row)">删除</el-button>
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
            searchData: {title:''},
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
                    className: 'push',
                    method: 'searchPushList',
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
                /*this.firstTableData.forEach((e, i) => {
                    e.newsTime = this.getLocalTime(e.newsTime);
                });*/
                this.paginationData.total = res.page.totalNum;
            });
        },

        // 添加
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },


         show(row) {

             this.$confirm('是否确定发布此产品?', '提示', {
                 confirmButtonText: '确定',
                 cancelButtonText: '取消',
                 type: 'error'
             }).then(() => {
                 this.$nextTick(async () => {
                 let params = {
                     className:'push',
                     method:'pushMessage',
                     param: row,
                 };
             let res = await this.$store.dispatch('http/post', params);
             if (res) {
                 this.$message({message: '已成功发布', type: 'success'});
             }
             // 重新加载页面数据
             this.getFirstTableData();
         });
         }).catch(() => {
                 this.$message({type: 'info', message: '取消发布'});
         });
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
            this.$confirm('此操作将删除该通知, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'push',
                        method: 'deletePush',
                        param: {
                            pushMessageId: val.pushMessageId
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

