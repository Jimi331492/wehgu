package com.wehgu.admin.utils;

import com.wehgu.admin.config.security.dto.SecurityUser;
import com.wehgu.admin.entities.MpComment;
import com.wehgu.admin.entities.SysPermission;
import com.wehgu.admin.entities.vo.CommentVO;
import com.wehgu.admin.service.ISysUserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class UserUtil {


    @Resource
    private ISysUserService userCaseService;

    public static String getLoginUserUID(){
        SecurityUser loginUser = getLoginUser();
        if (loginUser != null){
            return loginUser.getCurrentUserInfo().getUserUuid();
        }else {
            throw new RuntimeException("当前登录用户的UID不存在");
        }
    }

    public static String getCurrentCustomerUID(){
        SecurityUser loginUser = getLoginUser();
        if (loginUser != null){
            return loginUser.getCurrentUserInfo().getUserDetailUuid();
        }else {
            throw new RuntimeException("当前小程序用户的UID不存在");
        }
    }

    public static String getLoginUserType(){
        SecurityUser loginUser = getLoginUser();
        if (loginUser != null){
            return loginUser.getType();
        }else {
            throw new RuntimeException("当前登录用户的用户类型不存在");
        }
    }


    public static String getUserDept(){
        SecurityUser loginUser = getLoginUser();
        if (loginUser != null ){
            if (loginUser.getCurrentUserInfo().getDeptName() == null){
                throw new RuntimeException("当前登录用户部门为空");
            }
            return loginUser.getCurrentUserInfo().getDeptName();
        }else {
            throw new RuntimeException("当前登录用户不存在");
        }
    }


    public  String getUserTelephone(){
        SecurityUser loginUser = getLoginUser();
        if (loginUser != null){
            if (loginUser.getCurrentUserInfo().getTelephone() == null){
                throw new RuntimeException("当前登录用户的手机号为空");
            }
            return loginUser.getCurrentUserInfo().getTelephone();
        }else {
            throw new RuntimeException("当前登录用户不存在");
        }
    }
    public static SecurityUser getLoginUser() {

        /**
         SecurityContextHolder.getContext()获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
         总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
         获取当前认证了的 principal(当事人),或者 request token (令牌)
         如果没有认证，会是 null,该例子是认证之后的情况
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //有登陆用户就返回登录用户，没有就返回null
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                return (SecurityUser) authentication.getPrincipal();
            }
        }

        return null;
    }

    public static List<SysPermission> permListToTree(List<SysPermission> permList) {

        List<SysPermission> permTree=new ArrayList<>();
        // 先将List按parentId分组，分别放在一个map中
        Map<Integer, List<SysPermission>> byParentId = permList.stream().collect(Collectors.groupingBy(SysPermission::getParentId));
        // 将其放入对应的组织下
        permList.forEach(item -> item.setChildren(byParentId.get(item.getPermId())));
        // 只留下根节点，本例中根节点父ID为-1。具体视情况而定。
        permList.stream().filter(item -> item.getParentId().equals(0)).forEach(permTree::add);

        //如果children为空 给他个空数组
//        for (SysPermission item:permTree) {
//            if(item.getChildren()==null){
//                List<SysPermission> arr= new ArrayList<>();
//                item.setChildren(arr);
//            }
//        }

        return permTree;
    }


    public static List<CommentVO> commentListToTree(List<CommentVO> commentList) {

        List<CommentVO> commentTree=new ArrayList<>();
        // 先将List按parentId分组，分别放在一个map中
        Map<Integer, List<CommentVO>> byParentId = commentList.stream().collect(Collectors.groupingBy(CommentVO::getParentId));
        // 将其放入对应的组织下
        commentList.forEach(item -> item.setChildren(byParentId.get(item.getCommentId())));
        // 只留下根节点，本例中根节点父ID为-1。具体视情况而定。
        commentList.stream().filter(item -> item.getParentId().equals(0)).forEach(commentTree::add);


        return commentTree;
    }



}