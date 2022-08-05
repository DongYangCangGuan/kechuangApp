<template>
    <div>
        <el-container>

            <el-header class="header">
                <el-row class="headerbtn">
          <span style="float: left">只显示本机构管辖人员：
            <el-switch v-model="switch1"
                       active-color="#13ce66"
                       inactive-color="#ff4949"
                       @change="change">
            </el-switch>
              <!--{{switch1?"是":"否"}}-->
          </span>
                    <el-input v-model="searchData.name" :maxlength="50" style="width:230px" clearable
                              placeholder="名字"></el-input>
                    <el-button type="primary" @click="Search" :rules="rules" icon="el-icon-search"></el-button>
                    <el-button type="primary" v-if="deptId==='001'" @click="handelStaffAddBtnClick" icon="el-icon-plus"></el-button>
                    <el-button type="primary" v-if="deptId==='001'" @click="handelStaffInputBtnClick"><i
                        class="el-icon-upload el-icon--right"/></el-button>
                </el-row>
            </el-header>

            <el-main>
                <el-table :data="tableData" border height="450"
                          :cell-style="{'text-align':'center'}"
                          :header-cell-style="{background:'#eef1f6',color:'#606266','text-align':'center'}"
                          style="width: 100%" @sort-change="sort">
                    <el-table-column prop="id" v-if="false"></el-table-column>
                    <el-table-column prop="name" label="名字" sortable width="140"></el-table-column>
                    <el-table-column prop="departmentname" label="机构" sortable width="140"></el-table-column>
                    <el-table-column label="是否停用" align="center" width="169">
                        <template slot-scope="scope">
                            <el-switch v-model="scope.row.isused==1"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949"
                                       disabled>
                            </el-switch>
                            {{ scope.row.isused == "1" ? "使用" : "停用" }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="email" label="Email" sortable width="160"></el-table-column>
                    <el-table-column label="操作" width="170">
                        <template slot-scope="scope">
                            <el-button @click="handelStaffEditBtnClick(scope.row)" size="small">修改</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>

            <el-footer class="footer">
                <div style="margin-top: 12px;">
                    <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                                   :current-page="listQuery.currentPage" :page-sizes="[10,20,30,50]"
                                   :page-size="listQuery.pageSize"
                                   layout="total, sizes, prev, pager, next, jumper" :total="listQuery.total">
                    </el-pagination>
                </div>
            </el-footer>

        </el-container>
    </div>
</template>
<script>

import bus from '@COM/utils/bus';

export default {
    props: {
        //子页面调用父页面
        onAdd: {
            type: Function,
            default: null
        },
        onModify: {
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
            data: [],
            switch1: true,
            rules: {
                required: true, message: '请输入姓名', trigger: 'blur'
            },
            tableData: [],
            UserName: '',
            listQuery: {
                //当前页
                currentPage: 1,
                //当前每页显示条数
                pageSize: 10,
                //总条目数
                total: 0,
                orderKind: '',
                orderName: '',
                currentItem: 0,
            },
            deptId:'',
            searchData: {
                name: '',
                departmentId: ''
            },
        }
    },
    methods: {
        change() {
            this.load()
        },
        //搜索
        Search() {
            this.$nextTick(async () => {
                if (this.searchData.name == '' | this.searchData.name == null) {
                    //alert("请输入名字!")
                    this.$message({
                        message: "请输入名字！",
                        type: "info"
                    });
                } else {
                    let params = {className: 'Staff', method: 'selectByName', param: this.searchData};
                    let res = await this.$store.dispatch('http/post', params);
                    //console.log(res)
                    if (res.length > 0) {
                        this.tableData = res;
                        this.listQuery.total = res.length
                    } else {
                        this.$message({
                            showClose: true,
                            message: '无此人员！'
                        });
                    }
                }
            })
        },
        //排序
        sort(val) {
            this.order(val.order);
            if (val.prop == "departmentname") {
                val.prop = "departmentid";
            }
            this.listQuery.orderName = val.prop;
            this.load()
        },
        //关联sort
        order(val) {
            if (val == "descending") {
                this.listQuery.orderKind = 'desc'
            } else if (val == "ascending") {
                this.listQuery.orderKind = 'asc'
            }
        },
        //分页控件
        handleSizeChange(val) {
            this.listQuery.pageSize = val
        },
        //分页控件
        handleCurrentChange(val) {
            this.listQuery.currentPage = val;
            this.listQuery.currentItem = (this.listQuery.currentPage - 1) * this.listQuery.pageSize;
            this.load()
        },
        //加载
        load() {
            this.deptId= this.data.id
            console.log("this.deptId",this.deptId)
            this.$nextTick(async () => {
                let departmentid = {
                    departmentid: this.data.id,
                    switch1: this.switch1
                };

                let sum = {className: 'Staff', method: 'getStaffSum', param: departmentid};
                let total = await this.$store.dispatch('http/post', sum);
                this.listQuery.total = total;
                if (total > 0) {
                    let page = {
                        departmentid: this.data.id,
                        currentPage1: this.listQuery.currentItem,
                        pageSize1: this.listQuery.pageSize,
                        orderKind: this.listQuery.orderKind,
                        orderName: this.listQuery.orderName,
                        switch1: this.switch1
                    };
                    let params = {className: 'Staff', method: 'getStaffById', param: page};
                    let response = await this.$store.dispatch('http/post', params)
                    this.tableData = response;
                } else {
                    this.tableData = [];
                    this.$message({
                        showClose: true,
                        message: '无人员！'
                    });
                }
            })
        },
        //新增
        handelStaffAddBtnClick: function () {
            if (this.onAdd) {
                this.onAdd(null)
            }
        },
        //修改
        handelStaffEditBtnClick: function (data) {
            if (this.onModify) {
                this.onModify(null);
                this.$store.dispatch('db/addStore', {key: "editStaffForm", value: data});
            }
            console.log(data)
        },

        //批量导入
        handelStaffInputBtnClick: function () {
            if (this.onImport) {
                this.onImport(null)
            }
        },

        handleNodeClick(data) {
            this.$nextTick(async () => {
                this.data = data;
                this.load();
                this.searchData.departmentId = data.id;
            })
        },
    },

    created() {
        bus.$on('dept-node-select', this.handleNodeClick)
    },

    beforeDestroy() {
        bus.$off('dept-node-select', this.handleNodeClick)
    },
}
</script>

<style scoped>

.header {
    background-color: rgb(240, 244, 245);
    text-align: left;
    margin-bottom: 10px;
}

.headerbtn {
    /*margin-top: 15px;*/
    text-align: right;
    line-height: 5;
}

.footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
}

</style>
