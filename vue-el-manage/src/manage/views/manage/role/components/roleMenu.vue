<template>
  <div v-loading="loading">
    <el-header class="header">
      <el-row class="headerbtn">
        <span style="float: left" class="title">菜单信息</span>
      </el-row>
    </el-header>
    <el-container>
      <el-scrollbar style="height: 480px;min-width: 100%">
        <el-tree
          ref="tree"
          node-key="id"
          :default-checked-keys="checkedkeys"
          show-checkbox
          :render-content="renderContent"
          :data="data"
          highlight-current
          :props="defaultProps">
        </el-tree>
      </el-scrollbar>
    </el-container>
    <el-footer class="el-footer">
      <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
      <el-button type="success" icon="el-icon-check" plain @click="addClick">保存</el-button>
    </el-footer>
  </div>
</template>

<script>

  export default {
    name: "role-Menu",
    props: {
      //子页面调用父页面
      onBack: {
        type: Function,
        default: null
      },
      treeKey: '',
    },
    data() {
      return {
        data: [],
        loading: false,
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        checkedkeys: [],
        roleid: ''
      }
    },

    methods: {
      // 对el-tree添加图标
      renderContent(h, { node, data, store }) {
        return (
          <span>
            <svg-icon icon-class="dir" class-name="card-panel-icon"/>&nbsp;<span>{node.label}</span>
          </span>
        )
      },
      // 初始化菜单树
      loadChecked(roleid) {
        this.data = [];
        this.checkedkeys = [];
        this.roleid = roleid;

        this.$nextTick(async ()=> {
          this.loading = true;
          let params = {className: 'Menu', method: 'selectMenuByParentId', param: {}};
          this.data = await this.$store.dispatch('http/post', params);  // 获取菜单树
          params = { className: 'Menu', method: 'selectMenuByRoleId', param: {roleid: roleid} };
          this.checkedkeys = await this.$store.dispatch('http/post', params); // 获取菜单树选中节点
          this.loading = false;
        });
      },
      // 保存操作
      addClick() {
        this.loading = true;
        // 获取树的选中+半选中节点
        const checkedKeys = this.$refs.tree.getCheckedKeys();
        const halfCheckedKeys = this.$refs.tree.getHalfCheckedKeys();
        halfCheckedKeys.push(...checkedKeys);
        let ids = halfCheckedKeys.join(',');
        // 调用后台
        this.$nextTick(async ()=> {
          let params = {className: 'Menu', method: 'saveMenuByRoleId', param: {roleId: this.roleid, menuIds: ids}};
          let res = await this.$store.dispatch('http/post', params);
          if (res > 0) {
            this.$message({ showClose: true,  message: '保存成功！',  type: 'success' });
            this.loading = false;
            // 返回主页面
            setTimeout(() => {
              if (this.onBack) {
                this.onBack('null')
              }
            }, 1000)
          } else {
            this.$message({showClose: true, message: '操作失败！'});
            this.loading = false;
          }
        });
      },
      // 返回操作
      backClick() {
        // 返回首页
        if (this.onBack) {
          this.onBack(null)
        }
      },
    },
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
