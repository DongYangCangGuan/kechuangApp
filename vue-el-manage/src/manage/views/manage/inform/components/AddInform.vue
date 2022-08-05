<template>
    <div>
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">添加消息</span>
            </el-row>
        </el-header>
        <el-main>
            <div>
                <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                    <el-row class="panel-group" :gutter="24">
                        <el-col>
                            <el-form-item label="消息标题：" prop="title">
                                <el-input class="userInput" v-model="form.title"
                                          placeholder="请输入消息标题" size="small"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :gutter="24">
                        <el-col>
                            <el-form-item label="消息内容：" prop="content">
                                <el-input type="textarea" class="userInput" v-model="form.content"
                                          placeholder="请输入消息内容" size="small"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :gutter="24">
                        <el-col>
                            <el-form-item label="分组消息：" prop="taskType">
                                <el-radio-group v-model="form.taskType" @change="taskTypeRadioChange" size="small">
                                    <el-radio  v-for="(item,index) in taskTypeOptions" :key="index" :label="item.code"
                                    >{{ item.name }}
                                    </el-radio>
                                </el-radio-group>
                                <select-user-list ref="selectUserList"></select-user-list>
                                <select-type-list ref="selectTypeList"></select-type-list>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :gutter="24">
                        <el-col>
                            <el-form-item label="通知渠道：" >
<!--                                disabled-->
                                <el-checkbox v-model="checked1"  label="小程序通知"   border></el-checkbox>
                                <el-checkbox v-model="checked2" label="邮箱通知"  border></el-checkbox>
                            </el-form-item>
                        </el-col>
                    </el-row>

<!--                    <el-row class="panel-group" :gutter="24">-->
<!--                        <el-col>-->
<!--                            <el-form-item label="需要反馈：" prop="isFeedBack">-->
<!--                                <el-switch v-model="form.isFeedBack" size="small"-->
<!--                                           active-color="#13ce66"-->
<!--                                           inactive-color="#ff4949" :active-value='1' :inactive-value='0'></el-switch>-->
<!--                            </el-form-item>-->
<!--                        </el-col>-->
<!--                    </el-row>-->
                </el-form>
            </div>
        </el-main>

        <el-footer class="el-footer">
            <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
            <el-button type="success" icon="el-icon-check" plain @click="saveClick">保存</el-button>
        </el-footer>

    </div>


</template>

<script>

import selectUserList from "./SelectUserList";
import selectTypeList from "./SelectTypeList";

export default {
    name: "add-inform",
    components: {
        selectUserList,
        selectTypeList,
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
            data: [],
            typemes:false,
            checked1: false,
            checked2: false,
            form: {
                title: '',
                content: '',
                taskType: '',
                userIds: [],
                isFeedBack: 0,
                flag:0
            },
            rules: {
                title: [{required: true, message: "请输入消息标题", trigger: 'blur'}],
                content: [{required: true, message: "请输入消息内容", trigger: 'blur'}],
            },
            taskTypeOptions: [{
                code: 'all',
                name: '全局消息'
            },{
                code: 'user',
                name: '用户消息'
            },{
                code: 'type',
                name: '类型消息'
            }],

        }
    },

    // 钩子函数
    created() {
        // this.getPageMessage();//加载页面信息
    },

    methods: {
        //加载页面信息
        getPageMessage() {
            this.$nextTick(async () => {
                const params = {
                    className: 'Notes',
                    method: 'selectDictionaryByKindEqTaskType',
                    param: {}
                }
                this.taskTypeOptions = await this.$store.dispatch('http/post', params);
                console.log('this.taskTypeOptions=',this.taskTypeOptions)
            });
        },

        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
            });
        },

        //清空form
        clearForm() {
            this.$refs.selectUserList.selectedUserList = [];
            this.$refs.selectUserList.selectedEnsureUserIds = [];
            this.form = {
                title: '',
                content: '',
                taskType: '',
                userIds: [],
                isFeedBack: 0,
            };
        },

        //发布类型按钮变更事件
        taskTypeRadioChange() {

            if (this.form.taskType == 'user') {
                this.$refs.selectUserList.showPage = true;
                this.$refs.selectTypeList.showPage = false;
            }else if(this.form.taskType == 'type'){
                this.$refs.selectTypeList.showPage = true;
                this.$refs.selectUserList.showPage = false;
            }else{
                this.$refs.selectTypeList.showPage = false;
                this.$refs.selectUserList.showPage = false;
            }

        },

        // 保存操作
        saveClick() {
            let obj = new Array();
            this.$refs.form.validate(valid => {
                if (valid) {
                    obj = this.$refs.selectUserList.selectedEnsureUserIds;
                    console.log('this.$refs.selectTypeList',this.$refs.selectTypeList.value)
                     console.log("obj",obj)
                    if (this.form.taskType == 'user' && obj.length == 0) {
                        this.alertMessage("发布消息为用户消息，没有选择用户，请去选择至少一个用户", "warning");
                    } else if (this.form.taskType == 'all' && obj.length > 0) {
                        this.alertMessage("发布消息为全局消息，不能出现选中用户", "warning");
                    }else if (this.form.taskType == 'type') {
                        let code="";
                        // obj = this.$refs.selectUserList.selectedEnsureUserIds;
                        // this.form.userIds = obj;
                        this.form.taskType = this.$refs.selectTypeList.value
                        // this.form.flag=this.$refs.selectUserList.selectedEnsureUserIds
                        this.insert();
                    }  else if (this.form.taskType == null || this.form.taskType == '') {
                        this.alertMessage("请选择消息的发布类型", "warning");
                    } else {
                        this.form.userIds = obj;
                        this.insert();
                    }
                } else {
                    return false;
                }
            });
        },

        insert() {
            // 调用后台
            this.$nextTick(async () => {
                //1微信通知，2邮件通知 3都通知，0系统内部通知
                if(this.checked1==true && this.checked2==false){
                    this.form.flag='1';
                }else if(this.checked1==false && this.checked2==true){
                    this.form.flag='2';
                }else  if(this.checked1==true && this.checked2==true){
                    this.form.flag='3';
                }else{
                    this.form.flag='0';
                }
                let params = {
                    className: 'Notes',
                    method: 'insertNotes',
                    param: this.form,
                };
                let res = await this.$store.dispatch('http/post', params);
                if (res) {
                    this.$message({showClose: true, message: '保存成功！', type: 'success'});

                    // 返回主页面
                    setTimeout(() => {
                        if (this.onBack) {
                            this.clearForm();
                            this.onBack('succ')
                        }
                    }, 1000)
                } else {
                    this.$message({showClose: true, message: '操作失败！'});
                }
            });
        },

        // 返回操作
        backClick() {
            // 返回首页
            if (this.onBack) {
                this.clearForm();
                this.onBack(null);
            }
        },

    },
}
</script>

<style scoped>
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

.userInput {
    width: 500px;
}

.dialog-title span {
    font-size: 14px;
    font-weight: 500;
}
</style>
