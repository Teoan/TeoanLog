package io.github.teoan.log.auto.configure;

import io.github.teoan.log.core.handle.MongoLogHandle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
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
@ConditionalOnProperty(value = "teoan.log.enabled.mongodb", matchIfMissing = true)
public class TeoanLogMongoDBConfiguration {
    @Bean
    MongoLogHandle getMongoLogHandle() {
        return new MongoLogHandle();
    }
}
