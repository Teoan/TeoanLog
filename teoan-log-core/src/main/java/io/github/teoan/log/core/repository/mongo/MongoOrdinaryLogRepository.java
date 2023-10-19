package io.github.teoan.log.core.repository.mongo;

import io.github.teoan.log.core.domain.OrdinaryLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Teoan
 * @since 2023/9/19 21:54
 */
@Repository
public interface MongoOrdinaryLogRepository extends MongoRepository<OrdinaryLogDO, String> {
}
