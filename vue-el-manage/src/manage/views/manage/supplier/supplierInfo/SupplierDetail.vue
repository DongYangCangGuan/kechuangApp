<template>
    <div>
      <el-form :model="form">
        <div class="btn" >
          <el-button type="primary" @click="goInfo">返 回</el-button>
        </div>
        <hr class="hr"/>
        <el-row class="top">
          <el-col :span="24" class="top1">
            基本信息
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商名称" :label-width="formLabelWidth" prop="customerName">
              <el-input :disabled="true" v-model="form.supplierName" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="营业执照" :label-width="formLabelWidth" prop="area">
              <el-input :disabled="true" v-model="form.license" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="注册时间" :label-width="formLabelWidth" prop="customerType">
              <el-input :disabled="true" v-model="form.createTime" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" :label-width="formLabelWidth" prop="address">
              <el-input :disabled="true" v-model="form.isused" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="地址" :label-width="formLabelWidth">
              <el-input :disabled="true" v-model="form.address" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" :label-width="formLabelWidth">
              <el-input :disabled="true" v-model="form.person" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="审核人" :label-width="formLabelWidth">
              <el-input :disabled="true" v-model="form.modifierName" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核时间" :label-width="formLabelWidth">
              <el-input :disabled="true" v-model="form.modifyTime" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="联系电话" :label-width="formLabelWidth">
              <el-input :disabled="true" v-model="form.phone" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮箱" :label-width="formLabelWidth">
              <el-input :disabled="true" v-model="form.email" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="微信号" :label-width="formLabelWidth">
              <el-input :disabled="true" v-model="form.wechartId" autocomplete="off" class="inputWidth"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <hr class="hr" />
      </el-form>

       <el-row class="bottom">
       <el-col :span="24" class="top1">
           供货信息
        </el-col>
       </el-row>

      <template>
        <el-table :data="form.goods" border class="table">
          <el-table-column prop="name" label="供货名称"></el-table-column>
          <el-table-column prop="brand" label="品牌"></el-table-column>
          <el-table-column prop="price" label="价格"></el-table-column>
          <el-table-column prop="weight" label="重量"></el-table-column>
          <el-table-column prop="color" label="颜色"></el-table-column>
<!--          <el-table-column label="供货图片"><el-button type="primary" size="mini" @click="showpic()">点击查看</el-button></el-table-column>-->
          <el-table-column label="供货图片">
            <template slot-scope="scope">
              <span @click="showpic(scope.row.imageURL)">查看图片</span>
            </template>
          </el-table-column>
        </el-table>
      </template>
      <el-dialog title="图片详情"
                 :visible.sync="pic"
                 :modal-append-to-body="false"
                 :append-to-body="true">
          <img style="width: 100%;height: 100%;" :src="imageUrl" alt="暂无上传的图片或图片加载失败">
      </el-dialog>

      <el-footer class="footer">
        <div>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page = "listQuery.pageIndex"
            :page-size="listQuery.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="listQuery.total">
          </el-pagination>
        </div>
      </el-footer>

    </div>
</template>

<script>
    export default {
   // 接受父组件的传值
    props: {
      onBack: {
        type: Function,
        default: null
      }
    },
      name: "Supplier-Detail",
      data(){
        return{
          pic:false,
          form: {
            createTime:'',
            supplierName:'',
            address:'',
            person:'',
            phone:'',
            license:'',
            email:'',
            wechartId:'',
            modifyTime:'',
            modifierName:'',
            isused:'',
            goods:[],
          },
          formLabelWidth: '120px',

          listQuery:{
            pageIndex:1,
            pageSize:5,
            pageCount:0,//总页数
            total: 0
          },
          select:true,
          imageUrl:'',
        }
      },
      mounted(){
        this.supplierId=sessionStorage.getItem('supplierId')
        console.log(this.supplierId)
        this.getInfo()
      },
      methods:{
        handleSizeChange(val){
          this.listQuery.pageSize = val;
          this.getInfo();
        },
        handleCurrentChange(val){
          this.listQuery.pageIndex = val;
          this.getInfo()
        },
        showpic(){
          // console.log(imageURL);
          // let newImageUrl = imageURL.slice(0,5)+'/office'+imageURL.slice(5);
          // console.log(newImageUrl)
          // this.imageUrl=newImageUrl;
          // this.form.goods.pic=row.pic
          this.pic=true;
        },
        getInfo(){
          let _this = this;
          //进入页面获取所有数据
          this.$nextTick(async ()=> {
            let para={
              page:{
                pageIndex:_this.listQuery.pageIndex,
                pageSize:_this.listQuery.pageSize,
              },
              searchData:{
                 id:this.supplierId
               }
            };
            let params = {
              className:'supplier',
              method:'querySupplierById',
              param:JSON.stringify(para)
            };
            let res = await this.$store.dispatch('http/post',params);
            console.log(res);
            this.form.supplierName=res.name;
            this.form.createTime=res.createTime;
            this.form.address=res.address;
            this.form.person=res.person;
            this.form.phone=res.phone;
            this.form.license=res.license;
            this.form.email=res.email;
            this.form.wechartId=res.wechartId;
            this.form.modifyTime=res.modifyTime;
            this.form.modifierName=res.modifierName;
            this.form.isused=res.isused;
            this.form.goods=res.pageVo.dataList;
            this.listQuery.total=res.pageVo.page.totalNum;
          })
        },
        goInfo(){
          if(this.onBack){
            this.onBack('succ')
           }
        }
      }
    }

</script>

<style scoped>
  .hr{
    height:2px;
    border:none;
    border-top:1px solid #e4e7ec;
  }
  .btn{
   text-align: left
  }
  .top{
    padding-top: 1rem;
  }
  .top1{
    line-height: 24px;
    font-size: 16px;
    color: #445c88;
    font-weight: bold;
    padding-left: 2rem;
  }
  .bottom{
    padding-bottom: 1rem;
  }
  .table{
    width: 100%
  }
  .footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
  }
  /deep/ .el-main{
    padding: 10px;
  }
</style>
