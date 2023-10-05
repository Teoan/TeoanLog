package io.github.teoan.log.auto.configure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 自动装配类
 *
 * @author Teoan
 * @since 2023/04/18 10:18
 */
@Configuration
@ComponentScan("io.github.teoan.log")
@EnableElasticsearchRepositories(basePackages = "io.github.teoan.log")
public class TeoanLogConfiguration {
}
