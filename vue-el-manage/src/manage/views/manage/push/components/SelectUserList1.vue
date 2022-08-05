<template>
    <div>
        <div v-if="showPage">
            <el-button @click="showDialog" type="primary">选择</el-button>
            <el-button @click="showOpenObjs" type="primary">查看通知用户</el-button>
            <show-user-list ref="showUserList"></show-user-list>
            <div class="selectedDiv">
                <div v-if="selectedUserList.length > 0">
                    <span>已选中人员：</span>
                    <div class="userDiv">
                        <el-tag
                            class="tagItem"
                            v-for="item in selectedUserList"
                            :key="index"
                            closable
                            @close="handleClose(item)"
                            :type="item.type">
                            {{ item.openId }}
                        </el-tag>
                    </div>
                </div>
            </div>
        </div>
        <el-dialog
            title="用户列表"
            :visible.sync="isSearch"
            width="70%"
            top="1vh"
            :modal-append-to-body="false"
            :close-on-click-modal="false"
            :append-to-body="true"
            :show-close="true"
        >
            <div>
                <div class="userDataDiv">
                    <el-form :inline="true">
                        <el-form-item label="用户编号">
                            <el-input
                                clearable
                                placeholder="请输入用户编号"
                                size="small"
                                v-model="searchData.userId"
                                class="userInput"
                            />
                        </el-form-item>
                        <el-form-item label="openId">
                            <el-input
                                clearable
                                placeholder="请输入openId"
                                size="small"
                                v-model="searchData.openId"
                                class="userInput"
                            />
                        </el-form-item>
                        <el-form-item label="用户昵称">
                            <el-input
                                clearable
                                placeholder="请输入用户昵称"
                                size="small"
                                v-model="searchData.nickName"
                                class="userInput"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button :loading="loading" icon="el-icon-search" size="mini" @click="getFirstTableData"
                                       type="primary">搜索
                            </el-button>
                            <el-button icon="el-icon-refresh" size="mini" type="primary" @click="reset">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <el-table
                        :data="firstTableData"
                        :select-on-indeterminate="false"
                        @selection-change="handleSelectionChange"
                        border
                        ref="multipleTable"
                        :row-key="bindRowKeys"
                        height="300"
                    >
                        <el-table-column :reserve-selection="true" type="selection" width="55"></el-table-column>
                        <el-table-column label="用户编号" prop="userId" width="200"></el-table-column>
                        <el-table-column label="openId" prop="openId" width="350"></el-table-column>
                        <el-table-column label="用户昵称" prop="nickName"></el-table-column>
                    </el-table>
                    <el-footer class="foot">
                        <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="paginationData.currentPage"
                            :page-sizes="[10, 20, 30, 40, 50]"
                            :page-size="paginationData.pageSize"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="paginationData.total">
                        </el-pagination>
                    </el-footer>
                </div>
                <!-- 已选名单查看 -->
                <div class="selected-personal selected-list">
                    <div class="selected-personal-title userDiv" v-if="selectedUserList.length>0">
                        <span>已选名单:</span>
                        <div class="selected-personal-item">
                            <span :key="index" class="itemInfo"
                                  v-for="(item,index) in selectedUserList">{{ item.openId }}</span>
                        </div>
                    </div>
                </div>
                <div slot="footer" class="dialog-footer" style="text-align: right;margin-top: 20px;">
                    <el-button @click="cancel">取 消</el-button>
                    <el-button @click="Btnclraen">清空</el-button>
                    <el-button @click="submitSelected()" type="primary">确定</el-button>
                </div>
            </div>
        </el-dialog>
    </div>

</template>

<script>
import ShowUserList from "./ShowUserList";

