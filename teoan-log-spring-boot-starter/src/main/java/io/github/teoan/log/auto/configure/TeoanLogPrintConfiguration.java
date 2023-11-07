package io.github.teoan.log.auto.configure;

import io.github.teoan.log.core.handle.PrintLogHandle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Teoan
 * @since 2023/11/7 23:13
 */
@Configuration
@ConditionalOnProperty(value = "teoan.log.print.enabled", matchIfMissing = true)
public class TeoanLogPrintConfiguration {
    @Bean
    PrintLogHandle getPrintLogHandle() {
        return new PrintLogHandle();
    }
}
