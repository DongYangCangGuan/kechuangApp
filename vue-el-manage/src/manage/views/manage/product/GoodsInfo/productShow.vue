<template>
  <div>
    <div>
      <el-container>
        <el-header class="header">
          <el-row class="headerBtn">
            <el-input :maxlength="100" v-model="processName" placeholder="请输入商品名称或者商品描述" style="width:300px" clearable></el-input>
            <el-button type="primary" class="btnCss" @click="getDictionary">搜索</el-button>
          </el-row>
        </el-header>
        <el-main>
          <el-table
            :data="tableData"
            style="width: 100%">
            <el-table-column
              prop="pic"
              label="缩略图">
               <template slot-scope="scope">
                  <img  :src="scope.row.pic" alt="" style="width: 50px;height: 50px">
              </template>
            </el-table-column>
            <el-table-column
              prop="name"
              label="商品名称">
            </el-table-column>
            <el-table-column
              prop="detail"
              label="商品描述">
            </el-table-column>
            <el-table-column
              prop="price"
              label="单价">
                <template slot-scope="scope">
                    <div style="color: red">{{scope.row.price | numFilter}}</div> 
                </template>
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="创建日期">
               <template slot-scope="scope">
                   <div>{{scope.row.createTime | formatDate}}</div>
               </template>
            </el-table-column>
            <el-table-column
              prop="isused"
              label="发布状态">
                <template slot-scope="scope">
                   <el-tag v-if="scope.row.isused==0" type="danger" @click="deleteGoodsIndo(scope.row,1)">启用</el-tag>
                   <el-tag v-if="scope.row.isused==1" type="success" @click="deleteGoodsIndo(scope.row,0)">停用</el-tag>
                </template>
            </el-table-column>
             <el-table-column
              prop="isRecommend"
              label="推荐状态">
                <template slot-scope="scope">
                   <el-tag v-if="scope.row.isRecommend==1" type="success" @click="ToRecommend(scope.row,1)">取消推荐</el-tag>
                   <el-tag v-if="scope.row.isRecommend==0" type="danger" @click="ToRecommend(scope.row,0)">添加推荐</el-tag>
                </template>
            </el-table-column>
            <el-table-column
              label="操作">
              <template slot-scope="scope">
               <div class="el-icon-view" style="color: #f17d05;font-weight: bold;" @click="read(scope.row)">查看</div>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
        <el-footer class="footer" style="margin-left: auto;"> 
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page = currentPage
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            :total='this.listQuery.totalNum'>
          </el-pagination>
        </el-footer>

        <el-dialog
        title="查看"
        :visible.sync="dialogVisible"
        width="60%"
        :before-close="handleClose">
        <span>
          <el-form ref="form" :model="form"  label-width="80px">
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="form.name" :maxlength="100" placeholder="请输入商品名称.." ></el-input>
          </el-form-item >
          <el-form-item label="商品描述" prop="detail">
            <el-input v-model="form.detail" :maxlength="100" placeholder="请输入商品描述.."></el-input>
          </el-form-item>
          <el-form-item label="单价" prop="price">
            <el-input v-model="form.price" :maxlength="100" placeholder="单价.."></el-input>
          </el-form-item>
          <el-form-item label="发布状态" prop="isused">
            <el-tag v-if="form.isused==1" type="success">启用</el-tag>
            <el-tag v-if="form.isused==0" type="danger">停用</el-tag>
          </el-form-item>
           <el-form-item label="推荐状态" prop="isRecommend">
            <el-tag v-if="form.isRecommend==1" type="success">启用</el-tag>
            <el-tag v-if="form.isRecommend==0" type="danger">停用</el-tag>
          </el-form-item>
          </el-form>
        </span>
        </el-dialog>
      </el-container>
  </div>
  </div>
</template>

