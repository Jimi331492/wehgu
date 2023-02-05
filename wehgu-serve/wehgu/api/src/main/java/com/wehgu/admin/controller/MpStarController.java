package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpPost;
import com.wehgu.admin.entities.MpStar;
import com.wehgu.admin.entities.dto.StarDTO;
import com.wehgu.admin.entities.query.CommentPageQuery;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.vo.CommentVO;
import com.wehgu.admin.entities.vo.StarVO;
import com.wehgu.admin.service.IMpStarService;
import com.wehgu.admin.service.IRedisService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.RedisKeyUtils;
import com.wehgu.admin.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@Api(tags = "点赞相关-接口")
@RestController
@RequestMapping("/star")
public class MpStarController {
    @Resource
    private IMpStarService iMpStarService;

    @Resource
    private IRedisService iRedisService;

    @PostMapping(value = "/getStarPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取点赞记录", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取点赞记录")
    public ResultTemplate getStarPage(@RequestBody StarPageQuery input) {

        Page<StarVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<StarVO> result = iMpStarService.selectStars(page, input);

        return ResultTemplate.ok(200, "获取点赞记录成功", result);
    }

    @PostMapping(value = "/saveStar", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "点赞或取消点赞", httpMethod = "POST", response = ResultTemplate.class, notes = "点赞或取消点赞")
    public ResultTemplate saveStar(@RequestBody StarDTO input) {

        MpStar star = new MpStar();
        //如果传来的帖子UID和评论UID都为空的话 报错
        EmptyUtil.isEmpty(input.getLinkedUuid(), "被点赞的UID不能为空");
        EmptyUtil.isEmpty(input.getType(), "被点赞类型不能为空");
        EmptyUtil.isEmpty(input.getStatus(), "点赞状态不能为空");

        star.setUserDetailUuid(UserUtil.getCurrentCustomerUID());

        BeanUtil.copyProperties(input, star);

        EmptyUtil.bool(iMpStarService.saveOrUpdateByMultiId(star), "点赞失败");

        return ResultTemplate.ok(200, "操作成功");
    }

    @DeleteMapping(value = "/deleteStar", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除点赞记录接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除点赞记录接口")
    public ResultTemplate deletePost(@RequestBody StarDTO input) {

        EmptyUtil.bool(
                iMpStarService.remove(new QueryWrapper<MpStar>()
                        .eq("linked_uuid", input.getLinkedUuid())
                        .eq("user_detail_uuid", input.getUserDetailUuid())), "删除失败");

        return ResultTemplate.ok("删除成功");
    }

    @PostMapping(value = "/saveStarToRedis", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "点赞或取消点赞", httpMethod = "POST", response = ResultTemplate.class, notes = "点赞或取消点赞")
    public ResultTemplate saveStarToRedis(@RequestBody StarDTO input) {

        MpStar star = new MpStar();
        //如果传来的帖子UID和评论UID都为空的话 报错
        EmptyUtil.isEmpty(input.getLinkedUuid(), "被点赞的UID不能为空");
        EmptyUtil.isEmpty(input.getType(), "被点赞类型不能为空");
        EmptyUtil.isEmpty(input.getStatus(), "点赞状态不能为空");

        BeanUtil.copyProperties(input, star);
        star.setUserDetailUuid(UserUtil.getCurrentCustomerUID());

        MpStar one = iMpStarService.getOne(new QueryWrapper<MpStar>()
                .eq("user_detail_uuid", star.getUserDetailUuid())
                .eq("linked_uuid", star.getLinkedUuid())
                .eq("type", star.getType())
                .last("LIMIT 1"));

        StarPageQuery query = new StarPageQuery();
        query.setUserDetailUuid(star.getUserDetailUuid());
        query.setLinkedUuid(star.getLinkedUuid());
        query.setType(star.getType());

        HashMap<String,Object> starInRedis = iRedisService.queryStarFromRedis(query);

        boolean isRedisEmpty = starInRedis.isEmpty();//redis中没有
        boolean isDBEmpty = ObjectUtils.isEmpty(one);//db中没有



        //只要redis有
        if (!isRedisEmpty) {
            //输入的和redis中的不一样才计数,一样的说明上次已经记过了
            String key= RedisKeyUtils.getStarKey(query.getType(),query.getUserDetailUuid(),query.getLinkedUuid());
            MpStar inRedis= (MpStar) starInRedis.get(key);
            boolean inputEqualRedis = inRedis.getStatus().equals(input.getStatus());
            if (!inputEqualRedis) {//如果输入的和redis中不相同
                if (input.getStatus() == 1) {
                    iRedisService.incrementStarNum(input.getType(), input.getLinkedUuid());
                } else {
                    iRedisService.decrementStarNum(input.getType(), input.getLinkedUuid());
                }
            }
        }else{//redis没有

            if (!isDBEmpty) {//如果数据库有
                //输入的和DB中的不一样才计数,一样的同样说明上次已经记过了
                boolean inputEqualDB = one.getStatus().equals(input.getStatus());
                if (!inputEqualDB) {
                    if (input.getStatus() == 1) {
                        iRedisService.incrementStarNum(input.getType(), input.getLinkedUuid());
                    } else {
                        iRedisService.decrementStarNum(input.getType(), input.getLinkedUuid());
                    }
                }
            }else{//redis没有 数据库也没有

                if (input.getStatus() == 1) {
                    iRedisService.incrementStarNum(input.getType(), input.getLinkedUuid());
                }
            }
        }

        iRedisService.saveStarToRedis(star);


        return ResultTemplate.ok(200, "操作成功");
    }

}
