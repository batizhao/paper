CREATE DATABASE IF NOT EXISTS `paper`;

USE `paper`;

CREATE TABLE `ds` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `url` varchar(255) NOT NULL COMMENT 'url',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` varchar(32) NOT NULL DEFAULT 'open' COMMENT '状态',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `url` (`url`)
) COMMENT='数据源';

-- root/password
INSERT INTO `ds` (`id`, `name`, `url`, `username`, `password`, `status`)
VALUES
	(1,'system','jdbc:mysql://localhost:3306/pecado-system?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai','root','iKNB/cBzVsexgv3M90wY5D+I/nkf91sYKEbFs8nnhMq//jhEXwwbpHZ31yh3P4L/','open'),
	(2,'ims','jdbc:mysql://localhost:3306/pecado-ims?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai','root','iKNB/cBzVsexgv3M90wY5D+I/nkf91sYKEbFs8nnhMq//jhEXwwbpHZ31yh3P4L/','open');

-- root/root
-- INSERT INTO `ds` (`id`, `name`, `url`, `username`, `password`, `status`)
-- VALUES
-- (1, 'test', 'jdbc:mysql://172.31.21.180:30306/pecado-test?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai', 'root', 'wjGJBWDnD41oIGGltPetOgHWTx39u1N8qfmGSdFovRsfRCPD3xQXh7DY1yCjVYzG', 'open');
