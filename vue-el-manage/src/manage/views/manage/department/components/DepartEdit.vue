<template>
    <div v-loading="loading.form">
        <el-header class="header">
            <el-row class="headerbtn">
                <span style="float: left">修改机构信息</span>
            </el-row>
        </el-header>
        <el-container>
            <el-scrollbar style="width: 100%">
                <el-main>
                    <el-form ref="form" status-icon :model="form" :rules="rules" label-width="80px">
                        <el-row class="panel-group" :gutter="20">
                            <el-col :xs="12" :sm="12" :lg="10">
                                <el-form-item label="机构编号" prop="code">
                                    <el-input v-model="form.code" name="code" placeholder="机构编号"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="12" :sm="12" :lg="10">
                                <el-form-item label="机构名称" prop="name">
                                    <el-input v-model="form.name" name="name" placeholder="机构名称"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="20">
                            <el-col :xs="12" :sm="12" :lg="10">
                                <el-form-item label="上级机构">
                                    <treeselect
                                        v-model="form.parentid"
                                        :options="options"
                                        placeholder="上级机构"
                                        :searchable="false"
                                        :clearable="false"
                                        :normalizer="normalizer">
                                    </treeselect>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="12" :sm="12" :lg="10">
                                <el-form-item label="工作地址" prop="address">
                                    <el-input v-model="form.address" name="address" placeholder="工作地址"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="20">
                            <el-col :xs="12" :sm="12" :lg="10">
                                <el-form-item label="机构级别">
                                    <el-select v-model="form.level">
                                        <el-option
                                            v-for="(item,index) in dropoptions"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="20">
                            <el-col :xs="5" :sm="5" :lg="4">
                                <el-form-item label="创建人员">
                                    <el-input v-model="form.creatorname" name="creatorid" placeholder="创建人员"
                                              disabled></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="7" :sm="7" :lg="6">
                                <el-form-item label="创建时间">
                                    <el-input v-model="form.createtime" name="createtime" placeholder="创建时间"
                                              disabled></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="5" :sm="5" :lg="4">
                                <el-form-item label="上次修改">
                                    <el-input v-model="form.modifiername" name="modifierid" placeholder="修改人员"
                                              disabled></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="7" :sm="7" :lg="6">
                                <el-form-item label="上次修改">
                                    <el-input v-model="form.modifytime" name="modifytime" placeholder="修改时间"
                                              disabled></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row class="panel-group" :gutter="20">
                            <el-col :xs="12" :sm="12" :lg="10">
                                <el-form-item label="是否启用" prop="isused">
                                    <el-switch v-model="form.isused" active-color="#13ce66"
                                               inactive-color="#ff4949"></el-switch>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                    <el-footer class="el-footer">
                        <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
                        <el-button :loading="loading.btn" type="success" icon="el-icon-check" plain @click="saveClick">
                            保存
                        </el-button>
                    </el-footer>
                </el-main>
            </el-scrollbar>
        </el-container>
    </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
    name: "edit-depart",
    props: {
        onBack: {
            type: Function,
            default: null
        }
    },
    components: {Treeselect},
    data() {
        var checkCode = async (rule, value, callback) => {
            if (!value) {
                return callback(new Error('请输入机构编号'))
            } else {
                // 提交后台验证机构编号是否存在
                const params = {
                    className: 'Dept',
                    method: 'isDeptCodeExisted',
                    param: {
                        id: this.form.id,
                        code: this.form.code
                    }
                };
                const res = await this.$store.dispatch('http/post', params);
                if (res === '203') {
                    return callback(new Error('机构编号已存在！'))
                } else {
                    return callback()
                }
            }
        };
        return {
            form: {
                id: '',
                code: '',
                name: '',
                address: '',
                parentid: '',
                parentname: '',
                creatorid: '',
                creatorname: '',
                createtime: '',
                modifierid: '',
                modifiername: '',
                modifytime: '',
                isused: true,
                treeabout: '',
                level: ''
            },
            dropoptions: '',
            rules: {
                code: [
                    {required: true, validator: checkCode, trigger: 'blur'}
                ],
                name: [
                    {required: true, message: '请输入机构名称', trigger: 'blur'}
                ],
                address: [
                    {required: true, message: '请输入工作地址', trigger: 'blur'}
                ]
            },
            loading: {
                form: false,
                btn: false
            },
            options: [],
            normalizer(node) {  // 自定义下拉树节点模板
                return {
                    id: node.id,
                    label: node.name,
                    children: JSON.stringify(node.children) === '[]' ? '' : node.children
                }
            }
        }
    },
    methods: {
        backClick: function () {
            if (this.onBack) {
                this.onBack(null)
            }
        },
        saveClick: function () {
            this.$refs.form.validate(valid => {
                if (valid) {
                    // 提交后台
                    this.loading.btn = true;
                    this.$nextTick(async () => {
                        if (this.form.parentid == null || this.form.parentid == '')
                            this.form.parentid = 0;

                        const params = {
                            className: 'Dept',
                            method: 'editDeptInfo',
                            param: this.form
                        };
                        const res = await this.$store.dispatch('http/post', params);
                        if (res === '200') {
                            this.$message({showClose: true, message: '提交成功', type: 'success'});
                            this.loading.btn = false;
                            // 返回机构信息页面
                            setTimeout(() => {
                                if (this.onBack) {
                                    this.onBack('succ')
                                }
                            }, 1000)
                        } else if (res === '201') {
                            this.$message({showClose: true, message: 'error', type: 'error'});
                            this.loading.btn = false;
                        } else if (res === '203') {
                            this.$message({showClose: true, message: '当前机构不能变动成为自己的子机构！', type: 'error'});
                            this.loading.btn = false;
                        } else if (res === '204') {
                            this.$message({showClose: true, message: '该部门下有已启用的人员,无法停用！', type: 'error'});
                            this.loading.btn = false;
                        } else if (res === '205') {
                            this.$message({showClose: true, message: '该部门下有已启用的部门,无法停用！', type: 'error'});
                            this.loading.btn = false;
                        }
                    })
                } else {
                    return false;
                }
            });
        }
    },
    created() {
        this.$nextTick(async () => {
            this.loading.form = true;

            // 加载机构树供用户变更上级机构树
            let params = {
                className: 'Dept',
                method: 'getDeptTreeByParentId',
                param: {
                    id: '0'
                }
            };
            this.options = await this.$store.dispatch('http/post', params);// 填充下拉树
            if (this.options.parentname === '' || this.options.parentname == null) {
                this.options[0].parentname = '';
            }
            // console.log('123',this.from.parentid)
            // 加载机构级别下拉框
            params = {
                className: 'Staff',
                method: 'selectEducationOrType',
                param: {
                    kind: "DepartmentClassification"
                }
            };
            this.dropoptions = await this.$store.dispatch('http/post', params);

            // 等待机构树完全加载完毕后，填充页面
            this.form = this.$store.state.db.get('selectedDeptData');
            console.log('111', this.form);

            this.loading.form = false;
        });
    }
}
</script>

<style scoped>
.header {
    background-color: #f9f8fb;
    text-align: left;
    margin-bottom: 10px;
}

.headerbtn {
    text-align: right;
    line-height: 5;
    height: 12px;
}

</style>
