<template>
  <div class="app-container">
    <panel-label-group @handleType='handleType' v-model="system"/>
    <panel-part-group @handleRow="handleRow" :type="system"/>
    <panel-tab-group/>
  </div>
</template>

<script>
  export default {
    name: "System",
    components: {
      PanelLabelGroup: () => import('./components/PanelLabelGroup').then(m => m.default),
      PanelPartGroup: () => import('./components/PanelPartGroup').then(m => m.default),
      PanelTabGroup: () => import('./components/PanelTabGroup').then(m => m.default)
    },
    data() {
      return {
        system: 'task'
      }
    },
    methods: {
      handleType() {
        // console.log(this.system)
        //点击事件处理
        this.$router.push(`/detail/${this.system}`)
      },
      handleRow(row){
        this.$router.push(`/detail/${this.system}/${row.name}`)
      }
    }
  }
</script>

<style lang="scss">

</style>
