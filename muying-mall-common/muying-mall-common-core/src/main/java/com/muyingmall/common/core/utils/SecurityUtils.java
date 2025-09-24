package com.muyingmall.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 安全工具类 - 兼容层
 * 
 * <p>该类的部分功能已迁移到Security模块，此处保留基础功能用于向后兼容。</p>
 * 
 * <p>推荐使用Security模块中的增强版本：</p>
 * <ul>
 *   <li>{@link com.muyingmall.common.security.crypto.PasswordEncoder} - 密码加密服务</li>
 *   <li>{@link com.muyingmall.common.security.utils.AuthenticationUtils} - 认证工具类</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0 (兼容层)
 */
@Slf4j
public class SecurityUtils {

    /**
     * 默认的随机字符集
     */
    private static final String DEFAULT_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    /**
     * 安全随机数生成器
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 私有构造函数，防止实例化
     */
    private SecurityUtils() {
        throw new UnsupportedOperationException("工具类不能被实例化");
    }

    // =============================密码加密相关方法=============================

    /**
     * 生成随机盐值
     * 
     * <p>使用SecureRandom生成16字节的随机盐值，并使用Base64编码。</p>
     *
     * @return Base64编码的盐值字符串
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 使用SHA-256算法对密码进行哈希加密
     * 
     * <p>将密码和盐值组合后使用SHA-256算法进行哈希计算。</p>
     *
     * @param password 原始密码
     * @param salt     盐值
     * @return 哈希后的密码（十六进制字符串）
     * @throws IllegalArgumentException 如果密码或盐值为null
     */
    public static String hashPassword(String password, String salt) {
        if (password == null) {
            throw new IllegalArgumentException("密码不能为null");
        }
        if (salt == null) {
            throw new IllegalArgumentException("盐值不能为null");
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256算法不可用", e);
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 验证密码是否正确
     * 
     * <p>将输入的密码使用相同的盐值进行哈希，然后与存储的哈希值进行比较。</p>
     *
     * @param password     输入的密码
     * @param hashedPassword 存储的哈希密码
     * @param salt         盐值
     * @return true表示密码正确，false表示密码错误
     */
    public static boolean verifyPassword(String password, String hashedPassword, String salt) {
        if (password == null || hashedPassword == null || salt == null) {
            return false;
        }
        
        try {
            String inputHash = hashPassword(password, salt);
            return inputHash.equals(hashedPassword);
        } catch (Exception e) {
            log.error("密码验证失败", e);
            return false;
        }
    }

    // =============================哈希计算方法=============================

    /**
     * 计算字符串的MD5哈希值
     *
     * @param input 输入字符串
     * @return MD5哈希值（十六进制字符串）
     * @throws IllegalArgumentException 如果输入为null
     */
    public static String md5(String input) {
        if (input == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5算法不可用", e);
            throw new RuntimeException("MD5计算失败", e);
        }
    }

    /**
     * 计算字符串的SHA-256哈希值
     *
     * @param input 输入字符串
     * @return SHA-256哈希值（十六进制字符串）
     * @throws IllegalArgumentException 如果输入为null
     */
    public static String sha256(String input) {
        if (input == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256算法不可用", e);
            throw new RuntimeException("SHA-256计算失败", e);
        }
    }

    // =============================随机数生成方法=============================

    /**
     * 生成指定长度的随机字符串
     * 
     * <p>使用默认字符集（大小写字母和数字）生成随机字符串。</p>
     *
     * @param length 字符串长度，必须大于0
     * @return 随机字符串
     * @throws IllegalArgumentException 如果长度小于等于0
     */
    public static String generateRandomString(int length) {
        return generateRandomString(length, DEFAULT_CHARSET);
    }

    /**
     * 使用指定字符集生成随机字符串
     *
     * @param length  字符串长度，必须大于0
     * @param charset 字符集
     * @return 随机字符串
     * @throws IllegalArgumentException 如果长度小于等于0或字符集为null或空
     */
    public static String generateRandomString(int length, String charset) {
        if (length <= 0) {
            throw new IllegalArgumentException("字符串长度必须大于0");
        }
        if (charset == null || charset.isEmpty()) {
            throw new IllegalArgumentException("字符集不能为null或空");
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = SECURE_RANDOM.nextInt(charset.length());
            sb.append(charset.charAt(randomIndex));
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的随机数字字符串
     *
     * @param length 字符串长度，必须大于0
     * @return 随机数字字符串
     * @throws IllegalArgumentException 如果长度小于等于0
     */
    public static String generateRandomNumbers(int length) {
        return generateRandomString(length, "0123456789");
    }

    /**
     * 生成随机整数
     *
     * @param min 最小值（包含）
     * @param max 最大值（不包含）
     * @return 随机整数
     * @throws IllegalArgumentException 如果min >= max
     */
    public static int generateRandomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("最小值必须小于最大值");
        }
        return SECURE_RANDOM.nextInt(max - min) + min;
    }

    // =============================Spring Security集成方法=============================

    /**
     * 获取当前登录用户名
     * 
     * <p>需要Spring Security环境。如果Spring Security不可用或用户未认证，返回null。</p>
     *
     * @return 当前用户名，如果获取失败则返回null
     */
    @ConditionalOnClass(name = "org.springframework.security.core.context.SecurityContextHolder")
    public static String getCurrentUsername() {
        try {
            // 使用反射来避免编译时依赖
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = securityContextHolderClass.getMethod("getContext").invoke(null);
            Object authentication = context.getClass().getMethod("getAuthentication").invoke(context);

            if (authentication != null) {
                Boolean isAuthenticated = (Boolean) authentication.getClass().getMethod("isAuthenticated").invoke(authentication);
                if (Boolean.TRUE.equals(isAuthenticated)) {
                    return (String) authentication.getClass().getMethod("getName").invoke(authentication);
                }
            }
        } catch (Exception e) {
            log.debug("Spring Security不可用或获取当前用户名失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 判断当前用户是否已认证
     * 
     * <p>需要Spring Security环境。如果Spring Security不可用，返回false。</p>
     *
     * @return true表示已认证，false表示未认证
     */
    @ConditionalOnClass(name = "org.springframework.security.core.context.SecurityContextHolder")
    public static boolean isAuthenticated() {
        try {
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = securityContextHolderClass.getMethod("getContext").invoke(null);
            Object authentication = context.getClass().getMethod("getAuthentication").invoke(context);

            if (authentication != null) {
                Boolean isAuthenticated = (Boolean) authentication.getClass().getMethod("isAuthenticated").invoke(authentication);
                return Boolean.TRUE.equals(isAuthenticated);
            }
        } catch (Exception e) {
            log.debug("Spring Security不可用或判断认证状态失败: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取当前认证信息
     * 
     * <p>需要Spring Security环境。返回Object类型以避免编译依赖。</p>
     *
     * @return 认证信息对象，如果获取失败则返回null
     */
    @ConditionalOnClass(name = "org.springframework.security.core.context.SecurityContextHolder")
    public static Object getCurrentAuthentication() {
        try {
            Class<?> securityContextHolderClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = securityContextHolderClass.getMethod("getContext").invoke(null);
            return context.getClass().getMethod("getAuthentication").invoke(context);
        } catch (Exception e) {
            log.debug("Spring Security不可用或获取认证信息失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取当前登录用户ID
     * 
     * <p>需要Spring Security环境和JWT支持。
     * 注意：这是一个示例实现，实际项目中需要根据具体情况调整。</p>
     *
     * @return 用户ID，如果获取失败则返回null
     */
    public static Integer getCurrentUserId() {
        try {
            // 这里只返回null，实际应用中需要从JWT中解析用户ID
            // 或者从UserDetails中获取
            log.debug("getCurrentUserId()方法需要在实际项目中根据具体的认证机制进行实现");
            return null;
        } catch (Exception e) {
            log.debug("获取当前用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    // =============================工具方法=============================

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * 检查字符串是否为强密码
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
    public static boolean isStrongPassword(String password) {
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
     * 生成UUID（去除连字符）
     *
     * @return 32位的UUID字符串
     */
    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带连字符的UUID
     *
     * @return 36位的UUID字符串（包含连字符）
     */
    public static String generateUUIDWithHyphens() {
        return java.util.UUID.randomUUID().toString();
    }
}