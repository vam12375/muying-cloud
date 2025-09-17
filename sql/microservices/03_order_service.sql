-- ================================================================================
-- 订单服务数据库 (muying_order_service)
-- 版本: 1.0.0
-- 创建时间: 2025-09-16
-- 描述: 订单服务相关表，包括订单、订单项、购物车、物流等
-- ================================================================================

-- 创建数据库
DROP DATABASE IF EXISTS muying_order_service;
CREATE DATABASE muying_order_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_order_service;

-- 订单表
CREATE TABLE `order` (
  `order_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名(冗余字段)',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `product_amount` decimal(10, 2) NOT NULL COMMENT '商品总金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '运费',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠券金额',
  `points_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '积分抵扣金额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '实际支付金额',
  `payment_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式：wechat-微信，alipay-支付宝，wallet-余额支付',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取消原因',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending_payment' COMMENT '订单状态：pending_payment-待付款，paid-已付款，pending_shipment-待发货，shipped-已发货，completed-已完成，cancelled-已取消，refunded-已退款',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货省份',
  `receiver_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货城市',
  `receiver_district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货区县',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货详细地址',
  `receiver_postal_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `logistics_company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司',
  `logistics_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'web' COMMENT '订单来源：web-网站，app-手机应用，wechat-微信小程序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_payment_time`(`payment_time` ASC) USING BTREE,
  INDEX `idx_logistics_no`(`logistics_no` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- 订单商品表
CREATE TABLE `order_product` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` int UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `product_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称(冗余字段)',
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片(冗余字段)',
  `product_price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `product_quantity` int NOT NULL COMMENT '购买数量',
  `product_total_price` decimal(10, 2) NOT NULL COMMENT '商品总价',
  `product_real_price` decimal(10, 2) NOT NULL COMMENT '实际支付金额',
  `product_sku` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品规格(冗余字段)',
  `category_id` int UNSIGNED NULL DEFAULT NULL COMMENT '分类ID(冗余字段)',
  `brand_id` int UNSIGNED NULL DEFAULT NULL COMMENT '品牌ID(冗余字段)',
  `is_commented` tinyint(1) NULL DEFAULT 0 COMMENT '是否已评价：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_is_commented`(`is_commented` ASC) USING BTREE,
  CONSTRAINT `fk_order_product_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品表' ROW_FORMAT = DYNAMIC;

-- 订单状态日志表
CREATE TABLE `order_state_log` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` int UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `old_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原状态',
  `new_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新状态',
  `operator_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'system' COMMENT '操作类型：system-系统，user-用户，admin-管理员',
  `operator_id` int NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_order_state_log` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单状态日志表' ROW_FORMAT = DYNAMIC;

-- 购物车表
CREATE TABLE `cart` (
  `cart_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `product_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `selected` tinyint(1) NULL DEFAULT 1 COMMENT '是否选中：0-否，1-是',
  `specs` json NULL COMMENT '规格信息',
  `specs_hash` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格信息哈希值，用于唯一索引',
  `price_snapshot` decimal(10, 2) NULL DEFAULT NULL COMMENT '加入购物车时的价格快照',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-无效, 1-有效, 2-已下单, 3-库存不足',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`cart_id`) USING BTREE,
  UNIQUE INDEX `idx_user_goods_specs`(`user_id` ASC, `product_id` ASC, `specs_hash` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_update_time`(`update_time` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = DYNAMIC;

-- 物流公司表
CREATE TABLE `logistics_company` (
  `company_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '物流公司ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `company_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司代码',
  `company_website` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司官网',
  `company_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客服电话',
  `api_config` json NULL COMMENT 'API配置信息',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`company_id`) USING BTREE,
  UNIQUE INDEX `idx_company_code`(`company_code` ASC) USING BTREE,
  INDEX `idx_company_name`(`company_name` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物流公司表' ROW_FORMAT = DYNAMIC;

-- 物流信息表
CREATE TABLE `logistics` (
  `logistics_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` int UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `logistics_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流单号',
  `company_id` int UNSIGNED NOT NULL COMMENT '物流公司ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流公司名称(冗余字段)',
  `company_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流公司代码(冗余字段)',
  `sender_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发件人姓名',
  `sender_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发件人电话',
  `sender_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发件人地址',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人电话',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人地址',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '包裹重量(kg)',
  `freight_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending' COMMENT '物流状态：pending-待发货，shipped-已发货，in_transit-运输中，delivered-已送达，exception-异常',
  `ship_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '送达时间',
  `estimated_delivery_time` datetime NULL DEFAULT NULL COMMENT '预计送达时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`logistics_id`) USING BTREE,
  UNIQUE INDEX `idx_logistics_no`(`logistics_no` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_company_id`(`company_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `fk_logistics_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_logistics_company` FOREIGN KEY (`company_id`) REFERENCES `logistics_company` (`company_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物流信息表' ROW_FORMAT = DYNAMIC;

-- 物流轨迹表
CREATE TABLE `logistics_trace` (
  `trace_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '轨迹ID',
  `logistics_id` int UNSIGNED NOT NULL COMMENT '物流ID',
  `logistics_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流单号',
  `trace_time` datetime NOT NULL COMMENT '轨迹时间',
  `trace_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '轨迹内容',
  `trace_location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '轨迹位置',
  `trace_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '轨迹状态',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作员',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`trace_id`) USING BTREE,
  INDEX `idx_logistics_id`(`logistics_id` ASC) USING BTREE,
  INDEX `idx_logistics_no`(`logistics_no` ASC) USING BTREE,
  INDEX `idx_trace_time`(`trace_time` ASC) USING BTREE,
  CONSTRAINT `fk_logistics_trace` FOREIGN KEY (`logistics_id`) REFERENCES `logistics` (`logistics_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物流轨迹表' ROW_FORMAT = DYNAMIC;

-- 插入基础物流公司数据
INSERT INTO `logistics_company` (`company_name`, `company_code`, `company_website`, `company_phone`, `sort_order`, `status`) VALUES
('顺丰速运', 'SF', 'https://www.sf-express.com', '95338', 1, 1),
('圆通速递', 'YTO', 'https://www.yto.net.cn', '95554', 2, 1),
('中通快递', 'ZTO', 'https://www.zto.com', '95311', 3, 1),
('申通快递', 'STO', 'https://www.sto.cn', '95543', 4, 1),
('韵达速递', 'YD', 'https://www.yunda.com', '95546', 5, 1),
('百世快递', 'BEST', 'https://www.best-inc.com', '400-885-6561', 6, 1),
('德邦快递', 'DBL', 'https://www.deppon.com', '95353', 7, 1),
('京东快递', 'JD', 'https://www.jdl.cn', '950616', 8, 1),
('天天快递', 'HHTT', 'https://www.ttkdex.com', '400-188-8888', 9, 1),
('邮政EMS', 'EMS', 'https://www.ems.com.cn', '11183', 10, 1);

-- 创建订单管理存储过程：更新订单状态
DELIMITER ;;
CREATE PROCEDURE `proc_update_order_status`(
  IN p_order_id INT,
  IN p_new_status VARCHAR(20),
  IN p_operator_type VARCHAR(20),
  IN p_operator_id INT,
  IN p_operator_name VARCHAR(50),
  IN p_remark VARCHAR(255),
  OUT p_result INT
)
BEGIN
  DECLARE v_old_status VARCHAR(20);
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_result = 0; -- 失败
  END;
  
  START TRANSACTION;
  
  -- 获取当前状态
  SELECT status INTO v_old_status FROM `order` WHERE order_id = p_order_id FOR UPDATE;
  
  IF v_old_status IS NULL THEN
    SET p_result = -1; -- 订单不存在
    ROLLBACK;
  ELSE
    -- 更新订单状态
    UPDATE `order` SET 
      status = p_new_status,
      payment_time = CASE WHEN p_new_status = 'paid' THEN NOW() ELSE payment_time END,
      delivery_time = CASE WHEN p_new_status = 'shipped' THEN NOW() ELSE delivery_time END,
      receive_time = CASE WHEN p_new_status = 'completed' THEN NOW() ELSE receive_time END,
      finish_time = CASE WHEN p_new_status = 'completed' THEN NOW() ELSE finish_time END,
      cancel_time = CASE WHEN p_new_status = 'cancelled' THEN NOW() ELSE cancel_time END
    WHERE order_id = p_order_id;
    
    -- 记录状态变更日志
    INSERT INTO order_state_log (
      order_id, order_no, old_status, new_status, 
      operator_type, operator_id, operator_name, remark
    ) 
    SELECT 
      p_order_id, order_no, v_old_status, p_new_status,
      p_operator_type, p_operator_id, p_operator_name, p_remark
    FROM `order` WHERE order_id = p_order_id;
    
    SET p_result = 1; -- 成功
    COMMIT;
  END IF;
END;;
DELIMITER ;

-- 创建购物车清理存储过程
DELIMITER ;;
CREATE PROCEDURE `proc_clean_expired_cart_items`()
BEGIN
  -- 删除超过30天未更新的购物车项
  DELETE FROM `cart` WHERE `update_time` < DATE_SUB(NOW(), INTERVAL 30 DAY);
  
  -- 删除已经标记为过期的购物车项
  DELETE FROM `cart` WHERE `expire_time` IS NOT NULL AND `expire_time` < NOW();
  
  -- 将已下单的购物车项标记为已下单状态
  UPDATE `cart` SET `status` = 2 
  WHERE `cart_id` IN (
    SELECT c.cart_id FROM `cart` c
    JOIN `order_product` op ON c.product_id = op.product_id 
    JOIN `order` o ON op.order_id = o.order_id
    WHERE c.user_id = o.user_id AND o.create_time > c.create_time
  );
END;;
DELIMITER ;

-- 创建订单统计视图
CREATE VIEW `v_order_statistics` AS
SELECT 
  DATE(create_time) AS order_date,
  COUNT(*) AS total_orders,
  COUNT(CASE WHEN status = 'pending_payment' THEN 1 END) AS pending_payment_count,
  COUNT(CASE WHEN status = 'paid' THEN 1 END) AS paid_count,
  COUNT(CASE WHEN status = 'shipped' THEN 1 END) AS shipped_count,
  COUNT(CASE WHEN status = 'completed' THEN 1 END) AS completed_count,
  COUNT(CASE WHEN status = 'cancelled' THEN 1 END) AS cancelled_count,
  SUM(total_amount) AS total_amount,
  SUM(pay_amount) AS pay_amount,
  AVG(total_amount) AS avg_order_amount
FROM `order`
WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
GROUP BY DATE(create_time)
ORDER BY order_date DESC;

-- 创建热门商品统计视图
CREATE VIEW `v_popular_products` AS
SELECT 
  op.product_id,
  op.product_name,
  COUNT(*) AS order_count,
  SUM(op.product_quantity) AS total_quantity,
  SUM(op.product_total_price) AS total_sales_amount,
  AVG(op.product_price) AS avg_price
FROM order_product op
JOIN `order` o ON op.order_id = o.order_id
WHERE o.status IN ('paid', 'shipped', 'completed')
  AND o.create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
GROUP BY op.product_id, op.product_name
ORDER BY total_quantity DESC, total_sales_amount DESC;

-- 创建索引优化查询性能
CREATE INDEX `idx_order_user_status` ON `order` (`user_id`, `status`);
CREATE INDEX `idx_order_payment_time` ON `order` (`payment_time`, `status`);
CREATE INDEX `idx_order_finish_time` ON `order` (`finish_time`, `status`);
CREATE INDEX `idx_order_product_commented` ON `order_product` (`is_commented`, `create_time`);
CREATE INDEX `idx_cart_user_selected` ON `cart` (`user_id`, `selected`);
CREATE INDEX `idx_logistics_status_time` ON `logistics` (`status`, `ship_time`);

SET FOREIGN_KEY_CHECKS = 1;