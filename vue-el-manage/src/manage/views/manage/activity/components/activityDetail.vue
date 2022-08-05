<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">活动反馈信息</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div>
                <span class="labelStyle">活动信息</span>
            </div>
            <div class="memberInfo">
                <div>
                    <el-form :model="formData" label-width="100px">
                        <el-form-item label="活动标语" prop="name">
                            <el-input v-model="formData.name" readonly class="userInput"></el-input>
                        </el-form-item>
                        <el-form-item label="活动标题" prop="title">
                            <el-input v-model="formData.title" readonly class="userInput"></el-input>
                        </el-form-item>
                        <el-form-item label="活动内容" prop="description">
                            <el-input v-model="formData.description"
                                      type="textarea"
                                      :rows="10"
                                      class="userInput"
                                      readonly></el-input>
                        </el-form-item>
<!--                        <el-form-item label="发布时间" prop="createTime">-->
<!--                            <el-input v-model="formData.createTime" readonly class="userInput"></el-input>-->
<!--                        </el-form-item>-->
<!--                        <el-form-item label="起始时间" prop="artStartTime">-->
<!--                            <el-input v-model="formData.artStartTime" readonly class="userInput"></el-input>-->
<!--                        </el-form-item>-->
<!--                        <el-form-item label="结束时间" prop="artEndTime">-->
<!--                            <el-input v-model="formData.artEndTime" readonly class="userInput"></el-input>-->
<!--                        </el-form-item>-->
<!--                        <el-form-item label="发布人" prop="realName">-->
<!--                            <el-input v-model="formData.realName" readonly class="userInput"></el-input>-->
<!--                        </el-form-item>-->
                    </el-form>
                </div>

            </div>

            <div>
                <span class="labelStyle">报名机构明细</span>
                <span style="float: right">
                    <el-button size='small'
                           icon="el-icon-download"
                           type="primary"
                           @click.native.prevent="exportexcel2()">
                        导出
                    </el-button>
                </span>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
                    <el-table-column label="公司名称" prop="enterpriseName" align="center" width="180"></el-table-column>
                    <el-table-column label="联系人" prop="name" align="center" min-width="120"></el-table-column>
                    <el-table-column label="联系方式" prop="phone" align="center" width="200" show-overflow-tooltip></el-table-column>
                    <template v-for="(item, index) in activityTemplate">
                        <el-table-column  v-if="item.templateType === '0'" :label="item.template" :prop="item.seq" align="center" min-width="160">
                            <template slot-scope="scope">
                                <span v-if="scope.row[item.seq]=== '1'">是</span>
                                <span v-else>否</span>
                            </template>
                        </el-table-column>
                        <el-table-column  v-if="item.templateType === '2'" :label="item.template" :prop="item.seq" align="center" min-width="160">
                            <template slot-scope="scope">
                                <span>{{scope.row[item.seq]}}</span>
                            </template>
                        </el-table-column>
                    </template>
<!--                    <el-table-column label="是否有效" prop="isused" align="center" min-width="160">-->
<!--                        <template slot-scope="scope">-->
<!--                            <span v-if="scope.row.isused === '1'">是</span>-->
<!--                            <span v-else>否</span>-->
<!--                        </template>-->
<!--                    </el-table-column>-->
<!--                    <el-table-column label="是否有现成产品" prop="readymade" align="center" min-width="160">-->
<!--                        <template slot-scope="scope">-->
<!--                            <span v-if="scope.row.readymade === '1'">是</span>-->
<!--                            <span v-else>否</span>-->
<!--                        </template>-->
<!--                    </el-table-column>-->
<!--                    <el-table-column label="是否可定制" prop="customization" align="center" min-width="160">-->
<!--                        <template slot-scope="scope">-->
<!--                            <span v-if="scope.row.customization === '1'">是</span>-->
<!--                            <span v-else>否</span>-->
<!--                        </template>-->
<!--                    </el-table-column>-->
                    <el-table-column label="报名时间" prop="createTime" align="center" min-width="120"></el-table-column>
                   <el-table-column label="报名备注" prop="remark" align="center" min-width="150"></el-table-column>
<!--                   <el-table-column label="操作" align="center" fixed="right" width="220">&ndash;&gt;-->
<!--                       <template slot-scope="scope">-->
<!--                            <el-button size="mini" type="info" plain @click="downloadFile(scope.row)">下载附件</el-button>-->
<!--                       </template>-->
<!--                   </el-table-column>-->
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

            <el-button type="info"
                       icon="el-icon-back"
                       plain
                       @click="backBtn">返回
            </el-button>

        </el-main>
    </div>
</template>

