-- ================================================================================
-- 评论服务数据库 (muying_comment_service)
-- 版本: 1.0.0
-- 创建时间: 2025-09-16
-- 描述: 评论服务相关表，包括商品评论、回复、标签、模板等
-- ================================================================================

-- 创建数据库
DROP DATABASE IF EXISTS muying_comment_service;
CREATE DATABASE muying_comment_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE muying_comment_service;

-- 评价表
CREATE TABLE `comment` (
  `comment_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `product_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `order_id` int UNSIGNED NOT NULL COMMENT '订单ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评价内容',
  `rating` tinyint NOT NULL DEFAULT 5 COMMENT '评分(1-5)',
  `images` json NULL COMMENT '评价图片',
  `is_anonymous` tinyint(1) NULL DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `has_replied` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已回复：0-否，1-是',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_rating`(`rating` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_product_rating_time`(`product_id` ASC, `rating` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价表' ROW_FORMAT = DYNAMIC;

-- 评价回复表
CREATE TABLE `comment_reply` (
  `reply_id` int NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `comment_id` int NOT NULL COMMENT '评价ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复内容',
  `reply_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '回复类型：1-商家回复，2-用户追评',
  `reply_user_id` int NULL DEFAULT NULL COMMENT '回复用户ID（商家回复时为管理员ID）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`reply_id`) USING BTREE,
  INDEX `idx_comment_id`(`comment_id` ASC) USING BTREE,
  INDEX `idx_reply_type`(`reply_type` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_comment_reply` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价回复表' ROW_FORMAT = Dynamic;

-- 评价标签表
CREATE TABLE `comment_tag` (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `tag_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '标签类型：1-系统标签，2-用户自定义标签',
  `product_category_id` int NULL DEFAULT NULL COMMENT '关联的商品分类ID（可为空）',
  `usage_count` int NOT NULL DEFAULT 0 COMMENT '使用次数',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tag_id`) USING BTREE,
  UNIQUE INDEX `uk_tag_name`(`tag_name` ASC) USING BTREE,
  INDEX `idx_product_category`(`product_category_id` ASC) USING BTREE,
  INDEX `idx_tag_type`(`tag_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_usage_count`(`usage_count` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价标签表' ROW_FORMAT = Dynamic;

-- 评价标签关系表
CREATE TABLE `comment_tag_relation` (
  `relation_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `comment_id` int NOT NULL COMMENT '评价ID',
  `tag_id` int NOT NULL COMMENT '标签ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`relation_id`) USING BTREE,
  UNIQUE INDEX `uk_comment_tag`(`comment_id` ASC, `tag_id` ASC) USING BTREE,
  INDEX `idx_comment_id`(`comment_id` ASC) USING BTREE,
  INDEX `idx_tag_id`(`tag_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_tag_relation_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_tag_relation_tag` FOREIGN KEY (`tag_id`) REFERENCES `comment_tag` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价标签关系表' ROW_FORMAT = Dynamic;

-- 评价模板表
CREATE TABLE `comment_template` (
  `template_id` int NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板名称',
  `template_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板内容',
  `template_type` tinyint NOT NULL DEFAULT 1 COMMENT '模板类型：1-系统预设，2-用户自定义',
  `min_rating` tinyint NULL DEFAULT NULL COMMENT '适用评分范围（最小值）',
  `max_rating` tinyint NULL DEFAULT NULL COMMENT '适用评分范围（最大值）',
  `category_id` int NULL DEFAULT NULL COMMENT '适用商品类别ID',
  `use_count` int NOT NULL DEFAULT 0 COMMENT '使用次数',
  `user_id` int NULL DEFAULT NULL COMMENT '创建用户ID（系统模板为null）',
  `weight` int NOT NULL DEFAULT 0 COMMENT '排序权重',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`template_id`) USING BTREE,
  INDEX `idx_template_type`(`template_type` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_rating_range`(`min_rating` ASC, `max_rating` ASC) USING BTREE,
  INDEX `idx_weight`(`weight` DESC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价模板表' ROW_FORMAT = Dynamic;

-- 评价奖励配置表
CREATE TABLE `comment_reward_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `reward_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖励类型：points-积分',
  `reward_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '基础奖励' COMMENT '奖励名称',
  `reward_description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '奖励描述',
  `reward_level` tinyint NOT NULL DEFAULT 1 COMMENT '奖励等级：1-基础，2-进阶，3-高级',
  `reward_value` int NOT NULL COMMENT '奖励值',
  `min_content_length` int NOT NULL DEFAULT 0 COMMENT '最小内容长度要求',
  `min_rating` tinyint NULL DEFAULT NULL COMMENT '最低评分要求',
  `require_image` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否要求图片：0-不要求，1-要求',
  `min_images` tinyint NOT NULL DEFAULT 0 COMMENT '最低图片数量要求',
  `is_first_comment` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否首次评价奖励：0-否，1-是',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`config_id`) USING BTREE,
  INDEX `idx_reward_type`(`reward_type` ASC) USING BTREE,
  INDEX `idx_reward_level`(`reward_level` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价奖励配置表' ROW_FORMAT = Dynamic;

-- 插入基础评价标签数据
INSERT INTO `comment_tag` (`tag_name`, `tag_type`, `product_category_id`, `usage_count`, `status`) VALUES
('物流快', 1, NULL, 0, 1),
('质量好', 1, NULL, 0, 1),
('性价比高', 1, NULL, 0, 1),
('描述相符', 1, NULL, 0, 1),
('服务好', 1, NULL, 0, 1),
('包装精美', 1, NULL, 0, 1),
('送货快', 1, NULL, 0, 1),
('正品保障', 1, NULL, 0, 1),
('材质优良', 1, NULL, 0, 1),
('做工精细', 1, NULL, 0, 1);

-- 插入基础评价模板数据
INSERT INTO `comment_template` (`template_name`, `template_content`, `template_type`, `min_rating`, `max_rating`, `weight`, `status`) VALUES
('好评模板1', '质量非常好，物流很快，包装也很完整，非常满意的一次购物体验！', 1, 4, 5, 100, 1),
('好评模板2', '商品质量很好，与描述一致，非常满意，下次还会购买！', 1, 4, 5, 90, 1),
('好评模板3', '收到货了，包装很好，物流很快，商品质量也不错，价格也实惠，很满意！', 1, 4, 5, 80, 1),
('中评模板1', '商品质量一般，没有想象中的那么好，但价格便宜，物有所值。', 1, 3, 3, 70, 1),
('中评模板2', '商品基本符合描述，但做工一般，希望卖家能够改进。', 1, 3, 3, 60, 1),
('差评模板1', '收到的商品与描述不符，质量较差，不太满意。', 1, 1, 2, 50, 1),
('差评模板2', '物流太慢了，商品质量也一般，不是很满意。', 1, 1, 2, 40, 1),
('商家好评回复', '亲爱的顾客，感谢您对我们产品的认可与支持！您的满意是我们最大的动力。如有任何关于产品使用的问题，欢迎随时联系我们的售后客服。我们将持续提供优质的母婴产品和服务，期待您的再次光临！', 1, 4, 5, 200, 1),
('商家中评回复', '感谢您的购买和反馈！我们非常重视您的体验，对于没能完全满足您的期望深表歉意。我们会认真考虑您的建议，不断改进产品和服务质量。如有任何具体问题需要解决，欢迎联系客服，我们会为您提供专业的售后支持。', 1, 3, 3, 180, 1),
('商家差评回复', '非常抱歉没能为您带来满意的购物体验！您的反馈对我们至关重要，我们已记录您反映的问题，将立即进行改进。为表歉意，请联系我们的客服处理售后事宜，我们会尽最大努力解决您遇到的问题，并提供相应的补偿方案。感谢您的理解和支持！', 1, 1, 2, 160, 1);

-- 插入评价奖励配置数据
INSERT INTO `comment_reward_config` (`reward_type`, `reward_name`, `reward_description`, `reward_level`, `reward_value`, `min_content_length`, `min_rating`, `require_image`, `min_images`, `is_first_comment`, `status`) VALUES
('points', '基础评价奖励', '完成订单评价获得基础积分', 1, 5, 0, NULL, 0, 0, 0, 1),
('points', '优质评价奖励', '评价内容详细，字数超过50字', 2, 10, 50, NULL, 0, 0, 0, 1),
('points', '图文评价奖励', '评价包含图片，更加直观', 2, 15, 0, NULL, 1, 1, 0, 1),
('points', '高质量图文评价', '评价内容详细且包含图片', 3, 20, 50, NULL, 1, 1, 0, 1),
('points', '多图评价奖励', '评价包含3张及以上图片', 3, 25, 0, NULL, 1, 3, 0, 1),
('points', '首次评价奖励', '首次评价商品获得额外奖励', 2, 10, 0, NULL, 0, 0, 1, 1),
('points', '好评奖励', '给出4-5星好评', 1, 8, 0, 4, 0, 0, 0, 1);

-- 创建评论管理存储过程：自动标记评论回复状态
DELIMITER ;;
CREATE PROCEDURE `proc_mark_comment_replied`(
  IN p_comment_id INT,
  OUT p_result INT
)
BEGIN
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_result = 0; -- 失败
  END;
  
  START TRANSACTION;
  
  -- 更新评论回复状态
  UPDATE `comment` SET has_replied = 1 WHERE comment_id = p_comment_id;
  
  SET p_result = 1; -- 成功
  COMMIT;
END;;
DELIMITER ;

-- 创建自动更新标签使用次数的触发器
DELIMITER ;;
CREATE TRIGGER `tr_update_tag_usage_count_insert`
AFTER INSERT ON `comment_tag_relation`
FOR EACH ROW
BEGIN
  UPDATE `comment_tag` SET usage_count = usage_count + 1 WHERE tag_id = NEW.tag_id;
END;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `tr_update_tag_usage_count_delete`
AFTER DELETE ON `comment_tag_relation`
FOR EACH ROW
BEGIN
  UPDATE `comment_tag` SET usage_count = usage_count - 1 WHERE tag_id = OLD.tag_id AND usage_count > 0;
END;;
DELIMITER ;

-- 创建评论统计视图
CREATE VIEW `v_comment_statistics` AS
SELECT 
  DATE(create_time) AS comment_date,
  COUNT(*) AS total_comments,
  COUNT(CASE WHEN rating = 5 THEN 1 END) AS five_star_count,
  COUNT(CASE WHEN rating = 4 THEN 1 END) AS four_star_count,
  COUNT(CASE WHEN rating = 3 THEN 1 END) AS three_star_count,
  COUNT(CASE WHEN rating = 2 THEN 1 END) AS two_star_count,
  COUNT(CASE WHEN rating = 1 THEN 1 END) AS one_star_count,
  AVG(rating) AS avg_rating,
  COUNT(CASE WHEN images IS NOT NULL THEN 1 END) AS with_images_count,
  COUNT(CASE WHEN has_replied = 1 THEN 1 END) AS replied_count,
  ROUND(COUNT(CASE WHEN has_replied = 1 THEN 1 END) * 100.0 / COUNT(*), 2) AS reply_rate
FROM `comment`
WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
  AND status = 1
GROUP BY DATE(create_time)
ORDER BY comment_date DESC;

-- 创建商品评价汇总视图
CREATE VIEW `v_product_comment_summary` AS
SELECT 
  product_id,
  COUNT(*) AS total_comments,
  AVG(rating) AS avg_rating,
  COUNT(CASE WHEN rating = 5 THEN 1 END) AS five_star_count,
  COUNT(CASE WHEN rating = 4 THEN 1 END) AS four_star_count,
  COUNT(CASE WHEN rating = 3 THEN 1 END) AS three_star_count,
  COUNT(CASE WHEN rating = 2 THEN 1 END) AS two_star_count,
  COUNT(CASE WHEN rating = 1 THEN 1 END) AS one_star_count,
  COUNT(CASE WHEN images IS NOT NULL THEN 1 END) AS with_images_count,
  ROUND(COUNT(CASE WHEN rating >= 4 THEN 1 END) * 100.0 / COUNT(*), 2) AS good_rating_rate,
  MAX(create_time) AS latest_comment_time
FROM `comment`
WHERE status = 1
GROUP BY product_id;

-- 创建索引优化查询性能
CREATE INDEX `idx_comment_user_product` ON `comment` (`user_id`, `product_id`);
CREATE INDEX `idx_comment_product_rating` ON `comment` (`product_id`, `rating`);
CREATE INDEX `idx_comment_status_time` ON `comment` (`status`, `create_time`);
CREATE INDEX `idx_comment_has_replied` ON `comment` (`has_replied`, `create_time`);
CREATE INDEX `idx_comment_template_type_status` ON `comment_template` (`template_type`, `status`);

SET FOREIGN_KEY_CHECKS = 1;