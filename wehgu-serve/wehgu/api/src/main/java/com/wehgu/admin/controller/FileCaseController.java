package com.wehgu.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wehgu.admin.common.ResultTemplate;
import com.wehgu.admin.entities.SysOss;
import com.wehgu.admin.entities.dto.PicturePathSaveDTO;
import com.wehgu.admin.entities.query.PicturePathQuery;
import com.wehgu.admin.entities.dto.PicturePathSequence;
import com.wehgu.admin.service.ISysOssService;
import com.wehgu.admin.utils.BaseUtil;
import com.wehgu.admin.utils.EmptyUtil;
import com.wehgu.admin.utils.OSS.ImageUtil;
import com.wehgu.admin.utils.OSS.OSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangj
 * @since 2019-07-12
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件处理-接口")
public class FileCaseController {

	@Resource
	private OSSUtil ossUtil;
	@Resource
	private ImageUtil imageUtils;

	@Resource
	private ISysOssService iSysOssService;

	@ApiOperation("根据相对路径获取网络路径")
	@PostMapping(value = "getURIByPath")
	public ResultTemplate getImageURIByPath(@RequestBody String path){
		if(StringUtils.isBlank(path)){
			return ResultTemplate.err("图片路径不能为空");
		}
		boolean exitsObject = ossUtil.isExitsObject(path);
		if(!exitsObject){
			return ResultTemplate.err("该图片已不存在");
		}
		String URI = ossUtil.generatePresignedUrl(path, 5);
		return ResultTemplate.ok("获取图标网络路径成功",URI);
	}

	@ApiOperation("图片路径存储")
	@PostMapping(value = "savePicturePath")
	public ResultTemplate savePicturePath(@RequestBody PicturePathSaveDTO input) {
		EmptyUtil.isEmpty(input.getEntityName(),"图片对应实体必填");
		EmptyUtil.isEmpty(input.getEntityUID(),"图片对应实体UID必填");
		EmptyUtil.isEmpty(input.getPathSequenceList(),"路径必填");

		List<SysOss> sysOssList = new ArrayList<>();
		for (int i =0;i<input.getPathSequenceList().size();i++){
			PicturePathSequence ppsItem=input.getPathSequenceList().get(i);
			EmptyUtil.isEmpty(ppsItem.getPath(),"路径必填");
			if(ppsItem.getSequence()==null){
				ppsItem.setSequence(i);
			}

			SysOss ossItem = new SysOss();
			ossItem.setOssUuid(BaseUtil.getUUID());
			ossItem.setPath(input.getPathSequenceList().get(i).getPath()) ;
			ossItem.setRelationUuid(input.getEntityUID());
			ossItem.setStoreTypeTable(input.getEntityName() ) ;
			ossItem.setSequence(ppsItem.getSequence()) ;

			sysOssList.add(ossItem);
		}

		EmptyUtil.bool(iSysOssService.saveBatch(sysOssList), "图片路径存储失败");
		return ResultTemplate.ok("操作成功");
	}

	@ApiOperation("获取图片路径")
	@PostMapping("/getPicturePath")
	public ResultTemplate getPicture(@RequestBody PicturePathQuery input) {
		List<String> picturePathList = new ArrayList<>();
		EmptyUtil.isEmpty(input.getEntityName(), "查询图片对应实体必填");
		EmptyUtil.isEmpty(input.getEntityUID(), "图片对应实体UID必填");


		List<SysOss> sysOssList = iSysOssService.list(new QueryWrapper<SysOss>()
				.eq("store_type_table",input.getEntityName())
				.eq("relation_uuid",input.getEntityUID()));

		EmptyUtil.isEmpty(sysOssList,"这个UID没有图片数据");

		for (SysOss bp : sysOssList) {
			String pa = bp.getPath();
			picturePathList.add(pa);
		}


		return ResultTemplate.ok(200,"查询成功",picturePathList);
	}



	@ApiOperation("图片上传")
	@PostMapping( value ="uploadPicture",consumes = "multipart/form-data",headers = "content-type=multipart/form-data")
	public ResultTemplate uploadPicture(@RequestPart(name = "picture") MultipartFile picture, @RequestParam(name = "pictype") String pictype) {
		EmptyUtil.isEmpty(pictype,"上传类型必填");

		String temporaryPath=imageUtils.savePictureToOss(pictype, picture, "");
		return ResultTemplate.ok("上传成功",temporaryPath);
	}


	@ApiOperation("视频上传")
	@PostMapping( value ="uploadVideo",consumes = "multipart/form-data",headers = "content-type=multipart/form-data")
	public ResultTemplate uploadVideo(@RequestPart(name = "picture")MultipartFile picture, @RequestParam(name = "pictype") String pictype) {

		EmptyUtil.isEmpty(pictype,"上传类型必填");

		String temporaryPath=imageUtils.saveVideoToOss(pictype, picture, "");

		return ResultTemplate.ok("上传成功",temporaryPath);
	}

}