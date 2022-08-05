<template>
  <div>
    <div>
      <!-- 主页面 -->
      <div>
        <div class="container">
          <div class="top">
            <div class="left">详细信息</div>
            <div class="right">
              <el-button type="success" icon="el-icon-plus" size="small" plain @click='addPicture'>添加</el-button>
            </div>
          </div>
          <div class="table">
            <el-table
              :data="firstTableData"
              stripe
              style="width: 100%;text-align: center;border:1px solid #bbc8c1">
              <el-table-column label="序号" type="index" align="center" width="50px"></el-table-column>
              <el-table-column label="名称" prop="picName" align="center" width="350px"></el-table-column>
              <el-table-column label="图片" align="center">
                <template slot-scope="scope">
                  <el-popover
                    placement="right"
                    trigger="hover">
                    <img :src="scope.row.picUrl" style="height: 250px;width: 493px;"/>
                    <img slot="reference" :src="scope.row.picUrl" alt="" style="height: 50px;width: 99px;">
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column label="创建者" prop="creatorName" align="center" width="200px"></el-table-column>
              <el-table-column label="创建时间" prop="createTime" align="center" width="200px"></el-table-column>
              <el-table-column label="操作" align="center" fixed="right" width="200px">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" plain @click="deletePictureBaseMsg(scope.row)">删除</el-button>
                </template>
              </el-table-column>
              <el-table-column label="启用状态" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.isUsed"
                    active-color="#00A854"
                    active-value="1"
                    inactive-color="#F04134"
                    inactive-value="0"
                    @change="changeSwitch(scope.row)">
                  </el-switch>
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

      <!--图片添加页面-->
      <el-col :span="12">
        <el-dialog
          :width="'40%'"
          :title="firstDialog.title"
          :visible.sync="firstDialog.visible"
          @close="closeDialog"
          :modal-append-to-body="false"
          :append-to-body="true">
          <el-form :model="firstDialog.form" ref='firstDialogForm' status-icon label-width="100px">
            <el-form-item label="请选择要上传的图片" label-width="150px">
              <el-upload
                ref="upload"
                action="https://jsonplaceholder.typicode.com/posts/"
                class="avatar-uploader"
                list-type="picture-card"
                :limit=1
                :http-request="myUpload"
                :before-upload="beforeAvatarUpload"
                :on-remove="OnRemove">
                <i class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="是否启用" label-width="150px">
              <el-switch v-model="firstDialog.form.isUsed" active-color="#13ce66"  inactive-color="#ff4949"></el-switch>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button :loading="loading" type="primary" @click.native.prevent="submitPicture('firstDialogForm')">保存</el-button>
            <el-button @click="cancelDialog('firstDialogForm')" style="margin-left:20px">取消</el-button>
          </div>
        </el-dialog>
      </el-col>
    </div>
  </div>
</template>

