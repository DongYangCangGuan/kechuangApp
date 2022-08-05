<template>
<!--  添加跳转新页面操作  -->
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">新增机构信息</span>
            </el-row>
        </el-header>

        <el-main>
            <div>
                <el-form ref="form" :model="form" label-width="150px" :rules="rules">

                    <el-row class="panel-group" :gutter="24">
                        <el-col>
                            <el-form-item label="机构类型" prop="memberType" >

                                <el-select v-model="form.memberType" placeholder="请选择" class="comboSelect" size="small">
                                    <el-option
                                        v-for="(item,index) in memberTypeList"
                                        :key="index"
                                        :label="item.name"
                                        :value="item.code"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>

<!-- GP-->
                    <div v-if="form.memberType === '1'">
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="企业账号" prop="enterpriseCode">
                                    <el-input v-model="form.enterpriseCode" :maxLength="10" placeholder="请输入企业账号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="机构简称" prop="abbreviation">
                                    <el-input v-model="form.abbreviation" :maxLength="120" placeholder="请输入机构简称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="会员名称" prop="enterpriseName">
                                    <el-input v-model="form.enterpriseName" :maxLength="120" placeholder="请输入会员的名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="主要合伙人" prop="contact">
                                    <el-input v-model="form.contact" :maxLength="120" placeholder="请输入主要合伙人名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="联系方式" prop="phone">
                                    <el-input v-model="form.phone" :maxLength="11" placeholder="请输入手机号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="邮箱" prop="email">
                                    <el-input v-model="form.email" :maxLength="120" placeholder="请输入会员的邮箱"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="科创投资基金" prop="investmentFund">
                                    <el-input v-model="form.investmentFund" :maxLength="120" placeholder="请输入科创投资基金"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="总部所在地" prop="address">
                                    <el-input v-model="form.address" :maxLength="120" placeholder="请输入总部所在地"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="上海是否有办公室" prop="sHOffice">
                                    <!--                                <el-input v-model="form.SHOffice" :maxLength="120"-->
                                    <!--                                          size="small"></el-input>-->
                                    <el-select v-model="form.sHOffice" placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in SHOfficeList"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="关注行业" prop="attentionIndustryId">
                                    <el-select v-model="form.attentionIndustryId" multiple placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in attentionIndustry"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="关注阶段" prop="attentionStageId">
                                    <el-select v-model="form.attentionStageId" multiple placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in attentionStage"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="投后项目经理" prop="investmentManager" >
                                    <el-input v-model="form.investmentManager" :maxLength="120" placeholder="请输入投后项目经理"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>

                    </div>

<!--创业公司-->
                    <div v-if="form.memberType === '5'">
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="企业账号" prop="enterpriseCode">
                                    <el-input v-model="form.enterpriseCode" :maxLength="10" placeholder="请输入企业账号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="会员名称" prop="enterpriseName">
                                    <el-input v-model="form.enterpriseName" :maxLength="120" placeholder="请输入会员的名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="联系人" prop="contact">
                                    <el-input v-model="form.contact" :maxLength="120" placeholder="请输入联系人名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="联系方式" prop="phone">
                                    <el-input v-model="form.phone" :maxLength="11" placeholder="请输入手机号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="邮箱" prop="email">
                                    <el-input v-model="form.email" :maxLength="120" placeholder="请输入会员的邮箱"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="项目法定名称" prop="enterpriseName" v-if="form.memberType ==='002'">
                                    <el-input v-model="form.enterpriseName" :maxLength="120" placeholder="请输入项目法定名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="项目简称" prop="abbreviation">
                                    <el-input v-model="form.abbreviation" :maxLength="120" placeholder="请输入项目简称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="投资基金" prop="investmentFund">
