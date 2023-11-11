package io.github.teoan.log.auto.configure;

import io.github.teoan.log.core.config.TaskProperties;
import io.github.teoan.log.core.config.TeoanLogAutoConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * 自动装配类
 *
 * @author Teoan
 * @since 2023/04/18 10:18
 */
@Configuration
@ComponentScan("io.github.teoan.log")
public class TeoanLogConfiguration {

    @Resource
    private TeoanLogAutoConfigProperties teoanLogAutoConfigProperties;

    /**
     * 自定义日志处理线程池
     */
    @Bean("teoanLogTaskExecutor")
    ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        TaskProperties task = teoanLogAutoConfigProperties.getTask();
        threadPoolTaskExecutor.setCorePoolSize(task.getCore());
        threadPoolTaskExecutor.setMaxPoolSize(task.getMax());
        threadPoolTaskExecutor.setQueueCapacity(task.getQueue());
        threadPoolTaskExecutor.setThreadNamePrefix("teoan-log-handler-thread-");
        return threadPoolTaskExecutor;
    }


}
