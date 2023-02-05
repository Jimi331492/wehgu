package com.wehgu.admin.config.security.filter;



import com.wehgu.admin.common.Constant;
import com.wehgu.admin.config.AuthProperties;
import com.wehgu.admin.config.Constants;
import com.wehgu.admin.config.security.chooser.InspectionSolver;
import com.wehgu.admin.config.security.chooser.InspectionSolverChooser;
import com.wehgu.admin.config.security.impl.UserDetailsServiceImpl;
import com.wehgu.admin.config.security.login.UserAuthenticationEntryPoint;
import com.wehgu.admin.utils.MultiReadHttpServletRequest;
import com.wehgu.admin.utils.MultiReadHttpServletResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * <p> 访问鉴权 - 每次访问接口都会经过此 </p>
 */
@Slf4j
@Component
public class MyAuthenticationFilter extends OncePerRequestFilter {


    @Resource
    private UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    @Resource
    private InspectionSolverChooser chooser;
    @Resource
    private AuthProperties myProperties;

    @Resource
    private final UserDetailsServiceImpl userDetailsService;

    protected MyAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        log.info("请求头类型： " + request.getContentType());
        String requestURI = request.getServletPath();
        String path = request.getServletPath().split("/")[1];
        log.info("请求路径： " + requestURI);
        log.info(requestURI+"当前路径请求");
        log.info("是否为开放接口："+myProperties.getAuth().getNotLoginUrl().contains(requestURI));

        //swagger路径
        if ((path.equals("swagger-ui") || path.equals("webjars") || path.equals("swagger-resources") || path.equals("v3") || path.equals("v2"))) {
            filterChain.doFilter(request, response);
            return;
        }
        //不需要登陆就可以访问的接口
        if (myProperties.getAuth().getNotLoginUrl().contains(requestURI))
        {
            filterChain.doFilter(request, response);
            return;
        }

        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);
        MultiReadHttpServletResponse wrappedResponse = new MultiReadHttpServletResponse(response);

        logRequestBody(wrappedRequest);

        try {
            /*
              电脑登录后将token储存cookie中 从cookie获取token验证
              微信公众号openID储存header中 从header中获取openID验证
              微信小程序unionID存储header中 从header中获取unionID验证
             */
            String userAgent = wrappedRequest.getHeader("user-agent").toLowerCase();
            if (userAgent.toLowerCase().contains("micromessenger") || userAgent.toLowerCase().contains("iphone")) {
                //微信端请求
                String sessionId = wrappedRequest.getHeader(Constant.WX_SP_SESSIONID_HEADER);
                String unionid = wrappedRequest.getHeader(Constant.WX_SP_UNIONID_HEADER);

                if (StringUtils.isNotBlank(sessionId) || StringUtils.isNotBlank(unionid)){
                    //微信小程序
                    InspectionSolver choose = chooser.choose(Constant.WE_CHAT_MINI_PROGRAM);
                    UsernamePasswordAuthenticationToken authenticationToken = choose.solve(wrappedRequest,wrappedResponse, userDetailsService);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }else {
                    //微信公众号
                    InspectionSolver choose = chooser.choose(Constant.WE_CHAT_PUBLIC_ACCOUNT);
                    UsernamePasswordAuthenticationToken authenticationToken = choose.solve(wrappedRequest,wrappedResponse, userDetailsService);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }else {
                //PC端请求
                InspectionSolver choose = chooser.choose(Constant.PC);
                UsernamePasswordAuthenticationToken authenticationToken = choose.solve(wrappedRequest,wrappedResponse, userDetailsService);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } catch (ExpiredJwtException e) {
            // jwt令牌过期
            SecurityContextHolder.clearContext();
            this.userAuthenticationEntryPoint.commence(wrappedRequest, response, null);
        } catch (AuthenticationException e) {
            //安全验证失败
            SecurityContextHolder.clearContext();
            this.userAuthenticationEntryPoint.commence(wrappedRequest, response, e);
        } finally {
            logResponseBody(wrappedRequest, wrappedResponse);
        }

    }

    private void logRequestBody(MultiReadHttpServletRequest request) {
        if (request != null) {
            try {
                String bodyJson = request.getBodyJsonStrByJson(request);
                String url = request.getRequestURI().replace("//", "/");
                System.out.println("-------------------------------- 请求url: " + url + " --------------------------------");
                Constants.URL_MAPPING_MAP.put(url, url);
                log.info("`{}` 接收到的参数: {}", url, bodyJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void logResponseBody(MultiReadHttpServletRequest request, MultiReadHttpServletResponse response) {
        if (response != null) {
            byte[] buf = response.getBody();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, response.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    payload = "[unknown]";
                }
                log.info("`{}`  耗时:{}ms  返回的参数: {}", Constants.URL_MAPPING_MAP.get(request.getRequestURI()), LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli(), payload);
            }
        }
    }

}
