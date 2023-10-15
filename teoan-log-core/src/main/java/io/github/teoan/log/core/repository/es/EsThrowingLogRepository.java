package io.github.teoan.log.core.repository.es;

import io.github.teoan.log.core.domain.ThrowingLogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Teoan
 * @since 2023/9/19 21:55
 */
@Repository
public interface EsThrowingLogRepository extends ElasticsearchRepository<ThrowingLogDO, String> {
}
