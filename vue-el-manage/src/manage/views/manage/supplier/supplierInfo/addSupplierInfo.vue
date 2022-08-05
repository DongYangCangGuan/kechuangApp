<template>
  <el-container >
    <el-main>
      <div class="mainCss">
        <div >
          <div slot="title" class="card-Title" style="">
            {{title}}供应商
          </div>

          <!-- 基本信息 -->
          <div>
            <el-form :model="form" :rules="rules" ref="form">
              <el-row class="top">
                <el-col :span="24" class="top1">
                  基本信息
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="供应商名称" :label-width="formLabelWidth" prop="name">
                    <el-input  v-model="form.name" autocomplete="off" class="inputWidth"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="营业执照" :label-width="formLabelWidth" prop="license">
                    <el-input  v-model="form.license" autocomplete="off" class="inputWidth"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="12">
                  <el-form-item label="供应商类型" :label-width="formLabelWidth" prop="type">
                    <el-select  v-model="form.type" placeholder="请选择类型" class="inputWidth">
                      <el-option
                        v-for="item in typeOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="供应商级别" :label-width="formLabelWidth" prop="clazz">
                    <el-select   v-model="form.clazz" placeholder="请选择类型"class="inputWidth">
                      <el-option
                        v-for="item in clazzOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="12">
                  <el-form-item label="地址" :label-width="formLabelWidth" prop="address">
                    <el-input  v-model="form.address" autocomplete="off" class="inputWidth"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="联系人" :label-width="formLabelWidth" prop="person">
                    <el-input  v-model="form.person" autocomplete="off" class="inputWidth"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="12">
                  <el-form-item label="联系电话" :label-width="formLabelWidth" prop="phone">
                    <el-input  v-model="form.phone" autocomplete="off" class="inputWidth"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="电子邮箱" :label-width="formLabelWidth" prop="email">
                    <el-input  v-model="form.email" autocomplete="off" class="inputWidth"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="微信号" :label-width="formLabelWidth">
                    <el-input  v-model="form.wechartId" autocomplete="off" class="inputWidth"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <hr  class='hr'/>


            </el-form>
          </div>
          <div  class="btn" >
            <el-button type="danger" @click="goSupplierInfo">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
        </div>
      </div>
      <div>

      </div>
    </el-main>
  </el-container>
</template>

<script>
  export default {
    name: "Supplier-Add",
    // 接受父组件的传值
    props: {
      onBack: {
        type: Function,
        default: null
      }
    },
    data() {
      // 此处自定义校验手机号码js逻辑
      var phoneReg = /^[1][3,4,5,7,8][0-9]{9}$/
      var validatePhone = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入联系电话!!'))
        }
        setTimeout(() => {
          if (!phoneReg.test(value)) {
            callback(new Error('格式有误!!'))
          } else {
            callback()
          }
        }, 1000)
      }

      var checklicense = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请填写营业执照!!'))
        }
        setTimeout(() => {
          if (!Number.isInteger(+value)) {
            callback(new Error('请输入正确格式!!'))
          }else {
            callback()
          }
        }, 100)
      }
      return {
        //表单字段
        form: {
          type:'',
          clazz:'',
          name:'',
          address:'',
          person:'',
          phone:'',
          license:'',
          email:'',
          wechartId:'',
        },
       //字段宽度
        formLabelWidth: '120px',
        typeOptions: [
          { value: '1',
          label: '商品供应商'},
          { value: '2',
            label: '硬件设备供应商'},
        ],
        clazzOptions: [
          { value: '1',
            label: '一级供应商'},
          { value: '2',
            label: '二级供应商'},
        ],
        title:'新增',
        select:false,
        rules: {
          name: [
            { required: true, message: '请输入供应商名称!!', trigger: 'blur' },
          ],
          license:[
            { required: true, validator: checklicense, trigger: 'blur' },
          ],
          type: [
            { required: true, message: '请选择供应商类型!!', trigger: 'change' }
          ],
          clazz:[
            { required: true, message: '请选择供应商级别!!', trigger: 'change' }
          ],
          address:[
            { required: true, message: '请输入地址!!', trigger: 'blur' },
          ],
          person:[
            { required: true, message: '请输入联系人!!', trigger: 'blur' },
          ],
          phone:[
            { required: true, validator: validatePhone, trigger: 'blur' },
          ],
          email:[
            { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
          ]

        },
      }
    },
    mounted(){
    },
    methods: {
      //点击保存新增的供应商信息数据
      save(){
        this.$refs.form.validate((valid) =>{
          if(valid){
          this.$nextTick(async ()=> {
            let param = {
              name:this.form.name,
              type:this.form.type,
              clazz:this.form.clazz,
              address:this.form.address,
              person:this.form.person,
              phone:this.form.phone,
              license:this.form.license,
              email: this.form.email,
              wechartId: this.form.wechartId
            }
            let params = {
              className:'supplier',
              method:'addSupplier',
              param:JSON.stringify(param)
            };
            let res = await this.$store.dispatch('http/post',params);
            console.log(res)
            if(res=="添加成功"){
              this.$message ({
                message:'保存成功',
                type:'succ'
              })
            } else{
              this.$message ({
                message:'保存失败',
                type:'error'
              })
            }
          });
           setTimeout(() => {
            if (this.onBack){
              this.onBack('succ')
            }
           }, 500);
          }
        })
       },
      //点击取消返回供应商列表页面
      goSupplierInfo(){
        if (this.onBack){
            this.onBack('succ')
        }
      }
    }
  }
</script>

<style lang="less">
  .card-panel.animated.pulse.el-row{
    height: 100% !important;
  }
  .headerCss {
    display: flex;
    display: -webkit-flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-top: 10px;
    padding-top: 10px;
    padding-bottom: 10px;
    border-radius: 10px;
    border: 1px solid #ebebeb;
    box-shadow: 5px 5px 5px #ebebeb;
    font-size: 12px;

    .leftCss {
      display: flex;
      display: -webkit-flex;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;
    }

    .rightCss {
      margin-right: 10px;
    }

    span {
      white-space: nowrap;
      margin-left: 10px;
    }
  }
.hr{
  height:1rem;
  border:none;
  border-top:1px solid #e4e7ec;
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
  .btn{
    text-align: center;
    padding: 1rem 0;
  }
  .mainCss {
    border: 1px solid #ebebeb;
    border-radius: 10px;
    box-shadow: 5px 5px 5px #ebebeb;

    .customerCss {
      height: 40px;
      background-color: #eceffb;
      border-top-left-radius: 10px;
      border-top-right-radius: 10px;

      div {
        height: 40px;
        line-height: 40px;
        width: 100px;
        background-color: white;
        text-align: center;
        border: 1px solid #ebebeb;
        border-top-left-radius: 10px;
        font-size: 13px;
        color: #589df8;
      }
    }

    .tabCss {
      width: 98%;
      margin: 1%;
      box-shadow: 5px 5px 5px #ebebeb;

      i {
        font-size: 20px;
        color: #1e6abc;
      }

      .selCss {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;

        div {
          width: 80px;
          text-align: center;
        }
      }

      .opCss {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
      }
    }
    .card-Title{
      color:#5a9df6;
      font-size:18px;
      font-weight: bold;
      background-color: #f6f7fb;
      border: 1px solid #ebebeb;
      border-top-left-radius:10px;
      border-top-right-radius:10px;
      line-height: 3rem;
      padding-left: 2rem;
    }
    .inputWidth{
      width: 25.4rem;
    }
  }

</style>
