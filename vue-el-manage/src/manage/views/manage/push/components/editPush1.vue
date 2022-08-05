<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">编辑推送</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div>
                <el-form :model="form" label-width="130px" ref="form" :rules="rules">
<!--                    <el-form-item label="模版" prop="title">-->
<!--                        <el-select  v-model="form.templateType" placeholder="请选择">-->
<!--                            <el-option-->
<!--                                v-for="item in options2"-->
<!--                                :key="item.code"-->
<!--                                :label="item.name"-->
<!--                                :value="item.code">-->
<!--                            </el-option>-->
<!--                        </el-select>-->
<!--                    </el-form-item>-->
                    <el-form-item label="发布群体" prop="title">
                        <el-select v-model="form.sendType" placeholder="请选择">
                            <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
<!--                    <el-form-item label="推送标题" prop="title">-->
<!--                        <el-input v-model="form.title"></el-input>-->
<!--                    </el-form-item>-->

                    <el-form-item label="链接地址" prop="title">
                        <el-input v-model="form.url" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="小程序ID" prop="title">
                        <el-input v-model="form.appId" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="标题" prop="keywords1">
                        <el-input v-model="form.keywords1"></el-input>
                    </el-form-item>
                    <el-form-item label="时间" prop="keywords2">
<!--                        <el-input v-model="form.keywords2"></el-input>-->
                        <el-date-picker
                            v-model="form.keywords2"
                            align="right"
                            type="date"
                            placeholder="请选择发布日期"
                            size="small"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            clearable
                            class="userInput">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="内容" prop="keywords3">
                        <el-input type="textarea" v-model="form.keywords3"></el-input>
                    </el-form-item>
<!--                    <el-form-item label="关键字4" prop="keywords4">-->
<!--                        <el-input v-model="form.keywords4"></el-input>-->
<!--                    </el-form-item>-->
<!--                    <el-form-item label="关键字5" prop="keywords5">-->
<!--                        <el-input v-model="form.keywords5"></el-input>-->
<!--                    </el-form-item>-->
<!--                    <el-form-item label="备注" prop="remark">-->
<!--                        <el-input v-model="form.remark"></el-input>-->
<!--                    </el-form-item>-->

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
            options2:[

            ],
            options: [{
                value: '0',
                label: '所有人'
            },{
                value: '2',
                label: '科创'
            }, {
                value: '4',
                label: '合作伙伴'
            }, {
                value: '3',
                label: 'LP'
            }, {
                value: '1',
                label: 'GP'
            }, {
                value: '5',
                label: '创业公司'
            }],
            // fileList: [],
            dynamicUploader: '',
            rules: {
                title: [
                    {required: true, message: 'title', trigger: 'blur'}
                ],
                url: [
                    {required: true, message: 'url', trigger: 'blur'}
                ],
            }
        }
    },

    methods: {

        init(row) {
            this.getFirstTableData();
            console.log('row==',row);
            this.form = row;
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },
        //查询模板
        getFirstTableData(){
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'templateType'  //微信推送模版
                    }
                }
                let res = await this.$store.dispatch('http/post', params);
                console.log('模板res3333',res);
                this.options2=res
            });
        },


        // 发起申请保存数据
        saveInfo() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    console.log('this.form=',this.form);
                    this.$nextTick(async () => {
                        let params = {
                            className: 'push',
                            method: 'updatePush',
                            param: this.form
                        };
                        let res = await this.$store.dispatch('http/post', params);
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
