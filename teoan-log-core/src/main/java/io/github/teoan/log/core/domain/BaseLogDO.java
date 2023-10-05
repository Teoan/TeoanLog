package io.github.teoan.log.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author Teoan
 * @since 2023/9/20 21:35
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseLogDO implements Serializable {

    /**
     * 日志来源 操作模块
     */
    @Field(type = FieldType.Keyword)
    String operSource;

    /**
     * 日志级别
     */
    @Field(type = FieldType.Keyword)
    String severity;

    /**
     * 日志操作名称
     */
    @Field(type = FieldType.Keyword)
    String operName;

    /**
     * 日志操作描述
     */
    @Field(type = FieldType.Text)
    String description;


    /**
     * 请求路径
     */
    @Field(type = FieldType.Keyword)
    String url;


    /**
     * 请求方式
     */
    @Field(type = FieldType.Text)
    String httpMethod;

    /**
     * 方法类名
     */
    @Field(type = FieldType.Keyword)
    String className;

    /**
     * 方法名
     */
    @Field(type = FieldType.Keyword)
    String method;

    /**
     * 请求IP
     */
    @Field(type = FieldType.Keyword)
    String ip;


    /**
     * 方法入参
     */
    @Field(type = FieldType.Object)
    Object[] args;


}
