<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">编辑审核用户信息</span>
            </el-row>
        </el-header>

        <el-main>
            <div>
                <el-form ref="form" :rules="rules" :model="formData" label-width="120px">

                    <el-form-item label="身份类型" prop="memberType">
                        <el-select v-model="formData.memberType" placeholder="请选择身份类型"
                                   @change="changeMemberType(formData.memberType)" class="userInput" size="small">
                            <el-option
                                v-for="(item,index) in memberTypeList"
                                :key="index"
                                :label="item.name"
                                :value="item.code"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="单位名称" prop="enterpriseName">
                        <el-select v-model="formData.enterpriseName" placeholder="请选择单位名称"
                                   @change="changeEnterpriseName(formData.enterpriseName)" class="userInput" size="small">
                            <el-option
                                v-for="(item,index) in enterpriseList"
                                :key="index"
                                :label="item.enterpriseName"
                                :value="item.enterpriseName"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="单位编号" prop="memberId">
                        <el-select v-model="formData.memberId" placeholder="请选择单位编号"
                                   @change="changeMemberId(formData.memberId)" class="userInput" size="small">
                            <el-option
                                v-for="(item,index) in enterpriseList"
                                :key="index"
                                :label="item.id"
                                :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="申请人" prop="realName">
                        <el-input class="userInput" v-model="formData.realName" placeholder="请输入申请人"
                                  size="small" ></el-input>
                    </el-form-item>
                    <el-form-item label="职位" prop="job">
                        <el-input class="userInput" v-model="formData.job"
                                  size="small" ></el-input>
                    </el-form-item>
                    <el-form-item label="联系方式" prop="uphone">
                        <el-input class="userInput" v-model="formData.uphone" placeholder="请输入联系方式"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="uemail">
                        <el-input class="userInput" v-model="formData.uemail" placeholder="请输入邮箱"
                                  size="small"></el-input>
                    </el-form-item>

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
    name: "edit-certification",
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

        return {
            memberTypeList: [], //会员类型
            formData: { //用来存储用户输入的会员信息

            },
            enterpriseList: [],
            rules: {
                memberType: [
                    {required: true, trigger: 'blur'}
                ],
                memberId: [
                    {required: true, trigger: 'blur'}
                ],
                enterpriseName: [
                    {required: true, trigger: 'blur'}
                ],
                uphone: [
                    {required: true, validator: phoneVerify, trigger: 'blur'}
                ],
                uemail: [
                    {required: true, validator: emailVerify, trigger: 'blur'}
                    // {required: true, trigger: 'blur'}
                ],
            },
            loading: {
                form: false,
                btn: false
            },
            normalizer(node) {  // 自定义下拉树节点模板
                return {
                    id: node.id,
                    label: (node.name != undefined && node.name != null && node.name != '') ? node.name : node.realName,
                    children: node.children // 当选中节点展开时没有子节点，设置其子节点为''
                }
            },
        }
    },

    methods: {
        init(val) {
            // console.log('row===',row);
            this.getMemberTypeList();
            this.getPageMessage(val);

        },
        //套餐中报告属性的选中事件
        comboSelect() {
            if (this.form.reportKinds.length > this.comboValue - 1) {
                this.$message({message: '操作取消，已达到该套餐的最大选择数！', type: 'warning'});
            }
        },

        getPageMessage(val) {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getMemberDetailById',
                    param: {
                        id: val

                    }
                };
                let res = await this.$store.dispatch('http/post', params)
                this.formData = res;
                console.log('this.formData===',this.formData);
                this.getEnterpriseName(this.formData.memberType);
            });
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

        //根据会员类型获取会员单位信息
        getEnterpriseName(val) {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getMemberByType',
                    param:{
                        memberType: val //会员类型，对应getMemberTypeList接口里面的code
                    }
                };
                let res = await this.$store.dispatch('http/post', params)
                console.log('res222===',res);
                this.enterpriseList = res;

            });
        },

        changeMemberType(val) {
            this.getEnterpriseName(val);
            // let arr = this.enterpriseList;
            this.formData.enterpriseName = '';
            this.formData.memberId = '';
        },

        changeEnterpriseName(val) {
            console.log('val===',val);
            this.enterpriseList.forEach((e) => {
                if(e.enterpriseName == val) {
                    this.formData.memberId = e.id;
                }
            })
        },

        changeMemberId(val) {
            this.enterpriseList.forEach((e) => {
                if(e.id == val) {
                    this.formData.enterpriseName = e.enterpriseName;
                }
            })
        },



        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },

        //保存按钮
        saveBtn() {
            console.log('this.formData22=',this.formData);
            this.$refs.form.validate(valid => {
                if (valid) {
                    this.$nextTick(async () => {
                        let params = {
                            className:'Member',
                            method: 'updateMemberUser',
                            param: this.formData
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        console.log('res333===',res);
                        if (res) {
                            this.$message({showClose: true, message: '保存成功！', type: 'success'});
                            this.loading.btn = false;
                            setTimeout(() => {
                                if (this.onBack) {
                                    this.onBack('succ');
                                }
                            }, 500);
                        } else {
                            this.$message({showClose: true, message: '保存失败！', type: 'error'});
                            this.loading.btn = false;
                        }
                    })

                } else {
                    return false;
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

.userInput {
    width: 500px;
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
