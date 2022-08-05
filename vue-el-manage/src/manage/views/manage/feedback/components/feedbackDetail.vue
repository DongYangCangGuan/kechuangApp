<template>
    <div>
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title; span-style">问题详情</span>
            </el-row>
        </el-header>
        <el-main>
            <div class="div-style">

                <el-form ref="form" :model="form" label-width="100px">
                    <el-row class="panel-group" :gutter="24">
                        <el-col>
                            <el-form-item label="用户编号：" prop="userId">
                                <el-input class="input-style" readonly v-model="form.userId"></el-input>
                            </el-form-item>
                            <el-form-item label="用户名：" prop="userName">
                                <el-input class="input-style" readonly v-model="form.userName"></el-input>
                            </el-form-item>
                            <el-form-item label="创建时间" prop="createTime">
                                <el-input class="input-style" readonly v-model="form.createTime"></el-input>
                            </el-form-item>
                            <el-form-item label="意见" prop="opinion">
                                <el-input class="input-style" readonly v-model="form.opinion"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
        </el-main>

        <el-footer class="el-footer">
            <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
        </el-footer>

    </div>


</template>

<script>

export default {
    name: "feedback-Detail",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        },
    },
    data() {
        return {
            form: {
                userName: '',
                createTime: '',
                opinion: '',
            },
        }
    },

    mounted() {
        // this.getFirstTableData();
    },

    methods: {
        //消息提示
        alertMessage(message, type) {
            this.$message({
                message: message,
                type: type
            });
        },

        //清空form
        clearData() {
            this.form.questionDescription = '';
            this.form.sequen = '';
            this.form.createTime = '';
            this.form.creatorName = '';
        },


        load(id) {
            this.form.id = id;
            this.getFirstTableData();
        },

        getFirstTableData() {
            this.$nextTick(async () => {
                let params = {
                    className: 'feedback',
                    method: 'getFeedbackDetailById',
                    param: {
                        id: this.form.id,
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.form.userId = res.userId;
                this.form.userName = res.userName;
                this.form.createTime = res.createTime;
                this.form.creatorName = res.creatorName;
            });
        },

        // 返回操作
        backClick() {

            // 返回首页
            if (this.onBack) {
                this.clearData();
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

.headerbtn {
    text-align: right;
    line-height: 5;
    height: 12px;
}

.dialog-title span {
    font-size: 14px;
    font-weight: 500;
}

.span-style {
    float: left;
}

.div-style {
    width: 100%;
}

.input-style {
    width: 500px;
}
</style>
