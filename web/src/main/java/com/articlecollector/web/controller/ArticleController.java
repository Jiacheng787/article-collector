package com.articlecollector.web.controller;

import com.articlecollector.web.dbModel.ArticleModel;
import com.articlecollector.web.entity.ArticleBaseInfo;
import com.articlecollector.web.entity.ArticlePageUI;
import com.articlecollector.web.mapper.ArticleMapper;
import com.articlecollector.web.responseEntity.ServerResponse;
import com.articlecollector.web.thread.AsyncService;
import com.articlecollector.web.validator.ArticleValidator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/article-collector/v1/articleInfo")
@Validated
@Slf4j
public class ArticleController {

    private final AsyncService asyncService;
    private final ArticleMapper articleMapper;

    @GetMapping
    public ResponseEntity<ServerResponse> getArticles(
            @NotBlank(message = "当前页数不能为空") @RequestParam("current") String current,
            @NotBlank(message = "每页条数不能为空") @RequestParam("size") String size
    ) {
        // 设置分页信息
        IPage<ArticleModel> articleModelPage = new Page<>(Long.parseLong(current), Long.parseLong(size));
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
        // 根据 Wrapper 条件，查询全部记录
//        List<ArticleModel> list = articleMapper.selectList(wrapper);
        return ServerResponse.ok(new ArticlePageUI(total, records));
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<ServerResponse> addArticle(@RequestBody @Validated ArticleValidator article) {
        String url = article.getUrl();
        String tag = article.getTag();
        String source = article.getSource();

        CompletableFuture<ArticleBaseInfo> baseInfo = asyncService.spider(url);
        // 等待所有任务都执行完
        // 有点像 Promise.all
        CompletableFuture.allOf(baseInfo).join();
        // 获取每个任务的返回结果
        // get 方法需要捕获 InterruptedException ，这边直接用 lombok 注解 @SneakyThrows 来搞定
//        System.out.println(baseInfo.get());

        int res = articleMapper.insert(new ArticleModel(
            "Garfield",
            baseInfo.get().getTitle(),
            baseInfo.get().getThumb(),
            url,
            tag,
            source.isBlank() ? baseInfo.get().getSource() : source
        ));

        return ServerResponse.ok(res);
    }

    @PutMapping
    public ResponseEntity<ServerResponse> changeFavoriteStatus(
            @NotNull(message = "文章 ID 不能为空") @RequestParam("articleId") Long articleId,
            @NotBlank(message = "星标状态不能为空") @RequestParam("isFavorite") String isFavorite
    ) {
        // 对于单个参数校验，需要在 Controller 类前面加 @Validated 注解
        // 同时还需要捕获 ConstraintViolationException 异常
//        log.info(articleId);
        LambdaUpdateWrapper<ArticleModel> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(ArticleModel::getId, articleId)
                .set(ArticleModel::getIsFavorite, Integer.valueOf(isFavorite));

        int res = articleMapper.update(null, updateWrapper);
        return ServerResponse.ok(res);
    }

    @DeleteMapping
    public ResponseEntity<ServerResponse> deleteArticle(
            @NotBlank(message = "文章 ID 不能为空") @RequestParam("articleId") String articleId
    ) {
        log.info(articleId);
        int res = articleMapper.safeDeleteById(Long.valueOf(articleId));
        return ServerResponse.ok(res);
    }
}
