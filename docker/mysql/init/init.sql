-- 创建微服务数据库（新架构）
CREATE DATABASE IF NOT EXISTS muying_user_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_product_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_order_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_payment_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_comment_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_logistics_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_points_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_search_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_admin_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_coupon_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS muying_message_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 为了兼容现有配置，保留旧数据库名称
CREATE DATABASE IF NOT EXISTS user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS product_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS order_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS payment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS search_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS logistics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS comment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS points_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS admin_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建Nacos数据库
CREATE DATABASE IF NOT EXISTS nacos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用用户数据库并创建表
USE user_db;

CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `gender` tinyint DEFAULT NULL COMMENT '性别 0:女 1:男',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `user_address` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人手机号',
  `province` varchar(50) NOT NULL COMMENT '省份',
  `city` varchar(50) NOT NULL COMMENT '城市',
  `district` varchar(50) NOT NULL COMMENT '区县',
  `detail_address` varchar(255) NOT NULL COMMENT '详细地址',
  `postal_code` varchar(10) DEFAULT NULL COMMENT '邮政编码',
  `is_default` tinyint DEFAULT '0' COMMENT '是否默认地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户地址表';

-- 使用商品数据库并创建表
USE product_db;

CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `short_description` varchar(500) COMMENT '简短描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `original_price` decimal(10,2) COMMENT '原价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `category_id` int NOT NULL COMMENT '分类ID',
  `brand_id` int NOT NULL COMMENT '品牌ID',
  `main_image` varchar(255) NOT NULL COMMENT '主图',
  `images` text COMMENT '图片列表',
  `specifications` text COMMENT '规格参数',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态',
  `sales_count` int DEFAULT '0' COMMENT '销量',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `weight` decimal(8,2) COMMENT '重量',
  `unit` varchar(10) COMMENT '单位',
  `sku` varchar(100) COMMENT 'SKU',
  `tags` varchar(500) COMMENT '标签',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(200) COMMENT '分类描述',
  `parent_id` int DEFAULT '0' COMMENT '父分类ID',
  `level` int DEFAULT '1' COMMENT '分类级别',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `icon` varchar(255) COMMENT '图标',
  `image` varchar(255) COMMENT '分类图片',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(50) NOT NULL COMMENT '品牌名称',
  `description` varchar(200) COMMENT '品牌描述',
  `logo` varchar(255) COMMENT '品牌Logo',
  `website` varchar(255) COMMENT '官网地址',
  `country` varchar(50) COMMENT '国家',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌表';

-- 使用订单数据库并创建表
USE order_db;

CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `user_id` int NOT NULL COMMENT '用户ID',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '订单状态',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `payable_amount` decimal(10,2) NOT NULL COMMENT '应付金额',
  `shipping_fee` decimal(10,2) DEFAULT '0.00' COMMENT '运费',
  `payment_method` varchar(20) COMMENT '支付方式',
  `payment_status` varchar(20) DEFAULT 'UNPAID' COMMENT '支付状态',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人手机号',
  `receiver_address` varchar(255) NOT NULL COMMENT '收货地址',
  `remark` varchar(500) COMMENT '备注',
  `pay_time` datetime COMMENT '支付时间',
  `ship_time` datetime COMMENT '发货时间',
  `finish_time` datetime COMMENT '完成时间',
  `cancel_time` datetime COMMENT '取消时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

CREATE TABLE IF NOT EXISTS `order_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` int NOT NULL COMMENT '订单ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) COMMENT '商品图片',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `quantity` int NOT NULL COMMENT '购买数量',
  `total_price` decimal(10,2) NOT NULL COMMENT '总价',
  `specifications` varchar(500) COMMENT '规格参数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

CREATE TABLE IF NOT EXISTS `cart` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) COMMENT '商品图片',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `quantity` int NOT NULL COMMENT '数量',
  `specifications` varchar(500) COMMENT '规格参数',
  `selected` tinyint DEFAULT '1' COMMENT '是否选中',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 插入一些示例数据
USE product_db;

INSERT IGNORE INTO `category` (`id`, `name`, `description`, `parent_id`, `level`) VALUES
(1, '婴儿用品', '0-3岁婴儿相关用品', 0, 1),
(2, '幼儿用品', '3-6岁幼儿相关用品', 0, 1),
(3, '孕产用品', '孕妇和产妇相关用品', 0, 1),
(4, '奶粉', '各品牌婴幼儿奶粉', 1, 2),
(5, '辅食', '婴幼儿辅食', 1, 2),
(6, '纸尿裤', '婴儿纸尿裤', 1, 2);

INSERT IGNORE INTO `brand` (`id`, `name`, `description`, `country`) VALUES
(1, '惠氏', '全球知名婴幼儿营养品牌', '美国'),
(2, '雀巢', '全球食品饮料公司', '瑞士'),
(3, '美赞臣', '婴幼儿营养专家', '美国'),
(4, '帮宝适', '宝洁公司纸尿裤品牌', '美国'),
(5, '好奇', '金佰利公司纸尿裤品牌', '美国');

INSERT IGNORE INTO `product` (`id`, `name`, `description`, `price`, `original_price`, `stock`, `category_id`, `brand_id`, `main_image`, `status`) VALUES
(1, '惠氏启赋3段奶粉800g', '适合12-36个月幼儿，含有丰富的营养成分', 328.00, 358.00, 100, 4, 1, '/images/product1.jpg', 'ACTIVE'),
(2, '美赞臣蓝臻2段奶粉900g', '适合6-12个月婴儿，接近母乳营养', 298.00, 328.00, 80, 4, 3, '/images/product2.jpg', 'ACTIVE'),
(3, '帮宝适一级帮纸尿裤L码', '超薄透气，12小时长效干爽', 89.00, 99.00, 200, 6, 4, '/images/product3.jpg', 'ACTIVE'),
(4, '好奇铂金装纸尿裤M码', '云柔表层，瞬吸锁水', 79.00, 89.00, 150, 6, 5, '/images/product4.jpg', 'ACTIVE');

USE user_db;

-- 插入测试用户
INSERT IGNORE INTO `user` (`id`, `username`, `password`, `email`, `phone`, `nickname`, `status`) VALUES
(1, 'testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P4krMaAkdnmNlI', 'test@example.com', '13800138000', '测试用户', 'ACTIVE');