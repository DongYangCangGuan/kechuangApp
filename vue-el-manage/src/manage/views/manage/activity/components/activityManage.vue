<template>
    <div>
        <div class="selectBox" v-loading="loading.form">
            <el-row :getter="24" class="searchTwo">
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
                    <!--                  1  <el-row>-->
                    <!--                        <el-button size='small'-->
                    <!--                                   icon="el-icon-refresh"-->
                    <!--                                   type="primary"-->
                    <!--                                   @click="reset">-->
                    <!--                            重置-->
                    <!--                        </el-button>-->
                    <!--                    </el-row>-->
                    <!--                    <el-row>-->
                    <!--                        <el-button type="primary"-->
                    <!--                                   icon="el-icon-download"-->
                    <!--                                   size="small"-->
                    <!--                                   @click='templateDownLoad'>-->
                    <!--                            模板下载-->
                    <!--                        </el-button>-->
                    <!--                    </el-row>-->
                    <!--                    <el-row>-->
                    <!--                        <upload-excel-->
                    <!--                            ref="uploadExcel"-->
                    <!--                            @submitUpload="submitUpload"-->
                    <!--                            :allowSize="allowFileSize"-->
                    <!--                            :allowType="allowFileType"-->
                    <!--                            :file="uploadExcel"-->
                    <!--                            @finishLoading="loadingFile=false"-->
                    <!--                            @startLoading="loadingFile=true"-->
                    <!--                        ></upload-excel>-->
                    <!--                    </el-row>-->
                </el-col>
            </el-row>
        </div>
        <div class="container">
            <div class="top">
                <div class="left">活动基本信息</div>
                <div class="right">
                    <el-button type="success"
                               icon="el-icon-plus"
                               size="small"
                               plain
                               @click="add">创建活动
                    </el-button>
                </div>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
                    <el-table-column label="活动标语" prop="name" align="center" width="150"></el-table-column>
                    <el-table-column label="标题" prop="title" align="center" min-width="150" show-overflow-tooltip></el-table-column>
                    <el-table-column label="内容" prop="description" align="center" width="200" show-overflow-tooltip></el-table-column>
<!--                    <el-table-column label="起始时间" prop="artStartTime" align="center" min-width="150"></el-table-column>-->
                    <el-table-column label="发布时间" prop="createTime" align="center" min-width="160"></el-table-column>
                    <el-table-column label="起始时间" prop="artStartTime" align="center" min-width="160"></el-table-column>
                    <el-table-column label="结束时间" prop="artEndTime" align="center" min-width="160"></el-table-column>
                    <el-table-column label="已报名" prop="signTolNum" align="center" min-width="100"></el-table-column>
                    <el-table-column label="发布人" prop="realName" align="center" width="100"></el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" width="220">
                        <template slot-scope="scope">
                            <el-button size="mini" type="info" plain @click="showCompany(scope.row)">查看</el-button>
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

            <!--     活动发布弹框       -->
