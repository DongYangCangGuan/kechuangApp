<template>
    <!--  添加跳转新页面操作  -->
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">新增用户信息</span>
            </el-row>
        </el-header>

        <el-main>
            <div>
                <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
<!--                    <el-form-item label="上传头像" prop="headImg">-->
<!--                        <el-upload-->
<!--                            ref="upload"-->
<!--                            class="avatar-uploader"-->
<!--                            :class="dynamicUploader"-->
<!--                            action="https://jsonplaceholder.typicode.com/posts/"-->
<!--                            :on-remove="OnRemove"-->
<!--                            list-type="picture-card"-->
<!--                            :limit=1-->
<!--                            :file-list="files"-->
<!--                            :http-request="myUpload"-->
<!--                            :before-upload="beforeAvatarUpload">-->
<!--                            <i class="el-icon-plus avatar-uploader-icon"></i>-->
<!--                        </el-upload>-->
<!--                    </el-form-item>-->

                    <el-form-item label="昵称" prop="nickName">
                        <el-input class="userInput" v-model="formData.nickName" placeholder="请输入用户昵称"
                                  size="small"></el-input>
                    </el-form-item>
<!--                    <el-form-item label="用户名" prop="realName">-->
<!--                        <el-input class="userInput" v-model="formData.realName" placeholder="请输入用户名"-->
<!--                                  size="small"></el-input>-->
<!--                    </el-form-item>-->
                    <el-form-item label="openId" prop="openId">
                        <el-input class="userInput" v-model="formData.openId" placeholder="请输入openId"
                                  size="small"></el-input>
                    </el-form-item>
<!--                    <el-form-item label="性别" prop="gender">-->
<!--                        <el-select v-model="formData.gender" placeholder="请选择性别" class="userInput" size="small">-->
<!--                            <el-option-->
<!--                                v-for="(item,index) in genderList"-->
<!--                                :key="index"-->
<!--                                :label="item.name"-->
<!--                                :value="item.code"-->
<!--                            ></el-option>-->
<!--                        </el-select>-->
<!--                    </el-form-item>-->
<!--                    <el-form-item label="出生日期" prop="birthdate">-->
<!--                        <el-date-picker-->
<!--                            v-model="formData.birthdate"-->
<!--                            align="right"-->
<!--                            type="date"-->
<!--                            placeholder="请选择出生日期"-->
<!--                            size="small"-->
<!--                            format="yyyy-MM-dd"-->
<!--                            value-format="yyyy-MM-dd"-->
<!--                            clearable-->
<!--                            class="userInput">-->
<!--                        </el-date-picker>-->
<!--                    </el-form-item>-->
<!--                    <el-form-item label="身份证" prop="identityCard">-->
<!--                        <el-input class="userInput" v-model="formData.identityCard" placeholder="请输入身份证"-->
<!--                                  size="small"></el-input>-->
<!--                    </el-form-item>-->
                    <el-form-item label="手机号" prop="phone">
                        <el-input class="userInput" v-model="formData.phone" placeholder="请输入手机号"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input class="userInput" v-model="formData.email" placeholder="请输入邮箱"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="公司名称" prop="memberid">
                        <el-input class="userInput" v-model="formData.memberid" placeholder="请输入公司名称"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="角色身份" prop="uRole">
                        <el-select v-model="formData.uRole" placeholder="请选择身份类型" class="userInput" size="small">
                            <el-option
                                v-for="(item,index) in memberTypeList"
                                :key="index"
                                :label="item.name"
                                :value="item.code"
                            ></el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="认证标记" prop="certificationMark">
                        <el-select v-model="formData.certificationMark" placeholder="请选择认证标记" class="userInput" size="small">
                            <el-option
                                v-for="(item,index) in certificationMarkList"
                                :key="index"
                                :label="item.name"
                                :value="item.code"
                            ></el-option>
                        </el-select>
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
    name: "addUserInfo",
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
            dynamicUploader: '',
            files: [],
            memberTypeList: [], //会员类型
            genderList: [{
                name: '男',
                code: 0
            },{
                name: '女',
                code: 1
            },{
                name: '中性',
                code: 2
            }],
            certificationMarkList: [{
                name: '未认证',
                code: 0
            },{
                name: '审核中',
                code: 1
            },{
                name: '审核失败',
                code: 2
            },{
                name: '已认证',
                code: 3
            }],
            formData: {},
            rules: {
                nickName: [{required: true, message: '请输入昵称', trigger: 'blur'}],
                openId: [{required: true, message: '请输入openId', trigger: 'blur'}],
                gender: [{required: true, message: '请选择性别'}],
                phone: [
                    {required: true, validator: phoneVerify, trigger: 'blur'}
                ],
                email: [
                    {required: true, validator: emailVerify, trigger: 'blur'}
                ],
                memberid: [{required: true, message: '请输入公司名称', trigger: 'blur'}],
                uRole: [{required: true, message: '请选择身份类型'}],
                certificationMark: [{required: true, message: '请选择认证标记'}]
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

        //移除图片
        OnRemove(file) {
            this.formData.headImg = '';
            this.dynamicUploader = '';
        },
        //上传之前触发
        beforeAvatarUpload(file) {
            const extension1 = file.name.split('.')[file.name.split('.').length - 1] === 'jpg';
            const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'png';
            const extension3 = file.name.split('.')[file.name.split('.').length - 1] === 'gif';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!extension1 && !extension2 && !extension3) {
                this.$message({
                    message: '上传模板只能是 jpg、png、gif格式!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
                return false;
            }
            if (!isLt2M) {
                this.$message({
                    message: '上传模板大小不能超过 2MB!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
                return false;
            }
            return (extension1 || extension2 || extension3) && isLt2M;
        },
        //图片上传覆盖默认的上传行为
        myUpload(content) {
            this.$nextTick(async () => {
                this.loading.form = true;
                this.dynamicUploader = 'exist-uploader';
                let fd = new FormData();
                fd.append('file', content.file);
                let params = {
                    param: fd,
                    url: process.env.BASE_API + '/api/file/imgFileUpload'
                };
                let response = await this.$store.dispatch('http/fileUpload', params);
                if (response.code === 200) {
                    this.formData.headImg = response.data;
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                } else {
                    this.files = [];
                    this.dynamicUploader = '';
                    this.$message({
                        message: response.msg,
                        type: 'error'
                    });
                }
                this.loading.form = false;
            });
        },


        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.$refs.form.resetFields();//清除form表格的内容和验证
                this.onBack(null);
            }
        },


        //保存按钮
        saveBtn: function () {
            this.$refs.form.validate(valid => {
                if (valid) {
                    console.log('this.formData',this.formData);
                    this.loading.btn = true;
                    this.$nextTick(async () => {
                        let params = {
                            className: 'userMange',
                            method: 'insertUser ',
                            param: this.formData
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

.userInput {
    width: 50%;
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
