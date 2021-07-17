package com.articlecollector.web.entity;

import com.articlecollector.web.dbModel.ArticleModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true) // setter 方法返回自身实例，可以链式调用
@AllArgsConstructor
// Serializable是一个对象序列化的接口
// 一个类只有实现了Serializable接口，它的对象才是可序列化的
// 实际上，Serializable是一个空接口，没有什么具体内容，它的目的只是简单的标识一个类的对象可以被序列化
public class ArticlePageUI implements Serializable {
    // 查询记录总数
    private Long total;
    // 查询结果集
    private List<ArticleModel> records;
}
