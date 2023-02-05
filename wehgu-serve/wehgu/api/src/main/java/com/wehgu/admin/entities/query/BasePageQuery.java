package com.wehgu.admin.entities.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * <p> 基础分页检索参数 </p>
 *
 */
@ApiModel
public class BasePageQuery  {
    @ApiModelProperty("")
    private Integer page;
    @ApiModelProperty("")
    private Integer limit;
//    @ApiModelProperty("")
//    private LocalDateTime startTime;
//    @ApiModelProperty("")
//    private LocalDateTime endTime;
//
//    public LocalDateTime setStartTime(LocalDateTime startTime) {
//        return this.startTime=startTime;
//    }
//
//    public LocalDateTime getStartTime() {
//        return startTime;
//    }
//
//    public LocalDateTime setEndTime(LocalDateTime endTime) {
//        return this.endTime=endTime;
//    }
//
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }



    public Integer getPage() {
        return page == null ? 1 : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit == null ? Integer.MAX_VALUE : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}