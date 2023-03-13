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

 Date: 13/03/2023 15:05:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '性别',
  `id_number` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '身份证号',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态 0:禁用，1:正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  UNIQUE INDEX `id_number`(`id_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '员工信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '管理员', '17791900557', '男', '110101199001010047', 1);
INSERT INTO `employee` VALUES (1628604306791866370, '李小花', '13259481101', '0', '610113200009211611', 1);
INSERT INTO `employee` VALUES (1628685321669443586, '杨胡与', '13259481111', '0', '610126200103211425', 1);
INSERT INTO `employee` VALUES (1628688082813329410, 'l\'l\'l', '17791900556', '1', '610113200003211425', 1);
INSERT INTO `employee` VALUES (1628688366667046914, 'yyy', '17791900555', '0', '610123200000921161', 1);
INSERT INTO `employee` VALUES (1628691302046621697, 'yhy', '17791900551', '1', '610113200009231161', 1);
INSERT INTO `employee` VALUES (1628691894554865666, 'hyy', '13897590017', '0', '610113200303211611', 1);
INSERT INTO `employee` VALUES (1628717865509150722, '以后也会有、', '17791900098', '1', '610113200009281611', 1);
INSERT INTO `employee` VALUES (1628718782191099906, '意义1', '17791900898', '1', '610113200103211427', 1);
INSERT INTO `employee` VALUES (1628719358282899458, '作者', '17791900456', '1', '610113200009211567', 1);
INSERT INTO `employee` VALUES (1628763046455398402, 'y\'h\'y', '17791900664', '1', '610113200000927161', 1);
INSERT INTO `employee` VALUES (1628966415811809281, '越远', '17791900999', '1', '610113200009271611', 1);
INSERT INTO `employee` VALUES (1629106794758455298, '1', '18866655554', '1', '610113200009271612', 1);
INSERT INTO `employee` VALUES (1629107151890857985, '1', '18866655551', '1', '610113200009271615', 1);
INSERT INTO `employee` VALUES (1629107824745299969, '1', '18866655558', '1', '610113200009271619', 1);
INSERT INTO `employee` VALUES (1629108687983452161, 'hhh', '18888811112', '1', '629112222249443221', 1);
INSERT INTO `employee` VALUES (1629112505890529282, 'gggg', '18888777772', '1', '623333111100009992', 1);
INSERT INTO `employee` VALUES (1629112933596291074, 'tgt', '17791900447', '1', '610113200009271621', 1);
INSERT INTO `employee` VALUES (1632327792353878017, 'zwb', '15009219738', '0', '610113200108021611', 1);

SET FOREIGN_KEY_CHECKS = 1;
