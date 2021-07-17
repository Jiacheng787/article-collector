<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <van-nav-bar :title="$route.meta.title" :fixed="true" :placeholder="true">
      <template #right>
        <van-icon name="plus" size="18" @click="handleAdd" />
      </template>
    </van-nav-bar>
    
    <!-- 内容展示区域 -->
    <router-view></router-view>
    
    <!-- 底部 TAB 栏 -->
    <van-tabbar v-model="active" @change="onChange">
      <van-tabbar-item 
        v-for="(item, index) in menuItem"
        :key="index"
        :icon="item.icon">
        {{ item.name }}
      </van-tabbar-item>
    </van-tabbar>

    <!-- 添加文章弹框 -->
    <add-popup ref="add-popup" />
  </div>
</template>

<script>
import { NavBar, Icon, Tabbar, TabbarItem } from 'vant';
import AddPopup from "@/components/AddPopup";

export default {
  name: 'App',
  components: {
    [NavBar.name]: NavBar,
    [Icon.name]: Icon,
    [Tabbar.name]: Tabbar,
    [TabbarItem.name]: TabbarItem,
    AddPopup
  },
  data() {
    return {
      active: 0,
      menuItem: [
        {
          name: "概览",
          icon: "home-o",
          path: "/home"
        },
        {
          name: "文章",
          icon: "like-o",
          path: "/article"
        },
        {
          name: "笔记",
          icon: "label-o",
          path: "/notes"
        },
        {
          name: "我的",
          icon: "friends-o",
          path: "/my"
        }
      ]
    }
  },
  watch: {
    '$route': {
      handler(to, from) {
        // 监听路由变化激活菜单
        // this.menuItem.forEach((item, index) => {
        //   if (to.path.includes(item.path)) {
        //     this.selectedKeys = [`${index}`];
        //   }
        // })
        const index = this.menuItem.findIndex(item => item.path == to.path);
        this.active = index;
      },
      immediate: true
    }
  },
  methods: {
    onChange(index) {
      const { path } = this.menuItem[index];
      if (this.$route.path != path) {
        this.$router.push(path);
      }
    },
    handleAdd() {
      this.$refs["add-popup"].isAddPopShow = true;
    }
  }
}
</script>

<style>
body {
  margin: 0;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
