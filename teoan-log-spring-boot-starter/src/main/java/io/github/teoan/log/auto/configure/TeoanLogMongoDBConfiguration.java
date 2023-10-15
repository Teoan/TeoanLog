package io.github.teoan.log.auto.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Teoan
 * @since 2023/10/5 17:32
 */
@EnableMongoRepositories(basePackages = "io.github.teoan.log.core.repository.mongo")
@Configuration
@ConditionalOnClass(MongoTemplate.class)
public class TeoanLogMongoDBConfiguration {
}
