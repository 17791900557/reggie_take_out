/*
 Navicat Premium Data Transfer

 Source Server         : yhy
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : regiee

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 13/03/2023 15:05:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户名',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `status` int NOT NULL COMMENT '1代表正常0代表挂失',
  `balance` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1111111112', '导读的', 1, 33.00);
INSERT INTO `account` VALUES ('1211111111', '分分分', 1, 0.00);
INSERT INTO `account` VALUES ('1222222222', '烦烦烦', 1, 0.00);
INSERT INTO `account` VALUES ('1901190207', '杨胡燚', 0, 9999.00);
INSERT INTO `account` VALUES ('2111111111', '导读的', 1, 0.00);
INSERT INTO `account` VALUES ('2222222220', '丁顶', 1, 0.00);
INSERT INTO `account` VALUES ('2222222221', '丁顶', 1, 0.00);
INSERT INTO `account` VALUES ('2222222223', '踩踩踩从', 1, 500.00);

SET FOREIGN_KEY_CHECKS = 1;
