package io.github.teoan.log.core.config;

import lombok.Data;

/**
 * @author Teoan
 * @since 2023/11/11 17:15
 */
@Data
public class TaskProperties {

    /**
     * 线程池核心数
     */
    Integer core = 5;

    /**
     * 线程池最大数
     */
    Integer max = 20;

    /**
     * 线程池队列大小
     */
    Integer queue = 1000;


}
