package io.github.batizhao.repository;

import io.github.batizhao.domain.Score;
import org.springframework.data.repository.CrudRepository;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface ScoreRepository extends CrudRepository<Score, Long> {

    Iterable<Score> findByAccountId(Long accountId);

}
