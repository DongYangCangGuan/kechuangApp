<template>
    <div>
<!--        <div>-->
<!--            <span class="labelStyle">报名机构明细</span>-->
<!--        </div>-->
        <div class="table">
            <el-table
                :data="firstTableData"
                stripe>
                <el-table-column label="公司名称" prop="member.enterpriseName" align="center" width="200"></el-table-column>
                <el-table-column label="微信名" prop="nickName" align="center" width="200" ></el-table-column>
                <el-table-column label="职位" prop="job" align="center" width="200" ></el-table-column>
                <el-table-column label="联系人" prop="realName" align="center" width="200"></el-table-column>
                <el-table-column label="联系电话" prop="phone" align="center" width="200" ></el-table-column>
                <el-table-column label="邮箱" prop="email" align="center" min-width="180" ></el-table-column>
                <el-table-column label="操作" align="center" fixed="right" width="340">
                    <template slot-scope="scope">
                        <el-button size="mini" type="info" plain @click="details(scope.row)">查看</el-button>
                    </template>
                </el-table-column>

<!--                <el-table-column label="报名备注" prop="remark" align="center" min-width="150"></el-table-column>-->

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

        <el-dialog
            :close-on-click-modal="false"
            :append-to-body="true"
            :visible.sync="dialog"
            style="text-align:left !important;"

        >
            <div class="dialog-main">
                <div class="main_title" >{{questionnaireName}}</div>
                <div class="main_item" v-for="(item,index) in formInline" :key="index">
                    <div style="font-size: 16px;font-weight: 700;"> {{item.sequen}} 、 {{item.questionDescription}} ({{item.questionType === '0' ? '单选' : '多选'}}) </div>
                    <div style="box-sizing: border-box;padding-left: 15px;" v-for="(o, ind) in item.questionOptionsList" :key="ind">
                        {{o.answer}}、{{o.content}}
                    </div>
                </div>
            </div>
        </el-dialog>
        <el-footer>
            <el-button type="info" icon="el-icon-back" plain @click="backBtn">返回</el-button>
            <el-button style="margin-left: 93%;position:relative;top:-40px;" icon="el-icon-download" type="primary" :loading="ExportLoad"  @click.native.prevent="exportexcel()">导出</el-button>
        </el-footer>
    </div>
</template>

<script>
export default {
    name: "questionnaireDetailsManage",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            firstTableData: [],
            questionnaireName: '',
            ormInline: [],
            paginationData: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            dialog: false,
            searchData: {
                // memberId: '',
                // userName: '',
            },
            form: {},
            loading: {
                form: false,
                btn: false,
            },
            productId: '',
            type: '',
            imgList: [],
            formInline: [],
            imgUrl: '',
            productLabelList: [],
            labelList: [],
            fileList: [],
            ExportLoad:false,
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
            this.id = row.questionBelong;
            // this.formData = row;
            // this.activityTemplate = row.activityTemplate;
            this.getFirstTableData();
            this.paginationData.currentPage = 1;
        },
        //加载页面信息
        getFirstTableData() {
            console.log('this.id===',this.id);
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'getAnswerMember',
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
                console.log('问卷明细res111',res);
                this.firstTableData = res.dataList;
                // this.firstTableData.forEach((e) => {
                //     let arr = e.sdList;
                //     arr.forEach((m) => {
                //         e[m.seq] = m.answer;
                //     })
                //
                // })
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

        //查看问卷
        //查询
        details(row) {
            console.log('row=',row);
            this.dialog = true;
            // this.questionnaireName = row.questionname;
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'getAnswerDetail',
                    param: {
                            userId:row.userId,
                            questionBelong:this.id
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('问卷明细调查',res);
                this.formInline = res;
                // this.firstTableData.forEach((e) => {
                //     let arr = e.sdList;
                //     arr.forEach((m) => {
                //         e[m.seq] = m.answer;
                //     })
                //
                // })
                // console.log('this.firstTableData=',this.firstTableData);
                // this.paginationData.total = res.page.totalNum;
            });
            // this.formInline = row.question;
        },
        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);

            }
        },
        async exportexcel(row, column) {
            this.loading=true;
            let _this=this;
            console.log('this.id===',this.id);
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'getAnswerMember',
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
                let res = await _this.$store.dispatch('http/post', params);
                console.log("开始导出123" + res.dataList)
                this.loading = false;
                this.dataList(row, column, res.dataList);
            })
        },
        //导出
        dataList(row,column,data){
            console.log("开始导出1",data)
            let tempArr = [];
            for(let o in data){

                if(data[o].member){
                    let obj = {
                        enterpriseName: data[o].member.enterpriseName === undefined ? '' : data[o].member.enterpriseName,
                        nickName: data[o].nickName === undefined ? '' : data[o].nickName,
                        job: data[o].job === undefined ? '' : data[o].job,
                        realName: data[o].realName === undefined ? '' : data[o].realName,
                        phone: data[o].phone === undefined ? '' : data[o].phone,
                        email: data[o].email === undefined ? '' : data[o].email,
                        // createTime: this.getLocalTime(data[o].createTime) === undefined ? '' : this.getLocalTime(data[o].createTime),
                        // modifierName: data[o].modifierName === undefined ? '' : data[o].modifierName,
                    }
                    tempArr.push(obj)
                } else {
                    let obj = {
                        enterpriseName:  '' ,
                        nickName: data[o].nickName === undefined ? '' : data[o].nickName,
                        job: data[o].job === undefined ? '' : data[o].job,
                        realName: data[o].realName === undefined ? '' : data[o].realName,
                        phone: data[o].phone === undefined ? '' : data[o].phone,
                        email: data[o].email === undefined ? '' : data[o].email,
                        // createTime: this.getLocalTime(data[o].createTime) === undefined ? '' : this.getLocalTime(data[o].createTime),
                        // modifierName: data[o].modifierName === undefined ? '' : data[o].modifierName,
                    }
                    tempArr.push(obj)
                }

            }
            console.log("开始导出")
            //列标题
            let str = '<tr><td>公司名称</td><td>微信名</td><td>职位</td><td>联系人</td><td>联系方式</td><td>邮箱</td></tr>';
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
            link.download = "问卷明细.xls";
            link.href = uri + this.getbase64(template);
            link.click();

        },
        getbase64(s){
            return window.btoa(unescape(encodeURIComponent(s)))
        },
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
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
</style>
