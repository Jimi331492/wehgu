package com.wehgu.admin.common.snowId;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class IdCommon {

    @Value("${snowflake.epolldate}")
    private String date_str;

    @Resource
    private WorkDataCenterId workDataCenterId;

//todo 这个函数能不能改成时间+时间戳
    public String getUID(){
        Date date= Convert.toDate(date_str);
        Snowflake snowflake = Singleton.get(Snowflake.class, date, workDataCenterId.getWorkId(), workDataCenterId.getDataCenterId(), false);
        return String.valueOf(snowflake.nextId());
    }
}