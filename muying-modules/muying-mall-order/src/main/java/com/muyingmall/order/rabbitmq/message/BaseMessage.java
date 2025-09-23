package com.muyingmall.order.rabbitmq.message;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息基类
 * 
 * @author MuYing Mall
 * @date 2024-01-15
 */
@Data
public abstract class BaseMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 消息唯一ID
     */
    private String messageId;
    
    /**
     * 消息来源服务
     */
    private String source;
    
    /**
     * 事件类型
     */
    private String eventType;
    
    /**
     * 消息版本
     */
    private String version = "1.0";
    
    /**
     * 时间戳
     */
    private Date timestamp;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 操作用户ID
     */
    private Long userId;
    
    /**
     * 链路追踪ID
     */
    private String traceId;
    
    /**
     * 重试次数
     */
    private Integer retryCount = 0;
    
    public BaseMessage() {
        this.createTime = new Date();
        this.timestamp = new Date();
        this.metadata = new HashMap<>();
    }
    
    /**
     * 添加元数据
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }
    
    /**
     * 获取元数据
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }
}