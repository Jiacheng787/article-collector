package com.articlecollector.web.dbModel;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyBatis-Plus 自动填充
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    // 插入数据时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ...");
        this.setFieldValByName("collectedTime", new Date().getTime(), metaObject);
        this.setFieldValByName("isFavorite", 0, metaObject);
        this.setFieldValByName("isRead", 0, metaObject);
        this.setFieldValByName("deleteFlag", 0, metaObject);
    }

    // 更新数据时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ...");
    }
}
