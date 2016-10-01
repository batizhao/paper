package io.github.batizhao.repository;

import io.github.batizhao.domain.Course;
import org.springframework.data.repository.CrudRepository;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface CourseRepository extends CrudRepository<Course, Long> {

}
