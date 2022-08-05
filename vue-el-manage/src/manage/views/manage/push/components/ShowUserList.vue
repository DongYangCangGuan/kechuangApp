<template>
    <el-dialog :visible.sync="showOpenObjVisi" width="70%" top="1vh" title="查看通知人员" append-to-body>
        <el-container class="container">
            <el-header>
                <el-form label="通知名单" :inline="true">
                    <el-form-item>
                        <el-input v-model="searchData.userId" placeholder="请输入用户编号" clearable/>
                    </el-form-item>
                    <el-form-item>
                        <el-button-group>
                            <el-button type="primary" icon="el-icon-search" @click="search"
                                       :loading="searchLoading">搜索
                            </el-button>
                            <el-button type="info" icon="el-icon-refresh" @click="reset">重置</el-button>
                        </el-button-group>
                    </el-form-item>
                </el-form>
            </el-header>
            <el-main class="main">
                <span v-if="userIds.length == 0">开放对象：全员</span>
                <el-table :data="firstTableData" :height="350" v-else>
                    <el-table-column label="开放用户编号" prop="userId"></el-table-column>
                    <el-table-column label="开放openId" prop="openId"></el-table-column>
                    <el-table-column label="开放昵称" prop="nickName"></el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button type='danger' @click="deleteOpenObjById(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer class="footer">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="paginationData.currentPage"
                    :page-sizes="[10, 20, 30, 40, 50]"
                    :page-size="paginationData.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="paginationData.total">
                </el-pagination>
            </el-footer>
        </el-container>
    </el-dialog>
</template>
<script>
export default {
    name: 'show-user-list',
    data() {
        return {
            userIds: [],
            showOpenObjVisi: false,
            paginationData: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            searchData: {
                userId: '',
            },
            firstTableData: [],
            searchLoading: false
        }
    },

    methods: {
        load(userIds) {
            this.userIds = userIds;
            this.showOpenObjVisi = true;
            this.searchLoading = false;
            this.paginationData.currentPage = 1;
            this.reset();
        },

        //查询开放对象
        search() {
            if (this.checkData() == false) {
                return;
            }
            let data = {
                page: {
                    pageIndex: this.paginationData.currentPage,
                    pageSize: this.paginationData.pageSize
                },
                searchData: {
                    userIds: this.userIds,
                    userId: this.searchData.userId,
                }
            }
            this.$nextTick(async () => {
                this.searchLoading = true;
                //异步调用
                const params = {
                    //传参
                    className: "UserInfo",
                    method: "getUserInfoBatchById",
                    param: data
                };
                //调用服务器
                const res = await this.$store.dispatch("http/post", params); //调用服务器
                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum;
                this.searchLoading = false;
            });
        },
        //删除开放对象
        deleteOpenObjById(val) {
            this.$confirm('此操作将永久删除开放对象, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.userIds.splice(this.userIds.indexOf(val.id),1);
                this.$message({
                    message: '删除成功',
                    type: 'success'
                });
                this.search();
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },

        reset() {
            this.searchData.userId = '';
            this.searchLoading = false;
            this.search();
        },

        //分页功能
        //人员
        // 每页总条数发生改变
        handleSizeChange(val) {
            this.paginationData.pageSize = val;
            this.search();
        },

        // 页数发生改变
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.search();
        },
        checkData() {
            if (this.userIds.length == 0) {
                this.$message({
                    message: '开放对象为全员',
                    type: 'warning'
                });
                return false;
            }
            return true;
        }
    }
}
</script>
<style scoped>
.footer {
    padding: 2% 0 0 0;
}

.container {
    height: 550px;
}
</style>
