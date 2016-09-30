package io.github.batizhao.service;

import io.github.batizhao.domain.Score;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface ScoreService {

	Iterable<Score> findByAccountId();

	Iterable<Score> findAll();

	Score save(Score Score);

	Score findOne(Long id);

	Score update(Score Score);

	void delete(Long id);
}