<script>
  export default {
    name:"pictureManage",
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
        upSuc: false
      };
    },

    // 钩子函数
    created() {
      this.getFirstTableData(); // 初始化页面时获取后台数据
    },

    // 方法
    methods: {
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
          let params = { className: 'Picture', method: 'getPictureInfo', param: data };
          let res = await this.$store.dispatch('http/post', params);

          if(res.dataList != null && res.dataList.length > 0){
            console.log("TableData",res.dataList);
            this.getPic(res.dataList);
            this.paginationData.total = res.page.totalNum;
          }

        });
      },
      // 初始化添加图片弹出框
      addPicture() {
        this.firstDialog.title = '';
        this.firstDialog.visible = true;
        this.firstDialog.form = {
          id: '',
          isUsed: false,
          picUrl: '',
        };
      },

      // 保存图片
      submitPicture(formName) {
        //清除图片缓存
        this.$refs.upload.clearFiles();
        //重新上传
        this.upSuc = false;

        this.loading = true;
        this.$nextTick(async ()=> {
          let picUrl = this.firstDialog.form.picUrl;
          let subName = picUrl.substring(picUrl.lastIndexOf("/")+1);

          let params = {
            className: 'Picture',
            method: 'addPicture',
            param: {
              "id": this.firstDialog.form.id,
              "picName": subName,
              "isUsed": this.firstDialog.form.isUsed,
              "picUrl": this.firstDialog.form.picUrl,
              "creatorId": this.$store.state.user.entity.id,
            }
          };

          if (picUrl != null && picUrl != ''){
            let res = await this.$store.dispatch('http/post', params);
            this.firstDialog.visible = false;
            if (res == '200') {
              this.upSuc = true;
              this.$message({ showClose: true, message: 'success', type: 'success'});
            } else {
              this.$message({ showClose: true, message: 'error', type: 'error'});
            }
            //重新加载
            this.getFirstTableData();
            this.loading = false;
          } else {
            //未选择图片
            this.$message({
              message: '请选择要上传的图片!',
              type: 'error',
              duration: 2500
            });
            //过渡数据加载时间
            this.loading = false;
            return false;
          }
        });

      },
      // 删除图片
      deletePictureBaseMsg(data) {
        let isUsed = data.isUsed;
        if(isUsed === "1"){
          this.$message({ message: '该图片已使用，禁止删除！', type: 'warning' });
          return false;
        }
        this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        }).then(() => {
          this.$nextTick(async ()=> {
            let params = {
              className: 'Picture',
              method: 'deletePicture',
              param: {
                "id": data.id
              }
            };
            let res = await this.$store.dispatch('http/post', params);
            if (res == '203') {
              this.$message({ message: '删除失败！', type: 'warning' });
            } else if (res == '200') {
              this.$message({ message: '删除成功！', type: 'success' });
            }
            // 重新加载页面数据
            this.getFirstTableData();
          });
        }).catch(() => {
          this.$message({ type: 'info', message: '已取消' });
        });
      },
      //删除钩子函数
      OnRemove(file,fileList) {
        this.$nextTick(async ()=> {
            let params = {
              className: 'Picture',
              method: 'cancelUpload',
              param: {
                "picUrl": this.firstDialog.form.picUrl
              }
            };
            let res = await this.$store.dispatch('http/post', params);

            this.firstDialog.form.picUrl = "";
        });
      },
      //图片上传
      myUpload(content) {
        this.$nextTick(async () => {
          let fd = new FormData();
          fd.append('file', content.file);
          let params = {param: fd, url: process.env.BASE_API + '/api/file/fileupload'};
          let response = await this.$store.dispatch('http/fileUpload', params);
          if (response) {
            this.firstDialog.form.picUrl = response;
            // this.$message({
            //   message: '上传成功'
            // });
          } else {
            this.$refs.upload.clearFiles();

            this.$message({
              message: '上传失败'
            });
          }
        })
      },
      //图片下载
      getPic(tableData) {
        let base = this;
        tableData.forEach(function (item) {
          base.$nextTick(async () => {
            let picUrl = {param: item.picUrl, url: '/api/file/downloadfile'};
            //先清除Url
            item.picUrl = "";
            //下载Image
            let res = await base.$store.dispatch('http/fileDownload', picUrl);
            if (res != "" && res != null) {
              let path = "data:image/png;base64," + res;
              //再重新赋值
              item.picUrl = path;
            }
          });
        });

        this.firstTableData = tableData;
      },
      //上传钩子函数
      beforeAvatarUpload(file) {
        const nameSuffix = (file.name.split('.')[file.name.split('.').length - 1]).toLowerCase();
        const extension1 = nameSuffix === 'jpg';
        const extension2 = nameSuffix === 'png';
        const extension3 = nameSuffix === 'gif';
        console.log("nameSuffix",nameSuffix);
        const isLt4M = file.size / 1024 / 1024 < 4;

        if (!extension1 && !extension2 && !extension3) {
          this.$message({
            message: '上传的图片只能是jpg、png或gif格式！',
            type: 'error',
            duration: 2500
          });
          this.$refs.upload.clearFiles();
        }

        if (!isLt4M) {
          this.$message({
            message: '上传的图片大小不能超过4MB!',
            type: 'error',
            duration: 2500
          });
          this.$refs.upload.clearFiles();
        }
        return extension1 || extension2 || extension3 && isLt4M
      },

      //改变图片状态
      changeSwitch(data) {
        this.$nextTick(async ()=> {
          let params = {
            className: 'Picture',
            method: 'changePicStatus',
            param: {
              "id": data.id,
              "isUsed": data.isUsed
            }
          };

          let res = await this.$store.dispatch('http/post', params);

          if (res == '200') {
            this.$message({ showClose: true, message: 'success', type: 'success'});
          } else {
            this.$message({ showClose: true, message: 'error', type: 'error'});
          }
        });
      },

      // 弹出框取消按钮
      cancelDialog(formName) {
        this.firstDialog.visible = false;

        this.$nextTick(async ()=> {
            let params = {
              className: 'Picture',
              method: 'cancelUpload',
              param: {
                "picUrl": this.firstDialog.form.picUrl
              }
            };
            let res = await this.$store.dispatch('http/post', params);

            // if (res == '203') {
            //   this.$message({ message: '取消失败！', type: 'warning' });
            // } else if (res == '200') {
            //   this.$message({ message: '取消成功！', type: 'success' });
            // }
            this.loading = false;
            //清除图片缓存
            this.$refs.upload.clearFiles();
        })
      },
      // 弹出框关闭按钮触发
      closeDialog() {
        // this.$refs['firstDialogForm'].clearValidate();
        if(!this.upSuc ){
          this.$nextTick(async ()=> {
            let params = {
              className: 'Picture',
              method: 'cancelUpload',
              param: {
                "picUrl": this.firstDialog.form.picUrl
              }
            };
            let res = await this.$store.dispatch('http/post', params);

            //清除图片缓存
            this.$refs.upload.clearFiles();
            this.firstDialog.form.picUrl = "";
          });
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
