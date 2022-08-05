<template>
    <div>
        <!-- 主页面 -->
        <div>
            <div class="selectBox">
                <el-row :getter="24" class="searchTwo">
<!--                    <el-col :span="6">-->
<!--                        单位编号：-->
<!--                        <el-input placeholder="请输入单位编号" v-model="searchData.memberId" clearable size='small'-->
<!--                                  class="userInput"></el-input>-->
<!--                    </el-col>-->
                    <el-col :span="6">
                        单位名称：
                        <el-input placeholder="请输入单位名称" v-model="searchData.enterpriseName" clearable size='small'
                                  class="userInput"></el-input>
                    </el-col>
                    <el-col :span="6">
                        用户类型：
                        <el-select v-model="searchData.memberType" placeholder="请选择" class="userInput" size="small">
                            <el-option
                                v-for="(item,index) in memberTypeList"
                                :key="index"
                                :label="item.name"
                                :value="item.code"
                            ></el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="6">
                        <el-button size='small' icon="el-icon-search"
                                   style="margin-left: 10px;"
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
                        <el-button size='small'
                                   icon="el-icon-download"
                                   type="primary"
                                   @click.native.prevent="exportexcel()" :loading="ExportLoad">
                            导出
                        </el-button>
                    </el-col>
                </el-row>


            </div>
            <div class="container">
                <div class="top">
                    <div class="left">用户身份认证</div>
                </div>
                <div class="table">
                    <el-table
                        :data="firstTableData"
                        stripe>
                        <el-table-column label="身份类型" prop="typeName" align="center" min-width="180"></el-table-column>
                        <el-table-column label="单位名称" prop="enterpriseName" align="center"
                                         min-width="250"></el-table-column>
<!--                        <el-table-column label="单位编号" prop="memberId" align="center" min-width="250"></el-table-column>-->
                        <el-table-column label="联系人" prop="realName" align="center"
                                         min-width="200"></el-table-column>
                        <el-table-column label="职务" prop="job" align="center" min-width="200"></el-table-column>
                        <el-table-column label="联系方式" prop="phone" align="center" min-width="200"></el-table-column>
                        <el-table-column label="邮箱" prop="email" align="center" min-width="250"></el-table-column>
                        <el-table-column label="申请认证时间" prop="createTime" align="center" min-width="250"></el-table-column>
                        <el-table-column label="审核人" prop="modifierName" align="center" min-width="250"></el-table-column>

                        <el-table-column label="操作" align="center" fixed="right" min-width="340">
                            <template slot-scope="scope">
                                <el-button v-if="scope.row.approvalstatus === '0'" size="mini" type="primary" plain @click="allot(scope.row)">审核</el-button>
                                <el-button  v-if="scope.row.approvalstatus === '1'" type="success" size="mini" plain>已审核</el-button>
                                <el-button  v-if="scope.row.approvalstatus === '3'" type="success" size="mini" plain>审核中</el-button>
                                <el-button  v-if="scope.row.approvalstatus === '2'"  size="mini" type="info" plain @click="allot(scope.row)">已驳回</el-button>
                                <!--                                <el-button size="mini" type="danger" plain @click="reject(scope.row)">拒绝审核</el-button>-->
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

<!--        <el-button size="mini" @click="showLoading = true">查看</el-button>-->

        <!--会员选择项-->
        <el-dialog
            :visible.sync="showLoading"
            width="60%"
            :modal-append-to-body="false"
            :close-on-click-modal="false"
            :append-to-body="true"
            :show-close="true"
        >
            <div>
