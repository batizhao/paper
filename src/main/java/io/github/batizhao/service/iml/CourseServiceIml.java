package io.github.batizhao.service.iml;

import io.github.batizhao.domain.Course;
import io.github.batizhao.repository.CourseRepository;
import io.github.batizhao.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author batizhao
 * @since 2016/10/1
 */
@Service
public class CourseServiceIml implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Override
    public Iterable<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findOne(Long courseId) {
        return courseRepository.findOne(courseId);
    }
}
