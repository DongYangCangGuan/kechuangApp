<template>
    <div class="staffDiv">
        <el-container>

            <el-header>
                <el-row>
                    <span class="header">批量导入</span>
                </el-row>
            </el-header>

            <el-main>
                <el-upload
                    class="upload-demo"
                    ref="upload"
                    action="https://jsonplaceholder.typicode.com/posts/"
                    :file-list="fileLists"
                    :limit=1
                    :auto-upload="true"
                    :http-request="myUpload"
                    :on-remove="handleRemove"
                    :before-upload="beforeAvatarUpload">
                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                    <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器
                    </el-button>
                    <span style="font-size: 13px;color: #606266;padding-left: 1rem;">
            <el-switch v-model="switch1"
                       active-color="#13ce66"
                       inactive-color="#ff4949"
                       inactive-text="只查看错误记录"
                       active-text="全部记录"
                       @change="change">
            </el-switch>
                        <!--{{switch1?"是":"否"}}-->
          </span>
                    <!--<el-button style="margin-left: 10px;" size="small" type="danger" @click="findMsgData">查看错误记录</el-button>-->
                    <div slot="tip" style="margin-top: 15px;margin-bottom: 10px; line-height: 20px"
                         class="el-upload__tip">
                        * 只能上传Excel2007文件，且只能上传一张表 <br/>
                        * 有错误信息无法上传，请重新选取文件
                    </div>
                </el-upload>

                <div class="table">
                    <el-table
                        :data="firstTableData"
                        :row-style="tableRowStyle"
                        stripe>
                        <el-table-column label="工号" prop="user_id" min-width="100" align="center"></el-table-column>
                        <el-table-column label="姓名" prop="user_name" min-width="100" align="center"></el-table-column>
                        <el-table-column label="性别" prop="user_sex" min-width="80" align="center"></el-table-column>
                        <el-table-column label="登陆名" prop="user_loginname" min-width="100"
                                         align="center"></el-table-column>
                        <el-table-column label="所属部门" prop="user_dept" min-width="100" align="center"></el-table-column>
                        <el-table-column label="生日" prop="user_birth" min-width="200" align="center"></el-table-column>
                        <el-table-column label="角色" prop="user_role" min-width="80" align="center"></el-table-column>
                        <el-table-column label="手机号" prop="user_phone" min-width="120" align="center"></el-table-column>
                        <el-table-column label="邮箱" prop="user_email" align="center" min-width="180"></el-table-column>
                        <el-table-column class="tag-errorMsg" label="备注" prop="errorMsg"
                                         align="center" min-width="200"></el-table-column>
                    </el-table>
                </div>
            </el-main>

            <el-footer>
                <!--分页-->
                <div class="block">
                    <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="0"
                        layout="prev, pager, next"
                        :total="PageNum*10">
                    </el-pagination>
                </div>


                <el-button type="info" icon="el-icon-back" plain @click="cancleClick">返回</el-button>
                <el-button type="success" icon="el-icon-check" plain @click="inputStaffInfoClick">导入</el-button>
            </el-footer>


        </el-container>
    </div>
</template>
<script>
import XLSX from 'xlsx'
import FileSaver from 'file-saver'

