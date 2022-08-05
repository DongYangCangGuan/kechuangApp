<template>
  <div>
    <!-- 主页面 -->
    <div>
      <div class="selectBox">
        角色编码:
        <el-input placeholder="请输角色编码" v-model="searchData.code" clearable size='small' style='width: 150px'></el-input>
        角色名称:
        <el-input placeholder="请输角色名称" v-model="searchData.name" clearable size='small' style='width: 150px'></el-input>
        <el-button size='small' type="primary" @click="getFirstTableData">查询</el-button>
      </div>
      <div class="container">
        <div class="top">
          <div class="left">角色基本信息</div>
          <div class="right">
            <el-button type="success" icon="el-icon-plus" size="small" plain @click='addRole'>添加</el-button>
          </div>
        </div>
        <div class="table">
          <el-table
            :data="firstTableData"
            stripe
            style="width: 100%;text-align: center;border:1px solid #bbc8c1">
            <el-table-column label="序号" type="index" width="50"></el-table-column>
            <el-table-column label="角色编号" prop="code" align="center"></el-table-column>
            <el-table-column label="角色名称" prop="name" align="center"></el-table-column>
            <el-table-column label="所属域" prop="area" align="center"></el-table-column>
            <el-table-column label="是否有效" align="center">
              <template slot-scope="scope">
                {{ scope.row.isused ? "是" : "否" }}
              </template>
            </el-table-column>
            <el-table-column label="修改人" prop="username" align="center"></el-table-column>
            <el-table-column label="修改时间" prop="createtime" align="center" width="170"></el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="340">
              <template slot-scope="scope">
                <el-button size="mini" type="info" plain @click="editRole(scope.row)">编辑</el-button>
                <el-button size="mini" type="primary" plain @click="assignMenus(scope.row)">分配菜单</el-button>
                <el-button size="mini" type="primary" plain @click="assignUsers(scope.row)">分配用户</el-button>
                <el-button size="mini" type="danger" plain @click="deleteRoleBaseMsg(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="paginationData.currentPage"
            :page-sizes="[10,20,30,40,50]"
            :page-size="paginationData.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="paginationData.total">
          </el-pagination>
        </div>
      </div>
    </div>

    <!--角色添加页面-->
    <el-col :span="12">
        <el-dialog
          :title="firstDialog.title"
          :visible.sync="firstDialog.visible"
          @close="closeDialog"
          :modal-append-to-body="false"
          :append-to-body="true">
          <el-form :model="firstDialog.form" :rules='rules' ref='firstDialogForm' status-icon label-width="100px">
            <el-form-item label="角色编码" prop="code">
                <el-input name="code"  v-model="firstDialog.form.code" placeholder="请输入角色编码" clearable></el-input>
            </el-form-item>
            <el-form-item label="角色名称" prop="name">
                <el-input name="name"  v-model="firstDialog.form.name" placeholder="请输入角色名称" clearable></el-input>
            </el-form-item>
            <el-form-item label="所属域">
              <el-select name="area"  v-model="firstDialog.form.area" placeholder="请选择">
                <el-option v-for="item in firstDialog.form.options" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="启用">
              <el-switch v-model="firstDialog.form.isused" active-color="#13ce66" inactive-color="#ff4949" @change="isUsed($event,firstDialog.form.code)"></el-switch>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button :loading="loading" type="primary" @click.native.prevent="submitRole('firstDialogForm')">保存</el-button>
            <el-button @click="cancelDialog('firstDialogForm')">取消</el-button>
          </div>
        </el-dialog>
    </el-col>
  </div>
</template>

