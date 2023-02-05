package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpPost;
import com.wehgu.admin.entities.MpTag;
import com.wehgu.admin.entities.SysRole;
import com.wehgu.admin.entities.SysUserDetail;
import com.wehgu.admin.entities.dto.BatchSavePostDTO;
import com.wehgu.admin.entities.dto.CustomerDTO;
import com.wehgu.admin.entities.dto.PostDTO;
import com.wehgu.admin.entities.query.CustomerPageQuery;
import com.wehgu.admin.entities.query.LikedPostPageQuery;
import com.wehgu.admin.entities.query.PostPageQuery;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.vo.CustomerVO;
import com.wehgu.admin.entities.vo.PostVO;
import com.wehgu.admin.entities.vo.StarVO;
import com.wehgu.admin.service.IMpPostService;
import com.wehgu.admin.service.IMpStarService;
import com.wehgu.admin.service.IMpTagService;
import com.wehgu.admin.service.IRedisService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@RestController
@Api(tags = "帖子管理-接口")
@RequestMapping("/post")
public class MpPostController {

    @Resource
    private IMpPostService iMpPostService;

    @Resource
    private IRedisService iRedisService;

    @Resource
    private IMpTagService iMpTagService;

    @PostMapping(value = "/getPostPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取帖子", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取帖子")
    public ResultTemplate getPostPage(@RequestBody PostPageQuery input) {

        Page<PostVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<PostVO> result = iMpPostService.selectPosts(page, input);

        return ResultTemplate.ok(200, "获取帖子列表成功", result);
    }

    @DeleteMapping(value = "/deletePost", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除帖子接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除帖子接口")
    public ResultTemplate deletePost(@RequestBody List<String> input) {

        for (String UID:input) {
            EmptyUtil.bool(
                    iMpPostService.remove(new QueryWrapper<MpPost>()
                            .eq("post_uuid",UID)),"删除失败");
        }

        return ResultTemplate.ok("操作成功");
    }

    @PostMapping(value = "/savePost", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存帖子接口", httpMethod = "POST", response = ResultTemplate.class, notes = "保存帖子接口")
    public ResultTemplate savePost(@RequestBody PostDTO input) {

        if(StringUtils.isBlank(input.getUserDetailUuid())){
            input.setUserDetailUuid(UserUtil.getCurrentCustomerUID());
        }

        MpPost post=new MpPost();
        BeanUtil.copyProperties(input,post);
        String UID =input.getPostUuid();


        if(StringUtils.isBlank(UID)){
            //新增
            UID= BaseUtil.getUUID();
            post.setPostUuid(UID);
            EmptyUtil.isEmpty(input.getContent(),"内容不能为空");
            EmptyUtil.isEmpty(input.getTag(),"标签不能为空");


            MpTag one=iMpTagService.getOne(new QueryWrapper<MpTag>()
                    .eq("title",input.getTag()));

            if(one==null){//新增tag
                MpTag tag=new MpTag();
                String tagUID= BaseUtil.getUUID();
                tag.setTagUuid(tagUID);
                tag.setTitle(input.getTag());
                tag.setPostNum(1);
                EmptyUtil.bool(iMpTagService.save(tag),"新增标签失败");

                post.setTagUuid(tagUID);
            }else{
                one.setPostNum(one.getPostNum()+1);
                EmptyUtil.bool(iMpTagService.updateById(one),"更新标签记录失败");
                post.setTagUuid(one.getTagUuid());
            }


            EmptyUtil.bool(iMpPostService.save(post),"发布失败");

        }else {
            EmptyUtil.bool(iMpPostService.update(post,new QueryWrapper<MpPost>()
                    .eq("post_uuid",UID)),"编辑失败");
        }


        return ResultTemplate.ok("操作成功",UID);
    }

    @PostMapping(value = "/batchSavePost", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "批量操作帖子接口", httpMethod = "POST", response = ResultTemplate.class, notes = "批量操作帖子接口")
    public ResultTemplate batchSavePost(@RequestBody BatchSavePostDTO input) {

        EmptyUtil.isEmpty(input.getPostUIDList(),"所选不能为空");

        for (String UID:input.getPostUIDList()) {
            MpPost post=new MpPost();
            BeanUtil.copyProperties(input,post);
            post.setPostUuid(UID);

            EmptyUtil.bool(
                    iMpPostService.update(post,new QueryWrapper<MpPost>()
                            .eq("post_uuid",UID)),"保存失败");
        }



        return ResultTemplate.ok("操作成功");
    }

    @PostMapping(value = "/getLikedPostPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取用户点赞记录帖子", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取用户点赞记录帖子")
    public ResultTemplate getLikedPostPage (@RequestBody List<StarVO> input) {
        List<PostVO> list=new ArrayList<>();



        for (StarVO item:input) {
            if(item.getType()!=0) continue;
            PostVO one= iMpPostService.selectByUID(item.getLinkedUuid());

            list.add(one);
        }

        return ResultTemplate.ok(200, "获取点赞帖子列表成功", list);
    }


}
