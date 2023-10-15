package io.github.teoan.log.core.repository.mongo;

import io.github.teoan.log.core.domain.ThrowingLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Teoan
 * @since 2023/9/19 21:55
 */
@Repository
public interface MongoThrowingLogRepository extends MongoRepository<ThrowingLogDO, String> {
}
