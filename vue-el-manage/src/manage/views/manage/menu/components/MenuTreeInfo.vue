<template>
  <div v-loading="loading">
    <el-header class="header">
      <el-row class="headerbtn">
        <span style="float: left" class="title">变更上下级</span>
      </el-row>
    </el-header>
    <el-container>
      <el-main>
        <el-tree
          :render-content="renderContent"
          :props="defaultProps"
          :data="treeData"
          :expand-on-click-node="false"
          :highlight-current="true"
          @node-drag-end="handleDragEnd"
          draggable
        >
        </el-tree>
      </el-main>
      <el-footer class="el-footer">
        <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
        <el-button type="success" icon="el-icon-refresh" plain @click="refreshClick">刷新</el-button>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
  export default {
    name: "menu-tree-info",
    props: {  // 接受父组件传值
      onBack: {
        type: Function,
        default: null
      },
    },
    data() {
      return {
        defaultProps: {
          label: 'name',
          children: 'children'
        },
        treeData: [],
        loading: false
      }
    },
    methods: {
      // 树节点图标
      renderContent(h, { node, data, store }) {
        return (
          <span>
            <svg-icon icon-class="dir" class-name="card-panel-icon"/>&nbsp;<span>{node.label}</span>
          </span>
        );
      },
      // 树节点拖拽结束时（可能未成功）触发的事件
      handleDragEnd(draggingNode, dropNode, dropType, ev) {
        const myid = draggingNode.data.id;
        let myparentid = '';
        if (dropType === 'inner') { // 成为子节点
          myparentid = dropNode.data.id;
        } else {
          myparentid = dropNode.data.parentid
        }
        this.$nextTick(async () => {
          const params = {
            className: 'Menu',
            method: 'modifyMenuTree',
            param: {
              id: myid,
              parentid: myparentid
            }
          };
          const res = await this.$store.dispatch('http/post', params)
          if (res === '200') {
            this.$message({  showClose: true,  message: '操作成功！',  type: 'success'  });
          } else {
            this.$message({  showClose: true,  message: '保存失败！',  type: 'error'  });
          }
        })
      },
      // 返回按钮
      backClick() {
        if (this.onBack) {
          this.onBack(null)
        }
      },
      // 刷新按钮，重新加载菜单树
      refreshClick() {
        this.$nextTick(async () => {
          this.loading = true;

          const params = {
            className: 'Menu',
            method: 'selectMenuByParentId',
            param: {}
          };
          this.treeData = await this.$store.dispatch('http/post', params);

          this.loading = false;
        })
      }
    },
    // 初始化菜单树
    mounted() {
      this.$nextTick(async () => {
        this.loading = true;

        const params = {
          className: 'Menu',
          method: 'selectMenuByParentId',
          param: {
            id: '0'
          }
        };
        this.treeData = await this.$store.dispatch('http/post', params)

        this.loading = false;
      })
    }
  }
</script>

<style scoped>
  .header{
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
  }

  .headerbtn{
    text-align: right;
    line-height: 5;height: 12px;
  }
</style>

