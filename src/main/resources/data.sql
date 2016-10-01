INSERT INTO `account` (`id`, `email`, `password`, `username`, `name`, `roles`, `time`)
VALUES
	(1, 'batizhao@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'admin', '系统管理员', 'administrator', '2016-09-29 10:00:00'),
	(2, 'zhangsan@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'zhangsan', '张老师', 'teacher', '2016-09-30 10:57:58'),
	(3, 'lisi@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'lisi', '李老师', 'teacher', '2016-09-30 10:58:42'),
	(4, 'wangwu@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'wangwu', '王五', 'student', '2016-09-30 10:59:22'),
	(5, 'zhaoliu@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'zhaoliu', '赵六', 'student', '2016-09-30 10:59:38'),
	(6, 'sunxingzhe@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'sunxingzhe', '孙行者', 'student', '2016-09-30 10:59:22'),
	(7, 'zhexingsun@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'zhexingsun', '者行孙', 'student', '2016-09-30 10:59:38'),
	(8, 'xingzhesun@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'xingzhesun', '行者孙', 'student', '2016-09-30 10:59:22'),
	(9, 'tangseng@qq.com', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'tangseng', '唐僧', 'student', '2016-09-30 10:59:38');

INSERT INTO `course` (`id`, `name`, `summary`, `time`)
VALUES
	(1, '计算机应用基础', '一门计算机入门课程，属于公共基础课，是为非计算机专业\n                    类学生提供计算机一般应用所必需的基础知识、能力和素质的课程。', '2016-09-30 10:57:58'),
	(2, '微机接口及应用', '主要包括现代微机系统的组成和发展以及新技术介绍', '2016-09-30 10:57:58'),
	(3, '数据结构', '数据结构是计算机存储、组织数据的方式。', '2016-09-30 11:57:58'),
	(4, '互联网及其应用', '《互联网及其应用》是吉林大学出版社2004年06月出版的图书。', '2016-09-30 11:57:58');

INSERT INTO `score` (`id`, `account_id`, `account_name`, `course_id`, `course_name`, `score`, `time`)
VALUES
	(1, 4, '王五', 1, '计算机应用基础', 85, '2016-09-30 10:57:58'),
	(2, 4, '王五', 2, '微机接口及应用', 88, '2016-09-30 10:58:58'),
	(3, 4, '王五', 3, '数据结构', 75, '2016-09-30 11:57:58'),
	(4, 4, '王五', 4, '互联网及其应用', 80, '2016-09-30 12:58:58'),

	(5, 5, '赵六', 1, '计算机应用基础', 81, '2016-09-30 10:57:58'),
	(6, 5, '赵六', 2, '微机接口及应用', 72, '2016-09-30 10:58:58'),
	(7, 5, '赵六', 3, '数据结构', 79, '2016-09-30 11:57:58'),
	(8, 5, '赵六', 4, '互联网及其应用', 92, '2016-09-30 12:58:58'),

	(9, 6, '孙行者', 1, '计算机应用基础', 75, '2016-09-30 10:57:58'),
	(10, 6, '孙行者', 2, '微机接口及应用', 76, '2016-09-30 10:58:58'),
	(11, 6, '孙行者', 3, '数据结构', 77, '2016-09-30 11:57:58'),
	(12, 6, '孙行者', 4, '互联网及其应用', 78, '2016-09-30 12:58:58'),

	(13, 7, '者行孙', 1, '计算机应用基础', 83, '2016-09-30 10:57:58'),
	(14, 7, '者行孙', 2, '微机接口及应用', 84, '2016-09-30 10:58:58'),
	(15, 7, '者行孙', 3, '数据结构', 85, '2016-09-30 11:57:58'),
	(16, 7, '者行孙', 4, '互联网及其应用', 86, '2016-09-30 12:58:58'),

	(17, 8, '行者孙', 1, '计算机应用基础', 64, '2016-09-30 10:57:58'),
	(18, 8, '行者孙', 2, '微机接口及应用', 65, '2016-09-30 10:58:58'),
	(19, 8, '行者孙', 3, '数据结构', 66, '2016-09-30 11:57:58'),
	(20, 8, '行者孙', 4, '互联网及其应用', 67, '2016-09-30 12:58:58'),

	(21, 9, '唐僧', 1, '计算机应用基础', 93, '2016-09-30 12:58:58'),
	(22, 9, '唐僧', 2, '微机接口及应用', 94, '2016-09-30 10:57:58'),
	(23, 9, '唐僧', 3, '数据结构', 95, '2016-09-30 10:58:58'),
	(24, 9, '唐僧', 4, '互联网及其应用', 96, '2016-09-30 11:57:58');
