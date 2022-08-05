<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">编辑活动信息</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div>
                <el-form :rules="rules" ref="form" :model="formInline" label-width="130px">

                    <el-form-item label="活动名称" prop="name">
                        <el-input v-model="formInline.name"></el-input>
                    </el-form-item>

                    <el-form-item label="活动标题" prop="title">
                        <el-input v-model="formInline.title"></el-input>
                    </el-form-item>

                    <el-form-item label="活动内容" prop="description">
                        <el-input
                            type="textarea"
                            :rows="10"
                            v-model="formInline.description"></el-input>
                    </el-form-item>

                    <el-form-item label="活动开始时间" prop="artStartTime">
                        <!--                            <el-input v-model="formInline.activityTime"></el-input>-->
                        <el-date-picker
                            v-model="formInline.artStartTime"
                            align="right"
                            type="date"
                            placeholder="请选择开始日期"
                            size="small"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            clearable
                            style="width: 100%;"
                        >
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="活动结束时间" prop="artEndTime">
                        <el-date-picker
                            v-model="formInline.artEndTime"
                            align="right"
                            type="date"
                            placeholder="请选择结束日期"
                            size="small"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            clearable
                            style="width: 100%;"
                        >
                        </el-date-picker>
                    </el-form-item>
                </el-form>

                <div class="dialog-footer" slot="footer">
                    <el-button type="info"
                               icon="el-icon-back"
                               plain
                               @click="backBtn">返回
                    </el-button>
                    <!--                    <el-button type="primary" @click="cancelInfo()">取 消</el-button>-->
                    <el-button type="primary" icon="el-icon-upload2" @click="showUpload()">编辑自定义模板</el-button>
                    <el-button type="success" icon="el-icon-download" @click="saveInfo()">保存</el-button>

                </div>
            </div>

        </el-main>

        <!--     上传模板弹框       -->
        <!--        :before-close="handleClose"-->
        <el-dialog
            :close-on-click-modal="false"
            :append-to-body="true"
            :visible.sync="dialog"
            style="text-align:left !important;"

        >

            <el-form label-width="100px" ref="formWork" style="margin: 20px 0;" class="dialog-form">
                <div v-for="(item,index) in formInline.activityTemplate" :key="index">
                    <el-form-item :label="'模板'+parseFloat(index+1)">
                        <el-input v-model="item.template"></el-input>
                        <el-button @click="addItem"
                                   icon="el-icon-circle-plus-outline" size="small" circle></el-button>
                        <el-button @click="removeItem(index)"
                                   :style="formInline.activityTemplate.length == 1 ? 'pointer-events: none;': ''"
                                   icon="el-icon-remove-outline" size="small" circle></el-button>
                    </el-form-item>
                    <el-form-item label="类型">
                        <el-radio-group v-model="item.templateType" size="small">
                            <el-radio  v-for="(ite,ind) in templateTypeList" :key="ind" :label="ite.code">{{ ite.name }}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </div>
            </el-form>

            <div class="dialog-footer" slot="footer">
                <el-button type="primary" @click="cancelUpload()">取 消</el-button>
                <el-button type="success" icon="el-icon-download" @click="saveUpload()">保存</el-button>
            </div>

        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "edit-activity",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            signList: [],
            firstTableData: [],
            paginationData: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            loading: {
                form: false,
                btn: false,
            },
            formInline: {},
            dialog: false,
            // activityTemplate:[{
            //     template: '',
            //     seq: ''
            // }]
            templateTypeList: [{
                code: '0',
                name: '单选'
            },
            //     {
            //     code: '1',
            //     name: '多选'
            // },
                {
                code: '2',
                name: '输入框'
            }],
            rules: {
                name: [{required: true,message:'请输入活动标语',trigger: 'blur'}],
                title: [{required: true,message:'请输入活动标题',trigger: 'blur'}],
                description: [{required: true,message:'请输入活动内容',trigger: 'blur'}],
                artStartTime: [{required: true,message:'请选择活动开始时间',trigger: 'blur'}],
                artEndTime: [{required: true,message:'请选择活动结束时间',trigger: 'blur'}]
            },
        }
    },

    methods: {

        init(row) {
            console.log('row==',row);
            this.formInline = row;
            // this.activityTemplate = row.activityTemplate
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },

        dateVerify() {
            const startTime = this.formInline.artStartTime;
            const endTime = this.formInline.artEndTime;
            if (startTime > endTime) {
                this.$message({
                    message: '开始日期不能大于截止日期',
                    type: 'warning',
                });
                return false;
            } else {
                return true;
            }
        },

        // 发起申请保存数据
        saveInfo() {
            this.$refs.form.validate(valid => {
                if (valid && this.dateVerify()) {
                    console.log('this.formInline=',this.formInline);
                    this.$nextTick(async () => {
                        let params = {
                            className: 'Activity',
                            method: 'updateActivity',
                            param: this.formInline
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        console.log('res222',res);
                        if (res) {
                            this.$message({showClose: true, message: '编辑成功！', type: 'success'});
                            //this.$refs.form.resetFields();//清除form表格的内容和验证
                            setTimeout(() => {
                                if (this.onBack) {
                                    //this.$refs.form.resetFields();//清除form表格的内容和验证
                                    this.onBack('succ');
                                }
                                this.dialog = false;
                            }, 500);
                        } else {
                            this.$message({showClose: true, message: '编辑失败！', type: 'error'});
                            this.loading.btn = false;
                        }
                    });
                }
            })
        },

        showUpload() {
            this.dialog = true;
        },

        addItem() {
            this.formInline.activityTemplate.push({
                template: '',
                seq: '',
                templateType: ''
            });
        },
        removeItem(index) {
            this.formInline.activityTemplate.splice(index,1);
        },

        cancelUpload () {
            this.dialog = false;
        },

        saveUpload() {
            this.dialog = false;
            this.formInline.activityTemplate.forEach((e, i) => {
                e.seq = i+1;
                e.id = e.activityTemId;
            });
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
