<template>
    <div class="currentDiv" :loading="loading.form">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">机构信息</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div>
                <div>
                    <span class="labelStyle">机构单位信息</span>
                </div>
                <div class="memberInfo">

                    <div v-if="code ==='1'">
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                企业账号：{{ form.enterpriseCode }}
                            </el-col>

                            <el-col :span="8">
                                机构名称：{{ form.enterpriseName }}
                            </el-col>

                            <el-col :span="8">
                                会员类型：{{ form.typeName }}
                            </el-col>
                        </el-row>
<!--                        <el-row class="panel-group">-->
<!--                            <el-col>-->
<!--                                密码：{{form.pwd}}-->
<!--                            </el-col>-->
<!--                        </el-row>-->
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                主要合伙人：{{form.contact}}
                            </el-col>

                            <el-col :span="8">
                                联系电话：{{ form.phone }}
                            </el-col>

                            <el-col :span="8">
                                邮箱：{{ form.email }}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                机构简称：{{form.abbreviation}}
                            </el-col>
                            <el-col :span="8">
                                科创投资基金：{{form.investmentFund}}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                总部所在地点：{{form.address}}
                            </el-col>
                            <el-col :span="8">
                                上海是否有办公室：{{form.sHOffice == '1'? '有':'没有'}}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                关注行业：{{form.attentionIndustry}}
                            </el-col>
                            <el-col :span="8">
                                关注阶段：{{form.attentionStage}}
                            </el-col>
                            <el-col :span="8">
                                投后项目经理：{{form.investmentManager}}
                            </el-col>
                        </el-row>
                    </div>

                    <div v-if="code ==='5'">
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                企业账号：{{ form.enterpriseCode }}
                            </el-col>

                            <el-col :span="8">
                                机构名称：{{ form.enterpriseName }}
                            </el-col>

                            <el-col :span="8">
                                机构类型：{{ form.typeName }}
                            </el-col>
                        </el-row>
<!--                        <el-row class="panel-group">-->
<!--                            <el-col>-->
<!--                                密码：{{form.pwd}}-->
<!--                            </el-col>-->
<!--                        </el-row>-->
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                联系人：{{ form.contact }}
                            </el-col>
                            <el-col :span="8">
                                联系电话：{{ form.phone }}
                            </el-col>

                            <el-col :span="8">
                                邮箱：{{ form.email }}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                项目法定名称：{{form.enterpriseName}}
                            </el-col>
                            <el-col :span="8">
                                项目简称：{{form.abbreviation }}
                            </el-col>
                            <el-col :span="8">
                               投资基金：{{ form.investmentFund }}
                              <!--  投资基金：{{ investmentFund }}-->
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                科创行业分类：{{form.industryClassification}}
                            </el-col>
                            <el-col :span="8">
                                科创直投分类：{{form.directInvestmentClassification}}
                            </el-col>
                            <el-col :span="8">
                                犀牛标签：{{form.rhinocerosLabel}}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                项目注册地：{{form.address1}}
                            </el-col>
                            <el-col :span="8">
                                项目经营地：{{form.address}}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                首次投资时间：{{form.firstInvestmentTime}}
                            </el-col>
                            <el-col :span="8">
                                首次投资阶段：{{form.firstInvestmentStage}}
                            </el-col>
                            <el-col :span="8">
                                是否领投：{{form.leadInvestment=1?'是':'否'}}
                            </el-col>
                        </el-row>

                    </div>

                    <div v-if="code ==='4'">
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                企业账号：{{ form.enterpriseCode }}
                            </el-col>

                            <el-col :span="8">
                                机构名称：{{ form.enterpriseName }}
                            </el-col>

                            <el-col :span="8">
                                机构类型：{{ form.typeName }}
                            </el-col>
                        </el-row>
<!--                        <el-row class="panel-group">-->
<!--                            <el-col>-->
<!--                                密码：{{form.pwd}}-->
<!--                            </el-col>-->
<!--                        </el-row>-->
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                联系人：{{ form.contact }}
                            </el-col>
                            <el-col :span="8">
                                联系电话：{{ form.phone }}
                            </el-col>

                            <el-col :span="8">
                                邮箱：{{ form.email }}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                机构简称：{{form.abbreviation}}
                            </el-col>
                        </el-row>
                    </div>

                    <div v-if="code ==='3'">
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                企业账号：{{ form.enterpriseCode }}
                            </el-col>

                            <el-col :span="8">
                                机构名称：{{ form.enterpriseName }}
                            </el-col>

                            <el-col :span="8">
                                机构类型：{{ form.typeName }}
                            </el-col>
                        </el-row>
<!--                        <el-row class="panel-group">-->
<!--                            <el-col>-->
<!--                                密码：{{form.pwd}}-->
<!--                            </el-col>-->
<!--                        </el-row>-->
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                联系人：{{ form.contact }}
                            </el-col>
                            <el-col :span="8">
                                联系电话：{{ form.phone }}
                            </el-col>

                            <el-col :span="8">
                                邮箱：{{ form.email }}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                LP简称：{{form.abbreviation}}
                            </el-col>
                            <el-col :span="8">
                                LP全称：{{form.enterpriseName}}
                            </el-col>
                            <el-col :span="8">
                                参与科创基金：{{form.participationFund}}
                            </el-col>
                        </el-row>
                    </div>

                    <div v-if="code ==='2'">
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                企业账号：{{ form.enterpriseCode }}
                            </el-col>

                            <el-col :span="8">
                                机构名称：{{ form.enterpriseName }}
                            </el-col>

                            <el-col :span="8">
                                机构类型：{{ form.typeName }}
                            </el-col>
                        </el-row>
