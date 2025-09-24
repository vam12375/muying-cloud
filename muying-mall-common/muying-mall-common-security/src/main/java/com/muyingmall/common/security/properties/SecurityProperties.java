package com.muyingmall.common.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置属性类
 * 
 * <p>定义Security模块的配置属性，支持外部化配置。</p>
 * 
 * <p>配置示例：</p>
 * <pre>{@code
 * # application.yml
 * muying:
 *   security:
 *     enabled: true
 *     jwt:
 *       secret: "your-secret-key-here"
 *       expiration: 86400
 *       refresh-expiration: 604800
 *       issuer: "muying-mall"
 *     password:
 *       bcrypt-strength: 12
 *       policy:
 *         min-length: 8
 *         require-uppercase: true
 *         require-lowercase: true
 *         require-digit: true
 *         require-special-char: true
 *     anonymous:
 *       paths:
 *         - "/api/auth/**"
 *         - "/api/public/**"
 *         - "/health"
 *     cors:
 *       enabled: true
 *       allowed-origins: "*"
 *       allowed-methods: "GET,POST,PUT,DELETE,OPTIONS"
 *       allowed-headers: "*"
 *       max-age: 3600
 * }</pre>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@ConfigurationProperties(prefix = "muying.security")
public class SecurityProperties {

    /**
     * 是否启用安全模块
     */
    private boolean enabled = true;

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 密码配置
     */
    private Password password = new Password();

    /**
     * 匿名访问配置
     */
    private Anonymous anonymous = new Anonymous();

    /**
     * CORS配置
     */
    private Cors cors = new Cors();

    /**
     * 会话配置
     */
    private Session session = new Session();

