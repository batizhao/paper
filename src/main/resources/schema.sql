DROP TABLE if EXISTS account;

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
);