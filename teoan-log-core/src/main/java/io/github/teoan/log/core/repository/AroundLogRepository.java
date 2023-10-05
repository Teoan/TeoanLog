package io.github.teoan.log.core.repository;

import io.github.teoan.log.core.domain.AroundLogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Teoan
 * @since 2023/9/19 21:54
 */
@Repository
public interface AroundLogRepository extends ElasticsearchRepository<AroundLogDO, String> {
}
