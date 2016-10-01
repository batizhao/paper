package io.github.batizhao.service.iml;

import io.github.batizhao.domain.Account;
import io.github.batizhao.domain.Course;
import io.github.batizhao.domain.Score;
import io.github.batizhao.dto.ScoreDto;
import io.github.batizhao.repository.ScoreRepository;
import io.github.batizhao.service.AccountService;
import io.github.batizhao.service.CourseService;
import io.github.batizhao.service.ScoreService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/30
 */
@Service
public class ScoreServiceIml implements ScoreService {

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    CourseService courseService;

    @Override
    public Iterable<Score> findByAccountId() {
        Account account = (Account) SecurityUtils.getSubject().getPrincipal();
        return scoreRepository.findByAccountId(account.getId());
    }

    @Override
    public List<ScoreDto> sumRanking() {
        return scoreRepository.sumRanking();
    }

    @Override
    public Score save(Score score) {
        Account account = accountService.findOne(score.getAccountId());
        score.setAccountName(account.getName());
        Course course = courseService.findOne(score.getCourseId());
        score.setCourseName(course.getName());
        return scoreRepository.save(score);
    }

    @Override
    public Score findOne(Long id) {
        return scoreRepository.findOne(id);
    }

    @Override
    public Score update(Score score) {
        return scoreRepository.save(score);
    }

    @Override
    public void delete(Long id) {
        scoreRepository.delete(id);
    }

    @Override
    public Iterable<Score> finAll() {
        return scoreRepository.findAll();
    }
}