    // =============================Getters and Setters=============================

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Anonymous getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Anonymous anonymous) {
        this.anonymous = anonymous;
    }

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    // =============================内部配置类=============================

    /**
     * JWT配置类
     */
    public static class Jwt {
        /**
         * JWT签名密钥
         */
        private String secret = "ThisIsAReallyLongAndSecureSecretKeyForHS512AlgorithmSoItShouldWorkFineNowPleaseEnsureItIsReallySecureAndRandomEnough";

        /**
         * JWT令牌过期时间（秒）
         */
        private Long expiration = 86400L; // 24小时

        /**
         * JWT刷新令牌过期时间（秒）
         */
        private Long refreshExpiration = 604800L; // 7天

        /**
         * JWT签发者
         */
        private String issuer = "muying-mall";

        /**
         * JWT受众
         */
        private String audience = "muying-mall-users";

        /**
         * 是否启用刷新令牌
         */
        private boolean enableRefreshToken = true;

        /**
         * 令牌头名称
         */
        private String headerName = "Authorization";

        /**
         * 令牌前缀
         */
        private String tokenPrefix = "Bearer ";

        /**
         * 令牌参数名
         */
        private String parameterName = "token";

        // Getters and Setters
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }

        public Long getExpiration() { return expiration; }
        public void setExpiration(Long expiration) { this.expiration = expiration; }

        public Long getRefreshExpiration() { return refreshExpiration; }
        public void setRefreshExpiration(Long refreshExpiration) { this.refreshExpiration = refreshExpiration; }

        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }

        public String getAudience() { return audience; }
        public void setAudience(String audience) { this.audience = audience; }

        public boolean isEnableRefreshToken() { return enableRefreshToken; }
        public void setEnableRefreshToken(boolean enableRefreshToken) { this.enableRefreshToken = enableRefreshToken; }

        public String getHeaderName() { return headerName; }
        public void setHeaderName(String headerName) { this.headerName = headerName; }

        public String getTokenPrefix() { return tokenPrefix; }
        public void setTokenPrefix(String tokenPrefix) { this.tokenPrefix = tokenPrefix; }

        public String getParameterName() { return parameterName; }
        public void setParameterName(String parameterName) { this.parameterName = parameterName; }
    }

    /**
     * 密码配置类
     */
    public static class Password {
        /**
         * BCrypt加密强度
         */
        private int bcryptStrength = 12;

        /**
         * 密码策略
         */
        private Policy policy = new Policy();

        /**
         * 是否启用密码历史检查
         */
        private boolean enablePasswordHistory = false;

        /**
         * 密码历史记录数量
         */
        private int passwordHistoryCount = 5;

        // Getters and Setters
        public int getBcryptStrength() { return bcryptStrength; }
        public void setBcryptStrength(int bcryptStrength) { this.bcryptStrength = bcryptStrength; }

        public Policy getPolicy() { return policy; }
        public void setPolicy(Policy policy) { this.policy = policy; }

        public boolean isEnablePasswordHistory() { return enablePasswordHistory; }
        public void setEnablePasswordHistory(boolean enablePasswordHistory) { this.enablePasswordHistory = enablePasswordHistory; }

        public int getPasswordHistoryCount() { return passwordHistoryCount; }
        public void setPasswordHistoryCount(int passwordHistoryCount) { this.passwordHistoryCount = passwordHistoryCount; }

        /**
         * 密码策略配置类
         */
        public static class Policy {
            /**
             * 最小长度
             */
            private int minLength = 8;

            /**
             * 最大长度（0表示无限制）
             */
            private int maxLength = 0;

            /**
             * 是否要求大写字母
             */
            private boolean requireUppercase = true;

            /**
             * 是否要求小写字母
             */
            private boolean requireLowercase = true;

            /**
             * 是否要求数字
             */
            private boolean requireDigit = true;

            /**
             * 是否要求特殊字符
             */
            private boolean requireSpecialChar = true;

            /**
             * 禁用的密码列表
             */
            private List<String> forbiddenPasswords = new ArrayList<>();

            // Getters and Setters
            public int getMinLength() { return minLength; }
            public void setMinLength(int minLength) { this.minLength = minLength; }

            public int getMaxLength() { return maxLength; }
            public void setMaxLength(int maxLength) { this.maxLength = maxLength; }

            public boolean isRequireUppercase() { return requireUppercase; }
            public void setRequireUppercase(boolean requireUppercase) { this.requireUppercase = requireUppercase; }

            public boolean isRequireLowercase() { return requireLowercase; }
            public void setRequireLowercase(boolean requireLowercase) { this.requireLowercase = requireLowercase; }

            public boolean isRequireDigit() { return requireDigit; }
            public void setRequireDigit(boolean requireDigit) { this.requireDigit = requireDigit; }

            public boolean isRequireSpecialChar() { return requireSpecialChar; }
            public void setRequireSpecialChar(boolean requireSpecialChar) { this.requireSpecialChar = requireSpecialChar; }

            public List<String> getForbiddenPasswords() { return forbiddenPasswords; }
            public void setForbiddenPasswords(List<String> forbiddenPasswords) { this.forbiddenPasswords = forbiddenPasswords; }
        }
    }

    /**
     * 匿名访问配置类
     */
    public static class Anonymous {
        /**
         * 匿名访问路径列表
         */
        private List<String> paths = new ArrayList<>();

        /**
         * 是否记录匿名访问日志
         */
        private boolean logAccess = true;

        /**
         * 匿名访问限流配置
         */
        private RateLimit rateLimit = new RateLimit();

        // 构造函数，设置默认匿名访问路径
        public Anonymous() {
            paths.add("/api/auth/**");
            paths.add("/api/public/**");
            paths.add("/health");
            paths.add("/actuator/health");
            paths.add("/swagger-ui/**");
            paths.add("/v3/api-docs/**");
        }

        // Getters and Setters
        public List<String> getPaths() { return paths; }
        public void setPaths(List<String> paths) { this.paths = paths; }

        public boolean isLogAccess() { return logAccess; }
        public void setLogAccess(boolean logAccess) { this.logAccess = logAccess; }

        public RateLimit getRateLimit() { return rateLimit; }
        public void setRateLimit(RateLimit rateLimit) { this.rateLimit = rateLimit; }

        /**
         * 限流配置类
         */
        public static class RateLimit {
            /**
             * 是否启用限流
             */
            private boolean enabled = false;

            /**
             * 每分钟最大请求数
             */
            private int maxRequestsPerMinute = 100;

            /**
             * 限流时间窗口（秒）
             */
            private int windowSize = 60;

            // Getters and Setters
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }

            public int getMaxRequestsPerMinute() { return maxRequestsPerMinute; }
            public void setMaxRequestsPerMinute(int maxRequestsPerMinute) { this.maxRequestsPerMinute = maxRequestsPerMinute; }

            public int getWindowSize() { return windowSize; }
            public void setWindowSize(int windowSize) { this.windowSize = windowSize; }
        }
    }

    /**
     * CORS配置类
     */
    public static class Cors {
        /**
         * 是否启用CORS
         */
        private boolean enabled = true;

        /**
         * 允许的源
         */
        private List<String> allowedOrigins = new ArrayList<>();

        /**
         * 允许的方法
         */
        private List<String> allowedMethods = new ArrayList<>();

        /**
         * 允许的头
         */
        private List<String> allowedHeaders = new ArrayList<>();

        /**
         * 暴露的头
         */
        private List<String> exposedHeaders = new ArrayList<>();

        /**
         * 是否允许凭证
         */
        private boolean allowCredentials = true;

        /**
         * 预检请求缓存时间（秒）
         */
        private long maxAge = 3600;

        // 构造函数，设置默认值
        public Cors() {
            allowedOrigins.add("*");
            allowedMethods.add("GET");
            allowedMethods.add("POST");
            allowedMethods.add("PUT");
            allowedMethods.add("DELETE");
            allowedMethods.add("OPTIONS");
            allowedHeaders.add("*");
        }

        // Getters and Setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public List<String> getAllowedOrigins() { return allowedOrigins; }
        public void setAllowedOrigins(List<String> allowedOrigins) { this.allowedOrigins = allowedOrigins; }

        public List<String> getAllowedMethods() { return allowedMethods; }
        public void setAllowedMethods(List<String> allowedMethods) { this.allowedMethods = allowedMethods; }

        public List<String> getAllowedHeaders() { return allowedHeaders; }
        public void setAllowedHeaders(List<String> allowedHeaders) { this.allowedHeaders = allowedHeaders; }

        public List<String> getExposedHeaders() { return exposedHeaders; }
        public void setExposedHeaders(List<String> exposedHeaders) { this.exposedHeaders = exposedHeaders; }

        public boolean isAllowCredentials() { return allowCredentials; }
        public void setAllowCredentials(boolean allowCredentials) { this.allowCredentials = allowCredentials; }

        public long getMaxAge() { return maxAge; }
        public void setMaxAge(long maxAge) { this.maxAge = maxAge; }
    }

    /**
     * 会话配置类
     */
    public static class Session {
        /**
         * 会话超时时间
         */
        private Duration timeout = Duration.ofMinutes(30);

        /**
         * 是否启用记住我功能
         */
        private boolean enableRememberMe = true;

        /**
         * 记住我令牌有效期
         */
        private Duration rememberMeTimeout = Duration.ofDays(7);

        /**
         * 最大并发会话数
         */
        private int maxConcurrentSessions = 1;

        /**
         * 会话固化保护
         */
        private boolean sessionFixationProtection = true;

        // Getters and Setters
        public Duration getTimeout() { return timeout; }
        public void setTimeout(Duration timeout) { this.timeout = timeout; }

        public boolean isEnableRememberMe() { return enableRememberMe; }
        public void setEnableRememberMe(boolean enableRememberMe) { this.enableRememberMe = enableRememberMe; }

        public Duration getRememberMeTimeout() { return rememberMeTimeout; }
        public void setRememberMeTimeout(Duration rememberMeTimeout) { this.rememberMeTimeout = rememberMeTimeout; }

        public int getMaxConcurrentSessions() { return maxConcurrentSessions; }
        public void setMaxConcurrentSessions(int maxConcurrentSessions) { this.maxConcurrentSessions = maxConcurrentSessions; }

        public boolean isSessionFixationProtection() { return sessionFixationProtection; }
        public void setSessionFixationProtection(boolean sessionFixationProtection) { this.sessionFixationProtection = sessionFixationProtection; }
    }
}