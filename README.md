# article-collector

使用 Vue 2.6 + Vant UI 开发的文章一键收藏工具。

作为一个技术人，相信大家一定跟我一样，每天关注的技术公众号、技术群、掘金社区都有大量文章推送，但又没时间及时阅读，微信收藏虽然方便，但是收藏之后基本不会再看了，后面想看，查找又费事。因此，本人专门开发了技术人专属的文章一键收藏工具

本产品特点如下：

- 文章一键收藏，看到好文章，copy link 之后打开本页面，即可添加文章；
- 添加的文章会自动爬取标题、缩略图；
- 文章会自动判断是否已经添加过，添加过就不再重复添加；
- 除了添加文章，还可以添加不超过140字笔记；
- 支持按标题、分类、收藏日期查询文章；
- 看到好文章可以添加星标；
- 文章支持已读未读；
- 支持创建并分享文章列表；
- 提供概览功能，显示文章收藏总数，每月收藏趋势；

技术亮点：

- 前端技术栈使用 Vue 2.6，Vue router，Vuex 开发；
- 使用 Vant UI 移动端组件；
- 使用 [babel-plugin-import](https://github.com/ant-design/babel-plugin-import) 实现组件按需引入；
- Vue-cli 使用环境变量
- 使用 SpringBoot 开发后端接口，持久层框架选用 MyBatis-Plus；
- 文章列表采用前端懒加载 (上拉加载更多) + 后端分页 (MyBatis-Plus 提供的分页插件)，同时支持下拉刷新；
- 后端使用大量 `lombok` 注解简化代码，使用 `javax.validation` 注解实现优雅参数校验；
- 后端接口完全按照 RESTful 规范，使用自定义响应体，自定义异常类；
- 使用 Jsoup 实现爬虫功能，主要爬取 meta 标签获取文章标题、作者、简介、封面图；
- 使用 SpringBoot 提供的线程池运行爬虫；

主要难点：

- 使用 QueryWrapper 的 lambda 方式查询时遇到报错的情况，仔细看了一遍文档也没找到原因，网上技术社区也找不到解决方案，最后 debug 发现是 MyBatis-Plus 3.4.3 的版本有 bug，回退到 3.4.2 可以正常使用；
- 前端调更新的接口更新字段，一直更新失败，控制台也没有报错，后来尝试硬编码，直接将需要更新的值写在代码里面，发现更新成功，进一步排查发现是 Java 的 Long 类型 id，传给前端之后精度丢失，导致 update 的时候查不到记录，因此在 JSON 序列化的时候使用 jackson 将 id 转为字符串，再次更新成功；



## 参考

[Vant UI 官网](https://youzan.github.io/vant/#/zh-CN/swipe-cell)

[Vant UI 码云镜像](https://vant-contrib.gitee.io/vant/#/zh-CN/)

[SpringBoot中如何优雅的使用多线程](https://zhuanlan.zhihu.com/p/134636915)



## Project setup

```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
