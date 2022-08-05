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
                <el-col :span="6">
                    机构类型：
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
                    机构名称：
<!--                    <el-input placeholder="请输入会员名称" v-model="searchData.enterpriseName" clearable size='small'-->
<!--                              class="userInput"></el-input>-->
                    <el-autocomplete
                        class="userInput"
                        v-model="searchData.enterpriseName"
                        :fetch-suggestions="querySearch"
                        placeholder="请输入内容"
                        :trigger-on-focus="false"
                        @select="handleSelect"
                        clearable
                        size='small'
                    ></el-autocomplete>
                </el-col>

            </el-row>

            <el-row :getter="24" class="searchTwo" type="flex" justify="end">
                <el-col class="searchDataBtn">
                    <el-row>
                        <el-button size='small'
                                   icon="el-icon-search"
                                   type="primary"
                                   @click="handleQuery">
                            查询
                        </el-button>
                    </el-row>
                    <el-row>
                        <el-button size='small'
                                   icon="el-icon-download"
                                   type="primary"
                                   @click.native.prevent="exportexcel2()">
                            导出
                        </el-button>
                    </el-row>
                    <el-row>
                        <el-button size='small'
                                   icon="el-icon-refresh"
                                   type="primary"
                                   @click="reset">
                            重置
                        </el-button>
                    </el-row>
                    <el-row>
                        <!--<template slot-scope="scope">-->

                        <!--</template>-->
                        <!--                    <el-col :span="12">-->
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
                        <!--                    </el-col>-->
                    </el-row>
                </el-col>
            </el-row>

        </div>
        <div class="container">
            <div class="top">
                <div class="left">机构基本信息</div>
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
                    <el-table-column label="企业账号" prop="enterpriseCode" align="center" min-width="200"></el-table-column>
                    <el-table-column label="机构名称" prop="enterpriseName" align="center"
                                     min-width="200"></el-table-column>
                    <el-table-column label="机构类型" prop="typeName" align="center" min-width="150"></el-table-column>
<!--                    <el-table-column label="认证码" prop="pwd" align="center" min-width="150"></el-table-column>-->

<!--                    <el-table-column label="套餐购买" prop="comboPrice" align="center" min-width="100"></el-table-column>-->
<!--                    <el-table-column label="客户经理" prop="accountManagerName" align="center"-->
<!--                                     min-width="100"></el-table-column>-->
                    <el-table-column label="创建时间" prop="createTime" align="center" width="180"></el-table-column>
                    <el-table-column label="是否导入" prop="flag" align="center" width="180"></el-table-column>
<!--                    <el-table-column label="联系人/主要合伙人" prop="contact" align="center" min-width="150"></el-table-column>-->
<!--                    <el-table-column label="联系方式" prop="phone" align="center" min-width="200"></el-table-column>-->
<!--                    <el-table-column label="邮箱" prop="email" align="center" min-width="250"></el-table-column>-->
                    <el-table-column label="操作" align="center" fixed="right" width="340">
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
</template>

<script>
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import UploadExcel from "@C/UploadExcel";
import axios from 'axios'

