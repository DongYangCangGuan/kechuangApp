<template>
    <div class="currentDiv" :loading="loading.form">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">用户信息</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div>
<!--                <div>-->
<!--                    <span class="labelStyle">用户信息</span>-->
<!--                </div>-->
                <div class="memberInfo">
                    <div>
<!--                        <el-row class="panel-group" :getter="24">-->
<!--                            <el-col>-->
<!--                                头像：<img :src="form.headImg" alt="">-->
<!--                            </el-col>-->
<!--                        </el-row>-->
                        <el-row class="panel-group" :getter="24">
                            <el-col :span="8">
                                昵称：{{ form.nickName }}
                            </el-col>
<!--                            <el-col :span="8">-->
<!--                                用户名：{{ form.realName }}-->
<!--                            </el-col>-->
<!--                            <el-col :span="8">-->
<!--                                性别：-->
<!--                                <span v-if="form.gender == '0'">男</span>-->
<!--                                <span v-if="form.gender == '1'">女</span>-->
<!--                                <span v-if="form.gender == '2'">中性</span>-->
<!--                            </el-col>-->
                            <el-col :span="8">
                                密码：{{form.passWord}}
                            </el-col>
                        </el-row>
<!--                        <el-row class="panel-group">-->
<!--                            <el-col>-->
<!--                                密码：{{form.passWord}}-->
<!--                            </el-col>-->
<!--                        </el-row>-->
<!--                        <el-row class="panel-group" :getter="24">-->
<!--                            <el-col :span="8">-->
<!--                                出生日期：{{form.birthdate}}-->
<!--                            </el-col>-->

<!--                            <el-col :span="8">-->
<!--                                身份证：{{ form.identityCard }}-->
<!--                            </el-col>-->
<!--                        </el-row>-->
                        <el-row class="panel-group" :getter="24">

                            <el-col :span="8">
                                手机号：{{ form.phone }}
                            </el-col>

                            <el-col :span="8">
                                邮箱：{{ form.email }}
                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                公司名称：{{form.memberId}}
                            </el-col>
                            <el-col :span="8">
                                角色身份：
                                <span v-if="form.uRole == '1'">GP</span>
                                <span v-if="form.uRole == '2'">科创</span>
                                <span v-if="form.uRole == '3'">LP</span>
                                <span v-if="form.uRole == '4'">合作伙伴</span>
                                <span v-if="form.uRole == '5'">创业公司</span>

                            </el-col>
                        </el-row>
                        <el-row class="panel-group">
                            <el-col :span="8">
                                openId：{{form.openId}}
                            </el-col>
                            <el-col :span="8">
                                认证标记：
                                <span v-if="form.certificationMark == '0'">未认证</span>
                                <span v-if="form.certificationMark == '1'">审核中</span>
                                <span v-if="form.certificationMark == '2'">审核失败</span>
                                <span v-if="form.certificationMark == '3'">已认证</span>

                            </el-col>
                        </el-row>
                    </div>

                    <div>
                        <el-button type="info"
                                   icon="el-icon-back"
                                   plain
                                   style="margin-top: 20px;"
                                   @click="backBtn">返回
                        </el-button>
                    </div>
                </div>

            </div>
        </el-main>
    </div>
</template>

<script>
export default {
    name: "userInfoDetail",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            form: {},
            loading: {
                form: false,
                btn: false,
            }
        }
    },

    methods: {
        init(row) {
            console.log('row==',row);
            this.form = row;
            this.form.birthdate = this.getLocalTime(this.form.birthdate);
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


        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
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
    //margin-bottom: 50px;
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
