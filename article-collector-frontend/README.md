# article-collector

使用 Vue 2.6 + Vant UI 开发的文章一键收藏工具。

作为一个技术人，相信大家一定跟我一样，每天关注的技术公众号、技术群、掘金社区都有大量文章推送，但又没时间及时阅读，微信收藏虽然方便，但是收藏之后基本不会再看了，后面想看，查找又费事。因此，本人专门开发了技术人专属的文章一键收藏工具

本项目特点如下：

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



使用 [babel-plugin-import](https://github.com/ant-design/babel-plugin-import) 实现组件按需引入，在 `babel.config.js` 中添加如下内容：

```js
module.exports = {
  plugins: [
    ['import', {
      libraryName: 'vant',
      libraryDirectory: 'es',
      style: true
    }, 'vant']
  ]
}
```



`Lazyload` 是 `Vue` 指令，使用前需要在 `main.js` 中对指令进行注册：

```js
import Vue from 'vue';
import { Lazyload } from 'vant';

Vue.use(Lazyload);
```

将 `v-lazy` 指令的值设置为你需要懒加载的图片：

```vue
<img v-for="img in imageList" v-lazy="img" />
```

如果需要组件懒加载，则需要在 `main.js` 中注册时设置 `lazyComponent` 选项：

```js
import Vue from 'vue';
import { Lazyload } from 'vant';

// 注册时设置`lazyComponent`选项
Vue.use(Lazyload, {
  lazyComponent: true,
});
```

将需要懒加载的组件放在 `lazy-component` 标签中，即可实现组件懒加载：

```vue
<lazy-component>
  <img v-for="img in imageList" v-lazy="img" />
</lazy-component>
```



Vant UI 两种组件注册方法：

```vue
<template>
	<van-popup></van-popup>
</template>

<script>
import { Popup } from "vant";
export default {
    components: {
        [Popup.name]: Popup
    }
}
</script>
```

或者

```vue
<template>
	<van-popup></van-popup>
</template>

<script>
import { Popup as VantPopup } from "vant";
export default {
    components: {
    	VantPopup
	}
}
</script>
```



文章字段包括：

```js
{
    id: "", // 文章 ID
    owner: "", // 所属用户
    title: "", // 文章标题
    thumb: "", // 缩略图
    url: "", // 文章 URL
    tag: "", // 分类
    source: "", // 来自哪个平台
    collectedTime: "", // 收藏日期
    isFavorite: true, // 是否星标，通过字体样式展现
    isRead: false // 是否已读，未读 tag 红色，已读 tag 浅色
}
```



## 如何实现一键收藏

最初的方案是先复制文章 URL，然后打开本 Web 项目，通过 Clipboard API 读取剪贴板内容。Clipboard API 返回 Promise 对象，且可以将任意内容（比如图片）放入剪贴板。

在使用前需要判断浏览器是否支持 Clipboard API：

```js
const clipboardObj = navigator.clipboard;
```

如果上面的代码返回 `undefined` ，说明当前浏览器不支持这个 API。

由于剪贴板可能会涉及敏感内容，因此这个 API 限制比较多。

首先，Chrome 浏览器规定，只有 **HTTPS** 协议的页面才能使用这个 **API**。不过，在开发环境（localhost）允许使用非加密协议。

其次，调用时需要明确获得用户的许可。权限的具体实现使用了 Permissions **API**，跟剪贴板相关的有两个权限：`clipboard-write`（写权限）和`clipboard-read`（读权限）。"写权限"自动授予脚本，而"读权限"必须用户明确同意给予。也就是说，写入剪贴板，脚本可以自动完成，但是读取剪贴板时，浏览器会弹出一个对话框，询问用户是否同意读取。

另外，需要注意的是，脚本读取的总是**当前页面的剪贴板**。这带来的一个问题是，如果把相关的代码粘贴到开发者工具中直接运行，可能会报错，因为这时的当前页面是开发者工具的窗口，而不是网页页面。

```js
(async () => {
  const text = await navigator.clipboard.readText();
  console.log(text);
})();
```

一个解决方法就是，相关代码放到`setTimeout()`里面延迟运行，在调用函数之前快速点击浏览器的页面窗口，将其变成当前页。

```js
setTimeout(async () => {
  const text = await navigator.clipboard.readText();
  console.log(text);
}, 2000);
```

这个方案主要问题，如果不是 HTTPS 协议，则 `navigator.clipboard` 对象访问不到，开发环境除外。而生产环境使用 HTTPS 涉及域名、证书等问题。

现在的方案，一键收藏采用 iOS 的捷径 app 实现。在手机端复制文章 URL 后，通过捷径 app 读取剪贴板内容，然后将 URL 发送给后台。



## 如何爬取文章标题缩略图

进一步研究捷径 app 发现，其实捷径 app 自带爬虫功能，可以爬取文章的标题、缩略图等。但是这个爬虫执行效率太低，而且存在爬不到内容的情况。因此，决定在服务器端爬取内容。

需要爬取内容的文章，主要是微信公众号和掘金，通过查看网页源代码，发现这两个平台都对 SEO 提供了较好的支持，只需要爬取相应的 meta 标签，即可获取到相关内容。

```js
const metas = document.getElementsByTagName('meta');
for (let i in metas) {
    if (metas[i].getAttribute("property") === "og:title") {
        console.log(metas[i].getAttribute("content"))
    }
}
```



通常 `@RestControllerAdvice` 只能捕获 Controller 类抛出的异常，无法捕获 404 异常。但是只要在 `application.properties` 中进行如下配置，就可以捕获到 `NoHandlerFoundException` 异常。

```properties
spring.mvc.throw-exception-if-no-handler-found=true
spring.mvc.static-path-pattern=/statics/**
```

此外，在调试的时候发现，如果一个 Controller 类中没有定义某种请求方法（例如 put 请求），在调接口的时候用 put 去请求，此时框架就会抛出 `HttpRequestMethodNotSupportedException` 异常，同时向前端返回 405 错误，一样在 `@RestControllerAdvice` 中进行处理即可。

修改 SpringBoot IP 地址和端口号，在 `application.properties` 中进行配置即可。

```properties
server.address=0.0.0.0
server.port=8088
```

如果一个类的构造器设为私有，那么这个类就不能在类的外面进行实例化，但是可以通过类的静态方法进行实例化，然后通过外部调用类的静态方法创建实例。

```java
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleBaseInfo {
    private String title;
    private String thumb;
    private String source;
}

// 构造方法设为私有后，不能在类的外面实例化
new ArticleBaseInfo();
```

SpringBoot 如何使用线程池。在 SpringBoot 应用中，经常会遇到在一个接口中，同时做事情1，事情2，事情3，如果同步执行的话，则本次接口时间取决于事情1 2 3执行时间之和；如果三件事同时执行，则本次接口时间取决于事情1 2 3执行时间最长的那个，合理使用多线程，可以大大缩短接口时间。

SpringBoot 应用中需要添加 `@EnableAsync` 注解，来开启异步调用，一般还会配置一个线程池，异步的方法交给特定的线程池完成：

```java
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean("doSomethingExecutor")
    public Executor doSomethingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(10);
        // 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(20);
        // 缓冲队列：用来缓冲执行任务的队列
        executor.setQueueCapacity(500);
        // 允许线程的空闲时间60秒：当超过了核心线程之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("do-something-");
        // 缓冲队列满了之后的拒绝策略：由调用线程处理（一般是主线程）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }

}
```

使用的方式非常简单，在需要异步的方法上加 `@Async` 注解：

```java
@Slf4j
@Service
public class AsyncService {

    // 指定使用beanname为doSomethingExecutor的线程池
    @Async("doSomethingExecutor")
    public String doSomething(String message) {
        log.info("do something, message={}", message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("do something error: ", e);
        }
        return message;
    }
}
```

在 Controller 类中使用：

```java
@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/open/something")
    public String something() {
        int count = 10;
        for (int i = 0; i < count; i++) {
            asyncService.doSomething("index = " + i);
        }
        lon
        return "success";
    }
}
```

当异步方法有返回值时，如何获取异步方法执行的返回结果呢？这时需要异步调用的方法带有返回值CompletableFuture。

CompletableFuture是对Feature的增强，Feature只能处理简单的异步任务，而CompletableFuture可以将多个异步任务进行复杂的组合：

```java
@Slf4j
@Service
public class AsyncService {

    @Async("doSomethingExecutor")
    public CompletableFuture<String> doSomething1(String message) throws InterruptedException {
        log.info("do something1: {}", message);
        Thread.sleep(1000);
        return CompletableFuture.completedFuture("do something1: " + message);
    }

    @Async("doSomethingExecutor")
    public CompletableFuture<String> doSomething2(String message) throws InterruptedException {
        log.info("do something2: {}", message);
        Thread.sleep(1000);
        return CompletableFuture.completedFuture("; do something2: " + message);
    }

    @Async("doSomethingExecutor")
    public CompletableFuture<String> doSomething3(String message) throws InterruptedException {
        log.info("do something3: {}", message);
        Thread.sleep(1000);
        return CompletableFuture.completedFuture("; do something3: " + message);
    }
}
```

在 Controller 类中使用：

```java
@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @SneakyThrows
    @ApiOperation("异步 有返回值")
    @GetMapping("/open/somethings")
    public String somethings() {
        CompletableFuture<String> createOrder = asyncService.doSomething1("create order");
        CompletableFuture<String> reduceAccount = asyncService.doSomething2("reduce account");
        CompletableFuture<String> saveLog = asyncService.doSomething3("save log");

        // 等待所有任务都执行完
        CompletableFuture.allOf(createOrder, reduceAccount, saveLog).join();
        // 获取每个任务的返回结果
        String result = createOrder.get() + reduceAccount.get() + saveLog.get();
        return result;
    }
}
```

从上面的代码可以看出，`CompletableFuture.allof` 有点像 Promise.all 用法。需要注意的是，获取每个任务的返回结果的 `get()` 方法，需要捕获 `InterruptedException` ，这边直接用 lombok 的注解 `@SneakyThrows` 来搞定。另外 `@ApiOperation` 是 Swagger 的注解用于生成 API 文档。

MyBatis-Plus 进行条件查询会用到 `QueryWrapper` ，最基础的用法如下：

```java
QueryWrapper<ArticleModel> queryWrapper = new QueryWrapper<>();
queryWrapper
  .eq("owner", "Garfield")
  .eq("delete_flag", 0);
List<ArticleModel> list = articleMapper.selectList(queryWrapper);
```

上面这样写有个问题，字段名出现了**硬编码**。为了避免在代码中出现硬编码，可以引入 lambda ：

```java
QueryWrapper<ArticleModel> queryWrapper = new QueryWrapper<>();
queryWrapper.lambda()
  .eq(ArticleModel::getOwner, "Garfield")
  .eq(ArticleModel::getDeleteFlag, 0);
List<ArticleModel> list = articleMapper.selectList(queryWrapper);
```

> 注意本人在 MyBatis-Plus 3.4.3 版本使用 lambda 有 bug ，动态拼接 SQL 语句会报错，回退到 3.4.2 可以正常使用

为了简化 lambda 的使用，可以改写成 `LambdaQueryWrapper` 构造器：

```java
LambdaQueryWrapper<ArticleModel> wrapper = new QueryWrapper<ArticleModel>().lambda();
```

或者这样写：

```java
LambdaQueryWrapper<ArticleModel> wrapper = new LambdaQueryWrapper<>();
```

然后这样使用：

```java
LambdaQueryWrapper<ArticleModel> wrapper = new LambdaQueryWrapper<>();
wrapper
  .eq(ArticleModel::getOwner, "Garfield")
  .eq(ArticleModel::getDeleteFlag, 0);
List<ArticleModel> list = articleMapper.selectList(queryWrapper);
```

对于 RequestBody 参数，通常都是定义一个类，然后在参数前面加上 `@Validated` 注解。那么对于 url query 参数，在参数前面直接加 `@NotBlank` ，然后需要在 Controller 类的前面加 `@Validated` 注解，不然不会进行校验：

```java
@RestController
@RequestMapping("/")
@Validated // 不加这个注解，不会对参数进行校验
@Slf4j
pubic class ArticleController {
  @PutMapping
  public ResponseEntity<ServerResponse> changeFavoriteStatus(
  	@NotBlank(message = "文章 ID 不能为空") @RequestParam("articleId") String articleId
  ) {
    return ServerResponse.ok(articleId);
  }
}
```

对于 RequestBody 参数，如果校验不通过，会抛出 `MethodArgumentNotValidExcetion` 异常，但是单个参数校验抛出的是 `ConstraintViolationException` 异常，需要单独进行捕获：

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(value = ConstraintViolationException.class)
  public ResponseEntity<ServerResponse> handleConstraintViolationException(
    ConstraintViolationException e
  ) {
    log.error(e.gerMessage());
    // 测试发现打印出的信息是 "changeFavoriteStatus.articleId: 文章 ID 不能为空"
    // 因此在返回给前端之前对字符串进行处理
    return ServerResponse.badRequest(e.getMessage().split(": ")[1]);
  }
}
```

Mybatis-Plus 自动填充，在插入数据时像时间等字段，希望可以自动填充，MyBatis-Plus 提供了这个功能，需要实现 `MetaObjectHandler` 接口：

```java
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
  
  @Override
  public void insertFill(MetaObject metaObject) {
    log.info("start insert fill ...");
    this.setFieldValByName("collectedTime", new Date().getTime(), metaObject);
    this.setFieldValByName("isFavorite", 0, metaObject);
  }
  
  @Override
  public void updateFill(MetaObject metaObject) {
    log.info("start update fill ...");
  }
}
```

然后定义需要自动填充的字段即可：

```java
@Data
@TableName("tb_article")
public class ArticleModel {
  
