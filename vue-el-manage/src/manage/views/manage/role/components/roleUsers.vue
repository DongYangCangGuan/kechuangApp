<template>
    <div v-loading="loading.form">
        <el-container>
            <el-header class="header">
                <el-row class="headerbtn">
                    <span style="float: left" class="title">人员列表</span>
                    <el-input v-model="searchData.name" :maxlength="100" style="width:230px" clearable
                              placeholder="名字"></el-input>
                    <el-button type="primary" plain @click="getPageInfo" :loading="loading.btn">搜索</el-button>
                    <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
                </el-row>
            </el-header>
            <el-main>
                <el-table
                    :data="firstTableData"
                    stripe
                    style="width: 100%;text-align:center;border:1px solid #bbc8c1">
                    <el-table-column label="序号" type="index" width="50"></el-table-column>
                    <el-table-column prop="empid" label="员工编号" align="center"></el-table-column>
                    <el-table-column prop="name" label="员工姓名" align="center"></el-table-column>
                    <el-table-column prop="departname" label="部门" align="center" width="180"></el-table-column>
                    <el-table-column label="操作" align="center" width="100">
                        <template slot-scope="scope">
                            <el-button size="mini" type="danger" plain @click="deleteRoleUser(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="paginationData.currentPage"
                    :page-sizes="[10,20,30,40,50]"
                    :page-size="paginationData.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="paginationData.total">
                </el-pagination>
            </el-main>
        </el-container>
    </div>
</template>

<script>

export default {
    name: "role-Users",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            searchData: {
                roleid: '',
                name: ''
            },
            firstTableData: [],
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
            loading: {
                form: false,
                btn: false
            }
        }
    },
    methods: {
        // 加载数据
        load(id) {
            if (id) {
                this.loading.form = true;
                this.searchData.roleid = id;
                this.getPageInfo();
            }
        },

        //获取页面用户信息
        getPageInfo() {
            this.loading.btn = true;
            this.$nextTick(async () => {
                let params = {
                    className: 'roleUsers',
                    method: 'getUserAndRoles',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: this.searchData
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalPage;
                this.loading.btn = false;
                this.loading.form = false;
            });
        },

        // 删除角色下的用户
        deleteRoleUser(row) {
            let userId = row.id;
            let roleId = this.searchData.roleid;
            this.$confirm(`确定从该角色中移除${row.name}用户？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'roleUsers',
                        method: 'delRoleforUser',
                        param: {
                            roleId: roleId,
                            userId: userId
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    if (res == '200') {
                        this.$message({showClose: true, message: '操作成功！', type: 'success'});
                        setTimeout(() => {
                            this.getPageInfo();
                        }, 500)
                    } else {
                        this.$message({showClose: true, message: '操作失败！', type: 'error'});
                    }
                })
            }).catch(() => {
                this.$message({type: 'info', message: '已取消删除'});
            });
        },

        // 每页总条数发生改变
        handleSizeChange(val) {
            this.paginationData.pageSize = val;
            this.getPageInfo();
        },

        // 页数发生改变
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.getPageInfo();
        },

        // 返回主页面
        backClick() {
            this.searchData.name = '';
            if (this.onBack) {
                this.onBack(null)
            }
        }
    }
}
</script>
<style scoped>
.header {
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
}

.headerbtn {
    text-align: right;
    line-height: 5;
    height: 12px;
}
</style>
