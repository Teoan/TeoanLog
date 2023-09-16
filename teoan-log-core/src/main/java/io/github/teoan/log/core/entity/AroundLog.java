package io.github.teoan.log.core.entity;

import io.github.teoan.log.core.enums.LogSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日志对象
 * @author Teoan
 * @since 2023/9/16 09:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AroundLog {



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
     * 执行时间
     */
    Long execTime;


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

    /**
     * 方法执行结果
     */
    Object result;

}