  @TableField(fill = FieldFill.INSERT)
  private Long colletedTime;
  
  @TableField(fill = FieldFill.UPDATE)
  private Long updatedTime;
  
}
```



如何自定义 mapper 方法。MyBatis-Plus 提供了默认的 BaseMapper，可以满足大部分需求，有时候需要自定义 mapper，可以像下面这样：

```java
public interface ArticleMapper extends BaseMapper<ArticleModel> {
  // 自定义 mapper 方法，用于实现软删除
  @Update("UPDATE tb_article SET delete_flag = 1 WHERE id = #{id}")
  int safeDeleteById(@Param("id") Long id);
}
```

> 注意 Mybatis-Plus 本身提供了软删除机制，原理就是多加一个字段用来表示删除状态，然后在查询的时候带上这个条件进行查询，可以选择在查询结果里过滤掉用来标记的字段

然后在 Controller 方法里面这样用：

```java
@RequiredArgsConstructor
@RestController
@RequestMapping("/")
@Validated
public class ArticleController {
  
  private final ArticleMapper articleMapper;
  
  @DeleteMapping
  public ResponseEntity<ServerResponse> deleteArticle(
    @NotBlank(message = "文章 ID 不能为空") @RequestParam("articleId") String articleId
  ) {
    int res = articleMapper.safeDeleteById(Long.valueof(articleId));
  }
  
}
```

这边涉及到字符串转为 Long 类型的操作，这边总结一下：

- 转为包装类型 Long ：`Long.valueOf(articleId)`
- 先转为包装类，然后拆箱为基本类型 long ：`Long.valueOf(articleId).longValue()`
- 直接转为基本类型 long ：`Long.parseLong(articleId)`

上面的代码有同学会问，为什么不直接接收 Long 类型的参数，就不需要类型转换了。这边本人测试了一下，如果是接收 Long 类型的参数，需要用 `@NotNull` 注解，那么对于单个参数的校验，如果不传或者传空值，会抛出 `MissingServletRequestParameterException` 异常，但是这个异常 `getMessage()` 不能获取自定义的错误信息。如果是 String 类型，就可以用 `@NotBlank` 注解，对于单个参数的校验，如果传空值，会抛出 `ConstraintViolationException` 异常，这个异常可以通过 `getMessage()` 获取到自定义错误信息。虽然 `@NotBlank` 如果参数不传，也会抛出 `MissingServletRequestParameterException` 异常，但是可以通过前端设置参数默认值来避免。



MyBatis-Plus 分页的实现。主要用了 selectPage 方法，但是使用前需要进行配置。在项目根目录下建一个 config 目录，然后建一个 `MybatisPlusInterceptor` 类，里面代码如下：

```java
// 分页插件需要进行配置，否则不生效
@Configuration
public class MybatisPlusConfig {
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
  	interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }
}
```

还需要定义一个返回的类，因为分页不光需要返回记录，还需要返回总条数，不然前端不知道一共有几条了：

```java
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ArticlePageUI implements Serializable {
  // 查询记录总数
  private Long total;
  // 查询结果集
  private List<ArticleModel> records;
}
```

> 使用 lombok 的 `@Accessors(chain = true)` 注解，可以让 setter 方法返回自身实例 this ，可以链式调用

> Serializable 接口是启用其序列化功能的接口。实现 java.io.Serializable 接口的类是可序列化的。而实际上，通过 `Ctrl` + `B` 查看源码可以发现，Serializable 是一个空接口，没有什么具体内容，当我们让实体类实现 Serializable 接口时，其实是在告诉 JVM 此类可被序列化，可被默认的序列化机制序列化。需要注意的是，Serializable 的序列化是将对象转为流的形式，便于存储和传输，可以认为就是一个“freeze”的过程，它将一个对象freeze（冷冻）住，然后进行存储，等到再次需要的时候，再将这个对象de-freeze就可以立即使用，而 JSON 序列化是将对象转为字符串，不是一个概念

然后在 Controller 类中使用：

```java
@GetMapping
public ResponseEntity<ServerResponse> getArticles(
	@RequestParam("current") Integer current,
  @RequestParam("size") Integer size
) {
  // 设置分页信息
  // 可以接受 Integer 或 Long 类型参数
  IPage<ArticleModel> articleModelPage = new Page<>(current, size);
  // 条件构造器
  LambdaQueryWrapper<ArticleModel> wrapper = new LambdaQueryWrapper<>();
  wrapper
    .eq(ArticleModel::getOwner, "Garfield")
    .eq(ArticleModel::getDeleteFlag, 0)
    .orderByDesc(ArticleModel::getCollectedTime);
  // 条件查询
  IPage<ArticleModel> articleModelIPage = articleMapper.selectPage(articleModelPage, wrapper);
  long total = articleModelIPage.getTotal();
  List<ArticleModel> records = articleModelIPage.getRecords();
  return ServerResponse.ok(new ArticlePageUI(total, records));
}
```



`@NotNull` 、`@NotEmpty` 、和 `@NotBlank` 的区别：

- `@NotNull` 不能为 null，但可以为 empty (即长度为 0)，用于 Integer 、Long 等**基本类型**的非空校验，而且被其标注的字段可以使用 `@Size` 、`@Max` 、`@Min` 对数值大小进行控制；
- `@NotEmpty` 不能为 null，且长度必须大于 0 ，用在**集合类**或者**数组**上；
- `@NotBlank` **只能用于 String 类型**，不能为 null ，而且调用 `trim()` 之后，长度必须大于 0 ；

> 校验字符串是否为空，由于 Java 的 String 是 char 类型数组，因此 String 只有包装类型，没有基本类型，实际上就是对象，所以 @NotNull 只排除了 `null` ，还可以是空字符串或者空格，`@NotEmpty` 排除了空字符串，`@NotBlank` 排除了空格









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