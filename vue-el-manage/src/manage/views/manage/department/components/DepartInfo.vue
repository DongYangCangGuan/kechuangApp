<template>
    <div>
        <el-header class="header">
            <el-row class="headerbtn">
                <span style="float: left">部门详情</span>
                <el-button type="success" icon="el-icon-plus" plain @click="add">新增</el-button>
                <el-button type="primary" icon="el-icon-edit" plain @click="edit" class="animated zoomIn slow"
                           v-if="isShow">修改
                </el-button>
            </el-row>
        </el-header>
        <el-container>
            <el-scrollbar style="width: 100%">
                <el-main>
                    <el-form ref="form" :model="form" label-width="80px">
                        <el-form-item label="机构编号">
                            <el-input v-model="form.code" :readonly=true placeholder="机构编号"></el-input>
                        </el-form-item>
                        <el-form-item label="机构名称">
                            <el-input v-model="form.name" :readonly=true placeholder="机构名称"></el-input>
                        </el-form-item>
                        <el-form-item label="上级机构">
                            <el-input v-model="form.parentname" :readonly=true placeholder="上级机构"></el-input>
                        </el-form-item>
                        <el-form-item label="工作地址">
                            <el-input v-model="form.address" :readonly=true placeholder="工作地址"></el-input>
                        </el-form-item>
                        <el-form-item label="是否可用">
                            <el-switch v-model="form.isused" active-color="#13ce66" inactive-color="#ff4949"
                                       disabled></el-switch>
                        </el-form-item>
                        <el-form-item label="机构级别">
                            <el-input v-model="form.level" :readonly=true placeholder="机构级别"></el-input>
                        </el-form-item>
                        <el-form-item label="创建人员">
                            <el-input v-model="form.creatorid" :readonly=true placeholder="创建人员"></el-input>
                        </el-form-item>
                        <el-form-item label="创建时间">
                            <el-input v-model="form.createtime" :readonly=true placeholder="修改时间"></el-input>
                        </el-form-item>
                        <el-form-item label="上次修改">
                            <el-input v-model="form.modifierid" :readonly=true placeholder="修改人员"></el-input>
                        </el-form-item>
                        <el-form-item label="上次修改">
                            <el-input v-model="form.modifytime" :readonly=true placeholder="修改时间"></el-input>
                        </el-form-item>
                    </el-form>
                </el-main>
            </el-scrollbar>
        </el-container>
    </div>
</template>


<script>
import bus from '@COM/utils/bus';

export default {
    // 接受父组件的传值
    props: {
        onAdd: {
            type: Function,
            default: null
        },
        onModify: {
            type: Function,
            default: null
        }
    },
    name: 'depart-info',
    data() {
        return {
            form: {
                id: '',
                code: '',
                name: '',
                address: '',
                parentid: '',
                parentname: '',
                creatorid: '',
                createtime: '',
                modifierid: '',
                modifytime: '',
                isused: false,
                treeabout: '',
                level: ''
            },
            isShow: false
        }
    },
    methods: {
        add: function () {
            if (this.onAdd) {
                this.onAdd(null)
            }
        },
        edit: function () {
            if (this.onModify) {
                this.onModify(null)
            }
        },
        handleNodeClick(data) {
            this.$nextTick(async () => {
                this.isShow = true;
                const params = {
                    className: 'Dept',
                    method: 'getDeptInfoByDeptId',
                    param: {id: data.id}
                };
                const treeNode = await this.$store.dispatch('http/post', params);
                this.form = treeNode;
                this.$store.dispatch('db/addStore', {key: "selectedDeptData", value: treeNode});
            })
        }
    },
    // 钩子函数，处理左侧机构树节点选中
    created() {
        bus.$on('dept-node-select', this.handleNodeClick)
    },
    beforeDestroy() {
        bus.$off('dept-node-select', this.handleNodeClick)
        this.$store.dispatch('db/addStore', {key: "selectedDeptData", value: ''});
    }
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

.el-dropdown {
    vertical-align: top;
}

.el-dropdown + .el-dropdown {
    margin-left: 15px;
}

.el-icon-arrow-down {
    font-size: 12px;
}
</style>
