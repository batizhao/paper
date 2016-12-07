DROP TABLE if EXISTS account;
DROP TABLE if EXISTS score;
DROP TABLE if EXISTS course;

CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(128) NOT NULL COMMENT '用户邮箱',
  `password` varchar(64) NOT NULL COMMENT '用户密码',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `roles` varchar(255) NOT NULL COMMENT '用户角色',
  `time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) CHARSET=utf8;

CREATE TABLE `score` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分数ID',
  `account_id` int(11) NOT NULL COMMENT '用户ID',
  `account_name` varchar(255) NOT NULL COMMENT '用户姓名',
  `course_id` int(11) NOT NULL COMMENT '课程ID',
  `course_name` varchar(255) NOT NULL COMMENT '课程名称',
  `score` int(11) NOT NULL COMMENT '分数',
  `time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) CHARSET=utf8;

CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `name` varchar(255) NOT NULL COMMENT '课程名称',
  `summary` varchar(255) NOT NULL DEFAULT '' COMMENT '课程简介',
  `time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) CHARSET=utf8;