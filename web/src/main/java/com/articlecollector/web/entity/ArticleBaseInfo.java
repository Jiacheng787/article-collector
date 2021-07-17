package com.articlecollector.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleBaseInfo {
    private String title;
    private String thumb;
    private String source;
}
