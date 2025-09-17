-- ================================================================================
-- 商品服务数据库 (muying_product_service)
-- 版本: 1.0.0
-- 创建时间: 2025-09-16
-- 描述: 商品服务相关表，包括商品信息、分类、品牌、规格等
-- ================================================================================

-- 创建数据库
DROP DATABASE IF EXISTS muying_product_service;
CREATE DATABASE muying_product_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_product_service;

-- 商品分类表
CREATE TABLE `category` (
  `category_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` int UNSIGNED NULL DEFAULT 0 COMMENT '父分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = DYNAMIC;

-- 品牌表
CREATE TABLE `brand` (
  `brand_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌logo',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '品牌描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`brand_id`) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌表' ROW_FORMAT = DYNAMIC;

-- 商品表
CREATE TABLE `product` (
  `product_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `subtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品副标题',
  `category_id` int UNSIGNED NOT NULL COMMENT '分类ID',
  `brand_id` int UNSIGNED NULL DEFAULT NULL COMMENT '品牌ID',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品原价',
  `cost_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '成本价',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `warning_stock` int NULL DEFAULT 10 COMMENT '预警库存',
  `sales_count` int NOT NULL DEFAULT 0 COMMENT '销售数量',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览数量',
  `main_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品描述',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品重量(kg)',
  `unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '件' COMMENT '商品单位',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `is_new` tinyint(1) NULL DEFAULT 0 COMMENT '是否新品：0-否，1-是',
  `is_hot` tinyint(1) NULL DEFAULT 0 COMMENT '是否热销：0-否，1-是',
  `is_recommend` tinyint(1) NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '商品状态：0-下架，1-上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`product_id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_brand_id`(`brand_id` ASC) USING BTREE,
  INDEX `idx_price`(`price` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_stock`(`stock` ASC) USING BTREE,
  INDEX `idx_sales_count`(`sales_count` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_is_new`(`is_new` ASC) USING BTREE,
  INDEX `idx_is_hot`(`is_hot` ASC) USING BTREE,
  INDEX `idx_is_recommend`(`is_recommend` ASC) USING BTREE,
  FULLTEXT KEY `ft_name`(`name`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_product_brand` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`brand_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- 商品图片表
CREATE TABLE `product_image` (
  `image_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `product_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `alt_text` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `type` tinyint NULL DEFAULT 1 COMMENT '图片类型：1-商品图，2-详情图',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`image_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE,
  CONSTRAINT `fk_product_image` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品图片表' ROW_FORMAT = DYNAMIC;

-- 商品参数表
CREATE TABLE `product_param` (
  `param_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '参数ID',
  `product_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `param_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数名称',
  `param_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数值',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`param_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `fk_product_param` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品参数表' ROW_FORMAT = DYNAMIC;

-- 商品规格表
CREATE TABLE `product_specs` (
  `spec_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `product_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `spec_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格名称',
  `spec_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格值(JSON格式)',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '规格价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '规格原价',
  `cost_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '规格成本价',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU编码',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格图片',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '重量',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`spec_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_sku_code`(`sku_code` ASC) USING BTREE,
  INDEX `idx_stock`(`stock` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `fk_product_specs` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品规格表' ROW_FORMAT = DYNAMIC;

-- 规格值表
CREATE TABLE `spec_value` (
  `value_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '规格值ID',
  `spec_id` int UNSIGNED NOT NULL COMMENT '规格ID',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`value_id`) USING BTREE,
  INDEX `idx_spec_id`(`spec_id` ASC) USING BTREE,
  INDEX `idx_value`(`value`(20) ASC) USING BTREE,
  CONSTRAINT `fk_spec_value` FOREIGN KEY (`spec_id`) REFERENCES `product_specs` (`spec_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '规格值表' ROW_FORMAT = DYNAMIC;

-- 库存变更记录表
CREATE TABLE `stock_log` (
  `log_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `product_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `spec_id` int UNSIGNED NULL DEFAULT NULL COMMENT '规格ID',
  `change_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变更类型：IN-入库，OUT-出库，ADJUST-调整',
  `change_quantity` int NOT NULL COMMENT '变更数量',
  `stock_before` int NOT NULL COMMENT '变更前库存',
  `stock_after` int NOT NULL COMMENT '变更后库存',
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变更原因',
  `reference_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联单号(如订单号)',
  `operator_id` int NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_spec_id`(`spec_id` ASC) USING BTREE,
  INDEX `idx_change_type`(`change_type` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_reference_no`(`reference_no` ASC) USING BTREE,
  CONSTRAINT `fk_stock_log_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_stock_log_spec` FOREIGN KEY (`spec_id`) REFERENCES `product_specs` (`spec_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库存变更记录表' ROW_FORMAT = DYNAMIC;

-- 插入基础分类数据
INSERT INTO `category` (`category_id`, `parent_id`, `name`, `icon`, `sort_order`, `status`) VALUES
(1, 0, '奶粉', 'categorys/milk.png', 1, 1),
(2, 0, '护理', 'categorys/diaper.png', 2, 1),
(3, 0, '服饰', 'categorys/clothing.png', 3, 1),
(4, 0, '玩具', 'categorys/toy.png', 4, 1),
(5, 0, '洗护', 'categorys/care.png', 5, 1),
(6, 0, '喂养', 'categorys/feeding.png', 6, 1);

-- 插入基础品牌数据
INSERT INTO `brand` (`brand_id`, `name`, `logo`, `description`, `sort_order`, `status`) VALUES
(1, '惠氏', 'brands/wyeth.png', '惠氏营养品是全球知名的婴幼儿营养品牌', 1, 1),
(2, '美素佳儿', 'brands/friso.png', '美素佳儿是荷兰皇家菲仕兰旗下的婴幼儿奶粉品牌', 2, 1),
(3, '帮宝适', 'brands/pampers.png', '帮宝适是宝洁公司旗下的婴儿纸尿裤品牌', 3, 1),
(4, '花王', 'brands/huawang.png', '花王是日本著名的个人护理用品品牌', 4, 1),
(5, '爱他美', 'brands/aptamil.png', '爱他美是来自德国的高端婴幼儿配方奶粉品牌', 5, 1),
(6, '费雪', 'brands/fisher-price.png', '费雪是全球著名玩具品牌', 6, 1),
(7, '飞利浦新安怡', 'brands/avent.png', '飞利浦新安怡是全球领先的婴幼儿喂养用品品牌', 7, 1),
(8, '美德乐', 'brands/medela.png', '美德乐是源自瑞士的全球知名母乳喂养解决方案品牌', 8, 1),
(9, '贝亲', 'brands/pigeon.png', '贝亲是日本知名的母婴用品品牌', 9, 1),
(10, '嘉宝', 'brands/gerber.png', '嘉宝是全球领先的婴幼儿辅食品牌', 10, 1);

-- 创建库存管理存储过程
DELIMITER ;;
CREATE PROCEDURE `proc_update_stock`(
  IN p_product_id INT,
  IN p_spec_id INT,
  IN p_change_type VARCHAR(20),
  IN p_change_quantity INT,
  IN p_reference_no VARCHAR(64),
  IN p_reason VARCHAR(100),
  IN p_operator_id INT,
  IN p_operator_name VARCHAR(50),
  OUT p_result INT
)
BEGIN
  DECLARE v_current_stock INT DEFAULT 0;
  DECLARE v_new_stock INT DEFAULT 0;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_result = 0; -- 失败
  END;
  
  START TRANSACTION;
  
  -- 获取当前库存
  IF p_spec_id IS NOT NULL THEN
    SELECT stock INTO v_current_stock FROM product_specs WHERE spec_id = p_spec_id FOR UPDATE;
  ELSE
    SELECT stock INTO v_current_stock FROM product WHERE product_id = p_product_id FOR UPDATE;
  END IF;
  
  -- 计算新库存
  IF p_change_type = 'IN' THEN
    SET v_new_stock = v_current_stock + p_change_quantity;
  ELSEIF p_change_type = 'OUT' THEN
    SET v_new_stock = v_current_stock - p_change_quantity;
    IF v_new_stock < 0 THEN
      SET p_result = -1; -- 库存不足
      ROLLBACK;
      LEAVE proc_update_stock;
    END IF;
  ELSEIF p_change_type = 'ADJUST' THEN
    SET v_new_stock = p_change_quantity;
  END IF;
  
  -- 更新库存
  IF p_spec_id IS NOT NULL THEN
    UPDATE product_specs SET stock = v_new_stock WHERE spec_id = p_spec_id;
  ELSE
    UPDATE product SET stock = v_new_stock WHERE product_id = p_product_id;
  END IF;
  
  -- 记录库存变更日志
  INSERT INTO stock_log (
    product_id, spec_id, change_type, change_quantity, 
    stock_before, stock_after, reason, reference_no, 
    operator_id, operator_name
  ) VALUES (
    p_product_id, p_spec_id, p_change_type, p_change_quantity,
    v_current_stock, v_new_stock, p_reason, p_reference_no,
    p_operator_id, p_operator_name
  );
  
  SET p_result = 1; -- 成功
  COMMIT;
END;;
DELIMITER ;

-- 创建搜索优化视图
CREATE VIEW `v_product_search` AS
SELECT 
  p.product_id,
  p.name,
  p.subtitle,
  p.price,
  p.original_price,
  p.stock,
  p.sales_count,
  p.main_image,
  p.is_new,
  p.is_hot,
  p.is_recommend,
  p.status,
  c.name AS category_name,
  b.name AS brand_name,
  p.create_time
FROM product p
LEFT JOIN category c ON p.category_id = c.category_id
LEFT JOIN brand b ON p.brand_id = b.brand_id
WHERE p.status = 1;

-- 创建低库存预警视图
CREATE VIEW `v_low_stock_products` AS
SELECT 
  p.product_id,
  p.name,
  p.stock,
  p.warning_stock,
  c.name AS category_name,
  b.name AS brand_name,
  p.update_time
FROM product p
LEFT JOIN category c ON p.category_id = c.category_id
LEFT JOIN brand b ON p.brand_id = b.brand_id
WHERE p.stock <= p.warning_stock AND p.status = 1;

-- 创建索引优化查询性能
CREATE INDEX `idx_product_category_status` ON `product` (`category_id`, `status`);
CREATE INDEX `idx_product_brand_status` ON `product` (`brand_id`, `status`);
CREATE INDEX `idx_product_price_status` ON `product` (`price`, `status`);
CREATE INDEX `idx_product_sales_status` ON `product` (`sales_count` DESC, `status`);
CREATE INDEX `idx_category_parent_status` ON `category` (`parent_id`, `status`);

SET FOREIGN_KEY_CHECKS = 1;