<script>
  import bus from '@COM/utils/bus';
  export default {
    name: "product-Show",
    data() {
      return {
        currentPage:1,
        dialogVisible:false,
        tableData:[],
        Recommendid:[],
        form:{
          id:'',
          name:'',
          detail:'',
          isused:'',
          price:'',
          pic:'',
          isRecommend:'',
          createTime:'',
          modifytime:'',
        },
        isShow: false,
        processName:'',
        treeabout:'',
        listQuery:{
          pageIndex:1,
          pageSize:10,
          totalNum:0,
        },
      }
    }, 
    filters: {
      formatDate: function (value) {
        let date = new Date(value);
        let y = date.getFullYear();
        let MM = date.getMonth() + 1;
        MM = MM < 10 ? ('0' + MM) : MM;
        let d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        let h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        let m = date.getMinutes();
        m = m < 10 ? ('0' + m) : m;
        let s = date.getSeconds();
        s = s < 10 ? ('0' + s) : s;
        return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
      },
      numFilter(value) {
      //截取当前数据到小数点后两位
      let realVal=parseFloat(value).toFixed(2)
      return realVal
      },
    },
    mounted: function () {
      let self = this;
      bus.$on('goods-select-node',function(data) {
        self.listQuery.pageIndex=1;
        self.processName=''
        self.treeabout=data.treeabout
        self.getInfo();
      })
      this.$nextTick(function (){
        self.getInfo();
      }.bind(this));
    },
    methods: {
      //查看
      read(row){
        this.form.id=row.id
        this.form.name=row.name
        this.form.detail=row.detail
        this.form.price= parseFloat(row.price).toFixed(2)
        this.form.pic=row.pic
        this.form.isused=row.isused
        this.form.createTime=row.createTime
        this.form.modifytime=row.modifytime
        this.form.isRecommend=row.isRecommend
        this.dialogVisible=true
      },
     handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      //搜索
      getDictionary(){
         if(this.processName!=""){
          const re = /^[\u0391-\uFFE5A-Za-z0-9]+$/;
          const rsCheck = re.test(this.processName);
          if (!rsCheck){
            this.$message.info('请输入有效字符!');
          }else{
            this.listQuery.pageIndex=1;
            this.treeabout='';
            this.getInfo()
          }
        }else{
           this.treeabout='';
           this.processName='';
           this.getInfo()
        }
      },
      handleSizeChange(val){
        this.listQuery.pageSize= val;
        this.getInfo();
      },
      handleCurrentChange(val){
       this.listQuery.pageIndex = val;
       this.getInfo()
      },
      deleteGoodsIndo(row,index){
        this.$nextTick(async ()=> {
          let para={
            goodsid:row.id,
            isused:index
          };
          let params = {
            className:'productManage',
            method:'deleteGoodsIndo',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          this.$message({showClose: true,message:"操作成功",type: 'success'});
          this.getInfo();
        });
      },
      getInfo(){
        //加载页面数据
         this.$nextTick(async ()=> {
          let para={
            page:{
              pageIndex:this.listQuery.pageIndex,
              pageSize:this.listQuery.pageSize,
            },
            searchData:{
              keyWord:this.processName,
              treeabout:this.treeabout
            }
          };
          let params = {
            className:'productManage',
            method:'getgoodsInfo',
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          console.log(res)
          this.tableData=res.dataList;
          this.listQuery.totalNum=res.page.totalNum
        })
      },
      ToRecommend(row,index){
        this.Recommendid=[]
        this.Recommendid.push(row.id);
        let method;
        if(index==0){
          method='addToRecommend'
        }else{
          method='cancelRecommend'
        }
        this.$nextTick(async ()=> {
          let para={
             ids:this.Recommendid
          };
          let params = {
            className:'productManage',
            method:method,
            param:JSON.stringify(para)
          };
          let res = await this.$store.dispatch('http/post',params);
          if(res=="添加成功" || res=="取消成功"){
            this.$message({showClose: true,message:res,type: 'success'});
          }else{
            this.$message({message:res});
          }
           this.getInfo()
        })
      }
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
