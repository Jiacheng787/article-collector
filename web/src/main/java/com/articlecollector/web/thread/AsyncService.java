package com.articlecollector.web.thread;

import com.articlecollector.web.entity.ArticleBaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncService {
    @Async("spiderExecutor")
    public CompletableFuture<ArticleBaseInfo> spider(String url) {
        String title = "", thumb = "", source = "";
        try {
            Document document = Jsoup.connect(url).get();
            Elements metas = document.getElementsByTag("meta");

            if(url.startsWith("https://juejin.cn")) {
                // 掘金文章
                source = "掘金";
                for (Element meta : metas) {
                    if (meta.attr("itemprop").equals("headline")) {
                        title = meta.attr("content");
                    }
                    if (meta.attr("itemprop").equals("image")) {
                        thumb = meta.attr("content");
                    }
                }
            } else if(url.startsWith("https://mp.weixin.qq.com")) {
                // 微信公众号文章
                source = "微信公众号";
                for (Element meta : metas) {
                    if (meta.attr("property").equals("og:title")) {
                        title = meta.attr("content");
                    }
                    if (meta.attr("property").equals("og:image")) {
                        thumb = meta.attr("content");
                    }
                }
            }
        } catch (IOException e) {
            log.error("spider executor error:", e);
        }
        return CompletableFuture.completedFuture(new ArticleBaseInfo(title, thumb, source));
    }
}
