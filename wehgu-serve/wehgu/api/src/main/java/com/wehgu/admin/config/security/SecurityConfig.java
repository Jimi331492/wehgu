package com.wehgu.admin.config.security;

import com.wehgu.admin.config.AuthProperties;
import com.wehgu.admin.config.security.filter.MyAuthenticationFilter;
import com.wehgu.admin.config.security.filter.UrlFilterInvocationSecurityMetadataSource;
import com.wehgu.admin.config.security.filter.UserAuthenticationProcessingFilter;
import com.wehgu.admin.config.security.filter.WechatAuthenticationProcessingFilter;
import com.wehgu.admin.config.security.handler.UrlAccessDecisionManager;
import com.wehgu.admin.config.security.handler.UrlAccessDeniedHandler;
import com.wehgu.admin.config.security.login.UserAuthenticationEntryPoint;
import com.wehgu.admin.config.security.login.customer.CustomerAuthenticationFailureHandler;
import com.wehgu.admin.config.security.login.customer.CustomerAuthenticationProvider;
import com.wehgu.admin.config.security.login.customer.CustomerAuthenticationSuccessHandler;
import com.wehgu.admin.config.security.login.user.UserAuthenticationFailureHandler;
import com.wehgu.admin.config.security.login.user.UserAuthenticationProvider;
import com.wehgu.admin.config.security.login.user.UserAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

/**
 * <p> Security 核心配置类 </p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    AuthProperties authProperties;

    @Resource//用户登录成功的handler
    private UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;

    @Resource//用户登录失败的handler
    private UserAuthenticationFailureHandler userAuthenticationFailureHandler;

    @Resource//用户登录成功的handler
    private CustomerAuthenticationSuccessHandler customerAuthenticationSuccessHandler;

    @Resource//用户登录失败的handler
    private CustomerAuthenticationFailureHandler customerAuthenticationFailureHandler;



    /**
     * 访问鉴权 - 认证token、签名...的filter
     */
    private final MyAuthenticationFilter myAuthenticationFilter;
    /**
     * 访问权限认证异常处理 用户
     */
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final CustomerAuthenticationProvider customerAuthenticationProvider ;

    // <=====================上面是登录认证相关  下面为url权限相关  ====================================>
    /**
     * 获取访问url所需要的角色信息
     */
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
    /**
     * 认证权限处理 - 将上面所获得角色权限与当前登录用户的角色做对比，如果包含其中一个角色即可正常访问
     */
    private final UrlAccessDecisionManager urlAccessDecisionManager;
    /**
     * 自定义访问无权限接口时403响应内容
     */
    private final UrlAccessDeniedHandler urlAccessDeniedHandler;

    public SecurityConfig(MyAuthenticationFilter myAuthenticationFilter,
                          UserAuthenticationEntryPoint userAuthenticationEntryPoint,
                          UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource,
                          UrlAccessDeniedHandler urlAccessDeniedHandler,
                          UrlAccessDecisionManager urlAccessDecisionManager,
                          UserAuthenticationProvider userAuthenticationProvider,
                          CustomerAuthenticationProvider customerAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.customerAuthenticationProvider = customerAuthenticationProvider;
        this.myAuthenticationFilter = myAuthenticationFilter;
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
        this.urlFilterInvocationSecurityMetadataSource = urlFilterInvocationSecurityMetadataSource;
        this.urlAccessDeniedHandler = urlAccessDeniedHandler;
        this.urlAccessDecisionManager = urlAccessDecisionManager;
    }

    @Bean
    public PasswordEncoder passWordEncoder(){
        return new BCryptPasswordEncoder();
    };

    @Override
    protected AuthenticationManager authenticationManager(){
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(customerAuthenticationProvider,userAuthenticationProvider));
        //不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }

    @Bean
    UserAuthenticationProcessingFilter userAuthenticationFilter() throws Exception {
        UserAuthenticationProcessingFilter filter = new UserAuthenticationProcessingFilter();
        filter.setAuthenticationSuccessHandler(userAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(userAuthenticationFailureHandler);
        filter.setFilterProcessesUrl("/login");
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

// @Bean
// WechatAuthenticationProcessingFilter customerAuthenticationFilter() throws Exception {
//     WechatAuthenticationProcessingFilter filter = new WechatAuthenticationProcessingFilter();
//     filter.setAuthenticationSuccessHandler(customerAuthenticationSuccessHandler);
//     filter.setAuthenticationFailureHandler(customerAuthenticationFailureHandler);
//     filter.setFilterProcessesUrl("/mp_Login");
//     filter.setAuthenticationManager(authenticationManagerBean());
//     return filter;
// }


    /**
     * 权限配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/**").authorizeRequests();

        // 禁用CSRF 开启跨域
        http.csrf().disable().cors();
        // 未登录认证异常
        http.exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint);
        // 登录过后访问无权限的接口时自定义403响应内容
        http.exceptionHandling().accessDeniedHandler(urlAccessDeniedHandler);

        http.logout().logoutUrl("/logout");
        // url权限认证处理
        registry.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
                o.setAccessDecisionManager(urlAccessDecisionManager);
                return o;
            }
        });

        // 不创建会话 - 即通过前端传token到后台过滤器中验证是否存在访问权限
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 标识只能在 服务器本地ip[127.0.0.1或localhost] 访问 `/home` 这个接口，其他ip地址无法访问
        registry.antMatchers("/home").hasIpAddress("127.0.0.1");

        // 允许匿名的url - 可理解为放行接口 - 除配置文件忽略url以外，其它所有请求都需经过认证和授权
        for (String url : authProperties.getAuth().getIgnoreUrls()) {
            registry.antMatchers(url).permitAll();
        }
        // OPTIONS(选项)：查找适用于一个特定网址资源的通讯选择。 在不需执行具体的涉及数据传输的动作情况下， 允许客户端来确定与资源相关的选项以及 / 或者要求， 或是一个服务器的性能
        registry.antMatchers(HttpMethod.OPTIONS, "/**").denyAll();
        // 自动登录 - cookie储存方式
        registry.and().rememberMe();
        // 其余所有请求都需要认证
        registry.anyRequest().authenticated();
        // 防止iframe 造成跨域
        registry.and().headers().frameOptions().disable();

        // 自定义顾客过滤器在登录时认证用户名、密码
        http.addFilterAt(userAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(myAuthenticationFilter, BasicAuthenticationFilter.class);
        // 自定义管理员过滤器在登录时认证用户名、密码  ************到时候在加
//        http.addFilterAt(customerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(myAuthenticationFilter, BasicAuthenticationFilter.class);
    }

    /**
     * 忽略拦截url或静态资源文件夹 - web.ignoring(): 会直接过滤该url - 将不会经过Spring Security过滤器链
     * http.permitAll(): 不会绕开springsecurity验证，相当于是允许该路径通过
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET,
                "/favicon.ico",
                "/**/*.png",
                "/**/*.ttf",
                "/*.html",
                "/**/*.css",
                "/**/*.js",
                // -- swagger ui
                "/swagger-ui.html",
                "/swagger-ui/*",
                "/doc.html/*",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars/**");
    }


}
