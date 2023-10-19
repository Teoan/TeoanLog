package io.github.teoan.log.core.repository.es;

import io.github.teoan.log.core.domain.OrdinaryLogDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Teoan
 * @since 2023/9/19 21:54
 */
@Repository
public interface EsOrdinaryLogRepository extends ElasticsearchRepository<OrdinaryLogDO, String> {
}
