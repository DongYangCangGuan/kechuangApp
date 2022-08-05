<template>
  <div>
    <el-header class="header">
      <el-row class="headerbtn">
        <span style="float: left">菜单信息</span>
        <el-button type="success" icon="el-icon-plus" plain @click="add">新增</el-button>
        <el-button type="primary" icon="el-icon-edit" plain @click="modify" class="animated zoomIn slow" v-if="isShow">修改</el-button>
      </el-row>
    </el-header>
    <el-container>
      <el-scrollbar style="width: 100%">
        <el-main>
          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="编号" prop="code">
              <el-input v-model="form.code" :readonly=true placeholder="菜单编号"></el-input>
            </el-form-item>
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" :readonly=true placeholder="菜单名称"></el-input>
            </el-form-item>
            <el-form-item label="上级菜单">
              <el-input v-model="form.parentname" :readonly=true placeholder="上级菜单"></el-input>
            </el-form-item>
            <el-form-item label="布局">
              <el-input v-model="form.component" :readonly=true placeholder="菜单布局"></el-input>
            </el-form-item>
            <el-form-item label="路径" prop="url">
              <el-input v-model="form.url" :readonly=true placeholder="菜单路径"></el-input>
            </el-form-item>
            <el-form-item label="图标" prop="icon">
              <svg-icon v-if="form.icon=='404'" icon-class="404" />
              <svg-icon v-if="form.icon=='bug'" icon-class="bug" />
              <svg-icon v-if="form.icon=='chart'" icon-class="chart" />
              <svg-icon v-if="form.icon=='clipboard'" icon-class="clipboard" />
              <svg-icon v-if="form.icon=='component'" icon-class="component" />
              <svg-icon v-if="form.icon=='dashboard'" icon-class="dashboard" />
              <svg-icon v-if="form.icon=='documentation'" icon-class="documentation" />
              <svg-icon v-if="form.icon=='drag'" icon-class="drag" />
              <svg-icon v-if="form.icon=='home_click'" icon-class="home_click" />
              <svg-icon v-if="form.icon=='guide'" icon-class="guide" />
              <svg-icon v-if="form.icon=='clock'" icon-class="clock" />
              <svg-icon v-if="form.icon=='dir'" icon-class="dir" />
              <svg-icon v-if="form.icon=='qq'" icon-class="qq" />
              <svg-icon v-if="form.icon=='shopping'" icon-class="shopping" />
              <svg-icon v-if="form.icon=='size'" icon-class="size" />
              <svg-icon v-if="form.icon=='email'" icon-class="email" />
              <svg-icon v-if="form.icon=='example'" icon-class="example" />
              <svg-icon v-if="form.icon=='excel'" icon-class="excel" />
              <svg-icon v-if="form.icon=='eye'" icon-class="eye" />
              <svg-icon v-if="form.icon=='form'" icon-class="form" />
              <svg-icon v-if="form.icon=='icon'" icon-class="icon" />
              <svg-icon v-if="form.icon=='international'" icon-class="international" />
              <svg-icon v-if="form.icon=='language'" icon-class="language" />
              <svg-icon v-if="form.icon=='lock'" icon-class="lock" />
              <svg-icon v-if="form.icon=='message'" icon-class="message" />
              <svg-icon v-if="form.icon=='money'" icon-class="money" />
              <svg-icon v-if="form.icon=='password'" icon-class="password" />
              <svg-icon v-if="form.icon=='people'" icon-class="people" />
              <svg-icon v-if="form.icon=='peoples'" icon-class="peoples" />
              <svg-icon v-if="form.icon=='star'" icon-class="star" />
              <svg-icon v-if="form.icon=='tab'" icon-class="tab" />
              <svg-icon v-if="form.icon=='table'" icon-class="table" />
              <svg-icon v-if="form.icon=='theme'" icon-class="theme" />
              <svg-icon v-if="form.icon=='user'" icon-class="user" />
              <svg-icon v-if="form.icon=='tree'" icon-class="tree" />
              <svg-icon v-if="form.icon=='zip'" icon-class="zip" />
              <svg-icon v-if="form.icon=='document'" icon-class="document" />
              <svg-icon v-if="form.icon=='calendar'" icon-class="calendar" />
            </el-form-item>
            <el-form-item label="是否缓存" prop="noCache">
              <el-switch v-model="form.noCache" :readonly=true></el-switch>
            </el-form-item>
            <el-form-item label="是否可用" prop="isused">
              <el-switch v-model="form.isused" :readonly=true></el-switch>
            </el-form-item>
            <el-form-item label="修改人" prop="modifier">
              <el-input v-model="form.modifier" :readonly=true placeholder="修改人"></el-input>
            </el-form-item>
            <el-form-item label="修改时间" prop="modifytime">
              <el-input v-model="form.modifytime"
                        :readonly=true
                        value-format="yyyy-MM-dd"
                        format="yyyy-MM-dd"
                        placeholder="修改时间">
              </el-input>
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
    props: {  // 接受父组件的传值
      onAdd: {
        type: Function,
        default: null
      },
      onModify: {
        type: Function,
        default: null
      }
    },
    data() {
      return {
        form: {
          id: '',
          name: '',
          code: '',
          component: '',
          path: '',
          url: '',
          isframe: false,
          icon: '',
          noCache: false,
          isused: true,
          parentname: '',
          modifytime: '',
        },
        isShow: false
      }
    },
    methods: {
      add() {
        if (this.onAdd) {
          this.onAdd(null)
        }
      },
      modify() {
        if (this.onModify) {
          this.onModify(null)
        }
      },
      choosehandle(data) {
        this.$nextTick(async () => {
          this.isShow = true;
          const params = {
            className: 'Menu',
            method: 'getMenuInfoByID',
            param: {id: data.id}
          };
          const treeNode = await this.$store.dispatch('http/post', params);
          this.form = treeNode;
          //  保存到store
          this.$store.dispatch('db/addStore', {key: "selectedMenuData", value: treeNode});
        })
      }
    },
    created() {
      bus.$on('menu-node-select', this.choosehandle)
    },
    beforeDestroy () {
      bus.$off('menu-node-select', this.choosehandle)
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
