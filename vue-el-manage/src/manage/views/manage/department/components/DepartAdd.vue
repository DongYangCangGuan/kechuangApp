<template>
    <div>
        <el-header class="header">
            <el-row class="headerbtn">
                <span style="float: left">新增机构信息</span>
            </el-row>
        </el-header>
        <el-container>
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
                                    :load-options="loadOptions"
                                    :options="options"
                                    placeholder="上级机构"
                                    :searchable="false"
                                    :clearable="false"
                                    :normalizer="normalizer"
                                    v-if="treeShow"/>
                                <el-input v-model="form.parentname" name="parentname" placeholder="上级机构" v-else
                                          readonly></el-input>
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
                                <el-select v-model="form.level" clearable placeholder="请选择">
                                    <el-option
                                        v-for="item in dropoptions"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.code">
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
                        <!--<el-col :xs="5" :sm="5" :lg="4">-->
                        <!--<el-form-item label="修改人员">-->
                        <!--<el-input v-model="form.modifierid" name="modifierid" placeholder="修改人员" disabled></el-input>-->
                        <!--</el-form-item>-->
                        <!--</el-col>-->
                        <!--<el-col :xs="7" :sm="7" :lg="6">-->
                        <!--&lt;!&ndash;<el-form-item label="修改时间">&ndash;&gt;-->
                        <!--&lt;!&ndash;<el-input v-model="form.modifytime" name="modifytime" placeholder="修改时间" disabled></el-input>&ndash;&gt;-->
                        <!--&lt;!&ndash;</el-form-item>&ndash;&gt;-->
                        <!--</el-col>-->
                    </el-row>
                    <el-form-item label="是否可用" prop="isused">
                        <el-switch v-model="form.isused" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                    </el-form-item>
                </el-form>
                <el-footer class="el-footer">
                    <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
                    <el-button :loading="loading" type="success" icon="el-icon-check" plain @click="addClick">保存
                    </el-button>
                </el-footer>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import {formatDate} from "@COM/utils/datetime";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
    name: "add-depart",
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
                //creatorid: this.$store.state.user.entity.empid,
                creatorname: this.$store.state.user.entity.userName,
                createtime: formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'),
                //modifierid: this.$store.state.user.entity.empid,
                //modifytime: formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'),
                isused: true,
                treeabout: '',
                level: '',
            },
            treeShow: false,
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
            loading: false,
            options: null,
            normalizer(node) {  // 自定义下拉树节点模板
                return {
                    id: node.id,
                    label: node.name,
                    children: JSON.stringify(node.children) === '[]' ? '' : node.children // 当选中节点展开时没有子节点，设置其子节点为''
                }
            }
        }
    },
    methods: {
        // 动态加载子树
        async loadOptions({action, parentNode, callback}) {
            if (action === 'LOAD_CHILDREN_OPTIONS') {
                const params = {
                    className: 'Dept',
                    method: 'getSubDeptsByDeptId',
                    param: {
                        id: parentNode.id,
                        isused: '1'
                    }
                };
                parentNode.children = await this.$store.dispatch('http/post', params);

                // 回调
                callback()
            }
        },
        treetest: function () {
            let data = this.$store.state.db.get('selectedDeptData');
            this.form.parentid = data.id;
            this.form.parentname = data.name;
            if (data == '' || data == null) {
                this.treeShow = true;
            }
        },
        backClick: function () {
            if (this.onBack) {
                this.onBack(null)
            }
        },
        addClick: function () {
            this.$refs.form.validate(valid => {
                if (valid) {
                    // 提交后台
                    this.loading = true;
                    this.$nextTick(async () => {
                        if (this.form.parentid == null || this.form.parentid == '')
                            this.form.parentid = 0;

                        const params = {
                            className: 'Dept',
                            method: 'addDeptInfo',
                            param: this.form
                        };

                        const res = await this.$store.dispatch('http/post', params);
                        if (res === '200') {
                            this.$message({showClose: true, message: 'success', type: 'success'});
                            this.loading = false;
                            // 返回机构信息页面
                            setTimeout(() => {
                                if (this.onBack) {
                                    this.onBack('succ')
                                }
                            }, 1000)
                        } else {
                            this.$message({showClose: true, message: 'error', type: 'error'});
                            this.loading = false;
                        }
                    });
                } else {
                    return false;
                }
            });
        }
    },
    created() {
        this.$nextTick(async () => {
            // 加载页面下拉框树（只加载根节点）
            let params = {
                className: 'Dept',
                method: 'getDeptInfoByDeptId',
                param: {id: this.$store.state.user.entity.departmentId}
            };
            const treeRoot = await this.$store.dispatch('http/post', params);
            this.form.parentid = treeRoot.id;     // 默认选中根节点
            this.options = [treeRoot];            // 填充下拉树
            // 加载机构级别下拉框
            params = {
                className: 'Staff',
                method: 'selectEducationOrType',
                param: {kind: "DepartmentClassification"}
            };
            this.dropoptions = await this.$store.dispatch('http/post', params);
            this.treetest();
        });
        console.log('this.from', this.from)
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
