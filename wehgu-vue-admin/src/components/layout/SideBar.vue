<!--
 * @Author: 龙际妙
 * @Date: 2021-10-15 14:27:30
 * @Description: 
 * @FilePath: \wehgu-vue-admin\src\components\layout\SideBar.vue
 * @LastEditTime: 2022-04-07 18:20:08
 * @LastEditors: Please set LastEditors
-->
<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      @open="btn"
      :default-active="activePath"
      :collapse="isCollapse"
      background-color="#242633"
      text-color="#bfcbd9"
      active-text-color="#e6a23c"
      unique-opened
      router
    >
      <el-sub-menu :index="item.permId + ''" v-for="(item, index) in isMenuList" :key="index">
        <template #title>
          <i :class="item.icon"></i>
          <!-- 文本 -->
          <span>{{ item.title }}</span>
        </template>
        <el-menu-item :index="subitem.url" v-for="subitem in item.children" :key="subitem.permId" @click="savaNavState(subitem.url)">
          <!-- 图标 -->
          <i :class="subitem.icon"></i>
          <!-- 文本 -->
          <span>{{ subitem.title }}</span>
        </el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      menuList: [],
      show: true,
      activePath: '',
    }
  },
  created() {},
  computed: {
    isCollapse() {
      console.log(this.$store.getters.collapse)
      return this.$store.getters.collapse
    },
    // 获取菜单列表
    isMenuList() {
      return this.$store.getters.menuList
    },
  },

  methods: {
    getPerms(data) {
      data.forEach((item, i) => {
        console.log(item)
        if (item.menus) {
          // 调用递归函数
          this.getPerms(item.menus)
        }
        if (item.menus) {
          if (data.perms) {
            console.log(data.perms)
            let obj = {
              menuId: data[i].menuId,
              perms: data[i].perms,
            }
            console.log(obj)
            this.$store.dispatch('SET_BUTTONS', obj)
          }
        }
      })
    },

    btn(e) {
      console.log(e)
    },
    // 保存链接(组件)的激活状态
    savaNavState(activePath) {
      window.sessionStorage.setItem('activePath', activePath)
      this.activePath = activePath
    },
  },
}
</script>

<style scoped lang="less">
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}

// //子节点选中样式(前面只有一级，后面子级)
// .el-menu-item.is-active，.el-submenu .el-menu-item.is-active {
//   background-color: red !important;
// }
// //子节点选中鼠标移入样式
// .el-menu-item.is-active:hover，.el-submenu .el-menu-item:hover {
//   background-color: pink !important;
// }
// //子节点鼠标移入样式
.el-menu-item:active {
  background-color: #6b63b5;
  filter: brightness(1.5); //设置亮度值，范围：0-1
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 250px;
  min-height: 400px;
}

.sidebar > ul {
  height: 100%;
}
</style>
