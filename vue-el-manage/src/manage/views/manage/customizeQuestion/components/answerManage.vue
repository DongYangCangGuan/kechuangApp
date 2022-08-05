<template>
    <div>
        <div>
            <el-header>
                <div class="top">
                    <div class="left">回答列表</div>
                    <div class="right">
                        用户:
                        <el-input placeholder="请输入用户名"
                                  v-model="searchData.userName"
                                  clearable size='small'
                                  class="input-style"></el-input>&nbsp;
                        问题:
                        <el-input placeholder="请输入问题描述"
                                  v-model="searchData.questionDescription"
                                  clearable size="small"
                                  class="input-style"></el-input>
                        <el-button size='small'
                                   type="primary"
                                   icon="el-icon-search"
                                   @click="handleQuery">查询
                        </el-button>
                        <el-button size="small"
                                   :loading="loading"
                                   type="primary"
                                   icon="el-icon-refresh-right"
                                   @click="refresh">重置页面
                        </el-button>
                        <el-button size="small" type="success" icon="el-icon-success" @click="getRepliedTableData">
                            查看已回复
                        </el-button>
                        <el-button size="small" type="danger" icon="el-icon-error" @click="getUnRepliedTableData">
                            查看未回复
                        </el-button>
                        <el-button size="small" type="primary" icon="el-icon-info" @click="backClick">返回问题列表</el-button>
                    </div>
                </div>
            </el-header>

            <el-main>
                <div class="table">
                    <el-table
                        :data="firstTableData"
                        stripe
                        class="table-style">
                        <el-table-column label="序号" type="index" width="50"></el-table-column>
                        <el-table-column label="问题描述" prop="questionDescription" width="350"
                                         align="center"></el-table-column>
                        <el-table-column label="回答内容" prop="answer" align="center"></el-table-column>
                        <el-table-column label="回答提交人" prop="creatorName" align="center"></el-table-column>
                        <el-table-column label="回答提交时间" prop="createTime" align="center" width="180">
                            <template slot-scope="scope">
                                <span>{{ scope.row.createTime }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" fixed="right" width="280">
                            <template slot-scope="scope">
                                <el-button size="mini"
                                           type="primary"
                                           plain
                                           @click=replyDate(scope.row);
                                           v-if="scope.row.reply == null || scope.row.reply == ''">回复
                                </el-button>
                                <el-button size="mini"
                                           type="primary"
                                           @click=detail(scope.row);
                                           v-else>查看
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="paginationData.currentPage"
                        :page-sizes="[10,20,30,40,50]"
                        :page-size="paginationData.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="paginationData.total">
                    </el-pagination>

                </div>

            </el-main>
        </div>

        <el-dialog :title="this.firstDialog.title" :visible.sync="this.firstDialog.visible" @close="closeDialog"
                   :append-to-body=true>
            <el-form :model="firstDialog.form" ref="firstDialogForm">
                <el-form-item label="回复内容" :label-width="formLabelWidth">
                    <el-input v-model="firstDialog.form.reply" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancelDialog('firstDialogForm')">取消</el-button>
                <el-button
                    type="primary"
                    @click.native.prevent="submit('firstDialogForm')">回复
                </el-button>
            </div>
        </el-dialog>

        <el-dialog title="回复" :visible.sync="detailDialogFormVisible" :append-to-body=true>
            <el-form :model="form">
                <el-form-item label="问题描述：" prop="questionDescription">
                    <el-input class="dialog-style" readonly v-model="form.questionDescription"></el-input>
                </el-form-item>
                <el-form-item label="回答内容：" prop="answer">
                    <el-input class="dialog-style" readonly v-model="form.answer"></el-input>
                </el-form-item>
                <el-form-item label="回答时间：" prop="createTime">
                    <el-input class="dialog-style" readonly v-model="form.createTime"></el-input>
                </el-form-item>
                <el-form-item label="回答人员：" prop="creatorName">
                    <el-input class="dialog-style" readonly v-model="form.creatorName"></el-input>
                </el-form-item>
                <el-form-item label="回复内容：" prop="reply">
                    <el-input class="dialog-style" readonly v-model="form.reply"></el-input>
                </el-form-item>
                <el-form-item label="回复人员：" prop="replyerName">
                    <el-input class="dialog-style" readonly v-model="form.replyerName"></el-input>
                </el-form-item>
                <el-form-item label="回复时间：" prop="replyTime">
                    <el-input class="dialog-style" readonly v-model="form.replyTime"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="detailDialogFormVisible = false">取消</el-button>
                <el-button type="primary" @click="detailDialogFormVisible = false">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "answer-Manage",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        },
    },
    data() {
        return {
            // 搜索对象
            searchData: {
                userName: '',
                questionDescription: '',
            },
            firstTableData: [],
            detailDialogFormVisible: false,
            formLabelWidth: '120px',
            loading: false,
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
            form: {
                id: '',
                questionDescription: '',
                answer: '',
                creatorName: '',
                createTime: '',
                reply: '',
                replyTime: '',
                replyerName: ''
            },
            firstDialog: {
                title: '',
                visible: false,
                type: '',
                form: {}
            },
        };
    },

    // 钩子函数
    created() {
        this.getFirstTableData(); // 页面创建时获取后台数据
    },

    // 方法
    methods: {

        replyDate(row) {
            this.firstDialog.title = '回复';
            this.firstDialog.visible = true
            this.firstDialog.form = {
                id: row.id,
                reply: row.reply,
            }
        },

        submit(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    let data = this.firstDialog.form;
                    this.$nextTick(async () => {
                        let params = {
                            className: 'customizeAnswer',
                            method: 'replyCustomizeAnswer',
                            param: data
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        this.$refs[formName].clearValidate();
                        this.firstDialog.visible = false;
                        if (res == true) {
                            this.$message({showClose: true, message: '回复成功', type: 'success'});
                        } else {
                            this.$message({showClose: true, message: '回复失败', type: 'error'});
                        }
                        this.getFirstTableData();
                    });
                } else {
                    return false;
                }
            });
        },

        refresh() {
            this.searchData.userName = '';
            this.searchData.questionDescription = '';
            this.firstTableData = [];
            this.loading = true;
            let data = {
                page: {
                    pageIndex: this.paginationData.currentPage,
                    pageSize: this.paginationData.pageSize,
                },
                searchdata: {
                    userName: '',
                    questionDescription: ''
                },
            };
            this.$nextTick(async () => {
                let params = {className: 'customizeAnswer', method: 'getCustomizeAnswerList', param: data};
                let res = await this.$store.dispatch('http/post', params);

                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum;
            });
            this.loading = false;
        },

        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
            });
        },

        //关闭弹窗
        closeDialog() {
            this.$refs['firstDialogForm'].clearValidate();
        },

        //取消弹窗
        cancelDialog(formName) {
            this.$refs[formName].clearValidate();
            this.firstDialog.visible = false;
        },

        //带条件查询
        handleQuery() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        // 获取已回复
        getRepliedTableData() {
            this.firstTableData = [];
            this.paginationData.total = 0;
            let data = {
                page: {
                    pageIndex: this.paginationData.currentPage,
                    pageSize: this.paginationData.pageSize,
                },
                searchdata: {
                    userName: this.searchData.userName,
                    questionDescription: this.searchData.questionDescription
                },
            };
            this.$nextTick(async () => {
                let params = {className: 'customizeAnswer', method: 'getRepliedCustomizeAnswerList', param: data};
                let res = await this.$store.dispatch('http/post', params);

                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum;
            });
        },

        // 获取未回复
        getUnRepliedTableData() {
            this.firstTableData = [];
            this.paginationData.total = 0;
            let data = {
                page: {
                    pageIndex: this.paginationData.currentPage,
                    pageSize: this.paginationData.pageSize,
                },
                searchdata: {
                    userName: this.searchData.userName,
                    questionDescription: this.searchData.questionDescription
                },
            };
            this.$nextTick(async () => {
                let params = {className: 'customizeAnswer', method: 'getUnRepliedCustomizeAnswerList', param: data};
                let res = await this.$store.dispatch('http/post', params);

                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum;
            });
        },

        // 获取列表
        getFirstTableData() {
            this.firstTableData = [];
            let data = {
                page: {
                    pageIndex: this.paginationData.currentPage,
                    pageSize: this.paginationData.pageSize,
                },
                searchdata: {
                    userName: this.searchData.userName,
                    questionDescription: this.searchData.questionDescription
                },
            };
            this.$nextTick(async () => {
                let params = {className: 'customizeAnswer', method: 'getCustomizeAnswerList', param: data};
                let res = await this.$store.dispatch('http/post', params);

                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum;
            });
        },

        reply() {
            this.$nextTick(async () => {
                let params = {
                    className: 'customizeAnswer',
                    method: 'replyCustomizeAnswer',
                    param: {
                        id: this.form.id,
                        reply: this.form.reply
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                if (res) {
                    this.$message({showClose: true, message: '回复成功！', type: 'success'});
                    this.getFirstTableData();
                } else {
                    this.$message({showClose: true, message: '回复失败！', type: 'error'});
                }
            });
        },

        detail(val) {
            this.detailDialogFormVisible = true;
            this.$nextTick(async () => {
                let params = {
                    className: 'customizeAnswer',
                    method: 'getCustomizeAnswerDetailById',
                    param: {
                        id: val.id,
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.form.id = res.id;
                this.form.questionDescription = res.questionDescription;
                this.form.answer = res.answer;
                this.form.createTime = res.createTime;
                this.form.creatorName = res.creatorName;
                this.form.reply = res.reply;
                this.form.replyerName = res.replyerName;
                this.form.replyTime = res.replyTime;
            });
        },

        // 返回操作
        backClick() {

            // 返回首页
            if (this.onBack) {
                this.onBack(null);
            }
        },

        // 每页总条数发生改变
        handleSizeChange(val) {
            this.paginationData.pageSize = val;
            this.getFirstTableData();
        },

        // 页数发生改变
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.getFirstTableData();
        }
    }
};
</script>


<style lang="scss" scoped>
$dark_gray: #889aa4;

.right {
    float: right;
    margin-top: -7px;
}

.left {
    float: left;
    border-left: 4px solid #6ba7bd;
    padding-left: 3px;
}

.page {
    float: right
}

.table {
    width: 100%;
    padding: 1px 22px 15px 22px;
}

.top {
    width: 100%;
    height: 20px;
    padding: 0px 22px;
    margin: 15px 0px;
}

.container {
    border: 1px solid #bbc8c1;
    height: 100%;
    width: 100%
}

.selectBox {
    margin: 10px 22px 10px 22px
}


.input-style {
    width: 150px;
}

.table-style {
    width: 100%;
    text-align: center;
    border: 1px solid #bbc8c1
}

.dialog-style {
    width: 500px;
}
</style>

