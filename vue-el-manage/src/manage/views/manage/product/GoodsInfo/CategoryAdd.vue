<template>
  <div>
    <el-header class="header">
      <el-row class="headerbtn">
        <span style="float:left;color:#808080">新增类别</span>
        <el-button :loading="loading" type="primary" icon="el-icon-plus" @click="addInfoClick">添加</el-button>
      </el-row>
    </el-header>
    <el-container>
      <el-scrollbar style="width:100%">
        <el-main>
          <el-form ref="form" :model="form" label-width="100px">
            <el-form-item label="分类名称" prop="categoryName"
             :rules="[
             {required:true,trigger:'blur',validator:validatorName}]">
              <el-input v-model="form.categoryName"  placeholder="分类名称"></el-input>
            </el-form-item>
            <el-form-item label="商品分类级别" prop="catClass">
              <el-select v-model="form.catClass" filterable placeholder="商品分类级别">
              <el-option
                v-for="item in catClassData" :key="item.id"
                :value="item.id"
                :label="item.name">
              </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="上级父节点">
              <treeselect
                v-model="form.parentId"
                :load-options="loadOptions"
                :options="SelectTreeData"
                 placeholder="上级机构"
                :searchable="false"
                :clearable="false"
                :normalizer="normalizer"/>
            </el-form-item>
            <el-form-item label="是否启用"  prop="used">
              <el-switch v-model="form.used"  active-color="#13ce66" inactive-color="#ff4949" ></el-switch>
            </el-form-item>
            <el-form-item label="分类图片" prop="pic">
              <el-upload
                ref="upload"
                class="avatar-uploader"
                action="https://jsonplaceholder.typicode.com/posts/"
                :on-remove="OnRemove"
                list-type="picture-card"
                :limit=1
                :file-list="filelist"
                :http-request="myUpload"
                :before-upload="beforeAvatarUpload">
                <i class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-main>
      </el-scrollbar>
    </el-container>
  </div>
</template>
<script>
  import bus from '@COM/utils/bus';
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  export default {
    // 接受父组件的传值
    props: {
      onBack: {
        type: Function,
        default: null
      }
    },
    components: { Treeselect },
    name:'Category-Add',
    data() {
      return {
        filelist:[],
        form:{
          categoryName:'',
          catClass:0,
          parentId:'',
          parentName:'',
          used:true,
          isUsed:'',
          pic:''
        },
        loading: false,
        SelectTreeData:[],
        catClassData:[{id:0,name:'一级分类'},{id:1,name:'二级分类'},{id:2,name:'三级分类'}],
        treeShow:false,
        normalizer(node) {  // 自定义下拉树节点模板
          return {
            id:node.id,
            label:node.name,
            children: JSON.stringify(node.children) === '[]' ? '' : node.children // 当选中节点展开时没有子节点，设置其子节点为''
          }
        }
      }
    },
    methods: {
     backClick: function () {
     if(this.onBack) {
        this.onBack(null)
      }
     },
     // 动态加载子树
      async loadOptions({ action, parentNode, callback }) {
        if (action === 'LOAD_CHILDREN_OPTIONS') {
          const params = {
           className:'productManage',
           method:'categoriesParent',
            param: {
               parentId:parentNode.id,
               treeAbout:parentNode.treeAbout
            }
          };
          parentNode.children = await this.$store.dispatch('http/post', params);
          // 回调
          callback()
        }
     },
     validatorName(rule, value, callback){
      if (value.length < 1) {
        callback(new Error('请输入分类名称'))
      }else{
        const re = /^[\u0391-\uFFE5A-Za-z0-9]+$/;
        const rsCheck = re.test(value);
        if (!rsCheck) {
          callback(new Error('请输入有效字符'));
        } else {
          callback();
        }
      }
     },
     //删除上传头像
      OnRemove(){
        this.form.pic="";
      },
      //覆盖默认的上传行为
      myUpload(content){
        this.$nextTick(async () => {
          let fd=new FormData();
          fd.append('file',content.file);
          let params = { param: fd ,url:'office/api/file/fileupload'};
          let response = await this.$store.dispatch('http/fileUpload', params);
          if(response){
            console.log('pic:',response);
            this.form.pic=response;
            this.$message({
              message: '上传成功'
            });
          }else{
            this.$message({
              message: '上传失败'
            });
          }
        })
      },
      //上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
      beforeAvatarUpload(file) {
        const extension = file.name.split('.')[file.name.split('.').length-1] === 'jpg'
        const extension2 = file.name.split('.')[file.name.split('.').length-1] === 'png'
        const isLt2M = file.size / 1024 / 1024 < 10
        if (!extension && !extension2 && !extension3) {// && !extension4 && !extension5) {
          this.$message({
            message: '上传图片只能是 jpg、png格式!',
            type: 'error',
            duration: 2500
          });
          this.$refs.upload.clearFiles();
        }
        if (!isLt2M) {
          this.$message({
            message: '上传模板大小不能超过 10MB!',
            type: 'error',
            duration: 2500
          });
          this.$refs.upload.clearFiles();
        }
        return extension || extension2 || extension3 ||extension4 || extension5&& isLt2M
      },
      treetest:function(){
        let data = this.$store.state.db.get('selectedDeptData');
        console.log('123',data);
        this.form.parentId = undefined;
        this.form.parentName = undefined;
        if(data == ''||data == null){
          this.treeShow = true;
        }
      },
     //确定按钮
     addInfoClick: function () {
        if(this.form.used==true){
          this.form.isUsed="1"
        }else{
          this.form.isUsed="0"
        }
        this.$refs.form.validate(valid =>{
        if(valid) {
        this.loading = true;
        this.$nextTick(async () => {
        let method=null
        if(this.form.parentId==undefined){
          method='addRootCategory';
        }else{
          method='addSubCategory';
        }
        let params = {className:'productManage', method:method, param:this.form};
        let repose = await this.$store.dispatch('http/post', params);
        if(repose==='添加成功'){
          this.$message({showClose: true,message: '保存成功！',type: 'success'});
          this.loading = false;
          setTimeout(() => {
            if (this.onBack){
              this.onBack('succ')
            }
          }, 500);
        }else{
          this.$message({
           showClose: true,
            message: '保存失败！',
            type: 'error'
          });
           this.loading = false;
          }
        })
        }else{
          return false;
        }
      })
    },
    },
    created (){
      this.$nextTick(async ()=> {
      let para={};
      let params = {
        className:"productManage",
        method:"categories",
        param: JSON.stringify(para)};
      let response = await this.$store.dispatch('http/post', params);
         this.SelectTreeData=response
      });
      this.treetest();
    },
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
  .el-dropdown {
    vertical-align: top;
  }
  .el-dropdown + .el-dropdown {
    margin-left: 15px;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
