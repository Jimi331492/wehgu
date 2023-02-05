package com.wehgu.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p> MyBatisPlus自定义字段自动填充处理类 - 实体类中使用 @TableField注解 </p>
 *
 * @description: 注意前端传值时要为null
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    /**
     * 创建时间
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter("addTime")) {
            setFieldValByName("addTime", LocalDateTime.now(), metaObject);
        }

    }

    /**
     * 最后一次更新时间
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("updateTime")) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }

}