export default {
    name: "member-manage",
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
        Treeselect, UploadExcel
    },
    data() {
        return {
            //excel上传 start
            loadingFile: false,
            allowFileType: ['xls', 'xlsx'],
            allowFileSize: 10,
            uploadExcel: [],
            formData: {
                urlDownload: [],
                fileName: '',
                fileUrl: '',
            },
            memberTypeList: [],
            //excel上传 end

            // 搜索对象
            searchData: {
                // memberId: '', //会员编号
                // enterpriseName: '', //会员名称
                // comboId: '', //套餐类型
                // accountManager: null, //客户经理
                 startTime: '', //开始时间
                 endTime: '', //结束时间
                memberType: '',
                enterpriseName: ''
            },
            enterpriseList: [],
            loading: {
                form: false,
            },
            comboOptions: [], //用来存储下拉框的套餐类型信息
            departmentOptions: null,//用来存储下拉框的客户经理信息
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
        this.getEnterpriseList();
    },

    // 方法
    methods: {
        // 获取会员列表
        getFirstTableData() {
            console.log('this.searchData=',this.searchData)
            //验证时间
            if (this.dateVerify()) {
                this.firstTableData = [];
                this.$nextTick(async () => {
                    let params = {
                        className: 'Member',
                        method: 'getMemberInfoPageVo',
                        param: {
                            page: {
                                pageIndex: this.paginationData.currentPage,
                                pageSize: this.paginationData.pageSize,
                            },
                            searchdata: this.searchData,
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res111',res)
                    this.firstTableData = res.dataList;
                    this.firstTableData.forEach((e, i) => {
                        e.comboPrice = (e.comboPrice === undefined || e.comboPrice == null || e.comboPrice == '') ? '无' : (e.comboPrice / 10000) + "w";
                        e.createTime = this.getLocalTime(e.createTime);
                    });

                    this.paginationData.total = res.page.totalNum;
                });
            }
        },
        getEnterpriseList() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Member',
                    method: 'getMemberInfoPageVo',
                    param: {
                        page: {
                            pageIndex: 1,
                            pageSize: 9999999999,
                        },
                        searchdata: {},
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res222',res);
                res.dataList.forEach((e, i) => {
                    this.enterpriseList.push({
                        value: e.enterpriseName
                    });
                });
                console.log('this.enterpriseList=',this.enterpriseList);
            });
        },

        querySearch(queryString, cb) {
            let arr = this.enterpriseList;
            let results = queryString ? arr.filter(this.createFilter(queryString)) : arr;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        createFilter(queryString) {
            return (e) => {
                return (e.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        handleSelect(item) {
            console.log(item);
        },

        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        //上传(导入数据)
        submitUpload(serviceFilePath) {
            this.$nextTick(async () => {
                let param = {
                    filePath: serviceFilePath
                }
                let params = {
                    className: 'Member',
                    method: 'batchImportMember',
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

        //模板下载
        templateDownLoad() {
            //列标题
            let str = '<tr>' +
                '<th>企业账号(2开头的10位数字)</th><th>密码(6-20位)</th><th>会员名称</th><th>联系人</th><th>联系方式</th>' +
                '<th>邮箱</th><th>会员套餐</th><th>客户经理名称</th><th>客户经理编号</th>' +
                '<th>套餐选择(xx,...,xx)</th><th>联系地址(可为空)</th><th>备注(可为空)</th>' +
                '</tr>' + '<tr>' +
                '<td>2xxxxxxxxxx</td><td>xxx...xxx</td><td>xx...</td><td>xx...</td><td>1xxxxxxxxxxx</td>' +
                '<td>xx...@...xx.com</td><td>xx...套餐</td><td>xx...</td><td>xx...</td>' +
                '<td>xx...,xx...,xx...</td><td>xx省xx市xx区xx街道xx号xx</td><td>xx...</td>' +
                '</tr>';

            //Worksheet名
            let worksheet = 'Sheet1';
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
            link.download = "会员信息.xlsx";
            link.href = uri + this.getbase64(template);
            link.click();
            this.$message({
                message: '下载的模板不可直接使用，请重新保存出新文件后使用',
                type: 'info',
            });
        },
        // getbase64(s) {
        //     return window.btoa(unescape(encodeURIComponent(s)))
        // },

        //重置
        reset() {
            this.searchData = {
                startTime: '',
                endTime: '',
                memberType: '',
                enterpriseName: ''
            }
            this.getFirstTableData();
        },

        //获取页面相关信息
        getPageMessage() {
            this.getComboOptions(); //获取套餐的下拉框信息
            this.getAccountManagerOptions(); //获取客户经理的下拉框信息
            this.getFirstTableData(); // 页面创建时获取后台数据
            this.getMemberTypeList();
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

        //获取套餐的下拉框信息
        getComboOptions() {
            this.comboOptions = [];
            this.$nextTick(async () => {
                let params = {
                    className: 'Member',
                    method: 'selectComboOfCodeAndNameList',
                    params: {}
                }

                let res = await this.$store.dispatch('http/post', params)
                this.comboOptions = res;
            });
        },

        //获取客户经理的下拉框信息
        getAccountManagerOptions() {
            this.accountManagerOptions = [];
            this.$nextTick(async () => {
                //查询机构信息
                const departmentParams = {
                    className: 'Member',
                    method: 'selectDepartmentOfCodeAndNameList',
                    param: {}
                };
                this.departmentOptions = await this.$store.dispatch('http/post', departmentParams);
            });
        },

        // 根据机构动态加载行内用户信息
        async loadOptionsAccountManager({action, parentNode, callback}) {
            if (action === 'LOAD_CHILDREN_OPTIONS') {
                const params = {
                    className: 'Member',
                    method: 'selectUserByDepartmentIdAndUroleEqZeroList',
                    param: {
                        departmentId: parentNode.id,
                    }
                };
                parentNode.children = await this.$store.dispatch('http/post', params);
                // 回调
                callback();
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

       async exportexcel(){
                console.log("row")
                /*let fd = {


                };*/
            // debugger;
            //     // $.ajax("/manage/api/file/exportexcel",fd,{ responseType: 'blob'})
            //     $.axios({
            //         method: "POST",
            //         url: "manage/api/file/exportexcel",
            //         data: fd,
            //         responseType: 'blob',
            //         async:false,
            //     })
            //         .then(res => {
            //             alert("1111")

               /* const res = await axios.post('/api/file/exportexcel')

               console.log(res)
                    const blob = new Blob([res.data]);//处理文档流
                const fileName = this.listData[row].fileName;
                const elink = document.createElement('a');
                elink.style.display = 'none';
                elink.href = window.URL.createObjectURL(blob);
                elink.setAttribute("download",fileName);
                document.body.appendChild(elink);
                elink.click();
                URL.revokeObjectURL(elink.href); // 释放URL 对象
                document.body.removeChild(elink);*/
        //     })
        //     .catch(error => {
        // alert("222")
        //             // 请求失败
        //             this.$message({showClose: true, message: '下载异常，请稍后再试', type: 'error'});
        //     });
       /*window.open("http://127.0.0.1:8081/manage/api/excel/exportexcel?memberType="+this.searchData.memberType
           +"&startTime="+this.searchData.startTime+"&endTime="+this.searchData.endTime,"_parent");*/
    window.open("http://218.83.152.167:21301/public-gateway/service-manage/manage/api/excel/exportexcel?memberType="+this.searchData.memberType
        +"&startTime="+this.searchData.startTime+"&endTime="+this.searchData.endTime+"&enterpriseName="+this.searchData.enterpriseName,"_parent");
            /*let params = {param: fd, url: 'api/excel/exportexcel'};
            await this.$store.dispatch('http/fileUpload2', params);*/
            //console.log(response, 'sfdsd')
            /*if (response.code === 200) {
                this.serviceFilePath = response.data;
                this.$message({
                    message: '读取成功',
                    type: 'success'
                });
            } else {
                this.fileList = [];
                this.showName = `只支持xlsx和xls结尾的文件,最大上传文件为${this.allowSize}MB！`;
                this.$message({message: '读取失败: ' + response.msg, type: 'error'});
            }*/

        },

        //导出
        dataList(row,column,data){
            console.log("开始导出1",data)
            let tempArr = [];
            for(let o in data){
                let obj = {
                    enterpriseCode: data[o].enterpriseCode === undefined ? '' : data[o].enterpriseCode,
                    enterpriseName: data[o].enterpriseName === undefined ? '' : data[o].enterpriseName,
                    typeName: data[o].typeName === undefined ? '' : data[o].typeName,
                    createTime: this.getLocalTime(data[o].createTime) === undefined ? '' : this.getLocalTime(data[o].createTime),
                    flag: data[o].flag === undefined ? '' : data[o].flag,
                    contact: data[o].contact === undefined ? '' : data[o].contact,
                    phone:data[o].phone === undefined ? '' : data[o].phone,
                    email:data[o].email === undefined ? '' : data[o].email,
                }
                tempArr.push(obj)
            }
            console.log("开始导出")
            //列标题
            let str = '<tr><td>企业账号</td><td>会员名称</td><td>会员类型</td><td>创建时间</td><td>是否导入</td><td>联系人/主要合伙人</td><td>联系方式</td><td>邮箱</td></tr>';
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
            link.download = "会员信息.xls";
            link.href = uri + this.getbase64(template);
            link.click();

        },
        getbase64(s){
            return window.btoa(unescape(encodeURIComponent(s)))
        },
        async exportexcel2(row, column) {
            this.loading=true;
            let _this=this;
            this.$nextTick(async () => {
                let params = {
                    className: 'Member',
                    method: 'getMemberInfoPageVo',
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

        // 添加
        add() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        //查询
        show(row) {
            console.log('row=',row)
            if (this.onDetail) {
                this.onDetail(row.id, row.memberType);
            }
        },

        // 编辑
        edit(row) {
            if (this.onEdit) {
                this.onEdit(row.id, row.memberType);
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
                        method: 'deleteMember',
                        param: {
                            memberId: val.id
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
    justify-content: flex-end;
    .el-row {
        margin: 10px 8px;
    }
}
/deep/.el-row .upload-demo {
    //margin-top: 20px;
    //margin-left: 75%;
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

.uploadExcel {
    //margin-left: 77%;
}

</style>

