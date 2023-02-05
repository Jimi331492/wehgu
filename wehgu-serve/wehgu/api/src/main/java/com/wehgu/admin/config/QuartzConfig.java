package com.wehgu.admin.config;

import com.wehgu.admin.utils.quartzUtil;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    private static final String STAR_TASK_IDENTITY = "StarTaskQuartz";

    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder.newJob(quartzUtil.class).withIdentity(STAR_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(1200)  //设置时间周期单位秒
                //.withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity(STAR_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}
