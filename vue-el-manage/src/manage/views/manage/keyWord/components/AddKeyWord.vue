<template>
    <div class="currentDiv" v-loading="loading.form">
        <el-header class="header">
            <el-row class="headerBtn">
                <el-span class="title">新增字典信息</el-span>
            </el-row>
        </el-header>

        <el-main>
            <div>
                <el-tabs v-model="activeName" type="card">
                    <el-tab-pane label="添加选项" name="first">
                        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                            <el-row class="panel-group">
                                <el-form-item label="编号" prop="code">
                                    <el-input class="userInput" v-model="form.code" :maxLength="50"
                                              placeholder="请输入编号.."
                                              size="small"></el-input>
                                    <el-col :span="1">
                                    <span v-show="isCheckCode" class="svg-container">
                                        <i class="el-icon-loading"></i>
                                    </span>
                                    </el-col>
                                </el-form-item>
                            </el-row>

                            <el-row class="panel-group">
                                <el-form-item label="名称" prop="name">
                                    <el-input class="userInput" v-model="form.name" :maxLength="50"
                                              placeholder="请输入名称.."
                                              size="small"></el-input>
                                    <el-col :span="1">
                                    <span v-show="isCheckName" class="svg-container">
                                        <i class="el-icon-loading"></i>
                                    </span>
                                    </el-col>
                                </el-form-item>
                            </el-row>

                            <el-row class="panel-group">
                                <el-form-item label="类别" prop="kind">
                                    <el-select class="userInput" v-model="form.kind" placeholder="请选择种类" size="small"
                                               @change="kindChange">
                                        <el-option
                                            v-for="item in datalist"
                                            :key="item.code"
                                            :label="item.name"
                                            :value="item.code"
                                            v-if="item.code ? true:false"
                                        >
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-row>

                            <el-row class="panel-group" v-show="selectCombo">
                                <el-form-item label="价格：" prop="price">
                                    <el-input class="userInput" v-model="form.price" :maxLength="50"
                                              placeholder="请输入价格.."
                                              size="small"></el-input>
                                </el-form-item>
                            </el-row>

                            <el-row class="panel-group" v-show="selectCombo || selectTime">
                                <el-form-item label="关联数值：" prop="value">
                                    <el-input class="userInput" v-model="form.value" :maxLength="50"
                                              placeholder="请输入关联数值.."
                                              size="small"></el-input>
                                </el-form-item>
                            </el-row>

                            <el-row class="panel-group">
                                <el-form-item label="上传图片：" prop="pic">
                                    <el-upload
                                        ref="upload"
                                        class="avatar-uploader"
                                        :class="dynamicUploader"
                                        action="https://jsonplaceholder.typicode.com/posts/"
                                        :on-remove="OnRemove"
                                        list-type="picture-card"
                                        :limit=1
                                        :file-list="files"
                                        :http-request="myUpload"
                                        :before-upload="beforeAvatarUpload">
                                        <i class="el-icon-plus avatar-uploader-icon"></i>
                                    </el-upload>
                                </el-form-item>
                            </el-row>
                        </el-form>
                    </el-tab-pane>

                    <el-tab-pane label="添加种类" name="second">
                        <el-form ref="dictForm" :model="dictForm" :rules="dictRules" label-width="100px">
                            <el-row class="panel-group">
                                <el-form-item label="编号" prop="code">
                                    <el-input class="userInput" v-model="dictForm.code" :maxLength="50"
                                              placeholder="请输入编号.."
                                              size="small"></el-input>
                                    <el-col :span="1">
                                    <span v-show="isCheckCode" class="svg-container">
                                        <i class="el-icon-loading"></i>
                                    </span>
                                    </el-col>
                                </el-form-item>
                            </el-row>

                            <el-row class="panel-group">
                                <el-form-item label="类名称" prop="name">
                                    <el-input class="userInput" v-model="dictForm.name" :maxLength="50"
                                              placeholder="请输入名称.."
                                              size="small"></el-input>
                                    <el-col :span="1">
                                        <span v-show="isCheckName" class="svg-container">
                                            <i class="el-icon-loading"></i>
                                        </span>
                                    </el-col>
                                </el-form-item>
                            </el-row>

                            <el-row class="panel-group">
                                <el-form-item label="属于报告" prop="type">
                                    <el-switch v-model="dictForm.type" active-color="#13ce66" inactive-color="#ff4949"
                                               active-value="reportProperty" inactive-value=""></el-switch>
                                </el-form-item>
                            </el-row>
                        </el-form>
                    </el-tab-pane>
                </el-tabs>
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
export default {
    name: "add-key-word",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        //验证选项编号
        var selectCode = async (rule, value, callback) => {
            //code存在吗
            if (value == null || value === '') {
                callback(new Error('请输入编号'));
            } else {
                this.isCheckCode = true;
                let params = {
                    className: 'Dictionary',
                    method: 'selectCode1',
                    param: {
                        code: this.form.code,
                    }
                };
                console.log(params, 'params-no-dict');
                let response = await this.$store.dispatch('http/post', params);
                console.log(response, 'add-no-dict');
                if (response == 200) callback(new Error('编码存在'));
                else callback();
                this.isCheckCode = false;
            }
        }

        //验证选项名称
        var selectName = async (rule, value, callback) => {
            //name存在吗
            if (value === '') {
                callback(new Error('请输入名称'));
            } else {
                this.isCheckName = true;
                let params = {
                    className: 'Dictionary',
                    method: 'selectName1',
                    param: {
                        name: this.form.name,
                    }
                };
                let response = await this.$store.dispatch('http/post', params);
                if (response == 200) callback(new Error('名称存在'));
                else callback();
                this.isCheckName = false;
            }
        }

        //验证code是否存在
        var selectDictCode = async (rule, value, callback) => {
            //code存在吗
            if (value === '') {
                callback(new Error('请输入编号'));
            } else {
                this.isCheckCode = true;
                const params = {
                    className: 'Dictionary',
                    method: 'selectCode',
                    param: {
                        code: this.dictForm.code,
                    }
                };
                let response = await this.$store.dispatch('http/post', params);
                if (response == 200) callback(new Error('编码存在'));
                else callback();
                this.isCheckCode = false;
            }
        }

        //验证名称是否存在
        var selectDictName = async (rule, value, callback) => {
            //name存在吗
            if (value === '') {
                callback(new Error('请输入名称'));
            } else {
                this.isCheckName = true;
                const params = {
                    className: 'Dictionary',
                    method: 'selectName',
                    param: {
                        "name": this.dictForm.name,
                    }
                };
                let response = await this.$store.dispatch('http/post', params);
                if (response == 200) callback(new Error('名称存在'));
                else callback();

                this.isCheckName = false;
            }
        }

        //价格验证
        var priceVerify = async (rule, value, callback) => {
            if (this.selectCombo) {
                if (value === '') {
                    callback(new Error('请输入价格'));
                } else if (!(/^\d+(\.\d{2})?$/).test(value)) {
                    callback(new Error('不符合规范,价格格式为00.00'));
                }
            }
        }

        //关联数值验证
        var valueVerify = async (rule, value, callback) => {
            if (this.selectCombo || this.selectTime) {
                if (!(/^\d+$/).test(value)) {
                    callback(new Error('关联数值为整形，仅能输入数字'));
                }
            }
        }

        return {
            isCheckCode: false,
            isCheckName: false,
            activeName: 'first',
            datalist: [],
            selectCombo: false,
            selectTime: false,
            form: {
                code: '',
                name: '',
                kind: '',
                pic: '',
                picUrl: '',
                price: '',
                value: ''
            },

            dictForm: {
                code: '',
                name: '',
                type: '',
            },

            rules: {
                code: [
                    {required: true, validator: selectCode, trigger: 'blur'}
                ],
                name: [
                    {required: true, validator: selectName, trigger: 'blur'}
                ],
                kind: [
                    {required: true, message: '请选择类型', trigger: 'blur'}
                ],
                price: [
                    {required: true, validator: priceVerify, trigger: 'blur'}
                ],
                value: [
                    {require: true, validator: valueVerify, trigger: 'blur'}
                ]
            },

            dictRules: {
                code: [
                    {required: true, validator: selectDictCode, trigger: 'blur'}
                ],
                name: [
                    {required: true, validator: selectDictName, trigger: 'blur'}
                ],
            },
            loading: {
                form: false,
                btn: false
            },
            files: [],
            dynamicUploader: ''
        }
    },

    created() {//钩子函数
        this.getSelect();
    },

    methods: {
        //返回
        backBtn() {
            if (this.onBack) {
                this.clearPage();
                this.onBack(null);
            }
        },

        //类别变换事件
        kindChange(val) {
            this.selectCombo = false;
            this.selectTime = false;
            if (val == 'comboMessage')
                this.selectCombo = true;
            else if (val == 'time')
                this.selectTime = true;
        },

        //保存
        saveBtn() {
            this.loading.btn = true;
            if (this.activeName == 'first') {//添加选项
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.$nextTick(async () => {
                            const params = {
                                className: 'Dictionary',
                                method: 'addDictionary',
                                param: this.form
                            };
                            let repose = await this.$store.dispatch('http/post', params);
                            if (repose == 200) {
                                this.$message({showClose: true, message: '增加成功！'});
                                setTimeout(() => {
                                    if (this.onBack) {
                                        this.clearPage();
                                        this.onBack('succ');
                                    }
                                }, 500);
                            } else {
                                this.$message({showClose: true, message: '操作失败！'});
                            }
                        });
                    }
                });
            } else if (this.activeName == 'second') {//添加种类
                this.$refs.dictForm.validate(valid => {
                    if (valid) {//增加大类
                        this.$nextTick(async () => {
                            let params = {
                                className: 'Dictionary',
                                method: 'addclassDictionary',
                                param: this.dictForm
                            };
                            let repose = await this.$store.dispatch('http/post', params);
                            if (repose == 200) {
                                this.$message({showClose: true, message: '增加成功！', type: 'success'});
                                setTimeout(() => {
                                    if (this.onBack) {
                                        this.clearPage();
                                        this.onBack('succ');
                                    }
                                }, 500);
                            } else {
                                this.$message({showClose: true, message: '操作失败！', type: 'error'});
                            }
                        });
                    }
                });
            } else {
                this.$message({message: '似乎出现了一些问题，请重新操作！', type: 'warning'});
            }
            this.loading.btn = false;
        },

        //移除图片
        OnRemove(file) {
            this.form.pic = '';
            this.dynamicUploader = '';
        },

        //清除页面信息
        clearPage() {
            this.activeName = 'first';
            this.$refs.form.resetFields();
            this.$refs.dictForm.resetFields();
            this.$refs.upload.clearFiles();
            this.OnRemove();
            this.kindChange();
        },

        //获取类名称的列表
        getSelect() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Dictionary',
                    method: 'getKindnameInfo',
                    param: {}
                };
                this.datalist = await this.$store.dispatch('http/post', params);
            });
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
                    this.form.pic = response.data;
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
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.currentDiv {
    background: #fff;
    margin: 10px;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}

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

.userInput {
    width: 65%;
}

.exist-uploader /deep/ .el-upload--picture-card {
    display: none;
}
</style>
