<template>
  <el-container>
    <el-header style="width:100%" class="headerCss">
      <div>
        <div slot="title">
          商品评分权重设置
        </div>
      </div>
    </el-header>
    <el-container>
      <el-aside width="100%" style="line-height: 50px">
        <div class="mainCss">
          <div>
            <el-table
              :data="tableGoods"
              border
              :header-cell-style="{background:'#eef1f6',color:'#606266'}"
              style="width: 100%">
              <el-table-column
                prop="goodsName"
                label="商品类别"
              >
              </el-table-column>
              <el-table-column
                prop="priceWeight"
                label="价格权重"
              >
              </el-table-column>
              <el-table-column
                prop="priceMarkScore"
                label="价格基准分"
              >
              </el-table-column>
              <el-table-column
                prop="saleWeight"
                label="销量权重"
              >
              </el-table-column>
              <el-table-column
                prop="saleMarkScore"
                label="销量基准分"
              >
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button type="info" @click="updateGoodsWeight(scope.row)">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-aside>
    </el-container>

    <el-dialog
      @close="closeDialog"
      :visible.sync="isShow"
      width="30%">
      <el-form label-width="90px" :model="goodsInfo">
        <el-form-item label="价格权重:">
          <el-input v-model="goodsInfo.priceWeight" v-bind:class="{'errorClass':input1IsTrue}"  ></el-input>
        </el-form-item>
        <el-form-item label="价格基准分:">
          <el-input v-model="goodsInfo.priceMarkScore" v-bind:class="{'errorClass':input2IsTrue}"></el-input>
        </el-form-item>
        <el-form-item label="销量权重:">
          <el-input v-model="goodsInfo.saleWeight" v-bind:class="{'errorClass':input3IsTrue}"></el-input>
        </el-form-item>
        <el-form-item label="销量基准分:">
          <el-input v-model="goodsInfo.saleMarkScore" v-bind:class="{'errorClass':input4IsTrue}"></el-input>
        </el-form-item>
      </el-form>
      <div v-if="goodsInfoFlag" class="goods_meg">价格权重，销量权重之和应为1</div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="isShow = false">取 消</el-button>
    <el-button type="primary" @click="btnSave">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%">
      <span>{{msg}}</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible=false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>
  </el-container>
</template>

<script>
  export default {
    name: "CommodityScoring.vue",
    data() {
      return {
        priceWeight: '',
        tableGoods: [],
        isShow: false,
        goodsInfo: {
          priceWeight: '',
          priceMarkScore:'',
          saleWeight: '',
          saleMarkScore: '',
          goodsCategoryId:'',
          id:'',
        },
        dialogVisible:false,
        msg:'',
        input1IsTrue:false,
        input2IsTrue:false,
        input3IsTrue:false,
        input4IsTrue:false,
        goodsInfoFlag:false,
      }
    },
    created() {
      this.init();
    },
    methods: {
      closeDialog(){
        this.input1IsTrue=false
        this.input2IsTrue=false
        this.input3IsTrue=false
        this.input4IsTrue=false
        this.goodsInfoFlag=false
      },
      init() {
        this.GetWeightList();
      },
      async GetWeightList() {
        let para = {}
        const params =
          {
            className: 'GoodsWeight',
            method: 'GETGOODSWEIGHT',
            param: JSON.stringify(para)
          };
        let list = await this.$store.dispatch('http/post', params);
        for(let i in list){
          list[i].priceWeight=list[i].priceWeight*100+"%"
          list[i].saleWeight=list[i].saleWeight*100+"%"
        }
        this.tableGoods = list;
      },
      updateGoodsWeight(obj) {
        this.isShow = true;
        let priceWeight=obj.priceWeight.replace('%','');
        this.goodsInfo.priceWeight = parseFloat(priceWeight)/100;
        this.goodsInfo.priceMarkScore = obj.priceMarkScore;
        let saleWeight=obj.saleWeight.replace('%','');
        this.goodsInfo.saleWeight =parseFloat(saleWeight)/100;
        this.goodsInfo.saleMarkScore = obj.saleMarkScore;
        this.goodsInfo.goodsCategoryId=obj.goodsCategoryId;
        this.goodsInfo.id=obj.id;
      },
      async btnSave(){
        if(parseFloat(this.goodsInfo.priceWeight) + parseFloat(this.goodsInfo.saleWeight)!=1){
            this.goodsInfoFlag = true;
            return false;
        }

        let isTrue=this.check();
        if(isTrue) {
        this.isShow=false;
          let para = this.goodsInfo
          const params =
            {
              className: 'GoodsWeight',
              method: 'UPWEIGHTINFO',
              param: JSON.stringify(para)
            };
          let result = await this.$store.dispatch('http/post', params);
          if (result == 200) {
            this.GetWeightList();
          } else {
            this.msg = result;
            this.dialogVisible = true;
          }
        }
      },
      check(){
        let isTrue=true;
        this.msg="";
        let regExp = /^\d+\.?\d{0,2}$/;
        if(!regExp.test(this.goodsInfo.priceWeight)){
          isTrue=false;
          this.input1IsTrue=true;
        }
        if(!regExp.test(this.goodsInfo.priceMarkScore)){
          isTrue=false;
          this.input2IsTrue=true;
        }
        if(!regExp.test(this.goodsInfo.saleWeight)){
          isTrue=false;
          this.input3IsTrue=true;
        }
        if(!regExp.test(this.goodsInfo.saleMarkScore)){
          isTrue=false;
          this.input4IsTrue=true;
        }
        return isTrue;
      }
    }
  }
