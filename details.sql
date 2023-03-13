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

 Date: 13/03/2023 15:05:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for details
-- ----------------------------
DROP TABLE IF EXISTS `details`;
CREATE TABLE `details`  (
  `id` bigint NOT NULL,
  `balance` decimal(6, 2) NOT NULL,
  `update_time` datetime NOT NULL,
  `update_user` bigint NOT NULL,
  `account_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of details
-- ----------------------------
INSERT INTO `details` VALUES (1632773368983908353, 11.00, '2023-03-07 00:01:21', 1, 1111111112);
INSERT INTO `details` VALUES (1632773818760097793, 11.00, '2023-03-07 00:03:09', 1, 1111111112);
INSERT INTO `details` VALUES (1632776085127147521, -1.50, '2023-03-07 00:12:09', 1628604306791866370, 1111111112);
INSERT INTO `details` VALUES (1632777902963564546, 1.50, '2023-03-07 00:19:22', 1, 1111111112);
INSERT INTO `details` VALUES (1632778659318214658, 1.00, '2023-03-07 00:22:23', 1, 1111111112);
INSERT INTO `details` VALUES (1632931636490317826, 500.00, '2023-03-07 10:30:15', 1, 2222222223);
INSERT INTO `details` VALUES (1633406363432386561, -1.00, '2023-03-08 17:56:39', 1, 1111111112);
INSERT INTO `details` VALUES (1633406710301327361, -50.00, '2023-03-08 17:58:02', 1, 1222222222);
INSERT INTO `details` VALUES (1633408014729871361, -1.00, '2023-03-08 18:03:13', 1, 1111111112);
INSERT INTO `details` VALUES (1633409078288031745, 100.00, '2023-03-08 18:07:26', 1, 1222222222);
INSERT INTO `details` VALUES (1633409154586615810, -50.00, '2023-03-08 18:07:44', 1, 1222222222);
INSERT INTO `details` VALUES (1633409712512847874, -1.00, '2023-03-08 18:09:57', 1, 1111111112);
INSERT INTO `details` VALUES (1633410308259270658, -1.00, '2023-03-08 18:12:19', 1, 1111111112);

SET FOREIGN_KEY_CHECKS = 1;
