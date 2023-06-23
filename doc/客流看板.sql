/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云-mysql
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : bj-cdb-6r45n79k.sql.tencentcdb.com:60062
 Source Schema         : store_flow

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 19/08/2022 22:44:03
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '租户ID',
    `store_id`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '店铺ID',
    `store_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '店铺名称',
    `valid`       int(1) NOT NULL COMMENT '是否有效 0：有效 1：失效',
    `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_store_tenant` (`tenant_id`, `store_id`) USING BTREE COMMENT '店铺_租户'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '店铺表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_flow
-- ----------------------------
DROP TABLE IF EXISTS `store_flow`;
CREATE TABLE `store_flow`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '租户ID',
    `store_id`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '店铺ID',
    `flow_count`  bigint(10) NOT NULL COMMENT '客流量',
    `valid`       int(1) NOT NULL COMMENT '是否有效 0：有效 1：失效',
    `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_store_tenant` (`tenant_id`, `store_id`) USING BTREE COMMENT '店铺_租户'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '店铺客流表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_flow_age
-- ----------------------------
DROP TABLE IF EXISTS `store_flow_age`;
CREATE TABLE `store_flow_age`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '租户ID',
    `store_id`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '店铺ID',
    `age1`        bigint(10) NULL DEFAULT NULL COMMENT '0-10',
    `age2`        bigint(10) NULL DEFAULT NULL COMMENT '11-20',
    `age3`        bigint(10) NULL DEFAULT NULL COMMENT '21-30',
    `age4`        bigint(10) NULL DEFAULT NULL COMMENT '31-40',
    `age5`        bigint(10) NULL DEFAULT NULL COMMENT '41-50',
    `age6`        bigint(10) NULL DEFAULT NULL COMMENT '51-60',
    `age7`        bigint(10) NULL DEFAULT NULL COMMENT '61以上',
    `valid`       int(1) NOT NULL COMMENT '是否有效 0：有效 1：失效',
    `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_store_tenant` (`tenant_id`, `store_id`) USING BTREE COMMENT '店铺_租户'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '店铺客流表-年龄表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_flow_sex
-- ----------------------------
DROP TABLE IF EXISTS `store_flow_sex`;
CREATE TABLE `store_flow_sex`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '租户ID',
    `store_id`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '店铺ID',
    `male_count`    bigint(10) NOT NULL COMMENT '男性',
    `female_count`  bigint(10) NOT NULL COMMENT '女性',
    `unknown_count` bigint(10) NOT NULL COMMENT '未知',
    `valid`         int(1) NOT NULL COMMENT '是否有效 0：有效 1：失效',
    `create_time`   datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    `update_time`   datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX           `idx_store_tenant` (`tenant_id`, `store_id`) USING BTREE COMMENT '店铺_租户'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '店铺客流表-性别'
  ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
