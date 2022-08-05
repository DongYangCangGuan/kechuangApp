<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">编辑新闻</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div>
                <el-form :model="form" label-width="130px" ref="form" :rules="rules">
                    <el-form-item label="新闻图片" prop="newsPic">
                        <span>点击更换图片</span>
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
                            <img v-if="form.newsPic" :src="form.newsPic" alt="" width="100%" height="100%">
                            <i lass="el-icon-plus"></i>
                        </el-upload>
                    </el-form-item>

                    <el-form-item label="新闻标题" prop="title">
                        <el-input v-model="form.title"></el-input>
                    </el-form-item>

                    <el-form-item label="新闻内容" prop="describe">
                        <el-input
                            type="textarea"
                            :rows="10"
                            v-model="form.describe"></el-input>
                    </el-form-item>

                    <el-form-item label="外部链接" prop="newsLink">
                        <el-input v-model="form.newsLink"></el-input>
                    </el-form-item>

                </el-form>

                <div class="dialog-footer" slot="footer">
                    <el-button type="info"
                               icon="el-icon-back"
                               plain
                               @click="backBtn">返回
                    </el-button>
                    <el-button type="success" icon="el-icon-download" @click="saveInfo()">保存</el-button>

                </div>
            </div>

        </el-main>

    </div>
</template>

<script>
export default {
    name: "edit-news",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            loading: {
                form: false,
                btn: false,
            },
            form: {
                // newsPic: '',
                // oldPic: '',
                // title: '',
                // describe: '',
                // newsLink: '',
                // createTime: '',
                // newsTime: ''
            },
            files: [],
            // fileList: [],
            dynamicUploader: '',
            rules: {
                newsPic: [
                    {required: true, message: '请上传图片'}
                ],
                title: [
                    {required: true, message: '请输入新闻标题', trigger: 'blur'}
                ],
                describe: [
                    {required: true, message: '请输入新闻内容', trigger: 'blur'}
                ],
                newsLink: [
                    {required: true, message: '请输入外部链接', trigger: 'blur'}
                ],
            }
        }
    },

    methods: {

        init(row) {
            console.log('row==',row);
            this.form = row;
            this.getPic(this.form);
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },

        OnRemove(file) {
            this.form.newsPic = "";
            this.dynamicUploader = '';
        },

        getFile(file, fileList) {
            console.log('进入onchange')
            this.getBase64(file.raw).then(res => {
                const params = res
                this.form.newsPic=params
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
                    param: item.newsPic,
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
                    item.oldPic = item.newsPic;
                    item.newsPic = path;
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
                    this.form.newsPic = response.data;
                    this.form.oldPic = ""; //当新上传了图片后置空原来的图片路径
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                    this.fileList.push({name: 'img', url: response});
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

        // 发起申请保存数据
        saveInfo() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    console.log('this.form=',this.form);
                    this.$nextTick(async () => {
                        let params = {
                            className: 'newsMange',
                            method: 'updateNews',
                            param: this.form
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        console.log('res222',res);
                        if (res) {
                            this.$message({showClose: true, message: '编辑成功！', type: 'success'});
                            //this.$refs.form.resetFields();//清除form表格的内容和验证
                            setTimeout(() => {
                                if (this.onBack) {
                                    //this.$refs.form.resetFields();//清除form表格的内容和验证
                                    this.onBack('succ');
                                }
                            }, 500);
                        } else {
                            this.$message({showClose: true, message: '编辑失败！', type: 'error'});
                            this.loading.btn = false;
                        }
                    });
                }
            })

        }
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
    min-height: 220px;
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

.exist-uploader /deep/ .el-upload--picture-card {
    display: none;
}

.content {
    //height: 550px;
    margin-bottom: 20px;
    width: 70%;
}
.dialog-footer {
    margin-top: 50px;
}
.dialog-form .el-input {
    width: 80%;
}
</style>
