<template>
    <div>
        <!-- 主页面 -->
        <div>
            <div class="selectBox">
                <el-row :getter="24" class="searchTwo">
                    <el-col :span="6">
                        编号：
                        <el-input placeholder="请输入用户编号" v-model="searchData.id" clearable size='small'
                                  class="userInput"></el-input>
                    </el-col>
                    <el-col :span="6">
                        用户类型：
                        <el-select v-model="searchData.uRole" placeholder="请选择" class="userInput" size="small">
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
                                   icon="el-icon-download"
                                   type="primary"
                                   @click.native.prevent="exportData()">
                            导出
                        </el-button>
                        <el-button size='small'
                                   icon="el-icon-refresh"
                                   type="primary"
                                   @click="reset">
                            重置
                        </el-button>
                    </el-col>
                    <upload-excel
                        class="uploadExcel"
                        ref="uploadExcel"
                        @submitUpload="submitUpload"
                        :allowSize="allowFileSize"
                        :allowType="allowFileType"
                        :file="uploadExcel"
                        @finishLoading="loadingFile=false"
                        @startLoading="loadingFile=true"
                    ></upload-excel>
                </el-row>




            </div>
            <div class="container">
                <div class="top">
                    <div class="left">用户身份信息</div>
                    <div class="right">
                        <el-button type="success"
                                   icon="el-icon-plus"
                                   size="small"
                                   plain
                                   @click='add'>添加用户
                        </el-button>
                    </div>
                </div>
                <div class="table">
                    <el-table
                        :data="firstTableData"
                        stripe>
                        <el-table-column label="用户编号" prop="userId" align="center" min-width="250"></el-table-column>
                        <el-table-column label="昵称" prop="nickName" align="center" min-width="180"></el-table-column>
<!--                        <el-table-column label="用户名" prop="realName" align="center" min-width="150"></el-table-column>-->
<!--                        <el-table-column label="性别" prop="gender"-->
<!--                                         :formatter="formatSex" align="center" min-width="100"></el-table-column>-->

                        <el-table-column label="认证标记" prop="certificationMark"
                                         :formatter="formatMark" align="center" min-width="150"></el-table-column>
                        <el-table-column label="角色身份" prop="uRole"
                                         :formatter="formatUrole" align="center" min-width="150"></el-table-column>

<!--                        <el-table-column label="openId" prop="openId" align="center" min-width="280"></el-table-column>-->
                        <el-table-column label="创建时间" prop="createTime" align="center" min-width="250"></el-table-column>
<!--                        <el-table-column label="创建者" prop="creatorId" align="center" min-width="250"></el-table-column>-->
                        <el-table-column label="修改时间" prop="modifyTime" align="center" min-width="250"></el-table-column>
                        <el-table-column label="姓名" prop="realName" align="center" min-width="150"></el-table-column>
                        <el-table-column label="公司名称" prop="enterpriseName" align="center" min-width="150"></el-table-column>
                        <el-table-column label="职位" prop="job" align="center" min-width="150"></el-table-column>
<!--                        <el-table-column label="修改者" prop="modifierId" align="center" min-width="250"></el-table-column>-->

                        <el-table-column label="操作" align="center" fixed="right" min-width="340">
                            <template slot-scope="scope">
                                <el-button size="mini" type="info" plain @click="show(scope.row)">查看</el-button>
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
            </div>
        </div>

        <!--        <el-button size="mini" @click="showLoading = true">查看</el-button>-->

    </div>
</template>

<script>
import UploadExcel from "@C/UploadExcel";
import Treeselect from "@riophae/vue-treeselect";

