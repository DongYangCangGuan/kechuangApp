<template>
    <!--  添加跳转新页面操作  -->
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">编辑问卷调查表</span>
            </el-row>
        </el-header>

        <el-main>
            <div class="addquestionnaire">
                <el-form ref="fomTitle" style="width: 60%;margin: 20px auto;" class="addquestionnairename">
                    <el-form-item label="问卷名称">
                        <el-input v-model="questionname" @blur="verifyInput($event)"></el-input>
                    </el-form-item>
                    <el-form-item label="问卷类型">
                        <el-select v-model="firstLevel" placeholder="请选择问卷类型" class="comboSelect1" disabled  @change="changeVal($event)">
                            <el-option
                                v-for="(item,index) in questionnaireTypeList"
                                :key="index"
                                :label="item.name"
                                :value="item.code"
                            ></el-option>
                        </el-select>
                        <el-select v-model="secondLevel" placeholder="请选择问卷类型" class="comboSelect1" disabled  @change="changeVal2($event)">
                            <el-option
                                v-for="(item,index) in typeList"
                                :key="index"
                                :label="item.name"
                                :value="item.code"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <el-form ref="formContent" label-width="100px" v-for="(item,index) in ques" :key="index">
                    <el-form-item :label="'问题'+parseFloat(index+1)">
                        <el-input v-model="item.questionDescription"></el-input>
                        <el-button @click="additem" icon="el-icon-circle-plus-outline" circle></el-button>
                        <el-button @click="removeitem(index)" icon="el-icon-remove-outline" circle></el-button>
                    </el-form-item>
                    <el-form-item label="类型">
                        <el-radio-group v-model="item.questionType">
                            <el-radio label="0">单选</el-radio>
                            <el-radio label="1">多选</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item v-for="(it, ind) in item.myQuestion" :key="ind" :label=" String.fromCharCode(64 + parseInt(ind+1))">
                        <el-input v-model="it.content"></el-input>
                        <el-button size="mini" @click="addoption(index)" icon="el-icon-circle-plus-outline" circle></el-button>
                        <el-button size="mini" @click="removeoption(index,ind)" icon="el-icon-delete" circle></el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-main>

        <el-footer>
            <el-button type="info" icon="el-icon-back" plain @click="backBtn">返回</el-button>
            <el-button :loading="loading.btn" type="success" icon="el-icon-check" plain @click="saveBtn">保存
            </el-button>
        </el-footer>
    </div>
</template>

<script>

