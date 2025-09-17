-- ================================================================================
-- 用户服务数据库 (muying_user_service)
-- 版本: 1.0.0
-- 创建时间: 2025-09-16
-- 描述: 用户服务相关表，包括用户信息、账户、地址、会员等级等
-- ================================================================================

-- 创建数据库
DROP DATABASE IF EXISTS muying_user_service;
CREATE DATABASE muying_user_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_user_service;

-- 用户基本信息表
CREATE TABLE `user` (
  `user_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` enum('male','female','unknown') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'unknown' COMMENT '性别',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `role` enum('admin','user') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'user' COMMENT '角色',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` int NOT NULL DEFAULT 1 COMMENT '版本号，用于乐观锁控制',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `idx_email`(`email` ASC) USING BTREE,
  INDEX `idx_version`(`version` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- 用户账户表
CREATE TABLE `user_account` (
  `account_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  `frozen_balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '冻结余额',
  `points` int NOT NULL DEFAULT 0 COMMENT '账户积分',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '账户状态：0-冻结，1-正常',
  `pay_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付密码（加密存储）',
  `security_level` tinyint NOT NULL DEFAULT 1 COMMENT '安全等级：1-低，2-中，3-高',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`account_id`) USING BTREE,
  UNIQUE INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `fk_user_account_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户账户表' ROW_FORMAT = DYNAMIC;

-- 用户地址表
CREATE TABLE `user_address` (
  `address_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `receiver` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省份',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市',
  `district` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区/县',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `postal_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`address_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_is_default`(`is_default` ASC) USING BTREE,
  CONSTRAINT `fk_user_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户地址表' ROW_FORMAT = DYNAMIC;

-- 会员等级表
CREATE TABLE `member_level` (
  `level_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `level_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '等级名称',
  `level_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '等级代码',
  `min_points` int NOT NULL DEFAULT 0 COMMENT '最小积分要求',
  `max_points` int NULL DEFAULT NULL COMMENT '最大积分要求',
  `discount_rate` decimal(4,2) NOT NULL DEFAULT 1.00 COMMENT '折扣率',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '等级描述',
  `benefits` json NULL COMMENT '会员权益',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`level_id`) USING BTREE,
  UNIQUE INDEX `idx_level_code`(`level_code` ASC) USING BTREE,
  INDEX `idx_points_range`(`min_points` ASC, `max_points` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员等级表' ROW_FORMAT = DYNAMIC;

-- 账户交易记录表
CREATE TABLE `account_transaction` (
  `transaction_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '交易ID',
  `transaction_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易单号',
  `account_id` int UNSIGNED NOT NULL COMMENT '账户ID',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '交易金额',
  `balance` decimal(10, 2) NOT NULL COMMENT '交易后余额',
  `type` tinyint NOT NULL COMMENT '交易类型：1-充值，2-消费，3-退款，4-提现，5-转账，6-收入，7-其他',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式：alipay-支付宝，wechat-微信支付，bank-银行卡，balance-余额，other-其他',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '交易状态：0-失败，1-成功，2-处理中，3-已取消',
  `related_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联ID（如订单ID、退款ID等）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易描述',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`transaction_id`) USING BTREE,
  UNIQUE INDEX `idx_transaction_no`(`transaction_no` ASC) USING BTREE,
  INDEX `idx_account_id`(`account_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_transaction_account` FOREIGN KEY (`account_id`) REFERENCES `user_account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_transaction_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账户交易记录表' ROW_FORMAT = DYNAMIC;

-- 用户积分表
CREATE TABLE `user_points` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '积分ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `points` int NULL DEFAULT 0 COMMENT '积分总数',
  `level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '普通会员' COMMENT '会员等级',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_user_points`(`user_id` ASC, `points` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分表' ROW_FORMAT = DYNAMIC;

-- 插入会员等级基础数据
INSERT INTO `member_level` (`level_name`, `level_code`, `min_points`, `max_points`, `discount_rate`, `description`) VALUES
('普通会员', 'NORMAL', 0, 999, 1.00, '普通会员，无特殊优惠'),
('银牌会员', 'SILVER', 1000, 4999, 0.98, '银牌会员，享受2%折扣'),
('金牌会员', 'GOLD', 5000, 19999, 0.95, '金牌会员，享受5%折扣'),
('钻石会员', 'DIAMOND', 20000, NULL, 0.90, '钻石会员，享受10%折扣和专属服务');

-- 创建存储过程：用户交易处理
DELIMITER ;;
CREATE PROCEDURE `proc_user_transaction`(
  IN p_user_id INT,
  IN p_amount DECIMAL(10, 2),
  IN p_type TINYINT,
  IN p_payment_method VARCHAR(20),
  IN p_related_id VARCHAR(64),
  IN p_description VARCHAR(255),
  IN p_remark VARCHAR(255),
  OUT p_transaction_id BIGINT,
  OUT p_result INT
)
BEGIN
  DECLARE v_account_id INT;
  DECLARE v_balance DECIMAL(10, 2);
  DECLARE v_status TINYINT;
  DECLARE v_transaction_no VARCHAR(64);
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_result = 0; -- 失败
  END;
  
  START TRANSACTION;
  
  -- 检查用户账户是否存在且正常
  SELECT account_id, balance, status INTO v_account_id, v_balance, v_status
  FROM user_account WHERE user_id = p_user_id FOR UPDATE;
  
  IF v_account_id IS NULL THEN
    SET p_result = 0; -- 账户不存在
    ROLLBACK;
  ELSEIF v_status = 0 THEN
    SET p_result = 0; -- 账户已冻结
    ROLLBACK;
  ELSE
    -- 消费、提现、转账需要检查余额是否充足
    IF (p_type IN (2, 4, 5) AND v_balance < p_amount) THEN
      SET p_result = 0; -- 余额不足
      ROLLBACK;
    ELSE
      -- 生成交易单号
      SET v_transaction_no = CONCAT('TXN', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), LPAD(FLOOR(RAND() * 1000000), 6, '0'));
      
      -- 更新账户余额
      IF p_type IN (1, 3, 6) THEN -- 充值、退款、收入
        UPDATE user_account SET balance = balance + p_amount WHERE account_id = v_account_id;
      ELSEIF p_type IN (2, 4, 5) THEN -- 消费、提现、转账
        UPDATE user_account SET balance = balance - p_amount WHERE account_id = v_account_id;
      END IF;
      
      -- 获取更新后的余额
      SELECT balance INTO v_balance FROM user_account WHERE account_id = v_account_id;
      
      -- 记录交易流水
      INSERT INTO account_transaction (
        transaction_no, account_id, user_id, amount, balance, type, 
        payment_method, status, related_id, description, remark
      ) VALUES (
        v_transaction_no, v_account_id, p_user_id, p_amount, v_balance, p_type,
        p_payment_method, 1, p_related_id, p_description, p_remark
      );
      
      -- 获取交易ID
      SET p_transaction_id = LAST_INSERT_ID();
      SET p_result = 1; -- 成功
      COMMIT;
    END IF;
  END IF;
END;;
DELIMITER ;

-- 创建存储过程：用户充值
DELIMITER ;;
CREATE PROCEDURE `proc_user_recharge`(
  IN p_user_id INT,
  IN p_amount DECIMAL(10, 2),
  IN p_payment_method VARCHAR(20),
  IN p_related_id VARCHAR(64),
  IN p_description VARCHAR(255),
  OUT p_transaction_id BIGINT,
  OUT p_result INT
)
BEGIN
  CALL proc_user_transaction(
    p_user_id, p_amount, 1, p_payment_method, p_related_id, 
    IFNULL(p_description, '账户充值'), NULL, p_transaction_id, p_result
  );
END;;
DELIMITER ;

-- 创建存储过程：用户消费
DELIMITER ;;
CREATE PROCEDURE `proc_user_consume`(
  IN p_user_id INT,
  IN p_amount DECIMAL(10, 2),
  IN p_payment_method VARCHAR(20),
  IN p_order_id VARCHAR(64),
  IN p_description VARCHAR(255),
  OUT p_transaction_id BIGINT,
  OUT p_result INT
)
BEGIN
  CALL proc_user_transaction(
    p_user_id, p_amount, 2, p_payment_method, p_order_id, 
    IFNULL(p_description, '商品购买'), NULL, p_transaction_id, p_result
  );
END;;
DELIMITER ;

-- 创建索引优化查询性能
CREATE INDEX `idx_user_create_time` ON `user` (`create_time`);
CREATE INDEX `idx_user_status_role` ON `user` (`status`, `role`);
CREATE INDEX `idx_account_balance` ON `user_account` (`balance`);
CREATE INDEX `idx_transaction_create_time_type` ON `account_transaction` (`create_time`, `type`);
CREATE INDEX `idx_address_default` ON `user_address` (`user_id`, `is_default`);

SET FOREIGN_KEY_CHECKS = 1;