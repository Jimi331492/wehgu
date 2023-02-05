package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpComment;
import com.wehgu.admin.entities.MpPost;
import com.wehgu.admin.entities.dto.CommentDTO;
import com.wehgu.admin.entities.dto.PostDTO;
import com.wehgu.admin.entities.query.CommentPageQuery;
import com.wehgu.admin.entities.query.PostPageQuery;
import com.wehgu.admin.entities.vo.CommentVO;
import com.wehgu.admin.entities.vo.PostVO;
import com.wehgu.admin.service.IMpCommentService;
import com.wehgu.admin.service.IMpPostService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-21
 */
@RestController
@Api(tags = "评论-接口")
@RequestMapping("/comment")
public class MpCommentController {
    @Resource
    private IMpCommentService iMpCommentService;

    @Resource
    private IMpPostService iMpPostService;

    @PostMapping(value = "/getCommentPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取评论", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取评论")
    public ResultTemplate getCommentPage(@RequestBody CommentPageQuery input) {

        Page<CommentVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<CommentVO> result = iMpCommentService.selectComments(page, input);

        return ResultTemplate.ok(200, "获取评论列表成功", result);
    }

    @DeleteMapping(value = "/deleteComment", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除评论接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除评论接口")
    public ResultTemplate deleteComment(@RequestParam String UID) {

        MpComment one=iMpCommentService.getOne(new QueryWrapper<MpComment>()
                .eq("comment_uuid",UID)
                .last("LIMIT 1"));
        EmptyUtil.isEmpty(one,"该评论不存在");
        String postUID=one.getPostUuid();
        iMpCommentService.removeById(one.getCommentId());
        iMpPostService.decrementCommentNum(postUID);

        return ResultTemplate.ok("删除成功");
    }

    @PostMapping(value = "/saveComment", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存评论接口", httpMethod = "POST", response = ResultTemplate.class, notes = "保存评论接口")
    public ResultTemplate saveComment(@RequestBody CommentDTO input) {

        MpComment comment=new MpComment();
        BeanUtil.copyProperties(input,comment);
        String UID =input.getCommentUuid();

        if(StringUtils.isBlank(UID)){
            //新增
            UID= BaseUtil.getUUID();
            comment.setCommentUuid(UID);
            EmptyUtil.bool(iMpCommentService.save(comment),"评论失败");
            iMpPostService.incrementCommentNum(input.getPostUuid());
        }else {
            EmptyUtil.bool(iMpCommentService.update(comment,new QueryWrapper<MpComment>()
                    .eq("comment_uuid",UID)),"修改失败");
        }


        return ResultTemplate.ok("操作成功",UID);
    }
}
