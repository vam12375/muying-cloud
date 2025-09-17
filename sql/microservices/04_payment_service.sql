-- ================================================================================
-- 支付服务数据库 (muying_payment_service)
-- 版本: 1.0.0
-- 创建时间: 2025-09-16
-- 描述: 支付服务相关表，包括支付记录、退款记录、支付日志等
-- ================================================================================

-- 创建数据库
DROP DATABASE IF EXISTS muying_payment_service;
CREATE DATABASE muying_payment_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_payment_service;

-- 支付表
CREATE TABLE `payment` (
  `payment_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `payment_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号',
  `order_id` int UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名(冗余字段)',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `actual_amount` decimal(10, 2) NOT NULL COMMENT '实际支付金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'CNY' COMMENT '货币类型',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式：alipay-支付宝，wechat-微信支付，union-银联，wallet-余额支付',
  `payment_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付渠道：app-手机应用，web-网页，h5-手机网页，mini-小程序',
  `third_party_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方支付单号',
  `third_party_response` json NULL COMMENT '第三方支付响应',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '支付状态：pending-待支付，success-支付成功，failed-支付失败，cancelled-已取消，timeout-超时，refunded-已退款',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异步通知地址',
  `return_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '同步返回地址',
  `client_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `device_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备信息',
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`payment_id`) USING BTREE,
  UNIQUE INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_payment_method`(`payment_method` ASC) USING BTREE,
  INDEX `idx_third_party_no`(`third_party_no` ASC) USING BTREE,
  INDEX `idx_pay_time`(`pay_time` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付表' ROW_FORMAT = DYNAMIC;

-- 支付日志表
CREATE TABLE `payment_log` (
  `log_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `payment_id` int UNSIGNED NOT NULL COMMENT '支付ID',
  `payment_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号',
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作动作：create-创建，pay-支付，query-查询，notify-通知，cancel-取消',
  `old_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原状态',
  `new_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新状态',
  `request_params` json NULL COMMENT '请求参数',
  `response_data` json NULL COMMENT '响应数据',
  `error_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误码',
  `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `operator_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'system' COMMENT '操作类型：system-系统，user-用户，admin-管理员，third_party-第三方',
  `operator_id` int NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `client_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理',
  `execution_time` int NULL DEFAULT NULL COMMENT '执行时间(毫秒)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_payment_id`(`payment_id` ASC) USING BTREE,
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE,
  INDEX `idx_action`(`action` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_payment_log` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付日志表' ROW_FORMAT = DYNAMIC;

-- 支付状态日志表
CREATE TABLE `payment_state_log` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `payment_id` int UNSIGNED NOT NULL COMMENT '支付ID',
  `payment_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号',
  `old_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原状态',
  `new_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新状态',
  `change_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态变更原因',
  `operator_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'system' COMMENT '操作类型：system-系统，user-用户，admin-管理员，third_party-第三方',
  `operator_id` int NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_payment_id`(`payment_id` ASC) USING BTREE,
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_payment_state_log` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付状态日志表' ROW_FORMAT = DYNAMIC;

-- 退款表
CREATE TABLE `refund` (
  `refund_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '退款ID',
  `refund_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款单号',
  `payment_id` int UNSIGNED NOT NULL COMMENT '支付ID',
  `payment_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号',
  `order_id` int UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名(冗余字段)',
  `refund_amount` decimal(10, 2) NOT NULL COMMENT '退款金额',
  `refund_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '退款手续费',
  `actual_refund_amount` decimal(10, 2) NOT NULL COMMENT '实际退款金额',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'CNY' COMMENT '货币类型',
  `refund_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款方式：original-原路退回，wallet-退到余额，bank-退到银行卡',
  `refund_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款渠道',
  `third_party_refund_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方退款单号',
  `third_party_response` json NULL COMMENT '第三方退款响应',
  `refund_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'full' COMMENT '退款类型：full-全额退款，partial-部分退款',
  `refund_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款原因',
  `refund_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款描述',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '退款状态：pending-待处理，processing-处理中，success-退款成功，failed-退款失败，cancelled-已取消',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `process_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `success_time` datetime NULL DEFAULT NULL COMMENT '成功时间',
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `processor_id` int NULL DEFAULT NULL COMMENT '处理人ID',
  `processor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理人姓名',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`refund_id`) USING BTREE,
  UNIQUE INDEX `idx_refund_no`(`refund_no` ASC) USING BTREE,
  INDEX `idx_payment_id`(`payment_id` ASC) USING BTREE,
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_refund_method`(`refund_method` ASC) USING BTREE,
  INDEX `idx_apply_time`(`apply_time` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_refund_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款表' ROW_FORMAT = DYNAMIC;

-- 退款日志表
CREATE TABLE `refund_log` (
  `log_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `refund_id` int UNSIGNED NOT NULL COMMENT '退款ID',
  `refund_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款单号',
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作动作：apply-申请，approve-批准，reject-拒绝，process-处理，success-成功，fail-失败',
  `old_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原状态',
  `new_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新状态',
  `operator_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'system' COMMENT '操作类型：system-系统，user-用户，admin-管理员，third_party-第三方',
  `operator_id` int NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `operation_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作描述',
  `request_params` json NULL COMMENT '请求参数',
  `response_data` json NULL COMMENT '响应数据',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_refund_id`(`refund_id` ASC) USING BTREE,
  INDEX `idx_refund_no`(`refund_no` ASC) USING BTREE,
  INDEX `idx_action`(`action` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_refund_log` FOREIGN KEY (`refund_id`) REFERENCES `refund` (`refund_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款日志表' ROW_FORMAT = DYNAMIC;

-- 支付配置表
CREATE TABLE `payment_config` (
  `config_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称',
  `config_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '配置值',
  `is_encrypted` tinyint(1) NULL DEFAULT 0 COMMENT '是否加密：0-否，1-是',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE INDEX `idx_method_key`(`payment_method` ASC, `config_key` ASC) USING BTREE,
  INDEX `idx_payment_method`(`payment_method` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付配置表' ROW_FORMAT = DYNAMIC;

-- 插入基础支付配置数据
INSERT INTO `payment_config` (`payment_method`, `config_name`, `config_key`, `config_value`, `description`, `sort_order`, `status`) VALUES
('alipay', '支付宝应用ID', 'app_id', '', '支付宝开放平台应用APPID', 1, 1),
('alipay', '支付宝私钥', 'private_key', '', '应用私钥', 2, 1),
('alipay', '支付宝公钥', 'alipay_public_key', '', '支付宝公钥', 3, 1),
('alipay', '签名类型', 'sign_type', 'RSA2', '签名算法类型', 4, 1),
('alipay', '支付网关', 'gateway_url', 'https://openapi.alipay.com/gateway.do', '支付宝网关地址', 5, 1),
('wechat', '微信应用ID', 'app_id', '', '微信公众号或小程序APPID', 1, 1),
('wechat', '商户号', 'mch_id', '', '微信支付商户号', 2, 1),
('wechat', '商户密钥', 'key', '', '商户支付密钥', 3, 1),
('wechat', '支付网关', 'gateway_url', 'https://api.mch.weixin.qq.com', '微信支付网关地址', 4, 1),
('wallet', '余额支付', 'enabled', '1', '是否启用余额支付', 1, 1);

-- 创建支付管理存储过程：创建支付订单
DELIMITER ;;
CREATE PROCEDURE `proc_create_payment`(
  IN p_order_id INT,
  IN p_order_no VARCHAR(50),
  IN p_user_id INT,
  IN p_pay_amount DECIMAL(10,2),
  IN p_payment_method VARCHAR(20),
  IN p_payment_channel VARCHAR(20),
  IN p_client_ip VARCHAR(45),
  IN p_expire_minutes INT,
  OUT p_payment_no VARCHAR(64),
  OUT p_payment_id INT,
  OUT p_result INT
)
BEGIN
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_result = 0; -- 失败
  END;
  
  START TRANSACTION;
  
  -- 生成支付单号
  SET p_payment_no = CONCAT('PAY', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), LPAD(FLOOR(RAND() * 1000000), 6, '0'));
  
  -- 创建支付记录
  INSERT INTO payment (
    payment_no, order_id, order_no, user_id, pay_amount, actual_amount,
    payment_method, payment_channel, client_ip, 
    expire_time, status
  ) VALUES (
    p_payment_no, p_order_id, p_order_no, p_user_id, p_pay_amount, p_pay_amount,
    p_payment_method, p_payment_channel, p_client_ip,
    DATE_ADD(NOW(), INTERVAL p_expire_minutes MINUTE), 'pending'
  );
  
  SET p_payment_id = LAST_INSERT_ID();
  
  -- 记录支付日志
  INSERT INTO payment_log (
    payment_id, payment_no, action, new_status, operator_type, client_ip
  ) VALUES (
    p_payment_id, p_payment_no, 'create', 'pending', 'system', p_client_ip
  );
  
  SET p_result = 1; -- 成功
  COMMIT;
END;;
DELIMITER ;

-- 创建支付状态更新存储过程
DELIMITER ;;
CREATE PROCEDURE `proc_update_payment_status`(
  IN p_payment_id INT,
  IN p_new_status VARCHAR(20),
  IN p_third_party_no VARCHAR(100),
  IN p_operator_type VARCHAR(20),
  IN p_operator_id INT,
  IN p_operator_name VARCHAR(50),
  IN p_remark VARCHAR(255),
  OUT p_result INT
)
BEGIN
  DECLARE v_old_status VARCHAR(20);
  DECLARE v_payment_no VARCHAR(64);
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_result = 0; -- 失败
  END;
  
  START TRANSACTION;
  
  -- 获取当前状态
  SELECT status, payment_no INTO v_old_status, v_payment_no 
  FROM payment WHERE payment_id = p_payment_id FOR UPDATE;
  
  IF v_old_status IS NULL THEN
    SET p_result = -1; -- 支付记录不存在
    ROLLBACK;
  ELSE
    -- 更新支付状态
    UPDATE payment SET 
      status = p_new_status,
      third_party_no = COALESCE(p_third_party_no, third_party_no),
      pay_time = CASE WHEN p_new_status = 'success' THEN NOW() ELSE pay_time END
    WHERE payment_id = p_payment_id;
    
    -- 记录状态变更日志
    INSERT INTO payment_state_log (
      payment_id, payment_no, old_state, new_state, 
      operator_type, operator_id, operator_name, remark
    ) VALUES (
      p_payment_id, v_payment_no, v_old_status, p_new_status,
      p_operator_type, p_operator_id, p_operator_name, p_remark
    );
    
    SET p_result = 1; -- 成功
    COMMIT;
  END IF;
END;;
DELIMITER ;

-- 创建退款申请存储过程
DELIMITER ;;
CREATE PROCEDURE `proc_create_refund`(
  IN p_payment_id INT,
  IN p_order_id INT,
  IN p_user_id INT,
  IN p_refund_amount DECIMAL(10,2),
  IN p_refund_reason VARCHAR(100),
  IN p_refund_desc VARCHAR(255),
  OUT p_refund_no VARCHAR(64),
  OUT p_refund_id INT,
  OUT p_result INT
)
BEGIN
  DECLARE v_payment_no VARCHAR(64);
  DECLARE v_order_no VARCHAR(50);
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_result = 0; -- 失败
  END;
  
  START TRANSACTION;
  
  -- 获取支付信息
  SELECT payment_no, order_no INTO v_payment_no, v_order_no
  FROM payment WHERE payment_id = p_payment_id;
  
  IF v_payment_no IS NULL THEN
    SET p_result = -1; -- 支付记录不存在
    ROLLBACK;
  ELSE
    -- 生成退款单号
    SET p_refund_no = CONCAT('REF', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), LPAD(FLOOR(RAND() * 1000000), 6, '0'));
    
    -- 创建退款记录
    INSERT INTO refund (
      refund_no, payment_id, payment_no, order_id, order_no, user_id,
      refund_amount, actual_refund_amount, refund_method, 
      refund_reason, refund_desc, status
    ) VALUES (
      p_refund_no, p_payment_id, v_payment_no, p_order_id, v_order_no, p_user_id,
      p_refund_amount, p_refund_amount, 'original',
      p_refund_reason, p_refund_desc, 'pending'
    );
    
    SET p_refund_id = LAST_INSERT_ID();
    
    -- 记录退款日志
    INSERT INTO refund_log (
      refund_id, refund_no, action, old_status, new_status, 
      operator_type, operation_desc
    ) VALUES (
      p_refund_id, p_refund_no, 'apply', NULL, 'pending',
      'user', '用户申请退款'
    );
    
    SET p_result = 1; -- 成功
    COMMIT;
  END IF;
END;;
DELIMITER ;

-- 创建支付统计视图
CREATE VIEW `v_payment_statistics` AS
SELECT 
  DATE(create_time) AS payment_date,
  payment_method,
  COUNT(*) AS total_count,
  COUNT(CASE WHEN status = 'success' THEN 1 END) AS success_count,
  COUNT(CASE WHEN status = 'failed' THEN 1 END) AS failed_count,
  COUNT(CASE WHEN status = 'pending' THEN 1 END) AS pending_count,
  SUM(pay_amount) AS total_amount,
  SUM(CASE WHEN status = 'success' THEN actual_amount ELSE 0 END) AS success_amount,
  AVG(CASE WHEN status = 'success' THEN actual_amount END) AS avg_success_amount,
  ROUND(COUNT(CASE WHEN status = 'success' THEN 1 END) * 100.0 / COUNT(*), 2) AS success_rate
FROM payment
WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
GROUP BY DATE(create_time), payment_method
ORDER BY payment_date DESC, payment_method;

-- 创建退款统计视图
CREATE VIEW `v_refund_statistics` AS
SELECT 
  DATE(create_time) AS refund_date,
  refund_method,
  COUNT(*) AS total_count,
  COUNT(CASE WHEN status = 'success' THEN 1 END) AS success_count,
  COUNT(CASE WHEN status = 'failed' THEN 1 END) AS failed_count,
  COUNT(CASE WHEN status = 'pending' THEN 1 END) AS pending_count,
  SUM(refund_amount) AS total_refund_amount,
  SUM(CASE WHEN status = 'success' THEN actual_refund_amount ELSE 0 END) AS success_refund_amount,
  AVG(CASE WHEN status = 'success' THEN actual_refund_amount END) AS avg_refund_amount
FROM refund
WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
GROUP BY DATE(create_time), refund_method
ORDER BY refund_date DESC, refund_method;

-- 创建索引优化查询性能
CREATE INDEX `idx_payment_user_method` ON `payment` (`user_id`, `payment_method`);
CREATE INDEX `idx_payment_status_time` ON `payment` (`status`, `pay_time`);
CREATE INDEX `idx_payment_method_time` ON `payment` (`payment_method`, `create_time`);
CREATE INDEX `idx_refund_user_status` ON `refund` (`user_id`, `status`);
CREATE INDEX `idx_refund_status_time` ON `refund` (`status`, `apply_time`);
CREATE INDEX `idx_payment_log_action_time` ON `payment_log` (`action`, `create_time`);
CREATE INDEX `idx_refund_log_action_time` ON `refund_log` (`action`, `create_time`);

SET FOREIGN_KEY_CHECKS = 1;