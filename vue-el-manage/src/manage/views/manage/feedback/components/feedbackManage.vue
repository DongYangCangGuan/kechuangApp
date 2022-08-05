<template>
    <div>
        <!-- 主页面-->

        <el-header>
            <div class="top">
                <div class="left">问题列表</div>
                <div class="right">
                    <el-button :loading="loading" type="info" icon="el-icon-refresh" size="small" plain
                               @click="refresh">刷新列表
                    </el-button>
                </div>
            </div>
        </el-header>

        <el-main>
            <el-table
                :data="firstTableData"
                stripe
                class="table-style">
                <el-table-column label="序号" type="index" width="100"></el-table-column>
                <el-table-column label="用户名" prop="userName" width="100"></el-table-column>
                <el-table-column label="上传时间" prop="createTime" align="center" width="300"></el-table-column>
                <el-table-column label="反馈内容" prop="opinion" align="center" width="400"></el-table-column>
                <el-table-column label="操作" align="center" fixed="right" width="300">
                    <template slot-scope="scope">
                        <el-button size="mini" type="danger" plain @click="del(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
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

</template>

<script>
export default {
    name: "feedback-Manage",
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
        },

        onImport: {
            type: Function,
            default: null
        },

    },
    data() {
        return {
            firstTableData: [],
            loading: false,
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
        };

    },

    // 钩子函数
    created() {
        this.getFirstTableData(); // 页面创建时获取后台数据
    },

    // 方法
    methods: {

        //刷新
        refresh() {
            this.getFirstTableData();
        },

        // 获取问题列表
        getFirstTableData() {
            this.firstTableData = [];
            this.loading = true;
            this.$nextTick(async () => {
                let params = {
                    className: 'feedback',
                    method: 'getFeedbackPageVo',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log(res)
                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum
            });
            this.loading = false;
        },

        //查看
        detail(val) {
            if (this.onDetail) {
                this.onDetail(val.id);
            }
        },

        // 删除
        del(val) {
            this.$confirm('此操作将永久删除该问题, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'feedback',
                        method: 'deleteFeedback',
                        param: {
                            id: val.id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log(res)
                    if (res == true) {
                        this.$message({
                            message: "删除成功",
                            type: "success"
                        });
                    } else {
                        this.$message({message: '删除失败', type: 'error'});
                    }
                    // 重新加载页面数据
                    this.getFirstTableData();
                });
            }).catch(() => {
                this.$message({type: 'info', message: '取消删除'});
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


<style lang="scss" scoped>
$dark_gray: #889aa4;

.right {
    float: right;
    margin-top: -7px;
}

.left {
    float: left;
    border-left: 4px solid #6ba7bd;
    padding-left: 3px;
}

.page {
    float: right
}

.table {
    width: 100%;
    padding: 1px 22px 15px 22px;
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
    margin: 0px 22px 15px 22px
}

.table-style {
    width: 100%;
    text-align: center;
    border: 1px solid #bbc8c1
}
</style>

