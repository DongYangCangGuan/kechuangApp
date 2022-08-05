<template>
  <div class="container">
    <div class="panel-group" :class="control.state.treeInfoStyle" v-show="control.state.isTree">
      <el-row class="card-panel animated pulse" :gutter="20">
        <menu-tree-info :onBack="AddCancelBtn"></menu-tree-info>
      </el-row>
    </div>
    <div :class="control.state.showInfoStyle" v-show="control.state.isShow">
      <el-row class="panel-group" :gutter="20">
        <el-col :xs="12" :sm="12" :lg="10" class="card-panel-col">
          <el-row class="card-panel animated pulse" :gutter="20">
            <menu-tree :onChange="ChagneTreeBtn" ref="tree" ></menu-tree>
          </el-row>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="14" class="card-panel-col">
          <div class="card-panel animated pulse ">
            <menu-info :onAdd="AddBtn" :onModify="EditBtn" ></menu-info>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="panel-group" :class="control.state.addInfoStyle" v-if="control.state.isAdd">
      <el-row class="card-panel animated pulse" :gutter="20">
        <add-menu :onBack="AddCancelBtn"></add-menu>
      </el-row>
    </div>
    <div class="panel-group" :class="control.state.editInfoStyle" v-if="control.state.isEdit">
      <el-row class="card-panel animated pulse" :gutter="20">
        <edit-menu :onBack="EditCancelBtn"></edit-menu>
      </el-row>
    </div>
  </div>
</template>

<script>
  import MenuTree from './components/MenuTree.vue'
  import MenuInfo from './components/MenuInfo.vue'
  import AddMenu from './components/AddMenu.vue'
  import MenuTreeInfo from './components/MenuTreeInfo.vue'
  import EditMenu from './components/EditMenu.vue'
  import control from '@MAN/store/modules/control';

  export default {
    name: "manage-menu",
    components: {
      MenuTree, MenuInfo, AddMenu, MenuTreeInfo, EditMenu
    },
    data() {
      return {
        control: control
      }
    },
    methods:{
      ChagneTreeBtn:function(){
        this.$store.commit('SET_FORWARD_STYLE','tree');
      },
      ReloadTreeBtn:function(){
        this.$refs.tree.reload();
      },
      AddBtn: function (val) {
        this.$store.commit('SET_FORWARD_STYLE','add');
      },
      EditBtn: function (val) {
        this.$store.commit('SET_FORWARD_STYLE','edit');
      },
      AddCancelBtn: function (val) {
        if(val=='succ')
          this.ReloadTreeBtn();
        this.$store.commit('SET_BACK_STYLE' );

      },
      EditCancelBtn: function (val) {
        if(val=='succ')
          this.ReloadTreeBtn();
        this.$store.commit('SET_BACK_STYLE' );
      },

    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .container{
    padding: 8px;
  }

  .box1{
    position: relative;
    border-radius: 3px;
    background: #ffffff;
    border-top: 3px solid #3c763d;
    margin-bottom: 20px;
    width: 100%;
  }

  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  .panel-group {
    margin-left: 5px;
    margin-top: 1px;
    margin-right: 8px;
    .card-panel-col {
      margin-bottom: 32px;
    }
    .card-panel {
      height: 40rem;
      cursor: default;
      font-size: 12px;
      position: relative;
      overflow: auto;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
      border-color: rgba(0, 0, 0, .05);
      border-radius: 5px;
      &.selected {
        box-shadow: 1px 1px 5px #888888;
      }
      &:hover {
        box-shadow: 2px 2px 10px #888888;
        .card-panel-icon-wrapper {
          color: #fff;
        }
        .icon-people {
          background: #40c9c6;
        }
        .icon-message {
          background: #36a3f7;
        }
        .icon-money {
          background: #f4516c;
        }
        .icon-shoppingCard {
          background: #34bfa3
        }
      }
      .icon-people {
        color: #40c9c6;
      }
      .icon-message {
        color: #36a3f7;
      }
      .icon-money {
        color: #f4516c;
      }
      .icon-shoppingCard {
        color: #34bfa3
      }
      .card-panel-icon-wrapper {
        float: left;
        margin: 14px 0 0 14px;
        padding: 16px;
        transition: all 0.38s ease-out;
        border-radius: 6px;
      }
      .card-panel-icon {
        float: left;
        font-size: 28px;
      }
      .card-panel-description {
        float: right;
        font-weight: bold;
        margin: 26px;
        margin-left: 0px;
        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }
        .card-panel-num {
          font-size: 20px;
        }
      }
    }
  }
</style>