export default {
    name: "edit-consultingInfo",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    components: {

    },
    data() {

        return {
            searchData: {},
            nameList: [],
            formData: {},
            questionname: '',
            type: '',
            questionnaireTypeList: [],
            typeList: [],
            firstLevel: '',
            secondLevel: '',
            ques: [
                // {
                //     questionDescription: '',
                //     questionType: '',
                //     releaseFlag: '',
                //     sequen: '',
                //     myQuestion: [
                //         {answer: '', content:''},
                //         {answer: '', content:''}
                //     ]
                // }
            ],

            loading: {
                form: false,
                btn: false
            },

            normalizer(node) {  // 自定义下拉树节点模板
                return {
                    id: node.id,
                    label: (node.name != undefined && node.name != null && node.name != '') ? node.name : node.realName,
                    children: node.children // 当选中节点展开时没有子节点，设置其子节点为''
                }
            },
        }
    },

    //钩子函数
    created() {
        this.getPageMessage();//加载页面信息
    },

    methods: {
        init(row) {
            console.log('row=',row);
            // this.formData = row;
            this.questionname = row.questionname;
            this.secondLevel = row.questionBelong;
            this.ques = row.question;
        },

        // 获取会员列表
        getNameList() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'getQuestionnaireList',
                    param: {
                        page: {
                            pageIndex: 1,
                            pageSize: 999999,
                        },
                        searchData: this.searchData,
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                let arr = res.questionnaireList;
                arr.forEach((e, i) => {
                    this.nameList.push(e.questionname);
                });
                console.log('this.nameList==',this.nameList);
            });
        },

        verifyInput(val) {
            console.log('val==',val);
            if(this.nameList.includes(this.questionname) == true) {
                this.$message({showClose: true, message: '问卷名重复，请重新输入！', type: 'error'});
                this.questionname = '';
            }
        },

        getTypeList() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Dictionary',
                    method: 'selectByParent',
                    param: {
                        parentId: '0',
                        kind: 'producttype'
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log(res);
                this.questionnaireTypeList = res;
            });
        },

        getGPTypeList() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Dictionary',
                    method: 'selectByParent',
                    param: {
                        parentId: '1',
                        kind: 'producttype'
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log(res);
                res.forEach((e) => {
                    if(e.code == this.secondLevel) {
                        this.firstLevel = e.parentId;
                        this.typeList = res;
                    }
                })
            });
        },

        getAnotherTypeList() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Dictionary',
                    method: 'selectByParent',
                    param: {
                        parentId: '2',
                        kind: 'producttype'
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log(res);
                res.forEach((e) => {
                    if(e.code == this.secondLevel) {
                        this.firstLevel = e.parentId;
                        this.typeList = res;
                    }
                })
            });
        },

        changeVal(value) {
            console.log('value==',value);
            this.getNextTypeList(value)
        },
        changeVal2(value) {
            console.log('value2==',value);
            this.secondLevel = value;
        },

        getNextTypeList(id) {
            this.$nextTick(async () => {
                let params = {
                    className: 'Dictionary',
                    method: 'selectByParent',
                    param: {
                        parentId: id,
                        kind: 'producttype'
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res222',res);
                this.typeList = res;
                this.secondLevel = res[0].code;
            });
        },

        additem() {
            let obj = {
                questionDescription: '',
                questionType: '',
                releaseFlag: '',
                sequen: '',
                myQuestion: [
                    {answer: '', content:''},
                    {answer: '', content:''}
                ]
            }
            this.ques.push(obj)
        },
        removeitem(index) {
            this.ques.splice(index, 1)
        },
        addoption(index) {
            let obj = {order: '', content:''}
            this.ques[index].myQuestion.push(obj)
        },
        removeoption(index, ind) {
            this.ques[index].myQuestion.splice(ind, 1)
        },
        //套餐中报告属性的选中事件
        comboSelect() {
            if (this.form.reportKinds.length > this.comboValue - 1) {
                this.$message({message: '操作取消，已达到该套餐的最大选择数！', type: 'warning'});
            }
        },


        //加载页面信息 结合父组件
        // getPageMessage() {
        //     this.$nextTick(async () => {
        //         // this.loading.form = true;
        //         // this.loading.form = false;
        //
        //         //查询会员的套餐类型信息
        //         let comboParams = {
        //             className: 'Member',
        //             method: 'selectComboOfCodeAndNameList',
        //             params: {}
        //         }
        //         this.comboOptions = await this.$store.dispatch('http/post', comboParams);
        //
        //         //查询报告的相关属性信息
        //         const dictionaryParams = {
        //             className: 'Member',
        //             method: 'selectDictionaryByPropertyList',
        //             param: {}
        //         };
        //         this.comboDictionaryOptions = await this.$store.dispatch('http/post', dictionaryParams);
        //         this.comboDictionaryOptions.forEach((e) => {
        //             e.children.forEach((e) => {
        //                 if (e.children == null || e.children == '' || e.children.length == 0) e.children = '';
        //             });
        //         });
        //
        //         //查询最高级机构的代码和名称信息
        //         const departmentParams = {
        //             className: 'Member',
        //             method: 'selectDepartmentOfCodeAndNameList',
        //             param: {}
        //         };
        //         this.departmentOptions = await this.$store.dispatch('http/post', departmentParams);
        //         this.loading.form = false;
        //     });
        // },


        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                // this.$refs.formContent.resetFields();//清除form表格的内容和验证
                this.questionname= '',
                    this.ques= [
                        {
                            questionDescription: '',
                            questionType: '',
                            releaseFlag: '',
                            sequen: '',
                            myQuestion: [
                                {answer: '', content:''},
                                {answer: '', content:''}
                            ]
                        }
                    ]
                this.onBack(null);

            }
        },

        //保存按钮
        saveBtn: function () {
            console.log('ques22',this.ques);
            for(let i=0;i<this.ques.length;i++) {
                this.ques[i].sequen = i+1;
                for(let j=0;j<this.ques[i].myQuestion.length;j++) {
                    this.ques[i].myQuestion[j].answer = String.fromCharCode(64 + parseInt(j+1))
                }
            }
            console.log('ques33',this.ques);
            this.formData.questionname = this.questionname;
            this.formData.questionBelong = this.secondLevel;
            this.formData.ques = this.ques;
            console.log('1this.formData=',this.formData);

            this.$nextTick(async () => {
                let params = {
                    className: 'Activity',
                    method: 'updateQuestionnaire',
                    param: this.formData
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res222=',res);
                if (res) {
                    this.$message({showClose: true, message: '编辑成功！', type: 'success'});
                    // this.loading.btn = false;
                    setTimeout(() => {
                        if (this.onBack) {
                            // console.log(this.$refs.formContent);
                            // this.$refs.formContent.resetFields();//清除form表格的内容和验证
                            this.onBack('succ');
                        }
                    }, 500);
                } else {
                    this.$message({showClose: true, message: '编辑失败！', type: 'error'});
                    this.loading.btn = false;
                }
            })

        },
    },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-main {
    width: 85%;
}

/deep/.el-form {
    margin-bottom: 50px;
}
/deep/.addquestionnaire .el-input {
    width: 85%;
}
/deep/.addquestionnairename .el-input {
    width: 73%;
}
/deep/.addquestionnaire .el-input--medium .el-input__inner {
    //width: 95%;
}
/deep/.optionitem .el-input__inner {
    height: 30px;
    font-size: 12px;
}

.header {
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
}

.headerBtn {
    text-align: right;
    line-height: 5;
    height: 12px;
    float: left;
}

.labelStyle {
    display: inline-block;
    color: #101010;
    font-size: 15px;
    font-weight: 700;
    margin-bottom: 20px;
}

.currentDiv {
    background: #fff;
    margin: 10px;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}

.comboSelect {
    width: 100%;
}
.comboSelect1 {
    width: 42%;
}


/deep/ .vue-treeselect__control {
    height: 32px;
    line-height: 32px;
}

/deep/ .vue-treeselect__placeholder {
    font-size: 13px;
    line-height: 32px;
}

/deep/ .vue-treeselect__label {
    font-size: 13px;
}

/deep/ .vue-treeselect__single-value {
    font-size: 13px;
    line-height: 32px;
}


</style>
