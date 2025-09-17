-- =================================================================================
-- 微服务架构数据库设计
-- 版本: 1.0.0
-- 创建时间: 2025-09-13
-- 描述: 母婴商城微服务架构数据库，每个服务独立数据库
-- =================================================================================

-- =================================================================================
-- 1. 用户服务数据库 (muying_user_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_user_service;
CREATE DATABASE muying_user_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_user_service;

-- 用户基本信息表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `gender` tinyint DEFAULT '0' COMMENT '性别: 0-未知, 1-男, 2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-正常',
  `register_source` varchar(20) DEFAULT NULL COMMENT '注册来源',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户账户表
CREATE TABLE `user_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  `frozen_balance` decimal(10,2) DEFAULT '0.00' COMMENT '冻结余额',
  `total_recharge` decimal(10,2) DEFAULT '0.00' COMMENT '累计充值',
  `total_consume` decimal(10,2) DEFAULT '0.00' COMMENT '累计消费',
  `payment_password` varchar(100) DEFAULT NULL COMMENT '支付密码',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-冻结, 1-正常',
  `version` int DEFAULT '0' COMMENT '乐观锁版本号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账户表';

-- 用户地址表
CREATE TABLE `user_address` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `province` varchar(50) NOT NULL COMMENT '省份',
  `city` varchar(50) NOT NULL COMMENT '城市',
  `district` varchar(50) NOT NULL COMMENT '区县',
  `detail_address` varchar(255) NOT NULL COMMENT '详细地址',
  `postal_code` varchar(10) DEFAULT NULL COMMENT '邮编',
  `is_default` tinyint DEFAULT '0' COMMENT '是否默认地址: 0-否, 1-是',
  `tag` varchar(20) DEFAULT NULL COMMENT '地址标签: 家, 公司, 学校等',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_default` (`is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- 会员等级表
CREATE TABLE `member_level` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `level_name` varchar(50) NOT NULL COMMENT '等级名称',
  `level_code` varchar(20) NOT NULL COMMENT '等级代码',
  `min_growth_value` int NOT NULL COMMENT '最小成长值',
  `max_growth_value` int NOT NULL COMMENT '最大成长值',
  `discount_rate` decimal(4,2) DEFAULT '1.00' COMMENT '折扣率',
  `points_multiplier` decimal(4,2) DEFAULT '1.00' COMMENT '积分倍率',
  `privileges` json DEFAULT NULL COMMENT '会员权益',
  `icon` varchar(255) DEFAULT NULL COMMENT '等级图标',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_level_code` (`level_code`),
  KEY `idx_growth_value` (`min_growth_value`, `max_growth_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级表';

-- 用户会员信息表
CREATE TABLE `user_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会员信息ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `level_id` bigint NOT NULL COMMENT '会员等级ID',
  `growth_value` int DEFAULT '0' COMMENT '成长值',
  `expire_time` datetime DEFAULT NULL COMMENT '会员过期时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户会员信息表';

-- 账户交易记录表
CREATE TABLE `account_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '交易ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `transaction_no` varchar(64) NOT NULL COMMENT '交易流水号',
  `type` varchar(20) NOT NULL COMMENT '交易类型: RECHARGE-充值, CONSUME-消费, REFUND-退款, WITHDRAW-提现',
  `amount` decimal(10,2) NOT NULL COMMENT '交易金额',
  `balance_before` decimal(10,2) NOT NULL COMMENT '交易前余额',
  `balance_after` decimal(10,2) NOT NULL COMMENT '交易后余额',
  `order_no` varchar(64) DEFAULT NULL COMMENT '关联订单号',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-处理中, 1-成功, 2-失败',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transaction_no` (`transaction_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户交易记录表';

-- =================================================================================
-- 2. 商品服务数据库 (muying_product_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_product_service;
CREATE DATABASE muying_product_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_product_service;

-- 商品分类表
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) DEFAULT NULL COMMENT '分类编码',
  `level` tinyint NOT NULL COMMENT '分类层级',
  `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
  `image` varchar(255) DEFAULT NULL COMMENT '分类图片',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `is_show` tinyint DEFAULT '1' COMMENT '是否显示: 0-否, 1-是',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_level` (`level`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 品牌表
CREATE TABLE `brand` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `brand_name` varchar(50) NOT NULL COMMENT '品牌名称',
  `brand_code` varchar(50) DEFAULT NULL COMMENT '品牌编码',
  `logo` varchar(255) DEFAULT NULL COMMENT '品牌logo',
  `description` text COMMENT '品牌描述',
  `website` varchar(255) DEFAULT NULL COMMENT '品牌官网',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `is_show` tinyint DEFAULT '1' COMMENT '是否显示: 0-否, 1-是',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_brand_name` (`brand_name`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

-- 商品表
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_sn` varchar(64) NOT NULL COMMENT '商品编号',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `title` varchar(255) DEFAULT NULL COMMENT '商品标题',
  `subtitle` varchar(255) DEFAULT NULL COMMENT '副标题',
  `main_image` varchar(255) DEFAULT NULL COMMENT '主图',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `market_price` decimal(10,2) DEFAULT NULL COMMENT '市场价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `stock` int DEFAULT '0' COMMENT '库存数量',
  `warning_stock` int DEFAULT '0' COMMENT '预警库存',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `weight` decimal(10,2) DEFAULT NULL COMMENT '重量(kg)',
  `volume` decimal(10,2) DEFAULT NULL COMMENT '体积(m³)',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `sales_count` int DEFAULT '0' COMMENT '销量',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `is_new` tinyint DEFAULT '0' COMMENT '是否新品: 0-否, 1-是',
  `is_hot` tinyint DEFAULT '0' COMMENT '是否热销: 0-否, 1-是',
  `is_recommend` tinyint DEFAULT '0' COMMENT '是否推荐: 0-否, 1-是',
  `is_on_sale` tinyint DEFAULT '1' COMMENT '是否上架: 0-否, 1-是',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_sn` (`product_sn`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_price` (`price`),
  KEY `idx_sales_count` (`sales_count`),
  KEY `idx_create_time` (`create_time`),
  FULLTEXT KEY `ft_product_name` (`product_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品图片表
CREATE TABLE `product_image` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `image_type` tinyint DEFAULT '1' COMMENT '图片类型: 1-商品图, 2-详情图',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_image_type` (`image_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';

-- 商品规格表
CREATE TABLE `product_specs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `spec_name` varchar(50) NOT NULL COMMENT '规格名称',
  `spec_value` varchar(100) NOT NULL COMMENT '规格值',
  `sku` varchar(64) NOT NULL COMMENT 'SKU编码',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int DEFAULT '0' COMMENT '库存',
  `image` varchar(255) DEFAULT NULL COMMENT '规格图片',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku` (`sku`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';

-- 商品属性表
CREATE TABLE `product_attribute` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `attribute_name` varchar(50) NOT NULL COMMENT '属性名',
  `attribute_value` varchar(255) NOT NULL COMMENT '属性值',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';

-- 商品详情表
CREATE TABLE `product_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `description` text COMMENT '商品描述',
  `content` longtext COMMENT '商品详情内容',
  `parameters` json DEFAULT NULL COMMENT '商品参数',
  `service_info` text COMMENT '售后服务',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品详情表';

-- 库存记录表
CREATE TABLE `inventory_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku` varchar(64) DEFAULT NULL COMMENT 'SKU编码',
  `type` varchar(20) NOT NULL COMMENT '类型: IN-入库, OUT-出库, ADJUST-调整',
  `quantity` int NOT NULL COMMENT '数量',
  `stock_before` int NOT NULL COMMENT '操作前库存',
  `stock_after` int NOT NULL COMMENT '操作后库存',
  `order_no` varchar(64) DEFAULT NULL COMMENT '关联订单号',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_sku` (`sku`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存记录表';

-- =================================================================================
-- 3. 订单服务数据库 (muying_order_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_order_service;
CREATE DATABASE muying_order_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_order_service;

-- 订单表
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名(冗余)',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `product_amount` decimal(10,2) NOT NULL COMMENT '商品总金额',
  `freight_amount` decimal(10,2) DEFAULT '0.00' COMMENT '运费',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `coupon_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠券金额',
  `points_amount` decimal(10,2) DEFAULT '0.00' COMMENT '积分抵扣金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_no` varchar(64) DEFAULT NULL COMMENT '支付流水号',
  `order_status` varchar(20) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态',
  `shipping_status` varchar(20) DEFAULT 'UNSHIPPED' COMMENT '发货状态',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `receiver_province` varchar(50) NOT NULL COMMENT '省份',
  `receiver_city` varchar(50) NOT NULL COMMENT '城市',
  `receiver_district` varchar(50) NOT NULL COMMENT '区县',
  `receiver_address` varchar(255) NOT NULL COMMENT '详细地址',
  `receiver_postal_code` varchar(10) DEFAULT NULL COMMENT '邮编',
  `shipping_time` datetime DEFAULT NULL COMMENT '发货时间',
  `shipping_company` varchar(50) DEFAULT NULL COMMENT '快递公司',
  `shipping_no` varchar(50) DEFAULT NULL COMMENT '快递单号',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(255) DEFAULT NULL COMMENT '取消原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `invoice_type` tinyint DEFAULT '0' COMMENT '发票类型: 0-不开发票, 1-电子发票, 2-纸质发票',
  `invoice_title` varchar(100) DEFAULT NULL COMMENT '发票抬头',
  `invoice_tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `source_type` varchar(20) DEFAULT NULL COMMENT '订单来源',
  `version` int DEFAULT '0' COMMENT '乐观锁版本号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_payment_time` (`payment_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单商品表
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_sn` varchar(64) NOT NULL COMMENT '商品编号',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `sku_id` bigint DEFAULT NULL COMMENT 'SKU ID',
  `sku` varchar(64) DEFAULT NULL COMMENT 'SKU编码',
  `spec_name` varchar(100) DEFAULT NULL COMMENT '规格名称',
  `quantity` int NOT NULL COMMENT '购买数量',
  `total_amount` decimal(10,2) NOT NULL COMMENT '商品总金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `real_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `is_gift` tinyint DEFAULT '0' COMMENT '是否赠品: 0-否, 1-是',
  `is_commented` tinyint DEFAULT '0' COMMENT '是否已评价: 0-否, 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 购物车表
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_sn` varchar(64) NOT NULL COMMENT '商品编号',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `sku_id` bigint DEFAULT NULL COMMENT 'SKU ID',
  `sku` varchar(64) DEFAULT NULL COMMENT 'SKU编码',
  `spec_name` varchar(100) DEFAULT NULL COMMENT '规格名称',
  `quantity` int NOT NULL COMMENT '数量',
  `is_selected` tinyint DEFAULT '1' COMMENT '是否选中: 0-否, 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product_sku` (`user_id`, `product_id`, `sku_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单状态日志表
CREATE TABLE `order_status_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `status_before` varchar(20) DEFAULT NULL COMMENT '变更前状态',
  `status_after` varchar(20) NOT NULL COMMENT '变更后状态',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `operator_type` varchar(20) DEFAULT NULL COMMENT '操作人类型: USER-用户, ADMIN-管理员, SYSTEM-系统',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单状态日志表';

-- 收藏表
CREATE TABLE `favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) DEFAULT NULL COMMENT '商品名称(冗余)',
  `product_image` varchar(255) DEFAULT NULL COMMENT '商品图片(冗余)',
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '商品价格(冗余)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- =================================================================================
-- 4. 支付服务数据库 (muying_payment_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_payment_service;
CREATE DATABASE muying_payment_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_payment_service;

-- 支付记录表
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `payment_no` varchar(64) NOT NULL COMMENT '支付流水号',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `payment_method` varchar(20) NOT NULL COMMENT '支付方式: ALIPAY, WECHAT, BALANCE',
  `payment_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `trade_no` varchar(100) DEFAULT NULL COMMENT '第三方交易号',
  `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
  `callback_content` text COMMENT '回调内容',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `version` int DEFAULT '0' COMMENT '乐观锁版本号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_payment_time` (`payment_time`),
  KEY `idx_trade_no` (`trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 退款记录表
CREATE TABLE `refund` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退款ID',
  `refund_no` varchar(64) NOT NULL COMMENT '退款单号',
  `payment_no` varchar(64) NOT NULL COMMENT '原支付流水号',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `refund_amount` decimal(10,2) NOT NULL COMMENT '退款金额',
  `refund_reason` varchar(255) DEFAULT NULL COMMENT '退款原因',
  `refund_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '退款状态',
  `refund_method` varchar(20) NOT NULL COMMENT '退款方式',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `trade_refund_no` varchar(100) DEFAULT NULL COMMENT '第三方退款单号',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `version` int DEFAULT '0' COMMENT '乐观锁版本号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_no` (`refund_no`),
  KEY `idx_payment_no` (`payment_no`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_refund_status` (`refund_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款记录表';

-- 支付状态日志表
CREATE TABLE `payment_status_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `payment_id` bigint NOT NULL COMMENT '支付ID',
  `payment_no` varchar(64) NOT NULL COMMENT '支付流水号',
  `status_before` varchar(20) DEFAULT NULL COMMENT '变更前状态',
  `status_after` varchar(20) NOT NULL COMMENT '变更后状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_payment_id` (`payment_id`),
  KEY `idx_payment_no` (`payment_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付状态日志表';

-- 退款日志表
CREATE TABLE `refund_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `refund_id` bigint NOT NULL COMMENT '退款ID',
  `refund_no` varchar(64) NOT NULL COMMENT '退款单号',
  `status_before` varchar(20) DEFAULT NULL COMMENT '变更前状态',
  `status_after` varchar(20) NOT NULL COMMENT '变更后状态',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_refund_id` (`refund_id`),
  KEY `idx_refund_no` (`refund_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款日志表';

-- =================================================================================
-- 5. 评论服务数据库 (muying_comment_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_comment_service;
CREATE DATABASE muying_comment_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_comment_service;

-- 评论表
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名(冗余)',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像(冗余)',
  `rating` tinyint NOT NULL COMMENT '评分: 1-5',
  `content` text COMMENT '评论内容',
  `images` json DEFAULT NULL COMMENT '评论图片',
  `is_anonymous` tinyint DEFAULT '0' COMMENT '是否匿名: 0-否, 1-是',
  `is_show` tinyint DEFAULT '1' COMMENT '是否显示: 0-否, 1-是',
  `has_replied` tinyint DEFAULT '0' COMMENT '是否已回复: 0-否, 1-是',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `reply_count` int DEFAULT '0' COMMENT '回复数',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-待审核, 1-已通过, 2-已屏蔽',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 评论回复表
CREATE TABLE `comment_reply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `comment_id` bigint NOT NULL COMMENT '评论ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父回复ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名(冗余)',
  `user_type` varchar(20) DEFAULT 'USER' COMMENT '用户类型: USER-用户, MERCHANT-商家',
  `to_user_id` bigint DEFAULT NULL COMMENT '回复目标用户ID',
  `to_username` varchar(50) DEFAULT NULL COMMENT '回复目标用户名',
  `content` text NOT NULL COMMENT '回复内容',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-待审核, 1-已通过, 2-已屏蔽',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_comment_id` (`comment_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论回复表';

-- 评论标签表
CREATE TABLE `comment_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `tag_type` varchar(20) DEFAULT NULL COMMENT '标签类型',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_tag_type` (`tag_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论标签表';

-- 评论标签关系表
CREATE TABLE `comment_tag_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `comment_id` bigint NOT NULL COMMENT '评论ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_tag` (`comment_id`, `tag_id`),
  KEY `idx_comment_id` (`comment_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论标签关系表';

-- 评论点赞表
CREATE TABLE `comment_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `comment_id` bigint DEFAULT NULL COMMENT '评论ID',
  `reply_id` bigint DEFAULT NULL COMMENT '回复ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`, `user_id`),
  UNIQUE KEY `uk_reply_user` (`reply_id`, `user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';

-- =================================================================================
-- 6. 物流服务数据库 (muying_logistics_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_logistics_service;
CREATE DATABASE muying_logistics_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_logistics_service;

-- 物流公司表
CREATE TABLE `logistics_company` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公司ID',
  `company_name` varchar(100) NOT NULL COMMENT '公司名称',
  `company_code` varchar(50) NOT NULL COMMENT '公司编码',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `website` varchar(255) DEFAULT NULL COMMENT '官网',
  `api_url` varchar(255) DEFAULT NULL COMMENT 'API地址',
  `api_key` varchar(100) DEFAULT NULL COMMENT 'API密钥',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_code` (`company_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流公司表';

-- 物流信息表
CREATE TABLE `logistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `logistics_no` varchar(64) NOT NULL COMMENT '物流单号',
  `company_id` bigint NOT NULL COMMENT '物流公司ID',
  `company_name` varchar(100) NOT NULL COMMENT '物流公司名称',
  `company_code` varchar(50) NOT NULL COMMENT '物流公司编码',
  `sender_name` varchar(50) DEFAULT NULL COMMENT '发件人姓名',
  `sender_phone` varchar(20) DEFAULT NULL COMMENT '发件人电话',
  `sender_address` varchar(255) DEFAULT NULL COMMENT '发件人地址',
  `receiver_name` varchar(50) NOT NULL COMMENT '收件人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收件人电话',
  `receiver_address` varchar(255) NOT NULL COMMENT '收件人地址',
  `logistics_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '物流状态',
  `current_location` varchar(255) DEFAULT NULL COMMENT '当前位置',
  `is_signed` tinyint DEFAULT '0' COMMENT '是否签收: 0-否, 1-是',
  `sign_time` datetime DEFAULT NULL COMMENT '签收时间',
  `sign_person` varchar(50) DEFAULT NULL COMMENT '签收人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_logistics_no` (`logistics_no`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_logistics_status` (`logistics_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息表';

-- 物流轨迹表
CREATE TABLE `logistics_track` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '轨迹ID',
  `logistics_id` bigint NOT NULL COMMENT '物流ID',
  `logistics_no` varchar(64) NOT NULL COMMENT '物流单号',
  `location` varchar(255) DEFAULT NULL COMMENT '位置',
  `description` text COMMENT '描述',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  `operator_phone` varchar(20) DEFAULT NULL COMMENT '操作人电话',
  `track_time` datetime NOT NULL COMMENT '轨迹时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_logistics_id` (`logistics_id`),
  KEY `idx_logistics_no` (`logistics_no`),
  KEY `idx_track_time` (`track_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹表';

-- =================================================================================
-- 7. 积分服务数据库 (muying_points_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_points_service;
CREATE DATABASE muying_points_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_points_service;

-- 用户积分表
CREATE TABLE `user_points` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '积分ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_points` int DEFAULT '0' COMMENT '总积分',
  `available_points` int DEFAULT '0' COMMENT '可用积分',
  `frozen_points` int DEFAULT '0' COMMENT '冻结积分',
  `used_points` int DEFAULT '0' COMMENT '已使用积分',
  `expired_points` int DEFAULT '0' COMMENT '已过期积分',
  `version` int DEFAULT '0' COMMENT '乐观锁版本号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户积分表';

-- 积分历史表
CREATE TABLE `points_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '历史ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `transaction_no` varchar(64) NOT NULL COMMENT '交易流水号',
  `type` varchar(20) NOT NULL COMMENT '类型: EARN-获得, USE-使用, EXPIRE-过期, FREEZE-冻结, UNFREEZE-解冻',
  `points` int NOT NULL COMMENT '积分数',
  `balance_before` int NOT NULL COMMENT '变更前积分',
  `balance_after` int NOT NULL COMMENT '变更后积分',
  `source` varchar(50) DEFAULT NULL COMMENT '来源',
  `order_no` varchar(64) DEFAULT NULL COMMENT '关联订单号',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-无效, 1-有效',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transaction_no` (`transaction_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_expire_time` (`expire_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分历史表';

-- 积分规则表
CREATE TABLE `points_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `rule_code` varchar(50) NOT NULL COMMENT '规则编码',
  `rule_type` varchar(20) NOT NULL COMMENT '规则类型: SHOPPING-购物, SIGN-签到, COMMENT-评论, SHARE-分享',
  `points_value` int DEFAULT NULL COMMENT '固定积分值',
  `points_rate` decimal(5,2) DEFAULT NULL COMMENT '积分比率',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最小金额',
  `max_points` int DEFAULT NULL COMMENT '最大积分',
  `daily_limit` int DEFAULT NULL COMMENT '每日限制',
  `total_limit` int DEFAULT NULL COMMENT '总限制',
  `valid_days` int DEFAULT NULL COMMENT '有效天数',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule_code` (`rule_code`),
  KEY `idx_rule_type` (`rule_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分规则表';

-- 积分商品表
CREATE TABLE `points_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `points_price` int NOT NULL COMMENT '积分价格',
  `cash_price` decimal(10,2) DEFAULT NULL COMMENT '现金价格',
  `stock` int DEFAULT '0' COMMENT '库存',
  `exchange_limit` int DEFAULT NULL COMMENT '兑换限制',
  `description` text COMMENT '描述',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-下架, 1-上架',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分商品表';

-- 积分兑换记录表
CREATE TABLE `points_exchange` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '兑换ID',
  `exchange_no` varchar(64) NOT NULL COMMENT '兑换单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `points_price` int NOT NULL COMMENT '积分价格',
  `cash_price` decimal(10,2) DEFAULT NULL COMMENT '现金价格',
  `quantity` int NOT NULL COMMENT '数量',
  `total_points` int NOT NULL COMMENT '总积分',
  `total_cash` decimal(10,2) DEFAULT NULL COMMENT '总现金',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
  `shipping_info` json DEFAULT NULL COMMENT '配送信息',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exchange_no` (`exchange_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分兑换记录表';

-- =================================================================================
-- 8. 搜索服务数据库 (muying_search_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_search_service;
CREATE DATABASE muying_search_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_search_service;

-- 搜索历史表
CREATE TABLE `search_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '历史ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `keyword` varchar(100) NOT NULL COMMENT '搜索关键词',
  `search_type` varchar(20) DEFAULT 'PRODUCT' COMMENT '搜索类型',
  `result_count` int DEFAULT '0' COMMENT '结果数量',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `device_type` varchar(20) DEFAULT NULL COMMENT '设备类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_keyword` (`keyword`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';

-- 热门搜索表
CREATE TABLE `hot_search` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '热搜ID',
  `keyword` varchar(100) NOT NULL COMMENT '关键词',
  `search_count` bigint DEFAULT '0' COMMENT '搜索次数',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `is_show` tinyint DEFAULT '1' COMMENT '是否显示: 0-否, 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_keyword` (`keyword`),
  KEY `idx_search_count` (`search_count`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='热门搜索表';

-- 搜索推荐表
CREATE TABLE `search_recommend` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '推荐ID',
  `keyword` varchar(100) NOT NULL COMMENT '关键词',
  `recommend_type` varchar(20) DEFAULT NULL COMMENT '推荐类型',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `is_show` tinyint DEFAULT '1' COMMENT '是否显示: 0-否, 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_keyword` (`keyword`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索推荐表';

-- 搜索统计表
CREATE TABLE `search_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `date` date NOT NULL COMMENT '日期',
  `keyword` varchar(100) NOT NULL COMMENT '关键词',
  `search_count` int DEFAULT '0' COMMENT '搜索次数',
  `user_count` int DEFAULT '0' COMMENT '用户数',
  `click_count` int DEFAULT '0' COMMENT '点击次数',
  `conversion_count` int DEFAULT '0' COMMENT '转化次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date_keyword` (`date`, `keyword`),
  KEY `idx_date` (`date`),
  KEY `idx_keyword` (`keyword`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索统计表';

-- =================================================================================
-- 9. 管理员服务数据库 (muying_admin_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_admin_service;
CREATE DATABASE muying_admin_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_admin_service;

-- 管理员表
CREATE TABLE `admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `department` varchar(50) DEFAULT NULL COMMENT '部门',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 角色表
CREATE TABLE `admin_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE `admin_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父权限ID',
  `permission_name` varchar(50) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(100) NOT NULL COMMENT '权限编码',
  `permission_type` varchar(20) DEFAULT NULL COMMENT '权限类型: MENU-菜单, BUTTON-按钮, API-接口',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关系表
CREATE TABLE `admin_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关系表';

-- 管理员登录记录表
CREATE TABLE `admin_login_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `admin_id` bigint NOT NULL COMMENT '管理员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `login_location` varchar(100) DEFAULT NULL COMMENT '登录地点',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `login_status` tinyint DEFAULT '1' COMMENT '登录状态: 0-失败, 1-成功',
  `failure_reason` varchar(255) DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`),
  KEY `idx_admin_id` (`admin_id`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员登录记录表';

-- 管理员操作日志表
CREATE TABLE `admin_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` bigint NOT NULL COMMENT '管理员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `module` varchar(50) DEFAULT NULL COMMENT '模块',
  `operation` varchar(100) NOT NULL COMMENT '操作',
  `method` varchar(200) DEFAULT NULL COMMENT '方法',
  `params` text COMMENT '参数',
  `result` text COMMENT '结果',
  `error_msg` text COMMENT '错误信息',
  `operation_time` bigint DEFAULT NULL COMMENT '操作耗时(ms)',
  `operation_ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `operation_location` varchar(100) DEFAULT NULL COMMENT '操作地点',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-失败, 1-成功',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_admin_id` (`admin_id`),
  KEY `idx_module` (`module`),
  KEY `idx_operation` (`operation`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';

-- =================================================================================
-- 10. 优惠券服务数据库 (muying_coupon_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_coupon_service;
CREATE DATABASE muying_coupon_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_coupon_service;

-- 优惠券模板表
CREATE TABLE `coupon_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `coupon_name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `coupon_type` varchar(20) NOT NULL COMMENT '优惠券类型: FULL_REDUCTION-满减, DISCOUNT-折扣, CASH-现金券',
  `face_value` decimal(10,2) DEFAULT NULL COMMENT '面值',
  `discount_rate` decimal(4,2) DEFAULT NULL COMMENT '折扣率',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最低使用金额',
  `max_discount` decimal(10,2) DEFAULT NULL COMMENT '最高优惠金额',
  `use_scope` varchar(20) DEFAULT 'ALL' COMMENT '使用范围: ALL-全场, CATEGORY-指定分类, PRODUCT-指定商品',
  `scope_ids` json DEFAULT NULL COMMENT '范围ID列表',
  `valid_type` varchar(20) NOT NULL COMMENT '有效期类型: FIXED-固定日期, DAYS-领取后天数',
  `valid_days` int DEFAULT NULL COMMENT '有效天数',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `total_quantity` int DEFAULT NULL COMMENT '发放总量',
  `issued_quantity` int DEFAULT '0' COMMENT '已发放数量',
  `used_quantity` int DEFAULT '0' COMMENT '已使用数量',
  `per_user_limit` int DEFAULT '1' COMMENT '每人限领',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_coupon_type` (`coupon_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板表';

-- 用户优惠券表
CREATE TABLE `user_coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户优惠券ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `coupon_code` varchar(50) NOT NULL COMMENT '优惠券码',
  `coupon_name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `coupon_type` varchar(20) NOT NULL COMMENT '优惠券类型',
  `face_value` decimal(10,2) DEFAULT NULL COMMENT '面值',
  `discount_rate` decimal(4,2) DEFAULT NULL COMMENT '折扣率',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最低使用金额',
  `use_scope` varchar(20) DEFAULT 'ALL' COMMENT '使用范围',
  `scope_ids` json DEFAULT NULL COMMENT '范围ID列表',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `use_status` varchar(20) NOT NULL DEFAULT 'UNUSED' COMMENT '使用状态: UNUSED-未使用, USED-已使用, EXPIRED-已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `order_no` varchar(64) DEFAULT NULL COMMENT '使用订单号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coupon_code` (`coupon_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_use_status` (`use_status`),
  KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- 优惠券发放记录表
CREATE TABLE `coupon_issue_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `issue_type` varchar(20) DEFAULT NULL COMMENT '发放方式: MANUAL-手动, AUTO-自动, DRAW-领取',
  `issue_channel` varchar(50) DEFAULT NULL COMMENT '发放渠道',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券发放记录表';

-- =================================================================================
-- 11. 消息服务数据库 (muying_message_service)
-- =================================================================================
DROP DATABASE IF EXISTS muying_message_service;
CREATE DATABASE muying_message_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_message_service;

-- 用户消息表
CREATE TABLE `user_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `message_type` varchar(20) NOT NULL COMMENT '消息类型: SYSTEM-系统, ORDER-订单, PROMOTION-促销, LOGISTICS-物流',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `business_id` varchar(64) DEFAULT NULL COMMENT '业务ID',
  `is_read` tinyint DEFAULT '0' COMMENT '是否已读: 0-未读, 1-已读',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除: 0-否, 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_message_type` (`message_type`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息表';

-- 消息模板表
CREATE TABLE `message_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_code` varchar(50) NOT NULL COMMENT '模板编码',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_type` varchar(20) NOT NULL COMMENT '模板类型',
  `title_template` varchar(200) NOT NULL COMMENT '标题模板',
  `content_template` text NOT NULL COMMENT '内容模板',
  `variables` json DEFAULT NULL COMMENT '变量列表',
  `status` tinyint DEFAULT '1' COMMENT '状态: 0-禁用, 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';

-- 推送记录表
CREATE TABLE `push_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `push_type` varchar(20) NOT NULL COMMENT '推送类型: SMS-短信, EMAIL-邮件, APP-应用推送',
  `template_id` bigint DEFAULT NULL COMMENT '模板ID',
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `receiver` varchar(100) NOT NULL COMMENT '接收者',
  `push_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '推送状态',
  `push_time` datetime DEFAULT NULL COMMENT '推送时间',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '错误信息',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_push_type` (`push_type`),
  KEY `idx_push_status` (`push_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送记录表';

-- =================================================================================
-- 初始化数据
-- =================================================================================

-- 初始化管理员数据
USE muying_admin_service;
INSERT INTO `admin_role` (`role_name`, `role_code`, `description`) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有所有权限'),
('运营管理员', 'OPERATION_ADMIN', '负责运营管理'),
('客服', 'CUSTOMER_SERVICE', '负责客户服务');

INSERT INTO `admin_user` (`username`, `password`, `real_name`, `role_id`) VALUES
('admin', '$2a$10$EqPxPo.5BQsWXsVNdzIhgOftKNeoJPLr8K7SXwWTBnQ8Md9qMocEy', '系统管理员', 1);

-- 初始化会员等级
USE muying_user_service;
INSERT INTO `member_level` (`level_name`, `level_code`, `min_growth_value`, `max_growth_value`, `discount_rate`, `points_multiplier`) VALUES
('普通会员', 'NORMAL', 0, 999, 1.00, 1.00),
('银卡会员', 'SILVER', 1000, 4999, 0.98, 1.20),
('金卡会员', 'GOLD', 5000, 9999, 0.95, 1.50),
('钻石会员', 'DIAMOND', 10000, 99999, 0.90, 2.00);

-- 初始化积分规则
USE muying_points_service;
INSERT INTO `points_rule` (`rule_name`, `rule_code`, `rule_type`, `points_value`, `description`) VALUES
('每日签到', 'DAILY_SIGN', 'SIGN', 10, '每日签到获得10积分'),
('购物积分', 'SHOPPING_POINTS', 'SHOPPING', NULL, '购物金额1:1获得积分'),
('评论奖励', 'COMMENT_REWARD', 'COMMENT', 20, '评论商品获得20积分'),
('分享奖励', 'SHARE_REWARD', 'SHARE', 5, '分享商品获得5积分');

-- 初始化物流公司
USE muying_logistics_service;
INSERT INTO `logistics_company` (`company_name`, `company_code`, `contact_phone`) VALUES
('顺丰速运', 'SF', '95338'),
('圆通快递', 'YTO', '95554'),
('中通快递', 'ZTO', '95311'),
('申通快递', 'STO', '95543'),
('韵达快递', 'YD', '95546'),
('京东物流', 'JD', '95311'),
('邮政EMS', 'EMS', '11183');

-- 创建索引优化查询性能
-- 注：这里只列出部分关键索引，实际应用中需要根据查询模式添加更多索引