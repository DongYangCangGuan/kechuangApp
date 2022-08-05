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
                            <el-form-item label="问题描述：" prop="questionDescription">
                                <el-input class="input-style" readonly v-model="form.questionDescription"></el-input>
                            </el-form-item>
                            <el-form-item label="优先级：" prop="sequen">
                                <el-input class="input-style" readonly v-model="form.sequen"></el-input>
                            </el-form-item>
                            <el-form-item label="创建时间" prop="createTime">
                                <el-input class="input-style" readonly v-model="form.createTime"></el-input>
                            </el-form-item>
                            <el-form-item label="创建者" prop="creatorName">
                                <el-input class="input-style" readonly v-model="form.creatorName"></el-input>
                            </el-form-item>
                            <el-form-item label="上次修改时间" prop="modifyTime">
                                <el-input class="input-style" readonly v-model="form.modifyTime"></el-input>
                            </el-form-item>
                            <el-form-item label="上次修改者" prop="modifierName">
                                <el-input class="input-style" readonly v-model="form.modifierName"></el-input>
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
    name: "question-Detail",
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
                id: '',
                questionDescription: '',
                sequen: '',
                createTime: '',
                creatorName: '',
                modifyTime: '',
                modifierName: ''
            },
        }
    },

    mounted() {
        // this.getFirstTableData();
    },

    methods: {

        //清空form
        clearData() {
            this.form.questionDescription = '';
            this.form.sequen = '';
            this.form.createTime = '';
            this.form.creatorName = '';
            this.form.modifyTime = '';
            this.form.modifierName = '';
        },


        load(id) {
            this.form.id = id;
            this.getFirstTableData();
        },

        getFirstTableData() {
            this.$nextTick(async () => {
                let params = {
                    className: 'customizeQuestion',
                    method: 'getCustomizeQuestionDetailById',
                    param: {
                        id: this.form.id,
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.form.questionDescription = res.questionDescription;
                this.form.sequen = res.sequen;
                this.form.createTime = res.createTime;
                this.form.creatorName = res.creatorName;
                if (res.modifyTime == null) {
                    this.form.modifyTime = res.createTime;
                } else {
                    this.form.modifyTime = res.createTime;
                }
                if (res.modifierName == null) {
                    this.form.modifierName = res.creatorName;
                } else {
                    this.form.modifierName = res.modifierName;
                }
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
