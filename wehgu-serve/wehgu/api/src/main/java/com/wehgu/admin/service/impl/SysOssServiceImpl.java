package com.wehgu.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wehgu.admin.entities.SysOss;
import com.wehgu.admin.entities.dto.AuthDTO;
import com.wehgu.admin.entities.query.OSSAuthPageQuery;
import com.wehgu.admin.entities.query.OssPageQuery;
import com.wehgu.admin.entities.vo.AuthVO;
import com.wehgu.admin.entities.vo.CarouseVO;
import com.wehgu.admin.entities.vo.OssAuthVO;
import com.wehgu.admin.entities.vo.OssVO;
import com.wehgu.admin.mapper.SysOssMapper;
import com.wehgu.admin.service.ISysOssService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wehgu.admin.utils.OSS.OSSUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generate by isMiao
 * @since 2022-03-12
 */
@Service
public class SysOssServiceImpl extends ServiceImpl<SysOssMapper, SysOss> implements ISysOssService {

    @Resource
    private SysOssMapper sysOssMapper;
    @Resource
    private OSSUtil ossUtil;

    @Override
    public Page<OssVO> selectOss(Page<OssVO> page, OssPageQuery input) {
        Page<OssVO> ossPage=sysOssMapper.selectOss(page, input);
        for (OssVO item:ossPage.getRecords()) {
            if(StringUtils.isNotBlank(item.getPath())){
                boolean exitsObject = ossUtil.isExitsObject(item.getPath());
                if(!exitsObject){
                    item.setURI("/assets/404.png");
                }else{
                    String URI = ossUtil.generatePresignedUrl(item.getPath(), 5);
                    item.setURI(URI);
                }
            }

        }
        return ossPage;
    }



    @Override
    public List<AuthVO> selectOssAuth(OSSAuthPageQuery input) {
        List<AuthVO> result=new ArrayList<>();
        HashMap<String,List<OssAuthVO>> map=new HashMap<>();
        List<OssAuthVO> list=sysOssMapper.groupByUID(input);
        for (OssAuthVO item:list) {
            if(!map.containsKey(item.getRelationUuid())){
                List<OssAuthVO> child=new ArrayList<>();
                child.add(item);
                map.put(item.getRelationUuid(),child);
            }else{
                map.get(item.getRelationUuid()).add(item);
            }
        }

        for (Map.Entry<String,List<OssAuthVO>> userAuthMap: map.entrySet()) {
            List<OssAuthVO> itemOSS=userAuthMap.getValue();

            AuthVO authVO=new AuthVO();
            BeanUtil.copyProperties(itemOSS.get(0),authVO);
            AuthDTO authDTO=new AuthDTO();
            BeanUtil.copyProperties(itemOSS.get(0),authDTO);
            authVO.setAuthDTO(authDTO);

            //获取头像
            if(authVO.getAvatar()!=null
                    &&authVO.getAvatar().startsWith("avatar")){
                boolean exitsObject = ossUtil.isExitsObject(authVO.getAvatar());
                if(!exitsObject){
                    authVO.setAvatar("/404.png");
                }
                String URI = ossUtil.generatePresignedUrl(authVO.getAvatar(), 10);
                authVO.setAvatar(URI);
            }

            List<String> studentCardURI=new ArrayList<>();
            for (OssAuthVO item:itemOSS) {
                 if(StringUtils.isNotBlank(item.getPath())){
                    boolean exitsObject = ossUtil.isExitsObject(item.getPath());
                    if(!exitsObject){
                        studentCardURI.add("/assets/404.png");
                    }else{
                        String URI = ossUtil.generatePresignedUrl(item.getPath(), 5);
                        studentCardURI.add(URI);
                    }
                }
            }

            authVO.setStudentCardURI(studentCardURI);
            result.add(authVO);
        }

        return result;
    }
}
