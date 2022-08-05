<template>
  <div class="container">
   <div :class="control.state.showInfoStyle" v-show="control.state.isShow">
    <el-row class="panel-group" :gutter="20">
      <el-col :xs="12" :sm="12" :lg="7" class="card-panel-col">
        <div class="card-panel animated pulse">
          <Category-tree></Category-tree>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="17" class="card-panel-col">
        <div class="card-panel animated pulse">
          <product-Category :onAdd="AddBtn" :onModify="EditBtn"></product-Category>
        </div>
      </el-col>
    </el-row>
  </div>
  <div class="panel-group" :class="control.state.addInfoStyle" v-if="control.state.isAdd">
    <el-row class="card-panel animated pulse" :gutter="20">
       <Category-Add :onBack="AddCancelBtn"></Category-Add>
    </el-row>
  </div>
  <div class="panel-group" :class="control.state.editInfoStyle" v-if="control.state.isEdit">
  <el-row class="card-panel animated pulse" :gutter="20">
    <Category-Edit :onBack="EditCancelBtn"></Category-Edit>
  </el-row>
  </div>
</div>
</template>

<script>
  import CategoryTree from './GoodsInfo/CategoryTree'
  import productCategory from './GoodsInfo/productCategory'
  import CategoryAdd from './GoodsInfo/CategoryAdd';
  import CategoryEdit from './GoodsInfo/CategoryEdit';
  import control from '@MAN/store/modules/control';
  export default {
    name: "manage-goods",
    components:{
      CategoryTree,
      productCategory,
      CategoryAdd,
      CategoryEdit
    },
    data() {
      return {
       control:control
      }
    },
    methods: {
      AddBtn: function (val) {
        this.$store.commit('SET_FORWARD_STYLE','add');
      },
      EditBtn: function (val) {
        this.$store.commit('SET_FORWARD_STYLE','edit');
      },
      AddCancelBtn: function (val) {
        this.$store.commit('SET_BACK_STYLE');
      },
      EditCancelBtn: function (val) {
        this.$store.commit('SET_BACK_STYLE');
      },
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .container{
    padding: 8px;
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
    margin-top: 18px;
    .card-panel-col{
      margin-bottom: 32px;
    }
    .card-panel{
      height: 40rem;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      overflow:auto;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
      border-color: rgba(0, 0, 0, .05);
      &:hover {
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
        font-size: 48px;
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

