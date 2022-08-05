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
                <el-col :span="12" class="searchDataBtn">
                    <el-row>
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
                    </el-row>
                </el-col>
            </el-row>
        </div>
        <div class="container">
            <div class="top">
                <div class="left">问卷基本信息</div>
<!--                <div class="right">-->
<!--                    <el-button type="success"-->
<!--                               icon="el-icon-plus"-->
<!--                               size="small"-->
<!--                               plain-->
<!--                               @click='add'>添加-->
<!--                    </el-button>-->
<!--                </div>-->
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
                    <el-table-column label="问卷名称" prop="questionname" align="center" min-width="200"></el-table-column>
                    <el-table-column label="创建时间" prop="createTime" align="center" min-width="150"></el-table-column>
                    <el-table-column label="更新时间" prop="modifyTime" align="center" min-width="150"></el-table-column>
                    <el-table-column label="有效状态" align="center" min-width="150">
                        <template slot-scope="scope">
                            {{ scope.row.releaseFlag === 0 ? "删除" : "存在" }}
                        </template>
                    </el-table-column>

                    <el-table-column label="操作" align="center" fixed="right" width="340">
                        <template slot-scope="scope">
                            <el-button size="mini" type="info" plain @click="details(scope.row)">查看</el-button>
<!--                            <el-button size="mini" type="primary" plain @click="edit(scope.row)">编辑</el-button>-->
<!--                            <el-button size="mini" type="danger" plain @click="del(scope.row)">删除</el-button>-->
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

<!--        <el-button size="mini" @click="dialog = true">查看</el-button>-->
        <el-dialog
            :close-on-click-modal="false"
            :append-to-body="true"
            :visible.sync="dialog"
            style="text-align:left !important;"

        >
<!--            :before-close="handleClose"-->
<!--            <el-form>-->
<!--                <el-form label-width="100px" v-for="(item,index) in formInline" :key="index">-->
<!--                    <el-form-item :label="'题目'+parseFloat(index+1)">-->
<!--                        <el-input v-model="item.title"></el-input>-->
<!--                        <span>({{item.type}})</span>-->
<!--                    </el-form-item>-->
<!--                    <el-form-item v-for="(it, ind) in item.options" :key="ind" :label=" String.fromCharCode(64 + parseInt(ind+1))">-->
<!--                        <el-input v-model="it.content"></el-input>-->
<!--                    </el-form-item>-->
<!--                </el-form>-->
<!--            </el-form>-->
            <div class="dialog-main">
                <div class="main_title" >{{questionnaireName}}</div>
                <div class="main_item" v-for="(item,index) in formInline" :key="index">
                    <div style="font-size: 16px;font-weight: 700;"> {{item.sequen}} 、 {{item.questionDescription}} ({{item.questionType === '0' ? '单选' : '多选'}}) </div>
                    <div style="box-sizing: border-box;padding-left: 15px;" v-for="(o, ind) in item.myQuestion" :key="ind">
                        {{o.answer}}、{{o.content}}
                    </div>
                </div>
            </div>

<!--            <div class="dialog-footer" slot="footer">-->
<!--                <el-button type="primary" @click="dialog = false">通 过</el-button>-->
<!--                <el-button type="success" @click="saveInfo(formInline)">拒 绝</el-button>-->
<!--            </div>-->
        </el-dialog>
    </div>
</template>

<script>

export default {
    name: "questionnaire",
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
    components: {
    },
    data() {
        return {
            // 搜索对象
            searchData: {
                // startTime: '', //开始时间
                // endTime: '', //结束时间

            },
            questionnaireName: '',
            formInline: [
                // {
                //     title: '某某题目',
                //     type: '单选',
                //     options: [
                //         {order: 'A',content:'a'},
                //         {order: 'B',content:'b'}
                //     ]
                // },
                // {
                //     title: '某某题目2',
                //     type: '单选',
                //     options: [
                //         {order: 'A',content:'a'},
                //         {order: 'B',content:'b'}
                //     ]
                // }
            ],
            dialog: false,
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
        this.getPageMessage(); //获取页面相关信息
    },

    // 方法
    methods: {

        //重置
        reset() {
            this.searchData = {

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
            //验证时间
            if (this.dateVerify()) {
                this.firstTableData = [];
                this.$nextTick(async () => {
                    let params = {
                        className: 'Activity',
                        method: 'getQuestionnaireList',
                        param: {
                            page: {
                                pageIndex: this.paginationData.currentPage,
                                pageSize: this.paginationData.pageSize,
                            },
                            searchData: this.searchData,
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res=',res)
                    this.firstTableData = res.questionnaireList;
                    this.firstTableData.forEach((e, i) => {
                        e.createTime = this.getLocalTime(e.createTime)
                        e.modifyTime = this.getLocalTime(e.modifyTime)
                    });
                    this.paginationData.total = res.total;
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

        // 发起申请保存数据
        saveInfo(usertableData) {

            this.dialog = false;
            // alert("111")
            console.log("formInlineformInline",usertableData);
            //把上传的图片地址放在imageList中
            console.log('this.imageList11111111111',this.imageList)
            console.log('this.imageList11111111111this.applyForm.processData',this.applyForm.processData)
            this.dialog = false;

            let param = {
                "image1":this.applyForm.processData[0],
                "image2": this.applyForm.processData[1],
                "image3": this.applyForm.processData[2],
                "repairsType": this.radio,
                "remark":this.saveRemark,
            }
            // alert("11111")
            // 存储到表单中
            this.$nextTick(async ()=> {
                let params = {
                    className:'propertyRepairs',
                    method:'addPropertyRepairs',
                    param:JSON.stringify(param)
                };
                // console.log("1111------" + params);
                let res = await this.$store.dispatch('http/post',params);
                console.log('res1111111111111111',res);

                // this.dialog = false;
                this.page.pageIndex = 1;
                this.page.pageSize = 10;
                // this.getInfo();
                this.getInfo();
            })
            // } else {
            //   alert("表单不能为空");
            // }

            // this.getInfodpet();
            // this.formInline = {};
            // usertableData:{
            //     createTime: '',
            //       code: '',
            //     remark: '',
            //     repairsType: ''
            // },
            // this.usertableData.createTime = usertableData.departmentid;
            // this.usertableData.code = usertableData.departmentid;
            // this.usertableData.remark = usertableData.departmentid;
            // this.usertableData.repairsType = usertableData.departmentid;




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
                this.onEdit(row);
            }
        },
        //详情
        details(row){
            console.log("问卷详情：",row)
            if (this.onDetail) {
                this.onDetail(row);
            }
        },

        //查询
        show(row) {
            console.log('row=',row);
            this.dialog = true;
            this.questionnaireName = row.questionname;
            this.formInline = row.question;
        },

        // 删除
        del(val) {
            console.log('val=',val);
            this.$confirm('此操作将永久删除该问卷信息, 是否继续?', '提示', {
                confirmButtonText: '确定',

                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'Activity',
                        method: 'deleteQuestionnaire',
                        param: {


                            questionBelong: val.questionBelong
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res==',res);
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
    padding-left: 10px;
}

.page {
    float: right
}


.main_title {
    margin: 20px 0;
    text-align: center;
    font-size: 18px;
    font-weight: 700;
}
.main_item {
    margin: 25px;
    line-height: 35px;
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

