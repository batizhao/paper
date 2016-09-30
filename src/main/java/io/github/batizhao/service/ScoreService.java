package io.github.batizhao.service;

import io.github.batizhao.domain.Score;
import io.github.batizhao.dto.ScoreDto;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface ScoreService {

	Iterable<Score> findByAccountId();

	List<ScoreDto> sumRanking();

	Score save(Score Score);

	Score findOne(Long id);

	Score update(Score Score);

	void delete(Long id);
}
