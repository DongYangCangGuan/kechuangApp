<template>
    <!-- 主页面 -->
    <div>
        <div class="selectBox">
            <el-row :getter="24">
                <el-col :span="6">
                    产品名称：
                    <el-input class="userInput"
                              placeholder="请输入产品名称"
                              v-model="searchData.productName"
                              clearable
                              size='small'
                    ></el-input>
                </el-col>
                <el-col :span="6">
                    产品编号：
                    <el-input class="userInput"
                              placeholder="请输入产品编号"
                              v-model="searchData.id"
                              clearable
                              size='small'
                    ></el-input>
                </el-col>
                <el-col :span="6" style="position: relative;">
                        产品类型：
                    <el-cascader
                        class="userInput"
                        size='small'
                        v-model="cascaderList"
                        :options="productTypeList"
                        :props="defaultProps"
                        :show-all-levels="false">
                    </el-cascader>

<!--                    <el-input class="userInput"-->
<!--                              placeholder="请选择产品类型"-->
<!--                              v-model="typeName"-->
<!--                              @focus="projectOrgFun"-->
<!--                              size='small'-->
<!--                    >-->
<!--                    </el-input>-->
<!--                    <el-tree  class="ORGTree"-->
<!--                              v-show="ishowTree"-->
<!--                              ref="tree"-->
<!--                              :data="productTypeList"-->
<!--                              key="id"-->
<!--                              node-key="id"-->
<!--                              highlight-current-->
<!--                              @node-click="handleNodeClick"-->
<!--                              :props="defaultProps">-->
<!--                    </el-tree>-->

<!--                    <treeselect-->
<!--                        v-model="searchData.productTypeList"-->
<!--                        :load-options="comboDictionaryDisabled"-->
<!--                        :options="productTypeList"-->
<!--                        :disable-branch-nodes="true"-->
<!--                        placeholder="请选择类型"-->
<!--                        @select="changeSelect"-->
<!--                        @input="inputChange"-->
<!--                        :searchable="false"-->
<!--                        :clearable="true"-->
<!--                        :normalizer="normalizer"-->
<!--                        noChildrenText="没有数据"-->
<!--                        loadingText="加载中..."-->
<!--                        class="userInput"-->
<!--                    />-->
                </el-col>

            </el-row>

            <el-row :getter="24" class="searchTwo">
                <el-col :span="6">
                    产品状态：
                    <el-select v-model="searchData.status" placeholder="请选择" class="userInput" size="small">
                        <el-option
                            v-for="(item,index) in statusList"
                            :key="index"
                            :label="item.name"
                            :value="item.code"
                        ></el-option>
                    </el-select>
                </el-col>
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
                                   icon="el-icon-refresh"
                                   type="primary"
                                   @click="reset">
                            重置
                        </el-button>
                    </el-row>
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
                <div class="left">产品信息</div>
                <div class="right">
                    <el-button type="success" icon="el-icon-plus" size="small" plain @click='add'>添加
                    </el-button>
                </div>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe
                    style="width: 100%;text-align: center;border:1px solid #bbc8c1">
<!--                    <el-table-column label="产品序号" prop="proSeq" align="center" min-width="180"></el-table-column>-->
                    <el-table-column label="缩略图" prop="pic" align="center" min-width="200">
                        <template slot-scope="scope">
                            <img :src="scope.row.pic" width="80" height="80">
                        </template>
                    </el-table-column>
                    <el-table-column label="产品编号" prop="id" align="center" min-width="200"></el-table-column>
                    <el-table-column label="产品名称" prop="productName" align="center" min-width="200"></el-table-column>
                    <el-table-column label="产品简介" prop="introduction" align="center" min-width="250" show-overflow-tooltip></el-table-column>
                    <el-table-column label="产品类型" prop="typeName" align="center" min-width="150"></el-table-column>
                    <el-table-column label="上传时间" prop="createTime" align="center" min-width="180"></el-table-column>
                    <el-table-column label="产品状态" align="center" min-width="150">
                        <template slot-scope="scope">
