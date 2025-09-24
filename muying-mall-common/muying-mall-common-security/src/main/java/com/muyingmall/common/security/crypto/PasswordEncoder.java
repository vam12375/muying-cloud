package com.muyingmall.common.security.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码加密服务类
 * 
 * <p>提供多种密码加密算法和验证功能，支持BCrypt、SHA-256等加密方式。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>BCrypt密码加密和验证（推荐）</li>
 *   <li>SHA-256密码加密和验证（兼容旧系统）</li>
 *   <li>随机盐值生成</li>
 *   <li>密码强度检查</li>
 *   <li>密码策略验证</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @Autowired
 * private PasswordEncoder passwordEncoder;
 * 
 * // BCrypt加密（推荐）
 * String encodedPassword = passwordEncoder.encode("password123");
 * boolean matches = passwordEncoder.matches("password123", encodedPassword);
 * 
 * // SHA-256加密（兼容旧系统）
 * String salt = passwordEncoder.generateSalt();
 * String hashedPassword = passwordEncoder.encodeWithSalt("password123", salt);
 * boolean isValid = passwordEncoder.matchesWithSalt("password123", hashedPassword, salt);
 * 
 * // 密码强度检查
 * boolean isStrong = passwordEncoder.isStrongPassword("Password123!");
 * }</pre>
 * 
 * <p>安全建议：</p>
 * <ul>
 *   <li>新系统推荐使用BCrypt算法</li>
 *   <li>旧系统迁移时可使用SHA-256兼容模式</li>
 *   <li>定期更新密码加密强度</li>
 *   <li>启用密码策略验证</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-24
 * @version 1.0
 */
