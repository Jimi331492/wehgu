package com.wehgu.admin.common.snowId;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "otherconfig")
@EnableConfigurationProperties
@Component
public class WorkDataCenterId {

    private Long workId;

    private Long dataCenterId;
}