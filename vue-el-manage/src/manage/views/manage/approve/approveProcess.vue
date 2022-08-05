<template>
  <div>
    <el-container>
      <el-header class="header">
        <el-row class="headerBtn">

          <el-select v-model="processName" clearable placeholder="审批流程">
            <el-option
              v-for="item in approveOptions"
              :key="item.value"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
          <el-select v-model="roleName" clearable placeholder="角色">
            <el-option v-for="item in roleOptions"
                       :key="item.value"
                       :label="item"
                       :value="item">
            </el-option>
          </el-select>
          <span class="black">
            关键词:
          </span>
          <el-input v-model='keyWord' :maxlength="100" style="width:200px" clearable></el-input>
          <el-button type="primary" class="btnCss" @click="getDictionary">搜索</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="addShow" plain >新增</el-button>
        </el-row>
      </el-header>
      <el-main>
        <el-table :data="tableData" style="width: 100%">
          <el-table-column label=" " type="index"></el-table-column>
          <el-table-column prop="processName" label="流程名称"></el-table-column>
          <el-table-column prop="nodeName" label="节点名称"></el-table-column>
          <el-table-column prop="roleName" label="角色名称"></el-table-column>
          <el-table-column prop="modifierName" label="修改人"></el-table-column>
          <el-table-column prop="modifyTime" label="修改时间"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <div class="el-icon-view" style="color: #f17d05;font-weight: bold;" @click="read(scope.row.id)">查看</div>
              <div class="el-icon-edit" style="color: #7b4b02;font-weight: bold;"  @click="editor(scope.row.id)">编辑</div>
              <div class="el-icon-delete" style="color: #ff0000;font-weight: bold;" @click="deleteNode(scope.row.id)">删除</div>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer class="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page = '1'
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
          :total='this.listQuery.totalNum'>
        </el-pagination>
      </el-footer>
    </el-container>

      <el-dialog :title="addNode.title" @close="closeDialog('addNodeForm')" :visible.sync="addNode.visible" :modal-append-to-body="false" :append-to-body="true">
        <el-form :model="addNodeForm" :rules="rules" ref='addNodeForm' status-icon label-width="100px">
          <el-form-item label="选择流程" prop="processType">
            <el-select v-model="addNodeForm.processType" :disabled='showOr2==1?false:true' placeholder="请选择流程">
              <el-option v-for="item in processValueOptions" :key="item.value" :label="item.processName" :value="item.processType"></el-option>
            </el-select>
          </el-form-item>
          <span>节点:</span>
          <el-form-item label="节点名称" prop="nodeName">
            <el-input v-model="addNodeForm.nodeName" placeholder="请输入节点名称" :disabled='showOr==1?false:true' clearable></el-input>
          </el-form-item>
          <el-form-item label="节点索引" prop="nodeIndex">
            <el-input v-model="addNodeForm.nodeIndex" placeholder="请输入节点索引" :disabled='showOr==1?false:true' clearable></el-input>
          </el-form-item>
          <el-form-item label="角色编号" prop="roleCode">
            <el-input v-model="addNodeForm.roleCode" placeholder="请输入角色编号" :disabled='showOr==1?false:true' clearable></el-input>
          </el-form-item>
          <el-form-item label="" prop="resource">
            <el-radio-group v-model="addNodeForm.orgLevel">
              <el-radio :label=0 :disabled='showOr==1?false:true'>发起机构审批</el-radio>
              <el-radio :label=1 :disabled='showOr==1?false:true'>上级机构审批</el-radio>
            </el-radio-group>
          </el-form-item>
            <el-button class="dialog_btn" :loading="loading" type="primary" @click="save"  v-if="saveBtn1">保存</el-button>
            <el-button class="dialog_btn" :loading="loading" type="primary" @click="editorSave" v-if="saveBtn2">保存编辑</el-button>

        </el-form>

      </el-dialog>


  </div>
</template>