<!--            :before-close="handleClose"-->
            <el-dialog
                :close-on-click-modal="false"
                :append-to-body="true"
                :visible.sync="dialog"
                style="text-align:left !important;"

            >
                <el-form :model="formInline" label-width="130px" ref="form">

                    <el-form-item label="活动名称" prop="name">
                        <el-input v-model="formInline.name"></el-input>
                    </el-form-item>

                    <el-form-item label="活动标题" prop="title">
                        <el-input v-model="formInline.title"></el-input>
                    </el-form-item>

                    <el-form-item label="活动内容" prop="description">
                        <el-input
                            type="textarea"
                            :rows="10"
                            v-model="formInline.description"></el-input>
                    </el-form-item>

                    <el-form-item label="活动开始时间" prop="artStartTime">
                        <!--                            <el-input v-model="formInline.activityTime"></el-input>-->
                        <el-date-picker
                            v-model="formInline.artStartTime"
                            align="right"
                            type="date"
                            placeholder="请选择开始日期"
                            size="small"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            clearable
                            style="width: 100%;"
                        >
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="活动结束时间" prop="artEndTime">
                        <!--                            <el-input v-model="formInline.activityTime"></el-input>-->
                        <el-date-picker
                            v-model="formInline.artEndTime"
                            align="right"
                            type="date"
                            placeholder="请选择结束日期"
                            size="small"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            clearable
                            style="width: 100%;"
                        >
                        </el-date-picker>
                    </el-form-item>

                </el-form>

                <div class="dialog-footer" slot="footer">
                    <el-button type="primary" @click="cancelInfo()">取 消</el-button>
                    <el-button type="success" icon="el-icon-download" @click="saveInfo()">保存</el-button>
                </div>

            </el-dialog>



            <!--    活动编辑弹窗    -->
            <div>
                <el-dialog
                    :close-on-click-modal="false"
                    :append-to-body="true"
                    :visible.sync="dialog1"
                    style="text-align:left !important;"
                >
                    <el-form :model="formData" label-width="130px"  ref="editForm">

                            <el-form-item label="活动名称" prop="name">
                                <el-input v-model="formData.name"></el-input>
                            </el-form-item>

                            <el-form-item label="活动标题" prop="title">
                                <el-input v-model="formData.title"></el-input>
                            </el-form-item>

                            <el-form-item label="活动内容" prop="description">
                                <el-input
                                    type="textarea"
                                    :rows="10"
                                    v-model="formData.description"></el-input>
                            </el-form-item>

                            <el-form-item label="活动开始时间" prop="artStartTime">
                                <!--                            <el-input v-model="formInline.activityTime"></el-input>-->
                                <el-date-picker
                                    v-model="formData.artStartTime"
                                    align="right"
                                    type="date"
                                    placeholder="请选择开始日期"
                                    size="small"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    style="width: 100%;"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item label="活动结束时间" prop="artEndTime">
                                <el-date-picker
                                    v-model="formData.artEndTime"
                                    align="right"
                                    type="date"
                                    placeholder="请选择结束日期"
                                    size="small"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    style="width: 100%;"
                                >
                                </el-date-picker>
                            </el-form-item>
                    </el-form>

                        <div class="dialog-footer" slot="footer">
                            <el-button type="primary" @click="cancelEdit()">取 消</el-button>
                            <el-button type="success" icon="el-icon-download" @click="saveEdit()">保存</el-button>
                        </div>


                </el-dialog>
            </div>

        </div>


    </div>
</template>

<script>

