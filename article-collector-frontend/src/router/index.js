import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

const meta = {
    requiresAuth: true, // 需要鉴权
};
const router = new Router({
    routes: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/home',
            component: () => import('@/views/Home.vue'),
            meta: {
                title: "概览"
            }
        },
        {
            path: '/article',
            component: () => import('@/views/Article.vue'),
            meta: {
                title: "文章"
            }
        },
        {
            path: '/notes',
            component: () => import('@/views/Notes.vue'),
            meta: {
                title: "笔记"
            }
        },
        {
            path: '/my',
            component: () => import('@/views/My.vue'),
            meta: {
                title: "我的"
            }
        }
        // {
        //     path: '/activity',
        //     component: () => import('@/views/Activity.vue'),
        //     meta: {
        //         ...meta,
        //     }
        // }
    ]
})

// router.beforeEach((to, from, next) => {
//     if (to.meta.requiresAuth) {
//         // 需要鉴权
//         const token = getCookie("token");
//         if (token && token !== 'undefined') {
//             next();
//         } else {
//             next("/login");
//         }
//     } else {
//         // 不需要身份校验 直接通过
//         next();
//     }
// })

export default router;