<script>
  //import $http from '@H'

  export default {
    name: "approveProcess",
    data() {
      return {
        addNode:{
          title: '采购订单流程',
          visible: false,
        },
        showOr:0,
        showOr2:0,
        processType:'',
        nodeName:'',
        nodeIndex:'',
        roleCode:'',
        processValue:'',
        listQuery:{
          pageIndex:1,
          pageSize:10,
          totalNum:0,
        },
        processName:'',
        processName1:'',
        keyWord:'',
        total:0,
        showRadio:true,
        // newShow:false,
        saveBtn1:true,
        saveBtn2:true,
        oldShow:true,
        saveBtn:true,
        radio:'1',
        orgLevel:'',
        loading:false,
        currentPage:'1',
        pageNum:'',
        processId:'',
        roleName:'',
        processValueOptions:[],
        tableData:[],
        approveOptions:[],
        roleOptions:[],
        addNodeForm:{
          processType:'',
          processName1:'',
          nodeName:'',
          nodeIndex:'',
          roleCode:'',
          orgLevel:''
        },
        rules:{
          processType:[ { required: true, message: '请选择流程', trigger: 'change' }],
          nodeName:[{ required: true, message: '请输入节点名称', trigger: 'blur' },],
          nodeIndex:[{ required: true, message: '请输入节点索引', trigger: 'blur' },],
          roleCode:[{ required: true, message: '请输入角色编号', trigger: 'blur' },],
          orgLevel:[ { required: true, message: '请选择活动资源', trigger: 'change' }]
        },
        id:'',
      }
    },
    mounted(){
      this. getInfo()
    },
    methods: {
      closeDialog(addNodeForm){
        this.$refs[addNodeForm].resetFields();
      },

      //进入页面获取数据
      getInfo(){
        //获取流程名称下拉框
        this.$nextTick(async ()=> {
          let para={
          };
        let params = {
          className: 'approveNode',
          method: 'loadProcessNames',
          param: JSON.stringify(para) };
        let res = await this.$store.dispatch('http/post', params);
         console.log(res)
        this.approveOptions = res;
      });
        //加载弹框下拉框
        this.$nextTick(async ()=> {
          let para={
          };
          let params = {
            className: 'approveNode',
            method: 'loadProcessDef',
            param: JSON.stringify(para) };
          let res = await this.$store.dispatch('http/post', params);
          console.log(res)
          this.processValueOptions = res;
        });
        //加载审批角色名称下拉框
        this.$nextTick(async ()=> {
          let para={
          };
          let params = {
            className:'approveNode',
            method:'loadApproveRoles',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.roleOptions = res;
        });
        this.listQuery.pageIndex=1;
        //加载页面数据
        this.$nextTick(async ()=> {
          let para={
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData:{
              processName:"",
              roleName:"",
               keyWord:""
        }
          };
          let params = {
            className:'approveNode',
            method:'search',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.tableData=res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })

    },
     //查看
      read(id){
        console.log(id);
        this.addNode.visible=true;
        this.oldShow=true;
        // this.newShow=true;
        this.showRadio=false;
        this.saveBtn=false;
        this.saveBtn1=false;
        this.saveBtn2=false;
        this.showOr=2;
        this.showOr2=2;

        //根据id查询单个节点信息
        this.$nextTick(async ()=> {
          let para={
            id:id
          };
          let params = {
            className:'approveNode',
            method:'queryById',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.addNodeForm.processName1 = res.node.processName;
          this.addNodeForm.processType=res.node.processType;
          this.addNodeForm.nodeIndex=res.node.nodeIndex;
          this.addNodeForm.nodeName=res.node.nodeName;
          this.addNodeForm.roleCode=res.node.roleCode;
          this.addNodeForm.orgLevel=res.node.orgLevel;
        });
      },
      //编辑
      editor(id){
        this.id=id;
        this.addNode.visible=true;
        this.oldShow=true;
        this.showRadio=true;
        this.radio='2';
        this.saveBtn=true;
        this.saveBtn1=false;
        this.saveBtn2=true;
        this.showOr=1;
        this.showOr2=2;
        //根据id查询单个节点信息
        this.$nextTick(async ()=> {
          let para={
            id:id
          };
          let params = {
            className:'approveNode',
            method:'queryById',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.addNodeForm.processName1 = res.node.processName;
          this.addNodeForm.processType=res.node.processType;
          this.addNodeForm.nodeIndex=res.node.nodeIndex;
          this.addNodeForm.nodeName=res.node.nodeName;
          this.addNodeForm.roleCode=res.node.roleCode;
          this.addNodeForm.orgLevel=res.node.orgLevel;
        });
      },
      //删除
      deleteNode(id){
        this.$nextTick(async ()=> {
          let para={
            id:id
          };
          let params = {
            className:'approveNode',
            method:'deleteById',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          if(res.code=='200'){
            this.$message ({
              message:'删除成功',
              type:'succ'
            })
            this.getInfo();
          } else{
            this.$message ({
              message:'删除失败',
              type:'error'
            })
          };
        });

      },
      //搜索功能
      getDictionary() {
        this.$nextTick(async () => {
          let para = {
            page:
              {
                pageIndex:this.listQuery.pageIndex,
                pageSize:this.listQuery.pageSize,
              },
            searchData: {
              processName:this.processName,
              roleName: this.roleName,
              keyWord: this.keyWord
            }
          };
          let params = {
            className: 'approveNode',
            method: 'search',
            param: JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post', params);
          console.log(res);
          this.tableData = res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })
      },
      handleSizeChange(val){
        this.listQuery.pageSize= val;
        this.getDictionary();
      },
      handleCurrentChange(val){
       this.listQuery.pageIndex=val;
       this.getDictionary()
      },
      addShow(){
        this.addNode.visible=true;
        // this.radio='1';
        this.showRadio=true;
        this.oldShow=true;
        this.saveBtn2=false;
        this.saveBtn1=true;
        this.processName1 = '';
        this.processType='';
        this.nodeIndex='';
        this.nodeName='';
        this.roleCode='';
        this.orgLevel='';
        this.showOr=1;
        this.showOr2=1;
      },
      //保存数据
      save(){
        this.loading=true;
        this.$refs.addNodeForm.validate((valid) =>{
          if (valid){
            this.$nextTick(async ()=> {
              for(let i=0;i<this.processValueOptions.length;i++){
                if(this.addNodeForm.processType==this.processValueOptions[i].processType){
                  this.addNodeForm.processName1=this.processValueOptions[i].processName;
                  console.log(this.addNodeForm.processName1)
                  break;
                }
              }
              let para={
                processType:this.addNodeForm.processType,
                processName:this.addNodeForm.processName1,
                nodeIndex:this.addNodeForm.nodeIndex,
                nodeName:this.addNodeForm.nodeName,
                roleCode:this.addNodeForm.roleCode,
                orgLevel:this.addNodeForm.orgLevel,
              };
              let params = {
                className:'approveNode',
                method:'addNode',
                param:JSON.stringify(para)
              };
              let res = await this.$store.dispatch('http/post',params);
              console.log(res)
              if(res.code == '200'){
                this.$message ({
                  message:'保存成功',
                  type:'succ',
                })
                this. getInfo();
                this.loading=false;
              } else{
                this.$message ({
                  message:'保存失败',
                  type:'error'
                })
                this.loading=false;
              };
              this.addNode.visible=false;
            });
          }
        })

      },
      editorSave(){
        this.loading=true;
        this.$nextTick(async ()=> {
          let para={
            // processType:this.processName1.processType,
            // processName:this.processName1.processName,
            // nodeIndex:this.nodeIndex,
            // nodeName:this.nodeName,
            // roleCode:this.roleCode,
            // orgLevel:this.orgLevel,
            id:this.id,
            processType:this.addNodeForm.processType,
            processName:this.addNodeForm.processName1,
            nodeIndex:this.addNodeForm.nodeIndex,
            nodeName:this.addNodeForm.nodeName,
            roleCode:this.addNodeForm.roleCode,
            orgLevel:this.addNodeForm.orgLevel,
          };
          let params = {
            className:'approveNode',
            method:'updateById',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          if(!res){
            this.$message ({
              message:'保存成功',
              type:'succ'
            })
            this.loading=false;
          } else{
            this.$message ({
              message:'保存失败',
              type:'error'
            })
            this.loading=false;
          };
          this.addNode.visible=false;
        });
      },
    }
  }
</script>

<style scoped>
  .header {
    background-color: rgb(240, 244, 245);
    text-align: left;
  }



  .btnCss {
    margin-left: 10px;
  }

  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
  }
  .inputWidth{
    width:40%;
  }
.marginTop{
  margin-top: 15px;
}
.black{
  color: black;
}
  .aaa {
    float: right;
  }
  .dialog_btn{
    position: absolute;
    right: 50%;
    bottom: 1em;
  }
</style>
