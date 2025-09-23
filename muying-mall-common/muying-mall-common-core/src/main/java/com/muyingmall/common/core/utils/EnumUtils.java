package com.muyingmall.common.core.utils;

import com.muyingmall.common.core.enums.OrderStatus;
import com.muyingmall.common.core.enums.PaymentStatus;

/**
 * 枚举工具类
 * 
 * <p>提供枚举类型的通用操作方法，包括枚举值的查找、转换和验证等功能。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>根据编码查找枚举值</li>
 *   <li>枚举值与编码的相互转换</li>
 *   <li>枚举值的验证和默认值处理</li>
 *   <li>支持订单状态和支付状态枚举</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * // 根据编码获取订单状态
 * OrderStatus status = EnumUtils.getOrderStatusByCode("PAID");
 * 
 * // 获取支付状态编码
 * String code = EnumUtils.getPaymentStatusCode(PaymentStatus.PAID);
 * 
 * // 验证枚举值
 * boolean isValid = EnumUtils.isValidOrderStatus("COMPLETED");
 * }</pre>
 * 
 * <p>注意事项：</p>
 * <ul>
 *   <li>当传入无效编码时，会返回默认的枚举值</li>
 *   <li>所有方法都进行了null值检查</li>
 *   <li>编码匹配区分大小写</li>
 * </ul>
 * 
 * @author 母婴商城开发团队
 * @since 2025-09-23
 * @version 1.0
 */
public class EnumUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private EnumUtils() {
        throw new UnsupportedOperationException("工具类不能被实例化");
    }

    // =============================订单状态相关方法=============================

    /**
     * 根据状态编码获取订单状态枚举
     * 
     * <p>如果编码为null或找不到对应的枚举值，返回默认的待支付状态。</p>
     *
     * @param code 状态编码
     * @return 订单状态枚举，默认返回PENDING_PAYMENT
     */
    public static OrderStatus getOrderStatusByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return OrderStatus.PENDING_PAYMENT;
        }
        
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode().equals(code.trim())) {
                return status;
            }
        }
        
        return OrderStatus.PENDING_PAYMENT;
    }

    /**
     * 获取订单状态的编码
     * 
     * <p>如果订单状态为null，返回null。</p>
     *
     * @param status 订单状态枚举
     * @return 状态编码，如果status为null则返回null
     */
    public static String getOrderStatusCode(OrderStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 获取订单状态的描述
     * 
     * <p>如果订单状态为null，返回null。</p>
     *
     * @param status 订单状态枚举
     * @return 状态描述，如果status为null则返回null
     */
    public static String getOrderStatusDescription(OrderStatus status) {
        return status != null ? status.getDescription() : null;
    }

    /**
     * 验证订单状态编码是否有效
     *
     * @param code 状态编码
     * @return true表示有效，false表示无效
     */
    public static boolean isValidOrderStatusCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode().equals(code.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 获取所有订单状态编码
     *
     * @return 订单状态编码数组
     */
    public static String[] getAllOrderStatusCodes() {
        OrderStatus[] statuses = OrderStatus.values();
        String[] codes = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            codes[i] = statuses[i].getCode();
        }
        return codes;
    }

    // =============================支付状态相关方法=============================

    /**
     * 根据状态编码获取支付状态枚举
     * 
     * <p>如果编码为null或找不到对应的枚举值，返回默认的待支付状态。</p>
     *
     * @param code 状态编码
     * @return 支付状态枚举，默认返回PENDING
     */
    public static PaymentStatus getPaymentStatusByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return PaymentStatus.PENDING;
        }
        
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getCode().equals(code.trim())) {
                return status;
            }
        }
        
        return PaymentStatus.PENDING;
    }

    /**
     * 获取支付状态的编码
     * 
     * <p>如果支付状态为null，返回null。</p>
     *
     * @param status 支付状态枚举
     * @return 状态编码，如果status为null则返回null
     */
    public static String getPaymentStatusCode(PaymentStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * 获取支付状态的描述
     * 
     * <p>如果支付状态为null，返回null。</p>
     *
     * @param status 支付状态枚举
     * @return 状态描述，如果status为null则返回null
     */
    public static String getPaymentStatusDescription(PaymentStatus status) {
        return status != null ? status.getDescription() : null;
    }

    /**
     * 验证支付状态编码是否有效
     *
     * @param code 状态编码
     * @return true表示有效，false表示无效
     */
    public static boolean isValidPaymentStatusCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getCode().equals(code.trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 获取所有支付状态编码
     *
     * @return 支付状态编码数组
     */
    public static String[] getAllPaymentStatusCodes() {
        PaymentStatus[] statuses = PaymentStatus.values();
        String[] codes = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            codes[i] = statuses[i].getCode();
        }
        return codes;
    }

    // =============================通用枚举操作方法=============================

    /**
     * 根据枚举类和编码查找枚举值
     * 
     * <p>通用的枚举查找方法，适用于实现了getCode()方法的枚举类。</p>
     *
     * @param enumClass 枚举类
     * @param code      编码
     * @param <T>       枚举类型
     * @return 找到的枚举值，如果未找到则返回null
     */
    public static <T extends Enum<T>> T findByCode(Class<T> enumClass, String code) {
        if (enumClass == null || code == null || code.trim().isEmpty()) {
            return null;
        }
        
        try {
            for (T enumConstant : enumClass.getEnumConstants()) {
                // 使用反射调用getCode方法
                String enumCode = (String) enumConstant.getClass().getMethod("getCode").invoke(enumConstant);
                if (code.trim().equals(enumCode)) {
                    return enumConstant;
                }
            }
        } catch (Exception e) {
            // 如果枚举类没有getCode方法，忽略异常
        }
        
        return null;
    }

    /**
     * 根据枚举类和描述查找枚举值
     * 
     * <p>通用的枚举查找方法，适用于实现了getDescription()方法的枚举类。</p>
     *
     * @param enumClass   枚举类
     * @param description 描述
     * @param <T>         枚举类型
     * @return 找到的枚举值，如果未找到则返回null
     */
    public static <T extends Enum<T>> T findByDescription(Class<T> enumClass, String description) {
        if (enumClass == null || description == null || description.trim().isEmpty()) {
            return null;
        }
        
        try {
            for (T enumConstant : enumClass.getEnumConstants()) {
                // 使用反射调用getDescription方法
                String enumDescription = (String) enumConstant.getClass().getMethod("getDescription").invoke(enumConstant);
                if (description.trim().equals(enumDescription)) {
                    return enumConstant;
                }
            }
        } catch (Exception e) {
            // 如果枚举类没有getDescription方法，忽略异常
        }
        
        return null;
    }

    /**
     * 验证枚举值是否在指定的枚举类中
     *
     * @param enumClass 枚举类
     * @param value     要验证的值
     * @param <T>       枚举类型
     * @return true表示有效，false表示无效
     */
    public static <T extends Enum<T>> boolean isValidEnum(Class<T> enumClass, String value) {
        if (enumClass == null || value == null || value.trim().isEmpty()) {
            return false;
        }
        
        try {
            Enum.valueOf(enumClass, value.trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 获取枚举类的所有值的名称
     *
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 枚举值名称数组
     */
    public static <T extends Enum<T>> String[] getAllEnumNames(Class<T> enumClass) {
        if (enumClass == null) {
            return new String[0];
        }
        
        T[] enumConstants = enumClass.getEnumConstants();
        String[] names = new String[enumConstants.length];
        for (int i = 0; i < enumConstants.length; i++) {
            names[i] = enumConstants[i].name();
        }
        return names;
    }
}