@Component
public class PasswordEncoder {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncoder.class);

    /**
     * BCrypt密码编码器
     */
    private final BCryptPasswordEncoder bCryptEncoder;

    /**
     * 安全随机数生成器
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 默认盐值长度
     */
    private static final int DEFAULT_SALT_LENGTH = 16;

    /**
     * 构造函数
     */
    public PasswordEncoder() {
        // 使用强度为12的BCrypt编码器
        this.bCryptEncoder = new BCryptPasswordEncoder(12);
        log.info("密码加密服务初始化完成，使用BCrypt强度: 12");
    }

    // =============================BCrypt加密方法（推荐）=============================

    /**
     * 使用BCrypt算法加密密码
     * 
     * <p>BCrypt是目前推荐的密码加密算法，具有自适应性和抗彩虹表攻击能力。</p>
     *
     * @param rawPassword 原始密码，不能为null
     * @return BCrypt加密后的密码字符串
     * @throws IllegalArgumentException 如果原始密码为null
     */
    public String encode(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("原始密码不能为null");
        }
        
        try {
            String encoded = bCryptEncoder.encode(rawPassword);
            log.debug("密码BCrypt加密成功");
            return encoded;
        } catch (Exception e) {
            log.error("BCrypt密码加密失败", e);
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 验证BCrypt加密的密码
     * 
     * <p>验证原始密码与BCrypt加密后的密码是否匹配。</p>
     *
     * @param rawPassword     原始密码
     * @param encodedPassword BCrypt加密后的密码
     * @return true表示密码匹配，false表示密码不匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            log.debug("密码验证失败：原始密码或加密密码为null");
            return false;
        }
        
        try {
            boolean matches = bCryptEncoder.matches(rawPassword, encodedPassword);
            log.debug("BCrypt密码验证结果: {}", matches);
            return matches;
        } catch (Exception e) {
            log.error("BCrypt密码验证失败", e);
            return false;
        }
    }

    /**
     * 检查密码是否需要重新加密
     * 
     * <p>检查已加密的密码是否使用了较低的加密强度，如果是则建议重新加密。</p>
     *
     * @param encodedPassword 已加密的密码
     * @return true表示需要重新加密，false表示当前加密强度足够
     */
    public boolean upgradeEncoding(String encodedPassword) {
        if (encodedPassword == null) {
            return true;
        }
        
        try {
            return bCryptEncoder.upgradeEncoding(encodedPassword);
        } catch (Exception e) {
            log.debug("检查密码加密强度失败: {}", e.getMessage());
            return true;
        }
    }

    // =============================SHA-256加密方法（兼容旧系统）=============================

    /**
     * 生成随机盐值
     * 
     * <p>使用SecureRandom生成指定长度的随机盐值，并使用Base64编码。</p>
     *
     * @return Base64编码的盐值字符串
     */
    public String generateSalt() {
        return generateSalt(DEFAULT_SALT_LENGTH);
    }

    /**
     * 生成指定长度的随机盐值
     * 
     * <p>使用SecureRandom生成指定长度的随机盐值，并使用Base64编码。</p>
     *
     * @param length 盐值字节长度，必须大于0
     * @return Base64编码的盐值字符串
     * @throws IllegalArgumentException 如果长度小于等于0
     */
    public String generateSalt(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("盐值长度必须大于0");
        }
        
        byte[] salt = new byte[length];
        SECURE_RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 使用SHA-256算法和盐值加密密码
     * 
     * <p>将密码和盐值组合后使用SHA-256算法进行哈希计算。主要用于兼容旧系统。</p>
     *
     * @param rawPassword 原始密码，不能为null
     * @param salt        盐值，不能为null
     * @return SHA-256哈希后的密码（十六进制字符串）
     * @throws IllegalArgumentException 如果密码或盐值为null
     */
    public String encodeWithSalt(String rawPassword, String salt) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("原始密码不能为null");
        }
        if (salt == null) {
            throw new IllegalArgumentException("盐值不能为null");
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(rawPassword.getBytes());
            String encoded = bytesToHex(hashedPassword);
            log.debug("密码SHA-256加密成功");
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256算法不可用", e);
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 验证SHA-256加密的密码
     * 
     * <p>将输入的密码使用相同的盐值进行SHA-256哈希，然后与存储的哈希值进行比较。</p>
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 存储的哈希密码
     * @param salt            盐值
     * @return true表示密码正确，false表示密码错误
     */
    public boolean matchesWithSalt(String rawPassword, String encodedPassword, String salt) {
        if (rawPassword == null || encodedPassword == null || salt == null) {
            log.debug("SHA-256密码验证失败：参数包含null值");
            return false;
        }
        
        try {
            String inputHash = encodeWithSalt(rawPassword, salt);
            boolean matches = inputHash.equals(encodedPassword);
            log.debug("SHA-256密码验证结果: {}", matches);
            return matches;
        } catch (Exception e) {
            log.error("SHA-256密码验证失败", e);
            return false;
        }
    }

    // =============================密码策略验证方法=============================

    /**
     * 检查密码是否符合强密码要求
     * 
     * <p>强密码要求：</p>
     * <ul>
     *   <li>长度至少8位</li>
     *   <li>包含大写字母</li>
     *   <li>包含小写字母</li>
     *   <li>包含数字</li>
     *   <li>包含特殊字符</li>
     * </ul>
     *
     * @param password 密码
     * @return true表示是强密码，false表示不是强密码
     */
    public boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                hasSpecial = true;
            }
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * 检查密码是否符合中等强度要求
     * 
     * <p>中等强度密码要求：</p>
     * <ul>
     *   <li>长度至少6位</li>
     *   <li>包含字母和数字</li>
     * </ul>
     *
     * @param password 密码
     * @return true表示符合中等强度，false表示不符合
     */
    public boolean isMediumPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean hasLetter = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        
        return hasLetter && hasDigit;
    }

    /**
     * 获取密码强度等级
     * 
     * <p>返回密码强度等级：</p>
     * <ul>
     *   <li>WEAK: 弱密码</li>
     *   <li>MEDIUM: 中等强度密码</li>
     *   <li>STRONG: 强密码</li>
     * </ul>
     *
     * @param password 密码
     * @return 密码强度等级
     */
    public PasswordStrength getPasswordStrength(String password) {
        if (isStrongPassword(password)) {
            return PasswordStrength.STRONG;
        } else if (isMediumPassword(password)) {
            return PasswordStrength.MEDIUM;
        } else {
            return PasswordStrength.WEAK;
        }
    }

    /**
     * 验证密码是否符合指定策略
     * 
     * <p>根据密码策略验证密码是否符合要求。</p>
     *
     * @param password 密码
     * @param policy   密码策略
     * @return 验证结果
     */
    public PasswordValidationResult validatePassword(String password, PasswordPolicy policy) {
        if (password == null) {
            return new PasswordValidationResult(false, "密码不能为null");
        }
        
        if (policy == null) {
            policy = PasswordPolicy.getDefault();
        }
        
        // 检查最小长度
        if (password.length() < policy.getMinLength()) {
            return new PasswordValidationResult(false, 
                String.format("密码长度不能少于%d位", policy.getMinLength()));
        }
        
        // 检查最大长度
        if (policy.getMaxLength() > 0 && password.length() > policy.getMaxLength()) {
            return new PasswordValidationResult(false, 
                String.format("密码长度不能超过%d位", policy.getMaxLength()));
        }
        
        // 检查字符要求
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                hasSpecial = true;
            }
        }
        
        if (policy.isRequireUppercase() && !hasUpper) {
            return new PasswordValidationResult(false, "密码必须包含大写字母");
        }
        
        if (policy.isRequireLowercase() && !hasLower) {
            return new PasswordValidationResult(false, "密码必须包含小写字母");
        }
        
        if (policy.isRequireDigit() && !hasDigit) {
            return new PasswordValidationResult(false, "密码必须包含数字");
        }
        
        if (policy.isRequireSpecialChar() && !hasSpecial) {
            return new PasswordValidationResult(false, "密码必须包含特殊字符");
        }
        
        return new PasswordValidationResult(true, "密码符合要求");
    }

    // =============================工具方法=============================

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    // =============================内部类=============================

    /**
     * 密码强度枚举
     */
    public enum PasswordStrength {
        WEAK("弱"),
        MEDIUM("中等"),
        STRONG("强");

        private final String description;

        PasswordStrength(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 密码策略类
     */
    public static class PasswordPolicy {
        private int minLength = 8;
        private int maxLength = 0; // 0表示无限制
        private boolean requireUppercase = true;
        private boolean requireLowercase = true;
        private boolean requireDigit = true;
        private boolean requireSpecialChar = true;

        public static PasswordPolicy getDefault() {
            return new PasswordPolicy();
        }

        public static PasswordPolicy getSimple() {
            PasswordPolicy policy = new PasswordPolicy();
            policy.minLength = 6;
            policy.requireUppercase = false;
            policy.requireSpecialChar = false;
            return policy;
        }

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
    }

    /**
     * 密码验证结果类
     */
    public static class PasswordValidationResult {
        private final boolean valid;
        private final String message;

        public PasswordValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }
}