export default {
    name: "InputStaff",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            switch1: true,
            serviceFilePath: '',
            fileLists: [],
            firstTableData: [],
            dataList: [],
            PageNum: '', //页数
            currentPage: 1,  //当前页
            pageSize1: 10,
            firstTotal: 0,
            totalNum: 0,//总记录数
            currentPage4: '',
            batchid: ''
        }
    },
    methods: {
        //设置表格行的样式
        tableRowStyle({row, rowIndex}) {
            let rIndex = '';
            for (let i = 0; i < this.firstTableData.length; i++) {
                if (this.firstTableData[i].errorMsg != '') {
                    rIndex = i;
                    // console.log(rowIndex)
                    if (rowIndex == rIndex) {
                        return 'color:red';
                    }
                }
            }
        },
        //上传文件之前 先判断文件后缀名和文件大小
        beforeAvatarUpload(file) {
            const fileType = file.name.split('.')[file.name.split('.').length - 1] === 'xlsx';
            const fileSize = file.size / 1024 / 1024 < 10;
            if (!fileType) {
                this.$message({
                    message: '上传的文件只能是 xlsx 格式！',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
            }
            if (!fileSize) {
                this.$message({
                    message: '上传模板大小不能超过10MB！',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
            }
            return fileType && fileSize
        },
        //覆盖默认的上传行为
        myUpload(content) {
            this.$nextTick(async () => {
                let fd = new FormData();
                fd.append('file', content.file);
                //console.log(fd)
                let params = {param: fd, url: process.env.BASE_API + '/api/file/fileupload'};
                let response = await this.$store.dispatch('http/fileUpload', params);
                if (response) {
                    console.log("serviceFilePath:", response);
                    this.serviceFilePath = response;
                    this.$message({
                        message: '读取成功'
                    });
                } else {
                    this.$message.error('读取失败');
                }
            });
        },
        //上传服务器 解析excel
        submitUpload() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Staff',
                    method: 'insertFileImport',
                    param: {
                        filePath: this.serviceFilePath
                    }
                };
                let res = await this.$store.dispatch("http/post", params);
                this.batchid = res.batchid;
                console.log('batchid：', res.batchid);
                if (res.code == '200') {
                    //成功
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                    //数据回显
                    this.showStaffData();
                } else {
                    //失败
                    this.$message.error(res.msg);
                }
            });
        },
        //回显数据
        showStaffData() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Staff',
                    method: 'showStaffData',
                    param: {
                        searchdata: {
                            batchid: this.batchid
                        },
                        page: {
                            pageIndex: this.currentPage,
                            pageSize: this.pageSize1,
                        }
                    },
                };
                let response = await this.$store.dispatch("http/post", params);
                console.log('dataList：', response);
                this.firstTableData = response.dataList;
                this.dataList = response.dataList;
                this.PageNum = response.page.totalPage;
                this.totalNum = response.page.totalNum;
            });
        },
        //分页
        handleSizeChange(val) {
            this.fenye(val);
        },
        handleCurrentChange(val) {
            this.hang(val);
        },
        fenye(val) {
            this.pageSize1 = val;
            this.showStaffData();
            // this.pageSize1=10;
        },
        hang(val) {
            this.currentPage = val;
            this.showStaffData();
            //this.currentPage=1;
        },
        //取消按钮
        cancleClick: function () {
            if (this.onBack) {
                this.onBack(null)
            }
        },
        //导入User表
        insertUser: function () {
            this.$nextTick(async () => {
                let params = {
                    className: 'Staff',
                    method: 'insertUserData',
                    param: {
                        batchid: this.batchid
                    }
                };
                let res = await this.$store.dispatch("http/post", params);
                console.log(res);
                if (res) {
                    let total = this.totalNum;
                    let succMsg = ",共有" + total + "条记录导入成功"
                    setTimeout(() => {
                        this.$message({
                            message: '导入成功' + succMsg,
                            type: 'success'
                        });
                    }, 500);
                    this.firstTableData = []
                } else {
                    this.$message.error('导入失败')
                }
            });
        },
        //导入按钮 p
        inputStaffInfoClick: function () {
            //判断有无错误信息
            this.$nextTick(async () => {
                let params = {
                    className: 'Staff',
                    method: 'isEmptyErrorMsg',
                    param: {
                        batchid: this.batchid
                    }
                };
                let res = await this.$store.dispatch("http/post", params);
                console.log(res);
                if (res.code == '200') {
                    //导入User表
                    this.insertUser();
                } else {
                    this.$message.error(res.msg);
                    this.handleRemove();
                }

            });
        },
        //提示多少条导入成功

        //只显示错误信息
        change(val) {
            console.log(val);
            if (val) {
                this.showStaffData()
            } else {
                this.findMsgData(val)
            }
        },
        //清空table数据
        handleRemove() {
            this.firstTableData = []
        },
        exportExcel() {
            /* generate workbook object from table */
            let wb = XLSX.utils.table_to_book(document.querySelector('#out-table'))
            /* get binary string as output */
            let wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
            try {
                FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), 'sheetjs.xlsx')
            } catch (e) {
                if (typeof console !== 'undefined') console.log(e, wbout)
            }
            return wbout
        },
        //查看错误信息
        findMsgData: function () {
            this.$nextTick(async () => {
                let params = {
                    className: 'Staff',
                    method: 'selectErrorMsg',
                    param: {
                        batchid: this.batchid
                    }
                };
                let res = await this.$store.dispatch("http/post", params);
                console.log(res);
                this.firstTableData = res;
            });
        },
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.table {
    width: 100%;

    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1
    }
}

.header {
    font: 20px Extra large;
    color: #303133;
    line-height: 1.7;
}

.staffDiv {
    background: #fff;
    margin: 32px;
    padding: 32px;
    border-radius: 2px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
</style>
