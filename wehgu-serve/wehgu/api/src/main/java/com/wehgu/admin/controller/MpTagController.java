package com.wehgu.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.MpComment;
import com.wehgu.admin.entities.MpStar;
import com.wehgu.admin.entities.MpTag;
import com.wehgu.admin.entities.dto.StarDTO;
import com.wehgu.admin.entities.dto.TagDTO;
import com.wehgu.admin.entities.query.StarPageQuery;
import com.wehgu.admin.entities.query.TagPageQuery;
import com.wehgu.admin.entities.vo.StarVO;
import com.wehgu.admin.entities.vo.TagVO;
import com.wehgu.admin.service.IMpStarService;
import com.wehgu.admin.service.IMpTagService;
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
 * @since 2022-04-07
 */
@RestController
@Api(tags = "标签管理-接口")
@RequestMapping("/tag")
public class MpTagController {
    @Resource
    IMpTagService iMpTagService;

    @PostMapping(value = "/getTagPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "分页获取标签列表", httpMethod = "POST", response = ResultTemplate.class, notes = "分页获取标签列表")
    public ResultTemplate getTagPage(@RequestBody TagPageQuery input) {

        Page<TagVO> page = new Page<>(input.getPage(), input.getLimit());

        Page<TagVO> result = iMpTagService.selectTags(page, input);

        return ResultTemplate.ok("获取标签列表成功", result);
    }

    @PostMapping(value = "/saveTag", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存标签", httpMethod = "POST", response = ResultTemplate.class, notes = "点赞或取消点赞")
    public ResultTemplate saveTag(@RequestBody TagDTO input) {

        MpTag tag=new MpTag();

        BeanUtil.copyProperties(input,tag);
        String UID =input.getTagUuid();

        if(StringUtils.isBlank(UID)){
            //新增
            UID= BaseUtil.getUUID();
            tag.setTagUuid(UID);
            EmptyUtil.bool(iMpTagService.save(tag),"新增失败");

        }else {
            EmptyUtil.bool(iMpTagService.update(tag,new QueryWrapper<MpTag>()
                    .eq("tag_uuid",UID)),"编辑失败");
        }


        return ResultTemplate.ok("操作成功");
    }

    @DeleteMapping(value = "/deleteTag", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除标签接口", httpMethod = "DELETE", response = ResultTemplate.class, notes = "删除标签接口")
    public ResultTemplate deleteTag(@RequestParam String UID) {

        EmptyUtil.bool(
                iMpTagService.remove(new QueryWrapper<MpTag>()
                        .eq("tag_uuid",UID)),"删除失败");

        return ResultTemplate.ok("删除成功");
    }
}
