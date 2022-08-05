<template>
    <div class="currentDiv" v-loading="loading.form">
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">编辑轮播广告信息</span>
            </el-row>
        </el-header>

        <el-main>
            <div>
                <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                    <el-row class="panel-group">
                        <el-form-item label="上传图片">
                            <span>请重新上传图片</span>
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
                                :on-change="getFile"
                                :before-upload="beforeAvatarUpload">
                                <img v-if="form.pic" :src="form.pic" alt="" width="100%" height="100%">
                                <i lass="el-icon-plus"></i>
                            </el-upload>
                        </el-form-item>
                    </el-row>

<!--                    <el-row class="panel-group">-->
<!--                        <el-form-item label="跳转路径：" prop="url">-->
<!--                            <el-input class="userInput" v-model="form.url" :maxLength="120" placeholder="请输入联系人名称"-->
<!--                                      size="small"></el-input>-->
<!--                        </el-form-item>-->
<!--                    </el-row>-->

                    <el-row class="panel-group">
                        <el-form-item label="标题：" prop="title">
                            <el-input class="userInput" v-model="form.title" :maxLength="10"
                                      placeholder="请输入"
                                      size="small"></el-input>
                        </el-form-item>
                    </el-row>
                    <el-row class="panel-group">
                        <el-form-item label="优先级：" prop="index">
                            <el-input class="userInput" v-model="form.index" :maxLength="10"
                                      placeholder="请输入"
                                      size="small"></el-input>
                        </el-form-item>
                    </el-row>

                    <el-row class="panel-group">
                        <el-form-item label="是否有效：" prop="issued">
                            <el-switch v-model="form.isused" size="small"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949" :active-value='true'
                                       :inactive-value='false'></el-switch>
                        </el-form-item>
                    </el-row>
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
export default {
    name: "edit-view-pager",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        //优先级验证
        var indexVerify = async (rule, value, callback) => {
            if (value) {
                if (!(/^[1-9]\d*$/).test(value)) {//数字验证
                    callback(new Error("优先级是非0的正整数，请重新输入"));
                } else {
                    if (this.originalIndex != value) {//判断是否和原来的一致
                        const params = {
                            className: 'Slider',
                            method: 'checkSliderByIndex',
                            param: {
                                index: this.form.index
                            }
                        };

                        let res = await this.$store.dispatch('http/post', params);
                        if (res) callback(new Error("优先级已存在，请重新输入"));
                    }
                }
            }

        }

        return {
            form: { //用来存储用户输入的轮播图信息
                id: '',//编号
                pic: '', //图片路径
                oldPic: '',//旧的图片路径
                url: '', //跳转路径
                index: '', //优先级
                isused: '',//是否有效
                title:'',//轮播图标题
            },
            originalIndex: '',//存着原来的优先级
            rules: {
                pic: [
                    {required: true, message: '请选择图片', trigger: 'blur'}
                ],
                url: [
                    {required: true, message: '请输入跳转路径', trigger: 'blur'}
                ],
               /* title: [
                    {required: true, message: '请输入轮播图标题', trigger: 'blur'}
                ],*/
                index: [
                    {required: true, validator: indexVerify, trigger: 'blur'}
                ]
            },
            loading: {
                form: false,
                btn: false
            },
            files: [],
            dynamicUploader: ''
        }
    },

    methods: {
        getInfoById(id) {
            this.clearPageInfo();
            this.getPageMessage(id);
        },

        getPageMessage(id) {
            this.$nextTick(async () => {
                this.loading.form = true;
                const params = {
                    className: 'Slider',
                    method: 'getSliderById',
                    param: {
                        id: id
                    }
                };
                this.form = await this.$store.dispatch('http/post', params);
                this.originalIndex = this.form.index;
                this.getPic(this.form);
                this.loading.form = false;
            });
        },

        getFile(file, fileList) {
            this.getBase64(file.raw).then(res => {
                const params = res
                this.form.pic=params
        })
        },
        getBase64(file) {
            return new Promise(function (resolve, reject) {
                const reader = new FileReader()
                let imgResult = ''
                reader.readAsDataURL(file)
                reader.onload = function () {
                    imgResult = reader.result
                }
                reader.onerror = function (error) {
                    reject(error)
                }
                reader.onloadend = function () {
                    resolve(imgResult)
                }
            })
        },
        //将路径转成base64位的图片
        getPic(item) {
            this.$nextTick(async () => {
                let params = {
                    param: item.pic,
                    url: process.env.BASE_API + '/api/file/downloadfile'
                };
                let res = await this.$store.dispatch('http/fileDownload', params);
                let path = "";
                if (res != "" && res != null) {
                    path = "data:image/png;base64," + res;
                } else {
                    path = "";
                }
                if (path != "") {
                    item.oldPic = item.pic;
                    item.pic = path;
                }
            });
        },

        OnRemove(file) {
            this.form.pic = "";
            this.dynamicUploader = '';
        },

        myUpload(content) {
            this.$nextTick(async () => {
                this.loading.form = true;
                this.dynamicUploader = 'exist-uploader';
                let fd = new FormData();
                fd.append('file', content.file);
                let params = {param: fd, url: process.env.BASE_API + '/api/file/imgupload'};
                let response = await this.$store.dispatch('http/fileUpload2', params);
                this.loading.form = false;
                if (response.code === 200) {
                   this.form.pic = response.data;
                    this.form.oldPic = ""; //当新上传了图片后置空原来的图片路径
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                    this.fileList.push({name: this.reportParam.title, url: response});
                } else {
                    this.files = [];
                    this.dynamicUploader = '';
                    this.$message({
                        message: response.msg,
                        type: 'error'
                    });
                }
            });
        },

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

        //清除页面信息
        clearPageInfo() {
            this.$refs.form.resetFields();//清除form表格的内容和验证
            this.$refs.upload.clearFiles(); //清楚el-upload上传的缓存
            this.dynamicUploader = '';
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },


        //保存按钮
        saveBtn: function () {
            this.$refs.form.validate(valid => {
                if (valid) {
                    this.loading.btn = true;
                    this.$nextTick(async () => {
                        let params = {
                            className: 'Slider',
                            method: 'updateSlider',
                            param: { //用来存储用户输入的轮播图信息
                                id: this.form.id,//编号
                                pic: this.form.oldPic != null && this.form.oldPic != '' ? this.form.oldPic : this.form.pic, //图片路径
                                url: this.form.url, //跳转路径
                                index: this.form.index, //优先级
                                isused: this.form.isused,//是否有效
                                title: this.form.title,//标题
                            }
                        };
                        let res = await this.$store.dispatch('http/post', params);
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
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}

.userInput {
    width: 65%;
}

.exist-uploader /deep/ .el-upload--picture-card {
    display: none;
}
</style>
