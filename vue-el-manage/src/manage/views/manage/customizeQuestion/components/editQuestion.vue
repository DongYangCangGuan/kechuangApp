<template>
    <div>
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title; span-style">编辑问题</span>
            </el-row>
        </el-header>
        <el-main>
            <div style="width: 100%;">

                <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                    <el-row class="panel-group" :gutter="24">
                        <el-col>
                            <el-form-item label="问题id：" prop="id">
                                <el-input class="input-style" readonly v-model="form.id"></el-input>
                            </el-form-item>
                            <el-form-item label="问题描述：" prop="questionDescription">
                                <el-input class="input-style" v-model="form.questionDescription"></el-input>
                            </el-form-item>
                            <el-form-item label="优先级：" prop="sequen" >
                                <el-input class="input-style" v-model="form.sequen"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
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

export default {
    name: "add-Question",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        },
    },
    data() {

        // 数字验证
        var indexVerify = async (rule, value, callback) => {
            if (value) {
                if (!(/^\d+$/).test(value)) {//数字验证
                    callback(new Error("优先级是一个数字，请输入数字"));
                } else {
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

        return {
            form: {
                id: '',
                questionDescription: '',
                sequen: ""
            },
            rules: {
                questionDescription: [{required: true, message: "请输入问题描述", trigger: 'blur'}],
                sequen: [{required: true, message: "请输入问题优先级", trigger: 'blur'},
                    {required: true, validator: indexVerify, trigger: 'blur'}],
            }
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
            this.form.id = ''
            this.form.questionDescription = ''
            this.form.sequen = ''
        },

        load(id) {
            this.form.id = id;
            this.getFirstTableData();
        },

        getFirstTableData() {
            this.$nextTick(async () => {
                let params = {
                    className: 'customizeQuestion',
                    method: 'getCustomizeQuestionById',
                    param: {
                        id: this.form.id,
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.form.questionDescription = res.questionDescription;
                this.form.sequen = res.sequen
            });
        },

        // 保存操作
        saveClick() {
            // 调用后台
            this.$nextTick(async () => {
                let params = {
                    className: 'customizeQuestion',
                    method: 'updateCustomizeQuestion',
                    param: this.form
                };
                let res = await this.$store.dispatch('http/post', params);
                if (res) {
                    this.$message({showClose: true, message: '保存成功！', type: 'success'});

                    // 返回主页面
                    setTimeout(() => {
                        if (this.onBack) {
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
