package com.wehgu.admin;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import com.wehgu.admin.config.AuthProperties;
import com.wehgu.admin.utils.ApplicationContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableConfigurationProperties({AuthProperties.class})
@EnableTransactionManagement
@EnableOpenApi
@MapperScan("com.wehgu.admin.mapper")
@EnableCaching
@EnableMPP
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);
        ApplicationContextUtil.setApplicationContext(app);
    }

}
