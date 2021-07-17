package com.articlecollector.web.dbModel;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

// 如果不加 @TableName 注解，表名默认为类名转为下划线形式
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("tb_article")
public class ArticleModel {
    // 使用 MyBatis-Plus 的 insert 方法，在底层会默认生成一个 Long 类型的 UUID
    // 如果数据库字段类型设为 int 或者设置了自增 id 会导致插入失败
    // Java 中的 Long 类型字段表示的范围比 JS 的 number 类型更大
    // 在序列化时如果不处理直接返回给前端，前端在反序列化时会损失精度
    // 使用 SpringBoot 默认的 jackson 对 Long 类型 id 在序列化时转为字符串
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private final String owner;
    private final String title;
    private final String thumb;
    private final String url;
    private final String tag;
    private final String source;

    // 定义自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private Long collectedTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer isFavorite;

    @TableField(fill = FieldFill.INSERT)
    private Integer isRead;

    @TableField(fill = FieldFill.INSERT)
    private Integer deleteFlag;
}