<script>
  export default {
    name:"role-Manage",
    props: {
      //子页面调用父页面
      onChangeTree: {
        type: Function,
        default: null
      },
      onChangeStaff: {
        type: Function,
        default: null
      },
    },
    data() {
      // 角色新增时校验角色编号是否已经存在
      let checkCode = async (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入角色编码'))
        } else {
          // 提交后台验证角色编码是否存在
          const params = {
            className: 'Role',
            method: 'checkRoleCode',
            param: {
              id: this.firstDialog.form.id,
              code: this.firstDialog.form.code
            }
          };
          const res = await this.$store.dispatch('http/post', params);
          if (res === '203') {
            return callback(new Error('角色编号已存在！'))
          } else {
            return callback()
          }
        }
      };
      // 角色新增时校验角色名称是否已经存在
      let checkName = async (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入角色名称'))
        } else {
          // 提交后台验证角色名称是否存在
          const params = {
            className: 'Role',
            method: 'checkRoleName',
            param: {
              id: this.firstDialog.form.id,
              name: this.firstDialog.form.name
            }
          };
          const res = await this.$store.dispatch('http/post', params);
          if (res === '203') {
            return callback(new Error('角色名称已存在！'))
          } else {
            return callback()
          }
        }
      };
      return {
        // 搜索对象
        searchData: {
          code: '',
          name: ''
        },
        firstTableData: [],
        paginationData: {
          // 当前页
          currentPage: 1,
          // 当前每页显示条数
          pageSize: 10,
          // 总条目数
          total: 0
        },
        firstDialog: {
          title: '',
          visible: false,
          type: '',
          form: {}
        },
        rules: {
          code: [{ required: true, validator: checkCode, trigger: 'blur' }],
          name: [{ required: true, validator: checkName, trigger: 'blur' }]
        },
        loading: false,
      };
    },

    // 钩子函数
    created() {
      this.getFirstTableData(); // 页面创建时获取后台数据
    },

    // 方法
    methods: {
      //Author:DBJ  判断该角色下是否有人员
      isUsed($event,val){
        console.log(val)
        this.$nextTick(async ()=> {
          let params = { className: 'Role', method: 'isRoleUsed', param: {code:val} };
          let res = await this.$store.dispatch('http/post', params);
          console.log(res)
          if(res>0){
            this.firstDialog.form.isused = true;
            this.$message({ message: '该角色已经使用，禁止停用！', type: 'warning' });
          }else {
            ~ this.firstDialog.form.isused ;
            this.$message({  showClose: true,  message: 'success',  type: 'success' });
          }
        });

      },
      // 获取角色列表
      getFirstTableData() {
        this.firstTableData = [];
        let data = {
          page: {
            pageIndex: this.paginationData.currentPage,
            pageSize: this.paginationData.pageSize,
          },
          searchdata: {
            code: this.searchData.code,
            name: this.searchData.name
          },
        };
        this.$nextTick(async ()=> {
          let params = { className: 'Role', method: 'getRole', param: data };
          let res = await this.$store.dispatch('http/post', params);
          this.firstTableData = res.dataList;
          this.paginationData.total = res.page.totalNum;
        });
      },
      // 初始化添加角色弹出框
      addRole() {
        this.firstDialog.title = '添加角色';
        this.firstDialog.visible = true;
        this.firstDialog.type = 'add';
        this.firstDialog.form = {
          id: '',
          code: '',
          name: '',
          area: '',
          isused: true,
          options: [
            { value: 'user', label: '普通用户组' },
            { value: 'admin', label: '管理员组' }
          ],
        };
      },
      // 初始化修改角色弹出框
      editRole(row) {
        this.firstDialog.title = '编辑角色';
        this.firstDialog.visible = true;
        this.firstDialog.type = 'edit';
        this.firstDialog.form = {
          id: row.id,
          name: row.name,
          code: row.code,
          area: row.area,
          isused: row.isused,
          options: [
            { value: 'user', label: '普通用户组' },
            { value: 'admin', label: '管理员组' }
          ],
        };
      },
      // 保存角色
      submitRole(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.loading = true;
            if (this.firstDialog.type === "add") {
              let data = this.firstDialog.form;
              this.$nextTick(async ()=> {
                let params = { className: 'Role', method: 'addRole', param: data };
                let res = await this.$store.dispatch('http/post', params);
                this.$refs[formName].clearValidate();
                this.firstDialog.visible = false;
                if (res == '200') {
                  this.$message({  showClose: true,  message: 'success',  type: 'success' });
                } else {
                  this.$message({  showClose: true,  message: 'error',  type: 'error' });
                }
                this.getFirstTableData();
                this.loading = false;
              });
            } else if (this.firstDialog.type === 'edit') {
              let data = this.firstDialog.form;
              this.$nextTick(async ()=> {
                let params = { className: 'Role', method: 'updateRole', param:data };
                let res = await this.$store.dispatch('http/post', params);
                this.$refs[formName].clearValidate();
                this.firstDialog.visible = false;
                if (res == '200') {
                  this.$message({  showClose: true,  message: 'success',  type: 'success' });
                } else {
                  this.$message({  showClose: true,  message: 'error',  type: 'error' });
                }
                this.getFirstTableData();
                this.loading = false;
              });
            }
          } else {
            return false;
          }
        });
      },
      // 删除角色
      deleteRoleBaseMsg(roleId) {
        this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        }).then(() => {
          this.$nextTick(async ()=> {
            let params = { className: 'Role', method: 'deleteRole', param: {id: roleId} };
            let res = await this.$store.dispatch('http/post', params);
            if (res == '203') {
              this.$message({ message: '该角色已经使用，禁止删除！', type: 'warning' });
            } else if (res == '200') {
              this.$message({ message: '删除成功', type: 'success' });
            }
            // 重新加载页面数据
            this.getFirstTableData();
          });
        }).catch(() => {
          this.$message({ type: 'info', message: '已取消删除' });
        });
      },
      // 弹出框取消按钮
      cancelDialog(formName) {
        this.$refs[formName].clearValidate();
        this.firstDialog.visible = false;
      },
      // 弹出框关闭按钮触发
      closeDialog() {
        this.$refs['firstDialogForm'].clearValidate();
      },
      // 分配菜单
      assignMenus(row) {
        let roleId = row.id;
        if (this.onChangeTree) {
          this.onChangeTree(roleId)
        }
      },
      // 分配用户
      assignUsers(row) {
        let roleId = row.id;
        if (this.onChangeStaff) {
          this.onChangeStaff(roleId)
        }
      },
      // 每页总条数发生改变
      handleSizeChange(val) {
        this.paginationData.pageSize = val;
        this.getFirstTableData();
      },
      // 页数发生改变
      handleCurrentChange(val) {
        this.paginationData.currentPage = val;
        this.getFirstTableData();
      }
    }
  };
</script>


<style lang="scss" scoped>
  $dark_gray:#889aa4;

  .right{
    float: right;
    margin-top: -7px;
  }
  .left{
    float: left;
    border-left:4px solid #6ba7bd;
    padding-left: 3px;
  }
  .page{
    float: right
  }
  .table{
    width: 100%;
    padding: 1px 22px 15px 22px;
  }
  .top{
    width: 100%;
    height: 20px;
    padding: 0px 22px;
    margin: 15px 0px;
  }
  .container{
    border:1px solid #bbc8c1;
    height: 100%;
    width: 100%
  }
  .selectBox{
    margin:0px 22px 15px 22px
  }
  .assignUser{
    margin:15px 22px;
  }

  .svg-container {
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    margin-left: -49px;
    display: inline-block;
  }
</style>

