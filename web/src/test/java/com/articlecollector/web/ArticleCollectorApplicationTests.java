package com.articlecollector.web;

import com.articlecollector.web.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@RequiredArgsConstructor
@SpringBootTest
class ArticleCollectorApplicationTests {

    private final ArticleMapper articleMapper;

    @Test
    void updateField() {
        Integer res = articleMapper.selectCount(null);
        System.out.println(res);
    }

}
