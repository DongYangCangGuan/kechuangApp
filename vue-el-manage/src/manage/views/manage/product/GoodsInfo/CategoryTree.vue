<template>
  <div>
    <el-header class="header">
      <el-row class="headerbtn">
        <span style="float: left;color:#808080">类别列表</span>
        <!--<el-button type="primary" icon="el-icon-share" @click="change" plain>变更上下级</el-button>-->
        <el-button type="info" icon="el-icon-refresh" @click="reload" plain>刷新</el-button>
      </el-row>
    </el-header>
    <el-container>
      <el-tree
        class="treestyle"
        :props="defaultProps"
        @node-click="nodeClick"
        :expand-on-click-node="false"
        :highlight-current="true"
        :load="openNode"
        :key="treeKey"
        lazy>
      </el-tree>
    </el-container>
  </div>
</template>
<script>
  import bus from '@COM/utils/bus';
  export default {
    name: 'Category-tree',
    data() {
      return {
       defaultProps:{
          label: 'name',
          children: 'zones'
        },
        treeKey:''
      }
    },
    methods: {
      reload() {
        this.treeKey = +new Date();
      },
      nodeClick(data, node, con) {
        console.log(data);
        console.log(node);
        console.log(con);
        bus.$emit('goods-select-node', data);
      },
      openNode(node, resolve){
        //懒加载加载数据
      if(node.level === 0){
        this.$nextTick(async ()=> {
        let para={};
        let params = {
          className:"productManage",
          method:"categories",
          param: JSON.stringify(para)};
        let response = await this.$store.dispatch('http/post', params);
            console.log(response)
            const ResData = response;
            // console.log(ResData.data.length)
            if (ResData.length){
              return resolve(response)
            } else {
            return resolve([])
            }
        });
       }else if (node.level > 0){
        this.$nextTick(async ()=> {
        let para={
           parentId:node.data.id,
           treeAbout:node.data.treeAbout
        };
        console.info(para);
        let params = {
          className:'productManage',
          method:'categoriesParent',
          param: JSON.stringify(para) 
        };
        let response = await this.$store.dispatch('http/post', params);
            console.info(response)
            if(response.length > 0) {
                return resolve(response)
              }else{
                return resolve([])
            }
           })
        }else{
          return resolve([])
        }
      }
    }
  };
</script>
<style>
  .header{
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
  }
  .headerbtn{
    text-align: right;
    line-height: 5;height: 12px;
  }
  .treestyle{
    width: 100%;min-height: 400px;
  }
</style>
