<template>
    <div>
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">查看消息</span>
            </el-row>
        </el-header>
        <el-main class="content">
            <div>
                <el-form ref="form" label-width="100px">
                    <el-row>
                        <el-form-item label="消息标题：">{{ form.title }}</el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="消息内容：">
                            <template>
                                <span v-html="form.content"></span>
                            </template>
                        </el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="已读次数：">{{ form.readNum }} / {{ form.totalNum }}</el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="发布类型：">{{ form.taskType === 'all'? '全局消息':'用户消息' }}</el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="需要反馈：">{{ form.isFeedBack }}</el-form-item>
                    </el-row>
                </el-form>

                <el-form label-width="80px" :inline="true">
                    <el-form-item label="阅读状态:">
                        <el-select class="userInput" v-model="searchData.status" size="small">
                            <el-option v-for="item in statusList" :key="item.value" :label="item.label"
                                       :value="item.value"/>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="用户编号:">
                        <el-input class="userInput" v-model="searchData.userId" placeholder="请输入用户编号" size="small"
                                  clearable/>
                    </el-form-item>

                    <el-form-item label="openId:">
                        <el-input class="userInput" v-model="searchData.openId" placeholder="请输入openId" size="small"
                                  clearable/>
                    </el-form-item>

                    <el-form-item label-width="10px">
                        <el-button-group>
                            <el-button type="primary" size="medium" icon="el-icon-search"
                                       @click="handleSearch">检索
                            </el-button>
                            <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
                        </el-button-group>
                    </el-form-item>

                </el-form>
            </div>
            <div class="table">
                <el-table
                    :data="firstTableData"
                    stripe>
                    <el-table-column label="序号" type="index" align="center" width="50"></el-table-column>
                    <el-table-column prop="userInfo.id" label="用户编号" align="center"></el-table-column>
                    <el-table-column prop="userInfo.openId" label="openId" align="center"></el-table-column>
                    <el-table-column prop="userInfo.nickName" label="昵称" align="center"></el-table-column>
                    <el-table-column prop="status" label="阅读状态" align="center" width="100"></el-table-column>
                    <el-table-column prop="createTime" label="阅读时间" align="center"></el-table-column>
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
</template>

<script>

export default {
    name: "inform-detail",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        },
    },

    data() {
        return {
            firstTableData: [],
            paginationData: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            searchData: {
                notesId: "",
                userId: "",
                openId: "",
                status: "",
            },
            statusList: [
                {value: "0", label: "未读"},
                {value: "1", label: "已读"},
                {value: "", label: "全部"},
            ],
            form: {
                id: '',
                title: '',
                readNum: 0,
                content: '',
                taskType: '',
                isFeedBack: 0,
                notesTaskTypeName: '',
            },
        }
    },

    methods: {
        clearForm() {
            this.form = {
                id: this.form.id,
                title: '',
                content: '',
                taskType: '',
                isFeedBack: 0,
                readNum: 0,
                totalNum: 0,
            }
        },

        load(id) {
            this.form.id = id;
            this.searchData.notesId = id;
            this.getNotesFormList();
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        handleSearch() {
            this.paginationData.currentPage = 1;
            this.getFirstTableData();
        },

        getNotesFormList() {
            this.clearForm();
            this.$nextTick(async () => {
                let params = {
                    className: 'Notes',
                    method: 'getNotesById',
                    param: {
                        id: this.form.id,
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.form = res;
                console.log('form=',this.form);
                this.form.isFeedBack = this.form.isFeedBack === 0 ? "不需要反馈" : this.form.isFeedBack === 1 ? "需要反馈" : "未定义";
            });
        },

        getFirstTableData() {
            this.firstTableData = []; //清空table绑定数据
            this.$nextTick(async () => { //异步提交
                let params = {
                    className: "Notes",
                    method: "selectNotesDetailByNotesId",
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: this.searchData,
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.firstTableData = res.dataList;
                this.firstTableData.forEach((e, i) => {
                    e.status = e.status == 1 ? "已读" : e.status == 0 ? "未读" : "未定义";
                });
                this.paginationData.total = res.page.totalNum;
            });
        },

        // 返回操作
        backClick() {
            // 返回首页
            if (this.onBack) {
                this.onBack(null)
            }
        },

        //分页功能
        //每页总条数发送改变
        handleSizeChange(val) {
            this.paginationData.pageSize = val;
            this.getFirstTableData();
        },

        //页数发生改变
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.getFirstTableData();
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
}

.title {
    float: left;
}


.content {
    height: 550px;
    margin-bottom: 20px;
}

.userInput {
    width: 150px;
}

.table {
    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1;
    }
}
</style>
