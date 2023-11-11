package io.github.teoan.log.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author Teoan
 * @since 2023/11/11 21:30
 */
@Configuration
@ConfigurationProperties(prefix = TeoanLogAutoConfigProperties.PREFIX)
@Data
public class TeoanLogAutoConfigProperties {

    public static final String PREFIX = "teoan.log";

    @NestedConfigurationProperty
    Integer batch = 5;

    @NestedConfigurationProperty
    EnabledProperties enabled;

    @NestedConfigurationProperty
    TaskProperties task;

}
