package com.muyingmall.common.security.config;

import com.muyingmall.common.security.crypto.PasswordEncoder;
import com.muyingmall.common.security.jwt.JwtUtils;
import com.muyingmall.common.security.properties.SecurityProperties;
import com.muyingmall.common.security.utils.AuthenticationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security模块自动配置类
 * 
 * <p>提供Security模块的自动配置功能，包括：</p>
 * <ul>
 *   <li>JWT工具类配置</li>
 *   <li>密码加密服务配置</li>
 *   <li>认证工具类配置</li>
 *   <li>CORS跨域配置</li>
 *   <li>安全属性配置</li>
 * </ul>
 * 
 * <p>自动配置条件：</p>
 * <ul>
 *   <li>Web应用环境</li>
 *   <li>Security模块启用（muying.security.enabled=true）</li>
 *   <li>相关类存在于classpath中</li>
 * </ul>
 * 
 * <p>配置示例：</p>
 * <pre>{@code
 * # 启用Security模块
 * muying.security.enabled=true
 * 
 * # JWT配置
 * muying.security.jwt.secret=your-secret-key
 * muying.security.jwt.expiration=86400
 * 
 * # CORS配置
 * muying.security.cors.enabled=true
 * muying.security.cors.allowed-origins=http://localhost:3000
 * }</pre>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "muying.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SecurityProperties.class)