<!--                        <el-row class="panel-group">-->
<!--                            <el-col>-->
<!--                                密码：{{form.pwd}}-->
<!--                            </el-col>-->
<!--                        </el-row>-->
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                联系人：{{ form.contact }}
                            </el-col>
                            <el-col :span="8">
                                联系电话：{{ form.phone }}
                            </el-col>

                            <el-col :span="8">
                                邮箱：{{ form.email }}
                            </el-col>
                        </el-row>
                    </div>

                </div>

                <div>
                    <span class="labelStyle">机构明细</span>
                </div>
                <div>
                    <el-form label-width="120px" :inline="true">
                        <el-row :getter="24">
                            <el-col :span="8">
                                <el-form-item label="用户名:">
                                    <el-input v-model="searchData.userName"
                                              style='width: 250px'
                                              placeholder="请输入用户名"
                                              clearable/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="16">
                                <el-form-item label-width="10px">
                                    <el-button-group>
                                        <el-button type="primary"
                                                   size="medium"
                                                   icon="el-icon-search"
                                                   @click="getFirstTableData">检索
                                        </el-button>
                                        <el-button type="info"
                                                   icon="el-icon-back"
                                                   plain
                                                   @click="backBtn">返回
                                        </el-button>
                                    </el-button-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
            </div>

            <div class="table">
                <el-table :data="firstTableData" stripe>
<!--                    <el-table-column label="序号" type="index" width="50"-->
<!--                                     align="center"></el-table-column>-->
                    <el-table-column prop="userName" label="用户名" align="center"></el-table-column>
                    <el-table-column prop="realName" label="真实姓名" align="center"></el-table-column>
                    <el-table-column prop="nickName" label="昵称" align="center"></el-table-column>
                </el-table>
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="paginationData.currentPage"
                    :page-sizes="[1,10,20,30,40,50]"
                    :page-size="paginationData.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="paginationData.total">
                </el-pagination>
            </div>
        </el-main>
    </div>
</template>

<script>
export default {
    name: "member-detail",
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
            paginationData: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            searchData: {
                // memberId: '',
                // userName: '',
            },
            code: '',
            form: {
                //用来存储用户输入的会员信息
                // enterpriseCode: '',
                // enterpriseName: '', //会员名称
                // pwd: '',
                // contact: '', //联系人
                // phone: '', //联系电话
                // address: '', //联系地址
                // email: '', //邮箱
                // comboName: '', //套餐名称
                // comboPrice: '',//套餐价格
                // startTime: '',//套餐开始时间
                // endTime: '',//套餐结束时间
                // reportKinds: null, //会员套餐中选择的详情
                // accountManagerName: '',//客户经理的名称
                // remark: '', //描述
                //
                // dictionaryList: null,//报告属性的集合
            },
            loading: {
                form: false,
                btn: false,
            },
            investmentFundList: [],
            investmentFund: ''
        }
    },

    methods: {
        init(id,code) {
            this.searchData = {
                // memberId: '',
                // userName: '',
            };
            this.code = code;
            console.log('this.code=',this.code);
            this.searchData.memberId = id;
            this.getPageMessage();
            this.getFirstTableData();
        },

        getInvestmentFund(val) {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getGPMemberlist'
                };
                let res = await this.$store.dispatch('http/post', params)
                this.investmentFundList = res;
                this.investmentFundList.forEach((e) => {
                    if(e.id === val) {
                        this.investmentFund = e.enterpriseName
                    }
                })
            });

        },

        //获取会员下属用户分页信息
        getFirstTableData() {
            this.$nextTick(async () => {
                //根据编号查询会员的相关信息
                let params = {
                    className: 'Member',
                    method: 'selectUserByMemberIdPageVo',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: this.searchData,
                    },
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res=',res);
                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum;
            });
        },

        //加载页面信息
        getPageMessage() {
            this.$nextTick(async () => {
                //根据编号查询会员的相关信息
                let params = {
                    className: 'Member',
                    method: 'getMemberById',
                    param: {
                        memberId: this.searchData.memberId,
                    },
                };
                this.form = await this.$store.dispatch('http/post', params);
                console.log('this.form=',this.form);
                //this.getInvestmentFund(this.form.investmentFund);
                this.form.memberId = this.searchData.memberId;
            });
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },

        //分页功能
        //每页总条数发送改变
        handleSizeChange(val) {
            this.paginationData.pageSize = val;
            this.getFirstTableData();
        },

        //页数发生改变
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.getFirstTableData();
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
    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1;
    }
}

.memberInfo {
    padding-left: 20px;
    //min-height: 200px;
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
    margin-bottom: 20px;
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
</style>
