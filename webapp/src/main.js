import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import 'font-awesome/css/font-awesome.css'
import App from './App.vue'
import VueRouter from 'vue-router'
import routes from './routes'
import './css/ship.css'
import './css/login.css'
import 'bootstrap/dist/css/bootstrap.min.css'
Vue.use(ElementUI);
Vue.use(VueRouter);
require("babel-polyfill");


const router = new VueRouter({routes});

router.beforeEach((to, from, next) => {
    //NProgress.start();
    if (to.path === '/login') {
        sessionStorage.removeItem('user');
    }
    let user = JSON.parse(sessionStorage.getItem('user'));
    if (!user && to.path !== '/login') {
        next({ path: '/login' })
    } else {
        next()
    }
});

new Vue({
  router,
  render: h => h(App)
}).$mount('#app');
