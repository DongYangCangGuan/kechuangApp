<template>
  <div>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="$route.path"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :active-text-color="variables.menuActiveText"
        mode="vertical"
      >
        <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path"/>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import SidebarItem from './SidebarItem'
  import variables from '@MAN/styles/variables.scss'

  export default {
    components: {SidebarItem},
    computed: {
      ...mapState({
        permission_routes:state=>state.permission.routes,
        sidebar:state=>state.app.sidebar
      }),
      variables() {
        return variables
      },
      isCollapse() {
        return !this.sidebar.opened
      }
    }
  }
</script>