<script>
import axios from 'axios';
import qs from 'qs';
export default {
    name: "activity-detail",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            signList: [],
            firstTableData: [],
            paginationData: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            loading: {
                form: false,
                btn: false,
            },
            id: '',
            formData: {},
            activityTemplate: []
        }
    },

    methods: {

        init(row) {
            console.log('row===',row);
            // this.paginationData.total = data.length;
            // this.paginationData.currentPage = 1;
            // this.signList = data;
            // this.firstTableData = data.slice((this.paginationData.currentPage-1)*this.paginationData.pageSize,
            //     this.paginationData.currentPage*this.paginationData.pageSize);
            this.id = row.id;
            this.formData = row;
            this.activityTemplate = row.activityTemplate;
            this.getFirstTableData();
            this.paginationData.currentPage = 1;
        },

        //加载页面信息
        getFirstTableData() {
            console.log('this.id===',this.id);
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'getSignList',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: {
                            id: this.id
                        }
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res111',res);
                this.firstTableData = res.dataList;
                this.firstTableData.forEach((e) => {
                    let arr = e.sdList;
                    arr.forEach((m) => {
                        e[m.seq] = m.answer;
                    })

                })
                console.log('this.firstTableData=',this.firstTableData);
                this.paginationData.total = res.page.totalNum;
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
        //导出
        dataList(row,column,data){
            console.log("开始导出1",data)
            let tempArr = [];
            for(let o in data){
                let obj = {
                    enterpriseCode: data[o].enterpriseName === undefined ? '' : data[o].enterpriseName,
                    enterpriseName: data[o].name === undefined ? '' : data[o].name,
                    typeName: data[o].phone === undefined ? '' : data[o].phone,
                    createTime: data[o].createTime === undefined ? '' : data[o].createTime,
                    remark: data[o].remark === undefined ? '' : data[o].remark,
                    // createTime: this.getLocalTime(data[o].createTime) === undefined ? '' : this.getLocalTime(data[o].createTime),
                    // flag: data[o].flag === undefined ? '' : data[o].flag,
                    // contact: data[o].contact === undefined ? '' : data[o].contact,
                    // phone:data[o].phone === undefined ? '' : data[o].phone,
                    // email:data[o].email === undefined ? '' : data[o].email,
                }
                tempArr.push(obj)
            }
            console.log("开始导出")
            //列标题
            let str = '<tr><td>公司名称</td><td>联系人</td><td>联系方式</td><td>报名时间</td><td>报名备注</td></tr>';
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
            link.download = "报名信息.xls";
            link.href = uri + this.getbase64(template);
            link.click();

        },
        getbase64(s){
            return window.btoa(unescape(encodeURIComponent(s)))
        },
        //导出
        async exportexcel2(row, column) {
            this.loading=true;
            let _this=this;
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'getSignList',
                    param: {
                        page: {
                            pageIndex: 1,
                            pageSize: 999999,
                        },
                        searchdata: {
                            id: this.id
                        }
                    }
                };
                let res = await _this.$store.dispatch('http/post', params);
                console.log("开始导出123" + res.dataList)
                this.loading = false;
                this.dataList(row, column, res.dataList);
            })
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },

        //分页功能
        // 每页总条数发生改变
        // handleSizeChange(val) {
        //
        //     this.paginationData.pageSize = val;
        //     this.firstTableData = this.signList.slice((this.paginationData.currentPage-1)*this.paginationData.pageSize,
        //         this.paginationData.currentPage*this.paginationData.pageSize)
        // },
        // 页数发生改变
        // handleCurrentChange(val) {
        //     console.log('val===',val);
        //     this.paginationData.currentPage = val;
        //     this.firstTableData = this.signList.slice((this.paginationData.currentPage-1)*this.paginationData.pageSize,
        //         this.paginationData.currentPage*this.paginationData.pageSize)
        // },

        downloadFile(val){
            console.info(val);
            this.ExportLoad=true
            let thisData = qs.stringify({
              ID : val.id
            });
            axios({
            method: "POST",
            url: "/seal/manage/Activity/ActivityLoad",
            data: thisData,
            responseType: 'blob',
            })
            .then(res => {
                console.log('res111',res);
                const blob = new Blob([res.data]);//处理文档流
                let elink = document.createElement('a');
                elink.style.display = 'none';
                elink.href = window.URL.createObjectURL(blob);
                elink.setAttribute("download",res.headers.filename);
                document.body.appendChild(elink);
                elink.click();
                URL.revokeObjectURL(elink.href); // 释放URL 对象
                document.body.removeChild(elink);
            });
        },
    },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.header {
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
}

.headerbtn {
    text-align: right;
    line-height: 5;
    height: 12px;
    float: left;
}

.table {
    padding-top:10px;
    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1;
    }
}

.memberInfo {
    padding-left: 20px;
    //min-height: 220px;
    margin-bottom: 50px;
}

.panel-group {
    font-size: 15px;
    margin-bottom: 18px;
}

.labelStyle {
    display: inline-block;
    color: #101010;
    font-size: 15px;
    font-weight: 700;
    margin-bottom: 10px;
}

.currentDiv {
    background: #fff;
    margin: 10px;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.content {
    //height: 550px;
    margin-bottom: 20px;
}
.userInput {
    width: 70%;
}
</style>
