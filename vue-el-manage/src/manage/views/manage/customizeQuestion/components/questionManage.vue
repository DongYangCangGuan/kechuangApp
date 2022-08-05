<template>
    <div>
        <el-header>
            <div class="top">
                <div class="left">问题列表</div>
                <div class="right">
                    <el-button type="success" icon="el-icon-plus" size="small" plain @click='add'>添加</el-button>
                    <el-button :loading="loading" type="info" icon="el-icon-refresh" size="small" plain
                               @click="refresh">刷新列表
                    </el-button>
                    <el-button type="primary" icon="el-icon-info" size="small" @click="impt">查看回答内容</el-button>
                </div>
            </div>
        </el-header>
        <el-main>
            <el-table
                :data="firstTableData"
                stripe
                class="table-style">
                <el-table-column label="序号" type="index" width="100"></el-table-column>
                <el-table-column label="优先级" prop="sequen" width="100"></el-table-column>
                <el-table-column label="问题描述" prop="questionDescription" width="500" align="center"></el-table-column>
                <el-table-column label="操作" align="center" fixed="right" width="400">
                    <template slot-scope="scope">
                        <el-button size="mini" type="info" plain @click="release(scope.row)"
                                   v-if="scope.row.releaseFlag == 0">发布
                        </el-button>
                        <el-button size="mini" type="info" @click="detail(scope.row)" v-else>查看
                        </el-button>
                        <el-button size="mini" type="primary" plain @click="edit(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" plain @click="del(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
    </div>

</template>

<script>
export default {
    name: "question-Manage",
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
        }
    },
    data() {
        return {
            firstTableData: [],
            loading: false
        };
    },

    // 钩子函数
    created() {
        this.getFirstTableData(); // 页面创建时获取后台数据
    },

    // 方法
    methods: {

        refresh() {
            this.getFirstTableData();
        },

        // 获取问题列表
        getFirstTableData() {
            this.firstTableData = [];
            this.loading = true;
            this.$nextTick(async () => {
                let params = {className: 'customizeQuestion', method: 'getCustomizeQuestionList', param: {}};
                let res = await this.$store.dispatch('http/post', params);

                this.firstTableData = res;
            });
            this.loading = false;
        },

        // 添加
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        // 修改
        edit(val) {
            if (val.releaseFlag == 1) {
                this.$message({
                    message: "已发布消息不允许编辑",
                    type: "info"
                });
            } else {
                if (this.onEdit) {
                    this.onEdit(val.id);
                }
            }
        },


        impt() {
            if (this.onImport) {
                this.onImport(null);
            }
        },

        detail(val) {
            if (this.onDetail) {
                this.onDetail(val.id);
            }
        },

        // 删除
        del(val) {
            if (val.releaseFlag == 1) {
                this.$message({
                    message: "已发布消息不允许删除",
                    type: "info"
                });
            } else {
                this.$confirm('此操作将永久删除该问题, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'error'
                }).then(() => {
                    this.$nextTick(async () => {
                        let params = {
                            className: 'customizeQuestion',
                            method: 'deleteCustomizeQuestion',
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
                            this.$message({message: '删除出现错误', type: 'error'});
                        }
                        // 重新加载页面数据
                        this.getFirstTableData();
                    });
                }).catch(() => {
                    this.$message({type: 'info', message: '取消删除'});
                });
            }
        },

        release(val) {
            this.$confirm('是否发布', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'customizeQuestion',
                        method: 'releaseCustomizeQuestion',
                        param: {
                            id: val.id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    if (res) {
                        this.$message({
                            message: "发布成功",
                            type: "success"
                        });
                        this.getFirstTableData();
                    } else {
                        this.$message({
                            message: "发布失败",
                            type: "error"
                        });
                    }
                    // 重新加载页面数据
                });
            }).catch(() => {
                this.alertMessage("已取消发布", "info");
            });
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

