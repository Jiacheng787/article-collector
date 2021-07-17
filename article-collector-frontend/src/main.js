import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import { Lazyload } from 'vant'

Vue.config.productionTip = false

// 懒加载指令注册
Vue.use(Lazyload);

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