<!--                                    <el-input v-model="form.investmentFund" :maxLength="120" placeholder="请输入投资基金"-->
<!--                                              size="small"></el-input>-->

                                    <el-select v-model="form.investmentFund" placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in investmentFundList"
                                            :key="index"
                                            :label="item.enterpriseName"
                                            :value="item.id"
                                        ></el-option>
                                    </el-select>

                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="科创行业分类" prop="industryClassificationId">
                                    <el-select v-model="form.industryClassificationId" placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in industryClassification"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="科创直投分类" prop="directInvestmentClassificationId">
                                    <el-select v-model="form.directInvestmentClassificationId" placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in directInvestmentClassification"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="犀牛标签" prop="rhinocerosLabelId">
                                    <el-select v-model="form.rhinocerosLabelId" multiple placeholder="请选择" class="comboSelect"
                                               @change="handleCheckedChange" size="small">
                                        <el-option
                                            v-for="(item,index) in rhinocerosLabel"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="项目注册地" prop="address1">
                                    <el-input v-model="form.address1" :maxLength="120" placeholder="请输入项目注册地"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="项目经营地" prop="address">
                                    <el-input v-model="form.address" :maxLength="120" placeholder="请输入项目经营地"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="首次投资时间" prop="firstInvestmentTime">
                                    <el-input v-model="form.firstInvestmentTime" :maxLength="120" placeholder="请输入首次投资时间"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="首次投资阶段" prop="firstInvestmentStageId">
                                    <el-select v-model="form.firstInvestmentStageId" placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in firstInvestmentStage"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="是否领投" prop="leadInvestment">
                                    <!--                                <el-input v-model="form.leadInvestment" :maxLength="120" placeholder="请输入会员的邮箱"-->
                                    <!--                                          size="small"></el-input>-->
                                    <el-select v-model="form.leadInvestment" placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in leadInvestmentList"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>


                    </div>

<!--供应商-->
                    <div v-if="form.memberType === '4'">
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="企业账号" prop="enterpriseCode">
                                    <el-input v-model="form.enterpriseCode" :maxLength="10" placeholder="请输入企业账号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="机构简称" prop="abbreviation">
                                    <el-input v-model="form.abbreviation" :maxLength="120" placeholder="请输入机构简称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="会员名称" prop="enterpriseName">
                                    <el-input v-model="form.enterpriseName" :maxLength="120" placeholder="请输入会员的名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="联系人" prop="contact">
                                    <el-input v-model="form.contact" :maxLength="120" placeholder="请输入联系人名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="联系方式" prop="phone">
                                    <el-input v-model="form.phone" :maxLength="11" placeholder="请输入手机号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="邮箱" prop="email">
                                    <el-input v-model="form.email" :maxLength="120" placeholder="请输入会员的邮箱"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>


                    </div>

<!--LP-->
                    <div v-if="form.memberType === '3'">
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="企业账号" prop="enterpriseCode">
                                    <el-input v-model="form.enterpriseCode" :maxLength="10" placeholder="请输入企业账号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="会员名称" prop="enterpriseName">
                                    <el-input v-model="form.enterpriseName" :maxLength="120" placeholder="请输入会员的名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="联系人" prop="contact">
                                    <el-input v-model="form.contact" :maxLength="120" placeholder="请输入联系人名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="联系方式" prop="phone">
                                    <el-input v-model="form.phone" :maxLength="11" placeholder="请输入手机号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="邮箱" prop="email">
                                    <el-input v-model="form.email" :maxLength="120" placeholder="请输入会员的邮箱"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="LP全称" prop="enterpriseName">
                                    <el-input v-model="form.enterpriseName" :maxLength="120" placeholder="请输入LP全称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="LP简称" prop="abbreviation">
                                    <el-input v-model="form.abbreviation" :maxLength="10" placeholder="请输入LP简称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="参与科创基金" prop="participationFundId">
                                    <el-select v-model="form.participationFundId" multiple placeholder="请选择" class="comboSelect" size="small">
                                        <el-option
                                            v-for="(item,index) in participationFund"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.code"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>

                    </div>

<!--科创 -->
                    <div v-if="form.memberType !== '1' && form.memberType !== '3' && form.memberType !== '4' && form.memberType !== '5'">
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="企业账号" prop="enterpriseCode">
                                    <el-input v-model="form.enterpriseCode" :maxLength="10" placeholder="请输入企业账号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="会员名称" prop="enterpriseName">
                                    <el-input v-model="form.enterpriseName" :maxLength="120" placeholder="请输入会员的名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="联系人" prop="contact">
                                    <el-input v-model="form.contact" :maxLength="120" placeholder="请输入联系人名称"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="联系方式" prop="phone">
                                    <el-input v-model="form.phone" :maxLength="11" placeholder="请输入手机号"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="24">
                            <el-col>
                                <el-form-item label="邮箱" prop="email">
                                    <el-input v-model="form.email" :maxLength="120" placeholder="请输入会员的邮箱"
                                              size="small"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>

                    </div>



