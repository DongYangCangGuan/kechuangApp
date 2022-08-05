<template>
  <div v-loading="loading.form">
    <el-header class="header">
      <el-row class="headerbtn">
        <span style="float: left" class="title">菜单信息</span>
      </el-row>
    </el-header>
    <el-container>
      <el-scrollbar style="width: 100%">
        <el-main>
          <el-form ref="form" status-icon :model="form" :rules="rules" label-width="80px">
            <el-row class="panel-group" :gutter="20">
              <el-col :xs="12" :sm="12" :lg="10">
                <el-form-item label="编号" prop="code">
                  <el-input v-model="form.code" name="code" placeholder="菜单编号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="10">
                <el-form-item label="名称" prop="name">
                  <el-input v-model="form.name" placeholder="菜单名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="panel-group" :gutter="20">
              <el-col :xs="12" :sm="12" :lg="10">
                <el-form-item label="上级菜单">
                  <treeselect
                    v-model="form.parentid"
                    :options="options"
                    placeholder="上级菜单"
                    :searchable="false"
                    :clearable="false"
                    :normalizer="normalizer"
                  />
                </el-form-item>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="10">
                <el-form-item label="布局">
                  <el-select v-model="form.component" class="el-input">
                    <el-option label="显示菜单" value="Layout"></el-option>
                    <el-option label="不显示左侧菜单" value="full"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="panel-group" :gutter="20">
              <el-col :xs="12" :sm="12" :lg="10">
                <el-form-item label="url" prop="url">
                  <el-input v-model="form.url" placeholder="菜单地址"></el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="10">
                <el-form-item label="path" prop="path">
                  <el-input v-model="form.path" placeholder="菜单路径"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="panel-group" :gutter="20">
              <el-col :xs="12" :sm="12" :lg="10">
                <el-form-item label="图标"  prop="icon">
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
                  <el-select v-model="form.icon">
                    <el-option label="404" value="404">
                      <svg-icon icon-class="404" />
                      404
                    </el-option>
                    <el-option label="bug" value="bug">
                      <svg-icon icon-class="bug" />
                      bug
                    </el-option>
                    <el-option label="chart" value="chart">
                      <svg-icon icon-class="chart" />
                      chart
                    </el-option>
                    <el-option label="clipboard" value="clipboard">
                      <svg-icon icon-class="clipboard" />
                      clipboard
                    </el-option>
                    <el-option label="component" value="component">
                      <svg-icon icon-class="component" />
                      component
                    </el-option>
                    <el-option label="dashboard" value="dashboard">
                      <svg-icon icon-class="dashboard" />
                      dashboard
                    </el-option>
                    <el-option label="documentation" value="documentation">
                      <svg-icon icon-class="documentation" />
                      documentation
                    </el-option>
                    <el-option label="drag" value="drag">
                      <svg-icon icon-class="drag" />
                      drag
                    </el-option>
                    <el-option label="home_click" value="home_click">
                      <svg-icon icon-class="home_click" />
                      home_click
                    </el-option>
                    <el-option label="guide" value="guide">
                      <svg-icon icon-class="guide" />
                      guide
                    </el-option>
                    <el-option label="clock" value="clock">
                      <svg-icon icon-class="clock" />
                      clock
                    </el-option>
                    <el-option label="dir" value="dir">
                      <svg-icon icon-class="dir" />
                      dir
                    </el-option>
                    <el-option label="qq" value="qq">
                      <svg-icon icon-class="qq" />
                      qq
                    </el-option>
                    <el-option label="shopping" value="shopping">
                      <svg-icon icon-class="shopping" />
                      shopping
                    </el-option>
                    <el-option label="size" value="size">
                      <svg-icon icon-class="size" />
                      size
                    </el-option>
                    <el-option label="email" value="email">
                      <svg-icon icon-class="email" />
                      email
                    </el-option>
                    <el-option label="example" value="example">
                      <svg-icon icon-class="example" />
                      example
                    </el-option>
                    <el-option label="excel" value="excel">
                      <svg-icon icon-class="excel" />
                      excel
                    </el-option>
                    <el-option label="eye" value="eye">
                      <svg-icon icon-class="eye" />
                      eye
                    </el-option>
                    <el-option label="form" value="form">
                      <svg-icon icon-class="form" />
                      form
                    </el-option>
                    <el-option label="icon" value="icon">
                      <svg-icon icon-class="icon" />
                      icon
                    </el-option>
                    <el-option label="international" value="international">
                      <svg-icon icon-class="international" />
                      international
                    </el-option>
                    <el-option label="language" value="language">
                      <svg-icon icon-class="language" />
                      language
                    </el-option>
                    <el-option label="lock" value="lock">
                      <svg-icon icon-class="lock" />
                      lock
                    </el-option>
                    <el-option label="message" value="message">
                      <svg-icon icon-class="message" />
                      message
                    </el-option>
                    <el-option label="money" value="money">
                      <svg-icon icon-class="money" />
                      money
                    </el-option>
                    <el-option label="password" value="password">
                      <svg-icon icon-class="password" />
                      password
                    </el-option>
                    <el-option label="people" value="people">
                      <svg-icon icon-class="people" />
                      people
                    </el-option>
                    <el-option label="peoples" value="peoples">
                      <svg-icon icon-class="peoples" />
                      peoples
                    </el-option>
                    <el-option label="star" value="star">
                      <svg-icon icon-class="star" />
                      star
                    </el-option>
                    <el-option label="tab" value="tab">
                      <svg-icon icon-class="tab" />
                      tab
                    </el-option>
                    <el-option label="table" value="table">
                      <svg-icon icon-class="table" />
                      tab
                    </el-option>
                    <el-option label="theme" value="theme">
                      <svg-icon icon-class="theme" />
                      theme
                    </el-option>
                    <el-option label="user" value="user">
                      <svg-icon icon-class="user" />
                      user
                    </el-option>
                    <el-option label="tree" value="tree">
                      <svg-icon icon-class="tree" />
                      tree
                    </el-option>
                    <el-option label="zip" value="zip">
                      <svg-icon icon-class="zip" />
                      zip
                    </el-option>
                    <el-option label="document" value="document">
                      <svg-icon icon-class="document" />
                      document
                    </el-option>
                    <el-option label="calendar" value="calendar">
                      <svg-icon icon-class="calendar" />
                      calendar
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="4" :sm="4" :lg="5">
                <el-form-item label="是否缓存" prop="noCache">
                  <el-switch v-model="form.noCache"></el-switch>
                </el-form-item>
              </el-col>
              <el-col :xs="4" :sm="4" :lg="5">
                <el-form-item label="是否可用" prop="isused">
                  <el-switch v-model="form.isused"></el-switch>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-footer class="el-footer">
            <el-button type="info" icon="el-icon-back" plain @click="backClick">返回</el-button>
            <el-button :loading="loading.btn" type="success" icon="el-icon-check" plain @click="saveClick">保存</el-button>
          </el-footer>
        </el-main>
      </el-scrollbar>
    </el-container>
  </div>
