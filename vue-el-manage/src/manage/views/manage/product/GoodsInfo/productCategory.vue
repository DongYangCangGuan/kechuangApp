<template>
  <div>
    <el-header class="header">
      <el-row class="headerbtn">
        <span style="float:left;color:#808080">商品类别</span>
        <el-button type="primary" icon="el-icon-plus" plain @click="Add">新增</el-button>
        <el-button type="info" icon="el-icon-edit" :disabled="deleteShow" plain @click="Modify">修改</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="deleteShow" plain @click="DeleteClick">删除</el-button>
      </el-row>
    </el-header>
    <el-container>
      <el-scrollbar style="width:100%">
        <el-main>
          <el-form ref="form" :model="form" label-width="100px">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="form.name" :readonly=true placeholder="分类名称"></el-input>
            </el-form-item>
            <el-form-item label="商品分类级别" prop="catClass">
              <el-select v-model="form.catClass" filterable placeholder="商品分类级别">
              <el-option
                v-for="item in catClassData"  :key="item.id" disabled 
                :value="item.id"
                :label="item.name">
              </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="上级父节点" prop="parentName">
              <el-input v-model="form.parentName" :readonly=true placeholder="上级父节点"></el-input>
            </el-form-item>
            <el-form-item label="是否启用"  prop="used">
              <el-switch v-model="form.used"  active-color="#13ce66" inactive-color="#ff4949" disabled></el-switch>
            </el-form-item>
             <el-form-item label="分类图片" prop="pic">
              <el-image
                 style="width: 100px; height: 100px"
                :src="form.pic">
              </el-image>
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
    // 接受父组件的传值
    props: {
      onAdd: {
        type: Function,
        default: null
      },
      onModify: {
        type: Function,
        default: null
      }
    },
    name:'product-Category',
    data() {
      return {
        form:{
          id:'',
          name:'',
          catClass:'',
          parentname:'',
          used:false,
          pic:''
        },
        deleteShow:true,
        categoryId:[],
        catClassData:[{id:0,name:'一级分类'},{id:1,name:'二级分类'},{id:2,name:'三级分类'}],
      }
    },
    methods: {
      Add:function(){
        if (this.onAdd) {
          this.onAdd(null)
        }
      },
      Modify: function(){
        if (this.onModify) {
          this.onModify(null)
        }
      },
      handleNodeClick(data) {
        this.$nextTick(async ()=> {
          let para={
              id:data.id,
          };
          let params = {
            className:'productManage',
            method:'queryCategoryById',
            param:JSON.stringify(para)
          };
          let treeNode = await this.$store.dispatch('http/post',params);
          this.form = treeNode.category[0];
          this.deleteShow=false;
          console.info("11111111",this.form);
          this.$store.dispatch('db/addStore', {key: "selectedDeptData", value: treeNode.category[0]});
          //console.log('111', this.$store.state.db.get('selectedDeptData'))
        })
      },
      DeleteClick(){
        this.categoryId=[];
      this.$confirm('此操作将永久删除 ' + this.form.name + ', 是否继续?', '提示', { type: 'warning' })
        .then(() => { // 向请求服务端删除
          this.categoryId.push(this.form.id)
          this.$nextTick(async ()=> {
          let para={
             ids:this.categoryId
          };
          let params = {
            className:'productManage',
            method:'deleteCategories',
            param:JSON.stringify(para)
          };
          let treeNode = await this.$store.dispatch('http/post',params);
           this.$message.info(treeNode);
         })
        }) .catch(() => {
        this.$message.info('已取消操作!');
      });
    },
    },
    // 钩子函数，处理左侧机构树节点选中
    created (){
      bus.$on('goods-select-node', this.handleNodeClick)
    },
    beforeDestroy () {
      bus.$off('goods-select-node', this.handleNodeClick)
      this.$store.dispatch('db/addStore', {key: "selectedDeptData", value: ''});
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
    line-height: 5;
    height: 12px;
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