<!--                            {{ scope.row.status === 0 ? "无效" : "有效" }}-->
                            <div v-if="scope.row.status == 0">无效</div>
                            <div v-if="scope.row.status == 1">有效</div>
                            <div v-if="scope.row.status == 2" >已发布</div>
                        </template>
                    </el-table-column>
                    <el-table-column label="合作方" prop="departmentName" align="center" min-width="180"></el-table-column>
                    <el-table-column label="发布人" prop="modifierName" align="center" min-width="180"></el-table-column>

                    <el-table-column label="操作" align="center" fixed="right" min-width="320">
                        <template slot-scope="scope">
<!--                            (productPermission === true) && (scope.row.status === 2)-->
                            <el-button v-if="(productPermission == true) && (scope.row.status == 1)" size="mini" type="info" plain @click="issue(scope.row)">发布</el-button>
                            <el-button v-if="(productPermission == true) && (scope.row.status == 2)" size="mini" type="info" plain >已发布</el-button>
                            <el-button size="mini" type="success" plain @click="show(scope.row)">查看</el-button>
                            <el-button size="mini" type="primary" plain @click="edit(scope.row)">编辑</el-button>
                            <el-button size="mini" v-if="scope.row.status=='0'" type="danger" plain >已下架</el-button>
                            <el-button size="mini" v-else type="danger" plain @click="deleteReport(scope.row.id)">下架</el-button>
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

