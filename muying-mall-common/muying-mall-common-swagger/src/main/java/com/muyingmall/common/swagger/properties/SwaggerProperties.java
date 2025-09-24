package com.muyingmall.common.swagger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger配置属性类
 * 
 * @author 青柠檬
 * @since 2025-09-23
 */
@ConfigurationProperties(prefix = "muying.swagger")
public class SwaggerProperties {

    /**
     * 是否启用Swagger文档
     */
    private boolean enabled = true;

    /**
     * API文档标题
     */
    private String title = "母婴商城API";

    /**
     * API文档版本
     */
    private String version = "1.0.0";

    /**
     * API文档描述
     */
    private String description = "母婴商城微服务API文档";

    /**
     * 服务条款URL
     */
    private String termsOfServiceUrl = "";

    /**
     * 联系人信息
     */
    private Contact contact = new Contact();

    /**
     * 许可证信息
     */
    private License license = new License();

    /**
     * 服务器信息
     */
    private Server server = new Server();

    /**
     * 分组配置
     */
    private Group group = new Group();

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * 联系人信息
     */
    public static class Contact {
        /**
         * 联系人姓名
         */
        private String name = "母婴商城开发团队";

        /**
         * 联系人邮箱
         */
        private String email = "dev@muyingmall.com";

        /**
         * 联系人网址
         */
        private String url = "https://www.muyingmall.com";

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * 许可证信息
     */
    public static class License {
        /**
         * 许可证名称
         */
        private String name = "Apache 2.0";

        /**
         * 许可证URL
         */
        private String url = "https://www.apache.org/licenses/LICENSE-2.0";

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * 服务器信息
     */
    public static class Server {
        /**
         * 服务器URL
         */
        private String url = "http://localhost:8080";

        /**
         * 服务器描述
         */
        private String description = "开发环境";

        // Getters and Setters
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    /**
     * 分组配置
     */
    public static class Group {
        /**
         * 是否启用分组
         */
        private boolean enabled = true;

        /**
         * 默认分组名称
         */
        private String defaultGroup = "default";

        /**
         * 包扫描路径
         */
        private String basePackage = "com.muyingmall";

        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDefaultGroup() {
            return defaultGroup;
        }

        public void setDefaultGroup(String defaultGroup) {
            this.defaultGroup = defaultGroup;
        }

        public String getBasePackage() {
            return basePackage;
        }

        public void setBasePackage(String basePackage) {
            this.basePackage = basePackage;
        }
    }
}