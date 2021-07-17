package com.articlecollector.web.validator;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ArticleValidator {
    @NotBlank(message = "文章链接不能为空")
    private String url;
    @NotBlank(message = "标签不能为空")
    private String tag;

    private String source;
}
