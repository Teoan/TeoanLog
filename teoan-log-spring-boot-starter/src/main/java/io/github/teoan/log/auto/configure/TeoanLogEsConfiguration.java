package io.github.teoan.log.auto.configure;

import io.github.teoan.log.core.handle.EsLogHandle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author Teoan
 * @since 2023/10/5 17:32
 */
@EnableElasticsearchRepositories(basePackages = "io.github.teoan.log.core.repository.es")
@Configuration
@ConditionalOnClass(ElasticsearchRestTemplate.class)
@ConditionalOnProperty(value = "teoan.log.enabled.elasticsearch", matchIfMissing = true)
public class TeoanLogEsConfiguration {
    @Bean
    EsLogHandle getEsLogHandle() {
        return new EsLogHandle();
    }
}
