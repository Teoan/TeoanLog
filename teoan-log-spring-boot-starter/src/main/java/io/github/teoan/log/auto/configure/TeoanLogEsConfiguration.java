package io.github.teoan.log.auto.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
public class TeoanLogEsConfiguration {
}
