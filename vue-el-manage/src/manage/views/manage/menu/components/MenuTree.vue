<template>
    <div>
        <el-header class="header">
            <el-row class="headerBtn">
                <span style="float: left" class="title">菜单信息</span>
                <el-button type="primary" icon="el-icon-sort" @click="change" plain>变更上下级</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="reload" plain>刷新菜单</el-button>
            </el-row>
        </el-header>
        <el-container>
            <el-tree
                class="treestyle"
                ref="tree"
                :render-content="renderContent"
                :props="defaultProps"
                @node-click="nodeClick"
                :expand-on-click-node="false"
                :highlight-current="true"
                :load="openNode"
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
    name: 'menu-tree',
    data() {
        return {
            defaultProps: {
                label: 'name',
                children: 'zones',
            },
            treeKey: ''
        }
    },
    props: {  // 接受父组件的传值
        onChange: {
            type: Function,
            default: null
        },
    },
    methods: {
        change() {
            if (this.onChange) {
                this.onChange(null)
            }
        },
        reload() {
            this.treeKey = +new Date();
        },
        renderContent(h, {node, data, store}) {
            return (
                <span>
            <svg-icon icon-class="dir" class-name="card-panel-icon"/>
                    &nbsp;<span>{node.label}</span>
          </span>
            );
        },
        nodeClick(data, node, con) {
            bus.$emit('menu-node-select', data)
        },
        // 菜单树加载
        openNode(node, resolve) {
            setTimeout(async () => {  // 懒加载
                if (node.level === 0) {
                    const params = {
                        className: 'Menu',
                        method: 'getMenuList',
                        param: {id: this.$store.state.user.entity.departmentId}
                    };
                    console.log(await this.$store.dispatch('http/post', params), 'menu------------->');
                    return resolve(await this.$store.dispatch('http/post', params));
                } else if (node.level > 0) {
                    const params = {
                        className: 'Menu',
                        method: 'getMenuListByID',
                        param: {id: node.data.id}
                    };
                    return resolve(await this.$store.dispatch('http/post', params))
                }
            }, 500);
        }
    }
};
</script>

<style scoped>
.treestyle {
    width: 100%;
    min-height: 400px;
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
}
</style>
