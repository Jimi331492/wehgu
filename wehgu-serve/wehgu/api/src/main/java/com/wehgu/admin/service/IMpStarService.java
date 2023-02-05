package com.wehgu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.jeffreyning.mybatisplus.service.IMppService;
import com.wehgu.admin.entities.MpStar;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.vo.StarVO;


import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
public interface IMpStarService extends IMppService<MpStar> {

    /**
     * 根据被点赞人的id查询点赞列表（即查询都谁给这个人点赞过）
     * @param page 分页参数
     * @param input 条件查询参数
     * @return
     */
    Page<StarVO> selectStars(Page<StarVO> page, StarPageQuery input);

    /**
     * 保存点赞记录
     * @param mpStar
     * @return MpStar
     */
    MpStar saveOne(MpStar mpStar);

    /**
     * 批量保存或修改
     * @param list
     */
    boolean saveAll(List<MpStar> list);

    /**
     * 从redis查询
     * @param input
     */
    HashMap<String,Object> queryStarFromRedis(StarPageQuery input);

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void saveStarFromRedisToDB();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void saveStarNumFromRedisToDB();


}
