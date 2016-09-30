package io.github.batizhao.service.iml;

import io.github.batizhao.domain.Account;
import io.github.batizhao.domain.Score;
import io.github.batizhao.repository.ScoreRepository;
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

    @Override
    public Iterable<Score> findByAccountId() {
        Account account = (Account) SecurityUtils.getSubject().getPrincipal();
        return scoreRepository.findByAccountId(account.getId());
    }

    @Override
    public Iterable<Score> findAll() {
        return scoreRepository.findAll();
    }

    @Override
    public Score save(Score Score) {
        return null;
    }

    @Override
    public Score findOne(Long id) {
        return null;
    }

    @Override
    public Score update(Score Score) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