export default {
    name: "userInfoManage",
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
        UploadExcel
    },
    data() {
        return {
            // 搜索对象
            searchData: {},
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
            loadingFile: false,
            allowFileType: ['xls', 'xlsx'],
            allowFileSize: 10,
            uploadExcel: [],
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
        //上传(导入数据)
        submitUpload(serviceFilePath) {
            this.$nextTick(async () => {
                let param = {
                    filePath: serviceFilePath
                }
                let params = {
                    className: 'userMange',
                    method: 'exportUser',
                    param: param
                }
                let res = await this.$store.dispatch("http/post", params);
                if (res.code == '200') {
                    this.$message({message: '导入成功', type: 'success'});
                    this.getFirstTableData();//重新加载表格信息
                    this.uploadExcel = [];
                    this.$refs.uploadExcel.init();
                } else {
                    this.$message.error(res.code + ':' + res.msg);
                }
            });
        },
        formatSex(row, column, cellValue) {
            if (cellValue == 0){
                return '男';
            }else if (cellValue == 1){
                return '女';
            }else if (cellValue == 2){
                return '中性';
            }
        },
        formatMark(row, column, cellValue) {
            if (cellValue == 0){
                return '未认证';
            }else if (cellValue == 1){
                return '审核中';
            }else if (cellValue == 2){
                return '审核失败';
            }else if (cellValue == 3){
                return '已认证';
            }
        },
        formatUrole(row, column, cellValue) {
            if (cellValue == 1){
                return 'GP';
            }else if (cellValue == 2){
                return '科创';
            }else if (cellValue == 3){
                return 'LP';
            }else if (cellValue == 4){
                return '合作伙伴';
            }else if (cellValue == 5){
                return '创业公司';
            }
        },
        // formatUrole(value) {
        //     if(value == '1'){
        //         return 'GP ';
        //     }if(value =='2'){
        //         return '科创';
        //     }
        //     if(value =='3'){
        //         return 'LP';
        //     }
        //     if(value =='4'){
        //         return '供应商';
        //     }
        //     if(value =='5'){
        //         return '创业公司';
        //     }
        // },
        formatDate(value) {
            let date = new Date(value);
            let y = date.getFullYear();
            let MM = date.getMonth() + 1;
            MM = MM < 10 ? ('0' + MM) : MM;
            let d = date.getDate();
            d = d < 10 ? ('0' + d) : d;
            let h = date.getHours();
            h = h < 10 ? ('0' + h) : h;
            let m = date.getMinutes();
            m = m < 10 ? ('0' + m) : m;
            let s = date.getSeconds();
            s = s < 10 ? ('0' + s) : s;
            return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
        },
        getbase64(s){
            return window.btoa(unescape(encodeURIComponent(s)))
        },

        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
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

        reset() {
                this.searchData = {}
            this.getFirstTableData();
        },
        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        // 获取用户列表
        getFirstTableData() {
            this.loading.btn = true;
            this.firstTableData = [];
            console.log('this.searchData=',this.searchData);
            this.$nextTick(async () => {
                let params = {
                    className:'userMange',
                    method: 'getUserList',
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
                    e.createTime = this.getLocalTime(e.createTime);
                    e.modifyTime = this.getLocalTime(e.modifyTime);
                });
                this.paginationData.total = res.page.totalNum;
                this.loading.btn = false;
            });
        },

        // 添加
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        //查询
        show(row) {
            if (this.onDetail) {
                this.onDetail(row);
            }
        },

        // 编辑
        edit(row) {
            if (this.onEdit) {
                this.onEdit(row);
            }
        },

        // 删除
        del(val) {
            this.$confirm('此操作将永久删除该用户信息, 是否继续?', '提示', {
                confirmButtonText: '确定',

                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'userMange',
                        method: 'deleteUser',
                        param: {
                            id: val.id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res111=',res)
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

        //导出
        dataList(row,column,data){
            console.log("开始导出1",data)
            let tempArr = [];
            for(let o in data){
                let obj = {
                    name: data[o].nickName === undefined ? '' : data[o].nickName,
                    gender: this.formatSex(row,column,data[o].gender) === undefined ? '' : this.formatSex(row,column,data[o].gender),
                    phone: data[o].phone === undefined ? '' : data[o].phone,
                    email: data[o].email === undefined ? '' : data[o].email,
                    enterpriseName: data[o].enterpriseName === undefined ? '' : data[o].enterpriseName,
                    uRole: this.formatUrole(row,column,data[o].uRole) === undefined ? '' : this.formatUrole(row,column,data[o].uRole),
                    createTime: this.getLocalTime(data[o].createTime) === undefined ? '' : this.getLocalTime(data[o].createTime),
                }
                tempArr.push(obj)
            }
            console.log("开始导出")
            //列标题
            let str = '<tr><td>昵称</td><td>手机号</td><td>邮箱</td><td>企业名称</td><td>用户类型</td><td>创建时间</td></tr>';
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
            link.download = "人员管理.xls";
            link.href = uri + this.getbase64(template);
            link.click();

        },

        //导出功能
        exportData(row, column){
            this.loading=true;
            let _this=this;
            this.$nextTick(async ()=>{
                let params = {
                    className:'userMange',
                    method: 'getUserList',
                    param: {
                        page: {
                            pageIndex: 1,
                            pageSize: 999999,
                        },
                        searchdata: this.searchData
                    }
                };
                let res = await _this.$store.dispatch('http/post', params);
                console.log("开始导出123"+res.dataList)
                this.loading=false;
                this.dataList(row, column,res.dataList);
            })
        },

        async exportexcel(){
            console.log("row")

            window.open("http://218.83.152.167:21301/public-gateway/service-manage/manage/api/excel/exportexcel?id="
                +this.searchData.id,"_parent");

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
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'updateApprovalStatus',
                    param: {
                        id: this.selectedEnsureUserId,
                        approvalstatus: '1' //1 已审核  0 未审核  ，2 拒绝 如果当前状态不是未审核状态0，无法更改
                    }
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
            if (this.selectedEnsureUserId.length > 0) {
                this.$nextTick(async () => {
                    let params = {
                        className:'Member',
                        method: 'updateApprovalStatus',
                        param: {
                            id: this.selectedEnsureUserId,
                            approvalstatus: '2' //1 已审核  0 未审核  ，2 拒绝 如果当前状态不是未审核状态0，无法更改
                        }
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
    width: 100%;
    margin-top: 35px;
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

/deep/.uploadExcel .el-upload__tip {
    position: absolute;
    right: 10%;
}
</style>