export default {
    name: "select-user-list",
    components: {
        ShowUserList,
    },
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        },
    },
    data() {
        return {
            searchData: { //查询区域绑定的查询对象
                userId: "",
                openId: "",
                nickName: "",
            },
            paginationData: {
                // 当前页
                currentPage: 1,
                // 当前每页显示条数
                pageSize: 10,
                // 总条目数
                total: 0
            },
            loading: false,
            isSearch: false,
            firstTableData: [],
            multipleSelection: [], //存储着选中的数据
            selectedUserList: [], //选中用户数据
            selectedEnsureUserIds: [], //确定选中的用户编号数据
            originalSelectedUserList: [],//存储者原来的数据
            showUserList: [], //显示的用户信息
            showPage: false,
        }
    },

    methods: {
        changeSelected(val) {
            let _that = this;
            let arr = new Array();
            console.log(this.selectedEnsureUserIds);
            if (this.selectedEnsureUserIds.length > 0 && this.originalSelectedUserList.length > 0) {
                this.originalSelectedUserList.forEach(function (e, i) {
                    if (_that.selectedEnsureUserIds.indexOf(e.userId) != -1) {
                        arr.push(e);
                    }
                });
                this.multipleSelection = arr;
                this.toggleSelection(this.multipleSelection);
            } else {
                this.toggleSelection(null);
            }
        },

        editManage(val) {
            this.selectedUserList = [];
            for (let i = 0; i < val.length; i++) {
                let tag = {
                    userId: val[i]
                }
                this.selectedUserList.push(tag);
            }
            this.submitSelected();
            this.selectedUserList = [];
        },

        toggleSelection(rows) {
            if (rows) {
                // 再次选中之前先把限贷所有的选中全部清空一下
                this.$refs.multipleTable.clearSelection();

                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row, true);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },

        //显示具体的开放对象
        showOpenObjs(operRow, functionPoint) {
            if (this.selectedEnsureUserIds.length == 0) {
                this.$message({
                    message: '选择的用户为空',
                    type: 'warning'
                });
                return;
            }
            this.$refs.showUserList.load(this.selectedEnsureUserIds);
        },

        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
            });
        },

        //移除选中用户
        handleClose(val) {
            this.selectedUserList.splice(this.selectedUserList.indexOf(val), 1);
            this.submitSelected();
        },

        bindRowKeys(row) {
            return row.userId;
        },

        handleSelectionChange(val) {
            for (let j = 0; j < this.selectedUserList.length; j++) {
                if (this.selectedUserList[j].id != '') {
                    //删除元素
                    this.selectedUserList.splice(j, 1);
                    //删除元素后，数组长度变化，需要将指针向前移一位
                    j--;
                }
            }
            for (let i = 0; i < val.length; i++) {
                let tag = {
                    userId: val[i].userId,
                    openId: val[i].openId,
                    nickName: val[i].nickName,
                }
                this.selectedUserList.push(tag);
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
        },

        /**
         * @name: 获取选中数据
         * @param {type}
         * @return:
         */
        submitSelected() {
            let userIds = new Array();
            if (this.selectedUserList.length > 0) {
                this.selectedUserList.forEach(function (e, i) {
                    userIds.push(e.userId);
                });
                this.selectedEnsureUserIds = userIds;
            } else {
                this.selectedEnsureUserIds = [];
            }
            this.isSearch = false;
        },

        //清空选中值
        Btnclraen() {
            if (this.$refs.multipleTable) {
                this.$refs.multipleTable.clearSelection();
            }
            this.selectedUserList = [];
        },

        //取消
        cancel() {
            this.isSearch = false;
            this.Btnclraen();
            this.clearSearchData();
        },

        //重置
        reset() {
            this.loading = false;
            this.clearSearchData();
            this.getFirstTableData();
        },

        //清空查询条件
        clearSearchData() {
            this.searchData = { //查询区域绑定的查询对象
                userId: "",
                openId: "",
                nickName: "",
            }
        },

        //查询页面数据
        getFirstTableData() {
            this.firstTableData = [];
            this.$nextTick(async () => {
                let params = {
                    // className: 'UserInfo',
                    className: 'push',
                    // method: 'getUserByUroleEqOnePageVo',
                    method: 'getwechatUserlist',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: this.searchdata,
                    },
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('getUserByUroleEqOnePageVo2:',res)
                this.firstTableData = res.dataList;
                this.originalSelectedUserList = res.dataList;
                this.paginationData.total = res.page.totalNum;

                this.changeSelected();
            });
        },

        //显示选项中的数据
        showDialog() {
            this.getFirstTableData(); // 页面创建时获取后台数据
            this.isSearch = true;
        },
    },
}
</script>

<style scoped>

.userDataDiv {
    background: #fff;
    padding: 15px;
    border: 1px solid #DCDFE6;
    -webkit-box-shadow: 0 2px 4px 0 rgb(0 0 0 / 12%), 0 0 6px 0 rgb(0 0 0 / 4%);
    box-shadow: 0 2px 4px 0 rgb(0 0 0 / 12%), 0 0 6px 0 rgb(0 0 0 / 4%);
}

.dialog-title span {
    font-size: 14px;
    font-weight: 500;
}

.selected-list {
    /*padding: 20px;*/
    padding: 20px 20px 0px 20px;
}

.selected-personal {
    margin-bottom: 20px;
}

.selected-personal-title {
    font-size: 14px;
    font-weight: 600;
    color: #304156;
    /*margin-bottom: 15px;*/
}

.selected-personal-item {
    position: relative;
    margin: 10px;
    color: #304156;
    height: auto;
    cursor: pointer;
    padding: 10px;
}

.itemInfo {
    border: 1px solid;
    padding: 5px;
    width: auto;
    border-radius: 5px;
    margin: 0 5px 0 0;
    line-height: 35px;
}

.userDiv {
    max-height: 300px;
    overflow: auto;
}

.userInput{
    width: 150px;
}

</style>