</script>

<style lang="less" scoped>
  .addcustomerFormClables {
    display: flex;
    /*margin-bottom: 10px !important;*/

    > label {
      width: 70px !important;
      min-width: 70px !important;
      padding-right: 0px !important;
      padding-left: 20px !important;
      margin-left: 58px;
      font-size: 12px !important;
    }

    > div {
      margin-left: 15px !important;
      width: 90% !important;

      > span {
        width: 100% !important;
      }
    }
  }

  .errorClass{
    border: 1px solid red;
  }
  .addTextarea {
    > div {
      > div {

        width: 80% !important;

        > textarea {
          height: 7em;
        }
      }
    }
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
      margin-right: 0.5rem;
    }
  }

  .mainCss {
    margin-top: 1rem;
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
        width: 80px;
        background-color: white;
        text-align: center;
        border: 1px solid #ebebeb;
        border-top-left-radius: 10px;
        font-size: 13px;
        color: #497bc4;
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
    }

    .pageCss {
      margin-top: 50px;
      margin-bottom: 10px;
      text-align: right;
      margin-right: 1%;
    }

  }

  .excelFileClass {
    .drop {
      max-width: 300px;
      max-height: 40px;
      font-size: 14px !important;
      line-height: 36px !important;
      margin: 0 5px !important;
    }
  }

  .card-Title {
    color: #5a9df6;
    font-size: 18px;
    font-weight: bold;
    background-color: #f6f7fb;
    border: 1px solid #ebebeb;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    line-height: 3rem;
    padding-left: 2rem;
  }


  .el-header, .el-footer {
    background-color: #ebebeb;
    color: #5a9df6;
    text-align: center;
    line-height: 60px;
  }

  .el-aside {
    /*background-color: #ffffff;*/
    /*color: #333;*/
    text-align: center;
    line-height: 200px;
    border: 1px solid #ebebeb;
    border-radius: 10px;
    box-shadow: 5px 5px 5px #ebebeb;

  }

  .el-main {
    /*background-color: #E9EEF3;*/
    /*color: #333;*/
    text-align: center;
    line-height: 160px;
    border: 1px solid #ebebeb;
    border-radius: 10px;
    box-shadow: 5px 5px 5px #ebebeb;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
  .goods_meg{
    color: red;
    font-size: 0.6em;
    text-align: center;
  }
</style>
