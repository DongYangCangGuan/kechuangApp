<template>
  <div class="container">
    <div  :class="control.state.showInfoStyle" v-show="control.state.isShow">
    <pictureManage :onChangeTree="ChagneTreeBtn" :onChangeStaff="ChangeStaff" :onBack="AddCancelBtn"></pictureManage>
  </div>
  </div>
</template>
<script>

  import pictureManage from "./components/pictureManage"
  import control from '@MAN/store/modules/control';

  export default {
    name:"index",
     data() {
      return {
        control:control,//控制滑动组件
        }
    },
    components:{
      pictureManage
    },
    watch:{


    },
    created(){

    },
    methods: {
      ChagneTreeBtn:function(id){
        this.$refs.tree.loadChecked(id);
        this.$store.commit('SET_FORWARD_STYLE','tree');
      },
      ChangeStaff:function(id){
        this.$store.commit('SET_FORWARD_STYLE','edit');

        this.$refs.user.load(id);
      },
       AddCancelBtn: function (val) {
        this.$store.commit('SET_BACK_STYLE' );
      },

    }

  };
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .container{
    padding: 8px;
  }

</style>
