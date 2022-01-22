SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
  `user_id` bigint NOT NULL COMMENT '用户Id',
  `state` tinyint NOT NULL COMMENT '状态标示：-1指无效，0指成功，1指已付款',
  `money` bigint NOT NULL COMMENT '金额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment
-- ----------------------------

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill`  (
  `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `number` int NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开启时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `version` int NOT NULL COMMENT '版本号',
  PRIMARY KEY (`seckill_id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE,
  INDEX `idx_end_time`(`end_time`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1003 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES (1000, '1000元秒杀iphone8', 1000, '2022-01-18 15:31:53', '2022-01-18 15:31:53', '2022-01-16 15:31:53', 0);
INSERT INTO `seckill` VALUES (1001, '500元秒杀华为', 1000, '2022-01-18 15:31:53', '2022-01-18 15:31:53', '2022-01-16 15:31:53', 0);
INSERT INTO `seckill` VALUES (1002, '300元秒杀小米4', 1000, '2022-01-18 15:31:53', '2022-01-18 15:31:53', '2022-01-16 15:31:53', 0);
INSERT INTO `seckill` VALUES (1003, '200元秒杀红米note', 1000, '2022-01-18 15:31:53', '2022-01-18 15:31:53', '2022-01-16 15:31:53', 0);

-- ----------------------------
-- Table structure for success_killed
-- ----------------------------
DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed`  (
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
  `user_id` bigint NOT NULL COMMENT '用户Id',
  `state` tinyint NOT NULL COMMENT '状态标示：-1指无效，0指成功，1指已付款',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`, `user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀成功明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of success_killed
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
