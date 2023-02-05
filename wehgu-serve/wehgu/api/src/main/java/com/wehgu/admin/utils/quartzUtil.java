package com.wehgu.admin.utils;

import com.wehgu.admin.service.IMpStarService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 点赞的定时任务
 */
@Slf4j
public class quartzUtil extends QuartzJobBean {
    @Resource
    private IMpStarService iMpStarService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) throws JobExecutionException {
        log.info("quartz Task-------- {}", sdf.format(new Date()));

        //将 Redis 里的点赞信息同步到数据库里
        iMpStarService.saveStarFromRedisToDB();
        iMpStarService.saveStarNumFromRedisToDB();
    }
}
