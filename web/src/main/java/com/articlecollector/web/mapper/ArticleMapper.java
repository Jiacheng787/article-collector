package com.articlecollector.web.mapper;

import com.articlecollector.web.dbModel.ArticleModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ArticleMapper extends BaseMapper<ArticleModel> {

    // 自定义 Mapper 方法，用于实现软删除
    @Update("UPDATE tb_article SET delete_flag = 1 WHERE id = #{id}")
    int safeDeleteById(@Param("id") Long id);

}
