<template>
  <div>
    <div>
      <!-- 主页面 -->
      <div>
        <div class="container">
          <div class="top">
            <div class="left">问题类型管理</div>
            <div class="right">
              <el-button type="success" icon="el-icon-plus" size="small" plain @click='addPicture'>添加</el-button>
            </div>
          </div>
          <div class="table">
            <el-table
              :data="firstTableData"
              stripe
              style="width: 100%;text-align: center;border:1px solid #bbc8c1">
              <el-table-column label="序号" type="index" align="center" ></el-table-column>
              <el-table-column label="名称" prop="name" align="center" ></el-table-column>
              <el-table-column label="创建时间" prop="createTime" align="center" ></el-table-column>
              <el-table-column label="启用状态" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.isused"
                    active-color="#00A854"
                    active-value="1"
                    inactive-color="#F04134"
                    inactive-value="0"
                    @change="changeSwitch(scope.row)">
                  </el-switch>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" fixed="right" width="200px">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="deleteQuestionType(scope.row)" plain >删除</el-button>
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
              :total="paginationData.total"
              style="margin-top: 20px"
              ali>
            </el-pagination>
          </div>
        </div>
      </div>


      <el-col :span="12">
        <el-dialog
          style="width:100%"
          :title="firstDialog.title"
          :visible.sync="firstDialog.visible"
          :modal-append-to-body="false"
          :append-to-body="true">


          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="类型名称" prop="name">
              <el-input v-model="ruleForm.name"></el-input>
            </el-form-item>

          </el-form>


          <div slot="footer" class="dialog-footer">
            <el-button :loading="loading" type="primary" @click="submitForm('ruleForm')">添加</el-button>
            <el-button style="margin-left:20px" @click="cancleSubmit('ruleForm')">取消</el-button>
          </div>
        </el-dialog>
      </el-col>
    </div>
  </div>
</template>

<script>
  export default {
    name:"questiontypeManage",
    data() {
      return {
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
          form: {}
        },
        loading: false,
        upSuc: false,

        ruleForm: {
          name: ''
        },

        rules: {
          name: [
            { required: true, message: '请输入类型名称', trigger: 'blur' }
          ],

        }
      };
    },

    // 钩子函数
    created() {
      this.getFirstTableData(); // 初始化页面时获取后台数据
    },

    // 方法
    methods: {
      openFullScreen2() {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });

        setTimeout(() => {
          loading.close();
        }, 2000);
      },
      // 获取图片列表
      getFirstTableData() {
        this.firstTableData = [];
        let data = {
          page: {
            pageIndex: this.paginationData.currentPage,
            pageSize: this.paginationData.pageSize,
          }
        };
        this.$nextTick(async ()=> {
          let params = {
            className: 'questionTypeService',
            method: 'selectQuestionTypeList',
            param: data
          };
          let res = await this.$store.dispatch('http/post', params);
          console.info("duuu",res)
          this.firstTableData = res.dataList

        });
      },
      // 初始化添加图片弹出框
      addPicture() {
        this.firstDialog.title = '添加问题类型';
        this.firstDialog.visible = true;
      },

      changeSwitch(param){
        console.log(param)
        const loading = this.$loading({lock: true, text: 'Loading', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)'});
        this.$nextTick(async ()=> {
          let params = {
            className: 'questionTypeService',
            method: 'updateQuestionStatus',
            param: {
              id:param.id,
              isused:param.isused
            }
          };
          console.log(params)
          let res = await this.$store.dispatch('http/post', params);
          console.log(res)
          if(res.code == 200){
            this.$message({message: '操作成功', type: 'success'});
            this.getFirstTableData()
          }else{
            this.$message({message: res.data, type: 'error'});
          }
          loading.close();
        });
    },

  deleteQuestionType(param){
        const loading = this.$loading({lock: true, text: 'Loading', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)'});

        this.$nextTick(async ()=> {
          let params = {
            className: 'questionTypeService',
            method: 'deleteQuestionType',
            param: {
              id:param.id
            }
          };
          console.log(params)
          let res = await this.$store.dispatch('http/post', params);
          console.log(res)
          if(res.code == 200){
            this.$message({message: '删除成功', type: 'success'});
            this.getFirstTableData()
          }else{
            this.$message({message: res.data, type: 'error'});
          }
          loading.close();
        });
      },

      submitForm(formName) {
        const loading = this.$loading({lock: true, text: 'Loading', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)'});

        this.$refs[formName].validate((valid) => {
          if (valid) {

            this.$nextTick(async ()=> {
              let params = {
                className: 'questionTypeService',
                method: 'createQuestiontype',
                param: {
                  name:this.ruleForm.name
                }
              };
              console.log(params)
              let res = await this.$store.dispatch('http/post', params);
              console.log(res)
              if(res.code == 200){
                this.$message({message: '添加成功', type: 'success'});
                this.firstDialog.visible = false;
                this.$refs[formName].resetFields();
                this.getFirstTableData()
              }else{
                this.$message({message: res.data, type: 'error'});
              }
              loading.close();
            });

          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },

      cancleSubmit(formName){
        this.firstDialog.visible = false;
        this.$refs[formName].resetFields();
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
  /*$dark_gray:#889aa4;*/
  .right{
    float: right;
    margin-top: -7px;
  }
  .left{
    float: left;
    border-left:4px solid #6ba7bd;
    padding-left: 3px;
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
  .dialog-footer{
    text-align: center;
  }
</style>
