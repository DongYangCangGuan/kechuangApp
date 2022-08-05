<template>
    <div>
        <div class="container" v-loading="loading.form">
            <div class="top">
                <div class="left">轮播广告基本信息</div>
                <div class="right">
                    <el-button type="success"
                               icon="el-icon-plus"
                               size="small"
                               plain
                               @click='add'>添加
                    </el-button>
                </div>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
                    <el-table-column label="序号" type="index" align="center" width="80"></el-table-column>
                    <el-table-column label="图片路径" prop="pic" align="center">
                        <template slot-scope="scope">
                            <img :src="scope.row.pic" width="80" height="80">
                        </template>
                    </el-table-column>
<!--                    <el-table-column label="跳转路径" prop="url" align="center"-->
<!--                                     min-width="350"></el-table-column>-->
                    <el-table-column label="标题" prop="title" align="center" min-width="120"></el-table-column>
                    <el-table-column label="优先级" prop="index" align="center" min-width="80"></el-table-column>
                    <el-table-column label="是否有效" prop="isused" align="center" min-width="80"></el-table-column>
                    <el-table-column label="修改人" prop="modifyusername" align="center" min-width="120"></el-table-column>
                    <el-table-column label="修改时间" prop="modifyTime" align="center" min-width="200"></el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" width="240">
                        <template slot-scope="scope">
                            <el-button size="mini" type="info" plain @click="edit(scope.row)">编辑</el-button>
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
    name: "view-pager-manage",
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
    },
    data() {
        return {
            loading: {
                form: false,
            },
            firstTableData: [], //用来存储表格的相关信息
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
        // 获取会员列表
        getFirstTableData() {
            this.firstTableData = [];
            this.$nextTick(async () => {
                this.loading.form = true;
                let params = {
                    className: 'Slider',
                    method: 'getSliderPageVo',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: {},
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.firstTableData = res.dataList;
                this.firstTableData.forEach((e, i) => {
                    e.isused = e.isused ? "是" : "否";
                });
                this.paginationData.total = res.page.totalNum;
                this.getPic(this.firstTableData);
                this.loading.form = false;
            });
        },

        //将路径转成base64位的图片
        getPic(tableData) {
            let base = this;
            for (const item of tableData) {
                base.$nextTick(async () => {
                    console.log(item, 'item----------->')
                    let pic = {
                        param: item.pic,
                        url: process.env.BASE_API + '/api/file/downloadfile'
                    };
                    let res = await base.$store.dispatch('http/fileDownload', pic);
                    let path = "";
                    if (res != "" && res != null) {
                        path = "data:image/png;base64," + res;
                    } else {
                        path = "";
                    }
                    if (path != "") {
                        item.pic = path;
                    }
                });
            }
        },

        // 添加
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        // 编辑
        edit(row) {
            if (this.onEdit) {
                this.onEdit(row.id);
            }
        },

        // 删除
        del(val) {
            this.$confirm('此操作将永久删除该轮播广告, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'Slider',
                        method: 'deleteSlider',
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
        },
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
}

.assignUser {
    margin: 15px 22px;
}

.svg-container {
    color: $dark_gray;
    vertical-align: middle;
    width: 35px;
    margin-left: -49px;
    display: inline-block;
}

/deep/ .vue-treeselect__control {
    height: 32px;
    line-height: 32px;
}

/deep/ .vue-treeselect__placeholder {
    font-size: 13px;
    line-height: 32px;
}

/deep/ .vue-treeselect__label {
    font-size: 13px;
}

/deep/ .vue-treeselect__single-value {
    font-size: 13px;
    line-height: 32px;
}

.searchDataBtn {
    display: flex;

    .el-row {
        margin: 0px 8px;
    }
}

.userInput {
    width: 65%;
}

.treeselectStyle {
    display: flex;
    align-items: center;
}

.searchTwo {
    margin-top: 8px;
}

</style>

