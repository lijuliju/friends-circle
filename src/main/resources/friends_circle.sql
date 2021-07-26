/*
Navicat MySQL Data Transfer

Source Server         : MySQL3306
Source Server Version : 80015
Source Host           : localhost:3306
Source Database       : wj

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2020-04-12 09:45:45
*/

-- ----------------------------
-- Table structure for fc_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `fc_dynamic`;
CREATE TABLE `fc_dynamic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '动态的文字内容',
  `area_code` bigint NOT NULL COMMENT '存储区号，便于区分同城',
  `type` bigint NOT NULL COMMENT '动态类型：1 纯文字；2 文字+ 图片；3纯图片；4 文字+视频；5 纯视频',
  `picture_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '图片信息',
  `video_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '视频信息',
  `addtime` bigint NOT NULL COMMENT '创建时间',
  `like_count` bigint NOT NULL COMMENT '点赞数量(默认0)',
  `comment_count` bigint NOT NULL COMMENT '评论数量(默认0)',
  `enable` bigint DEFAULT NULL COMMENT '是否可用',
  `del_time` bigint DEFAULT NULL COMMENT '删除时间',
  `week_like_count` bigint DEFAULT NULL COMMENT '一周内点赞数量',
  `detail_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址详情',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- ----------------------------
-- Table structure for fc_dynamic_comment
-- ----------------------------
DROP TABLE IF EXISTS `fc_dynamic_comment`;
CREATE TABLE `fc_dynamic_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `dyna_id` bigint NOT NULL COMMENT '动态ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `addtime` bigint DEFAULT NULL COMMENT '添加时间',
  `del_flag` bigint DEFAULT NULL COMMENT '删除标识',
  `del_time` bigint DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for fc_dynamic_like
-- ----------------------------
DROP TABLE IF EXISTS `fc_dynamic_like`;
CREATE TABLE `fc_dynamic_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `dyna_id` bigint DEFAULT NULL COMMENT '动态ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `addtime` bigint DEFAULT NULL,
  `del_flag` bigint DEFAULT '0',
  `del_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- ----------------------------

-- ----------------------------
-- Table structure for fc_reply_comment
-- ----------------------------
DROP TABLE IF EXISTS `fc_reply_comment`;
CREATE TABLE `fc_reply_comment` (
  `id` bigint NOT NULL COMMENT '唯一ID',
  `dyna_id` bigint NOT NULL COMMENT '相关动态ID',
  `user_id` bigint NOT NULL COMMENT '评论用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回复评论内容',
  `addtime` bigint NOT NULL COMMENT '添加时间',
  `com_id` bigint NOT NULL COMMENT '被回复评论ID',
  `del_flag` bigint DEFAULT NULL,
  `del_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `fc_user_info` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `addtime` bigint NOT NULL COMMENT '添加时间',
  `push_count` bigint NOT NULL COMMENT '发布总数',
  `praised_count` bigint NOT NULL COMMENT '获赞总数',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;