export default {
    name: "activityManage",
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
            // 搜索对象
            searchData: {
                artStartTime: '', //开始时间
                artEndTime: '', //结束时间
                name: ''
            },

            dialog: false,
            formInline: {},
            formData: {},
            loading: {
                form: false,
            },
            isShow: false,
            signList: [],
            pageSignList: [],
            enterpriseDetail: {},
            dialog1: false,
            firstTableData: [], //用来存储表格的相关信息
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
            paginationData1: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
            normalizer(node) {  // 自定义下拉树节点模板
                return {
                    id: node.id,
                    label: (node.name != undefined && node.name != null && node.name != '') ? node.name : node.realName,
                    children: node.children // 当选中节点展开时没有子节点，设置其子节点为''
                }
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



        //重置
        reset() {
            this.searchData = {
                startTime:'',
                endTime: '',
                // activityName: '',
                // activityTitle: '',
                // activityContent: '',
                // startTime: '',
                // publishTime: '',
                // registered: '',
                // publisher: '',
            }
            this.getFirstTableData();
        },
        //带条件查询
        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        //获取页面相关信息
        getPageMessage() {
            // this.getComboOptions(); //获取套餐的下拉框信息
            // this.getAccountManagerOptions(); //获取客户经理的下拉框信息
            this.getFirstTableData(); // 页面创建时获取后台数据
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
            console.log("this.searchData",this.searchData)
            //验证时间
            if (this.dateVerify()) {
                this.firstTableData = [];
                this.$nextTick(async () => {
                    let params = {
                        className: 'Activity',
                        method: 'getActivityList',
                        param: {
                            page: {
                                pageIndex: this.paginationData.currentPage,
                                pageSize: this.paginationData.pageSize,
                            },
                            searchdata: this.searchData,
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res111',res);
                    this.firstTableData = res.dataList;
                    this.firstTableData.forEach((e, i) => {
                        e.artStartTime = this.getLocalTime(e.artStartTime);
                        e.artEndTime = this.getLocalTime(e.artEndTime);
                        e.createTime = this.getLocalTime(e.createTime);
                    });
                    this.paginationData.total = res.page.totalNum;
                });
            }
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

        cancelInfo() {
            this.dialog = false;
            //this.$refs.form.resetFields();//清除form表格的内容和验证
        },

        // 发起申请保存数据
        saveInfo() {
            console.log('this.formInline=',this.formInline);
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'setActivity',
                    param: this.formInline
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res222',res);
                if (res) {
                    this.getFirstTableData();
                    this.$message({showClose: true, message: '发布成功！', type: 'success'});
                    this.dialog = false;
                    //this.$refs.form.resetFields();//清除form表格的内容和验证
                    setTimeout(() => {
                        // if (this.onBack) {
                        //     this.$refs.form.resetFields();//清除form表格的内容和验证
                        //     this.onBack('succ');
                        // }
                        // this.dialog = false;
                    }, 500);
                } else {
                    this.$message({showClose: true, message: '保存失败！', type: 'error'});
                    this.loading.btn = false;
                }
            });
        },

        cancelEdit() {
            this.dialog1 = false;
        },

        //保存编辑数据
        saveEdit() {
            console.log('this.formData=',this.formData);
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'updateActivity',
                    param: this.formData
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res222',res);
                if (res) {
                    this.getFirstTableData();
                    this.$message({showClose: true, message: '编辑成功！', type: 'success'});
                    this.dialog1 = false;
                    setTimeout(() => {
                        // if (this.onBack) {
                        //     this.$refs.form.resetFields();//清除form表格的内容和验证
                        //     this.onBack('succ');
                        // }
                        // this.dialog = false;
                    }, 500);
                } else {
                    this.$message({showClose: true, message: '编辑失败！', type: 'error'});
                    // this.loading.btn = false;
                }
            });
        },

        //查询
        showCompany(row) {
            console.log('row===',row);
            if (this.onDetail) {
                this.onDetail(row);
            }
        },

        // 添加
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        //编辑
        edit(row) {
            // this.formData = row;
            // console.log('this.formData =',this.formData);
            // this.dialog1 = true;
            if (this.onEdit) {
                this.onEdit(row);
            }
        },

        // 删除
        del(val) {
            this.$confirm('此操作将永久删除该活动信息, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'Activity',
                        method: 'deleteActivity',
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


        //下载附件
        downloadFile(url){

            if (this.dateVerify()) {
                // this.firstTableData = [];
                this.$nextTick(async () => {
                    let params = {
                        className: 'Activity',
                        method: 'getActivityList',
                        param: {

                            name:this.enterpriseDetail.name,
                            path:this.enterpriseDetail.path
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res111',res);
                    // this.firstTableData = res.activityList;
                    // this.firstTableData.forEach((e, i) => {
                    //     e.artStartTime = this.getLocalTime(e.artStartTime);
                    //     e.artEndTime = this.getLocalTime(e.artEndTime);
                    //     e.createTime = this.getLocalTime(e.createTime);
                    // });
                    // this.paginationData.total = res.page.totalNum;
                });
            }
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

        // 每页总条数发生改变
        // handleSizeChange1(val) {
        //     this.paginationData1.pageSize = val;
        //     this.pageSignList = this.signList.slice((this.paginationData1.currentPage-1)*this.paginationData1.pageSize,
        //         this.paginationData1.currentPage*this.paginationData1.pageSize)
        // },
        // 页数发生改变
        // handleCurrentChange1(val) {
        //     this.paginationData1.currentPage = val;
        //     this.pageSignList = this.signList.slice((this.paginationData1.currentPage-1)*this.paginationData1.pageSize,
        //         this.paginationData1.currentPage*this.paginationData1.pageSize)
        // },

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