<!--                <div class="table">-->
<!--                    -->
<!--                </div>-->

                <el-form ref="form" v-model="formData" label-width="150px">

                    <el-form-item label="身份类型">
                        <el-input disabled  v-model="formData.typeName"></el-input>
                    </el-form-item>
                    <el-form-item label="单位名称">
                        <el-input disabled  v-model="formData.enterpriseName"></el-input>
                    </el-form-item>
                    <el-form-item label="单位编号">
                        <el-input disabled  v-model="formData.memberId"></el-input>
                    </el-form-item>
                    <el-form-item label="联系人">
                        <el-input   v-model="formData.realName"></el-input>
                    </el-form-item>
                    <el-form-item label="职务">
                        <el-input   v-model="formData.job"></el-input>
                    </el-form-item>
                    <el-form-item label="联系方式">
                        <el-input   v-model="formData.uphone"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input   v-model="formData.uemail"></el-input>
                    </el-form-item>
                </el-form>

                <div slot="footer" class="dialog-footer" style="text-align: right;margin-top: 20px;">
                    <el-button @click="submitSelected" type="primary">同 意</el-button>
                    <el-button @click="cancelDialog" type="info">拒绝</el-button>
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';
import qs from 'qs';
export default {
    name: "member-Manage",
    props: {
        //子页面调用父页面
        onEdit: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            // 搜索对象
            searchData: {

            },
            firstTableData: [],
            formData: {},
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
            showLoading: false,
            loading: {
                form: false,
                btn: false,
            },
            multipleSelection: '',
            showFirstTableData: '',
            selectValue: {},
            selectedEnsureUserId: '',
            selectedEnsureMemberId: '',
            ExportLoad:false,
            memberTypeList: []
        };
    },

    // 钩子函数
    created() {
        this.getFirstTableData(); // 页面创建时获取后台数据
        this.getMemberTypeList();
    },

    // 方法
    methods: {
        //获取未认证的用户数
        getUnApprovedCount() {
            this.$store.dispatch('approveCount/getUnApprovedCount');
        },
        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
            });
        },
        reset() {
            this.searchData = {}
            this.getFirstTableData();
        },
        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },
        //查询类型
        getMemberTypeList() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getMemberTypeList'
                };
                let res = await this.$store.dispatch('http/post', params)
                this.memberTypeList = res;
                console.log('this.memberTypeList=',this.memberTypeList);
            });

        },

        //点击行触发，选中或不选中复选框
        handleRowClick(row, column, event) {
            this.$refs.multipleTable.toggleRowSelection(row);
        },

        //默认只允许选择一个
        handleSelectionChange(val) {
            if (val.length > 1) {
                this.$refs.multipleTable.clearSelection();
                this.$refs.multipleTable.toggleRowSelection(val.pop());
            } else {
                this.multipleSelection = val.pop();
            }

            if (this.multipleSelection === undefined) {
                this.selectValue = {};
            } else {
                let tag = {
                    id: this.multipleSelection.id,
                    name: this.multipleSelection.enterpriseName,
                }
                this.selectValue = tag;
            }
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


        // 获取待审核的用户列表
        getFirstTableData() {
            this.loading.btn = true;
            this.firstTableData = [];
            console.log('this.searchData=',this.searchData);
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getmemberDetailList',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: this.searchData
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res=',res);
                this.firstTableData = res.dataList;
                this.firstTableData.forEach((e, i) => {
                    e.createTime = this.getLocalTime(e.createTime)
                });
                this.paginationData.total = res.page.totalNum;
                this.loading.btn = false;
            });
        },

        // 编辑
        edit(row) {
            if (this.onEdit) {
                this.onEdit(row.id);
            }
        },

        // 删除
        del(val) {
            this.$confirm('此操作将永久删除该会员信息, 是否继续?', '提示', {
                confirmButtonText: '确定',

                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {

                    let params = {
                        className: 'Member',
                        method: 'delMemberUserById',
                        param: {
                            id:val.id,
                            userId: val.userId
                        }
                    };
                    console.log("params",params)
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

        // async exportexcel(){
        //     console.log("row")
        //
        //     window.open("http://218.83.152.167:21301/public-gateway/service-manage/manage/api/excel/exportexcel?memberId="
        //         +this.searchData.memberId,"_parent");
        //
        // },
        //
        //导出
        dataList(row,column,data){
            console.log("开始导出1",data)
            let tempArr = [];
            for(let o in data){
                let obj = {
                    memberId: data[o].memberId === undefined ? '' : data[o].memberId,
                    typeName: data[o].typeName === undefined ? '' : data[o].typeName,
                    enterpriseName: data[o].enterpriseName === undefined ? '' : data[o].enterpriseName,
                    realName: data[o].realName === undefined ? '' : data[o].realName,
                    phone: data[o].phone === undefined ? '' : data[o].phone,
                    email: data[o].email === undefined ? '' : data[o].email,
                    createTime: this.getLocalTime(data[o].createTime) === undefined ? '' : this.getLocalTime(data[o].createTime),
                    modifierName: data[o].modifierName === undefined ? '' : data[o].modifierName,
                }
                tempArr.push(obj)
            }
            console.log("开始导出")
            //列标题
            let str = '<tr><td>会员编号</td><td>身份类型</td><td>会员单位名称</td><td>联系人</td><td>联系方式</td><td>邮箱</td><td>申请认证时间</td><td>审核人</td></tr>';
            for(let i=0;i<tempArr.length;i++){
                str+='<tr>';
                for (let item in tempArr[i]) {
                    str+=`<td>${tempArr[i][item] + '\t'}</td>`;
                }
                str +='</tr>';
            }
            //Worksheet名
            let worksheet = 'Sheet1'
            let uri = 'data:application/vnd.ms-excel;base64,';

            //下载的表格模板数据
            let template = `<html xmlns:o="urn:schemas-microsoft-com:office:office"

                xmlns:x="urn:schemas-microsoft-com:office:excel"
                xmlns="http://www.w3.org/TR/REC-html40">

                <head><meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8"><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>
                <x:Name>${worksheet}</x:Name>
                <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>
                </x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->
                </head><body><table>${str}</table></body></html>`;

            //下载模板
            var link = document.createElement("a");
            link.download = "会员管理.xls";
            link.href = uri + this.getbase64(template);
            link.click();

        },
        getbase64(s){
            return window.btoa(unescape(encodeURIComponent(s)))
        },
        async exportexcel(row, column) {
            this.loading=true;
            let _this=this;
            this.$nextTick(async () => {
                let params = {
                    className: 'Member',
                    method: 'getmemberDetailList',
                    param: {
                        page: {
                            pageIndex: 1,
                            pageSize: 999999,
                        },
                        searchdata: this.searchData
                    }
                };
                let res = await _this.$store.dispatch('http/post', params);
                console.log("开始导出123" + res.dataList)
                this.loading = false;
                this.dataList(row, column, res.dataList);
            })
        },
        async exportexcel2(){
            this.ExportLoad=true
            let thisData = qs.stringify({
                memberId : this.searchData.memberId
            });
            axios({
                method: "POST",
                url: process.env.BASE_API +"/manage/Member/Memberload",
                data: thisData,
                responseType: 'blob',
            })
                .then(res => {
                this.ExportLoad=false
                const blob = new Blob([res.data]);//处理文档流
                const fileName = "会员认证审核.xls";
                const elink = document.createElement('a');
                elink.style.display = 'none';
                elink.href = window.URL.createObjectURL(blob);
                elink.setAttribute("download",fileName);
                document.body.appendChild(elink);
                elink.click();
                URL.revokeObjectURL(elink.href); // 释放URL 对象
                document.body.removeChild(elink);
            })
                .catch(error => {
                // 请求失败
                this.$message({showClose: true, message: '下载异常，请稍后再试', type: 'error'});
            });
        },

        // 分配会员号 审核弹窗
        allot(val) {
            console.log('val11',val)
            this.showLoading = true;
            // this.loading.form = true;
            this.selectedEnsureUserId = val.id;
            this.formData = {};
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getMemberDetailById',
                    param: {
                        id: this.selectedEnsureUserId

                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res22',res);
                this.formData = res;

                // this.showFirstTableData.forEach((e, i) => {
                //     e.comboPrice = (e.comboPrice === undefined || e.comboPrice == null || e.comboPrice == '') ? '无' : (e.comboPrice / 10000) + "w";
                // });
                // this.loading.form = false;
            });
        },

        //审核通过
        submitSelected() {
            // this.selectedEnsureMemberId = this.selectValue.id == null ? '' : this.selectValue.id;
            console.log('selectedEnsureUserId=',this.selectedEnsureUserId);
            this.formData.id = this.selectedEnsureUserId;
            this.formData.approvalstatus = '1';
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'updateApprovalStatus',
                    param: this.formData
                    // param: {
                    //     id: this.selectedEnsureUserId,
                    //     approvalstatus: '1', //1 已审核  0 未审核  ，2 拒绝 如果当前状态不是未审核状态0，无法更改
                    //     job:this.formData.job,
                    //     realName:this.formData.realName,
                    //     phone:this.formData.uphone,
                    //     email:this.formData.uemail,
                    // }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res333',res)

                if (res) {
                    this.showLoading = false;
                    this.alertMessage("操作成功", "success");
                    this.getUnApprovedCount();
                    this.getFirstTableData();//重新加载页面
                } else {
                    this.alertMessage("操作失败", "error");
                }
            });
        },

        // 审核拒绝
        cancelDialog() {
            this.formData.id = this.selectedEnsureUserId;
            this.formData.approvalstatus = '2';
            if (this.selectedEnsureUserId.length > 0) {
                this.$nextTick(async () => {
                    let params = {
                        className:'Member',
                        method: 'updateApprovalStatus',
                        param:this.formData
                        // param: {
                        //     id: this.selectedEnsureUserId,
                        //     approvalstatus: '2' //1 已审核  0 未审核  ，2 拒绝 如果当前状态不是未审核状态0，无法更改
                        // }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res333',res)

                    if (res) {
                        this.showLoading = false;
                        this.alertMessage("操作成功", "success");
                        this.getUnApprovedCount();
                        this.getFirstTableData();//重新加载页面
                    } else {
                        this.alertMessage("操作失败", "error");
                    }
                });
            }
            // if (this.$refs.multipleTable) {
            //     this.$refs.multipleTable.clearSelection();
            // }
            // this.selectValue = {};
        },


        //拒绝审核
        reject(val) {
            this.$confirm('此操作将拒绝该会员提交的审核信息, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'Member',
                        method: 'memberReviewFaild',
                        param: {
                            userId: val.id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    if (res) {
                        this.alertMessage("操作成功", "success");
                        this.getFirstTableData();//重新加载页面
                    } else {
                        this.alertMessage("操作失败", "error");
                    }
                    // 重新加载页面数据
                    this.getFirstTableData();
                });
            }).catch(() => {
                this.$message({type: 'info', message: '已取消'});
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
    float: right;
}

.table {
    width: 100%;
    padding: 1px 22px 15px 22px;

    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1;
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
    margin: 20px 22px 15px 22px
}

.assignUser {
    margin: 15px 22px;
}

.svg-container {
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    margin-left: -49px;
    display: inline-block;
}

/deep/.el-input {
    //width: 80%;
}

.dialog-footer {
    padding-right: 50px;
    margin-top: 45px;
}

.dialog-title span {
    font-size: 14px;
    font-weight: 500;
}

.selected-list {
    padding: 20px 20px 0px 20px;
}

.selected-personal {
    margin-bottom: 20px;
}

.selected-personal-title {
    font-size: 14px;
    font-weight: 600;
    color: #304156;
}

.selected-personal-item {
    position: relative;
    margin: 10px;
    color: #304156;
    height: auto;
    cursor: pointer;
    padding: 10px;
}

.itemInfo {
    border: 1px solid;
    padding: 5px;
    width: auto;
    border-radius: 5px;
    margin: 0 5px 0 0;
    line-height: 35px;
}

.userDiv {
    max-height: 300px;
    overflow: auto;
}

.userInput {
    width: 65%;
}

</style>

