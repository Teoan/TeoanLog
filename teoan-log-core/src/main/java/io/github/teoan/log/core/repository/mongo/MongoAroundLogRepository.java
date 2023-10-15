package io.github.teoan.log.core.repository.mongo;

import io.github.teoan.log.core.domain.AroundLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Teoan
 * @since 2023/9/19 21:54
 */
@Repository
public interface MongoAroundLogRepository extends MongoRepository<AroundLogDO, String> {
}