@ComponentScan(basePackages = "com.muyingmall.common.security")
public class SecurityAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityAutoConfiguration.class);

    /**
     * 安全配置属性
     */
    private final SecurityProperties securityProperties;

    /**
     * 构造函数
     *
     * @param securityProperties 安全配置属性
     */
    public SecurityAutoConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        log.info("Security模块自动配置开始初始化...");
    }

    /**
     * JWT工具类Bean配置
     * 
     * <p>当JWT相关类存在且未定义JwtUtils Bean时自动创建。</p>
     *
     * @return JWT工具类实例
     */
    @Bean
    @ConditionalOnClass(name = "io.jsonwebtoken.Jwts")
    @ConditionalOnMissingBean(JwtUtils.class)
    public JwtUtils jwtUtils() {
        log.info("配置JWT工具类Bean");
        return new JwtUtils();
    }

    /**
     * 密码加密服务Bean配置
     * 
     * <p>当Spring Security相关类存在且未定义PasswordEncoder Bean时自动创建。</p>
     *
     * @return 密码加密服务实例
     */
    @Bean
    @ConditionalOnClass(name = "org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder")
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        log.info("配置密码加密服务Bean");
        return new PasswordEncoder();
    }

    /**
     * 认证工具类Bean配置
     * 
     * <p>当Servlet相关类存在且未定义AuthenticationUtils Bean时自动创建。</p>
     *
     * @return 认证工具类实例
     */
    @Bean
    @ConditionalOnClass(name = "jakarta.servlet.http.HttpServletRequest")
    @ConditionalOnMissingBean(AuthenticationUtils.class)
    public AuthenticationUtils authenticationUtils() {
        log.info("配置认证工具类Bean");
        return new AuthenticationUtils();
    }

    /**
     * CORS跨域配置
     * 
     * <p>当启用CORS配置时自动配置跨域访问规则。</p>
     *
     * @return WebMvcConfigurer实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "muying.security.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnClass(name = "org.springframework.web.servlet.config.annotation.WebMvcConfigurer")
    public WebMvcConfigurer corsConfigurer() {
        log.info("配置CORS跨域访问规则");
        
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                SecurityProperties.Cors corsConfig = securityProperties.getCors();
                
                if (corsConfig.isEnabled()) {
                    log.info("启用CORS跨域配置");
                    
                    registry.addMapping("/**")
                            .allowedOriginPatterns(corsConfig.getAllowedOrigins().toArray(new String[0]))
                            .allowedMethods(corsConfig.getAllowedMethods().toArray(new String[0]))
                            .allowedHeaders(corsConfig.getAllowedHeaders().toArray(new String[0]))
                            .exposedHeaders(corsConfig.getExposedHeaders().toArray(new String[0]))
                            .allowCredentials(corsConfig.isAllowCredentials())
                            .maxAge(corsConfig.getMaxAge());
                    
                    log.info("CORS配置完成 - 允许的源: {}, 允许的方法: {}", 
                            corsConfig.getAllowedOrigins(), corsConfig.getAllowedMethods());
                } else {
                    log.info("CORS跨域配置已禁用");
                }
            }
        };
    }

    /**
     * Security配置信息Bean
     * 
     * <p>提供Security模块的配置信息，用于其他组件获取配置。</p>
     *
     * @return SecurityConfigInfo实例
     */
    @Bean
    @ConditionalOnMissingBean(SecurityConfigInfo.class)
    public SecurityConfigInfo securityConfigInfo() {
        log.info("配置Security配置信息Bean");
        return new SecurityConfigInfo(securityProperties);
    }

    /**
     * Security配置信息类
     * 
     * <p>封装Security模块的配置信息，提供便捷的访问方法。</p>
     */
    public static class SecurityConfigInfo {
        
        private final SecurityProperties properties;

        public SecurityConfigInfo(SecurityProperties properties) {
            this.properties = properties;
        }

        /**
         * 获取JWT配置
         *
         * @return JWT配置
         */
        public SecurityProperties.Jwt getJwtConfig() {
            return properties.getJwt();
        }

        /**
         * 获取密码配置
         *
         * @return 密码配置
         */
        public SecurityProperties.Password getPasswordConfig() {
            return properties.getPassword();
        }

        /**
         * 获取匿名访问配置
         *
         * @return 匿名访问配置
         */
        public SecurityProperties.Anonymous getAnonymousConfig() {
            return properties.getAnonymous();
        }

        /**
         * 获取CORS配置
         *
         * @return CORS配置
         */
        public SecurityProperties.Cors getCorsConfig() {
            return properties.getCors();
        }

        /**
         * 获取会话配置
         *
         * @return 会话配置
         */
        public SecurityProperties.Session getSessionConfig() {
            return properties.getSession();
        }

        /**
         * 检查Security模块是否启用
         *
         * @return true表示启用，false表示禁用
         */
        public boolean isSecurityEnabled() {
            return properties.isEnabled();
        }

        /**
         * 检查JWT功能是否启用
         *
         * @return true表示启用，false表示禁用
         */
        public boolean isJwtEnabled() {
            return properties.isEnabled() && properties.getJwt() != null;
        }

        /**
         * 检查CORS功能是否启用
         *
         * @return true表示启用，false表示禁用
         */
        public boolean isCorsEnabled() {
            return properties.isEnabled() && properties.getCors().isEnabled();
        }

        /**
         * 检查路径是否为匿名访问路径
         *
         * @param path 请求路径
         * @return true表示是匿名访问路径，false表示需要认证
         */
        public boolean isAnonymousPath(String path) {
            if (path == null || !properties.isEnabled()) {
                return false;
            }
            
            return properties.getAnonymous().getPaths().stream()
                    .anyMatch(pattern -> pathMatches(path, pattern));
        }

        /**
         * 路径匹配检查
         *
         * @param path    实际路径
         * @param pattern 匹配模式
         * @return true表示匹配，false表示不匹配
         */
        private boolean pathMatches(String path, String pattern) {
            // 简单的通配符匹配实现
            if (pattern.endsWith("/**")) {
                String prefix = pattern.substring(0, pattern.length() - 3);
                return path.startsWith(prefix);
            } else if (pattern.endsWith("/*")) {
                String prefix = pattern.substring(0, pattern.length() - 2);
                return path.startsWith(prefix) && path.indexOf('/', prefix.length()) == -1;
            } else {
                return path.equals(pattern);
            }
        }

        /**
         * 获取完整的配置属性
         *
         * @return SecurityProperties实例
         */
        public SecurityProperties getProperties() {
            return properties;
        }
    }
}