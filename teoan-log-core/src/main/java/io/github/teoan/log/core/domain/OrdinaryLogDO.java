package io.github.teoan.log.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teoan
 * @since 2023/10/17 23:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(indexName = "ordinary_log")
@org.springframework.data.mongodb.core.mapping.Document(value = "ordinary_log")
public class OrdinaryLogDO extends BaseDO implements Serializable {


    /**
     * 日志来源 操作模块
     */
    @Field(type = FieldType.Keyword)
    String operSource;

    /**
     * 日志级别
     */
    @Field(type = FieldType.Keyword)
    String level;

    /**
     * 日志内容
     */
    @Field(type = FieldType.Text)
    String message;


    /**
     * 线程名称
     */
    @Field(type = FieldType.Text)
    String threadName;


    /**
     * 日志名称
     */
    @Field(type = FieldType.Text)
    String loggerName;

    /**
     * 执行时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    LocalDateTime dateTime;

}
