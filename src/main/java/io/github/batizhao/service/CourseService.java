package io.github.batizhao.service;

import io.github.batizhao.domain.Account;
import io.github.batizhao.domain.Course;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface CourseService {

	Iterable<Course> findAll();

	Course findOne(Long courseId);
}
