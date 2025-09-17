package com.muyingmall.order.client;

import java.math.BigDecimal;

/**
 * 商品信息DTO（从商品服务返回）
 */
public class ProductInfo {
    private Integer id;
    private String name;
    private String mainImage;
    private BigDecimal price;
    private Integer stock;
    private String status;
    
    // getters and setters
    public Integer getId() { 
        return id; 
    }
    
    public void setId(Integer id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getMainImage() { 
        return mainImage; 
    }
    
    public void setMainImage(String mainImage) { 
        this.mainImage = mainImage; 
    }
    
    public BigDecimal getPrice() { 
        return price; 
    }
    
    public void setPrice(BigDecimal price) { 
        this.price = price; 
    }
    
    public Integer getStock() { 
        return stock; 
    }
    
    public void setStock(Integer stock) { 
        this.stock = stock; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
}