</template>

<script>
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: "edit-menu",
    props: {  // 接受父组件的传值
      onBack: {
        type: Function,
        default: null
      }
    },
    components: { Treeselect },
    data() {
      const checkCode = async (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入菜单编号'))
        } else {
          // 提交后台验证菜单编号是否存在
          const params = {
            className: 'Menu',
            method: 'isMenuCodeExisted',
            param: {
              id: this.form.id,
              code: this.form.code
            }
          };
          const res = await this.$store.dispatch('http/post', params);
          if (res === '203') {
            return callback(new Error('菜单编号已存在！'))
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
          parentid: '',
          parentname: '',
          component: 'Layout',
          path: '',
          url: '',
          icon: '',
          noCache: false,
          isused: true,
        },
        rules: {
          code: [
            { required: true, validator: checkCode, trigger: 'blur' }
          ],
          name: [
            { required: true, message: '请输入名称', trigger: 'blur' }
          ],
          // url: [
          //   { required: true, message: '请输入地址', trigger: 'blur' }
          // ],
          path: [
            { required: true, message: '请输入路径', trigger: 'blur' }
          ]
        },
        loading: {
          form: false,
          btn: false
        },
        options: [],
        normalizer(node) {  // 自定义下拉树节点模板
          return {
            id: node.id,
            label: node.name,
            children: JSON.stringify(node.children) === '[]' ? '' : node.children
          }
        }
      }
    },
    methods: {
      backClick() {
        if (this.onBack) {
          this.onBack(null)
        }
      },
      saveClick() {
        this.$refs.form.validate(valid => {
          if(valid) {
            // 提交后台
            this.loading.btn = true;
            this.$nextTick(async () => {
              const params = {
                className: 'Menu',
                method: 'ModifyMenu',
                param: this.form
              };
              const res = await this.$store.dispatch('http/post', params);
              if (res === '200') {
                this.$message({  showClose: true,  message: 'success',  type: 'success' });
                this.loading.btn = false;
                // 返回菜单信息页面
                setTimeout(() => {
                  if (this.onBack) {
                    this.onBack('succ')
                  }
                }, 1000)
              } else {
                this.$message({  showClose: true,  message: 'error',  type: 'error' });
                this.loading.btn = false;
              }
            })
          } else {
            return false;
          }
        });
      }
    },
    created() {
      this.$nextTick(async () => {
        this.loading.form = true;

        // 获取下拉菜单列表
        const params = {
          className: 'Menu',
          method: 'selectMenuByParentId',
          param: {
            id: '0'
          }
        };
        this.options = await this.$store.dispatch('http/post', params);

        // 等待菜单树完全加载完毕后，填充页面
        this.form = this.$store.state.db.get('selectedMenuData');
        if (this.form.parentid === '0') { // 没有上级机构时，页面显示为空
          this.form.parentid = null;
        }

        this.loading.form = false;
      });
    }
  }
</script>

<style scoped>
  .header{
    background-color: #f9f8fb;
    text-align: left;
    margin-bottom: 10px;
  }

  .headerbtn{
    text-align: right;
    line-height: 5;height: 12px;
  }
</style>