<!--                    <el-row class="panel-group" :gutter="24">-->
<!--                        <el-col>-->
<!--                            <el-form-item label="客户经理" prop="accountManagerName">-->
<!--                                &lt;!&ndash;                                <el-input v-model="form.accountManagerName" :maxLength="120" placeholder=""&ndash;&gt;-->
<!--                                &lt;!&ndash;                                          size="small"></el-input>&ndash;&gt;-->
<!--                                &lt;!&ndash;                                <el-input v-model="form.email" :maxLength="120" placeholder="请输入会员的邮箱"&ndash;&gt;-->
<!--                                &lt;!&ndash;                                          size="small"></el-input>&ndash;&gt;-->
<!--                                <el-select v-model="form.accountManagerName" class="comboSelect" size="small">-->
<!--                                    <el-option-->
<!--                                        v-for="(item,index) in customerManage"-->
<!--                                        :key="index"-->
<!--                                        :label="item.realName"-->
<!--                                        :value="item.id"-->
<!--                                    ></el-option>-->
<!--                                </el-select>-->
<!--                            </el-form-item>-->
<!--                        </el-col>-->
<!--                    </el-row>-->

                </el-form>
            </div>
        </el-main>

        <el-footer>
            <el-button type="info" icon="el-icon-back" plain @click="backBtn">返回</el-button>
            <el-button :loading="loading.btn" type="success" icon="el-icon-check" plain @click="saveBtn">保存
            </el-button>
        </el-footer>
    </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
    name: "add-member",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    components: {
        Treeselect
    },
    data() {
        //企业账号验证
        var enterpriseCodeVerify = async (rule, value, callback) => {
            if (!(/^2\d{9}$/).test(value)) {
                callback(new Error('企业账号由以2开头的10位数字组成'));
            } else {
                //提交后台验证会员代码是否存在
                const params = {
                    className: 'Member',
                    method: 'getMemberByEnterpriseCodeCheck',
                    param: {
                        enterpriseCode: this.form.enterpriseCode
                    }
                };

                const res = await this.$store.dispatch('http/post', params);
                if (res) callback(new Error('企业账号已存在'));
            }
        }

        //会员名称验证
        let enterpriseNameVerify = async (rule, value, callback) => {
            if (!value) {
                callback(new Error('请输入会员的名称'));
            } else {
                // 提交后台验证会员名称是否存在
                const params = {
                    className: 'Member',
                    method: 'getMemberByEnterpriseNameCheck',
                    param: {
                        enterpriseName: this.form.enterpriseName,
                    },
                };
                const res = await this.$store.dispatch('http/post', params);
                if (res) callback(new Error('会员名称已存在！'));
            }
        };


        //密码验证
        let pwdVerify = async (rule, value, callback) => {
            if (!value) {
                callback(new Error('请输入密码'));
            } else if (!(/^.{6,20}$/).test(value) || (/^\d+$/).test(value) || (/^[a-zA-Z]+$/).test(value) || (/^[\W_]+$/).test(value)) {
                callback(new Error('请将密码设置为6-20位，并且由字母，数字和符号两种以上组合'));
            }
        };

        //联系电话的验证
        var phoneVerify = async (rule, value, callback) => {
            if (!(/^1[34578]\d{9}$/.test(value))) {
                callback(new Error('请输入正确的手机号码!'));
            }
        };

        //邮箱的验证
        var emailVerify = async (rule, value, callback) => {
            if (!(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/.test(value))) {
                callback(new Error('请输入正确的邮箱格式!'));
            }
        };

        //套餐类型选择的验证
        var comboDictionaryVerify = async (rule, value, callback) => {
            if (!this.comboDictionaryDisabled) {
                if (this.form.reportKinds == null || this.form.reportKinds == '' || this.form.reportKinds.length == 0) {
                    callback(new Error('请选择套餐类型'));
                } else if (this.form.reportKinds.length > this.comboValue) {
                    callback(new Error('操作取消，该套餐上限选择数为：' + this.comboValue));
                }
            }
        };

        return {
            memberTypeList: [], //会员类型
            investmentFundList: [],
            SHOfficeList: [
                {
                    name: '是',
                    code: '1'
                },
                {
                    name: '否',
                    code: '0'
                }
            ],
            leadInvestmentList: [
                {
                    name: '是',
                    code: '1'
                },
                {
                    name: '否',
                    code: '0'
                }
            ],
            attentionIndustry: [], //关注行业
            attentionStage: [],//关注阶段
            industryClassification: [], //科创行业分类
            directInvestmentClassification: [],//科创直投分类
            rhinocerosLabel: [],//犀牛标签
            firstInvestmentStage: [],//首次投资阶段
            participationFund: [],  //参与科创基金
            value: '',
            comboOptions: [], //用来存储下拉框的套餐类型信息
            comboValue: 0,//可供选择的套餐报告类型的最大数量值
            form: { //用来存储用户输入的会员信息
                enterpriseCode: '', //企业账号
                enterpriseName: '', //会员名称
                memberType: '',
                pwd: '',
                contact: '', //联系人
                phone: '', //联系电话
                address: '', //联系地址
                email: '', //邮箱
                remark: '', //描述
                accountManager: ''
            },
            customerManage: '',
            comboDictionaryDisabled: true,//默认不能选择套餐中的详情
            rules: {
                enterpriseCode: [
                    {required: true, message: '请输入企业账号', trigger: 'blur'}
                ],
                enterpriseName: [
                    {required: true, message: '请输入会员名称', trigger: 'blur'}
                ],
                memberType: [
                    {required: true}
                ],
                contact: [
                    {required: true, message: '请输入联系人名称', trigger: 'blur'}
                ],
                phone: [
                    {required: true, validator: phoneVerify, trigger: 'blur'}
                ],
                email: [
                    {required: true, validator: emailVerify, trigger: 'blur'}
                ],
                // reportKinds: [
                //     {required: true, validator: comboDictionaryVerify, trigger: 'blur'}
                // ],
                // accountManager: [
                //     {required: true, message: '请选择客户经理', trigger: 'blur'}
                // ],
            },
            loading: {
                form: false,
                btn: false
            },
            departmentOptions: null, //用来存储下拉框的客户经理信息
            comboDictionaryOptions: null, //用来存储套餐选择的信息
            normalizer(node) {  // 自定义下拉树节点模板
                return {
                    id: node.id,
                    label: (node.name != undefined && node.name != null && node.name != '') ? node.name : node.realName,
                    children: node.children // 当选中节点展开时没有子节点，设置其子节点为''
                }
            },
        }
    },

    //钩子函数
    created() {
        //this.getPageMessage();//加载页面信息
        this.getMemberTypeList();
        this.getInvestmentFundList();
        this.getAttentionIndustry();
        this.getAttentionStage();
        this.getIndustryClassification();
        this.getDirectInvestmentClassification();
        this.getRhinocerosLabel();
        this.getFirstInvestmentStage();
        this.getParticipationFund();
    },

    methods: {
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

        getInvestmentFundList() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getGPMemberlist'
                };
                let res = await this.$store.dispatch('http/post', params)
                this.investmentFundList = res;
                console.log('this.investmentFundList=',this.investmentFundList);
            });
        },

        getAttentionIndustry() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'attentionIndustry'  //关注行业
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.attentionIndustry = res;
                console.log('this.attentionIndustry==',this.attentionIndustry);
                // 0: {code: '001', name: '信息技术'}
                // 1: {code: '002', name: '先进制造'}
            });
        },

        getAttentionStage() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'attentionStage'  //关注行业
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.attentionStage = res;
                console.log('this.this.attentionStage==',this.attentionStage);
            });
        },

        getIndustryClassification() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'industryClassification'  //关注行业
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.industryClassification = res;
            });
        },

        getDirectInvestmentClassification() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'directInvestmentClassification'  //关注行业
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.directInvestmentClassification = res;
            });
        },

        getRhinocerosLabel() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'rhinocerosLabel'  //关注行业
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.rhinocerosLabel = res;
                console.log('this.rhinocerosLabel===',this.rhinocerosLabel)
            });
        },

        getFirstInvestmentStage() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'firstInvestmentStage'  //关注行业
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.firstInvestmentStage = res;
            });
        },

        getParticipationFund() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'participationFund'  //关注行业
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.participationFund = res;
                console.log('this.participationFund==',this.participationFund);
            });
        },

        handleCheckedChange() {
            console.log('111111',this.form.rhinocerosLabelId);
        },

        //套餐中报告属性的选中事件
        comboSelect() {
            if (this.form.reportKinds.length > this.comboValue - 1) {
                this.$message({message: '操作取消，已达到该套餐的最大选择数！', type: 'warning'});
            }
        },

        //选中会员套餐事件
        comboChange(val) {
            this.comboValue = 0;//将值赋值0
            this.comboOptions.forEach((e, i) => {
                if (e.code == val) this.comboValue = e.value;
            });
            if (this.form.comboId == null || this.form.comboId == '') {
                this.form.reportKinds = null;
                this.comboDictionaryDisabled = true;
            } else {
                this.comboDictionaryDisabled = false;
            }
        },

        //加载页面信息
        getPageMessage() {
            this.$nextTick(async () => {
                // this.loading.form = true;
                // this.loading.form = false;

                //查询会员的套餐类型信息
                let comboParams = {
                    className: 'Member',
                    method: 'selectComboOfCodeAndNameList',
                    params: {}
                }
                this.comboOptions = await this.$store.dispatch('http/post', comboParams);

                //查询报告的相关属性信息
                const dictionaryParams = {
                    className: 'Member',
                    method: 'selectDictionaryByPropertyList',
                    param: {}
                };
                this.comboDictionaryOptions = await this.$store.dispatch('http/post', dictionaryParams);
                this.comboDictionaryOptions.forEach((e) => {
                    e.children.forEach((e) => {
                        if (e.children == null || e.children == '' || e.children.length == 0) e.children = '';
                    });
                });

                //查询最高级机构的代码和名称信息
                const departmentParams = {
                    className: 'Member',
                    method: 'selectDepartmentOfCodeAndNameList',
                    param: {}
                };
                this.departmentOptions = await this.$store.dispatch('http/post', departmentParams);
                this.loading.form = false;
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

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.$refs.form.resetFields();//清除form表格的内容和验证
                this.onBack(null);
            }
        },

        getCustomerManage() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'selectUserByUroleEqZeroList',
                    param: {

                    }
                }
                let res = await this.$store.dispatch('http/post', params);
                console.log('res333',res)
                this.customerManage = res
                // console.log('this.customerManage =',this.customerManage)
                // if (res) {
                //     this.$message({showClose: true, message: '保存成功！', type: 'success'});
                //     this.loading.btn = false;
                //     setTimeout(() => {
                //         if (this.onBack) {
                //             this.$refs.form.resetFields();//清除form表格的内容和验证
                //             this.onBack('succ');
                //         }
                //     }, 500);
                // } else {
                //     this.$message({showClose: true, message: '保存失败！', type: 'error'});
                //     this.loading.btn = false;
                // }
            })
        },

        //保存按钮
        saveBtn: function () {
            console.log('this.form',this.form);
            this.$refs.form.validate(valid => {
                if (valid) {
                    this.loading.btn = true;
                    this.$nextTick(async () => {
                        let params = {
                            className: 'Member',
                            method: 'insertMember',
                            param: this.form
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        console.log('res222',res)
                        if (res) {
                            this.$message({showClose: true, message: '保存成功！', type: 'success'});
                            this.loading.btn = false;
                            setTimeout(() => {
                                if (this.onBack) {
                                    this.$refs.form.resetFields();//清除form表格的内容和验证
                                    this.onBack('succ');
                                }
                            }, 500);
                        } else {
                            this.$message({showClose: true, message: '保存失败！', type: 'error'});
                            this.loading.btn = false;
                        }
                    })
                }
            })

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

.headerBtn {
    text-align: right;
    line-height: 5;
    height: 12px;
    float: left;
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

.comboSelect {
    width: 100%;
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
</style>
