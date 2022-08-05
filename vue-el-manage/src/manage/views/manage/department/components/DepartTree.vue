<template>
    <div style="overflow: auto">
        <el-header class="header" v-if="this.showheader">
            <el-row class="headerbtn">
                <span style="float: left">部门列表</span>
                <!--<el-button type="primary" icon="el-icon-share" @click="change" plain>变更上下级</el-button>-->
                <el-button type="info" icon="el-icon-refresh" @click="reload" plain>刷新</el-button>
            </el-row>
        </el-header>
        <el-container style="overflow: auto">
            <el-tree
                style="overflow: auto"
                class="treestyle"
                :render-content="renderContent"
                :props="defaultProps"
                node-key="id"
                :default-expand-all="true"
                @node-click="nodeClick"
                :expand-on-click-node="false"
                :highlight-current="true"
                :load="nodeOpen"
                :key="treeKey"
                lazy
            >
            </el-tree>
        </el-container>
    </div>
</template>

<script>
import bus from '@COM/utils/bus';

export default {
    props: {
        showheader: {
            type: Boolean,
            required: true
        }
    },
    name: 'depart-tree',
    data() {
        return {
            defaultProps: {
                label: 'name',
                children: 'zones'
            },
            treeKey: ''
        }
    },
    methods: {
        reload() {
            this.treeKey = +new Date();
        },
        // 对el-tree添加图标
        renderContent(h, {node, data, store}) {
            return (
                <span>
            <svg-icon icon-class="dir" class-name="card-panel-icon"/>
                    &nbsp;<span>{node.label}</span>
          </span>
            );
        },
        // 机构树节点选中
        nodeClick(data, node, con) {
            bus.$emit('dept-node-select', data);
        },
        // 机构树数据加载
        nodeOpen(node, resolve) {
            console.log('执行了nodeopen');
            console.log('node=',node);
            if(node.childNodes.length == 0) {
                bus.$emit('dept-node-select', node.data);
            }

            setTimeout(async () => {  // 懒加载
                if (node.level == 0) {
                    const params = {
                        className: 'Dept',
                        method: 'getDeptInfoByDeptId',
                        // method: 'getDeptInfoList',
                        param: {id: this.$store.state.user.entity.departmentId}
                    };
                    return resolve([
                        await this.$store.dispatch('http/post', params)
                    ])
                } else if (node.level > 0) {
                    const params = {
                        className: 'Dept',
                        method: 'getSubDeptsByDeptId',
                        param: {id: node.data.id}
                    };
                    return resolve(
                        await this.$store.dispatch('http/post', params)
                    )
                }
            }, 500);
        }
    }
};
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

.treestyle {
    width: 100%;
    min-height: 400px;
}
</style>
