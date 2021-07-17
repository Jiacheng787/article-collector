package com.articlecollector.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.articlecollector.web.mapper")
public class ArticleCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleCollectorApplication.class, args);
    }

}
