<template>
    <div>
        <el-upload
            ref="upload"
            class="upload-demo"
            :data="param"
            :show-file-list="true"
            action="https://jsonplaceholder.typicode.com/posts/"
            :auto-upload="true"
            :http-request="batchUpload"
            :before-upload="beforeFileUpload"
            :on-remove="handleRemove"
            :limit="2"
            :accept=acceptFileType
            :file-list="fileList"
        >
            <el-button slot="trigger" icon="el-icon-upload" size="small" type="primary">批量上传</el-button>
            <el-button style="margin-left: 10px;" icon="el-icon-edit" size="small"
                       v-loading.fullscreen.lock="fullscreenLoading"
                       element-loading-text="正在拼命上传中"
                       element-loading-spinner="el-icon-loading"
                       type="primary" @click="submitUpload">导入数据
            </el-button>
            <div slot="tip" class="el-upload__tip info" v-show="this.showName!=''" style="padding-bottom: 6px;">
                {{ showName }}
            </div>
        </el-upload>
    </div>
</template>

<script>
export default {
    name: "upload-excel",
    props: {
        file: {
            default: function () {
                return [];
            }
        },
        fileName: {
            type: String,
            default: ''
        },
        path: {
            type: String,
            default: ''
        },
        allowType: {
            type: Array,
            default: ["zip"]
        },
        allowSize: {
            type: Number,
            default: 10
        },
        uploadType: {
            type: String,
            default: ''
        },
    },
    data() {
        return {
            fileList: [],
            param: {},
            showName: '只支持xlsx和xls结尾的文件,最大上传文件为10MB！',
            fullscreenLoading: false,
            serviceFilePath: '',
            uploadStatus: false,
        };
    },
    computed: {
        // getFileAction() {
        //     return process.env.BASE_API + "/service-bi/bi/uploadFile"
        // },
        acceptFileType() {
            return "." + this.allowType.join(',.')
        },
    },

    methods: {
        batchUpload(content) {
            if (this.uploadStatus) {
                this.$nextTick(async () => {
                    let fd = new FormData();
                    fd.append('file', content.file);
                    let params = {param: fd, url: process.env.BASE_API + '/api/file/fileupload'};
                    let response = await this.$store.dispatch('http/fileUpload2', params);
                    console.log(response, 'sfdsd')
                    if (response.code === 200) {
                        this.serviceFilePath = response.data;
                        this.$message({
                            message: '读取成功',
                            type: 'success'
                        });
                    } else {
                        this.fileList = [];
                        this.showName = `只支持xlsx和xls结尾的文件,最大上传文件为${this.allowSize}MB！`;
                        this.$message({message: '读取失败: ' + response.msg, type: 'error'});
                    }
                });
            }
        },

        submitUpload() {
            if (this.serviceFilePath == '' || this.serviceFilePath == null) {
                this.$message({message: '请选择上传文件！', type: 'warning'});
                return false;
            }
            this.fullscreenLoading = true;
            this.$emit('submitUpload', this.serviceFilePath);
            this.fullscreenLoading = false;
        },

        async beforeFileUpload(file) { // 上传文件之前钩子
            if (this.uploadType == 'versionUpload') {
                this.param.fileName = file.name;
            } else {
                this.param.fileName = this.fileName
            }
            this.param.path = this.path;

            const suffix = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase();
            if (this.allowType.indexOf(suffix) == -1) {
                this.$message.error('请选择正确的文件格式！');
                return false;
            }

            if (file.size / 1024 / 1024 > this.allowSize) {
                this.$message.error(`文件大小不能超过${this.allowSize}MB！`);
                return false;
            }
            this.showName = '';
            this.fileList.push(file);

            if (this.fileList != null && this.fileList.length > 1) {
                this.uploadStatus = false;
                await this.$confirm('已存在文件，是否使用新文件，将覆盖原有文件?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    this.fileList.splice(0, 1);
                    this.uploadStatus = true;
                }).catch((res) => {
                    this.fileList.splice(1, 1);
                    this.$message({message: '已取消', type: 'warning'});
                });
            } else {
                this.uploadStatus = true;
            }
            return true;
        },

        handleRemove(file, fileList) {
            this.fileList = fileList;
            if (fileList.length == 0) {
                this.serviceFilePath = '';
            }
        },

        beforeRemove(file, fileList) {
            if (file.status == "success" && fileList.length > 0) {
                return this.$confirm(`确定移除 ${file.name}？`);
            }
        },

        init() {
            this.fileList = [];
            this.param = {};
            this.showName = `只支持xlsx和xls结尾的文件,最大上传文件为${this.allowSize}MB！`;
            this.fullscreenLoading = false;
            this.serviceFilePath = '';
        }
    }
}
</script>

<style scoped>
.el-upload-list /deep/ .el-upload-list__item .is-ready {
    margin-top: 0px;
}
</style>
