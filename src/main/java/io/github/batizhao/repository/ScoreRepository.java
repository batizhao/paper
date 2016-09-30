package io.github.batizhao.repository;

import io.github.batizhao.domain.Score;
import io.github.batizhao.dto.ScoreDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface ScoreRepository extends CrudRepository<Score, Long> {

    Iterable<Score> findByAccountId(Long accountId);

    @Query(value = "select new io.github.batizhao.dto.ScoreDto(accountName, sum(score)) from Score group by accountId order by sum(score) desc")
    List<ScoreDto> sumRanking();

}