export default {
    name: "report-manage",
    components: {
        Treeselect, UploadExcel
    },
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
            productTypeList: [],
            //excel上传 end

            ishowTree: false,
            defaultProps: {
                // children: 'tree',
                label: 'name',
                value: 'code'
            },
            cascaderList: [],
            inputId:'',
            typeName: '',
            statusList: [{
                name: '未发布',
                code: '1'
            },{
                name: '已发布',
                code: '2'
            },{
                    name: '已下架',
                    code: '0'
            },],
            // 搜索对象
            searchData: {
                productName: '',
                id: '',
                productType: '',
                status: '',
                startTime: '',
                endTime: ''
            },
            searchData2: {},
            reportKinds: null,

            firstTableData: [],
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
            comboDictionaryDisabled: false,     //是否禁用控件
            comboDictionaryOptions: null,
            normalizer: (node) => {                  //用于规范化源数据
                return {
                    id: node.id,
                    label: node.name,
                    children: node.children,
                }
            },
            loading: {
                form: false,
                btn: false
            },
            productPermission: ''
        };
    },

    // 钩子函数
    created() {
        this.getProductPermission();
        // this.getPageMessage(); // 页面创建时获取后台数据
        this.getFirstTableData();
        this.getproductTypeList();
    },
    mounted() {
        this.loading.form = true;

    },

    // 方法
    methods: {

        // //套餐中报告属性的选中事件
        // comboSelect(node,a) {
        //     // this.reportKinds.push(node)
        //     console.log(node, a)
        //     // if (this.form.reportKinds.length > this.comboValue - 1) {
        //     //     this.$message({message: '操作取消，已达到该套餐的最大选择数！', type: 'warning'});
        //     // }
        // },
        // deselect(node) {
        //     let index = this.reportKinds.findIndex(item => item.id === node.id);
        //     this.reportKinds.splice(index, 1);
        //     console.log(this.reportKinds)
        // },

        //延迟加载
        // loadOptions({ action, parentNode, callback }) {
        //     if (action === 'LOAD_CHILDREN_OPTIONS') {
        //         setTimeout(fn, 1000)
        //         // 回调
        //         callback();
        //     }
        // },

        //带条件查询
        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        //获取该用户是否有发布、编辑产品的权限
        getProductPermission() {
            this.$nextTick(async () => {
                let params = {
                    className:'produce',
                    method:'getProductPermission',
                    param:{}
                };
                let res = await this.$store.dispatch('http/post', params);
                this.productPermission = res;
                console.log('this.productPermission==',this.productPermission)
            });
        },

        //获取页面加载信息
        getPageMessage() {
            this.$nextTick(async () => {

                //查询产品的相关属性信息
                const dictionaryParams = {
                    className: 'Member',
                    method: 'selectDictionaryByPropertyList',
                    param: {}
                };
                this.comboDictionaryOptions = await this.$store.dispatch('http/post', dictionaryParams);
                this.comboDictionaryOptions.forEach((e) => {
                    e.label = e.name;
                    e.children.forEach((e) => {
                        e.label = e.name;
                        if (e.children == null || e.children == '' || e.children.length == 0) e.children = '';
                    });
                });
                this.loading.form = false;
            })
        },

        projectOrgFun() {
            this.ishowTree = true
        },

        closeTree(){
            this.ishowTree = false
        },

        handleNodeClick(e){
            console.log('e==',e);
            // let res = this.$refs.tree.getCurrentKey()
            // this.searchData.productType = e.name
            this.typeName = e.name
            this.inputId = e.code
            this.ishowTree = false
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

        getproductTypeList() {
            this.$nextTick(async () => {
                let params = {
                    className:'produce',
                    method: 'getProductTypes'
                };
                let res = await this.$store.dispatch('http/post', params);

                for(let i = 0;i< res.length;i++) {
                    if( res[i].children && res[i].children.length > 0) {
                        let arr = res[i].children;
                        for(let j=0;j<arr.length;j++) {
                            if(arr[j].children.length > 0) {
                                let rootArr = arr[j].children
                                for (let k=0;k<rootArr.length;k++) {
                                    if(rootArr[k].children.length > 0) {
                                        rootArr[k] = rootArr[k]
                                    } else {
                                        this.deleteParam(rootArr[k])
                                    }
                                }
                            } else {
                                this.deleteParam(arr[j])

                            }
                        }
                    } else {
                        this.deleteParam(res[i])
                    }
                }

                this.productTypeList = res;
                console.log('this.productTypeList=',this.productTypeList)
            });
        },
        deleteParam(obj) {
            for(let e in obj) {
                if(e == 'children') {
                    delete obj[e]
                }
            }
        },

        // 获取产品列表
        getFirstTableData() {
            console.log(this.getproductTypeList());
            this.inputId = this.cascaderList.pop();
            this.searchData.productType = this.inputId;
            console.log('this.searchData111=',this.searchData);



            //验证时间
            if (this.dateVerify()) {
                this.firstTableData = [];
                this.$nextTick(async () => {
                    let params = {
                        className:'produce',
                        method: 'getproduces',
                        param: {
                            page: {
                                pageIndex: this.paginationData.currentPage,
                                pageSize: this.paginationData.pageSize,
                            },
                            searchdata: this.searchData,
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('res111=',res);
                    this.firstTableData = res.dataList;
                    this.firstTableData.forEach((e, i) => {
                        e.createTime = this.getLocalTime(e.createTime);
                    });
                    this.paginationData.total = res.page.totalNum;
                    // this.getPic(this.firstTableData);
                });
            }
        },

        //将路径转成base64位的图片
        getPic(tableData) {
            let base = this;
            tableData.forEach(function (item) {
                base.$nextTick(async () => {
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
            });
        },

        //重置
        reset() {
            this.cascaderList = [];
            this.inputId = '';
            this.typeName = '';
            this.searchData = {
                productName: '',
                id: '',
                productType: '',
                status: '',
                startTime: '',
                endTime: ''
            }
            this.getFirstTableData();
        },

        //模板下载
        templateDownLoad() {
            //列标题
            // let str = '<tr>' +
            //     '<th>企业账号(2开头的10位数字)</th><th>密码(6-20位)</th><th>会员名称</th><th>联系人</th><th>联系方式</th>' +
            //     '<th>邮箱</th><th>会员套餐</th><th>客户经理名称</th><th>客户经理编号</th>' +
            //     '<th>套餐选择(xx,...,xx)</th><th>联系地址(可为空)</th><th>备注(可为空)</th>' +
            //     '</tr>' + '<tr>' +
            //     '<td>2xxxxxxxxxx</td><td>xxx...xxx</td><td>xx...</td><td>xx...</td><td>1xxxxxxxxxxx</td>' +
            //     '<td>xx...@...xx.com</td><td>xx...套餐</td><td>xx...</td><td>xx...</td>' +
            //     '<td>xx...,xx...,xx...</td><td>xx省xx市xx区xx街道xx号xx</td><td>xx...</td>' +
            //     '</tr>';
            //
            // //Worksheet名
            // let worksheet = 'Sheet1'
            // let uri = 'data:application/vnd.ms-excel;base64,';
            //
            // //下载的表格模板数据
            // let template = `<html xmlns:o="urn:schemas-microsoft-com:office:office"
            //     xmlns:x="urn:schemas-microsoft-com:office:excel"
            //     xmlns="http://www.w3.org/TR/REC-html40">
            //     <head><meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8"><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>
            //     <x:Name>${worksheet}</x:Name>
            //     <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>
            //     </x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->
            //     </head><body><table>${str}</table></body></html>`;
            //
            // //下载模板
            // var link = document.createElement("a");
            // link.download = "会员信息.xlsx";
            // link.href = uri + this.getbase64(template);
            // link.click();
            // this.$message({
            //     message: '下载的模板不可直接使用，请重新保存出新文件后使用',
            //     type: 'info',
            // });
            this.$message({
                message: '该功能暂时不支持',
                type: 'info'
            })
        },
        // getbase64(s) {
        //     return window.btoa(unescape(encodeURIComponent(s)))
        // },

        //上传(导入数据)
        submitUpload(serviceFilePath) {
            this.$message({
                message: '上传未定义，无法上传',
                type: 'info'
            });
            // this.$nextTick(async () => {
            //     let param = {
            //         filePath: serviceFilePath
            //     }
            //     let params = {
            //         className: 'Member',
            //         method: 'batchImportMember',
            //         param: param
            //     }
            //     let res = await this.$store.dispatch("http/post", params);
            //     console.log(res, 'res');
            //     if (res.code == '200') {
            //         this.$message({message: '导入成功', type: 'success'});
            //         this.getFirstTableData();//重新加载表格信息
            //         this.uploadExcel = [];
            //         this.$refs.uploadExcel.init();
            //     } else {
            //         this.$message.error(res.code + ':' + res.msg);
            //     }
            // });
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

        //发布
        issue(row) {
            console.log('row===',row);
            this.$confirm('是否确定发布此产品?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className:'produce',
                        method:'releaseProductById',
                        param:{
                            id: row.id //产品id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    if (res) {
                        this.$message({message: '已成功发布', type: 'success'});
                    }
                    // 重新加载页面数据
                    this.getFirstTableData();
                });
            }).catch(() => {
                this.$message({type: 'info', message: '取消发布'});
            });
        },

        // 编辑
        edit(row) {
            console.log(row)
            if (this.onEdit) {
                this.onEdit(row.id,row.productType);
            }
        },

        //查看
        show(row) {
            console.log('row=',row)
            if (this.onDetail) {
                this.onDetail(row.id,row.productType);
            }
        },

        // 删除报告
        deleteReport(id) {
            this.$confirm('是否确定下架此产品?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className:'produce',
                        method: 'deleteProductById',
                        param: {
                            id: id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    console.log('22222',res);
                    if (res) {
                        this.$message({message: '已成功下架', type: 'success'});
                    }
                    // 重新加载页面数据
                    this.getFirstTableData();
                });
            }).catch(() => {
                this.$message({type: 'info', message: '取消下架'});
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


<style lang="scss" scoped>
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
    width: 30px;
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

.userInput {
    width: 65%;
}

.treeselectStyle {
    z-index: 100;
    display: flex;
    //align-items: center;
    flex-direction: column;
}

.searchTwo {
    margin-top: 8px;
}

.searchDataBtn {
    display: flex;
    //justify-content: center;
    .el-row {
        margin: 0px 8px!important;
    }
}
.selectBox .el-row {
    margin: 15px 0;
}
/deep/.treeselectStyle .el-input {
    width: 65%;
}
//.el-tree{
//    position:absolute;
//    cursor:default;
//    background:#fff;
//    color:#606266;
//    z-index:100;
//    border:1px solid #dcdfe6;
//    border-radius:5px;
//    width: 65%;
//    margin-top: 35px;
//}
.ORGTree{
    position:absolute;
    z-index: 999;
    min-height: 50px;
    //overflow: auto;
    width: 65%;
    margin-left: 5.2em;
    //left: 53%;
    //transform: translateX(-50%);
    border: 1px solid #dcdfe6;
    border-radius:5px;
}
</style>

