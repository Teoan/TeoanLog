package io.github.teoan.log.core.entity;

import io.github.teoan.log.core.enums.LogSeverity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Teoan
 * @since 2023/9/17 11:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseLog {

    /**
     * 日志来源 操作模块
     */
    String operSource;

    /**
     * 日志级别
     */
    LogSeverity severity;

    /**
     * 日志操作名称
     */
    String operName;

    /**
     * 日志操作描述
     */
    String description;


    /**
     * 请求路径
     */
    String url;


    /**
     * 请求方式
     */
    String httpMethod;

    /**
     * 方法类名
     */
    String className;

    /**
     * 方法名
     */
    String method;

    /**
     * 请求IP
     */
    String ip;


    /**
     * 方法入参
     */
    Object[] args;


}
