package io.github.teoan.log.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teoan
 * @since 2023/10/17 23:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdinaryLog implements Serializable {

    /**
     * 日志来源 操作模块
     */

    String operSource;

    /**
     * 日志级别
     */
    String level;

    /**
     * 日志内容
     */
    String message;


    /**
     * 线程名称
     */
    String threadName;


    /**
     * 日志名称
     */
    String loggerName;

    /**
     * 执行时间
     */
    LocalDateTime dateTime;

}
